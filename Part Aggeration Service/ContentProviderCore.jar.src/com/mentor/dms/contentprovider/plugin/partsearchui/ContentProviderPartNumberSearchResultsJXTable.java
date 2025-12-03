package com.mentor.dms.contentprovider.plugin.partsearchui;

import com.mentor.datafusion.utils.logger.MGLogger;
import com.mentor.dms.contentprovider.plugin.CustomCellRenderer;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.HighlighterFactory;

public class ContentProviderPartNumberSearchResultsJXTable extends JXTable {
  private static MGLogger log = MGLogger.getLogger(ContentProviderPartNumberSearchResultsJXTable.class);
  
  private ContentProviderPartNumberSearchMainPanel mainPanel;
  
  public ContentProviderPartNumberSearchResultsJXTable(ContentProviderPartNumberSearchMainPanel paramContentProviderPartNumberSearchMainPanel, ContentProviderPartNumberSearchResultsTableModel paramContentProviderPartNumberSearchResultsTableModel) {
    super(paramContentProviderPartNumberSearchResultsTableModel);
    this.mainPanel = paramContentProviderPartNumberSearchMainPanel;
    setRowSelectionAllowed(true);
    setSelectionMode(0);
    addHighlighter(HighlighterFactory.createAlternateStriping());
    setHorizontalScrollEnabled(true);
    MouseListener mouseListener = new MouseListener();
    addMouseListener(mouseListener);
  }
  
  public TableCellRenderer getCellRenderer(int paramInt1, int paramInt2) {
    Object object = getValueAt(paramInt1, paramInt2);
    return (TableCellRenderer)((object instanceof com.mentor.dms.contentprovider.plugin.AbstractPushButtonCell) ? new CustomCellRenderer() : super.getCellRenderer(paramInt1, paramInt2));
  }
  
  public TableCellEditor getCellEditor(int paramInt1, int paramInt2) {
    Object object = getValueAt(paramInt1, paramInt2);
    return (object instanceof com.mentor.dms.contentprovider.plugin.AbstractPushButtonCell) ? new ContentProviderPartNumberSearchPushButtonCellEditor() : super.getCellEditor(paramInt1, paramInt2);
  }
  
  class MouseListener extends MouseAdapter {
    public void mouseClicked(MouseEvent param1MouseEvent) {
      if (param1MouseEvent.getClickCount() == 2)
        ContentProviderPartNumberSearchResultsJXTable.this.mainPanel.doSelect(); 
    }
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\plugin\partsearchui\ContentProviderPartNumberSearchResultsJXTable.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */