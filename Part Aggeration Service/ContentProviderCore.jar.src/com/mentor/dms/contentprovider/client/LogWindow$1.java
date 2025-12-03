package com.mentor.dms.contentprovider.client;

import java.awt.Dialog;
import java.awt.Window;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

class null implements HierarchyListener {
  public void hierarchyChanged(HierarchyEvent paramHierarchyEvent) {
    Window window = SwingUtilities.getWindowAncestor(textArea);
    if (window instanceof Dialog) {
      Dialog dialog = (Dialog)window;
      if (!dialog.isResizable())
        dialog.setResizable(true); 
    } 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\client\LogWindow$1.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */