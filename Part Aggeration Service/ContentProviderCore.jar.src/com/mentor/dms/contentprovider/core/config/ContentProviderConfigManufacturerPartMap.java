package com.mentor.dms.contentprovider.core.config;

import com.mentor.dms.contentprovider.core.config.xml.BaseXMLClass;
import com.mentor.dms.contentprovider.core.config.xml.ConfigXMLManufacturerPartMap;
import com.mentor.dms.contentprovider.core.config.xml.ConfigXMLManufacturerPartPropertyMap;
import com.mentor.dms.contentprovider.core.config.xml.ConfigXMLScriptedManufacturerPartPropertyMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class ContentProviderConfigManufacturerPartMap {
  private ContentProviderConfigComponentCatalog compCatalog;
  
  private String classDMN;
  
  private ContentProviderConfig.PropertySyncType defaultSyncType = null;
  
  private List<AbstractContentProviderConfigManufacturerPartPropertyMap> manufacturerPartPropertyMapList = new ArrayList<>();
  
  private List<AbstractContentProviderConfigManufacturerPartPropertyMap> compCatalogManufacturerPropMapList = null;
  
  private HashSet<Integer> inheritedIDSet = new HashSet<>();
  
  public ContentProviderConfigManufacturerPartMap(ContentProviderConfigComponentCatalog paramContentProviderConfigComponentCatalog) {
    this.compCatalog = paramContentProviderConfigComponentCatalog;
  }
  
  public ContentProviderConfigComponentCatalog getComponentCatalog() {
    return this.compCatalog;
  }
  
  public String getClassDMN() {
    return this.classDMN;
  }
  
  public void setClassDMN(String paramString) {
    this.classDMN = paramString;
  }
  
  public ContentProviderConfig.PropertySyncType getDefaultSyncType() {
    return (this.defaultSyncType == null) ? this.compCatalog.getContentProviderConfig().getDefaultCompSyncType() : this.defaultSyncType;
  }
  
  public Collection<AbstractContentProviderConfigManufacturerPartPropertyMap> getLeafComponentPropertyMaps() {
    return this.manufacturerPartPropertyMapList;
  }
  
  public void addManufacturerPartPropertyMap(AbstractContentProviderConfigManufacturerPartPropertyMap paramAbstractContentProviderConfigManufacturerPartPropertyMap) {
    this.manufacturerPartPropertyMapList.add(paramAbstractContentProviderConfigManufacturerPartPropertyMap);
  }
  
  public void addImmediateManufacturerPartPropertyMap(AbstractContentProviderConfigManufacturerPartPropertyMap paramAbstractContentProviderConfigManufacturerPartPropertyMap) {
    this.manufacturerPartPropertyMapList.add(paramAbstractContentProviderConfigManufacturerPartPropertyMap);
    this.compCatalogManufacturerPropMapList.add(paramAbstractContentProviderConfigManufacturerPartPropertyMap);
  }
  
  public Collection<AbstractContentProviderConfigManufacturerPartPropertyMap> getCatalogComponentPropertyMaps() {
    if (this.compCatalogManufacturerPropMapList == null) {
      this.compCatalogManufacturerPropMapList = new ArrayList<>();
      this.compCatalogManufacturerPropMapList.addAll(getLeafComponentPropertyMaps());
      for (ContentProviderConfigComponentCatalog contentProviderConfigComponentCatalog = (ContentProviderConfigComponentCatalog)this.compCatalog.getParentDMSCatalog(); contentProviderConfigComponentCatalog != null; contentProviderConfigComponentCatalog = (ContentProviderConfigComponentCatalog)contentProviderConfigComponentCatalog.getParentDMSCatalog()) {
        for (ContentProviderConfigManufacturerPartMap contentProviderConfigManufacturerPartMap : contentProviderConfigComponentCatalog.getManufacturerPartMaps()) {
          for (AbstractContentProviderConfigManufacturerPartPropertyMap abstractContentProviderConfigManufacturerPartPropertyMap : contentProviderConfigManufacturerPartMap.getLeafComponentPropertyMaps()) {
            if (abstractContentProviderConfigManufacturerPartPropertyMap.isInherit()) {
              this.compCatalogManufacturerPropMapList.add(abstractContentProviderConfigManufacturerPartPropertyMap);
              this.inheritedIDSet.add(Integer.valueOf(abstractContentProviderConfigManufacturerPartPropertyMap.getId()));
            } 
          } 
        } 
      } 
    } 
    return this.compCatalogManufacturerPropMapList;
  }
  
  public void removeManufacturerPartPropertyMap(String paramString) {
    Iterator<AbstractContentProviderConfigManufacturerPartPropertyMap> iterator = this.manufacturerPartPropertyMapList.iterator();
    while (iterator.hasNext()) {
      AbstractContentProviderConfigManufacturerPartPropertyMap abstractContentProviderConfigManufacturerPartPropertyMap = iterator.next();
      if (abstractContentProviderConfigManufacturerPartPropertyMap instanceof ContentProviderConfigManufacturerPartPropertyMap) {
        ContentProviderConfigManufacturerPartPropertyMap contentProviderConfigManufacturerPartPropertyMap = (ContentProviderConfigManufacturerPartPropertyMap)abstractContentProviderConfigManufacturerPartPropertyMap;
        if (contentProviderConfigManufacturerPartPropertyMap.getComponentPropertyDMN().equals(paramString)) {
          iterator.remove();
          break;
        } 
      } 
    } 
    iterator = this.compCatalogManufacturerPropMapList.iterator();
    while (iterator.hasNext()) {
      AbstractContentProviderConfigManufacturerPartPropertyMap abstractContentProviderConfigManufacturerPartPropertyMap = iterator.next();
      if (abstractContentProviderConfigManufacturerPartPropertyMap instanceof ContentProviderConfigManufacturerPartPropertyMap) {
        ContentProviderConfigManufacturerPartPropertyMap contentProviderConfigManufacturerPartPropertyMap = (ContentProviderConfigManufacturerPartPropertyMap)abstractContentProviderConfigManufacturerPartPropertyMap;
        if (contentProviderConfigManufacturerPartPropertyMap.getComponentPropertyDMN().equals(paramString)) {
          iterator.remove();
          break;
        } 
      } 
    } 
  }
  
  public void removeManufacturerPartPropertyMap(AbstractContentProviderConfigManufacturerPartPropertyMap paramAbstractContentProviderConfigManufacturerPartPropertyMap) {
    this.manufacturerPartPropertyMapList.remove(paramAbstractContentProviderConfigManufacturerPartPropertyMap);
    this.compCatalogManufacturerPropMapList.remove(paramAbstractContentProviderConfigManufacturerPartPropertyMap);
  }
  
  public void read(ConfigXMLManufacturerPartMap paramConfigXMLManufacturerPartMap) throws ContentProviderConfigException {
    this.classDMN = paramConfigXMLManufacturerPartMap.getClassDMN();
    if (paramConfigXMLManufacturerPartMap.getManufacturerPartPropertyMaps() == null)
      return; 
    if (paramConfigXMLManufacturerPartMap.getManufacturerPartPropertyMaps().getDefaultSyncType() != null)
      this.defaultSyncType = Enum.<ContentProviderConfig.PropertySyncType>valueOf(ContentProviderConfig.PropertySyncType.class, paramConfigXMLManufacturerPartMap.getManufacturerPartPropertyMaps().getDefaultSyncType().value()); 
    for (BaseXMLClass baseXMLClass : paramConfigXMLManufacturerPartMap.getManufacturerPartPropertyMaps().getManufacturerPartPropertyMapOrScriptedManufacturerPartPropertyMap()) {
      if (baseXMLClass instanceof ConfigXMLManufacturerPartPropertyMap) {
        ConfigXMLManufacturerPartPropertyMap configXMLManufacturerPartPropertyMap = (ConfigXMLManufacturerPartPropertyMap)baseXMLClass;
        ContentProviderConfigManufacturerPartPropertyMap contentProviderConfigManufacturerPartPropertyMap = new ContentProviderConfigManufacturerPartPropertyMap(this);
        contentProviderConfigManufacturerPartPropertyMap.read(configXMLManufacturerPartPropertyMap);
        this.manufacturerPartPropertyMapList.add(contentProviderConfigManufacturerPartPropertyMap);
        continue;
      } 
      if (baseXMLClass instanceof ConfigXMLScriptedManufacturerPartPropertyMap) {
        ConfigXMLScriptedManufacturerPartPropertyMap configXMLScriptedManufacturerPartPropertyMap = (ConfigXMLScriptedManufacturerPartPropertyMap)baseXMLClass;
        ContentProviderConfigScriptedManufacturerPartPropertyMap contentProviderConfigScriptedManufacturerPartPropertyMap = new ContentProviderConfigScriptedManufacturerPartPropertyMap(this);
        contentProviderConfigScriptedManufacturerPartPropertyMap.read(configXMLScriptedManufacturerPartPropertyMap);
        this.manufacturerPartPropertyMapList.add(contentProviderConfigScriptedManufacturerPartPropertyMap);
      } 
    } 
  }
  
  public boolean isInherited(AbstractContentProviderConfigManufacturerPartPropertyMap paramAbstractContentProviderConfigManufacturerPartPropertyMap) {
    return this.inheritedIDSet.contains(Integer.valueOf(paramAbstractContentProviderConfigManufacturerPartPropertyMap.getId()));
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\config\ContentProviderConfigManufacturerPartMap.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */