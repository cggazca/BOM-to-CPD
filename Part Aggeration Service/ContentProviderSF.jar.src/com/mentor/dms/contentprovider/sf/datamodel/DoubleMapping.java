package com.mentor.dms.contentprovider.sf.datamodel;

import java.util.HashMap;

public class DoubleMapping {
  private static HashMap<String, HashMap<String, DoubleMapping>> doubleMappingMap = new HashMap<>();
  
  private String property;
  
  public DoubleMapping(String paramString) {
    this.property = paramString;
  }
  
  public static void add(String paramString, DoubleMapping paramDoubleMapping) {
    HashMap<Object, Object> hashMap = (HashMap)doubleMappingMap.get(paramString);
    if (hashMap == null) {
      hashMap = new HashMap<>();
      doubleMappingMap.put(paramString, hashMap);
    } 
    hashMap.put(paramDoubleMapping.property, paramDoubleMapping);
  }
  
  public static DoubleMapping get(String paramString1, String paramString2, boolean paramBoolean) {
    DoubleMapping doubleMapping = null;
    HashMap hashMap = doubleMappingMap.get(paramString1);
    if (hashMap != null)
      doubleMapping = (DoubleMapping)hashMap.get(paramString2); 
    if (!paramBoolean && doubleMapping == null) {
      hashMap = doubleMappingMap.get("*");
      if (hashMap != null)
        doubleMapping = (DoubleMapping)hashMap.get(paramString2); 
    } 
    return doubleMapping;
  }
  
  public static boolean hasExplicitPropMaps(String paramString) {
    return doubleMappingMap.containsKey(paramString);
  }
  
  public static boolean isPropMapped(String paramString1, String paramString2, boolean paramBoolean) {
    DoubleMapping doubleMapping = get(paramString1, paramString2, paramBoolean);
    return (doubleMapping != null);
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\datamodel\DoubleMapping.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */