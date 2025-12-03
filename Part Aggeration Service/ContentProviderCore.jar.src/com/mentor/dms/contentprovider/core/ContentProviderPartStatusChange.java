package com.mentor.dms.contentprovider.core;

import java.util.Date;

public class ContentProviderPartStatusChange extends AbstractContentProviderAlert {
  private String notificationDescription;
  
  private String notificationType;
  
  private String informationSource;
  
  private Date issueDate;
  
  private Date modifiedDate;
  
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
  
  public Date getModifiedDate() {
    return this.modifiedDate;
  }
  
  public void setModifiedDate(Date paramDate) {
    this.modifiedDate = paramDate;
  }
  
  public String getInformationSource() {
    return this.informationSource;
  }
  
  public void setInformationSource(String paramString) {
    this.informationSource = paramString;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\ContentProviderPartStatusChange.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */