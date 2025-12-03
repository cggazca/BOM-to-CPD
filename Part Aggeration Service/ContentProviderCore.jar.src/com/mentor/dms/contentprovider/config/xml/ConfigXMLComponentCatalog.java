package com.mentor.dms.contentprovider.config.xml;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {})
@XmlRootElement(name = "ComponentCatalog")
public class ConfigXMLComponentCatalog extends BaseXMLClass {
  @XmlElement(name = "ManufacturerPartMaps")
  protected ConfigXMLManufacturerPartMaps manufacturerPartMaps;
  
  @XmlElement(name = "ContentProviderMaps")
  protected ConfigXMLContentProviderMaps contentProviderMaps;
  
  @XmlAttribute(name = "classDMN", required = true)
  protected String classDMN;
  
  @XmlAttribute(name = "allowMoveParts")
  protected Boolean allowMoveParts;
  
  public ConfigXMLManufacturerPartMaps getManufacturerPartMaps() {
    return this.manufacturerPartMaps;
  }
  
  public void setManufacturerPartMaps(ConfigXMLManufacturerPartMaps paramConfigXMLManufacturerPartMaps) {
    this.manufacturerPartMaps = paramConfigXMLManufacturerPartMaps;
  }
  
  public ConfigXMLContentProviderMaps getContentProviderMaps() {
    return this.contentProviderMaps;
  }
  
  public void setContentProviderMaps(ConfigXMLContentProviderMaps paramConfigXMLContentProviderMaps) {
    this.contentProviderMaps = paramConfigXMLContentProviderMaps;
  }
  
  public String getClassDMN() {
    return this.classDMN;
  }
  
  public void setClassDMN(String paramString) {
    this.classDMN = paramString;
  }
  
  public boolean isAllowMoveParts() {
    return (this.allowMoveParts == null) ? true : this.allowMoveParts.booleanValue();
  }
  
  public void setAllowMoveParts(Boolean paramBoolean) {
    this.allowMoveParts = paramBoolean;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\config\xml\ConfigXMLComponentCatalog.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */