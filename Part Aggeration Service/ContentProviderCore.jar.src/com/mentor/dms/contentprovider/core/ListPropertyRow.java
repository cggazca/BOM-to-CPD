package com.mentor.dms.contentprovider.core;

import com.mentor.dms.contentprovider.core.utils.DateUtils;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ListPropertyRow {
  private Map mapVals;
  
  private Map mapFail;
  
  public ListPropertyRow(Map paramMap) {
    if (AbstractContentProvider.isV1API()) {
      this.mapVals = paramMap;
      this.mapFail = new HashMap<>();
    } else {
      this.mapVals = (Map)((Map)paramMap.get("fields")).get("succeeded");
      this.mapFail = (Map)((Map)paramMap.get("fields")).get("failed");
    } 
  }
  
  public ListPropertyRow(Map paramMap1, Map paramMap2) {
    this.mapVals = paramMap1;
    this.mapFail = paramMap2;
  }
  
  public String getValue(String paramString) {
    Object object = this.mapVals.get(paramString);
    if (object == null) {
      object = this.mapFail.get(paramString);
      if (object instanceof Map) {
        Map map = (Map)this.mapFail.get(paramString);
        object = map.get("code").toString() + ":" + map.get("code").toString();
      } 
    } 
    if (object instanceof Map) {
      Map map = (Map)object;
      if ("Timestamp".equals(map.get("__complex__")))
        return DateUtils.toStringTime(map); 
      if ("Url".equals(map.get("__complex__")))
        object = map.get("value"); 
    } 
    return object.toString();
  }
  
  public String getSucceededValue(String paramString) {
    Object object = this.mapVals.get(paramString);
    if (object == null)
      return ""; 
    if (object instanceof Map) {
      Map map = (Map)object;
      if ("Timestamp".equals(map.get("__complex__")))
        return DateUtils.toStringTime(map); 
      if ("Url".equals(map.get("__complex__")))
        object = map.get("value"); 
    } 
    return object.toString();
  }
  
  public Object getValueObj(String paramString) {
    Object object = this.mapVals.get(paramString);
    if (object == null)
      object = this.mapFail.get(paramString); 
    if (object instanceof Map) {
      Map map = (Map)object;
      if ("Timestamp".equals(map.get("__complex__")))
        return DateUtils.toStringTime(map); 
      if ("Url".equals(map.get("__complex__")))
        object = map.get("value"); 
    } 
    return object;
  }
  
  public boolean isTimestamp(String paramString) {
    Object object = this.mapVals.get(paramString);
    if (object instanceof Map) {
      Map map = (Map)object;
      if ("Timestamp".equals(map.get("__complex__")))
        return true; 
    } 
    return false;
  }
  
  public Set<String> getKeys() {
    return AbstractContentProvider.isV1API() ? this.mapVals.keySet() : new HashSet(this.mapVals.keySet());
  }
  
  public Set<String> getAllKeys() {
    if (AbstractContentProvider.isV1API())
      return this.mapVals.keySet(); 
    HashSet<String> hashSet = new HashSet(this.mapVals.keySet());
    hashSet.addAll(this.mapFail.keySet());
    return hashSet;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\ListPropertyRow.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */