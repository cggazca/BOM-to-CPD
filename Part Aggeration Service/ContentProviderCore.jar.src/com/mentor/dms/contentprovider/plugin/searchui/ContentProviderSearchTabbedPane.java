package com.mentor.dms.contentprovider.plugin.searchui;

import com.mentor.datafusion.utils.logger.MGLogger;
import com.mentor.dms.contentprovider.ContentProviderException;
import javax.swing.JTabbedPane;

public class ContentProviderSearchTabbedPane extends JTabbedPane {
  private static MGLogger log = MGLogger.getLogger(ContentProviderSearchTabbedPane.class);
  
  private ContentProviderSearchMainPanel mainPanel;
  
  public ContentProviderSearchTabbedPane(ContentProviderSearchMainPanel paramContentProviderSearchMainPanel) {
    this.mainPanel = paramContentProviderSearchMainPanel;
  }
  
  public void setSelectedIndex(int paramInt) {
    if (getSelectedIndex() == -1 || this.mainPanel.isSearchEnabled()) {
      super.setSelectedIndex(paramInt);
      try {
        ContentProviderSearchPanel contentProviderSearchPanel = (ContentProviderSearchPanel)getSelectedComponent();
        contentProviderSearchPanel.doSearch(false);
      } catch (ContentProviderException contentProviderException) {
        log.error(contentProviderException);
      } 
    } 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\plugin\searchui\ContentProviderSearchTabbedPane.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */