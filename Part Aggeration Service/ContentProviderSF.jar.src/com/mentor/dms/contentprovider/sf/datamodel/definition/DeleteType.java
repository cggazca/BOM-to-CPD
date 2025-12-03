package com.mentor.dms.contentprovider.sf.datamodel.definition;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "delete-type")
public class DeleteType {
  @XmlAttribute(name = "class", required = true)
  protected int clazz;
  
  @XmlAttribute
  protected String catalog;
  
  @XmlAttribute(required = true)
  protected String objectid;
  
  public int getClazz() {
    return this.clazz;
  }
  
  public void setClazz(int paramInt) {
    this.clazz = paramInt;
  }
  
  public String getCatalog() {
    return this.catalog;
  }
  
  public void setCatalog(String paramString) {
    this.catalog = paramString;
  }
  
  public String getObjectid() {
    return this.objectid;
  }
  
  public void setObjectid(String paramString) {
    this.objectid = paramString;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\datamodel\definition\DeleteType.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */