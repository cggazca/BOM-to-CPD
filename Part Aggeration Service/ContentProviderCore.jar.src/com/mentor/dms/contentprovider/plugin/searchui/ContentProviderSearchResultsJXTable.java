package com.mentor.dms.contentprovider.plugin.searchui;

import com.mentor.datafusion.oi.model.OIClass;
import com.mentor.datafusion.oi.type.OIObject;
import com.mentor.datafusion.utils.logger.MGLogger;
import com.mentor.dms.contentprovider.AbstractContentProvider;
import com.mentor.dms.contentprovider.ContentProviderException;
import com.mentor.dms.contentprovider.ContentProviderGlobal;
import com.mentor.dms.contentprovider.IContentProviderResultRecord;
import com.mentor.dms.contentprovider.actions.AbstractSearchResultsAction;
import com.mentor.dms.contentprovider.client.DesktopClient;
import com.mentor.dms.contentprovider.plugin.CustomCellRenderer;
import com.mentor.dms.contentprovider.sync.ContentProviderSync;
import com.mentor.dms.contentprovider.sync.gui.ContentProviderReconcileApp;
import com.mentor.dms.ui.objectpanel.ObjectPanel;
import com.mentor.dms.ui.objectpanel.ObjectPanelManager;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jdesktop.swingx.decorator.Highlighter;
import org.jdesktop.swingx.decorator.HighlighterFactory;

public class ContentProviderSearchResultsJXTable extends JXTable {
  private static MGLogger log = MGLogger.getLogger(ContentProviderSearchResultsJXTable.class);
  
  AbstractContentProvider ccp;
  
  private JPopupMenu popupMenu = new JPopupMenu();
  
  private ArrayList<AbstractSearchResultsAction> actionList = new ArrayList<>();
  
  public ContentProviderSearchResultsJXTable(ContentProviderSearchResultsTableModel paramContentProviderSearchResultsTableModel, AbstractContentProvider paramAbstractContentProvider) {
    super(paramContentProviderSearchResultsTableModel);
    this.ccp = paramAbstractContentProvider;
    setCellSelectionEnabled(false);
    setRowSelectionAllowed(true);
    setSelectionMode(2);
    addHighlighter(HighlighterFactory.createAlternateStriping());
    HighlightPredicate highlightPredicate = new HighlightPredicate() {
        public boolean isHighlighted(Component param1Component, ComponentAdapter param1ComponentAdapter) {
          ContentProviderSearchResultsTableModel contentProviderSearchResultsTableModel = (ContentProviderSearchResultsTableModel)ContentProviderSearchResultsJXTable.this.getModel();
          IContentProviderResultRecord iContentProviderResultRecord = contentProviderSearchResultsTableModel.getResultRecordAt(ContentProviderSearchResultsJXTable.this.convertRowIndexToModel(param1ComponentAdapter.row));
          boolean bool = false;
          try {
            bool = iContentProviderResultRecord.isExistsInDMS(ContentProviderGlobal.getOIObjectManager());
          } catch (ContentProviderException contentProviderException) {
            ContentProviderSearchResultsJXTable.log.error(contentProviderException);
          } 
          return bool;
        }
      };
    addHighlighter((Highlighter)new ColorHighlighter(highlightPredicate, Color.BLUE, Color.WHITE, new Color(0, 0, 139), Color.WHITE));
    setHorizontalScrollEnabled(true);
    if (ContentProviderSearchWindow.getCurrentSearchContext() == DesktopClient.SearchContext.EXTERNAL_CONTENT_ASSIGNMENT) {
      AssignExternalContentAction assignExternalContentAction = new AssignExternalContentAction();
      this.popupMenu.add((Action)assignExternalContentAction);
      this.actionList.add(assignExternalContentAction);
    } 
    if (paramAbstractContentProvider.getSearchResultsActions() != null)
      for (AbstractSearchResultsAction abstractSearchResultsAction : paramAbstractContentProvider.getSearchResultsActions()) {
        this.popupMenu.add((Action)abstractSearchResultsAction);
        this.actionList.add(abstractSearchResultsAction);
      }  
    PopupListener popupListener = new PopupListener();
    addMouseListener(popupListener);
  }
  
  public TableCellRenderer getCellRenderer(int paramInt1, int paramInt2) {
    Object object = getValueAt(paramInt1, paramInt2);
    return (TableCellRenderer)((object instanceof com.mentor.dms.contentprovider.plugin.AbstractPushButtonCell) ? new CustomCellRenderer() : super.getCellRenderer(paramInt1, paramInt2));
  }
  
  public TableCellEditor getCellEditor(int paramInt1, int paramInt2) {
    Object object = getValueAt(paramInt1, paramInt2);
    return (object instanceof com.mentor.dms.contentprovider.plugin.AbstractPushButtonCell) ? new ContentProviderSearchResultsPushButtonCellEditor() : super.getCellEditor(paramInt1, paramInt2);
  }
  
  public static class AssignExternalContentAction extends AbstractSearchResultsAction {
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
  
  class PopupListener extends MouseAdapter {
    public void mouseClicked(MouseEvent param1MouseEvent) {
      if (param1MouseEvent.getClickCount() == 2) {
        AbstractSearchResultsAction abstractSearchResultsAction = ContentProviderSearchResultsJXTable.this.actionList.get(0);
        if (abstractSearchResultsAction != null)
          abstractSearchResultsAction.doAction(); 
      } 
    }
    
    public void mousePressed(MouseEvent param1MouseEvent) {
      showPopup(param1MouseEvent);
    }
    
    public void mouseReleased(MouseEvent param1MouseEvent) {
      showPopup(param1MouseEvent);
    }
    
    private void showPopup(MouseEvent param1MouseEvent) {
      if (param1MouseEvent.isPopupTrigger()) {
        for (AbstractSearchResultsAction abstractSearchResultsAction : ContentProviderSearchResultsJXTable.this.actionList) {
          abstractSearchResultsAction.selectionHandler();
          abstractSearchResultsAction.setUserEnabled();
        } 
        ContentProviderSearchResultsJXTable.this.popupMenu.show(param1MouseEvent.getComponent(), param1MouseEvent.getX(), param1MouseEvent.getY());
      } 
    }
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\plugin\searchui\ContentProviderSearchResultsJXTable.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */