package com.mentor.dms.contentprovider;

import java.util.HashMap;

public class ContentProviderSubscribedAML {
  private HashMap<String, String> idPropMap = new HashMap<>();
  
  private String mpn;
  
  private String mfg;
  
  private String customerMPN;
  
  private String customerMfg;
  
  public void addIDProperty(String paramString1, String paramString2) {
    this.idPropMap.put(paramString1, paramString2);
  }
  
  public String getIDProperty(String paramString) {
    return this.idPropMap.get(paramString);
  }
  
  public String getPartNumber() {
    return this.mpn;
  }
  
  public void setPartNumber(String paramString) {
    this.mpn = paramString;
  }
  
  public String getManufacturer() {
    return this.mfg;
  }
  
  public void setManufacturer(String paramString) {
    this.mfg = paramString;
  }
  
  public String getCustomerPartNumber() {
    return this.customerMPN;
  }
  
  public void setCustomerPartNumber(String paramString) {
    this.customerMPN = paramString;
  }
  
  public String getCustomerManufacturer() {
    return this.customerMfg;
  }
  
  public void setCustomerManufacturer(String paramString) {
    this.customerMfg = paramString;
  }
  
  public HashMap<String, String> getIdPropMap() {
    return this.idPropMap;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\ContentProviderSubscribedAML.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */