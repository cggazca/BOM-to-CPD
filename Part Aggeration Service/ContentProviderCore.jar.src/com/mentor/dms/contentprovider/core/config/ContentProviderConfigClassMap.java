package com.mentor.dms.contentprovider.core.config;

import org.w3c.dom.Element;

public class ContentProviderConfigClassMap {
  private String contentProviderId;
  
  public String getContentProviderId() {
    return this.contentProviderId;
  }
  
  public void read(Element paramElement) throws ContentProviderConfigException {
    this.contentProviderId = paramElement.getAttribute("id");
  }
  
  public String toString() {
    return this.contentProviderId;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\config\ContentProviderConfigClassMap.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */