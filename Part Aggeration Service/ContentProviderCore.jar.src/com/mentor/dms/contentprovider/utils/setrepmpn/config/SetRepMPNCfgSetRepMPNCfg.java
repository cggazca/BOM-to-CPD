package com.mentor.dms.contentprovider.utils.setrepmpn.config;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {})
@XmlRootElement(name = "SetRepMPNCfg")
public class SetRepMPNCfgSetRepMPNCfg {
  @XmlElement(name = "ComponentRestrictions")
  protected SetRepMPNCfgComponentRestrictions componentRestrictions;
  
  @XmlElement(name = "ComponentAMLRestrictions")
  protected SetRepMPNCfgComponentAMLRestrictions componentAMLRestrictions;
  
  @XmlElement(name = "ManufacturerPartRestrictions")
  protected SetRepMPNCfgManufacturerPartRestrictions manufacturerPartRestrictions;
  
  @XmlElement(name = "FavoredManufacturerPart", required = true)
  protected SetRepMPNCfgFavoredManufacturerPart favoredManufacturerPart;
  
  public SetRepMPNCfgComponentRestrictions getComponentRestrictions() {
    return this.componentRestrictions;
  }
  
  public void setComponentRestrictions(SetRepMPNCfgComponentRestrictions paramSetRepMPNCfgComponentRestrictions) {
    this.componentRestrictions = paramSetRepMPNCfgComponentRestrictions;
  }
  
  public SetRepMPNCfgComponentAMLRestrictions getComponentAMLRestrictions() {
    return this.componentAMLRestrictions;
  }
  
  public void setComponentAMLRestrictions(SetRepMPNCfgComponentAMLRestrictions paramSetRepMPNCfgComponentAMLRestrictions) {
    this.componentAMLRestrictions = paramSetRepMPNCfgComponentAMLRestrictions;
  }
  
  public SetRepMPNCfgManufacturerPartRestrictions getManufacturerPartRestrictions() {
    return this.manufacturerPartRestrictions;
  }
  
  public void setManufacturerPartRestrictions(SetRepMPNCfgManufacturerPartRestrictions paramSetRepMPNCfgManufacturerPartRestrictions) {
    this.manufacturerPartRestrictions = paramSetRepMPNCfgManufacturerPartRestrictions;
  }
  
  public SetRepMPNCfgFavoredManufacturerPart getFavoredManufacturerPart() {
    return this.favoredManufacturerPart;
  }
  
  public void setFavoredManufacturerPart(SetRepMPNCfgFavoredManufacturerPart paramSetRepMPNCfgFavoredManufacturerPart) {
    this.favoredManufacturerPart = paramSetRepMPNCfgFavoredManufacturerPart;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovide\\utils\setrepmpn\config\SetRepMPNCfgSetRepMPNCfg.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */