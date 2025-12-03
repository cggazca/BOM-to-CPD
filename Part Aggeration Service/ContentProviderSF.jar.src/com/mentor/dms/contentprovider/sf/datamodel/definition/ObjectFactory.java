package com.mentor.dms.contentprovider.sf.datamodel.definition;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

@XmlRegistry
public class ObjectFactory {
  private static final QName _Data_QNAME = new QName("", "data");
  
  public ListType createListType() {
    return new ListType();
  }
  
  public DeleteType createDeleteType() {
    return new DeleteType();
  }
  
  public SetOfFields createSetOfFields() {
    return new SetOfFields();
  }
  
  public DmsElements createDmsElements() {
    return new DmsElements();
  }
  
  public ObjectType createObjectType() {
    return new ObjectType();
  }
  
  public FieldType createFieldType() {
    return new FieldType();
  }
  
  @XmlElementDecl(namespace = "", name = "data")
  public JAXBElement<DmsElements> createData(DmsElements paramDmsElements) {
    return new JAXBElement(_Data_QNAME, DmsElements.class, null, paramDmsElements);
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\datamodel\definition\ObjectFactory.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */