package com.mentor.dms.contentprovider.core;

import java.util.Date;
import org.apache.commons.codec.digest.DigestUtils;

public class ContentProviderDocument {
  private String objectId;
  
  private String documentTitle;
  
  private String URL;
  
  private String documentType;
  
  private Date publicationDate;
  
  public String getObjectId() {
    if (this.objectId == null) {
      String str1 = "<NULL>";
      if (this.publicationDate != null)
        str1 = "" + this.publicationDate.getTime(); 
      String str2 = this.documentTitle + this.documentTitle + this.URL + this.documentType;
      this.objectId = DigestUtils.md5Hex(str2);
    } 
    return this.objectId;
  }
  
  public void setObjectId(String paramString) {
    this.objectId = paramString;
  }
  
  public String getTitle() {
    return this.documentTitle;
  }
  
  public void setTitle(String paramString) {
    this.documentTitle = paramString;
  }
  
  public String getURL() {
    return this.URL;
  }
  
  public void setURL(String paramString) {
    this.URL = paramString;
  }
  
  public String getType() {
    return this.documentType;
  }
  
  public void setType(String paramString) {
    this.documentType = paramString;
  }
  
  public Date getPublicationDate() {
    return this.publicationDate;
  }
  
  public void setPublicationDate(Date paramDate) {
    this.publicationDate = paramDate;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\ContentProviderDocument.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */