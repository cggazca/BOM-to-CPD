package com.mentor.dms.contentprovider.sf.utils;

import com.mentor.datafusion.oi.OICursor;
import com.mentor.datafusion.oi.OIObjectManager;
import com.mentor.datafusion.oi.OIObjectManagerFactory;
import com.mentor.datafusion.oi.OIQuery;
import com.mentor.datafusion.oi.login.OIAuthenticate;
import com.mentor.datafusion.oi.login.OIAuthenticateFactory;
import com.mentor.datafusion.oi.model.OIClass;
import com.mentor.datafusion.oi.type.OIObject;
import com.mentor.datafusion.utils.logger.MGLogger;
import com.mentor.dms.contentprovider.core.AbstractContentProvider;
import com.mentor.dms.contentprovider.core.ContentProviderFactory;
import com.mentor.dms.contentprovider.core.ContentProviderGlobal;
import com.mentor.dms.contentprovider.core.config.ContentProviderConfig;
import com.mentor.dms.contentprovider.core.config.ContentProviderConfigComponentCatalog;
import java.util.Collection;
import java.util.HashMap;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class MoveComponentsApp {
  static MGLogger logger = MGLogger.getLogger(MoveComponentsApp.class);
  
  static OIObjectManagerFactory omf = null;
  
  static OIObjectManager om = null;
  
  static HashMap<String, OIClass> oiClassMap = new HashMap<>();
  
  public static void main(String[] paramArrayOfString) {
    System.out.println("#");
    System.out.println("#       SiliconExpert Move Components App - Version 1.1.1");
    System.out.println("#");
    System.out.println("#                Copyright Siemens 2025");
    System.out.println("#");
    System.out.println("#                      All Rights Reserved.");
    System.out.println("#");
    System.out.println("#        THIS WORK CONTAINS TRADE SECRET AND PROPRIETARY");
    System.out.println("#        INFORMATION WHICH IS THE PROPERTY OF SIEMENS");
    System.out.println("#        OR ITS LICENSORS AND IS SUBJECT TO LICENSE TERMS.");
    System.out.println("#");
    String str = null;
    Options options = new Options();
    options.addOption("help", false, "help");
    options.addOption("loginconfig", true, "EDM Library auto-login configuration");
    BasicParser basicParser = new BasicParser();
    CommandLine commandLine = null;
    try {
      commandLine = basicParser.parse(options, paramArrayOfString);
    } catch (ParseException parseException) {
      System.err.println("Error: " + parseException.getMessage());
      HelpFormatter helpFormatter = new HelpFormatter();
      helpFormatter.printHelp("MoveComponents", options);
      System.exit(1);
    } 
    if (commandLine.hasOption("help")) {
      HelpFormatter helpFormatter = new HelpFormatter();
      helpFormatter.printHelp("MoveComponents", options);
      System.exit(0);
    } 
    if (commandLine.hasOption("loginconfig")) {
      str = commandLine.getOptionValue("loginconfig");
    } else {
      System.err.println("Error: EDM Library auto-login configuration is required.");
      System.exit(1);
    } 
    try {
      logger.info("Connecting to EDM Library...");
      OIAuthenticate oIAuthenticate = OIAuthenticateFactory.createBatchAuthenticate(str);
      omf = oIAuthenticate.login("Move Manufacturer Parts Utilty");
      logger.info("Connected");
      om = omf.createObjectManager();
      moveComponents();
      logger.info("");
      logger.info("Complete.");
    } catch (Exception exception) {
      exception.printStackTrace();
    } finally {
      if (omf != null)
        omf.close(); 
      if (om != null)
        om.close(); 
    } 
  }
  
  private static void moveComponents() throws Exception {
    ContentProviderGlobal.setBatchExecMode();
    ContentProviderFactory.getInstance().registerContentProviders(om);
    AbstractContentProvider abstractContentProvider = ContentProviderFactory.getInstance().createContentProvider("SE");
    ContentProviderConfig contentProviderConfig = abstractContentProvider.getConfig();
    OIQuery oIQuery = om.createQuery("Component", true);
    oIQuery.addRestriction("ApprovedManufacturerList.ECRepresentativeMPN", "Y");
    oIQuery.addColumn("PartNumber");
    oIQuery.addColumn("MfgPartNumber");
    OICursor oICursor = oIQuery.execute();
    while (oICursor.next()) {
      logger.info("Processing Component '" + oICursor.getString("PartNumber") + "':");
      OIObject oIObject1 = oICursor.getObject("MfgPartNumber");
      String str1 = oIObject1.getOIClass().getName();
      Collection<ContentProviderConfigComponentCatalog> collection = contentProviderConfig.getComponentCatalogsByMPNDMN(str1);
      if (collection.size() == 0) {
        logger.warn("Manufactuer Part catalog '" + oIObject1.getOIClass().getLabel() + "' (" + str1 + ") is not mapped to a Component catalog.");
        continue;
      } 
      if (collection.size() > 1) {
        logger.warn("Manufactuer Part catalog '" + oIObject1.getOIClass().getLabel() + "' (" + str1 + ") is not mapped to more than one Component catalog.");
        continue;
      } 
      ContentProviderConfigComponentCatalog contentProviderConfigComponentCatalog = collection.iterator().next();
      OIObject oIObject2 = oICursor.getProxyObject().getObject();
      String str2 = oIObject2.getOIClass().getName();
      if (contentProviderConfigComponentCatalog.getClassDMN().equals(str2))
        continue; 
      OIClass oIClass1 = getOIClass(str2);
      OIClass oIClass2 = getOIClass(contentProviderConfigComponentCatalog.getClassDMN());
      logger.info("Moving Component from '" + oIClass1.getLabel() + " (" + str2 + ")' to '" + oIClass2.getLabel() + " (" + contentProviderConfigComponentCatalog.getClassDMN() + ")'...");
      try {
        om.moveInClassHierarchy(oIObject2, oIClass2);
        om.makePermanent(oIObject2);
      } catch (Exception exception) {
        logger.error("Unable to move Component: " + exception.getMessage());
      } finally {
        om.evict(oIObject2);
      } 
    } 
    oICursor.close();
  }
  
  private static OIClass getOIClass(String paramString) {
    OIClass oIClass = oiClassMap.get(paramString);
    if (oIClass == null) {
      oIClass = omf.getClassManager().getOIClass(paramString);
      oiClassMap.put(paramString, oIClass);
    } 
    return oIClass;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\s\\utils\MoveComponentsApp.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */