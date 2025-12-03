package com.mentor.dms.contentprovider.sf.datamodel.upgrade;

import com.mentor.datafusion.oi.OIException;
import com.mentor.datafusion.oi.OIObjectManager;
import com.mentor.datafusion.oi.OIObjectManagerFactory;
import com.mentor.datafusion.oi.OIQuery;
import com.mentor.datafusion.oi.login.OIAuthenticate;
import com.mentor.datafusion.oi.login.OIAuthenticateFactory;
import com.mentor.datafusion.oi.type.OIBitSetFactory;
import com.mentor.datafusion.oi.type.OIMutableBitSet;
import com.mentor.datafusion.oi.type.OIObject;
import com.mentor.datafusion.oi.type.OIObjectSet;
import com.mentor.datafusion.utils.logger.MGLogger;
import com.mentor.dms.contentprovider.core.AbstractContentProvider;
import com.mentor.dms.contentprovider.core.ContentProviderFactory;
import com.mentor.dms.contentprovider.core.ContentProviderGlobal;
import com.mentor.dms.contentprovider.core.config.ContentProviderConfig;
import com.mentor.dms.contentprovider.core.config.ContentProviderConfigException;
import com.mentor.dms.contentprovider.core.utils.LogConfigLoader;
import com.mentor.dms.contentprovider.core.utils.validate.CPPartClass;
import com.mentor.dms.contentprovider.sf.datamodel.DMUpgradeProperties;
import com.mentor.dms.contentprovider.sf.datamodel.DomainNameConverter;
import com.mentor.dms.contentprovider.sf.datamodel.UnitConverter;
import com.mentor.dms.contentprovider.sf.datamodel.csv2xml.CSVParseComponent;
import com.mentor.dms.contentprovider.sf.datamodel.csv2xml.CSVParseMPN;
import com.mentor.dms.contentprovider.sf.datamodel.definition.CatalogCharactersticDef;
import com.mentor.dms.contentprovider.sf.datamodel.definition.CatalogDef;
import com.mentor.dms.contentprovider.sf.datamodel.definition.CharacteristicDef;
import com.mentor.dms.contentprovider.sf.datamodel.definition.DataModelDefinition;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class DatamodelUpgradeApp {
  static MGLogger logger = MGLogger.getLogger(DatamodelUpgradeApp.class);
  
  private static String upgradedir = null;
  
  private static String csvPath = null;
  
  private static OIObjectManager om = null;
  
  private static OIObjectManagerFactory omf = null;
  
  private static ArrayList<OIObject> affectedObjectList = new ArrayList<>();
  
  private static HashMap<String, CharacteristicDef> columnToCharDef = new HashMap<>();
  
  private static DataModelUpgrade dmu = null;
  
  private static DataModelDefinition newDMD = null;
  
  private static DataModelDefinition existingDMD = null;
  
  private static List<CPPartClass> partListCP;
  
  private static ContentProviderConfig config = null;
  
  private static ArrayList<String> createCharactDMNList = new ArrayList<>();
  
  private static int numErrors = 0;
  
  private static int numWarns = 0;
  
  private static boolean bProcessMPN = false;
  
  private static boolean bProcessComponent = false;
  
  private static boolean bProcessStatics = false;
  
  private static boolean bPass1 = false;
  
  private static boolean bPass2 = false;
  
  private static boolean bPass3 = false;
  
  private static boolean bPass4 = false;
  
  private static String mappingFile = null;
  
  public static void main(String[] paramArrayOfString) {
    try {
      LogConfigLoader.configLog4j();
    } catch (Exception exception) {
      exception.printStackTrace();
      System.out.println("Problems with the logging settings");
    } 
    logger.info("#");
    logger.info("#                DataModelUpgrade - Version 1.1.1");
    logger.info("#                  Data Model Upgrade Application");
    logger.info("#");
    logger.info("#                    Copyright Siemens 2025");
    logger.info("#");
    String str1 = null;
    boolean bool1 = false;
    boolean bool2 = false;
    String str2 = null;
    Options options = new Options();
    options.addOption("help", false, "help");
    options.addOption("loginconfig", true, "EDM Library auto-login configuration");
    options.addOption("upgradedir", true, "Data Model Upgrade Configuration directory");
    options.addOption("ccpid", true, "Content Provider ID");
    options.addOption("csvfile", true, "Validate Mapping result file (CSV)");
    options.addOption("mappingfile", true, "Content Provider Configuration Mapping filename");
    options.addOption("mpn", false, "Process Manufacturer Part parametric data model");
    options.addOption("component", false, "Process Component parametric data model");
    options.addOption("statics", false, "Process Manufacturer Part static (top-level) data model");
    options.addOption("commit", false, "Commits data model changes, otherwise only reports pending changes");
    options.addOption("report", false, "Creates a report of current data model");
    options.addOption("1", false, "Run pass 1");
    options.addOption("2", false, "Run pass 2");
    options.addOption("3", false, "Run pass 3");
    options.addOption("4", false, "Run pass 4");
    BasicParser basicParser = new BasicParser();
    CommandLine commandLine = null;
    try {
      commandLine = basicParser.parse(options, paramArrayOfString);
    } catch (ParseException parseException) {
      System.err.println("Error: " + parseException.getMessage());
      HelpFormatter helpFormatter = new HelpFormatter();
      helpFormatter.printHelp("DatamodelUpgradeApp", options);
      System.exit(1);
    } 
    if (commandLine.hasOption("help")) {
      HelpFormatter helpFormatter = new HelpFormatter();
      helpFormatter.printHelp("DatamodelUpgradeApp", options);
      System.exit(0);
    } 
    if (commandLine.hasOption("loginconfig")) {
      str1 = commandLine.getOptionValue("loginconfig");
    } else {
      System.err.println("Error: EDM Library auto-login configuration is required.");
      System.exit(1);
    } 
    if (commandLine.hasOption("ccpid")) {
      str2 = commandLine.getOptionValue("ccpid");
    } else {
      logger.error("Error: Content Provider ID is required.");
      System.exit(1);
    } 
    if (commandLine.hasOption("upgradedir")) {
      upgradedir = commandLine.getOptionValue("upgradedir");
    } else {
      System.err.println("Error: Data Model Upgrade Configuration directory is required.");
      System.exit(1);
    } 
    if (commandLine.hasOption("csvfile")) {
      csvPath = commandLine.getOptionValue("csvfile");
      File file = new File(csvPath);
      if (!file.canRead()) {
        logger.error("Error: Specified Validate Mapping result file(CSV) '" + csvPath + "' does not exist or cannot be read.");
        System.exit(1);
      } 
    } 
    if (commandLine.hasOption("mappingfile"))
      mappingFile = commandLine.getOptionValue("mappingfile"); 
    byte b = 0;
    bProcessMPN = commandLine.hasOption("mpn");
    if (bProcessMPN)
      b++; 
    bProcessComponent = commandLine.hasOption("component");
    if (bProcessComponent)
      b++; 
    bProcessStatics = commandLine.hasOption("statics");
    if (bProcessStatics)
      b++; 
    bool2 = commandLine.hasOption("report");
    if (bool2)
      b++; 
    if (b == 0) {
      System.err.println("Error: One of -mpn, -component or -report must be specified.");
      System.exit(1);
    } 
    if (b > 1) {
      System.err.println("Error: Only one of -mpn, -component or -report can be specified.");
      System.exit(1);
    } 
    b = 0;
    bPass1 = commandLine.hasOption("1");
    if (bPass1)
      b++; 
    bPass2 = commandLine.hasOption("2");
    if (bPass2)
      b++; 
    bPass3 = commandLine.hasOption("3");
    if (bPass3)
      b++; 
    bPass4 = commandLine.hasOption("4");
    if (bPass4)
      b++; 
    if ((bProcessMPN || bProcessComponent) && csvPath == null) {
      if (b > 1) {
        System.err.println("Error: Only one pass number of -1, -2, -3 or -4 can be specified.");
        System.exit(1);
      } 
      if (b == 0) {
        System.err.println("Error: When -component or -mpn is specified then one of -1, -2, -3 or -4 must be specified.");
        System.exit(1);
      } 
    } 
    String str3 = null;
    String str4 = null;
    File file1 = null;
    File file2 = new File(upgradedir);
    if (!file2.canRead() || !file2.canWrite()) {
      System.err.println("Error: Specified Data Model Upgrade Configuration directory '" + upgradedir + "' does not exist or cannot be read.");
      System.exit(1);
    } 
    bool1 = commandLine.hasOption("commit");
    try {
      readUpgradeStatusFile();
    } catch (Exception exception) {
      System.err.println(exception.getMessage());
      System.exit(1);
    } 
    if (bProcessMPN) {
      str3 = "0|60";
      str4 = "60";
      file1 = new File(upgradedir, "DataModelMPNUpgrade.xml");
    } else if (bProcessComponent) {
      str3 = "0|1";
      str4 = "1";
      file1 = new File(upgradedir, "DataModelCompUpgrade.xml");
    } else {
      str3 = "0";
      str4 = "60";
      file1 = new File(upgradedir, "StaticsMPNUpgrade.xml");
    } 
    if (!bool2 && csvPath == null) {
      logger.info("Reading new data model definition...");
      newDMD = new DataModelDefinition();
      try {
        newDMD.read(file1.getAbsolutePath());
      } catch (Exception exception) {
        exception.printStackTrace();
        System.err.println("Error: Data model definition file '" + file1.getAbsolutePath() + "' does not exist or cannot be read.");
        System.exit(1);
      } 
      logger.info("Reading data model upgrade configuration file...");
      File file = new File(upgradedir, "DataModelUpgrade.xml");
      try {
        dmu = DataModelUpgradeXML.read(file.getAbsolutePath());
      } catch (ContentProviderConfigException contentProviderConfigException) {
        contentProviderConfigException.printStackTrace();
        System.err.println(contentProviderConfigException.getMessage());
        System.exit(1);
      } 
    } 
    try {
      DMUpgradeProperties.load();
      UnitConverter.load();
      DomainNameConverter.load();
    } catch (Exception exception) {
      exception.printStackTrace();
      logger.error("ERROR: Unable to read setting file:  " + exception.getMessage());
      return;
    } 
    try {
      logger.info("Connecting to EDM Library Server...");
      OIAuthenticate oIAuthenticate = OIAuthenticateFactory.createBatchAuthenticate(str1);
      omf = oIAuthenticate.login("Data Model Upgrade Utility");
      om = omf.createObjectManager();
      ContentProviderGlobal.setBatchExecMode();
      ContentProviderGlobal.setOIObjectManager(om);
    } catch (Exception exception) {
      exception.printStackTrace();
      logger.error("ERROR: Unable to connect to EDM Library Server:  " + exception.getMessage());
      return;
    } 
    try {
      AbstractContentProvider abstractContentProvider = null;
      ContentProviderFactory.getInstance().registerContentProviders(om);
      abstractContentProvider = ContentProviderFactory.getInstance().createContentProvider(str2);
      if (abstractContentProvider == null) {
        logger.error("ERROR: Content provider toolbox does not have a \"" + str2 + "\" on EDM Library Server.");
        return;
      } 
      if (csvPath != null) {
        logger.info("Reading Content Provider data model...");
        partListCP = abstractContentProvider.getPartClassInfo();
        logger.info("Found " + partListCP.size() + " part classes in Content Provider \"" + str2 + "\"");
      } 
      logger.info("Reading existing EDM Library data model...");
      existingDMD = new DataModelDefinition();
      try {
        existingDMD.read(om, str3, str4, str2);
      } catch (Exception exception) {
        logger.error("ERROR: " + exception.getMessage(), exception);
        numErrors++;
        return;
      } 
      if (csvPath != null) {
        if (mappingFile == null) {
          config = abstractContentProvider.getConfig(true);
        } else {
          config = new ContentProviderConfig(abstractContentProvider);
          config.setObjectManager(om);
          config.read(new FileInputStream(mappingFile));
        } 
        logger.info("Export XML from CSV...");
        if (bProcessMPN) {
          CSVParseMPN cSVParseMPN = new CSVParseMPN(csvPath, abstractContentProvider, config);
          numErrors = cSVParseMPN.exportXML(upgradedir, partListCP, existingDMD);
          numWarns = cSVParseMPN.getWarnNum();
        } else {
          CSVParseComponent cSVParseComponent = new CSVParseComponent(csvPath, abstractContentProvider, config);
          numErrors = cSVParseComponent.exportXML(upgradedir, partListCP, existingDMD);
          numWarns = cSVParseComponent.getWarnNum();
        } 
        return;
      } 
      if (bool2) {
        createReport(existingDMD);
        return;
      } 
      if (bProcessStatics) {
        logger.info("Processing Manufacturer Part top-level characteristics...");
        OIObject oIObject = om.getObjectByID("60", "ObjectClass", true);
        for (CharacteristicDef characteristicDef1 : newDMD.charIdMap.values()) {
          CharacteristicDef characteristicDef2 = null;
          ArrayList<CharacteristicDef> arrayList = new ArrayList();
          for (CharacteristicDef characteristicDef : existingDMD.charIdMap.values()) {
            if (characteristicDef.informationText.equals(characteristicDef1.informationText))
              arrayList.add(characteristicDef); 
          } 
          if (arrayList.size() == 1) {
            characteristicDef2 = arrayList.iterator().next();
          } else if (arrayList.size() > 1) {
            for (CharacteristicDef characteristicDef : arrayList) {
              if (characteristicDef.tabSheet.equals(characteristicDef1.tabSheet)) {
                characteristicDef2 = characteristicDef;
                break;
              } 
            } 
          } 
          if (characteristicDef2 == null)
            characteristicDef2 = (CharacteristicDef)existingDMD.charIdMap.get(characteristicDef1.objectid); 
          if (characteristicDef2 != null && characteristicDef2.charactType != characteristicDef1.charactType) {
            logger.info("  Changing type for characteristic '" + characteristicDef1.informationText + "' in EDM Library...");
            OIObject oIObject2 = om.getObjectByID(characteristicDef2.objectid, "Characteristic", false);
            om.deleteObject(oIObject2);
            if (bool1)
              try {
                logger.info("  Committing : '" + oIObject2.getStringified(oIObject2.getOIClass().getIDField().getName()) + "'...");
                om.makePermanent(oIObject2);
                characteristicDef1.objectid = characteristicDef2.objectid;
                characteristicDef1.tableName = characteristicDef2.tableName;
                characteristicDef1.tableColumn = characteristicDef2.tableColumn;
                characteristicDef1.domainModelName = characteristicDef2.domainModelName;
                characteristicDef2 = null;
              } catch (OIException oIException) {
                logger.error("ERROR: " + oIException.getMessage());
                numErrors++;
                continue;
              } finally {
                om.evict(oIObject2);
              }  
          } 
          if (characteristicDef2 == null) {
            logger.info("  Adding definition for characteristic '" + characteristicDef1.informationText + "' to EDM Library...");
            OIObject oIObject2 = om.createObject("Characteristic");
            affectedObjectList.add(oIObject2);
            try {
              oIObject2.set("Characteristic", characteristicDef1.objectid);
              oIObject2.set("ObjectClass", oIObject);
              oIObject2.set("RefClass", oIObject);
              oIObject2.set("CharactType", Integer.valueOf(characteristicDef1.charactType));
              oIObject2.set("ValueType", Integer.valueOf(characteristicDef1.valueType));
              oIObject2.set("ValueLength", Integer.valueOf(characteristicDef1.valueLength));
              oIObject2.set("InitFileName", "SiliconExpertStaticsMPN.init");
              oIObject2.set("InformationWidth", Integer.valueOf(characteristicDef1.informationWidth));
              oIObject2.set("TableName", characteristicDef1.tableName);
              oIObject2.set("TableColumn", characteristicDef1.tableColumn);
              oIObject2.set("DisposeOrder", Integer.valueOf(characteristicDef1.disposeOrder));
              oIObject2.set("SearchAlignment", Integer.valueOf(characteristicDef1.searchAlignment));
              oIObject2.set("InformationAlignment", Integer.valueOf(characteristicDef1.informationAlignment));
              oIObject2.set("DomainModelName", characteristicDef1.domainModelName);
              OIObjectSet oIObjectSet = oIObject2.getSet("Text");
              OIObject oIObject3 = oIObjectSet.createLine();
              oIObject3.set("Language", "e");
              oIObject3.set("TabSheet", characteristicDef1.tabSheet);
              oIObject3.set("InformationText", characteristicDef1.informationText);
              oIObject3.set("SearchText", characteristicDef1.searchText);
              if (bool1) {
                logger.info("  Committing : '" + oIObject2.getStringified(oIObject2.getOIClass().getIDField().getName()) + "'...");
                try {
                  om.makePermanent(oIObject2);
                } catch (OIException oIException) {
                  logger.error("ERROR: " + oIException.getMessage());
                  numErrors++;
                } 
              } 
            } finally {
              om.evict(oIObject2);
            } 
            continue;
          } 
          boolean bool = (characteristicDef2.valueLength != characteristicDef1.valueLength) ? true : false;
          bool = (bool || characteristicDef2.informationWidth != characteristicDef1.informationWidth) ? true : false;
          bool = (bool || characteristicDef2.disposeOrder != characteristicDef1.disposeOrder) ? true : false;
          bool = (bool || characteristicDef2.searchAlignment != characteristicDef1.searchAlignment) ? true : false;
          bool = (bool || characteristicDef2.informationAlignment != characteristicDef1.informationAlignment) ? true : false;
          if (!bool)
            continue; 
          OIObject oIObject1 = om.getObjectByID(characteristicDef2.objectid, "Characteristic", false);
          logger.info("  Updating definition for characteristic '" + characteristicDef1.informationText + "' to EDM Library...");
          try {
            oIObject1.set("ValueLength", Integer.valueOf(characteristicDef1.valueLength));
            oIObject1.set("InformationWidth", Integer.valueOf(characteristicDef1.informationWidth));
            oIObject1.set("DisposeOrder", Integer.valueOf(characteristicDef1.disposeOrder));
            oIObject1.set("SearchAlignment", Integer.valueOf(characteristicDef1.searchAlignment));
            oIObject1.set("InformationAlignment", Integer.valueOf(characteristicDef1.informationAlignment));
            OIObjectSet oIObjectSet = oIObject1.getSet("Text");
            oIObjectSet.clear();
            OIObject oIObject2 = oIObjectSet.createLine();
            oIObject2.set("Language", "e");
            oIObject2.set("TabSheet", characteristicDef1.tabSheet);
            oIObject2.set("InformationText", characteristicDef1.informationText);
            oIObject2.set("SearchText", characteristicDef1.searchText);
            if (bool1) {
              logger.info("  Committing : '" + oIObject1.getStringified(oIObject1.getOIClass().getIDField().getName()) + "'...");
              try {
                om.makePermanent(oIObject1);
              } catch (OIException oIException) {
                logger.error("ERROR: " + oIException.getMessage());
                numErrors++;
              } 
            } 
          } finally {
            om.evict(oIObject1);
          } 
        } 
        return;
      } 
      if (bPass1) {
        logger.info("Upgrade pass #1...");
        try {
          logger.info("Processing known catalog additions and renames:");
          for (AddCatalog addCatalog : dmu.getUpgradeDirectives().getUpgradeDirectives()) {
            if (addCatalog instanceof AddCatalog) {
              AddCatalog addCatalog1 = addCatalog;
              CatalogDef catalogDef1 = (CatalogDef)existingDMD.catalogDMNMap.get(addCatalog1.getDomainModelName());
              if (catalogDef1 != null)
                continue; 
              CatalogDef catalogDef2 = (CatalogDef)newDMD.catalogDMNMap.get(addCatalog1.getDomainModelName());
              if (catalogDef2 == null) {
                logger.error("Error: AddCatalog directive '" + addCatalog1.getDomainModelName() + "' does not exist in Data Model Definition file.");
                numErrors++;
                continue;
              } 
              logger.info("  Adding catalog '" + catalogDef2.abbreviation + "'...");
              String str = catalogDef2.parentKey;
              CatalogDef catalogDef3 = (CatalogDef)existingDMD.catalogIdMap.get(str);
              if (catalogDef3 == null) {
                logger.error("Error:  AddCatalog directive parent Catalog '" + str + "' does not exist in EDM Library.");
                numErrors++;
                continue;
              } 
              HashSet<String> hashSet = new HashSet();
              for (CatalogDef catalogDef : existingDMD.catalogPathMap.values()) {
                if (catalogDef.parentKey.equals(catalogDef3.objectid))
                  hashSet.add(catalogDef.objectid.substring(catalogDef.objectid.length() - 2, catalogDef.objectid.length())); 
              } 
              StringBuilder stringBuilder = new StringBuilder("AA");
              boolean bool = false;
              while (!bool) {
                if (!hashSet.contains(stringBuilder.toString())) {
                  bool = true;
                  break;
                } 
                if (stringBuilder.toString().equals("ZZ"))
                  break; 
                char c = stringBuilder.charAt(1);
                if (c == 'Z') {
                  c = 'A';
                  char c1 = stringBuilder.charAt(0);
                  if (c1 == 'Z') {
                    c1 = 'A';
                  } else {
                    c1 = (char)(c1 + 1);
                  } 
                  stringBuilder.setCharAt(0, c1);
                } else {
                  c = (char)(c + 1);
                } 
                stringBuilder.setCharAt(1, c);
              } 
              if (!bool) {
                logger.error("Error:  New new child catalog IDs for '" + str + "' are not available.");
                numErrors++;
                continue;
              } 
              CatalogDef catalogDef4 = new CatalogDef(catalogDef2, catalogDef3.objectid + catalogDef3.objectid, catalogDef3.objectid);
              OIObject oIObject = om.createObject("CatalogGroup");
              try {
                affectedObjectList.add(oIObject);
                String str5 = catalogDef4.domainModelName;
                if (existingDMD.catalogDMNMap.containsKey(catalogDef2.domainModelName)) {
                  Pattern pattern = Pattern.compile("(\\S+)(\\d+)$");
                  Matcher matcher = pattern.matcher(str5);
                  if (matcher.find()) {
                    String str6 = matcher.group(1);
                    String str7 = matcher.group(2);
                    str5 = str6 + str6;
                  } else {
                    str5 = str5 + "1";
                  } 
                } 
                oIObject.set("CatalogGroup", catalogDef4.objectid);
                oIObject.set("ParentKey", catalogDef4.parentKey);
                oIObject.set("ObjectClass", Integer.valueOf(Integer.parseInt(catalogDef4.objectClass)));
                oIObject.set("InitFileName", catalogDef4.initFilename);
                oIObject.set("DomainModelName", str5);
                OIMutableBitSet oIMutableBitSet = OIBitSetFactory.newOIMutableBitSet();
                for (byte b1 = 0; b1 < catalogDef4.catalogStatus.length(); b1++) {
                  char c = catalogDef4.catalogStatus.charAt(b1);
                  oIMutableBitSet.set(b1, (c == '1'));
                } 
                oIObject.set("CatalogStatus", oIMutableBitSet);
                OIObjectSet oIObjectSet = oIObject.getSet("Text");
                OIObject oIObject3 = oIObjectSet.createLine();
                oIObject3.set("Language", "e");
                oIObject3.set("Abbreviation", catalogDef4.abbreviation);
                oIObject3.set("CatalogTitle", catalogDef4.catalogTitle);
                oIObject3.set("Description", catalogDef4.description);
                if (bool1) {
                  logger.info("  Committing : '" + oIObject.getStringified(oIObject.getOIClass().getIDField().getName()) + "'...");
                  try {
                    om.makePermanent(oIObject);
                  } catch (OIException oIException) {
                    logger.error("ERROR: " + oIException.getMessage());
                    numErrors++;
                  } 
                } 
                existingDMD.addCatalog(catalogDef4);
              } finally {
                om.evict(oIObject);
              } 
              continue;
            } 
            if (addCatalog instanceof RenameCatalog) {
              RenameCatalog renameCatalog = (RenameCatalog)addCatalog;
              CatalogDef catalogDef = (CatalogDef)existingDMD.catalogDMNMap.get(renameCatalog.getDomainModelName());
              if (catalogDef == null)
                continue; 
              logger.info("  Renaming catalog '" + catalogDef.catalogTitle + "' to '" + renameCatalog.getToName() + "'...");
              OIObject oIObject = om.getObjectByID(catalogDef.objectid, "CatalogGroup", true);
              try {
                om.refreshAndLockObject(oIObject);
                affectedObjectList.add(oIObject);
                OIObjectSet oIObjectSet = oIObject.getSet("Text");
                for (OIObject oIObject3 : oIObjectSet) {
                  if (oIObject3.getString("Language").equals("e")) {
                    oIObject3.set("Abbreviation", renameCatalog.getToName());
                    oIObject3.set("CatalogTitle", renameCatalog.getToName());
                    break;
                  } 
                } 
                if (bool1) {
                  logger.info("  Committing : '" + oIObject.getStringified(oIObject.getOIClass().getIDField().getName()) + "'...");
                  try {
                    om.makePermanent(oIObject);
                  } catch (OIException oIException) {
                    logger.error("ERROR: " + oIException.getMessage());
                    numErrors++;
                  } 
                } 
              } finally {
                om.evict(oIObject);
              } 
            } 
          } 
          logger.info("Processing missing characteristic definitions:");
          OIObject oIObject1 = om.getObjectByID("0", "ObjectClass", true);
          OIObject oIObject2 = om.getObjectByID(str4, "ObjectClass", true);
          for (CharacteristicDef characteristicDef1 : newDMD.charDMNMap.values()) {
            if (characteristicDef1.objectid.contains("dyn_id"))
              continue; 
            CharacteristicDef characteristicDef2 = (CharacteristicDef)existingDMD.charDMNMap.get(characteristicDef1.domainModelName);
            OIObject oIObject = null;
            if (characteristicDef2 != null) {
              if (characteristicDef2.status.equals("U")) {
                logger.info("  Update characteristic status '" + characteristicDef1.informationText + "' in EDM Library...");
                oIObject = getCharObject(characteristicDef2);
                oIObject.set("Status", "A");
              } else {
                logger.info("  characteristic '" + characteristicDef1.informationText + "' already exists in EDM Library...");
              } 
            } else {
              logger.info("  Adding definition for characteristic '" + characteristicDef1.informationText + "' to EDM Library...");
              CharacteristicDef characteristicDef = columnToCharDef.get(characteristicDef1.tableColumn);
              if (characteristicDef != null) {
                logger.error("Error:  EDM Library characteristic  '" + characteristicDef.informationText + "' has same column '" + characteristicDef1.tableColumn + "' as missing characterstic '" + characteristicDef1.informationText + "'.  Skipping...");
                numErrors++;
                continue;
              } 
              oIObject = om.createObject("Characteristic");
              affectedObjectList.add(oIObject);
              oIObject.set("Characteristic", characteristicDef1.objectid);
              oIObject.set("ObjectClass", oIObject1);
              oIObject.set("RefClass", oIObject2);
              oIObject.set("ValueType", Integer.valueOf(characteristicDef1.valueType));
              oIObject.set("ValueLength", Integer.valueOf(characteristicDef1.valueLength));
              oIObject.set("Precision", Integer.valueOf(characteristicDef1.precision));
              oIObject.set("InitFileName", characteristicDef1.initFilename);
              oIObject.set("InformationWidth", Integer.valueOf(characteristicDef1.informationWidth));
              oIObject.set("TableName", characteristicDef1.tableName);
              oIObject.set("TableColumn", characteristicDef1.tableColumn);
              oIObject.set("DisposeOrder", Integer.valueOf(characteristicDef1.disposeOrder));
              oIObject.set("SearchAlignment", Integer.valueOf(characteristicDef1.searchAlignment));
              oIObject.set("InformationAlignment", Integer.valueOf(characteristicDef1.informationAlignment));
              oIObject.set("DomainModelName", characteristicDef1.domainModelName);
              if (characteristicDef1.unit != null)
                oIObject.set("Patternmatch", characteristicDef1.unit); 
              OIObjectSet oIObjectSet = oIObject.getSet("Text");
              OIObject oIObject3 = oIObjectSet.createLine();
              oIObject3.set("Language", "e");
              oIObject3.set("TabSheet", characteristicDef1.tabSheet);
              oIObject3.set("InformationText", characteristicDef1.informationText);
              oIObject3.set("SearchText", characteristicDef1.searchText);
            } 
            if (bool1 && oIObject != null) {
              logger.info("  Committing : '" + oIObject.getStringified(oIObject.getOIClass().getIDField().getName()) + "'...");
              try {
                om.makePermanent(oIObject);
              } catch (OIException oIException) {
                logger.error("ERROR: " + oIException.getMessage());
                numErrors++;
              } finally {
                om.evict(oIObject);
              } 
            } 
          } 
          logger.info("Processing known renamed characteristics:");
          for (CharacteristicDef characteristicDef : existingDMD.charIdMap.values()) {
            OIObject oIObject = null;
            RenameProperty renameProperty = getRenameProperty(characteristicDef);
            if (renameProperty != null) {
              logger.info("  Renaming characteristic from '" + characteristicDef.informationText + "' to '" + renameProperty.getToName() + "' in EDM Library...");
              oIObject = om.getObjectByID(characteristicDef.objectid, "Characteristic", true);
              try {
                om.refreshAndLockObject(oIObject);
                affectedObjectList.add(oIObject);
                OIObjectSet oIObjectSet = oIObject.getSet("Text");
                for (OIObject oIObject3 : oIObjectSet) {
                  if (oIObject3.getString("InformationText").equals(characteristicDef.informationText)) {
                    oIObject3.set("InformationText", renameProperty.getToName());
                    oIObject3.set("SearchText", renameProperty.getToName());
                    break;
                  } 
                } 
                if (bool1) {
                  logger.info("  Committing : '" + oIObject.getStringified(oIObject.getOIClass().getIDField().getName()) + "'...");
                  try {
                    om.makePermanent(oIObject);
                    existingDMD.charLabelMap.remove(characteristicDef.informationText);
                    characteristicDef.informationText = renameProperty.getToName();
                    existingDMD.charLabelMap.put(characteristicDef.informationText, characteristicDef);
                  } catch (OIException oIException) {
                    logger.error("ERROR: " + oIException.getMessage());
                    numErrors++;
                  } 
                } 
              } finally {
                om.evict(oIObject);
              } 
            } 
          } 
          logger.info("Processing differences in characteristic value lengths:");
          for (CharacteristicDef characteristicDef1 : newDMD.charDMNMap.values()) {
            if (characteristicDef1.objectid.contains("dyn_id") || (characteristicDef1.valueType != 3 && characteristicDef1.valueType != 2))
              continue; 
            CharacteristicDef characteristicDef2 = (CharacteristicDef)existingDMD.charDMNMap.get(characteristicDef1.domainModelName);
            if (characteristicDef2 != null) {
              OIObject oIObject = null;
              if (characteristicDef2.valueType != characteristicDef1.valueType) {
                logger.error("  ERROR: The type of characteristic '" + characteristicDef2.informationText + "' is different from the xml file.");
                continue;
              } 
              try {
                if (characteristicDef1.valueLength > characteristicDef2.valueLength) {
                  logger.info("  Updating EDM Library characteristic '" + characteristicDef2.informationText + "' length from " + characteristicDef2.valueLength + " to " + characteristicDef1.valueLength + "...");
                  oIObject = om.getObjectByID(characteristicDef2.objectid, "Characteristic", true);
                  om.refreshAndLockObject(oIObject);
                  affectedObjectList.add(oIObject);
                  oIObject.set("ValueLength", Integer.valueOf(characteristicDef1.valueLength));
                } 
                if (characteristicDef1.unit != null && !characteristicDef1.unit.equals(characteristicDef2.unit)) {
                  logger.info("  Updating EDM Library characteristic '" + characteristicDef2.informationText + "' Unit from " + characteristicDef2.unit + " to " + characteristicDef1.unit + "...");
                  if (oIObject == null) {
                    oIObject = om.getObjectByID(characteristicDef2.objectid, "Characteristic", true);
                    om.refreshAndLockObject(oIObject);
                    affectedObjectList.add(oIObject);
                  } 
                  oIObject.set("Patternmatch", characteristicDef1.unit);
                } 
                if (bool1 && oIObject != null) {
                  logger.info("  Committing : '" + oIObject.getStringified(oIObject.getOIClass().getIDField().getName()) + "'...");
                  om.makePermanent(oIObject);
                } 
              } catch (OIException oIException) {
                logger.error("ERROR: " + oIException.getMessage());
                numErrors++;
              } finally {
                if (oIObject != null)
                  om.evict(oIObject); 
              } 
            } 
          } 
        } catch (Exception exception) {
          exception.printStackTrace();
          logger.error("ERROR: " + exception.getMessage());
          numErrors++;
          return;
        } finally {
          try {
            om.evict(affectedObjectList);
          } catch (OIException oIException) {
            logger.error("ERROR: " + oIException.getMessage());
          } 
        } 
      } 
      if (bPass2) {
        logger.info("Upgrade pass #2...");
        affectedObjectList.clear();
        try {
          logger.info("Validating EDM Library catalogs...");
          for (CatalogDef catalogDef1 : newDMD.catalogIdMap.values()) {
            CatalogDef catalogDef2 = (CatalogDef)existingDMD.catalogDMNMap.get(catalogDef1.domainModelName);
            if (catalogDef2 == null) {
              logger.error("  ERROR: EDM Library does not contain catalog path '" + catalogDef1.path + "' which exists in " + abstractContentProvider.getName() + ".");
              numErrors++;
            } 
          } 
          logger.info("Processing catalog characteristics:");
          for (CatalogDef catalogDef1 : newDMD.catalogDMNMap.values()) {
            boolean bool = false;
            CatalogDef catalogDef2 = (CatalogDef)existingDMD.catalogDMNMap.get(catalogDef1.domainModelName);
            if (catalogDef2 != null) {
              OIObject oIObject = null;
              OIObjectSet oIObjectSet = null;
              try {
                for (CatalogCharactersticDef catalogCharactersticDef1 : catalogDef1.charDMNMap.values()) {
                  CatalogCharactersticDef catalogCharactersticDef2 = (CatalogCharactersticDef)catalogDef2.charDMNMap.get(catalogCharactersticDef1.charDef.domainModelName);
                  if (catalogCharactersticDef2 == null) {
                    CharacteristicDef characteristicDef = (CharacteristicDef)existingDMD.charDMNMap.get(catalogCharactersticDef1.charDef.domainModelName);
                    if (characteristicDef == null) {
                      logger.error("    ERROR: Characteristic '" + catalogCharactersticDef1.charDef.informationText + "' not found.");
                      continue;
                    } 
                    if (characteristicDef.classNum != 0) {
                      logger.info("    '" + characteristicDef.informationText + "' is not catalog characteristics.");
                      continue;
                    } 
                    if (!bool) {
                      logger.info("  Upgrades to catalog characteristics for '" + catalogDef2.path + "':");
                      bool = true;
                    } 
                    logger.info("    Adding characteristic '" + catalogCharactersticDef1.charDef.informationText + "' (" + catalogCharactersticDef1.charDef.objectid + ")...");
                    if (oIObject == null) {
                      oIObject = om.getObjectByID(catalogDef2.objectid, "CatalogGroup", true);
                      om.refreshAndLockObject(oIObject);
                      affectedObjectList.add(oIObject);
                      oIObjectSet = oIObject.getSet("CatalogCharacteristics");
                    } 
                    OIObject oIObject1 = oIObjectSet.createLine();
                    OIObject oIObject2 = getCharObject(characteristicDef);
                    oIObject1.set("Characteristic", oIObject2);
                    oIObject1.set("TabSheet", catalogCharactersticDef1.tabSheet);
                    oIObject1.set("OrderNo", catalogCharactersticDef1.orderNo);
                    continue;
                  } 
                  if (!catalogCharactersticDef2.tabSheet.equals(catalogCharactersticDef1.tabSheet)) {
                    if (!bool) {
                      logger.info("  Upgrades to catalog characteristics for '" + catalogDef2.abbreviation + "':");
                      bool = true;
                    } 
                    if (oIObject == null) {
                      oIObject = om.getObjectByID(catalogDef2.objectid, "CatalogGroup", true);
                      om.refreshAndLockObject(oIObject);
                      affectedObjectList.add(oIObject);
                      oIObjectSet = oIObject.getSet("CatalogCharacteristics");
                    } 
                    for (OIObject oIObject1 : oIObjectSet) {
                      if (oIObject1.getStringified("Characteristic").equals(catalogCharactersticDef2.charDef.objectid)) {
                        logger.info("    Moving characteristic '" + catalogCharactersticDef1.charDef.informationText + "' to '" + catalogCharactersticDef1.tabSheet + "'...");
                        oIObject1.set("TabSheet", catalogCharactersticDef1.tabSheet);
                        oIObject1.set("OrderNo", catalogCharactersticDef1.orderNo);
                      } 
                    } 
                  } 
                } 
                if (oIObject != null && bool1)
                  try {
                    logger.info("    Committing : '" + oIObject.getStringified(oIObject.getOIClass().getIDField().getName()) + "'...");
                    om.makePermanent(oIObject);
                  } catch (OIException oIException) {
                    logger.error("ERROR: " + oIException.getMessage());
                    numErrors++;
                  }  
              } finally {
                if (oIObject != null)
                  om.evict(oIObject); 
              } 
            } 
          } 
          Map<String, List<DeleteProperty>> map = getDeleteCharactMap();
          logger.info("Remove catalog characteristics:");
          for (String str : map.keySet()) {
            CatalogDef catalogDef = (CatalogDef)existingDMD.catalogDMNMap.get(str);
            OIObject oIObject = om.getObjectByID(catalogDef.objectid, "CatalogGroup", true);
            if (oIObject == null) {
              logger.warn("  WARN: Catalog '" + str + "' not found in EDM.");
              continue;
            } 
            om.refreshAndLockObject(oIObject);
            List list = map.get(str);
            for (DeleteProperty deleteProperty : list) {
              CharacteristicDef characteristicDef = (CharacteristicDef)existingDMD.charDMNMap.get(deleteProperty.domainModelName);
              if (characteristicDef == null) {
                logger.warn("  WARN: Characteristic '" + deleteProperty.name + "' in '" + catalogDef.abbreviation + "' not found in EDM.");
                continue;
              } 
              OIObjectSet oIObjectSet = oIObject.getSet("CatalogCharacteristics");
              Iterator<OIObject> iterator = oIObjectSet.iterator();
              logger.info("    Removing characteristic '" + characteristicDef.informationText + "' (" + characteristicDef.objectid + ")  from catalog '" + catalogDef.abbreviation + "'...");
              while (iterator.hasNext()) {
                OIObject oIObject1 = iterator.next();
                if (oIObject1.getStringified("Characteristic").equals(characteristicDef.objectid))
                  iterator.remove(); 
              } 
            } 
            if (oIObject != null) {
              om.makePermanent(oIObject);
              affectedObjectList.add(oIObject);
            } 
          } 
        } catch (Exception exception) {
          logger.error("ERROR: " + exception.getMessage());
          numErrors++;
          return;
        } finally {
          try {
            om.evict(affectedObjectList);
          } catch (OIException oIException) {
            logger.error("ERROR: " + oIException.getMessage());
          } 
        } 
      } 
      if (bPass3) {
        logger.info("Upgrade pass #3...");
        affectedObjectList.clear();
        try {
          logger.info("Processing known catalog deletions:");
          for (DeleteCatalog deleteCatalog : dmu.getUpgradeDirectives().getUpgradeDirectives()) {
            if (deleteCatalog instanceof DeleteCatalog) {
              DeleteCatalog deleteCatalog1 = deleteCatalog;
              CatalogDef catalogDef = (CatalogDef)existingDMD.catalogDMNMap.get(deleteCatalog1.getDomainModelName());
              if (catalogDef == null) {
                logger.info("  DeleteCatalog directive '" + deleteCatalog1.getName() + "' (" + deleteCatalog1.getDomainModelName() + ") does not exist in EDM Library.");
                continue;
              } 
              logger.info("  Deleting catalog '" + catalogDef.abbreviation + "'...");
              OIObject oIObject = om.getObjectByID(catalogDef.objectid, "CatalogGroup", true);
              try {
                om.deleteObject(oIObject);
                affectedObjectList.add(oIObject);
                if (bool1)
                  try {
                    logger.info("  Committing : '" + oIObject.getStringified(oIObject.getOIClass().getIDField().getName()) + "'...");
                    om.makePermanent(oIObject);
                  } catch (OIException oIException) {
                    logger.error("ERROR: " + oIException.getMessage());
                    numErrors++;
                  }  
              } finally {
                om.evict(oIObject);
              } 
            } 
          } 
        } catch (Exception exception) {
          logger.error("ERROR: " + exception.getMessage());
          numErrors++;
          return;
        } finally {
          try {
            om.evict(affectedObjectList);
          } catch (OIException oIException) {
            logger.error("ERROR: " + oIException.getMessage());
          } 
        } 
      } 
      if (bPass4) {
        logger.info("Upgrade pass #4...");
        affectedObjectList.clear();
        try {
          logger.info("Processing deleted characteristic definitions:");
          HashSet<String> hashSet = new HashSet();
          for (DeleteProperty deleteProperty : dmu.getUpgradeDirectives().getUpgradeDirectives()) {
            if (deleteProperty instanceof DeleteProperty) {
              DeleteProperty deleteProperty1 = deleteProperty;
              if (hashSet.contains(deleteProperty1.domainModelName))
                continue; 
              removeUnusedCharacteristics(deleteProperty1.domainModelName, deleteProperty1.name, bool1);
              hashSet.add(deleteProperty1.domainModelName);
            } 
            if (deleteProperty instanceof DeleteAllProperty) {
              DeleteAllProperty deleteAllProperty = (DeleteAllProperty)deleteProperty;
              if (hashSet.contains(deleteAllProperty.domainModelName))
                continue; 
              removeUnusedCharacteristics(deleteAllProperty.domainModelName, deleteAllProperty.name, bool1);
              hashSet.add(deleteAllProperty.domainModelName);
            } 
          } 
        } catch (Exception exception) {
          logger.error("ERROR: " + exception.getMessage());
          numErrors++;
          return;
        } finally {
          try {
            om.evict(affectedObjectList);
          } catch (OIException oIException) {
            oIException.printStackTrace();
          } 
        } 
      } 
    } catch (Exception exception) {
      logger.error("ERROR: " + exception.getMessage());
      numErrors++;
    } finally {
      logger.info("");
      logger.info("Number of errors: " + numErrors);
      if (0 < numWarns)
        logger.info("Number of warnings: " + numWarns); 
      try {
        om.evict(affectedObjectList);
      } catch (OIException oIException) {
        logger.error("ERROR: " + oIException.getMessage());
      } 
      if (bool1)
        try {
          writeUpgradeStatusFile();
        } catch (Exception exception) {
          logger.warn(exception.getMessage());
        }  
      logger.info("");
      logger.info("Disconnecting from EDM Library Server...");
      if (om != null) {
        om.close();
        om = null;
      } 
      if (omf != null) {
        omf.close();
        omf = null;
      } 
    } 
  }
  
  private static Map<String, List<DeleteProperty>> getDeleteCharactMap() {
    HashMap<Object, Object> hashMap = new HashMap<>();
    for (DeleteProperty deleteProperty : dmu.getUpgradeDirectives().getUpgradeDirectives()) {
      if (deleteProperty instanceof DeleteProperty) {
        List<DeleteProperty> list;
        DeleteProperty deleteProperty1 = deleteProperty;
        String str = deleteProperty1.catalogDMN;
        if (hashMap.containsKey(str)) {
          list = (List)hashMap.get(str);
        } else {
          list = new ArrayList();
          hashMap.put(str, list);
        } 
        list.add(deleteProperty1);
      } 
      if (deleteProperty instanceof DeleteAllProperty) {
        DeleteAllProperty deleteAllProperty = (DeleteAllProperty)deleteProperty;
        label30: for (CatalogDef catalogDef : existingDMD.catalogIdMap.values()) {
          String str = catalogDef.domainModelName;
          Set set = catalogDef.charDMNMap.keySet();
          for (String str1 : set) {
            CatalogCharactersticDef catalogCharactersticDef = (CatalogCharactersticDef)catalogDef.charDMNMap.get(str1);
            if (deleteAllProperty.domainModelName.equals(catalogCharactersticDef.charDef.domainModelName)) {
              if (hashMap.containsKey(str)) {
                List list = (List)hashMap.get(str);
                continue label30;
              } 
              ArrayList<DeleteProperty> arrayList = new ArrayList();
              hashMap.put(str, arrayList);
              DeleteProperty deleteProperty1 = new DeleteProperty();
              deleteProperty1.setCatalogDMN(str);
              deleteProperty1.setDomainModelName(deleteAllProperty.domainModelName);
              deleteProperty1.setName(deleteAllProperty.getName());
              arrayList.add(deleteProperty1);
            } 
          } 
        } 
      } 
    } 
    return (Map)hashMap;
  }
  
  private static void removeUnusedCharacteristics(String paramString1, String paramString2, boolean paramBoolean) throws OIException {
    CharacteristicDef characteristicDef = (CharacteristicDef)existingDMD.charDMNMap.get(paramString1);
    if (characteristicDef == null) {
      logger.info("  Characteristic '" + paramString2 + "' not found.");
      return;
    } 
    if (characteristicDef.classNum != 0) {
      logger.info("  Characteristic '" + paramString2 + "' is not catalog characteristic.");
      return;
    } 
    logger.info("  Removing characteristic definition for '" + characteristicDef.informationText + "' from EDM Library...");
    if (isCharactristicUsed(characteristicDef.objectid)) {
      logger.info("    Characteristic '" + characteristicDef.informationText + "' is in use.");
      return;
    } 
    OIObject oIObject = null;
    try {
      oIObject = getCharObject(characteristicDef);
      oIObject.set("Status", "U");
      affectedObjectList.add(oIObject);
      if (paramBoolean) {
        logger.info("    Committing : '" + oIObject.getStringified(oIObject.getOIClass().getIDField().getName()) + "'...");
        om.makePermanent(oIObject);
      } 
    } catch (Exception exception) {
      logger.error("ERROR: " + exception.getMessage());
      numErrors++;
    } finally {
      om.evict(oIObject);
    } 
  }
  
  private static boolean isCharactristicUsed(String paramString) {
    try {
      OIQuery oIQuery = om.createQuery("CatalogGroup", true);
      oIQuery.addRestriction("CatalogCharacteristics.Characteristic", paramString);
      long l = oIQuery.count();
      if (0L < l)
        return true; 
    } catch (OIException oIException) {
      oIException.printStackTrace();
      return true;
    } 
    return false;
  }
  
  private static OIObject getCharObject(CharacteristicDef paramCharacteristicDef) throws OIException {
    OIObject oIObject = null;
    try {
      oIObject = om.getObjectByID(paramCharacteristicDef.objectid, "Characteristic", true);
    } catch (Exception exception) {}
    if (oIObject == null)
      oIObject = getCharObject((CharacteristicDef)existingDMD.charLabelMap.get(paramCharacteristicDef.informationText)); 
    return oIObject;
  }
  
  private static RenameProperty getRenameProperty(CharacteristicDef paramCharacteristicDef) {
    RenameProperty renameProperty = null;
    for (RenameProperty renameProperty1 : dmu.getUpgradeDirectives().getUpgradeDirectives()) {
      if (renameProperty1 instanceof RenameProperty) {
        RenameProperty renameProperty2 = renameProperty1;
        if ((renameProperty2.getAppliesTo() == null || ((!renameProperty2.getAppliesTo().equals("Component") || bProcessComponent) && (!renameProperty2.getAppliesTo().equals("ManufacturerPart") || bProcessMPN))) && renameProperty2.getDomainModelName().equals(paramCharacteristicDef.domainModelName)) {
          renameProperty = renameProperty2;
          break;
        } 
      } 
    } 
    return renameProperty;
  }
  
  private static void createReport(DataModelDefinition paramDataModelDefinition) {
    for (CatalogDef catalogDef : paramDataModelDefinition.catalogIdMap.values()) {
      for (String str : catalogDef.charLabelMap.keySet())
        logger.info(catalogDef.path + "." + catalogDef.path); 
    } 
  }
  
  private static int getCurrentPassNumber() {
    return bPass1 ? 1 : (bPass2 ? 2 : (bPass3 ? 3 : (bPass4 ? 4 : -1)));
  }
  
  private static String getPropPrefix() {
    String str = "Unk";
    if (bProcessComponent) {
      str = "Comp";
    } else if (bProcessMPN) {
      str = "MPN";
    } 
    return str;
  }
  
  private static void readUpgradeStatusFile() throws Exception {
    int i = 0;
    int j = 0;
    String str = getPropPrefix();
    File file = new File(upgradedir, "upgradestatus.properties");
    Properties properties = new Properties();
    try {
      properties.load(new FileInputStream(file));
      i = Integer.parseInt(properties.getProperty(str + "PassNumber", "0"));
      j = Integer.parseInt(properties.getProperty(str + "NumErrors", "0"));
    } catch (Exception exception) {}
    int k = getCurrentPassNumber();
    if (k == -1 || i == k)
      return; 
    if (k > i + 1)
      throw new Exception("Error: Cannot run pass number '" + k + "'.  Must first run commited pass number '" + i + 1 + "' with no errors."); 
    if (j > 0)
      throw new Exception("Error: Cannot run pass number '" + k + "'.  Last pass number '" + i + "' returned " + j + " errors."); 
  }
  
  private static void writeUpgradeStatusFile() throws Exception {
    File file = new File(upgradedir, "upgradestatus.properties");
    Properties properties = new Properties();
    try {
      properties.load(new FileInputStream(file));
    } catch (Exception exception) {}
    String str = getPropPrefix();
    properties.setProperty(str + "PassNumber", Integer.toString(getCurrentPassNumber()));
    properties.setProperty(str + "NumErrors", Integer.toString(numErrors));
    properties.store(new FileWriter(file), (String)null);
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\datamode\\upgrade\DatamodelUpgradeApp.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */