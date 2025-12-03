package com.mentor.dms.contentprovider;

import com.mentor.dms.contentprovider.plugin.searchui.ContentProviderSearchWindow;
import java.util.Collection;

public class ContentProviderSearch {
  public static AbstractContentProvider getActiveContentProvider() {
    return ContentProviderSearchWindow.getSelectedContentProvider();
  }
  
  public static Collection<IContentProviderResultRecord> getSelectedSearchResults() {
    return ContentProviderSearchWindow.getSelectedResults();
  }
  
  public static int getSelectedSearchResultCount() {
    return ContentProviderSearchWindow.getSelectionCount();
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\ContentProviderSearch.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */