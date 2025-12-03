package com.mentor.dms.contentprovider.sf.utils;

import com.mentor.datafusion.utils.logger.MGLogger;
import com.mentor.dms.contentprovider.sf.AggregationServiceWebCall;
import java.io.InputStream;
import java.util.HashMap;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ManufacturerPartMatchApp {
  static MGLogger logger = MGLogger.getLogger(ManufacturerPartMatchApp.class);
  
  static String seuser = null;
  
  static String sepass = null;
  
  static String inputfile = null;
  
  static String outputfile = null;
  
  static String pnColumn = null;
  
  static String mfgColumn = null;
  
  static int partCount = 1;
  
  public static void main(String[] paramArrayOfString) {
    System.out.println("#");
    System.out.println("#       Manufacturer Part Match Application - Version 1.1.1");
    System.out.println("#");
    System.out.println("#                Copyright Siemens 2025");
    System.out.println("#");
    System.out.println("#                      All Rights Reserved.");
    System.out.println("#");
    System.out.println("#        THIS WORK CONTAINS TRADE SECRET AND PROPRIETARY");
    System.out.println("#        INFORMATION WHICH IS THE PROPERTY OF SIEMENS ");
    System.out.println("#        OR ITS LICENSORS AND IS SUBJECT TO LICENSE TERMS.");
    System.out.println("#");
    Options options = new Options();
    options.addOption("help", false, "help");
    options.addOption("seuser", true, "SiliconExpert API username");
    options.addOption("sepass", true, "SiliconExpert API password");
    options.addOption("inputfile", true, "Input CSV file");
    options.addOption("outputfile", true, "Output CSV file");
    options.addOption("pnColumn", true, "Part Number Column name");
    options.addOption("mfgColumn", true, "Manufacturer Column name");
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
    if (commandLine.hasOption("seuser")) {
      seuser = commandLine.getOptionValue("seuser");
    } else {
      System.err.println("Error: SiliconExpert API username is required.");
      System.exit(1);
    } 
    if (commandLine.hasOption("sepass")) {
      sepass = commandLine.getOptionValue("sepass");
    } else {
      System.err.println("Error: SiliconExpert API password is required.");
      System.exit(1);
    } 
    if (commandLine.hasOption("inputfile")) {
      inputfile = commandLine.getOptionValue("inputfile");
    } else {
      System.err.println("Error: Input CSV file is required.");
      System.exit(1);
    } 
    if (commandLine.hasOption("outputfile")) {
      outputfile = commandLine.getOptionValue("outputfile");
    } else {
      System.err.println("Error: Output CSV file is required.");
      System.exit(1);
    } 
    if (commandLine.hasOption("pnColumn")) {
      pnColumn = commandLine.getOptionValue("pnColumn");
    } else {
      System.err.println("Error: Output CSV file is required.");
      System.exit(1);
    } 
    if (commandLine.hasOption("mfgColumn")) {
      mfgColumn = commandLine.getOptionValue("mfgColumn");
    } else {
      System.err.println("Error: Output CSV file is required.");
      System.exit(1);
    } 
    logger.info("");
    logger.info("Manufacturer Part Match completed.");
  }
  
  private static SEMatch searchExactMatch(String paramString1, String paramString2) throws Exception {
    SEMatch sEMatch = new SEMatch();
    String str1 = "[{\"partNumber\":\"" + paramString1 + "\",\"manufacturer\":\"" + paramString2 + "\"}]";
    HashMap<Object, Object> hashMap = new HashMap<>();
    hashMap.put("partNumber", str1);
    InputStream inputStream = null;
    AggregationServiceWebCall aggregationServiceWebCall = new AggregationServiceWebCall();
    inputStream = aggregationServiceWebCall.callSE(aggregationServiceWebCall.getWebServiceBaseURL() + "search/listPartSearch", hashMap, seuser, sepass);
    Document document = aggregationServiceWebCall.parseXML(inputStream);
    aggregationServiceWebCall.checkErrors(document, false);
    XPath xPath = XPathFactory.newInstance().newXPath();
    String str2 = "/ServiceResult/Result/PartData/PartList/PartDto";
    NodeList nodeList = (NodeList)xPath.evaluate(str2, document, XPathConstants.NODESET);
    if (nodeList.getLength() == 1) {
      Node node = nodeList.item(0);
      sEMatch.matchRating = xPath.evaluate("MatchRating/text()", node);
      sEMatch.comId = xPath.evaluate("ComID/text()", node);
      sEMatch.partNumber = xPath.evaluate("PartNumber/text()", node);
      sEMatch.mfg = xPath.evaluate("Manufacturer/text()", node);
      sEMatch.description = xPath.evaluate("Description/text()", node);
      sEMatch.lifecycle = xPath.evaluate("Lifecycle/text()", node);
      sEMatch.rohs = xPath.evaluate("RoHS/text()", node);
      sEMatch.rohsVersion = xPath.evaluate("RoHSVersion/text()", node);
      sEMatch.taxPath = xPath.evaluate("TaxonomyPath/text()", node);
      sEMatch.taxPathID = xPath.evaluate("TaxonomyPathID/text()", node);
      sEMatch.aliasNote = xPath.evaluate("AliasData/Aliasdto/note/text()", node);
    } else if (nodeList.getLength() > 1) {
      sEMatch.matchRating = "Multiple matches";
    } 
    return sEMatch;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\s\\utils\ManufacturerPartMatchApp.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */