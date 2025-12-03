package com.mentor.dms.contentprovider.sf.response.facets;

import com.google.gson.annotations.Expose;

public class Result {
  @Expose
  private String searchProviderId;
  
  @Expose
  private String partClassId;
  
  @Expose
  private Facets facets = null;
  
  public String getSearchProviderId() {
    return this.searchProviderId;
  }
  
  public String getPartClassId() {
    return this.partClassId;
  }
  
  public Facets getFacets() {
    return this.facets;
  }
  
  public void setSearchProviderId(String paramString) {
    this.searchProviderId = paramString;
  }
  
  public void setPartClassId(String paramString) {
    this.partClassId = paramString;
  }
  
  public void setFacets(Facets paramFacets) {
    this.facets = paramFacets;
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (!(paramObject instanceof Result))
      return false; 
    Result result = (Result)paramObject;
    if (!result.canEqual(this))
      return false; 
    String str1 = getSearchProviderId();
    String str2 = result.getSearchProviderId();
    if ((str1 == null) ? (str2 != null) : !str1.equals(str2))
      return false; 
    String str3 = getPartClassId();
    String str4 = result.getPartClassId();
    if ((str3 == null) ? (str4 != null) : !str3.equals(str4))
      return false; 
    Facets facets1 = getFacets();
    Facets facets2 = result.getFacets();
    return !((facets1 == null) ? (facets2 != null) : !facets1.equals(facets2));
  }
  
  protected boolean canEqual(Object paramObject) {
    return paramObject instanceof Result;
  }
  
  public int hashCode() {
    null = 1;
    String str1 = getSearchProviderId();
    null = null * 59 + ((str1 == null) ? 43 : str1.hashCode());
    String str2 = getPartClassId();
    null = null * 59 + ((str2 == null) ? 43 : str2.hashCode());
    Facets facets = getFacets();
    return null * 59 + ((facets == null) ? 43 : facets.hashCode());
  }
  
  public String toString() {
    return "Result(searchProviderId=" + getSearchProviderId() + ", partClassId=" + getPartClassId() + ", facets=" + String.valueOf(getFacets()) + ")";
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\response\facets\Result.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */