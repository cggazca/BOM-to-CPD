package com.mentor.dms.contentprovider.core.utils.validate;

public class CPPartClass {
  private String id;
  
  private String label;
  
  private String parentID;
  
  private int childNum = 0;
  
  public CPPartClass(String paramString) {
    this.id = paramString;
  }
  
  public void setLabel(String paramString) {
    this.label = paramString;
  }
  
  public void setParentID(String paramString) {
    this.parentID = paramString;
  }
  
  public String getId() {
    return this.id;
  }
  
  public String getLabel() {
    return this.label;
  }
  
  public String getParentID() {
    return this.parentID;
  }
  
  public void setChildNum(int paramInt) {
    this.childNum = paramInt;
  }
  
  public int getChildNum() {
    return this.childNum;
  }
  
  public boolean isLeafClass() {
    return (this.childNum == 0);
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\cor\\utils\validate\CPPartClass.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */