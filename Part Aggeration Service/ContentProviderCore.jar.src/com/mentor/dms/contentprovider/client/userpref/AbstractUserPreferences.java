package com.mentor.dms.contentprovider.client.userpref;

import com.mentor.dms.contentprovider.AbstractContentProvider;
import com.mentor.dms.contentprovider.ContentProviderException;
import java.util.prefs.Preferences;

public abstract class AbstractUserPreferences {
  private static final String nodeString = "/com/mentor/mcd/dms/ContentProvider/UserPreferences";
  
  private AbstractContentProvider ccp = null;
  
  private Preferences prefs = null;
  
  public AbstractUserPreferences(AbstractContentProvider paramAbstractContentProvider) {
    this.ccp = paramAbstractContentProvider;
    Preferences preferences = Preferences.userRoot();
    this.prefs = preferences.node("/com/mentor/mcd/dms/ContentProvider/UserPreferences/" + paramAbstractContentProvider.getId());
  }
  
  protected AbstractContentProvider getContentProvider() {
    return this.ccp;
  }
  
  protected Preferences getPreferences() {
    return this.prefs;
  }
  
  public abstract void initPanelUI(UserPreferencesPanel paramUserPreferencesPanel) throws ContentProviderException;
  
  public abstract void read() throws ContentProviderException;
  
  public abstract void save() throws ContentProviderException;
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\clien\\userpref\AbstractUserPreferences.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */