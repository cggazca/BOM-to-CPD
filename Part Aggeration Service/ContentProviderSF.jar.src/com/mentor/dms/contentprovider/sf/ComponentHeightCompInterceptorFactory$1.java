package com.mentor.dms.contentprovider.sf;

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
    if (paramOIObject.getMode() != 1 && paramOIObject.getMode() != 3)
      return Collections.emptyList(); 
    try {
      ComponentHeightCompInterceptorFactory.this.updateComponentHeight(paramOIObject);
    } catch (OIException oIException) {
      throw new InterceptionException(oIException);
    } 
    return Collections.emptyList();
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\ComponentHeightCompInterceptorFactory$1.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */