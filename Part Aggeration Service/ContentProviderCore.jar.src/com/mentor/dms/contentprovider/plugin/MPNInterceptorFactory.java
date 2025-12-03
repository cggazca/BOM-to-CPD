package com.mentor.dms.contentprovider.plugin;

import com.mentor.datafusion.oi.OIException;
import com.mentor.datafusion.oi.interceptor.CommitInterceptor;
import com.mentor.datafusion.oi.interceptor.InterceptionException;
import com.mentor.datafusion.oi.interceptor.InterceptorFactory;
import com.mentor.datafusion.oi.interceptor.OpenInterceptor;
import com.mentor.datafusion.oi.type.OIObject;
import com.mentor.datafusion.oi.util.LockUtilities;
import com.mentor.datafusion.utils.logger.MGLogger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class MPNInterceptorFactory implements InterceptorFactory {
  private static MGLogger log = MGLogger.getLogger(MPNInterceptorFactory.class);
  
  public CommitInterceptor getCommitInterceptor() {
    return new CommitInterceptor() {
        public void exception(Collection<OIObject> param1Collection, OIException param1OIException) {}
        
        public void postCommit(Collection<OIObject> param1Collection) {}
        
        public Collection<OIObject> preCommit(OIObject param1OIObject) throws InterceptionException {
          if (param1OIObject.getMode() == 3)
            try {
              OIObject oIObject = param1OIObject.getObject("ExternalContentId");
              if (oIObject == null)
                return Collections.emptyList(); 
              String str1 = param1OIObject.getString("ManufacturerpartId");
              if (str1.equals(oIObject.getObjectID()))
                return Collections.emptyList(); 
              String str2 = LockUtilities.getSessionLock(oIObject);
              if (str2 != null)
                LockUtilities.releaseSessionLock(oIObject); 
              oIObject.set("ExternalContentId", str1);
              ArrayList<OIObject> arrayList = new ArrayList();
              arrayList.add(oIObject);
              return arrayList;
            } catch (Exception exception) {
              String str = "Unable to rename referenced External Content object :" + exception.getMessage();
              MPNInterceptorFactory.log.warn(str);
              throw new InterceptionException(str);
            }  
          if (param1OIObject.getMode() == 5)
            try {
              OIObject oIObject = param1OIObject.getObject("ExternalContentId");
              if (oIObject == null)
                return Collections.emptyList(); 
              String str = LockUtilities.getSessionLock(oIObject);
              if (str != null)
                LockUtilities.releaseSessionLock(oIObject); 
              param1OIObject.getObjectManager().deleteObject(oIObject);
              ArrayList<OIObject> arrayList = new ArrayList();
              arrayList.add(oIObject);
              return arrayList;
            } catch (Exception exception) {
              String str = "Unable to delete associated External Content object :" + exception.getMessage();
              MPNInterceptorFactory.log.warn(str);
              throw new InterceptionException(str);
            }  
          return Collections.emptyList();
        }
      };
  }
  
  public OpenInterceptor getOpenInterceptor() {
    return null;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\plugin\MPNInterceptorFactory.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */