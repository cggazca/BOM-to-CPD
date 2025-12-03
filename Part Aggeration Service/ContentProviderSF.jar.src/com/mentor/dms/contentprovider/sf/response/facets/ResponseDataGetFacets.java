package com.mentor.dms.contentprovider.sf.response.facets;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseDataGetFacets {
  @SerializedName("result")
  @Expose
  private Result result;
  
  @SerializedName("error")
  @Expose
  private Error error;
  
  @SerializedName("success")
  @Expose
  private Boolean success;
  
  public Result getResult() {
    return this.result;
  }
  
  public Error getError() {
    return this.error;
  }
  
  public Boolean getSuccess() {
    return this.success;
  }
  
  public void setResult(Result paramResult) {
    this.result = paramResult;
  }
  
  public void setError(Error paramError) {
    this.error = paramError;
  }
  
  public void setSuccess(Boolean paramBoolean) {
    this.success = paramBoolean;
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (!(paramObject instanceof ResponseDataGetFacets))
      return false; 
    ResponseDataGetFacets responseDataGetFacets = (ResponseDataGetFacets)paramObject;
    if (!responseDataGetFacets.canEqual(this))
      return false; 
    Boolean bool1 = getSuccess();
    Boolean bool2 = responseDataGetFacets.getSuccess();
    if ((bool1 == null) ? (bool2 != null) : !bool1.equals(bool2))
      return false; 
    Result result1 = getResult();
    Result result2 = responseDataGetFacets.getResult();
    if ((result1 == null) ? (result2 != null) : !result1.equals(result2))
      return false; 
    Error error1 = getError();
    Error error2 = responseDataGetFacets.getError();
    return !((error1 == null) ? (error2 != null) : !error1.equals(error2));
  }
  
  protected boolean canEqual(Object paramObject) {
    return paramObject instanceof ResponseDataGetFacets;
  }
  
  public int hashCode() {
    null = 1;
    Boolean bool = getSuccess();
    null = null * 59 + ((bool == null) ? 43 : bool.hashCode());
    Result result = getResult();
    null = null * 59 + ((result == null) ? 43 : result.hashCode());
    Error error = getError();
    return null * 59 + ((error == null) ? 43 : error.hashCode());
  }
  
  public String toString() {
    return "ResponseDataGetFacets(result=" + String.valueOf(getResult()) + ", error=" + String.valueOf(getError()) + ", success=" + getSuccess() + ")";
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\response\facets\ResponseDataGetFacets.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */