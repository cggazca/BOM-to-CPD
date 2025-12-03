package com.mentor.dms.contentprovider.core.sync;

import com.mentor.dms.contentprovider.core.ContentProviderException;

public class ContentProviderSyncException extends ContentProviderException {
  private static final long serialVersionUID = 1L;
  
  public ContentProviderSyncException(String paramString) {
    super(paramString);
  }
  
  public ContentProviderSyncException(Exception paramException) {
    super(paramException);
  }
  
  public ContentProviderSyncException(String paramString, Exception paramException) {
    super(paramString, paramException);
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\sync\ContentProviderSyncException.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */