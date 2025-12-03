package com.mentor.dms.contentprovider.core.config.datamodel;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"partClasses"})
@XmlRootElement(name = "DoubleMapping")
public class DataModelCfgDoubleMapping {
  @XmlElement(name = "PartClasses", required = true)
  protected DataModelCfgPartClasses partClasses;
  
  @XmlAttribute(required = true)
  @XmlSchemaType(name = "anySimpleType")
  protected String property;
  
  public DataModelCfgPartClasses getPartClasses() {
    return this.partClasses;
  }
  
  public void setPartClasses(DataModelCfgPartClasses paramDataModelCfgPartClasses) {
    this.partClasses = paramDataModelCfgPartClasses;
  }
  
  public String getProperty() {
    return this.property;
  }
  
  public void setProperty(String paramString) {
    this.property = paramString;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\config\datamodel\DataModelCfgDoubleMapping.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */