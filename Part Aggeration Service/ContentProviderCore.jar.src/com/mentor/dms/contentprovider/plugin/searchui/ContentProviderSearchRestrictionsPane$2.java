package com.mentor.dms.contentprovider.plugin.searchui;

import com.mentor.datafusion.oi.model.OIField;
import com.mentor.dms.contentprovider.ContentProviderGlobal;
import com.mentor.dms.ui.searchmask.SearchMask;
import javax.swing.JOptionPane;

class null implements CheckBoxTreeListener {
  public void stateChanged(CheckBoxNode paramCheckBoxNode) {
    try {
      SearchMask searchMask = ContentProviderSearchRestrictionsPane.this.searchMaskManager.open(ContentProviderSearchRestrictionsPane.this.rest.getOIClass());
      OIField oIField = (OIField)paramCheckBoxNode.getClientObject();
      searchMask.setSelected(oIField, paramCheckBoxNode.isSelected());
    } catch (Exception exception) {
      JOptionPane.showMessageDialog(ContentProviderGlobal.getDMSInstance().getJFrame(), exception.getMessage());
    } 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\plugin\searchui\ContentProviderSearchRestrictionsPane$2.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */