package com.mentor.dms.contentprovider.client.userpref;

import com.mentor.datafusion.oi.OIObjectManager;
import com.mentor.datafusion.oi.OIObjectManagerFactory;
import com.mentor.datafusion.oi.login.OIAuthenticate;
import com.mentor.datafusion.oi.login.OIAuthenticateFactory;
import com.mentor.dms.contentprovider.AbstractContentProvider;
import com.mentor.dms.contentprovider.ContentProviderException;
import com.mentor.dms.contentprovider.ContentProviderFactory;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class UserPreferencesDialog extends JDialog implements ActionListener {
  private JButton btnSave;
  
  private JLabel errorLabel;
  
  private boolean bIsCancelled = false;
  
  public UserPreferencesDialog(Window paramWindow) {
    super(paramWindow, "Content Provider Preferences");
    setModal(true);
    setDefaultCloseOperation(2);
    setIconImage((new ImageIcon(getClass().getResource("images/preferences.gif"))).getImage());
    setResizable(false);
  }
  
  public void display() throws ContentProviderException {
    JTabbedPane jTabbedPane = new JTabbedPane();
    jTabbedPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    byte b = 0;
    for (AbstractContentProvider abstractContentProvider : ContentProviderFactory.getInstance().getRegisteredContentProviders()) {
      if (abstractContentProvider.getUserPreferences() == null)
        continue; 
      UserPreferencesPanel userPreferencesPanel = new UserPreferencesPanel();
      abstractContentProvider.getUserPreferences().initPanelUI(userPreferencesPanel);
      UserPreferencesTabComponent userPreferencesTabComponent = new UserPreferencesTabComponent(jTabbedPane, abstractContentProvider.getIcon());
      jTabbedPane.addTab(abstractContentProvider.getName(), (Component)userPreferencesPanel);
      jTabbedPane.setTabComponentAt(b++, userPreferencesTabComponent);
    } 
    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(jTabbedPane, "Center");
    JPanel jPanel1 = new JPanel();
    getContentPane().add(jPanel1, "South");
    jPanel1.setLayout(new BorderLayout(0, 0));
    JPanel jPanel2 = new JPanel();
    FlowLayout flowLayout = (FlowLayout)jPanel2.getLayout();
    flowLayout.setAlignment(2);
    jPanel1.add(jPanel2, "East");
    this.btnSave = new JButton("Save");
    this.btnSave.setIcon(new ImageIcon(getClass().getResource("images/save.png")));
    this.btnSave.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent param1ActionEvent) {
            boolean bool = true;
            if (bool) {
              UserPreferencesDialog.this.setVisible(false);
              UserPreferencesDialog.this.dispose();
              try {
                for (AbstractContentProvider abstractContentProvider : ContentProviderFactory.getInstance().getRegisteredContentProviders()) {
                  AbstractUserPreferences abstractUserPreferences = abstractContentProvider.getUserPreferences();
                  if (abstractUserPreferences == null)
                    continue; 
                  abstractUserPreferences.save();
                } 
              } catch (ContentProviderException contentProviderException) {
                contentProviderException.printStackTrace();
              } 
            } else {
              UserPreferencesDialog.this.errorLabel.setText("Please supply values for all required fields.");
              UserPreferencesDialog.this.errorLabel.setVisible(true);
            } 
          }
        });
    jPanel2.add(this.btnSave);
    getRootPane().setDefaultButton(this.btnSave);
    JButton jButton = new JButton("Cancel");
    jButton.setIcon(new ImageIcon(getClass().getResource("images/cancel.png")));
    jPanel2.add(jButton);
    jButton.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent param1ActionEvent) {
            UserPreferencesDialog.this.bIsCancelled = true;
            UserPreferencesDialog.this.setVisible(false);
            UserPreferencesDialog.this.dispose();
          }
        });
    jPanel2 = new JPanel();
    flowLayout = (FlowLayout)jPanel2.getLayout();
    flowLayout.setAlignment(0);
    jPanel1.add(jPanel2, "Center");
    this.errorLabel = new JLabel();
    this.errorLabel.setVisible(false);
    jPanel2.add(this.errorLabel);
    this.errorLabel.setForeground(new Color(220, 20, 60));
    pack();
    setLocationRelativeTo(getParent());
    setVisible(true);
  }
  
  public void actionPerformed(ActionEvent paramActionEvent) {}
  
  public static void main(String[] paramArrayOfString) {
    OIObjectManagerFactory oIObjectManagerFactory = null;
    OIObjectManager oIObjectManager = null;
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      OIAuthenticate oIAuthenticate = OIAuthenticateFactory.createBatchAuthenticate("Siemens");
      oIObjectManagerFactory = oIAuthenticate.login("Test File Attachment");
      oIObjectManager = oIObjectManagerFactory.createObjectManager();
      ContentProviderFactory.getInstance().registerContentProviders(oIObjectManager);
      UserPreferencesDialog userPreferencesDialog = new UserPreferencesDialog(null);
      userPreferencesDialog.display();
    } catch (Exception exception) {
      JOptionPane.showMessageDialog(null, exception.getMessage());
      exception.printStackTrace();
    } finally {}
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\clien\\userpref\UserPreferencesDialog.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */