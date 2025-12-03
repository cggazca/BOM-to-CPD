package com.mentor.dms.contentprovider.core.config;

import java.util.Comparator;

public class DMNComparator implements Comparator<ContentProviderConfigPropertyMap> {
  public int compare(ContentProviderConfigPropertyMap paramContentProviderConfigPropertyMap1, ContentProviderConfigPropertyMap paramContentProviderConfigPropertyMap2) {
    String str1 = (paramContentProviderConfigPropertyMap1.getDMN() == null) ? "" : paramContentProviderConfigPropertyMap1.getDMN();
    String str2 = (paramContentProviderConfigPropertyMap2.getDMN() == null) ? "" : paramContentProviderConfigPropertyMap2.getDMN();
    return str1.compareTo(str2);
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\config\ContentProviderConfigContentProviderMap$DMNComparator.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */