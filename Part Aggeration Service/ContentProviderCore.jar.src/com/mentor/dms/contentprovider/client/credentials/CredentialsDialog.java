package com.mentor.dms.contentprovider.client.credentials;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import com.mentor.datafusion.oi.OIObjectManager;
import com.mentor.datafusion.oi.OIObjectManagerFactory;
import com.mentor.datafusion.oi.login.OIAuthenticate;
import com.mentor.datafusion.oi.login.OIAuthenticateFactory;
import com.mentor.dms.contentprovider.AbstractContentProvider;
import com.mentor.dms.contentprovider.ContentProviderCredentialDefinition;
import com.mentor.dms.contentprovider.ContentProviderException;
import com.mentor.dms.contentprovider.ContentProviderFactory;
import com.mentor.dms.contentprovider.ContentProviderGlobal;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class CredentialsDialog extends JDialog implements ActionListener {
  private AbstractContentProvider ccp;
  
  private JButton btnSave;
  
  private JLabel errorLabel;
  
  private boolean bIsCancelled = false;
  
  private HashMap<ContentProviderCredentialDefinition, JTextField> txtFieldMap = new HashMap<>();
  
  private Map<String, String> credsMap = new HashMap<>();
  
  public CredentialsDialog(Window paramWindow, AbstractContentProvider paramAbstractContentProvider) {
    super(paramWindow, " Content Provider Credentials");
    this.ccp = paramAbstractContentProvider;
    setModal(true);
    setDefaultCloseOperation(2);
    setIconImage(ContentProviderGlobal.getAppIconImage());
    setResizable(false);
    setMinimumSize(new Dimension(425, 10));
  }
  
  public void display() throws ContentProviderException {
    Collection collection = this.ccp.getCredentialDefinitions();
    if (collection == null || collection.isEmpty()) {
      JOptionPane.showMessageDialog(getParent(), this.ccp.getName() + " does provide definition for credentials.");
      return;
    } 
    JPanel jPanel1 = new JPanel();
    getContentPane().add(jPanel1);
    jPanel1.setBorder(new EmptyBorder(5, 5, 5, 5));
    jPanel1.setLayout(new BorderLayout());
    CredentialsPanel credentialsPanel = new CredentialsPanel();
    JPanel jPanel2 = new JPanel();
    jPanel2.setLayout(new BoxLayout(jPanel2, 1));
    JLabel jLabel = new JLabel(this.ccp.getName());
    jLabel.setIcon(this.ccp.getIcon());
    jPanel2.add(jLabel);
    jPanel2.add(Box.createRigidArea(new Dimension(0, 5)));
    jPanel2.add(new JSeparator(0));
    jPanel1.add(jPanel2, "North");
    jPanel1.add((Component)credentialsPanel, "Center");
    credentialsPanel.setLayout((LayoutManager)new FormLayout(new ColumnSpec[] { FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow") }, new RowSpec[] { FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC }));
    byte b = 2;
    JPasswordField jPasswordField = null;
    for (ContentProviderCredentialDefinition contentProviderCredentialDefinition : collection) {
      JTextField jTextField;
      String str = contentProviderCredentialDefinition.getLabel();
      if (contentProviderCredentialDefinition.isRequired())
        str = str + "*"; 
      str = str + " :";
      jLabel = new JLabel(str);
      credentialsPanel.add(jLabel, "2, " + b + ", right, default");
      if (contentProviderCredentialDefinition.isPassword()) {
        jPasswordField = new JPasswordField(20);
      } else {
        jTextField = new JTextField(20);
      } 
      this.txtFieldMap.put(contentProviderCredentialDefinition, jTextField);
      jTextField.setText(this.ccp.getCredential(contentProviderCredentialDefinition.getId()));
      credentialsPanel.add(jTextField, "4, " + b + ", left, default");
      b += 2;
    } 
    JPanel jPanel3 = new JPanel();
    jPanel1.add(jPanel3, "South");
    jPanel3.setLayout(new BorderLayout(0, 0));
    JPanel jPanel4 = new JPanel();
    FlowLayout flowLayout = (FlowLayout)jPanel4.getLayout();
    flowLayout.setAlignment(2);
    jPanel3.add(jPanel4, "East");
    this.btnSave = new JButton("Save");
    this.btnSave.setIcon(new ImageIcon(getClass().getResource("images/save.png")));
    this.btnSave.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent param1ActionEvent) {
            boolean bool = true;
            for (Map.Entry<ContentProviderCredentialDefinition, JTextField> entry : CredentialsDialog.this.txtFieldMap.entrySet()) {
              if (((ContentProviderCredentialDefinition)entry.getKey()).isRequired() && ((JTextField)entry.getValue()).getText().trim().isEmpty()) {
                bool = false;
                break;
              } 
            } 
            if (bool) {
              CredentialsDialog.this.credsMap.clear();
              for (Map.Entry<ContentProviderCredentialDefinition, JTextField> entry : CredentialsDialog.this.txtFieldMap.entrySet())
                CredentialsDialog.this.credsMap.put(((ContentProviderCredentialDefinition)entry.getKey()).getId(), ((JTextField)entry.getValue()).getText()); 
              CredentialsDialog.this.setVisible(false);
              CredentialsDialog.this.dispose();
            } else {
              CredentialsDialog.this.errorLabel.setText("Please supply values for all required fields.");
              CredentialsDialog.this.errorLabel.setVisible(true);
            } 
          }
        });
    jPanel4.add(this.btnSave);
    getRootPane().setDefaultButton(this.btnSave);
    JButton jButton = new JButton("Cancel");
    jButton.setIcon(new ImageIcon(getClass().getResource("images/cancel.png")));
    jPanel4.add(jButton);
    jButton.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent param1ActionEvent) {
            CredentialsDialog.this.bIsCancelled = true;
            CredentialsDialog.this.setVisible(false);
            CredentialsDialog.this.dispose();
          }
        });
    jPanel4 = new JPanel();
    flowLayout = (FlowLayout)jPanel4.getLayout();
    flowLayout.setAlignment(0);
    jPanel3.add(jPanel4, "Center");
    this.errorLabel = new JLabel();
    this.errorLabel.setVisible(false);
    jPanel4.add(this.errorLabel);
    this.errorLabel.setForeground(new Color(220, 20, 60));
    pack();
    setLocationRelativeTo(getParent());
    setVisible(true);
  }
  
  public void actionPerformed(ActionEvent paramActionEvent) {}
  
  public Map<String, String> getCredentials() {
    return this.credsMap;
  }
  
  public boolean isCancelled() {
    return this.bIsCancelled;
  }
  
  public static void main(String[] paramArrayOfString) {
    OIObjectManagerFactory oIObjectManagerFactory = null;
    OIObjectManager oIObjectManager = null;
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      OIAuthenticate oIAuthenticate = OIAuthenticateFactory.createBatchAuthenticate("testedm");
      oIObjectManagerFactory = oIAuthenticate.login("Test File Attachment");
      oIObjectManager = oIObjectManagerFactory.createObjectManager();
      ContentProviderFactory.getInstance().registerContentProviders(oIObjectManager);
      AbstractContentProvider abstractContentProvider = ContentProviderFactory.getInstance().createContentProvider("SE");
      CredentialsDialog credentialsDialog = new CredentialsDialog(null, abstractContentProvider);
      credentialsDialog.display();
      if (!credentialsDialog.isCancelled()) {
        Map<String, String> map = credentialsDialog.getCredentials();
        for (Map.Entry<String, String> entry : map.entrySet())
          System.out.println((String)entry.getKey() + " = " + (String)entry.getKey()); 
      } 
    } catch (Exception exception) {
      JOptionPane.showMessageDialog(null, exception.getMessage());
      exception.printStackTrace();
    } finally {
      oIObjectManager.close();
      oIObjectManagerFactory.close();
    } 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\client\credentials\CredentialsDialog.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */