package com.mentor.dms.contentprovider.utils.setrepmpn.config;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BaseValueType")
@XmlSeeAlso({SetRepMPNCfgRollupValueType.class, SetRepMPNCfgPriorityValueType.class})
public class SetRepMPNCfgBaseValueType {
  @XmlAttribute(required = true)
  @XmlSchemaType(name = "anySimpleType")
  protected String value;
  
  @XmlAttribute
  protected Boolean regex;
  
  public String getValue() {
    return this.value;
  }
  
  public void setValue(String paramString) {
    this.value = paramString;
  }
  
  public boolean isRegex() {
    return (this.regex == null) ? false : this.regex.booleanValue();
  }
  
  public void setRegex(Boolean paramBoolean) {
    this.regex = paramBoolean;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovide\\utils\setrepmpn\config\SetRepMPNCfgBaseValueType.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */