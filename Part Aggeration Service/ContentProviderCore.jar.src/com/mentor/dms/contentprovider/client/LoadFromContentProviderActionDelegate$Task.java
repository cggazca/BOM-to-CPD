package com.mentor.dms.contentprovider.client;

import com.mentor.datafusion.oi.OIException;
import com.mentor.datafusion.oi.type.OIObject;
import com.mentor.datafusion.oi.type.OIProxyObject;
import com.mentor.dms.contentprovider.ContentProviderGlobal;
import com.mentor.dms.contentprovider.sync.ContentProviderSync;
import com.mentor.dms.contentprovider.sync.ContentProviderSyncException;
import com.mentor.dms.contentprovider.sync.gui.ContentProviderReconcileApp;
import java.util.Date;
import javax.swing.SwingWorker;

class Task extends SwingWorker<Void, Void> {
  public Void doInBackground() {
    byte b = 1;
    setProgress(0);
    LoadFromContentProviderActionDelegate.this.sbErrors.setLength(0);
    for (OIProxyObject oIProxyObject : LoadFromContentProviderActionDelegate.this.resultList) {
      int i = b + 1;
      LoadFromContentProviderActionDelegate.this.progressMonitor.setNote("Loading (" + i + " of " + LoadFromContentProviderActionDelegate.this.resultList.size() + ")");
      LoadFromContentProviderActionDelegate.this.progressMonitor.setProgress(b);
      int j = b * 100 / LoadFromContentProviderActionDelegate.this.resultList.size();
      setProgress(j);
      b++;
      OIObject oIObject1 = null;
      OIObject oIObject2 = null;
      try {
        oIObject2 = oIProxyObject.getObject();
        oIObject1 = oIObject2.getObject("ExternalContentId");
        LoadFromContentProviderActionDelegate.this.mpnIDSet.add(oIObject2.getObjectID());
      } catch (OIException oIException) {
        String str = "Error reading Manufacturer Part '" + oIObject2.getObjectID() + "': " + oIException.getMessage();
        LoadFromContentProviderActionDelegate.this.log.warn(str);
        LoadFromContentProviderActionDelegate.this.sbErrors.append(str + "\n");
        continue;
      } 
      if (oIObject1 == null) {
        String str = "Unable to get referenced External Content object for Manufacturer Part '" + oIProxyObject.getObjectID() + "'";
        LoadFromContentProviderActionDelegate.this.log.warn(str);
        LoadFromContentProviderActionDelegate.this.sbErrors.append(str + "\n");
        continue;
      } 
      try {
        ContentProviderSync.syncExernalContentPartRecordToDMS(oIObject2, oIObject1, LoadFromContentProviderActionDelegate.this.ccp, new Date(), true);
      } catch (ContentProviderSyncException contentProviderSyncException) {
        LoadFromContentProviderActionDelegate.this.sbErrors.append(contentProviderSyncException.getMessage() + "\n");
      } 
    } 
    return null;
  }
  
  public void done() {
    LoadFromContentProviderActionDelegate.this.progressMonitor.close();
    ContentProviderGlobal.getRootFrame().setEnabled(true);
    String str = LoadFromContentProviderActionDelegate.this.sbErrors.toString();
    if (!str.isEmpty())
      LogWindow.displayText("Load from External Content", str); 
    ContentProviderReconcileApp.createAndShowGUI(ContentProviderGlobal.getDMSInstance().getObjectManager(), LoadFromContentProviderActionDelegate.this.mpnIDSet);
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\client\LoadFromContentProviderActionDelegate$Task.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */