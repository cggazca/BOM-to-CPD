package com.mentor.dms.contentprovider.sf.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mentor.dms.contentprovider.core.utils.DateUtils;
import java.util.HashMap;
import java.util.Map;

public class PartProperties {
  @SerializedName("succeeded")
  @Expose
  private Map<String, Object> succeeded;
  
  @SerializedName("failed")
  @Expose
  private Map<String, ErrorObject> failed;
  
  public Object get(String paramString) {
    Object object = this.succeeded.get(paramString);
    if (object == null)
      object = this.failed.get(paramString); 
    return object;
  }
  
  public String getValue(String paramString) {
    Object object = this.succeeded.get(paramString);
    if (object == null)
      object = this.failed.get(paramString); 
    if (object instanceof Map) {
      Map map = (Map)object;
      if ("Timestamp".equals(map.get("__complex__")))
        return DateUtils.toStringTime(map); 
      if ("Url".equals(map.get("__complex__")))
        object = map.get("value"); 
    } 
    return object.toString();
  }
  
  public Map<String, Object> getAll() {
    HashMap<Object, Object> hashMap = new HashMap<>();
    hashMap.putAll(this.succeeded);
    hashMap.putAll(this.failed);
    return (Map)hashMap;
  }
  
  public Map<String, Object> getSucceeded() {
    return this.succeeded;
  }
  
  public Map<String, ErrorObject> getFailed() {
    return this.failed;
  }
  
  public void setSucceeded(Map<String, Object> paramMap) {
    this.succeeded = paramMap;
  }
  
  public void setFailed(Map<String, ErrorObject> paramMap) {
    this.failed = paramMap;
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (!(paramObject instanceof PartProperties))
      return false; 
    PartProperties partProperties = (PartProperties)paramObject;
    if (!partProperties.canEqual(this))
      return false; 
    Map<String, Object> map1 = getSucceeded();
    Map<String, Object> map2 = partProperties.getSucceeded();
    if ((map1 == null) ? (map2 != null) : !map1.equals(map2))
      return false; 
    Map<String, ErrorObject> map3 = getFailed();
    Map<String, ErrorObject> map4 = partProperties.getFailed();
    return !((map3 == null) ? (map4 != null) : !map3.equals(map4));
  }
  
  protected boolean canEqual(Object paramObject) {
    return paramObject instanceof PartProperties;
  }
  
  public int hashCode() {
    null = 1;
    Map<String, Object> map = getSucceeded();
    null = null * 59 + ((map == null) ? 43 : map.hashCode());
    Map<String, ErrorObject> map1 = getFailed();
    return null * 59 + ((map1 == null) ? 43 : map1.hashCode());
  }
  
  public String toString() {
    return "PartProperties(succeeded=" + String.valueOf(getSucceeded()) + ", failed=" + String.valueOf(getFailed()) + ")";
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\response\PartProperties.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */