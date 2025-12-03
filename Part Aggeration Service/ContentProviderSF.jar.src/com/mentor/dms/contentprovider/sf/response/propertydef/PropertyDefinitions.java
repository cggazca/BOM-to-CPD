package com.mentor.dms.contentprovider.sf.response.propertydef;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PropertyDefinitions {
  @SerializedName("id")
  @Expose
  public String id;
  
  @SerializedName("name")
  @Expose
  public String name;
  
  @SerializedName("type")
  @Expose
  public PropType type;
  
  public String getId() {
    return this.id;
  }
  
  public String getName() {
    return this.name;
  }
  
  public PropType getType() {
    return this.type;
  }
  
  public void setId(String paramString) {
    this.id = paramString;
  }
  
  public void setName(String paramString) {
    this.name = paramString;
  }
  
  public void setType(PropType paramPropType) {
    this.type = paramPropType;
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (!(paramObject instanceof PropertyDefinitions))
      return false; 
    PropertyDefinitions propertyDefinitions = (PropertyDefinitions)paramObject;
    if (!propertyDefinitions.canEqual(this))
      return false; 
    String str1 = getId();
    String str2 = propertyDefinitions.getId();
    if ((str1 == null) ? (str2 != null) : !str1.equals(str2))
      return false; 
    String str3 = getName();
    String str4 = propertyDefinitions.getName();
    if ((str3 == null) ? (str4 != null) : !str3.equals(str4))
      return false; 
    PropType propType1 = getType();
    PropType propType2 = propertyDefinitions.getType();
    return !((propType1 == null) ? (propType2 != null) : !propType1.equals(propType2));
  }
  
  protected boolean canEqual(Object paramObject) {
    return paramObject instanceof PropertyDefinitions;
  }
  
  public int hashCode() {
    null = 1;
    String str1 = getId();
    null = null * 59 + ((str1 == null) ? 43 : str1.hashCode());
    String str2 = getName();
    null = null * 59 + ((str2 == null) ? 43 : str2.hashCode());
    PropType propType = getType();
    return null * 59 + ((propType == null) ? 43 : propType.hashCode());
  }
  
  public String toString() {
    return "PropertyDefinitions(id=" + getId() + ", name=" + getName() + ", type=" + String.valueOf(getType()) + ")";
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\response\propertydef\PropertyDefinitions.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */