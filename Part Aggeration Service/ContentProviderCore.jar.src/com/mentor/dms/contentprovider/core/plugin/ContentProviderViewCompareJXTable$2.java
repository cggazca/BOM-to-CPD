package com.mentor.dms.contentprovider.core.plugin;

import java.awt.Component;
import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.decorator.HighlightPredicate;

class null implements HighlightPredicate {
  public boolean isHighlighted(Component paramComponent, ComponentAdapter paramComponentAdapter) {
    Object object = paramComponentAdapter.getValueAt(paramComponentAdapter.row, paramComponentAdapter.column);
    if (object instanceof String) {
      String str = (String)object;
      if (str.equals("** N/A **"))
        return ((Boolean)paramComponentAdapter.getValueAt(paramComponentAdapter.row, paramComponentAdapter.getColumnCount() - 1)).booleanValue(); 
    } 
    return false;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\plugin\ContentProviderViewCompareJXTable$2.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */