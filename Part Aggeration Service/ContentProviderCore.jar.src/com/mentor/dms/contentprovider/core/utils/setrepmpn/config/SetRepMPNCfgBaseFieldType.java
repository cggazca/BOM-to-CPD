package com.mentor.dms.contentprovider.core.utils.setrepmpn.config;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BaseFieldType")
@XmlSeeAlso({SetRepMPNCfgPrioritySelectionCriteriaType.class, SetRepMPNCfgCustomSelectionCriteriaType.class, SetRepMPNCfgAggregationRollupFieldType.class, SetRepMPNCfgBaseFieldDataType.class})
public class SetRepMPNCfgBaseFieldType {
  @XmlAttribute(required = true)
  @XmlSchemaType(name = "anySimpleType")
  protected String dmn;
  
  @XmlAttribute
  protected Boolean amlProperty;
  
  public String getDmn() {
    return this.dmn;
  }
  
  public void setDmn(String paramString) {
    this.dmn = paramString;
  }
  
  public boolean isAmlProperty() {
    return (this.amlProperty == null) ? false : this.amlProperty.booleanValue();
  }
  
  public void setAmlProperty(Boolean paramBoolean) {
    this.amlProperty = paramBoolean;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\cor\\utils\setrepmpn\config\SetRepMPNCfgBaseFieldType.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */