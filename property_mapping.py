"""
PAS Property ID Mappings

Maps PAS API property IDs to human-readable names and DBC field names.
Property IDs discovered from:
- MappingUpgradeSettings.properties
- SupplyPropertyEnum.java
- ContentProviderImpl.java
- ListView.properties
"""

# ============================================================================
# CORE PAS PROPERTIES - Used for part identification
# ============================================================================

# Core part identification (from MappingUpgradeSettings.properties)
PAS_PROPERTY_PART_ID = "e1aa6f26"           # DataProviderID / Part ID
PAS_PROPERTY_MANUFACTURER_NAME = "6230417e"  # Manufacturer Name
PAS_PROPERTY_MANUFACTURER_PN = "d8ac8dcc"    # Manufacturer Part Number
PAS_PROPERTY_DESCRIPTION = "bf4dd752"        # Description

# Search and display properties (from ContentProviderImpl.java)
PAS_PROPERTY_ROOT_CLASS = "76f2225d"         # Root Part Class ID

# URL properties
PAS_PROPERTY_DATASHEET_URL = "750a45c8"      # Current Datasheet URL (from search)
PAS_PROPERTY_FINDCHIPS_URL = "2a2b1476"      # Part Intelligence URL / Findchips URL

# Lifecycle properties (from Var Industries working implementation)
PAS_PROPERTY_LIFECYCLE_STATUS = "e5434e21"   # Life Cycle Status
PAS_PROPERTY_LIFECYCLE_STATUS_CODE = "a189d244"  # Lifecycle Status Code

# Boolean fields
PAS_PROPERTY_MILITARY_SPEC = "d8739de6"      # Military Spec (boolean)


# ============================================================================
# SUPPLY CHAIN ENRICHER PROPERTIES (ID: 33, Version: 1)
# ============================================================================

# Distributor information
PAS_SUPPLY_DISTRIBUTOR_ID = "951ed6a7"             # Distributor Id
PAS_SUPPLY_DISTRIBUTOR_NAME = "339c3014"           # Distributor Name
PAS_SUPPLY_DISTRIBUTOR_PN = "8f6be867"             # Distributor Part Number
PAS_SUPPLY_AUTHORIZED_STATUS = "08c82fa6"          # Distributor Authorized Status

# Part information from supply chain
PAS_SUPPLY_MANUFACTURER_NAME = "9df8ff36"          # Manufacturer Name (Supply Chain)
PAS_SUPPLY_DESCRIPTION = "bf4dd752"                # Description

# Lifecycle and compliance
PAS_SUPPLY_LIFECYCLE_STATUS = "d3ce1ea0"           # Life Cycle Status
PAS_SUPPLY_ROHS_COMPLIANCE = "41e14b24"            # RoHS Compliance

# Stock and ordering
PAS_SUPPLY_STOCK = "8f6f3508"                      # Stock
PAS_SUPPLY_STOCK_INDICATOR = "3708193e"            # Stock Indicator
PAS_SUPPLY_MIN_ORDER_QTY = "a376c2e6"              # Minimum Order Quantity
PAS_SUPPLY_CONTAINER_TYPE = "1a54a21c"             # Container Type

# Pricing
PAS_SUPPLY_PRICE_BREAKDOWN = "5702a948"            # Price Breakdown
PAS_SUPPLY_ESTIMATED_PRICING = "a0ecfd70"          # Estimated Pricing

# URLs and references
PAS_SUPPLY_DATASHEET_URL = "24538207"              # Datasheet URL
PAS_SUPPLY_BUY_NOW_URL = "db80a0d0"                # Buy Now URL

# Timestamps
PAS_SUPPLY_LAST_UPDATED = "fdd91810"               # Last Updated


# ============================================================================
# ALTERNATES PROPERTIES
# ============================================================================

PAS_ALTERNATES_FFF_EQUIVALENT = "f5724997"         # FFF Equivalent
PAS_ALTERNATES_FUNCTIONAL = "9c89c3f1"             # Functional Equivalent
PAS_ALTERNATES_SIMILAR = "33786d02"                # Similar Alternates
PAS_ALTERNATES_DIRECT = "06a09139"                 # Direct Alternates


# ============================================================================
# DOCUMENT PROPERTIES
# ============================================================================

PAS_DOCS_ENVIRONMENTAL = "5ebee733"                # Environmental Documents
PAS_DOCS_NOTICE = "a6e23430"                       # Notice Documents


# ============================================================================
# CATEGORY-SPECIFIC PARAMETRIC PROPERTIES (from SF-Mapping.xml)
# ============================================================================

# Resistors parametric properties
PAS_RESISTOR_RESISTANCE = "ccea073f"               # Resistance (Ω) - "Value" in DBC
PAS_RESISTOR_TOLERANCE = "a257f04d"                # Tolerance (%)
PAS_RESISTOR_POWER = "b734f175"                    # Rated Power Dissipation (P) (W)
PAS_RESISTOR_VOLTAGE = "0f30e63e"                  # Working Voltage (V)
PAS_RESISTOR_TYPE = "a823a726"                     # Resistor Type
PAS_RESISTOR_SIZE_CODE = "6f96199e"                # Size Code

# Capacitors parametric properties
PAS_CAPACITOR_CAPACITANCE = "efb047cd"             # Capacitance (µF) - "Value" in DBC
PAS_CAPACITOR_VOLTAGE = "12377564"                 # Rated (DC) Voltage (URdc) (V)
PAS_CAPACITOR_POS_TOLERANCE = "05436dcc"           # Positive Tolerance (%)
PAS_CAPACITOR_NEG_TOLERANCE = "748f6c85"           # Negative Tolerance (%)
PAS_CAPACITOR_TYPE = "c1501836"                    # Capacitor Type
PAS_CAPACITOR_DIELECTRIC = "49249462"              # Dielectric Material
PAS_CAPACITOR_SIZE_CODE = "6f96199e"               # Size Code

# Inductors parametric properties
PAS_INDUCTOR_INDUCTANCE = "f7ae1e75"               # Inductance-Nom (L) (µH) - "Value" in DBC
PAS_INDUCTOR_CURRENT = "fc931903"                  # Rated Current-Max (A)
PAS_INDUCTOR_DC_RESISTANCE = "ec01669a"            # DC Resistance (Ω)
PAS_INDUCTOR_TOLERANCE = "a257f04d"                # Tolerance (%)
PAS_INDUCTOR_SIZE_CODE = "a4677781"                # Case/Size Code

# Diodes parametric properties
PAS_DIODE_TYPE = "68ddced3"                        # Diode Type
PAS_DIODE_CONFIGURATION = "55daf8f5"               # Configuration
PAS_DIODE_POWER = "d1a37148"                       # Power Dissipation-Max (W)
PAS_DIODE_REF_VOLTAGE = "a7269fc5"                 # Reference Voltage-Nom (V)
PAS_DIODE_FORWARD_VOLTAGE = "c4a98aa2"             # Forward Voltage-Max (VF) (V)
PAS_DIODE_FORWARD_CURRENT = "dbc7f810"             # Forward Current-Max (A)
PAS_DIODE_REVERSE_VOLTAGE = "1aa66be0"             # Reverse Voltage-Max (V)
PAS_DIODE_BREAKDOWN_VOLTAGE = "72b02506"           # Breakdown Voltage-Nom (V)

# Transistors parametric properties
PAS_TRANSISTOR_VCE_MAX = "5e887a5f"                # Collector-Emitter Voltage-Max (V)
PAS_TRANSISTOR_IC_MAX = "b4fef04d"                 # Collector Current-Max (IC) (A)
PAS_TRANSISTOR_POWER = "baf6f251"                  # Power Dissipation-Max (Abs) (W)
PAS_TRANSISTOR_POWER_AMBIENT = "1c5e34bb"          # Power Dissipation Ambient-Max (W)
PAS_TRANSISTOR_VDS_MIN = "7ee81777"                # DS Breakdown Voltage-Min (V)
PAS_TRANSISTOR_ID_MAX = "3b360da1"                 # Drain Current-Max (Abs) (ID) (A)
PAS_TRANSISTOR_RDS_ON = "5bb59154"                 # Drain-source On Resistance-Max (Ω)
PAS_TRANSISTOR_HFE_MIN = "82b890fd"                # DC Current Gain-Min (hFE)

# Common packaging properties
PAS_SURFACE_MOUNT = "04593027"                     # Surface Mount (boolean)
PAS_PACKAGE_STYLE = "1265b003"                     # Package Style
PAS_PACKAGE_DESCRIPTION = "673e3550"               # Package Description
PAS_PACKAGE_HEIGHT = "971228a0"                    # Package Height (mm)
PAS_PACKAGE_LENGTH = "5208b061"                    # Package Length (mm)
PAS_PACKAGE_WIDTH = "611aade2"                     # Package Width (mm)


# ============================================================================
# PROPERTY ID TO NAME MAPPINGS
# ============================================================================

PROPERTY_ID_TO_NAME = {
    # Core properties
    "e1aa6f26": "Part ID",
    "6230417e": "Manufacturer Name",
    "d8ac8dcc": "Manufacturer Part Number",
    "bf4dd752": "Description",
    "76f2225d": "Root Part Class ID",
    "750a45c8": "Current Datasheet Url",
    "2a2b1476": "Findchips URL",
    "e5434e21": "Part Life Cycle Code",
    "a189d244": "Status Code",
    "d8739de6": "Military Spec",

    # Resistor properties
    "ccea073f": "Resistance",
    "a257f04d": "Tolerance",
    "b734f175": "Rated Power Dissipation (P)",
    "0f30e63e": "Working Voltage",
    "a823a726": "Resistor Type",

    # Capacitor properties
    "efb047cd": "Capacitance",
    "12377564": "Rated (DC) Voltage (URdc)",
    "05436dcc": "Positive Tolerance",
    "748f6c85": "Negative Tolerance",
    "c1501836": "Capacitor Type",
    "49249462": "Dielectric Material",

    # Inductor properties
    "f7ae1e75": "Inductance-Nom (L)",
    "fc931903": "Rated Current-Max",
    "ec01669a": "DC Resistance",

    # Diode properties
    "68ddced3": "Diode Type",
    "55daf8f5": "Configuration",
    "d1a37148": "Power Dissipation-Max",
    "a7269fc5": "Reference Voltage-Nom",
    "c4a98aa2": "Forward Voltage-Max (VF)",
    "dbc7f810": "Forward Current-Max",
    "1aa66be0": "Reverse Voltage-Max",
    "72b02506": "Breakdown Voltage-Nom",

    # Transistor properties
    "5e887a5f": "Collector-Emitter Voltage-Max",
    "b4fef04d": "Collector Current-Max (IC)",
    "baf6f251": "Power Dissipation-Max (Abs)",
    "1c5e34bb": "Power Dissipation Ambient-Max",
    "7ee81777": "DS Breakdown Voltage-Min",
    "3b360da1": "Drain Current-Max (Abs) (ID)",
    "5bb59154": "Drain-source On Resistance-Max",
    "82b890fd": "DC Current Gain-Min (hFE)",

    # Packaging properties
    "6f96199e": "Size Code",
    "a4677781": "Case/Size Code",
    "04593027": "Surface Mount",
    "1265b003": "Package Style",
    "673e3550": "Package Description",
    "971228a0": "Package Height",
    "5208b061": "Package Length",
    "611aade2": "Package Width",

    # Supply chain properties
    "951ed6a7": "Distributor Id",
    "339c3014": "Distributor Name",
    "8f6be867": "Distributor Part Number",
    "08c82fa6": "Distributor Authorized Status",
    "9df8ff36": "Manufacturer Name (Supply)",
    "41e14b24": "RoHS Compliance",
    "8f6f3508": "Stock",
    "3708193e": "Stock Indicator",
    "a376c2e6": "Minimum Order Quantity",
    "1a54a21c": "Container Type",
    "5702a948": "Price Breakdown",
    "a0ecfd70": "Estimated Pricing",
    "24538207": "Datasheet URL",
    "db80a0d0": "Buy Now URL",
    "fdd91810": "Last Updated",

    # Alternates
    "f5724997": "FFF Equivalent",
    "9c89c3f1": "Functional Equivalent",
    "33786d02": "Similar Alternates",
    "06a09139": "Direct Alternates",

    # Documents
    "5ebee733": "Environmental Documents",
    "a6e23430": "Notice Documents",
}


# ============================================================================
# DBC FIELD NAME TO PROPERTY ID MAPPINGS
# ============================================================================

# Common DBC fields that apply to all categories
DBC_COMMON_FIELDS = {
    "Manufacturer Name": PAS_PROPERTY_MANUFACTURER_NAME,
    "Manufacturer Part Number": PAS_PROPERTY_MANUFACTURER_PN,
    "Description": PAS_PROPERTY_DESCRIPTION,
    "Current Datasheet Url": PAS_PROPERTY_DATASHEET_URL,
    "Part Life Cycle Code": PAS_PROPERTY_LIFECYCLE_STATUS,
}

# Category-specific DBC field to PAS property ID mappings
DBC_CATEGORY_FIELD_MAPPINGS = {
    "Resistors": {
        # Common fields
        **DBC_COMMON_FIELDS,
        # Parametric fields
        "Value": PAS_RESISTOR_RESISTANCE,                    # Resistance -> Value
        "Tolerance": PAS_RESISTOR_TOLERANCE,
        "Rated Power Dissipation (P)": PAS_RESISTOR_POWER,
        "Size Code": PAS_RESISTOR_SIZE_CODE,
    },
    "Capacitors": {
        **DBC_COMMON_FIELDS,
        "Value": PAS_CAPACITOR_CAPACITANCE,                  # Capacitance -> Value
        "Positive Tolerance": PAS_CAPACITOR_POS_TOLERANCE,
        "Rated (DC) Voltage (URdc)": PAS_CAPACITOR_VOLTAGE,
        "Size Code": PAS_CAPACITOR_SIZE_CODE,
    },
    "Inductors": {
        **DBC_COMMON_FIELDS,
        "Value": PAS_INDUCTOR_INDUCTANCE,                    # Inductance -> Value
        "Tolerance": PAS_INDUCTOR_TOLERANCE,
        "Rated Current-Max": PAS_INDUCTOR_CURRENT,
        "DC Resistance": PAS_INDUCTOR_DC_RESISTANCE,
        "Size Code": PAS_INDUCTOR_SIZE_CODE,
    },
    "Diodes": {
        **DBC_COMMON_FIELDS,
        "Diode Type": PAS_DIODE_TYPE,
        "Configuration": PAS_DIODE_CONFIGURATION,
        "Power Dissipation-Max": PAS_DIODE_POWER,
        "Reference Voltage-Nom": PAS_DIODE_REF_VOLTAGE,
    },
    "Transistors": {
        **DBC_COMMON_FIELDS,
        "Collector-Emitter Voltage-Max": PAS_TRANSISTOR_VCE_MAX,
        "Collector Current-Max (IC)": PAS_TRANSISTOR_IC_MAX,
        "Power Dissipation-Max (Abs)": PAS_TRANSISTOR_POWER,
        "DC Current Gain-Min (hFE)": PAS_TRANSISTOR_HFE_MIN,
    },
}

# Legacy mapping for backwards compatibility
DBC_TO_PAS_PROPERTY = DBC_COMMON_FIELDS.copy()


# ============================================================================
# PAS PART CLASS TO DBC CATEGORY MAPPING
# ============================================================================

# Maps PAS part class names/keywords to DBC category table names
PAS_CLASS_TO_DBC_CATEGORY = {
    # Passives
    "resistor": "Resistors",
    "res": "Resistors",
    "capacitor": "Capacitors",
    "cap": "Capacitors",
    "inductor": "Inductors",
    "ferrite": "Inductors",
    "choke": "Inductors",

    # Semiconductors
    "diode": "Diodes",
    "rectifier": "Diodes",
    "zener": "Diodes",
    "schottky": "Diodes",
    "transistor": "Transistors",
    "mosfet": "Transistors",
    "bjt": "Transistors",
    "jfet": "Transistors",
    "igbt": "Transistors",

    # ICs
    "op-amp": "Amplifier Circuits",
    "opamp": "Amplifier Circuits",
    "operational amplifier": "Amplifier Circuits",
    "amplifier": "Amplifier Circuits",
    "comparator": "Amplifier Circuits",
    "microcontroller": "Microcontrollers and Processors",
    "mcu": "Microcontrollers and Processors",
    "processor": "Microcontrollers and Processors",
    "cpu": "Microcontrollers and Processors",
    "fpga": "Programmable Logic",
    "cpld": "Programmable Logic",
    "memory": "Memory",
    "sram": "Memory",
    "dram": "Memory",
    "flash": "Memory",
    "eeprom": "Memory",
    "logic": "Logic",
    "gate": "Logic",
    "flip-flop": "Logic",
    "buffer": "Logic",
    "driver": "Drivers And Interfaces",
    "interface": "Drivers And Interfaces",
    "converter": "Converters",
    "adc": "Converters",
    "dac": "Converters",
    "regulator": "Power Circuits",
    "ldo": "Power Circuits",
    "buck": "Power Circuits",
    "boost": "Power Circuits",
    "power supply": "Power Circuits",
    "pmic": "Power Circuits",

    # Connectors
    "connector": "Connectors",
    "header": "Connectors",
    "socket": "Connectors",
    "jack": "Connectors",
    "plug": "Connectors",
    "receptacle": "Connectors",
    "terminal": "Terminal Blocks",
    "terminal block": "Terminal Blocks",

    # Electromechanical
    "relay": "Relays",
    "switch": "Switches",
    "pushbutton": "Switches",
    "toggle": "Switches",

    # Protection
    "fuse": "Circuit Protection",
    "ptc": "Circuit Protection",
    "tvs": "Circuit Protection",
    "esd": "Circuit Protection",
    "varistor": "Circuit Protection",
    "surge": "Circuit Protection",

    # Other
    "crystal": "Crystals Resonators",
    "oscillator": "Crystals Resonators",
    "resonator": "Crystals Resonators",
    "led": "Optoelectronics",
    "optocoupler": "Optoelectronics",
    "photodiode": "Optoelectronics",
    "display": "Optoelectronics",
    "transformer": "Transformers",
    "sensor": "Sensors Transducers",
    "transducer": "Sensors Transducers",
    "battery": "Batteries",
    "filter": "Filters",
    "emi": "Filters",
    "rfi": "Filters",
}


def get_category_from_description(description: str, part_class: str = "") -> str:
    """
    Determine the DBC category from part description and/or class

    Args:
        description: Part description text from PAS
        part_class: Part class name from PAS (if available)

    Returns:
        DBC category name or None if not determined
    """
    text = f"{description} {part_class}".lower()

    # Check each keyword mapping
    for keyword, category in PAS_CLASS_TO_DBC_CATEGORY.items():
        if keyword in text:
            return category

    return None


def get_default_outputs() -> list:
    """
    Get the default list of property IDs to request from PAS

    Returns:
        List of property IDs
    """
    return [
        PAS_PROPERTY_MANUFACTURER_NAME,
        PAS_PROPERTY_MANUFACTURER_PN,
        PAS_PROPERTY_DATASHEET_URL,
        PAS_PROPERTY_FINDCHIPS_URL,
        PAS_PROPERTY_LIFECYCLE_STATUS,
        PAS_PROPERTY_LIFECYCLE_STATUS_CODE,
        PAS_PROPERTY_PART_ID,
    ]


def get_category_outputs(category: str) -> list:
    """
    Get category-specific property IDs to request from PAS

    Args:
        category: DBC category name (e.g., "Resistors", "Capacitors")

    Returns:
        List of property IDs including base outputs and category-specific ones
    """
    # Start with default outputs
    outputs = get_default_outputs()

    # Add category-specific outputs
    if category == "Resistors":
        outputs.extend([
            PAS_RESISTOR_RESISTANCE,
            PAS_RESISTOR_TOLERANCE,
            PAS_RESISTOR_POWER,
            PAS_RESISTOR_VOLTAGE,
            PAS_RESISTOR_TYPE,
            PAS_RESISTOR_SIZE_CODE,
        ])
    elif category == "Capacitors":
        outputs.extend([
            PAS_CAPACITOR_CAPACITANCE,
            PAS_CAPACITOR_VOLTAGE,
            PAS_CAPACITOR_POS_TOLERANCE,
            PAS_CAPACITOR_NEG_TOLERANCE,
            PAS_CAPACITOR_TYPE,
            PAS_CAPACITOR_DIELECTRIC,
            PAS_CAPACITOR_SIZE_CODE,
        ])
    elif category == "Inductors":
        outputs.extend([
            PAS_INDUCTOR_INDUCTANCE,
            PAS_INDUCTOR_CURRENT,
            PAS_INDUCTOR_DC_RESISTANCE,
            PAS_INDUCTOR_TOLERANCE,
            PAS_INDUCTOR_SIZE_CODE,
        ])
    elif category == "Diodes":
        outputs.extend([
            PAS_DIODE_TYPE,
            PAS_DIODE_CONFIGURATION,
            PAS_DIODE_POWER,
            PAS_DIODE_REF_VOLTAGE,
            PAS_DIODE_FORWARD_VOLTAGE,
            PAS_DIODE_FORWARD_CURRENT,
            PAS_DIODE_REVERSE_VOLTAGE,
            PAS_DIODE_BREAKDOWN_VOLTAGE,
        ])
    elif category == "Transistors":
        outputs.extend([
            PAS_TRANSISTOR_VCE_MAX,
            PAS_TRANSISTOR_IC_MAX,
            PAS_TRANSISTOR_POWER,
            PAS_TRANSISTOR_POWER_AMBIENT,
            PAS_TRANSISTOR_VDS_MIN,
            PAS_TRANSISTOR_ID_MAX,
            PAS_TRANSISTOR_RDS_ON,
            PAS_TRANSISTOR_HFE_MIN,
        ])

    # Remove duplicates while preserving order
    seen = set()
    unique_outputs = []
    for prop_id in outputs:
        if prop_id not in seen:
            seen.add(prop_id)
            unique_outputs.append(prop_id)

    return unique_outputs


def get_dbc_field_mapping(category: str) -> dict:
    """
    Get the DBC field to PAS property ID mapping for a category

    Args:
        category: DBC category name

    Returns:
        Dictionary mapping DBC field names to PAS property IDs
    """
    if category in DBC_CATEGORY_FIELD_MAPPINGS:
        return DBC_CATEGORY_FIELD_MAPPINGS[category]
    return DBC_COMMON_FIELDS.copy()


def get_supply_chain_outputs() -> list:
    """
    Get the list of supply chain enricher property IDs

    Returns:
        List of property IDs
    """
    return [
        PAS_SUPPLY_DISTRIBUTOR_ID,
        PAS_SUPPLY_DISTRIBUTOR_NAME,
        PAS_SUPPLY_DISTRIBUTOR_PN,
        PAS_SUPPLY_AUTHORIZED_STATUS,
        PAS_SUPPLY_STOCK_INDICATOR,
        PAS_SUPPLY_DESCRIPTION,
        PAS_SUPPLY_DATASHEET_URL,
        PAS_SUPPLY_BUY_NOW_URL,
        PAS_SUPPLY_LIFECYCLE_STATUS,
        PAS_SUPPLY_LAST_UPDATED,
        PAS_SUPPLY_PRICE_BREAKDOWN,
    ]
