package com.mentor.dms.contentprovider.sf;

import com.mentor.dms.contentprovider.core.ContentProviderException;
import com.mentor.dms.contentprovider.core.IContentProviderResultRecord;
import com.mentor.dms.contentprovider.core.IContentProviderResults;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ContentProviderResultsImpl implements IContentProviderResults {
  private List<ContentProviderResultRecordImpl> resultRecs = new ArrayList<>();
  
  private int resultCount = -1;
  
  private int returnCount = 0;
  
  private boolean bContainsECADModelResults = false;
  
  private String warningMessage = null;
  
  private String nextPageToken;
  
  protected void setResultCount(int paramInt) {
    this.resultCount = paramInt;
  }
  
  protected void setReturnCount(int paramInt) {
    this.returnCount = paramInt;
  }
  
  public int getResultCount() {
    return this.resultCount;
  }
  
  public int getReturnCount() {
    return this.returnCount;
  }
  
  public Collection<IContentProviderResultRecord> getResultRecords() throws ContentProviderException {
    return new ArrayList(this.resultRecs);
  }
  
  public void addResults(ContentProviderResultRecordImpl paramContentProviderResultRecordImpl) {
    this.resultRecs.add(paramContentProviderResultRecordImpl);
  }
  
  protected void setContainsECADModelResults(boolean paramBoolean) {
    this.bContainsECADModelResults = paramBoolean;
  }
  
  public boolean containsECADModelResults() {
    return this.bContainsECADModelResults;
  }
  
  public String getNextPageToken() {
    return this.nextPageToken;
  }
  
  public void setNextPageToken(String paramString) {
    this.nextPageToken = paramString;
  }
  
  public String getWarningMessage() {
    return this.warningMessage;
  }
  
  void setWarningMessage(String paramString) {
    this.warningMessage = paramString;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\ContentProviderResultsImpl.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */