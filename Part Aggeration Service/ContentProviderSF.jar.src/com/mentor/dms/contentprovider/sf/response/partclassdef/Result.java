package com.mentor.dms.contentprovider.sf.response.partclassdef;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Result {
  @SerializedName("details")
  @Expose
  public List<Detail> details = null;
  
  public List<Detail> getDetails() {
    return this.details;
  }
  
  public void setDetails(List<Detail> paramList) {
    this.details = paramList;
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (!(paramObject instanceof Result))
      return false; 
    Result result = (Result)paramObject;
    if (!result.canEqual(this))
      return false; 
    List<Detail> list1 = getDetails();
    List<Detail> list2 = result.getDetails();
    return !((list1 == null) ? (list2 != null) : !list1.equals(list2));
  }
  
  protected boolean canEqual(Object paramObject) {
    return paramObject instanceof Result;
  }
  
  public int hashCode() {
    null = 1;
    List<Detail> list = getDetails();
    return null * 59 + ((list == null) ? 43 : list.hashCode());
  }
  
  public String toString() {
    return "Result(details=" + String.valueOf(getDetails()) + ")";
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\response\partclassdef\Result.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */