package com.mentor.dms.contentprovider.plugin;

import com.mentor.dms.contentprovider.IContentProviderCustomTable;
import com.mentor.dms.contentprovider.IContentProviderResultRecord;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

public class ViewCustomTableCell extends AbstractPushButtonCell {
  private IContentProviderCustomTable customTable;
  
  public ViewCustomTableCell(IContentProviderResultRecord paramIContentProviderResultRecord, String paramString) {
    this(paramIContentProviderResultRecord, null, paramString);
  }
  
  public ViewCustomTableCell(IContentProviderResultRecord paramIContentProviderResultRecord, IContentProviderCustomTable paramIContentProviderCustomTable) {
    this(paramIContentProviderResultRecord, paramIContentProviderCustomTable, paramIContentProviderCustomTable.getCategoryLabel());
  }
  
  private ViewCustomTableCell(IContentProviderResultRecord paramIContentProviderResultRecord, IContentProviderCustomTable paramIContentProviderCustomTable, String paramString) {
    super(paramIContentProviderResultRecord);
    JLabel jLabel;
    this.customTable = null;
    this.customTable = paramIContentProviderCustomTable;
    if (paramIContentProviderCustomTable == null || paramIContentProviderCustomTable.getRowCount() == 0) {
      jLabel = new JLabel("No " + paramString + " available.");
    } else {
      jLabel = new JLabel("" + paramIContentProviderCustomTable.getRowCount() + "  " + paramIContentProviderCustomTable.getRowCount());
    } 
    jLabel.setAlignmentX(0.5F);
    jLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
    add(jLabel);
    if (paramIContentProviderCustomTable != null && paramIContentProviderCustomTable.getRowCount() > 0) {
      Icon icon = paramIContentProviderCustomTable.getIcon();
      if (icon == null) {
        Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("images/documents.png"));
        icon = new ImageIcon(image);
      } 
      JButton jButton = new JButton("View...", icon);
      jButton.setAlignmentX(0.5F);
      jButton.setActionCommand("ViewCustomTable");
      jButton.setEnabled((paramIContentProviderCustomTable.getRowCount() > 0));
      jButton.addActionListener(this);
      jButton.setToolTipText("View " + paramIContentProviderCustomTable.getCategoryLabel());
      add(jButton);
    } 
  }
  
  public void actionPerformed(ActionEvent paramActionEvent) {
    if (paramActionEvent.getActionCommand().equals("ViewCustomTable"))
      new ContentProviderViewCustomTableDlg(ContentProviderViewCompareWindow.getJFrame(), this.customTable); 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\plugin\ViewCustomTableCell.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */