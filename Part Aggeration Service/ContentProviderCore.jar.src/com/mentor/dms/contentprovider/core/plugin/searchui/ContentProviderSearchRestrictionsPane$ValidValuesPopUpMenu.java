package com.mentor.dms.contentprovider.core.plugin.searchui;

import com.mentor.dms.contentprovider.core.AbstractCriteria;
import com.mentor.dms.contentprovider.core.ContentProviderException;
import com.mentor.dms.contentprovider.core.client.PropertyValueSelectionDlg;
import com.mentor.dms.contentprovider.core.config.SearchCapability;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
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
    this.item = new JMenuItem("Set " + paramContentProviderSearchRestrictionsPane.ccp.getName() + " Qualified Values...", paramContentProviderSearchRestrictionsPane.ccp.getIcon());
    add(this.item);
    this.item.addActionListener(this);
  }
  
  public void actionPerformed(ActionEvent paramActionEvent) {
    List list;
    String str = (String)this.lbl.getClientProperty("CCPID");
    ContentProviderSearchRestrictionsPane.logger.info("Set Qualified Values. Property ID:[" + str + "]");
    JTextField jTextField = (JTextField)this.lbl.getLabelFor();
    ContentProviderSearchRestrictionsPane.this.setWaitCursor(true);
    try {
      HashMap hashMap = ContentProviderSearchRestrictionsPane.this.ccp.getConfig().getSearchCapabilityMap();
      boolean bool = ((SearchCapability)hashMap.get(str)).isFacet();
      if (!bool) {
        JOptionPane.showMessageDialog(this, "Valid values for \"" + this.lbl.getText() + "\" is not available.", "Select " + ContentProviderSearchRestrictionsPane.this.ccp.getName() + " Valid Value", 1);
        return;
      } 
      jTextField.setText("");
      AbstractCriteria abstractCriteria = ContentProviderSearchRestrictionsPane.this.searchPanel.getFacetsSearchFilteringCriteria();
      if (abstractCriteria == null)
        return; 
      if (ContentProviderSearchRestrictionsPane.this.searchPanel.rootFacet()) {
        JOptionPane.showMessageDialog(this, "\"Supplyframe Part Classes Root\" requires one or more search criteria.", "Select " + ContentProviderSearchRestrictionsPane.this.ccp.getName() + " Valid Value", 1);
        return;
      } 
      list = ContentProviderSearchRestrictionsPane.this.ccp.getValidValues(ContentProviderSearchRestrictionsPane.this.rest.getPartClass().getContentProviderId(), str, abstractCriteria);
    } catch (ContentProviderException contentProviderException) {
      ContentProviderSearchRestrictionsPane.this.searchPanel.setStatus(true, "'Valid values' acquisition failed : " + contentProviderException.getMessage());
      return;
    } finally {
      ContentProviderSearchRestrictionsPane.this.setWaitCursor(false);
    } 
    if (list == null || list.isEmpty()) {
      JOptionPane.showMessageDialog(this, "No valid values found for \"" + this.lbl.getText() + "\".", "Select " + ContentProviderSearchRestrictionsPane.this.ccp.getName() + " Valid Value", 1);
      return;
    } 
    PropertyValueSelectionDlg propertyValueSelectionDlg = new PropertyValueSelectionDlg(ContentProviderSearchWindow.getInstance(), ContentProviderSearchRestrictionsPane.this.ccp, this.lbl.getText(), list);
    if (!propertyValueSelectionDlg.isCancelled())
      jTextField.setText(propertyValueSelectionDlg.getRestriction()); 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\plugin\searchui\ContentProviderSearchRestrictionsPane$ValidValuesPopUpMenu.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */