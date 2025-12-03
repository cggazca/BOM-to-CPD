package com.mentor.dms.contentprovider.core.utils;

import com.mentor.datafusion.oi.OICursor;
import com.mentor.datafusion.oi.OIException;
import com.mentor.datafusion.oi.OIObjectManager;
import com.mentor.datafusion.oi.OIObjectManagerFactory;
import com.mentor.datafusion.oi.OIQuery;
import com.mentor.datafusion.oi.login.OIAuthenticate;
import com.mentor.datafusion.oi.login.OIAuthenticateFactory;
import com.mentor.datafusion.oi.login.OILoginData;
import com.mentor.datafusion.oi.model.OIClass;
import com.mentor.datafusion.oi.type.OIObject;
import com.mentor.datafusion.utils.logger.MGLogger;
import com.mentor.dms.contentprovider.core.AbstractContentProvider;
import com.mentor.dms.contentprovider.core.ContentProviderException;
import com.mentor.dms.contentprovider.core.ContentProviderFactory;
import com.mentor.dms.contentprovider.core.ContentProviderGlobal;
import com.mentor.dms.contentprovider.core.ContentProviderRegistryEntry;
import com.mentor.dms.contentprovider.core.IContentProviderResultRecord;
import com.mentor.dms.contentprovider.core.IContentProviderResults;
import com.mentor.dms.contentprovider.core.config.ContentProviderConfig;
import com.mentor.dms.contentprovider.core.config.ContentProviderConfigDMSCatalog;
import com.mentor.dms.contentprovider.core.plugin.searchui.ContentProviderSelectDlg;
import com.mentor.dms.contentprovider.core.sync.ContentProviderSync;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.UIManager;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class SearchAndAssignApp {
  static MGLogger logger = MGLogger.getLogger(SearchAndAssignApp.class);
  
  static String dmsConfigName = null;
  
  static String dmsUser = null;
  
  static String dmsPassword = null;
  
  static String dmsServer = null;
  
  static String ccpId = null;
  
  static String mpnCatalog = null;
  
  static String outputdir = null;
  
  static boolean bKeepRegistry = true;
  
  static boolean bNoRegistry = true;
  
  static OIObjectManagerFactory omf = null;
  
  static OIObjectManager om = null;
  
  static AbstractContentProvider ccp = null;
  
  static ContentProviderConfig ccpCfg = null;
  
  static HashMap<String, OIClass> oiClassMap = new HashMap<>();
  
  static int partCount = 1;
  
  static String fileNameTime;
  
  static String outputPath;
  
  static SearchAndAssignCreateCSV createCSV = new SearchAndAssignCreateCSV();
  
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
    logger.info("#         Search And Assign - Version 1.1.1");
    logger.info("#");
    logger.info("#                Copyright Siemens 2025");
    logger.info("#");
    logger.info("#                      All Rights Reserved.");
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
    options.addOption("mpncatalog", true, "Domain name of MPN catalog to search for parts");
    options.addOption("outputdir", true, "Output directory for search results");
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
    mpnCatalog = commandLine.getOptionValue("mpncatalog");
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
      omf = oIAuthenticate.login("Search and Assign Utility");
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
      OIObject oIObject = null;
      logger.info("Querying EDM Library for candidate Manufacturer Parts ...(ExternalContent existence check)");
      OICursor oICursor = null;
      try {
        OIQuery oIQuery = null;
        oIQuery = om.createQuery("ExternalContent", true);
        oIQuery.addColumn("ExternalContentId");
        logger.debug("Search ExternalContent.");
        oICursor = oIQuery.execute();
        ArrayList<String> arrayList = new ArrayList();
        while (oICursor.next())
          arrayList.add(oICursor.getString("ExternalContentId")); 
        oICursor = null;
        if (mpnCatalog != null) {
          oIQuery = om.createQuery(mpnCatalog, true);
        } else {
          oIQuery = om.createQuery("ManufacturerPart", true);
        } 
        oIQuery.addRestriction("ExternalContentId", "~NULL");
        oIQuery.addColumn("ManufacturerpartId");
        oIQuery.addColumn("PartNumber");
        oIQuery.addColumn("ManufacturerName");
        oICursor = oIQuery.execute();
        while (oICursor.next()) {
          if (!arrayList.contains(oICursor.getProxyObject().getObject().getString("ManufacturerpartId"))) {
            createCSV.createFile(outputdir, outputPath);
            logger.error("ManufacturePart '" + oICursor.getString("PartNumber") + "' of Manufacturer '" + oICursor.getString("ManufacturerName") + "' :ExternalContentID '" + oICursor.getProxyObject().getObject().getString("ManufacturerpartId") + "' does not exist in External Content.");
            SearchAndAssignContent searchAndAssignContent = new SearchAndAssignContent();
            searchAndAssignContent.setPartNumber(oICursor.getString("PartNumber"));
            searchAndAssignContent.setManufuctureName(oICursor.getString("ManufacturerName"));
            searchAndAssignContent.setMatchStatus("Error ECID does not exist :'" + oICursor.getProxyObject().getObject().getString("ManufacturerpartId") + "'.");
            createCSV.postscriptCSV(searchAndAssignContent);
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
      logger.info("Querying EDM Library for candidate Manufacturer Parts ...");
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
        logger.info("The total number of target data is " + oIQuery.count() + ".");
        while (oICursor.next()) {
          createCSV.createFile(outputdir, outputPath);
          String str1 = oICursor.getString("PartNumber");
          String str2 = oICursor.getString("ManufacturerName");
          logger.info("" + partCount++ + " : Processing Manufacturer Part '" + partCount++ + "' by '" + str1 + "'...");
          oIObject = oICursor.getProxyObject().getObject();
          processPart(oIObject);
        } 
        oICursor.close();
        setLastSyncDate(date2);
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
  
  protected static boolean processPart(OIObject paramOIObject) {
    SearchAndAssignContent searchAndAssignContent = new SearchAndAssignContent();
    ArrayList<String> arrayList = new ArrayList();
    String str1 = null;
    String str2 = null;
    try {
      str1 = paramOIObject.getString("PartNumber");
      str2 = paramOIObject.getString("ManufacturerName");
      if (str1 == null || str2 == null)
        throw new OIException("PartNumber or ManufacturerName is null."); 
      searchAndAssignContent.setPartNumber(str1);
      searchAndAssignContent.setManufuctureName(str2);
    } catch (OIException oIException) {
      logger.error("Unable to read part number and manufacturer name from EDM Manufacturer Part: " + oIException.getMessage());
      return false;
    } 
    IContentProviderResultRecord iContentProviderResultRecord = null;
    try {
      ArrayList<IContentProviderResultRecord> arrayList1 = new ArrayList();
      if (!str2.equals("") && !str2.equals("Unknown")) {
        logger.info("Search to Supplyframe by PartNumber(" + str1 + ") and ManufacturerName(" + str2 + ").");
        IContentProviderResults iContentProviderResults = ccp.searchExactMatchSF(str1, str2);
        if (iContentProviderResults == null) {
          logger.error("Unable to search for Part Number '" + str1 + "' by '" + str2 + "' in " + ccp.getName() + ".");
          return false;
        } 
        for (IContentProviderResultRecord iContentProviderResultRecord1 : iContentProviderResults.getResultRecords()) {
          if (iContentProviderResultRecord1.getPartProperty("6230417e").getValue().equals(str2) && iContentProviderResultRecord1.getPartProperty("d8ac8dcc").getValue().equals(str1))
            arrayList1.add(iContentProviderResultRecord1); 
        } 
        if (arrayList1.size() > 1) {
          logger.error("Search results contain more than one exact match Part in " + ccp.getName() + ".");
          searchAndAssignContent.setMatchStatus("Multiple");
          for (byte b = 0; b < arrayList1.size(); b++) {
            logger.error("\t" + b + 1 + ": PartNumber '" + ((IContentProviderResultRecord)arrayList1.get(b)).getPartProperty("d8ac8dcc").getValue() + "' by ManufacturerName '" + ((IContentProviderResultRecord)arrayList1.get(b)).getPartProperty("6230417e").getValue() + "'.");
            arrayList.add(((IContentProviderResultRecord)arrayList1.get(b)).getPartProperty("d8ac8dcc").getValue() + "@" + ((IContentProviderResultRecord)arrayList1.get(b)).getPartProperty("d8ac8dcc").getValue());
          } 
          searchAndAssignContent.setMatchValues(arrayList);
          createCSV.postscriptCSV(searchAndAssignContent);
          return false;
        } 
        if (arrayList1.size() == 1) {
          iContentProviderResultRecord = arrayList1.get(0);
          logger.info("Exact matches found for both PartNumber '" + str1 + "' and ManufacturerName '" + str2 + "'.");
          searchAndAssignContent.setMatchStatus("Found");
          arrayList.add(iContentProviderResultRecord.getPartProperty("d8ac8dcc").getValue() + "@" + iContentProviderResultRecord.getPartProperty("d8ac8dcc").getValue());
          searchAndAssignContent.setMatchValues(arrayList);
          createCSV.postscriptCSV(searchAndAssignContent);
        } else if (arrayList1.size() == 0) {
          logger.error("Unable to find an exact match for Part Number '" + str1 + "' by '" + str2 + "' in " + ccp.getName() + ".");
          logger.info("Searches for ManufuctureName(" + str2 + ") that is a partial match.");
          arrayList1.clear();
          for (IContentProviderResultRecord iContentProviderResultRecord1 : iContentProviderResults.getResultRecords()) {
            if (iContentProviderResultRecord1.getPartProperty("6230417e").getValue().contains(str2) && iContentProviderResultRecord1.getPartProperty("d8ac8dcc").getValue().equals(str1))
              arrayList1.add(iContentProviderResultRecord1); 
          } 
          if (arrayList1.size() > 1) {
            logger.error("Search results contain more than one Part in " + ccp.getName() + ".");
            searchAndAssignContent.setMatchStatus("Multiple");
            for (byte b = 0; b < arrayList1.size(); b++) {
              logger.error("\t" + b + 1 + ": PartNumber '" + ((IContentProviderResultRecord)arrayList1.get(b)).getPartProperty("d8ac8dcc").getValue() + "' by ManufacturerName '" + ((IContentProviderResultRecord)arrayList1.get(b)).getPartProperty("6230417e").getValue() + "'.");
              arrayList.add(((IContentProviderResultRecord)arrayList1.get(b)).getPartProperty("d8ac8dcc").getValue() + "@" + ((IContentProviderResultRecord)arrayList1.get(b)).getPartProperty("d8ac8dcc").getValue());
            } 
            searchAndAssignContent.setMatchValues(arrayList);
            createCSV.postscriptCSV(searchAndAssignContent);
            return false;
          } 
          if (arrayList1.size() == 1) {
            iContentProviderResultRecord = arrayList1.get(0);
            logger.info("PartNumber '" + str1 + "' exact match and ManufuctureName '" + str2 + "' partial match found.");
            searchAndAssignContent.setMatchStatus("Found");
            arrayList.add(iContentProviderResultRecord.getPartProperty("d8ac8dcc").getValue() + "@" + iContentProviderResultRecord.getPartProperty("d8ac8dcc").getValue());
            searchAndAssignContent.setMatchValues(arrayList);
            createCSV.postscriptCSV(searchAndAssignContent);
          } else if (arrayList1.size() == 0) {
            logger.info("Match judgment by PartNumber using only single-byte alphanumeric characters");
            Pattern pattern = Pattern.compile("[^A-Za-z0-9]");
            Matcher matcher = pattern.matcher(str1);
            String str = matcher.replaceAll("");
            logger.debug("PartNumber of converted EDM : " + str);
            for (IContentProviderResultRecord iContentProviderResultRecord1 : iContentProviderResults.getResultRecords()) {
              Matcher matcher1 = pattern.matcher(iContentProviderResultRecord1.getPartProperty("d8ac8dcc").getValue());
              String str5 = matcher1.replaceAll("");
              logger.debug("PartNumber of converted AS : " + str5);
              if (str5.equals(str))
                arrayList1.add(iContentProviderResultRecord1); 
            } 
            if (arrayList1.size() == 0) {
              logger.error("Not found PartNumber '" + str1 + "' and ManufuctureName '" + str2 + "' match. Comparison is made with the exception of the previous zero.");
              str = str.replaceFirst("^0+", "");
              for (IContentProviderResultRecord iContentProviderResultRecord1 : iContentProviderResults.getResultRecords()) {
                Matcher matcher1 = pattern.matcher(iContentProviderResultRecord1.getPartProperty("d8ac8dcc").getValue());
                String str5 = matcher1.replaceAll("");
                str5 = str5.replaceFirst("^0+", "");
                logger.debug("PartNumber of converted AS : " + str5);
                if (str5.equals(str))
                  arrayList1.add(iContentProviderResultRecord1); 
              } 
              if (arrayList1.size() == 1) {
                iContentProviderResultRecord = arrayList1.get(0);
                logger.info("PartNumber '" + str1 + "' and ManufuctureName '" + str2 + "' partial match found.");
                searchAndAssignContent.setMatchStatus("Found");
                arrayList.add(iContentProviderResultRecord.getPartProperty("d8ac8dcc").getValue() + "@" + iContentProviderResultRecord.getPartProperty("d8ac8dcc").getValue());
                searchAndAssignContent.setMatchValues(arrayList);
                createCSV.postscriptCSV(searchAndAssignContent);
              } else if (arrayList1.size() > 1) {
                iContentProviderResultRecord = arrayList1.get(0);
                logger.info("Multiple parts matched. The first part will be associated.");
                searchAndAssignContent.setMatchStatus("Found");
                arrayList.add(iContentProviderResultRecord.getPartProperty("d8ac8dcc").getValue() + "@" + iContentProviderResultRecord.getPartProperty("d8ac8dcc").getValue());
                searchAndAssignContent.setMatchValues(arrayList);
                createCSV.postscriptCSV(searchAndAssignContent);
              } 
            } else {
              iContentProviderResultRecord = arrayList1.get(0);
              if (arrayList1.size() == 1) {
                logger.info("PartNumber '" + str1 + "' exact match and ManufuctureName '" + str2 + "' partial match found.");
              } else {
                logger.info("Multiple parts matched. The first part will be associated.");
              } 
              searchAndAssignContent.setMatchStatus("Found");
              arrayList.add(iContentProviderResultRecord.getPartProperty("d8ac8dcc").getValue() + "@" + iContentProviderResultRecord.getPartProperty("d8ac8dcc").getValue());
              searchAndAssignContent.setMatchValues(arrayList);
              createCSV.postscriptCSV(searchAndAssignContent);
            } 
          } 
        } 
      } 
      if (iContentProviderResultRecord == null && (str2.equals("") || str2.equals("Unknown") || arrayList1.size() == 0)) {
        logger.info("Search to Supplyframe by PartNumber(" + str1 + ") only.");
        arrayList1.clear();
        IContentProviderResults iContentProviderResults = ccp.searchExactMatchSF(str1);
        if (iContentProviderResults == null) {
          logger.error("Unable to search for Part Number '" + str1 + "' in " + ccp.getName() + ".");
          return false;
        } 
        if (iContentProviderResults.getResultRecords().size() == 0) {
          logger.error("Unable to search for Part Number '" + str1 + "' in " + ccp.getName() + ".");
          searchAndAssignContent.setMatchStatus("None");
          createCSV.postscriptCSV(searchAndAssignContent);
          return false;
        } 
        if (iContentProviderResults.getResultRecords().size() == 1) {
          for (IContentProviderResultRecord iContentProviderResultRecord1 : iContentProviderResults.getResultRecords()) {
            iContentProviderResultRecord = iContentProviderResultRecord1;
            arrayList.add(iContentProviderResultRecord1.getPartProperty("d8ac8dcc").getValue() + "@" + iContentProviderResultRecord1.getPartProperty("d8ac8dcc").getValue());
          } 
          logger.warn("Found only one in PartNumber '" + str1 + "' search, but manufacturerName may be different. ManufuctureName is '" + iContentProviderResultRecord.getPartProperty("6230417e").getValue() + "'.");
          searchAndAssignContent.setMatchStatus("Need user review");
          searchAndAssignContent.setMatchValues(arrayList);
          createCSV.postscriptCSV(searchAndAssignContent);
          return false;
        } 
        ArrayList<IContentProviderResultRecord> arrayList2 = new ArrayList(iContentProviderResults.getResultRecords());
        logger.info("Search results in PartNumber search contain more than one Part in " + ccp.getName() + ".");
        for (IContentProviderResultRecord iContentProviderResultRecord1 : iContentProviderResults.getResultRecords()) {
          if (iContentProviderResultRecord1.getPartProperty("d8ac8dcc").getValue().equals(str1))
            arrayList1.add(iContentProviderResultRecord1); 
        } 
        if (arrayList1.size() == 0) {
          logger.info("Match judgment by PartNumber using only single-byte alphanumeric characters");
          Pattern pattern = Pattern.compile("[^A-Za-z0-9]");
          Matcher matcher = pattern.matcher(str1);
          String str = matcher.replaceAll("");
          logger.debug("PartNumber of converted EDM : " + str);
          for (IContentProviderResultRecord iContentProviderResultRecord1 : iContentProviderResults.getResultRecords()) {
            Matcher matcher1 = pattern.matcher(iContentProviderResultRecord1.getPartProperty("d8ac8dcc").getValue());
            String str5 = matcher1.replaceAll("");
            logger.debug("PartNumber of converted AS : " + str5);
            if (str5.equals(str))
              arrayList1.add(iContentProviderResultRecord1); 
          } 
          if (arrayList1.size() == 0) {
            logger.error("Not found PartNumber '" + str1 + "' exact match and ManufuctureName '" + str2 + "' partial match. Comparison is made with the exception of the previous zero.");
            str = str.replaceFirst("^0+", "");
            for (IContentProviderResultRecord iContentProviderResultRecord1 : iContentProviderResults.getResultRecords()) {
              Matcher matcher1 = pattern.matcher(iContentProviderResultRecord1.getPartProperty("d8ac8dcc").getValue());
              String str5 = matcher1.replaceAll("");
              str5 = str5.replaceFirst("^0+", "");
              logger.debug("PartNumber of converted AS : " + str5);
              if (str5.equals(str))
                arrayList1.add(iContentProviderResultRecord1); 
            } 
            if (arrayList1.size() == 0) {
              logger.info("PartNumber '" + str1 + "' partial match found.");
              for (byte b = 0; b < arrayList2.size(); b++) {
                logger.error("\t" + b + 1 + ": PartNumber '" + ((IContentProviderResultRecord)arrayList2.get(b)).getPartProperty("d8ac8dcc").getValue() + "' by ManufacturerName '" + ((IContentProviderResultRecord)arrayList2.get(b)).getPartProperty("6230417e").getValue() + "'.");
                arrayList.add(((IContentProviderResultRecord)arrayList2.get(b)).getPartProperty("d8ac8dcc").getValue() + "@" + ((IContentProviderResultRecord)arrayList2.get(b)).getPartProperty("d8ac8dcc").getValue());
              } 
              searchAndAssignContent.setMatchStatus("Multiple");
              searchAndAssignContent.setMatchValues(arrayList);
              createCSV.postscriptCSV(searchAndAssignContent);
              return false;
            } 
            if (arrayList1.size() == 1) {
              iContentProviderResultRecord = arrayList1.get(0);
              searchAndAssignContent.setMatchStatus("Need user review");
              logger.warn("Found only one in PartNumber '" + str1 + "' search (zero suppress), but manufacturerName may be different. ManufuctureName is '" + iContentProviderResultRecord.getPartProperty("6230417e").getValue() + "'.");
              arrayList.add(iContentProviderResultRecord.getPartProperty("d8ac8dcc").getValue() + "@" + iContentProviderResultRecord.getPartProperty("d8ac8dcc").getValue());
              searchAndAssignContent.setMatchValues(arrayList);
              createCSV.postscriptCSV(searchAndAssignContent);
              return false;
            } 
            iContentProviderResultRecord = arrayList1.get(0);
            logger.info("Multiple parts matched. The first part will be associated.");
            searchAndAssignContent.setMatchStatus("Found");
            arrayList.add(iContentProviderResultRecord.getPartProperty("d8ac8dcc").getValue() + "@" + iContentProviderResultRecord.getPartProperty("d8ac8dcc").getValue());
            searchAndAssignContent.setMatchValues(arrayList);
            createCSV.postscriptCSV(searchAndAssignContent);
          } else {
            if (arrayList1.size() == 1) {
              iContentProviderResultRecord = arrayList1.get(0);
              searchAndAssignContent.setMatchStatus("Need user review");
              logger.warn("Found only one in PartNumber '" + str1 + "' search (alphanumeric only), but manufacturerName may be different. ManufuctureName is '" + iContentProviderResultRecord.getPartProperty("6230417e").getValue() + "'.");
              arrayList.add(iContentProviderResultRecord.getPartProperty("d8ac8dcc").getValue() + "@" + iContentProviderResultRecord.getPartProperty("d8ac8dcc").getValue());
              searchAndAssignContent.setMatchValues(arrayList);
              createCSV.postscriptCSV(searchAndAssignContent);
              return false;
            } 
            if (arrayList1.size() > 1) {
              logger.error("Search results contain more than one Part in " + ccp.getName() + ".");
              searchAndAssignContent.setMatchStatus("Multiple");
              for (byte b = 0; b < arrayList1.size(); b++) {
                logger.error("\t" + b + 1 + ": PartNumber '" + ((IContentProviderResultRecord)arrayList1.get(b)).getPartProperty("d8ac8dcc").getValue() + "' by ManufacturerName '" + ((IContentProviderResultRecord)arrayList1.get(b)).getPartProperty("6230417e").getValue() + "'.");
                arrayList.add(((IContentProviderResultRecord)arrayList1.get(b)).getPartProperty("d8ac8dcc").getValue() + "@" + ((IContentProviderResultRecord)arrayList1.get(b)).getPartProperty("d8ac8dcc").getValue());
              } 
              searchAndAssignContent.setMatchValues(arrayList);
              createCSV.postscriptCSV(searchAndAssignContent);
              return false;
            } 
          } 
        } else {
          if (arrayList1.size() == 1) {
            iContentProviderResultRecord = arrayList1.get(0);
            searchAndAssignContent.setMatchStatus("Need user review");
            logger.warn("One exact match found for PartNumber(" + str1 + ") only, but manufacturerName may be different. ManufuctureName is '" + iContentProviderResultRecord.getPartProperty("6230417e").getValue() + "'.");
            arrayList.add(iContentProviderResultRecord.getPartProperty("d8ac8dcc").getValue() + "@" + iContentProviderResultRecord.getPartProperty("d8ac8dcc").getValue());
            searchAndAssignContent.setMatchValues(arrayList);
            createCSV.postscriptCSV(searchAndAssignContent);
            return false;
          } 
          if (arrayList1.size() > 1) {
            logger.error("Search results contain more than one Part in " + ccp.getName() + "with PartNumber '" + str1 + "' exact match.");
            logger.info("Searches for partial matches of ManufuctureName.");
            ArrayList<IContentProviderResultRecord> arrayList3 = new ArrayList();
            for (IContentProviderResultRecord iContentProviderResultRecord1 : arrayList1) {
              if (str2.contains(iContentProviderResultRecord1.getPartProperty("6230417e").getValue()))
                arrayList3.add(iContentProviderResultRecord1); 
            } 
            if (arrayList3.isEmpty()) {
              logger.info("No search results were included in " + str2 + ".");
              for (byte b = 0; b < arrayList1.size(); b++) {
                logger.error("\t" + b + 1 + ": PartNumber'" + ((IContentProviderResultRecord)arrayList1.get(b)).getPartProperty("d8ac8dcc").getValue() + "' by ManufacturerName'" + ((IContentProviderResultRecord)arrayList1.get(b)).getPartProperty("6230417e").getValue() + "'.");
                arrayList.add(((IContentProviderResultRecord)arrayList1.get(b)).getPartProperty("d8ac8dcc").getValue() + "@" + ((IContentProviderResultRecord)arrayList1.get(b)).getPartProperty("d8ac8dcc").getValue());
              } 
              searchAndAssignContent.setMatchValues(arrayList);
              searchAndAssignContent.setMatchStatus("Multiple");
              createCSV.postscriptCSV(searchAndAssignContent);
              return false;
            } 
            if (arrayList3.size() == 1) {
              iContentProviderResultRecord = arrayList3.get(0);
              logger.info("PartNumber '" + str1 + "' found with exact match and ManufuctureName '" + iContentProviderResultRecord.getPartProperty("6230417e").getValue() + "' included.");
              searchAndAssignContent.setMatchStatus("Found");
              arrayList.add(iContentProviderResultRecord.getPartProperty("d8ac8dcc").getValue() + "@" + iContentProviderResultRecord.getPartProperty("d8ac8dcc").getValue());
              searchAndAssignContent.setMatchValues(arrayList);
              createCSV.postscriptCSV(searchAndAssignContent);
            } else {
              logger.info("Found multiple PartNumber '" + str1 + "' exact matches included in ManufuctureName.");
              for (byte b = 0; b < arrayList3.size(); b++) {
                logger.error("\t" + b + 1 + ": PartNumber'" + ((IContentProviderResultRecord)arrayList3.get(b)).getPartProperty("d8ac8dcc").getValue() + "' by ManufacturerName'" + ((IContentProviderResultRecord)arrayList3.get(b)).getPartProperty("6230417e").getValue() + "'.");
                arrayList.add(((IContentProviderResultRecord)arrayList3.get(b)).getPartProperty("d8ac8dcc").getValue() + "@" + ((IContentProviderResultRecord)arrayList3.get(b)).getPartProperty("d8ac8dcc").getValue());
              } 
              searchAndAssignContent.setMatchValues(arrayList);
              searchAndAssignContent.setMatchStatus("Multiple");
              createCSV.postscriptCSV(searchAndAssignContent);
              return false;
            } 
          } 
        } 
      } 
    } catch (ContentProviderException contentProviderException) {
      logger.error("Unable to perform search in " + ccp.getName() + ": " + contentProviderException.getMessage());
      return false;
    } 
    logger.info("Assigning External Content...");
    try {
      ContentProviderSync.assignEC2MPN(ccp, paramOIObject, iContentProviderResultRecord.getIdPropertyMap(ccpCfg));
    } catch (ContentProviderException contentProviderException) {
      logger.error(contentProviderException.getMessage());
      return false;
    } 
    logger.info("Validating Manufacturer Part classification...");
    ContentProviderConfigDMSCatalog contentProviderConfigDMSCatalog = null;
    Collection<ContentProviderConfigDMSCatalog> collection = ccpCfg.getMPNCatalogsByContentProviderId(iContentProviderResultRecord.getPartClassID());
    if (collection == null || collection.isEmpty()) {
      logger.error("While attempting to validate Manufacturer Part classification:  Part Class Content Provider ID '" + iContentProviderResultRecord.getPartClassID() + "' not found in mapping configuration.");
      return false;
    } 
    contentProviderConfigDMSCatalog = collection.iterator().next();
    String str3 = contentProviderConfigDMSCatalog.getClassDMN();
    String str4 = paramOIObject.getOIClass().getName();
    if (!str3.equals(str4)) {
      logger.info("Moving Manufacturer Part from '" + str4 + "' to '" + str3 + "'...");
      OIClass oIClass = getOIClass(str3);
      try {
        om.moveInClassHierarchy(paramOIObject, oIClass);
        om.makePermanent(paramOIObject);
      } catch (Exception exception) {
        logger.error("Unable to move Manufacturer Part from '" + str4 + "' to '" + str3 + "': " + exception.getMessage());
        return false;
      } finally {
        try {
          om.evict(paramOIObject);
        } catch (OIException oIException) {}
      } 
    } 
    return true;
  }
  
  private void outputResult(List<IContentProviderResultRecord> paramList, String paramString, List<String> paramList1) {}
  
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
  
  protected static void setLastSyncDate(Date paramDate) {
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


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\cor\\utils\SearchAndAssignApp.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */