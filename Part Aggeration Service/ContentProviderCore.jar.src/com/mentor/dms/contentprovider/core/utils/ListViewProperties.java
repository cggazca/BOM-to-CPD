package com.mentor.dms.contentprovider.core.utils;

import com.mentor.datafusion.utils.logger.MGLogger;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

public class ListViewProperties {
  private static MGLogger logger = MGLogger.getLogger(ListViewProperties.class);
  
  private static final String INIT_FILE_PATH = "/resources/ListView.properties";
  
  private static Properties properties = null;
  
  public static String getProperty(String paramString) {
    return getProperty(paramString, "");
  }
  
  public static String getProperty(String paramString1, String paramString2) {
    return properties.getProperty(paramString1, paramString2);
  }
  
  public static int getIntProperty(String paramString, int paramInt) {
    try {
      return Integer.parseInt(properties.getProperty(paramString));
    } catch (Exception exception) {
      return paramInt;
    } 
  }
  
  public static List<String> getPropertyCSV(String paramString) {
    String str = properties.getProperty(paramString);
    return (str == null) ? null : csv2List(str);
  }
  
  private static ArrayList<String> csv2List(String paramString) {
    ArrayList<String> arrayList = new ArrayList();
    String[] arrayOfString = paramString.split(",", -1);
    for (int i = 0;; i++) {
      String str;
      if (i < arrayOfString.length) {
        str = arrayOfString[i];
        if (str.length() > 0 && str.charAt(0) == '"') {
          if (str.charAt(str.length() - 1) == '"') {
            str = StringUtils.strip(str, "\"");
            arrayList.add(str);
          } else {
            StringBuilder stringBuilder = new StringBuilder(str);
            for (int j = i + 1; j < arrayOfString.length; j++) {
              String str1 = arrayOfString[j];
              stringBuilder.append("," + str1);
              if (str1.length() > 0 && str1.charAt(str1.length() - 1) == '"') {
                i = j;
                str = stringBuilder.toString();
                str = StringUtils.strip(str, "\"");
                break;
              } 
            } 
            arrayList.add(str);
          } 
          continue;
        } 
      } else {
        break;
      } 
      arrayList.add(str);
    } 
    return arrayList;
  }
  
  public static Set<Object> getPropertyKey() {
    return properties.keySet();
  }
  
  static {
    properties = new Properties();
    try {
      InputStream inputStream = ListViewProperties.class.getResourceAsStream("/resources/ListView.properties");
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
      logger.error(String.format("Failed to load properties:%s", new Object[] { "/resources/ListView.properties" }), iOException);
    } catch (Throwable throwable) {
      throwable.getCause().printStackTrace();
    } 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\cor\\utils\ListViewProperties.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */