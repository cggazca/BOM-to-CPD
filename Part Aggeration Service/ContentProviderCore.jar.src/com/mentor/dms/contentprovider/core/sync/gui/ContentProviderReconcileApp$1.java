package com.mentor.dms.contentprovider.core.sync.gui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import org.jdesktop.swingx.table.TableColumnExt;

class null extends WindowAdapter {
  public void windowClosing(WindowEvent paramWindowEvent) {
    ContentProviderReconcileApp.this.confirmExit(paramWindowEvent.getWindow());
  }
  
  public void windowOpened(WindowEvent paramWindowEvent) {
    TableColumnExt tableColumnExt = ContentProviderReconcileApp.this.dmsReconcileJTable.getColumnExt(3);
    tableColumnExt.setMaxWidth(120);
    tableColumnExt.setMinWidth(30);
    tableColumnExt.setPreferredWidth(100);
    tableColumnExt.setWidth(100);
    tableColumnExt = ContentProviderReconcileApp.this.dmsReconcileJTable.getColumnExt(1);
    tableColumnExt.setMaxWidth(120);
    tableColumnExt.setMinWidth(30);
    tableColumnExt.setPreferredWidth(100);
    tableColumnExt.setWidth(100);
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\sync\gui\ContentProviderReconcileApp$1.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */