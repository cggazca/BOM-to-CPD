package com.mentor.dms.contentprovider.core.utils.setrepmpn.config;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CustomSelectionCriteriaType")
public class SetRepMPNCfgCustomSelectionCriteriaType extends SetRepMPNCfgBaseFieldType {
  @XmlAttribute(required = true)
  protected String compare;
  
  public String getCompare() {
    return this.compare;
  }
  
  public void setCompare(String paramString) {
    this.compare = paramString;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\cor\\utils\setrepmpn\config\SetRepMPNCfgCustomSelectionCriteriaType.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */