package com.mentor.dms.contentprovider.sf.datamodel.upgrade;

import com.mentor.datafusion.oi.OICursor;
import com.mentor.datafusion.oi.OIObjectManager;
import com.mentor.datafusion.oi.OIObjectManagerFactory;
import com.mentor.datafusion.oi.OIQuery;
import com.mentor.datafusion.oi.login.OIAuthenticate;
import com.mentor.datafusion.oi.login.OIAuthenticateFactory;
import com.mentor.datafusion.utils.logger.MGLogger;
import com.mentor.dms.contentprovider.sf.datamodel.definition.DmsElements;
import com.mentor.dms.contentprovider.sf.datamodel.definition.FieldType;
import com.mentor.dms.contentprovider.sf.datamodel.definition.ListType;
import com.mentor.dms.contentprovider.sf.datamodel.definition.ObjectType;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.xml.namespace.QName;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class ExportMPNStaticsXML {
  static MGLogger logger = MGLogger.getLogger(ExportMPNStaticsXML.class);
  
  static OIObjectManager om = null;
  
  static OIObjectManagerFactory omf = null;
  
  public static void main(String[] paramArrayOfString) {
    logger.info("#");
    logger.info("#                ExportMPNStaticsXML - Version 1.1.1");
    logger.info("#                  Export MPN Statics XML Application");
    logger.info("#");
    logger.info("#                    Copyright Siemens 2025");
    logger.info("#");
    String str1 = null;
    String str2 = null;
    Options options = new Options();
    options.addOption("help", false, "help");
    options.addOption("loginconfig", true, "EDM Library auto-login configuration");
    options.addOption("updcfgdir", true, "Data Model Upgrade Configuration directory");
    BasicParser basicParser = new BasicParser();
    CommandLine commandLine = null;
    try {
      commandLine = basicParser.parse(options, paramArrayOfString);
    } catch (ParseException parseException) {
      System.err.println("Error: " + parseException.getMessage());
      HelpFormatter helpFormatter = new HelpFormatter();
      helpFormatter.printHelp("ExportMPNStaticsXML", options);
      System.exit(1);
    } 
    if (commandLine.hasOption("help")) {
      HelpFormatter helpFormatter = new HelpFormatter();
      helpFormatter.printHelp("ExportMPNStaticsXML", options);
      System.exit(0);
    } 
    if (commandLine.hasOption("loginconfig")) {
      str1 = commandLine.getOptionValue("loginconfig");
    } else {
      System.err.println("Error: EDM Library auto-login configuration is required.");
      System.exit(1);
    } 
    if (commandLine.hasOption("updcfgdir")) {
      str2 = commandLine.getOptionValue("updcfgdir");
    } else {
      System.err.println("Error: Data Model Upgrade Configuration directory is required.");
      System.exit(1);
    } 
    File file = new File(str2);
    if (!file.canRead()) {
      System.err.println("Error: Specified Data Model Upgrade Configuration directory '" + str2 + "' does not exist or cannot be read.");
      System.exit(1);
    } 
    try {
      logger.info("Connecting to EDM Library Server...");
      OIAuthenticate oIAuthenticate = OIAuthenticateFactory.createBatchAuthenticate(str1);
      omf = oIAuthenticate.login("Export MPN Statics XML Utility");
      om = omf.createObjectManager();
      DmsElements dmsElements = new DmsElements();
      OIQuery oIQuery = om.createQuery("Characteristic", true);
      oIQuery.addRestriction("Status", "A");
      oIQuery.addRestriction("ObjectClass", "60");
      oIQuery.addRestriction("RefClass", "60");
      oIQuery.addRestriction("DomainModelName", "seSt*");
      oIQuery.addRestriction("Text.Language", "e");
      oIQuery.addColumn("Characteristic");
      oIQuery.addColumn("CharactType");
      oIQuery.addColumn("ValueType");
      oIQuery.addColumn("ValueLength");
      oIQuery.addColumn("InitFileName");
      oIQuery.addColumn("InformationWidth");
      oIQuery.addColumn("TableColumn");
      oIQuery.addColumn("DomainModelName");
      oIQuery.addColumn("TableName");
      oIQuery.addColumn("DisposeOrder");
      oIQuery.addColumn("SearchAlignment");
      oIQuery.addColumn("InformationAlignment");
      oIQuery.addColumn("Text.TabSheet");
      oIQuery.addColumn("Text.InformationText");
      oIQuery.addColumn("Text.SearchText");
      OICursor oICursor = oIQuery.execute();
      while (oICursor.next()) {
        ObjectType objectType = new ObjectType();
        objectType.setObjectid(oICursor.getString("Characteristic"));
        objectType.setClazz("Characteristic");
        dmsElements.getObjectOrDelete().add(objectType);
        FieldType fieldType = new FieldType();
        fieldType.setId("Characteristic");
        fieldType.setValue(oICursor.getString("Characteristic"));
        objectType.getListOrField().add(fieldType);
        fieldType = new FieldType();
        fieldType.setId("CharactType");
        fieldType.setValue(oICursor.getStringified("CharactType"));
        objectType.getListOrField().add(fieldType);
        fieldType = new FieldType();
        fieldType.setId("ValueType");
        fieldType.setValue(oICursor.getStringified("ValueType"));
        objectType.getListOrField().add(fieldType);
        fieldType = new FieldType();
        fieldType.setId("ValueLength");
        fieldType.setValue(oICursor.getStringified("ValueLength"));
        objectType.getListOrField().add(fieldType);
        fieldType = new FieldType();
        fieldType.setId("InitFileName");
        fieldType.setValue(oICursor.getString("InitFileName"));
        objectType.getListOrField().add(fieldType);
        fieldType = new FieldType();
        fieldType.setId("InformationWidth");
        fieldType.setValue(oICursor.getStringified("InformationWidth"));
        objectType.getListOrField().add(fieldType);
        fieldType = new FieldType();
        fieldType.setId("TableName");
        fieldType.setValue(oICursor.getString("TableName"));
        objectType.getListOrField().add(fieldType);
        fieldType = new FieldType();
        fieldType.setId("TableColumn");
        fieldType.setValue(oICursor.getString("TableColumn"));
        objectType.getListOrField().add(fieldType);
        fieldType = new FieldType();
        fieldType.setId("DomainModelName");
        fieldType.setValue(oICursor.getString("DomainModelName"));
        objectType.getListOrField().add(fieldType);
        fieldType = new FieldType();
        fieldType.setId("DisposeOrder");
        fieldType.setValue(oICursor.getStringified("DisposeOrder"));
        objectType.getListOrField().add(fieldType);
        fieldType = new FieldType();
        fieldType.setId("SearchAlignment");
        fieldType.setValue(oICursor.getStringified("SearchAlignment"));
        objectType.getListOrField().add(fieldType);
        fieldType = new FieldType();
        fieldType.setId("InformationAlignment");
        fieldType.setValue(oICursor.getStringified("InformationAlignment"));
        objectType.getListOrField().add(fieldType);
        ListType listType = new ListType();
        listType.setId("Text");
        listType.setClear(Boolean.valueOf(true));
        objectType.getListOrField().add(listType);
        fieldType = new FieldType();
        fieldType.setId("TabSheet");
        fieldType.setValue(oICursor.getString("TabSheet"));
        listType.getFieldOrList().add(fieldType);
        fieldType = new FieldType();
        fieldType.setId("SearchText");
        fieldType.setValue(oICursor.getString("SearchText"));
        listType.getFieldOrList().add(fieldType);
        fieldType = new FieldType();
        fieldType.setId("InformationText");
        fieldType.setValue(oICursor.getString("InformationText"));
        listType.getFieldOrList().add(fieldType);
      } 
      oICursor.close();
      File file1 = new File(file, "SiliconExpertStaticsMPNUpgrade.xml");
      FileOutputStream fileOutputStream = null;
      try {
        fileOutputStream = new FileOutputStream(file1);
        JAXBContext jAXBContext = JAXBContext.newInstance(new Class[] { DmsElements.class });
        Marshaller marshaller = jAXBContext.createMarshaller();
        marshaller.setProperty("jaxb.encoding", "UTF-8");
        JAXBElement jAXBElement = new JAXBElement(new QName("data"), DmsElements.class, dmsElements);
        marshaller.marshal(jAXBElement, fileOutputStream);
      } catch (JAXBException|java.io.FileNotFoundException jAXBException) {
        logger.error(jAXBException.getMessage());
      } finally {
        try {
          fileOutputStream.close();
        } catch (IOException iOException) {}
      } 
    } catch (Exception exception) {
      logger.error(exception.getMessage());
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
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\datamode\\upgrade\ExportMPNStaticsXML.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */