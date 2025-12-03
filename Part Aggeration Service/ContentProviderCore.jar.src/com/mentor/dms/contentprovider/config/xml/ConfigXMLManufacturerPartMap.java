package com.mentor.dms.contentprovider.config.xml;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {})
@XmlRootElement(name = "ManufacturerPartMap")
public class ConfigXMLManufacturerPartMap extends BaseXMLClass {
  @XmlElement(name = "ManufacturerPartPropertyMaps")
  protected ConfigXMLManufacturerPartPropertyMaps manufacturerPartPropertyMaps;
  
  @XmlAttribute(name = "classDMN", required = true)
  protected String classDMN;
  
  public ConfigXMLManufacturerPartPropertyMaps getManufacturerPartPropertyMaps() {
    return this.manufacturerPartPropertyMaps;
  }
  
  public void setManufacturerPartPropertyMaps(ConfigXMLManufacturerPartPropertyMaps paramConfigXMLManufacturerPartPropertyMaps) {
    this.manufacturerPartPropertyMaps = paramConfigXMLManufacturerPartPropertyMaps;
  }
  
  public String getClassDMN() {
    return this.classDMN;
  }
  
  public void setClassDMN(String paramString) {
    this.classDMN = paramString;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\config\xml\ConfigXMLManufacturerPartMap.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */