package com.mentor.dms.contentprovider.core.plugin;

import java.util.List;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

public class ContentProviderViewAlternatesTableModel extends DefaultTableModel {
  private List<String> propertyTypes;
  
  public ContentProviderViewAlternatesTableModel(Vector<? extends Vector> paramVector1, Vector<?> paramVector2, List<String> paramList) {
    super(paramVector1, paramVector2);
    this.propertyTypes = paramList;
  }
  
  public Class getColumnClass(int paramInt) {
    Object object = getValueAt(0, paramInt);
    return (object == null) ? Object.class : object.getClass();
  }
  
  public boolean isCellEditable(int paramInt1, int paramInt2) {
    return ((String)this.propertyTypes.get(paramInt2)).equals("URL");
  }
  
  public Object getValueAt(int paramInt1, int paramInt2) {
    String str = "** N/A **";
    Vector<E> vector = this.dataVector.elementAt(paramInt1);
    if (((String)this.propertyTypes.get(paramInt2)).equals("URL")) {
      String str1 = vector.elementAt(paramInt2).toString();
      return (str1 != null) ? new ViewDocumentCell(str1, 16, 16) : "";
    } 
    return vector.elementAt(paramInt2);
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\plugin\ContentProviderViewAlternatesTableModel.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */