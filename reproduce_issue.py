
import sys
import os
import logging
from populate_bom import PASClient

# Set up logging
logging.basicConfig(level=logging.DEBUG)

# Credentials from example.py
CLIENT_ID = "TtC6_9BSzXPzsFg0Iolj9"
CLIENT_SECRET = "UCubdl2a9hyc2pklk9X088f6QMitneY_My-3lUnnbrUnJGYO6Ltj7bCFk9_ffpgBbjt5_nWaKF1ZxPFOk-HcjA"

def test_enhanced_search():
    client = PASClient(client_id=CLIENT_ID, client_secret=CLIENT_SECRET)
    
    # Test parameters
    mpn = "1N4148WS-7-F" 
    mfg = "Diodes Incorporated"
    
    # Full list of Diode properties from property_mapping.py
    outputs = [
        "68ddced3", # Diode Type
        "55daf8f5", # Configuration
        "d1a37148", # Power Dissipation-Max
        "a7269fc5", # Reference Voltage-Nom
        "c4a98aa2", # Forward Voltage-Max (VF)
        "dbc7f810", # Forward Current-Max
        "1aa66be0", # Reverse Voltage-Max
        "72b02506", # Breakdown Voltage-Nom
    ]
    print(f"1. Initial search for {mfg} {mpn}...")
    initial_result = client.search_part(mpn, mfg)
    
    if not initial_result.get('found'):
        print("Part not found in initial search!")
        return

    part = initial_result['best_match'].get('searchProviderPart', {})
    found_class_id = part.get('partClassId')
    found_class_name = part.get('partClassName')
    print(f"Found Part: Class ID = {found_class_id}, Class Name = {found_class_name}")

    if not found_class_id:
        print("No Class ID found, defaulting to Root (76f2225d)")
        found_class_id = "76f2225d"

    print(f"2. Enhanced search with Class ID {found_class_id}...")
    
    # Manually construct request to override partClassId
    endpoint = f'/api/v2/search-providers/{client.config["SearchProviderId"]}/{client.config["SearchProviderVersion"]}/parametric/search'
    
    search_filter = {
        "__valueOperator__": "SmartMatch",
        "__expression__": "ValueExpression",
        "propertyId": "d8ac8dcc", # MPN
        "term": mpn
    }
    
    request_body = {
        "searchParameters": {
            "partClassId": found_class_id,
            "customParameters": {},
            "outputs": outputs,
            "sort": [],
            "paging": {"requestedPageSize": 10},
            "filter": search_filter
        }
    }
    
    try:
        result = client._make_request('POST', endpoint, json_data=request_body)
        print("Result:", result)
    except Exception as e:
        print(f"Error: {e}")

if __name__ == "__main__":
    test_enhanced_search()
