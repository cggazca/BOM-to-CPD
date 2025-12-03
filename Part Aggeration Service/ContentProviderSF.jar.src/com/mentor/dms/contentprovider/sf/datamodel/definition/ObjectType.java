package com.mentor.dms.contentprovider.sf.datamodel.definition;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElements;
import jakarta.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "object-type", propOrder = {"listOrField"})
public class ObjectType {
  @XmlElements({@XmlElement(name = "field", type = FieldType.class), @XmlElement(name = "list", type = ListType.class)})
  protected List<Object> listOrField;
  
  @XmlAttribute(name = "class", required = true)
  protected String clazz;
  
  @XmlAttribute(required = true)
  protected String objectid;
  
  @XmlAttribute
  protected String catalog;
  
  @XmlAttribute
  protected Boolean delete;
  
  @XmlAttribute
  protected String graphic;
  
  @XmlAttribute
  protected Boolean emptyfields;
  
  @XmlAttribute
  protected Boolean noninputfields;
  
  @XmlAttribute
  protected Boolean defaultvalues;
  
  @XmlAttribute
  protected Boolean unit;
  
  @XmlAttribute(name = "broken_ref")
  protected Boolean brokenRef;
  
  public List<Object> getListOrField() {
    if (this.listOrField == null)
      this.listOrField = new ArrayList(); 
    return this.listOrField;
  }
  
  public String getClazz() {
    return this.clazz;
  }
  
  public void setClazz(String paramString) {
    this.clazz = paramString;
  }
  
  public String getObjectid() {
    return this.objectid;
  }
  
  public void setObjectid(String paramString) {
    this.objectid = paramString;
  }
  
  public String getCatalog() {
    return this.catalog;
  }
  
  public void setCatalog(String paramString) {
    this.catalog = paramString;
  }
  
  public Boolean isDelete() {
    return this.delete;
  }
  
  public void setDelete(Boolean paramBoolean) {
    this.delete = paramBoolean;
  }
  
  public String getGraphic() {
    return this.graphic;
  }
  
  public void setGraphic(String paramString) {
    this.graphic = paramString;
  }
  
  public Boolean isEmptyfields() {
    return this.emptyfields;
  }
  
  public void setEmptyfields(Boolean paramBoolean) {
    this.emptyfields = paramBoolean;
  }
  
  public Boolean isNoninputfields() {
    return this.noninputfields;
  }
  
  public void setNoninputfields(Boolean paramBoolean) {
    this.noninputfields = paramBoolean;
  }
  
  public Boolean isDefaultvalues() {
    return this.defaultvalues;
  }
  
  public void setDefaultvalues(Boolean paramBoolean) {
    this.defaultvalues = paramBoolean;
  }
  
  public Boolean isUnit() {
    return this.unit;
  }
  
  public void setUnit(Boolean paramBoolean) {
    this.unit = paramBoolean;
  }
  
  public Boolean isBrokenRef() {
    return this.brokenRef;
  }
  
  public void setBrokenRef(Boolean paramBoolean) {
    this.brokenRef = paramBoolean;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\datamodel\definition\ObjectType.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */