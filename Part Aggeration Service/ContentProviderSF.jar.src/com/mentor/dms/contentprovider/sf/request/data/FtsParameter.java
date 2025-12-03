package com.mentor.dms.contentprovider.sf.request.data;

import com.mentor.dms.contentprovider.core.request.Filter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FtsParameter {
  private Map<String, String> customParameters = new HashMap<>();
  
  private Paging paging;
  
  private Map<String, String> match = new HashMap<>();
  
  private FtsPartClassContext partClassContext = new FtsPartClassContext();
  
  public void setTerm(String paramString) {
    this.match.put("term", paramString);
  }
  
  public void setPaging(Paging paramPaging) {
    this.paging = paramPaging;
  }
  
  public void setPartClassID(String paramString) {
    this.partClassContext.setPartClassID(paramString);
  }
  
  public void setFilter(Filter paramFilter) {
    this.partClassContext.setFilter(paramFilter);
  }
  
  public void setCustomParameter(String paramString1, String paramString2) {
    this.customParameters.put(paramString1, paramString2);
  }
  
  public void addOutputs(String paramString) {
    this.partClassContext.addOutputs(paramString);
  }
  
  public void setOutputs(List<String> paramList) {
    this.partClassContext.setOutputs(paramList);
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\request\data\FtsParameter.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */