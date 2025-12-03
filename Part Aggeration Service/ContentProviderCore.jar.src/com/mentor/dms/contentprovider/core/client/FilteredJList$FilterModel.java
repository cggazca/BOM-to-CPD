package com.mentor.dms.contentprovider.core.client;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import javax.swing.AbstractListModel;

class FilterModel extends AbstractListModel {
  ArrayList items = new ArrayList();
  
  ArrayList filterItems = new ArrayList();
  
  public Object getElementAt(int paramInt) {
    return (paramInt < this.filterItems.size()) ? this.filterItems.get(paramInt) : null;
  }
  
  public int getSize() {
    return this.filterItems.size();
  }
  
  public void addElement(Object paramObject) {
    this.items.add(paramObject);
    refilter();
  }
  
  private void refilter() {
    this.filterItems.clear();
    String str = FilteredJList.this.getFilterField().getText().toUpperCase();
    for (String str1 : FilteredJList.this.valueMap.values()) {
      if (str1.toUpperCase().indexOf(str, 0) != -1) {
        String str2 = searchFromValue(FilteredJList.this.valueMap, str1);
        this.filterItems.add(str2);
      } 
    } 
    fireContentsChanged(this, 0, getSize());
  }
  
  public String searchFromValue(Map<String, String> paramMap, String paramString) {
    for (Map.Entry<String, String> entry : paramMap.entrySet()) {
      if (Objects.equals(paramString, entry.getValue()))
        return (String)entry.getKey(); 
    } 
    return null;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\client\FilteredJList$FilterModel.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */