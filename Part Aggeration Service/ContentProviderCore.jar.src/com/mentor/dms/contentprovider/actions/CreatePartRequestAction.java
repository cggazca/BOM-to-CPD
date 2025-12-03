package com.mentor.dms.contentprovider.actions;

import com.mentor.datafusion.utils.logger.MGLogger;
import com.mentor.dms.contentprovider.ContentProviderSearch;
import com.mentor.dms.contentprovider.IContentProviderPartRequest;
import com.mentor.dms.contentprovider.IContentProviderResultRecord;
import com.mentor.dms.contentprovider.plugin.searchui.ContentProviderSearchWindow;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.Collection;
import javax.swing.ImageIcon;

public class CreatePartRequestAction extends AbstractSearchResultsAction {
  private static MGLogger log = MGLogger.getLogger(CreatePartRequestAction.class);
  
  private boolean bMultiplePartsPerRequest = false;
  
  public CreatePartRequestAction() {
    super("New Part Request", getImageIcon());
    setEnabled(true);
  }
  
  public CreatePartRequestAction(boolean paramBoolean) {
    this();
    this.bMultiplePartsPerRequest = paramBoolean;
  }
  
  private static ImageIcon getImageIcon() {
    Image image = Toolkit.getDefaultToolkit().getImage(CreatePartRequestAction.class.getResource("images/add_request.png"));
    return new ImageIcon(image);
  }
  
  public void selectionHandler() {
    if (this.bMultiplePartsPerRequest) {
      setEnabled((ContentProviderSearchWindow.getSelectionCount() >= 1 && ContentProviderSearchWindow.getSelectionCount() <= 5));
    } else {
      setEnabled((ContentProviderSearchWindow.getSelectionCount() == 1));
    } 
  }
  
  public void doAction() {
    ContentProviderSearchWindow.getInstance().setCursor(new Cursor(3));
    try {
      Collection<IContentProviderResultRecord> collection = ContentProviderSearch.getSelectedSearchResults();
      IContentProviderPartRequest iContentProviderPartRequest = ContentProviderSearch.getActiveContentProvider().getPartRequest();
      if (iContentProviderPartRequest != null)
        if (this.bMultiplePartsPerRequest) {
          iContentProviderPartRequest.createPartRequest(collection);
        } else {
          IContentProviderResultRecord iContentProviderResultRecord = collection.iterator().next();
          iContentProviderPartRequest.createPartRequest(iContentProviderResultRecord);
        }  
    } finally {
      ContentProviderSearchWindow.getInstance().setCursor(null);
    } 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\actions\CreatePartRequestAction.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */