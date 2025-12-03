package com.mentor.dms.contentprovider.core.plugin;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class null extends MouseAdapter {
  public void mouseMoved(MouseEvent paramMouseEvent) {
    Point point = paramMouseEvent.getPoint();
    if (point != null) {
      int i = ViewSupplyChainJXTable.this.rowAtPoint(point);
      int j = ViewSupplyChainJXTable.this.columnAtPoint(point);
      try {
        if (ViewSupplyChainJXTable.this.getCellEditor(i, j) instanceof PushButtonCellEditor)
          ViewSupplyChainJXTable.this.editCellAt(i, j); 
      } catch (Exception exception) {}
    } 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\plugin\ViewSupplyChainJXTable$1.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */