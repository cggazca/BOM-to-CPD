package com.mentor.dms.contentprovider.sf;

import java.util.HashMap;

class SearchInfo {
  boolean bPartNumberSearch = false;
  
  boolean bDescriptionSearch = false;
  
  boolean bKeywordSearch = false;
  
  boolean bParametricSearch = false;
  
  boolean bDefinitionSearch = false;
  
  public String searchURL;
  
  public HashMap<String, String> paramMap = new HashMap<>();
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\AggregationServiceWebCall$SearchInfo.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */