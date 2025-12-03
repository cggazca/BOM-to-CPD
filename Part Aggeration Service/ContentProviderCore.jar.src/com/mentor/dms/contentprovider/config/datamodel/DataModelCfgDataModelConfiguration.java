package com.mentor.dms.contentprovider.config.datamodel;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {})
@XmlRootElement(name = "DataModelConfiguration")
public class DataModelCfgDataModelConfiguration {
  @XmlElement(name = "ExcludeProperties")
  protected DataModelCfgExcludeProperties excludeProperties;
  
  @XmlElement(name = "PropertyLengthOverrides")
  protected DataModelCfgPropertyLengthOverrides propertyLengthOverrides;
  
  @XmlElement(name = "RedirectPartClasses")
  protected DataModelCfgRedirectPartClasses redirectPartClasses;
  
  @XmlElement(name = "ExcludePartClasses")
  protected DataModelCfgExcludePartClasses excludePartClasses;
  
  @XmlElement(name = "TopLevelScriptedMappings")
  protected DataModelCfgTopLevelScriptedMappings topLevelScriptedMappings;
  
  @XmlElement(name = "DoubleMappings")
  protected DataModelCfgDoubleMappings doubleMappings;
  
  @XmlElement(name = "ScriptedMappings")
  protected DataModelCfgScriptedMappings scriptedMappings;
  
  @XmlElement(name = "PartClassUnits")
  protected DataModelCfgPartClassUnits partClassUnits;
  
  @XmlElement(name = "UnitsMappings")
  protected DataModelCfgUnitsMappings unitsMappings;
  
  @XmlElement(name = "SampleParts")
  protected DataModelCfgSampleParts sampleParts;
  
  @XmlAttribute(required = true)
  protected String ccpVersion;
  
  @XmlAttribute(required = true)
  protected String releaseDate;
  
  @XmlAttribute(required = true)
  protected boolean componentSearch;
  
  public DataModelCfgExcludeProperties getExcludeProperties() {
    return this.excludeProperties;
  }
  
  public void setExcludeProperties(DataModelCfgExcludeProperties paramDataModelCfgExcludeProperties) {
    this.excludeProperties = paramDataModelCfgExcludeProperties;
  }
  
  public DataModelCfgPropertyLengthOverrides getPropertyLengthOverrides() {
    return this.propertyLengthOverrides;
  }
  
  public void setPropertyLengthOverrides(DataModelCfgPropertyLengthOverrides paramDataModelCfgPropertyLengthOverrides) {
    this.propertyLengthOverrides = paramDataModelCfgPropertyLengthOverrides;
  }
  
  public DataModelCfgRedirectPartClasses getRedirectPartClasses() {
    return this.redirectPartClasses;
  }
  
  public void setRedirectPartClasses(DataModelCfgRedirectPartClasses paramDataModelCfgRedirectPartClasses) {
    this.redirectPartClasses = paramDataModelCfgRedirectPartClasses;
  }
  
  public DataModelCfgExcludePartClasses getExcludePartClasses() {
    return this.excludePartClasses;
  }
  
  public void setExcludePartClasses(DataModelCfgExcludePartClasses paramDataModelCfgExcludePartClasses) {
    this.excludePartClasses = paramDataModelCfgExcludePartClasses;
  }
  
  public DataModelCfgTopLevelScriptedMappings getTopLevelScriptedMappings() {
    return this.topLevelScriptedMappings;
  }
  
  public void setTopLevelScriptedMappings(DataModelCfgTopLevelScriptedMappings paramDataModelCfgTopLevelScriptedMappings) {
    this.topLevelScriptedMappings = paramDataModelCfgTopLevelScriptedMappings;
  }
  
  public DataModelCfgDoubleMappings getDoubleMappings() {
    return this.doubleMappings;
  }
  
  public void setDoubleMappings(DataModelCfgDoubleMappings paramDataModelCfgDoubleMappings) {
    this.doubleMappings = paramDataModelCfgDoubleMappings;
  }
  
  public DataModelCfgScriptedMappings getScriptedMappings() {
    return this.scriptedMappings;
  }
  
  public void setScriptedMappings(DataModelCfgScriptedMappings paramDataModelCfgScriptedMappings) {
    this.scriptedMappings = paramDataModelCfgScriptedMappings;
  }
  
  public DataModelCfgPartClassUnits getPartClassUnits() {
    return this.partClassUnits;
  }
  
  public void setPartClassUnits(DataModelCfgPartClassUnits paramDataModelCfgPartClassUnits) {
    this.partClassUnits = paramDataModelCfgPartClassUnits;
  }
  
  public DataModelCfgUnitsMappings getUnitsMappings() {
    return this.unitsMappings;
  }
  
  public void setUnitsMappings(DataModelCfgUnitsMappings paramDataModelCfgUnitsMappings) {
    this.unitsMappings = paramDataModelCfgUnitsMappings;
  }
  
  public DataModelCfgSampleParts getSampleParts() {
    return this.sampleParts;
  }
  
  public void setSampleParts(DataModelCfgSampleParts paramDataModelCfgSampleParts) {
    this.sampleParts = paramDataModelCfgSampleParts;
  }
  
  public String getCcpVersion() {
    return this.ccpVersion;
  }
  
  public void setCcpVersion(String paramString) {
    this.ccpVersion = paramString;
  }
  
  public String getReleaseDate() {
    return this.releaseDate;
  }
  
  public void setReleaseDate(String paramString) {
    this.releaseDate = paramString;
  }
  
  public boolean isComponentSearch() {
    return this.componentSearch;
  }
  
  public void setComponentSearch(boolean paramBoolean) {
    this.componentSearch = paramBoolean;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\config\datamodel\DataModelCfgDataModelConfiguration.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */