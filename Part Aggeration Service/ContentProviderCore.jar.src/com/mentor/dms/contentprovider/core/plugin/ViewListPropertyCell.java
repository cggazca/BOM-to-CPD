package com.mentor.dms.contentprovider.core.plugin;

import com.mentor.dms.contentprovider.core.ComponentListProperty;
import com.mentor.dms.contentprovider.core.IContentProviderResultRecord;
import com.mentor.dms.contentprovider.core.ListPropertyRow;
import com.mentor.dms.contentprovider.core.utils.ListViewProperties;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

public class ViewListPropertyCell extends AbstractPushButtonCell {
  private ComponentListProperty listProp;
  
  private String labelString = "";
  
  public ViewListPropertyCell(IContentProviderResultRecord paramIContentProviderResultRecord, ComponentListProperty paramComponentListProperty, String paramString) {
    super(paramIContentProviderResultRecord);
    this.resultRecord = paramIContentProviderResultRecord;
    this.listProp = paramComponentListProperty;
    int i = 0;
    if (paramComponentListProperty.getList() != null)
      i = paramComponentListProperty.getList().size(); 
    this.labelString = "" + i + " " + i + "(s)";
    JLabel jLabel = new JLabel(this.labelString);
    jLabel.setAlignmentX(0.5F);
    jLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
    add(jLabel);
    if (i > 0) {
      Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("images/alternates.png"));
      ImageIcon imageIcon = new ImageIcon(image);
      JButton jButton = new JButton("View...", imageIcon);
      jButton.setAlignmentX(0.5F);
      jButton.setActionCommand("ViewListProp");
      jButton.setEnabled((paramComponentListProperty.getList().size() > 0));
      jButton.addActionListener(this);
      jButton.setToolTipText("View Property details");
      add(jButton);
    } 
  }
  
  public void actionPerformed(ActionEvent paramActionEvent) {
    if (paramActionEvent.getActionCommand().equals("ViewListProp"))
      ViewListPropertiesWindow.show(ContentProviderViewCompareWindow.getJFrame(), this.listProp, this.resultRecord); 
  }
  
  public String[][] getTable() {
    List list1 = this.listProp.getList();
    List list2 = ListViewProperties.getPropertyCSV(this.listProp.getId());
    int i = list1.size();
    if (i <= 0)
      return null; 
    String[][] arrayOfString = new String[i + 1][list2.size()];
    byte b1 = 0;
    for (String str : list2)
      arrayOfString[0][b1++] = str; 
    byte b2 = 1;
    for (ListPropertyRow listPropertyRow : list1) {
      b1 = 0;
      for (String str : list2)
        arrayOfString[b2][b1++] = listPropertyRow.getValue(str); 
      b2++;
    } 
    return arrayOfString;
  }
  
  public String toString() {
    return this.labelString;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\plugin\ViewListPropertyCell.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */