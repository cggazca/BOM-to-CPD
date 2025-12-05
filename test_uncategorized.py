
import os
import sqlite3
import logging
from populate_bom import CPDUpdater

# Mock DBCParser
class MockDBCParser:
    def __init__(self):
        self.categories = []

# Set up logging
logging.basicConfig(level=logging.INFO)

def test_uncategorized():
    db_file = "test_uncategorized.cpd"
    if os.path.exists(db_file):
        os.remove(db_file)
        
    updater = CPDUpdater(db_file, MockDBCParser())
    updater.connect()
    
    print("Testing ensure_uncategorized_table...")
    updater.ensure_uncategorized_table()
    
    if updater.table_exists("Uncategorized"):
        print("PASS: Table created")
    else:
        print("FAIL: Table not created")
        return

    print("Testing insert into Uncategorized...")
    data = {
        "Part Number": "TEST-PN-001",
        "Manufacturer Part Number": "TEST-MPN-001",
        "Manufacturer Name": "Test Mfg"
    }
    
    success = updater.insert_or_update_part("Uncategorized", data)
    if success:
        print("PASS: Insert successful")
    else:
        print("FAIL: Insert failed")
        return
        
    # Verify data
    conn = sqlite3.connect(db_file)
    cursor = conn.execute('SELECT * FROM "Uncategorized"')
    row = cursor.fetchone()
    print(f"Row: {row}")
    
    if row and row[1] == "TEST-PN-001":
         print("PASS: Data verified")
    else:
         print("FAIL: Data verification failed")

    updater.close()
    conn.close()
    
    # Cleanup
    if os.path.exists(db_file):
        os.remove(db_file)

if __name__ == "__main__":
    test_uncategorized()
