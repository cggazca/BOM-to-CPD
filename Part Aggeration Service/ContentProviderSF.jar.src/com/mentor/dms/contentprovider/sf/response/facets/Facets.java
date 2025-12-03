package com.mentor.dms.contentprovider.sf.response.facets;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mentor.dms.contentprovider.sf.response.ErrorObject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Facets {
  @SerializedName("succeeded")
  @Expose
  private Map<String, List<Object>> succeeded;
  
  @SerializedName("failed")
  @Expose
  private Map<String, ErrorObject> failed;
  
  public List<Object> get(String paramString) {
    return this.succeeded.get(paramString);
  }
  
  public List<Map.Entry<String, String>> getValues(String paramString, boolean paramBoolean) {
    TreeMap<String, Object> treeMap;
    if (paramBoolean) {
      TreeMap<Object, Object> treeMap1 = new TreeMap<>();
    } else {
      treeMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    } 
    List<?> list = this.succeeded.get(paramString);
    Collections.sort(list, new Comparator() {
          public int compare(Object param1Object1, Object param1Object2) {
            String str1 = null;
            String str2 = null;
            if (param1Object1 instanceof Map && param1Object2 instanceof Map) {
              Object object1 = ((Map)param1Object1).get("value");
              Object object2 = ((Map)param1Object2).get("value");
              str1 = object1.toString();
              str2 = object2.toString();
            } 
            return str1.compareTo(str2);
          }
        });
    for (Object object : list) {
      if (object instanceof Map) {
        String str1 = "";
        Object object1 = ((Map)object).get("value");
        if (object1 instanceof BigDecimal) {
          str1 = object1.toString();
        } else if (object1 instanceof String) {
          str1 = (String)object1;
        } else {
          str1 = object1.toString();
        } 
        String str2 = ((Map)object).get("count").toString();
        if (!paramBoolean && treeMap.containsKey(str1)) {
          int i = Integer.parseInt((String)treeMap.get(str1)) + Integer.parseInt(str2);
          str2 = String.valueOf(i);
        } 
        treeMap.put(str1, str2);
      } 
    } 
    ArrayList<?> arrayList = new ArrayList(treeMap.entrySet());
    Collections.sort(arrayList, new Comparator<Map.Entry<String, String>>() {
          public int compare(Map.Entry<String, String> param1Entry1, Map.Entry<String, String> param1Entry2) {
            try {
              BigDecimal bigDecimal1 = new BigDecimal(param1Entry1.getKey());
              BigDecimal bigDecimal2 = new BigDecimal(param1Entry2.getKey());
              return bigDecimal1.compareTo(bigDecimal2);
            } catch (Exception exception) {
              return ((String)param1Entry1.getKey()).compareTo(param1Entry2.getKey());
            } 
          }
        });
    return (List)arrayList;
  }
  
  public Map<String, List<Object>> getSucceeded() {
    return this.succeeded;
  }
  
  public Map<String, ErrorObject> getFailed() {
    return this.failed;
  }
  
  public void setSucceeded(Map<String, List<Object>> paramMap) {
    this.succeeded = paramMap;
  }
  
  public void setFailed(Map<String, ErrorObject> paramMap) {
    this.failed = paramMap;
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (!(paramObject instanceof Facets))
      return false; 
    Facets facets = (Facets)paramObject;
    if (!facets.canEqual(this))
      return false; 
    Map<String, List<Object>> map1 = getSucceeded();
    Map<String, List<Object>> map2 = facets.getSucceeded();
    if ((map1 == null) ? (map2 != null) : !map1.equals(map2))
      return false; 
    Map<String, ErrorObject> map3 = getFailed();
    Map<String, ErrorObject> map4 = facets.getFailed();
    return !((map3 == null) ? (map4 != null) : !map3.equals(map4));
  }
  
  protected boolean canEqual(Object paramObject) {
    return paramObject instanceof Facets;
  }
  
  public int hashCode() {
    null = 1;
    Map<String, List<Object>> map = getSucceeded();
    null = null * 59 + ((map == null) ? 43 : map.hashCode());
    Map<String, ErrorObject> map1 = getFailed();
    return null * 59 + ((map1 == null) ? 43 : map1.hashCode());
  }
  
  public String toString() {
    return "Facets(succeeded=" + String.valueOf(getSucceeded()) + ", failed=" + String.valueOf(getFailed()) + ")";
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\response\facets\Facets.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */