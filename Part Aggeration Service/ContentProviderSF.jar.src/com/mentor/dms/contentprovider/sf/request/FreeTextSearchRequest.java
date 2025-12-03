package com.mentor.dms.contentprovider.sf.request;

import com.mentor.dms.contentprovider.sf.request.data.EnrichingParameter;
import com.mentor.dms.contentprovider.sf.request.data.FtsParameter;
import java.util.ArrayList;
import java.util.List;

public class FreeTextSearchRequest {
  private FtsParameter ftsParameters;
  
  private List<EnrichingParameter> enrichingParameters = new ArrayList<>();
  
  public FreeTextSearchRequest(FtsParameter paramFtsParameter) {
    this.ftsParameters = paramFtsParameter;
  }
  
  public void addEnrichingParameter(EnrichingParameter paramEnrichingParameter) {
    this.enrichingParameters.add(paramEnrichingParameter);
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\request\FreeTextSearchRequest.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */