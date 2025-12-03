package com.mentor.dms.contentprovider;

import java.util.Collection;

public interface IContentProviderResults {
  public static final int RESULT_COUNT_UNKNOWN = -1;
  
  public static final int RESULT_COUNT_MORE = -2;
  
  public static final int RESULT_COUNT_ALL = -3;
  
  int getResultCount();
  
  int getReturnCount();
  
  Collection<IContentProviderResultRecord> getResultRecords() throws ContentProviderException;
  
  boolean containsECADModelResults();
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\IContentProviderResults.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */