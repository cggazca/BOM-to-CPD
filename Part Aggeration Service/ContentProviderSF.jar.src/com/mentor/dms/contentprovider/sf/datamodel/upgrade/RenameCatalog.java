package com.mentor.dms.contentprovider.sf.datamodel.upgrade;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "RenameCatalog")
public class RenameCatalog {
  @XmlAttribute(required = true)
  @XmlSchemaType(name = "anySimpleType")
  protected String domainModelName;
  
  @XmlAttribute(required = true)
  @XmlSchemaType(name = "anySimpleType")
  protected String toName;
  
  public String getDomainModelName() {
    return this.domainModelName;
  }
  
  public void setDomainModelName(String paramString) {
    this.domainModelName = paramString;
  }
  
  public String getToName() {
    return this.toName;
  }
  
  public void setToName(String paramString) {
    this.toName = paramString;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\datamode\\upgrade\RenameCatalog.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */