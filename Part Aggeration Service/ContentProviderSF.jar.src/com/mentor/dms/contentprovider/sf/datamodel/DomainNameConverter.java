package com.mentor.dms.contentprovider.sf.datamodel;

import com.mentor.datafusion.utils.logger.MGLogger;
import com.mentor.dms.contentprovider.sf.datamodel.upgrade.DatamodelUpgradeApp;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Properties;
import java.util.Set;

public class DomainNameConverter {
  private static MGLogger logger = MGLogger.getLogger(DomainNameConverter.class);
  
  private static final String CONF_FILE_PATH = "config/";
  
  private static final String CONF_FILE_NAME = "DomainNameConvertTable.properties";
  
  private static Properties properties = null;
  
  public static void load() throws IOException {
    properties = new Properties();
    ClassLoader classLoader = DatamodelUpgradeApp.class.getClassLoader();
    URL uRL = classLoader.getResource("config/");
    if (uRL == null)
      throw new FileNotFoundException("DomainNameConvertTable.properties"); 
    try {
      InputStream inputStream = classLoader.getResourceAsStream("config/DomainNameConvertTable.properties");
      try {
        properties.load(new InputStreamReader(inputStream, "UTF-8"));
        if (inputStream == null)
          throw new FileNotFoundException("DomainNameConvertTable.properties"); 
        if (inputStream != null)
          inputStream.close(); 
      } catch (Throwable throwable) {
        if (inputStream != null)
          try {
            inputStream.close();
          } catch (Throwable throwable1) {
            throwable.addSuppressed(throwable1);
          }  
        throw throwable;
      } 
    } catch (IOException iOException) {
      throw iOException;
    } 
  }
  
  public static String convert(String paramString) {
    return (paramString == null) ? null : convert(paramString, null);
  }
  
  public static String convert(String paramString1, String paramString2) {
    return properties.getProperty(paramString1, paramString2);
  }
  
  public static Set<Object> getPropertyKey() {
    return properties.keySet();
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\datamodel\DomainNameConverter.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */