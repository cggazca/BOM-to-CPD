package com.mentor.dms.contentprovider.sf.datamodel.csv2xml;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

public class CSVUtil {
  public static ArrayList<String> csv2List(String paramString) {
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
  
  public static Set<String> csv2Set(String paramString) {
    HashSet<String> hashSet = new HashSet();
    String[] arrayOfString = paramString.split(",", -1);
    for (int i = 0;; i++) {
      String str;
      if (i < arrayOfString.length) {
        str = arrayOfString[i];
        if (str.length() > 0 && str.charAt(0) == '"') {
          if (str.charAt(str.length() - 1) == '"') {
            str = StringUtils.strip(str, "\"");
            hashSet.add(str);
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
            hashSet.add(str);
          } 
          continue;
        } 
      } else {
        break;
      } 
      hashSet.add(str);
    } 
    return hashSet;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\datamodel\csv2xml\CSVUtil.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */