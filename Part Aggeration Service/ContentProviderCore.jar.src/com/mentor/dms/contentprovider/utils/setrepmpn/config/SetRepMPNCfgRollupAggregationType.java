package com.mentor.dms.contentprovider.utils.setrepmpn.config;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;

@XmlType(name = "RollupAggregationType")
@XmlEnum
public enum SetRepMPNCfgRollupAggregationType {
  AVG, COUNT, MAX, MIN, SUM;
  
  public String value() {
    return name();
  }
  
  public static SetRepMPNCfgRollupAggregationType fromValue(String paramString) {
    return valueOf(paramString);
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovide\\utils\setrepmpn\config\SetRepMPNCfgRollupAggregationType.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */