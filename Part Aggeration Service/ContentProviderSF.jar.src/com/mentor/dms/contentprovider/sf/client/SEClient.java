package com.mentor.dms.contentprovider.sf.client;

import com.mentor.datafusion.oi.type.OIObject;
import com.mentor.datafusion.utils.logger.MGLogger;
import com.mentor.dms.contentprovider.core.AbstractContentProvider;
import com.mentor.dms.contentprovider.core.ContentProviderFactory;
import com.mentor.dms.contentprovider.core.sync.ContentProviderSync;
import java.awt.Desktop;
import java.net.URI;
import java.net.URLEncoder;
import java.util.HashMap;

public class SEClient {
  static MGLogger logger = MGLogger.getLogger(SEClient.class);
  
  public static void viewSEPart(OIObject paramOIObject) {
    if (!paramOIObject.getOIClass().getRootClass().getName().equals("ManufacturerPart")) {
      logger.error("View in SiliconExport Online must be called from a Manufacturer Part.");
      return;
    } 
    try {
      HashMap hashMap = ContentProviderSync.getProviderIdMapForMPN("SE", paramOIObject);
      String str = (String)hashMap.get("DataProviderID");
      if (str == null) {
        logger.error("Manufacturer Part '" + paramOIObject.getObjectID() + "' has not been assigned to an SiliconExpert Part.");
        return;
      } 
      viewSEPart(str);
    } catch (Exception exception) {
      logger.error(exception.getMessage());
    } 
  }
  
  public static void viewSEPart(String paramString) {
    try {
      AbstractContentProvider abstractContentProvider = ContentProviderFactory.getInstance().createContentProvider("SE");
      openSEPartInBrowser(abstractContentProvider, paramString);
    } catch (Exception exception) {
      logger.error(exception.getMessage());
    } 
  }
  
  public static void openSEPartInBrowser(AbstractContentProvider paramAbstractContentProvider, String paramString) {
    Desktop desktop = Desktop.getDesktop();
    if (!desktop.isSupported(Desktop.Action.BROWSE)) {
      logger.error("Platform's Java AWT Desktop doesn't support the browse action.");
      return;
    } 
    String str = null;
    try {
      str = "https://my.siliconexpert.com/partdetail/" + URLEncoder.encode(paramString, "UTF-8") + ".html";
      URI uRI = new URI(str);
      desktop.browse(uRI);
    } catch (Exception exception) {
      logger.error("Unable to open SiliconExpert URL: " + str);
    } 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\client\SEClient.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */