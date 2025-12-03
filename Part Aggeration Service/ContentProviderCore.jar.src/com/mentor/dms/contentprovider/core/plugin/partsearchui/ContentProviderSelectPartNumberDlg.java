package com.mentor.dms.contentprovider.core.plugin.partsearchui;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import com.mentor.datafusion.oi.OICursor;
import com.mentor.datafusion.oi.OIObjectManager;
import com.mentor.datafusion.oi.OIObjectManagerFactory;
import com.mentor.datafusion.oi.OIQuery;
import com.mentor.datafusion.oi.login.OIAuthenticate;
import com.mentor.datafusion.oi.login.OIAuthenticateFactory;
import com.mentor.datafusion.utils.logger.MGLogger;
import com.mentor.dms.contentprovider.core.AbstractContentProvider;
import com.mentor.dms.contentprovider.core.ContentProviderException;
import com.mentor.dms.contentprovider.core.ContentProviderFactory;
import com.mentor.dms.contentprovider.core.ContentProviderGlobal;
import com.mentor.dms.contentprovider.core.IContentProviderResultRecord;
import com.mentor.dms.contentprovider.core.plugin.Activator;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

public class ContentProviderSelectPartNumberDlg extends JDialog implements ActionListener {
  private static MGLogger log = MGLogger.getLogger(ContentProviderSelectPartNumberDlg.class);
  
  private AbstractContentProvider ccp;
  
  private List<String> mfgList;
  
  private JTextField txtFieldPartNumber;
  
  private JTextField txtFieldMfg;
  
  private JTextField txtFieldID;
  
  private JButton btnSelect;
  
  private String lastSelectedPartNumber = "";
  
  private String lastSelectedMfg = "";
  
  private String lastSelectedID = "";
  
  private boolean bCancelled = false;
  
  private IContentProviderResultRecord selectedPartRec = null;
  
  public ContentProviderSelectPartNumberDlg(JFrame paramJFrame, AbstractContentProvider paramAbstractContentProvider, String paramString1, String paramString2, List<String> paramList) {
    super(paramJFrame, "Content Provider Part Number Search", true);
    this.ccp = paramAbstractContentProvider;
    this.mfgList = paramList;
    setIconImage(ContentProviderGlobal.getAppIconImage());
    JPanel jPanel1 = new JPanel();
    jPanel1.setLayout(new BorderLayout());
    jPanel1.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
    JPanel jPanel2 = new JPanel();
    jPanel2.setBorder(new EmptyBorder(10, 10, 20, 10));
    jPanel2.setLayout((LayoutManager)new FormLayout(new ColumnSpec[] { FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow") }, new RowSpec[] { FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC }));
    byte b = 2;
    JLabel jLabel = new JLabel("Part Number:");
    jPanel2.add(jLabel, "2, " + b + ", right, default");
    this.txtFieldPartNumber = new JTextField(40);
    this.txtFieldPartNumber.setText(paramString1);
    this.txtFieldPartNumber.getDocument().addDocumentListener(new DocumentListener() {
          public void changedUpdate(DocumentEvent param1DocumentEvent) {
            ContentProviderSelectPartNumberDlg.this.evaluateTextFieldChanges();
          }
          
          public void removeUpdate(DocumentEvent param1DocumentEvent) {
            ContentProviderSelectPartNumberDlg.this.evaluateTextFieldChanges();
          }
          
          public void insertUpdate(DocumentEvent param1DocumentEvent) {
            ContentProviderSelectPartNumberDlg.this.evaluateTextFieldChanges();
          }
        });
    jPanel2.add(this.txtFieldPartNumber, "4, " + b + ", left, default");
    b += 2;
    jLabel = new JLabel("Manufacturer:");
    jPanel2.add(jLabel, "2, " + b + ", right, default");
    this.txtFieldMfg = new JTextField(40);
    AutoCompleteDecorator.decorate(this.txtFieldMfg, paramList, false);
    this.txtFieldMfg.setText(paramString2);
    this.txtFieldMfg.getDocument().addDocumentListener(new DocumentListener() {
          public void changedUpdate(DocumentEvent param1DocumentEvent) {
            ContentProviderSelectPartNumberDlg.this.evaluateTextFieldChanges();
          }
          
          public void removeUpdate(DocumentEvent param1DocumentEvent) {
            ContentProviderSelectPartNumberDlg.this.evaluateTextFieldChanges();
          }
          
          public void insertUpdate(DocumentEvent param1DocumentEvent) {
            ContentProviderSelectPartNumberDlg.this.evaluateTextFieldChanges();
          }
        });
    jPanel2.add(this.txtFieldMfg, "4, " + b + ", left, default");
    b += 2;
    jLabel = new JLabel("Content Provider ID:");
    jPanel2.add(jLabel, "2, " + b + ", right, default");
    this.txtFieldID = new JTextField(40);
    this.txtFieldID.setEditable(false);
    jPanel2.add(this.txtFieldID, "4, " + b + ", left, default");
    jPanel1.add("Center", jPanel2);
    JPanel jPanel3 = new JPanel();
    jPanel3.setLayout(new BoxLayout(jPanel3, 1));
    jPanel3.setBorder(new EmptyBorder(5, 5, 5, 5));
    ImageIcon imageIcon = new ImageIcon(ContentProviderPartNumberSearchMainPanel.class.getResource("images/check.png"));
    this.btnSelect = new JButton("OK", imageIcon);
    this.btnSelect.setActionCommand("OK");
    this.btnSelect.addActionListener(this);
    this.btnSelect.setPreferredSize(new Dimension(100, 24));
    this.btnSelect.setMinimumSize(new Dimension(100, 24));
    this.btnSelect.setMaximumSize(new Dimension(100, 24));
    getRootPane().setDefaultButton(this.btnSelect);
    jPanel3.add(this.btnSelect);
    imageIcon = new ImageIcon(ContentProviderPartNumberSearchMainPanel.class.getResource("images/close_icon.png"));
    JButton jButton1 = new JButton("Cancel", imageIcon);
    jButton1.setActionCommand("Cancel");
    jButton1.addActionListener(this);
    jButton1.setPreferredSize(new Dimension(100, 24));
    jButton1.setMinimumSize(new Dimension(100, 24));
    jButton1.setMaximumSize(new Dimension(100, 24));
    jPanel3.add(jButton1);
    jPanel3.add(Box.createRigidArea(new Dimension(0, 10)));
    imageIcon = new ImageIcon(ContentProviderPartNumberSearchMainPanel.class.getResource("images/search_catalog.png"));
    JButton jButton2 = new JButton("Search", imageIcon);
    jButton2.setActionCommand("Search");
    jButton2.addActionListener(this);
    jButton2.setPreferredSize(new Dimension(100, 24));
    jButton2.setMinimumSize(new Dimension(100, 24));
    jButton2.setMaximumSize(new Dimension(100, 24));
    jButton2.setToolTipText("Search Content Provider");
    jPanel3.add(jButton2);
    jPanel1.add("East", jPanel3);
    b += 2;
    setContentPane(jPanel1);
    pack();
    setSize(560, 165);
    setLocationRelativeTo(paramJFrame);
    setResizable(false);
    setVisible(true);
  }
  
  private void evaluateTextFieldChanges() {
    if (this.lastSelectedPartNumber.equals(this.txtFieldPartNumber.getText()) && this.lastSelectedMfg.equals(this.txtFieldMfg.getText())) {
      this.txtFieldID.setText(this.lastSelectedID);
    } else {
      this.txtFieldID.setText("");
    } 
  }
  
  private static void setAppLookAndFeel() {
    try {
      if (System.getProperty("os.name").toLowerCase().startsWith("windows"))
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); 
    } catch (Exception exception) {
      exception.printStackTrace();
    } 
  }
  
  public void actionPerformed(ActionEvent paramActionEvent) {
    if (paramActionEvent.getActionCommand().equals("Search")) {
      try {
        this.selectedPartRec = ContentProviderPartNumberSearchDlg.doSearch(this, this.ccp, this.txtFieldPartNumber.getText(), this.txtFieldMfg.getText(), this.mfgList);
        if (this.selectedPartRec != null) {
          this.lastSelectedPartNumber = this.selectedPartRec.getPartNumber();
          this.txtFieldPartNumber.setText(this.lastSelectedPartNumber);
          this.lastSelectedMfg = this.selectedPartRec.getManufacturerName();
          this.txtFieldMfg.setText(this.lastSelectedMfg);
          this.lastSelectedID = this.selectedPartRec.getObjectID();
          this.txtFieldID.setText(this.lastSelectedID);
        } 
      } catch (ContentProviderException contentProviderException) {
        JOptionPane.showMessageDialog(ContentProviderGlobal.getRootFrame(), contentProviderException.getMessage());
      } 
    } else if (paramActionEvent.getActionCommand().equals("OK")) {
      String str = this.txtFieldID.getText();
      if (str.isEmpty()) {
        String str1 = this.txtFieldPartNumber.getText();
        if (str1.trim().isEmpty()) {
          JOptionPane.showMessageDialog(this, "Part Number is required.");
          return;
        } 
        String str2 = this.txtFieldMfg.getText();
        if (str2.trim().isEmpty()) {
          JOptionPane.showMessageDialog(this, "Manufacturer is required.");
          return;
        } 
        this.selectedPartRec = (IContentProviderResultRecord)new NoMatchContentProviderResultRecord(this.ccp, str1, str2);
      } 
      setVisible(false);
    } else if (paramActionEvent.getActionCommand().equals("Cancel")) {
      this.selectedPartRec = null;
      this.bCancelled = true;
      setVisible(false);
    } 
  }
  
  public boolean isCancelled() {
    return this.bCancelled;
  }
  
  public IContentProviderResultRecord getSelectedPart() {
    return this.selectedPartRec;
  }
  
  public static void main(String[] paramArrayOfString) {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception exception) {}
    OIObjectManagerFactory oIObjectManagerFactory = null;
    OIObjectManager oIObjectManager = null;
    try {
      ArrayList<String> arrayList = new ArrayList();
      AbstractContentProvider abstractContentProvider = null;
      OIAuthenticate oIAuthenticate = OIAuthenticateFactory.createBatchAuthenticate("localhost");
      oIObjectManagerFactory = oIAuthenticate.login("Test App");
      System.out.println("Connected");
      oIObjectManager = oIObjectManagerFactory.createObjectManager();
      Activator.setObjectManager(oIObjectManager);
      ContentProviderFactory.getInstance().registerContentProviders(oIObjectManager);
      abstractContentProvider = ContentProviderFactory.getInstance().createContentProvider("SE");
      OIQuery oIQuery = oIObjectManager.createQuery("Manufacturer", true);
      oIQuery.addColumn("ManufacturerName");
      OICursor oICursor = oIQuery.execute();
      while (oICursor.next())
        arrayList.add(oICursor.getString("ManufacturerName")); 
      ContentProviderSelectPartNumberDlg contentProviderSelectPartNumberDlg = new ContentProviderSelectPartNumberDlg(null, abstractContentProvider, "", "", arrayList);
      IContentProviderResultRecord iContentProviderResultRecord = contentProviderSelectPartNumberDlg.getSelectedPart();
      if (!contentProviderSelectPartNumberDlg.isCancelled())
        JOptionPane.showMessageDialog(null, iContentProviderResultRecord.getPartNumber() + " by " + iContentProviderResultRecord.getPartNumber() + " (" + iContentProviderResultRecord.getManufacturerName() + ")"); 
    } catch (Exception exception) {
      exception.printStackTrace();
    } finally {
      if (oIObjectManager != null)
        oIObjectManager.close(); 
      if (oIObjectManagerFactory != null)
        oIObjectManagerFactory.close(); 
    } 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\plugin\partsearchui\ContentProviderSelectPartNumberDlg.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */