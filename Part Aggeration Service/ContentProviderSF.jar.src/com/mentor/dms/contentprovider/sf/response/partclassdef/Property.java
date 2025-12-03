package com.mentor.dms.contentprovider.sf.response.partclassdef;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Property {
  public static final String TYPE_SCALAR = "Scalar";
  
  public static final String TYPE_COLLECTION = "Collection";
  
  public static final String TYPE_COMPLEX = "Complex";
  
  @SerializedName("id")
  @Expose
  public String id;
  
  @SerializedName("name")
  @Expose
  public String name;
  
  @SerializedName("type")
  @Expose
  public Type type;
  
  @SerializedName("inheritedFrom")
  @Expose
  public String inheritedFrom;
  
  public String getTypeString() {
    return this.type.getTypeDef().equals("Scalar") ? this.type.getScalarTypeDef() : (this.type.getTypeDef().equals("Complex") ? this.type.getComplexTypeDef() : (this.type.getTypeDef().equals("Collection") ? this.type.getTypeDef() : this.type.getScalarTypeDef()));
  }
  
  public String getId() {
    return this.id;
  }
  
  public String getName() {
    return this.name;
  }
  
  public Type getType() {
    return this.type;
  }
  
  public String getInheritedFrom() {
    return this.inheritedFrom;
  }
  
  public void setId(String paramString) {
    this.id = paramString;
  }
  
  public void setName(String paramString) {
    this.name = paramString;
  }
  
  public void setType(Type paramType) {
    this.type = paramType;
  }
  
  public void setInheritedFrom(String paramString) {
    this.inheritedFrom = paramString;
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (!(paramObject instanceof Property))
      return false; 
    Property property = (Property)paramObject;
    if (!property.canEqual(this))
      return false; 
    String str1 = getId();
    String str2 = property.getId();
    if ((str1 == null) ? (str2 != null) : !str1.equals(str2))
      return false; 
    String str3 = getName();
    String str4 = property.getName();
    if ((str3 == null) ? (str4 != null) : !str3.equals(str4))
      return false; 
    Type type1 = getType();
    Type type2 = property.getType();
    if ((type1 == null) ? (type2 != null) : !type1.equals(type2))
      return false; 
    String str5 = getInheritedFrom();
    String str6 = property.getInheritedFrom();
    return !((str5 == null) ? (str6 != null) : !str5.equals(str6));
  }
  
  protected boolean canEqual(Object paramObject) {
    return paramObject instanceof Property;
  }
  
  public int hashCode() {
    null = 1;
    String str1 = getId();
    null = null * 59 + ((str1 == null) ? 43 : str1.hashCode());
    String str2 = getName();
    null = null * 59 + ((str2 == null) ? 43 : str2.hashCode());
    Type type = getType();
    null = null * 59 + ((type == null) ? 43 : type.hashCode());
    String str3 = getInheritedFrom();
    return null * 59 + ((str3 == null) ? 43 : str3.hashCode());
  }
  
  public String toString() {
    return "Property(id=" + getId() + ", name=" + getName() + ", type=" + String.valueOf(getType()) + ", inheritedFrom=" + getInheritedFrom() + ")";
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\response\partclassdef\Property.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */