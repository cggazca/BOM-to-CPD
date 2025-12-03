package com.mentor.dms.contentprovider.sf.datamodel;

public class EDMClassMode {
  private static boolean bComponentMode = false;
  
  protected static String rootCatKey = "MM";
  
  protected static String catDMNPrefix = "M-";
  
  protected static String classNum = "60";
  
  protected static String classNumPrefix = "060";
  
  protected static String tableBaseName = "te_mpndyn";
  
  public static void setComponentMode() {
    bComponentMode = true;
    rootCatKey = "AA";
    catDMNPrefix = "C-";
    classNum = "1";
    classNumPrefix = "001";
    tableBaseName = "te_dyn";
  }
  
  public static boolean isComponentMode() {
    return bComponentMode;
  }
  
  public static String getRootCatKey() {
    return rootCatKey;
  }
  
  public static String getCatDMNPrefix() {
    return catDMNPrefix;
  }
  
  public static String getClassNum() {
    return classNum;
  }
  
  public static String getClassNumPrefix() {
    return classNumPrefix;
  }
  
  public static String getTableBaseName() {
    return tableBaseName;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\datamodel\EDMClassMode.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */