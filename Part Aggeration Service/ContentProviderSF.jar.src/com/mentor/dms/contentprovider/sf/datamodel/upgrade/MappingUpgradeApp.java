package com.mentor.dms.contentprovider.sf.datamodel.upgrade;

import com.mentor.datafusion.oi.OICursor;
import com.mentor.datafusion.oi.OIException;
import com.mentor.datafusion.oi.OIObjectManager;
import com.mentor.datafusion.oi.OIObjectManagerFactory;
import com.mentor.datafusion.oi.OIQuery;
import com.mentor.datafusion.oi.login.OIAuthenticate;
import com.mentor.datafusion.oi.login.OIAuthenticateFactory;
import com.mentor.datafusion.oi.type.OIBlob;
import com.mentor.datafusion.oi.type.OIObject;
import com.mentor.datafusion.oi.util.FileUtilities;
import com.mentor.datafusion.oi.util.OIFile;
import com.mentor.datafusion.utils.logger.MGLogger;
import com.mentor.dms.contentprovider.core.AbstractContentProvider;
import com.mentor.dms.contentprovider.core.ContentProviderFactory;
import com.mentor.dms.contentprovider.core.ContentProviderGlobal;
import com.mentor.dms.contentprovider.core.config.ContentProviderConfig;
import com.mentor.dms.contentprovider.core.config.SearchCapability;
import com.mentor.dms.contentprovider.core.utils.LogConfigLoader;
import com.mentor.dms.contentprovider.core.utils.validate.CPPartClass;
import com.mentor.dms.contentprovider.core.utils.validate.CPProperty;
import com.mentor.dms.contentprovider.sf.datamodel.MappingUpgradeProperties;
import com.mentor.dms.contentprovider.sf.datamodel.csv2xml.CSVUtil;
import com.mentor.dms.contentprovider.sf.datamodel.csv2xml.csvdata.CatalogFromCsv;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class MappingUpgradeApp {
  public static final int COL_PART_ID = 1;
  
  public static final int COL_PART_NAME = 2;
  
  public static final int COL_PROP_ID = 3;
  
  public static final int COL_PROP_NAME = 4;
  
  public static final int COL_CHK_ITEM2 = 7;
  
  public static final int COL_CHK_CONTENT = 8;
  
  public static final int COL_RESULT = 9;
  
  public static final int COL_COMMAND = 10;
  
  static MGLogger logger = MGLogger.getLogger(MappingUpgradeApp.class);
  
  private static final String XPATH_PARTCLASS_BASE = "/ContentProviderCfg/PartClasses/PartClass";
  
  private static final String XPATH_MPN_BASE = "/ContentProviderCfg/ManufacturerPartCatalogs/ManufacturerPartCatalog";
  
  private static final String XPATH_COMP_BASE = "/ContentProviderCfg/ComponentCatalogs/ComponentCatalog";
  
  private static AbstractContentProvider cpp;
  
  private static Document cfgDoc;
  
  private static List<CPPartClass> partListCP;
  
  private static Map<String, HashMap<String, CPProperty>> partPropetiesCP = new HashMap<>();
  
  private static HashMap<String, SearchCapability> searchPropertyMap;
  
  private static XPath xpath = XPathFactory.newInstance().newXPath();
  
  private static int errorNum = 0;
  
  private static int warnNum = 0;
  
  private static Set<String> propsID;
  
  private static Set<String> propsNoSearch;
  
  private static Map<String, String> searchDMNMap;
  
  private static Map<String, String> conversionDMNMap;
  
  public static void main(String[] paramArrayOfString) {
    try {
      LogConfigLoader.configLog4j();
    } catch (Exception exception) {
      exception.printStackTrace();
      System.out.println("Problems with the logging settings");
    } 
    logger.info("#");
    logger.info("#                  MappingUpgrade - Version 1.1.1");
    logger.info("#                   Mapping Upgrade Application");
    logger.info("#");
    logger.info("#                Copyright Siemens 2025");
    logger.info("#");
    logger.info("#                      All Rights Reserved.");
    logger.info("#");
    logger.info("#        THIS WORK CONTAINS TRADE SECRET AND PROPRIETARY");
    logger.info("#        INFORMATION WHICH IS THE PROPERTY OF SIEMENS");
    logger.info("#        OR ITS LICENSORS AND IS SUBJECT TO LICENSE TERMS.");
    logger.info("#");
    String str1 = null;
    String str2 = null;
    String str3 = null;
    String str4 = null;
    String str5 = null;
    boolean bool = false;
    Options options = new Options();
    options.addOption("help", false, "help");
    options.addOption("loginconfig", true, "EDM Library auto-login configuration");
    options.addOption("ccpid", true, "Content Provider ID");
    options.addOption("inmappingfile", true, "Input Content Provider Configuration Mapping filename");
    options.addOption("outmappingfile", true, "Output Content Provider Configuration Mapping filename");
    options.addOption("csvfile", true, "Validate Mapping result file (CSV)");
    BasicParser basicParser = new BasicParser();
    CommandLine commandLine = null;
    try {
      commandLine = basicParser.parse(options, paramArrayOfString);
    } catch (ParseException parseException) {
      logger.error("Error: " + parseException.getMessage());
      HelpFormatter helpFormatter = new HelpFormatter();
      helpFormatter.printHelp("MappingUpgrade", options);
      System.exit(1);
    } 
    if (commandLine.hasOption("help")) {
      HelpFormatter helpFormatter = new HelpFormatter();
      helpFormatter.printHelp("MappingUpgrade", options);
      System.exit(0);
    } 
    str1 = commandLine.getOptionValue("loginconfig");
    str2 = commandLine.getOptionValue("ccpid");
    str3 = commandLine.getOptionValue("inmappingfile");
    str4 = commandLine.getOptionValue("outmappingfile");
    if (str1 == null) {
      logger.error("Error: Either EDM Library auto-login configuration or Input Content Provider Configuration Mapping filename is required.");
      System.exit(1);
    } 
    if (str2 == null) {
      logger.error("Error: Content Provider ID is required when EDM Library auto-login configuration is specified.");
      System.exit(1);
    } 
    if (str3 != null && str4 == null) {
      logger.error("Error: Output Content Provider Configuration Mapping filename is required when Input Content Provider Configuration Mapping filename is specified.");
      System.exit(1);
    } 
    if (commandLine.hasOption("csvfile")) {
      str5 = commandLine.getOptionValue("csvfile");
    } else {
      logger.error("Error: Validate Mapping result file(CSV) is required.");
      System.exit(1);
    } 
    File file = new File(str5);
    if (!file.canRead()) {
      logger.error("Error: Specified Validate Mapping result file(CSV) '" + str5 + "' does not exist or cannot be read.");
      System.exit(1);
    } 
    OIObjectManagerFactory oIObjectManagerFactory = null;
    OIObjectManager oIObjectManager = null;
    OIObject oIObject = null;
    String str6 = null;
    try {
      logger.info("Reading mapping upgrade configuration file...");
      MappingUpgradeProperties.load();
      propsID = MappingUpgradeProperties.getProperties("idProperty");
      propsNoSearch = MappingUpgradeProperties.getProperties("noSearch");
      searchDMNMap = MappingUpgradeProperties.getPropertyMap("MPNsearchDMNMap");
      conversionDMNMap = MappingUpgradeProperties.getPropertyMap("conversionDMNMap");
      logger.info("Connecting to EDM Library...");
      OIAuthenticate oIAuthenticate = OIAuthenticateFactory.createBatchAuthenticate(str1);
      oIObjectManagerFactory = oIAuthenticate.login("Mapping Upgrade Application");
      logger.info("Connected to EDM Library.");
      oIObjectManager = oIObjectManagerFactory.createObjectManager();
      ContentProviderGlobal.setBatchExecMode();
      ContentProviderGlobal.setOIObjectManager(oIObjectManager);
      if (str3 == null) {
        logger.info("Extracting Content Provider Configuration Mapping...");
        OIQuery oIQuery = oIObjectManager.createQuery("ToolsContentProviderCfgs", true);
        oIQuery.addColumn("ToolBoxId");
        oIQuery.addRestriction("MetaDataMap.Key", "PROVIDER_ID");
        oIQuery.addRestriction("MetaDataMap.Value", str2);
        OICursor oICursor = oIQuery.execute();
        if (!oICursor.next()) {
          logger.error("ERROR: Content Provider Configuration Toolbox with PROVIDER_ID = '" + str2 + "' not found.");
          bool = true;
          return;
        } 
        oIObject = oICursor.getProxyObject().getObject();
        OIBlob oIBlob = oIObject.getBlob("CCPCfgBlob");
        String str = oIObject.getString("CCPCfgBlobPath");
        File file1 = new File(System.getProperty("java.io.tmpdir"), (new File(str)).getName());
        str4 = file1.getAbsolutePath();
        str3 = FilenameUtils.getFullPath(str4) + FilenameUtils.getFullPath(str4) + "_" + FilenameUtils.getBaseName(str4) + "." + nowDate();
        str4 = (new File(System.getProperty("java.io.tmpdir"), (new File(str)).getName())).getAbsolutePath();
        BufferedInputStream bufferedInputStream = new BufferedInputStream(oIBlob.getInputStream(), 32768);
        FileOutputStream fileOutputStream = new FileOutputStream(str3);
        try {
          byte[] arrayOfByte = new byte[1024];
          int i;
          for (i = bufferedInputStream.read(arrayOfByte); i > -1; i = bufferedInputStream.read(arrayOfByte))
            fileOutputStream.write(arrayOfByte, 0, i); 
          fileOutputStream.flush();
        } finally {
          fileOutputStream.close();
        } 
        logger.info("Current mapping configuration file was saved to '" + str3 + "'.");
      } 
      MappingUpgradeReport.createFile(str4);
      SchemaFactory schemaFactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
      StreamSource streamSource = new StreamSource(ContentProviderConfig.class.getResourceAsStream("schemas/ContentProviderCfg.xsd"));
      Schema schema = schemaFactory.newSchema(streamSource);
      Validator validator = schema.newValidator();
      logger.info("Reading current mapping configuration...");
      DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
      documentBuilderFactory.setNamespaceAware(true);
      DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
      if (isZipped(str3)) {
        try {
          ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(str3));
          try {
            ZipEntry zipEntry = zipInputStream.getNextEntry();
            str6 = zipEntry.getName();
            cfgDoc = documentBuilder.parse(zipInputStream);
            validator.validate(new DOMSource(cfgDoc));
            zipInputStream.close();
          } catch (Throwable throwable) {
            try {
              zipInputStream.close();
            } catch (Throwable throwable1) {
              throwable.addSuppressed(throwable1);
            } 
            throw throwable;
          } 
        } catch (Exception exception) {
          logger.error("ERROR: Failed to read mapping configuration. [" + str3 + "]");
          bool = true;
          return;
        } 
      } else {
        try {
          FileInputStream fileInputStream = new FileInputStream(str3);
          try {
            cfgDoc = documentBuilder.parse(fileInputStream);
            validator.validate(new DOMSource(cfgDoc));
            fileInputStream.close();
          } catch (Throwable throwable) {
            try {
              fileInputStream.close();
            } catch (Throwable throwable1) {
              throwable.addSuppressed(throwable1);
            } 
            throw throwable;
          } 
        } catch (Exception exception) {
          logger.error("ERROR: Failed to read mapping configuration. [" + str3 + "]");
          bool = true;
          return;
        } 
        str6 = (new File(str3)).getName();
      } 
      removeEmptyTextNodes(cfgDoc);
      ContentProviderFactory.getInstance().registerContentProviders(oIObjectManager);
      cpp = ContentProviderFactory.getInstance().createContentProvider(str2);
      if (cpp == null) {
        logger.error("ERROR: Content provider toolbox does not have a \"" + str2 + "\" on EDM Library Server.");
        bool = true;
        return;
      } 
      logger.info("Reading Content Provider data model...");
      partListCP = cpp.getPartClassInfo();
      logger.info("Found " + partListCP.size() + " part classes in Content Provider \"" + str2 + "\"");
      searchPropertyMap = cpp.readSeachCapabilities();
      processUpgrade(str5);
      if (0 < errorNum) {
        bool = true;
        return;
      } 
      logger.info("Writing upgraded mapping configuration...");
      if (str4.endsWith(".zip") || oIObject != null) {
        FileOutputStream fileOutputStream = new FileOutputStream(new File(str4));
        ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);
        StreamResult streamResult = new StreamResult(new OutputStreamWriter(zipOutputStream, "UTF-8"));
        zipOutputStream.putNextEntry(new ZipEntry(str6));
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty("omit-xml-declaration", "no");
        transformer.setOutputProperty("method", "xml");
        transformer.setOutputProperty("indent", "yes");
        transformer.setOutputProperty("encoding", "UTF-8");
        transformer.transform(new DOMSource(cfgDoc), streamResult);
        zipOutputStream.closeEntry();
        zipOutputStream.close();
      } else {
        FileOutputStream fileOutputStream = new FileOutputStream(new File(str4));
        StreamResult streamResult = new StreamResult(new OutputStreamWriter(fileOutputStream, "UTF-8"));
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty("omit-xml-declaration", "no");
        transformer.setOutputProperty("method", "xml");
        transformer.setOutputProperty("indent", "yes");
        transformer.setOutputProperty("encoding", "UTF-8");
        transformer.transform(new DOMSource(cfgDoc), streamResult);
        fileOutputStream.close();
      } 
      if (oIObject != null) {
        logger.info("Loading upgraded mapping to EDM Library...");
        oIObjectManager.refreshAndLockObject(oIObject);
        OIFile oIFile = FileUtilities.wrapBlob(oIObject.getBlob("CCPCfgBlob"));
        oIFile.lock();
        oIFile.setInputStream(new FileInputStream(str4));
        oIObjectManager.makePermanent(oIObject);
      } 
    } catch (Exception exception) {
      bool = true;
      logger.error(exception.getMessage(), exception);
    } finally {
      MappingUpgradeReport.close();
      if (oIObject != null)
        try {
          oIObjectManager.evict(oIObject);
        } catch (OIException oIException) {
          logger.warn(oIException.getMessage(), (Throwable)oIException);
        }  
      if (oIObjectManager != null)
        oIObjectManager.close(); 
      if (oIObjectManagerFactory != null) {
        logger.info("Disconnecting from EDM Library...");
        oIObjectManagerFactory.close();
      } 
      if (!bool) {
        logger.info("\nUpgrade complete!");
      } else {
        logger.info("\nUpgrade failed.");
      } 
      logger.info("");
      logger.info("Number of warnings: " + warnNum);
      logger.info("Number of errors  : " + errorNum);
      System.exit(bool);
    } 
  }
  
  private static int processUpgrade(String paramString) {
    logger.info("Reading csv... [" + paramString + "]");
    try {
      BufferedReader bufferedReader = new BufferedReader(new FileReader(paramString));
      try {
        String str1 = "";
        CatalogFromCsv catalogFromCsv = null;
        bufferedReader.readLine();
        byte b = 1;
        logger.info("Start upgrade mapping file.");
        logger.info("----------------------------");
        String str2;
        while ((str2 = bufferedReader.readLine()) != null) {
          b++;
          MappingUpgradeResult mappingUpgradeResult = null;
          try {
            ArrayList<String> arrayList = CSVUtil.csv2List(str2);
            if (!((String)arrayList.get(1)).equals(str1)) {
              str1 = arrayList.get(1);
              catalogFromCsv = new CatalogFromCsv(arrayList.get(1), arrayList.get(2));
              catalogFromCsv.setParentKey(getParentCatalogID(catalogFromCsv.getDomainName()));
            } 
            if (arrayList.size() > 10 && StringUtils.isNoneEmpty(new CharSequence[] { arrayList.get(10) })) {
              if (!((String)arrayList.get(9)).startsWith("NG:")) {
                logger.warn("Command is valid only for lines where the result is \"NG\". line:" + b);
                warnNum++;
                continue;
              } 
              String str3 = ((String)arrayList.get(10)).toUpperCase();
              String str4 = arrayList.get(8);
              if (str3.equals("C")) {
                HashMap<String, CPProperty> hashMap = getPropertiesCP(arrayList.get(1));
                mappingUpgradeResult = new MappingUpgradeResult(b, arrayList);
                if (str4.startsWith("1:")) {
                  mappingUpgradeResult.setProcName("1:Create Part Class Definition.");
                  processLogCat(mappingUpgradeResult);
                  mappingUpgradeResult = addCatalogPartClass(catalogFromCsv, hashMap, mappingUpgradeResult);
                } else if (str4.startsWith("3:")) {
                  mappingUpgradeResult.setProcName("3:Create Manufacturer Part Mapping.");
                  processLogCat(mappingUpgradeResult);
                  String str = catalogFromCsv.getDomainName();
                  mappingUpgradeResult = addCatalogMapMPN2PartClass(str, catalogFromCsv, hashMap, mappingUpgradeResult);
                } else if (str4.startsWith("4:")) {
                  mappingUpgradeResult.setProcName("4:Create Component Mapping.");
                  processLogCat(mappingUpgradeResult);
                  String str5 = "C_" + catalogFromCsv.getDomainName();
                  if (!isParentPartClass(catalogFromCsv.getDomainName()))
                    str5 = "C_" + catalogFromCsv.getParentKey(); 
                  mappingUpgradeResult = addCatalogMapComp2PartClass(str5, catalogFromCsv, hashMap, mappingUpgradeResult);
                  String str6 = catalogFromCsv.getDomainName();
                  mappingUpgradeResult = addCatalogMapMPN2Comps(str6, catalogFromCsv, hashMap, mappingUpgradeResult);
                } else if (str4.startsWith("7:")) {
                  mappingUpgradeResult.setProcName("7:Create Part Class Property Definition");
                  processLogProp(mappingUpgradeResult);
                  CPProperty cPProperty = hashMap.get(arrayList.get(3));
                  mappingUpgradeResult = addPropertyMapPartClass(arrayList.get(1), cPProperty, mappingUpgradeResult);
                } else if (str4.startsWith("9:")) {
                  mappingUpgradeResult.setProcName("9:Create Manufacturer Part Property Mapping");
                  processLogProp(mappingUpgradeResult);
                  mappingUpgradeResult = addPropertyMapMPN2PartClass(arrayList.get(7), catalogFromCsv.getDomainName(), arrayList.get(3), arrayList.get(4), mappingUpgradeResult);
                } else if (str4.startsWith("10:")) {
                  mappingUpgradeResult.setProcName("10:Create Component Property Mapping");
                  processLogProp(mappingUpgradeResult);
                  mappingUpgradeResult = addPropertyMapComp2PartClass(arrayList.get(7), catalogFromCsv.getDomainName(), arrayList.get(3), arrayList.get(4), mappingUpgradeResult);
                  mappingUpgradeResult = addPropertyMapComp2MPN(arrayList.get(7), catalogFromCsv.getDomainName(), arrayList.get(3), arrayList.get(4), mappingUpgradeResult);
                } else if (str4.startsWith("11:")) {
                  mappingUpgradeResult.setProcName("11:Create Manufacturer Part-Component Property Mapping");
                  processLogProp(mappingUpgradeResult);
                  mappingUpgradeResult = addPropertyMapComp2MPN(arrayList.get(7), catalogFromCsv.getDomainName(), arrayList.get(3), arrayList.get(4), mappingUpgradeResult);
                } else {
                  mappingUpgradeResult = null;
                } 
              } else if (str3.equals("D")) {
                mappingUpgradeResult = new MappingUpgradeResult(b, arrayList);
                if (str4.startsWith("16:")) {
                  mappingUpgradeResult.setProcName("16:Delete part Class definition.");
                  processLogCat(mappingUpgradeResult);
                  mappingUpgradeResult = deletePartClass(arrayList.get(1), mappingUpgradeResult);
                } else if (str4.startsWith("17:")) {
                  mappingUpgradeResult.setProcName("17:Delete part Class property definition.");
                  processLogProp(mappingUpgradeResult);
                  mappingUpgradeResult = deletePartClassProperty(arrayList.get(1), arrayList.get(3), mappingUpgradeResult);
                } else {
                  mappingUpgradeResult = null;
                } 
              } 
              if (mappingUpgradeResult != null) {
                if (mappingUpgradeResult.hasWarn())
                  for (String str : mappingUpgradeResult.getWarnMsgs()) {
                    logger.warn("  WARN: " + str + " line:" + b);
                    warnNum++;
                  }  
                if (mappingUpgradeResult.hasError())
                  for (String str : mappingUpgradeResult.getErrorMsgs()) {
                    logger.error("  ERROR: " + str + " line:" + b);
                    errorNum++;
                  }  
                MappingUpgradeReport.print(mappingUpgradeResult);
              } 
            } 
          } catch (Exception exception) {
            logger.error("ERROR: " + exception.getMessage() + " line:" + b, exception);
            errorNum++;
          } 
        } 
        bufferedReader.close();
        logger.info("----------------------------");
        bufferedReader.close();
      } catch (Throwable throwable) {
        try {
          bufferedReader.close();
        } catch (Throwable throwable1) {
          throwable.addSuppressed(throwable1);
        } 
        throw throwable;
      } 
    } catch (Throwable throwable) {
      logger.error("ERROR: " + throwable.getMessage(), throwable);
      errorNum++;
    } 
    return errorNum;
  }
  
  private static void processLogProp(MappingUpgradeResult paramMappingUpgradeResult) {
    logger.info(paramMappingUpgradeResult.getProcName() + "\t[" + paramMappingUpgradeResult.getProcName() + "]:" + paramMappingUpgradeResult.getPartID() + " - [" + paramMappingUpgradeResult.getPartName() + "]:" + paramMappingUpgradeResult.getPartPropID() + "\tline:" + paramMappingUpgradeResult.getPartPropName());
  }
  
  private static void processLogCat(MappingUpgradeResult paramMappingUpgradeResult) {
    logger.info(paramMappingUpgradeResult.getProcName() + "\t[" + paramMappingUpgradeResult.getProcName() + "]:" + paramMappingUpgradeResult.getPartID() + "\tline:" + paramMappingUpgradeResult.getPartName());
  }
  
  private static MappingUpgradeResult deletePartClass(String paramString, MappingUpgradeResult paramMappingUpgradeResult) throws XPathExpressionException {
    String str = "/ContentProviderCfg/PartClasses/PartClass[@id='" + paramString + "']";
    NodeList nodeList = (NodeList)xpath.evaluate(str, cfgDoc, XPathConstants.NODESET);
    if (nodeList.getLength() == 0)
      paramMappingUpgradeResult.addWarn("Element to be deleted not found.\t[" + str + "]"); 
    byte b;
    for (b = 0; b < nodeList.getLength(); b++)
      nodeList.item(b).getParentNode().removeChild(nodeList.item(b)); 
    str = "/ContentProviderCfg/ManufacturerPartCatalogs/ManufacturerPartCatalog/ContentProviderMaps/ContentProviderMap[@ccpId='" + paramString + "']";
    nodeList = (NodeList)xpath.evaluate(str, cfgDoc, XPathConstants.NODESET);
    if (nodeList.getLength() == 0)
      paramMappingUpgradeResult.addWarn("Element to be deleted not found.\t[" + str + "]"); 
    for (b = 0; b < nodeList.getLength(); b++) {
      Node node = nodeList.item(b).getParentNode();
      node.removeChild(nodeList.item(b));
      if (!node.hasChildNodes())
        node.getParentNode().getParentNode().removeChild(node.getParentNode()); 
    } 
    str = "/ContentProviderCfg/ComponentCatalogs/ComponentCatalog/ManufacturerPartMaps/ManufacturerPartMap[@classDMN='" + paramString + "']";
    nodeList = (NodeList)xpath.evaluate(str, cfgDoc, XPathConstants.NODESET);
    if (nodeList.getLength() == 0)
      paramMappingUpgradeResult.addWarn("Element to be deleted not found.\t[" + str + "]"); 
    for (b = 0; b < nodeList.getLength(); b++)
      nodeList.item(b).getParentNode().removeChild(nodeList.item(b)); 
    str = "/ContentProviderCfg/ComponentCatalogs/ComponentCatalog/ContentProviderMaps/ContentProviderMap[@ccpId='" + paramString + "']";
    nodeList = (NodeList)xpath.evaluate(str, cfgDoc, XPathConstants.NODESET);
    if (nodeList.getLength() == 0)
      paramMappingUpgradeResult.addWarn("Element to be deleted not found.\t[" + str + "]"); 
    for (b = 0; b < nodeList.getLength(); b++)
      nodeList.item(b).getParentNode().removeChild(nodeList.item(b)); 
    return paramMappingUpgradeResult;
  }
  
  private static MappingUpgradeResult deletePartClassProperty(String paramString1, String paramString2, MappingUpgradeResult paramMappingUpgradeResult) throws XPathExpressionException {
    String str = "/ContentProviderCfg/PartClasses/PartClass[@id='" + paramString1 + "']/Properties/Property[@id='" + paramString2 + "']";
    NodeList nodeList = (NodeList)xpath.evaluate(str, cfgDoc, XPathConstants.NODESET);
    if (nodeList.getLength() == 0)
      paramMappingUpgradeResult.addWarn("Element to be deleted not found.\t[" + str + "]"); 
    byte b;
    for (b = 0; b < nodeList.getLength(); b++)
      nodeList.item(b).getParentNode().removeChild(nodeList.item(b)); 
    str = "/ContentProviderCfg/ManufacturerPartCatalogs/ManufacturerPartCatalog/ContentProviderMaps/ContentProviderMap[@ccpId='" + paramString1 + "']/ContentProviderPropertyMaps/ContentProviderPropertyMap[@ccpId='" + paramString2 + "']";
    nodeList = (NodeList)xpath.evaluate(str, cfgDoc, XPathConstants.NODESET);
    if (nodeList.getLength() == 0)
      paramMappingUpgradeResult.addWarn("Element to be deleted not found.\t[" + str + "]"); 
    for (b = 0; b < nodeList.getLength(); b++)
      nodeList.item(b).getParentNode().removeChild(nodeList.item(b)); 
    str = "/ContentProviderCfg/ComponentCatalogs/ComponentCatalog/ManufacturerPartMaps/ManufacturerPartMap[@classDMN='" + paramString1 + "']/ManufacturerPartPropertyMaps/ManufacturerPartPropertyMap[@mpnDMN='" + paramString2 + "']";
    nodeList = (NodeList)xpath.evaluate(str, cfgDoc, XPathConstants.NODESET);
    if (nodeList.getLength() == 0)
      paramMappingUpgradeResult.addWarn("Element to be deleted not found.\t[" + str + "]"); 
    for (b = 0; b < nodeList.getLength(); b++)
      nodeList.item(b).getParentNode().removeChild(nodeList.item(b)); 
    str = "/ContentProviderCfg/ComponentCatalogs/ComponentCatalog/ContentProviderMaps/ContentProviderMap[@ccpId='" + paramString1 + "']/ContentProviderPropertyMaps/ContentProviderPropertyMap[@ccpId='" + paramString2 + "']";
    nodeList = (NodeList)xpath.evaluate(str, cfgDoc, XPathConstants.NODESET);
    if (nodeList.getLength() == 0)
      paramMappingUpgradeResult.addWarn("Element to be deleted not found.\t[" + str + "]"); 
    for (b = 0; b < nodeList.getLength(); b++)
      nodeList.item(b).getParentNode().removeChild(nodeList.item(b)); 
    return paramMappingUpgradeResult;
  }
  
  private static MappingUpgradeResult addCatalogPartClass(CatalogFromCsv paramCatalogFromCsv, Map<String, CPProperty> paramMap, MappingUpgradeResult paramMappingUpgradeResult) throws XPathExpressionException {
    String str1 = paramCatalogFromCsv.getDomainName();
    String str2 = "/ContentProviderCfg/PartClasses/PartClass[@id='" + str1 + "']";
    NodeList nodeList = (NodeList)xpath.evaluate(str2, cfgDoc, XPathConstants.NODESET);
    if (nodeList.getLength() == 0) {
      String str = getParentPath(str2);
      NodeList nodeList1 = (NodeList)xpath.evaluate(str, cfgDoc, XPathConstants.NODESET);
      if (nodeList1.getLength() == 0) {
        paramMappingUpgradeResult.addError("Parent node not found. [" + str + "]");
        return paramMappingUpgradeResult;
      } 
      if (nodeList1.getLength() > 1) {
        paramMappingUpgradeResult.addError("There are multiple parent nodes. [" + str + "]");
        return paramMappingUpgradeResult;
      } 
      Element element1 = cfgDoc.createElement("PartClass");
      element1.setAttribute("id", paramCatalogFromCsv.getDomainName());
      element1.setAttribute("label", paramCatalogFromCsv.getName());
      element1.setAttribute("parentId", paramCatalogFromCsv.getParentKey());
      Element element2 = cfgDoc.createElement("Properties");
      element1.appendChild(element2);
      nodeList1.item(0).appendChild(element1);
    } else {
      paramMappingUpgradeResult.addWarn("Part Class definition already exist.");
    } 
    return paramMappingUpgradeResult;
  }
  
  private static MappingUpgradeResult addCatalogMapMPN2Comps(String paramString, CatalogFromCsv paramCatalogFromCsv, Map<String, CPProperty> paramMap, MappingUpgradeResult paramMappingUpgradeResult) throws XPathExpressionException {
    String str1 = "C_" + paramCatalogFromCsv.getDomainName();
    String str2 = paramCatalogFromCsv.getName();
    if (!isParentPartClass(paramCatalogFromCsv.getDomainName())) {
      str1 = "C_" + paramCatalogFromCsv.getParentKey();
      str2 = getPartName(paramCatalogFromCsv.getParentKey());
    } 
    String str3 = "/ContentProviderCfg/ComponentCatalogs/ComponentCatalog[@classDMN='" + str1 + "']/ManufacturerPartMaps/ManufacturerPartMap[@classDMN='" + paramString + "']";
    NodeList nodeList = (NodeList)xpath.evaluate(str3, cfgDoc, XPathConstants.NODESET);
    if (nodeList.getLength() == 0) {
      String str = "/ContentProviderCfg/ComponentCatalogs/ComponentCatalog[@classDMN='" + str1 + "']";
      Element element = getOrCreateElement(str);
      if (!element.hasAttribute("classDMN")) {
        element.setAttribute("classDMN", str1);
        element.setAttribute("label", str2);
      } 
      NodeList nodeList1 = element.getElementsByTagName("ManufacturerPartMaps");
      if (nodeList1.getLength() == 0) {
        element.appendChild(cfgDoc.createElement("ManufacturerPartMaps"));
        nodeList1 = element.getElementsByTagName("ManufacturerPartMaps");
      } else if (nodeList1.getLength() > 1) {
        paramMappingUpgradeResult.addWarn("There are multiple nodes. [" + getParentPath(str3) + "]");
      } 
      for (byte b = 0; b < nodeList1.getLength(); b++) {
        Element element1 = cfgDoc.createElement("ManufacturerPartMap");
        element1.setAttribute("classDMN", paramString);
        Element element2 = cfgDoc.createElement("ManufacturerPartPropertyMaps");
        element1.appendChild(element2);
        nodeList1.item(b).appendChild(element1);
      } 
    } else {
      paramMappingUpgradeResult.addWarn("Mapping between Manufacturer Part and Component already exists.");
    } 
    return paramMappingUpgradeResult;
  }
  
  private static Element getOrCreateElement(String paramString) throws XPathExpressionException {
    NodeList nodeList = (NodeList)xpath.evaluate(paramString, cfgDoc, XPathConstants.NODESET);
    if (nodeList.getLength() == 0) {
      String str = getParentPath(paramString);
      Element element1 = getOrCreateElement(str);
      Element element2 = cfgDoc.createElement(getElementName(paramString));
      element1.appendChild(element2);
      return element2;
    } 
    if (1 < nodeList.getLength())
      logger.warn("There are multiple parent nodes. [" + paramString + "]"); 
    return (Element)nodeList.item(0);
  }
  
  private static String getElementName(String paramString) {
    if (!paramString.contains("/")) {
      System.out.println("Invalid xpath: " + paramString);
      return "";
    } 
    paramString = paramString.substring(paramString.lastIndexOf("/") + 1);
    return paramString.replaceAll("\\[@.*\\]$", "");
  }
  
  private static MappingUpgradeResult addCatalogMapMPN2PartClass(String paramString, CatalogFromCsv paramCatalogFromCsv, Map<String, CPProperty> paramMap, MappingUpgradeResult paramMappingUpgradeResult) throws MappingUpgradeException, XPathExpressionException {
    String str1 = paramCatalogFromCsv.getDomainName();
    String str2 = "";
    if (paramString != null)
      str2 = "[@classDMN='" + paramString + "']"; 
    String str3 = "/ContentProviderCfg/ManufacturerPartCatalogs/ManufacturerPartCatalog" + str2 + "/ContentProviderMaps/ContentProviderMap[@ccpId='" + str1 + "']";
    NodeList nodeList = (NodeList)xpath.evaluate(str3, cfgDoc, XPathConstants.NODESET);
    if (nodeList.getLength() == 0) {
      String str = "/ContentProviderCfg/ManufacturerPartCatalogs/ManufacturerPartCatalog" + str2;
      Element element = getOrCreateElement(str);
      if (!element.hasAttribute("classDMN")) {
        element.setAttribute("classDMN", str1);
        element.setAttribute("label", paramCatalogFromCsv.getName());
      } 
      NodeList nodeList1 = element.getElementsByTagName("ContentProviderMaps");
      if (nodeList1.getLength() == 0) {
        element.appendChild(cfgDoc.createElement("ContentProviderMaps"));
        nodeList1 = element.getElementsByTagName("ContentProviderMaps");
      } else if (nodeList1.getLength() > 1) {
        paramMappingUpgradeResult.addWarn("There are multiple nodes. [" + getParentPath(str3) + "]");
      } 
      for (byte b = 0; b < nodeList1.getLength(); b++) {
        Element element1 = cfgDoc.createElement("ContentProviderMap");
        element1.setAttribute("ccpId", str1);
        Element element2 = cfgDoc.createElement("ContentProviderPropertyMaps");
        element1.appendChild(element2);
        nodeList1.item(b).appendChild(element1);
      } 
    } else {
      paramMappingUpgradeResult.addWarn("Already mapped to Manufacturer Part.");
    } 
    return paramMappingUpgradeResult;
  }
  
  private static MappingUpgradeResult addCatalogMapComp2PartClass(String paramString, CatalogFromCsv paramCatalogFromCsv, Map<String, CPProperty> paramMap, MappingUpgradeResult paramMappingUpgradeResult) throws XPathExpressionException {
    String str1 = paramCatalogFromCsv.getDomainName();
    String str2 = "";
    if (paramString != null)
      str2 = "[@classDMN='" + paramString + "']"; 
    String str3 = "/ContentProviderCfg/ComponentCatalogs/ComponentCatalog" + str2 + "/ContentProviderMaps/ContentProviderMap[@ccpId='" + str1 + "']";
    NodeList nodeList = (NodeList)xpath.evaluate(str3, cfgDoc, XPathConstants.NODESET);
    if (nodeList.getLength() == 0) {
      paramString = "C_" + paramCatalogFromCsv.getDomainName();
      String str4 = paramCatalogFromCsv.getName();
      if (!isParentPartClass(paramCatalogFromCsv.getDomainName())) {
        paramString = "C_" + paramCatalogFromCsv.getParentKey();
        str4 = getPartName(paramCatalogFromCsv.getParentKey());
      } 
      String str5 = "/ContentProviderCfg/ComponentCatalogs/ComponentCatalog[@classDMN='" + paramString + "']";
      Element element = getOrCreateElement(str5);
      if (!element.hasAttribute("classDMN")) {
        element.setAttribute("classDMN", paramString);
        element.setAttribute("label", str4);
      } 
      NodeList nodeList1 = element.getElementsByTagName("ContentProviderMaps");
      if (nodeList1.getLength() == 0) {
        element.appendChild(cfgDoc.createElement("ContentProviderMaps"));
        nodeList1 = element.getElementsByTagName("ContentProviderMaps");
      } else if (nodeList1.getLength() > 1) {
        paramMappingUpgradeResult.addWarn("There are multiple nodes. [" + getParentPath(str3) + "]");
      } 
      for (byte b = 0; b < nodeList1.getLength(); b++) {
        Element element1 = cfgDoc.createElement("ContentProviderMap");
        element1.setAttribute("ccpId", str1);
        Element element2 = cfgDoc.createElement("ContentProviderPropertyMaps");
        element1.appendChild(element2);
        nodeList1.item(b).appendChild(element1);
      } 
    } else {
      paramMappingUpgradeResult.addWarn("Already mapped to Component.");
    } 
    return paramMappingUpgradeResult;
  }
  
  private static MappingUpgradeResult addPropertyMapPartClass(String paramString, CPProperty paramCPProperty, MappingUpgradeResult paramMappingUpgradeResult) throws MappingUpgradeException, XPathExpressionException {
    String str = "/ContentProviderCfg/PartClasses/PartClass[@id='" + paramString + "']/Properties/Property[@id='" + paramCPProperty.getId() + "']";
    NodeList nodeList = (NodeList)xpath.evaluate(str, cfgDoc, XPathConstants.NODESET);
    if (nodeList.getLength() == 0) {
      String str1 = getParentPath(str);
      NodeList nodeList1 = (NodeList)xpath.evaluate(str1, cfgDoc, XPathConstants.NODESET);
      if (nodeList1.getLength() == 0) {
        paramMappingUpgradeResult.addError("Part Class definition not Found.");
        return paramMappingUpgradeResult;
      } 
      if (nodeList1.getLength() > 1) {
        paramMappingUpgradeResult.addError("There are multiple Part Class Definitions.");
        return paramMappingUpgradeResult;
      } 
      Element element = cfgDoc.createElement("Property");
      element.setAttribute("id", paramCPProperty.getId());
      element.setAttribute("label", paramCPProperty.getName());
      if (paramCPProperty.getUnit() != null)
        element.setAttribute("baseUnits", paramCPProperty.getUnit()); 
      SearchCapability searchCapability = searchPropertyMap.get(paramCPProperty.getId());
      if (searchCapability != null && searchCapability.isSearchable())
        element.setAttribute("searchable", "true"); 
      if (propsNoSearch.contains(paramCPProperty.getId()))
        element.setAttribute("searchable", "false"); 
      if (propsID.contains(paramCPProperty.getId()))
        element.setAttribute("idProperty", "true"); 
      nodeList1.item(0).appendChild(element);
    } else {
      paramMappingUpgradeResult.addWarn("Part Class property definition already exist.");
    } 
    return paramMappingUpgradeResult;
  }
  
  private static MappingUpgradeResult addPropertyMapMPN2PartClass(String paramString1, String paramString2, String paramString3, String paramString4, MappingUpgradeResult paramMappingUpgradeResult) throws MappingUpgradeException, XPathExpressionException {
    String str1 = "";
    if (paramString1 != null)
      str1 = "[@classDMN='" + paramString1 + "']"; 
    String str2 = "/ContentProviderCfg/ManufacturerPartCatalogs/ManufacturerPartCatalog" + str1 + "/ContentProviderMaps/ContentProviderMap[@ccpId='" + paramString2 + "']/ContentProviderPropertyMaps/ContentProviderPropertyMap[@ccpId='" + paramString3 + "']";
    NodeList nodeList = (NodeList)xpath.evaluate(str2, cfgDoc, XPathConstants.NODESET);
    if (nodeList.getLength() == 0) {
      String str = getParentPath(str2);
      NodeList nodeList1 = (NodeList)xpath.evaluate(str, cfgDoc, XPathConstants.NODESET);
      if (nodeList1.getLength() == 0) {
        paramMappingUpgradeResult.addError("No manufacturer part mapped to the part class.");
        return paramMappingUpgradeResult;
      } 
      if (nodeList1.getLength() > 1 && paramString1 != null)
        paramMappingUpgradeResult.addWarn("There are multiple parent nodes."); 
      for (byte b = 0; b < nodeList1.getLength(); b++) {
        Element element = cfgDoc.createElement("ContentProviderPropertyMap");
        element.setAttribute("ccpId", paramString3);
        element.setAttribute("inherit", "false");
        element.setAttribute("label", paramString4);
        if (conversionDMNMap.containsKey(paramString3)) {
          element.setAttribute("dmn", conversionDMNMap.get(paramString3));
        } else {
          element.setAttribute("dmn", paramString3);
        } 
        if (searchDMNMap.containsKey(paramString3))
          element.setAttribute("searchDMN", searchDMNMap.get(paramString3)); 
        nodeList1.item(b).appendChild(element);
      } 
    } else {
      paramMappingUpgradeResult.addWarn("The property already mapped.");
    } 
    return paramMappingUpgradeResult;
  }
  
  private static MappingUpgradeResult addPropertyMapComp2PartClass(String paramString1, String paramString2, String paramString3, String paramString4, MappingUpgradeResult paramMappingUpgradeResult) throws XPathExpressionException {
    String str1 = "";
    if (paramString1 != null)
      str1 = "[@classDMN='" + paramString1 + "']"; 
    String str2 = "/ContentProviderCfg/ComponentCatalogs/ComponentCatalog" + str1 + "/ContentProviderMaps/ContentProviderMap[@ccpId='" + paramString2 + "']/ContentProviderPropertyMaps/ContentProviderPropertyMap[@ccpId='" + paramString3 + "']";
    NodeList nodeList = (NodeList)xpath.evaluate(str2, cfgDoc, XPathConstants.NODESET);
    if (nodeList.getLength() == 0) {
      String str = getParentPath(str2);
      NodeList nodeList1 = (NodeList)xpath.evaluate(str, cfgDoc, XPathConstants.NODESET);
      if (nodeList1.getLength() == 0) {
        paramMappingUpgradeResult.addError("No components mapped to the part class.");
        return paramMappingUpgradeResult;
      } 
      if (nodeList1.getLength() > 1 && paramString1 != null)
        paramMappingUpgradeResult.addWarn("There are multiple parent nodes.\t[" + str + "]"); 
      for (byte b = 0; b < nodeList1.getLength(); b++) {
        Element element = cfgDoc.createElement("ContentProviderPropertyMap");
        if (conversionDMNMap.containsKey(paramString3)) {
          element.setAttribute("dmn", conversionDMNMap.get(paramString3));
        } else {
          element.setAttribute("dmn", "001_" + paramString3);
        } 
        element.setAttribute("inherit", "false");
        element.setAttribute("label", paramString4);
        element.setAttribute("ccpId", paramString3);
        nodeList1.item(b).appendChild(element);
      } 
    } else {
      paramMappingUpgradeResult.addWarn("The property already mapped.");
    } 
    return paramMappingUpgradeResult;
  }
  
  private static MappingUpgradeResult addPropertyMapComp2MPN(String paramString1, String paramString2, String paramString3, String paramString4, MappingUpgradeResult paramMappingUpgradeResult) throws XPathExpressionException {
    String str1 = "";
    if (paramString1 != null)
      str1 = "[@classDMN='" + paramString1 + "']"; 
    String str2 = "/ContentProviderCfg/ComponentCatalogs/ComponentCatalog" + str1 + "/ManufacturerPartMaps/ManufacturerPartMap[@classDMN='" + paramString2 + "']/ManufacturerPartPropertyMaps/ManufacturerPartPropertyMap[@mpnDMN='" + paramString3 + "']";
    NodeList nodeList = (NodeList)xpath.evaluate(str2, cfgDoc, XPathConstants.NODESET);
    if (nodeList.getLength() == 0) {
      String str = getParentPath(str2);
      NodeList nodeList1 = (NodeList)xpath.evaluate(str, cfgDoc, XPathConstants.NODESET);
      if (nodeList1.getLength() == 0) {
        paramMappingUpgradeResult.addError("No Components mapped to the Manufactuer Part.");
        return paramMappingUpgradeResult;
      } 
      if (nodeList1.getLength() > 1 && paramString1 != null)
        paramMappingUpgradeResult.addWarn("There are multiple components."); 
      for (byte b = 0; b < nodeList1.getLength(); b++) {
        Element element = cfgDoc.createElement("ManufacturerPartPropertyMap");
        if (conversionDMNMap.containsKey(paramString3)) {
          element.setAttribute("compDMN", conversionDMNMap.get(paramString3));
          element.setAttribute("mpnDMN", conversionDMNMap.get(paramString3));
        } else {
          element.setAttribute("compDMN", "001_" + paramString3);
          element.setAttribute("mpnDMN", paramString3);
        } 
        element.setAttribute("inherit", "false");
        element.setAttribute("label", paramString4);
        element.setAttribute("syncType", "DIRECT");
        nodeList1.item(b).appendChild(element);
      } 
    } else {
      paramMappingUpgradeResult.addWarn("The property already mapped.");
    } 
    return paramMappingUpgradeResult;
  }
  
  private static String getParentPath(String paramString) {
    if (!paramString.contains("/")) {
      logger.warn("Invalid xpath: " + paramString);
      return paramString;
    } 
    return paramString.substring(0, paramString.lastIndexOf("/"));
  }
  
  private static String getParentCatalogID(String paramString) {
    for (CPPartClass cPPartClass : partListCP) {
      if (paramString.equals(cPPartClass.getId()))
        return cPPartClass.getParentID(); 
    } 
    return "";
  }
  
  private static boolean isParentPartClass(String paramString) {
    for (CPPartClass cPPartClass : partListCP) {
      if (paramString.equals(cPPartClass.getParentID()))
        return true; 
    } 
    return false;
  }
  
  private static String getPartName(String paramString) {
    for (CPPartClass cPPartClass : partListCP) {
      if (paramString.equals(cPPartClass.getParentID()))
        return cPPartClass.getLabel(); 
    } 
    logger.warn("Part class label not found. [" + paramString + "]");
    return "";
  }
  
  private static HashMap<String, CPProperty> getPropertiesCP(String paramString) throws Exception {
    if (!partPropetiesCP.containsKey(paramString)) {
      Thread.sleep(1000L);
      HashMap<Object, Object> hashMap = new HashMap<>();
      List list = cpp.getPartProperties(paramString);
      for (CPProperty cPProperty : list)
        hashMap.put(cPProperty.getId(), cPProperty); 
      partPropetiesCP.put(paramString, hashMap);
    } 
    return partPropetiesCP.get(paramString);
  }
  
  private static boolean isZipped(String paramString) throws IOException {
    byte[] arrayOfByte1 = { 80, 75, 3, 4 };
    boolean bool = true;
    FileInputStream fileInputStream = null;
    fileInputStream = new FileInputStream(paramString);
    byte[] arrayOfByte2 = new byte[4];
    fileInputStream.read(arrayOfByte2);
    for (byte b = 0; b < arrayOfByte1.length; b++) {
      if (arrayOfByte2[b] != arrayOfByte1[b])
        bool = false; 
    } 
    return bool;
  }
  
  private static String nowDate() {
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    return simpleDateFormat.format(calendar.getTime());
  }
  
  private static void removeEmptyTextNodes(Node paramNode) {
    try {
      if (paramNode == null)
        return; 
      if (paramNode.getNodeType() == 3) {
        String str = paramNode.getTextContent().trim();
        if (str.isEmpty())
          paramNode.getParentNode().removeChild(paramNode); 
      } else {
        String str = paramNode.getNodeName();
      } 
      NodeList nodeList = paramNode.getChildNodes();
      for (int i = nodeList.getLength() - 1; i >= 0; i--) {
        if (nodeList.item(i) != null)
          removeEmptyTextNodes(nodeList.item(i)); 
      } 
    } catch (Exception|Error exception) {
      exception.printStackTrace();
    } 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\datamode\\upgrade\MappingUpgradeApp.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */