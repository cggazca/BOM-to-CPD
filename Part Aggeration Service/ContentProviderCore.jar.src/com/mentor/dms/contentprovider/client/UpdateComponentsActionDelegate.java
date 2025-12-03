package com.mentor.dms.contentprovider.client;

import com.mentor.datafusion.oi.OIException;
import com.mentor.datafusion.oi.model.OIClass;
import com.mentor.datafusion.oi.type.OIObject;
import com.mentor.datafusion.oi.type.OIProxyObject;
import com.mentor.datafusion.utils.logger.MGLogger;
import com.mentor.dms.contentprovider.AbstractContentProvider;
import com.mentor.dms.contentprovider.ContentProviderException;
import com.mentor.dms.contentprovider.ContentProviderGlobal;
import com.mentor.dms.contentprovider.core.plugin.Activator;
import com.mentor.dms.contentprovider.sync.ContentProviderSync;
import com.mentor.dms.platform.ui.AbstractActionDelegate;
import com.mentor.dms.ui.searchmask.SearchMask;
import com.mentor.dms.ui.searchresult.SearchResult;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.ProgressMonitor;
import javax.swing.SwingWorker;

public class UpdateComponentsActionDelegate extends AbstractActionDelegate implements PropertyChangeListener {
  MGLogger log = MGLogger.getLogger(UpdateComponentsActionDelegate.class);
  
  private ProgressMonitor progressMonitor;
  
  private Task task;
  
  StringBuilder sbErrors = new StringBuilder();
  
  List<OIProxyObject> resultList;
  
  AbstractContentProvider ccp = null;
  
  public void actionPerformed(Action paramAction, ActionEvent paramActionEvent) {
    SearchMask searchMask = (SearchMask)Activator.getDefault().getDMSInstance().getSearchMaskManager().getActive();
    OIClass oIClass = searchMask.getOIClass();
    SearchResult searchResult = Activator.getDefault().getDMSInstance().getSearchResultManager().getSearchResult(oIClass);
    this.resultList = searchResult.getSelection();
    if (this.resultList.isEmpty()) {
      JOptionPane.showMessageDialog(ContentProviderGlobal.getRootFrame(), "No Manufacturer Parts selected.", "Update Components", 0);
      return;
    } 
    if (this.resultList.size() > 100) {
      JOptionPane.showMessageDialog(ContentProviderGlobal.getRootFrame(), "A maximum of 100 Manufacturer Parts can be selected.", "Update Components", 0);
      return;
    } 
    int i = JOptionPane.showConfirmDialog(ContentProviderGlobal.getRootFrame(), "Update Components from " + this.resultList.size() + " selected Manufacturer Part(s)?", "Update Components", 0);
    if (i != 0)
      return; 
    this.progressMonitor = new ProgressMonitor(ContentProviderGlobal.getRootFrame(), "Updating Components...", "", 0, this.resultList.size());
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
      UpdateComponentsActionDelegate.this.sbErrors.setLength(0);
      for (OIProxyObject oIProxyObject : UpdateComponentsActionDelegate.this.resultList) {
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
      ContentProviderGlobal.getRootFrame().setEnabled(true);
      String str = UpdateComponentsActionDelegate.this.sbErrors.toString();
      if (!str.isEmpty())
        LogWindow.displayText("Update Components", str); 
    }
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\client\UpdateComponentsActionDelegate.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */