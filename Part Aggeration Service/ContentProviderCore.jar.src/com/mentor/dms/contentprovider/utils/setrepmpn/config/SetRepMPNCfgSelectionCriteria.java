package com.mentor.dms.contentprovider.utils.setrepmpn.config;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElements;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"prioritySelectionCriteriaOrAggregationSelectionCriteriaOrCustomSelectionCriteria"})
@XmlRootElement(name = "SelectionCriteria")
public class SetRepMPNCfgSelectionCriteria {
  @XmlElements({@XmlElement(name = "CustomSelectionCriteria", type = SetRepMPNCfgCustomSelectionCriteriaType.class), @XmlElement(name = "AggregationSelectionCriteria", type = SetRepMPNCfgAggregationSelectionCriteriaType.class), @XmlElement(name = "PrioritySelectionCriteria", type = SetRepMPNCfgPrioritySelectionCriteriaType.class)})
  protected List<SetRepMPNCfgBaseFieldType> prioritySelectionCriteriaOrAggregationSelectionCriteriaOrCustomSelectionCriteria;
  
  public List<SetRepMPNCfgBaseFieldType> getPrioritySelectionCriteriaOrAggregationSelectionCriteriaOrCustomSelectionCriteria() {
    if (this.prioritySelectionCriteriaOrAggregationSelectionCriteriaOrCustomSelectionCriteria == null)
      this.prioritySelectionCriteriaOrAggregationSelectionCriteriaOrCustomSelectionCriteria = new ArrayList<>(); 
    return this.prioritySelectionCriteriaOrAggregationSelectionCriteriaOrCustomSelectionCriteria;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovide\\utils\setrepmpn\config\SetRepMPNCfgSelectionCriteria.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */