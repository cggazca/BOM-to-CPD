package com.mentor.dms.contentprovider.core.plugin;

import com.mentor.dms.contentprovider.core.ContentProviderDocument;
import com.mentor.dms.contentprovider.core.ContentProviderDocumentList;
import com.mentor.dms.contentprovider.core.ContentProviderException;
import com.mentor.dms.contentprovider.core.ContentProviderGlobal;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.net.URI;
import java.net.URL;
import javax.swing.JOptionPane;

public class ContentProviderDocumentViewer {
  public static void viewDocument(Component paramComponent, ContentProviderDocumentList paramContentProviderDocumentList) throws ContentProviderException {
    if (paramContentProviderDocumentList.size() > 1) {
      new ContentProviderViewDocumentsDlg(null, paramContentProviderDocumentList);
    } else if (paramContentProviderDocumentList.size() == 1) {
      viewDocument(paramComponent, (ContentProviderDocument)paramContentProviderDocumentList.get(0));
    } else {
      JOptionPane.showMessageDialog(ContentProviderGlobal.getRootFrame(), "No attachment available.", "View Attachment URL", 1);
    } 
  }
  
  public static void viewDocument(Component paramComponent, ContentProviderDocument paramContentProviderDocument) throws ContentProviderException {
    viewDocument(paramComponent, paramContentProviderDocument.getURL());
  }
  
  public static void viewDocument(Component paramComponent, String paramString) throws ContentProviderException {
    if (!Desktop.isDesktopSupported())
      throw new ContentProviderException("Platform's Java AWT Desktop is not supported."); 
    Desktop desktop = Desktop.getDesktop();
    if (!desktop.isSupported(Desktop.Action.BROWSE))
      throw new ContentProviderException("Platform's Java AWT Desktop doesn't support the browse action."); 
    if (paramComponent != null)
      paramComponent.setCursor(new Cursor(3)); 
    try {
      URL uRL = new URL(paramString);
      URI uRI = uRL.toURI();
      desktop.browse(uRI);
    } catch (Exception exception) {
      throw new ContentProviderException(exception.getMessage());
    } finally {
      if (paramComponent != null)
        paramComponent.setCursor(null); 
    } 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\plugin\ContentProviderDocumentViewer.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */