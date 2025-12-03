package com.mentor.dms.contentprovider;

import com.mentor.dms.contentprovider.config.ContentProviderConfig;
import com.mentor.dms.contentprovider.config.ContentProviderConfigProperty;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractContentProviderResultRecord implements IContentProviderResultRecord {
  private List<ComponentProperty> partProps = new ArrayList<>();
  
  private Map<String, ComponentProperty> partPropsMap = new HashMap<>();
  
  private Map<String, List<ComponentProperty>> partCategoryMap = new LinkedHashMap<>();
  
  private List<ContentProviderChangeAlert> changeAlertList = new ArrayList<>();
  
  private List<ContentProviderFailureAlert> failureAlertList = new ArrayList<>();
  
  private List<ContentProviderPartStatusChange> partStatusChangeList = new ArrayList<>();
  
  private List<ContentProviderEndOfLifeAlert> eolAlertList = new ArrayList<>();
  
  private ContentProviderDocumentList documentList = new ContentProviderDocumentList();
  
  private List<ContentProviderAlternate> alternatesList = new ArrayList<>();
  
  private List<IContentProviderCustomTable> customTableList = new ArrayList<>();
  
  private HashMap<String, ContentProviderDocumentList> categoryDocumentsMap = new HashMap<>();
  
  private String datasheetURL = null;
  
  private String productImageURL = null;
  
  protected AbstractContentProvider ccp = null;
  
  public AbstractContentProviderResultRecord(AbstractContentProvider paramAbstractContentProvider) {
    this.ccp = paramAbstractContentProvider;
  }
  
  public AbstractContentProvider getContentProvider() {
    return this.ccp;
  }
  
  public abstract String getObjectID();
  
  public abstract String getPartNumber();
  
  public abstract String getManufacturerName();
  
  public abstract String getPartClassID();
  
  public abstract String getPartClassName();
  
  public String getProductImageURL() {
    return this.productImageURL;
  }
  
  public void setProductImageURL(String paramString) {
    this.productImageURL = paramString;
  }
  
  public Collection<ComponentProperty> getPartProperties() {
    return this.partProps;
  }
  
  public ComponentProperty getPartProperty(String paramString) {
    return this.partPropsMap.get(paramString);
  }
  
  public List<ComponentProperty> addCategory(String paramString) {
    List<ComponentProperty> list = this.partCategoryMap.get(paramString);
    if (list == null) {
      list = new ArrayList();
      this.partCategoryMap.put(paramString, list);
    } 
    return list;
  }
  
  public Collection<ContentProviderChangeAlert> getChangeAlerts() {
    return this.changeAlertList;
  }
  
  public Collection<ContentProviderFailureAlert> getFailureAlerts() {
    return this.failureAlertList;
  }
  
  public Collection<ContentProviderPartStatusChange> getPartStatusChanges() {
    return this.partStatusChangeList;
  }
  
  public Collection<ContentProviderEndOfLifeAlert> getEndOfLifeAlerts() {
    return this.eolAlertList;
  }
  
  public ContentProviderDocumentList getDocuments() {
    return this.documentList;
  }
  
  public Collection<ContentProviderAlternate> getAlternates() {
    return this.alternatesList;
  }
  
  public Collection<IContentProviderCustomTable> getCustomTables() {
    return this.customTableList;
  }
  
  public ContentProviderDocumentList getCategoryDocuments(String paramString) {
    return this.categoryDocumentsMap.get(paramString);
  }
  
  public void addPartProperty(String paramString, ComponentProperty paramComponentProperty) {
    this.partProps.add(paramComponentProperty);
    this.partPropsMap.put(paramComponentProperty.getId(), paramComponentProperty);
    List<ComponentProperty> list = this.partCategoryMap.get(paramString);
    if (list == null)
      list = addCategory(paramString); 
    list.add(paramComponentProperty);
  }
  
  public Collection<ComponentProperty> getPartPropertiesByCategory(String paramString) {
    return this.partCategoryMap.get(paramString);
  }
  
  public Collection<String> getPartPropertyCategories() {
    return this.partCategoryMap.keySet();
  }
  
  public void addChangeAlert(ContentProviderChangeAlert paramContentProviderChangeAlert) {
    this.changeAlertList.add(paramContentProviderChangeAlert);
  }
  
  public void addFailureAlert(ContentProviderFailureAlert paramContentProviderFailureAlert) {
    this.failureAlertList.add(paramContentProviderFailureAlert);
  }
  
  public void addPartStatusChange(ContentProviderPartStatusChange paramContentProviderPartStatusChange) {
    this.partStatusChangeList.add(paramContentProviderPartStatusChange);
  }
  
  public void addEndOfLifeAlert(ContentProviderEndOfLifeAlert paramContentProviderEndOfLifeAlert) {
    this.eolAlertList.add(paramContentProviderEndOfLifeAlert);
  }
  
  public void addDocument(ContentProviderDocument paramContentProviderDocument) {
    this.documentList.add(paramContentProviderDocument);
  }
  
  public void addAlternate(ContentProviderAlternate paramContentProviderAlternate) {
    this.alternatesList.add(paramContentProviderAlternate);
  }
  
  public void addCustomTable(IContentProviderCustomTable paramIContentProviderCustomTable) {
    this.customTableList.add(paramIContentProviderCustomTable);
  }
  
  public Map<String, String> getIdPropertyMap(ContentProviderConfig paramContentProviderConfig) {
    HashMap<Object, Object> hashMap = new HashMap<>();
    for (ContentProviderConfigProperty contentProviderConfigProperty : paramContentProviderConfig.getIdProperties())
      hashMap.put(contentProviderConfigProperty.getContentProviderId(), getPartProperty(contentProviderConfigProperty.getContentProviderId()).getValue()); 
    return (Map)hashMap;
  }
  
  public String getDatasheetURL() {
    return this.datasheetURL;
  }
  
  public void setDatasheetURL(String paramString) {
    this.datasheetURL = paramString;
  }
  
  public void addCategoryDocumentList(String paramString, ContentProviderDocumentList paramContentProviderDocumentList) {
    this.categoryDocumentsMap.put(paramString, paramContentProviderDocumentList);
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\AbstractContentProviderResultRecord.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */