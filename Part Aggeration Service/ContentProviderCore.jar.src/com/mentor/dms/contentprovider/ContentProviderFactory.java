package com.mentor.dms.contentprovider;

import com.mentor.datafusion.oi.OICursor;
import com.mentor.datafusion.oi.OIException;
import com.mentor.datafusion.oi.OIHelper;
import com.mentor.datafusion.oi.OIObjectManager;
import com.mentor.datafusion.oi.OIQuery;
import com.mentor.datafusion.oi.interceptor.InterceptorFactory;
import com.mentor.datafusion.oi.interceptor.InterceptorFactoryPriorityDecorator;
import com.mentor.datafusion.utils.logger.MGLogger;
import com.mentor.dms.contentprovider.client.userpref.AbstractUserPreferences;
import com.mentor.dms.contentprovider.core.plugin.Activator;
import com.mentor.dms.contentprovider.plugin.ExternalContentInterceptorFactory;
import com.mentor.dms.contentprovider.plugin.MPNInterceptorFactory;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class ContentProviderFactory {
  private static MGLogger log = MGLogger.getLogger(ContentProviderFactory.class);
  
  private static ContentProviderFactory instance = null;
  
  private static boolean bInterceptorsRegistered = false;
  
  private ContentProviderAppConfig appConfig = null;
  
  private static OIObjectManager om = null;
  
  private HashMap<String, ContentProviderRegistryEntry> registeredContentProviders = new HashMap<>();
  
  private LinkedHashMap<String, AbstractContentProvider> contentProvidersInstances = new LinkedHashMap<>();
  
  public static synchronized ContentProviderFactory getInstance() {
    if (instance == null)
      instance = new ContentProviderFactory(); 
    return instance;
  }
  
  public ContentProviderAppConfig getAppConfig() {
    if (this.appConfig == null)
      this.appConfig = new ContentProviderAppConfig(); 
    return this.appConfig;
  }
  
  public void registerContentProviders(OIObjectManager paramOIObjectManager) throws ContentProviderException {
    om = paramOIObjectManager;
    ArrayList<ContentProviderRegistryEntry> arrayList = new ArrayList();
    try {
      getAppConfig().load(paramOIObjectManager);
      OIQuery oIQuery = paramOIObjectManager.createQuery("ToolsContentProviderCfgs", true);
      oIQuery.addRestriction("Status", "A");
      oIQuery.addColumn("ToolBoxId");
      OICursor oICursor = oIQuery.execute();
      while (oICursor.next()) {
        ContentProviderRegistryEntry contentProviderRegistryEntry = new ContentProviderRegistryEntry();
        contentProviderRegistryEntry.load(oICursor.getProxyObject().getObject());
        (getInstance()).registeredContentProviders.put(contentProviderRegistryEntry.getId(), contentProviderRegistryEntry);
        arrayList.add(contentProviderRegistryEntry);
      } 
      Collections.sort(arrayList, new Comparator<ContentProviderRegistryEntry>() {
            public int compare(ContentProviderRegistryEntry param1ContentProviderRegistryEntry1, ContentProviderRegistryEntry param1ContentProviderRegistryEntry2) {
              int i = param1ContentProviderRegistryEntry1.getSearchTabOrder();
              int j = param1ContentProviderRegistryEntry2.getSearchTabOrder();
              return (i < j) ? -1 : ((i == j) ? 0 : 1);
            }
          });
      for (ContentProviderRegistryEntry contentProviderRegistryEntry : arrayList)
        createContentProvider(contentProviderRegistryEntry.getId()); 
    } catch (Exception exception) {
      throw new ContentProviderException(exception.getMessage());
    } 
  }
  
  protected OIObjectManager getObjectManager() {
    return om;
  }
  
  public AbstractContentProvider createContentProvider(String paramString) throws ContentProviderException {
    AbstractContentProvider abstractContentProvider = this.contentProvidersInstances.get(paramString);
    if (abstractContentProvider == null)
      try {
        ContentProviderRegistryEntry contentProviderRegistryEntry = this.registeredContentProviders.get(paramString);
        if (contentProviderRegistryEntry == null)
          throw new ContentProviderException("Content Provider Configuration with PROVIDER_ID = '" + paramString + "' not found."); 
        log.debug("Loading Content Provider class: " + contentProviderRegistryEntry.getClassName());
        Class<?> clazz = Class.forName(contentProviderRegistryEntry.getClassName());
        Constructor<?> constructor = clazz.getDeclaredConstructor(new Class[] { ContentProviderRegistryEntry.class });
        abstractContentProvider = (AbstractContentProvider)constructor.newInstance(new Object[] { contentProviderRegistryEntry });
        this.contentProvidersInstances.put(paramString, abstractContentProvider);
        abstractContentProvider.initialize();
        AbstractUserPreferences abstractUserPreferences = abstractContentProvider.getUserPreferences();
        if (abstractUserPreferences != null)
          abstractUserPreferences.read(); 
      } catch (Exception exception) {
        log.debug("Unable to load Content Provider Configuration with PROVIDER_ID = '" + paramString + "': " + exception.getMessage());
      }  
    return abstractContentProvider;
  }
  
  public Collection<AbstractContentProvider> getRegisteredContentProviders() throws ContentProviderException {
    return this.contentProvidersInstances.values();
  }
  
  public Collection<String> getRegisteredContentProviderIds() {
    return this.contentProvidersInstances.keySet();
  }
  
  public static void registerInterceptors() throws ContentProviderException {
    if (bInterceptorsRegistered)
      return; 
    try {
      if (Activator.getDefault() == null)
        OIHelper.registerOI(om.getObjectManagerFactory()); 
      InterceptorFactoryPriorityDecorator interceptorFactoryPriorityDecorator = new InterceptorFactoryPriorityDecorator((InterceptorFactory)new MPNInterceptorFactory(), 500);
      interceptorFactoryPriorityDecorator.setIncludedClasses(Arrays.asList(new String[] { "060" }));
      OIHelper.addInterceptorFactoryToRegistry(om.getObjectManagerFactory(), interceptorFactoryPriorityDecorator);
      interceptorFactoryPriorityDecorator = new InterceptorFactoryPriorityDecorator((InterceptorFactory)new ExternalContentInterceptorFactory(), 500);
      interceptorFactoryPriorityDecorator.setIncludedClasses(Arrays.asList(new String[] { "950" }));
      OIHelper.addInterceptorFactoryToRegistry(om.getObjectManagerFactory(), interceptorFactoryPriorityDecorator);
      bInterceptorsRegistered = true;
    } catch (OIException oIException) {
      throw new ContentProviderException(oIException);
    } 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\ContentProviderFactory.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */