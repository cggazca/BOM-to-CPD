package com.mentor.dms.contentprovider.core.config.xml;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"partClasses", "manufacturerPartCatalogs", "componentCatalogs", "manufacturers"})
@XmlRootElement(name = "ContentProviderCfg")
public class ConfigXMLContentProviderCfg extends BaseXMLClass {
  @XmlElement(name = "PartClasses", required = true)
  protected ConfigXMLPartClasses partClasses;
  
  @XmlElement(name = "ManufacturerPartCatalogs")
  protected ConfigXMLManufacturerPartCatalogs manufacturerPartCatalogs;
  
  @XmlElement(name = "ComponentCatalogs")
  protected ConfigXMLComponentCatalogs componentCatalogs;
  
  @XmlElement(name = "Manufacturers")
  protected ConfigXMLManufacturers manufacturers;
  
  @XmlAttribute(name = "defaultInherit")
  protected Boolean defaultInherit;
  
  public ConfigXMLPartClasses getPartClasses() {
    return this.partClasses;
  }
  
  public void setPartClasses(ConfigXMLPartClasses paramConfigXMLPartClasses) {
    this.partClasses = paramConfigXMLPartClasses;
  }
  
  public ConfigXMLManufacturerPartCatalogs getManufacturerPartCatalogs() {
    return this.manufacturerPartCatalogs;
  }
  
  public void setManufacturerPartCatalogs(ConfigXMLManufacturerPartCatalogs paramConfigXMLManufacturerPartCatalogs) {
    this.manufacturerPartCatalogs = paramConfigXMLManufacturerPartCatalogs;
  }
  
  public ConfigXMLComponentCatalogs getComponentCatalogs() {
    return this.componentCatalogs;
  }
  
  public void setComponentCatalogs(ConfigXMLComponentCatalogs paramConfigXMLComponentCatalogs) {
    this.componentCatalogs = paramConfigXMLComponentCatalogs;
  }
  
  public ConfigXMLManufacturers getManufacturers() {
    return this.manufacturers;
  }
  
  public void setManufacturers(ConfigXMLManufacturers paramConfigXMLManufacturers) {
    this.manufacturers = paramConfigXMLManufacturers;
  }
  
  public boolean isDefaultInherit() {
    return (this.defaultInherit == null) ? true : this.defaultInherit.booleanValue();
  }
  
  public void setDefaultInherit(Boolean paramBoolean) {
    this.defaultInherit = paramBoolean;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\config\xml\ConfigXMLContentProviderCfg.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */