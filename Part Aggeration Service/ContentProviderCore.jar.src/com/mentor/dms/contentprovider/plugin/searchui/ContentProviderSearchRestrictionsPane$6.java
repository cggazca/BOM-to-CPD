package com.mentor.dms.contentprovider.plugin.searchui;

import com.mentor.datafusion.oi.model.OIField;
import com.mentor.dms.contentprovider.ContentProviderGlobal;
import com.mentor.dms.ui.searchmask.SearchMask;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

class null implements DocumentListener {
  public void insertUpdate(DocumentEvent paramDocumentEvent) {
    doUpdate(paramDocumentEvent);
  }
  
  public void removeUpdate(DocumentEvent paramDocumentEvent) {
    doUpdate(paramDocumentEvent);
  }
  
  public void changedUpdate(DocumentEvent paramDocumentEvent) {
    doUpdate(paramDocumentEvent);
  }
  
  private void doUpdate(DocumentEvent paramDocumentEvent) {
    try {
      SearchMask searchMask = ContentProviderSearchRestrictionsPane.this.searchMaskManager.open(ContentProviderSearchRestrictionsPane.this.rest.getOIClass());
      Document document = paramDocumentEvent.getDocument();
      OIField oIField = (OIField)document.getProperty("OIFIELD");
      String str = document.getText(0, document.getLength());
      searchMask.setRestriction(oIField, str);
      searchPanel.restrictionChangeEvent();
    } catch (Exception exception) {
      JOptionPane.showMessageDialog(ContentProviderGlobal.getDMSInstance().getJFrame(), exception.getMessage());
    } 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\plugin\searchui\ContentProviderSearchRestrictionsPane$6.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */