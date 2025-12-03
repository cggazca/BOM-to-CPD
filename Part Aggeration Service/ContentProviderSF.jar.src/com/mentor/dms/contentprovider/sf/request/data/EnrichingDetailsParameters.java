package com.mentor.dms.contentprovider.sf.request.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnrichingDetailsParameters {
  private String enrichingProviderId;
  
  private String enrichingProviderVersion;
  
  private Map<String, String> customParameters = new HashMap<>();
  
  private List<String> outputs = new ArrayList<>();
  
  public EnrichingDetailsParameters(String paramString1, String paramString2) {
    this.enrichingProviderId = paramString1;
    this.enrichingProviderVersion = paramString2;
  }
  
  public void setCustomParameter(String paramString1, String paramString2) {
    this.customParameters.put(paramString1, paramString2);
  }
  
  public void addOutputs(String paramString) {
    this.outputs.add(paramString);
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\request\data\EnrichingDetailsParameters.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */