package com.mentor.dms.contentprovider.sf.datamodel.upgrade;

import com.mentor.datafusion.oi.OICursor;
import com.mentor.datafusion.oi.OIException;
import com.mentor.datafusion.oi.OIObjectManager;
import com.mentor.datafusion.oi.OIObjectManagerFactory;
import com.mentor.datafusion.oi.OIQuery;
import com.mentor.datafusion.oi.login.OIAuthenticate;
import com.mentor.datafusion.oi.login.OIAuthenticateFactory;
import com.mentor.datafusion.oi.type.OIObject;
import com.mentor.datafusion.oi.type.OIObjectSet;
import com.mentor.datafusion.utils.logger.MGLogger;
import com.mentor.dms.contentprovider.core.config.ContentProviderConfig;
import com.mentor.dms.contentprovider.core.utils.LogConfigLoader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Iterator;
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

public class CharacteristicsToTopLevel {
  static MGLogger logger = MGLogger.getLogger(CharacteristicsToTopLevel.class);
  
  private static OIObjectManager om = null;
  
  private static OIObjectManagerFactory omf = null;
  
  private static Document cfgDoc;
  
  private static XPath xpath = XPathFactory.newInstance().newXPath();
  
  public static void main(String[] paramArrayOfString) {
    new CharacteristicsToTopLevel(paramArrayOfString);
  }
  
  public CharacteristicsToTopLevel(String[] paramArrayOfString) {
    try {
      LogConfigLoader.configLog4j();
    } catch (Exception exception) {
      exception.printStackTrace();
      System.out.println("Problems with the logging settings");
    } 
    logger.info("#");
    logger.info("#                DataModelUpgrade CharacteristicsMoveToTopLevel - Version 1.1.1");
    logger.info("#                  Data Model Upgrade Application");
    logger.info("#");
    logger.info("#                    Copyright Siemens 2025");
    logger.info("#");
    String str1 = "";
    String str2 = null;
    String str3 = null;
    Options options = new Options();
    options.addOption("function", true, "Execution function class name");
    options.addOption("loginconfig", true, "EDM Library auto-login configuration");
    options.addOption("inmappingfile", true, "Input Content Provider Configuration Mapping filename");
    options.addOption("outmappingfile", true, "Output Content Provider Configuration Mapping filename");
    BasicParser basicParser = new BasicParser();
    CommandLine commandLine = null;
    try {
      commandLine = basicParser.parse(options, paramArrayOfString);
    } catch (ParseException parseException) {
      System.err.println("Error: " + parseException.getMessage());
      HelpFormatter helpFormatter = new HelpFormatter();
      helpFormatter.printHelp("CharacteristicsUpgrade", options);
      System.exit(1);
    } 
    if (commandLine.hasOption("loginconfig")) {
      str1 = commandLine.getOptionValue("loginconfig");
    } else {
      System.err.println("Error: EDM Library auto-login configuration is required.");
      System.exit(1);
    } 
    str2 = commandLine.getOptionValue("inmappingfile");
    str3 = commandLine.getOptionValue("outmappingfile");
    if (str2 == null)
      logger.warn("Warn: Input Content Provider Configuration Mapping filename not set. Skip update Mapping file."); 
    if (str2 != null)
      if (str3 == null) {
        logger.error("Error: Output Content Provider Configuration Mapping filename is required.");
        System.exit(1);
      } else if (!createDirs(str3)) {
        logger.error("Error: Failed to create output file path.");
        System.exit(1);
      }  
    try {
      logger.info("Connecting to EDM Library Server...");
      OIAuthenticate oIAuthenticate = OIAuthenticateFactory.createBatchAuthenticate(str1);
      omf = oIAuthenticate.login("Data Model Upgrade Utility");
      om = omf.createObjectManager();
    } catch (Exception exception) {
      exception.printStackTrace();
      logger.error("ERROR: Unable to connect to EDM Library Server:  " + exception.getMessage());
      return;
    } 
    try {
      removeCatalogCharacteristics();
      updateToClassCharacteristics();
      updateMappings(str2, str3);
    } catch (Exception exception) {
      exception.printStackTrace();
      logger.error("ERROR: " + exception.getMessage(), exception);
    } finally {
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
    logger.info("CharacteristicsUpgrade End");
  }
  
  private void updateToClassCharacteristics() throws OIException {
    String[] arrayOfString = { "060SFYTEOL", "060SFCountryOfOrigin", "060SFMilitarySpec", "060SFReachComplianceCode", "060SFPartLifeCycleCode" };
    logger.info("");
    logger.info("Update characteristics Class-No. start.");
    OIObject oIObject1 = null;
    OIObject oIObject2 = om.getObjectByID("60", "ObjectClass", true);
    for (String str : arrayOfString) {
      try {
        oIObject1 = om.getObjectByID(str, "Characteristic", true);
        if (oIObject1 != null) {
          if (oIObject1.getObject("ObjectClass").getInteger("ClassNumber").intValue() != 60) {
            oIObject1.set("ObjectClass", oIObject2);
            om.makePermanent(oIObject1);
            logger.info("Update sucucess. [" + str + "]");
          } 
        } else {
          logger.warn("WARN: " + str + " not exisits");
        } 
      } catch (OIException oIException) {
        om.evict(oIObject1);
        throw oIException;
      } 
    } 
    logger.info("Update characteristics Class-No. complete.");
  }
  
  private void removeCatalogCharacteristics() throws OIException {
    String[] arrayOfString = { "060SFYTEOL", "060SFCountryOfOrigin", "060SFMilitarySpec", "060SFReachComplianceCode", "060SFPartLifeCycleCode" };
    OIObject oIObject = null;
    logger.info("");
    logger.info("Remove Catalog Characteristics start.");
    OIQuery oIQuery = om.createQuery("CatalogGroup", true);
    oIQuery.addRestriction("ObjectClass", "60");
    oIQuery.addRestriction("Text.Language", om.getObjectManagerFactory().getLanguage());
    oIQuery.addColumn("Text.CatalogTitle");
    oIQuery.addColumn("CatalogGroup");
    oIQuery.addColumn("ParentKey");
    oIQuery.addColumn("DomainModelName");
    oIQuery.addColumn("CatalogStatus");
    OICursor oICursor = oIQuery.execute();
    for (byte b = 0; oICursor.next(); b++) {
      logger.info("Checking catalog [" + oICursor.getString("CatalogTitle") + "]");
      String str = oICursor.getString("CatalogGroup");
      oIObject = om.getObjectByID(str, "CatalogGroup", true);
      om.refreshAndLockObject(oIObject);
      for (String str1 : arrayOfString) {
        OIObjectSet oIObjectSet = oIObject.getSet("CatalogCharacteristics");
        Iterator<OIObject> iterator = oIObjectSet.iterator();
        while (iterator.hasNext()) {
          OIObject oIObject1 = iterator.next();
          if (oIObject1.getStringified("Characteristic").equals(str1)) {
            logger.info("  Remove " + str1);
            iterator.remove();
            om.makePermanent(oIObject);
            om.refreshAndLockObject(oIObject);
            break;
          } 
        } 
      } 
      om.evict(oIObject);
    } 
    oICursor.close();
    logger.info("Remove Catalog Characteristics complete.");
  }
  
  private void updateMappings(String paramString1, String paramString2) throws Exception {
    logger.info("");
    logger.info("Mapping modification start.");
    String str = null;
    try {
      SchemaFactory schemaFactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
      StreamSource streamSource = new StreamSource(ContentProviderConfig.class.getResourceAsStream("schemas/ContentProviderCfg.xsd"));
      Schema schema = schemaFactory.newSchema(streamSource);
      Validator validator = schema.newValidator();
      logger.info("Reading mapping configuration...");
      DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
      documentBuilderFactory.setNamespaceAware(true);
      DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
      if (isZipped(paramString1)) {
        try {
          ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(paramString1));
          try {
            ZipEntry zipEntry = zipInputStream.getNextEntry();
            str = zipEntry.getName();
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
          logger.error("ERROR: Failed to read mapping configuration. [" + paramString1 + "]");
          throw exception;
        } 
      } else {
        try {
          FileInputStream fileInputStream = new FileInputStream(paramString1);
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
          logger.error("ERROR: Failed to read mapping configuration. [" + paramString1 + "]");
          throw exception;
        } 
        str = (new File(paramString1)).getName();
      } 
      removeEmptyTextNodes(cfgDoc);
      processUpgrade();
      logger.info("Writing upgraded mapping configuration...");
      if (paramString2.endsWith(".zip")) {
        FileOutputStream fileOutputStream = new FileOutputStream(new File(paramString2));
        ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);
        StreamResult streamResult = new StreamResult(new OutputStreamWriter(zipOutputStream, "UTF-8"));
        zipOutputStream.putNextEntry(new ZipEntry(str));
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
        FileOutputStream fileOutputStream = new FileOutputStream(new File(paramString2));
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
      logger.info("Mapping modification complete!");
    } catch (Exception exception) {
      logger.info("Mapping modification failed.");
      throw exception;
    } 
  }
  
  private static void processUpgrade() {
    logger.info("Updating searchable settings.");
    try {
      String str1 = "/ContentProviderCfg/PartClasses/PartClass/Properties";
      String str2 = str1 + "/Property[@id='3b3c5480']";
      String str3 = str1 + "/Property[@id='6e83fbac']";
      NodeList nodeList = (NodeList)xpath.evaluate(str2, cfgDoc, XPathConstants.NODESET);
      logger.info("Number of Property [Country Of Origin]:" + nodeList.getLength());
      byte b;
      for (b = 0; b < nodeList.getLength(); b++) {
        Element element = (Element)nodeList.item(b);
        element.setAttribute("searchable", "true");
      } 
      nodeList = (NodeList)xpath.evaluate(str3, cfgDoc, XPathConstants.NODESET);
      logger.info("Number of Property [YTEOL]:" + nodeList.getLength());
      for (b = 0; b < nodeList.getLength(); b++) {
        Element element = (Element)nodeList.item(b);
        element.setAttribute("searchable", "true");
      } 
      String str4 = "/ContentProviderCfg/ManufacturerPartCatalogs/ManufacturerPartCatalog[@classDMN='RootManufacturerPart']";
      String str5 = str4 + "/ContentProviderMaps/ContentProviderMap/ContentProviderPropertyMaps";
      String str6 = str5 + "/ContentProviderPropertyMap[@dmn='3b3c5480']";
      String str7 = str5 + "/ContentProviderPropertyMap[@dmn='6e83fbac']";
      nodeList = (NodeList)xpath.evaluate(str6, cfgDoc, XPathConstants.NODESET);
      if (nodeList.getLength() == 0) {
        NodeList nodeList1 = (NodeList)xpath.evaluate(str5, cfgDoc, XPathConstants.NODESET);
        if (nodeList1.getLength() == 0) {
          logger.warn("RootManufacturerPart is not mapped to the part class.");
          return;
        } 
        Element element = cfgDoc.createElement("ContentProviderPropertyMap");
        element.setAttribute("ccpId", "3b3c5480");
        element.setAttribute("inherit", "false");
        element.setAttribute("label", "Country Of Origin");
        element.setAttribute("dmn", "3b3c5480");
        nodeList1.item(0).appendChild(element);
        logger.info("Create property mapping [Country Of Origin] for RootManufacturerPart.");
      } 
      nodeList = (NodeList)xpath.evaluate(str7, cfgDoc, XPathConstants.NODESET);
      if (nodeList.getLength() == 0) {
        NodeList nodeList1 = (NodeList)xpath.evaluate(str5, cfgDoc, XPathConstants.NODESET);
        if (nodeList1.getLength() == 0) {
          logger.warn("RootManufacturerPart is not mapped to the part class.");
          return;
        } 
        Element element = cfgDoc.createElement("ContentProviderPropertyMap");
        element.setAttribute("ccpId", "6e83fbac");
        element.setAttribute("inherit", "false");
        element.setAttribute("label", "YTEOL");
        element.setAttribute("dmn", "6e83fbac");
        nodeList1.item(0).appendChild(element);
        logger.info("Create property mapping [YTEOL] for RootManufacturerPart.");
      } 
    } catch (Exception exception) {
      logger.error("ERROR: " + exception.getMessage(), exception);
    } 
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
  
  private boolean createDirs(String paramString) {
    File file = (new File(paramString)).getParentFile();
    return !file.exists() ? file.mkdirs() : true;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\datamode\\upgrade\CharacteristicsToTopLevel.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */