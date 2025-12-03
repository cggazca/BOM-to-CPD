package com.mentor.dms.contentprovider.core.plugin;

import java.awt.event.ActionEvent;

public class ViewDocumentCell extends AbstractPushButtonCell implements Comparable<ViewDocumentCell> {
  private String url;
  
  public ViewDocumentCell(String paramString) {
    if (paramString != null) {
      this.url = paramString.replaceAll(" ", "%20");
    } else {
      this.url = paramString;
    } 
    ViewDocumentButton viewDocumentButton = new ViewDocumentButton(this.url);
    add(viewDocumentButton);
  }
  
  public ViewDocumentCell(String paramString, int paramInt1, int paramInt2) {
    if (paramString != null) {
      this.url = paramString.replaceAll(" ", "%20");
    } else {
      this.url = paramString;
    } 
    ViewDocumentButton viewDocumentButton = new ViewDocumentButton(this.url, paramInt1, paramInt2);
    add(viewDocumentButton);
  }
  
  public void actionPerformed(ActionEvent paramActionEvent) {}
  
  public int compareTo(ViewDocumentCell paramViewDocumentCell) {
    return (this.url != null && paramViewDocumentCell.getUrl() != null) ? this.url.compareTo(paramViewDocumentCell.getUrl()) : ((paramViewDocumentCell.getUrl() != null) ? 1 : -1);
  }
  
  public String getUrl() {
    return this.url;
  }
  
  public String toString() {
    return this.url;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\plugin\ViewDocumentCell.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */