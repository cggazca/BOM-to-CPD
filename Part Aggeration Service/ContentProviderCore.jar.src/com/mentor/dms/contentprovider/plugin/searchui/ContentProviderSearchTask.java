package com.mentor.dms.contentprovider.plugin.searchui;

import com.mentor.dms.contentprovider.EmptyContentProviderResults;
import com.mentor.dms.contentprovider.IContentProviderResults;
import javax.swing.SwingWorker;

public class ContentProviderSearchTask extends SwingWorker<Void, ContentProviderSearchPanel> {
  private String queryMessageString;
  
  private String errorMessageString;
  
  private boolean bQueryOK;
  
  private IContentProviderResults results;
  
  private ContentProviderSearchPanel searchPanel;
  
  private int returnCount = -1;
  
  private boolean bContainsECADModelResults = false;
  
  public ContentProviderSearchTask(ContentProviderSearchPanel paramContentProviderSearchPanel) {
    this.searchPanel = paramContentProviderSearchPanel;
  }
  
  public String getQueryMessageString() {
    return this.queryMessageString;
  }
  
  public Void doInBackground() {
    this.bQueryOK = false;
    this.queryMessageString = "'" + this.searchPanel.getContentProvider().getName() + "' search ";
    try {
      if (this.searchPanel.isNextSearch()) {
        if (!this.searchPanel.getContentProvider().hasNextParts()) {
          this.bQueryOK = true;
          this.results = (IContentProviderResults)new EmptyContentProviderResults();
          this.queryMessageString += "has no more results. ";
          return null;
        } 
        this.results = this.searchPanel.getContentProvider().getNextParts(this.searchPanel.getSearchRestriction().getPartClass(), this.searchPanel.getSearchCriteria());
      } else {
        this.results = this.searchPanel.getContentProvider().searchParts(this.searchPanel.getSearchRestriction().getPartClass(), this.searchPanel.getSearchCriteria());
      } 
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
    if (this.bQueryOK) {
      try {
        this.searchPanel.getTableModel().setContentProviderResults(this.results);
      } catch (Exception exception) {
        this.queryMessageString = exception.getMessage();
      } 
      this.searchPanel.setReturnCount(this.returnCount);
    } else {
      this.searchPanel.setWarningIcon(this.errorMessageString);
    } 
    this.searchPanel.setStatus(this.queryMessageString);
    this.searchPanel.setComplete((this.results != null && this.results.containsECADModelResults()));
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\plugin\searchui\ContentProviderSearchTask.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */