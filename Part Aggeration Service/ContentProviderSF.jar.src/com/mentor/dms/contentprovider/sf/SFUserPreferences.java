package com.mentor.dms.contentprovider.sf;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import com.mentor.dms.contentprovider.core.AbstractContentProvider;
import com.mentor.dms.contentprovider.core.ContentProviderAppConfig;
import com.mentor.dms.contentprovider.core.ContentProviderException;
import com.mentor.dms.contentprovider.core.ContentProviderFactory;
import com.mentor.dms.contentprovider.core.client.userpref.AbstractUserPreferences;
import com.mentor.dms.contentprovider.core.client.userpref.UserPreferencesPanel;
import java.awt.LayoutManager;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class SFUserPreferences extends AbstractUserPreferences {
  private String numSearchResults = "50";
  
  private boolean bEnableProxy = false;
  
  private boolean bEnableSSLKeystore = false;
  
  private JComboBox<String> cmbNumSearchResults = null;
  
  private JRadioButton rbEnableProxyYes = null;
  
  private JRadioButton rbEnableSSLYes = null;
  
  private ContentProviderImpl seCCP = null;
  
  private ContentProviderAppConfig appConfig = null;
  
  public SFUserPreferences(AbstractContentProvider paramAbstractContentProvider) {
    super(paramAbstractContentProvider);
    this.seCCP = (ContentProviderImpl)paramAbstractContentProvider;
    this.appConfig = ContentProviderFactory.getInstance().getAppConfig();
  }
  
  public void read() throws ContentProviderException {
    this.numSearchResults = getPreferences().get("numSearchResults", "50");
    this.bEnableProxy = (getPreferences().getBoolean("enableProxy", this.appConfig.isProxyEnabled()) && this.appConfig.isProxyEnabled());
    this.bEnableSSLKeystore = (getPreferences().getBoolean("enableSSLKeystore", this.appConfig.isSSLKeystoreEnabled()) && this.appConfig.isSSLKeystoreEnabled());
  }
  
  public void save() throws ContentProviderException {
    this.numSearchResults = this.cmbNumSearchResults.getItemAt(this.cmbNumSearchResults.getSelectedIndex());
    getPreferences().put("numSearchResults", this.numSearchResults);
    this.bEnableProxy = this.rbEnableProxyYes.isSelected();
    getPreferences().putBoolean("enableProxy", this.bEnableProxy);
    this.bEnableSSLKeystore = this.rbEnableSSLYes.isSelected();
    getPreferences().putBoolean("enableSSLKeystore", this.bEnableSSLKeystore);
  }
  
  public void initPanelUI(UserPreferencesPanel paramUserPreferencesPanel) {
    paramUserPreferencesPanel.setLayout((LayoutManager)new FormLayout(new ColumnSpec[] { FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow") }, new RowSpec[] { FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC }));
    byte b = 2;
    JLabel jLabel = null;
    jLabel = new JLabel("Number of Extended Search results:");
    paramUserPreferencesPanel.add(jLabel, "2, " + b + ", right, default");
    this.cmbNumSearchResults = new JComboBox<>(new String[] { "10", "50", "100", "200" });
    paramUserPreferencesPanel.add(this.cmbNumSearchResults, "4, " + b + ", left, default");
    b += 2;
    jLabel = new JLabel("Enable HTTP proxy:");
    jLabel.setEnabled(this.appConfig.isProxyEnabled());
    paramUserPreferencesPanel.add(jLabel, "2, " + b + ", right, default");
    this.rbEnableProxyYes = new JRadioButton("Yes");
    this.rbEnableProxyYes.setEnabled(this.appConfig.isProxyEnabled());
    JRadioButton jRadioButton1 = new JRadioButton("No");
    jRadioButton1.setEnabled(this.appConfig.isProxyEnabled());
    ButtonGroup buttonGroup = new ButtonGroup();
    buttonGroup.add(this.rbEnableProxyYes);
    buttonGroup.add(jRadioButton1);
    JPanel jPanel = new JPanel();
    jPanel.setLayout(new BoxLayout(jPanel, 0));
    jPanel.setAlignmentX(0.0F);
    jPanel.add(this.rbEnableProxyYes);
    jPanel.add(jRadioButton1);
    paramUserPreferencesPanel.add(jPanel, "4, " + b + ", left, default");
    b += 2;
    jLabel = new JLabel("Enable SSL keystore");
    jLabel.setEnabled(this.appConfig.isSSLKeystoreEnabled());
    paramUserPreferencesPanel.add(jLabel, "2, " + b + ", right, default");
    this.rbEnableSSLYes = new JRadioButton("Yes");
    this.rbEnableSSLYes.setEnabled(this.appConfig.isSSLKeystoreEnabled());
    JRadioButton jRadioButton2 = new JRadioButton("No");
    jRadioButton2.setEnabled(this.appConfig.isSSLKeystoreEnabled());
    buttonGroup = new ButtonGroup();
    buttonGroup.add(this.rbEnableSSLYes);
    buttonGroup.add(jRadioButton2);
    jPanel = new JPanel();
    jPanel.setLayout(new BoxLayout(jPanel, 0));
    jPanel.setAlignmentX(0.0F);
    jPanel.add(this.rbEnableSSLYes);
    jPanel.add(jRadioButton2);
    paramUserPreferencesPanel.add(jPanel, "4, " + b + ", left, default");
    b += 2;
    this.cmbNumSearchResults.setSelectedItem(this.numSearchResults);
    this.rbEnableProxyYes.setSelected((this.bEnableProxy && this.appConfig.isProxyEnabled()));
    jRadioButton1.setSelected((!this.bEnableProxy || !this.appConfig.isProxyEnabled()));
    jPanel.setEnabled(false);
    this.rbEnableSSLYes.setSelected(this.bEnableSSLKeystore & this.appConfig.isSSLKeystoreEnabled());
    jRadioButton2.setSelected((!this.bEnableSSLKeystore || !this.appConfig.isSSLKeystoreEnabled()));
  }
  
  public String getNumSearchResults() {
    return this.numSearchResults;
  }
  
  public void setNumSearchResults(String paramString) {
    this.numSearchResults = paramString;
  }
  
  public boolean isProxyEnabled() {
    return this.bEnableProxy;
  }
  
  public void setProxyEnabled(boolean paramBoolean) {
    this.bEnableProxy = paramBoolean;
  }
  
  public boolean isSSLKeystoreEnabled() {
    return this.bEnableSSLKeystore;
  }
  
  public void setSSLKeystoreEnabled(boolean paramBoolean) {
    this.bEnableSSLKeystore = paramBoolean;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\SFUserPreferences.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */