package com.mentor.dms.contentprovider.mapping;

import com.mentor.datafusion.utils.logger.MGLogger;
import com.mentor.dms.contentprovider.sync.ContentProviderSyncException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class MappingUtils {
  static MGLogger logger = MGLogger.getLogger(MappingUtils.class);
  
  public Double getUnitsMultiplier(String paramString) {
    return paramString.startsWith("f") ? Double.valueOf(1.0E-15D) : (paramString.startsWith("p") ? Double.valueOf(1.0E-12D) : (paramString.startsWith("n") ? Double.valueOf(1.0E-9D) : (paramString.startsWith("u") ? Double.valueOf(1.0E-6D) : (paramString.startsWith("m") ? Double.valueOf(0.001D) : ((paramString.startsWith("k") || paramString.startsWith("K")) ? Double.valueOf(1000.0D) : (paramString.startsWith("M") ? Double.valueOf(1000000.0D) : (paramString.startsWith("G") ? Double.valueOf(1.0E9D) : (paramString.startsWith("T") ? Double.valueOf(1.0E12D) : Double.valueOf(1.0D)))))))));
  }
  
  public String getSingleValueDouble(String paramString1, String paramString2) throws ContentProviderSyncException {
    if (paramString1 == null || paramString1.trim().isEmpty() || paramString1.startsWith("N/A"))
      return ""; 
    paramString1 = paramString1.replaceAll("\\(.*\\)", "");
    String[] arrayOfString = paramString1.split("[\\|/]");
    if (arrayOfString.length == 0)
      return ""; 
    String str = arrayOfString[0];
    if (str == null || str.trim().isEmpty() || paramString1.startsWith("N/A"))
      return ""; 
    if (str.endsWith(paramString2))
      str = str.substring(0, str.length() - paramString2.length()); 
    if (str == null || str.trim().isEmpty() || paramString1.startsWith("N/A"))
      return ""; 
    Pattern pattern = Pattern.compile("^([0-9\\.]+)([f|p|n|m|u|k|K|M|G|T]*)$");
    Matcher matcher = pattern.matcher(str);
    return !matcher.find() ? "" : (str + str);
  }
  
  public String getRegexMatch(String paramString1, String paramString2, String paramString3) throws ContentProviderSyncException {
    String str = "";
    if (paramString1 == null || paramString1.trim().isEmpty())
      return str; 
    Pattern pattern = null;
    try {
      pattern = Pattern.compile(paramString2);
    } catch (PatternSyntaxException patternSyntaxException) {
      throw new ContentProviderSyncException("MappingUtils.getRegexMatch(): Invalid regular expression '" + paramString2 + "': " + patternSyntaxException.getMessage());
    } 
    Matcher matcher = pattern.matcher(paramString1);
    if (!matcher.find())
      throw new ContentProviderSyncException("MappingUtils.getRegexMatch(): Value '" + paramString1 + "' does not match regular expression '" + paramString2 + "'."); 
    if (matcher.groupCount() < 1)
      throw new ContentProviderSyncException("MappingUtils.getRegexMatch(): Regular expression '" + paramString2 + "' must contain a group."); 
    str = matcher.group(1).trim();
    if (!str.isEmpty())
      str = str + str; 
    return str;
  }
  
  public static void main(String[] paramArrayOfString) {
    try {
      MappingUtils mappingUtils = new MappingUtils();
      System.out.println(mappingUtils.getRegexMatch("360VACV", "(\\d+)(?:VDC|VAC)*", "V"));
    } catch (Exception exception) {
      exception.printStackTrace();
    } 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\mapping\MappingUtils.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */