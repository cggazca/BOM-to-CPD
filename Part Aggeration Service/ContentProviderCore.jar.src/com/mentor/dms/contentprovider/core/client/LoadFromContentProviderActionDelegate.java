package com.mentor.dms.contentprovider.core.client;

import com.mentor.datafusion.oi.OIException;
import com.mentor.datafusion.oi.model.OIClass;
import com.mentor.datafusion.oi.type.OIObject;
import com.mentor.datafusion.oi.type.OIProxyObject;
import com.mentor.datafusion.utils.logger.MGLogger;
import com.mentor.dms.contentprovider.core.AbstractContentProvider;
import com.mentor.dms.contentprovider.core.ContentProviderException;
import com.mentor.dms.contentprovider.core.ContentProviderGlobal;
import com.mentor.dms.contentprovider.core.ContentProviderRegistryEntry;
import com.mentor.dms.contentprovider.core.config.ContentProviderConfig;
import com.mentor.dms.contentprovider.core.config.ContentProviderConfigException;
import com.mentor.dms.contentprovider.core.plugin.Activator;
import com.mentor.dms.contentprovider.core.plugin.searchui.ContentProviderSelectDlg;
import com.mentor.dms.contentprovider.core.sync.ContentProviderSync;
import com.mentor.dms.contentprovider.core.sync.ContentProviderSyncException;
import com.mentor.dms.contentprovider.core.sync.gui.ContentProviderReconcileApp;
import com.mentor.dms.platform.ui.AbstractActionDelegate;
import com.mentor.dms.ui.searchmask.SearchMask;
import com.mentor.dms.ui.searchresult.SearchResult;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.DefaultFocusTraversalPolicy;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.ProgressMonitor;
import javax.swing.SwingWorker;

public class LoadFromContentProviderActionDelegate extends AbstractActionDelegate implements PropertyChangeListener {
  MGLogger log = MGLogger.getLogger(LoadFromContentProviderActionDelegate.class);
  
  private ProgressMonitor progressMonitor;
  
  private Task task;
  
  private StringBuilder sbErrors = new StringBuilder();
  
  private List<OIProxyObject> resultList;
  
  private AbstractContentProvider ccp = null;
  
  private HashSet<String> mpnIDSet = new HashSet<>();
  
  private Component orgGlassPane;
  
  public void actionPerformed(Action paramAction, ActionEvent paramActionEvent) {
    if (ContentProviderGlobal.getRootFrame() != null) {
      this.orgGlassPane = ContentProviderGlobal.getRootFrame().getGlassPane();
      ContentProviderGlobal.getRootFrame().setGlassPane(new LockingGlassPane());
      ContentProviderGlobal.getRootFrame().getGlassPane().setVisible(true);
    } 
    SearchMask searchMask = (SearchMask)Activator.getDefault().getDMSInstance().getSearchMaskManager().getActive();
    OIClass oIClass = searchMask.getOIClass();
    SearchResult searchResult = Activator.getDefault().getDMSInstance().getSearchResultManager().getSearchResult(oIClass);
    this.resultList = searchResult.getSelection();
    if (ContentProviderGlobal.getRootFrame() != null) {
      ContentProviderGlobal.getRootFrame().getGlassPane().setVisible(false);
      ContentProviderGlobal.getRootFrame().setGlassPane(this.orgGlassPane);
    } 
    if (this.resultList.isEmpty()) {
      JOptionPane.showMessageDialog(ContentProviderGlobal.getRootFrame(), "No Manufacturer Parts selected.", "Load from External Content", 0);
      return;
    } 
    if (this.resultList.size() > 100) {
      JOptionPane.showMessageDialog(ContentProviderGlobal.getRootFrame(), "A maximum of 100 Manufacturer Parts can be selected.", "Load from External Content", 0);
      return;
    } 
    try {
      this.ccp = ContentProviderSelectDlg.selectContentProvider(ContentProviderRegistryEntry.ContentProviderRole.MANUFACTURER_PART_CREATION);
    } catch (ContentProviderException contentProviderException) {
      JOptionPane.showMessageDialog(ContentProviderGlobal.getRootFrame(), contentProviderException.getMessage(), "Load from External Content", 0);
      return;
    } 
    if (this.ccp == null)
      return; 
    int i = JOptionPane.showConfirmDialog(ContentProviderGlobal.getRootFrame(), "Update External Content to " + this.resultList.size() + " selected Manufacturer Part(s)?", "Update from External Content", 0);
    if (i != 0)
      return; 
    this.progressMonitor = new ProgressMonitor(ContentProviderGlobal.getRootFrame(), "Update from External Content...", "", 0, this.resultList.size());
    this.progressMonitor.setProgress(0);
    this.progressMonitor.setMillisToDecideToPopup(0);
    this.progressMonitor.setMillisToPopup(0);
    if (ContentProviderGlobal.getRootFrame() != null) {
      this.orgGlassPane = ContentProviderGlobal.getRootFrame().getGlassPane();
      ContentProviderGlobal.getRootFrame().setGlassPane(new LockingGlassPane());
      ContentProviderGlobal.getRootFrame().getGlassPane().setVisible(true);
    } 
    this.mpnIDSet = new HashSet<>();
    this.task = new Task();
    this.task.addPropertyChangeListener(this);
    this.task.execute();
  }
  
  public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent) {
    if ("progress" == paramPropertyChangeEvent.getPropertyName() && (this.progressMonitor.isCanceled() || this.task.isDone()) && this.progressMonitor.isCanceled())
      this.task.cancel(false); 
  }
  
  private class LockingGlassPane extends JComponent {
    public LockingGlassPane() {
      setOpaque(false);
      setFocusTraversalPolicy(new DefaultFocusTraversalPolicy() {
            public boolean accept(Component param2Component) {
              return false;
            }
          });
      addKeyListener(new KeyAdapter() {
          
          });
      addMouseListener(new MouseAdapter() {
          
          });
      requestFocusInWindow();
      setCursor(Cursor.getPredefinedCursor(3));
    }
    
    public void setVisible(boolean param1Boolean) {
      super.setVisible(param1Boolean);
      setFocusTraversalPolicyProvider(param1Boolean);
    }
  }
  
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
  
  class null extends MouseAdapter {}
  
  class null extends KeyAdapter {}
  
  class null extends DefaultFocusTraversalPolicy {
    public boolean accept(Component param1Component) {
      return false;
    }
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\client\LoadFromContentProviderActionDelegate.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */