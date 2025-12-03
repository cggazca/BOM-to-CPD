package com.mentor.dms.contentprovider;

import java.util.Collection;
import java.util.Collections;

public class EmptyContentProviderResults implements IContentProviderResults {
  public int getResultCount() {
    return 0;
  }
  
  public int getReturnCount() {
    return 0;
  }
  
  public Collection<IContentProviderResultRecord> getResultRecords() throws ContentProviderException {
    return Collections.emptyList();
  }
  
  public boolean containsECADModelResults() {
    return false;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\EmptyContentProviderResults.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */