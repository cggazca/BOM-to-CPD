package com.mentor.dms.contentprovider.sf.datamodel;

import java.util.Comparator;

class StrlenComparator implements Comparator<String> {
  public int compare(String paramString1, String paramString2) {
    return (paramString1.length() < paramString2.length()) ? 1 : ((paramString1.length() > paramString2.length()) ? -1 : paramString1.compareTo(paramString2));
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\datamodel\Feature$StrlenComparator.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */