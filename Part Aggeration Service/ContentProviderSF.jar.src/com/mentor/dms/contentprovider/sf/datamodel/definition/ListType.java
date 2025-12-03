package com.mentor.dms.contentprovider.sf.datamodel.definition;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "list-type")
public class ListType extends SetOfFields {
  @XmlAttribute(required = true)
  protected String id;
  
  @XmlAttribute
  protected Boolean clear;
  
  public String getId() {
    return this.id;
  }
  
  public void setId(String paramString) {
    this.id = paramString;
  }
  
  public Boolean isClear() {
    return this.clear;
  }
  
  public void setClear(Boolean paramBoolean) {
    this.clear = paramBoolean;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\datamodel\definition\ListType.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */