package com.mentor.dms.contentprovider.sf.request.data;

import com.mentor.dms.contentprovider.core.request.Filter;
import java.util.ArrayList;
import java.util.List;

public class FtsPartClassContext {
  private String partClassId;
  
  private Filter filter;
  
  private List<String> outputs = new ArrayList<>();
  
  public void setPartClassID(String paramString) {
    this.partClassId = paramString;
  }
  
  public void setFilter(Filter paramFilter) {
    this.filter = paramFilter;
  }
  
  public void addOutputs(String paramString) {
    this.outputs.add(paramString);
  }
  
  public void setOutputs(List<String> paramList) {
    this.outputs = paramList;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\request\data\FtsPartClassContext.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */