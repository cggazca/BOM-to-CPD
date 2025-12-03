package com.mentor.dms.contentprovider.sf.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Result {
  @SerializedName("nextPageToken")
  @Expose
  private String nextPageToken;
  
  @SerializedName("totalCount")
  @Expose
  private Integer totalCount;
  
  @SerializedName("results")
  @Expose
  private List<SearchResult> results = null;
  
  public String getNextPageToken() {
    return this.nextPageToken;
  }
  
  public Integer getTotalCount() {
    return this.totalCount;
  }
  
  public List<SearchResult> getResults() {
    return this.results;
  }
  
  public void setNextPageToken(String paramString) {
    this.nextPageToken = paramString;
  }
  
  public void setTotalCount(Integer paramInteger) {
    this.totalCount = paramInteger;
  }
  
  public void setResults(List<SearchResult> paramList) {
    this.results = paramList;
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (!(paramObject instanceof Result))
      return false; 
    Result result = (Result)paramObject;
    if (!result.canEqual(this))
      return false; 
    Integer integer1 = getTotalCount();
    Integer integer2 = result.getTotalCount();
    if ((integer1 == null) ? (integer2 != null) : !integer1.equals(integer2))
      return false; 
    String str1 = getNextPageToken();
    String str2 = result.getNextPageToken();
    if ((str1 == null) ? (str2 != null) : !str1.equals(str2))
      return false; 
    List<SearchResult> list1 = getResults();
    List<SearchResult> list2 = result.getResults();
    return !((list1 == null) ? (list2 != null) : !list1.equals(list2));
  }
  
  protected boolean canEqual(Object paramObject) {
    return paramObject instanceof Result;
  }
  
  public int hashCode() {
    null = 1;
    Integer integer = getTotalCount();
    null = null * 59 + ((integer == null) ? 43 : integer.hashCode());
    String str = getNextPageToken();
    null = null * 59 + ((str == null) ? 43 : str.hashCode());
    List<SearchResult> list = getResults();
    return null * 59 + ((list == null) ? 43 : list.hashCode());
  }
  
  public String toString() {
    return "Result(nextPageToken=" + getNextPageToken() + ", totalCount=" + getTotalCount() + ", results=" + String.valueOf(getResults()) + ")";
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\response\Result.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */