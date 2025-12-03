package com.mentor.dms.contentprovider.core.actions;

import com.mentor.datafusion.oi.type.OIObject;
import com.mentor.datafusion.utils.logger.MGLogger;
import com.mentor.dms.contentprovider.core.ContentProviderException;
import com.mentor.dms.contentprovider.core.ContentProviderGlobal;
import com.mentor.dms.contentprovider.core.IContentProviderResultRecord;
import com.mentor.dms.contentprovider.core.plugin.searchui.ContentProviderSearchWindow;
import com.mentor.dms.contentprovider.core.sync.ContentProviderSync;
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
    super("Create/Update Manufacturer Part", getImageIcon(), "CREATE_MFG_PART_USERS");
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
      log.info("Crete/Update Manufacturer Part");
      Collection<IContentProviderResultRecord> collection = ContentProviderSearchWindow.getSelectedResults();
      IContentProviderResultRecord iContentProviderResultRecord = collection.iterator().next();
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


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\actions\CreateManufacturerPartAction.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */