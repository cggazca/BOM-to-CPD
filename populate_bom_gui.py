"""
BOM to CPD Population Tool - PyQt GUI

A graphical user interface for the BOM to CPD population workflow.
Provides file selection, authentication, progress tracking, and result display.
"""

import os
import sys
import json
import logging
from datetime import datetime
from typing import Optional

from PyQt5.QtWidgets import (
    QApplication, QMainWindow, QWidget, QVBoxLayout, QHBoxLayout,
    QGroupBox, QLabel, QLineEdit, QPushButton, QFileDialog,
    QTextEdit, QProgressBar, QSpinBox, QCheckBox, QTabWidget,
    QStatusBar, QMessageBox, QFrame, QSplitter, QComboBox,
    QTableWidget, QTableWidgetItem, QHeaderView, QMenuBar,
    QMenu, QAction, QDialog, QDialogButtonBox
)
from PyQt5.QtCore import Qt, QThread, pyqtSignal, QTimer
from PyQt5.QtGui import QFont, QIcon, QColor, QPalette, QBrush

# Add parent directory to path for imports
sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))

from populate_bom import PASClient, BOMParser, CPDUpdater, extract_part_data
from dbc_parser import DBCParser
from property_mapping import (
    get_category_from_description, get_category_outputs, DBC_CATEGORY_FIELD_MAPPINGS,
    PROPERTY_ID_TO_NAME, DBC_COMMON_FIELDS, get_property_name, get_all_property_mappings
)


class LogHandler(logging.Handler):
    """Custom logging handler that emits to a Qt signal"""

    def __init__(self, signal):
        super().__init__()
        self.signal = signal

    def emit(self, record):
        msg = self.format(record)
        self.signal.emit(msg)


class MappingViewerDialog(QDialog):
    """Dialog to view and validate PAS property ID mappings"""

    def __init__(self, parent=None):
        super().__init__(parent)
        self.parent_window = parent
        self.setWindowTitle("Property ID Mapping Viewer")
        self.setMinimumSize(900, 600)
        self.init_ui()
        self.load_mappings()

    def init_ui(self):
        """Initialize the dialog UI"""
        layout = QVBoxLayout(self)

        # Category filter
        filter_layout = QHBoxLayout()
        filter_layout.addWidget(QLabel("Category:"))
        self.category_combo = QComboBox()
        self.category_combo.addItem("All Categories")
        self.category_combo.addItems(sorted(DBC_CATEGORY_FIELD_MAPPINGS.keys()))
        self.category_combo.currentTextChanged.connect(self.filter_mappings)
        filter_layout.addWidget(self.category_combo)
        filter_layout.addStretch()
        layout.addLayout(filter_layout)

        # Mapping table
        self.mapping_table = QTableWidget()
        self.mapping_table.setColumnCount(4)
        self.mapping_table.setHorizontalHeaderLabels([
            "Category", "DBC Field", "PAS Property ID", "Property Name"
        ])
        self.mapping_table.horizontalHeader().setSectionResizeMode(QHeaderView.Stretch)
        self.mapping_table.setAlternatingRowColors(True)
        self.mapping_table.setSortingEnabled(True)
        layout.addWidget(self.mapping_table)

        # Validator section
        validator_group = QGroupBox("Property ID Validator")
        validator_layout = QHBoxLayout(validator_group)

        validator_layout.addWidget(QLabel("Property ID:"))
        self.validate_input = QLineEdit()
        self.validate_input.setPlaceholderText("Enter 8-character hex ID (e.g., f5bd4e8a)")
        self.validate_input.setMaxLength(8)
        self.validate_input.returnPressed.connect(self.validate_property_id)
        validator_layout.addWidget(self.validate_input, stretch=1)

        self.validate_btn = QPushButton("Validate")
        self.validate_btn.clicked.connect(self.validate_property_id)
        validator_layout.addWidget(self.validate_btn)

        self.validate_result = QLabel("")
        self.validate_result.setMinimumWidth(300)
        validator_layout.addWidget(self.validate_result)

        layout.addWidget(validator_group)

        # Button box
        button_layout = QHBoxLayout()

        export_btn = QPushButton("Export Mappings...")
        export_btn.clicked.connect(self.export_mappings)
        button_layout.addWidget(export_btn)

        button_layout.addStretch()

        close_btn = QPushButton("Close")
        close_btn.clicked.connect(self.close)
        button_layout.addWidget(close_btn)

        layout.addLayout(button_layout)

    def load_mappings(self):
        """Load all mappings into the table"""
        self.all_mappings = []

        # Load complete property mappings from SF-Mapping.xml
        all_props = get_all_property_mappings()
        prop_count = len(all_props)

        # Collect all mappings from all categories
        for category, fields in DBC_CATEGORY_FIELD_MAPPINGS.items():
            for dbc_field, prop_id in fields.items():
                prop_name = get_property_name(prop_id)
                self.all_mappings.append({
                    'category': category,
                    'dbc_field': dbc_field,
                    'prop_id': prop_id,
                    'prop_name': prop_name
                })

        # Update window title with count
        self.setWindowTitle(f"Property ID Mapping Viewer ({prop_count} properties loaded)")

        self.filter_mappings("All Categories")

    def filter_mappings(self, category: str):
        """Filter mappings by category"""
        self.mapping_table.setRowCount(0)

        for mapping in self.all_mappings:
            if category != "All Categories" and mapping['category'] != category:
                continue

            row = self.mapping_table.rowCount()
            self.mapping_table.insertRow(row)

            self.mapping_table.setItem(row, 0, QTableWidgetItem(mapping['category']))
            self.mapping_table.setItem(row, 1, QTableWidgetItem(mapping['dbc_field']))

            prop_id_item = QTableWidgetItem(mapping['prop_id'])
            prop_id_item.setFont(QFont("Consolas", 9))
            self.mapping_table.setItem(row, 2, prop_id_item)

            prop_name_item = QTableWidgetItem(mapping['prop_name'])
            if mapping['prop_name'] == "Unknown":
                prop_name_item.setForeground(QBrush(QColor(200, 100, 100)))
            else:
                prop_name_item.setForeground(QBrush(QColor(50, 150, 50)))
            self.mapping_table.setItem(row, 3, prop_name_item)

    def validate_property_id(self):
        """Validate a property ID using SF-Mapping.xml and PAS API"""
        prop_id = self.validate_input.text().strip().lower()

        if not prop_id:
            self.validate_result.setText("Please enter a property ID")
            self.validate_result.setStyleSheet("color: orange;")
            return

        if len(prop_id) != 8:
            self.validate_result.setText("Property ID must be 8 characters")
            self.validate_result.setStyleSheet("color: red;")
            return

        # Check SF-Mapping.xml and static mappings
        prop_name = get_property_name(prop_id)
        if prop_name != "Unknown":
            self.validate_result.setText(f"✓ Known: {prop_name}")
            self.validate_result.setStyleSheet("color: green; font-weight: bold;")
            return

        # Try to validate via PAS API if credentials available
        self.validate_result.setText("Checking PAS API...")
        self.validate_result.setStyleSheet("color: blue;")
        QApplication.processEvents()

        if self.parent_window:
            try:
                # Get credentials from parent window
                if self.parent_window.auth_method.currentIndex() == 0:
                    token = self.parent_window.token_edit.text().strip()
                    if token:
                        pas_client = PASClient(bearer_token=token)
                    else:
                        self.validate_result.setText("⚠ Unknown locally (no API token to verify)")
                        self.validate_result.setStyleSheet("color: orange;")
                        return
                else:
                    client_id = self.parent_window.client_id_edit.text().strip()
                    client_secret = self.parent_window.client_secret_edit.text().strip()
                    if client_id and client_secret:
                        pas_client = PASClient(client_id=client_id, client_secret=client_secret)
                    else:
                        self.validate_result.setText("⚠ Unknown locally (no API credentials to verify)")
                        self.validate_result.setStyleSheet("color: orange;")
                        return

                # Make API call to get property definition
                result = pas_client.get_property_definition(prop_id)
                if result and result.get('name'):
                    prop_name = result.get('name', 'Valid')
                    self.validate_result.setText(f"✓ Valid PAS ID: {prop_name}")
                    self.validate_result.setStyleSheet("color: green; font-weight: bold;")
                else:
                    self.validate_result.setText("✗ Invalid or unknown PAS property ID")
                    self.validate_result.setStyleSheet("color: red;")

            except Exception as e:
                self.validate_result.setText(f"⚠ Unknown locally (API error: {str(e)[:50]})")
                self.validate_result.setStyleSheet("color: orange;")
        else:
            self.validate_result.setText("⚠ Unknown locally (no API connection)")
            self.validate_result.setStyleSheet("color: orange;")

    def export_mappings(self):
        """Export all mappings to CSV"""
        timestamp = datetime.now().strftime("%Y%m%d_%H%M%S")
        default_name = f"property_mappings_{timestamp}.csv"

        file_path, _ = QFileDialog.getSaveFileName(
            self, "Export Mappings",
            default_name,
            "CSV Files (*.csv);;All Files (*)"
        )

        if file_path:
            try:
                with open(file_path, 'w', encoding='utf-8') as f:
                    f.write("Category,DBC Field,PAS Property ID,Property Name\n")
                    for mapping in self.all_mappings:
                        f.write(f'"{mapping["category"]}","{mapping["dbc_field"]}","{mapping["prop_id"]}","{mapping["prop_name"]}"\n')
                QMessageBox.information(self, "Export Complete", f"Mappings exported to:\n{file_path}")
            except Exception as e:
                QMessageBox.critical(self, "Export Error", f"Failed to export:\n{str(e)}")


class WorkerThread(QThread):
    """Background thread for processing BOM"""

    # Signals
    progress = pyqtSignal(int, int)  # current, total
    log_message = pyqtSignal(str)
    part_processed = pyqtSignal(dict)  # part result
    finished = pyqtSignal(dict)  # final results
    error = pyqtSignal(str)

    def __init__(self, config: dict):
        super().__init__()
        self.config = config
        self._is_cancelled = False

    def cancel(self):
        self._is_cancelled = True

    def run(self):
        try:
            self._process_bom()
        except Exception as e:
            self.error.emit(str(e))

    def _process_bom(self):
        config = self.config

        # Initialize components
        self.log_message.emit("Initializing components...")

        dbc_parser = DBCParser(config['dbc_file'])
        self.log_message.emit(f"Loaded {len(dbc_parser.categories)} categories from DBC")

        bom_parser = BOMParser(config['bom_file'])
        parts = bom_parser.parse()
        self.log_message.emit(f"Parsed {len(parts)} parts from BOM")

        # Apply limit if specified
        if config['limit'] > 0:
            parts = parts[:config['limit']]
            self.log_message.emit(f"Limited to first {config['limit']} parts")

        # Initialize PAS client
        pas_client = PASClient(
            client_id=config.get('client_id'),
            client_secret=config.get('client_secret'),
            bearer_token=config.get('token')
        )

        # Initialize CPD updater
        cpd_updater = None
        if not config['dry_run']:
            cpd_updater = CPDUpdater(config['cpd_file'], dbc_parser)
            cpd_updater.connect()
            cpd_updater.ensure_uncategorized_table()
            cpd_updater.backup()
            self.log_message.emit("Created database backup")

        # Track results
        results = {
            'processed': 0,
            'found': 0,
            'not_found': 0,
            'updated': 0,
            'inserted': 0,
            'errors': 0,
            'uncategorized': 0,
            'by_category': {}
        }

        # Process each part
        total = len(parts)
        for i, part in enumerate(parts):
            if self._is_cancelled:
                self.log_message.emit("Processing cancelled by user")
                break

            company_pn = part['company_pn']
            manufacturer = part['manufacturer']
            mpn = part['mpn']

            self.progress.emit(i + 1, total)
            self.log_message.emit(f"[{i+1}/{total}] {company_pn}: {manufacturer} / {mpn}")
            results['processed'] += 1

            # Search PAS
            try:
                pas_result = pas_client.search_part(mpn, manufacturer)
            except Exception as e:
                self.log_message.emit(f"  ERROR: {str(e)}")
                results['errors'] += 1
                self.part_processed.emit({
                    'company_pn': company_pn,
                    'mpn': mpn,
                    'manufacturer': manufacturer,
                    'status': 'Error',
                    'category': '',
                    'match_type': 'Error',
                    'message': str(e)
                })
                continue

            if pas_result.get('found'):
                results['found'] += 1

                # Use category from PAS search result (already detected with full algorithm)
                category = pas_result.get('category')
                if not category:
                    category = "Uncategorized"
                    results['uncategorized'] += 1

                results['by_category'][category] = results['by_category'].get(category, 0) + 1

                # Get matched MPN and Manufacturer from PAS result
                best_match = pas_result.get('best_match', {})
                sp_part = best_match.get('searchProviderPart', {})
                matched_mpn = sp_part.get('manufacturerPartNumber', '')
                matched_mfg = sp_part.get('manufacturerName', '')
                description = sp_part.get('description', '')

                self.log_message.emit(f"  Found! Category: {category}")
                self.log_message.emit(f"  Matched: {matched_mpn}@{matched_mfg}")

                # Do enhanced search with category-specific outputs for parametric data
                enhanced_result = pas_result  # Default to original result
                part_data = {}
                if category in DBC_CATEGORY_FIELD_MAPPINGS:
                    part_class_id = sp_part.get('partClassId', '76f2225d')
                    try:
                        enhanced_result = pas_client.search_part_with_outputs(
                            mpn, manufacturer, get_category_outputs(category), part_class_id
                        )
                        if enhanced_result.get('found'):
                            part_data = extract_part_data(enhanced_result, company_pn, category)
                            # Log parametric data that was extracted
                            parametric_fields = {k: v for k, v in part_data.items()
                                                if k not in ['Part Number', 'Status', 'Manufacturer Part Number',
                                                            'Manufacturer Name', 'Description', 'Part Life Cycle Code',
                                                            'Current Datasheet Url']}
                            if parametric_fields:
                                self.log_message.emit(f"  Parametric data: {parametric_fields}")
                            else:
                                self.log_message.emit(f"  No parametric data returned")
                        else:
                            enhanced_result = pas_result  # Fall back to original
                            part_data = extract_part_data(pas_result, company_pn, category)
                    except Exception as e:
                        self.log_message.emit(f"  Enhanced search failed: {e}")
                        enhanced_result = pas_result
                        part_data = extract_part_data(pas_result, company_pn, category)
                else:
                    part_data = extract_part_data(pas_result, company_pn, category)

                # Build match details for display - show matched MPN@MFG
                total_count = pas_result.get('total_count', 0)
                match_type = pas_result.get('match_type', '')
                match_details = f"{matched_mpn}@{matched_mfg}"
                if total_count > 1:
                    match_details += f" ({total_count} total)"

                # Insert data into database
                if not config['dry_run'] and cpd_updater:
                    if cpd_updater.insert_or_update_part(category, part_data):
                        results['inserted'] += 1
                        self.log_message.emit(f"  Inserted/Updated in {category}")
                    else:
                        results['errors'] += 1
                        self.log_message.emit(f"  Failed to insert")

                self.part_processed.emit({
                    'company_pn': company_pn,
                    'mpn': mpn,
                    'manufacturer': manufacturer,
                    'status': 'Found',
                    'category': category,
                    'match_type': match_type,
                    'message': match_details
                })
            else:
                results['not_found'] += 1
                total_count = pas_result.get('total_count', 0)
                self.log_message.emit(f"  Not found in PAS (searched {total_count} results)")

                self.part_processed.emit({
                    'company_pn': company_pn,
                    'mpn': mpn,
                    'manufacturer': manufacturer,
                    'status': 'Not Found',
                    'category': '',
                    'match_type': 'None',
                    'message': f'No match ({total_count} searched)'
                })

        # Cleanup
        if cpd_updater:
            cpd_updater.close()

        self.finished.emit(results)


class BOMPopulatorGUI(QMainWindow):
    """Main GUI window for BOM to CPD Population Tool"""

    def __init__(self):
        super().__init__()
        self.worker = None
        self.script_dir = os.path.dirname(os.path.abspath(__file__))
        self.init_ui()
        self.load_defaults()

    def init_ui(self):
        """Initialize the user interface"""
        self.setWindowTitle("BOM to CPD Population Tool")
        self.setMinimumSize(900, 700)

        # Create menu bar
        self._create_menu_bar()

        # Central widget
        central_widget = QWidget()
        self.setCentralWidget(central_widget)

        # Main layout
        main_layout = QVBoxLayout(central_widget)
        main_layout.setSpacing(10)
        main_layout.setContentsMargins(10, 10, 10, 10)

        # Create sections
        main_layout.addWidget(self._create_file_section())
        main_layout.addWidget(self._create_auth_section())
        main_layout.addWidget(self._create_options_section())
        main_layout.addWidget(self._create_control_section())
        main_layout.addWidget(self._create_output_section(), stretch=1)

        # Status bar
        self.status_bar = QStatusBar()
        self.setStatusBar(self.status_bar)
        self.status_bar.showMessage("Ready")

    def _create_menu_bar(self):
        """Create the application menu bar"""
        menubar = self.menuBar()

        # File menu
        file_menu = menubar.addMenu("File")

        export_log_action = QAction("Export Log...", self)
        export_log_action.setShortcut("Ctrl+E")
        export_log_action.triggered.connect(self._export_log)
        file_menu.addAction(export_log_action)

        file_menu.addSeparator()

        exit_action = QAction("Exit", self)
        exit_action.setShortcut("Ctrl+Q")
        exit_action.triggered.connect(self.close)
        file_menu.addAction(exit_action)

        # Settings menu
        settings_menu = menubar.addMenu("Settings")

        mapping_action = QAction("Mapping Viewer...", self)
        mapping_action.setShortcut("Ctrl+M")
        mapping_action.triggered.connect(self._show_mapping_viewer)
        settings_menu.addAction(mapping_action)

        # Help menu
        help_menu = menubar.addMenu("Help")

        about_action = QAction("About", self)
        about_action.triggered.connect(self._show_about)
        help_menu.addAction(about_action)

    def _show_mapping_viewer(self):
        """Show the mapping viewer dialog"""
        dialog = MappingViewerDialog(self)
        dialog.exec_()

    def _show_about(self):
        """Show about dialog"""
        QMessageBox.about(
            self,
            "About BOM to CPD Population Tool",
            "<h3>BOM to CPD Population Tool</h3>"
            "<p>Version 1.0</p>"
            "<p>Automates the process of enriching a Bill of Materials (BOM) "
            "with parametric data from the Siemens Part Aggregation Service (PAS) API "
            "and populating a Component Parts Database (CPD).</p>"
            "<p><b>Features:</b></p>"
            "<ul>"
            "<li>SearchAndAssign matching algorithm</li>"
            "<li>36 component categories supported</li>"
            "<li>Automatic category detection</li>"
            "<li>Parametric data extraction</li>"
            "</ul>"
        )

    def _create_file_section(self) -> QGroupBox:
        """Create file selection section"""
        group = QGroupBox("File Selection")
        layout = QVBoxLayout(group)

        # BOM file
        bom_layout = QHBoxLayout()
        bom_layout.addWidget(QLabel("BOM File:"))
        self.bom_edit = QLineEdit()
        self.bom_edit.setPlaceholderText("Select BOM Excel file (.xlsx)")
        bom_layout.addWidget(self.bom_edit, stretch=1)
        bom_btn = QPushButton("Browse...")
        bom_btn.clicked.connect(lambda: self._browse_file(self.bom_edit, "Excel Files (*.xlsx *.xls)"))
        bom_layout.addWidget(bom_btn)
        layout.addLayout(bom_layout)

        # CPD file
        cpd_layout = QHBoxLayout()
        cpd_layout.addWidget(QLabel("CPD File:"))
        self.cpd_edit = QLineEdit()
        self.cpd_edit.setPlaceholderText("Select CPD database file (.cpd)")
        cpd_layout.addWidget(self.cpd_edit, stretch=1)
        cpd_btn = QPushButton("Browse...")
        cpd_btn.clicked.connect(lambda: self._browse_file(self.cpd_edit, "CPD Files (*.cpd);;All Files (*)"))
        cpd_layout.addWidget(cpd_btn)
        layout.addLayout(cpd_layout)

        # DBC file
        dbc_layout = QHBoxLayout()
        dbc_layout.addWidget(QLabel("DBC File:"))
        self.dbc_edit = QLineEdit()
        self.dbc_edit.setPlaceholderText("Select DBC schema file (.dbc)")
        dbc_layout.addWidget(self.dbc_edit, stretch=1)
        dbc_btn = QPushButton("Browse...")
        dbc_btn.clicked.connect(lambda: self._browse_file(self.dbc_edit, "DBC Files (*.dbc);;All Files (*)"))
        dbc_layout.addWidget(dbc_btn)
        layout.addLayout(dbc_layout)

        return group

    def _create_auth_section(self) -> QGroupBox:
        """Create authentication section"""
        group = QGroupBox("PAS API Authentication")
        layout = QVBoxLayout(group)

        # Auth method selector
        method_layout = QHBoxLayout()
        method_layout.addWidget(QLabel("Method:"))
        self.auth_method = QComboBox()
        self.auth_method.addItems(["Bearer Token", "Client Credentials"])
        self.auth_method.currentIndexChanged.connect(self._on_auth_method_changed)
        method_layout.addWidget(self.auth_method)
        method_layout.addStretch()
        layout.addLayout(method_layout)

        # Token input
        token_layout = QHBoxLayout()
        token_layout.addWidget(QLabel("Bearer Token:"))
        self.token_edit = QLineEdit()
        self.token_edit.setEchoMode(QLineEdit.Password)
        self.token_edit.setPlaceholderText("Enter PAS API bearer token")
        token_layout.addWidget(self.token_edit, stretch=1)
        self.show_token_btn = QPushButton("Show")
        self.show_token_btn.setCheckable(True)
        self.show_token_btn.toggled.connect(self._toggle_token_visibility)
        token_layout.addWidget(self.show_token_btn)
        layout.addLayout(token_layout)

        # Client credentials (initially hidden)
        self.creds_widget = QWidget()
        creds_layout = QVBoxLayout(self.creds_widget)
        creds_layout.setContentsMargins(0, 0, 0, 0)

        id_layout = QHBoxLayout()
        id_layout.addWidget(QLabel("Client ID:"))
        self.client_id_edit = QLineEdit()
        self.client_id_edit.setPlaceholderText("Enter PAS API client ID")
        id_layout.addWidget(self.client_id_edit, stretch=1)
        creds_layout.addLayout(id_layout)

        secret_layout = QHBoxLayout()
        secret_layout.addWidget(QLabel("Client Secret:"))
        self.client_secret_edit = QLineEdit()
        self.client_secret_edit.setEchoMode(QLineEdit.Password)
        self.client_secret_edit.setPlaceholderText("Enter PAS API client secret")
        secret_layout.addWidget(self.client_secret_edit, stretch=1)
        creds_layout.addLayout(secret_layout)

        self.creds_widget.setVisible(False)
        layout.addWidget(self.creds_widget)

        return group

    def _create_options_section(self) -> QGroupBox:
        """Create options section"""
        group = QGroupBox("Options")
        layout = QHBoxLayout(group)

        # Dry run checkbox
        self.dry_run_check = QCheckBox("Dry Run (preview only, no database changes)")
        layout.addWidget(self.dry_run_check)

        layout.addSpacing(20)

        # Limit spinner
        layout.addWidget(QLabel("Limit parts:"))
        self.limit_spin = QSpinBox()
        self.limit_spin.setRange(0, 10000)
        self.limit_spin.setValue(0)
        self.limit_spin.setSpecialValueText("No limit")
        self.limit_spin.setToolTip("0 = process all parts")
        layout.addWidget(self.limit_spin)

        layout.addStretch()

        return group

    def _create_control_section(self) -> QWidget:
        """Create control buttons section"""
        widget = QWidget()
        layout = QHBoxLayout(widget)
        layout.setContentsMargins(0, 0, 0, 0)

        # Progress bar
        self.progress_bar = QProgressBar()
        self.progress_bar.setTextVisible(True)
        self.progress_bar.setFormat("%v / %m parts")
        layout.addWidget(self.progress_bar, stretch=1)

        # Buttons
        self.run_btn = QPushButton("Run")
        self.run_btn.setMinimumWidth(100)
        self.run_btn.setStyleSheet("QPushButton { background-color: #4CAF50; color: white; font-weight: bold; }")
        self.run_btn.clicked.connect(self.run_processing)
        layout.addWidget(self.run_btn)

        self.cancel_btn = QPushButton("Cancel")
        self.cancel_btn.setMinimumWidth(100)
        self.cancel_btn.setEnabled(False)
        self.cancel_btn.clicked.connect(self.cancel_processing)
        layout.addWidget(self.cancel_btn)

        return widget

    def _create_output_section(self) -> QWidget:
        """Create output/results section"""
        splitter = QSplitter(Qt.Vertical)

        # Results table
        table_group = QGroupBox("Results")
        table_layout = QVBoxLayout(table_group)

        self.results_table = QTableWidget()
        self.results_table.setColumnCount(7)
        self.results_table.setHorizontalHeaderLabels([
            "Company PN", "MPN", "Manufacturer", "Status", "Category", "Match Type", "Details"
        ])
        self.results_table.horizontalHeader().setSectionResizeMode(QHeaderView.Stretch)
        self.results_table.setAlternatingRowColors(True)
        table_layout.addWidget(self.results_table)

        splitter.addWidget(table_group)

        # Log output
        log_group = QGroupBox("Log")
        log_layout = QVBoxLayout(log_group)

        self.log_text = QTextEdit()
        self.log_text.setReadOnly(True)
        self.log_text.setFont(QFont("Consolas", 9))
        log_layout.addWidget(self.log_text)

        # Log controls
        log_controls = QHBoxLayout()
        clear_log_btn = QPushButton("Clear Log")
        clear_log_btn.clicked.connect(self.log_text.clear)
        log_controls.addWidget(clear_log_btn)
        export_log_btn = QPushButton("Export Log...")
        export_log_btn.clicked.connect(self._export_log)
        log_controls.addWidget(export_log_btn)
        log_controls.addStretch()
        log_layout.addLayout(log_controls)

        splitter.addWidget(log_group)

        # Set initial sizes
        splitter.setSizes([300, 200])

        return splitter

    def _browse_file(self, line_edit: QLineEdit, file_filter: str):
        """Open file browser dialog"""
        file_path, _ = QFileDialog.getOpenFileName(
            self, "Select File", self.script_dir, file_filter
        )
        if file_path:
            line_edit.setText(file_path)

    def _on_auth_method_changed(self, index: int):
        """Handle auth method selection change"""
        is_token = (index == 0)
        self.token_edit.setVisible(is_token)
        self.show_token_btn.setVisible(is_token)
        self.creds_widget.setVisible(not is_token)

    def _toggle_token_visibility(self, checked: bool):
        """Toggle token visibility"""
        self.token_edit.setEchoMode(
            QLineEdit.Normal if checked else QLineEdit.Password
        )
        self.show_token_btn.setText("Hide" if checked else "Show")

    def load_defaults(self):
        """Load default file paths and saved settings"""
        bom_default = os.path.join(self.script_dir, "EV50F63A_MCP19061_Dual_USB_DCP.xlsx")
        cpd_default = os.path.join(self.script_dir, "TemplateLibrary", "TemplateLibrary.cpd")
        dbc_default = os.path.join(self.script_dir, "TemplateLibrary", "TemplateLibrary.dbc")

        if os.path.exists(bom_default):
            self.bom_edit.setText(bom_default)
        if os.path.exists(cpd_default):
            self.cpd_edit.setText(cpd_default)
        if os.path.exists(dbc_default):
            self.dbc_edit.setText(dbc_default)

        # Load saved settings first
        self._load_settings()

        # Override with env vars if available
        client_id = os.environ.get("PAS_CLIENT_ID", "")
        client_secret = os.environ.get("PAS_CLIENT_SECRET", "")
        if client_id:
            self.client_id_edit.setText(client_id)
        if client_secret:
            self.client_secret_edit.setText(client_secret)

    def _get_settings_path(self) -> str:
        """Get the path to the settings file"""
        return os.path.join(self.script_dir, ".bom_gui_settings.json")

    def _load_settings(self):
        """Load saved settings from JSON file"""
        settings_path = self._get_settings_path()
        if os.path.exists(settings_path):
            try:
                with open(settings_path, 'r') as f:
                    settings = json.load(f)

                # Load credentials
                if settings.get('client_id'):
                    self.client_id_edit.setText(settings['client_id'])
                if settings.get('client_secret'):
                    self.client_secret_edit.setText(settings['client_secret'])

                # Load auth method preference
                if settings.get('auth_method') is not None:
                    self.auth_method.setCurrentIndex(settings['auth_method'])
                    self._on_auth_method_changed(settings['auth_method'])

                # Load last used files
                if settings.get('bom_file') and os.path.exists(settings['bom_file']):
                    self.bom_edit.setText(settings['bom_file'])
                if settings.get('cpd_file') and os.path.exists(settings['cpd_file']):
                    self.cpd_edit.setText(settings['cpd_file'])
                if settings.get('dbc_file') and os.path.exists(settings['dbc_file']):
                    self.dbc_edit.setText(settings['dbc_file'])

            except (json.JSONDecodeError, IOError) as e:
                logger.warning(f"Could not load settings: {e}")

    def _save_settings(self):
        """Save current settings to JSON file"""
        settings = {
            'client_id': self.client_id_edit.text(),
            'client_secret': self.client_secret_edit.text(),
            'auth_method': self.auth_method.currentIndex(),
            'bom_file': self.bom_edit.text(),
            'cpd_file': self.cpd_edit.text(),
            'dbc_file': self.dbc_edit.text(),
        }

        try:
            with open(self._get_settings_path(), 'w') as f:
                json.dump(settings, f, indent=2)
        except IOError as e:
            logger.warning(f"Could not save settings: {e}")

    def validate_inputs(self) -> bool:
        """Validate all inputs before processing"""
        errors = []

        # Check files exist
        if not os.path.exists(self.bom_edit.text()):
            errors.append("BOM file not found")
        if not os.path.exists(self.cpd_edit.text()):
            errors.append("CPD file not found")
        if not os.path.exists(self.dbc_edit.text()):
            errors.append("DBC file not found")

        # Check authentication
        if self.auth_method.currentIndex() == 0:  # Bearer token
            if not self.token_edit.text().strip():
                errors.append("Bearer token is required")
        else:  # Client credentials
            if not self.client_id_edit.text().strip():
                errors.append("Client ID is required")
            if not self.client_secret_edit.text().strip():
                errors.append("Client Secret is required")

        if errors:
            QMessageBox.warning(
                self, "Validation Error",
                "Please fix the following errors:\n\n" + "\n".join(f"- {e}" for e in errors)
            )
            return False

        return True

    def run_processing(self):
        """Start the BOM processing"""
        if not self.validate_inputs():
            return

        # Clear previous results
        self.results_table.setRowCount(0)
        self.log_text.clear()
        self.progress_bar.setValue(0)

        # Build config
        config = {
            'bom_file': self.bom_edit.text(),
            'cpd_file': self.cpd_edit.text(),
            'dbc_file': self.dbc_edit.text(),
            'dry_run': self.dry_run_check.isChecked(),
            'limit': self.limit_spin.value(),
        }

        if self.auth_method.currentIndex() == 0:
            config['token'] = self.token_edit.text().strip()
        else:
            config['client_id'] = self.client_id_edit.text().strip()
            config['client_secret'] = self.client_secret_edit.text().strip()

        # Save settings for next time
        self._save_settings()

        # Update UI state
        self.run_btn.setEnabled(False)
        self.cancel_btn.setEnabled(True)
        self.status_bar.showMessage("Processing...")

        # Create and start worker thread
        self.worker = WorkerThread(config)
        self.worker.progress.connect(self._on_progress)
        self.worker.log_message.connect(self._on_log_message)
        self.worker.part_processed.connect(self._on_part_processed)
        self.worker.finished.connect(self._on_finished)
        self.worker.error.connect(self._on_error)
        self.worker.start()

    def cancel_processing(self):
        """Cancel the running process"""
        if self.worker:
            self.worker.cancel()
            self.status_bar.showMessage("Cancelling...")

    def _on_progress(self, current: int, total: int):
        """Handle progress update"""
        self.progress_bar.setMaximum(total)
        self.progress_bar.setValue(current)

    def _on_log_message(self, message: str):
        """Handle log message"""
        timestamp = datetime.now().strftime("%H:%M:%S")
        self.log_text.append(f"[{timestamp}] {message}")
        # Auto-scroll to bottom
        self.log_text.verticalScrollBar().setValue(
            self.log_text.verticalScrollBar().maximum()
        )

    def _on_part_processed(self, result: dict):
        """Handle part processed result"""
        row = self.results_table.rowCount()
        self.results_table.insertRow(row)

        self.results_table.setItem(row, 0, QTableWidgetItem(result['company_pn']))
        self.results_table.setItem(row, 1, QTableWidgetItem(result['mpn']))
        self.results_table.setItem(row, 2, QTableWidgetItem(result['manufacturer']))

        status_item = QTableWidgetItem(result['status'])
        if result['status'] == 'Found':
            status_item.setBackground(QColor(200, 255, 200))
        elif result['status'] == 'Not Found':
            status_item.setBackground(QColor(255, 255, 200))
        elif result['status'] == 'Error':
            status_item.setBackground(QColor(255, 200, 200))
        self.results_table.setItem(row, 3, status_item)

        self.results_table.setItem(row, 4, QTableWidgetItem(result['category']))
        self.results_table.setItem(row, 5, QTableWidgetItem(result.get('match_type', '')))
        self.results_table.setItem(row, 6, QTableWidgetItem(result['message']))

    def _on_finished(self, results: dict):
        """Handle processing finished"""
        self.run_btn.setEnabled(True)
        self.cancel_btn.setEnabled(False)

        # Show summary
        summary = f"""
Processing Complete!

Results:
  Processed: {results['processed']}
  Found: {results['found']}
  Not Found: {results['not_found']}
  Inserted/Updated: {results.get('inserted', 0)}
  Errors: {results['errors']}
  Uncategorized: {results['uncategorized']}

Categories:
"""
        for cat, count in sorted(results.get('by_category', {}).items()):
            summary += f"  {cat}: {count}\n"

        self._on_log_message(summary)
        self.status_bar.showMessage(
            f"Complete: {results['found']} found, {results['not_found']} not found, {results['errors']} errors"
        )

        # Show completion dialog
        QMessageBox.information(
            self, "Processing Complete",
            f"Processed {results['processed']} parts.\n\n"
            f"Found: {results['found']}\n"
            f"Not Found: {results['not_found']}\n"
            f"Errors: {results['errors']}"
        )

    def _on_error(self, error_msg: str):
        """Handle error"""
        self.run_btn.setEnabled(True)
        self.cancel_btn.setEnabled(False)
        self.status_bar.showMessage("Error occurred")

        QMessageBox.critical(self, "Error", f"An error occurred:\n\n{error_msg}")
        self._on_log_message(f"ERROR: {error_msg}")

    def _export_log(self):
        """Export log to a text file"""
        log_content = self.log_text.toPlainText()
        if not log_content.strip():
            QMessageBox.warning(self, "Export Log", "No log content to export.")
            return

        # Generate default filename with timestamp
        timestamp = datetime.now().strftime("%Y%m%d_%H%M%S")
        default_name = f"bom_processing_log_{timestamp}.txt"

        file_path, _ = QFileDialog.getSaveFileName(
            self, "Export Log",
            os.path.join(self.script_dir, default_name),
            "Text Files (*.txt);;All Files (*)"
        )

        if file_path:
            try:
                with open(file_path, 'w', encoding='utf-8') as f:
                    f.write(log_content)
                self.status_bar.showMessage(f"Log exported to {file_path}")
                QMessageBox.information(self, "Export Log", f"Log exported successfully to:\n{file_path}")
            except Exception as e:
                QMessageBox.critical(self, "Export Error", f"Failed to export log:\n{str(e)}")


def main():
    """Main entry point"""
    app = QApplication(sys.argv)

    # Set application style
    app.setStyle("Fusion")

    # Create and show main window
    window = BOMPopulatorGUI()
    window.show()

    sys.exit(app.exec_())


if __name__ == "__main__":
    main()
