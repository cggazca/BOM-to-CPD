package com.mentor.dms.contentprovider.sync.gui;

import com.mentor.datafusion.oi.OIException;
import com.mentor.datafusion.oi.type.OIObject;
import com.mentor.dms.contentprovider.config.ContentProviderConfigException;
import com.mentor.dms.contentprovider.sync.ContentProviderSync;
import com.mentor.dms.contentprovider.sync.ContentProviderSyncException;
import java.util.HashMap;
import java.util.HashSet;
import javax.swing.SwingWorker;

class Task extends SwingWorker<Void, Void> {
  public Void doInBackground() {
    byte b = 0;
    setProgress(0);
    ContentProviderReconcileApp.this.errorHash.clear();
    for (ReconcileTreeNode reconcileTreeNode : ((ReconcileTreeNode)ContentProviderReconcileApp.this.tm.getRoot()).getChildren()) {
      if (reconcileTreeNode.getSyncAction() != ContentProviderSync.SyncActionEnum.RECONCILE)
        continue; 
      HashMap<Object, Object> hashMap = new HashMap<>();
      ContentProviderReconcileApp.this.errorHash.put(reconcileTreeNode.getPropName(), hashMap);
      ContentProviderReconcileApp.this.progressMonitor.setNote(reconcileTreeNode.getPropName());
      ContentProviderReconcileApp.this.progressMonitor.setProgress(b);
      int i = b * 100 / ContentProviderReconcileApp.this.tm.getMPNSyncCount();
      setProgress(i);
      b++;
      HashSet<String> hashSet1 = new HashSet();
      HashSet<String> hashSet2 = new HashSet();
      for (ReconcileTreeNode reconcileTreeNode1 : reconcileTreeNode.getChildren()) {
        if (reconcileTreeNode1.getSyncAction() == ContentProviderSync.SyncActionEnum.IGNORE_ONCE) {
          hashSet1.add(((ReconcilePropertyTreeNode)reconcileTreeNode1).getPropId());
          continue;
        } 
        if (reconcileTreeNode1.getSyncAction() == ContentProviderSync.SyncActionEnum.IGNORE_ALWAYS)
          hashSet2.add(((ReconcilePropertyTreeNode)reconcileTreeNode1).getPropId()); 
      } 
      try {
        OIObject oIObject = ContentProviderReconcileApp.this.om.getObjectByID(reconcileTreeNode.getPropName(), "ExternalContent", true);
        oIObject.getObjectManager().refreshAndLockObject(oIObject);
        if (hashSet2.size() > 0)
          ContentProviderSync.setReconcileActions(oIObject, hashSet2); 
        ContentProviderSync.compareAndReconcile(null, oIObject, true, hashSet1, hashMap);
      } catch (OIException oIException) {
        ContentProviderReconcileApp.logger.error(oIException.getMessage());
      } catch (ContentProviderSyncException contentProviderSyncException) {
        ContentProviderReconcileApp.logger.error(contentProviderSyncException.getMessage());
      } catch (ContentProviderConfigException contentProviderConfigException) {
        ContentProviderReconcileApp.logger.error(contentProviderConfigException.getMessage());
      } 
      if (isCancelled())
        break; 
    } 
    return null;
  }
  
  public void done() {
    ContentProviderReconcileApp.this.progressMonitor.close();
    ContentProviderReconcileApp.this.reconcileMenuItem.setEnabled(true);
    ContentProviderReconcileApp.this.doLoad();
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\sync\gui\ContentProviderReconcileApp$Task.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */