"""
Unit tests for populate_bom.py

Tests BOM parsing, data extraction, and CPD population logic.
Does NOT test PAS API calls (requires authentication).
"""
import unittest
import sys
import os
import tempfile
import shutil
import sqlite3

# Add parent directory to path
sys.path.insert(0, os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

from populate_bom import BOMParser, CPDUpdater, extract_part_data
from dbc_parser import DBCParser, FieldDefinition, CategoryDefinition
from property_mapping import (
    PAS_PROPERTY_MANUFACTURER_NAME,
    PAS_PROPERTY_MANUFACTURER_PN,
    PAS_PROPERTY_DESCRIPTION,
    PAS_PROPERTY_DATASHEET_URL,
    PAS_PROPERTY_LIFECYCLE_STATUS,
    PAS_RESISTOR_RESISTANCE,
    PAS_RESISTOR_TOLERANCE,
    get_dbc_field_mapping,
)


class TestBOMParser(unittest.TestCase):
    """Test BOM Excel file parsing"""

    @classmethod
    def setUpClass(cls):
        """Set up test BOM file path"""
        script_dir = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
        cls.bom_path = os.path.join(
            script_dir, "EV50F63A_MCP19061_Dual_USB_DCP.xlsx"
        )
        cls.bom_exists = os.path.exists(cls.bom_path)

    def test_bom_file_exists(self):
        """Test that sample BOM file exists"""
        if not self.bom_exists:
            self.skipTest("BOM file not found")
        self.assertTrue(os.path.exists(self.bom_path))

    def test_parse_bom_returns_list(self):
        """Test parsing BOM returns list of parts"""
        if not self.bom_exists:
            self.skipTest("BOM file not found")
        parser = BOMParser(self.bom_path)
        parts = parser.parse()
        self.assertIsInstance(parts, list)

    def test_parse_bom_returns_parts(self):
        """Test parsing BOM returns non-empty list"""
        if not self.bom_exists:
            self.skipTest("BOM file not found")
        parser = BOMParser(self.bom_path)
        parts = parser.parse()
        self.assertGreater(len(parts), 0)

    def test_parts_have_company_pn(self):
        """Test each part has a company part number"""
        if not self.bom_exists:
            self.skipTest("BOM file not found")
        parser = BOMParser(self.bom_path)
        parts = parser.parse()
        for part in parts[:10]:  # Check first 10
            self.assertIn("company_pn", part)
            self.assertIsNotNone(part["company_pn"])

    def test_parts_have_manufacturer_mpn_pairs(self):
        """Test parts have manufacturer/MPN pairs"""
        if not self.bom_exists:
            self.skipTest("BOM file not found")
        parser = BOMParser(self.bom_path)
        parts = parser.parse()
        # At least some parts should have MPN data
        parts_with_mpn = [p for p in parts if p.get("manufacturer") and p.get("mpn")]
        self.assertGreater(len(parts_with_mpn), 0)

    def test_part_structure(self):
        """Test part dictionary structure"""
        if not self.bom_exists:
            self.skipTest("BOM file not found")
        parser = BOMParser(self.bom_path)
        parts = parser.parse()
        # Find a part with MPN
        part = next((p for p in parts if p.get("mpn")), None)
        if part:
            self.assertIn("company_pn", part)
            self.assertIn("manufacturer", part)
            self.assertIn("mpn", part)


class TestExtractPartData(unittest.TestCase):
    """Test extraction of part data from PAS response"""

    def test_extract_basic_properties(self):
        """Test extracting basic part properties"""
        # Mock PAS response matching actual API structure
        pas_response = {
            "best_match": {
                "searchProviderPart": {
                    "manufacturerName": "Test Manufacturer",
                    "manufacturerPartNumber": "TEST-MPN-123",
                    "description": "Test Component Description",
                    "properties": {
                        "succeeded": {
                            PAS_PROPERTY_DATASHEET_URL: "https://example.com/datasheet.pdf",
                            PAS_PROPERTY_LIFECYCLE_STATUS: "Active",
                        }
                    }
                }
            }
        }

        result = extract_part_data(pas_response, "TEST-PN-001", "Resistors")

        self.assertEqual(result.get("Manufacturer Name"), "Test Manufacturer")
        self.assertEqual(result.get("Manufacturer Part Number"), "TEST-MPN-123")
        self.assertEqual(result.get("Description"), "Test Component Description")

    def test_extract_resistor_properties(self):
        """Test extracting resistor-specific properties"""
        pas_response = {
            "best_match": {
                "searchProviderPart": {
                    "manufacturerName": "Vishay",
                    "manufacturerPartNumber": "CRCW060310K0FKEA",
                    "description": "RES 10K OHM 1% 0.1W 0603",
                    "properties": {
                        "succeeded": {
                            PAS_RESISTOR_RESISTANCE: "10000",
                            PAS_RESISTOR_TOLERANCE: "1",
                        }
                    }
                }
            }
        }

        result = extract_part_data(pas_response, "RES-001", "Resistors")

        self.assertEqual(result.get("Manufacturer Name"), "Vishay")
        self.assertIn("Value", result)  # Resistance -> Value

    def test_extract_with_no_parts(self):
        """Test extracting from response with no parts"""
        pas_response = {
            "best_match": None
        }

        result = extract_part_data(pas_response, "RES-002", "Resistors")

        # When no parts found, result should still contain company_pn
        self.assertEqual(result.get("Part Number"), "RES-002")

    def test_extract_handles_missing_properties(self):
        """Test handling of missing properties"""
        pas_response = {
            "best_match": {
                "searchProviderPart": {
                    "manufacturerName": "Test Mfg",
                    "manufacturerPartNumber": "",
                    "description": "",
                    "properties": {
                        "succeeded": {}
                    }
                }
            }
        }

        result = extract_part_data(pas_response, "RES-003", "Resistors")

        self.assertEqual(result.get("Manufacturer Name"), "Test Mfg")
        # Missing properties should not cause error


class MockDBCParser:
    """Mock DBCParser for testing CPDUpdater"""

    def __init__(self):
        # Create mock fields for Resistors category
        self.resistor_fields = [
            FieldDefinition("Part Number", "Part Number", "Part Number", 1),
            FieldDefinition("Manufacturer Name", "Manufacturer Name", "Manufacturer Name", 1),
            FieldDefinition("Manufacturer Part Number", "Manufacturer Part Number", "Manufacturer Part Number", 1),
            FieldDefinition("Description", "Description", "Description", 1),
            FieldDefinition("Value", "Value", "Resistance", 2),
            FieldDefinition("Tolerance", "Tolerance", "Tolerance", 2),
            FieldDefinition("Current Datasheet Url", "Current Datasheet Url", "Datasheet URL", 1),
            FieldDefinition("Part Life Cycle Code", "Part Life Cycle Code", "Lifecycle", 1),
        ]
        self.categories = {
            "Resistors": CategoryDefinition("Resistors", "Resistors", self.resistor_fields)
        }

    def get_category(self, name):
        return self.categories.get(name) or self.categories.get(name.lower())

    def get_all_categories(self):
        return list(self.categories.keys())


class TestCPDUpdater(unittest.TestCase):
    """Test CPD SQLite database operations"""

    def setUp(self):
        """Create temporary test database"""
        self.temp_dir = tempfile.mkdtemp()
        self.cpd_path = os.path.join(self.temp_dir, "test.cpd")
        # Create mock dbc_parser
        self.mock_dbc = MockDBCParser()
        # Create test database with Resistors table (matching real CPD structure)
        conn = sqlite3.connect(self.cpd_path)
        cursor = conn.cursor()
        cursor.execute("""
            CREATE TABLE Resistors (
                "obj_id" INTEGER PRIMARY KEY,
                "Part Number" TEXT,
                "Manufacturer Name" TEXT,
                "Manufacturer Part Number" TEXT,
                "Description" TEXT,
                "Value" REAL,
                "Tolerance" REAL,
                "Current Datasheet Url" TEXT,
                "Part Life Cycle Code" TEXT
            )
        """)
        conn.commit()
        conn.close()

    def tearDown(self):
        """Clean up temporary directory"""
        shutil.rmtree(self.temp_dir)

    def test_insert_new_part(self):
        """Test inserting a new part"""
        updater = CPDUpdater(self.cpd_path, self.mock_dbc)
        updater.connect()

        part_data = {
            "Part Number": "RES001",
            "Manufacturer Name": "Vishay",
            "Manufacturer Part Number": "CRCW060310K0FKEA",
            "Description": "RES 10K 1% 0603",
            "Value": 10000.0,
            "Tolerance": 1.0,
        }

        success = updater.insert_or_update_part("Resistors", part_data)
        updater.close()
        self.assertTrue(success)

        # Verify insertion
        conn = sqlite3.connect(self.cpd_path)
        cursor = conn.cursor()
        cursor.execute('SELECT * FROM Resistors WHERE "Part Number" = ?', ("RES001",))
        row = cursor.fetchone()
        conn.close()

        self.assertIsNotNone(row)

    def test_update_existing_part(self):
        """Test updating an existing part (uses MPN for matching)"""
        updater = CPDUpdater(self.cpd_path, self.mock_dbc)
        updater.connect()

        # Insert first - includes MPN for update matching
        part_data_v1 = {
            "Part Number": "RES002",
            "Manufacturer Name": "Vishay",
            "Manufacturer Part Number": "CRCW0603-10K",
            "Description": "Original Description",
        }
        updater.insert_or_update_part("Resistors", part_data_v1)

        # Update - same MPN triggers update
        part_data_v2 = {
            "Part Number": "RES002",
            "Manufacturer Name": "Vishay",
            "Manufacturer Part Number": "CRCW0603-10K",
            "Description": "Updated Description",
        }
        success = updater.insert_or_update_part("Resistors", part_data_v2)
        updater.close()
        self.assertTrue(success)

        # Verify update
        conn = sqlite3.connect(self.cpd_path)
        cursor = conn.cursor()
        cursor.execute(
            'SELECT "Description" FROM Resistors WHERE "Part Number" = ?',
            ("RES002",)
        )
        row = cursor.fetchone()
        conn.close()

        self.assertEqual(row[0], "Updated Description")

    def test_insert_without_part_number(self):
        """Test inserting a part without Part Number (allowed by implementation)"""
        updater = CPDUpdater(self.cpd_path, self.mock_dbc)
        updater.connect()

        part_data = {
            "Manufacturer Name": "Vishay",
            "Manufacturer Part Number": "TEST-MPN-001",
        }

        # Implementation allows insert without Part Number
        success = updater.insert_or_update_part("Resistors", part_data)
        updater.close()
        self.assertTrue(success)


class TestCPDUpdaterWithRealDB(unittest.TestCase):
    """Test CPD operations with actual TemplateLibrary.cpd (read-only)"""

    @classmethod
    def setUpClass(cls):
        """Set up path to real CPD file"""
        script_dir = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
        cls.cpd_path = os.path.join(
            script_dir, "TemplateLibrary", "TemplateLibrary.cpd"
        )
        cls.cpd_exists = os.path.exists(cls.cpd_path)

    def test_cpd_file_exists(self):
        """Test that CPD file exists"""
        if not self.cpd_exists:
            self.skipTest("CPD file not found")
        self.assertTrue(os.path.exists(self.cpd_path))

    def test_cpd_is_sqlite_database(self):
        """Test that CPD is valid SQLite database"""
        if not self.cpd_exists:
            self.skipTest("CPD file not found")
        conn = sqlite3.connect(self.cpd_path)
        cursor = conn.cursor()
        cursor.execute("SELECT name FROM sqlite_master WHERE type='table'")
        tables = cursor.fetchall()
        conn.close()
        self.assertGreater(len(tables), 0)

    def test_cpd_has_resistors_table(self):
        """Test that CPD has Resistors table"""
        if not self.cpd_exists:
            self.skipTest("CPD file not found")
        conn = sqlite3.connect(self.cpd_path)
        cursor = conn.cursor()
        cursor.execute("SELECT name FROM sqlite_master WHERE type='table' AND name='Resistors'")
        tables = cursor.fetchall()
        conn.close()
        self.assertEqual(len(tables), 1)

    def test_cpd_has_capacitors_table(self):
        """Test that CPD has Capacitors table"""
        if not self.cpd_exists:
            self.skipTest("CPD file not found")
        conn = sqlite3.connect(self.cpd_path)
        cursor = conn.cursor()
        cursor.execute("SELECT name FROM sqlite_master WHERE type='table' AND name='Capacitors'")
        tables = cursor.fetchall()
        conn.close()
        self.assertEqual(len(tables), 1)

    def test_cpd_has_connectors_table(self):
        """Test that CPD has Connectors table"""
        if not self.cpd_exists:
            self.skipTest("CPD file not found")
        conn = sqlite3.connect(self.cpd_path)
        cursor = conn.cursor()
        cursor.execute("SELECT name FROM sqlite_master WHERE type='table' AND name='Connectors'")
        tables = cursor.fetchall()
        conn.close()
        self.assertEqual(len(tables), 1)


class TestWorkflowIntegration(unittest.TestCase):
    """Integration tests for the full workflow (without API)"""

    @classmethod
    def setUpClass(cls):
        """Set up paths"""
        script_dir = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
        cls.bom_path = os.path.join(
            script_dir, "EV50F63A_MCP19061_Dual_USB_DCP.xlsx"
        )
        cls.dbc_path = os.path.join(
            script_dir, "TemplateLibrary", "TemplateLibrary.dbc"
        )
        cls.cpd_path = os.path.join(
            script_dir, "TemplateLibrary", "TemplateLibrary.cpd"
        )
        cls.all_exist = (
            os.path.exists(cls.bom_path) and
            os.path.exists(cls.dbc_path) and
            os.path.exists(cls.cpd_path)
        )

    def test_bom_to_category_flow(self):
        """Test BOM parsing -> category detection flow"""
        if not self.all_exist:
            self.skipTest("Required files not found")

        from dbc_parser import DBCParser

        # Parse BOM
        bom_parser = BOMParser(self.bom_path)
        parts = bom_parser.parse()

        # Parse DBC
        dbc_parser = DBCParser(self.dbc_path)

        # For each part with MPN, try to detect category
        parts_with_mpn = [p for p in parts if p.get("mpn")][:5]

        for part in parts_with_mpn:
            company_pn = part["company_pn"]
            # Category detection would happen after PAS search
            # Here we just verify the structure
            self.assertIsNotNone(company_pn)

    def test_field_mapping_to_cpd_columns(self):
        """Test field mapping matches CPD table columns"""
        if not self.all_exist:
            self.skipTest("Required files not found")

        # Get field mapping for Resistors
        mapping = get_dbc_field_mapping("Resistors")

        # Get CPD table columns
        conn = sqlite3.connect(self.cpd_path)
        cursor = conn.cursor()
        cursor.execute("PRAGMA table_info(Resistors)")
        columns = [row[1] for row in cursor.fetchall()]
        conn.close()

        # All mapped fields should exist in CPD
        for field_name in mapping.keys():
            self.assertIn(
                field_name, columns,
                f"Field '{field_name}' not in Resistors table"
            )


if __name__ == "__main__":
    unittest.main()
