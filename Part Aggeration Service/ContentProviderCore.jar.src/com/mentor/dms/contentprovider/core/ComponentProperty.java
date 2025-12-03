package com.mentor.dms.contentprovider.core;

public class ComponentProperty {
  private String id;
  
  private String label;
  
  private String value;
  
  private String uom;
  
  private boolean bVisibleInViewCompare = true;
  
  private boolean bIsDocumentURL = false;
  
  public ComponentProperty(String paramString1, String paramString2, String paramString3) {
    this.id = paramString1;
    this.label = paramString1;
    this.value = paramString2;
    this.uom = paramString3;
  }
  
  public ComponentProperty(String paramString1, String paramString2, String paramString3, String paramString4) {
    this.id = paramString1;
    this.label = paramString2;
    this.value = paramString3;
    this.uom = paramString4;
  }
  
  public ComponentProperty(String paramString1, String paramString2, String paramString3, boolean paramBoolean) {
    this.id = paramString1;
    this.label = paramString1;
    this.value = paramString2;
    this.uom = paramString3;
    this.bVisibleInViewCompare = paramBoolean;
  }
  
  public ComponentProperty(String paramString1, String paramString2, String paramString3, String paramString4, boolean paramBoolean) {
    this.id = paramString1;
    this.label = paramString2;
    this.value = paramString3;
    this.uom = paramString4;
    this.bVisibleInViewCompare = paramBoolean;
  }
  
  public ComponentProperty(String paramString1, String paramString2) {
    this(paramString1, paramString2, "");
  }
  
  public ComponentProperty(String paramString1, String paramString2, boolean paramBoolean) {
    this(paramString1, paramString2, "", paramBoolean);
  }
  
  public void setDocumentURL(boolean paramBoolean) {
    this.bIsDocumentURL = paramBoolean;
  }
  
  public String getId() {
    return this.id;
  }
  
  public String getLabel() {
    return this.label;
  }
  
  public String getValue() {
    return this.value;
  }
  
  public String getValueWithOUM() {
    String str = this.value;
    if (!str.isEmpty())
      str = str + str; 
    return str;
  }
  
  public String getOUM() {
    return this.uom;
  }
  
  public boolean isVisibleInViewCompare() {
    return this.bVisibleInViewCompare;
  }
  
  public boolean isDocumentURL() {
    return (this.value.startsWith("http://") || this.value.startsWith("https://")) ? true : this.bIsDocumentURL;
  }
  
  public boolean isListProperty() {
    return false;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\ComponentProperty.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */