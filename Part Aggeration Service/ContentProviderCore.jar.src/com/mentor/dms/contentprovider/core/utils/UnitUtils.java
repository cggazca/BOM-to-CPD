package com.mentor.dms.contentprovider.core.utils;

import com.mentor.datafusion.oi.OICursor;
import com.mentor.datafusion.oi.OIException;
import com.mentor.datafusion.oi.OIQuery;
import com.mentor.dms.contentprovider.core.ContentProviderGlobal;
import com.mentor.dms.ui.DMSInstance;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UnitUtils {
  private static final String REGEXP = "[kMGTmunp]$";
  
  private static Pattern patternSI = Pattern.compile("[kMGTmunp]$");
  
  public static String convertUnit(String paramString) {
    String str = "";
    try {
      boolean bool = false;
      for (Object object : GetUnitProperties.getPropertyKey()) {
        if (object.equals(paramString))
          bool = true; 
      } 
      if (bool) {
        str = GetUnitProperties.getProperty(paramString);
      } else if (str.isEmpty()) {
        str = paramString;
      } 
    } catch (Throwable throwable) {
      throwable.printStackTrace();
    } 
    return str;
  }
  
  public static HashMap<String, Double> getUnitRange(String paramString) throws OIException {
    HashMap<Object, Object> hashMap = new HashMap<>();
    DMSInstance dMSInstance = ContentProviderGlobal.getDMSInstance();
    OIQuery oIQuery = dMSInstance.getObjectManager().createQuery("Unit", true);
    oIQuery.addRestriction("UnitKey", paramString);
    oIQuery.addColumn("UnitDefinition.RangeName");
    oIQuery.addColumn("UnitDefinition.RangeLimit");
    OICursor oICursor = oIQuery.execute();
    while (oICursor.next())
      hashMap.put(oICursor.getString("RangeName"), oICursor.getDouble("RangeLimit")); 
    oICursor.close();
    return (HashMap)hashMap;
  }
  
  public static boolean isNumberStrPrefix(String paramString) {
    try {
      BigDecimal bigDecimal = new BigDecimal(paramString);
      return true;
    } catch (NumberFormatException numberFormatException) {
      Matcher matcher = patternSI.matcher(paramString);
      if (matcher.find())
        try {
          BigDecimal bigDecimal = new BigDecimal(paramString.substring(0, matcher.start()));
          return true;
        } catch (NumberFormatException numberFormatException1) {
          return false;
        }  
      return false;
    } 
  }
  
  public static boolean isNumberStr(String paramString) {
    try {
      BigDecimal bigDecimal = new BigDecimal(paramString);
      return true;
    } catch (NumberFormatException numberFormatException) {
      return false;
    } 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\cor\\utils\UnitUtils.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */