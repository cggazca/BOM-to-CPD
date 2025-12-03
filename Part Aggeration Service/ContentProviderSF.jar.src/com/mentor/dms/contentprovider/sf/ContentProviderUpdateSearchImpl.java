package com.mentor.dms.contentprovider.sf;

import com.mentor.dms.contentprovider.core.AbstractContentProvider;
import com.mentor.dms.contentprovider.core.IContentProviderUpdateSearch;
import com.mentor.dms.contentprovider.core.IContentProviderUpdateSearchResults;
import com.mentor.dms.contentprovider.core.sync.CacheInfo;
import com.mentor.dms.contentprovider.core.sync.ContentProviderSyncException;
import java.util.Collection;
import java.util.Map;

public class ContentProviderUpdateSearchImpl implements IContentProviderUpdateSearch {
  private boolean bFullMode = true;
  
  private boolean bRestart = false;
  
  private CacheInfo cacheInfo = null;
  
  private Collection<Map<String, String>> dmsECIdList;
  
  private ContentProviderImpl ccp;
  
  public void setContentProvider(AbstractContentProvider paramAbstractContentProvider) {
    this.ccp = (ContentProviderImpl)paramAbstractContentProvider;
  }
  
  public void setDMSExternalContentList(Collection<Map<String, String>> paramCollection) {
    this.dmsECIdList = paramCollection;
  }
  
  public void setIncrementalMode() {
    this.bFullMode = false;
  }
  
  public void setFullMode() {
    this.bFullMode = true;
  }
  
  public void setRestartMode() {
    this.bRestart = true;
  }
  
  public IContentProviderUpdateSearchResults execute() throws ContentProviderSyncException {
    ContentProviderUpdateSearchResultsImpl contentProviderUpdateSearchResultsImpl = new ContentProviderUpdateSearchResultsImpl(this.ccp, this.cacheInfo, this.dmsECIdList);
    try {
      if (this.bFullMode) {
        contentProviderUpdateSearchResultsImpl.executeFullMode(this.bRestart);
      } else {
        contentProviderUpdateSearchResultsImpl.executeIncrementalMode(this.bRestart);
      } 
    } catch (Exception exception) {
      throw new ContentProviderSyncException(exception.getMessage());
    } 
    return contentProviderUpdateSearchResultsImpl;
  }
  
  public void setCustomSyncAppParams(Map<String, String> paramMap) throws ContentProviderSyncException {
    String str1 = paramMap.get("restart");
    if (str1 != null)
      throw new ContentProviderSyncException("Please use new '-restart' switch instead of using parameters."); 
    String str2 = paramMap.get("cachedir");
    if (str2 != null)
      throw new ContentProviderSyncException("Please use new '-cachedir' switch instead of using parameters."); 
  }
  
  public void setCacheInfo(CacheInfo paramCacheInfo) {
    this.cacheInfo = paramCacheInfo;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\ContentProviderUpdateSearchImpl.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */