package com.mentor.dms.contentprovider.client.catalogselector;

public abstract class AbstractNodeInfo {
  protected String label;
  
  public AbstractNodeInfo(String paramString) {
    this.label = paramString;
  }
  
  public String toString() {
    return this.label;
  }
  
  public String getToolTipText() {
    return this.label;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\client\catalogselector\AbstractNodeInfo.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */