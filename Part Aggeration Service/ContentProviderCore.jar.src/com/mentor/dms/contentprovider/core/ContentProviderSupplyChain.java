package com.mentor.dms.contentprovider.core;

import java.util.LinkedHashMap;

public class ContentProviderSupplyChain {
  LinkedHashMap<String, Object> properties = new LinkedHashMap<>();
  
  public void add(String paramString, Object paramObject) {
    this.properties.put(paramString, paramObject);
  }
  
  public LinkedHashMap<String, Object> getProperties() {
    return this.properties;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\ContentProviderSupplyChain.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */