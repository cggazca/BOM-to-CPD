"""
Unit tests for property_mapping.py

Tests the PAS property ID mappings and category-to-field mapping functions.
"""
import unittest
import sys
import os

# Add parent directory to path
sys.path.insert(0, os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

from property_mapping import (
    # Core property IDs
    PAS_PROPERTY_MANUFACTURER_NAME,
    PAS_PROPERTY_MANUFACTURER_PN,
    PAS_PROPERTY_DESCRIPTION,
    PAS_PROPERTY_DATASHEET_URL,
    PAS_PROPERTY_LIFECYCLE_STATUS,

    # Category-specific property IDs
    PAS_RESISTOR_RESISTANCE,
    PAS_CAPACITOR_CAPACITANCE,
    PAS_INDUCTOR_INDUCTANCE,
    PAS_DIODE_TYPE,
    PAS_TRANSISTOR_VCE_MAX,
    PAS_CONNECTOR_TYPE,
    PAS_CIRCUIT_PROTECTION_TYPE,

    # Mappings and functions
    DBC_COMMON_FIELDS,
    DBC_CATEGORY_FIELD_MAPPINGS,
    PAS_CLASS_TO_DBC_CATEGORY,
    PROPERTY_ID_TO_NAME,
    get_category_from_description,
    get_default_outputs,
    get_category_outputs,
    get_dbc_field_mapping,
    get_supply_chain_outputs,
)


class TestPropertyConstants(unittest.TestCase):
    """Test that property ID constants are properly defined"""

    def test_core_property_ids_are_hex_strings(self):
        """Core property IDs should be 8-character hex strings"""
        core_props = [
            PAS_PROPERTY_MANUFACTURER_NAME,
            PAS_PROPERTY_MANUFACTURER_PN,
            PAS_PROPERTY_DESCRIPTION,
            PAS_PROPERTY_DATASHEET_URL,
            PAS_PROPERTY_LIFECYCLE_STATUS,
        ]
        for prop_id in core_props:
            self.assertEqual(len(prop_id), 8, f"Property ID {prop_id} should be 8 chars")
            # Should be valid hex
            int(prop_id, 16)

    def test_category_property_ids_are_hex_strings(self):
        """Category-specific property IDs should be 8-character hex strings"""
        cat_props = [
            PAS_RESISTOR_RESISTANCE,
            PAS_CAPACITOR_CAPACITANCE,
            PAS_INDUCTOR_INDUCTANCE,
            PAS_DIODE_TYPE,
            PAS_TRANSISTOR_VCE_MAX,
            PAS_CONNECTOR_TYPE,
            PAS_CIRCUIT_PROTECTION_TYPE,
        ]
        for prop_id in cat_props:
            self.assertEqual(len(prop_id), 8, f"Property ID {prop_id} should be 8 chars")
            int(prop_id, 16)


class TestDBCCommonFields(unittest.TestCase):
    """Test common DBC field mappings"""

    def test_common_fields_exist(self):
        """Common fields should include essential part information"""
        required_fields = [
            "Manufacturer Name",
            "Manufacturer Part Number",
            "Description",
            "Current Datasheet Url",
            "Part Life Cycle Code",
        ]
        for field in required_fields:
            self.assertIn(field, DBC_COMMON_FIELDS, f"Missing common field: {field}")

    def test_common_fields_have_valid_property_ids(self):
        """Common field mappings should have valid property IDs"""
        for field_name, prop_id in DBC_COMMON_FIELDS.items():
            self.assertEqual(len(prop_id), 8, f"Property ID for {field_name} should be 8 chars")
            int(prop_id, 16)


class TestDBCCategoryFieldMappings(unittest.TestCase):
    """Test category-specific DBC field mappings"""

    def test_all_36_categories_mapped(self):
        """Should have mappings for all 36 DBC categories"""
        self.assertEqual(len(DBC_CATEGORY_FIELD_MAPPINGS), 36)

    def test_passive_categories_exist(self):
        """Passive component categories should be mapped"""
        passives = ["Resistors", "Capacitors", "Inductors", "RC Networks", "Filters"]
        for cat in passives:
            self.assertIn(cat, DBC_CATEGORY_FIELD_MAPPINGS, f"Missing category: {cat}")

    def test_semiconductor_categories_exist(self):
        """Semiconductor categories should be mapped"""
        semis = ["Diodes", "Transistors"]
        for cat in semis:
            self.assertIn(cat, DBC_CATEGORY_FIELD_MAPPINGS, f"Missing category: {cat}")

    def test_ic_categories_exist(self):
        """IC categories should be mapped"""
        ics = [
            "Amplifier Circuits", "Logic", "Memory",
            "Microcontrollers and Processors", "Programmable Logic"
        ]
        for cat in ics:
            self.assertIn(cat, DBC_CATEGORY_FIELD_MAPPINGS, f"Missing category: {cat}")

    def test_connector_categories_exist(self):
        """Connector categories should be mapped"""
        connectors = ["Connectors", "Sockets", "Terminal Blocks"]
        for cat in connectors:
            self.assertIn(cat, DBC_CATEGORY_FIELD_MAPPINGS, f"Missing category: {cat}")

    def test_resistors_have_value_field(self):
        """Resistors should map 'Value' to resistance property"""
        resistors = DBC_CATEGORY_FIELD_MAPPINGS["Resistors"]
        self.assertIn("Value", resistors)
        self.assertEqual(resistors["Value"], PAS_RESISTOR_RESISTANCE)

    def test_capacitors_have_value_field(self):
        """Capacitors should map 'Value' to capacitance property"""
        capacitors = DBC_CATEGORY_FIELD_MAPPINGS["Capacitors"]
        self.assertIn("Value", capacitors)
        self.assertEqual(capacitors["Value"], PAS_CAPACITOR_CAPACITANCE)

    def test_inductors_have_value_field(self):
        """Inductors should map 'Value' to inductance property"""
        inductors = DBC_CATEGORY_FIELD_MAPPINGS["Inductors"]
        self.assertIn("Value", inductors)
        self.assertEqual(inductors["Value"], PAS_INDUCTOR_INDUCTANCE)

    def test_categories_include_common_fields(self):
        """Each category mapping should include common fields"""
        for cat_name, mapping in DBC_CATEGORY_FIELD_MAPPINGS.items():
            # Check for essential common fields
            self.assertIn("Manufacturer Name", mapping, f"{cat_name} missing Manufacturer Name")
            self.assertIn("Description", mapping, f"{cat_name} missing Description")


class TestPASClassToCategoryMapping(unittest.TestCase):
    """Test PAS part class to DBC category mapping"""

    def test_passive_keywords_map_correctly(self):
        """Passive component keywords should map to correct categories"""
        self.assertEqual(PAS_CLASS_TO_DBC_CATEGORY["resistor"], "Resistors")
        self.assertEqual(PAS_CLASS_TO_DBC_CATEGORY["capacitor"], "Capacitors")
        self.assertEqual(PAS_CLASS_TO_DBC_CATEGORY["inductor"], "Inductors")

    def test_semiconductor_keywords_map_correctly(self):
        """Semiconductor keywords should map to correct categories"""
        self.assertEqual(PAS_CLASS_TO_DBC_CATEGORY["diode"], "Diodes")
        self.assertEqual(PAS_CLASS_TO_DBC_CATEGORY["transistor"], "Transistors")
        self.assertEqual(PAS_CLASS_TO_DBC_CATEGORY["mosfet"], "Transistors")

    def test_connector_keywords_map_correctly(self):
        """Connector keywords should map to correct categories"""
        self.assertEqual(PAS_CLASS_TO_DBC_CATEGORY["connector"], "Connectors")
        self.assertEqual(PAS_CLASS_TO_DBC_CATEGORY["header"], "Connectors")

    def test_protection_keywords_map_correctly(self):
        """Protection keywords should map to correct categories"""
        self.assertEqual(PAS_CLASS_TO_DBC_CATEGORY["fuse"], "Circuit Protection")
        self.assertEqual(PAS_CLASS_TO_DBC_CATEGORY["tvs"], "Circuit Protection")


class TestGetCategoryFromDescription(unittest.TestCase):
    """Test category detection from part description"""

    def test_resistor_detection(self):
        """Should detect resistors from description"""
        self.assertEqual(get_category_from_description("10K OHM 0603 RESISTOR"), "Resistors")
        self.assertEqual(get_category_from_description("SMD Resistor 100R"), "Resistors")

    def test_capacitor_detection(self):
        """Should detect capacitors from description"""
        self.assertEqual(get_category_from_description("100uF CAPACITOR MLCC"), "Capacitors")
        self.assertEqual(get_category_from_description("Ceramic Cap 10nF"), "Capacitors")

    def test_inductor_detection(self):
        """Should detect inductors from description"""
        self.assertEqual(get_category_from_description("22uH INDUCTOR SMD"), "Inductors")
        self.assertEqual(get_category_from_description("Ferrite Bead 100 Ohm"), "Inductors")

    def test_diode_detection(self):
        """Should detect diodes from description"""
        self.assertEqual(get_category_from_description("1N4148 DIODE"), "Diodes")
        self.assertEqual(get_category_from_description("Schottky Rectifier 3A"), "Diodes")

    def test_transistor_detection(self):
        """Should detect transistors from description"""
        self.assertEqual(get_category_from_description("2N7002 N-CH MOSFET"), "Transistors")
        self.assertEqual(get_category_from_description("NPN Transistor BC547"), "Transistors")

    def test_connector_detection(self):
        """Should detect connectors from description"""
        self.assertEqual(get_category_from_description("USB Type-C Connector"), "Connectors")
        self.assertEqual(get_category_from_description("2.54mm Pin Header"), "Connectors")

    def test_unknown_returns_none(self):
        """Should return None for unknown part types"""
        self.assertIsNone(get_category_from_description("Unknown Part XYZ"))
        self.assertIsNone(get_category_from_description(""))

    def test_uses_part_class_when_provided(self):
        """Should use part class in addition to description"""
        self.assertEqual(
            get_category_from_description("SMD Component", "Capacitors"),
            "Capacitors"
        )


class TestGetDefaultOutputs(unittest.TestCase):
    """Test default output property IDs"""

    def test_returns_list(self):
        """Should return a list"""
        outputs = get_default_outputs()
        self.assertIsInstance(outputs, list)

    def test_includes_essential_properties(self):
        """Should include essential identification properties"""
        outputs = get_default_outputs()
        self.assertIn(PAS_PROPERTY_MANUFACTURER_NAME, outputs)
        self.assertIn(PAS_PROPERTY_MANUFACTURER_PN, outputs)
        self.assertIn(PAS_PROPERTY_DATASHEET_URL, outputs)
        self.assertIn(PAS_PROPERTY_LIFECYCLE_STATUS, outputs)

    def test_all_outputs_are_valid_hex(self):
        """All output IDs should be valid hex strings"""
        outputs = get_default_outputs()
        for prop_id in outputs:
            self.assertEqual(len(prop_id), 8)
            int(prop_id, 16)


class TestGetCategoryOutputs(unittest.TestCase):
    """Test category-specific output property IDs"""

    def test_includes_default_outputs(self):
        """Should include all default outputs"""
        default = get_default_outputs()
        for cat in ["Resistors", "Capacitors", "Connectors"]:
            outputs = get_category_outputs(cat)
            for prop_id in default:
                self.assertIn(prop_id, outputs)

    def test_resistors_include_resistance(self):
        """Resistors should include resistance property"""
        outputs = get_category_outputs("Resistors")
        self.assertIn(PAS_RESISTOR_RESISTANCE, outputs)

    def test_capacitors_include_capacitance(self):
        """Capacitors should include capacitance property"""
        outputs = get_category_outputs("Capacitors")
        self.assertIn(PAS_CAPACITOR_CAPACITANCE, outputs)

    def test_connectors_include_connector_type(self):
        """Connectors should include connector type property"""
        outputs = get_category_outputs("Connectors")
        self.assertIn(PAS_CONNECTOR_TYPE, outputs)

    def test_unknown_category_returns_defaults_only(self):
        """Unknown category should return only default outputs"""
        outputs = get_category_outputs("NonExistentCategory")
        default = get_default_outputs()
        self.assertEqual(outputs, default)

    def test_no_duplicate_outputs(self):
        """Output list should have no duplicates"""
        for cat in DBC_CATEGORY_FIELD_MAPPINGS.keys():
            outputs = get_category_outputs(cat)
            self.assertEqual(len(outputs), len(set(outputs)), f"{cat} has duplicate outputs")


class TestGetDBCFieldMapping(unittest.TestCase):
    """Test DBC field to property ID mapping retrieval"""

    def test_returns_dict(self):
        """Should return a dictionary"""
        mapping = get_dbc_field_mapping("Resistors")
        self.assertIsInstance(mapping, dict)

    def test_known_category_returns_full_mapping(self):
        """Known category should return full field mapping"""
        mapping = get_dbc_field_mapping("Resistors")
        self.assertIn("Value", mapping)
        self.assertIn("Manufacturer Name", mapping)

    def test_unknown_category_returns_common_fields(self):
        """Unknown category should return common fields only"""
        mapping = get_dbc_field_mapping("NonExistentCategory")
        self.assertEqual(set(mapping.keys()), set(DBC_COMMON_FIELDS.keys()))

    def test_mapping_values_are_valid_property_ids(self):
        """All mapped values should be valid property IDs"""
        for cat in DBC_CATEGORY_FIELD_MAPPINGS.keys():
            mapping = get_dbc_field_mapping(cat)
            for field_name, prop_id in mapping.items():
                self.assertEqual(len(prop_id), 8, f"{cat}.{field_name} has invalid ID")


class TestGetSupplyChainOutputs(unittest.TestCase):
    """Test supply chain output property IDs"""

    def test_returns_list(self):
        """Should return a list"""
        outputs = get_supply_chain_outputs()
        self.assertIsInstance(outputs, list)

    def test_returns_non_empty(self):
        """Should return non-empty list"""
        outputs = get_supply_chain_outputs()
        self.assertGreater(len(outputs), 0)

    def test_all_outputs_are_valid_hex(self):
        """All output IDs should be valid hex strings"""
        outputs = get_supply_chain_outputs()
        for prop_id in outputs:
            self.assertEqual(len(prop_id), 8)
            int(prop_id, 16)


class TestPropertyIdToName(unittest.TestCase):
    """Test property ID to name lookup (via SF-Mapping.xml or fallback)"""

    def test_core_properties_have_names(self):
        """Core property IDs should have human-readable names"""
        from property_mapping import get_property_name
        # These should resolve via SF-Mapping.xml or fallback
        self.assertNotEqual(get_property_name(PAS_PROPERTY_MANUFACTURER_NAME), "Unknown")
        self.assertNotEqual(get_property_name(PAS_PROPERTY_MANUFACTURER_PN), "Unknown")
        self.assertNotEqual(get_property_name(PAS_PROPERTY_DESCRIPTION), "Unknown")

    def test_category_properties_have_names(self):
        """Category property IDs should resolve via SF-Mapping.xml"""
        from property_mapping import get_property_name
        # These should resolve via SF-Mapping.xml
        self.assertEqual(get_property_name(PAS_RESISTOR_RESISTANCE), "Resistance")
        self.assertEqual(get_property_name(PAS_CAPACITOR_CAPACITANCE), "Capacitance")
        self.assertEqual(get_property_name(PAS_DIODE_TYPE), "Diode Type")


if __name__ == "__main__":
    unittest.main()
