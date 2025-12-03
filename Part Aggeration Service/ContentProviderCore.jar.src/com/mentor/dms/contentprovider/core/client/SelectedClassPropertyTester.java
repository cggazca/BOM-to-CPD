package com.mentor.dms.contentprovider.core.client;

import com.mentor.datafusion.oi.model.OIClass;
import com.mentor.dms.contentprovider.core.plugin.Activator;
import com.mentor.dms.platform.internal.context.ContextService;
import com.mentor.dms.platform.ui.internal.selection.DefaultStructuredSelection;
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
    try {
      if (oIClass.getName().equals(paramObject2)) {
        Object object = ((ContextService)paramObject1).getCurrentContext().get("selection");
        ((ContextService)paramObject1).getContextSources();
        ((ContextService)paramObject1).getEvaluationContext();
        if (object != null) {
          DefaultStructuredSelection defaultStructuredSelection = (DefaultStructuredSelection)object;
          if (!defaultStructuredSelection.isEmpty())
            return true; 
        } 
      } 
    } catch (Exception exception) {
      logger.warn("SelectedClassPropertyTester Error:" + exception.getMessage());
    } 
    return false;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\client\SelectedClassPropertyTester.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */