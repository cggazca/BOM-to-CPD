package com.mentor.dms.contentprovider.sf.request.data;

import com.mentor.dms.contentprovider.core.request.Filter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchParameter {
  private String partClassId;
  
  private Map<String, String> customParameters = new HashMap<>();
  
  private List<String> outputs = new ArrayList<>();
  
  private List<Map<String, String>> sort = new ArrayList<>();
  
  private Paging paging;
  
  private Filter filter;
  
  public Filter getFilter() {
    return this.filter;
  }
  
  public SearchParameter(String paramString) {
    this.partClassId = paramString;
  }
  
  public void setPaging(Paging paramPaging) {
    this.paging = paramPaging;
  }
  
  public void setFilter(Filter paramFilter) {
    this.filter = paramFilter;
  }
  
  public void setCustomParameter(String paramString1, String paramString2) {
    this.customParameters.put(paramString1, paramString2);
  }
  
  public void addOutputs(String paramString) {
    this.outputs.add(paramString);
  }
  
  public void setOutputs(List<String> paramList) {
    this.outputs = paramList;
  }
  
  public void addSort(String paramString, boolean paramBoolean) {
    HashMap<Object, Object> hashMap = new HashMap<>();
    if (paramBoolean) {
      hashMap.put("PropertyId", paramString);
      hashMap.put("Direction", "Asc");
      this.sort.add(hashMap);
    } else {
      hashMap.put("PropertyId", paramString);
      hashMap.put("Direction", "Desc");
      this.sort.add(hashMap);
    } 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\request\data\SearchParameter.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */