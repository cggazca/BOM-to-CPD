package com.mentor.dms.contentprovider.plugin.searchui;

import com.mentor.datafusion.utils.logger.MGLogger;
import com.mentor.dms.contentprovider.ContentProviderException;
import com.mentor.dms.contentprovider.config.ContentProviderConfig;
import com.mentor.dms.contentprovider.config.ContentProviderConfigException;
import java.util.concurrent.ExecutionException;
import javax.swing.SwingWorker;

public class LoadConfigAndSearchTask extends SwingWorker<ContentProviderConfig, ContentProviderSearchPanel> {
  private static MGLogger log = MGLogger.getLogger(LoadConfigAndSearchTask.class);
  
  private ContentProviderSearchPanel searchPanel;
  
  public LoadConfigAndSearchTask(ContentProviderSearchPanel paramContentProviderSearchPanel) {
    this.searchPanel = paramContentProviderSearchPanel;
  }
  
  public ContentProviderConfig doInBackground() throws ContentProviderConfigException {
    return this.searchPanel.getContentProvider().getConfig();
  }
  
  public void done() {
    try {
      ContentProviderConfig contentProviderConfig = get();
    } catch (InterruptedException interruptedException) {
      log.error(interruptedException.getMessage());
    } catch (ExecutionException executionException) {
      log.error(executionException.getMessage());
    } 
    try {
      this.searchPanel.doSearchTask();
    } catch (ContentProviderException contentProviderException) {
      log.error(contentProviderException.getMessage());
    } 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\plugin\searchui\LoadConfigAndSearchTask.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */