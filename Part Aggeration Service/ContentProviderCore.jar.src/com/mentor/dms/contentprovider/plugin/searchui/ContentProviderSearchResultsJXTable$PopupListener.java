package com.mentor.dms.contentprovider.plugin.searchui;

import com.mentor.dms.contentprovider.actions.AbstractSearchResultsAction;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class PopupListener extends MouseAdapter {
  public void mouseClicked(MouseEvent paramMouseEvent) {
    if (paramMouseEvent.getClickCount() == 2) {
      AbstractSearchResultsAction abstractSearchResultsAction = ContentProviderSearchResultsJXTable.this.actionList.get(0);
      if (abstractSearchResultsAction != null)
        abstractSearchResultsAction.doAction(); 
    } 
  }
  
  public void mousePressed(MouseEvent paramMouseEvent) {
    showPopup(paramMouseEvent);
  }
  
  public void mouseReleased(MouseEvent paramMouseEvent) {
    showPopup(paramMouseEvent);
  }
  
  private void showPopup(MouseEvent paramMouseEvent) {
    if (paramMouseEvent.isPopupTrigger()) {
      for (AbstractSearchResultsAction abstractSearchResultsAction : ContentProviderSearchResultsJXTable.this.actionList) {
        abstractSearchResultsAction.selectionHandler();
        abstractSearchResultsAction.setUserEnabled();
      } 
      ContentProviderSearchResultsJXTable.this.popupMenu.show(paramMouseEvent.getComponent(), paramMouseEvent.getX(), paramMouseEvent.getY());
    } 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\plugin\searchui\ContentProviderSearchResultsJXTable$PopupListener.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */