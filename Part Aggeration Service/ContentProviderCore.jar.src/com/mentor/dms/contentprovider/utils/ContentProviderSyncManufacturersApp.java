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
import com.mentor.dms.contentprovider.ContentProviderFactory;
import com.mentor.dms.contentprovider.ContentProviderManufacturer;
import com.mentor.dms.contentprovider.ContentProviderManufacturerProperty;
import com.mentor.dms.contentprovider.config.ContentProviderConfig;
import com.mentor.dms.contentprovider.config.xml.ConfigXMLManufacturerPropertyMap;
import com.mentor.dms.contentprovider.sync.ContentProviderSync;
import java.io.File;
import java.io.FileInputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class ContentProviderSyncManufacturersApp {
  private static MGLogger logger = MGLogger.getLogger(ContentProviderSyncManufacturersApp.class);
  
  private static String dmsConfigName = null;
  
  private static String ccpId = null;
  
  private static String mappingFile = null;
  
  private static boolean bDebug = false;
  
  private static OIObjectManager om = null;
  
  private static AbstractContentProvider ccp = null;
  
  private static OIClass mfgOIClass = null;
  
  public static void main(String[] paramArrayOfString) {
    Options options = new Options();
    options.addOption("help", false, "help");
    options.addOption("loginconfig", true, "EDM Library auto-login configuration");
    options.addOption("ccpid", true, "Content Provider ID");
    options.addOption("mappingfile", true, "Content Provider mapping file");
    options.addOption("debug", false, "Debug mode");
    BasicParser basicParser = new BasicParser();
    CommandLine commandLine = null;
    try {
      commandLine = basicParser.parse(options, paramArrayOfString);
    } catch (ParseException parseException) {
      System.err.println("Error: " + parseException.getMessage());
      HelpFormatter helpFormatter = new HelpFormatter();
      helpFormatter.printHelp("ContentProviderSyncManufacturers", options);
      System.exit(1);
    } 
    if (commandLine.hasOption("help")) {
      HelpFormatter helpFormatter = new HelpFormatter();
      helpFormatter.printHelp("ContentProviderSyncManufacturers", options);
      System.exit(0);
    } 
    if (commandLine.hasOption("loginconfig")) {
      dmsConfigName = commandLine.getOptionValue("loginconfig");
    } else {
      System.err.println("Error: EDM Library auto-login configuration (-loginconfig) is required.");
      System.exit(1);
    } 
    if (commandLine.hasOption("ccpid")) {
      ccpId = commandLine.getOptionValue("ccpid");
    } else {
      System.err.println("Error: Content Provider ID (-ccpid) is required.");
      System.exit(1);
    } 
    mappingFile = commandLine.getOptionValue("mappingfile");
    bDebug = commandLine.hasOption("debug");
    System.out.println("#");
    System.out.println("#       Content Provider Sync Manufacturers - Version 1.16.3");
    System.out.println("#");
    System.out.println("#                   Copyright Siemens 2022");
    System.out.println("#");
    System.out.println("#                      All Rights Reserved.");
    System.out.println("#");
    System.out.println("#        THIS WORK CONTAINS TRADE SECRET AND PROPRIETARY");
    System.out.println("#        INFORMATION WHICH IS THE PROPERTY OF SIEMENS");
    System.out.println("#        OR ITS LICENSORS AND IS SUBJECT TO LICENSE TERMS.");
    System.out.println("#");
    ContentProviderSyncManufacturersApp contentProviderSyncManufacturersApp = new ContentProviderSyncManufacturersApp();
    contentProviderSyncManufacturersApp.doSync();
  }
  
  private void doSync() {
    OIObjectManagerFactory oIObjectManagerFactory = null;
    try {
      logger.info("Connecting to EDM Library...");
      OIAuthenticate oIAuthenticate = OIAuthenticateFactory.createBatchAuthenticate(dmsConfigName);
      oIObjectManagerFactory = oIAuthenticate.login("Content Provider Manufacturer Synchronization Application");
      om = oIObjectManagerFactory.createObjectManager();
      logger.info("Connected to EDM Library!");
      logger.info("Initalizing ContentProvider...");
      ContentProviderFactory.getInstance().registerContentProviders(om);
      ccp = ContentProviderFactory.getInstance().createContentProvider(ccpId);
      ContentProviderConfig contentProviderConfig = null;
      if (mappingFile == null) {
        contentProviderConfig = ccp.getConfig();
      } else {
        contentProviderConfig = new ContentProviderConfig(ccp);
        contentProviderConfig.setObjectManager(om);
        contentProviderConfig.setValidateScriptedMappings(false);
        contentProviderConfig.read(new FileInputStream(new File(mappingFile)));
      } 
      String str1 = contentProviderConfig.getManufacturersCCPManufacturerName();
      if (str1 == null) {
        logger.error("Content Provider Mapping does not contain mapping configuration for Manufacturers.  Unable to synchronize Manufacturers.");
        return;
      } 
      mfgOIClass = oIObjectManagerFactory.getClassManager().getOIClass("Manufacturer");
      boolean bool = true;
      for (ConfigXMLManufacturerPropertyMap configXMLManufacturerPropertyMap : contentProviderConfig.getManufacturersPropertyMaps()) {
        if (!mfgOIClass.hasField(configXMLManufacturerPropertyMap.getDmn())) {
          logger.error("Characteristic with domain name '" + configXMLManufacturerPropertyMap.getDmn() + "' does not exist in EDM Manufacturer class.");
          bool = false;
        } 
      } 
      if (!bool) {
        logger.error("Invalid Manufacturer mapping configuration.");
        return;
      } 
      String str2 = contentProviderConfig.getManufacturersCCPManufacturerID();
      if (str2 == null)
        logger.warn("Content Provider Mapping does not contain mapping to EDM 'Manufacturer ID'.  New Manufacturers will not be created."); 
      String str3 = contentProviderConfig.getManufacturersCCPID();
      if (str3 == null)
        logger.info("Content Provider Mapping does not contain mapping to 'EDM Content Provider Mfg ID'.  Synchronization will be performed based on Manufacturer Names."); 
      boolean bool1 = mfgOIClass.hasField("ECMfgID");
      logger.info("Reading EDM Library Manufacturers...");
      HashSet<String> hashSet = new HashSet();
      HashMap<Object, Object> hashMap1 = new HashMap<>();
      HashMap<Object, Object> hashMap2 = new HashMap<>();
      OIQuery oIQuery = om.createQuery("Manufacturer", true);
      oIQuery.addColumn("ManufacturerId");
      oIQuery.addColumn("ManufacturerName");
      if (bool1)
        oIQuery.addColumn("ECMfgID"); 
      OICursor oICursor = oIQuery.execute();
      while (oICursor.next()) {
        String str = oICursor.getString("ManufacturerId");
        hashSet.add(str);
        if (bool1) {
          String str4 = oICursor.getString("ECMfgID");
          if (!str4.trim().isEmpty())
            hashMap1.put(str4, str); 
        } 
        hashMap2.put(oICursor.getString("ManufacturerName"), str);
      } 
      oICursor.close();
      byte b = 1;
      Collection collection = ccp.getAvailableManufacturers();
      for (ContentProviderManufacturer contentProviderManufacturer : collection) {
        ContentProviderManufacturerProperty contentProviderManufacturerProperty = contentProviderManufacturer.getPropertyByName(str1);
        if (contentProviderManufacturerProperty == null || (((contentProviderManufacturerProperty.getValue() == null) ? 1 : 0) | contentProviderManufacturerProperty.getValue().isBlank()) != 0) {
          logger.warn("Manufacturer Name not returned from Content Provider.");
          continue;
        } 
        String str4 = contentProviderManufacturerProperty.getValue();
        String str5 = null;
        if (str2 != null && !str2.isBlank()) {
          ContentProviderManufacturerProperty contentProviderManufacturerProperty1 = contentProviderManufacturer.getPropertyByName(str2);
          if (contentProviderManufacturerProperty1 != null && contentProviderManufacturerProperty1.getValue() != null && !contentProviderManufacturerProperty1.getValue().isBlank())
            str5 = contentProviderManufacturerProperty1.getValue(); 
        } 
        String str6 = null;
        String str7 = null;
        if (bool1 && str3 != null && !str3.isBlank()) {
          ContentProviderManufacturerProperty contentProviderManufacturerProperty1 = contentProviderManufacturer.getPropertyByName(str3);
          if (contentProviderManufacturerProperty1 != null && contentProviderManufacturerProperty1.getValue() != null && !contentProviderManufacturerProperty1.getValue().isBlank()) {
            str7 = contentProviderManufacturerProperty1.getValue();
            str6 = (String)hashMap1.get(str7);
          } 
        } 
        if (str6 == null && str2 != null && !str2.isBlank()) {
          ContentProviderManufacturerProperty contentProviderManufacturerProperty1 = contentProviderManufacturer.getPropertyByName(str2);
          if (str5 != null && !str5.isBlank() && hashSet.contains(str5))
            str6 = str5; 
        } 
        if (str6 == null)
          str6 = (String)hashMap2.get(contentProviderManufacturerProperty.getValue()); 
        if (str6 == null && str2 == null) {
          logger.debug("Manufacturer '" + str4 + "' will not be created.");
          continue;
        } 
        logger.info("" + b++ + " : Processing Manufacturer '" + b++ + "'...");
        OIObject oIObject = null;
        try {
          boolean bool2 = false;
          if (str6 == null) {
            logger.info("Creating Manufacturer '" + str4 + "' (" + str5 + ")...");
            oIObject = om.createObject("Manufacturer");
            bool2 = true;
          } else {
            oIObject = om.getObjectByID(str6, "Manufacturer", false);
          } 
          bool2 |= ContentProviderSync.setManufacturerProperty(oIObject, "ManufacturerId", str5);
          bool2 |= ContentProviderSync.setManufacturerProperty(oIObject, "ManufacturerName", str4);
          if (str7 != null)
            bool2 |= ContentProviderSync.setManufacturerProperty(oIObject, "ECMfgID", str7); 
          for (ConfigXMLManufacturerPropertyMap configXMLManufacturerPropertyMap : contentProviderConfig.getManufacturersPropertyMaps())
            bool2 |= ContentProviderSync.setManufacturerProperty(oIObject, contentProviderManufacturer, configXMLManufacturerPropertyMap); 
          if (bool2) {
            if (oIObject.getMode() != 1)
              logger.info("Updating Manufacturer '" + str4 + "' (" + str5 + ")..."); 
            try {
              if (!bDebug)
                om.makePermanent(oIObject); 
            } catch (OIException oIException) {
              logger.error("Unable to create/save Manufacturer '" + str4 + "' (" + str5 + "): " + oIException.getMessage());
            } 
          } else {
            logger.error("No updates required.");
          } 
        } finally {
          if (oIObject != null)
            om.evict(oIObject); 
        } 
      } 
      logger.info("\nManufacturer synchronization completed.");
    } catch (Exception exception) {
      exception.printStackTrace();
    } finally {
      if (oIObjectManagerFactory != null)
        oIObjectManagerFactory.close(); 
      if (om != null)
        om.close(); 
    } 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovide\\utils\ContentProviderSyncManufacturersApp.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */