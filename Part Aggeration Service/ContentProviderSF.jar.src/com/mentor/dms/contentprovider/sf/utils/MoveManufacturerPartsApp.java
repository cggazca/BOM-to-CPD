package com.mentor.dms.contentprovider.sf.utils;

import com.mentor.datafusion.oi.OICursor;
import com.mentor.datafusion.oi.OIObjectManager;
import com.mentor.datafusion.oi.OIObjectManagerFactory;
import com.mentor.datafusion.oi.OIQuery;
import com.mentor.datafusion.oi.login.OIAuthenticate;
import com.mentor.datafusion.oi.login.OIAuthenticateFactory;
import com.mentor.datafusion.oi.model.OIClass;
import com.mentor.datafusion.oi.type.OIObject;
import com.mentor.datafusion.utils.logger.MGLogger;
import com.mentor.dms.contentprovider.core.AbstractContentProvider;
import com.mentor.dms.contentprovider.core.ContentProviderFactory;
import com.mentor.dms.contentprovider.core.ContentProviderGlobal;
import com.mentor.dms.contentprovider.core.config.ContentProviderConfig;
import com.mentor.dms.contentprovider.core.config.ContentProviderConfigContentProviderMap;
import com.mentor.dms.contentprovider.core.config.ContentProviderConfigMPNCatalog;
import com.mentor.dms.contentprovider.core.sync.ContentProviderSync;
import com.mentor.dms.contentprovider.core.utils.validate.CPPartClass;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.StringUtils;

public class MoveManufacturerPartsApp {
  static MGLogger logger = MGLogger.getLogger(MoveManufacturerPartsApp.class);
  
  static OIObjectManagerFactory omf = null;
  
  static OIObjectManager om = null;
  
  static String catalog = null;
  
  static HashMap<String, OIClass> oiClassMap = new HashMap<>();
  
  public static void main(String[] paramArrayOfString) {
    System.out.println("#");
    System.out.println("#    Move Manufacturer Parts App - Version 1.1.1");
    System.out.println("#");
    System.out.println("#                Copyright Siemens 2025");
    System.out.println("#");
    System.out.println("#                      All Rights Reserved.");
    System.out.println("#");
    System.out.println("#        THIS WORK CONTAINS TRADE SECRET AND PROPRIETARY");
    System.out.println("#        INFORMATION WHICH IS THE PROPERTY OF SIEMENS");
    System.out.println("#        OR ITS LICENSORS AND IS SUBJECT TO LICENSE TERMS.");
    System.out.println("#");
    String str = null;
    Options options = new Options();
    options.addOption("help", false, "help");
    options.addOption("loginconfig", true, "EDM Library auto-login configuration");
    options.addOption("catalog", true, "EDM Library catalog group name (label)");
    BasicParser basicParser = new BasicParser();
    CommandLine commandLine = null;
    try {
      commandLine = basicParser.parse(options, paramArrayOfString);
    } catch (ParseException parseException) {
      System.err.println("Error: " + parseException.getMessage());
      HelpFormatter helpFormatter = new HelpFormatter();
      helpFormatter.printHelp("MoveManufacturerParts", options);
      System.exit(1);
    } 
    if (commandLine.hasOption("help")) {
      HelpFormatter helpFormatter = new HelpFormatter();
      helpFormatter.printHelp("MoveManufacturerParts", options);
      System.exit(0);
    } 
    if (commandLine.hasOption("loginconfig")) {
      str = commandLine.getOptionValue("loginconfig");
    } else {
      System.err.println("Error: EDM Library auto-login configuration is required.");
      System.exit(1);
    } 
    catalog = commandLine.getOptionValue("catalog");
    try {
      logger.info("Connecting to EDM Library...");
      OIAuthenticate oIAuthenticate = OIAuthenticateFactory.createBatchAuthenticate(str);
      omf = oIAuthenticate.login("Move Manufacturer Parts Utilty");
      logger.info("Connected");
      om = omf.createObjectManager();
      moveManufacturerParts();
      logger.info("");
      logger.info("Complete.");
    } catch (Exception exception) {
      logger.error(exception.getMessage(), exception);
    } finally {
      if (omf != null)
        omf.close(); 
      if (om != null)
        om.close(); 
    } 
  }
  
  private static void moveManufacturerParts() throws Exception {
    HashMap<Object, Object> hashMap = new HashMap<>();
    ContentProviderGlobal.setBatchExecMode();
    ContentProviderGlobal.setOIObjectManager(om);
    ContentProviderFactory.getInstance().registerContentProviders(om);
    AbstractContentProvider abstractContentProvider = ContentProviderFactory.getInstance().createContentProvider("SF");
    ContentProviderConfig contentProviderConfig = abstractContentProvider.getConfig();
    HashMap<String, String> hashMap1 = getTaxonomyPathMap(abstractContentProvider);
    logger.info("Reading External Content...");
    OIQuery oIQuery = om.createQuery("ExternalContent", true);
    oIQuery.addColumn("ExternalContentId");
    OICursor oICursor = oIQuery.execute();
    while (oICursor.next())
      hashMap.put(oICursor.getString("ExternalContentId"), new HashMap<>()); 
    oICursor.close();
    oIQuery = om.createQuery("ExternalContent", true);
    oIQuery.addRestriction("ECPropSyncList.ECPropID", "3982bc42");
    oIQuery.addColumn("ExternalContentId");
    oIQuery.addColumn("ECPropSyncList.ECPropID");
    oIQuery.addColumn("ECPropSyncList.ECPropValue");
    oICursor = oIQuery.execute();
    while (oICursor.next()) {
      HashMap<String, String> hashMap2 = (HashMap)hashMap.get(oICursor.getString("ExternalContentId"));
      hashMap2.put(oICursor.getString("ECPropID"), oICursor.getString("ECPropValue"));
    } 
    oICursor.close();
    oIQuery = om.createQuery("ManufacturerPart", true);
    if (catalog != null) {
      logger.info("Adding restriction to catalog group '" + catalog + "'.");
      oIQuery.addRestriction("CatalogGroup", catalog);
    } 
    oIQuery.addRestriction("ExternalContentId", "~NULL");
    oIQuery.addColumn("ManufacturerpartId");
    oIQuery.addColumn("ExternalContentId");
    oIQuery.addColumn("PartNumber");
    oIQuery.addColumn("ManufacturerName");
    oICursor = oIQuery.execute();
    while (oICursor.next()) {
      logger.info("Processing Manufacturer Part '" + oICursor.getString("PartNumber") + "' from '" + oICursor.getString("ManufacturerName") + "':");
      HashMap hashMap2 = (HashMap)hashMap.get(oICursor.getStringified("ExternalContentId"));
      if (hashMap2 == null) {
        logger.error("Manufacturer Part references non-existent External Content object '" + oICursor.getStringified("ExternalContentId") + "'");
        continue;
      } 
      String str1 = (String)hashMap2.get("3982bc42");
      String str2 = hashMap1.get(str1);
      if (str2 == null)
        str2 = "Unknown"; 
      if (str1 == null) {
        logger.error("External Content object '" + oICursor.getStringified("ExternalContentId") + "' does not contain a synchronized 'PartClassID' property.");
        continue;
      } 
      Collection<ContentProviderConfigMPNCatalog> collection = contentProviderConfig.getMPNCatalogsByContentProviderId(str1);
      if (collection == null || collection.isEmpty()) {
        logger.warn("Part Class '" + str1 + " (" + str2 + ")' not found in mapping configuration.");
        continue;
      } 
      ContentProviderConfigMPNCatalog contentProviderConfigMPNCatalog = collection.iterator().next();
      ContentProviderConfigContentProviderMap contentProviderConfigContentProviderMap = contentProviderConfigMPNCatalog.getContentProviderMaps().iterator().next();
      if (contentProviderConfigContentProviderMap == null) {
        logger.error("MPN Catalog '" + contentProviderConfigMPNCatalog.getClassDMN() + "' does not contain Content Provider mappings.");
        continue;
      } 
      String str3 = contentProviderConfigMPNCatalog.getClassDMN();
      OIObject oIObject = oICursor.getProxyObject().getObject();
      String str4 = oIObject.getOIClass().getName();
      if (!str3.equals(str4)) {
        OIClass oIClass1 = getOIClass(str4);
        OIClass oIClass2 = getOIClass(str3);
        logger.info("Moving Manufacturer Part from '" + oIClass1.getLabel() + " (" + str4 + ")' to '" + oIClass2.getLabel() + " (" + str3 + ")'...");
        try {
          om.moveInClassHierarchy(oIObject, oIClass2);
          om.makePermanent(oIObject);
        } catch (Exception exception) {
          logger.error("Unable to move Manufacturer Part: " + exception.getMessage());
          continue;
        } finally {
          om.evict(oIObject);
        } 
        logger.info("Reloading External Content...");
        OIObject oIObject1 = null;
        try {
          oIObject1 = oIObject.getObject("ExternalContentId");
          ContentProviderSync.syncExernalContentPartRecordToDMS(oIObject, oIObject1, abstractContentProvider, new Date(), true);
          ContentProviderSync.compareAndReconcile(oIObject, oIObject1, true, null, null);
        } catch (Exception exception) {
          logger.error("Unable to reload External Content: " + exception.getMessage());
        } finally {
          om.evict(oIObject);
          if (oIObject1 != null)
            om.evict(oIObject1); 
        } 
      } 
    } 
    oICursor.close();
  }
  
  private static HashMap<String, String> getTaxonomyPathMap(AbstractContentProvider paramAbstractContentProvider) throws Exception {
    HashMap<Object, Object> hashMap = new HashMap<>();
    List list = paramAbstractContentProvider.getPartClassInfo();
    for (CPPartClass cPPartClass : list) {
      if (StringUtils.isEmpty(cPPartClass.getParentID())) {
        hashMap.put(cPPartClass.getId(), cPPartClass.getLabel());
        continue;
      } 
      String str = (String)hashMap.get(cPPartClass.getParentID());
      hashMap.put(cPPartClass.getId(), str + "-" + str);
    } 
    return (HashMap)hashMap;
  }
  
  private static OIClass getOIClass(String paramString) {
    OIClass oIClass = oiClassMap.get(paramString);
    if (oIClass == null) {
      oIClass = omf.getClassManager().getOIClass(paramString);
      oiClassMap.put(paramString, oIClass);
    } 
    return oIClass;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\s\\utils\MoveManufacturerPartsApp.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */