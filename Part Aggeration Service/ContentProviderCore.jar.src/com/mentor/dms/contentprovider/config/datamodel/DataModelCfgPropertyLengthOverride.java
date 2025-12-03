package com.mentor.dms.contentprovider.config.datamodel;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "PropertyLengthOverride")
public class DataModelCfgPropertyLengthOverride {
  @XmlAttribute(required = true)
  @XmlSchemaType(name = "anySimpleType")
  protected String id;
  
  @XmlAttribute(required = true)
  protected int length;
  
  public String getId() {
    return this.id;
  }
  
  public void setId(String paramString) {
    this.id = paramString;
  }
  
  public int getLength() {
    return this.length;
  }
  
  public void setLength(int paramInt) {
    this.length = paramInt;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\config\datamodel\DataModelCfgPropertyLengthOverride.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */