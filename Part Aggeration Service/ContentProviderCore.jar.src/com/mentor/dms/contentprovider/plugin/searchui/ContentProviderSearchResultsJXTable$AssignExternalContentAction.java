package com.mentor.dms.contentprovider.plugin.searchui;

import com.mentor.datafusion.oi.model.OIClass;
import com.mentor.datafusion.oi.type.OIObject;
import com.mentor.dms.contentprovider.AbstractContentProvider;
import com.mentor.dms.contentprovider.ContentProviderGlobal;
import com.mentor.dms.contentprovider.IContentProviderResultRecord;
import com.mentor.dms.contentprovider.actions.AbstractSearchResultsAction;
import com.mentor.dms.contentprovider.sync.ContentProviderSync;
import com.mentor.dms.contentprovider.sync.gui.ContentProviderReconcileApp;
import com.mentor.dms.ui.objectpanel.ObjectPanel;
import com.mentor.dms.ui.objectpanel.ObjectPanelManager;
import java.awt.Cursor;
import java.awt.Image;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class AssignExternalContentAction extends AbstractSearchResultsAction {
  public AssignExternalContentAction() {
    super("Assign External Content...", getImageIcon());
    setEnabled(true);
  }
  
  private static ImageIcon getImageIcon() {
    Image image1 = ContentProviderGlobal.getAppIconImage();
    Image image2 = image1.getScaledInstance(16, 16, 4);
    return new ImageIcon(image2);
  }
  
  public void selectionHandler() {
    setEnabled((ContentProviderSearchWindow.getSelectionCount() == 1));
  }
  
  public void doAction() {
    try {
      Collection<IContentProviderResultRecord> collection = ContentProviderSearchWindow.getSelectedResults();
      IContentProviderResultRecord iContentProviderResultRecord = collection.iterator().next();
      AbstractContentProvider abstractContentProvider = iContentProviderResultRecord.getContentProvider();
      OIObject oIObject = ContentProviderSearchWindow.getAssignmentMPN();
      String str1 = oIObject.getString("PartNumber");
      String str2 = oIObject.getStringified("ManufacturerName");
      int i = JOptionPane.showConfirmDialog(ContentProviderSearchWindow.getInstance(), "Assign selected External Content to Manufacturer Part '" + str1 + "' by '" + str2 + "'?", "Assign External Content", 0);
      if (i == 0) {
        OIClass oIClass = oIObject.getOIClass();
        ContentProviderSearchWindow.getInstance().setCursor(new Cursor(3));
        OIObject oIObject1 = ContentProviderSync.assignEC2MPN(abstractContentProvider, oIObject, iContentProviderResultRecord.getIdPropertyMap(abstractContentProvider.getConfig()));
        IContentProviderResultRecord iContentProviderResultRecord1 = abstractContentProvider.getPart(iContentProviderResultRecord);
        OIObject oIObject2 = ContentProviderSync.getSyncRegistryEntry(oIObject1, abstractContentProvider.getId());
        ContentProviderSync.syncExernalContentPartRecordToDMS(oIObject, oIObject1, oIObject2, abstractContentProvider, iContentProviderResultRecord1, new Date());
        HashSet<String> hashSet = new HashSet();
        hashSet.add(oIObject.getObjectID());
        ContentProviderReconcileApp.createAndShowGUI(oIObject1.getObjectManager(), hashSet);
        ObjectPanelManager objectPanelManager = ContentProviderGlobal.getDMSInstance().getObjectPanelManager();
        if (oIClass.getPath().equals(oIObject.getOIClass().getPath())) {
          objectPanelManager.update(oIObject);
        } else {
          JOptionPane.showMessageDialog(ContentProviderGlobal.getRootFrame(), "Manufacturer Part was moved from '" + oIClass.getLabel() + "' to '" + oIObject.getOIClass().getLabel() + "'.\n\nManufacturer Part will be closed and re-opened.", "Assign External Content", 0);
          ObjectPanel objectPanel = objectPanelManager.isObjectShown(oIObject);
          if (objectPanel != null)
            objectPanel.close(); 
          objectPanelManager.showObject(oIObject);
        } 
      } 
    } catch (Exception exception) {
      JOptionPane.showMessageDialog(ContentProviderGlobal.getRootFrame(), exception.getMessage());
    } finally {
      ContentProviderSearchWindow.getInstance().setCursor(null);
    } 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\plugin\searchui\ContentProviderSearchResultsJXTable$AssignExternalContentAction.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */