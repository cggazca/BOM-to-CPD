package com.mentor.dms.contentprovider.core.plugin;

import com.mentor.dms.contentprovider.core.IContentProviderCustomTable;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

public class ContentProviderViewCustomTableTableModel extends DefaultTableModel {
  private IContentProviderCustomTable customTable = null;
  
  public ContentProviderViewCustomTableTableModel(IContentProviderCustomTable paramIContentProviderCustomTable) {
    this.customTable = paramIContentProviderCustomTable;
  }
  
  public Class<?> getColumnClass(int paramInt) {
    return this.customTable.getColumnClass(paramInt);
  }
  
  public int getColumnCount() {
    return this.customTable.getColumnCount();
  }
  
  public String getColumnName(int paramInt) {
    return this.customTable.getColumnName(paramInt);
  }
  
  public int getRowCount() {
    return this.customTable.getRowCount();
  }
  
  public Object getValueAt(int paramInt1, int paramInt2) {
    return this.customTable.getValueAt(paramInt1, paramInt2);
  }
  
  public void setValueAt(Object paramObject, int paramInt1, int paramInt2) {}
  
  public boolean isCellEditable(int paramInt1, int paramInt2) {
    Object object = getValueAt(paramInt1, paramInt2);
    return (object instanceof AbstractPushButtonCell || object instanceof String);
  }
  
  public void setDataVector(Vector paramVector1, Vector paramVector2) {}
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\plugin\ContentProviderViewCustomTableTableModel.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */