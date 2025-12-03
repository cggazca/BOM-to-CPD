package com.mentor.dms.contentprovider.core.config.xml;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "ManufacturerPropertyMap")
public class ConfigXMLManufacturerPropertyMap extends BaseXMLClass {
  @XmlAttribute(name = "dmn", required = true)
  protected String dmn;
  
  @XmlAttribute(name = "ccpId", required = true)
  protected String ccpId;
  
  @XmlAttribute(name = "fieldType")
  protected ConfigXMLMfgFieldType fieldType;
  
  public String getDmn() {
    return this.dmn;
  }
  
  public void setDmn(String paramString) {
    this.dmn = paramString;
  }
  
  public String getCcpId() {
    return this.ccpId;
  }
  
  public void setCcpId(String paramString) {
    this.ccpId = paramString;
  }
  
  public ConfigXMLMfgFieldType getFieldType() {
    return this.fieldType;
  }
  
  public void setFieldType(ConfigXMLMfgFieldType paramConfigXMLMfgFieldType) {
    this.fieldType = paramConfigXMLMfgFieldType;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\config\xml\ConfigXMLManufacturerPropertyMap.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */