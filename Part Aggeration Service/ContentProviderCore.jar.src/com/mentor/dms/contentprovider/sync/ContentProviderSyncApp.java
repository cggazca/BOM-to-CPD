package com.mentor.dms.contentprovider.sync;

import com.mentor.datafusion.dfo.login.OperationCanceledException;
import com.mentor.datafusion.oi.OIException;
import com.mentor.datafusion.oi.OIHelper;
import com.mentor.datafusion.oi.OIObjectManager;
import com.mentor.datafusion.oi.OIObjectManagerFactory;
import com.mentor.datafusion.oi.interceptor.InterceptorFactory;
import com.mentor.datafusion.oi.interceptor.InterceptorFactoryPriorityDecorator;
import com.mentor.datafusion.oi.login.OIAuthenticate;
import com.mentor.datafusion.oi.login.OIAuthenticateFactory;
import com.mentor.datafusion.oi.login.OILoginData;
import com.mentor.datafusion.utils.logger.MGLogger;
import com.mentor.dms.contentprovider.ContentProviderException;
import com.mentor.dms.contentprovider.ContentProviderFactory;
import com.mentor.dms.contentprovider.ContentProviderGlobal;
import com.mentor.dms.contentprovider.config.ContentProviderConfigException;
import com.mentor.dms.contentprovider.sync.gui.ContentProviderReconcileApp;
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
    logger.info("#");
    logger.info("#                ContentProviderSync - Version 1.16.3");
    logger.info("#     EDM Library/Content Provider Synchronization Application");
    logger.info("#");
    logger.info("#                Copyright Siemens 2022");
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
    Options options = new Options();
    options.addOption("help", false, "help");
    options.addOption("loginconfig", true, "EDM Library auto-login configuration");
    options.addOption("user", true, "EDM Library username");
    options.addOption("password", true, "EDM Library password");
    options.addOption("server", true, "EDM Library server");
    options.addOption("subscribe", false, "Synchronize Content Provider subscriptions");
    options.addOption("get", false, "Get updates from Content Provider for subscribed parts since last synchronization");
    options.addOption("getfull", false, "Get full update from Content Provider for all subscribed parts");
    options.addOption("restart", false, "Restart the 'get' or 'getfull' operation from where it was interrupted");
    options.addOption("cachedir", true, "Directory used to store part data cache (Defaults to <userhome>/.dmsccp/synccache");
    options.addOption("reconcile", false, "Perform incremental reconcile of EDM Library Manufacturer Parts with External Content");
    options.addOption("reconcilefull", false, "Perform full reconcile of EDM Library Manufacturer Parts with External Content");
    options.addOption("reconcileauto", false, "Set auto mapping of SYNC properties during reconcile");
    options.addOption("reviewsync", false, "Launch Reconcile Application to review unreconciled MPNs");
    options.addOption("updcomps", false, "Perform incremental update of EDM Library Components with EDM Library MPN properties");
    options.addOption("updcompsaffected", false, "Perform update of EDM Library Components with EDM Library MPN properties for MPNs reconciled in this run");
    options.addOption("updcompsfull", false, "Perform full update of EDM Library Components with EDM Library MPN properties");
    options.addOption("interceptors", true, "Comma-separated list of InterceptoryFactory classes to register (must be in the CLASSPATH)");
    options.addOption("disableNotificationServices", false, "Disable Notification Services Event Provider processing");
    options.addOption("params", true, "Comma-separated list of \"param=value\" custom parameters");
    options.addOption("ccpid", true, "Content Provider ID");
    BasicParser basicParser = new BasicParser();
    CommandLine commandLine = null;
    try {
      commandLine = basicParser.parse(options, paramArrayOfString);
    } catch (ParseException parseException) {
      logger.error("Error: " + parseException.getMessage());
      HelpFormatter helpFormatter = new HelpFormatter();
      helpFormatter.printHelp("ContentProviderSync", options);
      System.exit(1);
    } 
    if (commandLine.hasOption("help")) {
      HelpFormatter helpFormatter = new HelpFormatter();
      helpFormatter.printHelp("ContentProviderSync", options);
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
      for (String str : str6.split("\\s*,\\s*")) {
        String[] arrayOfString = str.split("\\s*=\\s*");
        if (arrayOfString.length == 2)
          customSyncAppParams.put(arrayOfString[0], arrayOfString[1]); 
      } 
      if (!str5.isEmpty()) {
        logger.info("Registering user-supplied interceptors...");
        OIHelper.registerOI(oIObjectManagerFactory);
        for (String str : str5.split("\\s*,\\s*")) {
          logger.info("  Registering interceptor \"" + str + "\"...");
          Class<?> clazz = Class.forName(str);
          InterceptorFactoryPriorityDecorator interceptorFactoryPriorityDecorator = new InterceptorFactoryPriorityDecorator((InterceptorFactory)clazz.newInstance(), 500);
          OIHelper.addInterceptorFactoryToRegistry(oIObjectManagerFactory, interceptorFactoryPriorityDecorator);
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
      logger.error("Unable to connect to EDM Library server:  " + oIException.getMessage());
      bool = true;
    } catch (ContentProviderSyncException contentProviderSyncException) {
      logger.error(contentProviderSyncException.getMessage());
      bool = true;
    } catch (ContentProviderConfigException contentProviderConfigException) {
      logger.error("Problem encountered while reading Content Provider configuration :  " + contentProviderConfigException.getMessage());
      bool = true;
    } catch (ContentProviderException contentProviderException) {
      logger.error("Problem encountered while registering Content Providers:  " + contentProviderException.getMessage());
      bool = true;
    } catch (ClassNotFoundException classNotFoundException) {
      logger.error("Problem encountered while registering user-supplied interceptor factory:  " + classNotFoundException.getMessage());
      bool = true;
    } catch (InstantiationException instantiationException) {
      logger.error("Problem encountered while registering user-supplied interceptor factory:  " + instantiationException.getMessage());
      bool = true;
    } catch (IllegalAccessException illegalAccessException) {
      logger.error("Problem encountered while registering user-supplied interceptor factory:  " + illegalAccessException.getMessage());
      bool = true;
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


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\sync\ContentProviderSyncApp.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */