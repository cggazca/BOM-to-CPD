package com.mentor.dms.contentprovider.plugin.searchui;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class ContentProviderSearchResultsCellRenderer extends DefaultTableCellRenderer {
  public Component getTableCellRendererComponent(JTable paramJTable, Object paramObject, boolean paramBoolean1, boolean paramBoolean2, int paramInt1, int paramInt2) {
    Component component = super.getTableCellRendererComponent(paramJTable, paramObject, paramBoolean1, paramBoolean2, paramInt1, paramInt2);
    String str = (String)paramObject;
    if (str.equals("** N/A **"))
      component.setForeground(Color.GRAY); 
    return component;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\plugin\searchui\ContentProviderSearchResultsCellRenderer.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */