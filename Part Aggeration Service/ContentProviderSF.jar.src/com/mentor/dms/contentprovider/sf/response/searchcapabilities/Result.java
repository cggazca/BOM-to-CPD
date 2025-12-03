package com.mentor.dms.contentprovider.sf.response.searchcapabilities;

import com.google.gson.annotations.Expose;
import java.util.List;

public class Result {
  @Expose
  private List<PropertySearchCapability> propertySearchCapabilities = null;
  
  public List<PropertySearchCapability> getPropertySearchCapabilities() {
    return this.propertySearchCapabilities;
  }
  
  public void setPropertySearchCapabilities(List<PropertySearchCapability> paramList) {
    this.propertySearchCapabilities = paramList;
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (!(paramObject instanceof Result))
      return false; 
    Result result = (Result)paramObject;
    if (!result.canEqual(this))
      return false; 
    List<PropertySearchCapability> list1 = getPropertySearchCapabilities();
    List<PropertySearchCapability> list2 = result.getPropertySearchCapabilities();
    return !((list1 == null) ? (list2 != null) : !list1.equals(list2));
  }
  
  protected boolean canEqual(Object paramObject) {
    return paramObject instanceof Result;
  }
  
  public int hashCode() {
    null = 1;
    List<PropertySearchCapability> list = getPropertySearchCapabilities();
    return null * 59 + ((list == null) ? 43 : list.hashCode());
  }
  
  public String toString() {
    return "Result(propertySearchCapabilities=" + String.valueOf(getPropertySearchCapabilities()) + ")";
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\response\searchcapabilities\Result.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */