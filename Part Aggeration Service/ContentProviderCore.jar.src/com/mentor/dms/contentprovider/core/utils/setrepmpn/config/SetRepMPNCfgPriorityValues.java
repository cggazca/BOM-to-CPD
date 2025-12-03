package com.mentor.dms.contentprovider.core.utils.setrepmpn.config;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"priorityValue"})
@XmlRootElement(name = "PriorityValues")
public class SetRepMPNCfgPriorityValues {
  @XmlElement(name = "PriorityValue", required = true)
  protected List<SetRepMPNCfgPriorityValueType> priorityValue;
  
  public List<SetRepMPNCfgPriorityValueType> getPriorityValue() {
    if (this.priorityValue == null)
      this.priorityValue = new ArrayList<>(); 
    return this.priorityValue;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\cor\\utils\setrepmpn\config\SetRepMPNCfgPriorityValues.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */