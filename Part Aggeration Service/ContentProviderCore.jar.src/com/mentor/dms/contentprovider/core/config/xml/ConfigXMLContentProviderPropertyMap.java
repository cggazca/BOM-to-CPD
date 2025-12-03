package com.mentor.dms.contentprovider.core.config.xml;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "ContentProviderPropertyMap")
public class ConfigXMLContentProviderPropertyMap extends BaseXMLClass {
  @XmlAttribute(name = "ccpId", required = true)
  protected String ccpId;
  
  @XmlAttribute(name = "dmn")
  protected String dmn;
  
  @XmlAttribute(name = "searchDMN")
  protected String searchDMN;
  
  @XmlAttribute(name = "syncType")
  protected ConfigXMLSyncType syncType;
  
  @XmlAttribute(name = "inherit")
  protected Boolean inherit;
  
  @XmlAttribute(name = "label")
  @XmlSchemaType(name = "anySimpleType")
  protected String label;
  
  public String getCcpId() {
    return this.ccpId;
  }
  
  public void setCcpId(String paramString) {
    this.ccpId = paramString;
  }
  
  public String getDmn() {
    return this.dmn;
  }
  
  public void setDmn(String paramString) {
    this.dmn = paramString;
  }
  
  public String getSearchDMN() {
    return this.searchDMN;
  }
  
  public void setSearchDMN(String paramString) {
    this.searchDMN = paramString;
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
  
  public String getLabel() {
    return this.label;
  }
  
  public void setLabel(String paramString) {
    this.label = paramString;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\config\xml\ConfigXMLContentProviderPropertyMap.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */