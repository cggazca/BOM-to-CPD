package com.mentor.dms.contentprovider.core.config;

import com.mentor.dms.contentprovider.core.config.xml.ConfigXMLComponentCatalog;
import com.mentor.dms.contentprovider.core.config.xml.ConfigXMLContentProviderMap;
import com.mentor.dms.contentprovider.core.config.xml.ConfigXMLManufacturerPartMap;
import java.util.Collection;
import java.util.LinkedHashMap;

public class ContentProviderConfigComponentCatalog extends ContentProviderConfigDMSCatalog<ContentProviderConfigComponentCatalog> {
  private ContentProviderConfigDMSCatalogRepository<ContentProviderConfigComponentCatalog> repository;
  
  private LinkedHashMap<String, ContentProviderConfigManufacturerPartMap> dmn2ManufacturerPartMapMap = new LinkedHashMap<>();
  
  private boolean bAllowMoveParts = true;
  
  public ContentProviderConfigComponentCatalog(ContentProviderConfig paramContentProviderConfig, ContentProviderConfigDMSCatalogRepository<ContentProviderConfigComponentCatalog> paramContentProviderConfigDMSCatalogRepository) {
    super(paramContentProviderConfig);
    this.repository = paramContentProviderConfigDMSCatalogRepository;
  }
  
  public Collection<ContentProviderConfigManufacturerPartMap> getManufacturerPartMaps() {
    return this.dmn2ManufacturerPartMapMap.values();
  }
  
  public boolean isAllowMoveParts() {
    return this.bAllowMoveParts;
  }
  
  protected void addManufacturerPartMap(ContentProviderConfigManufacturerPartMap paramContentProviderConfigManufacturerPartMap) {
    this.dmn2ManufacturerPartMapMap.put(paramContentProviderConfigManufacturerPartMap.getClassDMN(), paramContentProviderConfigManufacturerPartMap);
  }
  
  protected void read(ConfigXMLComponentCatalog paramConfigXMLComponentCatalog) throws ContentProviderConfigException {
    this.classDMN = paramConfigXMLComponentCatalog.getClassDMN();
    this.repository.addCatalog(this);
    this.bAllowMoveParts = paramConfigXMLComponentCatalog.isAllowMoveParts();
    if (paramConfigXMLComponentCatalog.getContentProviderMaps() != null)
      for (ConfigXMLContentProviderMap configXMLContentProviderMap : paramConfigXMLComponentCatalog.getContentProviderMaps().getContentProviderMap()) {
        ContentProviderConfigContentProviderMap contentProviderConfigContentProviderMap = new ContentProviderConfigContentProviderMap(this);
        contentProviderConfigContentProviderMap.read(configXMLContentProviderMap);
        this.contentProviderMapList.add(contentProviderConfigContentProviderMap);
        this.repository.addContentProviderReference(configXMLContentProviderMap.getCcpId(), this);
      }  
    if (paramConfigXMLComponentCatalog.getManufacturerPartMaps() != null)
      for (ConfigXMLManufacturerPartMap configXMLManufacturerPartMap : paramConfigXMLComponentCatalog.getManufacturerPartMaps().getManufacturerPartMap()) {
        ContentProviderConfigManufacturerPartMap contentProviderConfigManufacturerPartMap = this.dmn2ManufacturerPartMapMap.get(configXMLManufacturerPartMap.getClassDMN());
        if (contentProviderConfigManufacturerPartMap == null) {
          contentProviderConfigManufacturerPartMap = new ContentProviderConfigManufacturerPartMap(this);
          this.dmn2ManufacturerPartMapMap.put(configXMLManufacturerPartMap.getClassDMN(), contentProviderConfigManufacturerPartMap);
        } 
        contentProviderConfigManufacturerPartMap.read(configXMLManufacturerPartMap);
      }  
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\config\ContentProviderConfigComponentCatalog.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */