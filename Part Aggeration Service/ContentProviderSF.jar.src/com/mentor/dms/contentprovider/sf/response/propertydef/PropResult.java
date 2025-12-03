package com.mentor.dms.contentprovider.sf.response.propertydef;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class PropResult {
  @SerializedName("properties")
  @Expose
  public List<PropertyDefinitions> properties = null;
  
  public List<PropertyDefinitions> getProperties() {
    return this.properties;
  }
  
  public void setProperties(List<PropertyDefinitions> paramList) {
    this.properties = paramList;
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (!(paramObject instanceof PropResult))
      return false; 
    PropResult propResult = (PropResult)paramObject;
    if (!propResult.canEqual(this))
      return false; 
    List<PropertyDefinitions> list1 = getProperties();
    List<PropertyDefinitions> list2 = propResult.getProperties();
    return !((list1 == null) ? (list2 != null) : !list1.equals(list2));
  }
  
  protected boolean canEqual(Object paramObject) {
    return paramObject instanceof PropResult;
  }
  
  public int hashCode() {
    null = 1;
    List<PropertyDefinitions> list = getProperties();
    return null * 59 + ((list == null) ? 43 : list.hashCode());
  }
  
  public String toString() {
    return "PropResult(properties=" + String.valueOf(getProperties()) + ")";
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\response\propertydef\PropResult.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */