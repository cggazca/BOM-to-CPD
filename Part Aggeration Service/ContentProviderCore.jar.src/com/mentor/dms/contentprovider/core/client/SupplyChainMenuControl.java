package com.mentor.dms.contentprovider.core.client;

import com.mentor.datafusion.oi.OIException;
import com.mentor.datafusion.oi.internal.OIInternalHelper;
import com.mentor.dms.contentprovider.core.ContentProviderGlobal;
import java.util.List;
import org.eclipse.core.expressions.PropertyTester;

public class SupplyChainMenuControl extends PropertyTester {
  public boolean test(Object paramObject1, String paramString, Object[] paramArrayOfObject, Object paramObject2) {
    try {
      return OIInternalHelper.checkNamedLicenses(ContentProviderGlobal.getDMSInstance().getOIObjectManagerFactory(), List.of("edmsf"));
    } catch (OIException oIException) {
      return false;
    } 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\client\SupplyChainMenuControl.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */