package com.mentor.dms.contentprovider.core.utils.setrepmpn.config;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {})
@XmlRootElement(name = "FavoredManufacturerPart")
public class SetRepMPNCfgFavoredManufacturerPart {
  @XmlElement(name = "SelectionCriteria", required = true)
  protected SetRepMPNCfgSelectionCriteria selectionCriteria;
  
  public SetRepMPNCfgSelectionCriteria getSelectionCriteria() {
    return this.selectionCriteria;
  }
  
  public void setSelectionCriteria(SetRepMPNCfgSelectionCriteria paramSetRepMPNCfgSelectionCriteria) {
    this.selectionCriteria = paramSetRepMPNCfgSelectionCriteria;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\cor\\utils\setrepmpn\config\SetRepMPNCfgFavoredManufacturerPart.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */