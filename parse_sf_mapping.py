"""
Parse SF-Mapping.xml to extract PAS Property ID to DBC field name mappings

This script extracts the property definitions from SF-Mapping.xml and creates
a comprehensive mapping between PAS property IDs and their labels/DBC field names.
"""

import xml.etree.ElementTree as ET
from collections import defaultdict
import json


def parse_sf_mapping(xml_file: str) -> dict:
    """
    Parse SF-Mapping.xml and extract property mappings

    Returns:
        Dictionary with:
        - part_classes: dict of class_id -> {label, parent_id, properties}
        - global_properties: dict of property_id -> label (common across all classes)
        - category_properties: dict of category_label -> {property_id: label}
    """

    tree = ET.parse(xml_file)
    root = tree.getroot()

    part_classes = {}
    global_properties = {}
    category_properties = defaultdict(dict)

    # Find all PartClass elements
    for part_class in root.iter('PartClass'):
        class_id = part_class.get('id')
        label = part_class.get('label')
        parent_id = part_class.get('parentId')

        properties = {}
        for prop in part_class.findall('.//Property'):
            prop_id = prop.get('id')
            prop_label = prop.get('label')
            base_units = prop.get('baseUnits', '')

            if prop_id and prop_label:
                properties[prop_id] = {
                    'label': prop_label,
                    'units': base_units
                }

                # Track global properties (appear in root class)
                if parent_id == '76f2225d' or class_id == '76f2225d':
                    global_properties[prop_id] = prop_label

        part_classes[class_id] = {
            'label': label,
            'parent_id': parent_id,
            'properties': properties
        }

        # Store category-level properties for main categories
        if parent_id == '76f2225d':  # Direct child of root
            category_properties[label] = properties

    return {
        'part_classes': part_classes,
        'global_properties': global_properties,
        'category_properties': dict(category_properties)
    }


def generate_property_mapping_code(mapping: dict) -> str:
    """Generate Python code for property_mapping.py"""

    lines = [
        '"""',
        'PAS Property ID Mappings - Generated from SF-Mapping.xml',
        '',
        'This file contains the comprehensive mapping between PAS API property IDs',
        'and their human-readable labels / DBC field names.',
        '"""',
        '',
        '# ============================================================================',
        '# GLOBAL PROPERTIES (Common across all part classes)',
        '# ============================================================================',
        '',
    ]

    # Add global properties
    for prop_id, label in sorted(mapping['global_properties'].items(), key=lambda x: x[1]):
        var_name = f"PAS_PROP_{label.upper().replace(' ', '_').replace('-', '_').replace('(', '').replace(')', '')}"
        var_name = var_name.replace('__', '_')[:50]  # Limit length
        lines.append(f'{var_name} = "{prop_id}"  # {label}')

    lines.extend([
        '',
        '',
        '# ============================================================================',
        '# PROPERTY ID TO LABEL MAPPINGS',
        '# ============================================================================',
        '',
        'PROPERTY_ID_TO_LABEL = {',
    ])

    # Add all property mappings
    all_props = {}
    for class_data in mapping['part_classes'].values():
        for prop_id, prop_data in class_data['properties'].items():
            if prop_id not in all_props:
                all_props[prop_id] = prop_data['label']

    for prop_id, label in sorted(all_props.items(), key=lambda x: x[1]):
        lines.append(f'    "{prop_id}": "{label}",')

    lines.append('}')

    # Add category-specific property sets
    lines.extend([
        '',
        '',
        '# ============================================================================',
        '# CATEGORY-SPECIFIC PROPERTIES',
        '# ============================================================================',
        '',
    ])

    for category, props in sorted(mapping['category_properties'].items()):
        var_name = f"{category.upper().replace(' ', '_').replace('-', '_')}_PROPERTIES"
        lines.append(f'{var_name} = {{')
        for prop_id, prop_data in sorted(props.items(), key=lambda x: x[1]['label']):
            label = prop_data['label']
            units = prop_data.get('units', '')
            if units:
                lines.append(f'    "{prop_id}": "{label} ({units})",')
            else:
                lines.append(f'    "{prop_id}": "{label}",')
        lines.append('}')
        lines.append('')

    return '\n'.join(lines)


def main():
    import os
    import sys

    # Fix console encoding for Unicode characters
    if sys.platform == 'win32':
        sys.stdout.reconfigure(encoding='utf-8', errors='replace')

    script_dir = os.path.dirname(os.path.abspath(__file__))
    xml_file = os.path.join(script_dir, 'SF-Mapping.xml')

    print(f"Parsing {xml_file}...")
    mapping = parse_sf_mapping(xml_file)

    print(f"\nFound {len(mapping['part_classes'])} part classes")
    print(f"Found {len(mapping['global_properties'])} global properties")
    print(f"Found {len(mapping['category_properties'])} top-level categories")

    # Save to JSON for reference (do this first before printing)
    json_file = os.path.join(script_dir, 'sf_mapping_extracted.json')
    with open(json_file, 'w', encoding='utf-8') as f:
        json.dump({
            'global_properties': mapping['global_properties'],
            'category_properties': {
                cat: {pid: pdata['label'] for pid, pdata in props.items()}
                for cat, props in mapping['category_properties'].items()
            },
            'category_properties_with_units': {
                cat: {pid: {'label': pdata['label'], 'units': pdata.get('units', '')}
                      for pid, pdata in props.items()}
                for cat, props in mapping['category_properties'].items()
            }
        }, f, indent=2, ensure_ascii=False)

    print(f"\nExtracted mappings saved to: {json_file}")

    # Print key mappings
    print("\n" + "=" * 60)
    print("KEY PROPERTY MAPPINGS FOR DBC")
    print("=" * 60)

    # Core properties
    core_props = {
        'd8ac8dcc': 'Manufacturer Part Number',
        '6230417e': 'Manufacturer',
        'bf4dd752': 'Description',
        '750a45c8': 'Current Datasheet Url',
        'e5434e21': 'Part Life Cycle Code',
        'a189d244': 'Status Code',
        'e1aa6f26': 'uid (Part ID)',
        '2a2b1476': 'Part Intelligence Url',
    }

    print("\nCore Properties:")
    for pid, label in core_props.items():
        print(f"  {pid} = {label}")

    # Category-specific properties - key parametric ones only
    key_categories = ['Resistors', 'Capacitors', 'Diodes', 'Transistors', 'Inductors']

    # Key parametric properties per category (what we care about for DBC)
    parametric_keywords = [
        'Resistance', 'Tolerance', 'Power', 'Voltage', 'Capacitance',
        'Current', 'Inductance', 'Frequency', 'Temperature', 'Package',
        'Mount', 'Size', 'Height', 'Length', 'Width'
    ]

    for cat in key_categories:
        if cat in mapping['category_properties']:
            print(f"\n{cat} Key Parametric Properties:")
            props = mapping['category_properties'][cat]

            count = 0
            for prop_id, prop_data in sorted(props.items(), key=lambda x: x[1]['label']):
                label = prop_data['label']
                # Only show key parametric properties
                if any(kw.lower() in label.lower() for kw in parametric_keywords):
                    units = prop_data.get('units', '')
                    # Sanitize units for console output
                    units = units.replace('Ω', 'Ohm').replace('µ', 'u').replace('°', 'deg')
                    if units:
                        print(f"  {prop_id} = {label} ({units})")
                    else:
                        print(f"  {prop_id} = {label}")
                    count += 1
            print(f"  ... ({len(props)} total properties)")


if __name__ == "__main__":
    main()
