package com.mentor.dms.contentprovider.config;

import com.mentor.dms.contentprovider.config.xml.ConfigXMLManufacturerPartPropertyMap;

public class ContentProviderConfigManufacturerPartPropertyMap extends AbstractContentProviderConfigManufacturerPartPropertyMap {
  private String compDMN;
  
  private String mpnDMN;
  
  public ContentProviderConfigManufacturerPartPropertyMap(ContentProviderConfigManufacturerPartMap paramContentProviderConfigManufacturerPartMap) {
    super(paramContentProviderConfigManufacturerPartMap);
  }
  
  public String getComponentPropertyDMN() {
    return this.compDMN;
  }
  
  public void setComponentPropertyDMN(String paramString) {
    this.compDMN = paramString;
  }
  
  public String getManufacturerPartPropertyDMN() {
    return this.mpnDMN;
  }
  
  public void setManufacturerPartPropertyDMN(String paramString) {
    this.mpnDMN = paramString;
  }
  
  public void read(ConfigXMLManufacturerPartPropertyMap paramConfigXMLManufacturerPartPropertyMap) throws ContentProviderConfigException {
    super.read(paramConfigXMLManufacturerPartPropertyMap);
    this.mpnDMN = paramConfigXMLManufacturerPartPropertyMap.getMpnDMN();
    this.compDMN = paramConfigXMLManufacturerPartPropertyMap.getCompDMN();
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\config\ContentProviderConfigManufacturerPartPropertyMap.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */