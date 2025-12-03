package com.mentor.dms.contentprovider.plugin.partsearchui;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import com.mentor.datafusion.utils.logger.MGLogger;
import com.mentor.dms.contentprovider.AbstractContentProvider;
import com.mentor.dms.contentprovider.AbstractCriteria;
import com.mentor.dms.contentprovider.ContentProviderException;
import com.mentor.dms.contentprovider.IContentProviderResultRecord;
import com.mentor.dms.contentprovider.config.ContentProviderConfigPartClass;
import com.mentor.dms.contentprovider.config.ContentProviderConfigProperty;
import com.mentor.dms.contentprovider.plugin.DMSStringExprLexer;
import com.mentor.dms.contentprovider.plugin.DMSStringExprParser;
import com.mentor.dms.contentprovider.plugin.searchui.ContentProviderSearchResultsColumn;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.util.List;
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
import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.TokenSource;
import org.antlr.runtime.TokenStream;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXStatusBar;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.jdesktop.swingx.plaf.LookAndFeelAddons;
import org.jdesktop.swingx.plaf.windows.WindowsClassicLookAndFeelAddons;
import org.jdesktop.swingx.table.TableColumnExt;

public class ContentProviderPartNumberSearchMainPanel extends JXPanel implements ActionListener {
  private static MGLogger log = MGLogger.getLogger(ContentProviderPartNumberSearchMainPanel.class);
  
  private boolean bSearchEnabled = false;
  
  private JTextField txtFieldPartNumber;
  
  private JTextField txtFieldMfg;
  
  private JButton btnSearch;
  
  private JButton btnSelect;
  
  private JButton btnCancel;
  
  private JButton btnViewOnline;
  
  private JLabel statusLabel;
  
  private ContentProviderPartNumberSearchDlg dlg;
  
  private AbstractContentProvider ccp;
  
  private ContentProviderPartNumberSearchResultsTableModel tm;
  
  private ContentProviderPartNumberSearchResultsJXTable table;
  
  private ContentProviderConfigPartClass partClass;
  
  private ContentProviderConfigProperty partNumberProp;
  
  private ContentProviderConfigProperty mfgProp;
  
  private AbstractCriteria crit;
  
  private IContentProviderResultRecord selectedPart = null;
  
  public ContentProviderPartNumberSearchMainPanel(ContentProviderPartNumberSearchDlg paramContentProviderPartNumberSearchDlg, AbstractContentProvider paramAbstractContentProvider, String paramString1, String paramString2, List<String> paramList) {
    this.dlg = paramContentProviderPartNumberSearchDlg;
    this.ccp = paramAbstractContentProvider;
    try {
      setLayout(new BorderLayout());
      setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
      JPanel jPanel1 = new JPanel();
      jPanel1.setBorder(new EmptyBorder(10, 10, 20, 10));
      jPanel1.setLayout((LayoutManager)new FormLayout(new ColumnSpec[] { FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow") }, new RowSpec[] { FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC }));
      byte b = 2;
      ImageIcon imageIcon = new ImageIcon(ContentProviderPartNumberSearchMainPanel.class.getResource("images/search_catalog.png"));
      this.btnSearch = new JButton("Search", imageIcon);
      this.btnSearch.setActionCommand("Search");
      this.btnSearch.addActionListener(this);
      this.btnSearch.setPreferredSize(new Dimension(100, 24));
      this.btnSearch.setToolTipText("Search Content Provider");
      jPanel1.add(this.btnSearch, "2, " + b + ", right, default");
      b += 2;
      JLabel jLabel = new JLabel("Part Number:");
      jPanel1.add(jLabel, "2, " + b + ", right, default");
      this.txtFieldPartNumber = new JTextField(40);
      this.txtFieldPartNumber.setText(paramString1);
      jPanel1.add(this.txtFieldPartNumber, "4, " + b + ", left, default");
      b += 2;
      jLabel = new JLabel("Manufacturer:");
      jPanel1.add(jLabel, "2, " + b + ", right, default");
      this.txtFieldMfg = new JTextField(40);
      AutoCompleteDecorator.decorate(this.txtFieldMfg, paramList, false);
      this.txtFieldMfg.setText(paramString2);
      jPanel1.add(this.txtFieldMfg, "4, " + b + ", left, default");
      JPanel jPanel2 = new JPanel();
      jPanel2.setLayout(new BoxLayout(jPanel2, 1));
      jPanel2.setBorder(new EmptyBorder(5, 5, 5, 5));
      imageIcon = new ImageIcon(ContentProviderPartNumberSearchMainPanel.class.getResource("images/check.png"));
      this.btnSelect = new JButton("Select", imageIcon);
      this.btnSelect.setActionCommand("Select");
      this.btnSelect.addActionListener(this);
      this.btnSelect.setPreferredSize(new Dimension(125, 24));
      this.btnSelect.setMinimumSize(new Dimension(125, 24));
      this.btnSelect.setMaximumSize(new Dimension(125, 24));
      this.btnSelect.setEnabled(false);
      paramContentProviderPartNumberSearchDlg.getRootPane().setDefaultButton(this.btnSelect);
      jPanel2.add(this.btnSelect);
      imageIcon = new ImageIcon(ContentProviderPartNumberSearchMainPanel.class.getResource("images/close_icon.png"));
      this.btnCancel = new JButton("Cancel", imageIcon);
      this.btnCancel.setActionCommand("Cancel");
      this.btnCancel.addActionListener(this);
      this.btnCancel.setPreferredSize(new Dimension(125, 24));
      this.btnCancel.setMinimumSize(new Dimension(125, 24));
      this.btnCancel.setMaximumSize(new Dimension(125, 24));
      jPanel2.add(this.btnCancel);
      jPanel2.add(Box.createRigidArea(new Dimension(0, 10)));
      this.btnViewOnline = new JButton("View Online", paramAbstractContentProvider.getIcon());
      this.btnViewOnline.setActionCommand("ViewOnline");
      this.btnViewOnline.addActionListener(this);
      this.btnViewOnline.setPreferredSize(new Dimension(125, 24));
      this.btnViewOnline.setMinimumSize(new Dimension(125, 24));
      this.btnViewOnline.setMaximumSize(new Dimension(125, 24));
      this.btnViewOnline.setEnabled(false);
      jPanel2.add(this.btnViewOnline);
      add("East", jPanel2);
      b += 2;
      add("North", jPanel1);
      LookAndFeelAddons.setAddon(WindowsClassicLookAndFeelAddons.class);
      this.tm = new ContentProviderPartNumberSearchResultsTableModel();
      this.table = new ContentProviderPartNumberSearchResultsJXTable(this, this.tm);
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
    } catch (Exception exception) {
      log.error(exception);
      JOptionPane.showMessageDialog(null, exception.getMessage());
    } 
  }
  
  public void actionPerformed(ActionEvent paramActionEvent) {
    if (paramActionEvent.getActionCommand().equals("Search")) {
      try {
        doSearch();
      } catch (ContentProviderException contentProviderException) {
        log.error(contentProviderException);
      } 
    } else if (paramActionEvent.getActionCommand().equals("Select")) {
      doSelect();
    } else if (paramActionEvent.getActionCommand().equals("Cancel")) {
      this.dlg.saveWindowPrefs();
      this.dlg.setVisible(false);
      this.dlg.dispose();
    } else if (paramActionEvent.getActionCommand().equals("ViewOnline")) {
      int i = this.table.convertRowIndexToModel(this.table.getSelectedRow());
      this.selectedPart = this.tm.getResultRecordAt(i);
      try {
        this.ccp.viewPartOnline(this.selectedPart.getObjectID());
      } catch (ContentProviderException contentProviderException) {
        log.error(contentProviderException.getMessage());
      } 
    } 
  }
  
  public void doSelect() {
    int i = this.table.convertRowIndexToModel(this.table.getSelectedRow());
    this.selectedPart = this.tm.getResultRecordAt(i);
    this.dlg.saveWindowPrefs();
    this.dlg.setVisible(false);
    this.dlg.dispose();
  }
  
  public void doSearch() throws ContentProviderException {
    if (!isSearchEnabled())
      return; 
    try {
      this.tm.clearPartList();
      setSearchEnabled(false);
      setWaitCursor(true);
    } catch (Exception exception) {
      setStatus(exception.getMessage());
      setSearchEnabled(true);
      setWaitCursor(false);
      return;
    } 
    setStatus("Loading Content Provider Configuration for '" + this.ccp.getName() + "'...");
    ContentProviderPartNumberSearchLoadConfigAndSearchTask contentProviderPartNumberSearchLoadConfigAndSearchTask = new ContentProviderPartNumberSearchLoadConfigAndSearchTask(this);
    contentProviderPartNumberSearchLoadConfigAndSearchTask.execute();
  }
  
  protected void doSearchTask() throws ContentProviderException {
    try {
      if (this.partClass == null) {
        this.partClass = this.ccp.getConfig().getPartClassByContentProviderId("Part");
        this.partNumberProp = this.partClass.getClassPropertyByContentProviderId(this.ccp.getPartNumberPropID());
        this.tm.addColumn(new ContentProviderSearchResultsColumn("Manufacturer Part Number", this.partNumberProp));
        this.mfgProp = this.partClass.getClassPropertyByContentProviderId(this.ccp.getManufacturerPropID());
        this.tm.addColumn(new ContentProviderSearchResultsColumn("Manufacturer", this.mfgProp));
        ContentProviderConfigProperty contentProviderConfigProperty = this.partClass.getClassPropertyByContentProviderId(this.ccp.getDescriptionPropID());
        this.tm.addColumn(new ContentProviderSearchResultsColumn("Description", contentProviderConfigProperty));
        this.tm.fireTableStructureChanged();
      } 
      this.crit = this.ccp.createCriteria();
      DMSStringExprLexer dMSStringExprLexer = new DMSStringExprLexer();
      CommonTokenStream commonTokenStream = new CommonTokenStream((TokenSource)dMSStringExprLexer);
      DMSStringExprParser dMSStringExprParser = new DMSStringExprParser((TokenStream)commonTokenStream);
      ANTLRInputStream aNTLRInputStream = new ANTLRInputStream(new ByteArrayInputStream(this.txtFieldPartNumber.getText().getBytes()));
      dMSStringExprLexer.setCharStream((CharStream)aNTLRInputStream);
      commonTokenStream.setTokenSource((TokenSource)dMSStringExprLexer);
      dMSStringExprParser.setTokenStream((TokenStream)commonTokenStream);
      DMSStringExprParser.prog_return prog_return = dMSStringExprParser.prog();
      if (dMSStringExprParser.getNumberOfSyntaxErrors() > 0)
        throw new ContentProviderException("Error in query syntax for 'Part Number'."); 
      this.crit.add(AbstractCriteria.stringGetCriteria(this.partNumberProp, prog_return.getTree()));
      if (!this.txtFieldMfg.getText().trim().isEmpty()) {
        aNTLRInputStream = new ANTLRInputStream(new ByteArrayInputStream(this.txtFieldMfg.getText().getBytes()));
        dMSStringExprLexer.setCharStream((CharStream)aNTLRInputStream);
        commonTokenStream.setTokenSource((TokenSource)dMSStringExprLexer);
        dMSStringExprParser.setTokenStream((TokenStream)commonTokenStream);
        prog_return = dMSStringExprParser.prog();
        if (dMSStringExprParser.getNumberOfSyntaxErrors() > 0)
          throw new ContentProviderException("Error in query syntax for 'Manufacturer'."); 
        this.crit.add(AbstractCriteria.stringGetCriteria(this.mfgProp, prog_return.getTree()));
      } 
    } catch (Exception exception) {
      setStatus(exception.getMessage());
      setSearchEnabled(true);
      setWaitCursor(false);
      return;
    } 
    setStatus("Querying Content Provider...");
    ContentProviderPartNumberSearchTask contentProviderPartNumberSearchTask = new ContentProviderPartNumberSearchTask(this);
    contentProviderPartNumberSearchTask.execute();
  }
  
  public void setStatus(String paramString) {
    this.statusLabel.setText(paramString);
  }
  
  public void setSearchEnabled(boolean paramBoolean) {
    this.bSearchEnabled = paramBoolean;
    this.btnSearch.setEnabled(paramBoolean);
    this.btnViewOnline.setEnabled(paramBoolean);
    this.btnCancel.setEnabled(paramBoolean);
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
  
  public ContentProviderConfigPartClass getPartClass() {
    return this.partClass;
  }
  
  public AbstractCriteria getSearchCriteria() {
    return this.crit;
  }
  
  public ContentProviderPartNumberSearchResultsTableModel getTableModel() {
    return this.tm;
  }
  
  public ContentProviderPartNumberSearchResultsJXTable getTable() {
    return this.table;
  }
  
  public void setComplete() {
    this.tm.fireTableDataChanged();
    if (this.tm.getRowCount() == 0) {
      this.btnSelect.setEnabled(false);
      this.btnViewOnline.setEnabled(false);
      return;
    } 
    for (byte b = 0; b < this.table.getRowCount(); b++) {
      int i = this.table.getRowHeight();
      for (byte b1 = 0; b1 < this.table.getColumnCount(); b1++) {
        Component component = this.table.prepareRenderer(this.table.getCellRenderer(b, b1), b, b1);
        i = Math.max(i, (component.getPreferredSize()).height);
      } 
      this.table.setRowHeight(b, i);
    } 
    this.table.packAll();
    TableColumnExt tableColumnExt = this.table.getColumnExt(0);
    tableColumnExt.setMaxWidth(40);
    this.table.addRowSelectionInterval(0, 0);
    this.btnSelect.setEnabled(true);
    this.btnViewOnline.setEnabled(true);
  }
  
  public IContentProviderResultRecord getSelectedPart() {
    return this.selectedPart;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\plugin\partsearchui\ContentProviderPartNumberSearchMainPanel.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */