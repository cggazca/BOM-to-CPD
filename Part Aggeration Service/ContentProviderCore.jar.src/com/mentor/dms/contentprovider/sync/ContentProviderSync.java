package com.mentor.dms.contentprovider.sync;

import com.mentor.datafusion.oi.OICursor;
import com.mentor.datafusion.oi.OIException;
import com.mentor.datafusion.oi.OIHelper;
import com.mentor.datafusion.oi.OIObjectManager;
import com.mentor.datafusion.oi.OIQuery;
import com.mentor.datafusion.oi.model.OIClass;
import com.mentor.datafusion.oi.model.OIField;
import com.mentor.datafusion.oi.model.OIStringField;
import com.mentor.datafusion.oi.type.OIObject;
import com.mentor.datafusion.oi.type.OIObjectSet;
import com.mentor.datafusion.oi.util.LockUtilities;
import com.mentor.datafusion.oi.util.StateUtilities;
import com.mentor.datafusion.utils.logger.MGLogger;
import com.mentor.dms.contentprovider.AbstractContentProvider;
import com.mentor.dms.contentprovider.ComponentProperty;
import com.mentor.dms.contentprovider.ContentProviderChangeAlert;
import com.mentor.dms.contentprovider.ContentProviderDocument;
import com.mentor.dms.contentprovider.ContentProviderDocumentList;
import com.mentor.dms.contentprovider.ContentProviderEndOfLifeAlert;
import com.mentor.dms.contentprovider.ContentProviderException;
import com.mentor.dms.contentprovider.ContentProviderFactory;
import com.mentor.dms.contentprovider.ContentProviderFailureAlert;
import com.mentor.dms.contentprovider.ContentProviderGlobal;
import com.mentor.dms.contentprovider.ContentProviderManufacturer;
import com.mentor.dms.contentprovider.ContentProviderManufacturerProperty;
import com.mentor.dms.contentprovider.ContentProviderPartNotFoundException;
import com.mentor.dms.contentprovider.ContentProviderPartStatusChange;
import com.mentor.dms.contentprovider.ContentProviderRegistryEntry;
import com.mentor.dms.contentprovider.IContentProviderApplication;
import com.mentor.dms.contentprovider.IContentProviderPartNumberGenerator;
import com.mentor.dms.contentprovider.IContentProviderResultRecord;
import com.mentor.dms.contentprovider.IContentProviderUpdateSearch;
import com.mentor.dms.contentprovider.IContentProviderUpdateSearchResults;
import com.mentor.dms.contentprovider.client.LogWindow;
import com.mentor.dms.contentprovider.client.catalogselector.CatalogSelectionDialog;
import com.mentor.dms.contentprovider.config.AbstractContentProviderConfigManufacturerPartPropertyMap;
import com.mentor.dms.contentprovider.config.ContentProviderConfig;
import com.mentor.dms.contentprovider.config.ContentProviderConfigComponentCatalog;
import com.mentor.dms.contentprovider.config.ContentProviderConfigContentProviderMap;
import com.mentor.dms.contentprovider.config.ContentProviderConfigException;
import com.mentor.dms.contentprovider.config.ContentProviderConfigMPNCatalog;
import com.mentor.dms.contentprovider.config.ContentProviderConfigManufacturerPartMap;
import com.mentor.dms.contentprovider.config.ContentProviderConfigManufacturerPartPropertyMap;
import com.mentor.dms.contentprovider.config.ContentProviderConfigPartClass;
import com.mentor.dms.contentprovider.config.ContentProviderConfigProperty;
import com.mentor.dms.contentprovider.config.ContentProviderConfigPropertyMap;
import com.mentor.dms.contentprovider.config.ContentProviderConfigScriptEngine;
import com.mentor.dms.contentprovider.config.ContentProviderConfigScriptedManufacturerPartPropertyMap;
import com.mentor.dms.contentprovider.config.xml.ConfigXMLManufacturerPropertyMap;
import com.mentor.dms.contentprovider.sync.gui.ContentProviderReconcileApp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import org.apache.log4j.Level;
import org.apache.log4j.Priority;

public class ContentProviderSync {
  static MGLogger logger = MGLogger.getLogger(ContentProviderSync.class);
  
  public static final String ID_PROPERTY_SUBSCRIPTION__ID = "SubscriptionID";
  
  protected static Map<String, String> customSyncAppParams = new HashMap<>();
  
  private static List<String> reconciledMPNList = new ArrayList<>();
  
  private static boolean bCommitMode = true;
  
  private static boolean bErrorCountEnabled = true;
  
  private static String cacheId = "";
  
  public static void setCommitMode(boolean paramBoolean) {
    bCommitMode = paramBoolean;
  }
  
  public static void setEnableErrorCount(boolean paramBoolean) {
    bErrorCountEnabled = paramBoolean;
  }
  
  public static void syncAllExternalContentProviders(OIObjectManager paramOIObjectManager, boolean paramBoolean1, boolean paramBoolean2, String paramString) throws ContentProviderException, ContentProviderSyncException {
    if (paramBoolean1) {
      logger.info("Performing full synchronization with all Content Providers...");
    } else {
      logger.info("Performing incremental synchronization with all Content Providers...");
    } 
    for (AbstractContentProvider abstractContentProvider : ContentProviderFactory.getInstance().getRegisteredContentProviders()) {
      if (abstractContentProvider.hasRole(ContentProviderRegistryEntry.ContentProviderRole.MANUFACTURER_PART_SYNCHRONIZATION))
        syncExternalContentProvider(paramOIObjectManager, abstractContentProvider, paramBoolean1, paramBoolean2, paramString); 
    } 
  }
  
  public static void syncExternalContentProvider(OIObjectManager paramOIObjectManager, AbstractContentProvider paramAbstractContentProvider, boolean paramBoolean1, boolean paramBoolean2, String paramString) throws ContentProviderSyncException {
    IContentProviderUpdateSearchResults iContentProviderUpdateSearchResults;
    setEnableErrorCount(true);
    byte b = 0;
    HashSet<String> hashSet = new HashSet();
    HashMap<Object, Object> hashMap1 = new HashMap<>();
    ContentProviderConfig contentProviderConfig = null;
    Date date = new Date();
    IContentProviderUpdateSearch iContentProviderUpdateSearch = paramAbstractContentProvider.createUpdateSearch();
    iContentProviderUpdateSearch.setContentProvider(paramAbstractContentProvider);
    iContentProviderUpdateSearch.setCustomSyncAppParams(customSyncAppParams);
    if (paramBoolean2)
      iContentProviderUpdateSearch.setRestartMode(); 
    CacheInfo cacheInfo = null;
    if (paramBoolean1) {
      logger.info("Performing full synchronization with Content Provider '" + paramAbstractContentProvider.getName() + "'...");
      iContentProviderUpdateSearch.setFullMode();
      if (paramBoolean2) {
        try {
          cacheInfo = CacheInfo.readCacheInfo(paramString);
        } catch (ContentProviderSyncException contentProviderSyncException) {
          throw new ContentProviderSyncException("Unable to restart synchronization:  " + contentProviderSyncException.getMessage());
        } 
      } else {
        cacheInfo = CacheInfo.createCacheInfo(new Date(0L), new Date(), paramString);
      } 
    } else {
      logger.info("Performing incremental synchronization with Content Provider '" + paramAbstractContentProvider.getName() + "'...");
      iContentProviderUpdateSearch.setIncrementalMode();
      if (paramBoolean2) {
        try {
          cacheInfo = CacheInfo.readCacheInfo(paramString);
        } catch (ContentProviderSyncException contentProviderSyncException) {
          throw new ContentProviderSyncException("Unable to restart synchronization:  " + contentProviderSyncException.getMessage());
        } 
      } else {
        cacheInfo = CacheInfo.createCacheInfo(getLastSyncDate(paramOIObjectManager, paramAbstractContentProvider), new Date(), paramString);
      } 
    } 
    iContentProviderUpdateSearch.setCacheInfo(cacheInfo);
    logger.info("Processing parts updated from '" + cacheInfo.getFromDateString() + "' to '" + cacheInfo.getToDateString() + "'.");
    logger.info("Reading mapping configuration for Content Provider '" + paramAbstractContentProvider.getName() + "'...");
    try {
      contentProviderConfig = paramAbstractContentProvider.getConfig();
    } catch (ContentProviderConfigException contentProviderConfigException) {
      throw new ContentProviderSyncException(contentProviderConfigException.getMessage());
    } 
    logger.info("Querying EDM Library for Manufacturer Parts which reference non-existent External Content...");
    try {
      HashSet<String> hashSet1 = new HashSet();
      OIQuery oIQuery = paramOIObjectManager.createQuery("ExternalContent", true);
      oIQuery.addColumn("ExternalContentId");
      OICursor oICursor = oIQuery.execute();
      while (oICursor.next())
        hashSet1.add(oICursor.getString("ExternalContentId")); 
      oICursor.close();
      oIQuery = paramOIObjectManager.createQuery("ManufacturerPart", true);
      oIQuery.addRestriction("ExternalContentId", "~NULL");
      oIQuery.addColumn("ManufacturerpartId");
      oIQuery.addColumn("ExternalContentId");
      oICursor = oIQuery.execute();
      while (oICursor.next()) {
        if (!hashSet1.contains(oICursor.getStringified("ExternalContentId")))
          logger.warn("Warning:  Manufacturer Part '" + oICursor.getString("ManufacturerpartId") + "' references non-existent External Content '" + oICursor.getStringified("ExternalContentId") + "'."); 
      } 
      oICursor.close();
    } catch (Exception exception) {
      logger.warn("Warning:  Error while querying EDM Library for Manufacturer Parts which reference non-existent External Content :  " + exception.getMessage());
    } 
    boolean bool = true;
    LinkedHashMap<Object, Object> linkedHashMap = new LinkedHashMap<>();
    try {
      logger.info("Querying EDM Library for External Content associated with this Content Provider...");
      OIQuery oIQuery = paramOIObjectManager.createQuery("ExternalContent", true);
      oIQuery.addRestriction("ECProviderReferences.ECProviderReferenceID", paramAbstractContentProvider.getId());
      oIQuery.addColumn("ExternalContentId");
      oIQuery.addColumn("ECProviderReferences.ECProviderReferenceKey");
      oIQuery.addColumn("ECProviderReferences.ECProviderReferenceValue");
      oIQuery.addRestriction("ECProviderSyncList.ECProviderSyncID", "NULL|" + paramAbstractContentProvider.getId());
      oIQuery.addColumn("ECProviderSyncList.ECLastSyncDate");
      try {
        oIQuery.addColumn("ECProviderSyncList.ECCacheId");
      } catch (OIException oIException) {
        bool = false;
        logger.warn("Warning:  EDM Library External Content data model should be upgraded to include cache ID in order increase performance when using '-restart' option.");
      } 
      oIQuery.addColumn("obj_user");
      oIQuery.addSortBy("ExternalContentId", true);
      OICursor oICursor = oIQuery.execute();
      logger.info("Reading EDM Library External Content...");
      byte b1 = 0;
      while (oICursor.next()) {
        String str1 = oICursor.getString("ExternalContentId");
        ECData eCData = (ECData)linkedHashMap.get(str1);
        if (eCData == null) {
          eCData = new ECData();
          linkedHashMap.put(str1, eCData);
          eCData.ecId = str1;
          eCData.lastSyncDate = oICursor.getDate("ECLastSyncDate");
          if (bool)
            eCData.cacheId = oICursor.getString("ECCacheId"); 
          String str = LockUtilities.getSessionLockHolder((OIObject)oICursor);
          if (str != null) {
            logger.warn("External Content object '" + str1 + "' is locked by '" + str + "'.");
            b1++;
          } 
        } 
        String str2 = oICursor.getString("ECProviderReferenceValue");
        if (str2 != null && !str2.trim().isEmpty()) {
          eCData.providerRefMap.put(oICursor.getString("ECProviderReferenceKey"), str2);
          continue;
        } 
        logger.warn("External Content object '" + str1 + "' contains a NULL value for provider key '" + str2 + "'.");
      } 
      oICursor.close();
      if (b1 > 0)
        logger.info("" + b1 + " External Content object(s) were locked.  Any attempted updates to these objects will fail."); 
      for (ECData eCData : linkedHashMap.values()) {
        String str = "";
        for (ContentProviderConfigProperty contentProviderConfigProperty : contentProviderConfig.getIdProperties()) {
          if (!str.isEmpty())
            str = str + ":"; 
          str = str + str;
        } 
        ArrayList<ECData> arrayList1 = (ArrayList)hashMap1.get(str);
        if (arrayList1 == null) {
          arrayList1 = new ArrayList();
          hashMap1.put(str, arrayList1);
        } 
        arrayList1.add(eCData);
      } 
    } catch (Exception exception) {
      throw new ContentProviderSyncException("Error while intializing synchronization of EDM Library External Content with Content Provider:  " + exception.getMessage());
    } 
    ArrayList<HashMap<String, String>> arrayList = new ArrayList();
    for (Map.Entry<Object, Object> entry : hashMap1.entrySet()) {
      ECData eCData = ((ArrayList<ECData>)entry.getValue()).get(0);
      arrayList.add(eCData.providerRefMap);
    } 
    iContentProviderUpdateSearch.setDMSExternalContentList(arrayList);
    logger.info("Querying Content Provider '" + paramAbstractContentProvider.getName() + "' for updated parts...");
    try {
      iContentProviderUpdateSearchResults = iContentProviderUpdateSearch.execute();
    } catch (ContentProviderException contentProviderException) {
      throw new ContentProviderSyncException("Error while querying Content Provider for updated parts:  " + contentProviderException.getMessage());
    } 
    cacheId = cacheInfo.getId();
    if (iContentProviderUpdateSearchResults.getCount() > 0) {
      boolean bool1 = true;
      while (bool1) {
        try {
          bool1 = iContentProviderUpdateSearchResults.next();
        } catch (ContentProviderException contentProviderException) {
          logger.error("Unable to get next updated part from Content Provider:  " + contentProviderException.getMessage());
          if (bErrorCountEnabled)
            b++; 
        } 
        if (!bool1)
          continue; 
        IContentProviderResultRecord iContentProviderResultRecord = null;
        try {
          iContentProviderResultRecord = iContentProviderUpdateSearchResults.getPartRecord();
        } catch (ContentProviderException contentProviderException) {
          logger.error("Unable to get part record from Content Provider: " + contentProviderException.getMessage());
          if (bErrorCountEnabled)
            b++; 
          continue;
        } 
        String str = "";
        for (ContentProviderConfigProperty contentProviderConfigProperty : contentProviderConfig.getIdProperties()) {
          if (!str.isEmpty())
            str = str + ":"; 
          str = str + str;
        } 
        if (hashMap1.containsKey(str)) {
          for (ECData eCData : hashMap1.get(str)) {
            if (eCData.cacheId.equals(cacheId)) {
              logger.info("External Content object '" + eCData.ecId + "' has already been loaded from this cache.  Skipping.");
              continue;
            } 
            OIObject oIObject1 = null;
            try {
              oIObject1 = paramOIObjectManager.getObjectByID(eCData.ecId, "ExternalContent", true);
            } catch (OIException oIException) {
              logger.error("Unable to get EDM Library External Content object '" + eCData.ecId + "' : " + oIException.getMessage());
              if (bErrorCountEnabled)
                b++; 
              continue;
            } 
            OIObject oIObject2 = null;
            try {
              oIObject2 = getSyncRegistryEntry(oIObject1, paramAbstractContentProvider.getId());
            } catch (ContentProviderSyncException contentProviderSyncException) {
              logger.error(contentProviderSyncException.getMessage());
              if (bErrorCountEnabled)
                b++; 
              continue;
            } finally {
              try {
                paramOIObjectManager.evict(oIObject2);
              } catch (OIException oIException) {}
            } 
            hashSet.add(eCData.ecId);
            try {
              syncExernalContentPartRecordToDMS(null, oIObject1, oIObject2, paramAbstractContentProvider, iContentProviderResultRecord, date);
            } catch (ContentProviderSyncException contentProviderSyncException) {
              if (bErrorCountEnabled)
                b++; 
              logger.error("Unable to save Content Provider data to EDM Library External Content object: " + contentProviderSyncException.getMessage());
            } 
          } 
          continue;
        } 
        logger.info("The following part has been updated in the Content Provider, but is not referenced by a EDM Library External Content object:");
        for (ContentProviderConfigProperty contentProviderConfigProperty : contentProviderConfig.getIdProperties())
          logger.info("  " + contentProviderConfigProperty.getContentProviderId() + " = '" + iContentProviderResultRecord.getPartProperty(contentProviderConfigProperty.getContentProviderId()).getValue() + "'"); 
      } 
    } else {
      logger.info("No updated parts were returned from Content Provider.");
    } 
    logger.info("Synchronizing External Content that has never been synchronized with the Content Provider...");
    for (ECData eCData : linkedHashMap.values()) {
      if (eCData.lastSyncDate == null && !hashSet.contains(eCData.ecId)) {
        OIObject oIObject;
        hashSet.add(eCData.ecId);
        try {
          oIObject = paramOIObjectManager.getObjectByID(eCData.ecId, "ExternalContent", true);
        } catch (OIException oIException) {
          continue;
        } 
        try {
          syncExernalContentPartRecordToDMS(null, oIObject, paramAbstractContentProvider, date);
        } catch (ContentProviderSyncException contentProviderSyncException) {
          logger.warn("Unable to save Content Provider data to EDM Library External Content object: " + contentProviderSyncException.getMessage());
        } 
      } 
    } 
    logger.info("Synchronizing External Content that has been affected by configuration change...");
    HashMap<Object, Object> hashMap2 = new HashMap<>();
    try {
      OIQuery oIQuery = paramOIObjectManager.createQuery("CatalogGroup", true);
      oIQuery.addRestriction("ObjectClass", "60");
      oIQuery.addColumn("CatalogGroup");
      oIQuery.addColumn("DomainModelName");
      OICursor oICursor = oIQuery.execute();
      while (oICursor.next())
        hashMap2.put(oICursor.getString("CatalogGroup"), oICursor.getString("DomainModelName")); 
      oICursor.close();
      oIQuery = paramOIObjectManager.createQuery("ManufacturerPart", true);
      oIQuery.addRestriction("ExternalContentId", "~NULL");
      oIQuery.addColumn("ManufacturerpartId");
      oIQuery.addColumn("000obj_cod");
      oIQuery.addColumn("ExternalContentId");
      oIQuery.addRestriction("ExternalContentId.ECProviderSyncList.ECProviderSyncID", paramAbstractContentProvider.getId());
      oIQuery.addColumn("ExternalContentId.ECProviderSyncList.ECPropertyCfgChkSum");
      oICursor = oIQuery.execute();
      while (oICursor.next()) {
        String str1 = (String)hashMap2.get(oICursor.getStringified("000obj_cod"));
        String str2 = oICursor.getStringified("ExternalContentId");
        if (!hashSet.contains(str2)) {
          hashSet.add(str2);
          ContentProviderConfigMPNCatalog contentProviderConfigMPNCatalog = contentProviderConfig.getMPNCatalogConfigByDMN(str1);
          if (contentProviderConfigMPNCatalog == null) {
            logger.warn("Unable to synchronize External Content for Manufacturer Part '" + oICursor.getString("ManufacturerpartId") + "' with Content Provider '" + paramAbstractContentProvider.getName() + "'. Configuration has no mapping defined for EDM Library Manufacturer Part catalog '" + oICursor.getProxyObject().getOIClass().getLabel() + " (" + str1 + ")'.");
            continue;
          } 
          ContentProviderConfigContentProviderMap contentProviderConfigContentProviderMap = contentProviderConfigMPNCatalog.getContentProviderMap();
          if (!oICursor.getString("ECPropertyCfgChkSum").equals(contentProviderConfigContentProviderMap.getPropertyConfigChecksum())) {
            OIObject oIObject = oICursor.getObject("ExternalContentId");
            try {
              syncExernalContentPartRecordToDMS(null, oIObject, paramAbstractContentProvider, date);
            } catch (ContentProviderSyncException contentProviderSyncException) {
              logger.warn("Unable to save Content Provider data to EDM Library External Content object: " + contentProviderSyncException.getMessage());
            } 
          } 
        } 
      } 
      oICursor.close();
    } catch (OIException oIException) {
      throw new ContentProviderSyncException("Error while querying EDM Library for Manufacturer Parts that have been affected by configuration change:  " + oIException.getMessage());
    } 
    logger.info("Synchronizing External Content that previously failed to synchronize with the Content Provider...");
    setEnableErrorCount(false);
    try {
      OIQuery oIQuery = paramOIObjectManager.createQuery("ExternalContentSyncReg", true);
      oIQuery.addRestriction("ContentProviderId", paramAbstractContentProvider.getId());
      oIQuery.addColumn("ExternalContentId");
      OICursor oICursor = oIQuery.execute();
      OIObject oIObject = null;
      while (oICursor.next()) {
        String str = oICursor.getString("ExternalContentId");
        if (!hashSet.contains(str)) {
          hashSet.add(str);
          try {
            oIObject = paramOIObjectManager.getObjectByID(str, "ExternalContent", true);
          } catch (OIException oIException) {
            logger.info("Need to remove the failure record : " + oIException.getMessage());
            continue;
          } 
          try {
            syncExernalContentPartRecordToDMS(null, oIObject, paramAbstractContentProvider, date);
          } catch (ContentProviderSyncException contentProviderSyncException) {
            logger.warn("Unable to save Content Provider data to EDM Library External Content object: " + contentProviderSyncException.getMessage());
          } 
        } 
      } 
      oICursor.close();
    } catch (OIException oIException) {
      throw new ContentProviderSyncException("Error while querying for previously failed External Content synchronizations:  " + oIException.getMessage());
    } 
    logger.info("Synchronization with Content Provider '" + paramAbstractContentProvider.getName() + "' completed with " + b + " errors.");
    if (b == 0) {
      logger.info("Updating last synchronization date for Content Provider '" + paramAbstractContentProvider.getName() + "...");
      setLastSyncDate(paramOIObjectManager, paramAbstractContentProvider, cacheInfo.getToDate());
    } else {
      logger.info("Last synchronization date for Content Provider '" + paramAbstractContentProvider.getName() + " will not be updated due to errors encountered.");
    } 
  }
  
  public static void syncExernalContentPartRecordToDMS(OIObject paramOIObject1, OIObject paramOIObject2, AbstractContentProvider paramAbstractContentProvider, Date paramDate) throws ContentProviderSyncException {
    syncExernalContentPartRecordToDMS(paramOIObject1, paramOIObject2, paramAbstractContentProvider, paramDate, false);
  }
  
  public static void syncExernalContentPartRecordToDMS(OIObject paramOIObject1, OIObject paramOIObject2, AbstractContentProvider paramAbstractContentProvider, Date paramDate, boolean paramBoolean) throws ContentProviderSyncException {
    OIObjectManager oIObjectManager = paramOIObject2.getObjectManager();
    OIObject oIObject = getSyncRegistryEntry(paramOIObject2, paramAbstractContentProvider.getId());
    IContentProviderResultRecord iContentProviderResultRecord = null;
    try {
      iContentProviderResultRecord = paramAbstractContentProvider.getPart(getProviderIdMapForEC(paramAbstractContentProvider.getId(), paramOIObject2));
    } catch (ContentProviderException contentProviderException) {
      try {
        oIObject.set("FailureDescription", contentProviderException.getMessage());
        oIObjectManager.makePermanent(oIObject);
      } catch (OIException oIException) {
        logger.warn("Unable to set failure status for Content Synchronization object '" + oIObject.getObjectID() + "'\n\n" + oIException.getMessage());
      } finally {
        try {
          oIObjectManager.evict(oIObject);
        } catch (OIException oIException) {}
      } 
      throw new ContentProviderSyncException("Unable to get part record from Content Provider: " + contentProviderException.getMessage());
    } 
    syncExernalContentPartRecordToDMS(paramOIObject1, paramOIObject2, oIObject, paramAbstractContentProvider, iContentProviderResultRecord, paramDate, paramBoolean);
  }
  
  public static void syncExernalContentPartRecordToDMS(OIObject paramOIObject1, OIObject paramOIObject2, OIObject paramOIObject3, AbstractContentProvider paramAbstractContentProvider, IContentProviderResultRecord paramIContentProviderResultRecord, Date paramDate) throws ContentProviderSyncException {
    syncExernalContentPartRecordToDMS(paramOIObject1, paramOIObject2, paramOIObject3, paramAbstractContentProvider, paramIContentProviderResultRecord, paramDate, false);
  }
  
  public static void syncExernalContentPartRecordToDMS(OIObject paramOIObject1, OIObject paramOIObject2, OIObject paramOIObject3, AbstractContentProvider paramAbstractContentProvider, IContentProviderResultRecord paramIContentProviderResultRecord, Date paramDate, boolean paramBoolean) throws ContentProviderSyncException {
    OIObjectManager oIObjectManager = paramOIObject2.getObjectManager();
    try {
      if (paramOIObject1 == null) {
        OIQuery oIQuery = oIObjectManager.createQuery("ManufacturerPart", true);
        oIQuery.addColumn("ManufacturerpartId");
        oIQuery.addRestriction("ExternalContentId", OIHelper.escapeQueryRestriction(paramOIObject2.getString("ExternalContentId")));
        OICursor oICursor = oIQuery.execute();
        if (oICursor.next())
          paramOIObject1 = oICursor.getProxyObject().getObject(); 
        if (paramOIObject1 == null)
          throw new ContentProviderSyncException("Unable to load updates to External Content Object '" + paramOIObject2.getString("ExternalContentId") + "':  Not referenced by a Manufacturer Part."); 
      } 
      logger.info("Loading updates to EDM Library External Content object '" + paramOIObject2.getString("ExternalContentId") + "' for Content Provider part:");
      for (ContentProviderConfigProperty contentProviderConfigProperty : paramAbstractContentProvider.getConfig().getIdProperties())
        logger.info("  " + contentProviderConfigProperty.getContentProviderId() + " = '" + paramIContentProviderResultRecord.getPartProperty(contentProviderConfigProperty.getContentProviderId()).getValue() + "'"); 
      ContentProviderConfigMPNCatalog contentProviderConfigMPNCatalog1 = null;
      boolean bool1 = false;
      boolean bool2 = false;
      ContentProviderConfigMPNCatalog contentProviderConfigMPNCatalog2 = paramAbstractContentProvider.getConfig().getMPNCatalogConfigByDMN(paramOIObject1.getOIClass().getName());
      if (contentProviderConfigMPNCatalog2 != null) {
        ContentProviderConfigContentProviderMap contentProviderConfigContentProviderMap1 = contentProviderConfigMPNCatalog2.getContentProviderMap();
        if (!contentProviderConfigContentProviderMap1.getContentProviderId().equals(paramIContentProviderResultRecord.getPartClassID())) {
          bool2 = true;
        } else {
          bool1 = true;
        } 
      } else {
        bool2 = true;
      } 
      Collection<ContentProviderConfigMPNCatalog> collection = paramAbstractContentProvider.getConfig().getMPNCatalogsByContentProviderId(paramIContentProviderResultRecord.getPartClassID());
      if (collection.size() == 1) {
        contentProviderConfigMPNCatalog1 = collection.iterator().next();
      } else if (collection.size() > 1) {
        throw new ContentProviderSyncException("EDM Library Catalog Config for Content Provider class '" + paramIContentProviderResultRecord.getPartClassName() + "' is mapped to more than one Manufacturer Part catalog.");
      } 
      if (contentProviderConfigMPNCatalog1 != null && bool2) {
        String str1 = contentProviderConfigMPNCatalog1.getClassDMN();
        OIClass oIClass = oIObjectManager.getObjectManagerFactory().getClassManager().getOIClass(str1);
        logger.info("Moving Manufacturer Part from catalog '" + paramOIObject1.getOIClass().getLabel() + " (" + paramOIObject1.getOIClass().getName() + ")' to '" + oIClass.getLabel() + " (" + oIClass.getName() + ")'...");
        try {
          oIObjectManager.moveInClassHierarchy(paramOIObject1, oIClass);
          oIObjectManager.makePermanent(paramOIObject1);
          bool1 = true;
        } catch (Exception exception) {
          logger.warn("Unable to move Manufacturer Part: " + exception.getMessage());
        } 
      } 
      if (!bool1) {
        logger.info("Manufacturer Part catalog '" + paramOIObject1.getOIClass().getLabel() + " (" + paramOIObject1.getOIClass().getName() + ")' does not have mapping to Content Provider class '" + paramIContentProviderResultRecord.getPartClassName() + "'.  Property mapping discrepancies may occur.");
        contentProviderConfigMPNCatalog1 = paramAbstractContentProvider.getConfig().getMPNCatalogConfigByDMN(paramOIObject1.getOIClass().getName());
        if (contentProviderConfigMPNCatalog1 != null) {
          logger.info("Using mappings for Manufacturer Part's catalog '" + paramOIObject1.getOIClass().getLabel() + " (" + paramOIObject1.getOIClass().getName() + ")'.");
        } else {
          contentProviderConfigMPNCatalog1 = paramAbstractContentProvider.getConfig().getMPNCatalogConfigByDMN("RootManufacturerPart");
          if (contentProviderConfigMPNCatalog1 != null)
            logger.info("Using mappings for the root Manufacturer Part Catalog."); 
        } 
        if (contentProviderConfigMPNCatalog1 == null)
          throw new ContentProviderSyncException("No suitable EDM Library Catalog Config mappings found."); 
      } 
      ContentProviderConfigContentProviderMap contentProviderConfigContentProviderMap = contentProviderConfigMPNCatalog1.getContentProviderMap();
      ContentProviderConfigPartClass contentProviderConfigPartClass = contentProviderConfigContentProviderMap.getPartClass();
      if (paramOIObject2.getMode() != 1)
        oIObjectManager.refreshAndLockObject(paramOIObject2); 
      String str = paramOIObject2.getString("ReconcileFlag");
      boolean bool3 = false;
      OIObjectSet oIObjectSet = paramOIObject2.getSet("ECProviderSyncList");
      for (OIObject oIObject1 : oIObjectSet) {
        if (oIObject1.getString("ECProviderSyncID").equals(paramAbstractContentProvider.getId()) && oIObject1.getString("ECPropertyCfgChkSum").equals(contentProviderConfigContentProviderMap.getPropertyConfigChecksum())) {
          bool3 = true;
          break;
        } 
      } 
      if (!bool3)
        str = "No"; 
      HashMap<Object, Object> hashMap = new HashMap<>();
      PropertySyncList propertySyncList = new PropertySyncList(paramOIObject2);
      for (PropertySyncListLine propertySyncListLine : propertySyncList.getPropertySyncListLines()) {
        if (propertySyncListLine.getContentProviderId().equals(paramAbstractContentProvider.getId())) {
          propertySyncListLine.setInclude(false);
          if (paramBoolean)
            propertySyncListLine.setPropPrevValue(""); 
          hashMap.put(propertySyncListLine.getPropId(), propertySyncListLine);
        } 
      } 
      propertySyncList.clearDMSList();
      for (ContentProviderConfigPropertyMap contentProviderConfigPropertyMap : contentProviderConfigContentProviderMap.getCatalogPropertyMaps()) {
        if (contentProviderConfigPropertyMap.getSyncType() == ContentProviderConfig.PropertySyncType.IGNORE)
          continue; 
        String str1 = paramAbstractContentProvider.getPropertyName(contentProviderConfigPropertyMap.getContentProviderId());
        if (!str1.equals(contentProviderConfigPropertyMap.getContentProviderId()))
          str1 = str1 + " (" + str1 + ")"; 
        ContentProviderConfigProperty contentProviderConfigProperty = contentProviderConfigPartClass.getClassPropertyByContentProviderId(contentProviderConfigPropertyMap.getContentProviderId());
        PropertySyncListLine propertySyncListLine = (PropertySyncListLine)hashMap.get(contentProviderConfigPropertyMap.getContentProviderId());
        ComponentProperty componentProperty = paramIContentProviderResultRecord.getPartProperty(contentProviderConfigPropertyMap.getContentProviderId());
        if (componentProperty == null) {
          if (propertySyncListLine != null) {
            logger.debug("Property '" + str1 + "' was not returned from Content Provider.  Existing External Content property value of '" + propertySyncListLine.getPropValue() + "' will be preserved.");
            propertySyncListLine.setInclude(true);
            continue;
          } 
          logger.debug("Property '" + str1 + "' is in configuration mapping but was not returned from Content Provider.");
          continue;
        } 
        String str2 = componentProperty.getValue();
        if (propertySyncListLine == null) {
          logger.debug("Creating new property '" + str1 + "' = '" + str2 + "'.");
          propertySyncListLine = new PropertySyncListLine();
          propertySyncListLine.setContentProviderId(paramAbstractContentProvider.getId());
          propertySyncListLine.setPropId(componentProperty.getId());
          propertySyncListLine.setPropPrevValue("<None>");
          if (str2 != null && !str2.isEmpty())
            str = "No"; 
          propertySyncList.addPropertySyncListLine(propertySyncListLine);
        } else {
          if (!propertySyncListLine.getPropValue().equals(str2)) {
            logger.info("Updating existing property '" + str1 + "' from '" + propertySyncListLine.getPropValue() + "' to '" + str2 + "'.");
          } else {
            logger.debug("Property '" + str1 + "' = '" + str2 + "' was unchanged.");
          } 
          if (!propertySyncListLine.getPropPrevValue().equals(str2))
            str = "No"; 
        } 
        propertySyncListLine.setPropValue(str2);
        propertySyncListLine.setInclude(true);
      } 
      propertySyncList.addIncludedToDMSList();
      if (paramOIObject2.getOIClass().hasField("PCNAlertList")) {
        OIObjectSet oIObjectSet1 = paramOIObject2.getSet("PCNAlertList");
        HashMap<Object, Object> hashMap1 = new HashMap<>();
        for (OIObject oIObject1 : oIObjectSet1)
          hashMap1.put(oIObject1.getString("Alert_ihsid"), oIObject1); 
        for (ContentProviderChangeAlert contentProviderChangeAlert : paramIContentProviderResultRecord.getChangeAlerts()) {
          OIObject oIObject1 = (OIObject)hashMap1.get(contentProviderChangeAlert.getObjectId());
          if (oIObject1 == null) {
            oIObject1 = oIObjectSet1.createLine();
            oIObject1.set("Alert_ihsid", contentProviderChangeAlert.getObjectId());
            hashMap1.put(contentProviderChangeAlert.getObjectId(), oIObject1);
          } 
          oIObject1.set("AlertNo", contentProviderChangeAlert.getAlertNumber());
          oIObject1.set("AlertSource", contentProviderChangeAlert.getInformationSource());
          oIObject1.set("AlertDesc", contentProviderChangeAlert.getNotificationDescription());
          oIObject1.set("ImpDate", contentProviderChangeAlert.getImplementationDate());
          oIObject1.set("IssueDate", contentProviderChangeAlert.getIssueDate());
          oIObject1.set("NotificationNo", contentProviderChangeAlert.getNotificationNumber());
          oIObject1.set("AlertCode", contentProviderChangeAlert.getCode());
          syncAlertDocuments(oIObject1.getSet("AlertDocList"), contentProviderChangeAlert.getAttachedDocuments());
        } 
      } 
      if (paramOIObject2.getOIClass().hasField("PFNAlertList")) {
        OIObjectSet oIObjectSet1 = paramOIObject2.getSet("PFNAlertList");
        HashMap<Object, Object> hashMap1 = new HashMap<>();
        for (OIObject oIObject1 : oIObjectSet1)
          hashMap1.put(oIObject1.getString("Alert_ihsid"), oIObject1); 
        for (ContentProviderFailureAlert contentProviderFailureAlert : paramIContentProviderResultRecord.getFailureAlerts()) {
          OIObject oIObject1 = (OIObject)hashMap1.get(contentProviderFailureAlert.getObjectId());
          if (oIObject1 == null) {
            oIObject1 = oIObjectSet1.createLine();
            oIObject1.set("Alert_ihsid", contentProviderFailureAlert.getObjectId());
            hashMap1.put(contentProviderFailureAlert.getObjectId(), oIObject1);
          } 
          oIObject1.set("AlertNo", contentProviderFailureAlert.getAlertNumber());
          oIObject1.set("AlertSource", contentProviderFailureAlert.getInformationSource());
          oIObject1.set("AlertDesc", contentProviderFailureAlert.getProblemDescription());
          oIObject1.set("IssueDate", contentProviderFailureAlert.getIssueDate());
          oIObject1.set("NotificationNo", contentProviderFailureAlert.getNotificationNumber());
          oIObject1.set("BatchSerialNo", contentProviderFailureAlert.getSerialNumber());
          oIObject1.set("ActionTakenPlanned", contentProviderFailureAlert.getPlannedAction());
          syncAlertDocuments(oIObject1.getSet("AlertDocList"), contentProviderFailureAlert.getAttachedDocuments());
        } 
      } 
      if (paramOIObject2.getOIClass().hasField("PSCAlertList")) {
        OIObjectSet oIObjectSet1 = paramOIObject2.getSet("PSCAlertList");
        HashMap<Object, Object> hashMap1 = new HashMap<>();
        for (OIObject oIObject1 : oIObjectSet1)
          hashMap1.put(oIObject1.getString("Alert_ihsid"), oIObject1); 
        for (ContentProviderPartStatusChange contentProviderPartStatusChange : paramIContentProviderResultRecord.getPartStatusChanges()) {
          OIObject oIObject1 = (OIObject)hashMap1.get(contentProviderPartStatusChange.getObjectId());
          if (oIObject1 == null) {
            oIObject1 = oIObjectSet1.createLine();
            oIObject1.set("Alert_ihsid", contentProviderPartStatusChange.getObjectId());
            hashMap1.put(contentProviderPartStatusChange.getObjectId(), oIObject1);
          } 
          oIObject1.set("AlertNo", contentProviderPartStatusChange.getAlertNumber());
          oIObject1.set("AlertSource", contentProviderPartStatusChange.getInformationSource());
          oIObject1.set("AlertDesc", contentProviderPartStatusChange.getNotificationDescription());
          oIObject1.set("ModDate", contentProviderPartStatusChange.getModifiedDate());
          oIObject1.set("IssueDate", contentProviderPartStatusChange.getIssueDate());
        } 
      } 
      if (paramOIObject2.getOIClass().hasField("EOLAlertList")) {
        OIObjectSet oIObjectSet1 = paramOIObject2.getSet("EOLAlertList");
        HashMap<Object, Object> hashMap1 = new HashMap<>();
        for (OIObject oIObject1 : oIObjectSet1)
          hashMap1.put(oIObject1.getString("Alert_ihsid"), oIObject1); 
        for (ContentProviderEndOfLifeAlert contentProviderEndOfLifeAlert : paramIContentProviderResultRecord.getEndOfLifeAlerts()) {
          OIObject oIObject1 = (OIObject)hashMap1.get(contentProviderEndOfLifeAlert.getObjectId());
          if (oIObject1 == null) {
            oIObject1 = oIObjectSet1.createLine();
            oIObject1.set("Alert_ihsid", contentProviderEndOfLifeAlert.getObjectId());
            hashMap1.put(contentProviderEndOfLifeAlert.getObjectId(), oIObject1);
          } 
          oIObject1.set("AlertNo", contentProviderEndOfLifeAlert.getAlertNumber());
          oIObject1.set("AlertSource", contentProviderEndOfLifeAlert.getLifeCycleInformationSource());
          oIObject1.set("LTBDate", contentProviderEndOfLifeAlert.getLastTimeBuyDate());
          oIObject1.set("LTDDate", contentProviderEndOfLifeAlert.getLastTimeDeliveryDate());
          syncAlertDocuments(oIObject1.getSet("AlertDocList"), contentProviderEndOfLifeAlert.getAttachedDocuments());
        } 
      } 
      Date date = paramOIObject2.getDate("LastModifiedDate");
      paramOIObject2.set("ReconcileFlag", str);
      oIObjectSet = paramOIObject2.getSet("ECProviderSyncList");
      OIObject oIObject = null;
      for (OIObject oIObject1 : oIObjectSet) {
        if (oIObject1.getString("ECProviderSyncID").equals(paramAbstractContentProvider.getId())) {
          oIObject = oIObject1;
          break;
        } 
      } 
      if (oIObject == null)
        oIObject = oIObjectSet.createLine(); 
      oIObject.set("ECProviderSyncID", paramAbstractContentProvider.getId());
      oIObject.set("ECLastSyncDate", paramDate);
      oIObject.set("ECPropertyCfgChkSum", contentProviderConfigContentProviderMap.getPropertyConfigChecksum());
      try {
        oIObject.set("ECCacheId", cacheId);
      } catch (OIException oIException) {}
      if (str.equals("Yes"))
        paramOIObject2.set("ReconcileDate", date); 
      if (bCommitMode)
        oIObjectManager.makePermanent(paramOIObject2); 
      try {
        oIObjectManager.deleteObject(paramOIObject3);
        oIObjectManager.makePermanent(paramOIObject3);
      } catch (OIException oIException) {
        logger.warn("Unable to remove External Content Synchronization object '" + paramOIObject3.getObjectID() + "': " + oIException.getMessage());
      } 
    } catch (Exception exception) {
      try {
        paramOIObject3.set("FailureDescription", exception.getMessage());
        oIObjectManager.makePermanent(paramOIObject3);
      } catch (OIException oIException) {
        logger.warn("Unable to set failure status for Content Synchronization object '" + paramOIObject3.getObjectID() + "'\n\n" + oIException.getMessage());
      } 
      String str = "Unable to synchronize External Content '" + paramOIObject2.getObjectID() + "' with Content Provider:  " + exception.getMessage();
      if (ContentProviderGlobal.isInteractiveExecMode()) {
        JOptionPane.showMessageDialog(ContentProviderGlobal.getRootFrame(), str);
      } else {
        logger.warn(str);
      } 
    } finally {
      if (bCommitMode)
        try {
          oIObjectManager.evict(paramOIObject2);
        } catch (OIException oIException) {} 
      try {
        oIObjectManager.evict(paramOIObject3);
      } catch (OIException oIException) {}
    } 
  }
  
  private static void syncAlertDocuments(OIObjectSet paramOIObjectSet, ContentProviderDocumentList paramContentProviderDocumentList) throws OIException {
    HashMap<Object, Object> hashMap = new HashMap<>();
    for (OIObject oIObject : paramOIObjectSet)
      hashMap.put(oIObject.getString("AlertDocLinekey"), oIObject); 
    for (ContentProviderDocument contentProviderDocument : paramContentProviderDocumentList) {
      OIObject oIObject = (OIObject)hashMap.get(contentProviderDocument.getObjectId());
      if (oIObject == null) {
        oIObject = paramOIObjectSet.createLine();
        oIObject.set("AlertDocLinekey", contentProviderDocument.getObjectId());
        hashMap.put(contentProviderDocument.getObjectId(), oIObject);
      } 
      oIObject.set("AlertDocType", contentProviderDocument.getType());
      oIObject.set("AlertDocTitle", contentProviderDocument.getTitle());
      oIObject.set("AlertDocPubDate", contentProviderDocument.getPublicationDate());
      oIObject.set("AlertDocURL", contentProviderDocument.getURL());
    } 
  }
  
  public static OIObject getSyncRegistryEntry(OIObject paramOIObject, String paramString) throws ContentProviderSyncException {
    OIObjectManager oIObjectManager = paramOIObject.getObjectManager();
    OIObject oIObject = null;
    try {
      OIQuery oIQuery = oIObjectManager.createQuery("ExternalContentSyncReg", true);
      oIQuery.addRestriction("ExternalContentId", OIHelper.escapeQueryRestriction(paramOIObject.getObjectID()));
      oIQuery.addRestriction("ContentProviderId", paramString);
      oIQuery.addColumn("ExternalContentSyncId");
      OICursor oICursor = oIQuery.execute();
      if (oICursor.next()) {
        oIObject = oICursor.getProxyObject().getObject();
      } else {
        oIObject = oIObjectManager.createObject("ExternalContentSyncReg");
        oIObject.set("ExternalContentId", paramOIObject.getObjectID());
        oIObject.set("ContentProviderId", paramString);
      } 
      oIObject.set("LastAttemptDate", new Date());
      oIObject.set("SyncStatus", "Failed");
      oIObject.set("FailureDescription", "Unknown");
      oIObjectManager.makePermanent(oIObject);
    } catch (OIException oIException) {
      throw new ContentProviderSyncException("Unable to initialize Synchronization Registry entry: " + oIException.getMessage());
    } 
    return oIObject;
  }
  
  public static void compareAndReconcile(OIObjectManager paramOIObjectManager, boolean paramBoolean1, boolean paramBoolean2) throws ContentProviderSyncException, ContentProviderConfigException {
    logger.info("Reconciling all External Content to EDM Library Manufacturer Parts...");
    reconciledMPNList.clear();
    try {
      OIQuery oIQuery = paramOIObjectManager.createQuery("ExternalContent", true);
      oIQuery.addColumn("ExternalContentId");
      if (!paramBoolean2) {
        logger.info("Querying for unreconciled Manufacturer Parts...");
        oIQuery.addRestriction("ReconcileFlag", "No");
      } else {
        logger.info("Querying for all Manufacturer Parts with External Content...");
      } 
      OICursor oICursor = oIQuery.execute();
      while (oICursor.next()) {
        OIObject oIObject = oICursor.getProxyObject().getObject();
        try {
          compareAndReconcile(null, oIObject, paramBoolean1, null, null);
        } catch (ContentProviderException contentProviderException) {
          logger.error(contentProviderException.getMessage());
        } 
      } 
    } catch (OIException oIException) {
      logger.error(oIException.getMessage());
    } 
  }
  
  public static void compareAndReconcile(OIObject paramOIObject1, OIObject paramOIObject2, boolean paramBoolean, HashSet<String> paramHashSet, HashMap<String, String> paramHashMap) throws ContentProviderSyncException, ContentProviderConfigException {
    OIObjectManager oIObjectManager = paramOIObject2.getObjectManager();
    boolean bool = true;
    try {
      if (paramOIObject1 == null) {
        OIQuery oIQuery = oIObjectManager.createQuery("ManufacturerPart", true);
        oIQuery.addRestriction("ExternalContentId", OIHelper.escapeQueryRestriction(paramOIObject2.getString("ExternalContentId")));
        oIQuery.addColumn("ManufacturerpartId");
        OICursor oICursor = oIQuery.execute();
        if (oICursor.next()) {
          paramOIObject1 = oICursor.getProxyObject().getObject();
        } else {
          throw new ContentProviderSyncException("External Content object '" + paramOIObject2.getString("ExternalContentId") + "' is not referenced by a Manufacturer Part.");
        } 
      } 
      if (!StateUtilities.isLocked(paramOIObject1)) {
        String str = LockUtilities.getSessionLockHolder(paramOIObject1);
        if (str != null) {
          logger.warn("Unable to reconcile Manufacturer Part '" + paramOIObject1.getString("ManufacturerpartId") + "' because it is locked by '" + str + "' in another session.  Skipping.");
          return;
        } 
      } 
      for (AbstractContentProvider abstractContentProvider : ContentProviderFactory.getInstance().getRegisteredContentProviders()) {
        if (!abstractContentProvider.hasRole(ContentProviderRegistryEntry.ContentProviderRole.MANUFACTURER_PART_SYNCHRONIZATION))
          continue; 
        ContentProviderConfig contentProviderConfig = abstractContentProvider.getConfig();
        logger.info("Reconciling Manufacturer Part '" + paramOIObject1.getString("ManufacturerpartId") + "' with properties synced from Content Provider '" + abstractContentProvider.getName() + "'.");
        ContentProviderConfigMPNCatalog contentProviderConfigMPNCatalog = contentProviderConfig.getMPNCatalogConfigByDMN(paramOIObject1.getOIClass().getName());
        if (contentProviderConfigMPNCatalog == null) {
          logger.warn("Content provider '" + abstractContentProvider.getName() + "' configuration has no mapping defined for EDM Library Manufacturer Part catalog '" + paramOIObject1.getOIClass().getLabel() + " (" + paramOIObject1.getOIClass().getName() + ")'.");
          continue;
        } 
        ContentProviderConfigContentProviderMap contentProviderConfigContentProviderMap = contentProviderConfigMPNCatalog.getContentProviderMap();
        ContentProviderConfigPartClass contentProviderConfigPartClass = contentProviderConfigContentProviderMap.getPartClass();
        PropertySyncList propertySyncList = new PropertySyncList(paramOIObject2);
        for (PropertySyncListLine propertySyncListLine : propertySyncList.getPropertySyncListLines()) {
          ContentProviderConfigPropertyMap contentProviderConfigPropertyMap = contentProviderConfigContentProviderMap.getPropertyMapByContentProviderId(propertySyncListLine.getPropId());
          if (contentProviderConfigPropertyMap == null) {
            logger.debug("Content Provider Property '" + propertySyncListLine.getPropId() + "' is not mapped in the Content Provider configuration.");
            continue;
          } 
          if (contentProviderConfigPropertyMap.getSyncType() == ContentProviderConfig.PropertySyncType.IGNORE) {
            logger.debug("Content Provider Property '" + propertySyncListLine.getPropId() + "' Sync Type is set to 'IGNORE' in the Content Provider configuration.");
            continue;
          } 
          if (contentProviderConfigPropertyMap.getDMN() == null || contentProviderConfigPropertyMap.getDMN().isEmpty()) {
            logger.debug("Content Provider Property '" + propertySyncListLine.getPropId() + "' is not mapped in the Content Provider configuration.");
            continue;
          } 
          if (paramHashSet != null && paramHashSet.contains(propertySyncListLine.getPropId())) {
            logger.info("User requested that property '" + propertySyncListLine.getPropId() + "' be ignored once.  Skipping.");
            continue;
          } 
          if (propertySyncListLine.getReconcileAction().equals("Ignore")) {
            logger.info("External Content's 'Reconcile Action' for property '" + propertySyncListLine.getPropId() + "' has been set to 'Ignore'.  Skipping.");
            continue;
          } 
          try {
            logger.debug("Reconciling characteristic '" + contentProviderConfigPropertyMap.getDMN() + "'...");
            ContentProviderConfigProperty contentProviderConfigProperty = contentProviderConfigPartClass.getClassPropertyByContentProviderId(contentProviderConfigPropertyMap.getContentProviderId());
            if (contentProviderConfigPropertyMap.getSyncType() == ContentProviderConfig.PropertySyncType.DIRECT) {
              setDMSCharacteristic(paramOIObject1, propertySyncListLine, contentProviderConfigPropertyMap, contentProviderConfigProperty);
              continue;
            } 
            if (contentProviderConfigPropertyMap.getSyncType() == ContentProviderConfig.PropertySyncType.SYNC) {
              String str = paramOIObject1.getStringified(contentProviderConfigPropertyMap.getDMN());
              boolean bool1 = compareExternalContentToDMSCharacteristic(paramOIObject1, contentProviderConfigPropertyMap, contentProviderConfigProperty, propertySyncListLine.getPropValue());
              if (bool1)
                continue; 
              if ((str == null || str.isEmpty()) && !propertySyncListLine.getPropValue().isEmpty()) {
                setDMSCharacteristic(paramOIObject1, propertySyncListLine, contentProviderConfigPropertyMap, contentProviderConfigProperty);
                continue;
              } 
              if ((str == null || str.isEmpty() || !propertySyncListLine.getPropValue().isEmpty()) && !propertySyncListLine.getPropValue().equals(propertySyncListLine.getPropPrevValue())) {
                if (paramBoolean) {
                  setDMSCharacteristic(paramOIObject1, propertySyncListLine, contentProviderConfigPropertyMap, contentProviderConfigProperty);
                  continue;
                } 
                bool = false;
                String str1 = paramOIObject1.getOIClass().getField(contentProviderConfigPropertyMap.getDMN()).getLabel();
                logger.info("Change occurred to the Content Provider property mapped to MPN characteristic '" + str1 + "'.  Review should be performed.");
              } 
            } 
          } catch (ContentProviderSyncException contentProviderSyncException) {
            logger.warn("  " + contentProviderSyncException.getMessage());
            if (paramHashMap != null)
              paramHashMap.put(contentProviderConfigPropertyMap.getDMN(), contentProviderSyncException.getMessage()); 
            bool = false;
          } 
        } 
        if (bool) {
          if (bCommitMode)
            oIObjectManager.makePermanent(paramOIObject1); 
          if (paramOIObject2.getMode() != 1)
            oIObjectManager.refreshAndLockObject(paramOIObject2); 
          OIObjectSet oIObjectSet = paramOIObject2.getSet("ECPropSyncList");
          for (OIObject oIObject : oIObjectSet)
            oIObject.set("ECPrevPropValue", oIObject.get("ECPropValue")); 
          paramOIObject2.set("ReconcileFlag", "Yes");
          paramOIObject2.set("ReconcileDate", paramOIObject2.getDate("LastModifiedDate"));
          if (bCommitMode)
            oIObjectManager.makePermanent(paramOIObject2); 
          reconciledMPNList.add(paramOIObject1.getString("ManufacturerpartId"));
          continue;
        } 
        logger.warn("Unable to reconcile Manufacturer Part '" + paramOIObject2.getString("ExternalContentId") + "' because one or more characteristics could not be reconciled.");
      } 
    } catch (OIException oIException) {
      String str = "Unable to reconcile Manufacturer Part '" + paramOIObject2.getObjectID() + "':\n\t" + oIException.getMessage();
      throw new ContentProviderSyncException(str);
    } catch (ContentProviderException contentProviderException) {
      String str = "Unable to reconcile Manufacturer Part '" + paramOIObject2.getObjectID() + "':\n\t" + contentProviderException.getMessage();
      throw new ContentProviderSyncException(str);
    } finally {
      if (bCommitMode)
        try {
          oIObjectManager.evict(paramOIObject2);
          if (paramOIObject1 != null)
            oIObjectManager.evict(paramOIObject1); 
        } catch (OIException oIException) {} 
    } 
  }
  
  public static void setDMSCharacteristic(OIObject paramOIObject, PropertySyncListLine paramPropertySyncListLine, ContentProviderConfigPropertyMap paramContentProviderConfigPropertyMap, ContentProviderConfigProperty paramContentProviderConfigProperty) throws ContentProviderSyncException {
    String str1 = paramContentProviderConfigPropertyMap.getDMN();
    String str2 = paramPropertySyncListLine.getPropValue();
    if (!str2.isEmpty())
      str2 = str2 + str2; 
    try {
      OIField oIField = paramOIObject.getOIClass().getField(str1);
      if (oIField.getType() == OIField.Type.STRING) {
        OIStringField oIStringField = (OIStringField)oIField;
        if (str2.length() > oIStringField.getMaximalLength()) {
          String str = "Unable to set Manufacturer Part characteristic '" + str1 + "' with value '" + str2 + "': Max length is " + oIStringField.getMaximalLength() + ".";
          throw new ContentProviderSyncException(str);
        } 
        paramOIObject.set(str1, str2);
      } else if (!str2.trim().isEmpty()) {
        if (oIField.getType() == OIField.Type.DATE) {
          SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
          paramOIObject.set(str1, simpleDateFormat.parse(str2));
        } else if (oIField.getType() == OIField.Type.DOUBLE || oIField.getType() == OIField.Type.INTEGER) {
          paramOIObject.setWithUnit(str1, str2);
        } 
      } else {
        paramOIObject.set(str1, null);
      } 
    } catch (OIException oIException) {
      String str = "Unable to set Manufacturer Part characteristic '" + str1 + "' with value '" + str2 + "':  " + oIException.getMessage();
      throw new ContentProviderSyncException(str);
    } catch (ParseException parseException) {
      String str = "Unable to set Manufacturer Part characteristic '" + str1 + "' with date value '" + str2 + "':  " + parseException.getMessage();
      throw new ContentProviderSyncException(str);
    } catch (NumberFormatException numberFormatException) {
      String str = "Unable to set Manufacturer Part characteristic '" + str1 + "' with numeric value '" + str2 + "':  " + numberFormatException.getMessage();
      throw new ContentProviderSyncException(str);
    } 
  }
  
  private static boolean compareExternalContentToDMSCharacteristic(OIObject paramOIObject, ContentProviderConfigPropertyMap paramContentProviderConfigPropertyMap, ContentProviderConfigProperty paramContentProviderConfigProperty, String paramString) throws ContentProviderSyncException {
    try {
      if (paramOIObject.getStringified(paramContentProviderConfigPropertyMap.getDMN()) == null)
        return (paramString == null || paramString.trim().isEmpty()); 
      if (paramString == null || paramString.trim().isEmpty())
        return (paramOIObject.getStringified(paramContentProviderConfigPropertyMap.getDMN()) == null); 
      OIField oIField = paramOIObject.getOIClass().getField(paramContentProviderConfigPropertyMap.getDMN());
      if (oIField.getType() == OIField.Type.STRING)
        return paramOIObject.getString(paramContentProviderConfigPropertyMap.getDMN()).equals(paramString); 
      if (oIField.getType() == OIField.Type.INTEGER) {
        int i = Integer.parseInt(paramString);
        return (paramOIObject.getInteger(paramContentProviderConfigPropertyMap.getDMN()).intValue() == i);
      } 
      if (oIField.getType() == OIField.Type.DOUBLE) {
        if (paramContentProviderConfigProperty.getBaseUnits() == null || paramContentProviderConfigProperty.getBaseUnits().trim().isEmpty()) {
          double d1 = Double.parseDouble(paramString);
          return (paramOIObject.getDouble(paramContentProviderConfigPropertyMap.getDMN()).doubleValue() == d1);
        } 
        boolean bool = false;
        double d = paramOIObject.getDouble(paramContentProviderConfigPropertyMap.getDMN()).doubleValue();
        try {
          if (!paramString.isEmpty())
            paramString = paramString + paramString; 
          paramOIObject.setWithUnit(paramContentProviderConfigPropertyMap.getDMN(), paramString);
          double d1 = paramOIObject.getDouble(paramContentProviderConfigPropertyMap.getDMN()).doubleValue();
          bool = (d == d1) ? true : false;
        } finally {
          paramOIObject.set(paramContentProviderConfigPropertyMap.getDMN(), Double.valueOf(d));
        } 
        return bool;
      } 
      if (oIField.getType() == OIField.Type.DATE) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        Date date = simpleDateFormat.parse(paramString);
        return (paramOIObject.getDate(paramContentProviderConfigPropertyMap.getDMN()) == date);
      } 
    } catch (Exception exception) {
      throw new ContentProviderSyncException("Unable to compare Manufacturer Part characteristic '" + paramContentProviderConfigPropertyMap.getDMN() + "' with value '" + paramString + "':  " + exception.getMessage());
    } 
    return false;
  }
  
  public static void setReconcileActions(OIObject paramOIObject, HashSet<String> paramHashSet) throws ContentProviderSyncException {
    try {
      OIObjectSet oIObjectSet = paramOIObject.getSet("ECPropSyncList");
      for (OIObject oIObject : oIObjectSet) {
        String str = oIObject.getString("ECPropID");
        if (paramHashSet.contains(str)) {
          oIObject.set("ECPropReconcileAction", "Ignore");
          continue;
        } 
        oIObject.set("ECPropReconcileAction", "Default");
      } 
      paramOIObject.getObjectManager().makePermanent(paramOIObject);
    } catch (OIException oIException) {
      String str = "Unable to set reconcile actions for Manufacturer Part '" + paramOIObject.getObjectID() + "':\n\t" + oIException.getMessage();
      logger.error(str);
      throw new ContentProviderSyncException(str);
    } 
  }
  
  public static void syncMPN2CompsIncremental(OIObjectManager paramOIObjectManager) throws ContentProviderException, ContentProviderSyncException, ContentProviderConfigException {
    logger.info("Incrementally synchronizing EDM Library Manufacturer Parts to Components..");
    HashMap<Object, Object> hashMap = new HashMap<>();
    try {
      OIQuery oIQuery = paramOIObjectManager.createQuery("ManufacturerPart", true);
      oIQuery.addColumn("ManufacturerpartId");
      oIQuery.addColumn("LastModifiedDate");
      OICursor oICursor = oIQuery.execute();
      while (oICursor.next())
        hashMap.put(oICursor.getString("ManufacturerpartId"), oICursor.getDate("LastModifiedDate")); 
      oICursor.close();
      oIQuery = paramOIObjectManager.createQuery("Component", true);
      for (Map.Entry entry : ContentProviderFactory.getInstance().getAppConfig().getApplicationImpl().getRepresentativeMPNSearchRestrictions().entrySet())
        oIQuery.addRestriction("ApprovedManufacturerList." + (String)entry.getKey(), (String)entry.getValue()); 
      oIQuery.addColumn("PartNumber");
      oIQuery.addColumn("ECMPNSyncDate");
      oIQuery.addColumn("ApprovedManufacturerList.MfgPartNumber");
      oICursor = oIQuery.execute();
      while (oICursor.next()) {
        String str = oICursor.getStringified("MfgPartNumber");
        Date date1 = oICursor.getDate("ECMPNSyncDate");
        Date date2 = (Date)hashMap.get(str);
        if (date2 != null && !date2.equals(date1)) {
          String str1 = oICursor.getString("PartNumber");
          OIObject oIObject = oICursor.getObject("MfgPartNumber");
          logger.info("Synchronizing Component '" + str1 + "'...");
          try {
            syncMPN2Comps(oIObject, str1, false);
          } catch (Exception exception) {
            logger.warn(exception.getMessage());
          } 
        } 
      } 
      oICursor.close();
    } catch (OIException oIException) {
      String str = "Error while querying EDM Library for Component and Manufacturer Part synchronization data: " + oIException.getMessage();
      throw new ContentProviderSyncException(str);
    } 
  }
  
  public static void syncMPN2CompsReconciled(OIObjectManager paramOIObjectManager) throws ContentProviderException, ContentProviderSyncException, ContentProviderConfigException {
    logger.info("Synchronizing EDM Library Manufacturer Parts to Components (for all Manufacturer Parts reconciled on this run)...");
    for (String str : reconciledMPNList) {
      OIObject oIObject = null;
      try {
        oIObject = paramOIObjectManager.getObjectByID(str, "ManufacturerPart", true);
      } catch (OIException oIException) {
        String str1 = "Error while querying EDM Library for Manufacturer Part '" + str + ": " + oIException.getMessage();
        logger.warn(str1);
      } 
      try {
        syncMPN2Comps(oIObject, "", false);
      } catch (Exception exception) {
        logger.warn(exception.getMessage());
      } 
    } 
  }
  
  public static void syncMPN2CompsAll(OIObjectManager paramOIObjectManager) throws ContentProviderSyncException, ContentProviderConfigException {
    logger.info("Synchronizing all Components with representative Manufacturer Part...");
    HashSet<String> hashSet = new HashSet();
    try {
      OIQuery oIQuery = paramOIObjectManager.createQuery("Component", true);
      oIQuery.addRestriction("ApprovedManufacturerList.MfgPartNumber", "~NULL");
      for (Map.Entry entry : ContentProviderFactory.getInstance().getAppConfig().getApplicationImpl().getRepresentativeMPNSearchRestrictions().entrySet())
        oIQuery.addRestriction("ApprovedManufacturerList." + (String)entry.getKey(), (String)entry.getValue()); 
      oIQuery.addColumn("PartNumber");
      oIQuery.addColumn("ApprovedManufacturerList.MfgPartNumber");
      OICursor oICursor = oIQuery.execute();
      while (oICursor.next()) {
        String str = oICursor.getString("PartNumber");
        if (hashSet.contains(str))
          continue; 
        hashSet.add(str);
        logger.info("Synchronizing Component '" + str + "'...");
        OIObject oIObject = oICursor.getObject("MfgPartNumber");
        try {
          syncMPN2Comps(oIObject, str, false);
        } catch (Exception exception) {
          logger.warn(exception.getMessage());
        } 
      } 
    } catch (Exception exception) {
      String str = "Error while querying EDM Library for all Components with representative Manufacturer Parts : " + exception.getMessage();
      throw new ContentProviderSyncException(str);
    } 
  }
  
  public static String syncMPN2Comps(OIObject paramOIObject, String paramString, boolean paramBoolean) throws ContentProviderException, ContentProviderConfigException {
    Level level = Level.ERROR;
    if (paramBoolean)
      level = Level.INFO; 
    StringBuilder stringBuilder = new StringBuilder("");
    try {
      OIObjectManager oIObjectManager = paramOIObject.getObjectManager();
      OIClass oIClass = paramOIObject.getOIClass();
      OIQuery oIQuery1 = oIObjectManager.createQuery("ExternalContent", true);
      oIQuery1.addRestriction("ExternalContentId", OIHelper.escapeQueryRestriction(paramOIObject.getString("ManufacturerpartId")));
      oIQuery1.addColumn("ExternalContentId");
      oIQuery1.addColumn("ReconcileFlag");
      OICursor oICursor1 = oIQuery1.execute();
      if (oICursor1.next()) {
        String str = oICursor1.getString("ReconcileFlag");
        if (!str.equals("Yes")) {
          String str1 = "Manufacturer Part '" + paramOIObject.getString("ManufacturerpartId") + "' has not been reconciled.  Components may be synchronized with outdated Content Provider properties.";
          if (ContentProviderGlobal.isInteractiveExecMode() && !paramBoolean) {
            int i = JOptionPane.showConfirmDialog(ContentProviderGlobal.getRootFrame(), str1 + "  Continue?");
            if (i != 0)
              return "Cancelled"; 
          } else {
            stringBuilder.append(str1 + "\n");
            logger.info(str1);
          } 
        } 
      } 
      AbstractContentProvider abstractContentProvider = getDefaultContentProvider();
      OIQuery oIQuery2 = oIObjectManager.createQuery("Component", true);
      oIQuery2.addColumn("PartNumber");
      oIQuery2.addRestriction("PartNumber", OIHelper.escapeQueryRestriction(paramString));
      oIQuery2.addRestriction("ApprovedManufacturerList.MfgPartNumber", OIHelper.escapeQueryRestriction(paramOIObject.getString("ManufacturerpartId")));
      OICursor oICursor2 = oIQuery2.execute();
      ArrayList<OIObject> arrayList = new ArrayList();
      while (oICursor2.next()) {
        OIObject oIObject = oICursor2.getProxyObject().getObject();
        OIObjectSet oIObjectSet = oIObject.getSet("ApprovedManufacturerList");
        byte b = 0;
        boolean bool = false;
        for (OIObject oIObject1 : oIObjectSet) {
          HashMap<Object, Object> hashMap = new HashMap<>();
          for (OIField oIField : oIObject1.getOIClass().getFields())
            hashMap.put(oIField.getName(), oIObject1.getStringified(oIField.getName())); 
          if (ContentProviderFactory.getInstance().getAppConfig().getApplicationImpl().isRepresentativeMPN(hashMap)) {
            b++;
            String str = oIObject1.getStringified("MfgPartNumber");
            if (str.equals(paramOIObject.getString("ManufacturerpartId")))
              bool = true; 
          } 
        } 
        if (bool) {
          if (b > 1) {
            String str = "Component '" + oICursor2.getString("PartNumber") + "' contains a reference to more than one representative Manufacturer Part.  Component will not be updated from Manufacturer Part.";
            displayCompSyncMessage(stringBuilder, str, level, paramBoolean);
            continue;
          } 
          arrayList.add(oIObject);
        } 
      } 
      oICursor2.close();
      if (arrayList.size() == 0) {
        String str;
        if (paramString.isEmpty()) {
          str = "No Components exist which reference a representative Manufacturer Part '" + paramOIObject.getString("ManufacturerpartId") + "'.  No Manufacturer Part to Component synchronization performed.";
        } else {
          str = "Component '" + paramString + "' does not reference a representative Manufacturer Part '" + paramOIObject.getString("ManufacturerpartId") + "'.  No Manufacturer Part to Component synchronization performed.";
        } 
        displayCompSyncMessage(stringBuilder, str, level, paramBoolean);
        return stringBuilder.toString();
      } 
      if (ContentProviderGlobal.isInteractiveExecMode() && !paramBoolean) {
        String str1 = "The following Component(s) will be synchronized with Manufacturer Part '" + paramOIObject.getString("ManufacturerpartId") + "'.  Do you want to continue?\n\n";
        String str2 = "";
        String str3 = "";
        String str4 = "";
        int i = 0;
        byte b = 70;
        for (OIObject oIObject : arrayList) {
          String str = oIObject.getObjectID();
          if (i + str.length() > b) {
            str3 = "\n";
            i = 0;
          } else {
            i += str.length();
            str3 = "";
          } 
          str4 = str4 + str4 + str2 + str3;
          str2 = ", ";
        } 
        if (ContentProviderGlobal.isInteractiveExecMode()) {
          int j = JOptionPane.showConfirmDialog(ContentProviderGlobal.getRootFrame(), str1 + str1, "Component - Update From Manufacturer Part", 0);
          if (j != 0)
            return "Cancelled"; 
        } 
      } 
      ArrayList<String> arrayList1 = new ArrayList();
      for (ContentProviderConfigComponentCatalog contentProviderConfigComponentCatalog : abstractContentProvider.getConfig().getComponentCatalogsByMPNDMN(oIClass.getName())) {
        if (contentProviderConfigComponentCatalog.isAllowMoveParts())
          arrayList1.add(contentProviderConfigComponentCatalog.getClassDMN()); 
      } 
      for (OIObject oIObject : arrayList) {
        if (abstractContentProvider.getConfig().isCompAllowMoveParts()) {
          boolean bool = arrayList1.contains(oIObject.getOIClass().getName());
          String str = null;
          boolean bool1 = false;
          if (!bool)
            if (arrayList1.size() == 1) {
              str = arrayList1.iterator().next();
              bool1 = true;
            } else if (arrayList1.size() > 1) {
              if (ContentProviderGlobal.isInteractiveExecMode()) {
                int i = JOptionPane.showConfirmDialog(ContentProviderGlobal.getRootFrame(), "Component '" + oIObject.getObjectID() + "' should be moved to a catalog that has a mapping to Manufacturer Part catalog '" + paramOIObject.getOIClass().getLabel() + " (" + paramOIObject.getOIClass().getName() + ")'.\n\nWould you like to move the Component?.", "Component - Update From Manufacturer Part", 0);
                if (i == 0) {
                  OIClass oIClass1;
                  CatalogSelectionDialog catalogSelectionDialog = new CatalogSelectionDialog(oIObjectManager, ContentProviderGlobal.getRootFrame(), "Select target Component catalog");
                  try {
                    oIClass1 = catalogSelectionDialog.showDialog(new ImageIcon(CatalogSelectionDialog.class.getResource("images/component.png")), "RootComponent", arrayList1);
                  } catch (OIException oIException) {
                    throw new ContentProviderSyncException("Unable to display Catalog Selection dialog: " + oIException.getMessage());
                  } 
                  if (oIClass1 != null) {
                    str = oIClass1.getName();
                    bool1 = true;
                  } 
                } 
              } else {
                String str1 = "Unable to move Component.  Manufacturer Part catalog '" + paramOIObject.getOIClass().getLabel() + " (" + paramOIObject.getOIClass().getName() + ")' maps to more than one Component catalog.";
                stringBuilder.append(str1 + "\n");
                logger.info(str1);
              } 
            }  
          if (str != null && bool1) {
            OIClass oIClass1 = oIObjectManager.getObjectManagerFactory().getClassManager().getOIClass(str);
            String str1 = "Moving Component '" + oIObject.getObjectID() + "' from catalog '" + oIObject.getOIClass().getLabel() + " (" + oIObject.getOIClass().getName() + ")' to '" + oIClass1.getLabel() + " (" + oIClass1.getName() + ")'...";
            stringBuilder.append(str1 + "\n");
            logger.info(str1);
            try {
              oIObjectManager.moveInClassHierarchy(oIObject, oIClass1);
              oIObjectManager.makePermanent(oIObject);
              bool = true;
            } catch (Exception exception) {
              logger.warn("Unable to move Component: " + exception.getMessage());
            } 
          } 
        } 
        try {
          String str1 = "Processing property mappings from Manufacturer Part '" + paramOIObject.getString("ManufacturerpartId") + "' to Component '" + oIObject.getObjectID() + "'...";
          stringBuilder.append(str1 + "\n");
          logger.info(str1);
          HashMap<Object, Object> hashMap = new HashMap<>();
          OIClass oIClass1 = oIObject.getOIClass();
          String str2 = oIClass1.getName();
          ContentProviderConfigComponentCatalog contentProviderConfigComponentCatalog = abstractContentProvider.getConfig().getComponentCatalogConfigByDMN(str2, true);
          boolean bool = false;
          for (ContentProviderConfigManufacturerPartMap contentProviderConfigManufacturerPartMap : contentProviderConfigComponentCatalog.getManufacturerPartMaps()) {
            if (!contentProviderConfigManufacturerPartMap.getClassDMN().equals(oIClass.getName()))
              continue; 
            bool = true;
            processMPNToCompMapping(abstractContentProvider, paramOIObject, oIObject, contentProviderConfigManufacturerPartMap, (HashMap)hashMap, stringBuilder, level, paramBoolean);
          } 
          if (!bool) {
            str1 = "Unable to find a ManufacturerPartMap mapping from Manufacturer Part catalog '" + oIClass.getLabel() + "' (" + oIClass.getName() + ")' to Component catalog '" + oIObject.getOIClass().getLabel() + " (" + oIObject.getOIClass().getName() + ")'. \nAttempting to map top-level properties...";
            stringBuilder.append(str1 + "\n");
            logger.warn(str1);
            contentProviderConfigComponentCatalog = abstractContentProvider.getConfig().getRootComponentCatalogConfig();
            for (ContentProviderConfigManufacturerPartMap contentProviderConfigManufacturerPartMap : contentProviderConfigComponentCatalog.getManufacturerPartMaps()) {
              bool = true;
              processMPNToCompMapping(abstractContentProvider, paramOIObject, oIObject, contentProviderConfigManufacturerPartMap, (HashMap)hashMap, stringBuilder, level, paramBoolean);
            } 
            if (!bool) {
              str1 = "Unable to find root ComponentCatalog mapping (DMN = 'RootComponent'.";
              stringBuilder.append(str1 + "\n");
              logger.warn(str1);
            } 
          } 
          if (hashMap.size() > 0 || oIObject.getDate("ECMPNSyncDate") == null || !oIObject.getDate("ECMPNSyncDate").equals(paramOIObject.getDate("LastModifiedDate"))) {
            try {
              oIObjectManager.refreshAndLockObject(oIObject);
              boolean bool1 = true;
              for (Map.Entry<Object, Object> entry : hashMap.entrySet()) {
                str1 = "  Updating '" + oIClass1.getField((String)entry.getKey()).getLabel() + "' from '" + oIObject.getStringifiedWithUnit((String)entry.getKey()) + "' to '" + String.valueOf(entry.getValue()) + "'...";
                stringBuilder.append(str1 + "\n");
                logger.info(str1);
                try {
                  setComponentCharacteristic(oIObject, (String)entry.getKey(), entry.getValue());
                } catch (ContentProviderSyncException contentProviderSyncException) {
                  str1 = contentProviderSyncException.getMessage();
                  stringBuilder.append(str1 + "\n");
                  logger.info(str1);
                  bool1 = false;
                } 
              } 
              if (!bool1)
                throw new ContentProviderException("One or more properties failed to copy from Manufacturer Part to Component."); 
              oIObject.set("ECMPNSyncDate", paramOIObject.getDate("LastModifiedDate"));
              oIObjectManager.makePermanent(oIObject);
              str1 = "Component '" + oIObject.getObjectID() + "' updated with properties from Manufacturer Part '" + paramOIObject.getString("ManufacturerpartId") + "'.\n";
              stringBuilder.append(str1 + "\n");
              logger.info(str1);
            } catch (Exception exception) {
              str1 = "Unable to synchronize Component '" + oIObject.getObjectID() + "' with Manufacturer Part '" + paramOIObject.getString("ManufacturerpartId") + "': " + exception.getMessage();
              displayCompSyncMessage(stringBuilder, str1, level, paramBoolean);
            } 
          } else {
            str1 = "Component update was not required.\n";
            stringBuilder.append(str1 + "\n");
            logger.info(str1);
          } 
        } finally {
          oIObjectManager.evict(oIObject);
        } 
      } 
      if (ContentProviderGlobal.isInteractiveExecMode() && !paramBoolean)
        LogWindow.displayText("Update Component(s)", String.valueOf(stringBuilder) + "\nManufacturer Part to Component(s) synchronization completed."); 
    } catch (OIException oIException) {
      String str = "Error encountered while attempting to synchronize Manufacturer Part '" + paramOIObject.getObjectID() + "' :  " + oIException.getMessage();
      displayCompSyncMessage(stringBuilder, str, level, paramBoolean);
    } 
    return stringBuilder.toString();
  }
  
  private static void processMPNToCompMapping(AbstractContentProvider paramAbstractContentProvider, OIObject paramOIObject1, OIObject paramOIObject2, ContentProviderConfigManufacturerPartMap paramContentProviderConfigManufacturerPartMap, HashMap<String, Object> paramHashMap, StringBuilder paramStringBuilder, Level paramLevel, boolean paramBoolean) throws OIException {
    String str = "Comparing mapped properties using ManufacturerPartMap with classDMN ='" + paramContentProviderConfigManufacturerPartMap.getClassDMN() + "'.";
    paramStringBuilder.append(str + "\n");
    logger.info(str);
    for (AbstractContentProviderConfigManufacturerPartPropertyMap abstractContentProviderConfigManufacturerPartPropertyMap : paramContentProviderConfigManufacturerPartMap.getCatalogComponentPropertyMaps()) {
      try {
        HashMap<Object, Object> hashMap = new HashMap<>();
        if (abstractContentProviderConfigManufacturerPartPropertyMap instanceof ContentProviderConfigManufacturerPartPropertyMap) {
          hashMap.put(((ContentProviderConfigManufacturerPartPropertyMap)abstractContentProviderConfigManufacturerPartPropertyMap).getComponentPropertyDMN(), abstractContentProviderConfigManufacturerPartPropertyMap);
        } else if (abstractContentProviderConfigManufacturerPartPropertyMap instanceof ContentProviderConfigScriptedManufacturerPartPropertyMap) {
          ContentProviderConfigScriptedManufacturerPartPropertyMap contentProviderConfigScriptedManufacturerPartPropertyMap = (ContentProviderConfigScriptedManufacturerPartPropertyMap)abstractContentProviderConfigManufacturerPartPropertyMap;
          ContentProviderConfigScriptEngine contentProviderConfigScriptEngine = paramAbstractContentProvider.getConfig().getScriptEngine();
          try {
            contentProviderConfigScriptEngine.callMappingFunction(paramOIObject1, paramOIObject2, contentProviderConfigScriptedManufacturerPartPropertyMap.getMappingFunction());
          } catch (ContentProviderConfigException contentProviderConfigException) {
            str = "Error encountered when calling ScriptedComponentPropertyMap mapping function : \nScript text = '" + contentProviderConfigScriptedManufacturerPartPropertyMap.getMappingFunction() + "'\n" + contentProviderConfigException.getMessage();
            displayCompSyncMessage(paramStringBuilder, str, paramLevel, paramBoolean);
          } 
          for (Map.Entry entry : contentProviderConfigScriptEngine.getComponentPropertyMap().getMap().entrySet())
            hashMap.put(entry.getKey(), entry.getValue()); 
        } 
        for (Map.Entry<Object, Object> entry : hashMap.entrySet()) {
          String str1 = (String)entry.getKey();
          Object object1 = entry.getValue();
          Object object2 = null;
          if (object1 instanceof ContentProviderConfigManufacturerPartPropertyMap) {
            object2 = paramOIObject1.get(((ContentProviderConfigManufacturerPartPropertyMap)object1).getManufacturerPartPropertyDMN());
          } else {
            object2 = object1;
          } 
          if (!compareValueToDMSComponentCharacteristic(paramOIObject2, str1, object2, abstractContentProviderConfigManufacturerPartPropertyMap.getSyncType(), paramStringBuilder))
            paramHashMap.put(str1, object2); 
        } 
      } catch (Exception exception) {
        str = "Unable to synchronize Component '" + paramOIObject2.getObjectID() + "' with Manufacturer Part '" + paramOIObject1.getString("ManufacturerpartId") + ": " + exception.getMessage();
        displayCompSyncMessage(paramStringBuilder, str, paramLevel, paramBoolean);
      } 
    } 
  }
  
  private static void displayCompSyncMessage(StringBuilder paramStringBuilder, String paramString, Level paramLevel, boolean paramBoolean) {
    if (ContentProviderGlobal.isInteractiveExecMode() && !paramBoolean) {
      JOptionPane.showMessageDialog(ContentProviderGlobal.getRootFrame(), paramString, "Synchronize MPN to Component(s)", 1);
    } else {
      paramStringBuilder.append(paramString + "\n");
      logger.log((Priority)paramLevel, paramString);
    } 
  }
  
  private static void setComponentCharacteristic(OIObject paramOIObject, String paramString, Object paramObject) throws ContentProviderSyncException {
    boolean bool1 = true;
    boolean bool2 = false;
    OIField oIField = null;
    try {
      oIField = paramOIObject.getOIClass().getField(paramString);
      if (oIField.getType() == OIField.Type.STRING) {
        if (paramObject == null)
          paramObject = ""; 
        if (paramObject instanceof String) {
          OIStringField oIStringField = (OIStringField)oIField;
          if (((String)paramObject).length() > oIStringField.getMaximalLength())
            logger.warn("Unable to set Component characteristic '" + paramString + "' with value '" + String.valueOf(paramObject) + "': Max length is " + oIStringField.getMaximalLength() + ".  Value will be truncated."); 
          paramOIObject.set(paramString, paramObject);
        } else {
          bool2 = true;
        } 
      } else if (oIField.getType() == OIField.Type.INTEGER) {
        if (paramObject == null || paramObject instanceof Integer) {
          paramOIObject.set(paramString, paramObject);
        } else if (paramObject instanceof String) {
          String str = (String)paramObject;
          if (str.trim().isEmpty()) {
            paramOIObject.set(paramString, null);
          } else {
            paramOIObject.setWithUnit(paramString, str);
          } 
        } else {
          bool2 = true;
        } 
      } else if (oIField.getType() == OIField.Type.DOUBLE) {
        if (paramObject == null || paramObject instanceof Double) {
          paramOIObject.set(paramString, paramObject);
        } else if (paramObject instanceof String) {
          String str = (String)paramObject;
          if (str.trim().isEmpty()) {
            paramOIObject.set(paramString, null);
          } else {
            paramOIObject.setWithUnit(paramString, (String)paramObject);
          } 
        } else {
          bool2 = true;
        } 
      } else if (oIField.getType() == OIField.Type.DATE) {
        if (paramObject == null || paramObject instanceof Date) {
          paramOIObject.set(paramString, paramObject);
        } else if (paramObject instanceof String) {
          SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
          Date date = simpleDateFormat.parse((String)paramObject);
          paramOIObject.set(paramString, date);
        } else {
          bool2 = true;
        } 
      } 
    } catch (Exception exception) {
      throw new ContentProviderSyncException("Unable to set Component characteristic '" + paramString + "' with value '" + String.valueOf(paramObject) + "':  " + exception.getMessage());
    } 
    if (bool2)
      throw new ContentProviderSyncException("Unable perform update - type mismatch between Manufacturer Part characteristic and Component characteristic:\nManufacturer Part value type = '" + paramObject.getClass().toString() + "'\nComponent '" + oIField.getLabel() + "' type = '" + String.valueOf(oIField.getType()) + "'."); 
  }
  
  private static boolean compareValueToDMSComponentCharacteristic(OIObject paramOIObject, String paramString, Object paramObject, ContentProviderConfig.PropertySyncType paramPropertySyncType, StringBuilder paramStringBuilder, boolean paramBoolean) throws ContentProviderSyncException {
    boolean bool = false;
    try {
      OIField oIField = paramOIObject.getOIClass().getField(paramString);
      String str1 = oIField.getLabel();
      String str2 = paramOIObject.getStringifiedWithUnit(paramString);
      if (paramPropertySyncType == ContentProviderConfig.PropertySyncType.IGNORE) {
        if (paramBoolean) {
          String str = "  Component characteristic (syncType = INGORE) '" + str1 + "' = '" + str2 + "' not updated to Manufacturer Part value '" + String.valueOf(paramObject) + "'...";
          paramStringBuilder.append(str + "\n");
          logger.warn(str);
        } 
        return true;
      } 
      if (paramBoolean)
        logger.debug("  Comparing Component characteristic '" + str1 + "' = '" + str2 + "' to Manufacturer Part value '" + String.valueOf(paramObject) + "'..."); 
      boolean bool1 = (paramOIObject.get(paramString) == null || (oIField.getType() == OIField.Type.STRING && paramOIObject.getString(paramString).isEmpty())) ? true : false;
      boolean bool2 = (paramObject == null || (paramObject instanceof String && ((String)paramObject).trim().isEmpty())) ? true : false;
      if (bool1 && bool2)
        return true; 
      if (bool1 != bool2) {
        if (paramPropertySyncType == ContentProviderConfig.PropertySyncType.SYNC && bool2) {
          if (paramBoolean) {
            String str = "  Component characteristic (syncType = SYNC) '" + str1 + "' = '" + str2 + "' not updated with NULL value from Manufacturer Part.";
            logger.warn(str);
            paramStringBuilder.append(str + "\n");
          } 
          return true;
        } 
        return false;
      } 
      if (oIField.getType() == OIField.Type.STRING) {
        if (paramObject instanceof String)
          bool = paramOIObject.getString(paramString).equals(paramObject); 
      } else if (oIField.getType() == OIField.Type.INTEGER) {
        if (paramObject instanceof Integer) {
          bool = paramOIObject.getInteger(paramString).equals(paramObject);
        } else if (paramObject instanceof String) {
          String str = (String)paramObject;
          try {
            int i = Integer.parseInt(str);
            bool = (paramOIObject.getInteger(paramString).intValue() == i);
          } catch (NumberFormatException numberFormatException) {
            throw new ContentProviderSyncException("Invalid integer format - " + numberFormatException.getMessage());
          } 
        } 
      } else if (oIField.getType() == OIField.Type.DOUBLE) {
        if (paramObject instanceof Double) {
          bool = paramOIObject.getDouble(paramString).equals(paramObject);
        } else if (paramObject instanceof String) {
          String str = (String)paramObject;
          bool = false;
          double d = paramOIObject.getDouble(paramString).doubleValue();
          try {
            paramOIObject.setWithUnit(paramString, str);
            double d1 = paramOIObject.getDouble(paramString).doubleValue();
            bool = (d == d1);
          } finally {
            paramOIObject.set(paramString, Double.valueOf(d));
          } 
        } 
      } else if (oIField.getType() == OIField.Type.DATE) {
        if (paramObject instanceof Date) {
          bool = paramOIObject.getDate(paramString).equals(paramObject);
        } else if (paramObject instanceof String) {
          SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
          Date date = simpleDateFormat.parse((String)paramObject);
          bool = paramOIObject.getDate(paramString).equals(date);
        } 
      } 
      if (!bool && paramPropertySyncType == ContentProviderConfig.PropertySyncType.SYNC) {
        if (paramBoolean) {
          String str = "  Component characteristic (syncType = SYNC) '" + str1 + "' = '" + str2 + "' not updated with Manufacturer Part value '" + String.valueOf(paramObject) + "'.";
          paramStringBuilder.append(str + "\n");
          logger.warn(str);
        } 
        bool = true;
      } 
    } catch (Exception exception) {
      throw new ContentProviderSyncException("Unable to compare Component characteristic '" + paramString + "' with value '" + String.valueOf(paramObject) + "':  " + exception.getMessage());
    } 
    return bool;
  }
  
  private static boolean compareValueToDMSComponentCharacteristic(OIObject paramOIObject, String paramString, Object paramObject, ContentProviderConfig.PropertySyncType paramPropertySyncType, StringBuilder paramStringBuilder) throws ContentProviderSyncException {
    return compareValueToDMSComponentCharacteristic(paramOIObject, paramString, paramObject, paramPropertySyncType, paramStringBuilder, true);
  }
  
  public static boolean compareValueToDMSCharacteristic(OIObject paramOIObject, String paramString, Object paramObject) throws ContentProviderSyncException {
    return compareValueToDMSComponentCharacteristic(paramOIObject, paramString, paramObject, ContentProviderConfig.PropertySyncType.DIRECT, new StringBuilder(), false);
  }
  
  public static void syncComp2MPN(OIObject paramOIObject) throws ContentProviderException, ContentProviderSyncException, ContentProviderConfigException {
    try {
      boolean bool = false;
      OIObjectSet oIObjectSet = paramOIObject.getSet("ApprovedManufacturerList");
      for (OIObject oIObject : oIObjectSet) {
        if (bool)
          break; 
        HashMap<Object, Object> hashMap = new HashMap<>();
        for (OIField oIField : oIObject.getOIClass().getFields())
          hashMap.put(oIField.getName(), oIObject.getStringified(oIField.getName())); 
        if (ContentProviderFactory.getInstance().getAppConfig().getApplicationImpl().isRepresentativeMPN(hashMap)) {
          OIObject oIObject1 = oIObject.getObject("MfgPartNumber");
          if (oIObject1 != null) {
            bool = true;
            syncMPN2Comps(oIObject1, paramOIObject.getString("PartNumber"), false);
          } 
        } 
      } 
      if (!bool) {
        String str = "Component '" + paramOIObject.getObjectID() + "' does not reference a representative Manfacturer Part.  No Manufacturer Part to Component synchronization performed.";
        if (ContentProviderGlobal.isInteractiveExecMode()) {
          JOptionPane.showMessageDialog(ContentProviderGlobal.getRootFrame(), str);
        } else {
          logger.info(str);
        } 
      } 
    } catch (OIException oIException) {
      String str = "Error while reading Approved Manufacturer List for Component '" + paramOIObject.getObjectID() + "':  " + oIException.getMessage();
      throw new ContentProviderSyncException(str);
    } 
  }
  
  public static OIObject assignEC2MPN(AbstractContentProvider paramAbstractContentProvider, OIObject paramOIObject, Map<String, String> paramMap) throws ContentProviderSyncException {
    OIObjectManager oIObjectManager = paramOIObject.getObjectManager();
    OIObject oIObject = null;
    try {
      String str1 = paramOIObject.getString("ManufacturerpartId");
      try {
        oIObject = oIObjectManager.getObjectByID(str1, "ExternalContent", true);
      } catch (OIException oIException) {}
      if (oIObject == null) {
        oIObject = oIObjectManager.createObject("ExternalContent");
        oIObject.set("ExternalContentId", str1);
      } else {
        HashMap<Object, Object> hashMap = new HashMap<>();
        OIObjectSet oIObjectSet = oIObject.getSet("ECProviderReferences");
        for (OIObject oIObject1 : oIObjectSet) {
          if (oIObject1.getString("ECProviderReferenceID").equals(paramAbstractContentProvider.getId()))
            hashMap.put(oIObject1.getString("ECProviderReferenceKey"), oIObject1.getString("ECProviderReferenceValue")); 
        } 
        boolean bool = false;
        if (hashMap.size() != 0)
          if (hashMap.size() == paramMap.size()) {
            for (String str3 : paramMap.keySet()) {
              String str4 = (String)hashMap.get(str3);
              if (str4 == null || !str4.equals(paramMap.get(str3)))
                bool = true; 
            } 
          } else {
            bool = true;
          }  
        if (bool && ContentProviderGlobal.isInteractiveExecMode()) {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("This Manufacturer Part is already assigned to the following Content Provider's part:\n\n");
          for (String str : hashMap.keySet())
            stringBuilder.append("  " + str + " : " + (String)hashMap.get(str) + "\n"); 
          stringBuilder.append("\nDo you want to continue?");
          int i = JOptionPane.showConfirmDialog(ContentProviderGlobal.getRootFrame(), stringBuilder, "EDM Library - Assign External Content", 0);
          if (i == 1)
            return null; 
        } 
        if (oIObject.getMode() != 1)
          oIObjectManager.refreshAndLockObject(oIObject); 
      } 
      ProviderRefList providerRefList = new ProviderRefList(oIObject);
      for (ProviderRefListLine providerRefListLine : providerRefList.getProviderRefListLines()) {
        if (providerRefListLine.getContentProviderId().equals(paramAbstractContentProvider.getId()))
          providerRefListLine.setInclude(false); 
      } 
      providerRefList.clearDMSList();
      providerRefList.addIncludedToDMSList();
      for (String str : paramMap.keySet()) {
        ProviderRefListLine providerRefListLine = new ProviderRefListLine();
        providerRefListLine.setContentProviderId(paramAbstractContentProvider.getId());
        providerRefListLine.setPropId(str);
        providerRefListLine.setPropValue(paramMap.get(str));
        providerRefListLine.addToDMSList(oIObject);
      } 
      PropertySyncList propertySyncList = new PropertySyncList(oIObject);
      for (PropertySyncListLine propertySyncListLine : propertySyncList.getPropertySyncListLines()) {
        if (propertySyncListLine.getContentProviderId().equals(paramAbstractContentProvider.getId()))
          propertySyncListLine.setInclude(false); 
      } 
      propertySyncList.clearDMSList();
      propertySyncList.addIncludedToDMSList();
      String str2 = paramAbstractContentProvider.getConfigurationParameter("DISABLE_SUBSCRIPTION");
      if (str2 == null || !str2.equalsIgnoreCase("TRUE")) {
        String str = paramAbstractContentProvider.addSubscription(paramMap);
        setExternalContentSubscription(paramAbstractContentProvider, oIObject, str);
      } 
      if (bCommitMode)
        oIObjectManager.makePermanent(oIObject); 
      if (paramOIObject.get("ExternalContentId") == null || !paramOIObject.getStringified("ExternalContentId").equals(paramOIObject.getString("ManufacturerpartId"))) {
        paramOIObject.set("ExternalContentId", oIObject);
        if (bCommitMode)
          oIObjectManager.makePermanent(paramOIObject); 
      } 
    } catch (Exception exception) {
      throw new ContentProviderSyncException(exception.getMessage());
    } finally {
      if (bCommitMode && oIObject != null)
        try {
          oIObjectManager.evict(oIObject);
        } catch (OIException oIException) {} 
    } 
    return oIObject;
  }
  
  public static void setExternalContentSubscription(AbstractContentProvider paramAbstractContentProvider, OIObject paramOIObject, String paramString) throws ContentProviderException {
    if (paramString == null || !paramAbstractContentProvider.isClientSubscriptionManagementRequired())
      return; 
    try {
      OIObject oIObject = null;
      OIObjectSet oIObjectSet = paramOIObject.getSet("ECProviderSubscribeList");
      for (OIObject oIObject1 : oIObjectSet) {
        if (oIObject1.getString("ECProviderSubscribeID").equals(paramAbstractContentProvider.getId())) {
          oIObject = oIObject1;
          break;
        } 
      } 
      if (oIObject == null) {
        oIObject = oIObjectSet.createLine();
        oIObject.set("ECProviderSubscribeID", paramAbstractContentProvider.getId());
      } 
      oIObject.set("ECProviderSubscribeValue", paramString);
    } catch (OIException oIException) {
      throw new ContentProviderException("Unable to set External Content subscription ID: " + oIException.getMessage());
    } 
  }
  
  public static OIObject createMPNFromEC(OIObjectManager paramOIObjectManager, IContentProviderResultRecord paramIContentProviderResultRecord, OverwriteEnum paramOverwriteEnum) throws ContentProviderException {
    IContentProviderResultRecord iContentProviderResultRecord;
    AbstractContentProvider abstractContentProvider = paramIContentProviderResultRecord.getContentProvider();
    ContentProviderConfig contentProviderConfig = abstractContentProvider.getConfig();
    try {
      iContentProviderResultRecord = abstractContentProvider.getPart(paramIContentProviderResultRecord);
    } catch (ContentProviderPartNotFoundException contentProviderPartNotFoundException) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("The following part was not found in Content Provider '" + abstractContentProvider.getName() + "':\n\n");
      for (ContentProviderConfigProperty contentProviderConfigProperty : contentProviderConfig.getIdProperties())
        stringBuilder.append("  " + contentProviderConfigProperty.getContentProviderId() + " : " + String.valueOf(paramIContentProviderResultRecord.getPartProperty(contentProviderConfigProperty.getContentProviderId())) + "\n"); 
      JOptionPane.showMessageDialog(ContentProviderGlobal.getRootFrame(), stringBuilder);
      return null;
    } 
    return createMPNFromEC(paramOIObjectManager, iContentProviderResultRecord.getPartNumber(), iContentProviderResultRecord.getManufacturerName(), iContentProviderResultRecord, paramOverwriteEnum);
  }
  
  public static OIObject createMPNFromEC(OIObjectManager paramOIObjectManager, String paramString, HashMap<String, String> paramHashMap, OverwriteEnum paramOverwriteEnum) throws ContentProviderException {
    return createMPNFromEC(paramOIObjectManager, paramString, null, null, paramHashMap, paramOverwriteEnum);
  }
  
  public static OIObject createMPNFromEC(OIObjectManager paramOIObjectManager, String paramString1, String paramString2, String paramString3, HashMap<String, String> paramHashMap, OverwriteEnum paramOverwriteEnum) throws ContentProviderException {
    IContentProviderResultRecord iContentProviderResultRecord;
    AbstractContentProvider abstractContentProvider = ContentProviderFactory.getInstance().createContentProvider(paramString1);
    try {
      iContentProviderResultRecord = abstractContentProvider.getPart(paramHashMap);
    } catch (ContentProviderPartNotFoundException contentProviderPartNotFoundException) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("The following part was not found in Content Provider '" + abstractContentProvider.getName() + "':\n\n");
      for (String str : paramHashMap.keySet())
        stringBuilder.append("  " + str + " : " + (String)paramHashMap.get(str) + "\n"); 
      JOptionPane.showMessageDialog(ContentProviderGlobal.getRootFrame(), stringBuilder);
      return null;
    } 
    if (paramString2 == null)
      paramString2 = iContentProviderResultRecord.getPartNumber(); 
    if (paramString3 == null)
      paramString3 = iContentProviderResultRecord.getManufacturerName(); 
    return createMPNFromEC(paramOIObjectManager, paramString2, paramString3, iContentProviderResultRecord, paramOverwriteEnum);
  }
  
  public static OIObject createMPNFromEC(OIObjectManager paramOIObjectManager, String paramString1, String paramString2, IContentProviderResultRecord paramIContentProviderResultRecord, OverwriteEnum paramOverwriteEnum) throws ContentProviderException {
    OIObject oIObject1 = null;
    try {
      OIQuery oIQuery = paramOIObjectManager.createQuery("ManufacturerPart", true);
      oIQuery.addRestriction("PartNumber", OIHelper.escapeQueryRestriction(paramString1));
      oIQuery.addRestriction("ManufacturerName", OIHelper.escapeQueryRestriction(paramIContentProviderResultRecord.getManufacturerName()));
      oIQuery.addColumn("ManufacturerpartId");
      OICursor oICursor = oIQuery.execute();
      if (oICursor.next())
        if (paramOverwriteEnum.equals(OverwriteEnum.YES)) {
          oIObject1 = oICursor.getProxyObject().getObject();
        } else if (paramOverwriteEnum.equals(OverwriteEnum.ASK)) {
          oIObject1 = oICursor.getProxyObject().getObject();
          int i = JOptionPane.showConfirmDialog(ContentProviderGlobal.getRootFrame(), "Manufacturer Part '" + paramString1 + "' from '" + paramString2 + "' already exists in EDM Library.  Update existing Manufacturer Part?", "Create Manufacturer Part", 0);
          if (i == 1)
            return oIObject1; 
        } else {
          throw new ContentProviderSyncAlreadyExistsException("Manufacturer Part '" + paramString1 + "' from '" + paramString2 + "' already exists in EDM Library.");
        }  
    } catch (OIException oIException) {
      throw new ContentProviderSyncException("Problem while querying for existing Manufacturer Part Number: " + oIException.getMessage());
    } 
    AbstractContentProvider abstractContentProvider = paramIContentProviderResultRecord.getContentProvider();
    IContentProviderResultRecord iContentProviderResultRecord = abstractContentProvider.getPart(paramIContentProviderResultRecord);
    OIObject oIObject2 = getMatchingManufacturer(paramOIObjectManager, abstractContentProvider, iContentProviderResultRecord.getManufacturerID(), iContentProviderResultRecord.getManufacturerName());
    if (oIObject2 == null)
      return null; 
    ContentProviderConfig contentProviderConfig = abstractContentProvider.getConfig();
    if (oIObject1 == null) {
      String str = null;
      Collection<ContentProviderConfigMPNCatalog> collection = contentProviderConfig.getMPNCatalogsByContentProviderId(paramIContentProviderResultRecord.getPartClassID());
      if (collection == null || collection.size() == 0) {
        if (ContentProviderGlobal.isInteractiveExecMode()) {
          OIClass oIClass;
          CatalogSelectionDialog catalogSelectionDialog = new CatalogSelectionDialog(paramOIObjectManager, ContentProviderGlobal.getRootFrame(), "Select target Manufacturer Part catalog");
          try {
            oIClass = catalogSelectionDialog.showDialog(new ImageIcon(CatalogSelectionDialog.class.getResource("images/mpn.png")), "RootManufacturerPart");
          } catch (OIException oIException) {
            throw new ContentProviderSyncException("Unable to display Catalog Selection dialog: " + oIException.getMessage());
          } 
          if (oIClass == null)
            return null; 
          str = oIClass.getName();
        } else {
          throw new ContentProviderSyncException("Content Provider class '" + paramIContentProviderResultRecord.getPartClassName() + "' was not mapped to a Manufacturer Part catalog in the configuration.");
        } 
      } else if (collection.size() == 1) {
        str = ((ContentProviderConfigMPNCatalog)collection.iterator().next()).getClassDMN();
      } else if (ContentProviderGlobal.isInteractiveExecMode()) {
        OIClass oIClass;
        ArrayList<String> arrayList = new ArrayList();
        for (ContentProviderConfigMPNCatalog contentProviderConfigMPNCatalog : collection)
          arrayList.add(contentProviderConfigMPNCatalog.getClassDMN()); 
        CatalogSelectionDialog catalogSelectionDialog = new CatalogSelectionDialog(paramOIObjectManager, ContentProviderGlobal.getRootFrame(), "Select target Manufacturer Part catalog");
        try {
          oIClass = catalogSelectionDialog.showDialog(new ImageIcon(CatalogSelectionDialog.class.getResource("images/mpn.png")), "RootManufacturerPart", arrayList);
        } catch (OIException oIException) {
          throw new ContentProviderSyncException("Unable to display Catalog Selection dialog: " + oIException.getMessage());
        } 
        if (oIClass == null)
          return null; 
        str = oIClass.getName();
      } else {
        throw new ContentProviderSyncException("Part Class '" + paramIContentProviderResultRecord.getPartClassName() + "' maps to more than one Manufacturer Part catalog in the configuration.");
      } 
      try {
        oIObject1 = paramOIObjectManager.createObject(str);
        oIObject1.set("PartNumber", paramString1);
        oIObject1.set("ManufacturerId", oIObject2);
        if (bCommitMode) {
          paramOIObjectManager.makePermanent(oIObject1);
          paramOIObjectManager.refreshObject(oIObject1);
        } 
      } catch (OIException oIException) {
        throw new ContentProviderSyncException("Unable to create new Manufacturer Part:" + oIException.getMessage());
      } 
    } 
    OIObject oIObject3 = assignEC2MPN(abstractContentProvider, oIObject1, paramIContentProviderResultRecord.getIdPropertyMap(contentProviderConfig));
    OIObject oIObject4 = getSyncRegistryEntry(oIObject3, abstractContentProvider.getId());
    syncExernalContentPartRecordToDMS(oIObject1, oIObject3, oIObject4, abstractContentProvider, paramIContentProviderResultRecord, new Date());
    if (ContentProviderGlobal.isBatchExecMode()) {
      compareAndReconcile(oIObject1, oIObject3, true, null, null);
    } else {
      HashSet<String> hashSet = new HashSet();
      hashSet.add(oIObject1.getObjectID());
      ContentProviderReconcileApp.createAndShowGUI(paramOIObjectManager, hashSet);
    } 
    return oIObject1;
  }
  
  public static OIObject getMatchingManufacturer(OIObjectManager paramOIObjectManager, String paramString) throws ContentProviderSyncException {
    return getMatchingManufacturer(paramOIObjectManager, null, null, paramString);
  }
  
  public static OIObject getMatchingManufacturer(OIObjectManager paramOIObjectManager, AbstractContentProvider paramAbstractContentProvider, String paramString) throws ContentProviderSyncException {
    return getMatchingManufacturer(paramOIObjectManager, paramAbstractContentProvider, null, paramString);
  }
  
  public static OIObject getMatchingManufacturer(OIObjectManager paramOIObjectManager, AbstractContentProvider paramAbstractContentProvider, String paramString1, String paramString2) throws ContentProviderSyncException {
    OIObject oIObject = null;
    ArrayList<MfgChoice> arrayList = new ArrayList();
    try {
      OIClass oIClass = paramOIObjectManager.getObjectManagerFactory().getClassManager().getOIClass("Manufacturer");
      if (paramString1 != null && !paramString1.isEmpty() && oIClass.hasDeclaredField("ECMfgID")) {
        OIQuery oIQuery = paramOIObjectManager.createQuery("Manufacturer", true);
        oIQuery.addColumn("ManufacturerId");
        oIQuery.addRestriction("ECMfgID", OIHelper.escapeQueryRestriction(paramString1));
        OICursor oICursor = oIQuery.execute();
        if (oICursor.next())
          arrayList.add(new MfgChoice(oICursor.getProxyObject())); 
      } 
      if (arrayList.isEmpty()) {
        OIQuery oIQuery = paramOIObjectManager.createQuery("Manufacturer", true);
        oIQuery.addColumn("ManufacturerId");
        oIQuery.addColumn("ManufacturerName");
        oIQuery.addRestriction("ManufacturerName", OIHelper.escapeQueryRestriction(paramString2));
        OICursor oICursor = oIQuery.execute();
        while (oICursor.next())
          arrayList.add(new MfgChoice(oICursor.getProxyObject())); 
        if (oIClass.hasDeclaredField("ECMfgNameAliasList")) {
          oIQuery = paramOIObjectManager.createQuery("Manufacturer", true);
          oIQuery.addColumn("ManufacturerId");
          oIQuery.addColumn("ManufacturerName");
          oIQuery.addColumn("ECMfgNameAliasList.ECMfgNameAlias");
          oIQuery.addRestriction("ECMfgNameAliasList.ECMfgNameAlias", OIHelper.escapeQueryRestriction(paramString2));
          oICursor = oIQuery.execute();
          while (oICursor.next())
            arrayList.add(new MfgChoice(oICursor.getProxyObject())); 
        } 
      } 
      if (arrayList.size() == 1) {
        oIObject = ((MfgChoice)arrayList.get(0)).getOIProxyObject().getObject();
      } else if (arrayList.size() > 1) {
        if (ContentProviderGlobal.isBatchExecMode()) {
          logger.warn("More than one EDM Library Manufacturer match for '" + paramString2 + "' found.");
        } else {
          MfgChoice mfgChoice = (MfgChoice)JOptionPane.showInputDialog(ContentProviderGlobal.getRootFrame(), "More than one EDM Library Manufacturer match for '" + paramString2 + "'found, please select from the list below:\n\nManufacturer ID : Manufacturer Name (Alias Name):\n", "Select Manufacturer", -1, null, arrayList.toArray(), null);
          if (mfgChoice != null)
            oIObject = mfgChoice.getOIProxyObject().getObject(); 
        } 
      } else {
        String str = "Manufacturer '" + paramString2 + "' ";
        if (paramString1 != null)
          str = str + "(" + str + ") "; 
        str = str + "does not exist in EDM Library.";
        if (ContentProviderGlobal.isBatchExecMode()) {
          logger.warn(str);
          return null;
        } 
        int i = JOptionPane.showConfirmDialog(ContentProviderGlobal.getRootFrame(), str + "\n\nWould you like to create it?", "Create Manufacturer Part", 0);
        if (i == 1)
          return null; 
        oIObject = createManufacturer(paramOIObjectManager, paramAbstractContentProvider, paramString1, paramString2);
      } 
    } catch (OIException oIException) {
      throw new ContentProviderSyncException("Problem while locating referenced EDM Library Manufacturer: " + oIException.getMessage());
    } 
    return oIObject;
  }
  
  public static OIObject createManufacturer(OIObjectManager paramOIObjectManager, AbstractContentProvider paramAbstractContentProvider, String paramString1, String paramString2) throws ContentProviderSyncException {
    OIObject oIObject = null;
    ContentProviderConfig contentProviderConfig = null;
    ContentProviderManufacturer contentProviderManufacturer = null;
    boolean bool = false;
    String str1 = null;
    String str2 = null;
    String str3 = null;
    if (paramAbstractContentProvider != null) {
      try {
        contentProviderConfig = paramAbstractContentProvider.getConfig();
      } catch (ContentProviderException contentProviderException) {
        throw new ContentProviderSyncException(contentProviderException);
      } 
      try {
        str1 = contentProviderConfig.getManufacturersCCPManufacturerName();
        if (str1 != null) {
          str2 = contentProviderConfig.getManufacturersCCPManufacturerID();
          str3 = contentProviderConfig.getManufacturersCCPID();
          contentProviderManufacturer = paramAbstractContentProvider.getManufacturer(paramString1, paramString2);
        } else {
          bool = true;
        } 
      } catch (ContentProviderException contentProviderException) {
        if (contentProviderException instanceof com.mentor.dms.contentprovider.ContentProviderNotSupportedException) {
          bool = true;
        } else if (!contentProviderException.getMessage().contains("No Results Found")) {
          throw new ContentProviderSyncException(contentProviderException);
        } 
      } 
      if (str1 != null && contentProviderManufacturer == null) {
        String str = "Manufacturer '" + paramString2 + "' ";
        if (paramString1 != null)
          str = str + "(" + str + ") "; 
        str = str + "was not found in '" + str + "'.\n\nWould you like to continue with Manufacturer creation?";
        int i = JOptionPane.showConfirmDialog(ContentProviderGlobal.getRootFrame(), str, "Create Manufacturer", 0);
        if (i == 1)
          return null; 
        bool = true;
      } 
    } else {
      bool = true;
    } 
    String str4 = null;
    if ((paramAbstractContentProvider != null && str2 == null) || bool) {
      int i = JOptionPane.showConfirmDialog(ContentProviderGlobal.getRootFrame(), "Use manufacturer name as the EDM Library Manufacturer ID?", "Create Manufacturer", 0);
      if (i == 0) {
        str4 = paramString2;
      } else {
        str4 = JOptionPane.showInputDialog(ContentProviderGlobal.getRootFrame(), "Please enter Manufacturer ID:", "Create Manufacturer", 3);
        if (str4 == null)
          return null; 
      } 
    } else if (str2 != null) {
      ContentProviderManufacturerProperty contentProviderManufacturerProperty = contentProviderManufacturer.getPropertyByName(str2);
      if (contentProviderManufacturerProperty != null)
        str4 = contentProviderManufacturerProperty.getValue(); 
      if (str4 == null || str4.isBlank()) {
        JOptionPane.showMessageDialog(ContentProviderGlobal.getRootFrame(), "Property '" + str2 + "' configured to map to Manufacturer ID was not returned by Content Provider.'");
        return null;
      } 
    } 
    try {
      oIObject = paramOIObjectManager.createObject("Manufacturer");
      oIObject.set("ManufacturerId", str4);
      if (bool) {
        oIObject.set("ManufacturerName", paramString2);
      } else {
        ContentProviderManufacturerProperty contentProviderManufacturerProperty = contentProviderManufacturer.getPropertyByName(str1);
        if (contentProviderManufacturerProperty == null || (((contentProviderManufacturerProperty.getValue() == null) ? 1 : 0) | contentProviderManufacturerProperty.getValue().isBlank()) != 0) {
          JOptionPane.showMessageDialog(ContentProviderGlobal.getRootFrame(), "Manufacturer Name not returned from Content Provider.");
          return null;
        } 
        setManufacturerProperty(oIObject, "ManufacturerName", contentProviderManufacturerProperty.getValue());
        if (str3 != null) {
          ContentProviderManufacturerProperty contentProviderManufacturerProperty1 = contentProviderManufacturer.getPropertyByName(str3);
          if (contentProviderManufacturerProperty1 != null && contentProviderManufacturerProperty1.getValue() != null && !contentProviderManufacturerProperty1.getValue().isBlank())
            setManufacturerProperty(oIObject, "ECMfgID", contentProviderManufacturerProperty1.getValue()); 
        } 
        for (ConfigXMLManufacturerPropertyMap configXMLManufacturerPropertyMap : contentProviderConfig.getManufacturersPropertyMaps())
          setManufacturerProperty(oIObject, contentProviderManufacturer, configXMLManufacturerPropertyMap); 
      } 
      paramOIObjectManager.makePermanent(oIObject);
    } catch (Exception exception) {
      JOptionPane.showMessageDialog(ContentProviderGlobal.getRootFrame(), "Unable to create Manufacturer: " + exception.getMessage());
      return null;
    } finally {
      if (oIObject != null)
        try {
          paramOIObjectManager.evict(oIObject);
        } catch (OIException oIException) {} 
    } 
    return oIObject;
  }
  
  public static boolean setManufacturerProperty(OIObject paramOIObject, ContentProviderManufacturer paramContentProviderManufacturer, ConfigXMLManufacturerPropertyMap paramConfigXMLManufacturerPropertyMap) throws OIException {
    ContentProviderManufacturerProperty contentProviderManufacturerProperty = paramContentProviderManufacturer.getPropertyByName(paramConfigXMLManufacturerPropertyMap.getCcpId());
    return (contentProviderManufacturerProperty != null) ? setManufacturerProperty(paramOIObject, paramConfigXMLManufacturerPropertyMap.getDmn(), contentProviderManufacturerProperty.getValue()) : false;
  }
  
  public static boolean setManufacturerProperty(OIObject paramOIObject, String paramString1, String paramString2) throws OIException {
    if (paramString2 == null || paramString2.isBlank())
      return false; 
    OIStringField oIStringField = (OIStringField)paramOIObject.getOIClass().getField(OIStringField.class, paramString1);
    int i = oIStringField.getMaximalLength();
    String str = paramOIObject.getString(paramString1);
    if (paramString2.length() > i) {
      paramString2 = paramString2.substring(0, i);
      logger.warn(oIStringField.getLabel() + " has maximum length of " + oIStringField.getLabel() + ".  Value will be truncated to '" + i + "'.");
    } 
    if (!paramString2.equals(str)) {
      logger.info("Setting " + paramString1 + " = '" + paramString2 + "'.");
      paramOIObject.set(paramString1, paramString2);
      return true;
    } 
    return false;
  }
  
  public static HashMap<String, String> getProviderIdMapForMPN(String paramString, OIObject paramOIObject) throws ContentProviderSyncException {
    HashMap<Object, Object> hashMap = new HashMap<>();
    try {
      if (paramOIObject.get("ExternalContentId") == null)
        throw new ContentProviderSyncException("Manufacturer Part is not associated with External Content."); 
      OIQuery oIQuery = paramOIObject.getObjectManager().createQuery("ExternalContent", true);
      oIQuery.addRestriction("ExternalContentId", OIHelper.escapeQueryRestriction(paramOIObject.getString("ManufacturerpartId")));
      oIQuery.addRestriction("ECProviderReferences.ECProviderReferenceID", paramString);
      oIQuery.addColumn("ECProviderReferences.ECProviderReferenceKey");
      oIQuery.addColumn("ECProviderReferences.ECProviderReferenceValue");
      OICursor oICursor = oIQuery.execute();
      while (oICursor.next())
        hashMap.put(oICursor.getString("ECProviderReferenceKey"), oICursor.getString("ECProviderReferenceValue")); 
      if (hashMap.isEmpty())
        throw new ContentProviderSyncException("This Manufacturer Part does not contain a reference to the given Content Provider."); 
    } catch (OIException oIException) {
      throw new ContentProviderSyncException("Problem querying External Content object: " + oIException.getMessage());
    } 
    return (HashMap)hashMap;
  }
  
  public static HashMap<String, String> getProviderIdMapForEC(String paramString, OIObject paramOIObject) throws ContentProviderSyncException {
    HashMap<Object, Object> hashMap = new HashMap<>();
    try {
      OIObjectSet oIObjectSet = paramOIObject.getSet("ECProviderReferences");
      for (OIObject oIObject : oIObjectSet) {
        if (oIObject.getString("ECProviderReferenceID").equals(paramString))
          hashMap.put(oIObject.getString("ECProviderReferenceKey"), oIObject.getString("ECProviderReferenceValue")); 
      } 
    } catch (OIException oIException) {
      throw new ContentProviderSyncException("Problem querying External Content object: " + oIException.getMessage());
    } 
    return (HashMap)hashMap;
  }
  
  public static OIObject createComponentFromMPN(OIObject paramOIObject, OverwriteEnum paramOverwriteEnum) throws ContentProviderException {
    return createComponentFromMPN(paramOIObject, paramOverwriteEnum, null);
  }
  
  public static OIObject createComponentFromMPN(OIObject paramOIObject, OverwriteEnum paramOverwriteEnum, String paramString) throws ContentProviderException {
    OIObjectManager oIObjectManager = paramOIObject.getObjectManager();
    OIObject oIObject = null;
    if (paramString == null)
      try {
        IContentProviderApplication iContentProviderApplication = ContentProviderFactory.getInstance().getAppConfig().getApplicationImpl();
        if (iContentProviderApplication instanceof IContentProviderPartNumberGenerator)
          paramString = ((IContentProviderPartNumberGenerator)iContentProviderApplication).getNewPartNumber(ContentProviderFactory.getInstance().getAppConfig().getAppConfigParams(), paramOIObject); 
      } catch (ContentProviderException contentProviderException) {
        JOptionPane.showMessageDialog(ContentProviderGlobal.getRootFrame(), "Unable to generate new Component part number : " + contentProviderException.getMessage());
        return null;
      }  
    if (paramString != null && !paramString.trim().isEmpty())
      try {
        OIQuery oIQuery = oIObjectManager.createQuery("Component", true);
        oIQuery.addRestriction("PartNumber", paramString);
        oIQuery.addColumn("PartNumber");
        OICursor oICursor = oIQuery.execute();
        if (oICursor.next())
          if (paramOverwriteEnum.equals(OverwriteEnum.YES)) {
            oIObject = oICursor.getProxyObject().getObject();
          } else if (paramOverwriteEnum.equals(OverwriteEnum.ASK)) {
            oIObject = oICursor.getProxyObject().getObject();
            int i = JOptionPane.showConfirmDialog(ContentProviderGlobal.getRootFrame(), "Part number '" + paramString + "' already exists in EDM Library.  Update existing Component?", "Create Component", 0);
            if (i == 1)
              return oIObject; 
          } else {
            throw new ContentProviderSyncAlreadyExistsException("Component '" + paramString + "' already exists in EDM Library.");
          }  
      } catch (OIException oIException) {
        JOptionPane.showMessageDialog(ContentProviderGlobal.getRootFrame(), "Problem while querying for existing Component: '" + paramString + "'.");
      }  
    if (oIObject == null) {
      AbstractContentProvider abstractContentProvider = getDefaultContentProvider();
      String str1 = paramOIObject.getOIClass().getName();
      String str2 = paramOIObject.getOIClass().getLabel();
      String str3 = null;
      ArrayList<String> arrayList = new ArrayList();
      for (ContentProviderConfigComponentCatalog contentProviderConfigComponentCatalog : abstractContentProvider.getConfig().getComponentCatalogs()) {
        for (ContentProviderConfigManufacturerPartMap contentProviderConfigManufacturerPartMap : contentProviderConfigComponentCatalog.getManufacturerPartMaps()) {
          if (contentProviderConfigManufacturerPartMap.getClassDMN().equals(str1))
            arrayList.add(contentProviderConfigComponentCatalog.getClassDMN()); 
        } 
      } 
      if (arrayList.size() == 0) {
        if (ContentProviderGlobal.isInteractiveExecMode()) {
          OIClass oIClass;
          CatalogSelectionDialog catalogSelectionDialog = new CatalogSelectionDialog(oIObjectManager, ContentProviderGlobal.getRootFrame(), "Select target Component catalog");
          try {
            oIClass = catalogSelectionDialog.showDialog(new ImageIcon(CatalogSelectionDialog.class.getResource("images/component.png")), "RootComponent");
          } catch (OIException oIException) {
            throw new ContentProviderSyncException("Unable to display Catalog Selection dialog: " + oIException.getMessage());
          } 
          if (oIClass == null)
            return null; 
          str3 = oIClass.getName();
        } else {
          throw new ContentProviderSyncException("Manufacturer Part catalog '" + str2 + " (" + str1 + ")' was not mapped to a Component catalog in the configuration.");
        } 
      } else if (arrayList.size() == 1) {
        str3 = arrayList.iterator().next();
      } else if (ContentProviderGlobal.isInteractiveExecMode()) {
        OIClass oIClass;
        CatalogSelectionDialog catalogSelectionDialog = new CatalogSelectionDialog(oIObjectManager, ContentProviderGlobal.getRootFrame(), "Select target Component catalog");
        try {
          oIClass = catalogSelectionDialog.showDialog(new ImageIcon(CatalogSelectionDialog.class.getResource("images/component.png")), "RootComponent", arrayList);
        } catch (OIException oIException) {
          throw new ContentProviderSyncException("Unable to display Catalog Selection dialog: " + oIException.getMessage());
        } 
        if (oIClass == null)
          return null; 
        str3 = oIClass.getName();
      } else {
        throw new ContentProviderSyncException("Manufacturer Part catalog '" + str2 + " (" + str1 + ")' maps to more than one Component catalog in the configuration.");
      } 
      try {
        oIObject = oIObjectManager.createObject(str3);
      } catch (OIException oIException) {
        JOptionPane.showMessageDialog(ContentProviderGlobal.getRootFrame(), "Unable to create Component : " + oIException.getMessage());
        return null;
      } 
    } 
    if (paramString != null)
      try {
        oIObject.set("PartNumber", paramString);
      } catch (OIException oIException) {
        try {
          oIObjectManager.evict(oIObject);
        } catch (OIException oIException1) {}
        JOptionPane.showMessageDialog(ContentProviderGlobal.getRootFrame(), "Unable to set Component part number to '" + paramString + "': " + oIException.getMessage());
      }  
    try {
      OIObjectSet oIObjectSet = oIObject.getSet("ApprovedManufacturerList");
      oIObjectSet.clear();
      OIObject oIObject1 = oIObjectSet.createLine();
      oIObject1.set("MfgPartNumber", paramOIObject);
    } catch (OIException oIException) {
      try {
        oIObjectManager.evict(oIObject);
      } catch (OIException oIException1) {}
      JOptionPane.showMessageDialog(ContentProviderGlobal.getRootFrame(), "Unable to add Manufacturer Part to Component's Approved Manufacturer Parts list : " + oIException.getMessage());
      return null;
    } 
    return oIObject;
  }
  
  public static Date getLastSyncDate(OIObjectManager paramOIObjectManager, AbstractContentProvider paramAbstractContentProvider) {
    Date date = new Date(0L);
    try {
      OIQuery oIQuery = paramOIObjectManager.createQuery("ToolsContentProviderSync", true);
      oIQuery.addRestriction("CallName", "EC_SYNC_" + paramAbstractContentProvider.getId());
      oIQuery.addColumn("ECSyncDate");
      OICursor oICursor = oIQuery.execute();
      if (oICursor.next()) {
        Date date1 = oICursor.getDate("ECSyncDate");
        if (date1 != null)
          date = date1; 
      } 
    } catch (OIException oIException) {
      logger.info("No last synchronization date found for Content Provider '" + paramAbstractContentProvider.getName() + "'...");
    } 
    return date;
  }
  
  public static void setLastSyncDate(OIObjectManager paramOIObjectManager, AbstractContentProvider paramAbstractContentProvider, Date paramDate) throws ContentProviderSyncException {
    try {
      OIObject oIObject;
      OIQuery oIQuery = paramOIObjectManager.createQuery("ToolsContentProviderSync", true);
      oIQuery.addRestriction("CallName", "EC_SYNC_" + paramAbstractContentProvider.getId());
      oIQuery.addColumn("ToolBoxId");
      oIQuery.addColumn("ECSyncDate");
      OICursor oICursor = oIQuery.execute();
      if (oICursor.next()) {
        oIObject = oICursor.getProxyObject().getObject();
      } else {
        oIObject = paramOIObjectManager.createObject("ToolsContentProviderSync");
        oIObject.set("CallName", "EC_SYNC_" + paramAbstractContentProvider.getId());
      } 
      oIObject.set("ECSyncDate", paramDate);
      paramOIObjectManager.makePermanent(oIObject);
      paramOIObjectManager.evict(oIObject);
    } catch (OIException oIException) {
      logger.warn("Unable to save last synchronization date for Content Provider '" + paramAbstractContentProvider.getName() + "'...");
    } 
  }
  
  public static void syncSubscriptions(OIObjectManager paramOIObjectManager) throws ContentProviderSyncException {
    logger.info("Synchronizing subscriptions of Manufacturer Parts with Content Providers...");
    try {
      for (AbstractContentProvider abstractContentProvider : ContentProviderFactory.getInstance().getRegisteredContentProviders()) {
        if (!abstractContentProvider.hasRole(ContentProviderRegistryEntry.ContentProviderRole.MANUFACTURER_PART_SYNCHRONIZATION))
          continue; 
        String str = abstractContentProvider.getConfigurationParameter("DISABLE_SUBSCRIPTION");
        if (str != null && str.equalsIgnoreCase("TRUE")) {
          logger.info("Adding/removing subscriptions have been disabled for Content Provider '" + abstractContentProvider.getName() + ".  Skipping...");
          continue;
        } 
        logger.info("Querying Content Provider '" + abstractContentProvider.getName() + "' for subscription parts...");
        try {
          HashMap<Object, Object> hashMap1 = new HashMap<>();
          for (Map map : abstractContentProvider.getSubscriptions()) {
            String str1 = "";
            for (ContentProviderConfigProperty contentProviderConfigProperty : abstractContentProvider.getConfig().getIdProperties()) {
              String str2 = (String)map.get(contentProviderConfigProperty.getContentProviderId());
              if (str2 != null)
                str1 = str1 + str1 + "=" + contentProviderConfigProperty.getContentProviderId() + ";"; 
            } 
            hashMap1.put(str1, map);
          } 
          logger.info("Querying EDM Library for External Content references to '" + abstractContentProvider.getName() + "'...");
          OIQuery oIQuery = paramOIObjectManager.createQuery("ExternalContent", true);
          oIQuery.addRestriction("ECProviderReferences.ECProviderReferenceID", abstractContentProvider.getId());
          oIQuery.addColumn("ExternalContentId");
          oIQuery.addColumn("ECProviderReferences.ECProviderReferenceKey");
          oIQuery.addColumn("ECProviderReferences.ECProviderReferenceValue");
          if (abstractContentProvider.isClientSubscriptionManagementRequired()) {
            oIQuery.addColumn("ECProviderSubscribeList.ECProviderSubscribeID");
            oIQuery.addColumn("ECProviderSubscribeList.ECProviderSubscribeValue");
          } 
          HashMap<Object, Object> hashMap2 = new HashMap<>();
          OICursor oICursor = oIQuery.execute();
          while (oICursor.next()) {
            String str1 = oICursor.getString("ExternalContentId");
            HashMap<Object, Object> hashMap = (HashMap)hashMap2.get(str1);
            if (hashMap == null) {
              hashMap = new HashMap<>();
              hashMap2.put(str1, hashMap);
            } 
            hashMap.put(oICursor.getString("ECProviderReferenceKey"), oICursor.getString("ECProviderReferenceValue"));
            if (abstractContentProvider.isClientSubscriptionManagementRequired() && oICursor.getString("ECProviderSubscribeID").equals(abstractContentProvider.getId()) && !oICursor.getString("ECProviderSubscribeValue").trim().isEmpty())
              hashMap.put("SubscriptionID", oICursor.getString("ECProviderSubscribeValue")); 
          } 
          oICursor.close();
          HashMap<Object, Object> hashMap3 = new HashMap<>();
          for (HashMap hashMap : hashMap2.values()) {
            String str1 = "";
            for (ContentProviderConfigProperty contentProviderConfigProperty : abstractContentProvider.getConfig().getIdProperties()) {
              String str2 = (String)hashMap.get(contentProviderConfigProperty.getContentProviderId());
              if (str2 != null)
                str1 = str1 + str1 + "=" + contentProviderConfigProperty.getContentProviderId() + ";"; 
            } 
            hashMap3.put(str1, hashMap);
          } 
          for (Map.Entry<Object, Object> entry : hashMap3.entrySet()) {
            if (!hashMap1.containsKey(entry.getKey())) {
              OIObject oIObject = null;
              try {
                abstractContentProvider.addSubscription((Map)entry.getValue());
                if (abstractContentProvider.isClientSubscriptionManagementRequired()) {
                  String str1 = (String)((Map)entry.getValue()).get("SubscriptionID");
                  String str2 = abstractContentProvider.addSubscription((Map)entry.getValue());
                  if ((str1 == null) ? (str2 == null) : str1.equals(str2)) {
                    oIObject = paramOIObjectManager.getObjectByID((String)entry.getKey(), "ExternalContent", true);
                    setExternalContentSubscription(abstractContentProvider, oIObject, str2);
                    if (bCommitMode)
                      paramOIObjectManager.makePermanent(oIObject); 
                  } 
                } 
              } catch (ContentProviderException contentProviderException) {
                logger.warn(contentProviderException.getMessage());
              } finally {
                if (bCommitMode && oIObject != null)
                  paramOIObjectManager.evict(oIObject); 
              } 
            } 
          } 
          for (Map.Entry<Object, Object> entry : hashMap1.entrySet()) {
            if (!hashMap3.containsKey(entry.getKey()))
              try {
                abstractContentProvider.deleteSubscription((Map)entry.getValue());
              } catch (ContentProviderException contentProviderException) {
                logger.warn(contentProviderException.getMessage());
              }  
          } 
        } catch (Exception exception) {
          logger.error("Error while sychronization subscriptions: " + exception.getMessage());
        } 
      } 
    } catch (Exception exception) {
      throw new ContentProviderSyncException(exception);
    } 
  }
  
  private static AbstractContentProvider getDefaultContentProvider() throws ContentProviderException {
    AbstractContentProvider abstractContentProvider = null;
    for (AbstractContentProvider abstractContentProvider1 : ContentProviderFactory.getInstance().getRegisteredContentProviders()) {
      if (abstractContentProvider1.hasRole(ContentProviderRegistryEntry.ContentProviderRole.COMPONENT_SYNCHRONIZATION)) {
        abstractContentProvider = abstractContentProvider1;
        break;
      } 
    } 
    if (abstractContentProvider == null)
      throw new ContentProviderSyncException("There are no Content Providers that have a role in Component Synchronization."); 
    return abstractContentProvider;
  }
  
  private static class ECData {
    String ecId;
    
    HashMap<String, String> providerRefMap = new HashMap<>();
    
    Date lastSyncDate;
    
    String cacheId = "";
  }
  
  public enum OverwriteEnum {
    YES, NO, ASK;
  }
  
  public enum SyncActionEnum {
    RECONCILE, IGNORE_ONCE, IGNORE_ALWAYS;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\sync\ContentProviderSync.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */