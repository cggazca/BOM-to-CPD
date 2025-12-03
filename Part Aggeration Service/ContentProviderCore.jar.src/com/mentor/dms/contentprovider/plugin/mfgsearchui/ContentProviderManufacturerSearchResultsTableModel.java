package com.mentor.dms.contentprovider.plugin.mfgsearchui;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

public class ContentProviderManufacturerSearchResultsTableModel extends AbstractTableModel {
  private ArrayList<String> mfgList = new ArrayList<>();
  
  public void clearManufacturerList() {
    this.mfgList.clear();
  }
  
  public String getManufacturerAt(int paramInt) {
    return this.mfgList.get(paramInt);
  }
  
  public void addManufacturer(String paramString) {
    this.mfgList.add(paramString);
  }
  
  public int getColumnCount() {
    return 1;
  }
  
  public String getColumnName(int paramInt) {
    return "Manufacturer Name";
  }
  
  public Class<? extends Object> getColumnClass(int paramInt) {
    return (Class)String.class;
  }
  
  public int getRowCount() {
    return this.mfgList.size();
  }
  
  public Object getValueAt(int paramInt1, int paramInt2) {
    return getManufacturerAt(paramInt1);
  }
  
  public boolean isCellEditable(int paramInt1, int paramInt2) {
    return (paramInt2 == 0);
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\plugin\mfgsearchui\ContentProviderManufacturerSearchResultsTableModel.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */