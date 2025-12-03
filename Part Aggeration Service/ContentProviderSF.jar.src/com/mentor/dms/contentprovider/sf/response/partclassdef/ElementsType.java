package com.mentor.dms.contentprovider.sf.response.partclassdef;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.HashMap;

public class ElementsType {
  @SerializedName("__typeDef__")
  @Expose
  public String typeDef;
  
  @SerializedName("__complexTypeDef__")
  @Expose
  public String complexTypeDef;
  
  @SerializedName("fields")
  @Expose
  public HashMap<String, Type> fields;
  
  public String getTypeDef() {
    return this.typeDef;
  }
  
  public String getComplexTypeDef() {
    return this.complexTypeDef;
  }
  
  public HashMap<String, Type> getFields() {
    return this.fields;
  }
  
  public void setTypeDef(String paramString) {
    this.typeDef = paramString;
  }
  
  public void setComplexTypeDef(String paramString) {
    this.complexTypeDef = paramString;
  }
  
  public void setFields(HashMap<String, Type> paramHashMap) {
    this.fields = paramHashMap;
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (!(paramObject instanceof ElementsType))
      return false; 
    ElementsType elementsType = (ElementsType)paramObject;
    if (!elementsType.canEqual(this))
      return false; 
    String str1 = getTypeDef();
    String str2 = elementsType.getTypeDef();
    if ((str1 == null) ? (str2 != null) : !str1.equals(str2))
      return false; 
    String str3 = getComplexTypeDef();
    String str4 = elementsType.getComplexTypeDef();
    if ((str3 == null) ? (str4 != null) : !str3.equals(str4))
      return false; 
    HashMap<String, Type> hashMap1 = getFields();
    HashMap<String, Type> hashMap2 = elementsType.getFields();
    return !((hashMap1 == null) ? (hashMap2 != null) : !hashMap1.equals(hashMap2));
  }
  
  protected boolean canEqual(Object paramObject) {
    return paramObject instanceof ElementsType;
  }
  
  public int hashCode() {
    null = 1;
    String str1 = getTypeDef();
    null = null * 59 + ((str1 == null) ? 43 : str1.hashCode());
    String str2 = getComplexTypeDef();
    null = null * 59 + ((str2 == null) ? 43 : str2.hashCode());
    HashMap<String, Type> hashMap = getFields();
    return null * 59 + ((hashMap == null) ? 43 : hashMap.hashCode());
  }
  
  public String toString() {
    return "ElementsType(typeDef=" + getTypeDef() + ", complexTypeDef=" + getComplexTypeDef() + ", fields=" + String.valueOf(getFields()) + ")";
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\response\partclassdef\ElementsType.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */