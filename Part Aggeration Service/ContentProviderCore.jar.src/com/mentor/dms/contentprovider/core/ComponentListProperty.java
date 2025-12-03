package com.mentor.dms.contentprovider.core;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ComponentListProperty extends ComponentProperty {
  private List<Map> list;
  
  public ComponentListProperty(String paramString, List<Map> paramList) {
    super(paramString, null);
    this.list = paramList;
  }
  
  public ComponentListProperty(String paramString1, String paramString2, List<Map> paramList) {
    super(paramString1, paramString2, (String)null, "");
    this.list = paramList;
  }
  
  public ComponentListProperty(String paramString, List<Map> paramList, boolean paramBoolean) {
    super(paramString, (String)null, "", paramBoolean);
    this.list = paramList;
  }
  
  public ComponentListProperty(String paramString1, String paramString2, List<Map> paramList, boolean paramBoolean) {
    super(paramString1, paramString2, null, "", paramBoolean);
    this.list = paramList;
  }
  
  public List<ListPropertyRow> getList() {
    ArrayList<ListPropertyRow> arrayList = new ArrayList();
    for (Map map : this.list)
      arrayList.add(new ListPropertyRow(map)); 
    return arrayList;
  }
  
  public String getValue() {
    return (new Gson()).toJson(this.list);
  }
  
  public boolean isListProperty() {
    return true;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\ComponentListProperty.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */