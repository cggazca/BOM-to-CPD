package com.mentor.dms.contentprovider.utils;

import java.util.HashMap;

class EDMMPNData {
  private String mpn;
  
  private HashMap<String, String> idPropMap = new HashMap<>();
  
  public EDMMPNData(String paramString) {
    this.mpn = paramString;
  }
  
  public String getMPN() {
    return this.mpn;
  }
  
  public void addIDPropRecord(String paramString1, String paramString2) {
    this.idPropMap.put(paramString1, paramString2);
  }
  
  public String getIDPropValue(String paramString) {
    return this.idPropMap.get(paramString);
  }
  
  public HashMap<String, String> getIdPropMap() {
    return this.idPropMap;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovide\\utils\EDMMPNData.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */