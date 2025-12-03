package com.mentor.dms.contentprovider.core.utils.setrepmpn.config;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AggregationSelectionCriteriaType")
public class SetRepMPNCfgAggregationSelectionCriteriaType extends SetRepMPNCfgBaseFieldDataType {
  @XmlAttribute(required = true)
  protected SetRepMPNCfgSelectionCriteriaAggregationType aggregationType;
  
  public SetRepMPNCfgSelectionCriteriaAggregationType getAggregationType() {
    return this.aggregationType;
  }
  
  public void setAggregationType(SetRepMPNCfgSelectionCriteriaAggregationType paramSetRepMPNCfgSelectionCriteriaAggregationType) {
    this.aggregationType = paramSetRepMPNCfgSelectionCriteriaAggregationType;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\cor\\utils\setrepmpn\config\SetRepMPNCfgAggregationSelectionCriteriaType.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */