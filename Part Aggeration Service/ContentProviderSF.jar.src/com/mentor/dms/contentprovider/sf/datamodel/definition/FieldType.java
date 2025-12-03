package com.mentor.dms.contentprovider.sf.datamodel.definition;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.XmlValue;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "field-type", propOrder = {"value"})
public class FieldType {
  @XmlValue
  protected String value;
  
  @XmlAttribute(required = true)
  protected String id;
  
  @XmlAttribute
  protected String multirefclass;
  
  @XmlAttribute(name = "null")
  protected Boolean _null;
  
  public String getValue() {
    return this.value;
  }
  
  public void setValue(String paramString) {
    this.value = paramString;
  }
  
  public String getId() {
    return this.id;
  }
  
  public void setId(String paramString) {
    this.id = paramString;
  }
  
  public String getMultirefclass() {
    return this.multirefclass;
  }
  
  public void setMultirefclass(String paramString) {
    this.multirefclass = paramString;
  }
  
  public Boolean isNull() {
    return this._null;
  }
  
  public void setNull(Boolean paramBoolean) {
    this._null = paramBoolean;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\datamodel\definition\FieldType.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */