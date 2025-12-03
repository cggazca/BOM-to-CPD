package com.mentor.dms.contentprovider.plugin;

public class ColumnSettings {
  private String name;
  
  private int order;
  
  private int width;
  
  public ColumnSettings() {}
  
  public ColumnSettings(String paramString, int paramInt1, int paramInt2) {
    this.name = paramString;
    this.order = paramInt1;
    this.width = paramInt2;
  }
  
  public String getName() {
    return this.name;
  }
  
  public void setName(String paramString) {
    this.name = paramString;
  }
  
  public int getOrder() {
    return this.order;
  }
  
  public void setOrder(int paramInt) {
    this.order = paramInt;
  }
  
  public int getWidth() {
    return this.width;
  }
  
  public void setWidth(int paramInt) {
    this.width = paramInt;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\plugin\ColumnSettings.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */