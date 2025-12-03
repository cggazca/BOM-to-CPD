package com.mentor.dms.contentprovider.config;

import com.mentor.dms.contentprovider.config.xml.ConfigXMLPartClass;
import com.mentor.dms.contentprovider.config.xml.ConfigXMLProperty;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class ContentProviderConfigPartClass {
  private ContentProviderConfig cfgDoc = null;
  
  private String id;
  
  private String parentId;
  
  private String label;
  
  private List<ContentProviderConfigPartClass> childPartClassList = new ArrayList<>();
  
  private List<ContentProviderConfigProperty> propertyList = new ArrayList<>();
  
  private ArrayList<ContentProviderConfigProperty> classPropList = null;
  
  private HashMap<String, ContentProviderConfigProperty> contentProviderIDToPropertyMap = null;
  
  public ContentProviderConfigPartClass(ContentProviderConfig paramContentProviderConfig) {
    this.cfgDoc = paramContentProviderConfig;
  }
  
  public ContentProviderConfig getContentProviderConfig() {
    return this.cfgDoc;
  }
  
  public Collection<ContentProviderConfigProperty> getLeafProperties() {
    return this.propertyList;
  }
  
  public Collection<ContentProviderConfigProperty> getClassProperties() {
    if (this.classPropList == null) {
      this.classPropList = new ArrayList<>();
      ContentProviderConfigPartClass contentProviderConfigPartClass = this;
      this.classPropList.addAll(getLeafProperties());
      for (contentProviderConfigPartClass = contentProviderConfigPartClass.getParentPartClass(); contentProviderConfigPartClass != null; contentProviderConfigPartClass = contentProviderConfigPartClass.getParentPartClass()) {
        for (ContentProviderConfigProperty contentProviderConfigProperty : contentProviderConfigPartClass.getLeafProperties()) {
          if (contentProviderConfigProperty.isInherit())
            this.classPropList.add(contentProviderConfigProperty); 
        } 
      } 
    } 
    return this.classPropList;
  }
  
  public String getContentProviderId() {
    return this.id;
  }
  
  public String getContentProviderLabel() {
    return (this.label != null && !this.label.trim().isEmpty()) ? this.label : this.id;
  }
  
  public String getParentContentProviderId() {
    return this.parentId;
  }
  
  public ContentProviderConfigPartClass getParentPartClass() {
    return this.cfgDoc.getPartClassByContentProviderId(this.parentId);
  }
  
  public Collection<ContentProviderConfigPartClass> getChildPartClasses() {
    return this.childPartClassList;
  }
  
  public ContentProviderConfigProperty getClassPropertyByContentProviderId(String paramString) {
    if (this.contentProviderIDToPropertyMap == null) {
      this.contentProviderIDToPropertyMap = new HashMap<>();
      for (ContentProviderConfigProperty contentProviderConfigProperty : getClassProperties())
        this.contentProviderIDToPropertyMap.put(contentProviderConfigProperty.getContentProviderId(), contentProviderConfigProperty); 
    } 
    return this.contentProviderIDToPropertyMap.get(paramString);
  }
  
  protected void addChildPartClass(ContentProviderConfigPartClass paramContentProviderConfigPartClass) {
    this.childPartClassList.add(paramContentProviderConfigPartClass);
  }
  
  protected void read(ConfigXMLPartClass paramConfigXMLPartClass) throws ContentProviderConfigException {
    this.id = paramConfigXMLPartClass.getId();
    this.parentId = paramConfigXMLPartClass.getParentId();
    this.label = paramConfigXMLPartClass.getLabel();
    if (paramConfigXMLPartClass.getProperties() == null)
      return; 
    for (ConfigXMLProperty configXMLProperty : paramConfigXMLPartClass.getProperties().getProperty()) {
      ContentProviderConfigProperty contentProviderConfigProperty = new ContentProviderConfigProperty(this);
      contentProviderConfigProperty.read(configXMLProperty);
      this.propertyList.add(contentProviderConfigProperty);
      if (this.parentId == null && contentProviderConfigProperty.isIdProperty())
        this.cfgDoc.idPropertyList.add(contentProviderConfigProperty); 
    } 
    Collections.sort(this.cfgDoc.idPropertyList);
  }
  
  public String getLabelPath(String paramString) {
    return getLabelPath(this, paramString);
  }
  
  public String getLabelPath(ContentProviderConfigPartClass paramContentProviderConfigPartClass, String paramString) {
    return (paramContentProviderConfigPartClass.getContentProviderLabel().equals("Part") || (paramContentProviderConfigPartClass.getParentPartClass() != null && paramContentProviderConfigPartClass.getParentPartClass().getContentProviderLabel().equals("Part"))) ? paramContentProviderConfigPartClass.getContentProviderLabel() : (getLabelPath(paramContentProviderConfigPartClass.getParentPartClass(), paramString) + getLabelPath(paramContentProviderConfigPartClass.getParentPartClass(), paramString) + paramString);
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\config\ContentProviderConfigPartClass.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */