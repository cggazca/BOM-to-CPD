package com.mentor.dms.contentprovider.core.plugin;

import com.mentor.datafusion.oi.OIException;
import com.mentor.datafusion.oi.interceptor.CommitInterceptor;
import com.mentor.datafusion.oi.interceptor.InterceptionException;
import com.mentor.datafusion.oi.interceptor.InterceptorFactory;
import com.mentor.datafusion.oi.interceptor.OpenInterceptor;
import com.mentor.datafusion.oi.type.OIObject;
import com.mentor.datafusion.utils.logger.MGLogger;
import java.util.Collection;
import java.util.Collections;

public class ExternalContentInterceptorFactory implements InterceptorFactory {
  private static MGLogger log = MGLogger.getLogger(ExternalContentInterceptorFactory.class);
  
  public CommitInterceptor getCommitInterceptor() {
    return new CommitInterceptor() {
        public void exception(Collection<OIObject> param1Collection, OIException param1OIException) {}
        
        public void postCommit(Collection<OIObject> param1Collection) {}
        
        public Collection<OIObject> preCommit(OIObject param1OIObject) throws InterceptionException {
          return (param1OIObject.getMode() != 5) ? Collections.emptyList() : Collections.emptyList();
        }
      };
  }
  
  public OpenInterceptor getOpenInterceptor() {
    return null;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\plugin\ExternalContentInterceptorFactory.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */