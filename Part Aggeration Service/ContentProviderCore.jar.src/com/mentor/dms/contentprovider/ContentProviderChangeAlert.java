package com.mentor.dms.contentprovider;

import java.util.Date;

public class ContentProviderChangeAlert extends AbstractContentProviderAlert {
  private String notificationNumber = "";
  
  private String notificationDescription = "";
  
  private String notificationType = "";
  
  private Date issueDate;
  
  private Date implementationDate;
  
  private String code = "";
  
  private String informationSource = "";
  
  private ContentProviderDocumentList attachedDocuments = new ContentProviderDocumentList();
  
  public String getNotificationNumber() {
    return this.notificationNumber;
  }
  
  public void setNotificationNumber(String paramString) {
    this.notificationNumber = paramString;
  }
  
  public String getNotificationDescription() {
    return this.notificationDescription;
  }
  
  public void setNotificationDescription(String paramString) {
    this.notificationDescription = paramString;
  }
  
  public String getNotificationType() {
    return this.notificationType;
  }
  
  public void setNotificationType(String paramString) {
    this.notificationType = paramString;
  }
  
  public Date getIssueDate() {
    return this.issueDate;
  }
  
  public void setIssueDate(Date paramDate) {
    this.issueDate = paramDate;
  }
  
  public Date getImplementationDate() {
    return this.implementationDate;
  }
  
  public void setImplementationDate(Date paramDate) {
    this.implementationDate = paramDate;
  }
  
  public String getCode() {
    return this.code;
  }
  
  public void setCode(String paramString) {
    this.code = paramString;
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


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\ContentProviderChangeAlert.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */