package com.mentor.dms.contentprovider.core.client.userpref;

import com.mentor.dms.contentprovider.core.AbstractContentProvider;
import com.mentor.dms.contentprovider.core.ContentProviderException;
import com.mentor.dms.contentprovider.core.ContentProviderFactory;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class null implements ActionListener {
  public void actionPerformed(ActionEvent paramActionEvent) {
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
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\clien\\userpref\UserPreferencesDialog$1.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */