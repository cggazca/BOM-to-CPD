package com.mentor.dms.contentprovider.core.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class DateUtils {
  private static final TimeZone UTC_TIMEZONE = TimeZone.getTimeZone("UTC");
  
  private static final SimpleDateFormat dfCP = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
  
  private static final SimpleDateFormat dfEDM = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  
  private static final SimpleDateFormat dfDefTimezone = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  
  public static String toUNIXTime(String paramString) {
    String str = paramString;
    try {
      Date date = dfEDM.parse(paramString);
      long l = date.getTime();
      str = String.valueOf(l);
    } catch (ParseException parseException) {
      parseException.printStackTrace();
    } 
    return str;
  }
  
  public static Date toDate(String paramString) throws ParseException {
    return dfEDM.parse(paramString);
  }
  
  public static Map toJsonTime(String paramString) {
    HashMap<Object, Object> hashMap = new HashMap<>();
    String str = paramString;
    hashMap.put("__complex__", "Timestamp");
    try {
      Date date = dfEDM.parse(paramString);
      str = dfCP.format(date);
    } catch (ParseException parseException) {}
    hashMap.put("iso8601Timestamp", str);
    return hashMap;
  }
  
  public static Map<String, String> toJsonTime(long paramLong) {
    HashMap<Object, Object> hashMap = new HashMap<>();
    hashMap.put("__complex__", "Timestamp");
    Date date = new Date(paramLong);
    String str = dfCP.format(date);
    if (str.endsWith("Z"))
      str = str.replaceAll("Z$", "+00:00"); 
    hashMap.put("iso8601Timestamp", str);
    return (Map)hashMap;
  }
  
  public static String toStringTime(Map paramMap) {
    if ("Timestamp".equals(paramMap.get("__complex__"))) {
      String str = paramMap.get("iso8601Timestamp").toString();
      try {
        Date date = dfCP.parse(str);
        str = dfEDM.format(date);
      } catch (ParseException parseException) {}
      return str;
    } 
    return paramMap.toString();
  }
  
  public static String toEDMTime(Date paramDate) {
    return dfDefTimezone.format(paramDate);
  }
  
  public static Date parse(String paramString) throws ParseException {
    return dfDefTimezone.parse(paramString);
  }
  
  static {
    dfCP.setTimeZone(UTC_TIMEZONE);
    dfEDM.setTimeZone(UTC_TIMEZONE);
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\cor\\utils\DateUtils.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */