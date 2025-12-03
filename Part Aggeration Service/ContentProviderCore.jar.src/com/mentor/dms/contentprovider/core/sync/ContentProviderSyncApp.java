package com.mentor.dms.contentprovider.core.sync;

import com.mentor.datafusion.dfo.login.OperationCanceledException;
import com.mentor.datafusion.oi.OIException;
import com.mentor.datafusion.oi.OIHelper;
import com.mentor.datafusion.oi.OIObjectManager;
import com.mentor.datafusion.oi.OIObjectManagerFactory;
import com.mentor.datafusion.oi.login.OIAuthenticate;
import com.mentor.datafusion.oi.login.OIAuthenticateFactory;
import com.mentor.datafusion.oi.login.OILoginData;
import com.mentor.datafusion.utils.logger.MGLogger;
import com.mentor.dms.contentprovider.core.ContentProviderException;
import com.mentor.dms.contentprovider.core.ContentProviderFactory;
import com.mentor.dms.contentprovider.core.ContentProviderGlobal;
import com.mentor.dms.contentprovider.core.config.ContentProviderConfigException;
import com.mentor.dms.contentprovider.core.sync.gui.ContentProviderReconcileApp;
import com.mentor.dms.contentprovider.core.utils.LogConfigLoader;
import java.util.Collections;
import java.util.Date;
import javax.swing.UIManager;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.NDC;

public class ContentProviderSyncApp extends ContentProviderSync {
  static MGLogger logger = MGLogger.getLogger(ContentProviderSyncApp.class);
  
  public static void main(String[] paramArrayOfString) {
    boolean bool = false;
    try {
      LogConfigLoader.configLog4j();
    } catch (Exception exception) {
      exception.printStackTrace();
      System.out.println("Problems with the logging settings");
    } 
    logger.info("#");
    logger.info("#                ContentProviderSync - Version 1.1.1");
    logger.info("#     EDM Library/Content Provider Synchronization Application");
    logger.info("#");
    logger.info("#                Copyright Siemens 2025");
    logger.info("#");
    logger.info("#                      All Rights Reserved.");
    logger.info("#");
    logger.info("#        THIS WORK CONTAINS TRADE SECRET AND PROPRIETARY");
    logger.info("#        INFORMATION WHICH IS THE PROPERTY OF SIEMENS");
    logger.info("#        OR ITS LICENSORS AND IS SUBJECT TO LICENSE TERMS.");
    logger.info("#");
    String str1 = null;
    String str2 = null;
    String str3 = null;
    String str4 = null;
    String str5 = "";
    String str6 = "";
    Options options1 = new Options();
    options1.addOption("help", false, "help");
    options1.addOption("loginconfig", true, "EDM Library auto-login configuration");
    options1.addOption("user", true, "EDM Library username");
    options1.addOption("password", true, "EDM Library password");
    options1.addOption("server", true, "EDM Library server");
    options1.addOption("get", false, "Get updates from Content Provider for subscribed parts since last synchronization");
    options1.addOption("getfull", false, "Get full update from Content Provider for all subscribed parts");
    options1.addOption("restart", false, "Restart the 'get' or 'getfull' operation from where it was interrupted");
    options1.addOption("cachedir", true, "Directory used to store part data cache (Defaults to <userhome>/.dmsccp/synccache");
    options1.addOption("reconcile", false, "Perform incremental reconcile of EDM Library Manufacturer Parts with External Content");
    options1.addOption("reconcilefull", false, "Perform full reconcile of EDM Library Manufacturer Parts with External Content");
    options1.addOption("reconcileauto", false, "Set auto mapping of SYNC properties during reconcile");
    options1.addOption("reviewsync", false, "Launch Reconcile Application to review unreconciled MPNs");
    options1.addOption("updcomps", false, "Perform incremental update of EDM Library Components with EDM Library MPN properties");
    options1.addOption("updcompsaffected", false, "Perform update of EDM Library Components with EDM Library MPN properties for MPNs reconciled in this run");
    options1.addOption("updcompsfull", false, "Perform full update of EDM Library Components with EDM Library MPN properties");
    options1.addOption("interceptors", true, "Comma-separated list of InterceptoryFactory classes to register (must be in the CLASSPATH)");
    options1.addOption("params", true, "Comma-separated list of \"param=value\" custom parameters");
    options1.addOption("ccpid", true, "Content Provider ID");
    Options options2 = new Options();
    options2.addOption("help", false, "help");
    options2.addOption("loginconfig", true, "EDM Library auto-login configuration");
    options2.addOption("user", true, "EDM Library username");
    options2.addOption("password", true, "EDM Library password");
    options2.addOption("server", true, "EDM Library server");
    options2.addOption("get", false, "Get updates from Content Provider for subscribed parts since last synchronization");
    options2.addOption("getfull", false, "Get full update from Content Provider for all subscribed parts");
    options2.addOption("restart", false, "Restart the 'get' or 'getfull' operation from where it was interrupted");
    options2.addOption("cachedir", true, "Directory used to store part data cache (Defaults to <userhome>/.dmsccp/synccache");
    options2.addOption("reconcile", false, "Perform incremental reconcile of EDM Library Manufacturer Parts with External Content");
    options2.addOption("reconcilefull", false, "Perform full reconcile of EDM Library Manufacturer Parts with External Content");
    options2.addOption("reconcileauto", false, "Set auto mapping of SYNC properties during reconcile");
    options2.addOption("reviewsync", false, "Launch Reconcile Application to review unreconciled MPNs");
    options2.addOption("updcomps", false, "Perform incremental update of EDM Library Components with EDM Library MPN properties");
    options2.addOption("updcompsaffected", false, "Perform update of EDM Library Components with EDM Library MPN properties for MPNs reconciled in this run");
    options2.addOption("updcompsfull", false, "Perform full update of EDM Library Components with EDM Library MPN properties");
    options2.addOption("ccpid", true, "Content Provider ID");
    BasicParser basicParser = new BasicParser();
    CommandLine commandLine = null;
    try {
      commandLine = basicParser.parse(options1, paramArrayOfString);
    } catch (ParseException parseException) {
      logger.error("Error: " + parseException.getMessage());
      HelpFormatter helpFormatter = new HelpFormatter();
      helpFormatter.printHelp("ContentProviderSync", options1);
      System.exit(1);
    } 
    if (commandLine.hasOption("help")) {
      HelpFormatter helpFormatter = new HelpFormatter();
      helpFormatter.printHelp("ContentProviderSync", options2);
      System.exit(0);
    } 
    str1 = commandLine.getOptionValue("loginconfig");
    if (str1 == null) {
      str4 = commandLine.getOptionValue("server");
      str2 = commandLine.getOptionValue("user");
      str3 = commandLine.getOptionValue("password");
      if ((str4 != null && (str2 == null || str3 == null)) || (str2 != null && (str4 == null || str3 == null)) || (str3 != null && (str2 == null || str4 == null))) {
        logger.error("Error: For username/password login to EDM Library, 'user', 'password', and 'server' must all be specified.");
        System.exit(1);
      } 
    } 
    boolean bool1 = commandLine.hasOption("subscribe");
    boolean bool2 = commandLine.hasOption("get");
    boolean bool3 = commandLine.hasOption("getfull");
    boolean bool4 = commandLine.hasOption("restart");
    boolean bool5 = commandLine.hasOption("reviewsync");
    boolean bool6 = commandLine.hasOption("reconcile");
    boolean bool7 = commandLine.hasOption("reconcilefull");
    boolean bool8 = commandLine.hasOption("reconcileauto");
    boolean bool9 = commandLine.hasOption("updcomps");
    boolean bool10 = commandLine.hasOption("updcompsaffected");
    boolean bool11 = commandLine.hasOption("updcompsfull");
    String str7 = commandLine.getOptionValue("cachedir");
    if (str7 == null)
      str7 = CacheInfo.getDefaultCacheDirectory(); 
    if (bool2 && bool3) {
      logger.error("Error: 'get' and 'getfull' options cannot both be specified.");
      System.exit(1);
    } 
    if ((((!bool2 && !bool3) ? 1 : 0) & bool4) != 0) {
      logger.error("Error: 'restart' applies only when 'get' and 'getfull' options are specified.");
      System.exit(1);
    } 
    if (bool6 && bool7) {
      logger.error("Error: 'reconcile' and 'reconcilefull' options cannot both be specified.");
      System.exit(1);
    } 
    if (!bool6 && !bool7 && bool8) {
      logger.error("Error: 'reconcileauto' must be used in combination with either 'reconcile' or 'reconcilefull' options.");
      System.exit(1);
    } 
    if ((bool9 && bool10) || (bool9 && bool11) || (bool10 && bool11)) {
      logger.error("Error: Only one of 'updcomps', 'updcompsaffected', 'updcompsfull' options can be specified.");
      System.exit(1);
    } 
    if (!bool1 && !bool2 && !bool3 && !bool5 && !bool6 && !bool7 && !bool9 && !bool10 && !bool11) {
      logger.error("Error: No synchronize options specified.");
      System.exit(1);
    } 
    if ((bool10 & (!bool6 ? 1 : 0)) != 0 && !bool7) {
      logger.error("Error: Affected Component update ('updcompsaffected') requires that one of 'reconcile' or 'reconcilefull' options be specified.");
      System.exit(1);
    } 
    if (commandLine.hasOption("interceptors"))
      str5 = commandLine.getOptionValue("interceptors"); 
    if (commandLine.hasOption("params"))
      str6 = commandLine.getOptionValue("params"); 
    logger.info("");
    logger.info("# EDM Library/Content Provider Synchronization started at " + String.valueOf(new Date()));
    logger.info("# Options:");
    if (str1 != null)
      logger.info("#     Login configuration: " + str1); 
    if (str4 != null)
      logger.info("#     Server: " + str4); 
    if (str2 != null)
      logger.info("#     User: " + str2); 
    logger.info("#     Synchronize Content Provider subscriptions: " + (bool1 ? "Yes" : "No"));
    logger.info("#     Content Provider to External Content synchronization (incremental): " + (bool2 ? "Yes" : "No"));
    logger.info("#     Content Provider to External Content synchronization: (full) " + (bool3 ? "Yes" : "No"));
    logger.info("#     Restart Content Provider to External Content synchronization: " + (bool4 ? "Yes" : "No"));
    logger.info("#     Incremental reconcile External Content to MPN: " + (bool6 ? "Yes" : "No"));
    logger.info("#     Full reconcile External Content to MPN: " + (bool7 ? "Yes" : "No"));
    logger.info("#     Reconcile auto mode for SYNC properties: " + (bool8 ? "Yes" : "No"));
    logger.info("#     Update Components (incremental): " + (bool9 ? "Yes" : "No"));
    logger.info("#     Update Components (affected): " + (bool10 ? "Yes" : "No"));
    logger.info("#     Update Components (full): " + (bool11 ? "Yes" : "No"));
    logger.info("#     Interceptors loaded: " + str5);
    logger.info("#     Custom parameters: " + str6);
    logger.info("#     Part cache directory: " + str7);
    logger.info("");
    OIObjectManager oIObjectManager = null;
    OIObjectManagerFactory oIObjectManagerFactory = null;
    try {
      OIAuthenticate oIAuthenticate;
      logger.info("Connecting to EDM Library...");
      if (str1 != null) {
        oIAuthenticate = OIAuthenticateFactory.createBatchAuthenticate(str1);
      } else {
        OILoginData oILoginData = OIAuthenticateFactory.createLoginData("ContentProviderSync");
        oILoginData.setLicenceRoleNames("dmscompeng");
        oILoginData.setShowLoginDialog(true);
        oILoginData.setShowRoleDialog(false);
        oILoginData.setShowProdLibDialog(false);
        if (str4 != null)
          oILoginData.setServer(str4); 
        if (str2 != null)
          oILoginData.setUsername(str2); 
        if (str3 != null)
          oILoginData.setPassword(str3); 
        if (str4 != null && str2 != null && str3 != null) {
          oIAuthenticate = OIAuthenticateFactory.createBatchAuthenticate(oILoginData);
        } else {
          try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
          } catch (Exception exception) {}
          oIAuthenticate = OIAuthenticateFactory.createInteractiveAuthenticate(oILoginData, null);
        } 
      } 
      try {
        oIObjectManagerFactory = oIAuthenticate.login("EDM Library/Content Provider Synchronization Application");
      } catch (OperationCanceledException operationCanceledException) {
        return;
      } 
      oIObjectManager = oIObjectManagerFactory.createObjectManager();
      NDC.push(oIObjectManagerFactory.getUserName());
      ContentProviderGlobal.setBatchExecMode();
      ContentProviderGlobal.setOIObjectManager(oIObjectManager);
      for (String str : str6.split("\\s*,\\s*")) {
        String[] arrayOfString = str.split("\\s*=\\s*");
        if (arrayOfString.length == 2)
          customSyncAppParams.put(arrayOfString[0], arrayOfString[1]); 
      } 
      if (!str5.isEmpty()) {
        logger.info("Registering user-supplied interceptors...");
        OIHelper.registerOI(oIObjectManagerFactory);
        byte b = 0;
        for (String str : str5.split("\\s*,\\s*")) {
          logger.info("  Registering interceptor \"" + str + "\"...");
          Class<?> clazz = Class.forName(str);
          OIHelper.addInterceptorFactoryToRegistry(oIObjectManagerFactory, clazz.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]), 500 + b++, Collections.emptySet(), Collections.emptySet(), false);
        } 
      } 
      logger.info("Registering configured Content Providers...");
      ContentProviderFactory.getInstance().registerContentProviders(oIObjectManager);
      if (bool1)
        syncSubscriptions(oIObjectManager); 
      if (bool2 || bool3)
        syncAllExternalContentProviders(oIObjectManager, bool3, bool4, str7); 
      if (bool6 || bool7)
        compareAndReconcile(oIObjectManager, bool8, bool7); 
      if (bool5) {
        logger.info("Launching the interactive Reconcile Application...");
        ContentProviderGlobal.setInteractiveExecMode();
        ContentProviderGlobal.setReviewSyncMode();
        ContentProviderReconcileApp.createAndShowGUI(oIObjectManager, null);
        ContentProviderGlobal.setBatchExecMode();
      } 
      if (bool9)
        syncMPN2CompsIncremental(oIObjectManager); 
      if (bool10)
        syncMPN2CompsReconciled(oIObjectManager); 
      if (bool11)
        syncMPN2CompsAll(oIObjectManager); 
    } catch (OIException oIException) {
      logger.error("Unable to connect to EDM Library server:  " + oIException.getMessage(), (Throwable)oIException);
      bool = true;
    } catch (ContentProviderSyncException contentProviderSyncException) {
      logger.error(contentProviderSyncException.getMessage());
      bool = true;
    } catch (ContentProviderConfigException contentProviderConfigException) {
      logger.error("Problem encountered while reading Content Provider configuration :  " + contentProviderConfigException.getMessage(), (Throwable)contentProviderConfigException);
      bool = true;
    } catch (ContentProviderException contentProviderException) {
      logger.error("Problem encountered while registering Content Providers:  " + contentProviderException.getMessage(), (Throwable)contentProviderException);
      bool = true;
    } catch (ClassNotFoundException classNotFoundException) {
      logger.error("Problem encountered while registering user-supplied interceptor factory:  " + classNotFoundException.getMessage(), classNotFoundException);
      bool = true;
    } catch (InstantiationException instantiationException) {
      logger.error("Problem encountered while registering user-supplied interceptor factory:  " + instantiationException.getMessage(), instantiationException);
      bool = true;
    } catch (IllegalAccessException illegalAccessException) {
      logger.error("Problem encountered while registering user-supplied interceptor factory:  " + illegalAccessException.getMessage(), illegalAccessException);
      bool = true;
    } catch (IllegalArgumentException|java.lang.reflect.InvocationTargetException|NoSuchMethodException illegalArgumentException) {
      logger.error("Problem encountered while registering user-supplied interceptor factory:  " + illegalArgumentException.getMessage(), illegalArgumentException);
      illegalArgumentException.printStackTrace();
    } finally {
      logger.debug("Closing connection to EDM Library...");
      if (oIObjectManager != null)
        oIObjectManager.close(); 
      if (oIObjectManagerFactory != null)
        oIObjectManagerFactory.close(); 
    } 
    if (!bool) {
      logger.info("EDM Library/External Content Synchronization completed at: " + String.valueOf(new Date()));
    } else {
      logger.error("EDM Library/External Content Synchronization failed.");
    } 
    System.exit(bool);
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\sync\ContentProviderSyncApp.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */