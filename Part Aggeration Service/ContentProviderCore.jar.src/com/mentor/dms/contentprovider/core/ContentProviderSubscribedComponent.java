package com.mentor.dms.contentprovider.core;

import java.util.ArrayList;
import java.util.List;

public class ContentProviderSubscribedComponent {
  private String cpn;
  
  private List<ContentProviderSubscribedAML> amlList = new ArrayList<>();
  
  public String getCPN() {
    return this.cpn;
  }
  
  public void setCPN(String paramString) {
    this.cpn = paramString;
  }
  
  public List<ContentProviderSubscribedAML> getAML() {
    return this.amlList;
  }
  
  public void addAML(ContentProviderSubscribedAML paramContentProviderSubscribedAML) {
    this.amlList.add(paramContentProviderSubscribedAML);
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\ContentProviderSubscribedComponent.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */