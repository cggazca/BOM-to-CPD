package com.mentor.dms.contentprovider.core.config;

import java.util.ArrayList;

public class DuplicateList {
  private ArrayList<String> partClassIDList = new ArrayList<>();
  
  private ArrayList<String> mpnDMNList = new ArrayList<>();
  
  private ArrayList<String> compDMNList = new ArrayList<>();
  
  public ArrayList<String> getPartClassIDList() {
    return this.partClassIDList;
  }
  
  public ArrayList<String> getMpnDMNList() {
    return this.mpnDMNList;
  }
  
  public ArrayList<String> getCompDMNList() {
    return this.compDMNList;
  }
  
  void setPartClassIDList(ArrayList<String> paramArrayList) {
    this.partClassIDList = paramArrayList;
  }
  
  void addPartClassIDList(String paramString) {
    this.partClassIDList.add(paramString);
  }
  
  void addMpnDMNListList(String paramString) {
    this.mpnDMNList.add(paramString);
  }
  
  void addCompDMNList(String paramString) {
    this.compDMNList.add(paramString);
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\config\DuplicateList.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */