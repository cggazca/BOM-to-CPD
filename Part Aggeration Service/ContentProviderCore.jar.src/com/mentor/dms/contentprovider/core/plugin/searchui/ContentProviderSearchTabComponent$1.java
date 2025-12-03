package com.mentor.dms.contentprovider.core.plugin.searchui;

import javax.swing.JLabel;
import javax.swing.JTabbedPane;

class null extends JLabel {
  public String getText() {
    int i = pane.indexOfTabComponent(ContentProviderSearchTabComponent.this);
    return (i != -1) ? pane.getTitleAt(i) : null;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\plugin\searchui\ContentProviderSearchTabComponent$1.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */