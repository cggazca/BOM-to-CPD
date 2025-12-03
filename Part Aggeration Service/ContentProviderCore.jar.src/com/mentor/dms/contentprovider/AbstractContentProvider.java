package com.mentor.dms.contentprovider;

import com.mentor.datafusion.oi.OIObjectManager;
import com.mentor.datafusion.utils.logger.MGLogger;
import com.mentor.dms.contentprovider.actions.AbstractSearchResultsAction;
import com.mentor.dms.contentprovider.client.userpref.AbstractUserPreferences;
import com.mentor.dms.contentprovider.config.ContentProviderConfig;
import com.mentor.dms.contentprovider.config.ContentProviderConfigException;
import com.mentor.dms.contentprovider.config.ContentProviderConfigPartClass;
import com.mentor.dms.contentprovider.mapping.MappingUtils;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import javax.swing.Icon;
import javax.swing.ImageIcon;

public abstract class AbstractContentProvider {
  private static MGLogger log = MGLogger.getLogger(AbstractContentProvider.class);
  
  private ContentProviderRegistryEntry reg = null;
  
  private ContentProviderConfig ccpCfg = null;
  
  public AbstractContentProvider(ContentProviderRegistryEntry paramContentProviderRegistryEntry) {
    this.reg = paramContentProviderRegistryEntry;
  }
  
  public OIObjectManager getObjectManager() {
    return ContentProviderFactory.getInstance().getObjectManager();
  }
  
  public String getId() {
    return this.reg.getId();
  }
  
  public String getName() {
    return this.reg.getName();
  }
  
  public String getToolboxId() {
    return this.reg.getToolboxId();
  }
  
  public String getCredential(String paramString) {
    return this.reg.getCredential(paramString);
  }
  
  public Map<String, String> getCredentials() {
    return this.reg.getCredentials();
  }
  
  public void setCredentials(Map<String, String> paramMap) {
    this.reg.setCredentials(paramMap);
  }
  
  public Collection<ContentProviderCredentialDefinition> getCredentialDefinitions() {
    return Collections.emptyList();
  }
  
  public boolean hasRole(ContentProviderRegistryEntry.ContentProviderRole paramContentProviderRole) {
    return this.reg.hasRole(paramContentProviderRole);
  }
  
  public Map<String, String> getConfigParams() {
    return this.reg.getConfigParams();
  }
  
  public String getConfigurationParameter(String paramString) {
    return getConfigParams().get(paramString);
  }
  
  public String getPropertyName(String paramString) {
    String str = this.reg.getPropertyNames().get(paramString);
    if (str == null)
      str = paramString; 
    return str;
  }
  
  public ContentProviderConfig getConfig(boolean paramBoolean) throws ContentProviderConfigException {
    if (this.ccpCfg == null) {
      this.ccpCfg = new ContentProviderConfig(this);
      try {
        this.ccpCfg.read(ContentProviderFactory.getInstance().getObjectManager());
      } catch (ContentProviderConfigException contentProviderConfigException) {
        if (!paramBoolean)
          throw contentProviderConfigException; 
        log.error(contentProviderConfigException.getMessage());
      } 
    } 
    return this.ccpCfg;
  }
  
  public ContentProviderConfig getConfig() throws ContentProviderConfigException {
    return getConfig(false);
  }
  
  public ImageIcon getLogoImageIcon() {
    return null;
  }
  
  public void viewPartOnline(String paramString) throws ContentProviderException {
    throw new ContentProviderNotSupportedException("viewPartOnline() not supported");
  }
  
  public Map<String, String> getValidValues(String paramString1, String paramString2) throws ContentProviderException {
    throw new ContentProviderNotSupportedException("getValidValues() not supported");
  }
  
  public String toString() {
    return getName();
  }
  
  public void initialize() throws ContentProviderException {}
  
  public abstract boolean isGetNextPartsSupported();
  
  public abstract boolean isClassificationSearchSupported();
  
  public abstract boolean isSearchResultsProductImageSupported();
  
  public abstract boolean isPartDetailsProductImageSupported();
  
  public abstract boolean isDocumentsSupported();
  
  public abstract boolean isChangeAlertsSupported();
  
  public abstract boolean isFailureAlertsSupported();
  
  public abstract boolean isPartStatusChangeAlertSupported();
  
  public abstract boolean isEndOfLifeAlertSupported();
  
  public abstract boolean isSuggestedAlternatesSupported();
  
  public abstract boolean isKeywordSearchSupported();
  
  public abstract boolean isClientSubscriptionManagementRequired();
  
  public abstract boolean isValidValuesSupported();
  
  public abstract String addSubscription(Map<String, String> paramMap) throws ContentProviderException;
  
  public abstract void deleteSubscription(Map<String, String> paramMap) throws ContentProviderException;
  
  public abstract Collection<Map<String, String>> getSubscriptions() throws ContentProviderException;
  
  public abstract AbstractCriteria createCriteria() throws ContentProviderException;
  
  public abstract IContentProviderResults searchParts(ContentProviderConfigPartClass paramContentProviderConfigPartClass, AbstractCriteria paramAbstractCriteria) throws ContentProviderException;
  
  public abstract IContentProviderResultRecord searchExactMatch(String paramString1, String paramString2) throws ContentProviderException;
  
  public abstract IContentProviderResultRecord getPart(Map<String, String> paramMap) throws ContentProviderException;
  
  public abstract IContentProviderResultRecord getPart(IContentProviderResultRecord paramIContentProviderResultRecord) throws ContentProviderException;
  
  public abstract Collection<ContentProviderSubscribedComponent> getSubscribedComponents() throws ContentProviderException;
  
  public abstract Collection<AbstractSearchResultsAction> getSearchResultsActions();
  
  public abstract IContentProviderUpdateSearch createUpdateSearch();
  
  public abstract IContentProviderPartRequest getPartRequest();
  
  public abstract Collection<DefaultDisplayColumn> getDefaultDisplayColumns();
  
  public abstract Icon getIcon();
  
  public abstract String getDatasheetURL(Map<String, String> paramMap) throws ContentProviderException;
  
  public abstract String getPartNumberPropID();
  
  public abstract String getManufacturerPropID();
  
  public abstract String getDescriptionPropID();
  
  public abstract AbstractUserPreferences getUserPreferences();
  
  public boolean hasNextParts() {
    return false;
  }
  
  public IContentProviderResults getNextParts(ContentProviderConfigPartClass paramContentProviderConfigPartClass, AbstractCriteria paramAbstractCriteria) throws ContentProviderException {
    return new EmptyContentProviderResults();
  }
  
  public Collection<String> searchManufacturers(String paramString) throws ContentProviderException {
    throw new ContentProviderNotSupportedException(" searchManufacturers() not supported");
  }
  
  public Collection<ContentProviderManufacturer> getAvailableManufacturers() throws ContentProviderException {
    throw new ContentProviderNotSupportedException(" getAvailableManufacturers() not supported");
  }
  
  public ContentProviderManufacturer getManufacturer(String paramString1, String paramString2) throws ContentProviderException {
    throw new ContentProviderNotSupportedException("getManufacturer() not supported");
  }
  
  public MappingUtils getMappingUtils() {
    return new MappingUtils();
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\AbstractContentProvider.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */