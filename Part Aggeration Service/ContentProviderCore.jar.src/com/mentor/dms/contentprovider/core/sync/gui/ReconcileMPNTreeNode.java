package com.mentor.dms.contentprovider.core.sync.gui;

import java.util.ArrayList;
import java.util.List;

public class ReconcileMPNTreeNode extends ReconcileTreeNode {
  private List<ReconcilePropertyTreeNode> children = new ArrayList<>();
  
  public ReconcileMPNTreeNode(String paramString) {
    super(paramString);
  }
  
  public List<ReconcilePropertyTreeNode> getChildren() {
    return this.children;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\sync\gui\ReconcileMPNTreeNode.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */