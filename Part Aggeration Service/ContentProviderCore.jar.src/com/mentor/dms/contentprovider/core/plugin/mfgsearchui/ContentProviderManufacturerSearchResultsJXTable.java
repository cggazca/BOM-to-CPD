package com.mentor.dms.contentprovider.core.plugin.mfgsearchui;

import com.mentor.datafusion.utils.logger.MGLogger;
import java.awt.Color;
import java.awt.Component;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jdesktop.swingx.decorator.Highlighter;
import org.jdesktop.swingx.decorator.HighlighterFactory;

public class ContentProviderManufacturerSearchResultsJXTable extends JXTable {
  private static MGLogger log = MGLogger.getLogger(ContentProviderManufacturerSearchResultsJXTable.class);
  
  private ContentProviderManufacturerSearchMainPanel mainPanel;
  
  public ContentProviderManufacturerSearchResultsJXTable(final ContentProviderManufacturerSearchMainPanel mainPanel, ContentProviderManufacturerSearchResultsTableModel paramContentProviderManufacturerSearchResultsTableModel) {
    super(paramContentProviderManufacturerSearchResultsTableModel);
    this.mainPanel = mainPanel;
    setCellSelectionEnabled(false);
    setRowSelectionAllowed(true);
    setSelectionMode(0);
    addHighlighter(HighlighterFactory.createAlternateStriping());
    HighlightPredicate highlightPredicate = new HighlightPredicate() {
        public boolean isHighlighted(Component param1Component, ComponentAdapter param1ComponentAdapter) {
          ContentProviderManufacturerSearchResultsTableModel contentProviderManufacturerSearchResultsTableModel = (ContentProviderManufacturerSearchResultsTableModel)ContentProviderManufacturerSearchResultsJXTable.this.getModel();
          String str = contentProviderManufacturerSearchResultsTableModel.getManufacturerAt(ContentProviderManufacturerSearchResultsJXTable.this.convertRowIndexToModel(param1ComponentAdapter.row));
          return mainPanel.isExistingMfg(str);
        }
      };
    addHighlighter((Highlighter)new ColorHighlighter(highlightPredicate, Color.BLUE, Color.WHITE, new Color(0, 0, 139), Color.WHITE));
    setHorizontalScrollEnabled(true);
    getSelectionModel().addListSelectionListener(new ListSelectionListener() {
          public void valueChanged(ListSelectionEvent param1ListSelectionEvent) {
            mainPanel.setSelected();
          }
        });
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\plugin\mfgsearchui\ContentProviderManufacturerSearchResultsJXTable.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */