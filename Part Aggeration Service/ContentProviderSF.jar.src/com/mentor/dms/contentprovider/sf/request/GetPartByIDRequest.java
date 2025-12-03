package com.mentor.dms.contentprovider.sf.request;

import com.mentor.dms.contentprovider.sf.request.data.EnrichingDetailsParameters;
import com.mentor.dms.contentprovider.sf.request.data.SearchDetailsParameters;
import java.util.ArrayList;
import java.util.List;

public class GetPartByIDRequest {
  private SearchDetailsParameters searchDetailsParameters = new SearchDetailsParameters();
  
  private List<EnrichingDetailsParameters> enrichingDetailsParameters = new ArrayList<>();
  
  public void addPartID(String paramString) {
    this.searchDetailsParameters.addPartID(paramString);
  }
  
  public void addEnrichingDetailsParameters(EnrichingDetailsParameters paramEnrichingDetailsParameters) {
    this.enrichingDetailsParameters.add(paramEnrichingDetailsParameters);
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\request\GetPartByIDRequest.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */