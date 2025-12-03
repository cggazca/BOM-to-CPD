package com.mentor.dms.contentprovider.core.client;

import com.mentor.datafusion.oi.OIException;
import com.mentor.datafusion.oi.OIObjectManagerFactory;
import com.mentor.datafusion.oi.internal.OIInternalHelper;
import com.mentor.dms.contentprovider.core.ContentProviderGlobal;
import java.util.List;

public class CheckLicense {
  public static boolean hasSupplyChainLicense() {
    try {
      OIObjectManagerFactory oIObjectManagerFactory = ContentProviderGlobal.getOIObjectManager().getObjectManagerFactory();
      return OIInternalHelper.checkNamedLicenses(oIObjectManagerFactory, List.of("edmsf"));
    } catch (OIException oIException) {
      oIException.printStackTrace();
      return false;
    } 
  }
  
  public static final boolean checkRole(String paramString) throws OIException {
    return OIInternalHelper.checkUserRole(ContentProviderGlobal.getOIObjectManager(), paramString);
  }
  
  public static final boolean checkSFRoleAdmin() throws OIException {
    return OIInternalHelper.checkUserRole(ContentProviderGlobal.getOIObjectManager(), "SFAdmin");
  }
  
  public static final boolean checkSFRoleUser() throws OIException {
    return OIInternalHelper.checkUserRole(ContentProviderGlobal.getOIObjectManager(), "SFUser");
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\client\CheckLicense.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */