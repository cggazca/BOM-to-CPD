package com.mentor.dms.contentprovider.core;

import com.mentor.dms.contentprovider.core.plugin.ViewDocumentCell;
import javax.swing.Icon;

public interface IContentProviderCustomTable {
  String getCategoryLabel();
  
  String getViewLabel();
  
  Icon getIcon();
  
  int getWidth();
  
  int getHeight();
  
  Class<?> getColumnClass(int paramInt);
  
  int getColumnCount();
  
  String getColumnName(int paramInt);
  
  int getRowCount();
  
  Object getValueAt(int paramInt1, int paramInt2);
  
  default ViewDocumentCell createViewDocumentCell(String paramString) {
    return new ViewDocumentCell(paramString);
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\IContentProviderCustomTable.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */