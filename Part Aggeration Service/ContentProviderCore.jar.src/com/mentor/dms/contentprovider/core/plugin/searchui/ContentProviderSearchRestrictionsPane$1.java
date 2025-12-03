package com.mentor.dms.contentprovider.core.plugin.searchui;

import com.mentor.dms.contentprovider.core.config.ContentProviderConfigContentProviderMap;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

class null implements ItemListener {
  public void itemStateChanged(ItemEvent paramItemEvent) {
    ContentProviderConfigContentProviderMap contentProviderConfigContentProviderMap = (ContentProviderConfigContentProviderMap)paramItemEvent.getItem();
    if (ContentProviderSearchRestrictionsPane.this.rest != null && ContentProviderSearchRestrictionsPane.this.rest.getCCPConfigMap() != null && ContentProviderSearchRestrictionsPane.this.rest.getCCPConfigMap() != contentProviderConfigContentProviderMap && ContentProviderSearchRestrictionsPane.this.displayed)
      searchPanel.doUpdateRestrictions(); 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\plugin\searchui\ContentProviderSearchRestrictionsPane$1.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */