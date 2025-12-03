package com.mentor.dms.contentprovider.core;

import java.util.Date;

public class ContentProviderEndOfLifeAlert extends AbstractContentProviderAlert {
  private Date lastTimeBuyDate;
  
  private Date lastTimeDeliveryDate;
  
  private String lifeCycleInformationSource;
  
  private ContentProviderDocumentList attachedDocuments = new ContentProviderDocumentList();
  
  public Date getLastTimeBuyDate() {
    return this.lastTimeBuyDate;
  }
  
  public void setLastTimeBuyDate(Date paramDate) {
    this.lastTimeBuyDate = paramDate;
  }
  
  public Date getLastTimeDeliveryDate() {
    return this.lastTimeDeliveryDate;
  }
  
  public void setLastTimeDeliveryDate(Date paramDate) {
    this.lastTimeDeliveryDate = paramDate;
  }
  
  public String getLifeCycleInformationSource() {
    return this.lifeCycleInformationSource;
  }
  
  public void setLifeCycleInformationSource(String paramString) {
    this.lifeCycleInformationSource = paramString;
  }
  
  public ContentProviderDocumentList getAttachedDocuments() {
    return this.attachedDocuments;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\ContentProviderEndOfLifeAlert.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */