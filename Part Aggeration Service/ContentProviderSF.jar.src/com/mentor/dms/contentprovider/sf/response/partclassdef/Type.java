package com.mentor.dms.contentprovider.sf.response.partclassdef;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Type {
  @SerializedName("__typeDef__")
  @Expose
  public String typeDef;
  
  @SerializedName("__scalarTypeDef__")
  @Expose
  public String scalarTypeDef;
  
  @SerializedName("caseSensitive")
  @Expose
  public Boolean caseSensitive;
  
  @SerializedName("elementsType")
  @Expose
  public ElementsType elementsType;
  
  @SerializedName("unit")
  @Expose
  public String unit;
  
  @SerializedName("__complexTypeDef__")
  @Expose
  public String complexTypeDef;
  
  public String getTypeDef() {
    return this.typeDef;
  }
  
  public String getScalarTypeDef() {
    return this.scalarTypeDef;
  }
  
  public Boolean getCaseSensitive() {
    return this.caseSensitive;
  }
  
  public ElementsType getElementsType() {
    return this.elementsType;
  }
  
  public String getUnit() {
    return this.unit;
  }
  
  public String getComplexTypeDef() {
    return this.complexTypeDef;
  }
  
  public void setTypeDef(String paramString) {
    this.typeDef = paramString;
  }
  
  public void setScalarTypeDef(String paramString) {
    this.scalarTypeDef = paramString;
  }
  
  public void setCaseSensitive(Boolean paramBoolean) {
    this.caseSensitive = paramBoolean;
  }
  
  public void setElementsType(ElementsType paramElementsType) {
    this.elementsType = paramElementsType;
  }
  
  public void setUnit(String paramString) {
    this.unit = paramString;
  }
  
  public void setComplexTypeDef(String paramString) {
    this.complexTypeDef = paramString;
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (!(paramObject instanceof Type))
      return false; 
    Type type = (Type)paramObject;
    if (!type.canEqual(this))
      return false; 
    Boolean bool1 = getCaseSensitive();
    Boolean bool2 = type.getCaseSensitive();
    if ((bool1 == null) ? (bool2 != null) : !bool1.equals(bool2))
      return false; 
    String str1 = getTypeDef();
    String str2 = type.getTypeDef();
    if ((str1 == null) ? (str2 != null) : !str1.equals(str2))
      return false; 
    String str3 = getScalarTypeDef();
    String str4 = type.getScalarTypeDef();
    if ((str3 == null) ? (str4 != null) : !str3.equals(str4))
      return false; 
    ElementsType elementsType1 = getElementsType();
    ElementsType elementsType2 = type.getElementsType();
    if ((elementsType1 == null) ? (elementsType2 != null) : !elementsType1.equals(elementsType2))
      return false; 
    String str5 = getUnit();
    String str6 = type.getUnit();
    if ((str5 == null) ? (str6 != null) : !str5.equals(str6))
      return false; 
    String str7 = getComplexTypeDef();
    String str8 = type.getComplexTypeDef();
    return !((str7 == null) ? (str8 != null) : !str7.equals(str8));
  }
  
  protected boolean canEqual(Object paramObject) {
    return paramObject instanceof Type;
  }
  
  public int hashCode() {
    null = 1;
    Boolean bool = getCaseSensitive();
    null = null * 59 + ((bool == null) ? 43 : bool.hashCode());
    String str1 = getTypeDef();
    null = null * 59 + ((str1 == null) ? 43 : str1.hashCode());
    String str2 = getScalarTypeDef();
    null = null * 59 + ((str2 == null) ? 43 : str2.hashCode());
    ElementsType elementsType = getElementsType();
    null = null * 59 + ((elementsType == null) ? 43 : elementsType.hashCode());
    String str3 = getUnit();
    null = null * 59 + ((str3 == null) ? 43 : str3.hashCode());
    String str4 = getComplexTypeDef();
    return null * 59 + ((str4 == null) ? 43 : str4.hashCode());
  }
  
  public String toString() {
    return "Type(typeDef=" + getTypeDef() + ", scalarTypeDef=" + getScalarTypeDef() + ", caseSensitive=" + getCaseSensitive() + ", elementsType=" + String.valueOf(getElementsType()) + ", unit=" + getUnit() + ", complexTypeDef=" + getComplexTypeDef() + ")";
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\response\partclassdef\Type.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */