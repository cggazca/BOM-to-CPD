package com.mentor.dms.contentprovider.sf.response.partclassdef;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Detail {
  @SerializedName("id")
  @Expose
  public String id;
  
  @SerializedName("name")
  @Expose
  public String name;
  
  @SerializedName("properties")
  @Expose
  public List<Property> properties = null;
  
  public String getId() {
    return this.id;
  }
  
  public String getName() {
    return this.name;
  }
  
  public List<Property> getProperties() {
    return this.properties;
  }
  
  public void setId(String paramString) {
    this.id = paramString;
  }
  
  public void setName(String paramString) {
    this.name = paramString;
  }
  
  public void setProperties(List<Property> paramList) {
    this.properties = paramList;
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (!(paramObject instanceof Detail))
      return false; 
    Detail detail = (Detail)paramObject;
    if (!detail.canEqual(this))
      return false; 
    String str1 = getId();
    String str2 = detail.getId();
    if ((str1 == null) ? (str2 != null) : !str1.equals(str2))
      return false; 
    String str3 = getName();
    String str4 = detail.getName();
    if ((str3 == null) ? (str4 != null) : !str3.equals(str4))
      return false; 
    List<Property> list1 = getProperties();
    List<Property> list2 = detail.getProperties();
    return !((list1 == null) ? (list2 != null) : !list1.equals(list2));
  }
  
  protected boolean canEqual(Object paramObject) {
    return paramObject instanceof Detail;
  }
  
  public int hashCode() {
    null = 1;
    String str1 = getId();
    null = null * 59 + ((str1 == null) ? 43 : str1.hashCode());
    String str2 = getName();
    null = null * 59 + ((str2 == null) ? 43 : str2.hashCode());
    List<Property> list = getProperties();
    return null * 59 + ((list == null) ? 43 : list.hashCode());
  }
  
  public String toString() {
    return "Detail(id=" + getId() + ", name=" + getName() + ", properties=" + String.valueOf(getProperties()) + ")";
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\response\partclassdef\Detail.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */