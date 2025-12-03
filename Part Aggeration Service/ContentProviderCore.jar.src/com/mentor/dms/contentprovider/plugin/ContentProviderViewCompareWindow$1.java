package com.mentor.dms.contentprovider.plugin;

import com.mentor.dms.contentprovider.ComponentProperty;
import java.util.Comparator;

class null implements Comparator<ComponentProperty> {
  public int compare(ComponentProperty paramComponentProperty1, ComponentProperty paramComponentProperty2) {
    return paramComponentProperty1.getLabel().compareTo(paramComponentProperty2.getLabel());
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\plugin\ContentProviderViewCompareWindow$1.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */