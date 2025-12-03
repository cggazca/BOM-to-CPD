package com.mentor.dms.contentprovider.sf.utils;

import com.mentor.datafusion.utils.logger.MGLogger;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class PropertyUtil {
  private static MGLogger logger = MGLogger.getLogger(PropertyUtil.class);
  
  private static final String INIT_FILE_PATH = "/resources/define.properties";
  
  private static Properties properties = null;
  
  public static String getProperty(String paramString) {
    return getProperty(paramString, "");
  }
  
  public static String getProperty(String paramString1, String paramString2) {
    return properties.getProperty(paramString1, paramString2);
  }
  
  static {
    properties = new Properties();
    try {
      InputStream inputStream = PropertyUtil.class.getResourceAsStream("/resources/define.properties");
      try {
        properties.load(new InputStreamReader(inputStream, "UTF-8"));
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
      logger.error(String.format("Failed to load properties:%s", new Object[] { "/resources/define.properties" }), iOException);
    } 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\s\\utils\PropertyUtil.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */