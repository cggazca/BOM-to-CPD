package com.mentor.dms.contentprovider;

import com.mentor.dms.contentprovider.sync.CacheInfo;
import com.mentor.dms.contentprovider.sync.ContentProviderSyncException;
import java.util.Collection;
import java.util.Map;

public interface IContentProviderUpdateSearch {
  void setContentProvider(AbstractContentProvider paramAbstractContentProvider);
  
  void setDMSExternalContentList(Collection<Map<String, String>> paramCollection);
  
  void setIncrementalMode();
  
  void setFullMode();
  
  IContentProviderUpdateSearchResults execute() throws ContentProviderSyncException;
  
  void setCustomSyncAppParams(Map<String, String> paramMap) throws ContentProviderSyncException;
  
  void setRestartMode();
  
  void setCacheInfo(CacheInfo paramCacheInfo);
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\IContentProviderUpdateSearch.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */