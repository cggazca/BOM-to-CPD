package com.mentor.dms.contentprovider.sync.gui;

import java.awt.Component;
import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.decorator.HighlightPredicate;

class null implements HighlightPredicate {
  public boolean isHighlighted(Component paramComponent, ComponentAdapter paramComponentAdapter) {
    int i = paramComponentAdapter.convertRowIndexToModel(paramComponentAdapter.row);
    String str = (String)paramComponentAdapter.getValueAt(i, 6);
    return (str != null);
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\sync\gui\ContentProviderReconcileJXTreeTable$2.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */