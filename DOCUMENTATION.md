# BOM to CPD Population Tool - Technical Documentation

## Overview

The **BOM to CPD Population Tool** automates the process of enriching a Bill of Materials (BOM) with parametric data from the Siemens Part Aggregation Service (PAS) API and populating a Component Parts Database (CPD) for use with Siemens EDA design tools.

### Purpose

This tool bridges the gap between a design engineer's BOM spreadsheet and a structured component library by:

1. Reading company part numbers and manufacturer/MPN pairs from a BOM Excel file
2. Searching the Siemens PAS API to find matching components and retrieve parametric data
3. Automatically categorizing components (Resistors, Capacitors, ICs, etc.)
4. Mapping PAS property IDs to the appropriate DBC/CPD database fields
5. Inserting or updating records in the TemplateLibrary.cpd SQLite database

---

## Architecture

```
+------------------+     +------------------+     +------------------+
|   BOM Excel      |     |   PAS API        |     |   CPD Database   |
|   (.xlsx)        |     |   (REST)         |     |   (.cpd/SQLite)  |
+--------+---------+     +--------+---------+     +--------+---------+
         |                        |                        |
         v                        v                        v
+--------+---------+     +--------+---------+     +--------+---------+
|   BOMParser      |     |   PASClient      |     |   CPDUpdater     |
|   - Parse Excel  |     |   - OAuth 2.0    |     |   - SQLite ops   |
|   - Extract MPN  |     |   - Search API   |     |   - Insert/Update|
+--------+---------+     +--------+---------+     +--------+---------+
         |                        |                        |
         +------------+-----------+------------------------+
                      |
              +-------v-------+
              | populate_bom  |
              | (Main Script) |
              +---------------+
                      |
         +------------+------------+
         |                         |
+--------v--------+       +--------v--------+
|   DBCParser     |       | property_mapping|
|   - Parse XML   |       | - PAS prop IDs  |
|   - Categories  |       | - Field maps    |
+-----------------+       +-----------------+
```

---

## Core Modules

### 1. `populate_bom.py` - Main Orchestrator

The main script that coordinates the entire workflow.

#### Key Classes:

| Class | Description |
|-------|-------------|
| `PASClient` | OAuth 2.0 authenticated client for Siemens PAS API |
| `BOMParser` | Parses BOM Excel files to extract part information |
| `CPDUpdater` | Manages SQLite database operations on the CPD file |

#### Key Functions:

| Function | Description |
|----------|-------------|
| `extract_part_data()` | Converts PAS API response to CPD column format |
| `main()` | Entry point, handles CLI arguments and orchestrates workflow |

---

### 2. `dbc_parser.py` - DBC Schema Parser

Parses the TemplateLibrary.dbc XML file to understand the database schema.

#### Data Classes:

| Class | Description |
|-------|-------------|
| `FieldDefinition` | Defines a single field: name, type (text/numeric), defaults |
| `CategoryDefinition` | Defines a category with its table name and field list |

#### DBCParser Methods:

| Method | Description |
|--------|-------------|
| `get_category(name)` | Returns category definition by name |
| `get_all_categories()` | Returns list of all category names |
| `find_category_for_part(description, part_class)` | Infers category from description |

---

### 3. `property_mapping.py` - Property ID Mappings

Maps PAS API property IDs (8-character hex strings) to DBC/CPD field names.

#### Key Constants:

**Core Properties (Common to all categories):**
```python
PAS_PROPERTY_MANUFACTURER_NAME = "6230417e"  # Manufacturer Name
PAS_PROPERTY_MANUFACTURER_PN   = "14da33e5"  # Manufacturer Part Number
PAS_PROPERTY_DESCRIPTION       = "d6c0bfe2"  # Part Description
PAS_PROPERTY_DATASHEET_URL     = "fb4edf83"  # Datasheet URL
PAS_PROPERTY_LIFECYCLE_STATUS  = "5c5a4d07"  # Lifecycle Status
```

**Category-Specific Properties (Examples):**
```python
# Resistors
PAS_RESISTOR_RESISTANCE = "f5bd4e8a"   # Resistance value
PAS_RESISTOR_TOLERANCE  = "c4c78a35"   # Tolerance percentage

# Capacitors
PAS_CAPACITOR_CAPACITANCE = "e41aff15" # Capacitance value
PAS_CAPACITOR_VOLTAGE     = "da3e61ea" # Rated voltage
```

#### Key Functions:

| Function | Description |
|----------|-------------|
| `get_category_from_description()` | Infers category from part description text |
| `get_dbc_field_mapping(category)` | Returns field-to-property-ID mapping for category |
| `get_category_outputs(category)` | Returns list of property IDs to request from PAS |
| `get_default_outputs()` | Returns common property IDs for all searches |

---

## Supported Categories (36 Total)

The tool supports all 36 component categories defined in the DBC schema:

### Passive Components
- Resistors
- Capacitors
- Inductors
- RC Networks
- Filters

### Semiconductors
- Diodes
- Transistors

### Integrated Circuits (Analog)
- Amplifier Circuits
- Converters
- Power Circuits
- Signal Circuits

### Integrated Circuits (Digital)
- Logic
- Memory
- Microcontrollers and Processors
- Programmable Logic

### Electromechanical
- Connectors
- Sockets
- Relays
- Switches
- Terminal Blocks

### Circuit Protection
- Circuit Protection (Fuses, TVS, ESD)

### Frequency Control
- Crystals Resonators

### Optoelectronics
- Optoelectronics (LEDs, Optocouplers)

### Power/Battery
- Batteries
- Power Supplies

### Sensors
- Sensors

### Misc
- Antennas
- Cables
- Displays
- Hardware
- Heatsinks
- Modules
- Motors
- Transformers
- Test Points
- Uncategorized

---

## Workflow

### Step 1: Parse BOM File

The `BOMParser` reads the Excel BOM file and extracts:
- **Company PN**: Internal part number (becomes "Part Number" in CPD)
- **MN 1, MN 2, ...**: Manufacturer names
- **MPN 1, MPN 2, ...**: Manufacturer part numbers

**Expected BOM Format:**
```
| Company PN | MN 1      | MPN 1           | MN 2   | MPN 2      |
|------------|-----------|-----------------|--------|------------|
| RES-001    | Vishay    | CRCW060310K0FKEA|        |            |
| CAP-002    | Murata    | GRM188R71H104KA |        |            |
```

### Step 2: Search PAS API

For each part with a valid MPN, the tool:
1. Authenticates via OAuth 2.0 (client credentials or bearer token)
2. Sends a search request with MPN and Manufacturer
3. Receives parametric data including:
   - Manufacturer details
   - Description
   - Datasheet URL
   - Lifecycle status
   - Category-specific parameters (resistance, capacitance, voltage ratings, etc.)

### Step 3: Infer Category

The tool determines the component category using:
1. **Part Class** from PAS response (if available)
2. **Description keywords** (e.g., "RESISTOR", "CAPACITOR", "MOSFET")

If no category can be determined, the part goes to "Uncategorized".

### Step 4: Extract Parametric Data

Using the category-specific field mapping, the tool:
1. Maps PAS property IDs to DBC field names
2. Handles complex value types (URLs, Quantities with units)
3. Extracts all relevant parametric data for the category

### Step 5: Update CPD Database

The `CPDUpdater` performs SQLite operations:
1. Checks if part already exists (by MPN)
2. **Updates** existing record if found
3. **Inserts** new record if not found
4. Handles category table creation for "Uncategorized"

---

## Usage

### Graphical User Interface (GUI)

The tool includes a PyQt5-based graphical interface for ease of use.

**Launch the GUI:**
```bash
python populate_bom_gui.py
```

**GUI Features:**

| Section | Description |
|---------|-------------|
| **File Selection** | Browse and select BOM, CPD, and DBC files |
| **Authentication** | Choose between Bearer Token or Client Credentials |
| **Options** | Dry-run mode, part limit |
| **Progress** | Real-time progress bar showing parts processed |
| **Results Table** | Color-coded results (green=found, yellow=not found, red=error) |
| **Log Output** | Timestamped log messages with auto-scroll |

**GUI Screenshot Layout:**
```
+----------------------------------------------------------+
|  BOM to CPD Population Tool                              |
+----------------------------------------------------------+
| File Selection                                            |
|   BOM File: [________________________] [Browse...]        |
|   CPD File: [________________________] [Browse...]        |
|   DBC File: [________________________] [Browse...]        |
+----------------------------------------------------------+
| PAS API Authentication                                    |
|   Method: [Bearer Token ▼]                               |
|   Bearer Token: [********************] [Show]            |
+----------------------------------------------------------+
| Options                                                   |
|   [✓] Dry Run    Limit parts: [0 (No limit)]            |
+----------------------------------------------------------+
| [====================] 45/100 parts    [Run] [Cancel]    |
+----------------------------------------------------------+
| Results                                                   |
| -------------------------------------------------------- |
| Company PN | MPN       | Mfg    | Status | Category     |
| RES-001    | CRCW0603  | Vishay | Found  | Resistors    |
| CAP-002    | GRM188R71 | Murata | Found  | Capacitors   |
+----------------------------------------------------------+
| Log                                                       |
| [12:34:56] Processing RES-001: Vishay / CRCW0603...     |
| [12:34:57]   Found! Category: Resistors                  |
+----------------------------------------------------------+
```

---

### Command Line Options

```bash
python populate_bom.py [OPTIONS]
```

| Option | Description | Default |
|--------|-------------|---------|
| `--bom, -b` | Path to BOM Excel file | `EV50F63A_MCP19061_Dual_USB_DCP.xlsx` |
| `--cpd, -c` | Path to CPD database | `TemplateLibrary/TemplateLibrary.cpd` |
| `--dbc, -d` | Path to DBC XML file | `TemplateLibrary/TemplateLibrary.dbc` |
| `--dry-run, -n` | Preview without modifying database | `False` |
| `--limit, -l` | Limit parts to process (0=all) | `0` |
| `--token, -t` | PAS API bearer token | - |
| `--client-id` | PAS API client ID | `$PAS_CLIENT_ID` env var |
| `--client-secret` | PAS API client secret | `$PAS_CLIENT_SECRET` env var |

### Examples

**Using Bearer Token:**
```bash
python populate_bom.py --token YOUR_BEARER_TOKEN
```

**Using Client Credentials:**
```bash
python populate_bom.py --client-id YOUR_ID --client-secret YOUR_SECRET
```

**Dry Run (Preview Only):**
```bash
python populate_bom.py --dry-run --limit 10
```

**Full Options:**
```bash
python populate_bom.py \
  --bom MyBOM.xlsx \
  --cpd TemplateLibrary/TemplateLibrary.cpd \
  --dbc TemplateLibrary/TemplateLibrary.dbc \
  --token YOUR_TOKEN
```

---

## File Structure

```
PopulateBOM/
├── populate_bom.py          # Main CLI script
├── populate_bom_gui.py      # PyQt5 GUI application
├── dbc_parser.py            # DBC XML parser
├── property_mapping.py      # PAS property ID mappings
├── CLAUDE.md                # Project guidance file
├── DOCUMENTATION.md         # This documentation
│
├── EV50F63A_MCP19061_Dual_USB_DCP.xlsx  # Sample BOM file
│
├── TemplateLibrary/
│   ├── TemplateLibrary.dbc  # DBC schema (XML)
│   └── TemplateLibrary.cpd  # CPD database (SQLite)
│
├── Supplychain/
│   ├── SF-Mapping.xml       # PAS property definitions
│   └── sf_mapping_extracted.json  # Extracted mappings
│
└── tests/
    ├── __init__.py
    ├── test_populate_bom.py     # BOM parser & CPD tests
    ├── test_dbc_parser.py       # DBC parser tests
    └── test_property_mapping.py # Property mapping tests
```

---

## Data Flow Diagram

```
BOM.xlsx                    PAS API                     CPD Database
    │                           │                           │
    │  ┌─────────────────┐      │                           │
    └─>│ Parse BOM       │      │                           │
       │ Extract:        │      │                           │
       │ - Company PN    │      │                           │
       │ - Manufacturer  │      │                           │
       │ - MPN           │      │                           │
       └────────┬────────┘      │                           │
                │               │                           │
                v               │                           │
       ┌─────────────────┐      │                           │
       │ For each part:  │      │                           │
       │ Search PAS API  │─────>│                           │
       └────────┬────────┘      │                           │
                │               │                           │
                │<──────────────┤ Return:                   │
                │               │ - Parametric data         │
                │               │ - Part class              │
                │               │ - Description             │
                v               │                           │
       ┌─────────────────┐      │                           │
       │ Infer Category  │      │                           │
       │ (from class or  │      │                           │
       │  description)   │      │                           │
       └────────┬────────┘      │                           │
                │               │                           │
                v               │                           │
       ┌─────────────────┐      │                           │
       │ Map Properties  │      │                           │
       │ PAS ID -> DBC   │      │                           │
       │ field name      │      │                           │
       └────────┬────────┘      │                           │
                │               │                           │
                v               │                           │
       ┌─────────────────┐      │                           │
       │ Insert/Update   │─────────────────────────────────>│
       │ CPD Record      │      │                           │
       └─────────────────┘      │                           │
```

---

## PAS API Details

### Endpoints

| Endpoint | URL |
|----------|-----|
| API Base | `https://api.pas.partquest.com` |
| Auth | `https://samauth.us-east-1.sws.siemens.com/token` |

### Provider Configuration

| Setting | Value |
|---------|-------|
| Search Provider ID | `44` |
| Search Provider Version | `2` |
| OAuth Scope | `sws.icarus.api.read` |

### Response Structure

```json
{
  "best_match": {
    "searchProviderPart": {
      "manufacturerName": "Vishay",
      "manufacturerPartNumber": "CRCW060310K0FKEA",
      "description": "RES 10K OHM 1% 0.1W 0603",
      "properties": {
        "succeeded": {
          "f5bd4e8a": "10000",        // Resistance
          "c4c78a35": "1",            // Tolerance
          "5c5a4d07": "Active"        // Lifecycle
        }
      }
    }
  }
}
```

---

## Error Handling

| Scenario | Behavior |
|----------|----------|
| Part not found in PAS | Logged, added to "Uncategorized" if enabled |
| Category cannot be determined | Part goes to "Uncategorized" table |
| Database error | Transaction rolled back, error logged |
| API authentication failure | Script exits with error |
| Missing required files | Script exits with error |

---

## Testing

Run the test suite:

```bash
# Run all tests
python -m pytest tests/ -v

# Run specific test file
python -m pytest tests/test_populate_bom.py -v

# Run with coverage
python -m pytest tests/ --cov=. --cov-report=html
```

### Test Coverage

| Module | Tests | Coverage |
|--------|-------|----------|
| `dbc_parser.py` | 31 | DBC parsing, category lookup |
| `property_mapping.py` | 42 | Property mappings, category detection |
| `populate_bom.py` | 20 | BOM parsing, data extraction, CPD ops |
| **Total** | **93** | Full workflow coverage |

---

## Dependencies

```
requests>=2.28.0    # HTTP client for PAS API
pandas>=1.5.0       # Excel file parsing
openpyxl>=3.0.0     # Excel file support for pandas
PyQt5>=5.15.0       # GUI framework (optional, for GUI mode)
```

Install with:
```bash
# Core dependencies (CLI only)
pip install requests pandas openpyxl

# With GUI support
pip install requests pandas openpyxl PyQt5
```

---

## Revision History

| Date | Version | Description |
|------|---------|-------------|
| 2025-12-03 | 1.0 | Initial documentation |

---

## Contact

For questions about the PAS API or authentication, contact your Siemens EDA administrator.
