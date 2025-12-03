package com.mentor.dms.contentprovider.sf.datamodel.upgrade;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "MergeCatalog")
public class MergeCatalog {
  @XmlAttribute(required = true)
  @XmlSchemaType(name = "anySimpleType")
  protected String fromPath;
  
  @XmlAttribute(required = true)
  @XmlSchemaType(name = "anySimpleType")
  protected String toPath;
  
  public String getFromPath() {
    return this.fromPath;
  }
  
  public void setFromPath(String paramString) {
    this.fromPath = paramString;
  }
  
  public String getToPath() {
    return this.toPath;
  }
  
  public void setToPath(String paramString) {
    this.toPath = paramString;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\datamode\\upgrade\MergeCatalog.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */