package com.mentor.dms.contentprovider.core.actions;

import com.mentor.datafusion.utils.logger.MGLogger;
import com.mentor.dms.contentprovider.core.IContentProviderResultRecord;
import com.mentor.dms.contentprovider.core.plugin.searchui.ContentProviderSearchWindow;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.Collection;
import javax.swing.ImageIcon;

public class CopyFindchipsURLAction extends AbstractSearchResultsAction {
  private static MGLogger log = MGLogger.getLogger(CopyFindchipsURLAction.class);
  
  public CopyFindchipsURLAction() {
    super("Copy Findchips URL", (ImageIcon)null);
    setEnabled(true);
  }
  
  private static ImageIcon getImageIcon() {
    Image image = Toolkit.getDefaultToolkit().getImage(CopyFindchipsURLAction.class.getResource("images/add_mpn.png"));
    return new ImageIcon(image);
  }
  
  public void selectionHandler() {
    setEnabled((ContentProviderSearchWindow.getSelectionCount() == 1));
  }
  
  public void doAction() {
    try {
      String str = "";
      Collection<IContentProviderResultRecord> collection = ContentProviderSearchWindow.getSelectedResults();
      IContentProviderResultRecord iContentProviderResultRecord = collection.iterator().next();
      str = iContentProviderResultRecord.getFindchipsURL();
      Toolkit toolkit = Toolkit.getDefaultToolkit();
      Clipboard clipboard = toolkit.getSystemClipboard();
      StringSelection stringSelection = new StringSelection(str);
      clipboard.setContents(stringSelection, stringSelection);
    } finally {
      ContentProviderSearchWindow.getInstance().setCursor(null);
    } 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\actions\CopyFindchipsURLAction.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */