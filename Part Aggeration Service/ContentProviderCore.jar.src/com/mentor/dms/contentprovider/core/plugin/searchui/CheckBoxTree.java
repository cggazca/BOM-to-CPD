package com.mentor.dms.contentprovider.core.plugin.searchui;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import org.jdesktop.swingx.JXTree;

public class CheckBoxTree extends JXTree {
  private DefaultMutableTreeNode rootNode = null;
  
  private ArrayList<CheckBoxTreeListener> listeners = new ArrayList<>();
  
  public CheckBoxTree() {
    super(new DefaultMutableTreeNode("Selection List"));
    this.rootNode = (DefaultMutableTreeNode)getModel().getRoot();
    CheckBoxNodeRenderer checkBoxNodeRenderer = new CheckBoxNodeRenderer();
    setCellRenderer(checkBoxNodeRenderer);
    setShowsRootHandles(true);
    setCellEditor(new CheckBoxNodeEditor(this));
    setEditable(true);
    setRootVisible(false);
  }
  
  public static void main(String[] paramArrayOfString) {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception exception) {
      exception.printStackTrace();
    } 
    JFrame jFrame = new JFrame("CheckBox Tree");
    CheckBoxTree checkBoxTree = new CheckBoxTree();
    checkBoxTree.addListener(new CheckBoxTreeListener() {
          public void stateChanged(CheckBoxNode param1CheckBoxNode) {
            System.out.println(param1CheckBoxNode.getText() + " = " + param1CheckBoxNode.getText());
          }
        });
    TabNode tabNode = checkBoxTree.addTab("General");
    checkBoxTree.addCheckBox(tabNode, "Part Number", true);
    checkBoxTree.addCheckBox(tabNode, "Manufacturer", true);
    tabNode = checkBoxTree.addTab("Package");
    checkBoxTree.addCheckBox(tabNode, "Package Height", true);
    checkBoxTree.addCheckBox(tabNode, "Package Weight", true);
    checkBoxTree.expandAll();
    JScrollPane jScrollPane = new JScrollPane((Component)checkBoxTree);
    jFrame.getContentPane().add(jScrollPane, "Center");
    jFrame.setSize(300, 150);
    jFrame.setVisible(true);
  }
  
  public void clear() {
    this.rootNode.removeAllChildren();
  }
  
  public TabNode addTab(String paramString) {
    TabNode tabNode = new TabNode(paramString);
    this.rootNode.add(tabNode);
    return tabNode;
  }
  
  public CheckBoxNode addCheckBox(TabNode paramTabNode, String paramString, boolean paramBoolean) {
    CheckBoxNode checkBoxNode = new CheckBoxNode(this, paramString, paramBoolean);
    paramTabNode.add(checkBoxNode);
    return checkBoxNode;
  }
  
  public CheckBoxNode addCheckBox(TabNode paramTabNode, String paramString, boolean paramBoolean, Object paramObject) {
    CheckBoxNode checkBoxNode = new CheckBoxNode(this, paramString, paramBoolean, paramObject);
    paramTabNode.add(checkBoxNode);
    return checkBoxNode;
  }
  
  public void addListener(CheckBoxTreeListener paramCheckBoxTreeListener) {
    this.listeners.add(paramCheckBoxTreeListener);
  }
  
  public Collection<CheckBoxTreeListener> getListeners() {
    return this.listeners;
  }
  
  public void selectAll(boolean paramBoolean) {
    if (this.rootNode.getChildCount() >= 0) {
      Enumeration<TreeNode> enumeration = this.rootNode.children();
      while (enumeration.hasMoreElements()) {
        TreeNode treeNode = enumeration.nextElement();
        Enumeration<? extends TreeNode> enumeration1 = treeNode.children();
        while (enumeration1.hasMoreElements()) {
          CheckBoxNode checkBoxNode = (CheckBoxNode)enumeration1.nextElement();
          checkBoxNode.setSelected(paramBoolean);
        } 
      } 
    } 
    refresh();
  }
  
  public void refresh() {
    DefaultTreeModel defaultTreeModel = (DefaultTreeModel)getModel();
    defaultTreeModel.nodeStructureChanged(this.rootNode);
    expandAll();
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\plugin\searchui\CheckBoxTree.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */