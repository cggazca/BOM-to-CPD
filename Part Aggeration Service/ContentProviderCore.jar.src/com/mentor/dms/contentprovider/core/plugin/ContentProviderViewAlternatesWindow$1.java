package com.mentor.dms.contentprovider.core.plugin;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

class null extends WindowAdapter {
  public void windowGainedFocus(WindowEvent paramWindowEvent) {
    if (ContentProviderViewAlternatesWindow.this.altsTable != null && ContentProviderViewAlternatesWindow.this.altsTable.getCellEditor() != null)
      ContentProviderViewAlternatesWindow.this.altsTable.getCellEditor().cancelCellEditing(); 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\plugin\ContentProviderViewAlternatesWindow$1.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */