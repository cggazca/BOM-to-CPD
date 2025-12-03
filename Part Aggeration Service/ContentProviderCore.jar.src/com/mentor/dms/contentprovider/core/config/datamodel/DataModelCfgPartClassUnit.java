package com.mentor.dms.contentprovider.core.config.datamodel;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "PartClassUnit")
public class DataModelCfgPartClassUnit {
  @XmlAttribute
  @XmlSchemaType(name = "anySimpleType")
  protected String property;
  
  @XmlAttribute
  @XmlSchemaType(name = "anySimpleType")
  protected String fromUnit;
  
  @XmlAttribute(required = true)
  @XmlSchemaType(name = "anySimpleType")
  protected String toUnit;
  
  public String getProperty() {
    return this.property;
  }
  
  public void setProperty(String paramString) {
    this.property = paramString;
  }
  
  public String getFromUnit() {
    return this.fromUnit;
  }
  
  public void setFromUnit(String paramString) {
    this.fromUnit = paramString;
  }
  
  public String getToUnit() {
    return this.toUnit;
  }
  
  public void setToUnit(String paramString) {
    this.toUnit = paramString;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\config\datamodel\DataModelCfgPartClassUnit.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */