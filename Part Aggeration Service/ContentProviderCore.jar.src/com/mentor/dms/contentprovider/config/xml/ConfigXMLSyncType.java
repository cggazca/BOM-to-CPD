package com.mentor.dms.contentprovider.config.xml;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;

@XmlType(name = "syncType")
@XmlEnum
public enum ConfigXMLSyncType {
  DIRECT, SYNC, IGNORE, CREATEONLY;
  
  public String value() {
    return name();
  }
  
  public static ConfigXMLSyncType fromValue(String paramString) {
    return valueOf(paramString);
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\config\xml\ConfigXMLSyncType.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */