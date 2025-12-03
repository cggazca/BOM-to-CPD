package com.mentor.dms.contentprovider.utils;

import java.util.Collection;
import java.util.HashMap;

class EDMCompData {
  private String cpn;
  
  private HashMap<String, EDMMPNData> mpnDataMap = new HashMap<>();
  
  public EDMCompData(String paramString) {
    this.cpn = paramString;
  }
  
  public String getPartNumber() {
    return this.cpn;
  }
  
  public EDMMPNData getMPN(String paramString) {
    return this.mpnDataMap.get(paramString);
  }
  
  public void addMPNRecord(String paramString1, String paramString2, String paramString3) {
    EDMMPNData eDMMPNData = this.mpnDataMap.get(paramString1);
    if (eDMMPNData == null) {
      eDMMPNData = new EDMMPNData(paramString1);
      this.mpnDataMap.put(paramString1, eDMMPNData);
    } 
    eDMMPNData.addIDPropRecord(paramString2, paramString3);
  }
  
  public Collection<EDMMPNData> getMPNs() {
    return this.mpnDataMap.values();
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovide\\utils\EDMCompData.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */