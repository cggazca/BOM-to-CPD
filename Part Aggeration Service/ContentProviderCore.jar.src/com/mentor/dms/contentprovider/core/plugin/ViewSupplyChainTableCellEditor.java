package com.mentor.dms.contentprovider.core.plugin;

import java.awt.Component;
import javax.swing.AbstractCellEditor;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

public class ViewSupplyChainTableCellEditor extends AbstractCellEditor implements TableCellEditor {
  protected JComponent component = null;
  
  public Object getCellEditorValue() {
    return this.component;
  }
  
  public Component getTableCellEditorComponent(JTable paramJTable, Object paramObject, boolean paramBoolean, int paramInt1, int paramInt2) {
    this.component = (JComponent)paramObject;
    return this.component;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\plugin\ViewSupplyChainTableCellEditor.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */