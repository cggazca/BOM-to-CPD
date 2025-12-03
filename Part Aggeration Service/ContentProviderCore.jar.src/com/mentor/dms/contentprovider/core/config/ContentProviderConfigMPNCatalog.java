package com.mentor.dms.contentprovider.core.config;

import com.mentor.dms.contentprovider.core.config.xml.ConfigXMLContentProviderMap;
import com.mentor.dms.contentprovider.core.config.xml.ConfigXMLManufacturerPartCatalog;

public class ContentProviderConfigMPNCatalog extends ContentProviderConfigDMSCatalog<ContentProviderConfigMPNCatalog> {
  private ContentProviderConfigDMSCatalogRepository<ContentProviderConfigMPNCatalog> repository;
  
  protected ContentProviderConfigMPNCatalog(ContentProviderConfig paramContentProviderConfig, ContentProviderConfigDMSCatalogRepository<ContentProviderConfigMPNCatalog> paramContentProviderConfigDMSCatalogRepository) {
    super(paramContentProviderConfig);
    this.repository = paramContentProviderConfigDMSCatalogRepository;
  }
  
  public ContentProviderConfigContentProviderMap getContentProviderMap() {
    return this.contentProviderMapList.get(0);
  }
  
  protected void read(ConfigXMLManufacturerPartCatalog paramConfigXMLManufacturerPartCatalog) throws ContentProviderConfigException {
    this.classDMN = paramConfigXMLManufacturerPartCatalog.getClassDMN();
    this.repository.addCatalog(this);
    if (paramConfigXMLManufacturerPartCatalog.getContentProviderMaps() != null)
      for (ConfigXMLContentProviderMap configXMLContentProviderMap : paramConfigXMLManufacturerPartCatalog.getContentProviderMaps().getContentProviderMap()) {
        ContentProviderConfigContentProviderMap contentProviderConfigContentProviderMap = new ContentProviderConfigContentProviderMap(this);
        contentProviderConfigContentProviderMap.read(configXMLContentProviderMap);
        this.contentProviderMapList.add(contentProviderConfigContentProviderMap);
        this.repository.addContentProviderReference(configXMLContentProviderMap.getCcpId(), this);
      }  
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\config\ContentProviderConfigMPNCatalog.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */