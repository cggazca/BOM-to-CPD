package com.mentor.dms.contentprovider;

public abstract class AbstractContentProviderAlert {
  private String objectId;
  
  private String alertNumber;
  
  public String getObjectId() {
    return this.objectId;
  }
  
  public void setObjectId(String paramString) {
    this.objectId = paramString;
  }
  
  public String getAlertNumber() {
    return this.alertNumber;
  }
  
  public void setAlertNumber(String paramString) {
    this.alertNumber = paramString;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\AbstractContentProviderAlert.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */