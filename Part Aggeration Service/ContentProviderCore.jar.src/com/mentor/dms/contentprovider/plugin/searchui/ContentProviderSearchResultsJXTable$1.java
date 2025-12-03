package com.mentor.dms.contentprovider.plugin.searchui;

import com.mentor.dms.contentprovider.ContentProviderException;
import com.mentor.dms.contentprovider.ContentProviderGlobal;
import com.mentor.dms.contentprovider.IContentProviderResultRecord;
import java.awt.Component;
import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.decorator.HighlightPredicate;

class null implements HighlightPredicate {
  public boolean isHighlighted(Component paramComponent, ComponentAdapter paramComponentAdapter) {
    ContentProviderSearchResultsTableModel contentProviderSearchResultsTableModel = (ContentProviderSearchResultsTableModel)ContentProviderSearchResultsJXTable.this.getModel();
    IContentProviderResultRecord iContentProviderResultRecord = contentProviderSearchResultsTableModel.getResultRecordAt(ContentProviderSearchResultsJXTable.this.convertRowIndexToModel(paramComponentAdapter.row));
    boolean bool = false;
    try {
      bool = iContentProviderResultRecord.isExistsInDMS(ContentProviderGlobal.getOIObjectManager());
    } catch (ContentProviderException contentProviderException) {
      ContentProviderSearchResultsJXTable.log.error(contentProviderException);
    } 
    return bool;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\plugin\searchui\ContentProviderSearchResultsJXTable$1.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */