package com.mentor.dms.contentprovider.sf.datamodel;

import com.mentor.datafusion.utils.logger.MGLogger;
import com.mentor.dms.contentprovider.sf.AggregationServiceWebCall;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class GenerateSampleParts extends AbstractSEDataModelApp {
  private static Logger logger = (Logger)MGLogger.getLogger(GenerateSampleParts.class);
  
  private static final Set<String> includeTaxonomySet = new HashSet<>();
  
  private static String outputDir = null;
  
  public static void main(String[] paramArrayOfString) {
    Options options = new Options();
    options.addOption("help", false, "help");
    options.addOption("seuser", true, "SiliconExpert web service username");
    options.addOption("sepass", true, "SiliconExpert web service password");
    options.addOption("outputdir", true, "Directory to generate output files.");
    BasicParser basicParser = new BasicParser();
    CommandLine commandLine = null;
    try {
      commandLine = basicParser.parse(options, paramArrayOfString);
    } catch (ParseException parseException) {
      System.err.println("Error: " + parseException.getMessage());
      HelpFormatter helpFormatter = new HelpFormatter();
      helpFormatter.printHelp("GenerateSampleParts", options);
      System.exit(1);
    } 
    if (commandLine.hasOption("help")) {
      HelpFormatter helpFormatter = new HelpFormatter();
      helpFormatter.printHelp("GenerateSampleParts", options);
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
    if (commandLine.hasOption("outputdir")) {
      outputDir = commandLine.getOptionValue("outputdir");
    } else {
      System.err.println("Error: Output directory is required.");
      System.exit(1);
    } 
    GenerateSampleParts generateSampleParts = new GenerateSampleParts();
    generateSampleParts.doGenerate();
  }
  
  private void doGenerate() {
    try {
      AggregationServiceWebCall aggregationServiceWebCall = new AggregationServiceWebCall();
      logger.info("Reading SiliconExpert data model...");
      HashMap<Object, Object> hashMap = new HashMap<>();
      byte b1 = 0;
      InputStream inputStream = null;
      DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      Document document = null;
      XPath xPath = XPathFactory.newInstance().newXPath();
      String str2 = outputDir + outputDir + "taxonomy.xml";
      inputStream = aggregationServiceWebCall.callSE(aggregationServiceWebCall.getWebServiceBaseURL() + "search/parametric/getAllTaxonomy", hashMap, seuser, sepass);
      saveXMLMessage(inputStream, str2);
      inputStream.close();
      try {
        document = documentBuilder.parse(new FileInputStream(new File(str2)));
      } catch (Exception exception) {
        logger.error("Unable to parse taxonomy file: " + str2);
        return;
      } 
      checkErrors(document);
      String str1 = "/ServiceResult/Result/TaxonomyList/Taxonomy";
      NodeList nodeList = (NodeList)xPath.evaluate(str1, document, XPathConstants.NODESET);
      for (byte b2 = 0; b2 < nodeList.getLength(); b2++) {
        Element element = (Element)nodeList.item(b2);
        String str = xPath.evaluate("PlType/text()", element);
        if (includeTaxonomySet.contains(str)) {
          str1 = "MainCategoryList/MainCategory";
          NodeList nodeList1 = (NodeList)xPath.evaluate(str1, element, XPathConstants.NODESET);
          for (byte b = 0; b < nodeList1.getLength(); b++) {
            Element element1 = (Element)nodeList1.item(b);
            String str3 = xPath.evaluate("CategoryName/text()", element1);
            str1 = "SubCategoryList/SubCategory";
            NodeList nodeList2 = (NodeList)xPath.evaluate(str1, element1, XPathConstants.NODESET);
            for (byte b3 = 0; b3 < nodeList2.getLength(); b3++) {
              Element element2 = (Element)nodeList2.item(b3);
              String str4 = xPath.evaluate("CategoryName/text()", element2);
              str1 = "ProductLines/*";
              NodeList nodeList3 = (NodeList)xPath.evaluate(str1, element2, XPathConstants.NODESET);
              for (byte b4 = 0; b4 < nodeList3.getLength(); b4++) {
                Element element3 = (Element)nodeList3.item(b4);
                String str5 = xPath.evaluate("plName/text()", element3);
                str2 = str + "_" + str + "_" + str3 + "_" + str4;
                str2 = outputDir + outputDir + File.separator + ".xml";
                System.out.println("Querying PL = " + str5 + "...");
                hashMap.clear();
                hashMap.put("plName", str5);
                inputStream = aggregationServiceWebCall.callSE(aggregationServiceWebCall.getWebServiceBaseURL() + "search/parametric/getSearchResult", hashMap, seuser, sepass);
                Document document1 = null;
                try {
                  document1 = documentBuilder.parse(inputStream);
                } catch (Exception exception) {
                  logger.error("Unable to parse search results file: " + str2);
                  return;
                } finally {
                  inputStream.close();
                } 
                checkErrors(document);
                str1 = "/ServiceResult/Result/PartsList/Part[1]/Features/Feature[FeatureName=\"DataProviderID\"]/FeatureValue/text()";
                String str6 = (String)xPath.evaluate(str1, document1, XPathConstants.STRING);
                System.out.println("" + ++b1 + " : Getting part details for ID = " + ++b1);
                hashMap.clear();
                hashMap.put("comIds", str6);
                inputStream = aggregationServiceWebCall.callSE(aggregationServiceWebCall.getWebServiceBaseURL() + "search/partDetail", hashMap, seuser, sepass);
                System.out.println("Saving to file = " + str2);
                saveXMLMessage(inputStream, str2);
              } 
            } 
          } 
        } 
      } 
    } catch (Exception exception) {
      exception.printStackTrace();
    } 
  }
  
  private static void saveXMLMessage(InputStream paramInputStream, String paramString) throws IOException {
    FileOutputStream fileOutputStream = new FileOutputStream(new File(paramString));
    if (paramInputStream != null) {
      byte[] arrayOfByte = new byte[1024];
      int i;
      for (i = paramInputStream.read(arrayOfByte); i != -1; i = paramInputStream.read(arrayOfByte))
        fileOutputStream.write(arrayOfByte, 0, i); 
    } 
    fileOutputStream.flush();
    fileOutputStream.close();
  }
  
  static {
    includeTaxonomySet.add("Electrical and Electronic Components");
    includeTaxonomySet.add("Interconnect");
    includeTaxonomySet.add("Optoelectronics, Lighting and Displays");
    includeTaxonomySet.add("Passive");
    includeTaxonomySet.add("Semiconductor");
    includeTaxonomySet.add("Test and Measurement");
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\datamodel\GenerateSampleParts.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */