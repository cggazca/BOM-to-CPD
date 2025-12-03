package com.mentor.dms.contentprovider.sf.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EnrichRecord {
  @SerializedName("properties")
  @Expose
  private EnrichProperties properties;
  
  public EnrichProperties getProperties() {
    return this.properties;
  }
  
  public void setProperties(EnrichProperties paramEnrichProperties) {
    this.properties = paramEnrichProperties;
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (!(paramObject instanceof EnrichRecord))
      return false; 
    EnrichRecord enrichRecord = (EnrichRecord)paramObject;
    if (!enrichRecord.canEqual(this))
      return false; 
    EnrichProperties enrichProperties1 = getProperties();
    EnrichProperties enrichProperties2 = enrichRecord.getProperties();
    return !((enrichProperties1 == null) ? (enrichProperties2 != null) : !enrichProperties1.equals(enrichProperties2));
  }
  
  protected boolean canEqual(Object paramObject) {
    return paramObject instanceof EnrichRecord;
  }
  
  public int hashCode() {
    null = 1;
    EnrichProperties enrichProperties = getProperties();
    return null * 59 + ((enrichProperties == null) ? 43 : enrichProperties.hashCode());
  }
  
  public String toString() {
    return "EnrichRecord(properties=" + String.valueOf(getProperties()) + ")";
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\response\EnrichRecord.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */