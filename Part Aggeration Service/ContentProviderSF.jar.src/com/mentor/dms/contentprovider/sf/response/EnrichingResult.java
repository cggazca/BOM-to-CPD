package com.mentor.dms.contentprovider.sf.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class EnrichingResult {
  @SerializedName("succeeded")
  @Expose
  private Map<String, EnrichRecordList> succeeded;
  
  @SerializedName("failed")
  @Expose
  private Map<String, ErrorObject> failed;
  
  public EnrichRecordList getEnrichRecordList() {
    EnrichRecordList enrichRecordList = null;
    Set<String> set = this.succeeded.keySet();
    Iterator<String> iterator = set.iterator();
    if (iterator.hasNext()) {
      String str = iterator.next();
      return this.succeeded.get(str);
    } 
    return null;
  }
  
  public Map<String, EnrichRecordList> getSucceeded() {
    return this.succeeded;
  }
  
  public Map<String, ErrorObject> getFailed() {
    return this.failed;
  }
  
  public void setSucceeded(Map<String, EnrichRecordList> paramMap) {
    this.succeeded = paramMap;
  }
  
  public void setFailed(Map<String, ErrorObject> paramMap) {
    this.failed = paramMap;
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (!(paramObject instanceof EnrichingResult))
      return false; 
    EnrichingResult enrichingResult = (EnrichingResult)paramObject;
    if (!enrichingResult.canEqual(this))
      return false; 
    Map<String, EnrichRecordList> map1 = getSucceeded();
    Map<String, EnrichRecordList> map2 = enrichingResult.getSucceeded();
    if ((map1 == null) ? (map2 != null) : !map1.equals(map2))
      return false; 
    Map<String, ErrorObject> map3 = getFailed();
    Map<String, ErrorObject> map4 = enrichingResult.getFailed();
    return !((map3 == null) ? (map4 != null) : !map3.equals(map4));
  }
  
  protected boolean canEqual(Object paramObject) {
    return paramObject instanceof EnrichingResult;
  }
  
  public int hashCode() {
    null = 1;
    Map<String, EnrichRecordList> map = getSucceeded();
    null = null * 59 + ((map == null) ? 43 : map.hashCode());
    Map<String, ErrorObject> map1 = getFailed();
    return null * 59 + ((map1 == null) ? 43 : map1.hashCode());
  }
  
  public String toString() {
    return "EnrichingResult(succeeded=" + String.valueOf(getSucceeded()) + ", failed=" + String.valueOf(getFailed()) + ")";
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\response\EnrichingResult.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */