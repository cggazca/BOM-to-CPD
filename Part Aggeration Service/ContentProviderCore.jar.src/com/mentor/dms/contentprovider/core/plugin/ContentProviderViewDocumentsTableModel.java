package com.mentor.dms.contentprovider.core.plugin;

import java.util.Vector;
import javax.swing.table.DefaultTableModel;

public class ContentProviderViewDocumentsTableModel extends DefaultTableModel {
  public ContentProviderViewDocumentsTableModel(Vector<? extends Vector> paramVector1, Vector<?> paramVector2) {
    super(paramVector1, paramVector2);
  }
  
  public Class getColumnClass(int paramInt) {
    return getValueAt(0, paramInt).getClass();
  }
  
  public boolean isCellEditable(int paramInt1, int paramInt2) {
    Object object = getValueAt(paramInt1, paramInt2);
    return (object instanceof com.mentor.dms.contentprovider.core.ContentProviderDocument);
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\plugin\ContentProviderViewDocumentsTableModel.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */