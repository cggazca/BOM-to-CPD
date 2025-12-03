package com.mentor.dms.contentprovider.sf.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.Map;

public class DefinitionProperty {
  @SerializedName("id")
  @Expose
  private String id;
  
  @SerializedName("name")
  @Expose
  private String name;
  
  @SerializedName("type")
  @Expose
  private Map<String, Object> type;
  
  public String getId() {
    return this.id;
  }
  
  public String getName() {
    return this.name;
  }
  
  public Map<String, Object> getType() {
    return this.type;
  }
  
  public void setId(String paramString) {
    this.id = paramString;
  }
  
  public void setName(String paramString) {
    this.name = paramString;
  }
  
  public void setType(Map<String, Object> paramMap) {
    this.type = paramMap;
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (!(paramObject instanceof DefinitionProperty))
      return false; 
    DefinitionProperty definitionProperty = (DefinitionProperty)paramObject;
    if (!definitionProperty.canEqual(this))
      return false; 
    String str1 = getId();
    String str2 = definitionProperty.getId();
    if ((str1 == null) ? (str2 != null) : !str1.equals(str2))
      return false; 
    String str3 = getName();
    String str4 = definitionProperty.getName();
    if ((str3 == null) ? (str4 != null) : !str3.equals(str4))
      return false; 
    Map<String, Object> map1 = getType();
    Map<String, Object> map2 = definitionProperty.getType();
    return !((map1 == null) ? (map2 != null) : !map1.equals(map2));
  }
  
  protected boolean canEqual(Object paramObject) {
    return paramObject instanceof DefinitionProperty;
  }
  
  public int hashCode() {
    null = 1;
    String str1 = getId();
    null = null * 59 + ((str1 == null) ? 43 : str1.hashCode());
    String str2 = getName();
    null = null * 59 + ((str2 == null) ? 43 : str2.hashCode());
    Map<String, Object> map = getType();
    return null * 59 + ((map == null) ? 43 : map.hashCode());
  }
  
  public String toString() {
    return "DefinitionProperty(id=" + getId() + ", name=" + getName() + ", type=" + String.valueOf(getType()) + ")";
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\response\DefinitionProperty.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */