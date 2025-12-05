
import logging
import time
from populate_bom import PASClient
from property_mapping import PROPERTY_ID_TO_NAME

# Set up logging
logging.basicConfig(level=logging.INFO, format='%(message)s')
logger = logging.getLogger(__name__)

# Credentials
CLIENT_ID = "TtC6_9BSzXPzsFg0Iolj9"
CLIENT_SECRET = "UCubdl2a9hyc2pklk9X088f6QMitneY_My-3lUnnbrUnJGYO6Ltj7bCFk9_ffpgBbjt5_nWaKF1ZxPFOk-HcjA"

def validate_properties():
    client = PASClient(client_id=CLIENT_ID, client_secret=CLIENT_SECRET)
    
    # Test parameters
    mpn = "CRCW040210K0FKED" 
    mfg = "Vishay"
    
    print(f"Validating {len(PROPERTY_ID_TO_NAME)} properties...")
    
    valid_props = []
    invalid_props = []
    
    for prop_id, prop_name in PROPERTY_ID_TO_NAME.items():
        print(f"Testing {prop_id} ({prop_name})...", end="", flush=True)
        
        try:
            # Try search with just this property
            result = client.search_part_with_outputs(mpn, mfg, [prop_id])
            
            if result.get('error'):
                print(f" FAILED: {result['error']}")
                invalid_props.append((prop_id, prop_name, result['error']))
            else:
                print(" OK")
                valid_props.append(prop_id)
                
        except Exception as e:
            print(f" ERROR: {str(e)}")
            invalid_props.append((prop_id, prop_name, str(e)))
            
        # Small delay to avoid rate limiting
        time.sleep(0.5)
            
    print("\n" + "="*50)
    print(f"Summary: {len(valid_props)} valid, {len(invalid_props)} invalid")
    print("="*50)
    
    if invalid_props:
        print("\nInvalid Properties:")
        for pid, name, err in invalid_props:
            print(f"- {pid} ({name}): {err}")

if __name__ == "__main__":
    validate_properties()
