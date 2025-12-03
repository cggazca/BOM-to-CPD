package com.mentor.dms.contentprovider.config;

import com.mentor.dms.contentprovider.config.xml.ConfigXMLContentProviderPropertyMap;

public class ContentProviderConfigPropertyMap implements Comparable<ContentProviderConfigPropertyMap> {
  private ContentProviderConfigContentProviderMap ccpMap = null;
  
  private String contentProviderID;
  
  private String dmn;
  
  private String searchDMN = null;
  
  private ContentProviderConfig.PropertySyncType syncType = null;
  
  private boolean bInherit = true;
  
  public ContentProviderConfigPropertyMap(ContentProviderConfigContentProviderMap paramContentProviderConfigContentProviderMap) {
    this.ccpMap = paramContentProviderConfigContentProviderMap;
  }
  
  public ContentProviderConfigContentProviderMap getContentProviderMap() {
    return this.ccpMap;
  }
  
  public ContentProviderConfigDMSCatalog getDMSCatalog() {
    return this.ccpMap.getDMSCatalog();
  }
  
  public String getContentProviderId() {
    return this.contentProviderID;
  }
  
  public String getDMN() {
    return this.dmn;
  }
  
  public String getSearchDMN() {
    return (this.searchDMN != null && !this.searchDMN.isEmpty()) ? this.searchDMN : this.dmn;
  }
  
  public ContentProviderConfig.PropertySyncType getSyncType() {
    return (this.syncType == null) ? this.ccpMap.getDefaultSyncType() : this.syncType;
  }
  
  public boolean isInherit() {
    return this.bInherit;
  }
  
  protected void setContentProviderId(String paramString) {
    this.contentProviderID = paramString;
  }
  
  protected void setDMN(String paramString) {
    this.dmn = paramString;
  }
  
  protected void setSearchDMN(String paramString) {
    this.searchDMN = paramString;
  }
  
  protected void setSyncType(ContentProviderConfig.PropertySyncType paramPropertySyncType) {
    this.syncType = paramPropertySyncType;
  }
  
  public void setInherit(boolean paramBoolean) {
    this.bInherit = paramBoolean;
  }
  
  public void read(ConfigXMLContentProviderPropertyMap paramConfigXMLContentProviderPropertyMap) throws ContentProviderConfigException {
    this.contentProviderID = paramConfigXMLContentProviderPropertyMap.getCcpId();
    if (paramConfigXMLContentProviderPropertyMap.getSyncType() != null)
      this.syncType = Enum.<ContentProviderConfig.PropertySyncType>valueOf(ContentProviderConfig.PropertySyncType.class, paramConfigXMLContentProviderPropertyMap.getSyncType().value()); 
    this.dmn = paramConfigXMLContentProviderPropertyMap.getDmn();
    this.searchDMN = paramConfigXMLContentProviderPropertyMap.getSearchDMN();
    this.bInherit = getContentProviderMap().getDMSCatalog().getContentProviderConfig().isDefaultInherit();
    if (paramConfigXMLContentProviderPropertyMap.isInherit() != null)
      this.bInherit = paramConfigXMLContentProviderPropertyMap.isInherit().booleanValue(); 
  }
  
  public int compareTo(ContentProviderConfigPropertyMap paramContentProviderConfigPropertyMap) {
    return getContentProviderId().compareTo(paramContentProviderConfigPropertyMap.getContentProviderId());
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\config\ContentProviderConfigPropertyMap.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */