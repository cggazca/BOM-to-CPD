package com.mentor.dms.contentprovider.core.utils.validate;

public class CPProperty {
  private String id;
  
  private String name;
  
  private String type;
  
  private String unit;
  
  private boolean searchable = false;
  
  public CPProperty(String paramString1, String paramString2, String paramString3) {
    this.id = paramString1;
    this.name = paramString2;
    this.type = paramString3;
  }
  
  public String getUnit() {
    return this.unit;
  }
  
  public void setUnit(String paramString) {
    this.unit = paramString;
  }
  
  public boolean isSearchable() {
    return this.searchable;
  }
  
  public void setSearchable(boolean paramBoolean) {
    this.searchable = paramBoolean;
  }
  
  public String getId() {
    return this.id;
  }
  
  public String getName() {
    return this.name;
  }
  
  public String getType() {
    return this.type;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\cor\\utils\validate\CPProperty.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */