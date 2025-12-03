package com.mentor.dms.contentprovider.plugin;

import java.awt.Component;
import java.util.Date;
import javax.swing.JComponent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.HighlighterFactory;

public class ContentProviderViewDocumentsJXTable extends JXTable {
  public ContentProviderViewDocumentsJXTable(DefaultTableModel paramDefaultTableModel) {
    super(paramDefaultTableModel);
    setRowSelectionAllowed(false);
    setColumnSelectionAllowed(false);
    setCellSelectionEnabled(true);
    setSelectionMode(2);
    addHighlighter(HighlighterFactory.createAlternateStriping());
  }
  
  public TableCellRenderer getCellRenderer(int paramInt1, int paramInt2) {
    Object object = getValueAt(paramInt1, paramInt2);
    return (object instanceof com.mentor.dms.contentprovider.ContentProviderDocument) ? new ViewAttachmentButtonCellRenderer() : super.getCellRenderer(paramInt1, paramInt2);
  }
  
  public TableCellEditor getCellEditor(int paramInt1, int paramInt2) {
    Object object = getValueAt(paramInt1, paramInt2);
    return (object instanceof com.mentor.dms.contentprovider.ContentProviderDocument) ? new ViewAttachmentButtonCellEditor() : super.getCellEditor(paramInt1, paramInt2);
  }
  
  public Component prepareRenderer(TableCellRenderer paramTableCellRenderer, int paramInt1, int paramInt2) {
    Component component = super.prepareRenderer(paramTableCellRenderer, paramInt1, paramInt2);
    Object object = getValueAt(paramInt1, paramInt2);
    JComponent jComponent = (JComponent)component;
    if (object instanceof com.mentor.dms.contentprovider.ContentProviderDocument) {
      jComponent.setToolTipText("View attachment...");
    } else if (object instanceof String) {
      jComponent.setToolTipText((String)object);
    } else if (object instanceof Date) {
      jComponent.setToolTipText(((Date)object).toString());
    } 
    return component;
  }
  
  public boolean isCellSelected(int paramInt1, int paramInt2) {
    return (paramInt2 == 0) ? false : super.isCellSelected(paramInt1, paramInt2);
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\plugin\ContentProviderViewDocumentsJXTable.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */