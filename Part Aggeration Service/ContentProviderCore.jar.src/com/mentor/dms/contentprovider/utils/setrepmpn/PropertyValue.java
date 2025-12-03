package com.mentor.dms.contentprovider.utils.setrepmpn;

public class PropertyValue implements Comparable<PropertyValue> {
  private Object value = null;
  
  private String strValue = null;
  
  public PropertyValue(String paramString) {
    this.strValue = paramString;
  }
  
  public PropertyValue(Object paramObject, String paramString) {
    this.value = paramObject;
    this.strValue = paramString;
  }
  
  public PropertyValue() {}
  
  public Object getValue() {
    return this.value;
  }
  
  public void setValue(Object paramObject) {
    this.value = paramObject;
  }
  
  public String getStringValue() {
    return this.strValue;
  }
  
  public void setStringValue(String paramString) {
    this.strValue = paramString;
  }
  
  public int compareTo(PropertyValue paramPropertyValue) {
    return compare(getValue(), paramPropertyValue.getValue());
  }
  
  final int compare(Object paramObject1, Object paramObject2) {
    return ((Comparable<Object>)paramObject1).compareTo(paramObject2);
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovide\\utils\setrepmpn\PropertyValue.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */