package com.mentor.dms.contentprovider.core;

import com.mentor.datafusion.oi.OIObjectManager;
import com.mentor.dms.contentprovider.core.config.ContentProviderConfig;
import java.util.Collection;
import java.util.Map;

public interface IContentProviderResultRecord {
  AbstractContentProvider getContentProvider();
  
  String getObjectID();
  
  String getPartNumber();
  
  String getManufacturerID();
  
  String getManufacturerName();
  
  String getPartClassID();
  
  String getPartClassName();
  
  String getProductImageURL();
  
  String getResponseRaw();
  
  Collection<ComponentProperty> getPartProperties();
  
  ComponentProperty getPartProperty(String paramString);
  
  Collection<String> getPartPropertyCategories();
  
  Collection<ComponentProperty> getPartPropertiesByCategory(String paramString);
  
  Collection<ContentProviderChangeAlert> getChangeAlerts();
  
  Collection<ContentProviderFailureAlert> getFailureAlerts();
  
  Collection<ContentProviderPartStatusChange> getPartStatusChanges();
  
  Collection<ContentProviderEndOfLifeAlert> getEndOfLifeAlerts();
  
  ContentProviderDocumentList getDocuments();
  
  Collection<ContentProviderAlternate> getAlternates();
  
  Collection<IContentProviderCustomTable> getCustomTables();
  
  ContentProviderDocumentList getCategoryDocuments(String paramString);
  
  String getDatasheetURL();
  
  String getFindchipsURL();
  
  Map<String, String> getIdPropertyMap(ContentProviderConfig paramContentProviderConfig);
  
  Collection<ContentProviderSupplyChain> getSupplyChain();
  
  boolean isExistsInDMS(OIObjectManager paramOIObjectManager) throws ContentProviderException;
  
  boolean hasECADModels();
  
  boolean isECADSymbolAvailable();
  
  boolean isECADFootprintAvailable();
  
  boolean isECAD3DModelAvailable();
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\IContentProviderResultRecord.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */