
import logging
from populate_bom import PASClient

# Set up logging
logging.basicConfig(level=logging.INFO)

# Credentials
CLIENT_ID = "TtC6_9BSzXPzsFg0Iolj9"
CLIENT_SECRET = "UCubdl2a9hyc2pklk9X088f6QMitneY_My-3lUnnbrUnJGYO6Ltj7bCFk9_ffpgBbjt5_nWaKF1ZxPFOk-HcjA"

def find_resistor_class():
    client = PASClient(client_id=CLIENT_ID, client_secret=CLIENT_SECRET)
    
    # We can't easily search for class ID by name without traversing the tree.
    # But we can search for a resistor part and see its class ID.
    
    mpn = "B2100A-13-F" 
    mfg = "Diodes Incorporated"
    
    print(f"Searching for {mfg} {mpn} to find its Class ID...")
    result = client.search_part(mpn, mfg)
    
    if result.get('found'):
        part = result['best_match'].get('searchProviderPart', {})
        class_id = part.get('partClassId')
        class_name = part.get('partClassName')
        print(f"Found Part: Class ID = {class_id}, Class Name = {class_name}")
        return class_id
    else:
        print("Part not found")
        return None

if __name__ == "__main__":
    find_resistor_class()
