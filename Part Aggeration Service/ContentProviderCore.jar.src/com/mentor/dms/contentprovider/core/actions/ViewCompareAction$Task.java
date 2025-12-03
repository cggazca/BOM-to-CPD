package com.mentor.dms.contentprovider.core.actions;

import com.mentor.dms.contentprovider.core.AbstractContentProvider;
import com.mentor.dms.contentprovider.core.ContentProviderException;
import com.mentor.dms.contentprovider.core.IContentProviderResultRecord;
import com.mentor.dms.contentprovider.core.plugin.ContentProviderViewCompareWindow;
import com.mentor.dms.contentprovider.core.plugin.searchui.ContentProviderSearchWindow;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.SwingWorker;

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


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\actions\ViewCompareAction$Task.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */