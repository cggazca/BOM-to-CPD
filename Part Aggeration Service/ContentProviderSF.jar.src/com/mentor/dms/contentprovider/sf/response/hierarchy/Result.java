package com.mentor.dms.contentprovider.sf.response.hierarchy;

public class Result {
  public PartClass root;
  
  public PartClass getRoot() {
    return this.root;
  }
  
  public void setRoot(PartClass paramPartClass) {
    this.root = paramPartClass;
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (!(paramObject instanceof Result))
      return false; 
    Result result = (Result)paramObject;
    if (!result.canEqual(this))
      return false; 
    PartClass partClass1 = getRoot();
    PartClass partClass2 = result.getRoot();
    return !((partClass1 == null) ? (partClass2 != null) : !partClass1.equals(partClass2));
  }
  
  protected boolean canEqual(Object paramObject) {
    return paramObject instanceof Result;
  }
  
  public int hashCode() {
    null = 1;
    PartClass partClass = getRoot();
    return null * 59 + ((partClass == null) ? 43 : partClass.hashCode());
  }
  
  public String toString() {
    return "Result(root=" + String.valueOf(getRoot()) + ")";
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\response\hierarchy\Result.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */