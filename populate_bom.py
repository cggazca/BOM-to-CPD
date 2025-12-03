"""
BOM to CPD Population Tool

Parses a BOM Excel file, searches PAS API for each part, infers component
category, extracts parametric data, and populates the TemplateLibrary.cpd
SQLite database.

Usage:
    python populate_bom.py [--bom BOM_FILE] [--cpd CPD_FILE] [--dbc DBC_FILE]
"""

import os
import sys
import sqlite3
import json
import time
import shutil
import logging
import argparse
from datetime import datetime, timedelta
from typing import Dict, List, Optional, Tuple, Any
import pandas as pd
import requests

from dbc_parser import DBCParser
from property_mapping import (
    PAS_PROPERTY_MANUFACTURER_NAME,
    PAS_PROPERTY_MANUFACTURER_PN,
    PAS_PROPERTY_DESCRIPTION,
    PAS_PROPERTY_DATASHEET_URL,
    PAS_PROPERTY_LIFECYCLE_STATUS,
    PAS_PROPERTY_PART_ID,
    PROPERTY_ID_TO_NAME,
    DBC_CATEGORY_FIELD_MAPPINGS,
    get_category_from_description,
    get_default_outputs,
    get_category_outputs,
    get_dbc_field_mapping,
)

# Set up logging
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(levelname)s - %(message)s'
)
logger = logging.getLogger(__name__)


class PASClient:
    """Simplified PAS API client for BOM population"""

    def __init__(self, client_id: str = None, client_secret: str = None, bearer_token: str = None):
        # Get credentials from environment variables or parameters
        self.config = {
            "PasUrl": "https://api.pas.partquest.com",
            "AuthServiceUrl": "https://samauth.us-east-1.sws.siemens.com/",
            "ClientId": client_id or os.environ.get("PAS_CLIENT_ID", ""),
            "ClientSecret": client_secret or os.environ.get("PAS_CLIENT_SECRET", ""),
            "SearchProviderId": "44",
            "SearchProviderVersion": "2",
        }

        # Support pre-obtained bearer token
        if bearer_token:
            self.access_token = bearer_token
            self.token_expires_at = datetime.now() + timedelta(hours=2)
        else:
            self.access_token = None
            self.token_expires_at = None

    def _get_access_token(self) -> str:
        """Get or refresh the access token"""
        if self.access_token and self.token_expires_at:
            if datetime.now() < self.token_expires_at:
                return self.access_token

        logger.info("Obtaining new access token...")

        token_url = f"{self.config['AuthServiceUrl']}token"
        auth = (self.config['ClientId'], self.config['ClientSecret'])

        auth_data = {
            'grant_type': 'client_credentials',
            'scope': 'sws.icarus.api.read'
        }

        headers = {
            'Content-Type': 'application/x-www-form-urlencoded'
        }

        response = requests.post(
            token_url,
            auth=auth,
            data=auth_data,
            headers=headers,
            timeout=10
        )
        response.raise_for_status()

        token_data = response.json()
        self.access_token = token_data['access_token']
        expires_in = token_data.get('expires_in', 7200)
        self.token_expires_at = datetime.now() + timedelta(seconds=expires_in - 60)

        logger.info("Access token obtained successfully")
        return self.access_token

    def _make_request(self, method: str, endpoint: str,
                      json_data: Optional[Dict] = None) -> Dict[str, Any]:
        """Make an authenticated request"""
        for attempt in range(3):
            try:
                token = self._get_access_token()

                headers = {
                    'Authorization': f'Bearer {token}',
                    'Content-Type': 'application/json',
                    'X-Siemens-Correlation-Id': f'corr-{int(time.time() * 1000)}',
                    'X-Siemens-Session-Id': f'session-{int(time.time())}',
                    'X-Siemens-Ebs-User-Country-Code': 'US',
                    'X-Siemens-Ebs-User-Currency': 'USD'
                }

                url = f"{self.config['PasUrl']}{endpoint}"

                response = requests.request(
                    method=method,
                    url=url,
                    headers=headers,
                    json=json_data,
                    timeout=60
                )

                if response.status_code == 401:
                    logger.warning("Token expired, refreshing...")
                    self.access_token = None
                    self.token_expires_at = None
                    continue

                response.raise_for_status()
                result = response.json()

                if not result.get('success', False):
                    error = result.get('error', {})
                    error_msg = error.get('message', 'Unknown error')
                    raise Exception(f"API Error: {error_msg}")

                return result

            except Exception as e:
                if attempt < 2:
                    logger.warning(f"Request failed (attempt {attempt + 1}/3): {e}")
                    time.sleep(1 * (attempt + 1))
                else:
                    raise

        raise Exception("Request failed after all retry attempts")

    def get_part_class_name(self, part_class_id: str) -> Optional[str]:
        """
        Look up part class name from PAS API

        Args:
            part_class_id: Hex ID of the part class (e.g., "5004c6a3")

        Returns:
            Part class name or None if not found
        """
        if not part_class_id:
            return None

        # Cache for part class names to avoid repeated API calls
        if not hasattr(self, '_part_class_cache'):
            self._part_class_cache = {}

        if part_class_id in self._part_class_cache:
            return self._part_class_cache[part_class_id]

        try:
            endpoint = f'/api/v2/search-providers/{self.config["SearchProviderId"]}/{self.config["SearchProviderVersion"]}/scheme/get-part-class-definitions'

            result = self._make_request('GET', endpoint + f'?PartClassIds={part_class_id}')

            if result.get('result') and result['result'].get('details'):
                definitions = result['result']['details']
                for defn in definitions:
                    if defn.get('id') == part_class_id:
                        name = defn.get('name', '')
                        self._part_class_cache[part_class_id] = name
                        logger.debug(f"Part class {part_class_id} -> {name}")
                        return name

        except Exception as e:
            logger.debug(f"Failed to get part class name for {part_class_id}: {e}")

        return None

    def _infer_category_from_mpn(self, mpn: str) -> Optional[str]:
        """
        Infer component category from MPN patterns

        Common MPN prefixes:
        - 1N, 1SS, BAV, BAT, BAS -> Diodes
        - 2N, 2SC, BC, BSS, BSZ, IRF, FD -> Transistors
        - RC, ERJ, CRCW, MCR -> Resistors
        - GRM, C0G, X7R, Y5V, EEE -> Capacitors
        - SN, MAX, LM, TL, AD, OP -> ICs
        """
        mpn_upper = mpn.upper()

        # Diode patterns
        if any(mpn_upper.startswith(p) for p in ['1N', '1SS', 'BAV', 'BAT', 'BAS', 'BZT', 'BZX', 'SMAJ', 'SMBJ']):
            return 'Diodes'

        # Transistor patterns
        if any(mpn_upper.startswith(p) for p in ['2N', '2SC', '2SA', 'BC', 'BSS', 'BSZ', 'IRF', 'FD', 'SI', 'AO', 'DMN', 'DMP']):
            return 'Transistors'

        # Resistor patterns
        if any(mpn_upper.startswith(p) for p in ['RC', 'ERJ', 'CRCW', 'MCR', 'RK', 'CR', 'CRE']):
            return 'Resistors'

        # Capacitor patterns
        if any(mpn_upper.startswith(p) for p in ['GRM', 'C0G', 'CL', 'EEE', 'EEF', 'UWT', 'VJ', 'CC']):
            return 'Capacitors'

        # Inductor patterns
        if any(mpn_upper.startswith(p) for p in ['LQH', 'LQM', 'BLM', 'MLZ', 'NR', 'SRN']):
            return 'Inductors'

        # Connector patterns (numeric part numbers often)
        if any(mpn_upper.startswith(p) for p in ['PBC', 'PPTC', 'TSW', 'FH', 'DF']):
            return 'Connectors'

        # Crystal/Oscillator patterns
        if any(mpn_upper.startswith(p) for p in ['ABM', 'ABS', 'ECS', 'FA-']):
            return 'Crystals Resonators'

        # LED patterns
        if any(mpn_upper.startswith(p) for p in ['LTST', 'SML', 'APT', 'CLV']):
            return 'Optoelectronics'

        return None

    def _infer_category_from_company_pn(self, company_pn: str) -> Optional[str]:
        """
        Infer category from company part number prefix

        Common company PN prefixes:
        - CAP -> Capacitors
        - RES/RSMT -> Resistors
        - DIODE -> Diodes
        - TRA -> Transistors
        - CON -> Connectors
        - IND -> Inductors
        """
        pn_upper = company_pn.upper()

        prefix_map = {
            'CAP': 'Capacitors',
            'RES': 'Resistors',
            'RSMT': 'Resistors',
            'DIODE': 'Diodes',
            'TRA': 'Transistors',
            'CON': 'Connectors',
            'IND': 'Inductors',
            'XTAL': 'Crystals Resonators',
            'OSC': 'Oscillators',
            'LED': 'Optoelectronics',
            'FUSE': 'Circuit Protection',
            'RELAY': 'Relays',
            'SW': 'Switches',
            'TRANS': 'Transformers',
            'IC': 'Logic',
            'MECH': 'Mechanical',
        }

        for prefix, category in prefix_map.items():
            if pn_upper.startswith(prefix):
                return category

        return None

    def search_part(self, manufacturer_pn: str, manufacturer: str) -> Dict[str, Any]:
        """
        Search for a part by MPN and manufacturer using parametric search

        Args:
            manufacturer_pn: Manufacturer part number to search
            manufacturer: Manufacturer name

        Returns dict with:
            - found: bool
            - parts: list of part data
            - best_match: dict with best matching part or None
            - category: inferred category name from PAS part class
        """
        # Use parametric search for accurate filtering by MPN and Manufacturer
        endpoint = f'/api/v2/search-providers/{self.config["SearchProviderId"]}/{self.config["SearchProviderVersion"]}/parametric/search'

        # Build filter based on whether manufacturer is provided
        if manufacturer and manufacturer.strip():
            # Two-parameter search: AND filter for both Part Number and Manufacturer
            search_filter = {
                "__logicalOperator__": "And",
                "__expression__": "LogicalExpression",
                "left": {
                    "__valueOperator__": "SmartMatch",
                    "__expression__": "ValueExpression",
                    "propertyId": PAS_PROPERTY_MANUFACTURER_NAME,
                    "term": manufacturer
                },
                "right": {
                    "__valueOperator__": "SmartMatch",
                    "__expression__": "ValueExpression",
                    "propertyId": PAS_PROPERTY_MANUFACTURER_PN,
                    "term": manufacturer_pn
                }
            }
            page_size = 10
        else:
            # One-parameter search: filter by Part Number only
            search_filter = {
                "__valueOperator__": "SmartMatch",
                "__expression__": "ValueExpression",
                "propertyId": PAS_PROPERTY_MANUFACTURER_PN,
                "term": manufacturer_pn
            }
            page_size = 50

        request_body = {
            "searchParameters": {
                "partClassId": "76f2225d",  # Root part class
                "customParameters": {},
                "outputs": get_default_outputs(),
                "sort": [],
                "paging": {
                    "requestedPageSize": page_size
                },
                "filter": search_filter
            }
        }

        try:
            result = self._make_request('POST', endpoint, json_data=request_body)

            if result.get('result') and result['result'].get('results'):
                parts = result['result']['results']
                total_count = result['result'].get('totalCount', len(parts))

                # Find best match
                best_match = None
                for part_data in parts:
                    part = part_data.get('searchProviderPart', {})
                    found_mpn = part.get('manufacturerPartNumber', '')
                    found_mfr = part.get('manufacturerName', '')

                    # Exact match check
                    if found_mpn.upper() == manufacturer_pn.upper():
                        if not manufacturer or manufacturer.upper() in found_mfr.upper():
                            best_match = part_data
                            break

                # If no exact match, take first result
                if not best_match and parts:
                    best_match = parts[0]

                # Extract category from best match
                category = None
                if best_match:
                    part = best_match.get('searchProviderPart', {})
                    description = part.get('description', '')
                    part_class = part.get('partClassName', '')
                    mpn = part.get('manufacturerPartNumber', '')

                    # Get partClassId from PAS response
                    part_class_id = part.get('partClassId', '')

                    # Try to get part class name from PAS API
                    part_class_name = self.get_part_class_name(part_class_id) if part_class_id else ''

                    # Try multiple methods to determine category
                    # 1. From PAS part class name
                    category = get_category_from_description(part_class_name, '') if part_class_name else None

                    # 2. From description if available
                    if not category and description:
                        category = get_category_from_description(description, '')

                    # 3. From MPN patterns as fallback
                    if not category:
                        category = self._infer_category_from_mpn(mpn)

                return {
                    'found': True,
                    'parts': parts,
                    'best_match': best_match,
                    'category': category,
                    'total_count': total_count
                }
            else:
                return {
                    'found': False,
                    'parts': [],
                    'best_match': None,
                    'category': None,
                    'total_count': 0
                }

        except Exception as e:
            logger.error(f"Search failed for {manufacturer} {manufacturer_pn}: {e}")
            return {
                'found': False,
                'parts': [],
                'best_match': None,
                'category': None,
                'error': str(e)
            }

    def search_part_with_outputs(self, manufacturer_pn: str, manufacturer: str,
                                  outputs: List[str]) -> Dict[str, Any]:
        """
        Search for a part with specific output properties

        Args:
            manufacturer_pn: Manufacturer part number
            manufacturer: Manufacturer name
            outputs: List of property IDs to include in response

        Returns:
            Same format as search_part
        """
        endpoint = f'/api/v2/search-providers/{self.config["SearchProviderId"]}/{self.config["SearchProviderVersion"]}/parametric/search'

        # Build filter
        if manufacturer and manufacturer.strip():
            search_filter = {
                "__logicalOperator__": "And",
                "__expression__": "LogicalExpression",
                "left": {
                    "__valueOperator__": "SmartMatch",
                    "__expression__": "ValueExpression",
                    "propertyId": PAS_PROPERTY_MANUFACTURER_NAME,
                    "term": manufacturer
                },
                "right": {
                    "__valueOperator__": "SmartMatch",
                    "__expression__": "ValueExpression",
                    "propertyId": PAS_PROPERTY_MANUFACTURER_PN,
                    "term": manufacturer_pn
                }
            }
        else:
            search_filter = {
                "__valueOperator__": "SmartMatch",
                "__expression__": "ValueExpression",
                "propertyId": PAS_PROPERTY_MANUFACTURER_PN,
                "term": manufacturer_pn
            }

        request_body = {
            "searchParameters": {
                "partClassId": "76f2225d",
                "customParameters": {},
                "outputs": outputs,  # Use category-specific outputs
                "sort": [],
                "paging": {"requestedPageSize": 10},
                "filter": search_filter
            }
        }

        try:
            result = self._make_request('POST', endpoint, json_data=request_body)

            if result.get('result') and result['result'].get('results'):
                parts = result['result']['results']

                # Find best match
                best_match = None
                for part_data in parts:
                    part = part_data.get('searchProviderPart', {})
                    found_mpn = part.get('manufacturerPartNumber', '')
                    found_mfr = part.get('manufacturerName', '')

                    if found_mpn.upper() == manufacturer_pn.upper():
                        if not manufacturer or manufacturer.upper() in found_mfr.upper():
                            best_match = part_data
                            break

                if not best_match and parts:
                    best_match = parts[0]

                return {
                    'found': True,
                    'parts': parts,
                    'best_match': best_match,
                    'total_count': result['result'].get('totalCount', len(parts))
                }
            else:
                return {'found': False, 'parts': [], 'best_match': None}

        except Exception as e:
            logger.debug(f"Enhanced search failed for {manufacturer_pn}: {e}")
            return {'found': False, 'parts': [], 'best_match': None, 'error': str(e)}


class BOMParser:
    """Parser for BOM Excel files"""

    def __init__(self, bom_file: str):
        self.bom_file = bom_file
        self.df = None

    def parse(self) -> List[Dict[str, Any]]:
        """
        Parse the BOM file and return list of parts

        Returns:
            List of dicts with keys: company_pn, manufacturer, mpn
        """
        logger.info(f"Parsing BOM file: {self.bom_file}")

        # Read Excel without headers first to find the actual header row
        df_raw = pd.read_excel(self.bom_file, header=None)

        # Find the header row (look for "Company PN" in first column)
        header_row = None
        for idx in range(min(10, len(df_raw))):
            first_cell = str(df_raw.iloc[idx, 0]).strip()
            if first_cell == "Company PN":
                header_row = idx
                break

        if header_row is None:
            raise ValueError("Could not find 'Company PN' header row in BOM file")

        logger.info(f"Found header row at index: {header_row}")

        # Re-read with correct header
        self.df = pd.read_excel(self.bom_file, header=header_row)

        # Get actual column names
        columns = list(self.df.columns)
        logger.info(f"Found columns: {columns}")

        parts = []

        for idx, row in self.df.iterrows():
            company_pn = str(row.iloc[0]).strip() if pd.notna(row.iloc[0]) else ''

            if not company_pn or company_pn == 'nan':
                continue

            # Find first valid MN/MPN pair (columns are: Company PN, MN 1, MPN 1, MN 2, MPN 2, ...)
            manufacturer = None
            mpn = None

            for i in range(1, len(columns) - 1, 2):
                mn_val = row.iloc[i] if i < len(row) else None
                mpn_val = row.iloc[i + 1] if i + 1 < len(row) else None

                if pd.notna(mn_val) and pd.notna(mpn_val):
                    manufacturer = str(mn_val).strip()
                    mpn = str(mpn_val).strip()
                    if manufacturer and mpn:
                        break

            if manufacturer and mpn:
                parts.append({
                    'company_pn': company_pn,
                    'manufacturer': manufacturer,
                    'mpn': mpn,
                    'row_index': idx
                })
            else:
                logger.warning(f"Row {idx}: No valid MN/MPN pair for {company_pn}")

        logger.info(f"Parsed {len(parts)} parts from BOM")
        return parts


class CPDUpdater:
    """Updates the TemplateLibrary.cpd SQLite database"""

    def __init__(self, cpd_file: str, dbc_parser: DBCParser):
        self.cpd_file = cpd_file
        self.dbc_parser = dbc_parser
        self.conn = None
        self._next_obj_id = None  # Cache for next obj_id

    def connect(self):
        """Connect to the SQLite database"""
        self.conn = sqlite3.connect(self.cpd_file)
        self.conn.row_factory = sqlite3.Row
        logger.info(f"Connected to CPD database: {self.cpd_file}")

    def close(self):
        """Close database connection"""
        if self.conn:
            self.conn.close()
            logger.info("Database connection closed")

    def backup(self):
        """Create a backup of the CPD file before modifications"""
        timestamp = datetime.now().strftime("%Y%m%d_%H%M%S")
        backup_file = f"{self.cpd_file}.backup_{timestamp}"
        shutil.copy2(self.cpd_file, backup_file)
        logger.info(f"Created backup: {backup_file}")
        return backup_file

    def get_tables(self) -> List[str]:
        """Get list of all tables in the database"""
        cursor = self.conn.execute(
            "SELECT name FROM sqlite_master WHERE type='table' ORDER BY name"
        )
        return [row[0] for row in cursor.fetchall()]

    def table_exists(self, table_name: str) -> bool:
        """Check if a table exists"""
        cursor = self.conn.execute(
            "SELECT name FROM sqlite_master WHERE type='table' AND name=?",
            (table_name,)
        )
        return cursor.fetchone() is not None

    def get_table_columns(self, table_name: str) -> List[str]:
        """Get column names for a table"""
        cursor = self.conn.execute(f'PRAGMA table_info("{table_name}")')
        return [row[1] for row in cursor.fetchall()]

    def find_part_by_mpn(self, table_name: str, mpn: str) -> Optional[Dict]:
        """Find a part in a table by Manufacturer Part Number"""
        if not self.table_exists(table_name):
            return None

        columns = self.get_table_columns(table_name)

        # Look for MPN column (various possible names)
        mpn_col = None
        for col in columns:
            if col.lower() in ['manufacturer part number', 'mpn', 'mfg part number']:
                mpn_col = col
                break

        if not mpn_col:
            # Try "Manufacturer Part Number" explicitly
            if 'Manufacturer Part Number' in columns:
                mpn_col = 'Manufacturer Part Number'

        if not mpn_col:
            return None

        cursor = self.conn.execute(
            f'SELECT * FROM "{table_name}" WHERE "{mpn_col}" = ?',
            (mpn,)
        )
        row = cursor.fetchone()
        if row:
            return dict(row)
        return None

    def get_next_obj_id(self) -> int:
        """Get the next available obj_id across all tables"""
        # If we already have a cached value, just increment and return
        if self._next_obj_id is not None:
            self._next_obj_id += 1
            return self._next_obj_id

        # First call - scan all tables for max obj_id
        max_id = 0

        for table in self.get_tables():
            columns = self.get_table_columns(table)
            if 'obj_id' in columns:
                cursor = self.conn.execute(f'SELECT MAX(obj_id) FROM "{table}"')
                row = cursor.fetchone()
                if row[0] is not None:
                    try:
                        val = int(row[0])
                        if val > max_id:
                            max_id = val
                    except (ValueError, TypeError):
                        pass

        self._next_obj_id = max_id + 1
        return self._next_obj_id

    def insert_or_update_part(self, table_name: str, part_data: Dict[str, Any]) -> bool:
        """
        Insert or update a part in the specified table

        Args:
            table_name: Name of the table (category)
            part_data: Dictionary of column -> value mappings

        Returns:
            True if successful, False otherwise
        """
        if not self.table_exists(table_name):
            logger.warning(f"Table does not exist: {table_name}")
            return False

        columns = self.get_table_columns(table_name)

        # Check if part already exists
        mpn = part_data.get('Manufacturer Part Number', '')
        existing = self.find_part_by_mpn(table_name, mpn) if mpn else None

        # Filter part_data to only include valid columns
        valid_data = {}
        for key, value in part_data.items():
            if key in columns:
                valid_data[key] = value

        if existing:
            # Update existing record
            obj_id = existing.get('obj_id')
            if obj_id:
                set_clause = ', '.join([f'"{k}" = ?' for k in valid_data.keys()])
                values = list(valid_data.values()) + [obj_id]

                self.conn.execute(
                    f'UPDATE "{table_name}" SET {set_clause} WHERE obj_id = ?',
                    values
                )
                self.conn.commit()
                logger.debug(f"Updated part in {table_name}: {mpn}")
                return True
        else:
            # Insert new record
            if 'obj_id' not in valid_data:
                valid_data['obj_id'] = self.get_next_obj_id()

            col_names = ', '.join([f'"{k}"' for k in valid_data.keys()])
            placeholders = ', '.join(['?' for _ in valid_data])
            values = list(valid_data.values())

            self.conn.execute(
                f'INSERT INTO "{table_name}" ({col_names}) VALUES ({placeholders})',
                values
            )
            self.conn.commit()
            logger.debug(f"Inserted part in {table_name}: {mpn}")
            return True

        return False


def extract_part_data(pas_result: Dict, company_pn: str, category: str = None) -> Dict[str, Any]:
    """
    Extract part data from PAS result into CPD column format

    Args:
        pas_result: Result from PAS search
        company_pn: Company part number from BOM
        category: DBC category name for category-specific field mapping

    Returns:
        Dict with CPD column names as keys
    """
    part_data = {
        'Part Number': company_pn,
        'Status': 'Active',
    }

    if pas_result.get('best_match'):
        part = pas_result['best_match'].get('searchProviderPart', {})
        properties = part.get('properties', {}).get('succeeded', {})

        # Core fields
        part_data['Manufacturer Part Number'] = part.get('manufacturerPartNumber', '')
        part_data['Manufacturer Name'] = part.get('manufacturerName', '')
        part_data['Description'] = part.get('description', '')

        # Extract from properties
        if PAS_PROPERTY_LIFECYCLE_STATUS in properties:
            part_data['Part Life Cycle Code'] = properties[PAS_PROPERTY_LIFECYCLE_STATUS]

        # Datasheet URL
        datasheet = properties.get(PAS_PROPERTY_DATASHEET_URL)
        if datasheet:
            if isinstance(datasheet, dict) and datasheet.get('__complex__') == 'Url':
                part_data['Current Datasheet Url'] = datasheet.get('value', '')
            elif isinstance(datasheet, str):
                part_data['Current Datasheet Url'] = datasheet

        # Extract category-specific parametric data
        if category:
            field_mapping = get_dbc_field_mapping(category)

            # Create reverse mapping: PAS property ID -> DBC field name
            pas_to_dbc = {v: k for k, v in field_mapping.items()}

            for prop_id, value in properties.items():
                if prop_id in pas_to_dbc:
                    dbc_field = pas_to_dbc[prop_id]
                    # Skip fields we've already set
                    if dbc_field in part_data:
                        continue

                    # Handle complex value types
                    if isinstance(value, dict):
                        if value.get('__complex__') == 'Url':
                            extracted_value = value.get('value', '')
                        elif value.get('__complex__') == 'Quantity':
                            # For quantities, extract the numeric value
                            extracted_value = value.get('value', '')
                        else:
                            extracted_value = str(value)
                    elif isinstance(value, (list, tuple)):
                        extracted_value = ', '.join(str(v) for v in value)
                    else:
                        extracted_value = value

                    if extracted_value:
                        part_data[dbc_field] = extracted_value

        # Log what parametric data was extracted
        parametric_fields = [k for k in part_data.keys()
                           if k not in ['Part Number', 'Status', 'Manufacturer Part Number',
                                       'Manufacturer Name', 'Description', 'Part Life Cycle Code',
                                       'Current Datasheet Url']]
        if parametric_fields:
            logger.debug(f"  Parametric data extracted: {parametric_fields}")

    return part_data


def main():
    parser = argparse.ArgumentParser(
        description='Populate CPD database from BOM using PAS API'
    )
    parser.add_argument(
        '--bom', '-b',
        default='EV50F63A_MCP19061_Dual_USB_DCP.xlsx',
        help='Path to BOM Excel file'
    )
    parser.add_argument(
        '--cpd', '-c',
        default='TemplateLibrary/TemplateLibrary.cpd',
        help='Path to CPD SQLite database'
    )
    parser.add_argument(
        '--dbc', '-d',
        default='TemplateLibrary/TemplateLibrary.dbc',
        help='Path to DBC XML configuration file'
    )
    parser.add_argument(
        '--dry-run', '-n',
        action='store_true',
        help='Perform a dry run without modifying the database'
    )
    parser.add_argument(
        '--limit', '-l',
        type=int,
        default=0,
        help='Limit number of parts to process (0 = all)'
    )
    parser.add_argument(
        '--token', '-t',
        help='PAS API bearer token (alternative to client credentials)'
    )
    parser.add_argument(
        '--client-id',
        help='PAS API client ID (or set PAS_CLIENT_ID env var)'
    )
    parser.add_argument(
        '--client-secret',
        help='PAS API client secret (or set PAS_CLIENT_SECRET env var)'
    )

    args = parser.parse_args()

    # Get script directory for relative paths
    script_dir = os.path.dirname(os.path.abspath(__file__))

    # Resolve paths
    bom_file = os.path.join(script_dir, args.bom) if not os.path.isabs(args.bom) else args.bom
    cpd_file = os.path.join(script_dir, args.cpd) if not os.path.isabs(args.cpd) else args.cpd
    dbc_file = os.path.join(script_dir, args.dbc) if not os.path.isabs(args.dbc) else args.dbc

    # Validate files exist
    if not os.path.exists(bom_file):
        logger.error(f"BOM file not found: {bom_file}")
        sys.exit(1)

    if not os.path.exists(cpd_file):
        logger.error(f"CPD file not found: {cpd_file}")
        sys.exit(1)

    if not os.path.exists(dbc_file):
        logger.error(f"DBC file not found: {dbc_file}")
        sys.exit(1)

    print("\n" + "=" * 60)
    print("BOM to CPD Population Tool")
    print("=" * 60)
    print(f"\nBOM File: {bom_file}")
    print(f"CPD File: {cpd_file}")
    print(f"DBC File: {dbc_file}")
    print(f"Dry Run: {args.dry_run}")
    print()

    # Initialize components
    logger.info("Initializing...")

    dbc_parser = DBCParser(dbc_file)
    logger.info(f"Loaded {len(dbc_parser.categories)} categories from DBC")

    bom_parser = BOMParser(bom_file)
    parts = bom_parser.parse()

    if args.limit > 0:
        parts = parts[:args.limit]
        logger.info(f"Limited to first {args.limit} parts")

    # Check for credentials
    has_credentials = (
        args.token or
        (args.client_id and args.client_secret) or
        (os.environ.get("PAS_CLIENT_ID") and os.environ.get("PAS_CLIENT_SECRET"))
    )

    if not has_credentials:
        logger.warning("No PAS API credentials provided!")
        logger.warning("Use --token, --client-id/--client-secret, or set PAS_CLIENT_ID/PAS_CLIENT_SECRET env vars")
        print("\nWould you like to enter a bearer token now? (y/n): ", end="")
        response = input().strip().lower()
        if response == 'y':
            print("Enter bearer token: ", end="")
            args.token = input().strip()
        else:
            logger.error("Cannot proceed without PAS API credentials")
            sys.exit(1)

    pas_client = PASClient(
        client_id=args.client_id,
        client_secret=args.client_secret,
        bearer_token=args.token
    )

    cpd_updater = CPDUpdater(cpd_file, dbc_parser)
    cpd_updater.connect()

    # Create backup unless dry run
    if not args.dry_run:
        cpd_updater.backup()

    # Process results tracking
    results = {
        'processed': 0,
        'found': 0,
        'not_found': 0,
        'updated': 0,
        'inserted': 0,
        'errors': 0,
        'skipped': 0,
        'by_category': {}
    }

    # Process each part
    print("\n" + "-" * 60)
    print("Processing Parts")
    print("-" * 60 + "\n")

    for i, part in enumerate(parts):
        company_pn = part['company_pn']
        manufacturer = part['manufacturer']
        mpn = part['mpn']

        logger.info(f"[{i+1}/{len(parts)}] {company_pn}: {manufacturer} / {mpn}")
        results['processed'] += 1

        # Search PAS
        pas_result = pas_client.search_part(mpn, manufacturer)

        if pas_result.get('found'):
            results['found'] += 1
            category = pas_result.get('category')

            if category:
                logger.info(f"  -> Found in PAS, Category: {category}")

                # Track by category
                if category not in results['by_category']:
                    results['by_category'][category] = 0
                results['by_category'][category] += 1

                # Extract part data with category-specific mappings
                part_data = extract_part_data(pas_result, company_pn, category)

                # TODO: Enhanced search disabled - outputs may need validation
                # If category supports enhanced outputs, do a second search
                # if category in DBC_CATEGORY_FIELD_MAPPINGS:
                #     enhanced_result = pas_client.search_part_with_outputs(
                #         mpn, manufacturer, get_category_outputs(category)
                #     )
                #     if enhanced_result.get('found'):
                #         part_data = extract_part_data(enhanced_result, company_pn, category)
                #         logger.debug(f"  -> Enhanced data extracted for {category}")

                if not args.dry_run:
                    # Insert/update in CPD
                    success = cpd_updater.insert_or_update_part(category, part_data)
                    if success:
                        results['updated'] += 1
                        logger.info(f"  -> Updated in CPD table: {category}")
                    else:
                        results['errors'] += 1
                        logger.warning(f"  -> Failed to update CPD")
                else:
                    logger.info(f"  -> [DRY RUN] Would update CPD table: {category}")
                    results['updated'] += 1
            else:
                results['skipped'] += 1
                logger.warning(f"  -> Found but could not determine category")
        else:
            results['not_found'] += 1
            if pas_result.get('error'):
                results['errors'] += 1
                logger.warning(f"  -> Error: {pas_result['error']}")
            else:
                logger.info(f"  -> Not found in PAS")

        # Rate limiting
        time.sleep(0.5)

    # Close database
    cpd_updater.close()

    # Print summary
    print("\n" + "=" * 60)
    print("Summary")
    print("=" * 60)
    print(f"\nTotal processed:   {results['processed']}")
    print(f"Found in PAS:      {results['found']}")
    print(f"Not found in PAS:  {results['not_found']}")
    print(f"Updated/Inserted:  {results['updated']}")
    print(f"Skipped (no cat):  {results['skipped']}")
    print(f"Errors:            {results['errors']}")

    if results['by_category']:
        print("\nParts by Category:")
        for cat, count in sorted(results['by_category'].items()):
            print(f"  {cat}: {count}")

    if args.dry_run:
        print("\n[DRY RUN] No changes were made to the database")

    print()


if __name__ == "__main__":
    main()
