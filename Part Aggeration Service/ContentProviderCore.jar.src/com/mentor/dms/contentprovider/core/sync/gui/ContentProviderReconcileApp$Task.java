package com.mentor.dms.contentprovider.core.sync.gui;

import com.mentor.datafusion.oi.OIException;
import com.mentor.datafusion.oi.type.OIObject;
import com.mentor.dms.contentprovider.core.config.ContentProviderConfigException;
import com.mentor.dms.contentprovider.core.sync.ContentProviderSync;
import com.mentor.dms.contentprovider.core.sync.ContentProviderSyncException;
import com.mentor.dms.contentprovider.core.sync.ReconcileResultCounter;
import java.util.HashMap;
import java.util.HashSet;
import javax.swing.SwingWorker;

class Task extends SwingWorker<Void, Void> {
  public Void doInBackground() {
    byte b = 0;
    setProgress(0);
    ContentProviderReconcileApp.this.errorHash.clear();
    new ReconcileResultCounter(ContentProviderReconcileApp.this.tm.getMPNSyncCount());
    for (ReconcileTreeNode reconcileTreeNode : ((ReconcileTreeNode)ContentProviderReconcileApp.this.tm.getRoot()).getChildren()) {
      if (reconcileTreeNode.getSyncAction() != ContentProviderSync.SyncActionEnum.RECONCILE)
        continue; 
      HashMap<Object, Object> hashMap1 = new HashMap<>();
      ContentProviderReconcileApp.this.errorHash.put(reconcileTreeNode.getPropName(), hashMap1);
      if (ContentProviderReconcileApp.this.progressMonitor != null) {
        ContentProviderReconcileApp.this.progressMonitor.setNote(reconcileTreeNode.getPropName());
        ContentProviderReconcileApp.this.progressMonitor.setProgress(b);
      } 
      int i = b * 100 / ContentProviderReconcileApp.this.tm.getMPNSyncCount();
      setProgress(i);
      b++;
      HashMap<Object, Object> hashMap2 = new HashMap<>();
      HashSet<String> hashSet = new HashSet();
      for (ReconcileTreeNode reconcileTreeNode1 : reconcileTreeNode.getChildren()) {
        if (reconcileTreeNode1.getSyncAction() == ContentProviderSync.SyncActionEnum.IGNORE_ONCE) {
          hashMap2.put(((ReconcilePropertyTreeNode)reconcileTreeNode1).getPropId(), ((ReconcilePropertyTreeNode)reconcileTreeNode1).getDMSValue());
          continue;
        } 
        if (reconcileTreeNode1.getSyncAction() == ContentProviderSync.SyncActionEnum.IGNORE_ALWAYS) {
          hashSet.add(((ReconcilePropertyTreeNode)reconcileTreeNode1).getPropId());
          hashMap2.put(((ReconcilePropertyTreeNode)reconcileTreeNode1).getPropId(), ((ReconcilePropertyTreeNode)reconcileTreeNode1).getDMSValue());
        } 
      } 
      if (isCancelled())
        break; 
      try {
        OIObject oIObject = ContentProviderReconcileApp.this.om.getObjectByID(reconcileTreeNode.getPropName(), "ExternalContent", true);
        oIObject.getObjectManager().refreshAndLockObject(oIObject);
        if (hashSet.size() > 0)
          ContentProviderSync.setReconcileActions(oIObject, hashSet); 
        ContentProviderSync.compareAndReconcile(null, oIObject, true, hashMap2, hashMap1);
      } catch (OIException oIException) {
        ContentProviderReconcileApp.logger.error(oIException.getMessage());
      } catch (ContentProviderSyncException contentProviderSyncException) {
        ContentProviderReconcileApp.logger.error(contentProviderSyncException.getMessage());
      } catch (ContentProviderConfigException contentProviderConfigException) {
        ContentProviderReconcileApp.logger.error(contentProviderConfigException.getMessage());
      } 
      ReconcileResultCounter.addiReconcileCount();
      if (isCancelled())
        break; 
    } 
    return null;
  }
  
  public void done() {
    if (ContentProviderReconcileApp.this.progressMonitor != null)
      ContentProviderReconcileApp.this.progressMonitor.close(); 
    ContentProviderReconcileApp.this.reconcileMenuItem.setEnabled(true);
    ContentProviderReconcileApp.this.doLoad();
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\sync\gui\ContentProviderReconcileApp$Task.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */