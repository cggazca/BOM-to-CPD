package com.mentor.dms.contentprovider.sf.datamodel;

import com.mentor.datafusion.utils.logger.MGLogger;
import com.mentor.dms.contentprovider.sf.datamodel.csv2xml.CSVUtil;
import com.mentor.dms.contentprovider.sf.datamodel.csv2xml.csvdata.CSVParseException;
import com.mentor.dms.contentprovider.sf.datamodel.upgrade.DatamodelUpgradeApp;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class MappingUpgradeProperties {
  private static MGLogger logger = MGLogger.getLogger(MappingUpgradeProperties.class);
  
  private static final String CONF_FILE_PATH = "config/";
  
  private static final String CONF_FILE_NAME = "MappingUpgradeSettings.properties";
  
  private static Properties properties = null;
  
  public static void load() throws IOException {
    properties = new Properties();
    ClassLoader classLoader = DatamodelUpgradeApp.class.getClassLoader();
    URL uRL = classLoader.getResource("config/");
    if (uRL == null)
      throw new FileNotFoundException("MappingUpgradeSettings.properties"); 
    try {
      InputStream inputStream = classLoader.getResourceAsStream("config/MappingUpgradeSettings.properties");
      try {
        if (inputStream == null)
          throw new FileNotFoundException("MappingUpgradeSettings.properties"); 
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
      throw iOException;
    } 
  }
  
  public static Set<String> getProperties(String paramString) throws CSVParseException {
    String str = properties.getProperty(paramString);
    if (str == null)
      throw new CSVParseException("Configuration file item is missing:\"" + paramString + "\""); 
    return CSVUtil.csv2Set(str);
  }
  
  public static Map<String, String> getPropertyMap(String paramString) throws CSVParseException {
    String str = properties.getProperty(paramString);
    if (str == null)
      throw new CSVParseException("Configuration file item is missing:\"" + paramString + "\""); 
    HashMap<Object, Object> hashMap = new HashMap<>();
    ArrayList arrayList = CSVUtil.csv2List(str);
    for (String str1 : arrayList) {
      if (str1.indexOf("=") == -1)
        throw new CSVParseException("Configuration \"" + paramString + "\" format is incorrect."); 
      String str2 = str1.substring(0, str1.indexOf("="));
      String str3 = str1.substring(str1.indexOf("=") + 1);
      hashMap.put(str2, str3);
    } 
    return (Map)hashMap;
  }
  
  public static Set<Object> getPropertyKey() {
    return properties.keySet();
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\datamodel\MappingUpgradeProperties.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */