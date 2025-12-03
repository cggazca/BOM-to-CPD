package com.mentor.dms.contentprovider.plugin.searchui;

import com.mentor.dms.dmsbrowser.DMSBrowserQuickSearchBar;
import com.mentor.dms.dmsbrowser.DMSSearchRestrictionClassPanel;
import com.mentor.dms.dmsbrowser.DMSSearchRestrictionPanel;
import java.awt.Component;
import java.awt.Container;

public class DMSQuickSearch {
  public static String getSearchString() {
    String str = null;
    DMSSearchRestrictionPanel dMSSearchRestrictionPanel = DMSSearchRestrictionPanel.getInstance();
    if (dMSSearchRestrictionPanel != null) {
      DMSSearchRestrictionClassPanel dMSSearchRestrictionClassPanel = dMSSearchRestrictionPanel.getCurSearchRestrictionClassPanel();
      if (dMSSearchRestrictionClassPanel != null && !dMSSearchRestrictionClassPanel.isClassicSearchVisible()) {
        DMSBrowserQuickSearchBar dMSBrowserQuickSearchBar = findQuickSearchBar((Container)dMSSearchRestrictionClassPanel);
        if (dMSBrowserQuickSearchBar != null)
          str = dMSBrowserQuickSearchBar.getSearchRestriction().trim(); 
      } 
    } 
    return str;
  }
  
  public static boolean isVisible() {
    DMSSearchRestrictionPanel dMSSearchRestrictionPanel = DMSSearchRestrictionPanel.getInstance();
    if (dMSSearchRestrictionPanel != null) {
      DMSSearchRestrictionClassPanel dMSSearchRestrictionClassPanel = dMSSearchRestrictionPanel.getCurSearchRestrictionClassPanel();
      if (dMSSearchRestrictionClassPanel != null && !dMSSearchRestrictionClassPanel.isClassicSearchVisible()) {
        DMSBrowserQuickSearchBar dMSBrowserQuickSearchBar = findQuickSearchBar((Container)dMSSearchRestrictionClassPanel);
        return (dMSBrowserQuickSearchBar != null);
      } 
    } 
    return false;
  }
  
  private static DMSBrowserQuickSearchBar findQuickSearchBar(Container paramContainer) {
    DMSBrowserQuickSearchBar dMSBrowserQuickSearchBar = null;
    for (Component component : paramContainer.getComponents()) {
      if (component instanceof DMSBrowserQuickSearchBar) {
        dMSBrowserQuickSearchBar = (DMSBrowserQuickSearchBar)component;
      } else if (component instanceof Container) {
        dMSBrowserQuickSearchBar = findQuickSearchBar((Container)component);
      } 
      if (dMSBrowserQuickSearchBar != null)
        break; 
    } 
    return dMSBrowserQuickSearchBar;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\plugin\searchui\DMSQuickSearch.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */