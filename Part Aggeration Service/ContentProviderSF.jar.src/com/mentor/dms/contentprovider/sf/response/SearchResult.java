package com.mentor.dms.contentprovider.sf.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchResult {
  @SerializedName("searchProviderPart")
  @Expose
  private SearchProviderPart searchProviderPart;
  
  @SerializedName("enrichingResult")
  @Expose
  private EnrichingResult enrichingResult;
  
  public SearchProviderPart getSearchProviderPart() {
    return this.searchProviderPart;
  }
  
  public EnrichingResult getEnrichingResult() {
    return this.enrichingResult;
  }
  
  public void setSearchProviderPart(SearchProviderPart paramSearchProviderPart) {
    this.searchProviderPart = paramSearchProviderPart;
  }
  
  public void setEnrichingResult(EnrichingResult paramEnrichingResult) {
    this.enrichingResult = paramEnrichingResult;
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (!(paramObject instanceof SearchResult))
      return false; 
    SearchResult searchResult = (SearchResult)paramObject;
    if (!searchResult.canEqual(this))
      return false; 
    SearchProviderPart searchProviderPart1 = getSearchProviderPart();
    SearchProviderPart searchProviderPart2 = searchResult.getSearchProviderPart();
    if ((searchProviderPart1 == null) ? (searchProviderPart2 != null) : !searchProviderPart1.equals(searchProviderPart2))
      return false; 
    EnrichingResult enrichingResult1 = getEnrichingResult();
    EnrichingResult enrichingResult2 = searchResult.getEnrichingResult();
    return !((enrichingResult1 == null) ? (enrichingResult2 != null) : !enrichingResult1.equals(enrichingResult2));
  }
  
  protected boolean canEqual(Object paramObject) {
    return paramObject instanceof SearchResult;
  }
  
  public int hashCode() {
    null = 1;
    SearchProviderPart searchProviderPart = getSearchProviderPart();
    null = null * 59 + ((searchProviderPart == null) ? 43 : searchProviderPart.hashCode());
    EnrichingResult enrichingResult = getEnrichingResult();
    return null * 59 + ((enrichingResult == null) ? 43 : enrichingResult.hashCode());
  }
  
  public String toString() {
    return "SearchResult(searchProviderPart=" + String.valueOf(getSearchProviderPart()) + ", enrichingResult=" + String.valueOf(getEnrichingResult()) + ")";
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\response\SearchResult.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */