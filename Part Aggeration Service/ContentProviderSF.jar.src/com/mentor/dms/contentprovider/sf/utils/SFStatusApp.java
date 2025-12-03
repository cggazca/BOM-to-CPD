package com.mentor.dms.contentprovider.sf.utils;

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

public class SFStatusApp {
  protected static String seuser = null;
  
  protected static String sepass = null;
  
  protected static String proxyhost = null;
  
  protected static String proxyport = null;
  
  protected static boolean bUseSSLCert = false;
  
  protected static String SSLKeystorePassword = null;
  
  public static void main(String[] paramArrayOfString) {
    Options options = new Options();
    options.addOption("help", false, "help");
    options.addOption("seuser", true, "SiliconExpert web service username");
    options.addOption("sepass", true, "SiliconExpert web service password");
    options.addOption("proxyhost", true, "Host or IP of proxy server");
    options.addOption("proxyport", true, "Proxy port");
    options.addOption("useSSLCert", false, "Use SSL Certificate");
    options.addOption("SSLKeystorePassword", true, "SSL Keystore Password");
    BasicParser basicParser = new BasicParser();
    CommandLine commandLine = null;
    try {
      commandLine = basicParser.parse(options, paramArrayOfString);
    } catch (ParseException parseException) {
      System.err.println("Error: " + parseException.getMessage());
      HelpFormatter helpFormatter = new HelpFormatter();
      helpFormatter.printHelp("GenerateDataModel", options);
      System.exit(1);
    } 
    if (commandLine.hasOption("help")) {
      HelpFormatter helpFormatter = new HelpFormatter();
      helpFormatter.printHelp("Silicon Expert Status", options);
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
    proxyhost = commandLine.getOptionValue("proxyhost");
    proxyport = commandLine.getOptionValue("proxyport");
    bUseSSLCert = commandLine.hasOption("useSSLCert");
    SSLKeystorePassword = commandLine.getOptionValue("SSLKeystorePassword");
    if (proxyhost != null && proxyport == null) {
      System.err.println("Error: Proxy port is required when using proxy host.");
      System.exit(1);
    } 
    if (proxyhost == null && proxyport != null) {
      System.err.println("Error: Proxy host is required when using proxy port.");
      System.exit(1);
    } 
    if (bUseSSLCert && SSLKeystorePassword == null) {
      System.err.println("Error: SSL Keystore Password required when using SSL Cert.");
      System.exit(1);
    } 
    try {
      HashMap<Object, Object> hashMap = new HashMap<>();
      InputStream inputStream = null;
      AggregationServiceWebCall aggregationServiceWebCall = new AggregationServiceWebCall();
      inputStream = aggregationServiceWebCall.callSE(aggregationServiceWebCall.getWebServiceBaseURL() + "search/userStatus", hashMap, seuser, sepass, (proxyhost != null), proxyhost, proxyport, bUseSSLCert, SSLKeystorePassword);
      Document document = aggregationServiceWebCall.parseXML(inputStream);
      aggregationServiceWebCall.checkErrors(document, false);
      XPath xPath = XPathFactory.newInstance().newXPath();
      String str1 = (String)xPath.evaluate("/ServiceResult/UserStatus/CreationDate/text()", document, XPathConstants.STRING);
      String str2 = (String)xPath.evaluate("/ServiceResult/UserStatus/ExpirationDate/text()", document, XPathConstants.STRING);
      String str3 = (String)xPath.evaluate("/ServiceResult/UserStatus/PartDetailLimit/text()", document, XPathConstants.STRING);
      String str4 = (String)xPath.evaluate("/ServiceResult/UserStatus/PartDetailCount/text()", document, XPathConstants.STRING);
      String str5 = (String)xPath.evaluate("/ServiceResult/UserStatus/PartDetailRemaining/text()", document, XPathConstants.STRING);
      System.out.println(seuser + ":");
      System.out.println("  Creation Date: " + str1);
      System.out.println("  Expiration Date: " + str2);
      System.out.println("  Limit: " + str3);
      System.out.println("  Count: " + str4);
      System.out.println("  Remaining: " + str5);
    } catch (Exception exception) {
      System.err.println(exception.getMessage());
    } 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\s\\utils\SFStatusApp.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */