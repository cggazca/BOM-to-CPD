package com.mentor.dms.contentprovider.client;

import com.mentor.datafusion.oi.model.OIClass;
import com.mentor.dms.contentprovider.core.plugin.Activator;
import com.mentor.dms.ui.searchmask.SearchMask;
import org.apache.log4j.Logger;
import org.eclipse.core.expressions.PropertyTester;

public class SelectedClassPropertyTester extends PropertyTester {
  private static Logger logger = Logger.getLogger(SelectedClassPropertyTester.class);
  
  public boolean test(Object paramObject1, String paramString, Object[] paramArrayOfObject, Object paramObject2) {
    SearchMask searchMask = (SearchMask)Activator.getDefault().getDMSInstance().getSearchMaskManager().getActive();
    if (searchMask == null || searchMask.getOIClass() == null)
      return false; 
    OIClass oIClass = searchMask.getOIClass().getRootClass();
    return oIClass.getName().equals(paramObject2);
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\client\SelectedClassPropertyTester.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */