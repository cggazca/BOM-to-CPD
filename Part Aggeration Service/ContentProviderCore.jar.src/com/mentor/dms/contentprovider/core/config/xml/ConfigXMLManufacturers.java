package com.mentor.dms.contentprovider.core.config.xml;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"manufacturerPropertyMaps"})
@XmlRootElement(name = "Manufacturers")
public class ConfigXMLManufacturers extends BaseXMLClass {
  @XmlElement(name = "ManufacturerPropertyMaps", required = true)
  protected ConfigXMLManufacturerPropertyMaps manufacturerPropertyMaps;
  
  @XmlAttribute(name = "ccpMfgName", required = true)
  protected String ccpMfgName;
  
  @XmlAttribute(name = "ccpMfgId")
  protected String ccpMfgId;
  
  @XmlAttribute(name = "ccpId")
  protected String ccpId;
  
  public ConfigXMLManufacturerPropertyMaps getManufacturerPropertyMaps() {
    return this.manufacturerPropertyMaps;
  }
  
  public void setManufacturerPropertyMaps(ConfigXMLManufacturerPropertyMaps paramConfigXMLManufacturerPropertyMaps) {
    this.manufacturerPropertyMaps = paramConfigXMLManufacturerPropertyMaps;
  }
  
  public String getCcpMfgName() {
    return this.ccpMfgName;
  }
  
  public void setCcpMfgName(String paramString) {
    this.ccpMfgName = paramString;
  }
  
  public String getCcpMfgId() {
    return this.ccpMfgId;
  }
  
  public void setCcpMfgId(String paramString) {
    this.ccpMfgId = paramString;
  }
  
  public String getCcpId() {
    return this.ccpId;
  }
  
  public void setCcpId(String paramString) {
    this.ccpId = paramString;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\config\xml\ConfigXMLManufacturers.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */