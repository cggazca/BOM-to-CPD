
import logging
import time
from populate_bom import PASClient

# Set up logging
logging.basicConfig(level=logging.INFO, format='%(message)s')
logger = logging.getLogger(__name__)

# Credentials
CLIENT_ID = "TtC6_9BSzXPzsFg0Iolj9"
CLIENT_SECRET = "UCubdl2a9hyc2pklk9X088f6QMitneY_My-3lUnnbrUnJGYO6Ltj7bCFk9_ffpgBbjt5_nWaKF1ZxPFOk-HcjA"

def validate_diode_properties():
    client = PASClient(client_id=CLIENT_ID, client_secret=CLIENT_SECRET)
    
    mpn = "1N4148WS-7-F" 
    mfg = "Diodes Incorporated"
    part_class_id = "5004c6a3" # Diode Class ID
    
    # Diode properties to test
    diode_props = {
        "68ddced3": "Diode Type",
        "55daf8f5": "Configuration",
        "d1a37148": "Power Dissipation-Max",
        "a7269fc5": "Reference Voltage-Nom",
        "c4a98aa2": "Forward Voltage-Max (VF)",
        "dbc7f810": "Forward Current-Max",
        "1aa66be0": "Reverse Voltage-Max",
        "72b02506": "Breakdown Voltage-Nom",
    }
    
    print(f"Validating {len(diode_props)} Diode properties for Class {part_class_id}...")
    
    valid_props = []
    invalid_props = []
    
    for prop_id, prop_name in diode_props.items():
        print(f"Testing {prop_id} ({prop_name})...", end="", flush=True)
        
        # Manually construct request
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
                "outputs": [prop_id], # Test one by one
                "sort": [],
                "paging": {"requestedPageSize": 10},
                "filter": search_filter
            }
        }
        
        try:
            result = client._make_request('POST', endpoint, json_data=request_body)
            
            if result.get('success'):
                print(" OK")
                valid_props.append(prop_id)
            else:
                print(f" FAILED: {result}")
                invalid_props.append((prop_id, prop_name, "API Error"))
                
        except Exception as e:
            print(f" ERROR: {str(e)}")
            invalid_props.append((prop_id, prop_name, str(e)))
            
        # Small delay
        time.sleep(0.5)
            
    print("\n" + "="*50)
    print(f"Summary: {len(valid_props)} valid, {len(invalid_props)} invalid")
    print("="*50)
    
    if invalid_props:
        print("\nInvalid Properties:")
        for pid, name, err in invalid_props:
            print(f"- {pid} ({name}): {err}")

if __name__ == "__main__":
    validate_diode_properties()
