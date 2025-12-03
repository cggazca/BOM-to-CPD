package com.mentor.dms.contentprovider.core.config.xml;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;

@XmlType(name = "mfgFieldType")
@XmlEnum
public enum ConfigXMLMfgFieldType {
  OBJECT_ID("OBJECT_ID"),
  STATUS("STATUS"),
  SHORT_NAME("SHORT_NAME"),
  LONG_NAME("LONG_NAME"),
  MFG_CODE("MFG_CODE"),
  CAGE_CODE("CAGE_CODE"),
  DUNS_NUMBER("DUNS_NUMBER"),
  ADDRESS("ADDRESS"),
  ADDRESS_LINE_1("ADDRESS_LINE1"),
  ADDRESS_LINE_2("ADDRESS_LINE2"),
  ADDRESS_LINE_3("ADDRESS_LINE3"),
  CITY("CITY"),
  REGION_STATE("REGION_STATE"),
  PHONE_NUMBER("PHONE_NUMBER"),
  COUNTRY_CODE("COUNTRY_CODE"),
  FAX_NUMBER("FAX_NUMBER"),
  EMAIL_ADDRESS("EMAIL_ADDRESS"),
  INTERNET_ADDRESS("INTERNET_ADDRESS"),
  ZIPCODE("ZIPCODE"),
  NOTES("NOTES"),
  OTHER("OTHER");
  
  private final String value;
  
  ConfigXMLMfgFieldType(String paramString1) {
    this.value = paramString1;
  }
  
  public String value() {
    return this.value;
  }
  
  public static ConfigXMLMfgFieldType fromValue(String paramString) {
    for (ConfigXMLMfgFieldType configXMLMfgFieldType : values()) {
      if (configXMLMfgFieldType.value.equals(paramString))
        return configXMLMfgFieldType; 
    } 
    throw new IllegalArgumentException(paramString);
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\config\xml\ConfigXMLMfgFieldType.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */