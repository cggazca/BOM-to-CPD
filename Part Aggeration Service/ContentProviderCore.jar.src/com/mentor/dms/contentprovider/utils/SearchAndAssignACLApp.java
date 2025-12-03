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
import com.mentor.dms.contentprovider.ContentProviderSubscribedAML;
import com.mentor.dms.contentprovider.ContentProviderSubscribedComponent;
import com.mentor.dms.contentprovider.IContentProviderResultRecord;
import com.mentor.dms.contentprovider.config.ContentProviderConfig;
import com.mentor.dms.contentprovider.config.ContentProviderConfigDMSCatalog;
import com.mentor.dms.contentprovider.config.ContentProviderConfigException;
import com.mentor.dms.contentprovider.config.ContentProviderConfigProperty;
import com.mentor.dms.contentprovider.plugin.searchui.ContentProviderSelectDlg;
import com.mentor.dms.contentprovider.sync.ContentProviderSync;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class SearchAndAssignACLApp {
  static MGLogger logger = MGLogger.getLogger(SearchAndAssignACLApp.class);
  
  static String dmsConfigName = null;
  
  static String ccpId = null;
  
  static String compCatalog = null;
  
  static String compRestrictions = null;
  
  static String amlRestrictions = null;
  
  static String mpnRestrictions = null;
  
  static boolean bIgnoreChildComps = false;
  
  static boolean bDebug = false;
  
  static OIObjectManagerFactory omf = null;
  
  static OIObjectManager om = null;
  
  static AbstractContentProvider ccp = null;
  
  static ContentProviderConfig ccpCfg = null;
  
  static HashMap<String, OIClass> oiClassMap = new HashMap<>();
  
  static int partCount = 1;
  
  public static void main(String[] paramArrayOfString) {
    System.out.println("#");
    System.out.println("#        Search And Assign (ACL) - Version 1.16.3");
    System.out.println("#");
    System.out.println("#                Copyright Siemens 2022");
    System.out.println("#");
    System.out.println("#                  All Rights Reserved.");
    System.out.println("#");
    System.out.println("#        THIS WORK CONTAINS TRADE SECRET AND PROPRIETARY");
    System.out.println("#        INFORMATION WHICH IS THE PROPERTY OF SIEMENS ");
    System.out.println("#        OR ITS LICENSORS AND IS SUBJECT TO LICENSE TERMS.");
    System.out.println("#");
    Options options = new Options();
    options.addOption("help", false, "help");
    options.addOption("loginconfig", true, "EDM Library auto-login configuration");
    options.addOption("ccpid", true, "Content Provider ID");
    options.addOption("compcatalog", true, "Domain name of Component catalog to search for parts");
    options.addOption("comprestrictions", true, "Comma-separated list of \"<dmn>=<restriction>\" Component search restrictions");
    options.addOption("amlrestrictions", true, "Comma-separated list of \"<dmn>=<restriction>\" Component AML search restrictions");
    options.addOption("mpnrestrictions", true, "Comma-separated list of \"<dmn>=<restriction>\" Manufacturer Part search restrictions");
    options.addOption("ignorechildcomps", false, "Ignore child components (piped part numbers)");
    options.addOption("debug", false, "Debug mode (Assignments not committed to EDM Library.");
    BasicParser basicParser = new BasicParser();
    CommandLine commandLine = null;
    try {
      commandLine = basicParser.parse(options, paramArrayOfString);
    } catch (ParseException parseException) {
      System.err.println("Error: " + parseException.getMessage());
      HelpFormatter helpFormatter = new HelpFormatter();
      helpFormatter.printHelp("Search and Assign (ACL)", options);
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
    ccpId = commandLine.getOptionValue("ccpid");
    compCatalog = commandLine.getOptionValue("compcatalog");
    compRestrictions = commandLine.getOptionValue("comprestrictions");
    amlRestrictions = commandLine.getOptionValue("amlrestrictions");
    mpnRestrictions = commandLine.getOptionValue("mpnrestrictions");
    bIgnoreChildComps = commandLine.hasOption("ignorechildcomps");
    bDebug = commandLine.hasOption("debug");
    try {
      logger.info("Connecting to EDM Library Server...");
      OIAuthenticate oIAuthenticate = OIAuthenticateFactory.createBatchAuthenticate(dmsConfigName);
      omf = oIAuthenticate.login("Search and Assign (ACL) Utility");
      om = omf.createObjectManager();
    } catch (OIException oIException) {
      logger.error("Error:  Unable to connect to EDM Library Server:  " + oIException.getMessage());
      System.exit(1);
    } 
    try {
      try {
        ContentProviderGlobal.setBatchExecMode();
        ContentProviderGlobal.setOIObjectManager(om);
        logger.info("Registering configured Content Providers...");
        ContentProviderFactory.getInstance().registerContentProviders(om);
        if (ccpId != null) {
          ccp = ContentProviderFactory.getInstance().createContentProvider(ccpId);
        } else {
          ccp = ContentProviderSelectDlg.selectContentProvider(ContentProviderRegistryEntry.ContentProviderRole.MANUFACTURER_PART_CREATION);
        } 
        logger.info("Loading " + ccp.getName() + " Content Provider mapping configuration...");
        ccpCfg = ccp.getConfig();
      } catch (ContentProviderException contentProviderException) {
        logger.error("Error:  " + contentProviderException.getMessage());
        System.exit(1);
      } 
      logger.info("Querying " + ccp.getName() + " for subscribed ACL parts...");
      Collection collection = null;
      try {
        collection = ccp.getSubscribedComponents();
      } catch (ContentProviderException contentProviderException) {
        logger.error("Error:  Unable to query " + ccp.getName() + " for subscribed ACL parts:  " + contentProviderException.getMessage());
        System.exit(1);
      } 
      logger.info("Querying EDM Library for candidate Components ...");
      EDMCompList eDMCompList = new EDMCompList();
      HashSet<String> hashSet = new HashSet();
      OICursor oICursor = null;
      try {
        OIQuery oIQuery = null;
        if (compCatalog != null) {
          oIQuery = om.createQuery(compCatalog, true);
        } else {
          oIQuery = om.createQuery("Component", true);
        } 
        if (bIgnoreChildComps) {
          logger.info("Ignoring all child components (piped part numbers)...");
          oIQuery.addRestriction("PartNumber", "~*\\|*");
        } 
        oIQuery.addColumn("PartNumber");
        oICursor = oIQuery.execute();
        while (oICursor.next())
          hashSet.add(oICursor.getString("PartNumber")); 
        oICursor.close();
        if (compCatalog != null) {
          oIQuery = om.createQuery(compCatalog, true);
        } else {
          oIQuery = om.createQuery("Component", true);
        } 
        if (compRestrictions != null)
          for (String str : compRestrictions.split("\\s*,\\s*")) {
            String[] arrayOfString = str.split("\\s*=\\s*");
            if (arrayOfString.length == 2) {
              if (bIgnoreChildComps && arrayOfString[0].equals("PartNumber")) {
                logger.error("Error:  Search restrictions cannot be applied to 'PartNumber' if '-ignorechildcomps' is enabled.");
                System.exit(1);
              } 
              logger.info("Adding Component restriction : " + arrayOfString[0] + " = '" + arrayOfString[1] + "'...");
              oIQuery.addRestriction(arrayOfString[0], arrayOfString[1]);
            } 
          }  
        if (amlRestrictions != null)
          for (String str : amlRestrictions.split("\\s*,\\s*")) {
            String[] arrayOfString = str.split("\\s*=\\s*");
            if (arrayOfString.length == 2) {
              logger.info("Adding Component AML restriction : " + arrayOfString[0] + " = '" + arrayOfString[1] + "'...");
              oIQuery.addRestriction("ApprovedManufacturerList." + arrayOfString[0], arrayOfString[1]);
            } 
          }  
        if (mpnRestrictions != null)
          for (String str : mpnRestrictions.split("\\s*,\\s*")) {
            String[] arrayOfString = str.split("\\s*=\\s*");
            if (arrayOfString.length == 2) {
              logger.info("Adding Manufacturer Part restriction : " + arrayOfString[0] + " = '" + arrayOfString[1] + "'...");
              oIQuery.addRestriction("ApprovedManufacturerList.MfgPartNumber." + arrayOfString[0], arrayOfString[1]);
            } 
          }  
        if (bIgnoreChildComps)
          oIQuery.addRestriction("PartNumber", "~*\\|*"); 
        oIQuery.addColumn("PartNumber");
        oIQuery.addColumn("ApprovedManufacturerList.MfgPartNumber");
        oIQuery.addRestriction("ApprovedManufacturerList.MfgPartNumber.ExternalContentId.ECProviderReferences.ECProviderReferenceID", "NULL|SE");
        oIQuery.addColumn("ApprovedManufacturerList.MfgPartNumber.ExternalContentId.ECProviderReferences.ECProviderReferenceKey");
        oIQuery.addColumn("ApprovedManufacturerList.MfgPartNumber.ExternalContentId.ECProviderReferences.ECProviderReferenceValue");
        oICursor = oIQuery.execute();
        while (oICursor.next()) {
          String str1 = oICursor.getString("PartNumber");
          String str2 = oICursor.getStringified("MfgPartNumber");
          String str3 = oICursor.getStringified("ECProviderReferenceKey");
          String str4 = oICursor.getStringified("ECProviderReferenceValue");
          eDMCompList.addComponentRecord(str1, str2, str3, str4);
        } 
        oICursor.close();
        for (ContentProviderSubscribedComponent contentProviderSubscribedComponent : collection) {
          if (!hashSet.contains(contentProviderSubscribedComponent.getCPN())) {
            logger.warn("Warning:  ACL CPN '" + contentProviderSubscribedComponent.getCPN() + "': Component not found in EDM Library.");
            continue;
          } 
          EDMCompData eDMCompData = eDMCompList.getComponent(contentProviderSubscribedComponent.getCPN());
          if (eDMCompData == null) {
            logger.warn("Warning:  ACL CPN '" + contentProviderSubscribedComponent.getCPN() + "': Component found in EDM Library but was filtered by search criteria (-comprestrictions, -amlrestrictions, -mpnrestrictions).");
            continue;
          } 
          logger.info("" + partCount++ + " : Processing Component '" + partCount++ + "'...");
          if (contentProviderSubscribedComponent.getAML().isEmpty()) {
            logger.warn("Warning:  ACL CPN '" + contentProviderSubscribedComponent.getCPN() + "' refernences no Manufacturer Parts.  Skipping.");
            continue;
          } 
          for (ContentProviderSubscribedAML contentProviderSubscribedAML : contentProviderSubscribedComponent.getAML()) {
            String str = contentProviderSubscribedAML.getCustomerManufacturer() + ":" + contentProviderSubscribedAML.getCustomerManufacturer();
            EDMMPNData eDMMPNData = eDMCompData.getMPN(str);
            if (eDMMPNData != null) {
              String str1 = getIDPropKey(eDMMPNData.getIdPropMap());
              String str2 = getIDPropKey(contentProviderSubscribedAML.getIdPropMap());
              if (!str1.equals(str2)) {
                logger.info("  Processing Manufacturer Part '" + contentProviderSubscribedAML.getCustomerPartNumber() + "' from '" + contentProviderSubscribedAML.getCustomerManufacturer() + "'...");
                if (!bDebug)
                  processPart(contentProviderSubscribedComponent.getCPN(), str, contentProviderSubscribedAML); 
              } 
              continue;
            } 
            logger.warn("  Warning:  Manufacturer Part '" + contentProviderSubscribedAML.getCustomerPartNumber() + "' from '" + contentProviderSubscribedAML.getCustomerManufacturer() + "' not in Component AML or filtered by search criteria.");
          } 
        } 
      } catch (Exception exception) {
        logger.error("Error:  Unable to process Manufacturer Parts in EDM Library:  " + exception.getMessage());
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
  
  private static boolean processPart(String paramString1, String paramString2, ContentProviderSubscribedAML paramContentProviderSubscribedAML) throws ContentProviderConfigException {
    OIObject oIObject;
    IContentProviderResultRecord iContentProviderResultRecord;
    try {
      oIObject = om.getObjectByID(paramString2, "ManufacturerPart", false);
    } catch (OIException oIException) {
      logger.error("    Error:  " + oIException.getMessage());
      return false;
    } 
    try {
      iContentProviderResultRecord = ccp.getPart(paramContentProviderSubscribedAML.getIdPropMap());
    } catch (ContentProviderException contentProviderException) {
      logger.error(contentProviderException.getMessage());
      return false;
    } 
    if (iContentProviderResultRecord == null) {
      logger.error("    Error:  Manufacturer Part not found in " + ccp.getName() + ".");
      for (ContentProviderConfigProperty contentProviderConfigProperty : ccp.getConfig().getIdProperties())
        logger.info("            " + contentProviderConfigProperty.getContentProviderId() + " = '" + (String)paramContentProviderSubscribedAML.getIdPropMap().get(contentProviderConfigProperty.getContentProviderId()) + "'"); 
      return false;
    } 
    logger.info("    Assigning External Content...");
    try {
      ContentProviderSync.assignEC2MPN(ccp, oIObject, paramContentProviderSubscribedAML.getIdPropMap());
    } catch (ContentProviderException contentProviderException) {
      logger.error(contentProviderException.getMessage());
      return false;
    } 
    logger.info("    Validating Manufacturer Part classification...");
    ContentProviderConfigDMSCatalog contentProviderConfigDMSCatalog = null;
    Collection<ContentProviderConfigDMSCatalog> collection = ccpCfg.getMPNCatalogsByContentProviderId(iContentProviderResultRecord.getPartClassID());
    if (collection == null || collection.isEmpty()) {
      logger.warn("    Warning:  While attempting to validate Manufacturer Part classification:  Part Class Content Provider ID '" + iContentProviderResultRecord.getPartClassName() + "' ( " + iContentProviderResultRecord.getPartClassID() + " ) not found in mapping configuration.  Part will not be moved.");
      return false;
    } 
    contentProviderConfigDMSCatalog = collection.iterator().next();
    String str1 = contentProviderConfigDMSCatalog.getClassDMN();
    String str2 = oIObject.getOIClass().getName();
    if (!str1.equals(str2)) {
      OIClass oIClass = getOIClass(str1);
      logger.info("    Moving Manufacturer Part from '" + oIObject.getOIClass().getLabel() + "' to '" + oIClass.getLabel() + "'...");
      try {
        om.moveInClassHierarchy(oIObject, oIClass);
        om.makePermanent(oIObject);
      } catch (Exception exception) {
        logger.error("    Error:  Unable to move Manufacturer Part from '" + oIObject.getOIClass().getLabel() + "' to '" + oIClass.getLabel() + "': " + exception.getMessage());
        return false;
      } finally {
        try {
          om.evict(oIObject);
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
  
  private static String getIDPropKey(HashMap<String, String> paramHashMap) throws ContentProviderException {
    String str1 = "";
    String str2 = "";
    for (ContentProviderConfigProperty contentProviderConfigProperty : ccp.getConfig().getIdProperties()) {
      String str = paramHashMap.get(contentProviderConfigProperty.getContentProviderId());
      if (str != null && !str.isEmpty()) {
        str1 = str1 + str1 + str2;
        str2 = ":";
      } 
    } 
    return str1;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovide\\utils\SearchAndAssignACLApp.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */