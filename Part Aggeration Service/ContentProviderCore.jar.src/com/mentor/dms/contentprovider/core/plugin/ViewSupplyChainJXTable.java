package com.mentor.dms.contentprovider.core.plugin;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.CompoundHighlighter;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jdesktop.swingx.decorator.Highlighter;
import org.jdesktop.swingx.decorator.HighlighterFactory;

public class ViewSupplyChainJXTable extends JXTable {
  public ViewSupplyChainJXTable(DefaultTableModel paramDefaultTableModel) {
    super(paramDefaultTableModel);
    setRowSelectionAllowed(false);
    setColumnSelectionAllowed(false);
    setCellSelectionEnabled(true);
    setSelectionMode(2);
    setSortable(false);
    HighlighterFactory.UIColorHighlighter uIColorHighlighter = new HighlighterFactory.UIColorHighlighter(HighlightPredicate.EVEN);
    ColorHighlighter colorHighlighter = new ColorHighlighter(HighlightPredicate.ODD, Color.WHITE, (Color)null);
    addHighlighter((Highlighter)new CompoundHighlighter(new Highlighter[] { (Highlighter)uIColorHighlighter, (Highlighter)colorHighlighter }));
    addMouseMotionListener(new MouseAdapter() {
          public void mouseMoved(MouseEvent param1MouseEvent) {
            Point point = param1MouseEvent.getPoint();
            if (point != null) {
              int i = ViewSupplyChainJXTable.this.rowAtPoint(point);
              int j = ViewSupplyChainJXTable.this.columnAtPoint(point);
              try {
                if (ViewSupplyChainJXTable.this.getCellEditor(i, j) instanceof PushButtonCellEditor)
                  ViewSupplyChainJXTable.this.editCellAt(i, j); 
              } catch (Exception exception) {}
            } 
          }
        });
  }
  
  public TableCellRenderer getCellRenderer(int paramInt1, int paramInt2) {
    Object object = getValueAt(paramInt1, paramInt2);
    if (object instanceof String) {
      boolean bool = false;
      return new JTextAreaCellRenderer(33, 10, true, bool);
    } 
    return (object instanceof ICustomCell) ? new CustomCellRenderer() : ((object instanceof javax.swing.JComponent) ? new ViewSupplyChainTableCellRenderer() : super.getCellRenderer(paramInt1, paramInt2));
  }
  
  public TableCellEditor getCellEditor(int paramInt1, int paramInt2) {
    Object object = getValueAt(paramInt1, paramInt2);
    return (object instanceof String) ? new JTextAreaCellEditor() : ((object instanceof AbstractPushButtonCell) ? new PushButtonCellEditor() : ((object instanceof javax.swing.JComponent) ? new ViewSupplyChainTableCellEditor() : super.getCellEditor(paramInt1, paramInt2)));
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\plugin\ViewSupplyChainJXTable.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */