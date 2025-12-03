package com.mentor.dms.contentprovider.plugin.searchui;

import com.mentor.dms.contentprovider.config.ContentProviderConfigContentProviderMap;
import javax.swing.JRadioButton;

public class JContentProviderMapRadioButton extends JRadioButton {
  private ContentProviderConfigContentProviderMap ccpMap = null;
  
  public ContentProviderConfigContentProviderMap getContentProviderMap() {
    return this.ccpMap;
  }
  
  public void setContentProviderMap(ContentProviderConfigContentProviderMap paramContentProviderConfigContentProviderMap) {
    this.ccpMap = paramContentProviderConfigContentProviderMap;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\plugin\searchui\ContentProviderSearchClassSelectDlg$JContentProviderMapRadioButton.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */