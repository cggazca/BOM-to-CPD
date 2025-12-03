package com.mentor.dms.contentprovider.core.utils;

import java.util.HashMap;

public class PropertiesUtils {
  private static final HashMap<String, String> replaceMap = new HashMap<>();
  
  public static String replace(String paramString) {
    return replaceMap.containsKey(paramString) ? replaceMap.get(paramString) : paramString;
  }
  
  static {
    replaceMap.put("Long Description", "Description");
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\cor\\utils\PropertiesUtils.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */