package com.mentor.dms.contentprovider.plugin.searchui;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import javax.swing.AbstractCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTree;
import javax.swing.event.ChangeEvent;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreePath;

class CheckBoxNodeEditor extends AbstractCellEditor implements TreeCellEditor {
  CheckBoxNodeRenderer renderer = new CheckBoxNodeRenderer();
  
  ChangeEvent changeEvent = null;
  
  CheckBoxTree tree;
  
  public CheckBoxNodeEditor(CheckBoxTree paramCheckBoxTree) {
    this.tree = paramCheckBoxTree;
  }
  
  public Object getCellEditorValue() {
    JCheckBox jCheckBox = this.renderer.getLeafRenderer();
    CheckBoxNode checkBoxNode = (CheckBoxNode)jCheckBox.getClientProperty("CBN");
    checkBoxNode.setSelected(jCheckBox.isSelected());
    return checkBoxNode;
  }
  
  public boolean isCellEditable(EventObject paramEventObject) {
    boolean bool = false;
    if (paramEventObject instanceof MouseEvent) {
      MouseEvent mouseEvent = (MouseEvent)paramEventObject;
      TreePath treePath = this.tree.getPathForLocation(mouseEvent.getX(), mouseEvent.getY());
      if (treePath != null) {
        Object object = treePath.getLastPathComponent();
        bool = object instanceof CheckBoxNode;
      } 
    } 
    return bool;
  }
  
  public Component getTreeCellEditorComponent(JTree paramJTree, Object paramObject, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, int paramInt) {
    Component component = this.renderer.getTreeCellRendererComponent(paramJTree, paramObject, true, paramBoolean2, paramBoolean3, paramInt, true);
    ItemListener itemListener = new ItemListener() {
        public void itemStateChanged(ItemEvent param1ItemEvent) {
          if (CheckBoxNodeEditor.this.stopCellEditing())
            CheckBoxNodeEditor.this.fireEditingStopped(); 
        }
      };
    if (component instanceof JCheckBox)
      ((JCheckBox)component).addItemListener(itemListener); 
    return component;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\plugin\searchui\CheckBoxNodeEditor.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */