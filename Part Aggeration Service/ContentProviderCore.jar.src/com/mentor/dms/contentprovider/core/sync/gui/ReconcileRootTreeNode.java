package com.mentor.dms.contentprovider.core.sync.gui;

import java.util.ArrayList;
import java.util.List;

public class ReconcileRootTreeNode extends ReconcileTreeNode {
  private List<ReconcileMPNTreeNode> children = new ArrayList<>();
  
  public ReconcileRootTreeNode(String paramString) {
    super(paramString);
  }
  
  public List<ReconcileMPNTreeNode> getChildren() {
    return this.children;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\sync\gui\ReconcileRootTreeNode.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */