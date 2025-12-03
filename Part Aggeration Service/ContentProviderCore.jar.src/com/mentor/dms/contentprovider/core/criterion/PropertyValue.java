package com.mentor.dms.contentprovider.core.criterion;

public class PropertyValue {
  Object value;
  
  String units;
  
  public PropertyValue(Object paramObject, String paramString) {
    this.value = paramObject;
    this.units = paramString;
  }
  
  public String getUnits() {
    return this.units;
  }
  
  public Object getValue() {
    return this.value;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\criterion\PropertyValue.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */