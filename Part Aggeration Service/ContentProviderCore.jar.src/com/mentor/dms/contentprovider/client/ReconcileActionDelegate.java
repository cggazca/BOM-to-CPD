package com.mentor.dms.contentprovider.client;

import com.mentor.datafusion.oi.OIException;
import com.mentor.datafusion.oi.model.OIClass;
import com.mentor.datafusion.oi.type.OIObject;
import com.mentor.datafusion.oi.type.OIProxyObject;
import com.mentor.datafusion.utils.logger.MGLogger;
import com.mentor.dms.contentprovider.AbstractContentProvider;
import com.mentor.dms.contentprovider.ContentProviderException;
import com.mentor.dms.contentprovider.ContentProviderGlobal;
import com.mentor.dms.contentprovider.ContentProviderRegistryEntry;
import com.mentor.dms.contentprovider.core.plugin.Activator;
import com.mentor.dms.contentprovider.plugin.searchui.ContentProviderSelectDlg;
import com.mentor.dms.contentprovider.sync.ContentProviderSync;
import com.mentor.dms.contentprovider.sync.gui.ContentProviderReconcileApp;
import com.mentor.dms.platform.ui.AbstractActionDelegate;
import com.mentor.dms.ui.searchmask.SearchMask;
import com.mentor.dms.ui.searchresult.SearchResult;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashSet;
import java.util.List;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.ProgressMonitor;
import javax.swing.SwingWorker;

public class ReconcileActionDelegate extends AbstractActionDelegate implements PropertyChangeListener {
  MGLogger log = MGLogger.getLogger(ReconcileActionDelegate.class);
  
  private ProgressMonitor progressMonitor;
  
  private Task task;
  
  private StringBuilder sbErrors = new StringBuilder();
  
  private List<OIProxyObject> resultList;
  
  private AbstractContentProvider ccp = null;
  
  private HashSet<String> mpnIDSet = new HashSet<>();
  
  public void actionPerformed(Action paramAction, ActionEvent paramActionEvent) {
    SearchMask searchMask = (SearchMask)Activator.getDefault().getDMSInstance().getSearchMaskManager().getActive();
    OIClass oIClass = searchMask.getOIClass();
    SearchResult searchResult = Activator.getDefault().getDMSInstance().getSearchResultManager().getSearchResult(oIClass);
    this.resultList = searchResult.getSelection();
    if (this.resultList.isEmpty()) {
      JOptionPane.showMessageDialog(ContentProviderGlobal.getRootFrame(), "No Manufacturer Parts selected.", "Reconcile", 0);
      return;
    } 
    if (this.resultList.size() > 100) {
      JOptionPane.showMessageDialog(ContentProviderGlobal.getRootFrame(), "A maximum of 100 Manufacturer Parts can be selected.", "Reconcile", 0);
      return;
    } 
    try {
      this.ccp = ContentProviderSelectDlg.selectContentProvider(ContentProviderRegistryEntry.ContentProviderRole.MANUFACTURER_PART_CREATION);
    } catch (ContentProviderException contentProviderException) {
      JOptionPane.showMessageDialog(ContentProviderGlobal.getRootFrame(), contentProviderException.getMessage(), "Reconcile", 0);
      return;
    } 
    if (this.ccp == null)
      return; 
    int i = JOptionPane.showConfirmDialog(ContentProviderGlobal.getRootFrame(), "Reconcile " + this.resultList.size() + " selected Manufacturer Part(s)?", "Reconcile", 0);
    if (i != 0)
      return; 
    this.progressMonitor = new ProgressMonitor(ContentProviderGlobal.getRootFrame(), "Reconciling...", "", 0, this.resultList.size());
    this.progressMonitor.setProgress(0);
    this.progressMonitor.setMillisToDecideToPopup(0);
    this.progressMonitor.setMillisToPopup(0);
    this.task = new Task();
    this.task.addPropertyChangeListener(this);
    ContentProviderGlobal.getRootFrame().setEnabled(false);
    this.task.execute();
  }
  
  public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent) {
    if ("progress" == paramPropertyChangeEvent.getPropertyName() && (this.progressMonitor.isCanceled() || this.task.isDone()) && this.progressMonitor.isCanceled())
      this.task.cancel(false); 
  }
  
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
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\client\ReconcileActionDelegate.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */