package com.mentor.dms.contentprovider.core.plugin.searchui;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.table.TableCellEditor;

class null extends MouseAdapter {
  public void mouseMoved(MouseEvent paramMouseEvent) {
    Point point = paramMouseEvent.getPoint();
    if (point != null) {
      int i = ContentProviderSearchResultsJXTable.this.rowAtPoint(point);
      int j = ContentProviderSearchResultsJXTable.this.columnAtPoint(point);
      try {
        TableCellEditor tableCellEditor = ContentProviderSearchResultsJXTable.this.getCellEditor(i, j);
        if (tableCellEditor instanceof ContentProviderSearchResultsPushButtonCellEditor)
          ContentProviderSearchResultsJXTable.this.editCellAt(i, j); 
      } catch (Exception exception) {}
    } 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\plugin\searchui\ContentProviderSearchResultsJXTable$2.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */