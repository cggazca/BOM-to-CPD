package com.mentor.dms.contentprovider.core;

import java.util.Date;

public class ContentProviderFailureAlert extends AbstractContentProviderAlert {
  private String notificationNumber;
  
  private String problemDescription;
  
  private String plannedAction;
  
  private Date issueDate;
  
  private String serialNumber;
  
  private String informationSource;
  
  private ContentProviderDocumentList attachedDocuments = new ContentProviderDocumentList();
  
  public String getNotificationNumber() {
    return this.notificationNumber;
  }
  
  public void setNotificationNumber(String paramString) {
    this.notificationNumber = paramString;
  }
  
  public String getProblemDescription() {
    return this.problemDescription;
  }
  
  public void setProblemDescription(String paramString) {
    this.problemDescription = paramString;
  }
  
  public String getPlannedAction() {
    return this.plannedAction;
  }
  
  public void setPlannedAction(String paramString) {
    this.plannedAction = paramString;
  }
  
  public Date getIssueDate() {
    return this.issueDate;
  }
  
  public void setIssueDate(Date paramDate) {
    this.issueDate = paramDate;
  }
  
  public String getSerialNumber() {
    return this.serialNumber;
  }
  
  public void setSerialNumber(String paramString) {
    this.serialNumber = paramString;
  }
  
  public String getInformationSource() {
    return this.informationSource;
  }
  
  public void setInformationSource(String paramString) {
    this.informationSource = paramString;
  }
  
  public ContentProviderDocumentList getAttachedDocuments() {
    return this.attachedDocuments;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\ContentProviderFailureAlert.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */