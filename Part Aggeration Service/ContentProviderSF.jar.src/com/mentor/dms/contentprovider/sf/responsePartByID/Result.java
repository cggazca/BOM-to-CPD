package com.mentor.dms.contentprovider.sf.responsePartByID;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {
  @SerializedName("searchProviderId")
  @Expose
  private String searchProviderId;
  
  @SerializedName("parts")
  @Expose
  private Part parts = null;
  
  public String getSearchProviderId() {
    return this.searchProviderId;
  }
  
  public Part getParts() {
    return this.parts;
  }
  
  public void setSearchProviderId(String paramString) {
    this.searchProviderId = paramString;
  }
  
  public void setParts(Part paramPart) {
    this.parts = paramPart;
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
    Part part1 = getParts();
    Part part2 = result.getParts();
    return !((part1 == null) ? (part2 != null) : !part1.equals(part2));
  }
  
  protected boolean canEqual(Object paramObject) {
    return paramObject instanceof Result;
  }
  
  public int hashCode() {
    null = 1;
    String str = getSearchProviderId();
    null = null * 59 + ((str == null) ? 43 : str.hashCode());
    Part part = getParts();
    return null * 59 + ((part == null) ? 43 : part.hashCode());
  }
  
  public String toString() {
    return "Result(searchProviderId=" + getSearchProviderId() + ", parts=" + String.valueOf(getParts()) + ")";
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\responsePartByID\Result.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */