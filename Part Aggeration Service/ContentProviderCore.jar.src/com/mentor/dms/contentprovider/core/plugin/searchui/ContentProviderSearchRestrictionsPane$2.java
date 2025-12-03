package com.mentor.dms.contentprovider.core.plugin.searchui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;

class null extends MouseAdapter {
  public void mousePressed(MouseEvent paramMouseEvent) {
    if (paramMouseEvent.isPopupTrigger())
      doPop(paramMouseEvent); 
  }
  
  public void mouseReleased(MouseEvent paramMouseEvent) {
    if (paramMouseEvent.isPopupTrigger())
      doPop(paramMouseEvent); 
  }
  
  private void doPop(MouseEvent paramMouseEvent) {
    JLabel jLabel = (JLabel)paramMouseEvent.getComponent();
    ContentProviderSearchRestrictionsPane.ValidValuesPopUpMenu validValuesPopUpMenu = new ContentProviderSearchRestrictionsPane.ValidValuesPopUpMenu(ContentProviderSearchRestrictionsPane.this, jLabel);
    validValuesPopUpMenu.show(paramMouseEvent.getComponent(), paramMouseEvent.getX(), paramMouseEvent.getY());
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\plugin\searchui\ContentProviderSearchRestrictionsPane$2.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */