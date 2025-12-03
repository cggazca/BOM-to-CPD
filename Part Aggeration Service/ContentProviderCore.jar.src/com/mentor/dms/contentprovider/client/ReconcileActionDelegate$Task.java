package com.mentor.dms.contentprovider.client;

import com.mentor.datafusion.oi.OIException;
import com.mentor.datafusion.oi.type.OIObject;
import com.mentor.datafusion.oi.type.OIProxyObject;
import com.mentor.dms.contentprovider.ContentProviderException;
import com.mentor.dms.contentprovider.ContentProviderGlobal;
import com.mentor.dms.contentprovider.sync.ContentProviderSync;
import com.mentor.dms.contentprovider.sync.gui.ContentProviderReconcileApp;
import javax.swing.SwingWorker;

class Task extends SwingWorker<Void, Void> {
  public Void doInBackground() {
    byte b = 1;
    setProgress(0);
    ReconcileActionDelegate.this.sbErrors.setLength(0);
    for (OIProxyObject oIProxyObject : ReconcileActionDelegate.this.resultList) {
      int i = b + 1;
      ReconcileActionDelegate.this.progressMonitor.setNote("Reconciling (" + i + " of " + ReconcileActionDelegate.this.resultList.size() + ")");
      ReconcileActionDelegate.this.progressMonitor.setProgress(b);
      int j = b * 100 / ReconcileActionDelegate.this.resultList.size();
      setProgress(j);
      b++;
      OIObject oIObject1 = null;
      OIObject oIObject2 = null;
      try {
        oIObject2 = oIProxyObject.getObject();
        oIObject1 = oIObject2.getObject("ExternalContentId");
        ReconcileActionDelegate.this.mpnIDSet.add(oIObject2.getObjectID());
      } catch (OIException oIException) {
        String str = "Error reading Manufacturer Part '" + oIObject2.getObjectID() + "': " + oIException.getMessage();
        ReconcileActionDelegate.this.log.warn(str);
        ReconcileActionDelegate.this.sbErrors.append(str + "\n");
        continue;
      } 
      if (oIObject1 == null) {
        String str = "Unable to get referenced External Content object for Manufacturer Part '" + oIProxyObject.getObjectID() + "'";
        ReconcileActionDelegate.this.log.warn(str);
        ReconcileActionDelegate.this.sbErrors.append(str + "\n");
        continue;
      } 
      try {
        ContentProviderSync.compareAndReconcile(oIObject2, oIObject1, true, null, null);
      } catch (ContentProviderException contentProviderException) {
        ReconcileActionDelegate.this.sbErrors.append(contentProviderException.getMessage() + "\n");
      } 
    } 
    return null;
  }
  
  public void done() {
    ReconcileActionDelegate.this.progressMonitor.close();
    ContentProviderGlobal.getRootFrame().setEnabled(true);
    String str = ReconcileActionDelegate.this.sbErrors.toString();
    if (!str.isEmpty())
      LogWindow.displayText("Reconcile", str); 
    ContentProviderReconcileApp.createAndShowGUI(ContentProviderGlobal.getDMSInstance().getObjectManager(), ReconcileActionDelegate.this.mpnIDSet);
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\client\ReconcileActionDelegate$Task.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */