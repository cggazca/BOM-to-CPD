package com.mentor.dms.contentprovider.sf.datamodel.definition;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElements;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "set-of-fields", propOrder = {"fieldOrList"})
@XmlSeeAlso({ListType.class})
public class SetOfFields {
  @XmlElements({@XmlElement(name = "list", type = ListType.class), @XmlElement(name = "field", type = FieldType.class)})
  protected List<Object> fieldOrList;
  
  public List<Object> getFieldOrList() {
    if (this.fieldOrList == null)
      this.fieldOrList = new ArrayList(); 
    return this.fieldOrList;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\datamodel\definition\SetOfFields.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */