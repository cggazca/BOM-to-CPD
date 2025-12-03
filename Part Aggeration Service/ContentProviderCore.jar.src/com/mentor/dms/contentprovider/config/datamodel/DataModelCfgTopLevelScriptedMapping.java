package com.mentor.dms.contentprovider.config.datamodel;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "TopLevelScriptedMapping")
public class DataModelCfgTopLevelScriptedMapping {
  @XmlAttribute(required = true)
  @XmlSchemaType(name = "anySimpleType")
  protected String mappingFunction;
  
  @XmlAttribute
  protected Boolean inherit;
  
  @XmlAttribute
  protected Boolean ignoreMissing;
  
  public String getMappingFunction() {
    return this.mappingFunction;
  }
  
  public void setMappingFunction(String paramString) {
    this.mappingFunction = paramString;
  }
  
  public boolean isInherit() {
    return (this.inherit == null) ? true : this.inherit.booleanValue();
  }
  
  public void setInherit(Boolean paramBoolean) {
    this.inherit = paramBoolean;
  }
  
  public boolean isIgnoreMissing() {
    return (this.ignoreMissing == null) ? true : this.ignoreMissing.booleanValue();
  }
  
  public void setIgnoreMissing(Boolean paramBoolean) {
    this.ignoreMissing = paramBoolean;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\config\datamodel\DataModelCfgTopLevelScriptedMapping.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */