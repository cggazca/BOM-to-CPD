"""
Unit tests for dbc_parser.py

Tests the DBC XML schema parser that extracts category and field definitions.
"""
import unittest
import sys
import os

# Add parent directory to path
sys.path.insert(0, os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

from dbc_parser import DBCParser, FieldDefinition, CategoryDefinition


class TestFieldDefinition(unittest.TestCase):
    """Test FieldDefinition dataclass"""

    def test_text_field(self):
        """Test creation of text type field"""
        field = FieldDefinition(
            field_name="Part Number",
            att_name="Part Number",
            prop_name="Part Number",
            value_type=1
        )
        self.assertEqual(field.field_name, "Part Number")
        self.assertFalse(field.is_numeric)

    def test_numeric_field(self):
        """Test creation of numeric type field"""
        field = FieldDefinition(
            field_name="Value",
            att_name="Value",
            prop_name="Resistance",
            value_type=2
        )
        self.assertEqual(field.field_name, "Value")
        self.assertTrue(field.is_numeric)

    def test_default_value(self):
        """Test default value handling"""
        field = FieldDefinition(
            field_name="Status",
            att_name="Status",
            prop_name="Status",
            value_type=1,
            default_value="Active"
        )
        self.assertEqual(field.default_value, "Active")


class TestCategoryDefinition(unittest.TestCase):
    """Test CategoryDefinition dataclass"""

    def test_get_field_names(self):
        """Test retrieval of field name list"""
        fields = [
            FieldDefinition("Field1", "Field1", "Field1", 1),
            FieldDefinition("Field2", "Field2", "Field2", 2),
        ]
        cat = CategoryDefinition("TestCategory", "TestTable", fields)

        names = cat.get_field_names()
        self.assertEqual(names, ["Field1", "Field2"])

    def test_get_field_by_name(self):
        """Test field lookup by name"""
        field1 = FieldDefinition("Value", "Value", "Resistance", 2)
        field2 = FieldDefinition("Tolerance", "Tolerance", "Tolerance", 2)
        cat = CategoryDefinition("Resistors", "Resistors", [field1, field2])

        found = cat.get_field_by_name("Value")
        self.assertIsNotNone(found)
        self.assertEqual(found.field_name, "Value")

    def test_get_field_by_name_case_insensitive(self):
        """Test case-insensitive field lookup"""
        field = FieldDefinition("Part Number", "Part Number", "Part Number", 1)
        cat = CategoryDefinition("Test", "Test", [field])

        found = cat.get_field_by_name("part number")
        self.assertIsNotNone(found)

    def test_get_field_by_name_not_found(self):
        """Test field lookup when not found"""
        cat = CategoryDefinition("Test", "Test", [])
        found = cat.get_field_by_name("NonExistent")
        self.assertIsNone(found)


class TestDBCParser(unittest.TestCase):
    """Test DBCParser with actual DBC file"""

    @classmethod
    def setUpClass(cls):
        """Load DBC file once for all tests"""
        script_dir = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
        dbc_path = os.path.join(script_dir, "TemplateLibrary", "TemplateLibrary.dbc")
        if os.path.exists(dbc_path):
            cls.parser = DBCParser(dbc_path)
            cls.dbc_exists = True
        else:
            cls.dbc_exists = False

    def test_dbc_file_loaded(self):
        """Test that DBC file was loaded"""
        if not self.dbc_exists:
            self.skipTest("DBC file not found")
        self.assertIsNotNone(self.parser)

    def test_categories_parsed(self):
        """Test that categories were parsed"""
        if not self.dbc_exists:
            self.skipTest("DBC file not found")
        self.assertGreater(len(self.parser.categories), 0)

    def test_expected_category_count(self):
        """Test expected number of categories"""
        if not self.dbc_exists:
            self.skipTest("DBC file not found")
        # Should have around 36 categories
        self.assertGreaterEqual(len(self.parser.categories), 30)

    def test_resistors_category_exists(self):
        """Test Resistors category is parsed"""
        if not self.dbc_exists:
            self.skipTest("DBC file not found")
        resistors = self.parser.get_category("Resistors")
        self.assertIsNotNone(resistors)
        self.assertEqual(resistors.library_name, "Resistors")

    def test_capacitors_category_exists(self):
        """Test Capacitors category is parsed"""
        if not self.dbc_exists:
            self.skipTest("DBC file not found")
        capacitors = self.parser.get_category("Capacitors")
        self.assertIsNotNone(capacitors)

    def test_connectors_category_exists(self):
        """Test Connectors category is parsed"""
        if not self.dbc_exists:
            self.skipTest("DBC file not found")
        connectors = self.parser.get_category("Connectors")
        self.assertIsNotNone(connectors)

    def test_get_category_case_insensitive(self):
        """Test case-insensitive category lookup"""
        if not self.dbc_exists:
            self.skipTest("DBC file not found")
        resistors = self.parser.get_category("resistors")
        self.assertIsNotNone(resistors)

    def test_get_all_categories(self):
        """Test getting list of all category names"""
        if not self.dbc_exists:
            self.skipTest("DBC file not found")
        categories = self.parser.get_all_categories()
        self.assertIsInstance(categories, list)
        self.assertIn("Resistors", categories)
        self.assertIn("Capacitors", categories)

    def test_resistors_has_value_field(self):
        """Test Resistors has Value field for resistance"""
        if not self.dbc_exists:
            self.skipTest("DBC file not found")
        resistors = self.parser.get_category("Resistors")
        value_field = resistors.get_field_by_name("Value")
        self.assertIsNotNone(value_field)
        self.assertTrue(value_field.is_numeric)

    def test_resistors_has_part_number_field(self):
        """Test Resistors has Part Number field"""
        if not self.dbc_exists:
            self.skipTest("DBC file not found")
        resistors = self.parser.get_category("Resistors")
        pn_field = resistors.get_field_by_name("Part Number")
        self.assertIsNotNone(pn_field)
        self.assertFalse(pn_field.is_numeric)

    def test_resistors_has_manufacturer_name_field(self):
        """Test Resistors has Manufacturer Name field"""
        if not self.dbc_exists:
            self.skipTest("DBC file not found")
        resistors = self.parser.get_category("Resistors")
        mfg_field = resistors.get_field_by_name("Manufacturer Name")
        self.assertIsNotNone(mfg_field)

    def test_capacitors_has_voltage_field(self):
        """Test Capacitors has voltage rating field"""
        if not self.dbc_exists:
            self.skipTest("DBC file not found")
        capacitors = self.parser.get_category("Capacitors")
        voltage_field = capacitors.get_field_by_name("Rated (DC) Voltage (URdc)")
        self.assertIsNotNone(voltage_field)
        self.assertTrue(voltage_field.is_numeric)


class TestDBCParserFindCategory(unittest.TestCase):
    """Test category detection from part description"""

    @classmethod
    def setUpClass(cls):
        """Load DBC file once for all tests"""
        script_dir = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
        dbc_path = os.path.join(script_dir, "TemplateLibrary", "TemplateLibrary.dbc")
        if os.path.exists(dbc_path):
            cls.parser = DBCParser(dbc_path)
            cls.dbc_exists = True
        else:
            cls.dbc_exists = False

    def test_find_resistor_category(self):
        """Test finding Resistors from description"""
        if not self.dbc_exists:
            self.skipTest("DBC file not found")
        cat = self.parser.find_category_for_part("10K OHM 0603 RESISTOR")
        self.assertEqual(cat, "Resistors")

    def test_find_capacitor_category(self):
        """Test finding Capacitors from description"""
        if not self.dbc_exists:
            self.skipTest("DBC file not found")
        cat = self.parser.find_category_for_part("100uF MLCC CAPACITOR")
        self.assertEqual(cat, "Capacitors")

    def test_find_inductor_category(self):
        """Test finding Inductors from description"""
        if not self.dbc_exists:
            self.skipTest("DBC file not found")
        cat = self.parser.find_category_for_part("22uH SMD INDUCTOR")
        self.assertEqual(cat, "Inductors")

    def test_find_diode_category(self):
        """Test finding Diodes from description"""
        if not self.dbc_exists:
            self.skipTest("DBC file not found")
        cat = self.parser.find_category_for_part("1N4148 SIGNAL DIODE")
        self.assertEqual(cat, "Diodes")

    def test_find_transistor_category(self):
        """Test finding Transistors from MOSFET description"""
        if not self.dbc_exists:
            self.skipTest("DBC file not found")
        cat = self.parser.find_category_for_part("2N7002 N-CH MOSFET 60V")
        self.assertEqual(cat, "Transistors")

    def test_find_connector_category(self):
        """Test finding Connectors from description"""
        if not self.dbc_exists:
            self.skipTest("DBC file not found")
        cat = self.parser.find_category_for_part("USB Type-C Connector 24-pin")
        self.assertEqual(cat, "Connectors")

    def test_find_led_in_optoelectronics(self):
        """Test finding LEDs in Optoelectronics"""
        if not self.dbc_exists:
            self.skipTest("DBC file not found")
        cat = self.parser.find_category_for_part("Red LED 0603 SMD")
        self.assertEqual(cat, "Optoelectronics")

    def test_find_crystal_category(self):
        """Test finding Crystals from description"""
        if not self.dbc_exists:
            self.skipTest("DBC file not found")
        cat = self.parser.find_category_for_part("16MHz CRYSTAL HC-49")
        self.assertEqual(cat, "Crystals Resonators")

    def test_find_fuse_in_circuit_protection(self):
        """Test finding Fuses in Circuit Protection"""
        if not self.dbc_exists:
            self.skipTest("DBC file not found")
        cat = self.parser.find_category_for_part("3A FUSE SMD 0603")
        self.assertEqual(cat, "Circuit Protection")

    def test_unknown_part_returns_none(self):
        """Test unknown part returns None"""
        if not self.dbc_exists:
            self.skipTest("DBC file not found")
        cat = self.parser.find_category_for_part("Unknown Random Component XYZ")
        self.assertIsNone(cat)

    def test_uses_part_class(self):
        """Test using part class for categorization"""
        if not self.dbc_exists:
            self.skipTest("DBC file not found")
        cat = self.parser.find_category_for_part("SMD Component", "Capacitor")
        self.assertEqual(cat, "Capacitors")


if __name__ == "__main__":
    unittest.main()
