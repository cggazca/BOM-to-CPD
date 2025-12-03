package com.mentor.dms.contentprovider.config.xml;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "Property")
public class ConfigXMLProperty extends BaseXMLClass {
  @XmlAttribute(name = "baseUnits")
  @XmlSchemaType(name = "anySimpleType")
  protected String baseUnits;
  
  @XmlAttribute(name = "id", required = true)
  @XmlSchemaType(name = "anySimpleType")
  protected String id;
  
  @XmlAttribute(name = "label")
  @XmlSchemaType(name = "anySimpleType")
  protected String label;
  
  @XmlAttribute(name = "idProperty")
  protected Boolean idProperty;
  
  @XmlAttribute(name = "searchable")
  protected Boolean searchable;
  
  @XmlAttribute(name = "inherit")
  protected Boolean inherit;
  
  public String getBaseUnits() {
    return this.baseUnits;
  }
  
  public void setBaseUnits(String paramString) {
    this.baseUnits = paramString;
  }
  
  public String getId() {
    return this.id;
  }
  
  public void setId(String paramString) {
    this.id = paramString;
  }
  
  public String getLabel() {
    return this.label;
  }
  
  public void setLabel(String paramString) {
    this.label = paramString;
  }
  
  public Boolean isIdProperty() {
    return this.idProperty;
  }
  
  public void setIdProperty(Boolean paramBoolean) {
    this.idProperty = paramBoolean;
  }
  
  public Boolean isSearchable() {
    return this.searchable;
  }
  
  public void setSearchable(Boolean paramBoolean) {
    this.searchable = paramBoolean;
  }
  
  public Boolean isInherit() {
    return this.inherit;
  }
  
  public void setInherit(Boolean paramBoolean) {
    this.inherit = paramBoolean;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\config\xml\ConfigXMLProperty.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */