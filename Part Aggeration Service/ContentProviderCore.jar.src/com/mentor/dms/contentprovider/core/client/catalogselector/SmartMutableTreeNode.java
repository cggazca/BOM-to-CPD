package com.mentor.dms.contentprovider.core.client.catalogselector;

import javax.swing.tree.DefaultMutableTreeNode;

public class SmartMutableTreeNode extends DefaultMutableTreeNode {
  public SmartMutableTreeNode(Object paramObject, boolean paramBoolean) {
    super(paramObject, paramBoolean);
  }
  
  public SmartMutableTreeNode() {}
  
  public boolean isLeaf() {
    return !getAllowsChildren();
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\client\catalogselector\SmartMutableTreeNode.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */