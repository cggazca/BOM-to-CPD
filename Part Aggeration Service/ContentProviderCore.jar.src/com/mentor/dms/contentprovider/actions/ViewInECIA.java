package com.mentor.dms.contentprovider.actions;

import com.mentor.datafusion.utils.logger.MGLogger;
import com.mentor.dms.contentprovider.IContentProviderResultRecord;
import com.mentor.dms.contentprovider.plugin.searchui.ContentProviderSearchWindow;
import java.awt.Desktop;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Collection;
import javax.swing.ImageIcon;

public class ViewInECIA extends AbstractSearchResultsAction {
  private static MGLogger log = MGLogger.getLogger(ViewInECIA.class);
  
  public ViewInECIA() {
    super("Search in TrustedParts.com", getImageIcon());
    setEnabled(true);
  }
  
  private static ImageIcon getImageIcon() {
    Image image = Toolkit.getDefaultToolkit().getImage(ViewInECIA.class.getResource("images/trustedparts.png"));
    return new ImageIcon(image);
  }
  
  public void selectionHandler() {
    setEnabled((ContentProviderSearchWindow.getSelectionCount() == 1));
  }
  
  public void doAction() {
    String str2;
    Collection<IContentProviderResultRecord> collection = ContentProviderSearchWindow.getSelectedResults();
    IContentProviderResultRecord iContentProviderResultRecord = collection.iterator().next();
    String str1 = iContentProviderResultRecord.getPartNumber();
    if (!Desktop.isDesktopSupported()) {
      log.error("Platform's Java AWT Desktop is not supported.");
      return;
    } 
    Desktop desktop = Desktop.getDesktop();
    if (!desktop.isSupported(Desktop.Action.BROWSE)) {
      log.error("Platform's Java AWT Desktop doesn't support the browse action.");
      return;
    } 
    try {
      str2 = "https://www.trustedparts.com/en/search/" + URLEncoder.encode(str1, "UTF-8");
    } catch (UnsupportedEncodingException unsupportedEncodingException) {
      log.error("Unable to encode part number in URL:  " + unsupportedEncodingException.getMessage());
      return;
    } 
    try {
      URI uRI = new URI(str2);
      desktop.browse(uRI);
    } catch (Exception exception) {
      log.error("Unable to search using ERC URL: " + str2);
    } 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\actions\ViewInECIA.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */