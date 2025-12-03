package com.mentor.dms.contentprovider.core.plugin;

import java.awt.Component;
import java.awt.Font;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

class null extends JTable {
  null(TableModel paramTableModel) {
    super(paramTableModel);
  }
  
  public Component prepareRenderer(TableCellRenderer paramTableCellRenderer, int paramInt1, int paramInt2) {
    Component component = super.prepareRenderer(paramTableCellRenderer, paramInt1, paramInt2);
    if (paramInt1 == 0) {
      component.setFont(new Font("boldFont", 1, 11));
    } else {
      component.setFont(new Font("MS Gothic", 0, 12));
    } 
    return component;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\plugin\ViewSupplyChainWindow$1.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */