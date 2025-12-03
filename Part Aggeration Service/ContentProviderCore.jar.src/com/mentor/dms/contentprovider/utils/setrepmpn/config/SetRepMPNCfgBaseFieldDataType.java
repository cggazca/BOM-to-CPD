package com.mentor.dms.contentprovider.utils.setrepmpn.config;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BaseFieldDataType", propOrder = {"values"})
@XmlSeeAlso({SetRepMPNCfgAggregationSelectionCriteriaType.class})
public class SetRepMPNCfgBaseFieldDataType extends SetRepMPNCfgBaseFieldType {
  @XmlElement(name = "Values")
  protected SetRepMPNCfgValues values;
  
  @XmlAttribute
  protected SetRepMPNCfgDataType dataType;
  
  @XmlAttribute
  protected String dateFormat;
  
  @XmlAttribute
  protected String regex;
  
  @XmlAttribute
  protected String filter;
  
  @XmlAttribute
  protected String textFormat;
  
  public SetRepMPNCfgValues getValues() {
    return this.values;
  }
  
  public void setValues(SetRepMPNCfgValues paramSetRepMPNCfgValues) {
    this.values = paramSetRepMPNCfgValues;
  }
  
  public SetRepMPNCfgDataType getDataType() {
    return (this.dataType == null) ? SetRepMPNCfgDataType.DEFAULT : this.dataType;
  }
  
  public void setDataType(SetRepMPNCfgDataType paramSetRepMPNCfgDataType) {
    this.dataType = paramSetRepMPNCfgDataType;
  }
  
  public String getDateFormat() {
    return this.dateFormat;
  }
  
  public void setDateFormat(String paramString) {
    this.dateFormat = paramString;
  }
  
  public String getRegex() {
    return this.regex;
  }
  
  public void setRegex(String paramString) {
    this.regex = paramString;
  }
  
  public String getFilter() {
    return this.filter;
  }
  
  public void setFilter(String paramString) {
    this.filter = paramString;
  }
  
  public String getTextFormat() {
    return this.textFormat;
  }
  
  public void setTextFormat(String paramString) {
    this.textFormat = paramString;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovide\\utils\setrepmpn\config\SetRepMPNCfgBaseFieldDataType.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */