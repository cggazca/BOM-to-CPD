package com.mentor.dms.contentprovider.core;

public class ContentProviderCredentialDefinition {
  private String id;
  
  private String label;
  
  private boolean bRequired;
  
  private boolean bIsPassword;
  
  public ContentProviderCredentialDefinition(String paramString1, String paramString2, boolean paramBoolean1, boolean paramBoolean2) {
    this.id = paramString1;
    this.label = paramString2;
    this.bRequired = paramBoolean1;
    this.bIsPassword = paramBoolean2;
  }
  
  public ContentProviderCredentialDefinition(String paramString1, String paramString2, boolean paramBoolean) {
    this(paramString1, paramString2, paramBoolean, false);
  }
  
  public String getId() {
    return this.id;
  }
  
  public String getLabel() {
    return this.label;
  }
  
  public boolean isRequired() {
    return this.bRequired;
  }
  
  public boolean isPassword() {
    return this.bIsPassword;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\ContentProviderCredentialDefinition.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */