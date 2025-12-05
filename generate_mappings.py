"""
Generate property mappings for all DBC categories by matching DBC field names to PAS property IDs.
"""
import json
import os
from dbc_parser import DBCParser

# Common fields that exist in all categories (don't need category-specific mapping)
COMMON_FIELDS = {
    "Part Number",
    "Part Interface",
    "Cell Name",
    "Status",
    "Description",
    "Part Life Cycle Code",
    "Manufacturer Name",
    "Manufacturer Part Number",
    "System Component",
    "Current Datasheet Url",
}

def normalize_field_name(name: str) -> str:
    """Normalize field name for matching (lowercase, no special chars)"""
    return name.lower().replace("-", " ").replace("(", "").replace(")", "").replace("/", " ").strip()

def main():
    script_dir = os.path.dirname(os.path.abspath(__file__))
    dbc_path = os.path.join(script_dir, "TemplateLibrary", "TemplateLibrary.dbc")
    json_path = os.path.join(script_dir, "sf_mapping_extracted.json")

    # Parse DBC
    parser = DBCParser(dbc_path)

    # Load SF-Mapping extracted JSON
    with open(json_path, 'r') as f:
        sf_data = json.load(f)

    # Invert the global properties: name -> id
    global_name_to_id = {}
    for prop_id, name in sf_data.get("global_properties", {}).items():
        normalized = normalize_field_name(name)
        global_name_to_id[normalized] = prop_id
        global_name_to_id[name.lower()] = prop_id  # Also keep original lowercase

    # Build category property mappings from sf_data
    category_name_to_id = {}
    for category, props in sf_data.get("category_properties", {}).items():
        category_name_to_id[category] = {}
        for prop_id, name in props.items():
            normalized = normalize_field_name(name)
            category_name_to_id[category][normalized] = prop_id
            category_name_to_id[category][name.lower()] = prop_id

    print("=" * 80)
    print("DBC CATEGORY FIELD TO PAS PROPERTY MAPPINGS")
    print("=" * 80)

    all_mappings = {}
    unmapped_fields = {}

    for category_name in sorted(parser.get_all_categories()):
        cat = parser.get_category(category_name)

        # Get parametric fields (exclude common fields)
        parametric_fields = []
        for field in cat.fields:
            if field.field_name not in COMMON_FIELDS:
                parametric_fields.append(field)

        if not parametric_fields:
            continue

        print(f"\n{'='*60}")
        print(f"Category: {category_name}")
        print(f"{'='*60}")

        all_mappings[category_name] = {}
        unmapped = []

        # Get category-specific properties from SF-Mapping
        cat_props = category_name_to_id.get(category_name, {})

        for field in parametric_fields:
            field_name = field.field_name
            normalized = normalize_field_name(field_name)

            # Try to find matching property ID
            prop_id = None

            # First check category-specific
            if normalized in cat_props:
                prop_id = cat_props[normalized]
            elif field_name.lower() in cat_props:
                prop_id = cat_props[field_name.lower()]
            # Then check global
            elif normalized in global_name_to_id:
                prop_id = global_name_to_id[normalized]
            elif field_name.lower() in global_name_to_id:
                prop_id = global_name_to_id[field_name.lower()]

            if prop_id:
                all_mappings[category_name][field_name] = prop_id
                type_str = "real" if field.is_numeric else "text"
                print(f"  [OK] {field_name} ({type_str}) -> {prop_id}")
            else:
                unmapped.append(field_name)
                print(f"  [??] {field_name} - NO MATCH FOUND")

        if unmapped:
            unmapped_fields[category_name] = unmapped

    # Print summary
    print("\n" + "=" * 80)
    print("SUMMARY")
    print("=" * 80)
    print(f"Total categories with parametric fields: {len(all_mappings)}")

    if unmapped_fields:
        print(f"\nCategories with unmapped fields:")
        for cat, fields in unmapped_fields.items():
            print(f"  {cat}: {fields}")

    # Generate Python code
    print("\n" + "=" * 80)
    print("GENERATED PYTHON CODE FOR property_mapping.py")
    print("=" * 80)

    print("""
# ============================================================================
# ALL CATEGORY-SPECIFIC DBC FIELD MAPPINGS (Auto-generated)
# ============================================================================

DBC_CATEGORY_FIELD_MAPPINGS = {""")

    for category in sorted(all_mappings.keys()):
        mappings = all_mappings[category]
        if mappings:
            print(f'    "{category}": {{')
            print('        **DBC_COMMON_FIELDS,')
            for field_name, prop_id in sorted(mappings.items()):
                print(f'        "{field_name}": "{prop_id}",')
            print('    },')

    print("}")

    # Generate get_category_outputs function
    print("""

def get_category_outputs(category: str) -> list:
    \"\"\"Get category-specific property IDs to request from PAS API\"\"\"
    outputs = get_default_outputs()

    if category in DBC_CATEGORY_FIELD_MAPPINGS:
        # Get all property IDs for this category
        for field_name, prop_id in DBC_CATEGORY_FIELD_MAPPINGS[category].items():
            if prop_id not in outputs:
                outputs.append(prop_id)

    return outputs
""")

if __name__ == "__main__":
    main()
