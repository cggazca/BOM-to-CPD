package com.mentor.dms.contentprovider.core.sync.gui;

import java.util.Comparator;

class null implements Comparator<ReconcileTreeNode> {
  public int compare(ReconcileTreeNode paramReconcileTreeNode1, ReconcileTreeNode paramReconcileTreeNode2) {
    return paramReconcileTreeNode1.getPropName().compareTo(paramReconcileTreeNode2.getPropName());
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\sync\gui\ContentProviderReconcileTableModel$1.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */