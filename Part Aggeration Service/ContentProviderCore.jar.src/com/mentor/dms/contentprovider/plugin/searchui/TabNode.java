package com.mentor.dms.contentprovider.plugin.searchui;

import javax.swing.tree.DefaultMutableTreeNode;

class TabNode extends DefaultMutableTreeNode {
  private String name;
  
  public TabNode(String paramString) {
    this.name = paramString;
  }
  
  public String getName() {
    return this.name;
  }
  
  public String toString() {
    return this.name;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\plugin\searchui\TabNode.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */