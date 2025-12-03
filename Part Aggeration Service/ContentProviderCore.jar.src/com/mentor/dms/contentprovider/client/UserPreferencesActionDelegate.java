package com.mentor.dms.contentprovider.client;

import com.mentor.dms.contentprovider.ContentProviderException;
import com.mentor.dms.contentprovider.ContentProviderGlobal;
import com.mentor.dms.contentprovider.client.userpref.UserPreferencesDialog;
import com.mentor.dms.ui.DefaultActionDelegate;
import com.mentor.dms.ui.popupcontext.ContextEvent;
import javax.swing.JOptionPane;

public class UserPreferencesActionDelegate extends DefaultActionDelegate {
  public void actionPerformed(ContextEvent paramContextEvent) {
    try {
      UserPreferencesDialog userPreferencesDialog = new UserPreferencesDialog(null);
      userPreferencesDialog.display();
    } catch (ContentProviderException contentProviderException) {
      JOptionPane.showMessageDialog(ContentProviderGlobal.getRootFrame(), contentProviderException.getMessage());
      contentProviderException.printStackTrace();
    } 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\client\UserPreferencesActionDelegate.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */