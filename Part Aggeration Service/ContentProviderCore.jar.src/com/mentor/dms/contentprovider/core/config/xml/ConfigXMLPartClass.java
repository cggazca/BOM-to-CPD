package com.mentor.dms.contentprovider.core.config.xml;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"properties"})
@XmlRootElement(name = "PartClass")
public class ConfigXMLPartClass extends BaseXMLClass {
  @XmlElement(name = "Properties")
  protected ConfigXMLProperties properties;
  
  @XmlAttribute(name = "id", required = true)
  @XmlSchemaType(name = "anySimpleType")
  protected String id;
  
  @XmlAttribute(name = "parentId")
  @XmlSchemaType(name = "anySimpleType")
  protected String parentId;
  
  @XmlAttribute(name = "label")
  @XmlSchemaType(name = "anySimpleType")
  protected String label;
  
  public ConfigXMLProperties getProperties() {
    return this.properties;
  }
  
  public void setProperties(ConfigXMLProperties paramConfigXMLProperties) {
    this.properties = paramConfigXMLProperties;
  }
  
  public String getId() {
    return this.id;
  }
  
  public void setId(String paramString) {
    this.id = paramString;
  }
  
  public String getParentId() {
    return this.parentId;
  }
  
  public void setParentId(String paramString) {
    this.parentId = paramString;
  }
  
  public String getLabel() {
    return this.label;
  }
  
  public void setLabel(String paramString) {
    this.label = paramString;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\config\xml\ConfigXMLPartClass.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */