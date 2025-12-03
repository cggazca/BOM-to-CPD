package com.mentor.dms.contentprovider.plugin.searchui;

import com.mentor.dms.contentprovider.ContentProviderException;
import com.mentor.dms.contentprovider.client.PropertyValueSelectionDlg;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;

class ValidValuesPopUpMenu extends JPopupMenu implements ActionListener {
  private JMenuItem item;
  
  private JLabel lbl;
  
  public ValidValuesPopUpMenu(JLabel paramJLabel) {
    this.lbl = paramJLabel;
    this.item = new JMenuItem("Set " + paramContentProviderSearchRestrictionsPane.ccp.getName() + " valid values...", paramContentProviderSearchRestrictionsPane.ccp.getIcon());
    add(this.item);
    this.item.addActionListener(this);
  }
  
  public void actionPerformed(ActionEvent paramActionEvent) {
    Map map;
    String str = (String)this.lbl.getClientProperty("CCPID");
    JTextField jTextField = (JTextField)this.lbl.getLabelFor();
    try {
      map = ContentProviderSearchRestrictionsPane.this.ccp.getValidValues(ContentProviderSearchRestrictionsPane.this.rest.getPartClass().getContentProviderId(), str);
    } catch (ContentProviderException contentProviderException) {
      JOptionPane.showMessageDialog(this, contentProviderException.getMessage(), "Select " + ContentProviderSearchRestrictionsPane.this.ccp.getName() + " Valid Value", 1);
      return;
    } 
    if (map == null) {
      JOptionPane.showMessageDialog(this, ContentProviderSearchRestrictionsPane.this.ccp.getName() + " does not provide valid values for '" + ContentProviderSearchRestrictionsPane.this.ccp.getName() + "' at this level.", "Select " + ContentProviderSearchRestrictionsPane.this.ccp.getName() + " Valid Value", 1);
      return;
    } 
    PropertyValueSelectionDlg propertyValueSelectionDlg = new PropertyValueSelectionDlg(ContentProviderSearchWindow.getInstance(), ContentProviderSearchRestrictionsPane.this.ccp, this.lbl.getText(), map.keySet());
    if (!propertyValueSelectionDlg.isCancelled())
      jTextField.setText(propertyValueSelectionDlg.getRestriction()); 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\plugin\searchui\ContentProviderSearchRestrictionsPane$ValidValuesPopUpMenu.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */