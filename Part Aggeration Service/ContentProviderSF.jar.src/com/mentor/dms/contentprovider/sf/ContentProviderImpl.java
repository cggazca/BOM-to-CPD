package com.mentor.dms.contentprovider.sf;

import com.mentor.datafusion.oi.OIException;
import com.mentor.datafusion.oi.OIHelper;
import com.mentor.datafusion.oi.interceptor.InterceptorFactoryPriorityDecorator;
import com.mentor.datafusion.utils.logger.MGLogger;
import com.mentor.dms.contentprovider.core.AbstractContentProvider;
import com.mentor.dms.contentprovider.core.AbstractCriteria;
import com.mentor.dms.contentprovider.core.ContentProviderCredentialDefinition;
import com.mentor.dms.contentprovider.core.ContentProviderException;
import com.mentor.dms.contentprovider.core.ContentProviderManufacturer;
import com.mentor.dms.contentprovider.core.ContentProviderRegistryEntry;
import com.mentor.dms.contentprovider.core.ContentProviderSubscribedComponent;
import com.mentor.dms.contentprovider.core.DefaultDisplayColumn;
import com.mentor.dms.contentprovider.core.IContentProviderPartRequest;
import com.mentor.dms.contentprovider.core.IContentProviderResultRecord;
import com.mentor.dms.contentprovider.core.IContentProviderResults;
import com.mentor.dms.contentprovider.core.IContentProviderUpdateSearch;
import com.mentor.dms.contentprovider.core.actions.AbstractDetailViewAction;
import com.mentor.dms.contentprovider.core.actions.AbstractSearchResultsAction;
import com.mentor.dms.contentprovider.core.actions.CopyFindchipsURLAction;
import com.mentor.dms.contentprovider.core.actions.CreateManufacturerPartAction;
import com.mentor.dms.contentprovider.core.actions.CreatePartRequestAction;
import com.mentor.dms.contentprovider.core.actions.ExportCSVAction;
import com.mentor.dms.contentprovider.core.actions.ViewCompareAction;
import com.mentor.dms.contentprovider.core.client.userpref.AbstractUserPreferences;
import com.mentor.dms.contentprovider.core.config.ContentProviderConfigPartClass;
import com.mentor.dms.contentprovider.core.config.SearchCapability;
import com.mentor.dms.contentprovider.core.utils.validate.CPPartClass;
import com.mentor.dms.contentprovider.core.utils.validate.CPProperty;
import com.mentor.dms.contentprovider.sf.client.SEClient;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

public class ContentProviderImpl extends AbstractContentProvider {
  private static MGLogger log = MGLogger.getLogger(ContentProviderImpl.class);
  
  private static AggregationServiceUserPreferences userPrefs;
  
  static final String SEARCH_SERVICE_USERNAME = "SEARCH_SERVICE_USERNAME";
  
  static final String SEARCH_SERVICE_PASSWORD = "SEARCH_SERVICE_PASSWORD";
  
  private SupplyFrameConnection connection = null;
  
  private static final String ROOT_CLASS_DOMAIN = "76f2225d";
  
  private static final List<String> FIELD_WIDEN_LIST = new ArrayList<>(Arrays.asList(new String[] { "bf4dd752" }));
  
  public ContentProviderImpl(ContentProviderRegistryEntry paramContentProviderRegistryEntry) {
    super(paramContentProviderRegistryEntry);
  }
  
  protected SupplyFrameConnection getAuthorizeConnection() {
    if (this.connection == null)
      this.connection = new SupplyFrameConnection(this); 
    return this.connection;
  }
  
  public void initialize() throws ContentProviderException {
    super.initialize();
    try {
      if (Activator.getDefault() == null) {
        OIHelper.registerOI(getObjectManager().getObjectManagerFactory());
        loadComponentHeightInterceptors();
      } else {
        LoadComponentHeightInterceptorsJob loadComponentHeightInterceptorsJob = new LoadComponentHeightInterceptorsJob();
        loadComponentHeightInterceptorsJob.schedule(5000L);
      } 
    } catch (OIException oIException) {
      throw new ContentProviderException("Unable to register OI API", oIException);
    } 
  }
  
  public Collection<ContentProviderCredentialDefinition> getCredentialDefinitions() {
    ArrayList<ContentProviderCredentialDefinition> arrayList = new ArrayList();
    arrayList.add(new ContentProviderCredentialDefinition("SEARCH_SERVICE_USERNAME", "Client ID", true));
    arrayList.add(new ContentProviderCredentialDefinition("SEARCH_SERVICE_PASSWORD", "Client secret", true, true));
    return arrayList;
  }
  
  public boolean isGetNextPartsSupported() {
    return true;
  }
  
  public boolean isClassificationSearchSupported() {
    return true;
  }
  
  public boolean isDocumentsSupported() {
    return false;
  }
  
  public boolean isChangeAlertsSupported() {
    return false;
  }
  
  public boolean isEndOfLifeAlertSupported() {
    return false;
  }
  
  public boolean isFailureAlertsSupported() {
    return false;
  }
  
  public boolean isPartStatusChangeAlertSupported() {
    return false;
  }
  
  public boolean isSuggestedAlternatesSupported() {
    return true;
  }
  
  public boolean isKeywordSearchSupported() {
    return true;
  }
  
  public boolean isClientSubscriptionManagementRequired() {
    return false;
  }
  
  public String addSubscription(Map<String, String> paramMap) throws ContentProviderException {
    AggregationServiceWebCall aggregationServiceWebCall = new AggregationServiceWebCall(this);
    return aggregationServiceWebCall.addSubscription(paramMap);
  }
  
  public void deleteSubscription(Map<String, String> paramMap) throws ContentProviderException {
    AggregationServiceWebCall aggregationServiceWebCall = new AggregationServiceWebCall(this);
    aggregationServiceWebCall.deleteSubscription(paramMap);
  }
  
  public Collection<Map<String, String>> getSubscriptions() throws ContentProviderException {
    AggregationServiceWebCall aggregationServiceWebCall = new AggregationServiceWebCall(this);
    return aggregationServiceWebCall.getSubscriptions();
  }
  
  public AbstractCriteria createCriteria() throws ContentProviderException {
    return new PartSearchCriteria(getConfigParams());
  }
  
  public Map<String, String> searchDefinitionsEnricher() throws ContentProviderException {
    AggregationServiceWebCall aggregationServiceWebCall = new AggregationServiceWebCall(this);
    return aggregationServiceWebCall.searchDefinitionsEnricher();
  }
  
  public IContentProviderResults searchParts(ContentProviderConfigPartClass paramContentProviderConfigPartClass, AbstractCriteria paramAbstractCriteria) throws ContentProviderException {
    AggregationServiceWebCall aggregationServiceWebCall = new AggregationServiceWebCall(this);
    return aggregationServiceWebCall.searchParts(paramContentProviderConfigPartClass, paramAbstractCriteria);
  }
  
  public boolean hasNextParts() {
    return AggregationServiceWebCall.hasNextParts();
  }
  
  public IContentProviderResults getNextParts(ContentProviderConfigPartClass paramContentProviderConfigPartClass, String paramString, AbstractCriteria paramAbstractCriteria) throws ContentProviderException {
    AggregationServiceWebCall aggregationServiceWebCall = new AggregationServiceWebCall(this);
    return aggregationServiceWebCall.getNextParts(paramContentProviderConfigPartClass, paramString, paramAbstractCriteria);
  }
  
  public IContentProviderResultRecord getPart(Map<String, String> paramMap) throws ContentProviderException {
    AggregationServiceWebCall aggregationServiceWebCall = new AggregationServiceWebCall(this);
    return aggregationServiceWebCall.getPart(paramMap);
  }
  
  public IContentProviderResultRecord getPart(IContentProviderResultRecord paramIContentProviderResultRecord) throws ContentProviderException {
    AggregationServiceWebCall aggregationServiceWebCall = new AggregationServiceWebCall(this);
    return aggregationServiceWebCall.getPart(paramIContentProviderResultRecord);
  }
  
  public IContentProviderResultRecord searchExactMatch(String paramString1, String paramString2) throws ContentProviderException {
    AggregationServiceWebCall aggregationServiceWebCall = new AggregationServiceWebCall(this);
    return aggregationServiceWebCall.searchExactMatch(paramString1, paramString2);
  }
  
  public IContentProviderResults searchExactMatchSF(String paramString1, String paramString2) throws ContentProviderException {
    AggregationServiceWebCall aggregationServiceWebCall = new AggregationServiceWebCall(this);
    return aggregationServiceWebCall.searchExactMatchSF(paramString1, paramString2);
  }
  
  public IContentProviderResults searchExactMatchSF(String paramString) throws ContentProviderException {
    AggregationServiceWebCall aggregationServiceWebCall = new AggregationServiceWebCall(this);
    return aggregationServiceWebCall.searchExactMatchSF(paramString);
  }
  
  public Collection<AbstractSearchResultsAction> getSearchResultsActions() {
    ArrayList<ViewCompareAction> arrayList = new ArrayList();
    arrayList.add(new ViewCompareAction());
    arrayList.add(new CreatePartRequestAction());
    arrayList.add(new CreateManufacturerPartAction());
    arrayList.add(new CopyFindchipsURLAction());
    return (Collection)arrayList;
  }
  
  public Collection<AbstractDetailViewAction> getDetailViewActions() {
    ArrayList<ExportCSVAction> arrayList = new ArrayList();
    arrayList.add(new ExportCSVAction());
    return (Collection)arrayList;
  }
  
  public IContentProviderUpdateSearch createUpdateSearch() {
    return new ContentProviderUpdateSearchImpl();
  }
  
  public IContentProviderPartRequest getPartRequest() {
    return (IContentProviderPartRequest)new CreatePartRequestAction();
  }
  
  public Collection<DefaultDisplayColumn> getDefaultDisplayColumns() {
    ArrayList<DefaultDisplayColumn> arrayList = new ArrayList();
    arrayList.add(new DefaultDisplayColumn("Manufacturer", "6230417e"));
    arrayList.add(new DefaultDisplayColumn("Manufacturer Part Number", "d8ac8dcc"));
    arrayList.add(new DefaultDisplayColumn("Current Datasheet Url", "750a45c8", false, false));
    arrayList.add(new DefaultDisplayColumn("Part Intelligence Url", "2a2b1476", false, false));
    arrayList.add(new DefaultDisplayColumn("uid", "e1aa6f26", false, false));
    return arrayList;
  }
  
  public Icon getIcon() {
    return new ImageIcon(ContentProviderImpl.class.getResource("images/supplyframe_small.png"));
  }
  
  public ImageIcon getLogoImageIcon() {
    return new ImageIcon(ContentProviderImpl.class.getResource("images/SF_logo.png"));
  }
  
  public boolean isSearchResultsProductImageSupported() {
    return false;
  }
  
  public boolean isPartDetailsProductImageSupported() {
    return true;
  }
  
  public String getDatasheetURL(Map<String, String> paramMap) throws ContentProviderException {
    AggregationServiceWebCall aggregationServiceWebCall = new AggregationServiceWebCall(this);
    IContentProviderResultRecord iContentProviderResultRecord = aggregationServiceWebCall.getPart(paramMap);
    return iContentProviderResultRecord.getDatasheetURL();
  }
  
  public String getPartNumberPropID() {
    return "d8ac8dcc";
  }
  
  public String getManufacturerPropID() {
    return "6230417e";
  }
  
  public String getDescriptionPropID() {
    return "bf4dd752";
  }
  
  public void viewPartOnline(String paramString) throws ContentProviderException {
    SEClient.openSEPartInBrowser(this, paramString);
  }
  
  public Collection<String> searchManufacturers(String paramString) throws ContentProviderException {
    AggregationServiceWebCall aggregationServiceWebCall = new AggregationServiceWebCall(this);
    return aggregationServiceWebCall.searchManufacturers(paramString);
  }
  
  public Collection<ContentProviderSubscribedComponent> getSubscribedComponents() throws ContentProviderException {
    AggregationServiceWebCall aggregationServiceWebCall = new AggregationServiceWebCall(this);
    return aggregationServiceWebCall.getSubscribedComponents();
  }
  
  public AbstractUserPreferences getUserPreferences() {
    if (userPrefs == null)
      userPrefs = new AggregationServiceUserPreferences(this); 
    return userPrefs;
  }
  
  public boolean isValidValuesSupported() {
    return true;
  }
  
  public List<Map.Entry<String, String>> getValidValues(String paramString1, String paramString2, AbstractCriteria paramAbstractCriteria) throws ContentProviderException {
    return SFValidValues.getPropValues(this, paramString1, paramString2, paramAbstractCriteria);
  }
  
  private void loadComponentHeightInterceptors() {
    InterceptorFactoryPriorityDecorator interceptorFactoryPriorityDecorator = new InterceptorFactoryPriorityDecorator(new ComponentHeightCompInterceptorFactory(), 500);
    interceptorFactoryPriorityDecorator.setIncludedClasses(Arrays.asList(new String[] { "001" }));
    OIHelper.addInterceptorFactoryToRegistry(getObjectManager().getObjectManagerFactory(), new ComponentHeightCompInterceptorFactory(), 500, Collections.emptySet(), Collections.emptySet(), false);
    interceptorFactoryPriorityDecorator = new InterceptorFactoryPriorityDecorator(new ComponentHeightMPNInterceptorFactory(), 500);
    interceptorFactoryPriorityDecorator.setIncludedClasses(Arrays.asList(new String[] { "060" }));
    OIHelper.addInterceptorFactoryToRegistry(getObjectManager().getObjectManagerFactory(), new ComponentHeightMPNInterceptorFactory(), 500, Collections.emptySet(), Collections.emptySet(), false);
  }
  
  public Collection<ContentProviderManufacturer> getAvailableManufacturers() throws ContentProviderException {
    AggregationServiceWebCall aggregationServiceWebCall = new AggregationServiceWebCall(this);
    return aggregationServiceWebCall.getAvailableManufacturers();
  }
  
  public ContentProviderManufacturer getManufacturer(String paramString1, String paramString2) throws ContentProviderException {
    AggregationServiceWebCall aggregationServiceWebCall = new AggregationServiceWebCall(this);
    return aggregationServiceWebCall.getManufacturer(paramString2);
  }
  
  public HashMap<String, SearchCapability> readSeachCapabilities() throws Exception {
    AggregationServiceWebCall aggregationServiceWebCall = new AggregationServiceWebCall(this);
    return aggregationServiceWebCall.getSeachCapabilities();
  }
  
  public ArrayList<CPPartClass> getPartClassInfo() throws Exception {
    AggregationServiceWebCall aggregationServiceWebCall = new AggregationServiceWebCall(this);
    return aggregationServiceWebCall.getPartClassInfo();
  }
  
  public List<CPProperty> getPartProperties(String paramString) throws Exception {
    AggregationServiceWebCall aggregationServiceWebCall = new AggregationServiceWebCall(this);
    return aggregationServiceWebCall.getPartProperties(paramString);
  }
  
  public boolean isWideField(String paramString) {
    return FIELD_WIDEN_LIST.contains(paramString);
  }
  
  public String getRootClassDomain() {
    return "76f2225d";
  }
  
  class LoadComponentHeightInterceptorsJob extends Job {
    public LoadComponentHeightInterceptorsJob() {
      super("Loading SiliconExpert Component Height Interceptors...");
      setSystem(true);
      setPriority(40);
    }
    
    public IStatus run(IProgressMonitor param1IProgressMonitor) {
      ContentProviderImpl.this.loadComponentHeightInterceptors();
      return Status.OK_STATUS;
    }
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\ContentProviderImpl.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */