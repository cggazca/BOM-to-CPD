package com.mentor.dms.contentprovider.sf.utils;

import com.mentor.datafusion.oi.OICursor;
import com.mentor.datafusion.oi.OIException;
import com.mentor.datafusion.oi.OIObjectManager;
import com.mentor.datafusion.oi.OIObjectManagerFactory;
import com.mentor.datafusion.oi.OIQuery;
import com.mentor.datafusion.oi.login.OIAuthenticate;
import com.mentor.datafusion.oi.login.OIAuthenticateFactory;
import com.mentor.datafusion.oi.model.OIStringField;
import com.mentor.datafusion.oi.type.OIObject;
import com.mentor.datafusion.utils.logger.MGLogger;
import com.mentor.dms.contentprovider.core.ContentProviderException;
import com.mentor.dms.contentprovider.core.ContentProviderFactory;
import com.mentor.dms.contentprovider.sf.AggregationServiceWebCall;
import com.mentor.dms.contentprovider.sf.ContentProviderImpl;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Properties;
import java.util.TreeSet;
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
import org.w3c.dom.NodeList;

public class SFSyncManufacturersApp {
  private static MGLogger logger = MGLogger.getLogger(SFSyncManufacturersApp.class);
  
  private static String dmsConfigName = null;
  
  private static OIObjectManager om = null;
  
  private static ContentProviderImpl ccp = null;
  
  private static boolean bUseSEID = false;
  
  private static TreeSet<String> mfgSet = new TreeSet<>();
  
  public static void main(String[] paramArrayOfString) {
    Options options = new Options();
    options.addOption("help", false, "help");
    options.addOption("loginconfig", true, "EDM Library auto-login configuration");
    options.addOption("useSEID", false, "Use the SE Mfg ID for the EDM Mfg Name, otherwise use the name");
    BasicParser basicParser = new BasicParser();
    CommandLine commandLine = null;
    try {
      commandLine = basicParser.parse(options, paramArrayOfString);
    } catch (ParseException parseException) {
      System.err.println("Error: " + parseException.getMessage());
      HelpFormatter helpFormatter = new HelpFormatter();
      helpFormatter.printHelp("SyncSEManufacturers", options);
      System.exit(1);
    } 
    if (commandLine.hasOption("help")) {
      HelpFormatter helpFormatter = new HelpFormatter();
      helpFormatter.printHelp("SyncSEManufacturers", options);
      System.exit(0);
    } 
    if (commandLine.hasOption("loginconfig")) {
      dmsConfigName = commandLine.getOptionValue("loginconfig");
    } else {
      System.err.println("Error: EDM Library auto-login configuration is required.");
      System.exit(1);
    } 
    bUseSEID = commandLine.hasOption("useSEID");
    System.out.println("#");
    System.out.println("#          Sync SiliconExpert Manufacturers - Version 1.1.1");
    System.out.println("#");
    System.out.println("#                   Copyright Siemens 2025");
    System.out.println("#");
    System.out.println("#                      All Rights Reserved.");
    System.out.println("#");
    System.out.println("#        THIS WORK CONTAINS TRADE SECRET AND PROPRIETARY");
    System.out.println("#        INFORMATION WHICH IS THE PROPERTY OF SIEMENS");
    System.out.println("#        OR ITS LICENSORS AND IS SUBJECT TO LICENSE TERMS.");
    System.out.println("#");
    SFSyncManufacturersApp sFSyncManufacturersApp = new SFSyncManufacturersApp();
    sFSyncManufacturersApp.doSync();
  }
  
  private void doSync() {
    OIObjectManagerFactory oIObjectManagerFactory = null;
    HashSet<String> hashSet = new HashSet();
    hashSet.add("Semiconductor");
    hashSet.add("Passive");
    hashSet.add("Electrical and Electronic Components");
    hashSet.add("Optoelectronics, Lighting and Displays");
    hashSet.add("Interconnect");
    hashSet.add("Test and Measurement");
    try {
      logger.info("Connecting to EDM Library...");
      OIAuthenticate oIAuthenticate = OIAuthenticateFactory.createBatchAuthenticate(dmsConfigName);
      oIObjectManagerFactory = oIAuthenticate.login("SiliconExpert Manufacturer Synchronization Application");
      om = oIObjectManagerFactory.createObjectManager();
      logger.info("Connected to EDM Library!");
      logger.info("Initalizing SiliconExpert ContentProvider...");
      ContentProviderFactory.getInstance().registerContentProviders(om);
      ccp = (ContentProviderImpl)ContentProviderFactory.getInstance().createContentProvider("SE");
      logger.info("Reading EDM Library Manufacturers...");
      HashMap<Object, Object> hashMap1 = new HashMap<>();
      OIQuery oIQuery = om.createQuery("Manufacturer", true);
      oIQuery.addColumn("ManufacturerId");
      oIQuery.addColumn("ECMfgID");
      OICursor oICursor = oIQuery.execute();
      while (oICursor.next()) {
        String str1 = oICursor.getString("ECMfgID");
        if (!str1.trim().isEmpty())
          hashMap1.put(str1, oICursor.getProxyObject().getObject()); 
      } 
      oICursor.close();
      AggregationServiceWebCall aggregationServiceWebCall = new AggregationServiceWebCall(ccp);
      HashMap<Object, Object> hashMap2 = new HashMap<>();
      InputStream inputStream = aggregationServiceWebCall.callSE(aggregationServiceWebCall.getWebServiceBaseURL() + "search/parametric/getAllTaxonomy", hashMap2);
      Document document = aggregationServiceWebCall.parseXML(inputStream);
      inputStream.close();
      aggregationServiceWebCall.checkErrors(document, true);
      XPath xPath = XPathFactory.newInstance().newXPath();
      NodeList nodeList = (NodeList)xPath.evaluate("/ServiceResult/Result/TaxonomyList/Taxonomy", document, XPathConstants.NODESET);
      for (byte b1 = 0; b1 < nodeList.getLength(); b1++) {
        Element element = (Element)nodeList.item(b1);
        String str1 = xPath.evaluate("PlType/text()", element);
        if (hashSet.contains(str1)) {
          NodeList nodeList1 = (NodeList)xPath.evaluate("MainCategoryList/MainCategory/SubCategoryList/SubCategory/ProductLines/*", element, XPathConstants.NODESET);
          for (byte b = 0; b < nodeList1.getLength(); b++) {
            Element element1 = (Element)nodeList1.item(b);
            processProductLine(aggregationServiceWebCall, element1);
          } 
        } 
      } 
      String str = null;
      Properties properties = new Properties();
      File file = new File(System.getProperty("java.io.tmpdir"), "mfgLoad.txt");
      if (file.exists()) {
        FileInputStream fileInputStream = new FileInputStream(file);
        try {
          properties.load(new FileInputStream(file));
        } finally {
          if (fileInputStream != null)
            fileInputStream.close(); 
        } 
        str = properties.getProperty("LastMfgName", null);
      } 
      byte b2 = 1;
      for (String str1 : mfgSet) {
        if (str != null) {
          if (str.equals(str1))
            str = null; 
          logger.info("Already processed Manufacturer '" + str1 + "' in previous load.  Skipping...");
          continue;
        } 
        logger.info("" + b2++ + " : Processing Manufacturer '" + b2++ + "'...");
        Element element = getSupplierInfo(aggregationServiceWebCall, str1);
        if (element == null) {
          logger.error("Error: Unable to get supplier info for Manufacturer '" + str1 + "'.");
          continue;
        } 
        String str2 = xPath.evaluate("ManufacturerID/text()", element);
        OIObject oIObject = (OIObject)hashMap1.get(str2);
        try {
          if (oIObject == null) {
            logger.info("Creating Manufacturer '" + str1 + "' (" + str2 + ")...");
            oIObject = om.createObject("Manufacturer");
            oIObject.set("ECMfgID", str2);
          } 
          boolean bool = false;
          if (bUseSEID) {
            bool |= setProperty(oIObject, "ManufacturerId", element, "ManufacturerID");
          } else {
            bool |= setProperty(oIObject, "ManufacturerId", element, "ManufacturerName");
          } 
          bool |= setProperty(oIObject, "ManufacturerName", element, "ManufacturerName");
          bool |= setProperty(oIObject, "InternetAddress", element, "ManufacturerURL");
          bool |= setProperty(oIObject, "TelephoneNo", element, "PhoneNumber");
          bool |= setProperty(oIObject, "ECMfgAddress", element, "Address");
          bool |= setProperty(oIObject, "EmailAddress", element, "Email");
          bool |= setProperty(oIObject, "ECMfgDUNSNumber", element, "DUNSNumber");
          if (bool) {
            if (oIObject.getMode() != 1)
              logger.info("Updating Manufacturer '" + str1 + "' (" + str2 + ")..."); 
            try {
              om.makePermanent(oIObject);
            } catch (OIException oIException) {
              logger.error("Unable to create/save Manufacturer '" + str1 + "' (" + str2 + "): " + oIException.getMessage());
            } 
          } else {
            logger.error("No updates required.");
          } 
          properties.setProperty("LastMfgName", str1);
          FileOutputStream fileOutputStream = new FileOutputStream(file);
          try {
            properties.store(fileOutputStream, "Load Manufacturer Status");
          } finally {
            if (fileOutputStream != null)
              fileOutputStream.close(); 
          } 
        } finally {
          if (oIObject != null)
            om.evict(oIObject); 
        } 
      } 
      file.delete();
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
  
  private void processProductLine(AggregationServiceWebCall paramAggregationServiceWebCall, Element paramElement) throws Exception {
    XPath xPath = XPathFactory.newInstance().newXPath();
    HashMap<Object, Object> hashMap = new HashMap<>();
    String str = xPath.evaluate("plName/text()", paramElement);
    hashMap.put("plName", str);
    logger.info("Reading Manufacturers for Product Line '" + str + "'...");
    InputStream inputStream = paramAggregationServiceWebCall.callSE(paramAggregationServiceWebCall.getWebServiceBaseURL() + "search/parametric/getPlFeatures", hashMap);
    Document document = paramAggregationServiceWebCall.parseXML(inputStream);
    inputStream.close();
    try {
      paramAggregationServiceWebCall.checkErrors(document, true);
    } catch (ContentProviderException contentProviderException) {
      logger.warn(contentProviderException.getMessage());
      return;
    } 
    NodeList nodeList = (NodeList)xPath.evaluate("/ServiceResult/Result/PlFeatures/Features/Feature[FeatureName = \"Manufacturer\"]/FeatureValues/FeatureValue", document, XPathConstants.NODESET);
    for (byte b = 0; b < nodeList.getLength(); b++) {
      Element element = (Element)nodeList.item(b);
      String str1 = element.getTextContent();
      mfgSet.add(str1);
    } 
  }
  
  private Element getSupplierInfo(AggregationServiceWebCall paramAggregationServiceWebCall, String paramString) {
    Element element = null;
    HashMap<Object, Object> hashMap = new HashMap<>();
    hashMap.put("manufacturerName", paramString);
    XPath xPath = XPathFactory.newInstance().newXPath();
    Document document = null;
    try {
      logger.info("Reading supplier profile ...");
      InputStream inputStream = paramAggregationServiceWebCall.callSE(paramAggregationServiceWebCall.getWebServiceBaseURL() + "search/supplierProfile", hashMap);
      document = paramAggregationServiceWebCall.parseXML(inputStream);
      paramAggregationServiceWebCall.checkErrors(document, true);
      String str1 = xPath.evaluate("/ServiceResult/Status/Code/text()", document);
      String str2 = xPath.evaluate("/ServiceResult/Status/Message/text()", document);
      if (str1.equals("3") || str2.equals("No Results Found"))
        return element; 
      element = (Element)xPath.evaluate("/ServiceResult/Result/SuppllierProfileData", document, XPathConstants.NODE);
    } catch (Exception exception) {
      logger.warn(exception.getMessage());
    } 
    return element;
  }
  
  private boolean setProperty(OIObject paramOIObject, String paramString1, Element paramElement, String paramString2) throws Exception {
    XPath xPath = XPathFactory.newInstance().newXPath();
    OIStringField oIStringField = (OIStringField)paramOIObject.getOIClass().getField(OIStringField.class, paramString1);
    int i = oIStringField.getMaximalLength();
    String str1 = xPath.evaluate(paramString2 + "/text()", paramElement);
    str1 = str1.replace("\t", " ");
    str1 = str1.replace("\n", " ");
    String str2 = paramOIObject.getString(paramString1);
    if (str1.length() > i) {
      str1 = str1.substring(0, i);
      logger.warn(oIStringField.getLabel() + " has maximum length of " + oIStringField.getLabel() + ".  Value will be truncated to '" + i + "'.");
    } 
    if (!str1.equals(str2)) {
      paramOIObject.set(paramString1, str1);
      return true;
    } 
    return false;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\s\\utils\SFSyncManufacturersApp.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */