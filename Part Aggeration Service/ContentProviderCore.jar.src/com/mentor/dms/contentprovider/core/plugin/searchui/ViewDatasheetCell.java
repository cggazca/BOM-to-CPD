package com.mentor.dms.contentprovider.core.plugin.searchui;

import com.mentor.dms.contentprovider.core.IContentProviderResultRecord;
import com.mentor.dms.contentprovider.core.plugin.AbstractPushButtonCell;
import java.awt.event.ActionEvent;

public class ViewDatasheetCell extends AbstractPushButtonCell {
  public ViewDatasheetCell(IContentProviderResultRecord paramIContentProviderResultRecord) {
    ViewDatasheetButton viewDatasheetButton = new ViewDatasheetButton(paramIContentProviderResultRecord);
    add(viewDatasheetButton);
  }
  
  public ViewDatasheetCell(IContentProviderResultRecord paramIContentProviderResultRecord, int paramInt1, int paramInt2) {
    ViewDatasheetButton viewDatasheetButton = new ViewDatasheetButton(paramIContentProviderResultRecord, paramInt1, paramInt2);
    add(viewDatasheetButton);
  }
  
  public void actionPerformed(ActionEvent paramActionEvent) {}
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\plugin\searchui\ViewDatasheetCell.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */