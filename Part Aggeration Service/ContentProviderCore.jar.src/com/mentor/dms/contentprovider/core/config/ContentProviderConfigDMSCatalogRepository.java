package com.mentor.dms.contentprovider.core.config;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ContentProviderConfigDMSCatalogRepository<T extends ContentProviderConfigDMSCatalog<?>> {
  private LinkedHashMap<String, T> dmn2CatalogMap = new LinkedHashMap<>();
  
  private Map<String, Map<String, T>> ccpIdToCatalogListMap = new HashMap<>();
  
  public void addCatalog(T paramT) {
    this.dmn2CatalogMap.put(paramT.getClassDMN(), paramT);
  }
  
  public Collection<T> getCatalogs() {
    return this.dmn2CatalogMap.values();
  }
  
  public T getCatalog(String paramString) {
    return this.dmn2CatalogMap.get(paramString);
  }
  
  public Collection<T> getCatalogsByContentProviderId(String paramString) {
    return this.ccpIdToCatalogListMap.containsKey(paramString) ? ((Map)this.ccpIdToCatalogListMap.get(paramString)).values() : Collections.emptyList();
  }
  
  public void addContentProviderReference(String paramString, T paramT) {
    Map<Object, Object> map = (Map)this.ccpIdToCatalogListMap.get(paramString);
    if (map == null) {
      map = new HashMap<>();
      this.ccpIdToCatalogListMap.put(paramString, map);
    } 
    if (!map.containsKey(paramT.getClassDMN()))
      map.put(paramT.getClassDMN(), paramT); 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\config\ContentProviderConfigDMSCatalogRepository.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */