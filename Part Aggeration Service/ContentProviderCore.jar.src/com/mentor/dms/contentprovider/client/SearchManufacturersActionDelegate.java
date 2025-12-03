package com.mentor.dms.contentprovider.client;

import com.mentor.datafusion.utils.logger.MGLogger;
import com.mentor.dms.ui.DefaultActionDelegate;
import com.mentor.dms.ui.popupcontext.ContextEvent;

public class SearchManufacturersActionDelegate extends DefaultActionDelegate {
  MGLogger log = MGLogger.getLogger(SearchManufacturersActionDelegate.class);
  
  public void actionPerformed(ContextEvent paramContextEvent) {
    DesktopClient.searchManufacturers();
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\client\SearchManufacturersActionDelegate.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */