package com.mentor.dms.contentprovider.config;

import com.mentor.dms.contentprovider.config.xml.ConfigXMLProperty;

public class ContentProviderConfigProperty implements Comparable<ContentProviderConfigProperty> {
  private ContentProviderConfigPartClass partClass = null;
  
  private boolean bSearchable = false;
  
  private boolean bIdProperty = false;
  
  private String contentProviderId;
  
  private String contentProviderLabel = null;
  
  private String baseUnits = "";
  
  private boolean bInherit = true;
  
  public ContentProviderConfigProperty(ContentProviderConfigPartClass paramContentProviderConfigPartClass) {
    this.partClass = paramContentProviderConfigPartClass;
  }
  
  public ContentProviderConfigPartClass getPartClass() {
    return this.partClass;
  }
  
  public boolean isSearchable() {
    return this.bSearchable;
  }
  
  public boolean isIdProperty() {
    return this.bIdProperty;
  }
  
  public String getContentProviderId() {
    return this.contentProviderId;
  }
  
  public String getBaseUnits() {
    return this.baseUnits;
  }
  
  public boolean isInherit() {
    return this.bInherit;
  }
  
  public void read(ConfigXMLProperty paramConfigXMLProperty) throws ContentProviderConfigException {
    this.contentProviderId = paramConfigXMLProperty.getId();
    this.contentProviderLabel = paramConfigXMLProperty.getLabel();
    if (paramConfigXMLProperty.isSearchable() != null)
      this.bSearchable = paramConfigXMLProperty.isSearchable().booleanValue(); 
    if (paramConfigXMLProperty.isIdProperty() != null)
      this.bIdProperty = paramConfigXMLProperty.isIdProperty().booleanValue(); 
    if (paramConfigXMLProperty.getBaseUnits() != null)
      this.baseUnits = paramConfigXMLProperty.getBaseUnits(); 
    this.bInherit = this.partClass.getContentProviderConfig().isDefaultInherit();
    if (paramConfigXMLProperty.isInherit() != null)
      this.bInherit = paramConfigXMLProperty.isInherit().booleanValue(); 
  }
  
  public int compareTo(ContentProviderConfigProperty paramContentProviderConfigProperty) {
    return getContentProviderId().compareTo(paramContentProviderConfigProperty.getContentProviderId());
  }
  
  public String getContentProviderLabel() {
    return (this.contentProviderLabel != null && !this.contentProviderLabel.isBlank()) ? this.contentProviderLabel : this.contentProviderId;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\config\ContentProviderConfigProperty.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */