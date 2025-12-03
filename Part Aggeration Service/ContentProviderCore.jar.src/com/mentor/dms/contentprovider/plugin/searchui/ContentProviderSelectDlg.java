package com.mentor.dms.contentprovider.plugin.searchui;

import com.mentor.datafusion.oi.OICursor;
import com.mentor.datafusion.oi.OIException;
import com.mentor.datafusion.oi.OIHelper;
import com.mentor.datafusion.oi.OIObjectManager;
import com.mentor.datafusion.oi.OIObjectManagerFactory;
import com.mentor.datafusion.oi.OIQuery;
import com.mentor.datafusion.oi.login.OIAuthenticate;
import com.mentor.datafusion.oi.login.OIAuthenticateFactory;
import com.mentor.dms.contentprovider.AbstractContentProvider;
import com.mentor.dms.contentprovider.ContentProviderException;
import com.mentor.dms.contentprovider.ContentProviderFactory;
import com.mentor.dms.contentprovider.ContentProviderGlobal;
import com.mentor.dms.contentprovider.ContentProviderRegistryEntry;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.KeyStroke;
import javax.swing.UIManager;

public class ContentProviderSelectDlg extends JDialog implements ActionListener {
  private AbstractContentProvider selectedContentProvider = null;
  
  private ButtonGroup group = new ButtonGroup();
  
  public ContentProviderSelectDlg(JFrame paramJFrame, List<AbstractContentProvider> paramList) {
    super(paramJFrame);
    setTitle("External Content Provider");
    setIconImage(ContentProviderGlobal.getAppIconImage());
    JPanel jPanel1 = new JPanel(new BorderLayout());
    setContentPane(jPanel1);
    jPanel1.add(new JLabel("Please select Content Provider:"), "North");
    JPanel jPanel2 = new JPanel(new GridLayout(0, 1));
    boolean bool = true;
    for (AbstractContentProvider abstractContentProvider : paramList) {
      JPanel jPanel = new JPanel(new FlowLayout(3));
      JContentProviderRadioButton jContentProviderRadioButton = new JContentProviderRadioButton();
      jContentProviderRadioButton.setContentProvider(abstractContentProvider);
      if (bool) {
        jContentProviderRadioButton.setSelected(true);
        bool = false;
      } 
      jPanel.add(jContentProviderRadioButton);
      jPanel.add(new JLabel(abstractContentProvider.getName(), abstractContentProvider.getIcon(), 2));
      this.group.add(jContentProviderRadioButton);
      jPanel2.add(jPanel);
    } 
    jPanel2.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
    jPanel1.add(jPanel2, "Before");
    JPanel jPanel3 = new JPanel();
    JButton jButton1 = new JButton("Select");
    jButton1.addActionListener(this);
    jButton1.setActionCommand("SELECT_BUTTON");
    getRootPane().setDefaultButton(jButton1);
    jPanel3.add(jButton1);
    JButton jButton2 = new JButton("Cancel");
    jButton2.addActionListener(this);
    jButton2.setActionCommand("CANCEL_BUTTON");
    jPanel3.add(jButton2);
    jPanel1.add(jPanel3, "South");
    jPanel1.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    pack();
    ActionListener actionListener = new ActionListener() {
        public void actionPerformed(ActionEvent param1ActionEvent) {
          ContentProviderSelectDlg.this.setVisible(false);
        }
      };
    KeyStroke keyStroke = KeyStroke.getKeyStroke(27, 0);
    getRootPane().registerKeyboardAction(actionListener, keyStroke, 2);
    setDefaultCloseOperation(2);
    setResizable(false);
    setModal(true);
    setLocationRelativeTo(paramJFrame);
  }
  
  public void actionPerformed(ActionEvent paramActionEvent) {
    if (paramActionEvent.getActionCommand().equals("SELECT_BUTTON")) {
      Enumeration<AbstractButton> enumeration = this.group.getElements();
      while (enumeration.hasMoreElements()) {
        JContentProviderRadioButton jContentProviderRadioButton = (JContentProviderRadioButton)enumeration.nextElement();
        if (jContentProviderRadioButton.isSelected()) {
          this.selectedContentProvider = jContentProviderRadioButton.getContentProvider();
          break;
        } 
      } 
      setVisible(false);
    } else if (paramActionEvent.getActionCommand().equals("CANCEL_BUTTON")) {
      this.selectedContentProvider = null;
      setVisible(false);
    } 
  }
  
  public static AbstractContentProvider selectContentProvider(ContentProviderRegistryEntry.ContentProviderRole paramContentProviderRole) throws ContentProviderException {
    AbstractContentProvider abstractContentProvider = null;
    ArrayList<AbstractContentProvider> arrayList = new ArrayList();
    for (AbstractContentProvider abstractContentProvider1 : ContentProviderFactory.getInstance().getRegisteredContentProviders()) {
      if (paramContentProviderRole == null || abstractContentProvider1.hasRole(paramContentProviderRole))
        arrayList.add(abstractContentProvider1); 
    } 
    if (arrayList.size() == 0)
      throw new ContentProviderException("No Content Providers are configured to participate in the role '" + String.valueOf(paramContentProviderRole) + "'."); 
    if (arrayList.size() == 1) {
      abstractContentProvider = arrayList.get(0);
    } else {
      ContentProviderSelectDlg contentProviderSelectDlg = new ContentProviderSelectDlg(null, arrayList);
      contentProviderSelectDlg.setVisible(true);
      abstractContentProvider = contentProviderSelectDlg.getSelectedContentProvider();
    } 
    return abstractContentProvider;
  }
  
  public static AbstractContentProvider selectContentProvider(OIObjectManager paramOIObjectManager, ContentProviderRegistryEntry.ContentProviderRole paramContentProviderRole, String paramString) throws ContentProviderException {
    AbstractContentProvider abstractContentProvider = null;
    ArrayList<AbstractContentProvider> arrayList = new ArrayList();
    HashSet<String> hashSet = new HashSet();
    try {
      OIQuery oIQuery = paramOIObjectManager.createQuery("ExternalContent", true);
      oIQuery.addRestriction("ExternalContentId", OIHelper.escapeQueryRestriction(paramString));
      oIQuery.addColumn("ECProviderReferences.ECProviderReferenceID");
      if (oIQuery.count() == 0L)
        throw new ContentProviderException("This part is not associated to any External Content."); 
      OICursor oICursor = oIQuery.execute();
      while (oICursor.next()) {
        String str = oICursor.getString("ECProviderReferenceID");
        if (!hashSet.contains(str)) {
          hashSet.add(str);
          AbstractContentProvider abstractContentProvider1 = ContentProviderFactory.getInstance().createContentProvider(str);
          if (paramContentProviderRole == null || abstractContentProvider1.hasRole(paramContentProviderRole))
            arrayList.add(abstractContentProvider1); 
        } 
      } 
    } catch (OIException oIException) {
      throw new ContentProviderException(oIException.getMessage());
    } 
    if (arrayList.size() == 0) {
      if (paramContentProviderRole != null)
        throw new ContentProviderException("None of this part's Content Providers are configured to participate in the role '" + String.valueOf(paramContentProviderRole) + "'."); 
      throw new ContentProviderException("This part does not have any associated Content Providers.");
    } 
    if (arrayList.size() == 1) {
      abstractContentProvider = arrayList.get(0);
    } else {
      ContentProviderSelectDlg contentProviderSelectDlg = new ContentProviderSelectDlg(null, arrayList);
      contentProviderSelectDlg.setVisible(true);
      abstractContentProvider = contentProviderSelectDlg.getSelectedContentProvider();
    } 
    return abstractContentProvider;
  }
  
  public static AbstractContentProvider selectContentProvider(OIObjectManager paramOIObjectManager, String paramString) throws ContentProviderException {
    return selectContentProvider(paramOIObjectManager, (ContentProviderRegistryEntry.ContentProviderRole)null, paramString);
  }
  
  private AbstractContentProvider getSelectedContentProvider() throws ContentProviderException {
    return this.selectedContentProvider;
  }
  
  public static void main(String[] paramArrayOfString) {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception exception) {
      exception.printStackTrace();
    } 
    try {
      OIObjectManagerFactory oIObjectManagerFactory = null;
      OIObjectManager oIObjectManager = null;
      OIAuthenticate oIAuthenticate = OIAuthenticateFactory.createBatchAuthenticate("dms_desktop");
      oIObjectManagerFactory = oIAuthenticate.login("Test App");
      System.out.println("Connected");
      oIObjectManager = oIObjectManagerFactory.createObjectManager();
      ContentProviderFactory.getInstance().registerContentProviders(oIObjectManager);
      AbstractContentProvider abstractContentProvider = selectContentProvider(oIObjectManager, (ContentProviderRegistryEntry.ContentProviderRole)null, "AVX Corp:08055C101JAJ2A");
      if (abstractContentProvider != null) {
        System.out.println(abstractContentProvider.getName());
      } else {
        System.out.println("No Content Provider selected.");
      } 
    } catch (Exception exception) {
      exception.printStackTrace();
    } 
  }
  
  public class JContentProviderRadioButton extends JRadioButton {
    private AbstractContentProvider ccp = null;
    
    public AbstractContentProvider getContentProvider() {
      return this.ccp;
    }
    
    public void setContentProvider(AbstractContentProvider param1AbstractContentProvider) {
      this.ccp = param1AbstractContentProvider;
    }
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\plugin\searchui\ContentProviderSelectDlg.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */