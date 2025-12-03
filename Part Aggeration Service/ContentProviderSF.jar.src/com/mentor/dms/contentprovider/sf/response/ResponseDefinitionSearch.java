package com.mentor.dms.contentprovider.sf.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseDefinitionSearch {
  @SerializedName("result")
  @Expose
  private SearchDefinitionResult result;
  
  @SerializedName("error")
  @Expose
  private ErrorObject error;
  
  @SerializedName("success")
  @Expose
  private Boolean success;
  
  public SearchDefinitionResult getResult() {
    return this.result;
  }
  
  public ErrorObject getError() {
    return this.error;
  }
  
  public Boolean getSuccess() {
    return this.success;
  }
  
  public void setResult(SearchDefinitionResult paramSearchDefinitionResult) {
    this.result = paramSearchDefinitionResult;
  }
  
  public void setError(ErrorObject paramErrorObject) {
    this.error = paramErrorObject;
  }
  
  public void setSuccess(Boolean paramBoolean) {
    this.success = paramBoolean;
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (!(paramObject instanceof ResponseDefinitionSearch))
      return false; 
    ResponseDefinitionSearch responseDefinitionSearch = (ResponseDefinitionSearch)paramObject;
    if (!responseDefinitionSearch.canEqual(this))
      return false; 
    Boolean bool1 = getSuccess();
    Boolean bool2 = responseDefinitionSearch.getSuccess();
    if ((bool1 == null) ? (bool2 != null) : !bool1.equals(bool2))
      return false; 
    SearchDefinitionResult searchDefinitionResult1 = getResult();
    SearchDefinitionResult searchDefinitionResult2 = responseDefinitionSearch.getResult();
    if ((searchDefinitionResult1 == null) ? (searchDefinitionResult2 != null) : !searchDefinitionResult1.equals(searchDefinitionResult2))
      return false; 
    ErrorObject errorObject1 = getError();
    ErrorObject errorObject2 = responseDefinitionSearch.getError();
    return !((errorObject1 == null) ? (errorObject2 != null) : !errorObject1.equals(errorObject2));
  }
  
  protected boolean canEqual(Object paramObject) {
    return paramObject instanceof ResponseDefinitionSearch;
  }
  
  public int hashCode() {
    null = 1;
    Boolean bool = getSuccess();
    null = null * 59 + ((bool == null) ? 43 : bool.hashCode());
    SearchDefinitionResult searchDefinitionResult = getResult();
    null = null * 59 + ((searchDefinitionResult == null) ? 43 : searchDefinitionResult.hashCode());
    ErrorObject errorObject = getError();
    return null * 59 + ((errorObject == null) ? 43 : errorObject.hashCode());
  }
  
  public String toString() {
    return "ResponseDefinitionSearch(result=" + String.valueOf(getResult()) + ", error=" + String.valueOf(getError()) + ", success=" + getSuccess() + ")";
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\response\ResponseDefinitionSearch.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */