package com.mentor.dms.contentprovider.utils;

import com.mentor.datafusion.oi.OICursor;
import com.mentor.datafusion.oi.OIException;
import com.mentor.datafusion.oi.OIObjectManager;
import com.mentor.datafusion.oi.OIObjectManagerFactory;
import com.mentor.datafusion.oi.OIQuery;
import com.mentor.datafusion.oi.login.OIAuthenticate;
import com.mentor.datafusion.oi.login.OIAuthenticateFactory;
import com.mentor.datafusion.oi.model.OIClass;
import com.mentor.datafusion.oi.type.OIObject;
import com.mentor.datafusion.utils.logger.MGLogger;
import com.mentor.dms.contentprovider.AbstractContentProvider;
import com.mentor.dms.contentprovider.ContentProviderException;
import com.mentor.dms.contentprovider.ContentProviderFactory;
import com.mentor.dms.contentprovider.ContentProviderGlobal;
import com.mentor.dms.contentprovider.ContentProviderRegistryEntry;
import com.mentor.dms.contentprovider.IContentProviderResultRecord;
import com.mentor.dms.contentprovider.config.ContentProviderConfig;
import com.mentor.dms.contentprovider.config.ContentProviderConfigDMSCatalog;
import com.mentor.dms.contentprovider.config.ContentProviderConfigPartClass;
import com.mentor.dms.contentprovider.config.ContentProviderConfigProperty;
import com.mentor.dms.contentprovider.plugin.searchui.ContentProviderSelectDlg;
import com.mentor.dms.contentprovider.sync.ContentProviderSync;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class SearchAndAssignApp {
  static MGLogger logger = MGLogger.getLogger(SearchAndAssignApp.class);
  
  static String dmsConfigName = null;
  
  static String ccpId = null;
  
  static boolean bKeepRegistry = false;
  
  static boolean bNoRegistry = false;
  
  static String mpnCatalog = null;
  
  static OIObjectManagerFactory omf = null;
  
  static OIObjectManager om = null;
  
  static AbstractContentProvider ccp = null;
  
  static ContentProviderConfig ccpCfg = null;
  
  static ContentProviderConfigPartClass ccpConfigPartClass = null;
  
  static ContentProviderConfigProperty ccpConfigPartNoProp = null;
  
  static ContentProviderConfigProperty ccpConfigMfgProp = null;
  
  static HashMap<String, OIClass> oiClassMap = new HashMap<>();
  
  static int partCount = 1;
  
  public static void main(String[] paramArrayOfString) {
    System.out.println("#");
    System.out.println("#         Search And Assign - Version 1.16.3");
    System.out.println("#");
    System.out.println("#                Copyright Siemens 2022");
    System.out.println("#");
    System.out.println("#                      All Rights Reserved.");
    System.out.println("#");
    System.out.println("#        THIS WORK CONTAINS TRADE SECRET AND PROPRIETARY");
    System.out.println("#        INFORMATION WHICH IS THE PROPERTY OF SIEMENS ");
    System.out.println("#        OR ITS LICENSORS AND IS SUBJECT TO LICENSE TERMS.");
    System.out.println("#");
    Options options = new Options();
    options.addOption("help", false, "help");
    options.addOption("loginconfig", true, "EDM Library auto-login configuration");
    options.addOption("ccpid", true, "Content Provider ID");
    options.addOption("keepregistry", false, "Do not remove existing error registry entries");
    options.addOption("noregistry", false, "Do not use error registry");
    options.addOption("mpncatalog", true, "Domain name of MPN catalog to search for parts");
    BasicParser basicParser = new BasicParser();
    CommandLine commandLine = null;
    try {
      commandLine = basicParser.parse(options, paramArrayOfString);
    } catch (ParseException parseException) {
      System.err.println("Error: " + parseException.getMessage());
      HelpFormatter helpFormatter = new HelpFormatter();
      helpFormatter.printHelp("Search and Assign", options);
      System.exit(1);
    } 
    if (commandLine.hasOption("help")) {
      HelpFormatter helpFormatter = new HelpFormatter();
      helpFormatter.printHelp("Search and Assign", options);
      System.exit(0);
    } 
    if (commandLine.hasOption("loginconfig")) {
      dmsConfigName = commandLine.getOptionValue("loginconfig");
    } else {
      System.err.println("Error: EDM Library auto-login configuration is required.");
      System.exit(1);
    } 
    if (commandLine.hasOption("ccpid"))
      ccpId = commandLine.getOptionValue("ccpid"); 
    bKeepRegistry = commandLine.hasOption("keepregistry");
    bNoRegistry = commandLine.hasOption("noregistry");
    mpnCatalog = commandLine.getOptionValue("mpncatalog");
    Date date = new Date();
    try {
      logger.info("Connecting to EDM Library Server...");
      OIAuthenticate oIAuthenticate = OIAuthenticateFactory.createBatchAuthenticate(dmsConfigName);
      omf = oIAuthenticate.login("Search and Assign Utility");
      om = omf.createObjectManager();
    } catch (OIException oIException) {
      logger.error("Unable to connect to EDM Library Server:  " + oIException.getMessage());
      System.exit(1);
    } 
    try {
      try {
        ContentProviderGlobal.setBatchExecMode();
        logger.info("Registering configured Content Providers...");
        ContentProviderFactory.getInstance().registerContentProviders(om);
        if (ccpId != null) {
          ccp = ContentProviderFactory.getInstance().createContentProvider(ccpId);
        } else {
          ccp = ContentProviderSelectDlg.selectContentProvider(ContentProviderRegistryEntry.ContentProviderRole.MANUFACTURER_PART_CREATION);
        } 
        logger.info("Loading " + ccp.getName() + " Content Provider mapping configuration...");
        ccpCfg = ccp.getConfig();
        ccpConfigPartClass = ccpCfg.getPartClassByContentProviderId("Part");
        ccpConfigPartNoProp = ccpConfigPartClass.getClassPropertyByContentProviderId(ccp.getPartNumberPropID());
        ccpConfigMfgProp = ccpConfigPartClass.getClassPropertyByContentProviderId(ccp.getManufacturerPropID());
      } catch (ContentProviderException contentProviderException) {
        logger.error(contentProviderException.getMessage());
        System.exit(1);
      } 
      if (!bNoRegistry && !bKeepRegistry) {
        logger.info("Clearing the Error Registry...");
        try {
          OIQuery oIQuery = om.createQuery("SearchAndAssignEC", true);
          oIQuery.addColumn("ErrorRegistryID");
          OICursor oICursor1 = oIQuery.execute();
          while (oICursor1.next()) {
            OIObject oIObject1 = oICursor1.getProxyObject().getObject();
            try {
              om.deleteObject(oIObject1);
              om.makePermanent(oIObject1);
            } catch (OIException oIException) {
              logger.warn("Unable to delete Error Registry object '" + oIObject1.getObjectID() + "':  " + oIException.getMessage());
              om.evict(oIObject1);
            } 
          } 
          oICursor1.close();
        } catch (OIException oIException) {
          logger.error("Unable to query Error Registry: " + oIException.getMessage());
          System.exit(1);
        } 
      } 
      OIObject oIObject = null;
      logger.info("Querying EDM Library for candidate Manufacturer Parts ...");
      OICursor oICursor = null;
      try {
        OIQuery oIQuery = null;
        if (mpnCatalog != null) {
          oIQuery = om.createQuery(mpnCatalog, true);
        } else {
          oIQuery = om.createQuery("ManufacturerPart", true);
        } 
        oIQuery.addRestriction("ExternalContentId", "NULL");
        try {
          oIQuery.addRestriction("IgnoreCPSync", "NULL|~Y");
        } catch (Exception exception) {}
        oIQuery.addColumn("ManufacturerpartId");
        oIQuery.addColumn("PartNumber");
        oIQuery.addColumn("ManufacturerName");
        oICursor = oIQuery.execute();
        while (oICursor.next()) {
          String str1 = oICursor.getString("PartNumber");
          String str2 = oICursor.getString("ManufacturerName");
          logger.info("" + partCount++ + " : Processing Manufacturer Part '" + partCount++ + "' by '" + str1 + "'...");
          oIObject = oICursor.getProxyObject().getObject();
          OIObject oIObject1 = null;
          if (!bNoRegistry)
            try {
              oIObject1 = om.createObject("SearchAndAssignEC");
              oIObject1.set("MPNIDRef", oIObject);
              oIObject1.set("ErrorDate", new Date());
              oIObject1.set("ErrorStatus", "Info");
              oIObject1.set("ErrorMessage", "Processing...");
              om.makePermanent(oIObject1);
            } catch (OIException oIException) {
              logger.error("Unable to create Error Registry entry for Part Number '" + str1 + "' by '" + str2 + "'.");
              continue;
            }  
          try {
            if (processPart(oIObject, oIObject1) && !bNoRegistry)
              om.deleteObject(oIObject1); 
          } finally {
            try {
              if (!bNoRegistry)
                om.makePermanent(oIObject1); 
            } catch (OIException oIException) {
              logger.warn(oIException.getMessage());
            } finally {
              if (!bNoRegistry)
                om.evict(oIObject1); 
            } 
          } 
        } 
        oICursor.close();
        if (!bNoRegistry)
          setLastSyncDate(date); 
      } catch (OIException oIException) {
        logger.error("Unable to process Manufacturer Parts in EDM Library:  " + oIException.getMessage());
        System.exit(1);
      } 
    } finally {
      logger.debug("Closing connection to EDM Library Server...");
      if (om != null)
        om.close(); 
      if (omf != null)
        omf.close(); 
    } 
    logger.info("");
    logger.info("Search and Assign completed.");
  }
  
  private static boolean processPart(OIObject paramOIObject1, OIObject paramOIObject2) {
    String str1 = null;
    String str2 = null;
    try {
      str1 = paramOIObject1.getString("PartNumber");
      str2 = paramOIObject1.getString("ManufacturerName");
    } catch (OIException oIException) {
      logError(paramOIObject2, "Unable to read part number and manufacturer name from EDM Manufacturer Part: " + oIException.getMessage());
      return false;
    } 
    IContentProviderResultRecord iContentProviderResultRecord = null;
    byte b = 0;
    while (b <= 3) {
      b++;
      try {
        iContentProviderResultRecord = ccp.searchExactMatch(str1, str2);
        if (iContentProviderResultRecord == null) {
          logError(paramOIObject2, "Unable to find an exact match for Part Number '" + str1 + "' by '" + str2 + "' in " + ccp.getName() + ".");
          return false;
        } 
      } catch (ContentProviderException contentProviderException) {
        if (b <= 3) {
          logger.warn("Error calling '" + ccp.getName() + "' API : " + contentProviderException.getMessage() + ".  Retrying (" + b + ")...");
          try {
            Thread.sleep(3000L);
          } catch (InterruptedException interruptedException) {}
          continue;
        } 
        logError(paramOIObject2, "Unable to perform search in " + ccp.getName() + ": " + contentProviderException.getMessage());
        return false;
      } 
    } 
    logger.info("Assigning External Content...");
    try {
      ContentProviderSync.assignEC2MPN(ccp, paramOIObject1, iContentProviderResultRecord.getIdPropertyMap(ccpCfg));
    } catch (ContentProviderException contentProviderException) {
      logError(paramOIObject2, contentProviderException.getMessage());
      return false;
    } 
    logger.info("Validating Manufacturer Part classification...");
    ContentProviderConfigDMSCatalog contentProviderConfigDMSCatalog = null;
    Collection<ContentProviderConfigDMSCatalog> collection = ccpCfg.getMPNCatalogsByContentProviderId(iContentProviderResultRecord.getPartClassID());
    if (collection == null || collection.isEmpty()) {
      logError(paramOIObject2, "While attempting to validate Manufacturer Part classification:  Part Class Content Provider ID '" + iContentProviderResultRecord.getPartClassID() + "' not found in mapping configuration.");
      return false;
    } 
    contentProviderConfigDMSCatalog = collection.iterator().next();
    String str3 = contentProviderConfigDMSCatalog.getClassDMN();
    String str4 = paramOIObject1.getOIClass().getName();
    if (!str3.equals(str4)) {
      logger.info("Moving Manufacturer Part from '" + str4 + "' to '" + str3 + "'...");
      OIClass oIClass = getOIClass(str3);
      try {
        om.moveInClassHierarchy(paramOIObject1, oIClass);
        om.makePermanent(paramOIObject1);
      } catch (Exception exception) {
        logError(paramOIObject2, "Unable to move Manufacturer Part from '" + str4 + "' to '" + str3 + "': " + exception.getMessage());
        return false;
      } finally {
        try {
          om.evict(paramOIObject1);
        } catch (OIException oIException) {}
      } 
    } 
    return true;
  }
  
  private static OIClass getOIClass(String paramString) {
    OIClass oIClass = oiClassMap.get(paramString);
    if (oIClass == null) {
      oIClass = omf.getClassManager().getOIClass(paramString);
      oiClassMap.put(paramString, oIClass);
    } 
    return oIClass;
  }
  
  private static void logError(OIObject paramOIObject, String paramString) {
    logger.warn(paramString);
    if (!bNoRegistry)
      try {
        paramOIObject.set("ErrorMessage", paramString);
        paramOIObject.set("ErrorStatus", "Warning");
      } catch (OIException oIException) {
        logger.warn(paramString);
      }  
  }
  
  private static void setLastSyncDate(Date paramDate) {
    try {
      OIObject oIObject;
      OIQuery oIQuery = om.createQuery("ToolsContentProviderSync", true);
      oIQuery.addRestriction("CallName", "EC_SYNC_" + ccp.getId());
      oIQuery.addColumn("ToolBoxId");
      oIQuery.addColumn("ECSearchAndAssign");
      OICursor oICursor = oIQuery.execute();
      if (oICursor.next()) {
        oIObject = oICursor.getProxyObject().getObject();
      } else {
        oIObject = om.createObject("ToolsContentProviderSync");
        oIObject.set("CallName", "EC_SYNC_" + ccp.getId());
      } 
      oIObject.set("ECSearchAndAssign", paramDate);
      om.makePermanent(oIObject);
      om.evict(oIObject);
    } catch (OIException oIException) {
      logger.warn("Unable to save last Search And Assign synchronization date for Content Provider '" + ccp.getName() + "'...");
    } 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovide\\utils\SearchAndAssignApp.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */