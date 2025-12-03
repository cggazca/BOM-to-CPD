package com.mentor.dms.contentprovider.core.utils;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import org.apache.log4j.PropertyConfigurator;

public class LogConfigLoader {
  public static void configLog4j() throws Exception {
    readConfig();
    URL uRL = new URL(System.getProperty("log4j.configuration"));
    PropertyConfigurator.configure(uRL);
  }
  
  private static void readConfig() throws Exception {
    String str = getEDMConfigurationPath() + "config.ini";
    URL uRL = new URL(str);
    Properties properties = new Properties();
    if (uRL != null) {
      InputStream inputStream = uRL.openStream();
      try {
        properties.load(inputStream);
      } finally {
        inputStream.close();
      } 
      String str1 = properties.getProperty("dms.home.dir");
      System.setProperty("dms.home.dir", str1);
    } 
  }
  
  private static String getEDMConfigurationPath() {
    String str = System.getenv("DBEDIR");
    if (str == null || str.isEmpty())
      str = System.getenv("SDD_HOME") + System.getenv("SDD_HOME") + "dms"; 
    return "file:" + str + File.separator + "java" + File.separator + "DMSBrowser" + File.separator + "configuration" + File.separator;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\cor\\utils\LogConfigLoader.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */