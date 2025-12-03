package com.mentor.dms.contentprovider.sf.datamodel.charactsupgrade;

import com.mentor.datafusion.oi.OIException;
import com.mentor.datafusion.oi.OIObjectManager;
import com.mentor.datafusion.oi.OIObjectManagerFactory;
import com.mentor.datafusion.oi.login.OIAuthenticate;
import com.mentor.datafusion.oi.login.OIAuthenticateFactory;
import com.mentor.datafusion.oi.type.OIObject;
import com.mentor.datafusion.oi.type.OIObjectSet;
import com.mentor.datafusion.utils.logger.MGLogger;
import com.mentor.dms.contentprovider.core.utils.LogConfigLoader;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class CharacteristicsUpgrade {
  static MGLogger logger = MGLogger.getLogger(CharacteristicsUpgrade.class);
  
  private static OIObjectManager om = null;
  
  private static OIObjectManagerFactory omf = null;
  
  public static void main(String[] paramArrayOfString) {
    new CharacteristicsUpgrade(paramArrayOfString);
  }
  
  public CharacteristicsUpgrade(String[] paramArrayOfString) {
    try {
      LogConfigLoader.configLog4j();
    } catch (Exception exception) {
      exception.printStackTrace();
      System.out.println("Problems with the logging settings");
    } 
    logger.info("#");
    logger.info("#                DataModelUpgrade CharacteristicsUpgrade - Version 1.1.1");
    logger.info("#                  Data Model Upgrade Application");
    logger.info("#");
    logger.info("#                    Copyright Siemens 2025");
    logger.info("#");
    String str = "";
    Options options = new Options();
    options.addOption("loginconfig", true, "EDM Library auto-login configuration");
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
      str = commandLine.getOptionValue("loginconfig");
    } else {
      System.err.println("Error: EDM Library auto-login configuration is required.");
      System.exit(1);
    } 
    try {
      logger.info("Connecting to EDM Library Server...");
      OIAuthenticate oIAuthenticate = OIAuthenticateFactory.createBatchAuthenticate(str);
      omf = oIAuthenticate.login("Data Model Upgrade Utility");
      om = omf.createObjectManager();
    } catch (Exception exception) {
      exception.printStackTrace();
      logger.error("ERROR: Unable to connect to EDM Library Server:  " + exception.getMessage());
      return;
    } 
    try {
      OIObject oIObject1 = null;
      boolean bool = true;
      try {
        oIObject1 = om.getObjectByID("060SFMilitarySpec1", "Characteristic", true);
      } catch (OIException oIException) {
        System.out.println("WARN: 060SFMilitarySpec1 not exisits");
        logger.warn("WARN: 060SFMilitarySpec1 not exisits");
        bool = false;
      } 
      try {
        if (bool) {
          om.deleteObject(oIObject1);
          om.makePermanent(oIObject1);
          logger.info("Delete 060SFMilitarySpec1 sucucess.");
        } 
      } catch (OIException oIException) {
        om.evict(oIObject1);
        oIException.printStackTrace();
        logger.error("ERROR: " + oIException.getMessage());
      } 
      OIObject oIObject2 = null;
      try {
        bool = true;
        try {
          oIObject2 = om.getObjectByID("001SFOnstateCurrentMax", "Characteristic", true);
        } catch (OIException oIException) {
          bool = false;
        } 
        if (!bool) {
          oIObject2 = om.getObjectByID("001SFOnStateCurrentMax", "Characteristic", false);
          oIObject2.set("Characteristic", "001SFOnstateCurrentMax");
          om.makePermanent(oIObject2);
          logger.info("Update 001SFOnStateCurrentMax to 001SFOnstateCurrentMax sucucess.");
        } else {
          logger.warn("WARN: 001SFOnstateCurrentMax already exisits");
          System.out.println("WARN: 001SFOnstateCurrentMax already exisits");
        } 
      } catch (OIException oIException) {
        om.evict(oIObject2);
        oIException.printStackTrace();
        logger.error("ERROR: " + oIException.getMessage());
      } 
      try {
        bool = true;
        try {
          oIObject2 = om.getObjectByID("060SFOnstateCurrentMax", "Characteristic", true);
        } catch (OIException oIException) {
          bool = false;
        } 
        if (!bool) {
          oIObject2 = om.getObjectByID("060SFOnStateCurrentMax", "Characteristic", false);
          oIObject2.set("Characteristic", "060SFOnstateCurrentMax");
          om.makePermanent(oIObject2);
          logger.info("Update 060SFOnStateCurrentMax to 060SFOnstateCurrentMax sucucess.");
        } else {
          logger.warn("WARN: 060SFOnstateCurrentMax already exisits");
          System.out.println("WARN: 060SFOnstateCurrentMax already exisits");
        } 
      } catch (OIException oIException) {
        om.evict(oIObject2);
        oIException.printStackTrace();
        logger.error("ERROR: " + oIException.getMessage());
      } 
      try {
        bool = true;
        try {
          oIObject2 = om.getObjectByID("060SFOnStateCurrentMax", "Characteristic", true);
        } catch (OIException oIException) {
          bool = false;
        } 
        if (!bool) {
          oIObject2 = om.getObjectByID("060SFOnStateCurrntMax", "Characteristic", false);
          oIObject2.set("Characteristic", "060SFOnStateCurrentMax");
          om.makePermanent(oIObject2);
          logger.info("Update 060SFOnStateCurrntMax to 060SFOnStateCurrentMax sucucess.");
        } else {
          logger.warn("WARN: 060SFOnStateCurrentMax already exisits");
          System.out.println("WARN: 060SFOnStateCurrentMax already exisits");
        } 
      } catch (OIException oIException) {
        om.evict(oIObject2);
        oIException.printStackTrace();
        logger.error("ERROR: " + oIException.getMessage());
      } 
      try {
        bool = true;
        try {
          oIObject2 = om.getObjectByID("001SFOnStateCurrentMax", "Characteristic", true);
        } catch (OIException oIException) {
          bool = false;
        } 
        if (!bool) {
          OIObject oIObject3 = om.getObjectByID("1", "ObjectClass", true);
          oIObject2 = om.createObject("Characteristic");
          oIObject2.set("Characteristic", "001SFOnStateCurrentMax");
          oIObject2.set("ObjectClass", oIObject3);
          oIObject2.set("RefClass", oIObject3);
          oIObject2.set("CharactType", Integer.valueOf(0));
          oIObject2.set("ValueType", Integer.valueOf(2));
          oIObject2.set("ValueLength", Integer.valueOf(64));
          oIObject2.set("InitFileName", "SiliconExpertStaticsMPN.init");
          oIObject2.set("TableName", "te_dyn_SF_C2");
          oIObject2.set("TableColumn", "SF_OnStateCurrentMax94e25fb4");
          oIObject2.set("InformationWidth", Integer.valueOf(20));
          oIObject2.set("DisposeOrder", Integer.valueOf(6192));
          oIObject2.set("SearchAlignment", Integer.valueOf(0));
          oIObject2.set("InformationAlignment", Integer.valueOf(0));
          oIObject2.set("DomainModelName", "001_94e25fb4");
          OIObjectSet oIObjectSet = oIObject2.getSet("Text");
          OIObject oIObject4 = oIObjectSet.createLine();
          oIObject4.set("Language", "e");
          oIObject4.set("TabSheet", "Supplyframe");
          oIObject4.set("InformationText", "On-State Current-Max");
          oIObject4.set("SearchText", "On-State Current-Max");
          om.makePermanent(oIObject2);
          logger.info("Create 001SFOnStateCurrentMax sucucess.");
        } else {
          logger.warn("WARN: 001SFOnStateCurrentMax already exisits");
          System.out.println("WARN: 001SFOnStateCurrentMax already exisits");
        } 
      } catch (OIException oIException) {
        oIException.printStackTrace();
        om.evict(oIObject2);
        logger.error("ERROR: " + oIException.getMessage());
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
    } catch (Exception exception) {
      exception.printStackTrace();
      logger.error("ERROR: Unable to connect to EDM Library Server:  " + exception.getMessage());
      return;
    } 
    logger.info("CharacteristicsUpgrade End");
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\datamodel\charactsupgrade\CharacteristicsUpgrade.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */