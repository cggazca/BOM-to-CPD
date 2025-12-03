package com.mentor.dms.contentprovider.actions;

import com.mentor.datafusion.oi.type.OIObject;
import com.mentor.datafusion.utils.logger.MGLogger;
import com.mentor.dms.contentprovider.ContentProviderException;
import com.mentor.dms.contentprovider.ContentProviderGlobal;
import com.mentor.dms.contentprovider.IContentProviderResultRecord;
import com.mentor.dms.contentprovider.plugin.searchui.ContentProviderSearchWindow;
import com.mentor.dms.contentprovider.sync.ContentProviderSync;
import com.mentor.dms.ui.searchmask.NoSearchMaskFoundException;
import com.mentor.dms.ui.searchmask.SearchMask;
import com.mentor.dms.ui.searchmask.SearchMaskManager;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.Collection;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class CreateManufacturerPartAction extends AbstractSearchResultsAction {
  private static MGLogger log = MGLogger.getLogger(CreateManufacturerPartAction.class);
  
  public CreateManufacturerPartAction() {
    super("Create Manufacturer Part", getImageIcon(), "CREATE_MFG_PART_USERS");
    setEnabled(true);
  }
  
  private static ImageIcon getImageIcon() {
    Image image = Toolkit.getDefaultToolkit().getImage(CreateManufacturerPartAction.class.getResource("images/add_mpn.png"));
    return new ImageIcon(image);
  }
  
  public void selectionHandler() {
    setEnabled((ContentProviderSearchWindow.getSelectionCount() == 1));
  }
  
  public void doAction() {
    try {
      Collection<IContentProviderResultRecord> collection = ContentProviderSearchWindow.getSelectedResults();
      IContentProviderResultRecord iContentProviderResultRecord = collection.iterator().next();
      int i = JOptionPane.showConfirmDialog(ContentProviderGlobal.getRootFrame(), "Create Manufacturer Part '" + iContentProviderResultRecord.getPartNumber() + "' from '" + iContentProviderResultRecord.getManufacturerName() + "'?", "Create Manufacturer Part", 0);
      if (i != 0)
        return; 
      ContentProviderSearchWindow.getInstance().setCursor(new Cursor(3));
      OIObject oIObject = ContentProviderSync.createMPNFromEC(ContentProviderGlobal.getDMSInstance().getObjectManager(), iContentProviderResultRecord, ContentProviderSync.OverwriteEnum.ASK);
      if (oIObject != null) {
        try {
          SearchMaskManager searchMaskManager = ContentProviderGlobal.getDMSInstance().getSearchMaskManager();
          SearchMask searchMask = searchMaskManager.open(oIObject.getOIClass());
        } catch (NoSearchMaskFoundException noSearchMaskFoundException) {}
        ContentProviderGlobal.getDMSInstance().getObjectPanelManager().showObject(oIObject);
      } 
    } catch (ContentProviderException contentProviderException) {
      JOptionPane.showMessageDialog(ContentProviderGlobal.getRootFrame(), contentProviderException.getMessage());
    } finally {
      ContentProviderSearchWindow.getInstance().setCursor(null);
    } 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\actions\CreateManufacturerPartAction.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */