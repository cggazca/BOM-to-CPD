package com.mentor.dms.contentprovider.sf;

import com.mentor.datafusion.oi.OIException;
import com.mentor.datafusion.oi.interceptor.CommitInterceptor;
import com.mentor.datafusion.oi.interceptor.InterceptionException;
import com.mentor.datafusion.oi.interceptor.OpenInterceptor;
import com.mentor.datafusion.oi.type.OIObject;
import java.util.Collection;
import java.util.Collections;

public class ComponentHeightCompInterceptorFactory extends ComponentHeightInterceptorFactory {
  public CommitInterceptor getCommitInterceptor() {
    return new CommitInterceptor() {
        public void exception(Collection<OIObject> param1Collection, OIException param1OIException) {}
        
        public void postCommit(Collection<OIObject> param1Collection) {}
        
        public Collection<OIObject> preCommit(OIObject param1OIObject) throws InterceptionException {
          if (param1OIObject.getMode() != 1 && param1OIObject.getMode() != 3)
            return Collections.emptyList(); 
          try {
            ComponentHeightCompInterceptorFactory.this.updateComponentHeight(param1OIObject);
          } catch (OIException oIException) {
            throw new InterceptionException(oIException);
          } 
          return Collections.emptyList();
        }
      };
  }
  
  public OpenInterceptor getOpenInterceptor() {
    return null;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\ComponentHeightCompInterceptorFactory.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */