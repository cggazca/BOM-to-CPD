package com.mentor.dms.contentprovider.utils;

import java.util.Collection;
import java.util.HashMap;

class EDMCompList {
  private HashMap<String, EDMCompData> compDataMap = new HashMap<>();
  
  public void addComponentRecord(String paramString1, String paramString2, String paramString3, String paramString4) {
    EDMCompData eDMCompData = this.compDataMap.get(paramString1);
    if (eDMCompData == null) {
      eDMCompData = new EDMCompData(paramString1);
      this.compDataMap.put(paramString1, eDMCompData);
    } 
    eDMCompData.addMPNRecord(paramString2, paramString3, paramString4);
  }
  
  public EDMCompData getComponent(String paramString) {
    return this.compDataMap.get(paramString);
  }
  
  public Collection<EDMCompData> getComponents() {
    return this.compDataMap.values();
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovide\\utils\EDMCompList.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */