package com.mentor.dms.contentprovider.plugin;

import java.awt.Component;
import javax.swing.JComponent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.HighlighterFactory;

public class ContentProviderViewCustomTableJXTable extends JXTable {
  public ContentProviderViewCustomTableJXTable(DefaultTableModel paramDefaultTableModel) {
    super(paramDefaultTableModel);
    setRowSelectionAllowed(true);
    setColumnSelectionAllowed(true);
    setCellSelectionEnabled(true);
    addHighlighter(HighlighterFactory.createAlternateStriping());
  }
  
  public Component prepareRenderer(TableCellRenderer paramTableCellRenderer, int paramInt1, int paramInt2) {
    Component component = super.prepareRenderer(paramTableCellRenderer, paramInt1, paramInt2);
    JComponent jComponent = (JComponent)component;
    jComponent.setToolTipText(getValueAt(0, paramInt2).toString());
    return component;
  }
  
  public TableCellRenderer getCellRenderer(int paramInt1, int paramInt2) {
    Object object = getValueAt(paramInt1, paramInt2);
    return (object instanceof String) ? new JTextAreaCellRenderer() : ((object instanceof AbstractPushButtonCell) ? new CustomCellRenderer() : super.getCellRenderer(paramInt1, paramInt2));
  }
  
  public TableCellEditor getCellEditor(int paramInt1, int paramInt2) {
    Object object = getValueAt(paramInt1, paramInt2);
    return (object instanceof String) ? new JTextAreaCellEditor() : ((object instanceof AbstractPushButtonCell) ? new PushButtonCellEditor() : super.getCellEditor(paramInt1, paramInt2));
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\plugin\ContentProviderViewCustomTableJXTable.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */