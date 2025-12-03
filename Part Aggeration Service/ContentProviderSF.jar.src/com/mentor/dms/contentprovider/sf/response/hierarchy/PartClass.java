package com.mentor.dms.contentprovider.sf.response.hierarchy;

import java.util.List;

public class PartClass {
  private String id;
  
  private String name;
  
  private List<PartClass> descendants;
  
  public String getId() {
    return this.id;
  }
  
  public String getName() {
    return this.name;
  }
  
  public List<PartClass> getDescendants() {
    return this.descendants;
  }
  
  public void setId(String paramString) {
    this.id = paramString;
  }
  
  public void setName(String paramString) {
    this.name = paramString;
  }
  
  public void setDescendants(List<PartClass> paramList) {
    this.descendants = paramList;
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (!(paramObject instanceof PartClass))
      return false; 
    PartClass partClass = (PartClass)paramObject;
    if (!partClass.canEqual(this))
      return false; 
    String str1 = getId();
    String str2 = partClass.getId();
    if ((str1 == null) ? (str2 != null) : !str1.equals(str2))
      return false; 
    String str3 = getName();
    String str4 = partClass.getName();
    if ((str3 == null) ? (str4 != null) : !str3.equals(str4))
      return false; 
    List<PartClass> list1 = getDescendants();
    List<PartClass> list2 = partClass.getDescendants();
    return !((list1 == null) ? (list2 != null) : !list1.equals(list2));
  }
  
  protected boolean canEqual(Object paramObject) {
    return paramObject instanceof PartClass;
  }
  
  public int hashCode() {
    null = 1;
    String str1 = getId();
    null = null * 59 + ((str1 == null) ? 43 : str1.hashCode());
    String str2 = getName();
    null = null * 59 + ((str2 == null) ? 43 : str2.hashCode());
    List<PartClass> list = getDescendants();
    return null * 59 + ((list == null) ? 43 : list.hashCode());
  }
  
  public String toString() {
    return "PartClass(id=" + getId() + ", name=" + getName() + ", descendants=" + String.valueOf(getDescendants()) + ")";
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\response\hierarchy\PartClass.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */