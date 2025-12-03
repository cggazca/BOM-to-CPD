package com.mentor.dms.contentprovider.core.plugin;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ToolTipManager;

class null extends MouseAdapter {
  public void mouseEntered(MouseEvent paramMouseEvent) {
    ToolTipManager.sharedInstance().setInitialDelay(0);
    ToolTipManager.sharedInstance().setDismissDelay(60000);
  }
  
  public void mouseExited(MouseEvent paramMouseEvent) {
    ToolTipManager.sharedInstance().setInitialDelay(ProductImageLabel.this.defaultInitialDelay);
    ToolTipManager.sharedInstance().setDismissDelay(ProductImageLabel.this.defaultDismissDelay);
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\plugin\ProductImageLabel$1.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */