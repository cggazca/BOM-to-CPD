package com.mentor.dms.contentprovider.plugin.partsearchui;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

class null implements DocumentListener {
  public void changedUpdate(DocumentEvent paramDocumentEvent) {
    ContentProviderSelectPartNumberDlg.this.evaluateTextFieldChanges();
  }
  
  public void removeUpdate(DocumentEvent paramDocumentEvent) {
    ContentProviderSelectPartNumberDlg.this.evaluateTextFieldChanges();
  }
  
  public void insertUpdate(DocumentEvent paramDocumentEvent) {
    ContentProviderSelectPartNumberDlg.this.evaluateTextFieldChanges();
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\plugin\partsearchui\ContentProviderSelectPartNumberDlg$1.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */