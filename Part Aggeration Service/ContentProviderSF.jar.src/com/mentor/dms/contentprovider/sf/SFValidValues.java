package com.mentor.dms.contentprovider.sf;

import com.mentor.dms.contentprovider.core.AbstractCriteria;
import com.mentor.dms.contentprovider.core.ContentProviderException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SFValidValues {
  public static List<Map.Entry<String, String>> getPropValues(ContentProviderImpl paramContentProviderImpl, String paramString1, String paramString2, AbstractCriteria paramAbstractCriteria) throws ContentProviderException {
    HashMap<Object, Object> hashMap = new HashMap<>();
    AggregationServiceWebCall aggregationServiceWebCall = new AggregationServiceWebCall(paramContentProviderImpl);
    List<Map.Entry<String, String>> list = aggregationServiceWebCall.getFacets(paramString1, paramString2, paramAbstractCriteria);
    hashMap.put(paramString2, list);
    return (List<Map.Entry<String, String>>)hashMap.get(paramString2);
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\SFValidValues.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */