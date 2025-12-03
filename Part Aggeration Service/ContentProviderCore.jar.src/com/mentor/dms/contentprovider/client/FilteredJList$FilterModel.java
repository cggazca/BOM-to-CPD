package com.mentor.dms.contentprovider.client;

import java.util.ArrayList;
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
    for (byte b = 0; b < this.items.size(); b++) {
      if (this.items.get(b).toString().toUpperCase().indexOf(str, 0) != -1)
        this.filterItems.add(this.items.get(b)); 
    } 
    fireContentsChanged(this, 0, getSize());
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\client\FilteredJList$FilterModel.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */