package com.mentor.dms.contentprovider.core.utils;

import com.mentor.datafusion.oi.OICursor;
import com.mentor.datafusion.oi.OIException;
import com.mentor.datafusion.oi.OIQuery;
import com.mentor.datafusion.oi.login.OIAuthenticate;
import com.mentor.datafusion.oi.login.OIAuthenticateFactory;
import com.mentor.datafusion.oi.login.OILoginData;
import com.mentor.datafusion.oi.type.OIObject;
import com.mentor.datafusion.utils.logger.MGLogger;
import com.mentor.dms.contentprovider.core.ContentProviderException;
import com.mentor.dms.contentprovider.core.ContentProviderFactory;
import com.mentor.dms.contentprovider.core.ContentProviderGlobal;
import com.mentor.dms.contentprovider.core.ContentProviderRegistryEntry;
import com.mentor.dms.contentprovider.core.plugin.searchui.ContentProviderSelectDlg;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import javax.swing.UIManager;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class SearchAndAssignACLApp extends SearchAndAssignApp {
  static MGLogger logger = MGLogger.getLogger(SearchAndAssignACLApp.class);
  
  static String compCatalog = null;
  
  static String compRestrictions = null;
  
  static String amlRestrictions = null;
  
  static String mpnRestrictions = null;
  
  static boolean bIgnoreChildComps = false;
  
  static boolean bDebug = false;
  
  private static HashSet<String> assignedMPNIDSet = new HashSet<>();
  
  public static void main(String[] paramArrayOfString) {
    boolean bool = false;
    Calendar calendar = Calendar.getInstance();
    Date date1 = calendar.getTime();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    fileNameTime = simpleDateFormat.format(date1);
    try {
      LogConfigLoader.configLog4j();
    } catch (Exception exception) {
      exception.printStackTrace();
      System.out.println("Problems with the logging settings");
    } 
    logger.info("#");
    logger.info("#        Search And Assign (ACL) - Version 1.1.1");
    logger.info("#");
    logger.info("#                Copyright Siemens 2025");
    logger.info("#");
    logger.info("#                  All Rights Reserved.");
    logger.info("#");
    logger.info("#        THIS WORK CONTAINS TRADE SECRET AND PROPRIETARY");
    logger.info("#        INFORMATION WHICH IS THE PROPERTY OF SIEMENS ");
    logger.info("#        OR ITS LICENSORS AND IS SUBJECT TO LICENSE TERMS.");
    logger.info("#");
    Options options = new Options();
    options.addOption("help", false, "help");
    options.addOption("loginconfig", true, "EDM Library auto-login configuration");
    options.addOption("user", true, "EDM Library username");
    options.addOption("password", true, "EDM Library password");
    options.addOption("server", true, "EDM Library server");
    options.addOption("ccpid", true, "Content Provider ID");
    options.addOption("compcatalog", true, "Domain name of Component catalog to search for parts");
    options.addOption("comprestrictions", true, "Comma-separated list of \"<dmn>=<restriction>\" Component search restrictions");
    options.addOption("amlrestrictions", true, "Comma-separated list of \"<dmn>=<restriction>\" Component AML search restrictions");
    options.addOption("mpnrestrictions", true, "Comma-separated list of \"<dmn>=<restriction>\" Manufacturer Part search restrictions");
    options.addOption("ignorechildcomps", false, "Ignore child components (piped part numbers)");
    options.addOption("outputdir", true, "Output directory for search results");
    options.addOption("debug", false, "Debug mode (Assignments not committed to EDM Library.");
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
    dmsConfigName = commandLine.getOptionValue("loginconfig");
    if (dmsConfigName == null) {
      dmsServer = commandLine.getOptionValue("server");
      dmsUser = commandLine.getOptionValue("user");
      dmsPassword = commandLine.getOptionValue("password");
      if ((dmsServer != null && (dmsUser == null || dmsPassword == null)) || (dmsUser != null && (dmsServer == null || dmsPassword == null)) || (dmsPassword != null && (dmsUser == null || dmsServer == null))) {
        logger.error("Error: For username/password login to EDM Library, 'user', 'password', and 'server' must all be specified.");
        System.exit(1);
      } 
    } 
    if (commandLine.hasOption("ccpid"))
      ccpId = commandLine.getOptionValue("ccpid"); 
    compCatalog = commandLine.getOptionValue("compcatalog");
    compRestrictions = commandLine.getOptionValue("comprestrictions");
    amlRestrictions = commandLine.getOptionValue("amlrestrictions");
    mpnRestrictions = commandLine.getOptionValue("mpnrestrictions");
    bIgnoreChildComps = commandLine.hasOption("ignorechildcomps");
    bDebug = commandLine.hasOption("debug");
    outputdir = commandLine.getOptionValue("outputdir");
    if (outputdir == null)
      outputdir = "./"; 
    outputPath = "SearchAndAssign_Result_" + fileNameTime + ".csv";
    Date date2 = new Date();
    try {
      OIAuthenticate oIAuthenticate;
      logger.info("Connecting to EDM Library Server...");
      if (dmsConfigName != null) {
        oIAuthenticate = OIAuthenticateFactory.createBatchAuthenticate(dmsConfigName);
      } else {
        OILoginData oILoginData = OIAuthenticateFactory.createLoginData("ContentProviderSync");
        oILoginData.setLicenceRoleNames("dmscompeng");
        oILoginData.setShowLoginDialog(true);
        oILoginData.setShowRoleDialog(false);
        oILoginData.setShowProdLibDialog(false);
        if (dmsServer != null)
          oILoginData.setServer(dmsServer); 
        if (dmsUser != null)
          oILoginData.setUsername(dmsUser); 
        if (dmsPassword != null)
          oILoginData.setPassword(dmsPassword); 
        if (dmsServer != null && dmsUser != null && dmsPassword != null) {
          oIAuthenticate = OIAuthenticateFactory.createBatchAuthenticate(oILoginData);
        } else {
          try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
          } catch (Exception exception) {}
          oIAuthenticate = OIAuthenticateFactory.createInteractiveAuthenticate(oILoginData, null);
        } 
      } 
      omf = oIAuthenticate.login("Search and Assign (ACL) Utility");
      om = omf.createObjectManager();
    } catch (OIException oIException) {
      logger.error("Unable to connect to EDM Library Server:  " + oIException.getMessage());
      System.exit(1);
    } 
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
      if (ccp == null) {
        logger.error("Content provider toolbox does not have a \"" + ccpId + "\" on EDM Library Server.");
        bool = true;
        return;
      } 
      logger.info("Loading " + ccp.getName() + " Content Provider mapping configuration...");
      ccpCfg = ccp.getConfig();
      OICursor oICursor = null;
      ArrayList<String> arrayList = new ArrayList();
      try {
        OIQuery oIQuery = null;
        oIQuery = om.createQuery("ExternalContent", true);
        oIQuery.addColumn("ExternalContentId");
        logger.debug("Search ExternalContent.");
        oICursor = oIQuery.execute();
        while (oICursor.next())
          arrayList.add(oICursor.getString("ExternalContentId")); 
      } catch (OIException oIException) {
        logger.error("Unable to process External Content in EDM Library:  " + oIException.getMessage());
        bool = true;
        return;
      } catch (Exception exception) {
        logger.error(exception.getMessage(), exception);
        bool = true;
        return;
      } 
      try {
        OIQuery oIQuery = null;
        oIQuery = om.createQuery("Component", true);
        if (compCatalog != null) {
          logger.info("Adding restriction to catalog group '" + compCatalog + "'.");
          oIQuery.addRestriction("Obj_cod", getCatalog(compCatalog, true));
        } 
        if (compRestrictions != null)
          for (String str : compRestrictions.split("\\s*,\\s*")) {
            String[] arrayOfString = str.split("\\s*=\\s*");
            if (arrayOfString.length == 2) {
              if (bIgnoreChildComps && arrayOfString[0].equals("PartNumber")) {
                logger.error("Error:  Search restrictions cannot be applied to 'PartNumber' if '-ignorechildcomps' is enabled.");
                bool = true;
                return;
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
        oIQuery.addRestriction("ApprovedManufacturerList.MfgPartNumber.ExternalContentId", "~NULL");
        oIQuery.addSortBy("PartNumber", true);
        oIQuery.addSortBy("ApprovedManufacturerList.MfgPartNumber", true);
        oIQuery.addColumn("PartNumber");
        oIQuery.addAlias("PartNumber", getCompAlias("PartNumber"));
        oIQuery.addColumn("ApprovedManufacturerList");
        oIQuery.addColumn("ApprovedManufacturerList.MfgPartNumber");
        oIQuery.addColumn("ApprovedManufacturerList.MfgPartNumber.ManufacturerName");
        oIQuery.addColumn("ApprovedManufacturerList.MfgPartNumber.PartNumber");
        oIQuery.addColumn("ApprovedManufacturerList.MfgPartNumber.ExternalContentId");
        oICursor = oIQuery.execute();
        logger.info("The total number of target data is " + oIQuery.count() + ".");
        while (oICursor.next()) {
          OIObject oIObject1 = oICursor.getObject("ExternalContentId");
          String str = oIObject1.getObjectID();
          if (!arrayList.contains(str)) {
            createCSV.createFile(outputdir, outputPath);
            logger.error("ManufacturePart '" + oICursor.getString("PartNumber") + "' of Manufucturer '" + oICursor.getString("ManufacturerName") + "' associated with Component '" + oICursor.getString(getCompAlias("PartNumber")) + "':ExternalContentID '" + str + "' does not exist in External Content.");
            SearchAndAssignContent searchAndAssignContent = new SearchAndAssignContent();
            searchAndAssignContent.setPartNumber(oICursor.getString("PartNumber"));
            searchAndAssignContent.setManufuctureName(oICursor.getString("ManufacturerName"));
            searchAndAssignContent.setMatchStatus("Error ECID does not exist :'" + str + "'.");
            createCSV.postscriptCSV(searchAndAssignContent);
          } 
        } 
        oICursor.close();
      } catch (OIException oIException) {
        logger.error("Unable to process External Content in EDM Library:  " + oIException.getMessage());
        bool = true;
        return;
      } catch (Exception exception) {
        logger.error(exception.getMessage(), exception);
        bool = true;
        return;
      } 
      OIObject oIObject = null;
      logger.info("Querying EDM Library for candidate Components ...");
      oICursor = null;
      try {
        OIQuery oIQuery = null;
        oIQuery = om.createQuery("Component", true);
        if (compCatalog != null) {
          logger.info("Adding restriction to catalog group '" + compCatalog + "'.");
          oIQuery.addRestriction("Obj_cod", getCatalog(compCatalog, true));
        } 
        if (compRestrictions != null)
          for (String str1 : compRestrictions.split("\\s*,\\s*")) {
            String[] arrayOfString = str1.split("\\s*=\\s*");
            if (arrayOfString.length == 2) {
              if (bIgnoreChildComps && arrayOfString[0].equals("PartNumber")) {
                logger.error("Error:  Search restrictions cannot be applied to 'PartNumber' if '-ignorechildcomps' is enabled.");
                bool = true;
                return;
              } 
              logger.info("Adding Component restriction : " + arrayOfString[0] + " = '" + arrayOfString[1] + "'...");
              oIQuery.addRestriction(arrayOfString[0], arrayOfString[1]);
            } 
          }  
        if (amlRestrictions != null)
          for (String str1 : amlRestrictions.split("\\s*,\\s*")) {
            String[] arrayOfString = str1.split("\\s*=\\s*");
            if (arrayOfString.length == 2) {
              logger.info("Adding Component AML restriction : " + arrayOfString[0] + " = '" + arrayOfString[1] + "'...");
              oIQuery.addRestriction("ApprovedManufacturerList." + arrayOfString[0], arrayOfString[1]);
            } 
          }  
        if (mpnRestrictions != null)
          for (String str1 : mpnRestrictions.split("\\s*,\\s*")) {
            String[] arrayOfString = str1.split("\\s*=\\s*");
            if (arrayOfString.length == 2) {
              logger.info("Adding Manufacturer Part restriction : " + arrayOfString[0] + " = '" + arrayOfString[1] + "'...");
              oIQuery.addRestriction("ApprovedManufacturerList.MfgPartNumber." + arrayOfString[0], arrayOfString[1]);
            } 
          }  
        if (bIgnoreChildComps)
          oIQuery.addRestriction("PartNumber", "~*\\|*"); 
        oIQuery.addRestriction("ApprovedManufacturerList.MfgPartNumber.ExternalContentId.ECProviderReferences.ECProviderReferenceID", "NULL|" + ccp.getId());
        oIQuery.addSortBy("PartNumber", true);
        oIQuery.addSortBy("ApprovedManufacturerList.MfgPartNumber", true);
        oIQuery.addColumn("PartNumber");
        oIQuery.addAlias("PartNumber", getCompAlias("PartNumber"));
        oIQuery.addColumn("ApprovedManufacturerList.MfgPartNumber");
        oIQuery.addColumn("ApprovedManufacturerList.MfgPartNumber.ManufacturerName");
        oIQuery.addColumn("ApprovedManufacturerList.MfgPartNumber.PartNumber");
        oIQuery.addColumn("ApprovedManufacturerList.MfgPartNumber.ExternalContentId.ECProviderReferences.ECProviderReferenceKey");
        oIQuery.addColumn("ApprovedManufacturerList.MfgPartNumber.ExternalContentId.ECProviderReferences.ECProviderReferenceValue");
        oIQuery.addColumn("ApprovedManufacturerList.MfgPartNumber.ExternalContentId");
        oICursor = oIQuery.execute();
        logger.info("The total number of target data is " + oIQuery.count() + ".");
        byte b = 0;
        String str = "";
        while (oICursor.next()) {
          if (!b && !bDebug) {
            createCSV.createFile(outputdir, outputPath);
            b++;
          } 
          String str1 = oICursor.getString(getCompAlias("PartNumber"));
          String str2 = oICursor.getString("PartNumber");
          String str3 = oICursor.getString("ManufacturerName");
          if (!str1.equals(str)) {
            logger.info("For Component '" + str1 + "'");
            str = str1;
          } 
          oIObject = oICursor.getObject("MfgPartNumber");
          if (oIObject != null) {
            logger.info(" " + partCount++ + " : Processing Manufacturer Part '" + str2 + "' by '" + str3 + "'...");
            if (bDebug)
              continue; 
            if (assignedMPNIDSet.contains(oIObject.getObjectID())) {
              logger.info("  Manufacturer part \"" + oIObject.getObjectID() + "\" is already assigned.");
              continue;
            } 
            OIObject oIObject1 = oICursor.getObject("ExternalContentId");
            if (oIObject1 == null || arrayList.contains(oIObject1.getObjectID())) {
              processPart(oIObject);
              assignedMPNIDSet.add(oIObject.getObjectID());
              continue;
            } 
            logger.info("  ExternalContent does not exist, so the association process is skipped. ");
          } 
        } 
        oICursor.close();
      } catch (OIException oIException) {
        logger.error("Unable to process Manufacturer Parts in EDM Library:  " + oIException.getMessage());
        bool = true;
        return;
      } catch (Exception exception) {
        logger.error(exception.getMessage(), exception);
        bool = true;
        return;
      } 
    } catch (ContentProviderException contentProviderException) {
      contentProviderException.printStackTrace();
      logger.error(contentProviderException.getMessage());
      bool = true;
      return;
    } finally {
      logger.debug("Closing connection to EDM Library Server...");
      if (om != null)
        om.close(); 
      if (omf != null)
        omf.close(); 
      if (bool)
        System.exit(1); 
    } 
    logger.info("Search and Assign completed.");
  }
  
  private static String getCatalog(String paramString, boolean paramBoolean) throws OIException {
    StringBuilder stringBuilder = new StringBuilder();
    HashSet<String> hashSet = new HashSet();
    String str = "";
    if (paramBoolean)
      str = "*"; 
    OIQuery oIQuery = om.createQuery("CatalogGroup", true);
    oIQuery.addRestriction("Text.Abbreviation", paramString);
    oIQuery.addColumn("CatalogGroup");
    OICursor oICursor = oIQuery.execute();
    while (oICursor.next()) {
      String str1 = oICursor.getString("CatalogGroup");
      if (!hashSet.contains(str1)) {
        stringBuilder.append(str1 + str1 + "|");
        hashSet.add(str1);
      } 
    } 
    if (0 < stringBuilder.length()) {
      stringBuilder.deleteCharAt(stringBuilder.length() - 1);
      return stringBuilder.toString();
    } 
    throw new OIException("Catalog \"" + paramString + "\" not found.");
  }
  
  private static String getCompAlias(String paramString) {
    return "COMP_" + paramString;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\cor\\utils\SearchAndAssignACLApp.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */