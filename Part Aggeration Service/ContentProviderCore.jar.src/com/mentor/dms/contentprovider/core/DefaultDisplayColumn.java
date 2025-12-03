package com.mentor.dms.contentprovider.core;

public class DefaultDisplayColumn {
  String label;
  
  String ccpid;
  
  private boolean displayOnResult = true;
  
  private boolean displayOnHeader = true;
  
  public DefaultDisplayColumn(String paramString1, String paramString2) {
    this.label = paramString1;
    this.ccpid = paramString2;
  }
  
  public DefaultDisplayColumn(String paramString1, String paramString2, boolean paramBoolean1, boolean paramBoolean2) {
    this.label = paramString1;
    this.ccpid = paramString2;
    this.displayOnResult = paramBoolean1;
    this.displayOnHeader = paramBoolean2;
  }
  
  public boolean isDisplayOnHeader() {
    return this.displayOnHeader;
  }
  
  public boolean isDisplayOnResult() {
    return this.displayOnResult;
  }
  
  public String getColumnLabel() {
    return this.label;
  }
  
  public String getContentProviderPropertyId() {
    return this.ccpid;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\DefaultDisplayColumn.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */