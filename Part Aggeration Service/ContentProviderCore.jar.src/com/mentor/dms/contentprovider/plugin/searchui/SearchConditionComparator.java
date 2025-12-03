package com.mentor.dms.contentprovider.plugin.searchui;

import com.mentor.dms.ui.searchmask.restrictions.SearchCondition;
import java.util.Comparator;

class SearchConditionComparator implements Comparator<SearchCondition> {
  public int compare(SearchCondition paramSearchCondition1, SearchCondition paramSearchCondition2) {
    return (paramSearchCondition1.getDisposeOrder() > paramSearchCondition2.getDisposeOrder()) ? 1 : ((paramSearchCondition1.getDisposeOrder() < paramSearchCondition2.getDisposeOrder()) ? -1 : paramSearchCondition1.getField().getName().compareTo(paramSearchCondition2.getField().getName()));
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\plugin\searchui\SearchConditionComparator.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */