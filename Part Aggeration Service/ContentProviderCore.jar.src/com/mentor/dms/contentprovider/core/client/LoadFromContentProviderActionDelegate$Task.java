package com.mentor.dms.contentprovider.core.client;

import com.mentor.datafusion.oi.OIException;
import com.mentor.datafusion.oi.type.OIObject;
import com.mentor.datafusion.oi.type.OIProxyObject;
import com.mentor.dms.contentprovider.core.ContentProviderGlobal;
import com.mentor.dms.contentprovider.core.config.ContentProviderConfig;
import com.mentor.dms.contentprovider.core.config.ContentProviderConfigException;
import com.mentor.dms.contentprovider.core.sync.ContentProviderSync;
import com.mentor.dms.contentprovider.core.sync.ContentProviderSyncException;
import com.mentor.dms.contentprovider.core.sync.gui.ContentProviderReconcileApp;
import java.util.Date;
import javax.swing.SwingWorker;

class Task extends SwingWorker<Void, Void> {
  public Void doInBackground() {
    byte b = 1;
    setProgress(0);
    LoadFromContentProviderActionDelegate.this.sbErrors.setLength(0);
    for (OIProxyObject oIProxyObject : LoadFromContentProviderActionDelegate.this.resultList) {
      if (isCancelled())
        return null; 
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
        String str = null;
        if (oIProxyObject.getObjectID() != null) {
          str = "Unable to get referenced External Content object for Manufacturer Part '" + oIProxyObject.getObjectID() + "'";
        } else {
          try {
            str = "Unable to get referenced External Content object for Manufacturer Part '" + String.valueOf(oIProxyObject.get("ManufacturerpartId")) + "'";
          } catch (OIException oIException) {
            str = "Unable to get referenced External Content object for Manufacturer Part ''.";
          } 
        } 
        LoadFromContentProviderActionDelegate.this.log.warn(str);
        LoadFromContentProviderActionDelegate.this.sbErrors.append(str + "\n");
        continue;
      } 
      try {
        ContentProviderConfig contentProviderConfig = LoadFromContentProviderActionDelegate.this.ccp.getConfig();
        ContentProviderSync.syncExernalContentPartRecordToDMS(oIObject2, oIObject1, LoadFromContentProviderActionDelegate.this.ccp, new Date(), true);
      } catch (ContentProviderSyncException contentProviderSyncException) {
        LoadFromContentProviderActionDelegate.this.sbErrors.append(contentProviderSyncException.getMessage() + "\n");
      } catch (ContentProviderConfigException contentProviderConfigException) {
        LoadFromContentProviderActionDelegate.this.sbErrors.append(contentProviderConfigException.getMessage() + "\n");
      } 
    } 
    return null;
  }
  
  public void done() {
    LoadFromContentProviderActionDelegate.this.progressMonitor.close();
    if (ContentProviderGlobal.getRootFrame() != null) {
      ContentProviderGlobal.getRootFrame().getGlassPane().setVisible(false);
      ContentProviderGlobal.getRootFrame().setGlassPane(LoadFromContentProviderActionDelegate.this.orgGlassPane);
    } 
    String str = LoadFromContentProviderActionDelegate.this.sbErrors.toString();
    if (!str.isEmpty())
      LogWindow.displayText("Load from External Content", str); 
    ContentProviderReconcileApp.createAndShowGUI(ContentProviderGlobal.getDMSInstance().getObjectManager(), LoadFromContentProviderActionDelegate.this.mpnIDSet);
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\client\LoadFromContentProviderActionDelegate$Task.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */