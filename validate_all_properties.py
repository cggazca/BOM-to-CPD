
import logging
import time
from typing import Dict, List, Tuple
from populate_bom import PASClient
from property_mapping import get_category_outputs, DBC_CATEGORY_FIELD_MAPPINGS

# Set up logging
logging.basicConfig(level=logging.INFO, format='%(message)s')
logger = logging.getLogger(__name__)

# Credentials (hardcoded for this script as per user context)
CLIENT_ID = "TtC6_9BSzXPzsFg0Iolj9"
CLIENT_SECRET = "UCubdl2a9hyc2pklk9X088f6QMitneY_My-3lUnnbrUnJGYO6Ltj7bCFk9_ffpgBbjt5_nWaKF1ZxPFOk-HcjA"

# Representative parts for each category
REPRESENTATIVE_PARTS = {
    "Resistors": ("CRCW040210K0FKED", "Vishay"),
    "Capacitors": ("CL21B104KBCNNNC", "Samsung"), # Common 100nF
    "Inductors": ("VLS252010T-4R7M", "TDK"), # Power Inductor
    "Diodes": ("1N4148WS-7-F", "Diodes Incorporated"),
    "Transistors": ("2N7002-7-F", "Diodes Incorporated"),
    "LEDs": ("LG Q971-KN-1", "Osram"), # Generic LED search might be needed if this fails
    "Connectors": ("632723300011", "Wurth Elektronik"),
    "Terminal Blocks": ("691102710002", "Wurth Elektronik"),
    "Fuses": ("0466001.NR", "Littelfuse"),
    "Switches": ("B3F-1000", "Omron"),
    "Crystals": ("ABS25-32.768KHZ-T", "Abracon"),
    "Oscillators": ("ASE-24.000MHZ-L-C-T", "Abracon"),
    "ICs": ("NE555DR", "Texas Instruments"),
}

def validate_all_properties():
    client = PASClient(client_id=CLIENT_ID, client_secret=CLIENT_SECRET)
    
    results = {} # Category -> (valid_list, invalid_list)
    
    for category, (mpn, mfg) in REPRESENTATIVE_PARTS.items():
        if category not in DBC_CATEGORY_FIELD_MAPPINGS:
            continue
            
        print(f"\nChecking Category: {category} (Part: {mfg} {mpn})")
        
        # 1. Find Part Class ID
        initial_result = client.search_part(mpn, mfg)
        if not initial_result.get('found'):
            print(f"  SKIPPING: Representative part not found in PAS.")
            continue
            
        part = initial_result['best_match'].get('searchProviderPart', {})
        part_class_id = part.get('partClassId')
        
        if not part_class_id:
            print(f"  SKIPPING: No Part Class ID found for part.")
            continue
            
        print(f"  Part Class ID: {part_class_id}")
        
        # 2. Get Properties to Test
        outputs = get_category_outputs(category)
        if not outputs:
            print("  No properties mapped for this category.")
            continue
            
        valid_props = []
        invalid_props = []
        
        print(f"  Testing {len(outputs)} properties...")
        
        for prop_id in outputs:
            # Test individual property
            endpoint = f'/api/v2/search-providers/{client.config["SearchProviderId"]}/{client.config["SearchProviderVersion"]}/parametric/search'
            
            search_filter = {
                "__valueOperator__": "SmartMatch",
                "__expression__": "ValueExpression",
                "propertyId": "d8ac8dcc", # MPN
                "term": mpn
            }
            
            request_body = {
                "searchParameters": {
                    "partClassId": part_class_id,
                    "customParameters": {},
                    "outputs": [prop_id],
                    "sort": [],
                    "paging": {"requestedPageSize": 1},
                    "filter": search_filter
                }
            }
            
            try:
                # Suppress logging for these requests to keep output clean
                result = client._make_request('POST', endpoint, json_data=request_body)
                
                if result.get('success'):
                    valid_props.append(prop_id)
                    print(".", end="", flush=True)
                else:
                    invalid_props.append(prop_id)
                    print("X", end="", flush=True)
                    
            except Exception as e:
                invalid_props.append(prop_id)
                print("E", end="", flush=True)
            
            time.sleep(0.2) # Rate limit
            
        print(f"\n  Result: {len(valid_props)} Valid, {len(invalid_props)} Invalid")
        if invalid_props:
            print(f"  Invalid IDs: {invalid_props}")
            
        results[category] = (valid_props, invalid_props)

    print("\n" + "="*60)
    print("FINAL SUMMARY")
    print("="*60)
    
    for category, (valid, invalid) in results.items():
        if invalid:
            print(f"\nCategory: {category}")
            print(f"  Invalid Properties to Remove: {invalid}")

if __name__ == "__main__":
    validate_all_properties()
