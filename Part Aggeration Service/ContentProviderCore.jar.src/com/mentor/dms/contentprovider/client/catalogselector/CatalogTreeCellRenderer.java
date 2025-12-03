package com.mentor.dms.contentprovider.client.catalogselector;

import java.awt.Component;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

public class CatalogTreeCellRenderer extends DefaultTreeCellRenderer {
  private ImageIcon rootImageIcon = null;
  
  public void setRootImage(ImageIcon paramImageIcon) {
    this.rootImageIcon = paramImageIcon;
  }
  
  public Component getTreeCellRendererComponent(JTree paramJTree, Object paramObject, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, int paramInt, boolean paramBoolean4) {
    super.getTreeCellRendererComponent(paramJTree, paramObject, paramBoolean1, paramBoolean2, paramBoolean3, paramInt, paramBoolean4);
    DefaultMutableTreeNode defaultMutableTreeNode = (DefaultMutableTreeNode)paramObject;
    if (defaultMutableTreeNode.getUserObject() instanceof AbstractNodeInfo)
      setToolTipText(((AbstractNodeInfo)defaultMutableTreeNode.getUserObject()).getToolTipText()); 
    if (!(defaultMutableTreeNode instanceof SmartMutableTreeNode))
      System.out.println((String)defaultMutableTreeNode.getUserObject()); 
    if (defaultMutableTreeNode.isRoot()) {
      setIcon(this.rootImageIcon);
    } else {
      setIcon((Icon)null);
    } 
    return this;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\client\catalogselector\CatalogTreeCellRenderer.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */