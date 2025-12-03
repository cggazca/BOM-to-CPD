package com.mentor.dms.contentprovider.plugin.partsearchui;

import com.mentor.dms.contentprovider.IContentProviderResultRecord;
import com.mentor.dms.contentprovider.IContentProviderResults;
import javax.swing.SwingWorker;

public class ContentProviderPartNumberSearchTask extends SwingWorker<Void, ContentProviderPartNumberSearchMainPanel> {
  private String queryMessageString;
  
  private String errorMessageString;
  
  private boolean bQueryOK;
  
  private IContentProviderResults results;
  
  private ContentProviderPartNumberSearchMainPanel searchPanel;
  
  private int returnCount = -1;
  
  public ContentProviderPartNumberSearchTask(ContentProviderPartNumberSearchMainPanel paramContentProviderPartNumberSearchMainPanel) {
    this.searchPanel = paramContentProviderPartNumberSearchMainPanel;
  }
  
  public String getQueryMessageString() {
    return this.queryMessageString;
  }
  
  public Void doInBackground() {
    this.bQueryOK = false;
    this.queryMessageString = "'" + this.searchPanel.getContentProvider().getName() + "' search ";
    try {
      this.results = this.searchPanel.getContentProvider().searchParts(this.searchPanel.getPartClass(), this.searchPanel.getSearchCriteria());
      this.bQueryOK = true;
      this.returnCount = this.results.getReturnCount();
      if (this.returnCount == 0) {
        this.queryMessageString += "returned no results.";
      } else if (this.results.getResultCount() == -1) {
        this.queryMessageString = this.queryMessageString + "returned " + this.queryMessageString + " results.";
      } else if (this.results.getResultCount() == -2) {
        this.queryMessageString = this.queryMessageString + "returned " + this.queryMessageString + " results.  Max number of results returned.";
      } else if (this.results.getResultCount() == -3) {
        this.queryMessageString = this.queryMessageString + "returned " + this.queryMessageString + " of all results.";
      } else {
        this.queryMessageString = this.queryMessageString + "returned " + this.queryMessageString + " of " + this.returnCount + " possible results.";
      } 
    } catch (Exception exception) {
      this.queryMessageString = this.queryMessageString + "failed:  " + this.queryMessageString;
      this.errorMessageString = exception.getMessage();
    } 
    return null;
  }
  
  public void done() {
    if (this.bQueryOK)
      try {
        for (IContentProviderResultRecord iContentProviderResultRecord : this.results.getResultRecords())
          this.searchPanel.getTableModel().addPart(iContentProviderResultRecord); 
      } catch (Exception exception) {
        this.queryMessageString = exception.getMessage();
      }  
    this.searchPanel.setSearchEnabled(true);
    this.searchPanel.setWaitCursor(false);
    this.searchPanel.setStatus(this.queryMessageString);
    this.searchPanel.setComplete();
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\plugin\partsearchui\ContentProviderPartNumberSearchTask.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */