package com.mentor.dms.contentprovider.sf.response.hierarchy;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClassHierarchyData {
  @SerializedName("result")
  @Expose
  public Result result;
  
  @SerializedName("success")
  @Expose
  public Boolean success;
  
  public Result getResult() {
    return this.result;
  }
  
  public Boolean getSuccess() {
    return this.success;
  }
  
  public void setResult(Result paramResult) {
    this.result = paramResult;
  }
  
  public void setSuccess(Boolean paramBoolean) {
    this.success = paramBoolean;
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (!(paramObject instanceof ClassHierarchyData))
      return false; 
    ClassHierarchyData classHierarchyData = (ClassHierarchyData)paramObject;
    if (!classHierarchyData.canEqual(this))
      return false; 
    Boolean bool1 = getSuccess();
    Boolean bool2 = classHierarchyData.getSuccess();
    if ((bool1 == null) ? (bool2 != null) : !bool1.equals(bool2))
      return false; 
    Result result1 = getResult();
    Result result2 = classHierarchyData.getResult();
    return !((result1 == null) ? (result2 != null) : !result1.equals(result2));
  }
  
  protected boolean canEqual(Object paramObject) {
    return paramObject instanceof ClassHierarchyData;
  }
  
  public int hashCode() {
    null = 1;
    Boolean bool = getSuccess();
    null = null * 59 + ((bool == null) ? 43 : bool.hashCode());
    Result result = getResult();
    return null * 59 + ((result == null) ? 43 : result.hashCode());
  }
  
  public String toString() {
    return "ClassHierarchyData(result=" + String.valueOf(getResult()) + ", success=" + getSuccess() + ")";
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\response\hierarchy\ClassHierarchyData.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */