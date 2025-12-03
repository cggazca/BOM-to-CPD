package com.mentor.dms.contentprovider.sf.response.facets;

import java.util.Comparator;
import java.util.Map;

class null implements Comparator<Object> {
  public int compare(Object paramObject1, Object paramObject2) {
    String str1 = null;
    String str2 = null;
    if (paramObject1 instanceof Map && paramObject2 instanceof Map) {
      Object object1 = ((Map)paramObject1).get("value");
      Object object2 = ((Map)paramObject2).get("value");
      str1 = object1.toString();
      str2 = object2.toString();
    } 
    return str1.compareTo(str2);
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\response\facets\Facets$1.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */