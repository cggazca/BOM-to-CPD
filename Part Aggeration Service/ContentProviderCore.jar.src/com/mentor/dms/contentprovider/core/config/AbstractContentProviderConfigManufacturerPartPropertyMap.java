package com.mentor.dms.contentprovider.core.config;

import com.mentor.dms.contentprovider.core.config.xml.ConfigXMLManufacturerPartPropertyMap;
import com.mentor.dms.contentprovider.core.config.xml.ConfigXMLScriptedManufacturerPartPropertyMap;

public abstract class AbstractContentProviderConfigManufacturerPartPropertyMap {
  private static int nextId = 1;
  
  private int id;
  
  protected ContentProviderConfigManufacturerPartMap manufacturerPartMap;
  
  protected ContentProviderConfig.PropertySyncType syncType = null;
  
  private boolean bInherit = true;
  
  private boolean bIgnoreMissing = false;
  
  private boolean bAutoMapped = false;
  
  private boolean bValid = true;
  
  private String errorText = "";
  
  public AbstractContentProviderConfigManufacturerPartPropertyMap(ContentProviderConfigManufacturerPartMap paramContentProviderConfigManufacturerPartMap) {
    this.manufacturerPartMap = paramContentProviderConfigManufacturerPartMap;
    this.id = nextId++;
  }
  
  public int getId() {
    return this.id;
  }
  
  public ContentProviderConfigManufacturerPartMap getManufacturerPartMap() {
    return this.manufacturerPartMap;
  }
  
  public ContentProviderConfigComponentCatalog getComponentCatalog() {
    return this.manufacturerPartMap.getComponentCatalog();
  }
  
  public void setSyncType(ContentProviderConfig.PropertySyncType paramPropertySyncType) {
    this.syncType = paramPropertySyncType;
  }
  
  public ContentProviderConfig.PropertySyncType getSyncType() {
    return (this.syncType == null) ? this.manufacturerPartMap.getDefaultSyncType() : this.syncType;
  }
  
  public boolean isInherit() {
    return this.bInherit;
  }
  
  public void setInherit(boolean paramBoolean) {
    this.bInherit = paramBoolean;
  }
  
  public boolean isIgnoreMissing() {
    return this.bIgnoreMissing;
  }
  
  public void setIgnoreMissing(boolean paramBoolean) {
    this.bIgnoreMissing = paramBoolean;
  }
  
  public boolean isAutoMapped() {
    return this.bAutoMapped;
  }
  
  public void setAutoMapped(boolean paramBoolean) {
    this.bAutoMapped = paramBoolean;
  }
  
  public boolean isValid() {
    return this.bValid;
  }
  
  public void setValid(boolean paramBoolean) {
    this.bValid = paramBoolean;
  }
  
  public void setErrorText(String paramString) {
    this.errorText = paramString;
  }
  
  public String getErrorText() {
    return this.errorText;
  }
  
  public void read(ConfigXMLManufacturerPartPropertyMap paramConfigXMLManufacturerPartPropertyMap) throws ContentProviderConfigException {
    if (paramConfigXMLManufacturerPartPropertyMap.getSyncType() != null)
      this.syncType = Enum.<ContentProviderConfig.PropertySyncType>valueOf(ContentProviderConfig.PropertySyncType.class, paramConfigXMLManufacturerPartPropertyMap.getSyncType().value()); 
    this.bInherit = this.manufacturerPartMap.getComponentCatalog().getContentProviderConfig().isDefaultInherit();
    if (paramConfigXMLManufacturerPartPropertyMap.isInherit() != null)
      this.bInherit = paramConfigXMLManufacturerPartPropertyMap.isInherit().booleanValue(); 
    if (paramConfigXMLManufacturerPartPropertyMap.isIgnoreMissing() != null)
      this.bIgnoreMissing = paramConfigXMLManufacturerPartPropertyMap.isIgnoreMissing().booleanValue(); 
  }
  
  public void read(ConfigXMLScriptedManufacturerPartPropertyMap paramConfigXMLScriptedManufacturerPartPropertyMap) throws ContentProviderConfigException {
    if (paramConfigXMLScriptedManufacturerPartPropertyMap.getSyncType() != null)
      this.syncType = Enum.<ContentProviderConfig.PropertySyncType>valueOf(ContentProviderConfig.PropertySyncType.class, paramConfigXMLScriptedManufacturerPartPropertyMap.getSyncType().value()); 
    this.bInherit = this.manufacturerPartMap.getComponentCatalog().getContentProviderConfig().isDefaultInherit();
    if (paramConfigXMLScriptedManufacturerPartPropertyMap.isInherit() != null)
      this.bInherit = paramConfigXMLScriptedManufacturerPartPropertyMap.isInherit().booleanValue(); 
    if (paramConfigXMLScriptedManufacturerPartPropertyMap.isIgnoreMissing() != null)
      this.bIgnoreMissing = paramConfigXMLScriptedManufacturerPartPropertyMap.isIgnoreMissing().booleanValue(); 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\config\AbstractContentProviderConfigManufacturerPartPropertyMap.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */