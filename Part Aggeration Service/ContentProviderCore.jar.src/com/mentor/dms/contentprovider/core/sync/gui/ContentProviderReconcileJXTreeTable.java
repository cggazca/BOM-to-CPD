package com.mentor.dms.contentprovider.core.sync.gui;

import java.awt.Color;
import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.table.TableCellEditor;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jdesktop.swingx.decorator.Highlighter;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jdesktop.swingx.decorator.PatternPredicate;
import org.jdesktop.swingx.treetable.TreeTableModel;

public class ContentProviderReconcileJXTreeTable extends JXTreeTable {
  private static final long serialVersionUID = 1L;
  
  private DefaultCellEditor syncActionCellEditor;
  
  public ContentProviderReconcileJXTreeTable(TreeTableModel paramTreeTableModel) {
    super(paramTreeTableModel);
    setTreeCellRenderer(new ReconcileTreeCellRenderer());
    setHighlighters(new Highlighter[] { HighlighterFactory.createSimpleStriping(), (Highlighter)new ColorHighlighter(new HighlightPredicate() {
              public boolean isHighlighted(Component param1Component, ComponentAdapter param1ComponentAdapter) {
                int i = param1ComponentAdapter.convertRowIndexToModel(param1ComponentAdapter.row);
                return ((Boolean)param1ComponentAdapter.getValueAt(i, 7)).booleanValue();
              }
            }Color.YELLOW, null), (Highlighter)new ColorHighlighter(new HighlightPredicate() {
              public boolean isHighlighted(Component param1Component, ComponentAdapter param1ComponentAdapter) {
                int i = param1ComponentAdapter.convertRowIndexToModel(param1ComponentAdapter.row);
                String str = (String)param1ComponentAdapter.getValueAt(i, 6);
                return (str != null);
              }
            }null, Color.RED), (Highlighter)new ColorHighlighter((HighlightPredicate)new PatternPredicate(".*Non-existent.*", 0), null, Color.RED) });
    setCellSelectionEnabled(false);
    JComboBox<String> jComboBox = new JComboBox();
    jComboBox.addItem("Reconcile");
    jComboBox.addItem("Ignore Once");
    jComboBox.addItem("Ignore Always");
    this.syncActionCellEditor = new DefaultCellEditor(jComboBox);
  }
  
  public TableCellEditor getCellEditor(int paramInt1, int paramInt2) {
    int i = convertColumnIndexToModel(paramInt2);
    return (i == 3) ? this.syncActionCellEditor : super.getCellEditor(paramInt1, paramInt2);
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\sync\gui\ContentProviderReconcileJXTreeTable.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */