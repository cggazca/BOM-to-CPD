package com.mentor.dms.contentprovider.sf;

import com.mentor.datafusion.oi.OICursor;
import com.mentor.datafusion.oi.OIException;
import com.mentor.datafusion.oi.OIHelper;
import com.mentor.datafusion.oi.OIObjectManager;
import com.mentor.datafusion.oi.OIQuery;
import com.mentor.datafusion.oi.interceptor.CommitInterceptor;
import com.mentor.datafusion.oi.interceptor.InterceptionException;
import com.mentor.datafusion.oi.interceptor.OpenInterceptor;
import com.mentor.datafusion.oi.type.OIObject;
import com.mentor.datafusion.utils.logger.MGLogger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class ComponentHeightMPNInterceptorFactory extends ComponentHeightInterceptorFactory {
  private static MGLogger log = MGLogger.getLogger(ComponentHeightMPNInterceptorFactory.class);
  
  public CommitInterceptor getCommitInterceptor() {
    return new CommitInterceptor() {
        public void exception(Collection<OIObject> param1Collection, OIException param1OIException) {}
        
        public void postCommit(Collection<OIObject> param1Collection) {}
        
        public Collection<OIObject> preCommit(OIObject param1OIObject) throws InterceptionException {
          if (param1OIObject.getMode() != 1 && param1OIObject.getMode() != 3)
            return Collections.emptyList(); 
          ArrayList<OIObject> arrayList = new ArrayList();
          try {
            if (!ComponentHeightMPNInterceptorFactory.this.isComponentHeightDataModelLoaded(param1OIObject.getObjectManager()) || (!param1OIObject.getOIClass().hasField("seProductDepth") && !param1OIObject.getOIClass().hasField("sePackageHeight")))
              return Collections.emptyList(); 
            OIObjectManager oIObjectManager = param1OIObject.getObjectManager();
            OIQuery oIQuery = oIObjectManager.createQuery("Component", true);
            oIQuery.addRestriction("ApprovedManufacturerList.MfgPartNumber", OIHelper.escapeQueryRestriction(param1OIObject.getObjectID()));
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
      };
  }
  
  public OpenInterceptor getOpenInterceptor() {
    return null;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\ComponentHeightMPNInterceptorFactory.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */