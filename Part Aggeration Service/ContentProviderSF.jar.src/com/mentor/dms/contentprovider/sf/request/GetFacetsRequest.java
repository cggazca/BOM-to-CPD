package com.mentor.dms.contentprovider.sf.request;

import com.mentor.dms.contentprovider.core.request.Filter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetFacetsRequest {
  private String partClassId;
  
  private Map<String, String> customParameters = new HashMap<>();
  
  private Filter filter;
  
  private List<String> facets = new ArrayList<>();
  
  public GetFacetsRequest(String paramString) {
    this.partClassId = paramString;
  }
  
  public void setCustomParameter(String paramString1, String paramString2) {
    this.customParameters.put(paramString1, paramString2);
  }
  
  public void setFilter(Filter paramFilter) {
    this.filter = paramFilter;
  }
  
  public void addFacet(String paramString) {
    this.facets.add(paramString);
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\request\GetFacetsRequest.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */