package com.mentor.dms.contentprovider.sf.responsePartByID;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mentor.dms.contentprovider.sf.response.ErrorObject;

public class ResponseDataPartByID {
  @SerializedName("result")
  @Expose
  private Result result;
  
  @SerializedName("error")
  @Expose
  private ErrorObject error;
  
  @SerializedName("success")
  @Expose
  private Boolean success;
  
  public Result getResult() {
    return this.result;
  }
  
  public ErrorObject getError() {
    return this.error;
  }
  
  public Boolean getSuccess() {
    return this.success;
  }
  
  public void setResult(Result paramResult) {
    this.result = paramResult;
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
    if (!(paramObject instanceof ResponseDataPartByID))
      return false; 
    ResponseDataPartByID responseDataPartByID = (ResponseDataPartByID)paramObject;
    if (!responseDataPartByID.canEqual(this))
      return false; 
    Boolean bool1 = getSuccess();
    Boolean bool2 = responseDataPartByID.getSuccess();
    if ((bool1 == null) ? (bool2 != null) : !bool1.equals(bool2))
      return false; 
    Result result1 = getResult();
    Result result2 = responseDataPartByID.getResult();
    if ((result1 == null) ? (result2 != null) : !result1.equals(result2))
      return false; 
    ErrorObject errorObject1 = getError();
    ErrorObject errorObject2 = responseDataPartByID.getError();
    return !((errorObject1 == null) ? (errorObject2 != null) : !errorObject1.equals(errorObject2));
  }
  
  protected boolean canEqual(Object paramObject) {
    return paramObject instanceof ResponseDataPartByID;
  }
  
  public int hashCode() {
    null = 1;
    Boolean bool = getSuccess();
    null = null * 59 + ((bool == null) ? 43 : bool.hashCode());
    Result result = getResult();
    null = null * 59 + ((result == null) ? 43 : result.hashCode());
    ErrorObject errorObject = getError();
    return null * 59 + ((errorObject == null) ? 43 : errorObject.hashCode());
  }
  
  public String toString() {
    return "ResponseDataPartByID(result=" + String.valueOf(getResult()) + ", error=" + String.valueOf(getError()) + ", success=" + getSuccess() + ")";
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\responsePartByID\ResponseDataPartByID.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */