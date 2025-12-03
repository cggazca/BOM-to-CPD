package com.mentor.dms.contentprovider.plugin.searchui;

class TabOrder {
  private String tabName;
  
  private Integer minDisposeOrder;
  
  public TabOrder(String paramString, int paramInt) {
    this.tabName = paramString;
    this.minDisposeOrder = Integer.valueOf(paramInt);
  }
  
  public String getTabName() {
    return this.tabName;
  }
  
  public void setTabName(String paramString) {
    this.tabName = paramString;
  }
  
  public Integer getMinDisposeOrder() {
    return this.minDisposeOrder;
  }
  
  public void setMinDisposeOrder(int paramInt) {
    this.minDisposeOrder = Integer.valueOf(paramInt);
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\plugin\searchui\TabOrder.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */