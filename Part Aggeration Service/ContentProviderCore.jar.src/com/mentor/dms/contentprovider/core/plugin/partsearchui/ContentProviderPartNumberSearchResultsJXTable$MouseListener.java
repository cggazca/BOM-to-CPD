package com.mentor.dms.contentprovider.core.plugin.partsearchui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class MouseListener extends MouseAdapter {
  public void mouseClicked(MouseEvent paramMouseEvent) {
    if (paramMouseEvent.getClickCount() == 2)
      ContentProviderPartNumberSearchResultsJXTable.this.mainPanel.doSelect(); 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\plugin\partsearchui\ContentProviderPartNumberSearchResultsJXTable$MouseListener.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */