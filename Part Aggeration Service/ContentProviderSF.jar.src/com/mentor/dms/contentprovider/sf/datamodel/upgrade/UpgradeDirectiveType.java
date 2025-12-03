package com.mentor.dms.contentprovider.sf.datamodel.upgrade;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElements;
import jakarta.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UpgradeDirectiveType", propOrder = {"upgradeDirectives"})
public class UpgradeDirectiveType extends BaseUpgradeDirectiveType {
  @XmlElements({@XmlElement(name = "AddCatalog", type = AddCatalog.class), @XmlElement(name = "RenameProperty", type = RenameProperty.class), @XmlElement(name = "DeleteCatalog", type = DeleteCatalog.class), @XmlElement(name = "RenameCatalog", type = RenameCatalog.class), @XmlElement(name = "MergeCatalog", type = MergeCatalog.class), @XmlElement(name = "DeleteProperty", type = DeleteProperty.class), @XmlElement(name = "DeleteAllProperty", type = DeleteAllProperty.class)})
  protected List<Object> upgradeDirectives;
  
  public List<Object> getUpgradeDirectives() {
    if (this.upgradeDirectives == null)
      this.upgradeDirectives = new ArrayList(); 
    return this.upgradeDirectives;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\datamode\\upgrade\UpgradeDirectiveType.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */