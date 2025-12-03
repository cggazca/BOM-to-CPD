package com.mentor.dms.contentprovider.core.config.xml;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElements;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"manufacturerPartPropertyMapOrScriptedManufacturerPartPropertyMap"})
@XmlRootElement(name = "ManufacturerPartPropertyMaps")
public class ConfigXMLManufacturerPartPropertyMaps extends BaseXMLClass {
  @XmlElements({@XmlElement(name = "ManufacturerPartPropertyMap", type = ConfigXMLManufacturerPartPropertyMap.class), @XmlElement(name = "ScriptedManufacturerPartPropertyMap", type = ConfigXMLScriptedManufacturerPartPropertyMap.class)})
  protected List<BaseXMLClass> manufacturerPartPropertyMapOrScriptedManufacturerPartPropertyMap;
  
  @XmlAttribute(name = "defaultSyncType")
  protected ConfigXMLSyncType defaultSyncType;
  
  public List<BaseXMLClass> getManufacturerPartPropertyMapOrScriptedManufacturerPartPropertyMap() {
    if (this.manufacturerPartPropertyMapOrScriptedManufacturerPartPropertyMap == null)
      this.manufacturerPartPropertyMapOrScriptedManufacturerPartPropertyMap = new ArrayList<>(); 
    return this.manufacturerPartPropertyMapOrScriptedManufacturerPartPropertyMap;
  }
  
  public ConfigXMLSyncType getDefaultSyncType() {
    return this.defaultSyncType;
  }
  
  public void setDefaultSyncType(ConfigXMLSyncType paramConfigXMLSyncType) {
    this.defaultSyncType = paramConfigXMLSyncType;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\config\xml\ConfigXMLManufacturerPartPropertyMaps.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */