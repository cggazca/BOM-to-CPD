package com.mentor.dms.contentprovider.core.sync;

import java.util.Date;
import java.util.HashMap;

class ECData {
  String ecId;
  
  HashMap<String, String> providerRefMap = new HashMap<>();
  
  Date lastSyncDate;
  
  String cacheId = "";
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\sync\ContentProviderSync$ECData.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */