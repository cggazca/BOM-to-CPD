package com.mentor.dms.contentprovider.core.sync.gui;

import com.mentor.datafusion.oi.OICursor;
import com.mentor.datafusion.oi.OIException;
import com.mentor.datafusion.oi.OIHelper;
import com.mentor.datafusion.oi.OIObjectManager;
import com.mentor.datafusion.oi.OIQuery;
import com.mentor.datafusion.oi.model.OIClass;
import com.mentor.datafusion.oi.model.OIField;
import com.mentor.datafusion.oi.model.OIStringField;
import com.mentor.datafusion.oi.type.OIObject;
import com.mentor.datafusion.utils.logger.MGLogger;
import com.mentor.dms.contentprovider.core.AbstractContentProvider;
import com.mentor.dms.contentprovider.core.ContentProviderException;
import com.mentor.dms.contentprovider.core.ContentProviderFactory;
import com.mentor.dms.contentprovider.core.ContentProviderRegistryEntry;
import com.mentor.dms.contentprovider.core.config.ContentProviderConfig;
import com.mentor.dms.contentprovider.core.config.ContentProviderConfigContentProviderMap;
import com.mentor.dms.contentprovider.core.config.ContentProviderConfigException;
import com.mentor.dms.contentprovider.core.config.ContentProviderConfigMPNCatalog;
import com.mentor.dms.contentprovider.core.config.ContentProviderConfigPartClass;
import com.mentor.dms.contentprovider.core.config.ContentProviderConfigProperty;
import com.mentor.dms.contentprovider.core.config.ContentProviderConfigPropertyMap;
import com.mentor.dms.contentprovider.core.sync.ContentProviderSync;
import com.mentor.dms.contentprovider.core.sync.ContentProviderSyncException;
import com.mentor.dms.contentprovider.core.sync.PropertySyncList;
import com.mentor.dms.contentprovider.core.sync.PropertySyncListLine;
import com.mentor.dms.contentprovider.core.utils.DateUtils;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.tree.TreePath;
import org.jdesktop.swingx.treetable.AbstractTreeTableModel;

public class ContentProviderReconcileTableModel extends AbstractTreeTableModel {
  static MGLogger logger = MGLogger.getLogger(ContentProviderReconcileTableModel.class);
  
  public static final String APP_TITLE = "EDM Library/External Content Property Reconcile";
  
  private ReconcileRootTreeNode treeRootNode;
  
  private Component parent;
  
  private boolean bIsUpdateErrorFilterEnabled = false;
  
  private boolean bReconcileDataExist = true;
  
  private boolean bReconcileFirst = true;
  
  public static final int COLUMN_MPN = 0;
  
  public static final int COLUMN_DMS_PROPERTY = 0;
  
  public static final int COLUMN_CCP_NAME = 1;
  
  public static final int COLUMN_CCP_VALUE = 2;
  
  public static final int COLUMN_SYNC_ACTION = 3;
  
  public static final int COLUMN_DMS_VALUE = 4;
  
  public static final int COLUMN_SYNC_TYPE = 5;
  
  public static final int COLUMN_ERROR_STRING = 6;
  
  public static final int COLUMN_HIGHLIGHT = 7;
  
  public String[] headers = new String[] { "Manufacturer Part/Property", "Content Provider", "Content Provider Value", "Action?", "EDM Library Value", "Sync Type" };
  
  public Object[] longvalues = new Object[] { "Fairchild Semiconductor:DM74LS266MX - EDM Library Property", "Parts Universe", "0.150 INCH, MS-120, SOIC-14", "0.150 INCH, MS-120, SOIC-14", "Sync Type", "Ignore Always" };
  
  public ContentProviderReconcileTableModel(Component paramComponent) {
    this.parent = paramComponent;
    this.treeRootNode = new ReconcileRootTreeNode("root");
  }
  
  public void loadUnReconciledProperties(OIObjectManager paramOIObjectManager, HashSet<String> paramHashSet, HashMap<String, HashMap<String, String>> paramHashMap, JDialog paramJDialog) throws ContentProviderException, ContentProviderConfigException, OIException {
    this.treeRootNode = new ReconcileRootTreeNode("root");
    boolean bool = false;
    HashSet<String> hashSet1 = new HashSet();
    HashSet<String> hashSet2 = new HashSet();
    ReconcileMPNTreeNode reconcileMPNTreeNode = null;
    logger.info("Querying EDM Library for unreconciled Manufacturer Parts where Content Provider properties have changed since the last reconcile...");
    HashMap<Object, Object> hashMap = new HashMap<>();
    OIQuery oIQuery = paramOIObjectManager.createQuery("ManufacturerPart", true);
    oIQuery.addRestriction("ExternalContentId", "~NULL");
    oIQuery.addRestriction("ReconcileFlag", "No");
    oIQuery.addColumn("ManufacturerpartId");
    oIQuery.addColumn("ExternalContentId");
    int i = 0;
    if (paramHashSet != null) {
      i = paramHashSet.size();
      if (i <= 100) {
        boolean bool1 = true;
        StringBuilder stringBuilder = new StringBuilder();
        for (String str : paramHashSet) {
          if (!bool1) {
            stringBuilder.append("|");
          } else {
            bool1 = false;
          } 
          stringBuilder.append(OIHelper.escapeQueryRestriction(str));
        } 
        oIQuery.addRestriction("ManufacturerpartId", stringBuilder.toString());
      } 
    } 
    OICursor oICursor = oIQuery.execute();
    if (this.bReconcileFirst && oIQuery.count() == 0L)
      this.bReconcileDataExist = false; 
    this.bReconcileFirst = false;
    while (oICursor.next()) {
      if (i > 100 && !paramHashSet.contains(oICursor.getString("ManufacturerpartId")))
        continue; 
      OIObject oIObject = oICursor.getProxyObject().getObject();
      OIClass oIClass = oIObject.getOIClass();
      try {
        ArrayList<OIObject> arrayList = (ArrayList)hashMap.get(oIClass.getName());
        if (arrayList == null) {
          arrayList = new ArrayList();
          hashMap.put(oIClass.getName(), arrayList);
        } 
        arrayList.add(oIObject.getObject("ExternalContentId"));
      } catch (OIException oIException) {
        logger.warn(oIException.getMessage());
      } 
    } 
    for (String str : ContentProviderFactory.getInstance().getRegisteredContentProviderIds()) {
      AbstractContentProvider abstractContentProvider = ContentProviderFactory.getInstance().createContentProvider(str);
      if (!abstractContentProvider.hasRole(ContentProviderRegistryEntry.ContentProviderRole.MANUFACTURER_PART_SYNCHRONIZATION))
        continue; 
      ContentProviderConfig contentProviderConfig = abstractContentProvider.getConfig();
      for (Map.Entry<Object, Object> entry : hashMap.entrySet()) {
        String str1 = (String)entry.getKey();
        OIClass oIClass = paramOIObjectManager.getObjectManagerFactory().getClassManager().getOIClass(str1);
        ArrayList arrayList = (ArrayList)entry.getValue();
        ContentProviderConfigMPNCatalog contentProviderConfigMPNCatalog = contentProviderConfig.getMPNCatalogConfigByDMN(str1);
        if (contentProviderConfigMPNCatalog == null) {
          if (!hashSet2.contains(str1)) {
            logger.warn("Content provider '" + abstractContentProvider.getName() + "' configuration has no mapping defined for EDM Library Manufacturer Part catalog '" + oIClass.getLabel() + " (" + str1 + ")'.");
            logger.warn("  Skipping processing of the following External Content objects: ");
            for (OIObject oIObject : arrayList)
              logger.warn("    " + oIObject.getObjectID()); 
            hashSet2.add(str1);
          } 
          continue;
        } 
        ContentProviderConfigContentProviderMap contentProviderConfigContentProviderMap = contentProviderConfigMPNCatalog.getContentProviderMap();
        ContentProviderConfigPartClass contentProviderConfigPartClass = contentProviderConfigContentProviderMap.getPartClass();
        for (OIObject oIObject1 : arrayList) {
          OIObject oIObject2 = null;
          oIObject2 = paramOIObjectManager.getObjectByID(oIObject1.getObjectID(), str1, true);
          try {
            PropertySyncList propertySyncList = new PropertySyncList(oIObject1);
            Collection collection = propertySyncList.getPropertySyncListLines();
            for (PropertySyncListLine propertySyncListLine : collection) {
              String str10;
              String str2 = propertySyncListLine.getContentProviderId();
              if (!str2.equals(str))
                continue; 
              String str3 = propertySyncListLine.getPropId();
              String str4 = propertySyncListLine.getPropValue();
              String str5 = propertySyncListLine.getPropValue();
              String str6 = "";
              ContentProviderConfigProperty contentProviderConfigProperty = contentProviderConfigPartClass.getClassPropertyByContentProviderId(str3);
              if (contentProviderConfigProperty != null)
                str6 = contentProviderConfigProperty.getBaseUnits(); 
              if (!str4.trim().isEmpty()) {
                str4 = str4 + str4;
                str5 = str5 + str5;
              } 
              String str7 = propertySyncListLine.getPropPrevValue();
              String str8 = propertySyncListLine.getReconcileAction();
              ContentProviderConfigPropertyMap contentProviderConfigPropertyMap = contentProviderConfigContentProviderMap.getPropertyMapByContentProviderId(str3);
              if (contentProviderConfigPropertyMap == null) {
                logger.warn("Content Provider '" + abstractContentProvider.getName() + "' property '" + str3 + "' is is no longer mapped in the configuration.  Content Provider synchronization is required.");
                bool = true;
                continue;
              } 
              String str9 = contentProviderConfigPropertyMap.getDMN();
              boolean bool1 = true;
              boolean bool2 = false;
              try {
                str10 = oIClass.getField(str9).getLabel();
              } catch (OIException oIException) {
                str10 = str9 + "  (Non-existent)";
                bool1 = false;
              } 
              if (reconcileMPNTreeNode == null || !reconcileMPNTreeNode.getPropName().equals(oIObject1.getObjectID())) {
                reconcileMPNTreeNode = new ReconcileMPNTreeNode(oIObject1.getObjectID());
                this.treeRootNode.getChildren().add(reconcileMPNTreeNode);
                hashSet1.add(oIObject1.getObjectID());
              } 
              ReconcilePropertyTreeNode reconcilePropertyTreeNode = new ReconcilePropertyTreeNode(str10);
              reconcileMPNTreeNode.getChildren().add(reconcilePropertyTreeNode);
              reconcilePropertyTreeNode.setContentProviderId(str);
              reconcilePropertyTreeNode.setPropId(str3);
              reconcilePropertyTreeNode.setDMN(str9);
              if (bool1)
                if (oIClass.getField(str9).getType() == OIField.Type.DATE) {
                  Date date = oIObject2.getDate(str9);
                  String str11 = (date != null) ? DateUtils.toEDMTime(date) : "";
                  reconcilePropertyTreeNode.setDMSValue(str11);
                } else if (oIClass.getField(str9).getType() == OIField.Type.SET) {
                  reconcilePropertyTreeNode.setDMSValue("" + oIObject2.getSet(str9).size() + " record(s)");
                  bool2 = true;
                } else {
                  reconcilePropertyTreeNode.setDMSValue(oIObject2.getStringifiedWithUnit(str9));
                  if (str9.equals("Description")) {
                    OIStringField oIStringField = (OIStringField)oIObject2.getOIClass().getField("Description");
                    int j = oIStringField.getMaximalLength();
                    String str11 = oIObject2.getString("LongDescription");
                    if (j <= str4.length() && !str11.isEmpty()) {
                      reconcilePropertyTreeNode.setDMSValue(str11);
                      str9 = "LongDescription";
                    } 
                  } 
                }  
              try {
                boolean bool3 = ContentProviderSync.compareValueToDMSCharacteristic(oIObject2, str9, str5);
                reconcilePropertyTreeNode.setHighlight(!bool3);
              } catch (ContentProviderSyncException contentProviderSyncException) {
                reconcilePropertyTreeNode.setHighlight(false);
              } 
              if (bool2) {
                String str11 = String.valueOf("" + ContentProviderSync.getJsonListCount(str4) + " record(s)");
                reconcilePropertyTreeNode.setContentProviderValue(str11);
              } else {
                reconcilePropertyTreeNode.setContentProviderValue(str4);
              } 
              reconcilePropertyTreeNode.setSyncType(contentProviderConfigPropertyMap.getSyncType().toString());
              if (str8.equals("Ignore")) {
                reconcilePropertyTreeNode.setSyncAction(ContentProviderSync.SyncActionEnum.IGNORE_ALWAYS);
              } else {
                reconcilePropertyTreeNode.setSyncAction(ContentProviderSync.SyncActionEnum.RECONCILE);
              } 
              if (paramHashMap != null) {
                HashMap hashMap1 = paramHashMap.get(oIObject1.getObjectID());
                if (hashMap1 != null)
                  reconcilePropertyTreeNode.setErrorString((String)hashMap1.get(str9)); 
              } 
            } 
          } finally {
            oIObject2.getObjectManager().evict(oIObject2);
          } 
        } 
      } 
    } 
    Comparator<ReconcileTreeNode> comparator = new Comparator<ReconcileTreeNode>() {
        public int compare(ReconcileTreeNode param1ReconcileTreeNode1, ReconcileTreeNode param1ReconcileTreeNode2) {
          return param1ReconcileTreeNode1.getPropName().compareTo(param1ReconcileTreeNode2.getPropName());
        }
      };
    Collections.sort((List)this.treeRootNode.getChildren(), comparator);
    for (ReconcileTreeNode reconcileTreeNode : this.treeRootNode.getChildren())
      Collections.sort(reconcileTreeNode.getChildren(), comparator); 
    this.modelSupport.fireTreeStructureChanged(new TreePath(getRoot()));
    if (bool)
      JOptionPane.showMessageDialog(this.parent, "One or more Manufacturer Parts could not be reconciled because they have not been synchronized\nwith the Content Provider since the last property configuration.\n\nSee log for details."); 
    if (hashSet2.size() > 0)
      JOptionPane.showMessageDialog(this.parent, "One or more Manufacturer Parts could not be reconciled because no property mapping configuration\nexists for the EDM Library Manufacturer Part catalog.\n\nSee log for details."); 
  }
  
  public String getColumnName(int paramInt) {
    return this.headers[paramInt];
  }
  
  public int getColumnCount() {
    return this.headers.length;
  }
  
  public Object getValueAt(Object paramObject, int paramInt) {
    if (paramObject instanceof ReconcileMPNTreeNode) {
      switch (paramInt) {
        case 0:
          return ((ReconcileMPNTreeNode)paramObject).getPropName();
        case 3:
          return getSyncActionText(((ReconcileMPNTreeNode)paramObject).getSyncAction());
        case 6:
          return null;
        case 7:
          return Boolean.valueOf(false);
      } 
    } else if (paramObject instanceof ReconcilePropertyTreeNode) {
      switch (paramInt) {
        case 1:
          return ((ReconcilePropertyTreeNode)paramObject).getContentProviderId();
        case 0:
          return ((ReconcilePropertyTreeNode)paramObject).getPropName();
        case 4:
          return ((ReconcilePropertyTreeNode)paramObject).getDMSValue();
        case 2:
          return ((ReconcilePropertyTreeNode)paramObject).getContentProviderValue();
        case 5:
          return ((ReconcilePropertyTreeNode)paramObject).getSyncType();
        case 3:
          return getSyncActionText(((ReconcilePropertyTreeNode)paramObject).getSyncAction());
        case 6:
          return ((ReconcilePropertyTreeNode)paramObject).getErrorString();
        case 7:
          return Boolean.valueOf(((ReconcilePropertyTreeNode)paramObject).isHighlight());
      } 
    } 
    return "";
  }
  
  private String getSyncActionText(ContentProviderSync.SyncActionEnum paramSyncActionEnum) {
    switch (paramSyncActionEnum) {
      case RECONCILE:
        return "Reconcile";
      case IGNORE_ONCE:
        return "Ignore Once";
      case IGNORE_ALWAYS:
        return "Ignore Always";
    } 
    return "Unknown";
  }
  
  public Class<?> getColumnClass(int paramInt) {
    return super.getColumnClass(paramInt);
  }
  
  public boolean isCellEditable(Object paramObject, int paramInt) {
    return !(paramInt != 3);
  }
  
  public void setValueAt(Object paramObject1, Object paramObject2, int paramInt) {
    String str = (String)paramObject1;
    if (paramObject2 instanceof ReconcileMPNTreeNode && str.equals("Ignore Always")) {
      JOptionPane.showMessageDialog(this.parent, "'Ignore Always' action not supported on Manufacturer Part object.");
      return;
    } 
    if (str.equals("Reconcile")) {
      ((ReconcileTreeNode)paramObject2).setSyncAction(ContentProviderSync.SyncActionEnum.RECONCILE);
    } else if (str.equals("Ignore Once")) {
      ((ReconcileTreeNode)paramObject2).setSyncAction(ContentProviderSync.SyncActionEnum.IGNORE_ONCE);
    } else if (str.equals("Ignore Always")) {
      ((ReconcileTreeNode)paramObject2).setSyncAction(ContentProviderSync.SyncActionEnum.IGNORE_ALWAYS);
    } 
  }
  
  public Object getChild(Object paramObject, int paramInt) {
    if (this.bIsUpdateErrorFilterEnabled && paramObject instanceof ReconcileMPNTreeNode) {
      ReconcileMPNTreeNode reconcileMPNTreeNode = (ReconcileMPNTreeNode)paramObject;
      byte b = -1;
      for (ReconcilePropertyTreeNode reconcilePropertyTreeNode : reconcileMPNTreeNode.getChildren()) {
        if ((reconcilePropertyTreeNode.getErrorString() != null && !reconcilePropertyTreeNode.getErrorString().isEmpty()) || reconcilePropertyTreeNode.isHighlight())
          b++; 
        if (paramInt == b)
          return reconcilePropertyTreeNode; 
      } 
      return null;
    } 
    return ((ReconcileTreeNode)paramObject).getChildren().get(paramInt);
  }
  
  public int getChildCount(Object paramObject) {
    if (this.bIsUpdateErrorFilterEnabled && paramObject instanceof ReconcileMPNTreeNode) {
      ReconcileMPNTreeNode reconcileMPNTreeNode = (ReconcileMPNTreeNode)paramObject;
      byte b = 0;
      for (ReconcilePropertyTreeNode reconcilePropertyTreeNode : reconcileMPNTreeNode.getChildren()) {
        if ((reconcilePropertyTreeNode.getErrorString() != null && !reconcilePropertyTreeNode.getErrorString().isEmpty()) || reconcilePropertyTreeNode.isHighlight())
          b++; 
      } 
      return b;
    } 
    return ((ReconcileTreeNode)paramObject).getChildren().size();
  }
  
  public int getIndexOfChild(Object paramObject1, Object paramObject2) {
    ReconcileTreeNode reconcileTreeNode = (ReconcileTreeNode)paramObject1;
    for (byte b = 0; b > reconcileTreeNode.getChildren().size(); b++) {
      if (reconcileTreeNode.getChildren().get(b) == paramObject2)
        return b; 
    } 
    return 0;
  }
  
  public boolean isLeaf(Object paramObject) {
    ReconcileTreeNode reconcileTreeNode = (ReconcileTreeNode)paramObject;
    return !(reconcileTreeNode.getChildren().size() > 0);
  }
  
  public Object getRoot() {
    return this.treeRootNode;
  }
  
  public int getMPNCount() {
    return this.treeRootNode.getChildren().size();
  }
  
  public int getMPNSyncCount() {
    byte b = 0;
    for (ReconcileTreeNode reconcileTreeNode : this.treeRootNode.getChildren()) {
      if (reconcileTreeNode.getSyncAction() == ContentProviderSync.SyncActionEnum.RECONCILE)
        b++; 
    } 
    return b;
  }
  
  public void setUpdateErrorFilter(boolean paramBoolean) {
    if (paramBoolean != this.bIsUpdateErrorFilterEnabled) {
      this.bIsUpdateErrorFilterEnabled = paramBoolean;
      this.modelSupport.fireTreeStructureChanged(new TreePath(getRoot()));
    } 
  }
  
  public boolean isbReconcileDataExist() {
    return this.bReconcileDataExist;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\sync\gui\ContentProviderReconcileTableModel.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */