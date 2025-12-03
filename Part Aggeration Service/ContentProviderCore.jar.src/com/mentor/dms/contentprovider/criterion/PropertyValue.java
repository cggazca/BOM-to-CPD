package com.mentor.dms.contentprovider.criterion;

public class PropertyValue {
  String value;
  
  String units;
  
  public PropertyValue(String paramString1, String paramString2) {
    this.value = paramString1;
    this.units = paramString2;
  }
  
  public String getUnits() {
    return this.units;
  }
  
  public String getValue() {
    return this.value;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\criterion\PropertyValue.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */