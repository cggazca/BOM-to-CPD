package com.mentor.dms.contentprovider.core.client;

import com.mentor.datafusion.oi.OIException;
import com.mentor.datafusion.oi.model.OIField;
import com.mentor.datafusion.oi.type.OIObject;
import com.mentor.datafusion.oi.type.OIProxyObject;
import com.mentor.dms.contentprovider.core.ContentProviderGlobal;
import com.mentor.dms.contentprovider.core.sync.ContentProviderSync;
import com.mentor.dms.contentprovider.core.sync.PropertySyncList;
import com.mentor.dms.contentprovider.core.sync.PropertySyncListLine;
import com.mentor.dms.contentprovider.core.sync.ReconcileResultCounter;
import com.mentor.dms.contentprovider.core.sync.gui.ContentProviderReconcileApp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import javax.swing.SwingWorker;

class Task extends SwingWorker<Void, Void> {
  public Void doInBackground() {
    byte b = 1;
    setProgress(0);
    ReconcileActionDelegate.this.sbErrors.setLength(0);
    new ReconcileResultCounter(ReconcileActionDelegate.this.resultList.size());
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
        String str = null;
        if (oIProxyObject.getObjectID() != null) {
          str = "Unable to get referenced External Content object for Manufacturer Part '" + oIProxyObject.getObjectID() + "'";
        } else {
          try {
            str = "Unable to get referenced External Content object for Manufacturer Part '" + String.valueOf(oIProxyObject.get("ManufacturerpartId")) + "'";
          } catch (OIException oIException) {
            str = "Unable to get referenced External Content object for Manufacturer Part.";
          } 
        } 
        ReconcileActionDelegate.this.log.warn(str);
        ReconcileActionDelegate.this.sbErrors.append(str + "\n");
        continue;
      } 
      try {
        HashMap<Object, Object> hashMap = new HashMap<>();
        PropertySyncList propertySyncList = new PropertySyncList(oIObject1);
        for (PropertySyncListLine propertySyncListLine : propertySyncList.getPropertySyncListLines()) {
          if (propertySyncListLine.getReconcileAction().equals("Ignore")) {
            OIField oIField = oIObject2.getOIClass().getField(propertySyncListLine.getPropId());
            if (oIField.getType() == OIField.Type.DATE) {
              SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
              hashMap.put(propertySyncListLine.getPropId(), simpleDateFormat.format(oIObject2.get(propertySyncListLine.getPropId())));
              continue;
            } 
            if (oIObject2.get(propertySyncListLine.getPropId()) != null) {
              hashMap.put(propertySyncListLine.getPropId(), String.valueOf(oIObject2.get(propertySyncListLine.getPropId())));
              continue;
            } 
            hashMap.put(propertySyncListLine.getPropId(), "");
          } 
        } 
        if (isCancelled())
          break; 
        ContentProviderSync.compareAndReconcile(oIObject2, oIObject1, true, hashMap, null);
      } catch (Exception exception) {
        ReconcileActionDelegate.this.sbErrors.append(exception.getMessage() + "\n");
      } 
      ReconcileResultCounter.addiReconcileCount();
      if (isCancelled())
        break; 
    } 
    return null;
  }
  
  public void done() {
    ReconcileActionDelegate.this.progressMonitor.close();
    if (ContentProviderGlobal.getRootFrame() != null) {
      ContentProviderGlobal.getRootFrame().getGlassPane().setVisible(false);
      ContentProviderGlobal.getRootFrame().setGlassPane(ReconcileActionDelegate.this.orgGlassPane);
    } 
    String str = ReconcileActionDelegate.this.sbErrors.toString();
    if (!str.isEmpty())
      LogWindow.displayText("Reconcile", str); 
    ContentProviderReconcileApp.createAndShowGUI(ContentProviderGlobal.getDMSInstance().getObjectManager(), ReconcileActionDelegate.this.mpnIDSet);
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\client\ReconcileActionDelegate$Task.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */