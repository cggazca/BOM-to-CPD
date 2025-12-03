# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a **Part Aggregation Service (PAS) Client** project for Siemens EDA tools. It provides Python scripts to search and match electronic parts using the Siemens PAS API, implementing the SearchAndAssign matching algorithm originally from legacy Java tools.

## Architecture

### Core Components

- **`pas_client.py`** - Main Python API client with:
  - OAuth 2.0 authentication against Siemens SAM Auth service
  - Part search with enriching (lifecycle, supply chain, pricing data)
  - CSV batch processing with Excel and HTML report generation
  - Distributor information extraction including pricing tiers

- **`Part Aggeration Service/example.py`** - Module-based PAS client (imports from `utils.constants`)
  - Implements SearchAndAssign matching algorithm with multiple match tiers:
    1. Exact match on Part Number AND Manufacturer
    2. Partial manufacturer match
    3. Alphanumeric-only matching (strips special characters)
    4. Leading zero suppression

### API Configuration

Default PAS API endpoints:
- **API URL**: `https://api.pas.partquest.com`
- **Auth URL**: `https://samauth.us-east-1.sws.siemens.com/token`
- **Search Provider**: ID `44`, Version `2`
- **Supply Chain Enricher**: ID `33`, Version `1`

Required OAuth scope: `sws.icarus.api.read`

### Match Types

The client returns these match type constants:
- `MATCH_TYPE_FOUND` - Single exact match
- `MATCH_TYPE_MULTIPLE` - Multiple possible matches
- `MATCH_TYPE_NEED_REVIEW` - Match found but needs manual review
- `MATCH_TYPE_NONE` - No match found
- `MATCH_TYPE_ERROR` - API or processing error

## BOM to CPD Population Tool

The main tool for populating the TemplateLibrary.cpd database from a BOM file:

```bash
# Run with bearer token
python populate_bom.py --token YOUR_BEARER_TOKEN

# Run with client credentials
python populate_bom.py --client-id YOUR_ID --client-secret YOUR_SECRET

# Dry run (no database changes)
python populate_bom.py --dry-run --limit 10

# Full options
python populate_bom.py --bom BOM.xlsx --cpd TemplateLibrary/TemplateLibrary.cpd --dbc TemplateLibrary/TemplateLibrary.dbc
```

### Supporting Modules

- **`dbc_parser.py`** - Parses the DBC XML schema to extract category definitions and field mappings
- **`property_mapping.py`** - PAS API property ID to DBC field name mappings

### Workflow

1. Parse BOM Excel file (expects columns: Company PN, MN 1, MPN 1, ...)
2. For each part, search PAS API using Manufacturer + MPN
3. Infer component category from PAS response (description/part class)
4. Extract parametric data and map to DBC schema
5. Insert/update records in CPD SQLite database

## Legacy CSV Processing Tool

```bash
# Run interactive CSV processing
python "Part Aggeration Service/example.py"
```

The tool prompts for:
1. Bearer token (optional - can auto-authenticate with client credentials)
2. Enable/disable enriching data
3. Input CSV file path (expects `MPN` and `MFG` columns)
4. Output Excel file path

Outputs generated:
- Excel file with match results (color-coded by match type)
- HTML report with Siemens branding for detailed viewing
- Raw JSON responses (if `EnableRawOutput` is enabled)

## Template Library

The `TemplateLibrary/` directory contains Siemens EDA design templates:
- **CellDBLibs/**: Component cell database files (`.cel`)
- **PartsDBLibs/**: Parts database files (`.pdb`)
- **Templates/Layout/**: PCB layout templates for various layer counts (2-18 layers, rigid-flex)
- **Templates/Schematic/**: DxDesigner schematic templates

## Dependencies

Python packages required:
- `requests` - HTTP client
- `pandas` - CSV/DataFrame processing
- `openpyxl` - Excel file generation
- `chardet` - File encoding detection
