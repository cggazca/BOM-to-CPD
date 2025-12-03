package com.mentor.dms.contentprovider.plugin.searchui;

import com.mentor.datafusion.utils.logger.MGLogger;
import com.mentor.dms.contentprovider.config.ContentProviderConfig;
import com.mentor.dms.contentprovider.config.ContentProviderConfigException;
import java.util.concurrent.ExecutionException;
import javax.swing.SwingWorker;

public class LoadConfigAndUpdateRestrictionTextTask extends SwingWorker<ContentProviderConfig, ContentProviderSearchPanel> {
  static MGLogger logger = MGLogger.getLogger(LoadConfigAndUpdateRestrictionTextTask.class);
  
  private ContentProviderSearchPanel searchPanel;
  
  public LoadConfigAndUpdateRestrictionTextTask(ContentProviderSearchPanel paramContentProviderSearchPanel) {
    this.searchPanel = paramContentProviderSearchPanel;
  }
  
  public ContentProviderConfig doInBackground() throws ContentProviderConfigException {
    return this.searchPanel.getContentProvider().getConfig();
  }
  
  public void done() {
    try {
      ContentProviderConfig contentProviderConfig = get();
    } catch (InterruptedException interruptedException) {
      logger.warn(interruptedException.getMessage());
    } catch (ExecutionException executionException) {
      logger.warn(executionException.getMessage());
    } 
    this.searchPanel.doUpdateRestrictions();
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\plugin\searchui\LoadConfigAndUpdateRestrictionTextTask.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */