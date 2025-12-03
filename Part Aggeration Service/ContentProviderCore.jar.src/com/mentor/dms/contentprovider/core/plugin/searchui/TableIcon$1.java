package com.mentor.dms.contentprovider.core.plugin.searchui;

import javax.swing.Icon;
import javax.swing.table.DefaultTableModel;

class null extends DefaultTableModel {
  null(Object[][] paramArrayOfObject, Object[] paramArrayOfObject1) {
    super(paramArrayOfObject, paramArrayOfObject1);
  }
  
  public Class getColumnClass(int paramInt) {
    return (paramInt == 0) ? Icon.class : getValueAt(0, paramInt).getClass();
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\plugin\searchui\TableIcon$1.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */