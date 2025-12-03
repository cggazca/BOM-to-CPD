package com.mentor.dms.contentprovider.sf.responsePartByID;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FailedRecord {
  @SerializedName("code")
  @Expose
  private String code;
  
  @SerializedName("message")
  @Expose
  private String message;
  
  public String getCode() {
    return this.code;
  }
  
  public String getMessage() {
    return this.message;
  }
  
  public void setCode(String paramString) {
    this.code = paramString;
  }
  
  public void setMessage(String paramString) {
    this.message = paramString;
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (!(paramObject instanceof FailedRecord))
      return false; 
    FailedRecord failedRecord = (FailedRecord)paramObject;
    if (!failedRecord.canEqual(this))
      return false; 
    String str1 = getCode();
    String str2 = failedRecord.getCode();
    if ((str1 == null) ? (str2 != null) : !str1.equals(str2))
      return false; 
    String str3 = getMessage();
    String str4 = failedRecord.getMessage();
    return !((str3 == null) ? (str4 != null) : !str3.equals(str4));
  }
  
  protected boolean canEqual(Object paramObject) {
    return paramObject instanceof FailedRecord;
  }
  
  public int hashCode() {
    null = 1;
    String str1 = getCode();
    null = null * 59 + ((str1 == null) ? 43 : str1.hashCode());
    String str2 = getMessage();
    return null * 59 + ((str2 == null) ? 43 : str2.hashCode());
  }
  
  public String toString() {
    return "FailedRecord(code=" + getCode() + ", message=" + getMessage() + ")";
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\responsePartByID\FailedRecord.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */