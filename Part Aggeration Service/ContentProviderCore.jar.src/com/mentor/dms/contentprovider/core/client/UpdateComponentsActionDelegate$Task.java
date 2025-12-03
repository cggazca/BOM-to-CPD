package com.mentor.dms.contentprovider.core.client;

import com.mentor.datafusion.oi.OIException;
import com.mentor.datafusion.oi.type.OIObject;
import com.mentor.datafusion.oi.type.OIProxyObject;
import com.mentor.dms.contentprovider.core.ContentProviderException;
import com.mentor.dms.contentprovider.core.ContentProviderGlobal;
import com.mentor.dms.contentprovider.core.sync.ContentProviderSync;
import javax.swing.SwingWorker;

class Task extends SwingWorker<Void, Void> {
  public Void doInBackground() {
    byte b = 1;
    setProgress(0);
    UpdateComponentsActionDelegate.this.sbErrors.setLength(0);
    for (OIProxyObject oIProxyObject : UpdateComponentsActionDelegate.this.resultList) {
      if (isCancelled())
        return null; 
      int i = b + 1;
      UpdateComponentsActionDelegate.this.progressMonitor.setNote("Processing (" + i + " of " + UpdateComponentsActionDelegate.this.resultList.size() + ")");
      UpdateComponentsActionDelegate.this.progressMonitor.setProgress(b);
      int j = b * 100 / UpdateComponentsActionDelegate.this.resultList.size();
      setProgress(j);
      b++;
      OIObject oIObject = null;
      try {
        oIObject = oIProxyObject.getObject();
      } catch (OIException oIException) {
        String str = "Error reading Manufacturer Part '" + oIObject.getObjectID() + "': " + oIException.getMessage();
        UpdateComponentsActionDelegate.this.log.warn(str);
        UpdateComponentsActionDelegate.this.sbErrors.append(str + "\n");
        continue;
      } 
      try {
        String str = ContentProviderSync.syncMPN2Comps(oIObject, "", true);
        UpdateComponentsActionDelegate.this.sbErrors.append(str + "\n");
      } catch (ContentProviderException contentProviderException) {
        UpdateComponentsActionDelegate.this.sbErrors.append(contentProviderException.getMessage() + "\n");
      } 
    } 
    return null;
  }
  
  public void done() {
    UpdateComponentsActionDelegate.this.progressMonitor.close();
    if (ContentProviderGlobal.getRootFrame() != null) {
      ContentProviderGlobal.getRootFrame().getGlassPane().setVisible(false);
      ContentProviderGlobal.getRootFrame().setGlassPane(UpdateComponentsActionDelegate.this.orgGlassPane);
    } 
    String str = UpdateComponentsActionDelegate.this.sbErrors.toString();
    if (!str.isEmpty())
      LogWindow.displayText("Update Components", str); 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\client\UpdateComponentsActionDelegate$Task.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */