package com.mentor.dms.contentprovider.core;

import java.util.Comparator;

class null implements Comparator<ContentProviderRegistryEntry> {
  public int compare(ContentProviderRegistryEntry paramContentProviderRegistryEntry1, ContentProviderRegistryEntry paramContentProviderRegistryEntry2) {
    int i = paramContentProviderRegistryEntry1.getSearchTabOrder();
    int j = paramContentProviderRegistryEntry2.getSearchTabOrder();
    return (i < j) ? -1 : ((i == j) ? 0 : 1);
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\ContentProviderFactory$1.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */