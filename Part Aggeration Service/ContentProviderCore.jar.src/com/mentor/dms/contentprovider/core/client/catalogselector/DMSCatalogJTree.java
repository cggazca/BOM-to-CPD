package com.mentor.dms.contentprovider.core.client.catalogselector;

import com.mentor.datafusion.oi.OICursor;
import com.mentor.datafusion.oi.OIException;
import com.mentor.datafusion.oi.OIObjectManager;
import com.mentor.datafusion.oi.OIObjectManagerFactory;
import com.mentor.datafusion.oi.OIQuery;
import com.mentor.datafusion.oi.model.OIClass;
import com.mentor.datafusion.oi.type.OIObject;
import com.mentor.datafusion.utils.logger.MGLogger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

public class DMSCatalogJTree<T extends CatalogNodeInfo> extends JTree {
  static MGLogger logger = MGLogger.getLogger(DMSCatalogJTree.class);
  
  private ICatalogNodeInfoFactory<T> factory;
  
  private OIObjectManager om;
  
  private OIObjectManagerFactory omf;
  
  private ArrayList<String> dmsPruneCatalogPathList = new ArrayList<>();
  
  private CatalogTreeCellRenderer catalogTreeCellRender;
  
  public DMSCatalogJTree(OIObjectManager paramOIObjectManager, ICatalogNodeInfoFactory<T> paramICatalogNodeInfoFactory) {
    this.om = paramOIObjectManager;
    this.factory = paramICatalogNodeInfoFactory;
    this.omf = paramOIObjectManager.getObjectManagerFactory();
    setModel(null);
    ToolTipManager.sharedInstance().registerComponent(this);
    this.catalogTreeCellRender = new CatalogTreeCellRenderer();
    setCellRenderer(this.catalogTreeCellRender);
  }
  
  public void loadCatalogTree(ImageIcon paramImageIcon, String paramString, Collection<String> paramCollection) throws OIException {
    for (String str : paramCollection)
      this.dmsPruneCatalogPathList.add(getCatalogIDForDMN(str)); 
    loadCatalogTree(paramImageIcon, paramString);
  }
  
  public void loadCatalogTree(ImageIcon paramImageIcon, String paramString) throws OIException {
    LinkedHashMap<Object, Object> linkedHashMap = new LinkedHashMap<>();
    OIQuery oIQuery = this.om.createQuery("CatalogGroup", true);
    oIQuery.addRestriction("DomainModelName", paramString);
    oIQuery.addColumn("CatalogGroup");
    oIQuery.addColumn("ParentKey");
    oIQuery.addColumn("Text.CatalogTitle");
    oIQuery.addColumn("DomainModelName");
    oIQuery.addColumn("CatalogStatus");
    OICursor oICursor = oIQuery.execute();
    if (!oICursor.next())
      return; 
    T t = this.factory.create((OIObject)oICursor);
    String str = t.getID();
    SmartMutableTreeNode smartMutableTreeNode = new SmartMutableTreeNode();
    smartMutableTreeNode.setUserObject(t);
    linkedHashMap.put(str, smartMutableTreeNode);
    DefaultTreeModel defaultTreeModel = new DefaultTreeModel(smartMutableTreeNode);
    setModel(defaultTreeModel);
    oICursor.close();
    oIQuery = this.om.createQuery("CatalogGroup", true);
    oIQuery.addRestriction("CatalogGroup", t.getID() + "??*");
    oIQuery.addRestriction("Text.Language", this.om.getObjectManagerFactory().getLanguage());
    oIQuery.addColumn("CatalogGroup");
    oIQuery.addColumn("ParentKey");
    oIQuery.addColumn("Text.CatalogTitle");
    oIQuery.addColumn("DomainModelName");
    oIQuery.addColumn("CatalogStatus");
    oIQuery.addSortBy("CatalogGroup", true);
    oICursor = oIQuery.execute();
    while (oICursor.next()) {
      t = this.factory.create((OIObject)oICursor);
      boolean bool = (this.dmsPruneCatalogPathList.size() == 0) ? true : false;
      if (!bool)
        for (String str1 : this.dmsPruneCatalogPathList) {
          if (str1.startsWith(t.getID()) || t.getID().startsWith(str1)) {
            bool = true;
            break;
          } 
        }  
      if (bool) {
        SmartMutableTreeNode smartMutableTreeNode1 = new SmartMutableTreeNode();
        smartMutableTreeNode1.setUserObject(t);
        linkedHashMap.put(t.getID(), smartMutableTreeNode1);
      } 
    } 
    oICursor.close();
    for (SmartMutableTreeNode smartMutableTreeNode1 : linkedHashMap.values()) {
      CatalogNodeInfo catalogNodeInfo = (CatalogNodeInfo)smartMutableTreeNode1.getUserObject();
      if (catalogNodeInfo.getID().equals(str))
        continue; 
      SmartMutableTreeNode smartMutableTreeNode2 = (SmartMutableTreeNode)linkedHashMap.get(catalogNodeInfo.getParentKey());
      smartMutableTreeNode2.add(smartMutableTreeNode1);
    } 
    for (SmartMutableTreeNode smartMutableTreeNode1 : linkedHashMap.values())
      smartMutableTreeNode1.setAllowsChildren((smartMutableTreeNode1.getChildCount() > 0)); 
    defaultTreeModel.nodeStructureChanged(smartMutableTreeNode);
    this.catalogTreeCellRender.setRootImage(paramImageIcon);
    if (this.dmsPruneCatalogPathList.size() > 0)
      expandAll(); 
  }
  
  public OIClass getSelectedOICatalog() {
    T t = getSelectedCatalogNode();
    return (t == null) ? null : this.omf.getClassManager().getOIClass(t.getDMN());
  }
  
  public T getSelectedNode() {
    SmartMutableTreeNode smartMutableTreeNode = (SmartMutableTreeNode)getLastSelectedPathComponent();
    return (T)((smartMutableTreeNode == null) ? null : (CatalogNodeInfo)smartMutableTreeNode.getUserObject());
  }
  
  public T getSelectedCatalogNode() {
    SmartMutableTreeNode smartMutableTreeNode = (SmartMutableTreeNode)getLastSelectedPathComponent();
    return (T)((smartMutableTreeNode == null) ? null : (CatalogNodeInfo)smartMutableTreeNode.getUserObject());
  }
  
  public void expandAll() {
    TreeNode treeNode = (TreeNode)getModel().getRoot();
    expandAll(new TreePath(treeNode));
  }
  
  private void expandAll(TreePath paramTreePath) {
    TreeNode treeNode = (TreeNode)paramTreePath.getLastPathComponent();
    if (treeNode.getChildCount() >= 0) {
      Enumeration<? extends TreeNode> enumeration = treeNode.children();
      while (enumeration.hasMoreElements()) {
        TreeNode treeNode1 = enumeration.nextElement();
        TreePath treePath = paramTreePath.pathByAddingChild(treeNode1);
        expandAll(treePath);
      } 
    } 
    expandPath(paramTreePath);
  }
  
  private String getCatalogIDForDMN(String paramString) throws OIException {
    String str = "**";
    OIQuery oIQuery = this.om.createQuery("CatalogGroup", true);
    oIQuery.addRestriction("DomainModelName", paramString);
    oIQuery.addColumn("CatalogGroup");
    OICursor oICursor = oIQuery.execute();
    if (oICursor.next())
      str = oICursor.getString("CatalogGroup"); 
    return str;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\client\catalogselector\DMSCatalogJTree.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */