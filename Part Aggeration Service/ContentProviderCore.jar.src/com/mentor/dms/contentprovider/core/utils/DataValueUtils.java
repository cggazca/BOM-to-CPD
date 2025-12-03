package com.mentor.dms.contentprovider.core.utils;

public class DataValueUtils {
  public static final String getAppropriateValue(String paramString) {
    try {
      Double double_ = Double.valueOf(Double.parseDouble(paramString));
      if (double_.doubleValue() == double_.longValue()) {
        paramString = String.valueOf(double_.longValue());
      } else {
        paramString = String.valueOf(double_);
      } 
    } catch (NumberFormatException numberFormatException) {}
    return paramString;
  }
  
  public static final String leftB(String paramString1, int paramInt, String paramString2) {
    StringBuffer stringBuffer = new StringBuffer();
    int i = 0;
    try {
      for (byte b = 0; b < paramString1.length(); b++) {
        String str = paramString1.substring(b, b + 1);
        byte[] arrayOfByte = str.getBytes(paramString2);
        if (i + arrayOfByte.length > paramInt)
          return stringBuffer.toString(); 
        stringBuffer.append(str);
        i += arrayOfByte.length;
      } 
    } catch (Exception exception) {
      exception.printStackTrace();
    } 
    return stringBuffer.toString();
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\cor\\utils\DataValueUtils.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */