package com.mentor.dms.contentprovider.plugin;

import javax.swing.JScrollPane;

public class ContentProviderViewCompareScrollableJTextArea extends JScrollPane {
  private ContentProviderViewCompareJTextArea textArea;
  
  public ContentProviderViewCompareScrollableJTextArea(String paramString) {
    this.textArea = new ContentProviderViewCompareJTextArea(paramString);
    getViewport().add(this.textArea);
  }
  
  public ContentProviderViewCompareJTextArea getJTextArea() {
    return this.textArea;
  }
  
  public String toString() {
    return this.textArea.getText();
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\plugin\ContentProviderViewCompareScrollableJTextArea.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */