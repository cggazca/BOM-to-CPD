package com.mentor.dms.contentprovider.utils.setrepmpn.config;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RollupValueType")
public class SetRepMPNCfgRollupValueType extends SetRepMPNCfgBaseValueType {
  @XmlAttribute
  @XmlSchemaType(name = "anySimpleType")
  protected String compValue;
  
  public String getCompValue() {
    return this.compValue;
  }
  
  public void setCompValue(String paramString) {
    this.compValue = paramString;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovide\\utils\setrepmpn\config\SetRepMPNCfgRollupValueType.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */