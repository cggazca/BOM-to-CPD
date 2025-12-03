package com.mentor.dms.contentprovider.sf.responsePartByID;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Part {
  @SerializedName("succeeded")
  @Expose
  private Map<String, PartRecord> succeeded;
  
  @SerializedName("failed")
  @Expose
  private Map<String, FailedRecord> failed;
  
  public PartRecord getPartRecord() {
    PartRecord partRecord = null;
    Set<String> set = this.succeeded.keySet();
    Iterator<String> iterator = set.iterator();
    if (iterator.hasNext()) {
      String str = iterator.next();
      return this.succeeded.get(str);
    } 
    return null;
  }
  
  public FailedRecord getFailedPartRecord() {
    FailedRecord failedRecord = null;
    Set<String> set = this.failed.keySet();
    Iterator<String> iterator = set.iterator();
    if (iterator.hasNext()) {
      String str = iterator.next();
      return this.failed.get(str);
    } 
    return null;
  }
  
  public Map<String, PartRecord> getSucceeded() {
    return this.succeeded;
  }
  
  public Map<String, FailedRecord> getFailed() {
    return this.failed;
  }
  
  public void setSucceeded(Map<String, PartRecord> paramMap) {
    this.succeeded = paramMap;
  }
  
  public void setFailed(Map<String, FailedRecord> paramMap) {
    this.failed = paramMap;
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (!(paramObject instanceof Part))
      return false; 
    Part part = (Part)paramObject;
    if (!part.canEqual(this))
      return false; 
    Map<String, PartRecord> map1 = getSucceeded();
    Map<String, PartRecord> map2 = part.getSucceeded();
    if ((map1 == null) ? (map2 != null) : !map1.equals(map2))
      return false; 
    Map<String, FailedRecord> map3 = getFailed();
    Map<String, FailedRecord> map4 = part.getFailed();
    return !((map3 == null) ? (map4 != null) : !map3.equals(map4));
  }
  
  protected boolean canEqual(Object paramObject) {
    return paramObject instanceof Part;
  }
  
  public int hashCode() {
    null = 1;
    Map<String, PartRecord> map = getSucceeded();
    null = null * 59 + ((map == null) ? 43 : map.hashCode());
    Map<String, FailedRecord> map1 = getFailed();
    return null * 59 + ((map1 == null) ? 43 : map1.hashCode());
  }
  
  public String toString() {
    return "Part(succeeded=" + String.valueOf(getSucceeded()) + ", failed=" + String.valueOf(getFailed()) + ")";
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\responsePartByID\Part.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */