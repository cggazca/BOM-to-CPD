package com.mentor.dms.contentprovider.core.plugin.partrequest;

import com.jgoodies.forms.builder.ButtonBarBuilder;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.FormLayout;
import com.mentor.datafusion.oi.OIClassManager;
import com.mentor.datafusion.oi.OICursor;
import com.mentor.datafusion.oi.OIException;
import com.mentor.datafusion.oi.OIObjectManager;
import com.mentor.datafusion.oi.OIQuery;
import com.mentor.datafusion.oi.model.OIClass;
import com.mentor.datafusion.oi.model.OIDoubleField;
import com.mentor.datafusion.oi.model.OIField;
import com.mentor.datafusion.oi.model.OIIntegerField;
import com.mentor.datafusion.oi.model.OIReferenceField;
import com.mentor.datafusion.oi.model.OIStringField;
import com.mentor.datafusion.oi.type.OIObject;
import com.mentor.dms.contentprovider.core.ContentProviderGlobal;
import com.mentor.dms.contentprovider.core.IContentProviderResultRecord;
import com.mentor.dms.contentprovider.core.actions.CreatePartRequestAction;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.DefaultFocusTraversalPolicy;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.border.Border;
import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXComboBox;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.plaf.LookAndFeelAddons;
import org.jdesktop.swingx.plaf.windows.WindowsClassicLookAndFeelAddons;

public class NewPartRequestDlg extends JDialog implements ActionListener {
  private CreatePartRequestAction action;
  
  private IContentProviderResultRecord resultRec;
  
  private int buttonPressed = 2;
  
  private String prodLib;
  
  private String siteDmnName;
  
  private String lastSiteDMN = "";
  
  private static final int STATUS_MANDATORY_FIELD = 18;
  
  private static final int STATUS_OBJECT_REF = 4;
  
  private ArrayList<CatalogField> inputList = new ArrayList<>();
  
  private JXComboBox cmbProdLib;
  
  private JXComboBox cmbSite;
  
  JXLabel labelMandatory = new JXLabel("Mandatory Field");
  
  private JScrollPane scrollPane;
  
  private final String LAYOUT_SPEC = "left:pref, 2dlu, pref:grow, 2dlu";
  
  private Window parentWindow;
  
  public NewPartRequestDlg(CreatePartRequestAction paramCreatePartRequestAction, Frame paramFrame, String paramString, IContentProviderResultRecord paramIContentProviderResultRecord, Set<String> paramSet, Map<String, String> paramMap) {
    super(paramFrame, false);
    this.action = paramCreatePartRequestAction;
    this.resultRec = paramIContentProviderResultRecord;
    char c1 = 'Ɛ';
    char c2 = 'ð';
    setTitle(paramString);
    setIconImage(ContentProviderGlobal.getAppIconImage());
    setDefaultCloseOperation(2);
    setSize(c1, c2);
    setLocationRelativeTo(paramFrame);
    JXPanel jXPanel1 = new JXPanel();
    getContentPane().add((Component)jXPanel1);
    jXPanel1.setBorder((Border)Borders.DIALOG);
    jXPanel1.setLayout(new BorderLayout());
    JXPanel jXPanel2 = new JXPanel();
    jXPanel2.setBorder((Border)Borders.DLU4);
    jXPanel2.setLayout((LayoutManager)new FormLayout("left:pref, 10dlu, pref:grow", "pref, 3dlu, pref, 3dlu, pref"));
    JXLabel jXLabel1 = new JXLabel("Site");
    jXPanel2.add((Component)jXLabel1, "1, 1, left, default");
    List<Site> list = convMapToList(paramMap);
    this.cmbSite = new JXComboBox(list.toArray());
    jXPanel2.add((Component)this.cmbSite, "3, 1, left, default");
    this.cmbSite.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent param1ActionEvent) {
            NewPartRequestDlg.this.updateView((NewPartRequestDlg.Site)NewPartRequestDlg.this.cmbSite.getSelectedItem());
          }
        });
    JXLabel jXLabel2 = new JXLabel("Production Library");
    jXPanel2.add((Component)jXLabel2, "1, 3, left, default");
    this.cmbProdLib = new JXComboBox(paramSet.toArray());
    jXPanel2.add((Component)this.cmbProdLib, "3, 3, left, default");
    jXPanel2.add((Component)this.labelMandatory, "1, 5, left, default");
    jXPanel1.add((Component)jXPanel2, "North");
    JXPanel jXPanel3 = new JXPanel();
    this.scrollPane = new JScrollPane((Component)jXPanel3, 22, 30);
    this.scrollPane.getVerticalScrollBar().setUnitIncrement(20);
    jXPanel1.add(this.scrollPane, "Center");
    JPanel jPanel = new JPanel();
    JXButton jXButton1 = new JXButton("OK");
    jXButton1.addActionListener(this);
    jXButton1.setActionCommand("OK_BUTTON");
    getRootPane().setDefaultButton((JButton)jXButton1);
    JXButton jXButton2 = new JXButton("Cancel");
    jXButton2.addActionListener(this);
    jXButton2.setActionCommand("CANCEL_BUTTON");
    ButtonBarBuilder buttonBarBuilder = new ButtonBarBuilder();
    buttonBarBuilder.addButton(new JComponent[] { (JComponent)jXButton1, (JComponent)jXButton2 });
    jPanel.add(buttonBarBuilder.build());
    jXPanel1.add(jPanel, "South");
    this.cmbSite.setSelectedIndex(0);
    setGlassPane(new LockingGlassPane());
    getGlassPane().setVisible(false);
  }
  
  private void setDialogSize(int paramInt) {
    int i = (Toolkit.getDefaultToolkit().getScreenSize()).width;
    int j = (Toolkit.getDefaultToolkit().getScreenSize()).height;
    char c1 = 'Ɛ';
    char c2 = 'ð';
    if (paramInt == 0) {
      c1 = 'Ɛ';
      c2 = '´';
      setMinimumSize(new Dimension(c1, c2));
      setPreferredSize(new Dimension(c1, c2));
      this.scrollPane.setVisible(false);
      this.labelMandatory.setVisible(false);
    } else {
      setMinimumSize(new Dimension(c1, c2));
      this.scrollPane.setVisible(true);
      this.labelMandatory.setVisible(true);
    } 
    setSize(c1, c2);
  }
  
  public void dispose() {
    if (getButton() != 0 && this.parentWindow != null)
      this.parentWindow.toFront(); 
    super.dispose();
  }
  
  public void setParentFrame(Window paramWindow) {
    this.parentWindow = paramWindow;
  }
  
  private void updateView(Site paramSite) {
    if (this.lastSiteDMN.equals(paramSite.domainName))
      return; 
    this.lastSiteDMN = paramSite.domainName;
    this.inputList = new ArrayList<>();
    try {
      OIObjectManager oIObjectManager = ContentProviderGlobal.getDMSInstance().getObjectManager();
      OIClassManager oIClassManager = ContentProviderGlobal.getDMSInstance().getOIObjectManagerFactory().getClassManager();
      OIClass oIClass = oIClassManager.getOIClass("Requests");
      for (OIField oIField : oIClass.getFields()) {
        if (oIField.hasFlag(OIField.Flag.ALWAYS_MANDATORY)) {
          CatalogField catalogField = new CatalogField();
          catalogField.domainName = oIField.getName();
          catalogField.label = oIField.getLabel();
          catalogField.fieldType = oIField.getType();
          this.inputList.add(catalogField);
          if (oIField.getType().equals(OIField.Type.REFERENCE)) {
            OIReferenceField oIReferenceField = (OIReferenceField)oIField;
            catalogField.refClassDomainName = oIReferenceField.getContentType().getName();
            continue;
          } 
          if (oIField.getType().equals(OIField.Type.STRING)) {
            catalogField.maxLength = ((OIStringField)oIField).getMaximalLength();
            continue;
          } 
          if (oIField.getType().equals(OIField.Type.INTEGER)) {
            catalogField.maxLength = ((OIIntegerField)oIField).getLength();
            continue;
          } 
          if (oIField.getType().equals(OIField.Type.DOUBLE)) {
            catalogField.maxLength = ((OIDoubleField)oIField).getLength();
            catalogField.precision = ((OIDoubleField)oIField).getPrecision();
          } 
        } 
      } 
      OIQuery oIQuery = oIObjectManager.createQuery("CatalogGroup", true);
      oIQuery.addAlias("DomainModelName", "CatalogDomainModelName");
      oIQuery.addAlias("CatalogCharacteristics.Characteristic.DomainModelName", "CharactDomainModelName");
      oIQuery.addRestriction("DomainModelName", paramSite.domainName);
      oIQuery.addRestriction("CatalogCharacteristics.Characteristic.Text.Language", "e");
      oIQuery.addRestriction("CatalogCharacteristics.Characteristic.DomainModelName", "~ProductionLibrary");
      oIQuery.addColumn("CatalogCharacteristics.Characteristic");
      oIQuery.addColumn("CatalogCharacteristics.Characteristic.DomainModelName");
      oIQuery.addColumn("CatalogCharacteristics.Characteristic.CharactStatus1");
      oIQuery.addColumn("CatalogCharacteristics.Characteristic.Text.InformationText");
      oIQuery.addColumn("CatalogCharacteristics.Characteristic.ValueLength");
      oIQuery.addColumn("CatalogCharacteristics.Characteristic.Precision");
      OICursor oICursor = oIQuery.execute();
      while (oICursor.next()) {
        if (oICursor.getBitSet("CharactStatus1").get(18)) {
          CatalogField catalogField = new CatalogField();
          catalogField.domainName = oICursor.getString("CharactDomainModelName");
          catalogField.label = oICursor.getString("InformationText");
          catalogField.maxLength = oICursor.getInteger("ValueLength").intValue();
          catalogField.precision = oICursor.getInteger("Precision").intValue();
          this.inputList.add(catalogField);
          OIObject oIObject = oICursor.getObject("Characteristic");
          if (oIObject.getBitSet("CharactStatus").get(4)) {
            OIObject oIObject1 = oIObject.getObject("RefClass");
            catalogField.refClassDomainName = oIObject1.getString("DomainModelName");
            catalogField.fieldType = OIField.Type.REFERENCE;
            continue;
          } 
          catalogField.setFieldType(oIObject.getInteger("ValueType").intValue());
        } 
      } 
    } catch (OIException oIException) {
      oIException.printStackTrace();
    } 
    FormLayout formLayout = new FormLayout("left:pref, 2dlu, pref:grow, 2dlu", "");
    DefaultFormBuilder defaultFormBuilder = new DefaultFormBuilder(formLayout);
    defaultFormBuilder.border((Border)Borders.DIALOG);
    for (CatalogField catalogField : this.inputList) {
      JTextField jTextField = new JTextField();
      catalogField.textfield = jTextField;
      defaultFormBuilder.append(catalogField.label, jTextField);
      defaultFormBuilder.nextLine();
    } 
    setDialogSize(this.inputList.size());
    JPanel jPanel = defaultFormBuilder.getPanel();
    this.scrollPane.getViewport().add(jPanel);
    this.scrollPane.revalidate();
    this.scrollPane.repaint();
  }
  
  private void updateView_test(Site paramSite) {
    if (this.lastSiteDMN.equals(paramSite.domainName))
      return; 
    System.out.println("itemStateChanged:" + paramSite.label + ": [" + paramSite.domainName + "]");
    this.lastSiteDMN = paramSite.domainName;
    int i = this.cmbSite.getSelectedIndex() * 10;
    FormLayout formLayout = new FormLayout("left:pref, 2dlu, pref:grow, 2dlu", "");
    DefaultFormBuilder defaultFormBuilder = new DefaultFormBuilder(formLayout);
    defaultFormBuilder.border((Border)Borders.DIALOG);
    for (byte b = 0; b < i; b++) {
      JTextField jTextField = new JTextField();
      jTextField.setEditable(true);
      jTextField.setText("");
      defaultFormBuilder.append("Mandatory Field" + b, jTextField);
      defaultFormBuilder.nextLine();
    } 
    setDialogSize(i);
    JPanel jPanel = defaultFormBuilder.getPanel();
    this.scrollPane.getViewport().add(jPanel);
    this.scrollPane.revalidate();
    this.scrollPane.repaint();
  }
  
  private List<Site> convMapToList(Map<String, String> paramMap) {
    ArrayList<Site> arrayList = new ArrayList();
    for (String str : paramMap.keySet()) {
      Site site = new Site(str, paramMap.get(str));
      arrayList.add(site);
    } 
    return arrayList;
  }
  
  public void actionPerformed(final ActionEvent e) {
    getGlassPane().setVisible(true);
    (new SwingWorker<Void, Void>() {
        protected Void doInBackground() throws Exception {
          if (e.getActionCommand().equals("OK_BUTTON")) {
            NewPartRequestDlg.this.prodLib = NewPartRequestDlg.this.cmbProdLib.getSelectedItem().toString();
            NewPartRequestDlg.this.siteDmnName = ((NewPartRequestDlg.Site)NewPartRequestDlg.this.cmbSite.getSelectedItem()).domainName;
            NewPartRequestDlg.this.buttonPressed = 0;
            NewPartRequestDlg.this.action.dialogClosing(NewPartRequestDlg.this);
          } else if (e.getActionCommand().equals("CANCEL_BUTTON")) {
            NewPartRequestDlg.this.buttonPressed = 2;
            NewPartRequestDlg.this.action.dialogClosing(NewPartRequestDlg.this);
          } 
          return null;
        }
        
        protected void done() {
          NewPartRequestDlg.this.getGlassPane().setVisible(false);
        }
      }).execute();
  }
  
  public int getButton() {
    return this.buttonPressed;
  }
  
  public String getProductionLibrary() {
    return this.prodLib;
  }
  
  public String getSiteDomainName() {
    return this.siteDmnName;
  }
  
  public IContentProviderResultRecord getResultRec() {
    return this.resultRec;
  }
  
  public List<CatalogField> getMandatoryFields() {
    return this.inputList;
  }
  
  public static void main(String[] paramArrayOfString) {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      LookAndFeelAddons.setAddon(WindowsClassicLookAndFeelAddons.class);
    } catch (Exception exception) {}
    try {
      CountDownLatch countDownLatch = new CountDownLatch(1);
      HashSet<String> hashSet = new HashSet();
      hashSet.add("LIB");
      hashSet.add("MTR01");
      hashSet.add("MTR02");
      hashSet.add("ETC01");
      hashSet.add("ETC02");
      hashSet.add("ENG01XXXXXXXXXXXX");
      HashMap<Object, Object> hashMap = new HashMap<>();
      hashMap.put("BaseDMN", "Base");
      hashMap.put("NEWDMN", "NEW");
      hashMap.put("FLDERDMN", "FLDER");
      hashMap.put("NEWDMN2", "NEW");
      NewPartRequestDlg newPartRequestDlg = new NewPartRequestDlg(null, null, "TITLE", null, hashSet, (Map)hashMap);
      newPartRequestDlg.setVisible(true);
      (new Thread(() -> {
            try {
              paramCountDownLatch.await();
            } catch (InterruptedException interruptedException) {
              interruptedException.printStackTrace();
            } 
            System.exit(0);
          })).start();
    } catch (Exception exception) {
      exception.printStackTrace();
    } 
  }
  
  class LockingGlassPane extends JComponent {
    public LockingGlassPane() {
      setOpaque(false);
      setFocusTraversalPolicy(new DefaultFocusTraversalPolicy() {
            public boolean accept(Component param2Component) {
              return false;
            }
          });
      addKeyListener(new KeyAdapter() {
          
          });
      addMouseListener(new MouseAdapter() {
          
          });
      requestFocusInWindow();
      setCursor(Cursor.getPredefinedCursor(3));
    }
    
    public void setVisible(boolean param1Boolean) {
      super.setVisible(param1Boolean);
      setFocusTraversalPolicyProvider(param1Boolean);
    }
  }
  
  private class Site {
    private String domainName;
    
    private String label;
    
    public Site(String param1String1, String param1String2) {
      this.domainName = param1String1;
      this.label = param1String2;
    }
    
    public String toString() {
      return this.label;
    }
  }
  
  class null extends MouseAdapter {}
  
  class null extends KeyAdapter {}
  
  class null extends DefaultFocusTraversalPolicy {
    public boolean accept(Component param1Component) {
      return false;
    }
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\plugin\partrequest\NewPartRequestDlg.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */