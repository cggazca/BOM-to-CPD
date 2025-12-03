package com.mentor.dms.contentprovider.sf.datamodel.upgrade;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "DeleteAllProperty")
public class DeleteAllProperty {
  @XmlAttribute(required = true)
  @XmlSchemaType(name = "anySimpleType")
  protected String domainModelName;
  
  @XmlAttribute
  protected String name;
  
  public String getDomainModelName() {
    return this.domainModelName;
  }
  
  public void setDomainModelName(String paramString) {
    this.domainModelName = paramString;
  }
  
  public String getName() {
    return this.name;
  }
  
  public void setName(String paramString) {
    this.name = paramString;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\datamode\\upgrade\DeleteAllProperty.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */