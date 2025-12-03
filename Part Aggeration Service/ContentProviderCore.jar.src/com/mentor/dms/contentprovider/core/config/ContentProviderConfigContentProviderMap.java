package com.mentor.dms.contentprovider.core.config;

import com.mentor.dms.contentprovider.core.config.xml.ConfigXMLContentProviderMap;
import com.mentor.dms.contentprovider.core.config.xml.ConfigXMLContentProviderPropertyMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.codec.digest.DigestUtils;

public class ContentProviderConfigContentProviderMap {
  private ContentProviderConfigDMSCatalog<?> dmsCatalog;
  
  private String contentProviderId;
  
  private ContentProviderConfig.PropertySyncType defaultSyncType = null;
  
  private List<ContentProviderConfigPropertyMap> leafPropertyMapList = new ArrayList<>();
  
  private HashMap<String, ContentProviderConfigPropertyMap> leafPropertyMapByContentProviderIdMap = new HashMap<>();
  
  private List<ContentProviderConfigPropertyMap> catalogPropertyMapList = null;
  
  private Map<String, ContentProviderConfigPropertyMap> dmsCatalogPropMapByDMNMap = null;
  
  private Map<String, ContentProviderConfigPropertyMap> dmsCatalogPropMapBySearchDMNMap = null;
  
  private Map<String, ContentProviderConfigPropertyMap> dmsCatalogPropMapByContentProviderIdMap = null;
  
  private String propertyConfigChecksum = null;
  
  private ContentProviderConfigPartClass partClass = null;
  
  public ContentProviderConfigContentProviderMap(ContentProviderConfigDMSCatalog<?> paramContentProviderConfigDMSCatalog) {
    this.dmsCatalog = paramContentProviderConfigDMSCatalog;
  }
  
  public ContentProviderConfigDMSCatalog getDMSCatalog() {
    return this.dmsCatalog;
  }
  
  public ContentProviderConfigPartClass getPartClass() {
    return this.partClass;
  }
  
  public String getContentProviderId() {
    return this.contentProviderId;
  }
  
  public String getContentProviderLabel() {
    return (this.partClass != null) ? this.partClass.getContentProviderLabel() : "Unknown Part Class";
  }
  
  public ContentProviderConfig.PropertySyncType getDefaultSyncType() {
    return (this.defaultSyncType == null) ? getDMSCatalog().getContentProviderConfig().getDefaultMPNSyncType() : this.defaultSyncType;
  }
  
  protected void setContentProviderId(String paramString) {
    this.contentProviderId = paramString;
  }
  
  protected void setPartClass(ContentProviderConfigPartClass paramContentProviderConfigPartClass) {
    this.partClass = paramContentProviderConfigPartClass;
  }
  
  protected void addContentProviderPropertyMap(ContentProviderConfigPropertyMap paramContentProviderConfigPropertyMap) {
    this.leafPropertyMapList.add(paramContentProviderConfigPropertyMap);
    this.leafPropertyMapByContentProviderIdMap.put(paramContentProviderConfigPropertyMap.getContentProviderId(), paramContentProviderConfigPropertyMap);
  }
  
  public List<ContentProviderConfigPropertyMap> getLeafPropertyMaps() {
    return this.leafPropertyMapList;
  }
  
  public ContentProviderConfigPropertyMap getLeafPropertyMapByContentProviderId(String paramString) {
    return this.leafPropertyMapByContentProviderIdMap.get(paramString);
  }
  
  public Collection<ContentProviderConfigPropertyMap> getCatalogPropertyMaps() {
    if (this.catalogPropertyMapList == null) {
      this.catalogPropertyMapList = new ArrayList<>();
      this.dmsCatalogPropMapByDMNMap = new HashMap<>();
      this.dmsCatalogPropMapBySearchDMNMap = new HashMap<>();
      this.dmsCatalogPropMapByContentProviderIdMap = new HashMap<>();
      for (ContentProviderConfigPropertyMap contentProviderConfigPropertyMap : this.leafPropertyMapList) {
        this.catalogPropertyMapList.add(contentProviderConfigPropertyMap);
        this.dmsCatalogPropMapByDMNMap.put(contentProviderConfigPropertyMap.getDMN(), contentProviderConfigPropertyMap);
        this.dmsCatalogPropMapBySearchDMNMap.put(contentProviderConfigPropertyMap.getSearchDMN(), contentProviderConfigPropertyMap);
        this.dmsCatalogPropMapByContentProviderIdMap.put(contentProviderConfigPropertyMap.getContentProviderId(), contentProviderConfigPropertyMap);
      } 
      for (ContentProviderConfigDMSCatalog<?> contentProviderConfigDMSCatalog = this.dmsCatalog.getParentDMSCatalog(); contentProviderConfigDMSCatalog != null; contentProviderConfigDMSCatalog = contentProviderConfigDMSCatalog.getParentDMSCatalog())
        addMaps(contentProviderConfigDMSCatalog); 
      Collections.sort(this.catalogPropertyMapList, new DMNComparator());
    } 
    return this.catalogPropertyMapList;
  }
  
  private void addMaps(ContentProviderConfigDMSCatalog<?> paramContentProviderConfigDMSCatalog) {
    for (ContentProviderConfigContentProviderMap contentProviderConfigContentProviderMap : paramContentProviderConfigDMSCatalog.getContentProviderMaps()) {
      for (ContentProviderConfigPropertyMap contentProviderConfigPropertyMap : contentProviderConfigContentProviderMap.leafPropertyMapList) {
        if (contentProviderConfigPropertyMap.isInherit()) {
          this.catalogPropertyMapList.add(contentProviderConfigPropertyMap);
          this.dmsCatalogPropMapByDMNMap.put(contentProviderConfigPropertyMap.getDMN(), contentProviderConfigPropertyMap);
          this.dmsCatalogPropMapBySearchDMNMap.put(contentProviderConfigPropertyMap.getSearchDMN(), contentProviderConfigPropertyMap);
          this.dmsCatalogPropMapByContentProviderIdMap.put(contentProviderConfigPropertyMap.getContentProviderId(), contentProviderConfigPropertyMap);
        } 
      } 
    } 
  }
  
  public ContentProviderConfigPropertyMap getPropertyMapByDMN(String paramString) {
    getCatalogPropertyMaps();
    return this.dmsCatalogPropMapByDMNMap.get(paramString);
  }
  
  public ContentProviderConfigPropertyMap getPropertyMapBySearchDMN(String paramString) {
    getCatalogPropertyMaps();
    return this.dmsCatalogPropMapBySearchDMNMap.get(paramString);
  }
  
  public ContentProviderConfigPropertyMap getPropertyMapByContentProviderId(String paramString) {
    getCatalogPropertyMaps();
    return this.dmsCatalogPropMapByContentProviderIdMap.get(paramString);
  }
  
  protected void read(ConfigXMLContentProviderMap paramConfigXMLContentProviderMap) throws ContentProviderConfigException {
    this.contentProviderId = paramConfigXMLContentProviderMap.getCcpId();
    this.partClass = this.dmsCatalog.getContentProviderConfig().getPartClassByContentProviderId(this.contentProviderId);
    if (paramConfigXMLContentProviderMap.getContentProviderPropertyMaps() == null)
      return; 
    if (paramConfigXMLContentProviderMap.getContentProviderPropertyMaps().getDefaultSyncType() != null)
      this.defaultSyncType = Enum.<ContentProviderConfig.PropertySyncType>valueOf(ContentProviderConfig.PropertySyncType.class, paramConfigXMLContentProviderMap.getContentProviderPropertyMaps().getDefaultSyncType().value()); 
    for (ConfigXMLContentProviderPropertyMap configXMLContentProviderPropertyMap : paramConfigXMLContentProviderMap.getContentProviderPropertyMaps().getContentProviderPropertyMap()) {
      ContentProviderConfigPropertyMap contentProviderConfigPropertyMap = new ContentProviderConfigPropertyMap(this);
      contentProviderConfigPropertyMap.read(configXMLContentProviderPropertyMap);
      this.leafPropertyMapList.add(contentProviderConfigPropertyMap);
    } 
  }
  
  public String getPropertyConfigChecksum() {
    if (this.propertyConfigChecksum == null) {
      StringBuilder stringBuilder = new StringBuilder();
      ContentProviderConfigPartClass contentProviderConfigPartClass = this.dmsCatalog.getContentProviderConfig().getPartClassByContentProviderId(getContentProviderId());
      stringBuilder.append(contentProviderConfigPartClass.getContentProviderId());
      for (ContentProviderConfigPropertyMap contentProviderConfigPropertyMap : getCatalogPropertyMaps()) {
        if (contentProviderConfigPropertyMap.getSyncType() != ContentProviderConfig.PropertySyncType.IGNORE) {
          ContentProviderConfigProperty contentProviderConfigProperty = contentProviderConfigPartClass.getClassPropertyByContentProviderId(contentProviderConfigPropertyMap.getContentProviderId());
          if (contentProviderConfigProperty != null)
            stringBuilder.append(contentProviderConfigProperty.getContentProviderId()); 
        } 
      } 
      this.propertyConfigChecksum = DigestUtils.md5Hex(stringBuilder.toString());
    } 
    return this.propertyConfigChecksum;
  }
  
  public String toString() {
    return getContentProviderLabel();
  }
  
  public class DMNComparator implements Comparator<ContentProviderConfigPropertyMap> {
    public int compare(ContentProviderConfigPropertyMap param1ContentProviderConfigPropertyMap1, ContentProviderConfigPropertyMap param1ContentProviderConfigPropertyMap2) {
      String str1 = (param1ContentProviderConfigPropertyMap1.getDMN() == null) ? "" : param1ContentProviderConfigPropertyMap1.getDMN();
      String str2 = (param1ContentProviderConfigPropertyMap2.getDMN() == null) ? "" : param1ContentProviderConfigPropertyMap2.getDMN();
      return str1.compareTo(str2);
    }
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\config\ContentProviderConfigContentProviderMap.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */