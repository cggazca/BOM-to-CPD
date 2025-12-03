package com.mentor.dms.contentprovider.core.plugin;

import java.util.HashMap;

public class ViewCompareDataForExport {
  private String[][] viewData = null;
  
  private HashMap<String, String[][]> listPropTable;
  
  public ViewCompareDataForExport(int paramInt1, int paramInt2) {
    this.viewData = new String[paramInt1][paramInt2];
    this.listPropTable = (HashMap)new HashMap<>();
  }
  
  public void set(int paramInt, String[] paramArrayOfString) {
    this.viewData[paramInt] = paramArrayOfString;
  }
  
  public String[][] get() {
    return this.viewData;
  }
  
  public void addListProperty(String paramString, String[][] paramArrayOfString) {
    this.listPropTable.put(paramString, paramArrayOfString);
  }
  
  public HashMap<String, String[][]> getListPropTable() {
    return this.listPropTable;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\plugin\ViewCompareDataForExport.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */