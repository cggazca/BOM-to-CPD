package com.mentor.dms.contentprovider.sf.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ErrorObject {
  @SerializedName("code")
  @Expose
  private String code;
  
  @SerializedName("message")
  @Expose
  private String message;
  
  @SerializedName("causes")
  @Expose
  private List<ErrorObject> causes;
  
  public String getCode() {
    return this.code;
  }
  
  public String getMessage() {
    return this.message;
  }
  
  public List<ErrorObject> getCauses() {
    return this.causes;
  }
  
  public void setCode(String paramString) {
    this.code = paramString;
  }
  
  public void setMessage(String paramString) {
    this.message = paramString;
  }
  
  public void setCauses(List<ErrorObject> paramList) {
    this.causes = paramList;
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (!(paramObject instanceof ErrorObject))
      return false; 
    ErrorObject errorObject = (ErrorObject)paramObject;
    if (!errorObject.canEqual(this))
      return false; 
    String str1 = getCode();
    String str2 = errorObject.getCode();
    if ((str1 == null) ? (str2 != null) : !str1.equals(str2))
      return false; 
    String str3 = getMessage();
    String str4 = errorObject.getMessage();
    if ((str3 == null) ? (str4 != null) : !str3.equals(str4))
      return false; 
    List<ErrorObject> list1 = getCauses();
    List<ErrorObject> list2 = errorObject.getCauses();
    return !((list1 == null) ? (list2 != null) : !list1.equals(list2));
  }
  
  protected boolean canEqual(Object paramObject) {
    return paramObject instanceof ErrorObject;
  }
  
  public int hashCode() {
    null = 1;
    String str1 = getCode();
    null = null * 59 + ((str1 == null) ? 43 : str1.hashCode());
    String str2 = getMessage();
    null = null * 59 + ((str2 == null) ? 43 : str2.hashCode());
    List<ErrorObject> list = getCauses();
    return null * 59 + ((list == null) ? 43 : list.hashCode());
  }
  
  public String toString() {
    return "ErrorObject(code=" + getCode() + ", message=" + getMessage() + ", causes=" + String.valueOf(getCauses()) + ")";
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\response\ErrorObject.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */