package com.mentor.dms.contentprovider.core.plugin;

import com.mentor.dms.contentprovider.core.AbstractContentProvider;
import com.mentor.dms.contentprovider.core.actions.AbstractDetailViewAction;
import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jdesktop.swingx.decorator.Highlighter;
import org.jdesktop.swingx.decorator.HighlighterFactory;

public class ContentProviderViewCompareJXTable extends JXTable {
  private JPopupMenu popupMenu = new JPopupMenu();
  
  private ArrayList<AbstractDetailViewAction> actionList = new ArrayList<>();
  
  public ContentProviderViewCompareJXTable(DefaultTableModel paramDefaultTableModel, AbstractContentProvider paramAbstractContentProvider) {
    super(paramDefaultTableModel);
    setRowSelectionAllowed(false);
    setColumnSelectionAllowed(false);
    setCellSelectionEnabled(true);
    setSelectionMode(2);
    setTableHeader(null);
    addHighlighter(HighlighterFactory.createAlternateStriping());
    HighlightPredicate highlightPredicate1 = new HighlightPredicate() {
        public boolean isHighlighted(Component param1Component, ComponentAdapter param1ComponentAdapter) {
          Object object = param1ComponentAdapter.getValueAt(param1ComponentAdapter.row, param1ComponentAdapter.column);
          if (object instanceof String) {
            String str = (String)object;
            if (!str.equals("** N/A **"))
              return ((Boolean)param1ComponentAdapter.getValueAt(param1ComponentAdapter.row, param1ComponentAdapter.getColumnCount() - 1)).booleanValue(); 
          } 
          return false;
        }
      };
    addHighlighter((Highlighter)new ColorHighlighter(highlightPredicate1, Color.yellow, Color.BLUE));
    HighlightPredicate highlightPredicate2 = new HighlightPredicate() {
        public boolean isHighlighted(Component param1Component, ComponentAdapter param1ComponentAdapter) {
          Object object = param1ComponentAdapter.getValueAt(param1ComponentAdapter.row, param1ComponentAdapter.column);
          if (object instanceof String) {
            String str = (String)object;
            if (str.equals("** N/A **"))
              return ((Boolean)param1ComponentAdapter.getValueAt(param1ComponentAdapter.row, param1ComponentAdapter.getColumnCount() - 1)).booleanValue(); 
          } 
          return false;
        }
      };
    addHighlighter((Highlighter)new ColorHighlighter(highlightPredicate2, Color.yellow, Color.GRAY));
    addMouseMotionListener(new MouseAdapter() {
          public void mouseMoved(MouseEvent param1MouseEvent) {
            Point point = param1MouseEvent.getPoint();
            if (point != null) {
              int i = ContentProviderViewCompareJXTable.this.rowAtPoint(point);
              int j = ContentProviderViewCompareJXTable.this.columnAtPoint(point);
              try {
                if (ContentProviderViewCompareJXTable.this.getCellEditor(i, j) instanceof PushButtonCellEditor)
                  ContentProviderViewCompareJXTable.this.editCellAt(i, j); 
              } catch (Exception exception) {}
            } 
          }
        });
  }
  
  public Component prepareRenderer(TableCellRenderer paramTableCellRenderer, int paramInt1, int paramInt2) {
    Component component = super.prepareRenderer(paramTableCellRenderer, paramInt1, paramInt2);
    if (paramInt1 > 0 && paramInt2 > -1 && getValueAt(0, paramInt2) instanceof ProductHeaderCell) {
      JComponent jComponent = (JComponent)component;
      ProductHeaderCell productHeaderCell = (ProductHeaderCell)getValueAt(0, paramInt2);
      jComponent.setToolTipText("<html><div align=\"center\"><b>" + productHeaderCell.getResultRecord().getPartNumber() + "<br>" + productHeaderCell.getResultRecord().getManufacturerName() + "<br><i>" + productHeaderCell.getResultRecord().getPartClassName() + "</i><br></b></div><html>");
    } 
    return component;
  }
  
  public TableCellRenderer getCellRenderer(int paramInt1, int paramInt2) {
    Object object = getValueAt(paramInt1, paramInt2);
    if (object instanceof String) {
      int i = getColumnCount();
      int j = (ContentProviderViewCompareWindow.WINDOWS_WIDTH - ContentProviderViewCompareWindow.PROPARTY_NAME_WIDTH - 35) / i;
      int k = j / 7;
      return new JTextAreaCellRenderer(k, 10);
    } 
    return (object instanceof ICustomCell) ? new CustomCellRenderer() : super.getCellRenderer(paramInt1, paramInt2);
  }
  
  public TableCellEditor getCellEditor(int paramInt1, int paramInt2) {
    Object object = getValueAt(paramInt1, paramInt2);
    return (object instanceof String) ? new JTextAreaCellEditor() : ((object instanceof AbstractPushButtonCell) ? new PushButtonCellEditor() : super.getCellEditor(paramInt1, paramInt2));
  }
  
  class PopupListener extends MouseAdapter {
    public void mouseClicked(MouseEvent param1MouseEvent) {
      if (param1MouseEvent.getClickCount() == 2) {
        AbstractDetailViewAction abstractDetailViewAction = ContentProviderViewCompareJXTable.this.actionList.get(0);
        if (abstractDetailViewAction != null)
          abstractDetailViewAction.doAction(); 
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
        for (AbstractDetailViewAction abstractDetailViewAction : ContentProviderViewCompareJXTable.this.actionList) {
          abstractDetailViewAction.selectionHandler();
          abstractDetailViewAction.setUserEnabled();
        } 
        ContentProviderViewCompareJXTable.this.popupMenu.show(param1MouseEvent.getComponent(), param1MouseEvent.getX(), param1MouseEvent.getY());
      } 
    }
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\plugin\ContentProviderViewCompareJXTable.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */