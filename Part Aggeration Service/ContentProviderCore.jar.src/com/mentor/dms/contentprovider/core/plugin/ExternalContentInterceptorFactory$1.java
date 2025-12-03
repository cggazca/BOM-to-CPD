package com.mentor.dms.contentprovider.core.plugin;

import com.mentor.datafusion.oi.OIException;
import com.mentor.datafusion.oi.interceptor.CommitInterceptor;
import com.mentor.datafusion.oi.interceptor.InterceptionException;
import com.mentor.datafusion.oi.type.OIObject;
import java.util.Collection;
import java.util.Collections;

class null implements CommitInterceptor {
  public void exception(Collection<OIObject> paramCollection, OIException paramOIException) {}
  
  public void postCommit(Collection<OIObject> paramCollection) {}
  
  public Collection<OIObject> preCommit(OIObject paramOIObject) throws InterceptionException {
    return (paramOIObject.getMode() != 5) ? Collections.emptyList() : Collections.emptyList();
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\plugin\ExternalContentInterceptorFactory$1.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */