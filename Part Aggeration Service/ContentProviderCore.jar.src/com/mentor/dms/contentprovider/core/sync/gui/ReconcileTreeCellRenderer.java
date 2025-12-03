package com.mentor.dms.contentprovider.core.sync.gui;

import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

public class ReconcileTreeCellRenderer extends DefaultTreeCellRenderer {
  private static final long serialVersionUID = 1L;
  
  private final ImageIcon componentIcon = new ImageIcon(ReconcileTreeCellRenderer.class.getResource("images/component.png"));
  
  private final ImageIcon propertyIcon = new ImageIcon(ReconcileTreeCellRenderer.class.getResource("images/property.png"));
  
  public Component getTreeCellRendererComponent(JTree paramJTree, Object paramObject, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, int paramInt, boolean paramBoolean4) {
    super.getTreeCellRendererComponent(paramJTree, paramObject, paramBoolean1, paramBoolean2, paramBoolean3, paramInt, paramBoolean4);
    ReconcileTreeNode reconcileTreeNode = (ReconcileTreeNode)paramObject;
    if (reconcileTreeNode instanceof ReconcilePropertyTreeNode) {
      setIcon(this.propertyIcon);
      setToolTipText(((ReconcilePropertyTreeNode)reconcileTreeNode).getErrorString());
    } else if (reconcileTreeNode instanceof ReconcileMPNTreeNode) {
      setIcon(this.componentIcon);
    } 
    return this;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\sync\gui\ReconcileTreeCellRenderer.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */