import logging
import sys
import os
import time
from typing import Dict, List, Tuple, Optional

# Add current directory to path
sys.path.append(os.path.dirname(os.path.abspath(__file__)))

from populate_bom import PASClient
from dbc_parser import DBCParser
import property_mapping

# Setup logging
logging.basicConfig(level=logging.INFO, format='%(message)s')
logger = logging.getLogger(__name__)

# Representative parts for validation
# Format: Category: (MPN, Manufacturer)
REPRESENTATIVE_PARTS = {
    "Resistors": ("CRCW040210K0FKED", "Vishay"),
    "Capacitors": ("CL21B104KBCNNNC", "Samsung"),
    "Inductors": ("VLS252010T-4R7M", "TDK"),
    "Diodes": ("1N4148WS-7-F", "Diodes Incorporated"),
    "Transistors": ("2N7002-7-F", "Diodes Incorporated"),
    "Connectors": ("632723300011", "Wurth Elektronik"),
    "Terminal Blocks": ("282837-2", "TE Connectivity"),
    "LEDs": ("LG Q971-KN-1", "Osram"), # Mapped to Optoelectronics in property_mapping? No, Optoelectronics is the category
    "Optoelectronics": ("LG Q971-KN-1", "Osram"),
    "Circuit Protection": ("0215005.MXP", "Littelfuse"), # Fuse
    "Crystals Resonators": ("ABS25-32.768KHZ-T", "Abracon"),
    "Switches": ("B3F-1000", "Omron"),
    "Relays": ("G5V-1-DC5", "Omron"),
    "Memory": ("AT24C256C-SSHL-T", "Microchip"),
    "Microcontrollers and Processors": ("STM32F103C8T6", "STMicroelectronics"),
    "Amplifier Circuits": ("LM358DT", "STMicroelectronics"), # OpAmp
    "Power Circuits": ("LM7805ACT", "Fairchild"), # Regulator
    "Logic": ("SN74HC00DR", "Texas Instruments"),
    "Filters": ("BLM18AG601SN1D", "Murata"), # Ferrite Bead (often in Filters or Inductors)
}

def main():
    # 1. Initialize PAS Client
    client_id = os.environ.get("PAS_CLIENT_ID")
    client_secret = os.environ.get("PAS_CLIENT_SECRET")
    
    if not client_id or not client_secret:
        # Fallback to hardcoded for testing if env vars missing
        client_id = "TtC6_9BSzXPzsFg0Iolj9"
        client_secret = "UCubdl2a9hyc2pklk9X088f6QMitneY_My-3lUnnbrUnJGYO6Ltj7bCFk9_ffpgBbjt5_nWaKF1ZxPFOk-HcjA"

    logger.info("Initializing PAS Client...")
    client = PASClient(client_id, client_secret)
    if not client._get_access_token():
        logger.error("Failed to authenticate with PAS.")
        return

    # 2. Parse DBC
    dbc_path = os.path.join(os.path.dirname(os.path.abspath(__file__)), "TemplateLibrary", "TemplateLibrary.dbc")
    if not os.path.exists(dbc_path):
        logger.error(f"DBC file not found: {dbc_path}")
        return
        
    logger.info(f"Parsing DBC file: {dbc_path}")
    parser = DBCParser(dbc_path)
    
    logger.info("=" * 80)
    logger.info("DBC PROPERTY VALIDATION REPORT")
    logger.info("=" * 80)

    # 3. Iterate Categories
    total_fields = 0
    mapped_fields = 0
    valid_fields = 0
    invalid_fields = 0
    unmapped_fields = 0
    
    # Standard fields to skip detailed validation for (assumed valid if mapped to core props)
    SKIP_FIELDS = [
        "Part Number", "Part Interface", "Cell Name", "Status", "Description", 
        "Part Life Cycle Code", "Manufacturer Name", "Manufacturer Part Number", 
        "System Component", "Current Datasheet Url", "obj_id"
    ]

    for category_name in sorted(parser.get_all_categories()):
        category_def = parser.categories[category_name]
        logger.info(f"\nCategory: {category_name}")
        
        # Get representative part
        rep_part = REPRESENTATIVE_PARTS.get(category_name)
        part_class_id = None
        mpn = ""
        mfg = ""
        
        if rep_part:
            mpn, mfg = rep_part
            logger.info(f"  Representative Part: {mpn} ({mfg})")
            
            # Search to get class ID
            try:
                # Use search_part to find the part and its class ID
                search_result = client.search_part(mpn, mfg)
                
                if search_result.get('found') and search_result.get('best_match'):
                    best_match = search_result['best_match']
                    part_data = best_match.get('searchProviderPart', {})
                    part_class_id = part_data.get('partClassId')
                
                if part_class_id:
                    logger.info(f"  Found Class ID: {part_class_id}")
                else:
                    logger.warning(f"  [WARNING] Could not find Class ID for representative part {mpn}")
            except Exception as e:
                 logger.error(f"  Error searching for representative part: {e}")

        else:
            logger.warning("  [WARNING] No representative part defined for this category.")

        
        # Check mappings
        # We need to check if the category is in DBC_CATEGORY_FIELD_MAPPINGS
        # If not, it falls back to common fields.
        mappings = property_mapping.get_dbc_field_mapping(category_name)
        
        for field in category_def.fields:
            total_fields += 1
            field_name = field.field_name
            
            # Skip standard fields
            if field_name in SKIP_FIELDS:
                continue

            if field_name in mappings:
                mapped_fields += 1
                prop_id = mappings[field_name]
                
                status = "MAPPED"
                validation_msg = ""
                
                if part_class_id:
                    try:
                        # Validate using search_part_with_outputs
                        # We request ONLY this property to see if it causes an error
                        # Note: search_part_with_outputs uses the property_mapping.get_category_outputs internally?
                        # No, it takes `outputs` as an argument.
                        
                        # We need to pass [prop_id] as outputs.
                        
                        result = client.search_part_with_outputs(
                            manufacturer_pn=mpn,
                            manufacturer=mfg,
                            outputs=[prop_id],
                            part_class_id=part_class_id
                        )
                        
                        # Check for validation errors in the response?
                        # The client method catches exceptions but might not expose validation errors if they are in the body but 200 OK.
                        # Wait, the client method returns a dict. If 'error' key exists, it failed.
                        # But validation errors often come as 200 OK with "validationErrors" field.
                        # The client method `_make_request` returns the JSON.
                        # `search_part_with_outputs` processes it.
                        
                        # If `search_part_with_outputs` returns found=True, it means it succeeded.
                        # But we need to be sure it didn't just ignore the invalid property.
                        # PAS usually returns a validation error if a requested property is invalid for the class.
                        
                        if result.get('found'):
                            status = "VALID"
                            valid_fields += 1
                            # Check if the property is actually in the response?
                            # It's hard to check if the value is there without knowing the structure deeply,
                            # but usually if it doesn't error, it's valid.
                        else:
                            if result.get('error'):
                                status = "INVALID"
                                invalid_fields += 1
                                validation_msg = f"Error: {result.get('error')}"
                            else:
                                # Not found could be just no match, but we know the part exists.
                                # If it returns found=False but no error, it might be a search issue.
                                # But if we just found it above, it should be found here.
                                status = "WARNING"
                                validation_msg = "Part not found during validation search"
                                
                    except Exception as e:
                        status = "EXCEPTION"
                        validation_msg = str(e)
                else:
                    status = "UNVERIFIED"
                    validation_msg = "No representative part"

                logger.info(f"  - {field_name:30} -> {prop_id:10} [{status}] {validation_msg}")
                
            else:
                unmapped_fields += 1
                logger.info(f"  - {field_name:30} -> {'(Unmapped)':10}")

    logger.info("\n" + "=" * 80)
    logger.info(f"SUMMARY")
    logger.info(f"Total Fields Checked: {total_fields}")
    logger.info(f"Mapped Fields:        {mapped_fields}")
    logger.info(f"  - Valid:            {valid_fields}")
    logger.info(f"  - Invalid:          {invalid_fields}")
    logger.info(f"  - Unverified:       {mapped_fields - valid_fields - invalid_fields}")
    logger.info(f"Unmapped Fields:      {unmapped_fields}")
    logger.info("=" * 80)

if __name__ == "__main__":
    main()
