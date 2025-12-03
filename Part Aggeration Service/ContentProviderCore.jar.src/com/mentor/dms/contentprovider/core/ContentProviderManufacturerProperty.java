package com.mentor.dms.contentprovider.core;

public class ContentProviderManufacturerProperty {
  private String name;
  
  private String value;
  
  private MfgPropertyType propType;
  
  public ContentProviderManufacturerProperty(String paramString1, String paramString2, MfgPropertyType paramMfgPropertyType) {
    this.name = paramString1;
    this.value = paramString2;
    this.propType = paramMfgPropertyType;
  }
  
  public String getName() {
    return this.name;
  }
  
  public String getValue() {
    return this.value;
  }
  
  public MfgPropertyType getPropType() {
    return this.propType;
  }
  
  public enum MfgPropertyType {
    OBJECT_ID, STATUS, SHORT_NAME, LONG_NAME, MFG_CODE, CAGE_CODE, DUNS_NUMBER, ADDRESS, ADDRESS_LINE1, ADDRESS_LINE2, ADDRESS_LINE3, CITY, REGION_STATE, PHONE_NUMBER, COUNTRY_CODE, FAX_NUMBER, EMAIL_ADDRESS, INTERNET_ADDRESS, ZIPCODE, NOTES, OTHER;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\ContentProviderManufacturerProperty.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */