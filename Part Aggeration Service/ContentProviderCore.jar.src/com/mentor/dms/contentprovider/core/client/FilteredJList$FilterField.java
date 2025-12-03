package com.mentor.dms.contentprovider.core.client;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

class FilterField extends JTextField implements DocumentListener {
  public FilterField(int paramInt) {
    super(paramInt);
    getDocument().addDocumentListener(this);
  }
  
  public void changedUpdate(DocumentEvent paramDocumentEvent) {
    ((FilteredJList.FilterModel)FilteredJList.this.getModel()).refilter();
  }
  
  public void insertUpdate(DocumentEvent paramDocumentEvent) {
    ((FilteredJList.FilterModel)FilteredJList.this.getModel()).refilter();
  }
  
  public void removeUpdate(DocumentEvent paramDocumentEvent) {
    ((FilteredJList.FilterModel)FilteredJList.this.getModel()).refilter();
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\client\FilteredJList$FilterField.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */