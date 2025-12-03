package com.mentor.dms.contentprovider.core.plugin;

import java.awt.Component;
import java.util.EventObject;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;

public class PushButtonCellEditor implements TableCellEditor {
  public Component getTableCellEditorComponent(JTable paramJTable, Object paramObject, boolean paramBoolean, int paramInt1, int paramInt2) {
    return (Component)paramObject;
  }
  
  public void addCellEditorListener(CellEditorListener paramCellEditorListener) {}
  
  public void cancelCellEditing() {}
  
  public Object getCellEditorValue() {
    return null;
  }
  
  public boolean isCellEditable(EventObject paramEventObject) {
    return true;
  }
  
  public void removeCellEditorListener(CellEditorListener paramCellEditorListener) {}
  
  public boolean shouldSelectCell(EventObject paramEventObject) {
    return false;
  }
  
  public boolean stopCellEditing() {
    return true;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\plugin\PushButtonCellEditor.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */