package com.mentor.dms.contentprovider.core.actions;

import com.mentor.datafusion.utils.logger.MGLogger;
import com.mentor.dms.contentprovider.core.AbstractContentProvider;
import com.mentor.dms.contentprovider.core.ContentProviderException;
import com.mentor.dms.contentprovider.core.ContentProviderGlobal;
import com.mentor.dms.contentprovider.core.IContentProviderResultRecord;
import com.mentor.dms.contentprovider.core.plugin.ContentProviderViewCompareWindow;
import com.mentor.dms.contentprovider.core.plugin.searchui.ContentProviderSearchWindow;
import java.awt.Component;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.ProgressMonitor;
import javax.swing.SwingWorker;

public class ViewCompareAction extends AbstractSearchResultsAction implements PropertyChangeListener {
  private static MGLogger log = MGLogger.getLogger(ViewCompareAction.class);
  
  private ProgressMonitor progressMonitor;
  
  private Task task;
  
  private boolean bTaskInProgress = false;
  
  private List<IContentProviderResultRecord> partRecList;
  
  public ViewCompareAction() {
    super("View/Compare selected...", getImageIcon());
    setEnabled(false);
  }
  
  private static ImageIcon getImageIcon() {
    Image image = Toolkit.getDefaultToolkit().getImage(ViewCompareAction.class.getResource("images/view_object.png"));
    return new ImageIcon(image);
  }
  
  public void actionPerformed(ActionEvent paramActionEvent) {
    doAction();
  }
  
  public void selectionHandler() {
    System.out.println("Task in progress = " + this.bTaskInProgress);
    setEnabled((ContentProviderSearchWindow.getSelectionCount() > 0 && !this.bTaskInProgress));
  }
  
  public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent) {
    if ("progress" == paramPropertyChangeEvent.getPropertyName() && (this.progressMonitor.isCanceled() || this.task.isDone()) && this.progressMonitor.isCanceled())
      this.task.cancel(false); 
  }
  
  public void doAction() {
    if (ContentProviderSearchWindow.getSelectedResults().size() > 10) {
      JOptionPane.showMessageDialog(ContentProviderGlobal.getRootFrame(), "A maximum of 10 parts can be compared.");
      return;
    } 
    log.info("View/Compare selected");
    this.progressMonitor = new ProgressMonitor((Component)ContentProviderSearchWindow.getInstance(), "Retrieving details for:", "", 0, ContentProviderSearchWindow.getSelectedResults().size());
    this.progressMonitor.setProgress(0);
    this.progressMonitor.setMillisToDecideToPopup(0);
    this.progressMonitor.setMillisToPopup(0);
    this.bTaskInProgress = true;
    this.task = new Task();
    this.task.addPropertyChangeListener(this);
    this.task.execute();
  }
  
  class Task extends SwingWorker<Void, Void> {
    public Void doInBackground() {
      setProgress(0);
      byte b = 0;
      try {
        AbstractContentProvider abstractContentProvider = ContentProviderSearchWindow.getSelectedContentProvider();
        ViewCompareAction.this.partRecList = new ArrayList<>();
        for (IContentProviderResultRecord iContentProviderResultRecord : ContentProviderSearchWindow.getSelectedResults()) {
          ViewCompareAction.this.progressMonitor.setNote(iContentProviderResultRecord.getPartNumber());
          ViewCompareAction.this.progressMonitor.setProgress(b);
          int i = b * 100 / ContentProviderSearchWindow.getSelectedResults().size();
          setProgress(i);
          b++;
          ViewCompareAction.this.partRecList.add(abstractContentProvider.getPart(iContentProviderResultRecord));
          if (isCancelled())
            break; 
        } 
      } catch (ContentProviderException contentProviderException) {
        ViewCompareAction.log.error(contentProviderException);
      } 
      return null;
    }
    
    public void done() {
      ViewCompareAction.this.bTaskInProgress = false;
      ViewCompareAction.this.progressMonitor.close();
      if (!isCancelled())
        ContentProviderViewCompareWindow.show((JFrame)ContentProviderSearchWindow.getInstance(), ViewCompareAction.this.partRecList); 
    }
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\actions\ViewCompareAction.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */