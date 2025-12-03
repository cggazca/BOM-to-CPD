package com.mentor.dms.contentprovider.sf.datamodel.definition;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElements;
import jakarta.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "dms-elements", propOrder = {"objectOrDelete"})
public class DmsElements {
  @XmlElements({@XmlElement(name = "object", type = ObjectType.class), @XmlElement(name = "delete", type = DeleteType.class)})
  protected List<Object> objectOrDelete;
  
  public List<Object> getObjectOrDelete() {
    if (this.objectOrDelete == null)
      this.objectOrDelete = new ArrayList(); 
    return this.objectOrDelete;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\datamodel\definition\DmsElements.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */