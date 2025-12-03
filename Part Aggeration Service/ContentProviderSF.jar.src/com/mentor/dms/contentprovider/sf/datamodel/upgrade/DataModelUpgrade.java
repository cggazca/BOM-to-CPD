package com.mentor.dms.contentprovider.sf.datamodel.upgrade;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"upgradeDirectives"})
@XmlRootElement(name = "DataModelUpgrade")
public class DataModelUpgrade {
  @XmlElement(name = "UpgradeDirectives", required = true)
  protected UpgradeDirectiveType upgradeDirectives;
  
  public UpgradeDirectiveType getUpgradeDirectives() {
    return this.upgradeDirectives;
  }
  
  public void setUpgradeDirectives(UpgradeDirectiveType paramUpgradeDirectiveType) {
    this.upgradeDirectives = paramUpgradeDirectiveType;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\datamode\\upgrade\DataModelUpgrade.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */