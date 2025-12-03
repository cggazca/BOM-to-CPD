package com.mentor.dms.contentprovider.core.plugin.partrequest;

class Site {
  private String domainName;
  
  private String label;
  
  public Site(String paramString1, String paramString2) {
    this.domainName = paramString1;
    this.label = paramString2;
  }
  
  public String toString() {
    return this.label;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\plugin\partrequest\NewPartRequestDlg$Site.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */