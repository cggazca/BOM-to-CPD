package com.mentor.dms.contentprovider.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class ContentProviderConfigDMSCatalog<T extends ContentProviderConfigDMSCatalog<?>> {
  private ContentProviderConfig cfgDoc = null;
  
  protected ContentProviderConfigDMSCatalog<?> parentDMSCatalog = null;
  
  protected String classDMN;
  
  protected List<ContentProviderConfigContentProviderMap> contentProviderMapList = new ArrayList<>();
  
  public ContentProviderConfigDMSCatalog(ContentProviderConfig paramContentProviderConfig) {
    this.cfgDoc = paramContentProviderConfig;
  }
  
  public ContentProviderConfig getContentProviderConfig() {
    return this.cfgDoc;
  }
  
  public ContentProviderConfigDMSCatalog<?> getParentDMSCatalog() {
    return this.parentDMSCatalog;
  }
  
  public String getClassDMN() {
    return this.classDMN;
  }
  
  protected void setClassDMN(String paramString) {
    this.classDMN = paramString;
  }
  
  protected void addContentProviderMap(ContentProviderConfigContentProviderMap paramContentProviderConfigContentProviderMap) {
    this.contentProviderMapList.add(paramContentProviderConfigContentProviderMap);
  }
  
  public Collection<ContentProviderConfigContentProviderMap> getContentProviderMaps() {
    return this.contentProviderMapList;
  }
  
  public ContentProviderConfigContentProviderMap getContentProviderMapInstance(ContentProviderConfigPartClass paramContentProviderConfigPartClass) {
    ContentProviderConfigContentProviderMap contentProviderConfigContentProviderMap = new ContentProviderConfigContentProviderMap(this);
    contentProviderConfigContentProviderMap.setContentProviderId(paramContentProviderConfigPartClass.getContentProviderId());
    contentProviderConfigContentProviderMap.setPartClass(paramContentProviderConfigPartClass);
    this.contentProviderMapList.add(contentProviderConfigContentProviderMap);
    return contentProviderConfigContentProviderMap;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\config\ContentProviderConfigDMSCatalog.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */