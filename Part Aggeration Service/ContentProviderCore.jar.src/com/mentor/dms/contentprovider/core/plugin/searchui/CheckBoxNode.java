package com.mentor.dms.contentprovider.core.plugin.searchui;

import javax.swing.tree.DefaultMutableTreeNode;

class CheckBoxNode extends DefaultMutableTreeNode {
  private CheckBoxTree tree;
  
  private String text;
  
  private boolean bSelected;
  
  private Object clientObject;
  
  public CheckBoxNode(CheckBoxTree paramCheckBoxTree, String paramString, boolean paramBoolean) {
    this.tree = paramCheckBoxTree;
    this.text = paramString;
    this.bSelected = paramBoolean;
  }
  
  public CheckBoxNode(CheckBoxTree paramCheckBoxTree, String paramString, boolean paramBoolean, Object paramObject) {
    this(paramCheckBoxTree, paramString, paramBoolean);
    setClientObject(paramObject);
  }
  
  public CheckBoxTree getTree() {
    return this.tree;
  }
  
  public boolean isSelected() {
    return this.bSelected;
  }
  
  public void setSelected(boolean paramBoolean) {
    this.bSelected = paramBoolean;
    for (CheckBoxTreeListener checkBoxTreeListener : this.tree.getListeners())
      checkBoxTreeListener.stateChanged(this); 
  }
  
  public String getText() {
    return this.text;
  }
  
  public void setText(String paramString) {
    this.text = paramString;
  }
  
  public void setClientObject(Object paramObject) {
    this.clientObject = paramObject;
  }
  
  public Object getClientObject() {
    return this.clientObject;
  }
  
  public String toString() {
    return this.text;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\plugin\searchui\CheckBoxNode.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */