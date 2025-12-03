package com.mentor.dms.contentprovider.sf.request;

import com.mentor.dms.contentprovider.sf.request.data.EnrichingParameter;
import com.mentor.dms.contentprovider.sf.request.data.SearchParameter;
import java.util.ArrayList;
import java.util.List;

public class ParametricSearchRequest {
  private SearchParameter searchParameters;
  
  private List<EnrichingParameter> enrichingParameters = new ArrayList<>();
  
  public SearchParameter getSearchParameters() {
    return this.searchParameters;
  }
  
  public ParametricSearchRequest(SearchParameter paramSearchParameter) {
    this.searchParameters = paramSearchParameter;
  }
  
  public void addEnrichingParameter(EnrichingParameter paramEnrichingParameter) {
    this.enrichingParameters.add(paramEnrichingParameter);
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\request\ParametricSearchRequest.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */