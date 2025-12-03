package com.mentor.dms.contentprovider.core.config.xml;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "ManufacturerPartPropertyMap")
public class ConfigXMLManufacturerPartPropertyMap extends BaseXMLClass {
  @XmlAttribute(name = "compDMN", required = true)
  protected String compDMN;
  
  @XmlAttribute(name = "mpnDMN", required = true)
  protected String mpnDMN;
  
  @XmlAttribute(name = "syncType")
  protected ConfigXMLSyncType syncType;
  
  @XmlAttribute(name = "inherit")
  protected Boolean inherit;
  
  @XmlAttribute(name = "ignoreMissing")
  protected Boolean ignoreMissing;
  
  @XmlAttribute(name = "label")
  @XmlSchemaType(name = "anySimpleType")
  protected String label;
  
  public String getCompDMN() {
    return this.compDMN;
  }
  
  public void setCompDMN(String paramString) {
    this.compDMN = paramString;
  }
  
  public String getMpnDMN() {
    return this.mpnDMN;
  }
  
  public void setMpnDMN(String paramString) {
    this.mpnDMN = paramString;
  }
  
  public ConfigXMLSyncType getSyncType() {
    return this.syncType;
  }
  
  public void setSyncType(ConfigXMLSyncType paramConfigXMLSyncType) {
    this.syncType = paramConfigXMLSyncType;
  }
  
  public Boolean isInherit() {
    return this.inherit;
  }
  
  public void setInherit(Boolean paramBoolean) {
    this.inherit = paramBoolean;
  }
  
  public Boolean isIgnoreMissing() {
    return this.ignoreMissing;
  }
  
  public void setIgnoreMissing(Boolean paramBoolean) {
    this.ignoreMissing = paramBoolean;
  }
  
  public String getLabel() {
    return this.label;
  }
  
  public void setLabel(String paramString) {
    this.label = paramString;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\config\xml\ConfigXMLManufacturerPartPropertyMap.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */