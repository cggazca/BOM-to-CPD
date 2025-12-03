package com.mentor.dms.contentprovider.core.client.catalogselector;

import com.mentor.datafusion.oi.OIException;
import com.mentor.datafusion.oi.type.OIObject;

public interface ICatalogNodeInfoFactory<T extends CatalogNodeInfo> {
  T create(OIObject paramOIObject) throws OIException;
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\client\catalogselector\ICatalogNodeInfoFactory.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */