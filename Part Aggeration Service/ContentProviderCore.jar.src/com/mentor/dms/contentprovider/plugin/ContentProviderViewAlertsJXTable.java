package com.mentor.dms.contentprovider.plugin;

import java.awt.Component;
import java.awt.Dimension;
import java.util.Date;
import javax.swing.JComponent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.HighlighterFactory;

public class ContentProviderViewAlertsJXTable extends JXTable {
  public ContentProviderViewAlertsJXTable(DefaultTableModel paramDefaultTableModel) {
    super(paramDefaultTableModel);
    setRowSelectionAllowed(false);
    setColumnSelectionAllowed(false);
    setCellSelectionEnabled(true);
    setSelectionMode(2);
    addHighlighter(HighlighterFactory.createAlternateStriping());
    setPreferredScrollableViewportSize(new Dimension(850, getRowHeight() * Math.min(getModel().getRowCount(), 5)));
    if (getColumn(0).getHeaderValue().equals(""))
      getColumn(0).setMaxWidth(20); 
  }
  
  public TableCellRenderer getCellRenderer(int paramInt1, int paramInt2) {
    Object object = getValueAt(paramInt1, paramInt2);
    return (object instanceof com.mentor.dms.contentprovider.ContentProviderDocumentList) ? new ViewAttachmentButtonCellRenderer() : super.getCellRenderer(paramInt1, paramInt2);
  }
  
  public TableCellEditor getCellEditor(int paramInt1, int paramInt2) {
    Object object = getValueAt(paramInt1, paramInt2);
    return (object instanceof com.mentor.dms.contentprovider.ContentProviderDocumentList) ? new ViewAttachmentButtonCellEditor() : super.getCellEditor(paramInt1, paramInt2);
  }
  
  public Component prepareRenderer(TableCellRenderer paramTableCellRenderer, int paramInt1, int paramInt2) {
    Component component = super.prepareRenderer(paramTableCellRenderer, paramInt1, paramInt2);
    Object object = getValueAt(paramInt1, paramInt2);
    JComponent jComponent = (JComponent)component;
    if (object instanceof com.mentor.dms.contentprovider.ContentProviderDocumentList) {
      jComponent.setToolTipText("View attachment...");
    } else if (object instanceof String) {
      jComponent.setToolTipText(formatToolTip((String)object));
    } else if (object instanceof Date) {
      jComponent.setToolTipText(((Date)object).toString());
    } 
    return component;
  }
  
  private String formatToolTip(String paramString) {
    StringBuilder stringBuilder = new StringBuilder("<html>");
    int i = 0;
    for (String str : paramString.split("\\s+")) {
      if (i + str.length() > 120) {
        stringBuilder.append("<br>");
        i = 0;
      } 
      stringBuilder.append(str + " ");
      i += str.length() + 1;
    } 
    stringBuilder.append("</html>");
    return stringBuilder.toString();
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\plugin\ContentProviderViewAlertsJXTable.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */