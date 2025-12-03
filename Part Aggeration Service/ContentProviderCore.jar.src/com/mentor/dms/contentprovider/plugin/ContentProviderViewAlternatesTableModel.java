package com.mentor.dms.contentprovider.plugin;

import java.util.Vector;
import javax.swing.table.DefaultTableModel;

public class ContentProviderViewAlternatesTableModel extends DefaultTableModel {
  public ContentProviderViewAlternatesTableModel(Vector<? extends Vector> paramVector1, Vector<?> paramVector2) {
    super(paramVector1, paramVector2);
  }
  
  public Class getColumnClass(int paramInt) {
    return getValueAt(0, paramInt).getClass();
  }
  
  public boolean isCellEditable(int paramInt1, int paramInt2) {
    return false;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\plugin\ContentProviderViewAlternatesTableModel.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */