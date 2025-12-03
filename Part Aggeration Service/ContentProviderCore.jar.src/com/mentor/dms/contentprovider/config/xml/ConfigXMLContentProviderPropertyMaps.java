package com.mentor.dms.contentprovider.config.xml;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"contentProviderPropertyMap"})
@XmlRootElement(name = "ContentProviderPropertyMaps")
public class ConfigXMLContentProviderPropertyMaps extends BaseXMLClass {
  @XmlElement(name = "ContentProviderPropertyMap")
  protected List<ConfigXMLContentProviderPropertyMap> contentProviderPropertyMap;
  
  @XmlAttribute(name = "defaultSyncType")
  protected ConfigXMLSyncType defaultSyncType;
  
  public List<ConfigXMLContentProviderPropertyMap> getContentProviderPropertyMap() {
    if (this.contentProviderPropertyMap == null)
      this.contentProviderPropertyMap = new ArrayList<>(); 
    return this.contentProviderPropertyMap;
  }
  
  public ConfigXMLSyncType getDefaultSyncType() {
    return this.defaultSyncType;
  }
  
  public void setDefaultSyncType(ConfigXMLSyncType paramConfigXMLSyncType) {
    this.defaultSyncType = paramConfigXMLSyncType;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\config\xml\ConfigXMLContentProviderPropertyMaps.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */