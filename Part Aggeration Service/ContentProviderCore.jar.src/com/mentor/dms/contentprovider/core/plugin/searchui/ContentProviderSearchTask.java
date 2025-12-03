package com.mentor.dms.contentprovider.core.plugin.searchui;

import com.mentor.dms.contentprovider.core.EmptyContentProviderResults;
import com.mentor.dms.contentprovider.core.IContentProviderResults;
import java.util.Map;
import javax.swing.SwingWorker;

public class ContentProviderSearchTask extends SwingWorker<Void, ContentProviderSearchPanel> {
  private final int QUERY_OK = 0;
  
  private final int QUERY_WARN = 1;
  
  private final int QUERY_ERROR = 2;
  
  private String queryMessageString;
  
  private String errorMessageString;
  
  private int bQueryOK = 0;
  
  private IContentProviderResults results;
  
  private ContentProviderSearchPanel searchPanel;
  
  private int returnCount = -1;
  
  private Map<String, String> definitionsEnricher = null;
  
  public ContentProviderSearchTask(ContentProviderSearchPanel paramContentProviderSearchPanel) {
    this.searchPanel = paramContentProviderSearchPanel;
  }
  
  public String getQueryMessageString() {
    return this.queryMessageString;
  }
  
  public Void doInBackground() {
    this.bQueryOK = 2;
    this.queryMessageString = "'" + this.searchPanel.getContentProvider().getName() + "' search ";
    try {
      if (this.searchPanel.isNextSearch()) {
        if (!this.searchPanel.getContentProvider().hasNextParts()) {
          this.bQueryOK = 0;
          this.results = (IContentProviderResults)new EmptyContentProviderResults();
          this.queryMessageString += "has no more results. ";
          return null;
        } 
        this.results = this.searchPanel.getContentProvider().getNextParts(this.searchPanel.getSearchRestriction().getPartClass(), this.searchPanel.getTableModel().getNextPageToken(), this.searchPanel.getSearchCriteria());
      } else {
        this.results = this.searchPanel.getContentProvider().searchParts(this.searchPanel.getSearchRestriction().getPartClass(), this.searchPanel.getSearchCriteria());
      } 
      if (this.definitionsEnricher == null || this.definitionsEnricher.isEmpty())
        this.definitionsEnricher = this.searchPanel.getContentProvider().searchDefinitionsEnricher(); 
      if (this.results.getWarningMessage() != null) {
        this.bQueryOK = 1;
        this.errorMessageString = this.results.getWarningMessage();
      } else {
        this.bQueryOK = 0;
      } 
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
      this.bQueryOK = 2;
      this.queryMessageString = this.queryMessageString + "failed:  " + this.queryMessageString;
      this.errorMessageString = exception.getMessage();
    } 
    return null;
  }
  
  public void done() {
    boolean bool = false;
    if (this.bQueryOK == 0) {
      try {
        this.searchPanel.getTableModel().setDefinitionsEnricher(this.definitionsEnricher);
        this.searchPanel.getTableModel().setContentProviderResults(this.results);
        if (this.returnCount > 1000)
          this.queryMessageString = "The maximum number of search results has been exceeded. Add more filtering criteria."; 
      } catch (Exception exception) {
        this.queryMessageString = exception.getMessage();
        bool = true;
      } 
      this.searchPanel.setReturnCount(this.returnCount);
    } else if (this.bQueryOK == 1) {
      try {
        this.searchPanel.getTableModel().setDefinitionsEnricher(this.definitionsEnricher);
        this.searchPanel.getTableModel().setContentProviderResults(this.results);
        this.queryMessageString = this.errorMessageString;
      } catch (Exception exception) {
        this.queryMessageString = exception.getMessage();
        bool = true;
      } 
      this.searchPanel.setReturnCountWarn(this.returnCount, this.errorMessageString);
    } else {
      this.searchPanel.setWarningIcon(this.errorMessageString);
      bool = true;
    } 
    this.searchPanel.setStatus(bool, this.queryMessageString);
    this.searchPanel.setComplete((this.results != null && this.results.containsECADModelResults()));
    if (this.returnCount > -1) {
      this.searchPanel.setCountText("" + this.searchPanel.getTable().getModel().getRowCount() + " / " + this.searchPanel.getTable().getModel().getRowCount());
    } else {
      this.searchPanel.setCountText("");
    } 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\plugin\searchui\ContentProviderSearchTask.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */