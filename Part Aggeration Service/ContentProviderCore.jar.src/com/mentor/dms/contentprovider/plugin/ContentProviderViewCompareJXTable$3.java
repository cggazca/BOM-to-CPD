package com.mentor.dms.contentprovider.plugin;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class null extends MouseAdapter {
  public void mouseMoved(MouseEvent paramMouseEvent) {
    Point point = paramMouseEvent.getPoint();
    if (point != null) {
      int i = ContentProviderViewCompareJXTable.this.rowAtPoint(point);
      int j = ContentProviderViewCompareJXTable.this.columnAtPoint(point);
      if (ContentProviderViewCompareJXTable.this.getCellEditor(i, j) instanceof PushButtonCellEditor)
        ContentProviderViewCompareJXTable.this.editCellAt(i, j); 
    } 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\plugin\ContentProviderViewCompareJXTable$3.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */