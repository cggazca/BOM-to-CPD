package com.mentor.dms.contentprovider;

import com.mentor.datafusion.oi.type.OIObject;
import java.util.Map;

public interface IContentProviderPartNumberGenerator {
  String getNewPartNumber(Map<String, String> paramMap, OIObject paramOIObject) throws ContentProviderException;
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\IContentProviderPartNumberGenerator.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */