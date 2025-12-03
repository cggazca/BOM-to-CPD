package com.mentor.dms.contentprovider.sf.datamodel.upgrade;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

@XmlRegistry
public class ObjectFactory {
  private static final QName _UpgradeDirectives_QNAME = new QName("", "UpgradeDirectives");
  
  public MergeCatalog createMergeCatalog() {
    return new MergeCatalog();
  }
  
  public RenameProperty createRenameProperty() {
    return new RenameProperty();
  }
  
  public RenameCatalog createRenameCatalog() {
    return new RenameCatalog();
  }
  
  public AddCatalog createAddCatalog() {
    return new AddCatalog();
  }
  
  public DeleteCatalog createDeleteCatalog() {
    return new DeleteCatalog();
  }
  
  public UpgradeDirectiveType createUpgradeDirectiveType() {
    return new UpgradeDirectiveType();
  }
  
  public DataModelUpgrade createDataModelUpgrade() {
    return new DataModelUpgrade();
  }
  
  @XmlElementDecl(namespace = "", name = "UpgradeDirectives")
  public JAXBElement<UpgradeDirectiveType> createUpgradeDirectives(UpgradeDirectiveType paramUpgradeDirectiveType) {
    return new JAXBElement(_UpgradeDirectives_QNAME, UpgradeDirectiveType.class, null, paramUpgradeDirectiveType);
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\datamode\\upgrade\ObjectFactory.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */