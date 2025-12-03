package com.mentor.dms.contentprovider.client.credentials;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class null implements ActionListener {
  public void actionPerformed(ActionEvent paramActionEvent) {
    CredentialsDialog.this.bIsCancelled = true;
    CredentialsDialog.this.setVisible(false);
    CredentialsDialog.this.dispose();
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\client\credentials\CredentialsDialog$2.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */