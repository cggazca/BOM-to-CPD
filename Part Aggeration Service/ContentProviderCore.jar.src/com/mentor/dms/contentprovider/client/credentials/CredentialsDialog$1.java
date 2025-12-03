package com.mentor.dms.contentprovider.client.credentials;

import com.mentor.dms.contentprovider.ContentProviderCredentialDefinition;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import javax.swing.JTextField;

class null implements ActionListener {
  public void actionPerformed(ActionEvent paramActionEvent) {
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
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\client\credentials\CredentialsDialog$1.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */