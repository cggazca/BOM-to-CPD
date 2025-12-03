package com.mentor.dms.contentprovider.sf;

import com.mentor.dms.contentprovider.core.client.DesktopClient;
import com.mentor.dms.ui.ActionDelegate;
import com.mentor.dms.ui.DefaultActionDelegate;
import com.mentor.dms.ui.popupcontext.ContextEvent;

public class PartSearchActionDelegate extends DefaultActionDelegate implements ActionDelegate {
  public void actionPerformed(ContextEvent paramContextEvent) {
    DesktopClient.doSearch(DesktopClient.SearchContext.COMPONENT_ENGINEER);
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\PartSearchActionDelegate.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */