package com.mentor.dms.contentprovider.sf.actions;

import com.mentor.dms.contentprovider.core.AbstractContentProvider;
import com.mentor.dms.contentprovider.core.ContentProviderSearch;
import com.mentor.dms.contentprovider.core.IContentProviderResultRecord;
import com.mentor.dms.contentprovider.core.actions.AbstractSearchResultsAction;
import com.mentor.dms.contentprovider.sf.client.SEClient;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.Collection;
import javax.swing.ImageIcon;

public class ViewSEPartOnlineAction extends AbstractSearchResultsAction {
  private static final long serialVersionUID = 1L;
  
  public ViewSEPartOnlineAction() {
    super("View in Supplyframe", getImageIcon());
    setEnabled(true);
  }
  
  private static ImageIcon getImageIcon() {
    Image image = Toolkit.getDefaultToolkit().getImage(ViewSEPartOnlineAction.class.getResource("images/supplyframe_small.png"));
    return new ImageIcon(image);
  }
  
  public void selectionHandler() {
    setEnabled((ContentProviderSearch.getSelectedSearchResultCount() == 1));
  }
  
  public void doAction() {
    AbstractContentProvider abstractContentProvider = ContentProviderSearch.getActiveContentProvider();
    Collection<IContentProviderResultRecord> collection = ContentProviderSearch.getSelectedSearchResults();
    IContentProviderResultRecord iContentProviderResultRecord = collection.iterator().next();
    SEClient.openSEPartInBrowser(abstractContentProvider, iContentProviderResultRecord.getObjectID());
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\actions\ViewSEPartOnlineAction.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */