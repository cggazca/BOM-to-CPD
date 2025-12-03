package com.mentor.dms.contentprovider.core.plugin;

import com.mentor.dms.contentprovider.core.ContentProviderGlobal;
import com.mentor.dms.contentprovider.core.IContentProviderCustomTable;
import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.border.CompoundBorder;

public class ContentProviderViewCustomTableDlg extends JDialog {
  private ContentProviderViewCustomTableJXTable customJXTable;
  
  public ContentProviderViewCustomTableDlg(Frame paramFrame, IContentProviderCustomTable paramIContentProviderCustomTable) {
    super(paramFrame, true);
    int i = paramIContentProviderCustomTable.getWidth();
    int j = paramIContentProviderCustomTable.getHeight();
    setTitle("View " + paramIContentProviderCustomTable.getCategoryLabel());
    setIconImage(ContentProviderGlobal.getAppIconImage());
    Container container = getContentPane();
    this.customJXTable = new ContentProviderViewCustomTableJXTable(new ContentProviderViewCustomTableTableModel(paramIContentProviderCustomTable));
    JScrollPane jScrollPane = new JScrollPane((Component)this.customJXTable);
    CompoundBorder compoundBorder = BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5), BorderFactory.createEtchedBorder(1));
    jScrollPane.setBorder(compoundBorder);
    container.add(jScrollPane);
    for (byte b = 0; b < this.customJXTable.getRowCount(); b++) {
      int k = this.customJXTable.getRowHeight();
      for (byte b1 = 0; b1 < this.customJXTable.getColumnCount(); b1++) {
        Component component = this.customJXTable.prepareRenderer(this.customJXTable.getCellRenderer(b, b1), b, b1);
        k = Math.max(k, (component.getPreferredSize()).height);
      } 
      this.customJXTable.setRowHeight(b, k);
    } 
    this.customJXTable.packAll();
    setDefaultCloseOperation(2);
    pack();
    setSize(i, j);
    setLocationRelativeTo(paramFrame);
    setVisible(true);
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\plugin\ContentProviderViewCustomTableDlg.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */