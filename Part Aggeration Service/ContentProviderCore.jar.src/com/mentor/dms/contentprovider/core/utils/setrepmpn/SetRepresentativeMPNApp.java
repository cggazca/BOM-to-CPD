package com.mentor.dms.contentprovider.core.utils.setrepmpn;

import com.mentor.datafusion.oi.OIObjectManager;
import com.mentor.datafusion.oi.OIObjectManagerFactory;
import com.mentor.datafusion.oi.login.OIAuthenticate;
import com.mentor.datafusion.oi.login.OIAuthenticateFactory;
import com.mentor.dms.contentprovider.core.utils.setrepmpn.config.SetRepMPNCfgSetRepMPNCfg;
import com.mentor.dms.contentprovider.core.utils.setrepmpn.config.SetRepMPNConfiguration;
import java.io.File;
import java.util.Date;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.Logger;

public class SetRepresentativeMPNApp {
  static Logger logger = Logger.getLogger(SetRepresentativeMPNApp.class);
  
  private static String loginConfigName = null;
  
  private static String configFilename = null;
  
  private static String scriptFilename = null;
  
  private static boolean bDebugMode;
  
  private static String mpnPathPrefix = "ApprovedManufacturerList.MfgPartNumber.";
  
  private static String amlPathPrefix = "ApprovedManufacturerList.";
  
  public static void main(String[] paramArrayOfString) {
    logger.info("#");
    logger.info("#              Set Representative MPN - Version 1.0.0");
    logger.info("#                    Copyright Siemens 2021");
    logger.info("#");
    logger.info("");
    Options options = new Options();
    options.addOption("help", false, "help");
    options.addOption("loginconfig", true, "EDM Batch Login configuration name");
    options.addOption("configfile", true, "Set Representatvie MPN config file");
    options.addOption("scriptfile", true, "Javascript file");
    options.addOption("debug", false, "Debug mode (no updates are performed)");
    BasicParser basicParser = new BasicParser();
    CommandLine commandLine = null;
    try {
      commandLine = basicParser.parse(options, paramArrayOfString);
    } catch (ParseException parseException) {
      logger.error("Error: " + parseException.getMessage());
      HelpFormatter helpFormatter = new HelpFormatter();
      helpFormatter.printHelp("SetRepresentativeMPN", options);
      System.exit(1);
    } 
    if (commandLine.hasOption("help")) {
      HelpFormatter helpFormatter = new HelpFormatter();
      helpFormatter.printHelp("SetRepresentativeMPN", options);
      System.exit(0);
    } 
    if (commandLine.hasOption("loginconfig")) {
      loginConfigName = commandLine.getOptionValue("loginconfig");
    } else {
      logger.error("Error: EDM Batch Login configuration name (-loginconfig) is required.");
      System.exit(1);
    } 
    configFilename = commandLine.getOptionValue("configfile");
    if (configFilename == null) {
      logger.error("Error: Set Representatvie MPN config file (-configfile) is required.");
      System.exit(1);
    } 
    scriptFilename = commandLine.getOptionValue("scriptfile");
    bDebugMode = commandLine.hasOption("debug");
    logger.info("#  Set Representative MPN started at " + String.valueOf(new Date()));
    logger.info("# Options:");
    logger.info("#     EDM Batch Login configuration name: " + loginConfigName);
    logger.info("#     Configuration file: " + configFilename);
    if (scriptFilename != null)
      logger.info("#     Javascript file: " + scriptFilename); 
    logger.info("#     Debug mode: " + (bDebugMode ? "Yes" : "No"));
    logger.info("");
    SetRepresentativeMPNEngine setRepresentativeMPNEngine = new SetRepresentativeMPNEngine();
    SetRepMPNCfgSetRepMPNCfg setRepMPNCfgSetRepMPNCfg = null;
    try {
      if (configFilename != null) {
        logger.info("Reading configuration file...");
        setRepMPNCfgSetRepMPNCfg = SetRepMPNConfiguration.read(configFilename);
      } 
      if (scriptFilename != null) {
        logger.info("Reading script file...");
        SetRepMPNScriptEngine.getScriptEngine().read(new File(scriptFilename));
      } 
    } catch (Exception exception) {
      logger.error(exception.getMessage());
      System.exit(1);
    } 
    logger.info("Connecting to EDM Library...");
    OIObjectManagerFactory oIObjectManagerFactory = null;
    OIObjectManager oIObjectManager = null;
    try {
      OIAuthenticate oIAuthenticate = OIAuthenticateFactory.createBatchAuthenticate(loginConfigName);
      oIObjectManagerFactory = oIAuthenticate.login("Set Represtative MPN");
      oIObjectManager = oIObjectManagerFactory.createObjectManager();
      if (configFilename == null)
        logger.info("Reading configuration from EDM Library Toolbox 'SetRepMPNToolbox'."); 
      setRepresentativeMPNEngine.setDebugMode(bDebugMode);
      setRepresentativeMPNEngine.run(oIObjectManager, setRepMPNCfgSetRepMPNCfg);
    } catch (Exception exception) {
      exception.printStackTrace();
    } finally {
      if (oIObjectManager != null)
        oIObjectManager.close(); 
      if (oIObjectManagerFactory != null)
        oIObjectManagerFactory.close(); 
    } 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\cor\\utils\setrepmpn\SetRepresentativeMPNApp.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */