package com.mentor.dms.contentprovider.sf.response.facets;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Map;

class null implements Comparator<Map.Entry<String, String>> {
  public int compare(Map.Entry<String, String> paramEntry1, Map.Entry<String, String> paramEntry2) {
    try {
      BigDecimal bigDecimal1 = new BigDecimal(paramEntry1.getKey());
      BigDecimal bigDecimal2 = new BigDecimal(paramEntry2.getKey());
      return bigDecimal1.compareTo(bigDecimal2);
    } catch (Exception exception) {
      return ((String)paramEntry1.getKey()).compareTo(paramEntry2.getKey());
    } 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\response\facets\Facets$2.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */