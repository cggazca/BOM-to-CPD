package com.mentor.dms.contentprovider.sf;

public class PartClassSearchInfo {
  private String path;
  
  private String[] pathArray;
  
  public String getID() {
    return this.pathArray[this.pathArray.length - 1];
  }
  
  public String getIDByLevel(int paramInt) {
    return this.pathArray[paramInt - 1];
  }
  
  public int getLevel() {
    return this.pathArray.length;
  }
  
  public String getPath() {
    return this.path;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\SFUtil$PartClassSearchInfo.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */