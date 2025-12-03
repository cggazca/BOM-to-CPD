package com.mentor.dms.contentprovider.sf.mapping;

import com.mentor.dms.contentprovider.core.mapping.MappingUtils;
import com.mentor.dms.contentprovider.core.sync.ContentProviderSyncException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MappingUtils extends MappingUtils {
  static Pattern capacitancePattern = Pattern.compile("^([0-9\\.]+)([punm]*(?:F)*)$");
  
  public static String getSingleValueResistance(String paramString) throws ContentProviderSyncException {
    if (paramString == null || paramString.trim().isEmpty() || paramString.startsWith("N/A"))
      return ""; 
    paramString = paramString.replaceAll("\\(.*\\)", "");
    Pattern pattern = Pattern.compile("^([0-9\\.]+)([unmkKMGT]*)(?:[/|]+[0-9\\.]+[unmkKMGT]*)*((?:Ohm)*)$");
    String str = "";
    Matcher matcher = pattern.matcher(paramString);
    if (matcher.find()) {
      String str1 = matcher.group(1);
      String str2 = matcher.group(2);
      String str3 = matcher.group(3).trim();
      str = str1 + str1 + str2;
    } else {
      String str1 = "Unable to parse numeric resistance value '" + paramString + "'.";
      throw new ContentProviderSyncException(str1);
    } 
    return str;
  }
  
  public static String getSingleValueCapacitance(String paramString) throws ContentProviderSyncException {
    if (paramString == null || paramString.trim().isEmpty() || paramString.startsWith("N/A"))
      return ""; 
    paramString = paramString.replaceAll("\\(.*\\)", "");
    String str = "";
    Matcher matcher = capacitancePattern.matcher(paramString);
    if (matcher.find()) {
      String str1 = matcher.group(1);
      String str2 = matcher.group(2).trim();
      if (str2.isEmpty())
        str2 = str2 + "F"; 
      str = str1 + str1;
    } else {
      String str1 = "Unable to parse numeric capacitance value '" + paramString + "'.";
      throw new ContentProviderSyncException(str1);
    } 
    return str;
  }
  
  public static String getSingleValueFrequency(String paramString) throws ContentProviderSyncException {
    if (paramString == null || paramString.trim().isEmpty() || paramString.startsWith("N/A"))
      return ""; 
    paramString = paramString.replaceAll("\\(.*\\)", "");
    Pattern pattern = Pattern.compile("^([0-9\\.]+)([nmkKMGT]*(?:Hz)*)$");
    String str = "";
    Matcher matcher = pattern.matcher(paramString);
    if (matcher.find()) {
      String str1 = matcher.group(1);
      String str2 = matcher.group(2).trim();
      if (str2.isEmpty())
        str2 = str2 + "Mhz"; 
      str = str1 + str1;
    } else {
      String str1 = "Unable to parse numeric resonance frequency value '" + paramString + "'.";
      throw new ContentProviderSyncException(str1);
    } 
    return str;
  }
  
  public static String getSingleValueImpedance(String paramString) throws ContentProviderSyncException {
    if (paramString == null || paramString.trim().isEmpty() || paramString.startsWith("N/A"))
      return ""; 
    paramString = paramString.replaceAll("\\(.*\\)", "");
    Pattern pattern = Pattern.compile("^([0-9\\.]+)([unmkKMGT]*)((?:Ohm)*)$");
    String str = "";
    Matcher matcher = pattern.matcher(paramString);
    if (matcher.find()) {
      String str1 = matcher.group(1);
      String str2 = matcher.group(2);
      String str3 = matcher.group(3).trim();
      str = str1 + str1 + str2;
    } else {
      String str1 = "Unable to parse numeric impedance value '" + paramString + "'.";
      throw new ContentProviderSyncException(str1);
    } 
    return str;
  }
  
  public static String getSingleValueInductance(String paramString) throws ContentProviderSyncException {
    if (paramString == null || paramString.trim().isEmpty() || paramString.startsWith("N/A"))
      return ""; 
    paramString = paramString.replaceAll("\\(.*\\)", "");
    Pattern pattern = Pattern.compile("^([0-9\\.]+)([kGunmMu]*)(?:[/|]+[0-9\\.]+[kGnmMu]*)*(H*)$");
    String str = "";
    Matcher matcher = pattern.matcher(paramString);
    if (matcher.find()) {
      String str1 = matcher.group(1);
      String str2 = matcher.group(2);
      String str3 = matcher.group(3).trim();
      if (str3.isEmpty())
        str3 = str3 + "H"; 
      str = str1 + str1 + str2;
    } else {
      String str1 = "Unable to parse numeric inductance value '" + paramString + "'.";
      throw new ContentProviderSyncException(str1);
    } 
    return str;
  }
  
  public static String getSingleValueVoltage(String paramString) throws ContentProviderSyncException {
    if (paramString == null || paramString.trim().isEmpty() || paramString.startsWith("N/A"))
      return ""; 
    if (paramString.equals("N/AV"))
      return ""; 
    paramString = paramString.replaceAll("\\(.*\\)", "");
    Pattern pattern = Pattern.compile("^([0-9\\.]+)([kGnmMu]*(?:V)*)$");
    String str = "";
    Matcher matcher = pattern.matcher(paramString);
    if (matcher.find()) {
      String str1 = matcher.group(1);
      String str2 = matcher.group(2).trim();
      if (str2.isEmpty())
        str2 = str2 + "V"; 
      str = str1 + str1;
    } else {
      String str1 = "Unable to parse numeric voltage value '" + paramString + "'.";
      throw new ContentProviderSyncException(str1);
    } 
    return str;
  }
  
  public static String getSingleValueRatedDCVoltage(String paramString) throws ContentProviderSyncException {
    Pattern pattern = Pattern.compile("^([0-9\\.]+)((?:VDC|VAC)+)$");
    if (paramString == null || paramString.trim().isEmpty() || paramString.startsWith("N/A"))
      return ""; 
    paramString = paramString.replaceAll("\\(.*\\)", "");
    String[] arrayOfString = paramString.split("[\\|/]");
    byte b = 0;
    String str = null;
    for (String str1 : arrayOfString) {
      Matcher matcher = pattern.matcher(str1);
      if (matcher.find()) {
        String str2 = matcher.group(1).trim();
        String str3 = matcher.group(2).trim();
        if (str3.equals("VDC")) {
          b++;
          str = str2;
          break;
        } 
      } 
    } 
    if (b == 0) {
      String str1 = "Unable to parse numeric rated DC voltage value '" + paramString + "'.";
      throw new ContentProviderSyncException(str1);
    } 
    return str + "V";
  }
  
  public static String getSingleValueCurrent(String paramString) throws ContentProviderSyncException {
    if (paramString == null || paramString.trim().isEmpty() || paramString.startsWith("N/A"))
      return ""; 
    paramString = paramString.replaceAll("\\(.*\\)", "");
    Pattern pattern = Pattern.compile("^([0-9\\.]+)([kGnmMu]*)(?:[/|]+[0-9\\.]+[kGnmMu]*)*(A*)$");
    String str = "";
    Matcher matcher = pattern.matcher(paramString);
    if (matcher.find()) {
      String str1 = matcher.group(1);
      String str2 = matcher.group(2).trim();
      if (str2.isEmpty() || !str2.endsWith("A"))
        str2 = str2 + "A"; 
      str = str1 + str1;
    } else {
      String str1 = "Unable to parse numeric current value '" + paramString + "'.";
      throw new ContentProviderSyncException(str1);
    } 
    return str;
  }
  
  public static String getSingleValuePowerRating(String paramString) throws ContentProviderSyncException {
    if (paramString == null || paramString.trim().isEmpty() || (paramString.startsWith("N/A") | paramString.startsWith("N/R")) != 0)
      return ""; 
    paramString = paramString.replaceAll("\\s*\\([0-9]+/[0-9]+\\)\\s*", "");
    Pattern pattern = Pattern.compile("^([0-9\\.]+)(?:\\|[0-9\\.]+)*(W*)$");
    String str = "";
    Matcher matcher = pattern.matcher(paramString);
    if (matcher.find()) {
      String str1 = matcher.group(1);
      String str2 = matcher.group(2).trim();
      if (str2.isEmpty())
        str2 = str2 + "W"; 
      str = str1 + str1;
    } else {
      String str1 = "Unable to parse numeric power rating value '" + paramString + "'.";
      throw new ContentProviderSyncException(str1);
    } 
    return str;
  }
  
  public static String getSingleValueTemperature(String paramString) throws ContentProviderSyncException {
    if (paramString == null || paramString.trim().isEmpty() || paramString.startsWith("N/A") || paramString.startsWith("N/R"))
      return ""; 
    paramString = paramString.replaceAll("\\(.*\\)", "");
    Pattern pattern = Pattern.compile("^([\\-]?[0-9\\.]+)(?:[/|]+[\\-]?[0-9\\.]+)*(?:�C)*$");
    String str = "";
    Matcher matcher = pattern.matcher(paramString);
    if (matcher.find()) {
      String str1 = matcher.group(1);
      str = str1 + "C";
    } else {
      String str1 = "Unable to parse numeric temperature value '" + paramString + "'.";
      throw new ContentProviderSyncException(str1);
    } 
    return str;
  }
  
  public static void main(String[] paramArrayOfString) {
    try {
      System.out.println(getRegexMatch("360VACV", "(\\d+)(?:VDC|VAC)*", "V"));
    } catch (Exception exception) {
      exception.printStackTrace();
    } 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\mapping\MappingUtils.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */