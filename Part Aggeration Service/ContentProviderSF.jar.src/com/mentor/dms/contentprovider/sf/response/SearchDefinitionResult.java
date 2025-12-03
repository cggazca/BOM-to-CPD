package com.mentor.dms.contentprovider.sf.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class SearchDefinitionResult {
  @SerializedName("propertyDefinitions")
  @Expose
  private List<DefinitionProperty> propertyDefinitions;
  
  public List<DefinitionProperty> getPropertyDefinitions() {
    return this.propertyDefinitions;
  }
  
  public void setPropertyDefinitions(List<DefinitionProperty> paramList) {
    this.propertyDefinitions = paramList;
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (!(paramObject instanceof SearchDefinitionResult))
      return false; 
    SearchDefinitionResult searchDefinitionResult = (SearchDefinitionResult)paramObject;
    if (!searchDefinitionResult.canEqual(this))
      return false; 
    List<DefinitionProperty> list1 = getPropertyDefinitions();
    List<DefinitionProperty> list2 = searchDefinitionResult.getPropertyDefinitions();
    return !((list1 == null) ? (list2 != null) : !list1.equals(list2));
  }
  
  protected boolean canEqual(Object paramObject) {
    return paramObject instanceof SearchDefinitionResult;
  }
  
  public int hashCode() {
    null = 1;
    List<DefinitionProperty> list = getPropertyDefinitions();
    return null * 59 + ((list == null) ? 43 : list.hashCode());
  }
  
  public String toString() {
    return "SearchDefinitionResult(propertyDefinitions=" + String.valueOf(getPropertyDefinitions()) + ")";
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\response\SearchDefinitionResult.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */