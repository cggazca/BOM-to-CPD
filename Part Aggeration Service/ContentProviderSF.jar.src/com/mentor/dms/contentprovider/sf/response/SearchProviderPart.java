package com.mentor.dms.contentprovider.sf.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchProviderPart {
  @SerializedName("partId")
  @Expose
  private String partId;
  
  @SerializedName("searchProviderId")
  @Expose
  private String searchProviderId;
  
  @SerializedName("partClassId")
  @Expose
  private String partClassId;
  
  @SerializedName("manufacturerName")
  @Expose
  private String manufacturerName;
  
  @SerializedName("manufacturerPartNumber")
  @Expose
  private String manufacturerPartNumber;
  
  @SerializedName("properties")
  @Expose
  private PartProperties properties;
  
  public String getPartId() {
    return this.partId;
  }
  
  public String getSearchProviderId() {
    return this.searchProviderId;
  }
  
  public String getPartClassId() {
    return this.partClassId;
  }
  
  public String getManufacturerName() {
    return this.manufacturerName;
  }
  
  public String getManufacturerPartNumber() {
    return this.manufacturerPartNumber;
  }
  
  public PartProperties getProperties() {
    return this.properties;
  }
  
  public void setPartId(String paramString) {
    this.partId = paramString;
  }
  
  public void setSearchProviderId(String paramString) {
    this.searchProviderId = paramString;
  }
  
  public void setPartClassId(String paramString) {
    this.partClassId = paramString;
  }
  
  public void setManufacturerName(String paramString) {
    this.manufacturerName = paramString;
  }
  
  public void setManufacturerPartNumber(String paramString) {
    this.manufacturerPartNumber = paramString;
  }
  
  public void setProperties(PartProperties paramPartProperties) {
    this.properties = paramPartProperties;
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (!(paramObject instanceof SearchProviderPart))
      return false; 
    SearchProviderPart searchProviderPart = (SearchProviderPart)paramObject;
    if (!searchProviderPart.canEqual(this))
      return false; 
    String str1 = getPartId();
    String str2 = searchProviderPart.getPartId();
    if ((str1 == null) ? (str2 != null) : !str1.equals(str2))
      return false; 
    String str3 = getSearchProviderId();
    String str4 = searchProviderPart.getSearchProviderId();
    if ((str3 == null) ? (str4 != null) : !str3.equals(str4))
      return false; 
    String str5 = getPartClassId();
    String str6 = searchProviderPart.getPartClassId();
    if ((str5 == null) ? (str6 != null) : !str5.equals(str6))
      return false; 
    String str7 = getManufacturerName();
    String str8 = searchProviderPart.getManufacturerName();
    if ((str7 == null) ? (str8 != null) : !str7.equals(str8))
      return false; 
    String str9 = getManufacturerPartNumber();
    String str10 = searchProviderPart.getManufacturerPartNumber();
    if ((str9 == null) ? (str10 != null) : !str9.equals(str10))
      return false; 
    PartProperties partProperties1 = getProperties();
    PartProperties partProperties2 = searchProviderPart.getProperties();
    return !((partProperties1 == null) ? (partProperties2 != null) : !partProperties1.equals(partProperties2));
  }
  
  protected boolean canEqual(Object paramObject) {
    return paramObject instanceof SearchProviderPart;
  }
  
  public int hashCode() {
    null = 1;
    String str1 = getPartId();
    null = null * 59 + ((str1 == null) ? 43 : str1.hashCode());
    String str2 = getSearchProviderId();
    null = null * 59 + ((str2 == null) ? 43 : str2.hashCode());
    String str3 = getPartClassId();
    null = null * 59 + ((str3 == null) ? 43 : str3.hashCode());
    String str4 = getManufacturerName();
    null = null * 59 + ((str4 == null) ? 43 : str4.hashCode());
    String str5 = getManufacturerPartNumber();
    null = null * 59 + ((str5 == null) ? 43 : str5.hashCode());
    PartProperties partProperties = getProperties();
    return null * 59 + ((partProperties == null) ? 43 : partProperties.hashCode());
  }
  
  public String toString() {
    return "SearchProviderPart(partId=" + getPartId() + ", searchProviderId=" + getSearchProviderId() + ", partClassId=" + getPartClassId() + ", manufacturerName=" + getManufacturerName() + ", manufacturerPartNumber=" + getManufacturerPartNumber() + ", properties=" + String.valueOf(getProperties()) + ")";
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\response\SearchProviderPart.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */