package com.mentor.dms.contentprovider;

import java.util.HashMap;
import java.util.Map;

public class DefaultContentProviderApplicationImpl implements IContentProviderApplication {
  public boolean isRepresentativeMPN(Map<String, String> paramMap) {
    String str = paramMap.get("ECRepresentativeMPN");
    return (str != null && str.equals("Y"));
  }
  
  public Map<String, String> getRepresentativeMPNSearchRestrictions() {
    HashMap<Object, Object> hashMap = new HashMap<>();
    hashMap.put("ECRepresentativeMPN", "Y");
    return (Map)hashMap;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\DefaultContentProviderApplicationImpl.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */