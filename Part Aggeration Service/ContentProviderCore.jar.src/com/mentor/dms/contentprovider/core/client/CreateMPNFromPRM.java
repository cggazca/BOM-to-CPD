package com.mentor.dms.contentprovider.core.client;

import com.mentor.datafusion.oi.type.OIObject;
import com.mentor.datafusion.oi.type.OIObjectSet;
import com.mentor.datafusion.utils.logger.MGLogger;
import com.mentor.dms.contentprovider.core.AbstractContentProvider;
import com.mentor.dms.contentprovider.core.ContentProviderException;
import com.mentor.dms.contentprovider.core.ContentProviderFactory;
import com.mentor.dms.contentprovider.core.ContentProviderGlobal;
import com.mentor.dms.contentprovider.core.IContentProviderResultRecord;
import com.mentor.dms.contentprovider.core.actions.UserUtils;
import com.mentor.dms.contentprovider.core.config.ContentProviderConfig;
import com.mentor.dms.contentprovider.core.config.ContentProviderConfigProperty;
import com.mentor.dms.contentprovider.core.sync.ContentProviderSync;
import java.awt.Cursor;
import java.util.HashMap;

public class CreateMPNFromPRM {
  static MGLogger logger = MGLogger.getLogger(CreateMPNFromPRM.class);
  
  public static void createMPNFromPRM(OIObject paramOIObject) {
    try {
      OIObjectSet oIObjectSet = paramOIObject.getSet("SciReqParts_PartsList");
      if (oIObjectSet == null || oIObjectSet.size() == 0)
        throw new ContentProviderException("Requested Parts not registered."); 
      logger.info("Create Manufacturer Part from Request. [" + paramOIObject.getObjectID() + "]");
      for (OIObject oIObject : oIObjectSet) {
        String str1 = oIObject.getStringified("SciReqParts_CpProviderId");
        String str2 = oIObject.getStringified("SciReqParts_CpPartUid");
        try {
          ContentProviderGlobal.getRootFrame().setCursor(new Cursor(3));
          AbstractContentProvider abstractContentProvider = ContentProviderFactory.getInstance().createContentProvider(str1);
          if (abstractContentProvider == null)
            throw new ContentProviderException("Providers \"" + str1 + "\" is not registered on EDM."); 
          ContentProviderConfig contentProviderConfig = abstractContentProvider.getConfig();
          if (!isUserEnabled(abstractContentProvider, "CREATE_MFG_PART_USERS"))
            throw new ContentProviderException("You need to belong to 'CREATE_MFG_PART_USERS' to perform this function."); 
          String str = null;
          for (ContentProviderConfigProperty contentProviderConfigProperty : contentProviderConfig.getIdProperties()) {
            if (contentProviderConfigProperty.getContentProviderLabel().equals("uid")) {
              str = contentProviderConfigProperty.getContentProviderId();
              break;
            } 
          } 
          HashMap<Object, Object> hashMap = new HashMap<>();
          hashMap.put(str, str2);
          IContentProviderResultRecord iContentProviderResultRecord = abstractContentProvider.getPart(hashMap);
          OIObject oIObject1 = ContentProviderSync.createMPNFromEC(ContentProviderGlobal.getDMSInstance().getObjectManager(), iContentProviderResultRecord, ContentProviderSync.OverwriteEnum.ASK);
          if (oIObject1 != null)
            ContentProviderGlobal.getDMSInstance().getObjectPanelManager().showObject(oIObject1); 
        } catch (ContentProviderException contentProviderException) {
          if (contentProviderException.isSSLCertException())
            throw contentProviderException; 
          logger.error(contentProviderException.getMessage(), (Throwable)contentProviderException);
        } 
      } 
    } catch (ContentProviderException contentProviderException) {
      if (contentProviderException.isSSLCertException()) {
        logger.error("The SSL certificate is invalid.\nSee log for details.");
        logger.error(contentProviderException.getMessage(), (Throwable)contentProviderException);
      } else {
        logger.error(contentProviderException.getMessage(), (Throwable)contentProviderException);
      } 
    } catch (Exception exception) {
      logger.error(exception.getMessage(), exception);
    } finally {
      ContentProviderGlobal.getRootFrame().setCursor(null);
    } 
  }
  
  public static boolean isUserEnabled(AbstractContentProvider paramAbstractContentProvider, String paramString) throws ContentProviderException {
    if (paramString == null)
      return true; 
    String str = paramAbstractContentProvider.getConfigurationParameter(paramString);
    if (str == null)
      return true; 
    str = str.trim();
    if (str.isEmpty())
      throw new ContentProviderException("'" + paramString + "' is not configured"); 
    boolean bool = false;
    String[] arrayOfString = str.split("\\s*,\\s*");
    for (String str1 : arrayOfString) {
      try {
        bool = UserUtils.isCurrentUserInGroup(ContentProviderGlobal.getOIObjectManager(), str1);
        if (bool)
          break; 
      } catch (Exception exception) {
        throw new ContentProviderException(exception);
      } 
    } 
    if (!bool)
      throw new ContentProviderException("You need to belong to " + String.join("/", arrayOfString) + " to perform this function."); 
    return bool;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\client\CreateMPNFromPRM.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */