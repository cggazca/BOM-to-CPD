package com.mentor.dms.contentprovider.client;

import com.mentor.dms.contentprovider.AbstractContentProvider;
import com.mentor.dms.contentprovider.ContentProviderException;
import com.mentor.dms.contentprovider.ContentProviderFactory;
import com.mentor.dms.contentprovider.ContentProviderGlobal;
import com.mentor.dms.contentprovider.ContentProviderPartNotFoundException;
import com.mentor.dms.contentprovider.IContentProviderResultRecord;
import com.mentor.dms.contentprovider.plugin.ContentProviderViewCompareWindow;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

class Task extends SwingWorker<Void, Void> {
  public Void doInBackground() {
    setProgress(0);
    ViewEC.this.progressMonitor.setProgress(0);
    try {
      ContentProviderFactory contentProviderFactory = ContentProviderFactory.getInstance();
      AbstractContentProvider abstractContentProvider = contentProviderFactory.createContentProvider(ViewEC.this.ccpId);
      IContentProviderResultRecord iContentProviderResultRecord = abstractContentProvider.getPart(ViewEC.this.idPropMap);
      if (iContentProviderResultRecord != null) {
        ViewEC.this.partRecList = new ArrayList<>();
        ViewEC.this.partRecList.add(iContentProviderResultRecord);
      } 
      setProgress(50);
      try {
        Thread.sleep(500L);
      } catch (InterruptedException interruptedException) {}
      if (!isCancelled())
        setProgress(100); 
    } catch (ContentProviderPartNotFoundException contentProviderPartNotFoundException) {
      JOptionPane.showMessageDialog(ContentProviderGlobal.getRootFrame(), "Referenced Content Provider part not found.");
    } catch (ContentProviderException contentProviderException) {
      ViewEC.logger.error(contentProviderException.getMessage());
    } 
    return null;
  }
  
  public void done() {
    ViewEC.this.bTaskInProgress = false;
    ViewEC.this.progressMonitor.close();
    if (!isCancelled() && ViewEC.this.partRecList != null && !ViewEC.this.partRecList.isEmpty())
      ContentProviderViewCompareWindow.show(ContentProviderGlobal.getRootFrame(), ViewEC.this.partRecList); 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\client\ViewEC$Task.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */