package com.mentor.dms.contentprovider.sf.mapping;

public class MappingProperty {
  private String propName;
  
  private Object propValue;
  
  public MappingProperty(String paramString, Object paramObject) {
    this.propName = paramString;
    this.propValue = paramObject;
  }
  
  public String getName() {
    return this.propName;
  }
  
  public Object getValue() {
    return this.propValue;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\mapping\MappingProperty.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */