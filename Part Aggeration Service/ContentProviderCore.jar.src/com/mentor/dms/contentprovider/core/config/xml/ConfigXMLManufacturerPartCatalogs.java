package com.mentor.dms.contentprovider.core.config.xml;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"manufacturerPartCatalog"})
@XmlRootElement(name = "ManufacturerPartCatalogs")
public class ConfigXMLManufacturerPartCatalogs extends BaseXMLClass {
  @XmlElement(name = "ManufacturerPartCatalog")
  protected List<ConfigXMLManufacturerPartCatalog> manufacturerPartCatalog;
  
  @XmlAttribute(name = "autoMapping")
  protected Boolean autoMapping;
  
  @XmlAttribute(name = "defaultSyncType")
  protected ConfigXMLSyncType defaultSyncType;
  
  public List<ConfigXMLManufacturerPartCatalog> getManufacturerPartCatalog() {
    if (this.manufacturerPartCatalog == null)
      this.manufacturerPartCatalog = new ArrayList<>(); 
    return this.manufacturerPartCatalog;
  }
  
  public boolean isAutoMapping() {
    return (this.autoMapping == null) ? false : this.autoMapping.booleanValue();
  }
  
  public void setAutoMapping(Boolean paramBoolean) {
    this.autoMapping = paramBoolean;
  }
  
  public ConfigXMLSyncType getDefaultSyncType() {
    return (this.defaultSyncType == null) ? ConfigXMLSyncType.DIRECT : this.defaultSyncType;
  }
  
  public void setDefaultSyncType(ConfigXMLSyncType paramConfigXMLSyncType) {
    this.defaultSyncType = paramConfigXMLSyncType;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\config\xml\ConfigXMLManufacturerPartCatalogs.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */