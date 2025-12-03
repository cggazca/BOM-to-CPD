package com.mentor.dms.contentprovider.utils.setrepmpn.config;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

@XmlRegistry
public class ObjectFactory {
  private static final QName _LookupValueType_QNAME = new QName("", "LookupValueType");
  
  public SetRepMPNCfgCustomSelectionCriteriaType createSetRepMPNCfgCustomSelectionCriteriaType() {
    return new SetRepMPNCfgCustomSelectionCriteriaType();
  }
  
  public SetRepMPNCfgSelectionCriteria createSetRepMPNCfgSelectionCriteria() {
    return new SetRepMPNCfgSelectionCriteria();
  }
  
  public SetRepMPNCfgRestriction createSetRepMPNCfgRestriction() {
    return new SetRepMPNCfgRestriction();
  }
  
  public SetRepMPNCfgBaseValueType createSetRepMPNCfgBaseValueType() {
    return new SetRepMPNCfgBaseValueType();
  }
  
  public SetRepMPNCfgBaseFieldDataType createSetRepMPNCfgBaseFieldDataType() {
    return new SetRepMPNCfgBaseFieldDataType();
  }
  
  public SetRepMPNCfgFavoredManufacturerPart createSetRepMPNCfgFavoredManufacturerPart() {
    return new SetRepMPNCfgFavoredManufacturerPart();
  }
  
  public SetRepMPNCfgAggregationRollupFieldType createSetRepMPNCfgAggregationRollupFieldType() {
    return new SetRepMPNCfgAggregationRollupFieldType();
  }
  
  public SetRepMPNCfgSetRepMPNCfg createSetRepMPNCfgSetRepMPNCfg() {
    return new SetRepMPNCfgSetRepMPNCfg();
  }
  
  public SetRepMPNCfgManufacturerPartRestrictions createSetRepMPNCfgManufacturerPartRestrictions() {
    return new SetRepMPNCfgManufacturerPartRestrictions();
  }
  
  public SetRepMPNCfgRollupValueType createSetRepMPNCfgRollupValueType() {
    return new SetRepMPNCfgRollupValueType();
  }
  
  public SetRepMPNCfgComponentAMLRestrictions createSetRepMPNCfgComponentAMLRestrictions() {
    return new SetRepMPNCfgComponentAMLRestrictions();
  }
  
  public SetRepMPNCfgBaseFieldType createSetRepMPNCfgBaseFieldType() {
    return new SetRepMPNCfgBaseFieldType();
  }
  
  public SetRepMPNCfgPrioritySelectionCriteriaType createSetRepMPNCfgPrioritySelectionCriteriaType() {
    return new SetRepMPNCfgPrioritySelectionCriteriaType();
  }
  
  public SetRepMPNCfgPriorityValues createSetRepMPNCfgPriorityValues() {
    return new SetRepMPNCfgPriorityValues();
  }
  
  public SetRepMPNCfgPriorityValueType createSetRepMPNCfgPriorityValueType() {
    return new SetRepMPNCfgPriorityValueType();
  }
  
  public SetRepMPNCfgAggregationSelectionCriteriaType createSetRepMPNCfgAggregationSelectionCriteriaType() {
    return new SetRepMPNCfgAggregationSelectionCriteriaType();
  }
  
  public SetRepMPNCfgComponentRestrictions createSetRepMPNCfgComponentRestrictions() {
    return new SetRepMPNCfgComponentRestrictions();
  }
  
  public SetRepMPNCfgValues createSetRepMPNCfgValues() {
    return new SetRepMPNCfgValues();
  }
  
  @XmlElementDecl(namespace = "", name = "LookupValueType")
  public JAXBElement<String> createLookupValueType(String paramString) {
    return new JAXBElement(_LookupValueType_QNAME, String.class, null, paramString);
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovide\\utils\setrepmpn\config\ObjectFactory.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */