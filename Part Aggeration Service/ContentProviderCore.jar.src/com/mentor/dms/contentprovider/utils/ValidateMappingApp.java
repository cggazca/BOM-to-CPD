package com.mentor.dms.contentprovider.utils;

import au.com.bytecode.opencsv.CSVReader;
import com.mentor.datafusion.oi.OICursor;
import com.mentor.datafusion.oi.OIObjectManager;
import com.mentor.datafusion.oi.OIObjectManagerFactory;
import com.mentor.datafusion.oi.OIQuery;
import com.mentor.datafusion.oi.login.OIAuthenticate;
import com.mentor.datafusion.oi.login.OIAuthenticateFactory;
import com.mentor.datafusion.oi.model.OIClass;
import com.mentor.datafusion.oi.model.OIField;
import com.mentor.datafusion.oi.model.OISetField;
import com.mentor.datafusion.oi.model.OIStringField;
import com.mentor.datafusion.utils.logger.MGLogger;
import com.mentor.dms.contentprovider.AbstractContentProvider;
import com.mentor.dms.contentprovider.ContentProviderFactory;
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
import com.mentor.dms.contentprovider.config.datamodel.DataModelCfgDataModelConfiguration;
import com.mentor.dms.contentprovider.config.datamodel.DataModelCfgExcludePartClasses;
import com.mentor.dms.contentprovider.config.datamodel.DataModelCfgPartClass;
import com.mentor.dms.contentprovider.config.datamodel.DataModelConfigurationXML;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class ValidateMappingApp {
  static MGLogger logger = MGLogger.getLogger(ValidateMappingApp.class);
  
  private static HashSet<String> mpnCatalogs = new HashSet<>();
  
  private static HashSet<String> mappingMPNCatalogs = new HashSet<>();
  
  private static HashSet<String> compCatalogs = new HashSet<>();
  
  private static HashSet<String> mappingCompCatalogs = new HashSet<>();
  
  private static DataModelCfgDataModelConfiguration dataModelCfg = null;
  
  public static void main(String[] paramArrayOfString) {
    logger.info("#");
    logger.info("#                 Validate Mapping - Version 1.16.3");
    logger.info("#                   Validate Mapping Application");
    logger.info("#");
    logger.info("#                Copyright Siemens 2022");
    logger.info("#");
    logger.info("#                      All Rights Reserved.");
    logger.info("#");
    logger.info("#        THIS WORK CONTAINS TRADE SECRET AND PROPRIETARY");
    logger.info("#        INFORMATION WHICH IS THE PROPERTY OF SIEMENS EDA");
    logger.info("#        OR ITS LICENSORS AND IS SUBJECT TO LICENSE TERMS.");
    logger.info("#");
    String str1 = null;
    String str2 = null;
    String str3 = null;
    String str4 = null;
    String str5 = null;
    String str6 = "RootManufacturerPart";
    HashSet<String> hashSet = new HashSet();
    String str7 = null;
    Options options = new Options();
    options.addOption("help", false, "help");
    options.addOption("loginconfig", true, "EDM Library auto-login configuration");
    options.addOption("ccpid", true, "Content Provider ID");
    options.addOption("datamodelconfigfile", true, "Data Model Configuration filename");
    options.addOption("mappingfile", true, "Content Provider Configuration Mapping filename");
    options.addOption("mappingfunctionsfile", true, "Content Provider Configuration Mapping functions Javascript filename");
    options.addOption("mpncatalogs", true, "Comma-separated list of EDM Library Manufacturer Part catalogs");
    options.addOption("ignoremaps", true, "Comma-separated list of mpn domain names to ignore when comparing with component maps");
    options.addOption("ihspropsfile", true, "IHS property CSV file");
    BasicParser basicParser = new BasicParser();
    CommandLine commandLine = null;
    try {
      commandLine = basicParser.parse(options, paramArrayOfString);
    } catch (ParseException parseException) {
      logger.error("Error: " + parseException.getMessage());
      HelpFormatter helpFormatter = new HelpFormatter();
      helpFormatter.printHelp("ValidateMapping", options);
      System.exit(1);
    } 
    if (commandLine.hasOption("help")) {
      HelpFormatter helpFormatter = new HelpFormatter();
      helpFormatter.printHelp("ValidateMapping", options);
      System.exit(0);
    } 
    if (commandLine.hasOption("loginconfig")) {
      str1 = commandLine.getOptionValue("loginconfig");
    } else {
      logger.error("Error: EDM Library auto-login configuration is required.");
      System.exit(1);
    } 
    if (commandLine.hasOption("ccpid")) {
      str2 = commandLine.getOptionValue("ccpid");
    } else {
      logger.error("Error: Content Provider ID is required.");
      System.exit(1);
    } 
    if (commandLine.hasOption("datamodelconfigfile"))
      str3 = commandLine.getOptionValue("datamodelconfigfile"); 
    if (commandLine.hasOption("mappingfile"))
      str4 = commandLine.getOptionValue("mappingfile"); 
    if (commandLine.hasOption("mappingfunctionsfile"))
      str5 = commandLine.getOptionValue("mappingfunctionsfile"); 
    if (commandLine.hasOption("mpncatalogs"))
      str6 = commandLine.getOptionValue("mpncatalogs"); 
    if (commandLine.hasOption("ignoremaps"))
      for (String str : commandLine.getOptionValue("ignoremaps").split("\\s*,\\s*"))
        hashSet.add(str);  
    if (commandLine.hasOption("ihspropsfile"))
      str7 = commandLine.getOptionValue("ihspropsfile"); 
    if (str3 != null) {
      logger.info("Reading data model configuration file...");
      try {
        dataModelCfg = DataModelConfigurationXML.read(str3);
      } catch (ContentProviderConfigException contentProviderConfigException) {
        logger.error(contentProviderConfigException.getMessage());
        return;
      } 
    } 
    OIObjectManagerFactory oIObjectManagerFactory = null;
    OIObjectManager oIObjectManager = null;
    try {
      logger.info("Connecting to EDM Library...");
      OIAuthenticate oIAuthenticate = OIAuthenticateFactory.createBatchAuthenticate(str1);
      oIObjectManagerFactory = oIAuthenticate.login("Validation Application");
      logger.info("Connected");
      oIObjectManager = oIObjectManagerFactory.createObjectManager();
      DataModelUtils dataModelUtils = new DataModelUtils(oIObjectManager);
      ContentProviderConfig contentProviderConfig = null;
      logger.info("Reading mapping configuration...");
      ContentProviderFactory.getInstance().registerContentProviders(oIObjectManager);
      AbstractContentProvider abstractContentProvider = ContentProviderFactory.getInstance().createContentProvider(str2);
      try {
        if (str4 == null) {
          contentProviderConfig = abstractContentProvider.getConfig(true);
        } else {
          contentProviderConfig = new ContentProviderConfig(abstractContentProvider);
          contentProviderConfig.setObjectManager(oIObjectManager);
          if (str5 != null) {
            logger.info("Reading mapping functions script...");
            contentProviderConfig.getScriptEngine().read(new File(str5));
          } 
          contentProviderConfig.read(new FileInputStream(str4));
        } 
      } catch (ContentProviderConfigException contentProviderConfigException) {
        if (contentProviderConfigException.getMessage().contains("Fatal Error")) {
          logger.error(contentProviderConfigException.getMessage());
          return;
        } 
        logger.info(contentProviderConfigException.getMessage());
      } 
      for (String str : str6.split("\\s*,\\s*")) {
        OIClass oIClass = null;
        try {
          oIClass = oIObjectManagerFactory.getClassManager().getOIClass(str);
        } catch (Exception exception) {
          logger.error("  For " + str + " : " + exception.getMessage());
        } 
        addCatalogs(oIClass, mpnCatalogs);
      } 
      ContentProviderConfigPartClass contentProviderConfigPartClass = null;
      HashSet<String> hashSet1 = new HashSet();
      logger.info("Duplicated PartClasses:");
      for (ContentProviderConfigPartClass contentProviderConfigPartClass1 : contentProviderConfig.getPartClasses()) {
        if (contentProviderConfigPartClass1.getParentPartClass() == null)
          contentProviderConfigPartClass = contentProviderConfigPartClass1; 
        if (hashSet1.contains(contentProviderConfigPartClass1.getContentProviderId())) {
          logger.info("  " + contentProviderConfigPartClass1.getContentProviderId());
          continue;
        } 
        hashSet1.add(contentProviderConfigPartClass1.getContentProviderId());
      } 
      if (contentProviderConfig.isDefaultInherit()) {
        logger.info("Duplicated PartClass property names:");
        for (ContentProviderConfigPartClass contentProviderConfigPartClass1 : contentProviderConfig.getPartClasses()) {
          if (contentProviderConfigPartClass1.getChildPartClasses().isEmpty()) {
            HashMap<Object, Object> hashMap = new HashMap<>();
            for (ContentProviderConfigPartClass contentProviderConfigPartClass2 = contentProviderConfigPartClass1; contentProviderConfigPartClass2 != null; contentProviderConfigPartClass2 = contentProviderConfigPartClass2.getParentPartClass()) {
              for (ContentProviderConfigProperty contentProviderConfigProperty : contentProviderConfigPartClass2.getLeafProperties()) {
                HashSet<String> hashSet2 = (HashSet)hashMap.get(contentProviderConfigProperty.getContentProviderId());
                if (hashSet2 == null) {
                  hashSet2 = new HashSet();
                  hashMap.put(contentProviderConfigProperty.getContentProviderId(), hashSet2);
                } 
                hashSet2.add(contentProviderConfigPartClass2.getContentProviderId());
              } 
            } 
            for (Map.Entry<Object, Object> entry : hashMap.entrySet()) {
              if (((HashSet)entry.getValue()).size() > 1) {
                logger.info("  Property '" + (String)entry.getKey() + "' was found in the following part classes:");
                for (String str : entry.getValue())
                  logger.info("    " + str); 
              } 
            } 
          } 
        } 
      } 
      LinkedHashMap<Object, Object> linkedHashMap1 = new LinkedHashMap<>();
      LinkedHashMap<Object, Object> linkedHashMap2 = new LinkedHashMap<>();
      for (ContentProviderConfigPartClass contentProviderConfigPartClass1 : contentProviderConfig.getPartClasses()) {
        if (contentProviderConfigPartClass1.getParentPartClass() == null || isPartClassIgnored(contentProviderConfigPartClass1))
          continue; 
        linkedHashMap1.put(contentProviderConfigPartClass1.getContentProviderId(), Boolean.valueOf(false));
        LinkedHashMap<Object, Object> linkedHashMap = (LinkedHashMap)linkedHashMap2.get(contentProviderConfigPartClass1.getContentProviderId());
        if (linkedHashMap == null) {
          linkedHashMap = new LinkedHashMap<>();
          linkedHashMap2.put(contentProviderConfigPartClass1.getContentProviderId(), linkedHashMap);
        } 
        for (ContentProviderConfigProperty contentProviderConfigProperty : contentProviderConfigPartClass1.getLeafProperties())
          linkedHashMap.put(contentProviderConfigProperty.getContentProviderId(), Boolean.valueOf(false)); 
      } 
      logger.info("Duplicated ManufacturerPartCatalogs:");
      for (ContentProviderConfigMPNCatalog contentProviderConfigMPNCatalog1 : contentProviderConfig.getMPNCatalogs()) {
        linkedHashMap1.put(contentProviderConfigMPNCatalog1.getContentProviderMap().getContentProviderId(), Boolean.valueOf(true));
        if (mappingMPNCatalogs.contains(contentProviderConfigMPNCatalog1.getClassDMN())) {
          logger.info("  " + contentProviderConfigMPNCatalog1.getClassDMN());
          continue;
        } 
        mappingMPNCatalogs.add(contentProviderConfigMPNCatalog1.getClassDMN());
      } 
      ArrayList<String> arrayList1 = new ArrayList<>(mpnCatalogs);
      for (String str : arrayList1) {
        if (mappingMPNCatalogs.contains(str)) {
          mpnCatalogs.remove(str);
          mappingMPNCatalogs.remove(str);
        } 
      } 
      logger.info("Manufacturer Part catalogs in EDM Library which are missing in ManufacturerPartCatalogs mappings:");
      for (String str : mpnCatalogs) {
        OIClass oIClass = null;
        try {
          oIClass = oIObjectManagerFactory.getClassManager().getOIClass(str);
        } catch (Exception exception) {
          logger.error("  For " + str + " : " + exception.getMessage());
          continue;
        } 
        logger.info("  " + dataModelUtils.getCatalogPath(oIClass) + "(" + str + ")");
      } 
      logger.info("ManufacturerPartCatalogs in mapping which don't exist in EDM Library:");
      for (String str : mappingMPNCatalogs) {
        if (!str.equals("RootManufacturerPart"))
          logger.info("  " + str); 
      } 
      logger.info("Discrepancies between EDM Library Manufacturer Part dynamic characteristics and ManufacturerPartCatalog ContentProviderProperty mappings:");
      for (String str : arrayList1) {
        boolean bool1 = false;
        OIClass oIClass = null;
        try {
          oIClass = oIObjectManagerFactory.getClassManager().getOIClass(str);
        } catch (Exception exception) {
          logger.error("  For " + str + " : " + exception.getMessage());
          continue;
        } 
        ContentProviderConfigMPNCatalog contentProviderConfigMPNCatalog1 = contentProviderConfig.getMPNCatalogConfigByDMN(str);
        if (contentProviderConfigMPNCatalog1 == null)
          continue; 
        ContentProviderConfigContentProviderMap contentProviderConfigContentProviderMap = contentProviderConfigMPNCatalog1.getContentProviderMap();
        HashSet<String> hashSet2 = new HashSet();
        for (OIField<?> oIField : (Iterable<OIField<?>>)oIClass.getDeclaredFields()) {
          hashSet2.add(oIField.getName());
          ContentProviderConfigPropertyMap contentProviderConfigPropertyMap = contentProviderConfigContentProviderMap.getPropertyMapByDMN(oIField.getName());
          if (contentProviderConfigPropertyMap == null) {
            if (!bool1) {
              logger.info("  Catalog: " + oIClass.getLabel() + "(" + str + ")");
              bool1 = true;
            } 
            logger.info("    Mapping is missing: '" + dataModelUtils.getInfoLabel(oIField) + "'(" + oIField.getName() + ")");
          } 
        } 
        HashSet<String> hashSet3 = new HashSet();
        for (OIField oIField : oIObjectManagerFactory.getClassManager().getOIClass("RootManufacturerPart").getFields())
          hashSet3.add(oIField.getName()); 
        for (ContentProviderConfigPropertyMap contentProviderConfigPropertyMap : contentProviderConfigContentProviderMap.getLeafPropertyMaps()) {
          if (contentProviderConfigPropertyMap.getDMN() == null)
            continue; 
          if (!hashSet2.contains(contentProviderConfigPropertyMap.getDMN()) && !hashSet3.contains(contentProviderConfigPropertyMap.getDMN())) {
            if (!bool1) {
              logger.info("  Catalog: " + oIClass.getLabel() + "(" + str + ")");
              bool1 = true;
            } 
            logger.info("    EDM Library is missing: " + contentProviderConfigPropertyMap.getDMN());
          } 
          LinkedHashMap<String, Boolean> linkedHashMap = (LinkedHashMap)linkedHashMap2.get(contentProviderConfigContentProviderMap.getContentProviderId());
          if (linkedHashMap != null)
            linkedHashMap.put(contentProviderConfigPropertyMap.getContentProviderId(), Boolean.valueOf(true)); 
        } 
      } 
      logger.info("Unused PartClasses in ManufacturerPartCatalog mappings:");
      for (Map.Entry<Object, Object> entry : linkedHashMap1.entrySet()) {
        if (!((Boolean)entry.getValue()).booleanValue()) {
          ContentProviderConfigPartClass contentProviderConfigPartClass1 = contentProviderConfig.getPartClassByContentProviderId((String)entry.getKey());
          logger.info("  PartClass: " + getPartClassLabelPath(contentProviderConfigPartClass1, " -> ") + " (" + (String)entry.getKey() + ")");
        } 
      } 
      logger.info("Unused top-level PartClass Properties in root ManufacturerPartCatalog:");
      ContentProviderConfigMPNCatalog contentProviderConfigMPNCatalog = contentProviderConfig.getMPNCatalogConfigByDMN("RootManufacturerPart");
      if (contentProviderConfigPartClass != null) {
        for (ContentProviderConfigProperty contentProviderConfigProperty : contentProviderConfigPartClass.getClassProperties()) {
          ContentProviderConfigContentProviderMap contentProviderConfigContentProviderMap = contentProviderConfigMPNCatalog.getContentProviderMaps().iterator().next();
          if (contentProviderConfigContentProviderMap.getPropertyMapByContentProviderId(contentProviderConfigProperty.getContentProviderId()) == null)
            logger.info("  Unused PartClass Property: " + contentProviderConfigProperty.getContentProviderId()); 
        } 
      } else {
        logger.info("Unable to determine root PartClass.");
      } 
      logger.info("Unused PartClass Properties in ManufacturerPartCatalogs:");
      for (Map.Entry<Object, Object> entry : linkedHashMap2.entrySet()) {
        boolean bool1 = false;
        for (Map.Entry entry1 : ((LinkedHashMap)entry.getValue()).entrySet()) {
          if (!((Boolean)entry1.getValue()).booleanValue()) {
            if (!bool1) {
              ContentProviderConfigPartClass contentProviderConfigPartClass1 = contentProviderConfig.getPartClassByContentProviderId((String)entry.getKey());
              logger.info("  PartClass: " + getPartClassLabelPath(contentProviderConfigPartClass1, " -> ") + " (" + (String)entry.getKey() + ")");
              bool1 = true;
            } 
            logger.info("    Unused PartClass Property: " + (String)entry1.getKey());
          } 
        } 
      } 
      for (OIClass oIClass : oIObjectManagerFactory.getClassManager().getOIClass("RootComponent").getSubclasses())
        addCatalogs(oIClass, compCatalogs); 
      linkedHashMap1 = new LinkedHashMap<>();
      linkedHashMap2 = new LinkedHashMap<>();
      for (ContentProviderConfigPartClass contentProviderConfigPartClass1 : contentProviderConfig.getPartClasses()) {
        if (contentProviderConfigPartClass1.getParentPartClass() == null || isPartClassIgnored(contentProviderConfigPartClass1))
          continue; 
        linkedHashMap1.put(contentProviderConfigPartClass1.getContentProviderId(), Boolean.valueOf(false));
        LinkedHashMap<Object, Object> linkedHashMap = (LinkedHashMap)linkedHashMap2.get(contentProviderConfigPartClass1.getContentProviderId());
        if (linkedHashMap == null) {
          linkedHashMap = new LinkedHashMap<>();
          linkedHashMap2.put(contentProviderConfigPartClass1.getContentProviderId(), linkedHashMap);
        } 
        for (ContentProviderConfigProperty contentProviderConfigProperty : contentProviderConfigPartClass1.getLeafProperties())
          linkedHashMap.put(contentProviderConfigProperty.getContentProviderId(), Boolean.valueOf(false)); 
      } 
      logger.info("Duplicated ComponentCatalogs:");
      for (ContentProviderConfigComponentCatalog contentProviderConfigComponentCatalog : contentProviderConfig.getComponentCatalogs()) {
        for (ContentProviderConfigContentProviderMap contentProviderConfigContentProviderMap : contentProviderConfigComponentCatalog.getContentProviderMaps())
          linkedHashMap1.put(contentProviderConfigContentProviderMap.getContentProviderId(), Boolean.valueOf(true)); 
        if (mappingCompCatalogs.contains(contentProviderConfigComponentCatalog.getClassDMN())) {
          logger.info("  " + contentProviderConfigComponentCatalog.getClassDMN());
          continue;
        } 
        mappingCompCatalogs.add(contentProviderConfigComponentCatalog.getClassDMN());
      } 
      ArrayList<String> arrayList2 = new ArrayList<>(compCatalogs);
      for (String str : arrayList2) {
        if (mappingCompCatalogs.contains(str)) {
          compCatalogs.remove(str);
          mappingCompCatalogs.remove(str);
        } 
      } 
      logger.info("Component catalogs in EDM Library which are missing in ComponentCatalogs mappings:");
      for (String str : compCatalogs) {
        OIClass oIClass = null;
        try {
          oIClass = oIObjectManagerFactory.getClassManager().getOIClass(str);
        } catch (Exception exception) {
          logger.error("  For " + str + " : " + exception.getMessage());
          continue;
        } 
        logger.info("  '" + oIClass.getLabel() + "' (" + str + ")");
      } 
      logger.info("ComponentCatalogs in mapping which don't exist in EDM Library:");
      for (String str : mappingCompCatalogs) {
        if (!str.equals("RootComponent")) {
          OIClass oIClass = null;
          try {
            oIClass = oIObjectManagerFactory.getClassManager().getOIClass(str);
          } catch (Exception exception) {
            logger.error("  For " + str + " : " + exception.getMessage());
            continue;
          } 
          logger.info("  '" + oIClass.getLabel() + "' (" + str + ")");
        } 
      } 
      logger.info("Discrepancies between EDM Library Component dynamic characteristics and ComponentCatalog ContentProviderProperty mappings:");
      for (String str : arrayList2) {
        boolean bool1 = false;
        OIClass oIClass = null;
        try {
          oIClass = oIObjectManagerFactory.getClassManager().getOIClass(str);
        } catch (Exception exception) {
          logger.error("  For " + str + " : " + exception.getMessage());
          continue;
        } 
        ContentProviderConfigComponentCatalog contentProviderConfigComponentCatalog = contentProviderConfig.getComponentCatalogConfigByDMN(str);
        if (contentProviderConfigComponentCatalog == null)
          continue; 
        for (ContentProviderConfigContentProviderMap contentProviderConfigContentProviderMap : contentProviderConfigComponentCatalog.getContentProviderMaps()) {
          HashSet<String> hashSet2 = new HashSet();
          for (OIField oIField : oIClass.getDeclaredFields())
            hashSet2.add(oIField.getName()); 
          HashSet<String> hashSet3 = new HashSet();
          for (OIField oIField : oIObjectManagerFactory.getClassManager().getOIClass("RootComponent").getFields())
            hashSet3.add(oIField.getName()); 
          HashSet<String> hashSet4 = new HashSet();
          for (OIField oIField : oIClass.getFields()) {
            if (oIField.getType() == OIField.Type.SET) {
              OIClass oIClass1 = ((OISetField)oIField).getContentType();
              for (OIField oIField1 : oIClass1.getDeclaredFields())
                hashSet4.add(oIField1.getName()); 
            } 
          } 
          for (ContentProviderConfigPropertyMap contentProviderConfigPropertyMap : contentProviderConfigContentProviderMap.getCatalogPropertyMaps()) {
            if (contentProviderConfigPropertyMap.getDMN() == null)
              continue; 
            if (!hashSet2.contains(contentProviderConfigPropertyMap.getDMN()) && !hashSet3.contains(contentProviderConfigPropertyMap.getDMN()) && !hashSet4.contains(contentProviderConfigPropertyMap.getDMN())) {
              if (!bool1) {
                logger.info("  Catalog: " + str);
                bool1 = true;
              } 
              logger.info("    EDM Library is missing: " + contentProviderConfigPropertyMap.getDMN());
            } 
            LinkedHashMap<String, Boolean> linkedHashMap = (LinkedHashMap)linkedHashMap2.get(contentProviderConfigContentProviderMap.getContentProviderId());
            if (linkedHashMap != null)
              linkedHashMap.put(contentProviderConfigPropertyMap.getContentProviderId(), Boolean.valueOf(true)); 
          } 
        } 
      } 
      boolean bool = (dataModelCfg != null && dataModelCfg.isComponentSearch()) ? true : false;
      if (bool) {
        logger.info("Unused PartClasses in ComponentCatalog mappings:");
        for (Map.Entry<Object, Object> entry : linkedHashMap1.entrySet()) {
          if (!((Boolean)entry.getValue()).booleanValue()) {
            ContentProviderConfigPartClass contentProviderConfigPartClass1 = contentProviderConfig.getPartClassByContentProviderId((String)entry.getKey());
            logger.info("  PartClass: " + getPartClassLabelPath(contentProviderConfigPartClass1, " -> ") + " (" + (String)entry.getKey() + ")");
          } 
        } 
        logger.info("Unused PartClass Properties in ComponentCatalogs:");
        for (Map.Entry<Object, Object> entry : linkedHashMap2.entrySet()) {
          boolean bool1 = false;
          for (Map.Entry entry1 : ((LinkedHashMap)entry.getValue()).entrySet()) {
            if (!((Boolean)entry1.getValue()).booleanValue()) {
              if (!bool1) {
                ContentProviderConfigPartClass contentProviderConfigPartClass1 = contentProviderConfig.getPartClassByContentProviderId((String)entry.getKey());
                logger.info("  PartClass: " + getPartClassLabelPath(contentProviderConfigPartClass1, " -> ") + " (" + (String)entry.getKey() + ")");
                bool1 = true;
              } 
              logger.info("    Unused PartClass Property: " + (String)entry1.getKey());
            } 
          } 
        } 
      } 
      logger.info("Leaf Manufacturer Part Catalogs that are not mapped in any ComponentCatalog:");
      for (ContentProviderConfigMPNCatalog contentProviderConfigMPNCatalog1 : contentProviderConfig.getMPNCatalogs()) {
        OIClass oIClass = null;
        try {
          oIClass = oIObjectManagerFactory.getClassManager().getOIClass(contentProviderConfigMPNCatalog1.getClassDMN());
        } catch (Exception exception) {
          logger.error("  For " + contentProviderConfigMPNCatalog1.getClassDMN() + " : " + exception.getMessage());
          continue;
        } 
        if (oIClass == null)
          continue; 
        boolean bool1 = true;
        for (ContentProviderConfigMPNCatalog contentProviderConfigMPNCatalog2 : contentProviderConfig.getMPNCatalogs()) {
          if (contentProviderConfigMPNCatalog2.getParentDMSCatalog() == contentProviderConfigMPNCatalog1) {
            bool1 = false;
            break;
          } 
        } 
        Collection collection = contentProviderConfig.getComponentCatalogsByMPNDMN(contentProviderConfigMPNCatalog1.getClassDMN());
        if (bool1 && collection.isEmpty())
          logger.info("  Manufacturer Part Catalog '" + oIClass.getLabel() + "' (" + contentProviderConfigMPNCatalog1.getClassDMN() + ")"); 
      } 
      logger.info("Leaf-level property mappings from Content Provider to Manufacturer Part that are not in Manufacturer Part to Component Mapping:");
      for (ContentProviderConfigComponentCatalog contentProviderConfigComponentCatalog : contentProviderConfig.getComponentCatalogs()) {
        boolean bool1 = false;
        boolean bool2 = true;
        for (ContentProviderConfigComponentCatalog contentProviderConfigComponentCatalog1 : contentProviderConfig.getComponentCatalogs()) {
          if (contentProviderConfigComponentCatalog1.getParentDMSCatalog() == contentProviderConfigComponentCatalog) {
            bool2 = false;
            break;
          } 
        } 
        if (!bool2)
          continue; 
        OIClass oIClass = null;
        try {
          oIClass = oIObjectManagerFactory.getClassManager().getOIClass(contentProviderConfigComponentCatalog.getClassDMN());
        } catch (Exception exception) {
          logger.error("  For " + contentProviderConfigComponentCatalog.getClassDMN() + " : " + exception.getMessage());
          continue;
        } 
        for (ContentProviderConfigManufacturerPartMap contentProviderConfigManufacturerPartMap : contentProviderConfigComponentCatalog.getManufacturerPartMaps()) {
          boolean bool3 = false;
          OIClass oIClass1 = null;
          try {
            oIClass1 = oIObjectManagerFactory.getClassManager().getOIClass(contentProviderConfigManufacturerPartMap.getClassDMN());
          } catch (Exception exception) {
            logger.error("  For " + contentProviderConfigManufacturerPartMap.getClassDMN() + " : " + exception.getMessage());
            continue;
          } 
          ContentProviderConfigMPNCatalog contentProviderConfigMPNCatalog1 = contentProviderConfig.getMPNCatalogConfigByDMN(contentProviderConfigManufacturerPartMap.getClassDMN());
          if (contentProviderConfigMPNCatalog1 == null)
            continue; 
          HashSet<String> hashSet2 = new HashSet();
          for (AbstractContentProviderConfigManufacturerPartPropertyMap abstractContentProviderConfigManufacturerPartPropertyMap : contentProviderConfigManufacturerPartMap.getCatalogComponentPropertyMaps()) {
            if (abstractContentProviderConfigManufacturerPartPropertyMap instanceof ContentProviderConfigManufacturerPartPropertyMap) {
              hashSet2.add(((ContentProviderConfigManufacturerPartPropertyMap)abstractContentProviderConfigManufacturerPartPropertyMap).getManufacturerPartPropertyDMN());
              continue;
            } 
            if (abstractContentProviderConfigManufacturerPartPropertyMap instanceof ContentProviderConfigScriptedManufacturerPartPropertyMap) {
              ContentProviderConfigScriptedManufacturerPartPropertyMap contentProviderConfigScriptedManufacturerPartPropertyMap = (ContentProviderConfigScriptedManufacturerPartPropertyMap)abstractContentProviderConfigManufacturerPartPropertyMap;
              ContentProviderConfigScriptEngine contentProviderConfigScriptEngine = contentProviderConfig.getScriptEngine();
              try {
                contentProviderConfigScriptEngine.callMappingFunction(oIClass1, oIClass, contentProviderConfigScriptedManufacturerPartPropertyMap.getMappingFunction());
                for (String str : contentProviderConfigScriptEngine.getManufacturerPartPropertyMap().getReferencedProps())
                  hashSet2.add(str); 
              } catch (ContentProviderConfigException contentProviderConfigException) {}
            } 
          } 
          for (ContentProviderConfigPropertyMap contentProviderConfigPropertyMap : contentProviderConfigMPNCatalog1.getContentProviderMap().getLeafPropertyMaps()) {
            if (!hashSet2.contains(contentProviderConfigPropertyMap.getDMN())) {
              OIField<?> oIField = dataModelUtils.findOIFieldParametricByDMN(oIClass1, contentProviderConfigPropertyMap.getDMN());
              if (oIField != null) {
                String str = dataModelUtils.getInfoLabel(oIField);
                if (!bool1) {
                  logger.info("  Component Catalog: '" + oIClass.getLabel() + "' (" + contentProviderConfigComponentCatalog.getClassDMN() + ")");
                  bool1 = true;
                } 
                if (!bool3) {
                  logger.info("    Manufacturer Part Catalog: '" + oIClass1.getLabel() + "' (" + contentProviderConfigManufacturerPartMap.getClassDMN() + ")");
                  bool3 = true;
                } 
                logger.info("      Manufacturer Part property: '" + str + "' (" + contentProviderConfigPropertyMap.getDMN() + ")");
              } 
            } 
          } 
        } 
      } 
      if (str7 != null) {
        HashMap<Object, Object> hashMap1 = new HashMap<>();
        HashMap<Object, Object> hashMap2 = new HashMap<>();
        HashMap<Object, Object> hashMap3 = new HashMap<>();
        logger.info("Reading IHS Properties file...");
        CSVReader cSVReader = new CSVReader(new FileReader(str7), ',', '"', '\\', 1);
        String[] arrayOfString;
        while ((arrayOfString = cSVReader.readNext()) != null)
          hashMap3.put(arrayOfString[0], arrayOfString); 
        cSVReader.close();
        HashMap<Object, Object> hashMap4 = new HashMap<>();
        OIQuery oIQuery = oIObjectManager.createQuery("Unit", true);
        oIQuery.addColumn("UnitKey");
        oIQuery.addColumn("UnitDefinition.RangeName");
        OICursor oICursor = oIQuery.execute();
        while (oICursor.next()) {
          HashSet<String> hashSet2 = (HashSet)hashMap4.get(oICursor.getString("UnitKey"));
          if (hashSet2 == null) {
            hashSet2 = new HashSet();
            hashMap4.put(oICursor.getString("UnitKey"), hashSet2);
          } 
          hashSet2.add(oICursor.getString("RangeName"));
        } 
        oICursor.close();
        logger.info("Validating data types and UNITS:");
        for (ContentProviderConfigMPNCatalog contentProviderConfigMPNCatalog1 : contentProviderConfig.getMPNCatalogs()) {
          OIClass oIClass = oIObjectManager.getObjectManagerFactory().getClassManager().getOIClass(contentProviderConfigMPNCatalog1.getClassDMN());
          if (oIClass == null)
            continue; 
          for (ContentProviderConfigContentProviderMap contentProviderConfigContentProviderMap : contentProviderConfigMPNCatalog1.getContentProviderMaps()) {
            ContentProviderConfigPartClass contentProviderConfigPartClass1 = contentProviderConfig.getPartClassByContentProviderId(contentProviderConfigContentProviderMap.getContentProviderId());
            if (contentProviderConfigPartClass1 == null)
              continue; 
            for (ContentProviderConfigPropertyMap contentProviderConfigPropertyMap : contentProviderConfigContentProviderMap.getLeafPropertyMaps()) {
              if (contentProviderConfigPropertyMap.getSyncType() == ContentProviderConfig.PropertySyncType.IGNORE)
                continue; 
              String[] arrayOfString1 = (String[])hashMap3.get(contentProviderConfigPropertyMap.getContentProviderId());
              if (arrayOfString1 == null) {
                logger.info("  IHS Property object ID '" + contentProviderConfigPropertyMap.getContentProviderId() + "' not found in IHS properties file. ");
                continue;
              } 
              OIField oIField = oIClass.getField(contentProviderConfigPropertyMap.getDMN());
              if (oIField == null)
                continue; 
              if (oIField.getType() == OIField.Type.STRING) {
                OIStringField oIStringField = (OIStringField)oIField.as(OIStringField.class);
                String str = arrayOfString1[4];
                if (!str.equals("VARCHAR2")) {
                  logger.info("  For MPN Catalog dmn = '" + contentProviderConfigMPNCatalog1.getClassDMN() + "' characteristic dmn = '" + contentProviderConfigPropertyMap.getDMN() + "' type is STRING.");
                  logger.info("    IHS Property object ID '" + contentProviderConfigPropertyMap.getContentProviderId() + "' type is " + str);
                } 
                int i = oIStringField.getMaximalLength();
                int j = Integer.parseInt(arrayOfString1[5]);
                hashMap1.put(contentProviderConfigPropertyMap.getDMN(), Integer.valueOf(i));
                Integer integer = (Integer)hashMap2.get(contentProviderConfigPropertyMap.getDMN());
                if (integer == null || j > integer.intValue())
                  hashMap2.put(contentProviderConfigPropertyMap.getDMN(), Integer.valueOf(j)); 
                continue;
              } 
              if (oIField.getType() == OIField.Type.DOUBLE || oIField.getType() == OIField.Type.INTEGER) {
                ContentProviderConfigProperty contentProviderConfigProperty = contentProviderConfigPartClass1.getClassPropertyByContentProviderId(contentProviderConfigPropertyMap.getContentProviderId());
                if (contentProviderConfigProperty != null && !contentProviderConfigProperty.getBaseUnits().isEmpty()) {
                  String str = oIField.getUnitName();
                  if (str != null) {
                    HashSet hashSet2 = (HashSet)hashMap4.get(oIField.getUnitName());
                    if (hashSet2 != null) {
                      if (!hashSet2.contains(contentProviderConfigProperty.getBaseUnits())) {
                        logger.info("  For MPN Catalog dmn = '" + contentProviderConfigMPNCatalog1.getClassDMN() + "' characteristic dmn = '" + contentProviderConfigPropertyMap.getDMN() + "' has Patternmatch  = '" + oIField.getUnitName() + "'");
                        logger.info("    IHS Property object ID '" + contentProviderConfigPropertyMap.getContentProviderId() + "' has baseUnits = '" + contentProviderConfigProperty.getBaseUnits() + "' which does not exist in referenced Units.");
                      } 
                      continue;
                    } 
                    logger.info("  For MPN Catalog dmn = '" + contentProviderConfigMPNCatalog1.getClassDMN() + "' characteristic dmn = '" + contentProviderConfigPropertyMap.getDMN() + "' has Patternmatch  = '" + oIField.getUnitName() + "' which is not found in Units class.");
                    logger.info("    IHS Property object ID '" + contentProviderConfigPropertyMap.getContentProviderId() + "' has baseUnits = '" + contentProviderConfigProperty.getBaseUnits() + "'.");
                    continue;
                  } 
                  logger.info("  For MPN Catalog dmn = '" + contentProviderConfigMPNCatalog1.getClassDMN() + "' characteristic dmn = '" + contentProviderConfigPropertyMap.getDMN() + "' has an empty Patternmatch.");
                  logger.info("    IHS Property object ID '" + contentProviderConfigPropertyMap.getContentProviderId() + "' has baseUnits = '" + contentProviderConfigProperty.getBaseUnits() + "'.");
                } 
              } 
            } 
          } 
        } 
        logger.info("Validating maximum string lengths:");
        for (Map.Entry<Object, Object> entry : hashMap2.entrySet()) {
          String str = (String)entry.getKey();
          int i = ((Integer)entry.getValue()).intValue();
          int j = ((Integer)hashMap1.get(str)).intValue();
          if (i != j) {
            logger.info("  Characteristic dmn = '" + str + "' has length = " + j);
            logger.info("    IHS Properties mapped to this characteristic have maximum length = " + i);
          } 
        } 
      } 
      logger.info("\nValidation complete!");
    } catch (Exception exception) {
      exception.printStackTrace();
    } finally {
      if (oIObjectManager != null)
        oIObjectManager.close(); 
      if (oIObjectManagerFactory != null)
        oIObjectManagerFactory.close(); 
    } 
  }
  
  private static void addCatalogs(OIClass paramOIClass, HashSet<String> paramHashSet) {
    paramHashSet.add(paramOIClass.getName());
    for (OIClass oIClass : paramOIClass.getSubclasses())
      addCatalogs(oIClass, paramHashSet); 
  }
  
  private static boolean isPartClassIgnored(ContentProviderConfigPartClass paramContentProviderConfigPartClass) {
    if (dataModelCfg == null)
      return false; 
    DataModelCfgExcludePartClasses dataModelCfgExcludePartClasses = dataModelCfg.getExcludePartClasses();
    if (dataModelCfgExcludePartClasses == null)
      return false; 
    for (DataModelCfgPartClass dataModelCfgPartClass : dataModelCfgExcludePartClasses.getPartClass()) {
      String str = getPartClassLabelPath(paramContentProviderConfigPartClass, ".");
      if (str.startsWith(dataModelCfgPartClass.getPath()))
        return true; 
    } 
    return false;
  }
  
  private static String getPartClassLabelPath(ContentProviderConfigPartClass paramContentProviderConfigPartClass, String paramString) {
    return (paramContentProviderConfigPartClass.getParentPartClass() != null) ? paramContentProviderConfigPartClass.getContentProviderLabel() : (getPartClassLabelPath(paramContentProviderConfigPartClass.getParentPartClass(), paramString) + getPartClassLabelPath(paramContentProviderConfigPartClass.getParentPartClass(), paramString) + paramString);
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovide\\utils\ValidateMappingApp.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */