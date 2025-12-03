package com.mentor.dms.contentprovider.sf.datamodel;

import com.mentor.datafusion.utils.logger.MGLogger;
import com.mentor.dms.contentprovider.sf.AggregationServiceWebCall;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
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
import org.xml.sax.helpers.AttributesImpl;

public class AnalyzeDuplicateProductLines {
  private static MGLogger logger = MGLogger.getLogger(AnalyzeDuplicateProductLines.class);
  
  private static String seuser = null;
  
  private static String sepass = null;
  
  private static String inputfile = null;
  
  public static void main(String[] paramArrayOfString) {
    Options options = new Options();
    options.addOption("help", false, "help");
    options.addOption("seuser", true, "SiliconExpert web service username");
    options.addOption("sepass", true, "SiliconExpert web service password");
    options.addOption("inputfile", true, "Duplicate Product Line ID XML filename");
    BasicParser basicParser = new BasicParser();
    CommandLine commandLine = null;
    try {
      commandLine = basicParser.parse(options, paramArrayOfString);
    } catch (ParseException parseException) {
      System.err.println("Error: " + parseException.getMessage());
      HelpFormatter helpFormatter = new HelpFormatter();
      helpFormatter.printHelp("AnalyzeDuplicateProductLines", options);
      System.exit(1);
    } 
    if (commandLine.hasOption("help")) {
      HelpFormatter helpFormatter = new HelpFormatter();
      helpFormatter.printHelp("AnalyzeDuplicateProductLines", options);
      System.exit(0);
    } 
    if (commandLine.hasOption("seuser")) {
      seuser = commandLine.getOptionValue("seuser");
    } else {
      System.err.println("Error: SiliconExpert username is required.");
      System.exit(1);
    } 
    if (commandLine.hasOption("sepass")) {
      sepass = commandLine.getOptionValue("sepass");
    } else {
      System.err.println("Error: SiliconExpert password is required.");
      System.exit(1);
    } 
    if (commandLine.hasOption("inputfile")) {
      inputfile = commandLine.getOptionValue("inputfile");
    } else {
      System.err.println("Error: SiliconExpert inputfile is required.");
      System.exit(1);
    } 
    AnalyzeDuplicateProductLines analyzeDuplicateProductLines = new AnalyzeDuplicateProductLines();
    analyzeDuplicateProductLines.doAnalyze();
  }
  
  private void doAnalyze() {
    try {
      logger.info("Reading SiliconExpert data model duplicate IDs...");
      DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      Document document = null;
      XPath xPath = XPathFactory.newInstance().newXPath();
      try {
        document = documentBuilder.parse(new FileInputStream(new File(inputfile)));
      } catch (Exception exception) {
        logger.error("Unable to open input file: " + inputfile);
        return;
      } 
      StreamResult streamResult = new StreamResult(new FileOutputStream(new File("D:/Consulting/IP/DemoContentProviderImpl/SiliconExpert/Mapping/DuplicatePLAndPaths.xml")));
      SAXTransformerFactory sAXTransformerFactory = (SAXTransformerFactory)TransformerFactory.newInstance();
      TransformerHandler transformerHandler = sAXTransformerFactory.newTransformerHandler();
      Transformer transformer = transformerHandler.getTransformer();
      transformer.setOutputProperty("encoding", "UTF-8");
      transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
      transformer.setOutputProperty("indent", "yes");
      transformerHandler.setResult(streamResult);
      transformerHandler.startDocument();
      AttributesImpl attributesImpl = new AttributesImpl();
      transformerHandler.startElement("", "", "data", attributesImpl);
      String str = "/data/PLs/PL";
      NodeList nodeList = (NodeList)xPath.evaluate(str, document, XPathConstants.NODESET);
      for (byte b = 0; b < nodeList.getLength(); b++) {
        Element element = (Element)nodeList.item(b);
        String str1 = xPath.evaluate("text()", element);
        logger.info("Processing PL ID '" + str1 + "'...");
        String str2 = searchPart(str1);
        String str3 = getTaxonomyPath(str2);
        attributesImpl.clear();
        attributesImpl.addAttribute("", "", "id", "CDATA", str1);
        attributesImpl.addAttribute("", "", "path", "CDATA", str3.replace(" > ", "."));
        transformerHandler.startElement("", "", "PL", attributesImpl);
        transformerHandler.endElement("", "", "PL");
      } 
      transformerHandler.endElement("", "", "data");
      transformerHandler.endDocument();
    } catch (Exception exception) {
      exception.printStackTrace();
      return;
    } 
  }
  
  private String searchPart(String paramString) throws Exception {
    HashMap<Object, Object> hashMap = new HashMap<>();
    hashMap.put("plId", paramString);
    hashMap.put("pageNumber", "1");
    XPath xPath = XPathFactory.newInstance().newXPath();
    AggregationServiceWebCall aggregationServiceWebCall = new AggregationServiceWebCall();
    InputStream inputStream = aggregationServiceWebCall.callSE(aggregationServiceWebCall.getWebServiceBaseURL() + "search/parametric/getSearchResult", hashMap, seuser, sepass);
    Document document = aggregationServiceWebCall.parseXML(inputStream);
    inputStream.close();
    String str = "/ServiceResult/Result/PartsList/Part[1]/Features/Feature[1]/FeatureValue/text()";
    return (String)xPath.evaluate(str, document, XPathConstants.STRING);
  }
  
  private String getTaxonomyPath(String paramString) throws Exception {
    HashMap<Object, Object> hashMap = new HashMap<>();
    hashMap.put("comIds", paramString);
    XPath xPath = XPathFactory.newInstance().newXPath();
    AggregationServiceWebCall aggregationServiceWebCall = new AggregationServiceWebCall();
    InputStream inputStream = aggregationServiceWebCall.callSE(aggregationServiceWebCall.getWebServiceBaseURL() + "search/partDetail", hashMap, seuser, sepass);
    Document document = aggregationServiceWebCall.parseXML(inputStream);
    inputStream.close();
    String str = "/ServiceResult/Results/ResultDto[1]/SummaryData/TaxonomyPathIDs/text()";
    return (String)xPath.evaluate(str, document, XPathConstants.STRING);
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\datamodel\AnalyzeDuplicateProductLines.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */