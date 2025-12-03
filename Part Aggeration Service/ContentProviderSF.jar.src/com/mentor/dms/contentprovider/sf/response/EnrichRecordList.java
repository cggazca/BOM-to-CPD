package com.mentor.dms.contentprovider.sf.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class EnrichRecordList {
  @SerializedName("enrichingProviderId")
  @Expose
  private String enrichingProviderId;
  
  @SerializedName("matchedRecords")
  @Expose
  private List<EnrichRecord> matchedRecords;
  
  public String getEnrichingProviderId() {
    return this.enrichingProviderId;
  }
  
  public List<EnrichRecord> getMatchedRecords() {
    return this.matchedRecords;
  }
  
  public void setEnrichingProviderId(String paramString) {
    this.enrichingProviderId = paramString;
  }
  
  public void setMatchedRecords(List<EnrichRecord> paramList) {
    this.matchedRecords = paramList;
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (!(paramObject instanceof EnrichRecordList))
      return false; 
    EnrichRecordList enrichRecordList = (EnrichRecordList)paramObject;
    if (!enrichRecordList.canEqual(this))
      return false; 
    String str1 = getEnrichingProviderId();
    String str2 = enrichRecordList.getEnrichingProviderId();
    if ((str1 == null) ? (str2 != null) : !str1.equals(str2))
      return false; 
    List<EnrichRecord> list1 = getMatchedRecords();
    List<EnrichRecord> list2 = enrichRecordList.getMatchedRecords();
    return !((list1 == null) ? (list2 != null) : !list1.equals(list2));
  }
  
  protected boolean canEqual(Object paramObject) {
    return paramObject instanceof EnrichRecordList;
  }
  
  public int hashCode() {
    null = 1;
    String str = getEnrichingProviderId();
    null = null * 59 + ((str == null) ? 43 : str.hashCode());
    List<EnrichRecord> list = getMatchedRecords();
    return null * 59 + ((list == null) ? 43 : list.hashCode());
  }
  
  public String toString() {
    return "EnrichRecordList(enrichingProviderId=" + getEnrichingProviderId() + ", matchedRecords=" + String.valueOf(getMatchedRecords()) + ")";
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\response\EnrichRecordList.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */