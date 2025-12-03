package com.mentor.dms.contentprovider.client;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JList;

class null extends MouseAdapter {
  public void mouseClicked(MouseEvent paramMouseEvent) {
    JList jList = (JList)paramMouseEvent.getSource();
    if (paramMouseEvent.getClickCount() == 2) {
      int i = jList.locationToIndex(paramMouseEvent.getPoint());
      if (i >= 0) {
        PropertyValueSelectionDlg.this.processSelection(list);
        PropertyValueSelectionDlg.this.dispose();
      } 
    } 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\client\PropertyValueSelectionDlg$1.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */