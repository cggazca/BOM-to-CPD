"""
DBC Schema Parser

Parses the TemplateLibrary.dbc XML file to extract:
- Component category definitions (LibraryName)
- Field definitions per category with data types
- Table name mappings to SQLite CPD database
"""

import xml.etree.ElementTree as ET
from dataclasses import dataclass
from typing import Dict, List, Optional
import re


@dataclass
class FieldDefinition:
    """Definition of a field in a DBC category"""
    field_name: str          # Database column name
    att_name: str            # Attribute name
    prop_name: str           # Property name
    value_type: int          # 1=text, 2=real
    default_value: str = ""

    @property
    def is_numeric(self) -> bool:
        return self.value_type == 2


@dataclass
class CategoryDefinition:
    """Definition of a component category in DBC"""
    library_name: str        # Category name (e.g., "Resistors")
    table_name: str          # SQLite table name
    fields: List[FieldDefinition]

    def get_field_names(self) -> List[str]:
        """Get list of all field names for this category"""
        return [f.field_name for f in self.fields]

    def get_field_by_name(self, name: str) -> Optional[FieldDefinition]:
        """Get field definition by name (case-insensitive)"""
        name_lower = name.lower()
        for f in self.fields:
            if f.field_name.lower() == name_lower:
                return f
        return None


class DBCParser:
    """Parser for TemplateLibrary.dbc XML configuration files"""

    def __init__(self, dbc_path: str):
        self.dbc_path = dbc_path
        self.categories: Dict[str, CategoryDefinition] = {}
        self._parse()

    def _parse(self):
        """Parse the DBC file and extract all category definitions"""
        tree = ET.parse(self.dbc_path)
        root = tree.getroot()

        # Find all CConfigLibEntry elements (category definitions)
        for elem in root.iter():
            if elem.tag.startswith('CConfigLibEntry'):
                category = self._parse_category(elem)
                if category:
                    self.categories[category.library_name] = category

    def _parse_category(self, elem: ET.Element) -> Optional[CategoryDefinition]:
        """Parse a single category definition"""
        library_name = elem.get('LibraryName')
        if not library_name:
            return None

        fields = []
        table_name = library_name  # Default to library name

        # Parse field definitions from CConfigAttEntry elements
        for child in elem.iter():
            if child.tag.startswith('CConfigAttEntry'):
                field = self._parse_field(child)
                if field:
                    fields.append(field)

            # Get table name from CConfigTableEntry
            if child.tag.startswith('CConfigTableEntry'):
                table_name = child.get('TableName', library_name)

        return CategoryDefinition(
            library_name=library_name,
            table_name=table_name,
            fields=fields
        )

    def _parse_field(self, elem: ET.Element) -> Optional[FieldDefinition]:
        """Parse a single field definition"""
        field_name = elem.get('FieldName')
        if not field_name:
            return None

        return FieldDefinition(
            field_name=field_name,
            att_name=elem.get('AttName', field_name),
            prop_name=elem.get('PropName', field_name),
            value_type=int(elem.get('ValueType', '1')),
            default_value=elem.get('DefaultValue', '')
        )

    def get_category(self, name: str) -> Optional[CategoryDefinition]:
        """Get category by name (case-insensitive)"""
        name_lower = name.lower()
        for cat_name, cat in self.categories.items():
            if cat_name.lower() == name_lower:
                return cat
        return None

    def get_all_categories(self) -> List[str]:
        """Get list of all category names"""
        return list(self.categories.keys())

    def find_category_for_part(self, description: str, part_class: str = "") -> Optional[str]:
        """
        Try to determine the best category for a part based on description and class

        Args:
            description: Part description text
            part_class: Part class from PAS (if available)

        Returns:
            Category name or None if not found
        """
        text = f"{description} {part_class}".lower()

        # Priority mapping - more specific first
        category_keywords = {
            "Resistors": ["resistor", "ohm", "res ", "smd resistor"],
            "Capacitors": ["capacitor", "cap ", "mlcc", "ceramic cap", "electrolytic"],
            "Inductors": ["inductor", "coil", "choke", "ferrite"],
            "Diodes": ["diode", "rectifier", "zener", "schottky", "tvs"],
            "Transistors": ["transistor", "mosfet", "jfet", "bjt", "igbt"],
            "Connectors": ["connector", "header", "socket", "jack", "plug", "receptacle"],
            "Circuit Protection": ["fuse", "ptc", "varistor", "surge", "esd"],
            "Crystals Resonators": ["crystal", "resonator", "oscillator", "xtal"],
            "Optoelectronics": ["led", "optocoupler", "photodiode", "laser"],
            "Power Circuits": ["regulator", "converter", "ldo", "buck", "boost", "power supply"],
            "Amplifier Circuits": ["amplifier", "op-amp", "opamp", "operational"],
            "Logic": ["logic", "gate", "flip-flop", "buffer", "inverter", "latch"],
            "Memory": ["memory", "sram", "dram", "flash", "eeprom", "eprom"],
            "Microcontrollers and Processors": ["microcontroller", "mcu", "processor", "cpu", "arm", "pic", "avr"],
            "Drivers And Interfaces": ["driver", "interface", "rs-232", "rs-485", "can", "uart", "i2c", "spi"],
            "Switches": ["switch", "pushbutton", "toggle", "dip switch"],
            "Relays": ["relay", "contactor"],
            "Transformers": ["transformer", "isolation"],
            "Filters": ["filter", "emi", "rfi", "low pass", "high pass", "band pass"],
            "Terminal Blocks": ["terminal", "screw terminal"],
            "Batteries": ["battery", "cell", "lithium", "coin cell"],
            "Sensors Transducers": ["sensor", "transducer", "thermocouple", "accelerometer"],
        }

        for category, keywords in category_keywords.items():
            for keyword in keywords:
                if keyword in text:
                    if category in self.categories:
                        return category

        return None


def main():
    """Test the DBC parser"""
    import os

    # Get the DBC file path
    script_dir = os.path.dirname(os.path.abspath(__file__))
    dbc_path = os.path.join(script_dir, "TemplateLibrary", "TemplateLibrary.dbc")

    if not os.path.exists(dbc_path):
        print(f"DBC file not found: {dbc_path}")
        return

    parser = DBCParser(dbc_path)

    print("=" * 60)
    print("DBC Schema Summary")
    print("=" * 60)
    print(f"\nFound {len(parser.categories)} categories:\n")

    for name in sorted(parser.get_all_categories()):
        cat = parser.categories[name]
        print(f"  {name} ({len(cat.fields)} fields)")

    # Show details for a few categories
    for cat_name in ["Resistors", "Capacitors", "Diodes"]:
        cat = parser.get_category(cat_name)
        if cat:
            print(f"\n{'=' * 40}")
            print(f"Category: {cat.library_name}")
            print(f"Table: {cat.table_name}")
            print(f"Fields:")
            for f in cat.fields:
                type_str = "real" if f.is_numeric else "text"
                print(f"  - {f.field_name} ({type_str})")


if __name__ == "__main__":
    main()
