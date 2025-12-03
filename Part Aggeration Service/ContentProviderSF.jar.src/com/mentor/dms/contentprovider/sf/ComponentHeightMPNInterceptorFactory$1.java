package com.mentor.dms.contentprovider.sf;

import com.mentor.datafusion.oi.OICursor;
import com.mentor.datafusion.oi.OIException;
import com.mentor.datafusion.oi.OIHelper;
import com.mentor.datafusion.oi.OIObjectManager;
import com.mentor.datafusion.oi.OIQuery;
import com.mentor.datafusion.oi.interceptor.CommitInterceptor;
import com.mentor.datafusion.oi.interceptor.InterceptionException;
import com.mentor.datafusion.oi.type.OIObject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

class null implements CommitInterceptor {
  public void exception(Collection<OIObject> paramCollection, OIException paramOIException) {}
  
  public void postCommit(Collection<OIObject> paramCollection) {}
  
  public Collection<OIObject> preCommit(OIObject paramOIObject) throws InterceptionException {
    if (paramOIObject.getMode() != 1 && paramOIObject.getMode() != 3)
      return Collections.emptyList(); 
    ArrayList<OIObject> arrayList = new ArrayList();
    try {
      if (!ComponentHeightMPNInterceptorFactory.this.isComponentHeightDataModelLoaded(paramOIObject.getObjectManager()) || (!paramOIObject.getOIClass().hasField("seProductDepth") && !paramOIObject.getOIClass().hasField("sePackageHeight")))
        return Collections.emptyList(); 
      OIObjectManager oIObjectManager = paramOIObject.getObjectManager();
      OIQuery oIQuery = oIObjectManager.createQuery("Component", true);
      oIQuery.addRestriction("ApprovedManufacturerList.MfgPartNumber", OIHelper.escapeQueryRestriction(paramOIObject.getObjectID()));
      oIQuery.addColumn("PartNumber");
      OICursor oICursor = oIQuery.execute();
      while (oICursor.next()) {
        OIObject oIObject = oICursor.getProxyObject().getObject();
        try {
          oIObjectManager.refreshAndLockObject(oIObject);
          if (ComponentHeightMPNInterceptorFactory.this.updateComponentHeight(oIObject)) {
            arrayList.add(oIObject);
            continue;
          } 
          oIObjectManager.evict(oIObject);
        } catch (OIException oIException) {
          ComponentHeightMPNInterceptorFactory.log.warn("Warning:  Unable to update Component height on '" + oIObject.getObjectID() + "': " + oIException.getMessage());
        } 
      } 
    } catch (OIException oIException) {
      throw new InterceptionException(oIException);
    } 
    return arrayList;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\ComponentHeightMPNInterceptorFactory$1.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */