package com.mentor.dms.contentprovider;

public class DefaultDisplayColumn {
  String label;
  
  String ccpid;
  
  public DefaultDisplayColumn(String paramString1, String paramString2) {
    this.label = paramString1;
    this.ccpid = paramString2;
  }
  
  public String getColumnLabel() {
    return this.label;
  }
  
  public String getContentProviderPropertyId() {
    return this.ccpid;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\DefaultDisplayColumn.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */