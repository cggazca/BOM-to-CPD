package com.mentor.dms.contentprovider.core.plugin.mfgsearchui;

import java.awt.Component;
import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.decorator.HighlightPredicate;

class null implements HighlightPredicate {
  public boolean isHighlighted(Component paramComponent, ComponentAdapter paramComponentAdapter) {
    ContentProviderManufacturerSearchResultsTableModel contentProviderManufacturerSearchResultsTableModel = (ContentProviderManufacturerSearchResultsTableModel)ContentProviderManufacturerSearchResultsJXTable.this.getModel();
    String str = contentProviderManufacturerSearchResultsTableModel.getManufacturerAt(ContentProviderManufacturerSearchResultsJXTable.this.convertRowIndexToModel(paramComponentAdapter.row));
    return mainPanel.isExistingMfg(str);
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\plugin\mfgsearchui\ContentProviderManufacturerSearchResultsJXTable$1.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */