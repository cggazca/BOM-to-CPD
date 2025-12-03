package com.mentor.dms.contentprovider.core.plugin;

import com.mentor.datafusion.oi.OIException;
import com.mentor.datafusion.oi.interceptor.CommitInterceptor;
import com.mentor.datafusion.oi.interceptor.InterceptionException;
import com.mentor.datafusion.oi.type.OIObject;
import com.mentor.datafusion.oi.util.LockUtilities;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

class null implements CommitInterceptor {
  public void exception(Collection<OIObject> paramCollection, OIException paramOIException) {}
  
  public void postCommit(Collection<OIObject> paramCollection) {}
  
  public Collection<OIObject> preCommit(OIObject paramOIObject) throws InterceptionException {
    if (paramOIObject.getMode() == 3) {
      MPNInterceptorFactory.log.info("CommitInterceptor. mode=3");
      try {
        OIObject oIObject = paramOIObject.getObject("ExternalContentId");
        if (oIObject == null)
          return Collections.emptyList(); 
        String str1 = paramOIObject.getString("ManufacturerpartId");
        if (str1.equals(oIObject.getObjectID()))
          return Collections.emptyList(); 
        String str2 = LockUtilities.getSessionLock(oIObject);
        if (str2 != null)
          LockUtilities.releaseSessionLock(oIObject); 
        MPNInterceptorFactory.log.info("Rename referenced External Content.");
        oIObject.set("ExternalContentId", str1);
        ArrayList<OIObject> arrayList = new ArrayList();
        arrayList.add(oIObject);
        return arrayList;
      } catch (Exception exception) {
        String str = "Unable to rename referenced External Content object :" + exception.getMessage();
        MPNInterceptorFactory.log.warn(str);
        throw new InterceptionException(str);
      } 
    } 
    if (paramOIObject.getMode() == 5) {
      MPNInterceptorFactory.log.info("CommitInterceptor. mode=5");
      try {
        OIObject oIObject = paramOIObject.getObject("ExternalContentId");
        if (oIObject == null)
          return Collections.emptyList(); 
        String str = LockUtilities.getSessionLock(oIObject);
        if (str != null)
          LockUtilities.releaseSessionLock(oIObject); 
        MPNInterceptorFactory.log.info("Delete associated External Content.");
        paramOIObject.getObjectManager().deleteObject(oIObject);
        ArrayList<OIObject> arrayList = new ArrayList();
        arrayList.add(oIObject);
        return arrayList;
      } catch (Exception exception) {
        String str = "Unable to delete associated External Content object :" + exception.getMessage();
        MPNInterceptorFactory.log.warn(str);
        throw new InterceptionException(str);
      } 
    } 
    return Collections.emptyList();
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\plugin\MPNInterceptorFactory$1.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */