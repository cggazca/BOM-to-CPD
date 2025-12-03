package com.mentor.dms.contentprovider.core.utils;

import com.mentor.datafusion.oi.OICursor;
import com.mentor.datafusion.oi.OIException;
import com.mentor.datafusion.oi.OIObjectManager;
import com.mentor.datafusion.oi.OIObjectManagerFactory;
import com.mentor.datafusion.oi.OIQuery;
import com.mentor.datafusion.oi.login.OIAuthenticate;
import com.mentor.datafusion.oi.login.OIAuthenticateFactory;
import com.mentor.datafusion.oi.model.OIClass;
import com.mentor.datafusion.oi.model.OIField;
import com.mentor.datafusion.oi.model.OIFieldNotFoundException;
import com.mentor.datafusion.utils.logger.MGLogger;
import com.mentor.dms.contentprovider.core.AbstractContentProvider;
import com.mentor.dms.contentprovider.core.ContentProviderFactory;
import com.mentor.dms.contentprovider.core.ContentProviderGlobal;
import com.mentor.dms.contentprovider.core.config.AbstractContentProviderConfigManufacturerPartPropertyMap;
import com.mentor.dms.contentprovider.core.config.ContentProviderConfig;
import com.mentor.dms.contentprovider.core.config.ContentProviderConfigComponentCatalog;
import com.mentor.dms.contentprovider.core.config.ContentProviderConfigContentProviderMap;
import com.mentor.dms.contentprovider.core.config.ContentProviderConfigException;
import com.mentor.dms.contentprovider.core.config.ContentProviderConfigMPNCatalog;
import com.mentor.dms.contentprovider.core.config.ContentProviderConfigManufacturerPartMap;
import com.mentor.dms.contentprovider.core.config.ContentProviderConfigManufacturerPartPropertyMap;
import com.mentor.dms.contentprovider.core.config.ContentProviderConfigPartClass;
import com.mentor.dms.contentprovider.core.config.ContentProviderConfigProperty;
import com.mentor.dms.contentprovider.core.config.ContentProviderConfigPropertyMap;
import com.mentor.dms.contentprovider.core.config.SearchCapability;
import com.mentor.dms.contentprovider.core.config.datamodel.DataModelCfgDataModelConfiguration;
import com.mentor.dms.contentprovider.core.config.datamodel.DataModelCfgExcludePartClasses;
import com.mentor.dms.contentprovider.core.config.datamodel.DataModelCfgPartClass;
import com.mentor.dms.contentprovider.core.config.datamodel.DataModelConfigurationXML;
import com.mentor.dms.contentprovider.core.utils.validate.CPPartClass;
import com.mentor.dms.contentprovider.core.utils.validate.CPProperty;
import com.mentor.dms.contentprovider.core.utils.validate.OutputResult;
import com.mentor.dms.contentprovider.core.utils.validate.OutputResultCSV;
import com.mentor.dms.contentprovider.core.utils.validate.ValidateResult;
import java.io.File;
import java.io.FileInputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class ValidateMappingApp {
  static MGLogger logger = MGLogger.getLogger(ValidateMappingApp.class);
  
  private static OIObjectManagerFactory omf = null;
  
  private static OIObjectManager om = null;
  
  private static ContentProviderConfig config = null;
  
  private static AbstractContentProvider cpp;
  
  private static List<CPPartClass> partListCP;
  
  private static HashSet<String> mpnCatalogs = new HashSet<>();
  
  private static HashSet<String> mappingMPNCatalogs = new HashSet<>();
  
  private static HashSet<String> compCatalogs = new HashSet<>();
  
  private static HashSet<String> mappingCompCatalogs = new HashSet<>();
  
  private static DataModelCfgDataModelConfiguration dataModelCfg = null;
  
  private static HashMap<String, SearchCapability> searchPropertyMap;
  
  private static CHECK_TYPE checktype = CHECK_TYPE.ALL;
  
  private static OutputResultCSV outputFile = new OutputResultCSV();
  
  private static boolean bOutputDef = false;
  
  private static boolean bOutputOnlyNG = false;
  
  private static OutputResult outputDef = new OutputResult();
  
  private HashMap<String, String> compMapDefault;
  
  private static HashMap<String, String> charDmnMapMPN = null;
  
  private static HashMap<String, String> charDmnMapComp = null;
  
  private static Map<String, List<CPProperty>> partPropetiesCP = new HashMap<>();
  
  public static void main(String[] paramArrayOfString) {
    try {
      LogConfigLoader.configLog4j();
    } catch (Exception exception) {
      exception.printStackTrace();
      System.out.println("Problems with the logging settings");
    } 
    logger.info("#");
    logger.info("#                 Validate Mapping - Version 1.1.1");
    logger.info("#                   Validate Mapping Application");
    logger.info("#");
    logger.info("#                Copyright Siemens 2025");
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
    boolean bool1 = false;
    String str7 = "";
    String str8 = "./";
    boolean bool2 = false;
    HashSet<String> hashSet1 = new HashSet();
    HashSet<String> hashSet2 = new HashSet();
    Options options = new Options();
    options.addOption("help", false, "help");
    options.addOption("loginconfig", true, "EDM Library auto-login configuration");
    options.addOption("ccpid", true, "Content Provider ID");
    options.addOption("mappingfile", true, "Content Provider Configuration Mapping filename");
    options.addOption("mpncatalogs", true, "Comma-separated list of EDM Library Manufacturer Part catalogs");
    options.addOption("ignoremaps", true, "Comma-separated list of mpn domain names to ignore when comparing with component maps");
    options.addOption("outputdir", true, "Output directory for results");
    options.addOption("checktype", true, "Check target\r\n  ALL:Check all items\r\n  CP:Only Content Provider\r\n  MP:Only mapping file");
    options.addOption("outputccpfile", false, "Output Content Provider part class definitions");
    options.addOption("resultNG", false, "Output only NG in the result (OK is not output)");
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
    if (commandLine.hasOption("mpncatalogs")) {
      str6 = commandLine.getOptionValue("mpncatalogs");
      bool1 = true;
    } 
    if (commandLine.hasOption("ignoremaps"))
      str7 = commandLine.getOptionValue("ignoremaps"); 
    if (commandLine.hasOption("outputdir"))
      str8 = commandLine.getOptionValue("outputdir"); 
    if (commandLine.hasOption("checktype")) {
      String str = commandLine.getOptionValue("checktype").toUpperCase();
      if (str.equals("CP")) {
        checktype = CHECK_TYPE.CP;
      } else if (str.equals("MP")) {
        checktype = CHECK_TYPE.MP;
      } 
    } 
    if (commandLine.hasOption("outputccpfile"))
      bOutputDef = true; 
    if (commandLine.hasOption("resultNG"))
      bOutputOnlyNG = true; 
    if (str3 != null) {
      logger.info("Reading data model configuration file...");
      try {
        dataModelCfg = DataModelConfigurationXML.read(str3);
      } catch (ContentProviderConfigException contentProviderConfigException) {
        logger.error(contentProviderConfigException.getMessage());
        return;
      } 
    } 
    try {
      outputFile.createFile(str8, getResultFileName(true));
      outputFile.setSuppressOK(bOutputOnlyNG);
      outputFile.writeHeader("Master Source,PartClass,PartClassName,Property,Propertyname,Destination Source,Check Item1,Check Item2,Check Contents,Result");
      if (bOutputDef)
        outputDef.createFile(str8, getDefinitionFileName(str2, true)); 
      try {
        logger.info("Connecting to EDM Library...");
        OIAuthenticate oIAuthenticate = OIAuthenticateFactory.createBatchAuthenticate(str1);
        omf = oIAuthenticate.login("Validation Application");
        logger.info("Connected");
        om = omf.createObjectManager();
        ContentProviderGlobal.setBatchExecMode();
        ContentProviderGlobal.setOIObjectManager(om);
      } catch (Exception exception) {
        logger.error("Unable to connect to EDM Library Server:  " + exception.getMessage());
        return;
      } 
      DataModelUtils dataModelUtils = new DataModelUtils(om);
      logger.info("Reading mapping configuration...");
      ContentProviderFactory.getInstance().registerContentProviders(om);
      cpp = ContentProviderFactory.getInstance().createContentProvider(str2);
      if (cpp == null) {
        logger.error("Content provider toolbox does not have a \"" + str2 + "\" on EDM Library Server.");
        return;
      } 
      try {
        if (str4 == null) {
          config = cpp.getConfig(true);
        } else {
          config = new ContentProviderConfig(cpp);
          config.setObjectManager(om);
          if (str5 != null) {
            logger.info("Reading mapping functions script...");
            config.getScriptEngine().read(new File(str5));
          } 
          config.read(new FileInputStream(str4));
        } 
      } catch (ContentProviderConfigException contentProviderConfigException) {
        if (contentProviderConfigException.getMessage().contains("Fatal Error")) {
          logger.error(contentProviderConfigException.getMessage());
          return;
        } 
        logger.info(contentProviderConfigException.getMessage());
      } 
      if (bool1) {
        hashSet1 = getPartIDFromCatalogs(str6);
        if (hashSet1.size() < 1) {
          logger.error("Error: Specified catalog does not exist. :[" + str6 + "]");
          return;
        } 
      } 
      hashSet2 = getPartIDFromCatalogs(str7);
      try {
        logger.info("Start validation");
        partListCP = cpp.getPartClassInfo();
        logger.info("Found " + partListCP.size() + " part classes in Content Provider \"" + str2 + "\"");
        searchPropertyMap = cpp.readSeachCapabilities();
        byte b = 1;
        if (bOutputDef) {
          logger.info("Output part class definition on Content Provider \"" + str2 + "\"");
          for (CPPartClass cPPartClass : partListCP) {
            logger.debug("In process " + b++ + "/" + partListCP.size());
            outputDefinition(cPPartClass);
          } 
        } 
        b = 1;
        if (checktype == CHECK_TYPE.CP || checktype == CHECK_TYPE.ALL) {
          logger.info("Start validation based on Content Provider part definition");
          for (CPPartClass cPPartClass : partListCP) {
            logger.debug("In process " + b++ + "/" + partListCP.size());
            if (bool1 && !hashSet1.contains(cPPartClass.getId())) {
              logger.debug("Skip " + cPPartClass.getId() + ":" + cPPartClass.getLabel() + "(Not in mpncatalogs)");
              continue;
            } 
            boolean bool = validPartClasses(cPPartClass);
            if (!bool)
              continue; 
            bool = validManufacturerPart(cPPartClass);
            if (!bool)
              continue; 
            if (hashSet2.contains(cPPartClass.getId())) {
              logger.debug("Skip " + cPPartClass.getId() + ":" + cPPartClass.getLabel() + "(ignoremaps)");
            } else {
              bool = validComponent(cPPartClass);
            } 
            bool = validManufacturerPartEDM(cPPartClass);
            if (hashSet2.contains(cPPartClass.getId()))
              continue; 
            bool = validComponentEDM(cPPartClass);
          } 
        } 
        if (checktype == CHECK_TYPE.MP || checktype == CHECK_TYPE.ALL) {
          logger.info("Start validation based on mapping file");
          validMapping();
        } 
        bool2 = true;
      } catch (Error error) {
        logger.error(error.getMessage(), error);
      } 
    } catch (Exception exception) {
      logger.error(exception.getMessage(), exception);
    } finally {
      if (om != null)
        om.close(); 
      if (omf != null)
        omf.close(); 
      if (bool2) {
        logger.info("\nValidation completed.");
      } else {
        logger.info("\nValidation Error");
      } 
    } 
  }
  
  private static String getResultFileName(boolean paramBoolean) {
    String str1 = "ResultValidateMapping";
    String str2 = ".csv";
    String str3 = "";
    if (paramBoolean) {
      LocalDateTime localDateTime = LocalDateTime.now();
      DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("_yyyyMMddHHmmss");
      str3 = dateTimeFormatter.format(localDateTime);
    } 
    return str1 + str1 + str3;
  }
  
  private static String getDefinitionFileName(String paramString, boolean paramBoolean) {
    String str1 = "PartClassDefinition[" + paramString + "]";
    String str2 = ".txt";
    String str3 = "";
    if (paramBoolean) {
      LocalDateTime localDateTime = LocalDateTime.now();
      DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("_yyyyMMddHHmmss");
      str3 = dateTimeFormatter.format(localDateTime);
    } 
    return str1 + str1 + str3;
  }
  
  private static boolean validPartClasses(CPPartClass paramCPPartClass) throws Exception {
    String str = paramCPPartClass.getId();
    try {
      ValidateResult validateResult = new ValidateResult(paramCPPartClass.getId(), paramCPPartClass.getLabel());
      ContentProviderConfigPartClass contentProviderConfigPartClass = config.getPartClassByContentProviderId(str);
      if (contentProviderConfigPartClass == null) {
        validateResult.setResult(1, ValidateResult.Result.NG, "Not exist in mapping");
        outputFile.write(validateResult);
      } else {
        validateResult.setResult(1, ValidateResult.Result.OK);
        outputFile.write(validateResult);
      } 
      if (config.getDuplicateList().getPartClassIDList().contains(paramCPPartClass.getId())) {
        validateResult.setResult(2, ValidateResult.Result.NG, "Duplicated in mapping");
        outputFile.write(validateResult);
        return false;
      } 
      validateResult.setResult(2, ValidateResult.Result.OK);
      outputFile.write(validateResult);
      List<CPProperty> list = getPropertiesCP(paramCPPartClass.getId());
      for (CPProperty cPProperty : list) {
        validateResult.setProperty(cPProperty.getId(), cPProperty.getName());
        if (contentProviderConfigPartClass == null) {
          validateResult.setResult(7, ValidateResult.Result.NG, "Properties not exist in mapping");
          outputFile.write(validateResult);
          continue;
        } 
        if (null == contentProviderConfigPartClass.getCPProperty(cPProperty.getId())) {
          validateResult.setResult(7, ValidateResult.Result.NG, "Properties not exist in mapping");
          outputFile.write(validateResult);
          continue;
        } 
        validateResult.setResult(7, ValidateResult.Result.OK);
        outputFile.write(validateResult);
        ContentProviderConfigProperty contentProviderConfigProperty = contentProviderConfigPartClass.getCPProperty(cPProperty.getId());
        if (null != contentProviderConfigProperty) {
          SearchCapability searchCapability = searchPropertyMap.get(cPProperty.getId());
          boolean bool = false;
          if (searchCapability != null && searchCapability.isSearchable())
            bool = true; 
          if (!bool && contentProviderConfigProperty.isSearchable()) {
            validateResult.setResult(8, ValidateResult.Result.NG, "Property searchable settings do not match");
            outputFile.write(validateResult);
            continue;
          } 
          validateResult.setResult(8, ValidateResult.Result.OK);
          outputFile.write(validateResult);
        } 
      } 
    } catch (Exception exception) {
      logger.error(exception.getMessage(), exception);
      outputFile.write("Exception occured. See logs for details.");
    } 
    return true;
  }
  
  private static boolean validManufacturerPart(CPPartClass paramCPPartClass) throws Exception {
    String str = paramCPPartClass.getId();
    try {
      ValidateResult validateResult = new ValidateResult(paramCPPartClass.getId(), paramCPPartClass.getLabel());
      Collection collection = config.getMPNCatalogsByContentProviderId(str);
      if (collection.size() == 0) {
        validateResult.setResult(3, ValidateResult.Result.NG, "Not exist in mapping");
        outputFile.write(validateResult);
        List<CPProperty> list1 = getPropertiesCP(paramCPPartClass.getId());
        for (CPProperty cPProperty : list1) {
          validateResult.setProperty(cPProperty.getId(), cPProperty.getName());
          validateResult.setResult(9, ValidateResult.Result.NG, "Properties not exist in mapping", paramCPPartClass.getId());
          outputFile.write(validateResult);
        } 
        return true;
      } 
      List<CPProperty> list = getPropertiesCP(paramCPPartClass.getId());
      for (ContentProviderConfigMPNCatalog contentProviderConfigMPNCatalog : collection) {
        validateResult.setResult(3, ValidateResult.Result.OK, "", contentProviderConfigMPNCatalog.getClassDMN());
        validateResult.setProperty("", "");
        outputFile.write(validateResult);
        for (ContentProviderConfigContentProviderMap contentProviderConfigContentProviderMap : contentProviderConfigMPNCatalog.getContentProviderMaps()) {
          if (!contentProviderConfigContentProviderMap.getContentProviderId().equals(str))
            continue; 
          for (CPProperty cPProperty : list) {
            validateResult.setProperty(cPProperty.getId(), cPProperty.getName());
            ContentProviderConfigPropertyMap contentProviderConfigPropertyMap = contentProviderConfigContentProviderMap.getPropertyMapByContentProviderId(cPProperty.getId());
            if (contentProviderConfigPropertyMap == null) {
              validateResult.setResult(9, ValidateResult.Result.NG, "Properties not exist in mapping", contentProviderConfigMPNCatalog.getClassDMN());
              outputFile.write(validateResult);
              continue;
            } 
            validateResult.setResult(9, ValidateResult.Result.OK, "", contentProviderConfigMPNCatalog.getClassDMN());
            outputFile.write(validateResult);
          } 
        } 
      } 
    } catch (Exception exception) {
      logger.error(exception.getMessage(), exception);
      outputFile.write("Exception occured. See logs for details.");
    } 
    return true;
  }
  
  private static boolean validManufacturerPartEDM(CPPartClass paramCPPartClass) throws Exception {
    String str = paramCPPartClass.getId();
    try {
      ValidateResult validateResult = new ValidateResult(paramCPPartClass.getId(), paramCPPartClass.getLabel());
      Collection collection = config.getMPNCatalogsByContentProviderId(str);
      for (ContentProviderConfigMPNCatalog contentProviderConfigMPNCatalog : collection) {
        String str1 = contentProviderConfigMPNCatalog.getClassDMN();
        if (str1.equals("RootManufacturerPart"))
          continue; 
        validateResult.setProperty("", "");
        OIClass oIClass = null;
        try {
          oIClass = omf.getClassManager().getOIClass(str1);
          if (oIClass == null) {
            validateResult.setResult(5, ValidateResult.Result.NG, "Not exist in EDM");
            outputFile.write(validateResult);
            continue;
          } 
          validateResult.setResult(5, ValidateResult.Result.OK, "", oIClass.getLabel());
          outputFile.write(validateResult);
        } catch (Exception exception) {
          logger.error("  For " + str1 + " : " + exception.getMessage());
          validateResult.setResult(5, ValidateResult.Result.NG, "Not exist in EDM");
          outputFile.write(validateResult);
          continue;
        } 
        List<CPProperty> list = getPropertiesCP(paramCPPartClass.getId());
        for (ContentProviderConfigContentProviderMap contentProviderConfigContentProviderMap : contentProviderConfigMPNCatalog.getContentProviderMaps()) {
          if (!contentProviderConfigContentProviderMap.getContentProviderId().equals(str))
            continue; 
          for (CPProperty cPProperty : list) {
            try {
              ContentProviderConfigPropertyMap contentProviderConfigPropertyMap = contentProviderConfigContentProviderMap.getPropertyMapByContentProviderId(cPProperty.getId());
              if (contentProviderConfigPropertyMap == null)
                continue; 
              validateResult.setProperty(cPProperty.getId(), cPProperty.getName());
              OIField oIField = oIClass.getField(contentProviderConfigPropertyMap.getDMN());
              if (oIField == null) {
                validateResult.setResult(12, ValidateResult.Result.NG, "Characteristics not exist in EDM");
                outputFile.write(validateResult);
                continue;
              } 
              validateResult.setResult(12, ValidateResult.Result.OK);
              outputFile.write(validateResult);
              String str2 = getMPNCharacteristicNameFromDMN(contentProviderConfigPropertyMap.getDMN());
              if (oIField.getUnitName() != null && !oIField.getUnitName().startsWith("SEDA")) {
                validateResult.setResult(13, ValidateResult.Result.NG, "Characteristic does not use \"SEDA\" unit", str2);
                outputFile.write(validateResult);
                continue;
              } 
              validateResult.setResult(13, ValidateResult.Result.OK, "", str2);
              outputFile.write(validateResult);
            } catch (OIFieldNotFoundException oIFieldNotFoundException) {
              validateResult.setResult(12, ValidateResult.Result.NG, "Characteristics not exist in EDM");
              outputFile.write(validateResult);
            } 
          } 
        } 
      } 
    } catch (Exception exception) {
      logger.error(exception.getMessage(), exception);
      outputFile.write("Exception occured. See logs for details.");
    } 
    return true;
  }
  
  private static String getMPNCharacteristicNameFromDMN(String paramString) {
    if (charDmnMapMPN == null) {
      charDmnMapMPN = new HashMap<>();
      try {
        OIQuery oIQuery = om.createQuery("Characteristic", true);
        oIQuery.addRestriction("RefClass", "60");
        oIQuery.addRestriction("ObjectClass", "60|0");
        oIQuery.addColumn("DomainModelName");
        oIQuery.addColumn("Characteristic");
        OICursor oICursor = oIQuery.execute();
        while (oICursor.next())
          charDmnMapMPN.put(oICursor.getString("DomainModelName"), oICursor.getString("Characteristic")); 
        oICursor.close();
      } catch (OIException oIException) {
        oIException.printStackTrace();
      } 
    } 
    return charDmnMapMPN.get(paramString);
  }
  
  private static String getCompCharacteristicNameFromDMN(String paramString) {
    if (charDmnMapComp == null) {
      charDmnMapComp = new HashMap<>();
      try {
        OIQuery oIQuery = om.createQuery("Characteristic", true);
        oIQuery.addRestriction("RefClass", "1");
        oIQuery.addRestriction("ObjectClass", "1|0");
        oIQuery.addColumn("DomainModelName");
        oIQuery.addColumn("Characteristic");
        OICursor oICursor = oIQuery.execute();
        while (oICursor.next())
          charDmnMapComp.put(oICursor.getString("DomainModelName"), oICursor.getString("Characteristic")); 
        oICursor.close();
      } catch (OIException oIException) {
        oIException.printStackTrace();
      } 
    } 
    return charDmnMapComp.get(paramString);
  }
  
  private static boolean validComponent(CPPartClass paramCPPartClass) throws Exception {
    String str = paramCPPartClass.getId();
    Collection<?> collection = config.getComponentCatalogsByContentProviderId(str);
    ArrayList<?> arrayList = new ArrayList(collection);
    try {
      ValidateResult validateResult = new ValidateResult(paramCPPartClass.getId(), paramCPPartClass.getLabel());
      if (arrayList.size() == 0) {
        validateResult.setResult(4, ValidateResult.Result.NG, "Not exist in mapping");
        outputFile.write(validateResult);
        List<CPProperty> list1 = getPropertiesCP(paramCPPartClass.getId());
        for (CPProperty cPProperty : list1) {
          validateResult.setProperty(cPProperty.getId(), cPProperty.getName());
          String str1 = paramCPPartClass.getId();
          if (paramCPPartClass.isLeafClass())
            str1 = paramCPPartClass.getParentID(); 
          validateResult.setResult(10, ValidateResult.Result.NG, "Properties not exist in mapping", "C_" + str1);
          outputFile.write(validateResult);
        } 
        return false;
      } 
      Collections.reverse(arrayList);
      List<CPProperty> list = getPropertiesCP(paramCPPartClass.getId());
      for (ContentProviderConfigComponentCatalog contentProviderConfigComponentCatalog : arrayList) {
        String str1 = contentProviderConfigComponentCatalog.getClassDMN();
        validateResult.setResult(4, ValidateResult.Result.OK, "", contentProviderConfigComponentCatalog.getClassDMN());
        validateResult.setProperty("", "");
        outputFile.write(validateResult);
        for (ContentProviderConfigContentProviderMap contentProviderConfigContentProviderMap : contentProviderConfigComponentCatalog.getContentProviderMaps()) {
          if (!contentProviderConfigContentProviderMap.getContentProviderId().equals(str))
            continue; 
          for (CPProperty cPProperty : list) {
            ContentProviderConfigPropertyMap contentProviderConfigPropertyMap = contentProviderConfigContentProviderMap.getPropertyMapByContentProviderId(cPProperty.getId());
            validateResult.setProperty(cPProperty.getId(), cPProperty.getName());
            if (contentProviderConfigPropertyMap == null) {
              validateResult.setResult(10, ValidateResult.Result.NG, "Properties not exist in mapping", contentProviderConfigComponentCatalog.getClassDMN());
              outputFile.write(validateResult);
              continue;
            } 
            validateResult.setResult(10, ValidateResult.Result.OK, "", contentProviderConfigComponentCatalog.getClassDMN());
            outputFile.write(validateResult);
          } 
        } 
        Collection collection1 = config.getMPNCatalogsByContentProviderId(str);
        if (collection1.size() == 0)
          return false; 
        for (ContentProviderConfigMPNCatalog contentProviderConfigMPNCatalog : collection1) {
          String str2 = contentProviderConfigMPNCatalog.getClassDMN();
          for (ContentProviderConfigContentProviderMap contentProviderConfigContentProviderMap : contentProviderConfigMPNCatalog.getContentProviderMaps()) {
            if (!contentProviderConfigContentProviderMap.getContentProviderId().equals(str))
              continue; 
            for (ContentProviderConfigManufacturerPartMap contentProviderConfigManufacturerPartMap : contentProviderConfigComponentCatalog.getManufacturerPartMaps()) {
              if (!contentProviderConfigManufacturerPartMap.getClassDMN().equals(str2))
                continue; 
              for (CPProperty cPProperty : list) {
                ContentProviderConfigPropertyMap contentProviderConfigPropertyMap = contentProviderConfigContentProviderMap.getPropertyMapByContentProviderId(cPProperty.getId());
                if (contentProviderConfigPropertyMap == null)
                  continue; 
                validateResult.setProperty(cPProperty.getId(), cPProperty.getName());
                boolean bool = false;
                for (AbstractContentProviderConfigManufacturerPartPropertyMap abstractContentProviderConfigManufacturerPartPropertyMap : contentProviderConfigManufacturerPartMap.getLeafComponentPropertyMaps()) {
                  ContentProviderConfigManufacturerPartPropertyMap contentProviderConfigManufacturerPartPropertyMap = (ContentProviderConfigManufacturerPartPropertyMap)abstractContentProviderConfigManufacturerPartPropertyMap;
                  if (contentProviderConfigManufacturerPartPropertyMap.getManufacturerPartPropertyDMN().equals(contentProviderConfigPropertyMap.getDMN())) {
                    bool = true;
                    break;
                  } 
                } 
                if (!bool) {
                  validateResult.setResult(11, ValidateResult.Result.NG, "Property mappings from Content Provider to Manufacturer Part that are not in Manufacturer Part to Component Mapping", contentProviderConfigComponentCatalog.getClassDMN());
                  outputFile.write(validateResult);
                  continue;
                } 
                validateResult.setResult(11, ValidateResult.Result.OK, "", contentProviderConfigComponentCatalog.getClassDMN());
                outputFile.write(validateResult);
              } 
            } 
          } 
        } 
      } 
    } catch (Exception exception) {
      logger.error(exception.getMessage(), exception);
      outputFile.write("Exception occured. See logs for details.");
    } 
    return true;
  }
  
  private static boolean validComponentEDM(CPPartClass paramCPPartClass) throws Exception {
    String str = paramCPPartClass.getId();
    try {
      ValidateResult validateResult = new ValidateResult(paramCPPartClass.getId(), paramCPPartClass.getLabel());
      Collection collection = config.getComponentCatalogsByContentProviderId(str);
      for (ContentProviderConfigComponentCatalog contentProviderConfigComponentCatalog : collection) {
        String str1 = contentProviderConfigComponentCatalog.getClassDMN();
        OIClass oIClass = null;
        try {
          oIClass = omf.getClassManager().getOIClass(str1);
          if (oIClass == null) {
            validateResult.setResult(6, ValidateResult.Result.NG, "Not exist in EDM");
            outputFile.write(validateResult);
            continue;
          } 
          validateResult.setResult(6, ValidateResult.Result.OK);
          outputFile.write(validateResult);
        } catch (Exception exception) {
          logger.error("  For " + str1 + " : " + exception.getMessage());
          validateResult.setResult(6, ValidateResult.Result.NG, "Not exist in EDM");
          outputFile.write(validateResult);
          continue;
        } 
        List<CPProperty> list = getPropertiesCP(paramCPPartClass.getId());
        for (ContentProviderConfigContentProviderMap contentProviderConfigContentProviderMap : contentProviderConfigComponentCatalog.getContentProviderMaps()) {
          if (!contentProviderConfigContentProviderMap.getContentProviderId().equals(str))
            continue; 
          for (CPProperty cPProperty : list) {
            validateResult.setProperty(cPProperty.getId(), cPProperty.getName());
            ContentProviderConfigPropertyMap contentProviderConfigPropertyMap = contentProviderConfigContentProviderMap.getPropertyMapByContentProviderId(cPProperty.getId());
            try {
              if (contentProviderConfigPropertyMap == null)
                continue; 
              OIField oIField = oIClass.getField(contentProviderConfigPropertyMap.getDMN());
              if (oIField == null) {
                validateResult.setResult(14, ValidateResult.Result.NG, "Characteristics not exist in EDM");
                outputFile.write(validateResult);
                continue;
              } 
              validateResult.setResult(14, ValidateResult.Result.OK);
              outputFile.write(validateResult);
              String str2 = getCompCharacteristicNameFromDMN(contentProviderConfigPropertyMap.getDMN());
              if (oIField.getUnitName() != null && !oIField.getUnitName().startsWith("SEDA")) {
                validateResult.setResult(15, ValidateResult.Result.NG, "Characteristic does not use \"SEDA\" unit", str2);
                outputFile.write(validateResult);
                continue;
              } 
              validateResult.setResult(15, ValidateResult.Result.OK, "", str2);
              outputFile.write(validateResult);
            } catch (OIFieldNotFoundException oIFieldNotFoundException) {
              validateResult.setResult(14, ValidateResult.Result.NG, "Characteristics not exist in EDM");
              outputFile.write(validateResult);
            } 
          } 
        } 
      } 
    } catch (Exception exception) {
      logger.error(exception.getMessage(), exception);
      outputFile.write("Exception occured. See logs for details.");
    } 
    return true;
  }
  
  private static boolean validMapping() throws Exception {
    try {
      for (ContentProviderConfigPartClass contentProviderConfigPartClass : config.getPartClasses()) {
        boolean bool = false;
        String str = contentProviderConfigPartClass.getContentProviderId();
        ValidateResult validateResult = new ValidateResult(str, contentProviderConfigPartClass.getContentProviderLabel());
        for (CPPartClass cPPartClass : partListCP) {
          if (cPPartClass.getId().equals(str)) {
            bool = true;
            break;
          } 
        } 
        if (!bool) {
          validateResult.setResult(16, ValidateResult.Result.NG, "Not exist in Content Provider");
          outputFile.write(validateResult);
          continue;
        } 
        validateResult.setResult(16, ValidateResult.Result.OK);
        outputFile.write(validateResult);
        List<CPProperty> list = getPropertiesCP(str);
        for (ContentProviderConfigProperty contentProviderConfigProperty : contentProviderConfigPartClass.getClassProperties()) {
          bool = false;
          validateResult.setProperty(contentProviderConfigProperty.getContentProviderId(), contentProviderConfigProperty.getContentProviderLabel());
          for (CPProperty cPProperty : list) {
            if (contentProviderConfigProperty.getContentProviderId().equals(cPProperty.getId())) {
              bool = true;
              break;
            } 
          } 
          if (!bool) {
            validateResult.setResult(17, ValidateResult.Result.NG, "Properties not exist in Content Provider");
            outputFile.write(validateResult);
            continue;
          } 
          validateResult.setResult(17, ValidateResult.Result.OK);
          outputFile.write(validateResult);
        } 
      } 
    } catch (Exception exception) {
      logger.error(exception.getMessage(), exception);
      outputFile.write("Exception occured. See logs for details.");
    } 
    return true;
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
  
  private static List<CPProperty> getPropertiesCP(String paramString) throws Exception {
    if (!partPropetiesCP.containsKey(paramString)) {
      List<CPProperty> list = cpp.getPartProperties(paramString);
      partPropetiesCP.put(paramString, list);
    } 
    return partPropetiesCP.get(paramString);
  }
  
  private static HashSet<String> getPartIDFromCatalogs(String paramString) {
    HashSet<String> hashSet1 = new HashSet();
    HashSet<String> hashSet2 = new HashSet();
    if (paramString == null || paramString.equals(""))
      return hashSet1; 
    for (String str : paramString.split("\\s*,\\s*")) {
      OIClass oIClass = null;
      try {
        oIClass = omf.getClassManager().getOIClass(str);
        if (oIClass == null) {
          logger.warn("not found Manufacturer part catalog.[" + str + "]");
        } else {
          addCatalogs(oIClass, hashSet2);
        } 
      } catch (Exception exception) {
        logger.error("  For " + str + " : " + exception.getMessage());
      } 
    } 
    for (String str : hashSet2) {
      ContentProviderConfigMPNCatalog contentProviderConfigMPNCatalog = config.getMPNCatalogConfigByDMN(str);
      if (contentProviderConfigMPNCatalog == null)
        continue; 
      for (ContentProviderConfigContentProviderMap contentProviderConfigContentProviderMap : contentProviderConfigMPNCatalog.getContentProviderMaps())
        hashSet1.add(contentProviderConfigContentProviderMap.getContentProviderId()); 
    } 
    return hashSet1;
  }
  
  private static void outputDefinition(CPPartClass paramCPPartClass) throws Exception {
    StringBuilder stringBuilder = new StringBuilder();
    List<CPProperty> list = getPropertiesCP(paramCPPartClass.getId());
    outputDef.writeHeader("\"" + paramCPPartClass.getId() + "\"\t\"" + paramCPPartClass.getLabel() + "\"");
    Collections.reverse(list);
    for (CPProperty cPProperty : list)
      stringBuilder.append("\"" + cPProperty.getId() + "\"\t\"" + cPProperty.getName() + "\"\t\"" + cPProperty.getType() + "\"\t\"" + convertNull(cPProperty.getUnit()) + "\"" + OutputResult.BR); 
    outputDef.write(stringBuilder.toString());
  }
  
  private static String convertNull(String paramString) {
    return (paramString == null) ? "" : paramString;
  }
  
  private enum CHECK_TYPE {
    ALL, CP, MP;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\cor\\utils\ValidateMappingApp.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */