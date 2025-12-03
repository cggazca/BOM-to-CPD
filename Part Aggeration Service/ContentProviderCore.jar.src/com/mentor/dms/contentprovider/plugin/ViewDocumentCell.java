package com.mentor.dms.contentprovider.plugin;

import java.awt.event.ActionEvent;

public class ViewDocumentCell extends AbstractPushButtonCell {
  public ViewDocumentCell(String paramString) {
    ViewDocumentButton viewDocumentButton = new ViewDocumentButton(paramString);
    add(viewDocumentButton);
  }
  
  public ViewDocumentCell(String paramString, int paramInt1, int paramInt2) {
    ViewDocumentButton viewDocumentButton = new ViewDocumentButton(paramString, paramInt1, paramInt2);
    add(viewDocumentButton);
  }
  
  public void actionPerformed(ActionEvent paramActionEvent) {}
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\plugin\ViewDocumentCell.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */