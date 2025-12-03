package com.mentor.dms.contentprovider.core.plugin.searchui;

import com.mentor.datafusion.oi.model.OIClass;
import com.mentor.dms.contentprovider.core.config.ContentProviderConfigContentProviderMap;
import com.mentor.dms.contentprovider.core.config.ContentProviderConfigDMSCatalog;
import com.mentor.dms.contentprovider.core.config.ContentProviderConfigPartClass;
import com.mentor.dms.contentprovider.core.config.ContentProviderConfigProperty;
import com.mentor.dms.ui.searchmask.restrictions.SearchCondition;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class ContentProviderSearchRestriction {
  private static HashMap<String, HashMap<String, String>> tabMapCache = new HashMap<>();
  
  private OIClass oiClass = null;
  
  private String searchRestrictionChecksum = "";
  
  private ContentProviderConfigDMSCatalog<?> dmsCatalogConfig = null;
  
  private ContentProviderConfigContentProviderMap ccpConfigMap = null;
  
  private ContentProviderConfigPartClass partClass;
  
  private HashMap<String, String> tabMap;
  
  private boolean bQuickSearchEnabled = false;
  
  private String quickSearchString = "";
  
  private ArrayList<ContentProviderCharacteristic> charList = new ArrayList<>();
  
  public ContentProviderConfigPartClass getPartClass() {
    return this.partClass;
  }
  
  public void setPartClass(ContentProviderConfigPartClass paramContentProviderConfigPartClass) {
    this.partClass = paramContentProviderConfigPartClass;
  }
  
  public OIClass getOIClass() {
    return this.oiClass;
  }
  
  public void setOIClass(OIClass paramOIClass) {
    this.oiClass = paramOIClass;
  }
  
  public String getSearchRestrictionChecksum() {
    return this.searchRestrictionChecksum;
  }
  
  public void setSearchRestrictionChecksum(String paramString) {
    this.searchRestrictionChecksum = paramString;
  }
  
  public ContentProviderConfigDMSCatalog<?> getDmsCatalogConfig() {
    return this.dmsCatalogConfig;
  }
  
  public void setDMSCatalogConfig(ContentProviderConfigDMSCatalog<?> paramContentProviderConfigDMSCatalog) {
    this.dmsCatalogConfig = paramContentProviderConfigDMSCatalog;
  }
  
  public ContentProviderConfigContentProviderMap getCCPConfigMap() {
    return this.ccpConfigMap;
  }
  
  public void setCCPConfigMap(ContentProviderConfigContentProviderMap paramContentProviderConfigContentProviderMap) {
    this.ccpConfigMap = paramContentProviderConfigContentProviderMap;
  }
  
  public boolean isQuickSearchEnabled() {
    return this.bQuickSearchEnabled;
  }
  
  public void setQuickSearchEnabled(boolean paramBoolean) {
    this.bQuickSearchEnabled = paramBoolean;
  }
  
  public String getQuickSearchString() {
    return this.quickSearchString;
  }
  
  public void setQuickSearchString(String paramString) {
    this.quickSearchString = paramString;
  }
  
  public void addCharacteristic(SearchCondition paramSearchCondition, ContentProviderConfigProperty paramContentProviderConfigProperty) {
    ContentProviderCharacteristic contentProviderCharacteristic = new ContentProviderCharacteristic(this, paramSearchCondition.getField(), paramContentProviderConfigProperty);
    this.charList.add(contentProviderCharacteristic);
  }
  
  public Collection<ContentProviderCharacteristic> getCharacteristics() {
    return this.charList;
  }
  
  public void setTabMap(HashMap<String, String> paramHashMap) {
    this.tabMap = paramHashMap;
  }
  
  public HashMap<String, String> getTabMap() {
    return this.tabMap;
  }
  
  public static HashMap<String, HashMap<String, String>> getTabMapCache() {
    return tabMapCache;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\plugin\searchui\ContentProviderSearchRestriction.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */