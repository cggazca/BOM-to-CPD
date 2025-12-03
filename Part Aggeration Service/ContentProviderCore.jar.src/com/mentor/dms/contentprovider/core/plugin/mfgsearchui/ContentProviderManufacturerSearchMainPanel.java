package com.mentor.dms.contentprovider.core.plugin.mfgsearchui;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import com.mentor.datafusion.oi.OICursor;
import com.mentor.datafusion.oi.OIException;
import com.mentor.datafusion.oi.OIObjectManager;
import com.mentor.datafusion.oi.OIQuery;
import com.mentor.datafusion.oi.type.OIObject;
import com.mentor.datafusion.utils.logger.MGLogger;
import com.mentor.dms.contentprovider.core.AbstractContentProvider;
import com.mentor.dms.contentprovider.core.ContentProviderException;
import com.mentor.dms.contentprovider.core.ContentProviderGlobal;
import com.mentor.dms.contentprovider.core.sync.ContentProviderSync;
import com.mentor.dms.contentprovider.core.sync.ContentProviderSyncException;
import com.mentor.dms.ui.searchmask.NoSearchMaskFoundException;
import com.mentor.dms.ui.searchmask.SearchMask;
import com.mentor.dms.ui.searchmask.SearchMaskManager;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXStatusBar;
import org.jdesktop.swingx.plaf.LookAndFeelAddons;
import org.jdesktop.swingx.plaf.windows.WindowsClassicLookAndFeelAddons;

public class ContentProviderManufacturerSearchMainPanel extends JXPanel implements ActionListener {
  private static MGLogger log = MGLogger.getLogger(ContentProviderManufacturerSearchMainPanel.class);
  
  private HashSet<String> existingMfgSet = new HashSet<>();
  
  private boolean bSearchEnabled = false;
  
  private JTextField txtFieldMfg;
  
  private JButton btnSearch;
  
  private JButton btnCreate;
  
  private JButton btnClose;
  
  private JLabel statusLabel;
  
  private ContentProviderManufacturerSearchDlg dlg;
  
  private AbstractContentProvider ccp;
  
  private ContentProviderManufacturerSearchResultsTableModel tm;
  
  private ContentProviderManufacturerSearchResultsJXTable table;
  
  public ContentProviderManufacturerSearchMainPanel(ContentProviderManufacturerSearchDlg paramContentProviderManufacturerSearchDlg, AbstractContentProvider paramAbstractContentProvider) {
    this.dlg = paramContentProviderManufacturerSearchDlg;
    this.ccp = paramAbstractContentProvider;
    try {
      setLayout(new BorderLayout());
      setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
      JPanel jPanel1 = new JPanel();
      jPanel1.setBorder(new EmptyBorder(10, 10, 20, 10));
      jPanel1.setLayout((LayoutManager)new FormLayout(new ColumnSpec[] { FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow") }, new RowSpec[] { FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC }));
      byte b = 2;
      ImageIcon imageIcon = new ImageIcon(ContentProviderManufacturerSearchMainPanel.class.getResource("images/search_catalog.png"));
      this.btnSearch = new JButton("Search", imageIcon);
      this.btnSearch.setActionCommand("Search");
      this.btnSearch.addActionListener(this);
      this.btnSearch.setPreferredSize(new Dimension(100, 24));
      this.btnSearch.setToolTipText("Search Content Provider");
      paramContentProviderManufacturerSearchDlg.getRootPane().setDefaultButton(this.btnSearch);
      jPanel1.add(this.btnSearch, "2, " + b + ", left, default");
      b += 2;
      JLabel jLabel = new JLabel("Manufacturer:");
      jPanel1.add(jLabel, "2, " + b + ", right, default");
      this.txtFieldMfg = new JTextField(40);
      jPanel1.add(this.txtFieldMfg, "4, " + b + ", left, default");
      JPanel jPanel2 = new JPanel();
      jPanel2.setLayout(new BoxLayout(jPanel2, 1));
      jPanel2.setBorder(new EmptyBorder(5, 5, 5, 5));
      imageIcon = new ImageIcon(ContentProviderManufacturerSearchMainPanel.class.getResource("images/mfg.png"));
      this.btnCreate = new JButton("Create...", imageIcon);
      this.btnCreate.setEnabled(false);
      this.btnCreate.setActionCommand("Create");
      this.btnCreate.addActionListener(this);
      this.btnCreate.setPreferredSize(new Dimension(125, 24));
      this.btnCreate.setMinimumSize(new Dimension(125, 24));
      this.btnCreate.setMaximumSize(new Dimension(125, 24));
      jPanel2.add(this.btnCreate);
      jPanel2.add(Box.createRigidArea(new Dimension(0, 10)));
      imageIcon = new ImageIcon(ContentProviderManufacturerSearchMainPanel.class.getResource("images/close_icon.png"));
      this.btnClose = new JButton("Close", imageIcon);
      this.btnClose.setActionCommand("Close");
      this.btnClose.addActionListener(this);
      this.btnClose.setPreferredSize(new Dimension(125, 24));
      this.btnClose.setMinimumSize(new Dimension(125, 24));
      this.btnClose.setMaximumSize(new Dimension(125, 24));
      jPanel2.add(this.btnClose);
      add("East", jPanel2);
      b += 2;
      add("North", jPanel1);
      LookAndFeelAddons.setAddon(WindowsClassicLookAndFeelAddons.class);
      this.tm = new ContentProviderManufacturerSearchResultsTableModel();
      this.table = new ContentProviderManufacturerSearchResultsJXTable(this, this.tm);
      JScrollPane jScrollPane = new JScrollPane((Component)this.table);
      jScrollPane.setAlignmentX(0.0F);
      jScrollPane.setBorder(new CompoundBorder(BorderFactory.createEtchedBorder(1), new EmptyBorder(0, 0, 5, 0)));
      add("Center", jScrollPane);
      JXStatusBar jXStatusBar = new JXStatusBar();
      jXStatusBar.setPreferredSize(new Dimension(10000, 24));
      jXStatusBar.setMaximumSize(new Dimension(10000, 24));
      this.statusLabel = new JLabel("");
      jXStatusBar.add(this.statusLabel);
      add("South", (Component)jXStatusBar);
      this.bSearchEnabled = true;
      readMfgs();
    } catch (Exception exception) {
      log.error(exception);
      JOptionPane.showMessageDialog(null, exception.getMessage());
    } 
  }
  
  public void actionPerformed(ActionEvent paramActionEvent) {
    if (paramActionEvent.getActionCommand().equals("Search")) {
      try {
        doSearch();
        this.btnCreate.setEnabled(false);
      } catch (ContentProviderException contentProviderException) {
        log.error(contentProviderException);
      } 
    } else if (paramActionEvent.getActionCommand().equals("Create")) {
      int i = JOptionPane.showConfirmDialog(ContentProviderGlobal.getRootFrame(), "Create Manufacturer '" + this.tm.getManufacturerAt(this.table.getSelectedRow()) + "'?", "Create Manufacturer", 0);
      if (i == 0)
        createSelectedMfg(); 
    } else if (paramActionEvent.getActionCommand().equals("Close")) {
      this.dlg.saveWindowPrefs();
      this.dlg.setVisible(false);
      this.dlg.dispose();
    } 
  }
  
  public void doSearch() throws ContentProviderException {
    if (!isSearchEnabled())
      return; 
    String str = this.txtFieldMfg.getText().trim();
    if (str.length() < 2) {
      JOptionPane.showMessageDialog(ContentProviderGlobal.getRootFrame(), "Please enter two or more characters for the Manufacturer search string.");
      return;
    } 
    try {
      this.tm.clearManufacturerList();
      setSearchEnabled(false);
      setWaitCursor(true);
      for (String str1 : this.ccp.searchManufacturers(str))
        this.tm.addManufacturer(str1); 
      this.tm.fireTableDataChanged();
      setStatus("" + this.tm.getRowCount() + " results returned.");
    } catch (Exception exception) {
      setStatus(exception.getMessage());
    } finally {
      setSearchEnabled(true);
      setWaitCursor(false);
    } 
  }
  
  public void setStatus(String paramString) {
    this.statusLabel.setText(paramString);
  }
  
  public void setSearchEnabled(boolean paramBoolean) {
    this.bSearchEnabled = paramBoolean;
    this.btnSearch.setEnabled(paramBoolean);
    this.btnClose.setEnabled(paramBoolean);
  }
  
  public boolean isSearchEnabled() {
    return this.bSearchEnabled;
  }
  
  public void setWaitCursor(boolean paramBoolean) {
    setCursor(paramBoolean ? new Cursor(3) : null);
  }
  
  public AbstractContentProvider getContentProvider() {
    return this.ccp;
  }
  
  public ContentProviderManufacturerSearchResultsTableModel getTableModel() {
    return this.tm;
  }
  
  public ContentProviderManufacturerSearchResultsJXTable getTable() {
    return this.table;
  }
  
  public void setSelected() {
    String str = this.tm.getManufacturerAt(this.table.getSelectedRow());
    this.btnCreate.setEnabled(!isExistingMfg(str));
  }
  
  private void readMfgs() {
    OIObjectManager oIObjectManager = ContentProviderGlobal.getOIObjectManager();
    try {
      setCursor(new Cursor(3));
      OIQuery oIQuery = oIObjectManager.createQuery("Manufacturer", true);
      oIQuery.addColumn("ManufacturerName");
      OICursor oICursor = oIQuery.execute();
      while (oICursor.next())
        this.existingMfgSet.add(oICursor.getString("ManufacturerName")); 
      oICursor.close();
    } catch (OIException oIException) {
      log.error(oIException.getMessage());
    } finally {
      setCursor(null);
    } 
  }
  
  private void createSelectedMfg() {
    OIObjectManager oIObjectManager = ContentProviderGlobal.getOIObjectManager();
    try {
      setCursor(new Cursor(3));
      String str = this.tm.getManufacturerAt(this.table.getSelectedRow());
      try {
        OIObject oIObject = ContentProviderSync.createManufacturer(oIObjectManager, this.ccp, null, str);
        try {
          SearchMaskManager searchMaskManager = ContentProviderGlobal.getDMSInstance().getSearchMaskManager();
          SearchMask searchMask = searchMaskManager.open(oIObject.getOIClass());
        } catch (NoSearchMaskFoundException noSearchMaskFoundException) {}
        ContentProviderGlobal.getDMSInstance().getObjectPanelManager().showObject(oIObject);
        JOptionPane.showMessageDialog(ContentProviderGlobal.getRootFrame(), "Manufacturer with name '" + str + "' has been created and opened in EDM Library Cockpit.");
      } catch (ContentProviderSyncException contentProviderSyncException) {
        log.error(contentProviderSyncException.getMessage());
      } 
    } finally {
      setCursor(null);
    } 
  }
  
  protected boolean isExistingMfg(String paramString) {
    return this.existingMfgSet.contains(paramString);
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\plugin\mfgsearchui\ContentProviderManufacturerSearchMainPanel.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */