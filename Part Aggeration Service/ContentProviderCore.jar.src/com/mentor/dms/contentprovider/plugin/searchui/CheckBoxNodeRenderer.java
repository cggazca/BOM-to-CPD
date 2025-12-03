package com.mentor.dms.contentprovider.plugin.searchui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JCheckBox;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;

class CheckBoxNodeRenderer implements TreeCellRenderer {
  private JCheckBox leafRenderer = new JCheckBox();
  
  private DefaultTreeCellRenderer nonLeafRenderer = new DefaultTreeCellRenderer();
  
  Color selectionBorderColor;
  
  Color selectionForeground;
  
  Color selectionBackground;
  
  Color textForeground;
  
  Color textBackground;
  
  protected JCheckBox getLeafRenderer() {
    return this.leafRenderer;
  }
  
  public CheckBoxNodeRenderer() {
    Font font = UIManager.getFont("Tree.font");
    if (font != null)
      this.leafRenderer.setFont(font); 
    Boolean bool = (Boolean)UIManager.get("Tree.drawsFocusBorderAroundIcon");
    this.leafRenderer.setFocusPainted((bool != null && bool.booleanValue()));
    this.selectionBorderColor = UIManager.getColor("Tree.selectionBorderColor");
    this.selectionForeground = UIManager.getColor("Tree.selectionForeground");
    this.selectionBackground = UIManager.getColor("Tree.selectionBackground");
    this.textForeground = UIManager.getColor("Tree.textForeground");
    this.textBackground = UIManager.getColor("Tree.textBackground");
  }
  
  public Component getTreeCellRendererComponent(JTree paramJTree, Object paramObject, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, int paramInt, boolean paramBoolean4) {
    Component component;
    if (paramBoolean3) {
      String str = paramJTree.convertValueToText(paramObject, paramBoolean1, paramBoolean2, paramBoolean3, paramInt, false);
      this.leafRenderer.setText(str);
      this.leafRenderer.setSelected(false);
      this.leafRenderer.setEnabled(paramJTree.isEnabled());
      this.leafRenderer.setForeground(this.textForeground);
      this.leafRenderer.setBackground(this.textBackground);
      if (paramObject != null && paramObject instanceof CheckBoxNode) {
        CheckBoxNode checkBoxNode = (CheckBoxNode)paramObject;
        this.leafRenderer.putClientProperty("CBN", checkBoxNode);
        this.leafRenderer.setText(checkBoxNode.getText());
        this.leafRenderer.setSelected(checkBoxNode.isSelected());
      } 
      component = this.leafRenderer;
    } else {
      component = this.nonLeafRenderer.getTreeCellRendererComponent(paramJTree, paramObject, paramBoolean1, paramBoolean2, paramBoolean3, paramInt, paramBoolean4);
    } 
    return component;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\plugin\searchui\CheckBoxNodeRenderer.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */