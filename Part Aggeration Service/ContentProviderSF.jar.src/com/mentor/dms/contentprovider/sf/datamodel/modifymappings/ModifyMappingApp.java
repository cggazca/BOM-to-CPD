package com.mentor.dms.contentprovider.sf.datamodel.modifymappings;

import com.mentor.datafusion.oi.OIObjectManager;
import com.mentor.datafusion.oi.OIObjectManagerFactory;
import com.mentor.datafusion.oi.login.OIAuthenticate;
import com.mentor.datafusion.oi.login.OIAuthenticateFactory;
import com.mentor.datafusion.utils.logger.MGLogger;
import com.mentor.dms.contentprovider.core.AbstractContentProvider;
import com.mentor.dms.contentprovider.core.ContentProviderFactory;
import com.mentor.dms.contentprovider.core.ContentProviderGlobal;
import com.mentor.dms.contentprovider.core.config.ContentProviderConfig;
import com.mentor.dms.contentprovider.core.config.SearchCapability;
import com.mentor.dms.contentprovider.core.utils.LogConfigLoader;
import com.mentor.dms.contentprovider.core.utils.validate.CPProperty;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ModifyMappingApp {
  static MGLogger logger = MGLogger.getLogger(ModifyMappingApp.class);
  
  private static final String XPATH_PARTCLASS_BASE = "/ContentProviderCfg/PartClasses/PartClass";
  
  private static final String XPATH_MPN_BASE = "/ContentProviderCfg/ManufacturerPartCatalogs/ManufacturerPartCatalog";
  
  private static final String XPATH_COMP_BASE = "/ContentProviderCfg/ComponentCatalogs/ComponentCatalog";
  
  private static OIObjectManager om = null;
  
  private static AbstractContentProvider cpp;
  
  private static Document cfgDoc;
  
  private static Map<String, HashMap<String, CPProperty>> partPropetiesCP = new HashMap<>();
  
  private static HashMap<String, SearchCapability> searchPropertyMap;
  
  private static XPath xpath = XPathFactory.newInstance().newXPath();
  
  private static int errorNum = 0;
  
  private static int warnNum = 0;
  
  public static void main(String[] paramArrayOfString) {
    try {
      LogConfigLoader.configLog4j();
    } catch (Exception exception) {
      exception.printStackTrace();
      System.out.println("Problems with the logging settings");
    } 
    logger.info("#");
    logger.info("#                  ModifyMapping - Version 1.1.1");
    logger.info("#                   Modify Mapping Application");
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
    boolean bool = false;
    Options options = new Options();
    options.addOption("help", false, "help");
    options.addOption("loginconfig", true, "EDM Library auto-login configuration");
    options.addOption("ccpid", true, "Content Provider ID");
    options.addOption("inmappingfile", true, "Input Content Provider Configuration Mapping filename");
    options.addOption("outmappingfile", true, "Output Content Provider Configuration Mapping filename");
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
    str2 = commandLine.getOptionValue("inmappingfile");
    str3 = commandLine.getOptionValue("outmappingfile");
    if (str2 == null) {
      logger.error("Error: Input Content Provider Configuration Mapping filename is required.");
      System.exit(1);
    } 
    if (str3 == null) {
      logger.error("Error: Output Content Provider Configuration Mapping filename is required.");
      System.exit(1);
    } 
    if (commandLine.hasOption("loginconfig")) {
      str4 = commandLine.getOptionValue("loginconfig");
    } else {
      logger.error("Error: EDM Library auto-login configuration is required.");
      System.exit(1);
    } 
    if (commandLine.hasOption("ccpid")) {
      str1 = commandLine.getOptionValue("ccpid");
    } else {
      logger.error("Error: Content Provider ID is required.");
      System.exit(1);
    } 
    OIObjectManagerFactory oIObjectManagerFactory = null;
    try {
      logger.info("Connecting to EDM Library...");
      OIAuthenticate oIAuthenticate = OIAuthenticateFactory.createBatchAuthenticate(str4);
      oIObjectManagerFactory = oIAuthenticate.login("Validation Application");
      logger.info("Connected");
      om = oIObjectManagerFactory.createObjectManager();
      ContentProviderGlobal.setBatchExecMode();
      ContentProviderGlobal.setOIObjectManager(om);
      ContentProviderFactory.getInstance().registerContentProviders(om);
      cpp = ContentProviderFactory.getInstance().createContentProvider(str1);
      searchPropertyMap = cpp.readSeachCapabilities();
      if (cpp == null) {
        logger.error("Content provider toolbox does not have a \"" + str1 + "\" on EDM Library Server.");
        return;
      } 
    } catch (Exception exception) {
      logger.error("Unable to connect to EDM Library Server:  " + exception.getMessage());
      return;
    } finally {
      if (om != null)
        om.close(); 
      if (oIObjectManagerFactory != null)
        oIObjectManagerFactory.close(); 
    } 
    String str5 = null;
    try {
      SchemaFactory schemaFactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
      StreamSource streamSource = new StreamSource(ContentProviderConfig.class.getResourceAsStream("schemas/ContentProviderCfg.xsd"));
      Schema schema = schemaFactory.newSchema(streamSource);
      Validator validator = schema.newValidator();
      logger.info("Reading current mapping configuration...");
      DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
      documentBuilderFactory.setNamespaceAware(true);
      DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
      if (isZipped(str2)) {
        try {
          ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(str2));
          try {
            ZipEntry zipEntry = zipInputStream.getNextEntry();
            str5 = zipEntry.getName();
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
          logger.error("ERROR: Failed to read mapping configuration. [" + str2 + "]");
          bool = true;
          return;
        } 
      } else {
        try {
          FileInputStream fileInputStream = new FileInputStream(str2);
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
          logger.error("ERROR: Failed to read mapping configuration. [" + str2 + "]");
          bool = true;
          return;
        } 
        str5 = (new File(str2)).getName();
      } 
      removeEmptyTextNodes(cfgDoc);
      processUpgrade();
      upgradeUnit();
      if (0 < errorNum) {
        bool = true;
        return;
      } 
      logger.info("");
      logger.info("Writing upgraded mapping configuration...");
      if (str3.endsWith(".zip")) {
        FileOutputStream fileOutputStream = new FileOutputStream(new File(str3));
        ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);
        StreamResult streamResult = new StreamResult(new OutputStreamWriter(zipOutputStream, "UTF-8"));
        zipOutputStream.putNextEntry(new ZipEntry(str5));
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
        FileOutputStream fileOutputStream = new FileOutputStream(new File(str3));
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
    } catch (Exception exception) {
      bool = true;
      logger.error(exception.getMessage(), exception);
    } finally {
      if (!bool) {
        logger.info("\nMapping modification complete!");
      } else {
        logger.info("\nMapping modification failed.");
      } 
      logger.info("");
      logger.info("Number of warnings: " + warnNum);
      logger.info("Number of errors  : " + errorNum);
      System.exit(bool);
    } 
  }
  
  private static int processUpgrade() {
    logger.info("");
    logger.info("Updating inherit settings.");
    try {
      String str = "/ContentProviderCfg/ManufacturerPartCatalogs/ManufacturerPartCatalog/ContentProviderMaps/ContentProviderMap/ContentProviderPropertyMaps";
      NodeList nodeList = (NodeList)xpath.evaluate(str, cfgDoc, XPathConstants.NODESET);
      logger.info("Number of Manufacturer Part mappings:" + nodeList.getLength());
      for (byte b = 1; b < nodeList.getLength(); b++) {
        boolean bool = false;
        NodeList nodeList1 = nodeList.item(b).getChildNodes();
        for (byte b1 = 0; b1 < nodeList1.getLength(); b1++) {
          Element element = (Element)nodeList1.item(b1);
          if (element.hasAttribute("dmn") && (element.getAttribute("dmn").equals("3b3c5480") || element.getAttribute("dmn").equals("6e83fbac"))) {
            bool = true;
            if (element.hasAttribute("inherit") && element.getAttribute("inherit").equals("true")) {
              element.setAttribute("inherit", "false");
              logger.info("Modify inherit settings. " + element.getAttribute("dmn"));
            } 
          } 
        } 
        if (!bool) {
          Element element1 = cfgDoc.createElement("ContentProviderPropertyMap");
          element1.setAttribute("ccpId", "3b3c5480");
          element1.setAttribute("dmn", "3b3c5480");
          element1.setAttribute("inherit", "false");
          element1.setAttribute("label", "Country Of Origin");
          nodeList.item(b).appendChild(element1);
          logger.info("Create new mapping. Country Of Origin:[3b3c5480]");
          Element element2 = cfgDoc.createElement("ContentProviderPropertyMap");
          element2.setAttribute("ccpId", "6e83fbac");
          element2.setAttribute("dmn", "6e83fbac");
          element2.setAttribute("inherit", "false");
          element2.setAttribute("label", "YTEOL");
          nodeList.item(b).appendChild(element2);
          logger.info("Create new mapping. YTEOL:[6e83fbac]");
        } 
      } 
    } catch (Exception exception) {
      logger.error("ERROR: " + exception.getMessage(), exception);
      errorNum++;
    } 
    logger.info("");
    logger.info("Remove list properties mappings for Component.");
    try {
      String str1 = "/ContentProviderCfg/ComponentCatalogs/ComponentCatalog/ContentProviderMaps/ContentProviderMap/ContentProviderPropertyMaps";
      String str2 = "/ContentProviderPropertyMap[@dmn='001_9c89c3f1']";
      String str3 = "/ContentProviderPropertyMap[@dmn='001_33786d02']";
      String str4 = "/ContentProviderPropertyMap[@dmn='001_f5724997']";
      String str5 = "/ContentProviderPropertyMap[@dmn='001_06a09139']";
      NodeList nodeList = (NodeList)xpath.evaluate(str1 + str1, cfgDoc, XPathConstants.NODESET);
      logger.info("Number of Component mappings. Functional Equivalent:[001_9c89c3f1] " + nodeList.getLength());
      int i;
      for (i = nodeList.getLength() - 1; 0 <= i; i--)
        nodeList.item(i).getParentNode().removeChild(nodeList.item(i)); 
      nodeList = (NodeList)xpath.evaluate(str1 + str1, cfgDoc, XPathConstants.NODESET);
      logger.info("Number of Component mappings. Similar Alternates:[001_33786d02] " + nodeList.getLength());
      for (i = nodeList.getLength() - 1; 0 <= i; i--)
        nodeList.item(i).getParentNode().removeChild(nodeList.item(i)); 
      nodeList = (NodeList)xpath.evaluate(str1 + str1, cfgDoc, XPathConstants.NODESET);
      logger.info("Number of Component mappings. FFF Equivalent:[001_f5724997] " + nodeList.getLength());
      for (i = nodeList.getLength() - 1; 0 <= i; i--)
        nodeList.item(i).getParentNode().removeChild(nodeList.item(i)); 
      nodeList = (NodeList)xpath.evaluate(str1 + str1, cfgDoc, XPathConstants.NODESET);
      logger.info("Number of Component mappings. Direct Alternates:[001_06a09139] " + nodeList.getLength());
      for (i = nodeList.getLength() - 1; 0 <= i; i--)
        nodeList.item(i).getParentNode().removeChild(nodeList.item(i)); 
    } catch (Exception exception) {
      logger.error("ERROR: " + exception.getMessage(), exception);
      errorNum++;
    } 
    return errorNum;
  }
  
  private static int upgradeUnit() {
    logger.info("");
    logger.info("Updating baseUnits.");
    Set<String> set = searchPropertyMap.keySet();
    for (String str : set) {
      SearchCapability searchCapability = searchPropertyMap.get(str);
      try {
        String str1 = str.toString();
        String str2 = "/ContentProviderCfg/PartClasses/PartClass/Properties/Property[@id='" + str1 + "']";
        NodeList nodeList = (NodeList)xpath.evaluate(str2, cfgDoc, XPathConstants.NODESET);
        String str3 = searchCapability.getUnit();
        if (logger.isDebugEnabled()) {
          logger.debug(String.valueOf(str) + " Unit:" + String.valueOf(str));
          logger.debug("Number of property definitions in mapping file:" + nodeList.getLength());
        } 
        boolean bool = false;
        for (byte b = 0; b < nodeList.getLength(); b++) {
          Element element = (Element)nodeList.item(b);
          String str4 = element.getAttribute("label");
          if (searchCapability.getUnit() != null) {
            if (!element.hasAttribute("baseUnits") || !element.getAttribute("baseUnits").equals(searchCapability.getUnit())) {
              if (!bool) {
                bool = true;
                logger.info("Updated baseUnits attribute.\t" + str4 + ":[" + String.valueOf(str) + "]\t" + element.getAttribute("baseUnits") + " to " + searchCapability.getUnit());
              } 
              element.setAttribute("baseUnits", searchCapability.getUnit());
            } 
          } else if (element.hasAttribute("baseUnits")) {
            if (!bool) {
              bool = true;
              logger.info("Remove baseUnits attribute. \t" + str4 + ":[" + String.valueOf(str) + "]");
            } 
            element.removeAttribute("baseUnits");
          } 
        } 
      } catch (XPathExpressionException xPathExpressionException) {
        logger.error(xPathExpressionException.getMessage(), xPathExpressionException);
        errorNum++;
      } 
    } 
    return errorNum;
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


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\datamodel\modifymappings\ModifyMappingApp.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */