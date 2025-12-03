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
@XmlType(name = "", propOrder = {"componentCatalog"})
@XmlRootElement(name = "ComponentCatalogs")
public class ConfigXMLComponentCatalogs extends BaseXMLClass {
  @XmlElement(name = "ComponentCatalog")
  protected List<ConfigXMLComponentCatalog> componentCatalog;
  
  @XmlAttribute(name = "autoMapping")
  protected Boolean autoMapping;
  
  @XmlAttribute(name = "defaultSyncType")
  protected ConfigXMLSyncType defaultSyncType;
  
  @XmlAttribute(name = "allowMoveParts")
  protected Boolean allowMoveParts;
  
  public List<ConfigXMLComponentCatalog> getComponentCatalog() {
    if (this.componentCatalog == null)
      this.componentCatalog = new ArrayList<>(); 
    return this.componentCatalog;
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
  
  public boolean isAllowMoveParts() {
    return (this.allowMoveParts == null) ? true : this.allowMoveParts.booleanValue();
  }
  
  public void setAllowMoveParts(Boolean paramBoolean) {
    this.allowMoveParts = paramBoolean;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\config\xml\ConfigXMLComponentCatalogs.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */