package com.mentor.dms.contentprovider.core.plugin.partsearchui;

import com.mentor.datafusion.utils.logger.MGLogger;
import com.mentor.dms.contentprovider.core.ContentProviderException;
import com.mentor.dms.contentprovider.core.config.ContentProviderConfig;
import com.mentor.dms.contentprovider.core.config.ContentProviderConfigException;
import java.util.concurrent.ExecutionException;
import javax.swing.SwingWorker;

public class ContentProviderPartNumberSearchLoadConfigAndSearchTask extends SwingWorker<ContentProviderConfig, ContentProviderPartNumberSearchMainPanel> {
  private static MGLogger log = MGLogger.getLogger(ContentProviderPartNumberSearchLoadConfigAndSearchTask.class);
  
  private ContentProviderPartNumberSearchMainPanel searchPanel;
  
  public ContentProviderPartNumberSearchLoadConfigAndSearchTask(ContentProviderPartNumberSearchMainPanel paramContentProviderPartNumberSearchMainPanel) {
    this.searchPanel = paramContentProviderPartNumberSearchMainPanel;
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


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\plugin\partsearchui\ContentProviderPartNumberSearchLoadConfigAndSearchTask.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */