package com.mentor.dms.contentprovider.core.client;

import com.mentor.datafusion.oi.OICursor;
import com.mentor.datafusion.oi.OIException;
import com.mentor.datafusion.oi.OIObjectManager;
import com.mentor.datafusion.oi.OIObjectManagerFactory;
import com.mentor.datafusion.oi.OIQuery;
import com.mentor.datafusion.oi.login.OIAuthenticate;
import com.mentor.datafusion.oi.login.OIAuthenticateFactory;
import com.mentor.datafusion.oi.model.OIClass;
import com.mentor.datafusion.oi.type.OIBlob;
import com.mentor.datafusion.oi.type.OIObject;
import com.mentor.datafusion.oi.type.OIObjectSet;
import com.mentor.datafusion.oi.util.FileUtilities;
import com.mentor.datafusion.oi.util.OIFile;
import com.mentor.datafusion.utils.logger.MGLogger;
import com.mentor.dms.contentprovider.core.AbstractContentProvider;
import com.mentor.dms.contentprovider.core.ContentProviderException;
import com.mentor.dms.contentprovider.core.ContentProviderFactory;
import com.mentor.dms.contentprovider.core.ContentProviderGlobal;
import com.mentor.dms.contentprovider.core.ContentProviderRegistryEntry;
import com.mentor.dms.contentprovider.core.IContentProviderResultRecord;
import com.mentor.dms.contentprovider.core.client.credentials.CredentialsDialog;
import com.mentor.dms.contentprovider.core.plugin.mfgsearchui.ContentProviderManufacturerSearchDlg;
import com.mentor.dms.contentprovider.core.plugin.partsearchui.ContentProviderSelectPartNumberDlg;
import com.mentor.dms.contentprovider.core.plugin.searchui.ContentProviderSearchWindow;
import com.mentor.dms.contentprovider.core.plugin.searchui.ContentProviderSelectDlg;
import com.mentor.dms.contentprovider.core.sync.ContentProviderSync;
import com.mentor.dms.contentprovider.core.sync.gui.ContentProviderReconcileApp;
import com.mentor.dms.ui.objectpanel.ObjectPanel;
import com.mentor.dms.ui.objectpanel.ObjectPanelManager;
import com.mentor.dms.ui.searchmask.NoSearchMaskFoundException;
import com.mentor.dms.ui.searchmask.SearchMask;
import com.mentor.dms.ui.searchmask.SearchMaskManager;
import java.awt.Cursor;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import org.apache.log4j.NDC;

public class DesktopClient {
  static MGLogger logger = MGLogger.getLogger(DesktopClient.class);
  
  public static void syncToComponents(OIObject paramOIObject) {
    try {
      logger.info("Update Component(s) start. [" + paramOIObject.getObjectID() + "]");
      ContentProviderSync.syncMPN2Comps(paramOIObject, "", false);
      logger.info("Update Component(s) end.");
    } catch (ContentProviderException contentProviderException) {
      if (contentProviderException.isSSLCertException()) {
        logger.error("The SSL certificate is invalid.\nSee log for details.");
        logger.error(contentProviderException.getMessage(), (Throwable)contentProviderException);
      } else {
        JOptionPane.showMessageDialog(ContentProviderGlobal.getRootFrame(), contentProviderException.getMessage(), "Synchronize MPN to Components", 0);
        logger.info(contentProviderException.getMessage(), (Throwable)contentProviderException);
      } 
    } 
  }
  
  public static void syncToMPN(OIObject paramOIObject) {
    OIClass oIClass = paramOIObject.getOIClass();
    try {
      logger.info("Update from Manufacturer Part start. [" + paramOIObject.getObjectID() + "]");
      ContentProviderSync.syncComp2MPN(paramOIObject);
      if (ContentProviderGlobal.getDMSInstance() != null) {
        ObjectPanelManager objectPanelManager = ContentProviderGlobal.getDMSInstance().getObjectPanelManager();
        if (oIClass.getPath().equals(paramOIObject.getOIClass().getPath())) {
          objectPanelManager.update(paramOIObject);
        } else {
          JOptionPane.showMessageDialog(ContentProviderGlobal.getRootFrame(), "Component '" + paramOIObject.getObjectID() + "' was moved from '" + oIClass.getLabel() + "' to '" + paramOIObject.getOIClass().getLabel() + "'.\n\nComponent will be closed and re-opened.", "Component - Update from Manufacturer Part", 0);
          ObjectPanel objectPanel = objectPanelManager.isObjectShown(paramOIObject);
          if (objectPanel != null)
            objectPanel.close(); 
          objectPanelManager.showObject(paramOIObject);
        } 
      } 
      logger.info("Update from Manufacturer Part end.");
    } catch (ContentProviderException contentProviderException) {
      if (contentProviderException.isSSLCertException()) {
        logger.error("The SSL certificate is invalid.\nSee log for details.");
        logger.error(contentProviderException.getMessage(), (Throwable)contentProviderException);
      } else {
        JOptionPane.showMessageDialog(ContentProviderGlobal.getRootFrame(), contentProviderException.getMessage(), "Component - Update from Manufacturer Part", 0);
        logger.info(contentProviderException.getMessage(), (Throwable)contentProviderException);
      } 
    } catch (Exception exception) {
      JOptionPane.showMessageDialog(ContentProviderGlobal.getRootFrame(), exception.getMessage(), "Component - Update from Manufacturer Part", 0);
      logger.info(exception.getMessage(), exception);
    } 
  }
  
  public static void reloadExternalContent(OIObject paramOIObject) {
    try {
      logger.info("Reload Content Provider. [" + paramOIObject.getObjectID() + "]");
      OIClass oIClass = paramOIObject.getOIClass();
      OIObject oIObject = paramOIObject.getObject("ExternalContentId");
      if (oIObject == null) {
        JOptionPane.showMessageDialog(ContentProviderGlobal.getRootFrame(), "Manufacturer Part is not associated to External Content.", "Remove External Content", 0);
        return;
      } 
      AbstractContentProvider abstractContentProvider = ContentProviderSelectDlg.selectContentProvider(paramOIObject.getObjectManager(), ContentProviderRegistryEntry.ContentProviderRole.MANUFACTURER_PART_SYNCHRONIZATION, paramOIObject.getStringified("ExternalContentId"));
      int i = JOptionPane.showConfirmDialog(ContentProviderGlobal.getRootFrame(), "Reload External Content from '" + abstractContentProvider.getName() + "' to Manufacturer Part '" + paramOIObject.getString("PartNumber") + "' from '" + paramOIObject.getStringified("ManufacturerName") + "'?", "Reload External Content", 0);
      if (i != 0)
        return; 
      if (ContentProviderGlobal.getRootFrame() != null)
        ContentProviderGlobal.getRootFrame().setCursor(new Cursor(3)); 
      abstractContentProvider.getConfig();
      ContentProviderSync.syncExernalContentPartRecordToDMS(paramOIObject, oIObject, abstractContentProvider, new Date(), true);
      HashSet<String> hashSet = new HashSet();
      hashSet.add(paramOIObject.getObjectID());
      ContentProviderReconcileApp.createAndShowGUI(paramOIObject.getObjectManager(), hashSet);
      if (ContentProviderGlobal.getDMSInstance() != null) {
        ObjectPanelManager objectPanelManager = ContentProviderGlobal.getDMSInstance().getObjectPanelManager();
        if (oIClass.getPath().equals(paramOIObject.getOIClass().getPath())) {
          objectPanelManager.update(paramOIObject);
        } else {
          JOptionPane.showMessageDialog(ContentProviderGlobal.getRootFrame(), "Manufacturer Part was moved from '" + oIClass.getLabel() + "' to '" + paramOIObject.getOIClass().getLabel() + "'.\n\nManufacturer Part will be closed and re-opened.", "Reload External Content", 0);
          ObjectPanel objectPanel = objectPanelManager.isObjectShown(paramOIObject);
          if (objectPanel != null)
            objectPanel.close(); 
          objectPanelManager.showObject(paramOIObject);
        } 
      } 
    } catch (ContentProviderException contentProviderException) {
      if (contentProviderException.isSSLCertException()) {
        logger.error("The SSL certificate is invalid.\nSee log for details.");
        logger.error(contentProviderException.getMessage(), (Throwable)contentProviderException);
      } else {
        JOptionPane.showMessageDialog(ContentProviderGlobal.getRootFrame(), contentProviderException.getMessage(), "Reload External Content", 0);
        logger.info(contentProviderException.getMessage(), (Throwable)contentProviderException);
      } 
    } catch (Exception exception) {
      JOptionPane.showMessageDialog(ContentProviderGlobal.getRootFrame(), exception.getMessage(), "Reload External Content", 0);
      logger.info(exception.getMessage(), exception);
    } finally {
      if (ContentProviderGlobal.getRootFrame() != null)
        ContentProviderGlobal.getRootFrame().setCursor(null); 
    } 
  }
  
  public static void removeExternalContent(OIObject paramOIObject) {
    try {
      logger.info("Remove External Content. [" + paramOIObject.getObjectID() + "]");
      OIObject oIObject = paramOIObject.getObject("ExternalContentId");
      if (oIObject == null) {
        JOptionPane.showMessageDialog(ContentProviderGlobal.getRootFrame(), "Manufacturer Part is not associated to External Content.", "Remove External Content", 0);
        return;
      } 
      AbstractContentProvider abstractContentProvider = ContentProviderSelectDlg.selectContentProvider(paramOIObject.getObjectManager(), ContentProviderRegistryEntry.ContentProviderRole.MANUFACTURER_PART_SYNCHRONIZATION, paramOIObject.getStringified("ExternalContentId"));
      int i = JOptionPane.showConfirmDialog(ContentProviderGlobal.getRootFrame(), "Remove External Content from '" + abstractContentProvider.getName() + "' Manufacturer Part '" + paramOIObject.getString("PartNumber") + "' from '" + paramOIObject.getStringified("ManufacturerName") + "'?", "Remove External Content", 0);
      if (i != 0)
        return; 
      if (ContentProviderGlobal.getRootFrame() != null)
        ContentProviderGlobal.getRootFrame().setCursor(new Cursor(3)); 
      ArrayList<OIObject> arrayList = new ArrayList();
      arrayList.add(paramOIObject);
      arrayList.add(oIObject);
      try {
        paramOIObject.set("ExternalContentId", null);
        paramOIObject.getObjectManager().deleteObject(oIObject);
        paramOIObject.getObjectManager().makePermanent(arrayList);
      } finally {
        paramOIObject.getObjectManager().evict(arrayList);
      } 
      if (ContentProviderGlobal.getDMSInstance() != null) {
        ObjectPanelManager objectPanelManager = ContentProviderGlobal.getDMSInstance().getObjectPanelManager();
        objectPanelManager.update(paramOIObject);
      } 
    } catch (Exception exception) {
      JOptionPane.showMessageDialog(ContentProviderGlobal.getRootFrame(), exception.getMessage(), "Remove External Content", 0);
    } finally {
      if (ContentProviderGlobal.getRootFrame() != null)
        ContentProviderGlobal.getRootFrame().setCursor(null); 
    } 
  }
  
  public static void createMPNFromRequestIHS(OIObject paramOIObject) throws Exception {
    String str1 = paramOIObject.getStringified("mcd_cp_pn");
    if (str1 == null || str1.isEmpty()) {
      logger.error(paramOIObject.getOIClass().getField("mcd_cp_pn").getLabel() + " must be supplied in the Request.");
      return;
    } 
    String str2 = paramOIObject.getStringified("mcd_cp_mfr");
    if (str2 == null || str2.isEmpty()) {
      logger.error(paramOIObject.getOIClass().getField("mcd_cp_mfr").getLabel() + " must be supplied in the Request.");
      return;
    } 
    String str3 = paramOIObject.getStringified("mcd_cp_obj_id");
    if (str3 == null || str3.isEmpty()) {
      logger.error(paramOIObject.getOIClass().getField("mcd_cp_obj_id").getLabel() + " must be supplied in the Request.");
      return;
    } 
    ContentProviderGlobal.getRootFrame().setCursor(new Cursor(3));
    try {
      HashMap<Object, Object> hashMap = new HashMap<>();
      hashMap.put("object-id", str3);
      OIObject oIObject = ContentProviderSync.createMPNFromEC(paramOIObject.getObjectManager(), "IHS", str1, str2, hashMap, ContentProviderSync.OverwriteEnum.ASK);
      if (oIObject != null) {
        try {
          SearchMaskManager searchMaskManager = ContentProviderGlobal.getDMSInstance().getSearchMaskManager();
          SearchMask searchMask = searchMaskManager.open(oIObject.getOIClass());
        } catch (NoSearchMaskFoundException noSearchMaskFoundException) {}
        ContentProviderGlobal.getDMSInstance().getObjectPanelManager().showObject(oIObject);
      } 
    } catch (ContentProviderException contentProviderException) {
      JOptionPane.showMessageDialog(ContentProviderGlobal.getRootFrame(), contentProviderException.getMessage());
    } finally {
      ContentProviderGlobal.getRootFrame().setCursor(null);
    } 
  }
  
  public static void doSearch(SearchContext paramSearchContext) {
    ContentProviderSearchWindow.doSearch(paramSearchContext);
  }
  
  public static void assignExternalContent(OIObject paramOIObject) {
    try {
      logger.info("Assign Content Provider start. [" + paramOIObject.getObjectID() + "]");
      ContentProviderSearchWindow.doExternalContentAssignment(paramOIObject);
      logger.info("Assign Content Provider end.");
    } catch (ContentProviderException contentProviderException) {
      if (contentProviderException.isSSLCertException()) {
        logger.error("The SSL certificate is invalid.\nSee log for details.");
        logger.error(contentProviderException.getMessage(), (Throwable)contentProviderException);
      } else {
        JOptionPane.showMessageDialog(ContentProviderGlobal.getRootFrame(), contentProviderException.getMessage());
        logger.info(contentProviderException.getMessage(), (Throwable)contentProviderException);
      } 
    } catch (Exception exception) {
      JOptionPane.showMessageDialog(ContentProviderGlobal.getRootFrame(), exception.getMessage());
      logger.info(exception.getMessage(), exception);
    } 
  }
  
  public static void doSearchByMPN(String paramString1, String paramString2) {
    try {
      OIClass oIClass = ContentProviderGlobal.getOIObjectManager().getObjectManagerFactory().getClassManager().getOIClass("RootManufacturerPart");
      ContentProviderSearchWindow.doSearchByMPN(oIClass, paramString1, paramString2);
    } catch (ContentProviderException contentProviderException) {
      JOptionPane.showMessageDialog(ContentProviderGlobal.getRootFrame(), contentProviderException.getMessage());
    } 
  }
  
  public static void createComponentFromMPN(OIObject paramOIObject) {
    try {
      logger.info("Create Component start. [" + paramOIObject.getObjectID() + "]");
      int i = JOptionPane.showConfirmDialog(ContentProviderGlobal.getRootFrame(), "Create Component from Manufacturer Part '" + paramOIObject.getString("PartNumber") + "' from '" + paramOIObject.getStringified("ManufacturerName") + "'?", "Create Component from Manufacturer Part", 0);
      if (i != 0)
        return; 
    } catch (OIException oIException) {
      JOptionPane.showMessageDialog(ContentProviderGlobal.getRootFrame(), "Problem reading Manufacturer Part : " + oIException.getMessage());
      return;
    } 
    OIObject oIObject = null;
    try {
      oIObject = ContentProviderSync.createComponentFromMPN(paramOIObject, ContentProviderSync.OverwriteEnum.ASK);
    } catch (ContentProviderException contentProviderException) {
      if (contentProviderException.isSSLCertException()) {
        logger.error("The SSL certificate is invalid.\nSee log for details.");
        logger.error(contentProviderException.getMessage(), (Throwable)contentProviderException);
      } else {
        JOptionPane.showMessageDialog(ContentProviderGlobal.getRootFrame(), "Problem creating Component : " + contentProviderException.getMessage());
        logger.info(contentProviderException.getMessage(), (Throwable)contentProviderException);
      } 
    } 
    if (oIObject == null)
      return; 
    try {
      ContentProviderSync.syncMPN2Comp(paramOIObject, oIObject);
    } catch (ContentProviderException contentProviderException) {
      JOptionPane.showMessageDialog(ContentProviderGlobal.getRootFrame(), contentProviderException.getMessage(), "Synchronize MPN to Components", 0);
    } 
    try {
      SearchMaskManager searchMaskManager = ContentProviderGlobal.getDMSInstance().getSearchMaskManager();
      SearchMask searchMask = searchMaskManager.open(oIObject.getOIClass());
    } catch (NoSearchMaskFoundException noSearchMaskFoundException) {}
    ContentProviderGlobal.getDMSInstance().getObjectPanelManager().showObject(oIObject);
    logger.info("Create Component end.");
  }
  
  public static void removeBlob(OIObject paramOIObject, String paramString) {
    try {
      String str = paramOIObject.getOIClass().getField(paramString).getLabel();
      int i = JOptionPane.showConfirmDialog(ContentProviderGlobal.getRootFrame(), "Remove " + str + " from this object?", "Remove File", 0);
      if (i != 0)
        return; 
      paramOIObject.getObjectManager().refreshAndLockObject(paramOIObject);
      OIBlob oIBlob = paramOIObject.getBlob(paramString);
      oIBlob.setInputStream(new ByteArrayInputStream("".getBytes()));
      OIFile oIFile = FileUtilities.wrapBlob(oIBlob);
      oIFile.delete(true);
      ContentProviderGlobal.getDMSInstance().getObjectPanelManager().update(paramOIObject);
      ContentProviderGlobal.getDMSInstance().getObjectPanelManager().showObject(paramOIObject);
    } catch (OIException oIException) {
      logger.error(oIException);
    } 
  }
  
  public static IContentProviderResultRecord selectManufacturerPart(OIObject paramOIObject) {
    return selectManufacturerPart(paramOIObject, "", "");
  }
  
  public static IContentProviderResultRecord selectManufacturerPart(OIObject paramOIObject, String paramString1, String paramString2) {
    try {
      AbstractContentProvider abstractContentProvider = ContentProviderSelectDlg.selectContentProvider(ContentProviderRegistryEntry.ContentProviderRole.COMPONENT_ENGINEER_SEARCH);
      ArrayList<String> arrayList = new ArrayList();
      OIQuery oIQuery = paramOIObject.getObjectManager().createQuery("Manufacturer", true);
      oIQuery.addColumn("ManufacturerName");
      OICursor oICursor = oIQuery.execute();
      while (oICursor.next())
        arrayList.add(oICursor.getString("ManufacturerName")); 
      oICursor.close();
      ContentProviderSelectPartNumberDlg contentProviderSelectPartNumberDlg = new ContentProviderSelectPartNumberDlg(ContentProviderGlobal.getRootFrame(), abstractContentProvider, paramString1, paramString2, arrayList);
      return contentProviderSelectPartNumberDlg.getSelectedPart();
    } catch (Exception exception) {
      JOptionPane.showMessageDialog(ContentProviderGlobal.getRootFrame(), exception.getMessage());
      return null;
    } 
  }
  
  public static void searchManufacturers() {
    try {
      AbstractContentProvider abstractContentProvider = ContentProviderSelectDlg.selectContentProvider(ContentProviderRegistryEntry.ContentProviderRole.COMPONENT_ENGINEER_SEARCH);
      ContentProviderManufacturerSearchDlg.show(ContentProviderGlobal.getRootFrame(), abstractContentProvider);
    } catch (Exception exception) {
      JOptionPane.showMessageDialog(ContentProviderGlobal.getRootFrame(), exception.getMessage());
    } 
  }
  
  public static AbstractContentProvider selectContentProvider(ContentProviderRegistryEntry.ContentProviderRole paramContentProviderRole) throws ContentProviderException {
    return ContentProviderSelectDlg.selectContentProvider(paramContentProviderRole);
  }
  
  public static void editCredentials(OIObject paramOIObject) {
    int i = paramOIObject.getMode();
    if (!paramOIObject.getOIClass().hasField("ECCredList")) {
      JOptionPane.showMessageDialog(ContentProviderGlobal.getRootFrame(), "Content Provider Credentials data model does not exit in toolbox.");
      return;
    } 
    OIObjectManager oIObjectManager = paramOIObject.getObjectManager();
    try {
      OIObjectSet oIObjectSet = paramOIObject.getSet("MetaDataMap");
      String str = null;
      for (OIObject oIObject : oIObjectSet) {
        if (oIObject.getString("Key").equals("PROVIDER_ID")) {
          str = oIObject.getString("Value");
          break;
        } 
      } 
      if (str == null) {
        JOptionPane.showMessageDialog(ContentProviderGlobal.getRootFrame(), "Content provider toolbox does not have a 'PROVIDER_ID'");
        return;
      } 
      logger.info("Edit Credentials for PROVIDER_ID [" + str + "]");
      AbstractContentProvider abstractContentProvider = ContentProviderFactory.getInstance().createContentProvider(str);
      CredentialsDialog credentialsDialog = new CredentialsDialog(ContentProviderGlobal.getRootFrame(), abstractContentProvider);
      credentialsDialog.display();
      if (!credentialsDialog.isCancelled()) {
        oIObjectManager.refreshAndLockObject(paramOIObject);
        OIObjectSet oIObjectSet1 = paramOIObject.getSet("ECCredList");
        oIObjectSet1.clear();
        for (Map.Entry entry : credentialsDialog.getCredentials().entrySet()) {
          OIObject oIObject = oIObjectSet1.createLine();
          oIObject.set("ECCredId", entry.getKey());
          oIObject.set("ECCredValue", entry.getValue());
        } 
        oIObjectManager.makePermanent(paramOIObject);
        abstractContentProvider.setCredentials(credentialsDialog.getCredentials());
        logger.info("Credentials updated.");
      } 
    } catch (Exception exception) {
      JOptionPane.showMessageDialog(ContentProviderGlobal.getRootFrame(), exception.getMessage());
    } finally {
      if (i == 2)
        try {
          oIObjectManager.evict(paramOIObject);
        } catch (OIException oIException) {} 
      if (ContentProviderGlobal.getDMSInstance() != null) {
        ObjectPanelManager objectPanelManager = ContentProviderGlobal.getDMSInstance().getObjectPanelManager();
        objectPanelManager.update(paramOIObject);
      } 
    } 
  }
  
  public static void main(String[] paramArrayOfString) {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception exception) {}
    OIObjectManager oIObjectManager = null;
    OIObjectManagerFactory oIObjectManagerFactory = null;
    try {
      System.out.println("Connecting to EDM Library Server...");
      OIAuthenticate oIAuthenticate = OIAuthenticateFactory.createBatchAuthenticate("testedm");
      oIObjectManagerFactory = oIAuthenticate.login("Test App");
      oIObjectManager = oIObjectManagerFactory.createObjectManager();
      System.out.println("Connected!");
      ContentProviderFactory.getInstance().registerContentProviders(oIObjectManager);
      NDC.push(oIObjectManagerFactory.getUserName());
      OIObject oIObject = oIObjectManager.getObjectByID("SiliconExpertContentProvider:", "Toolbox", true);
      try {
        oIObjectManager.refreshAndLockObject(oIObject);
        editCredentials(oIObject);
      } finally {
        oIObjectManager.evict(oIObject);
      } 
      AbstractContentProvider abstractContentProvider = ContentProviderFactory.getInstance().createContentProvider("SE");
      abstractContentProvider.searchExactMatch("0805", "AVX");
      return;
    } catch (Exception exception) {
      exception.printStackTrace();
    } finally {
      if (oIObjectManager != null)
        oIObjectManager.close(); 
      if (oIObjectManagerFactory != null)
        oIObjectManagerFactory.close(); 
    } 
  }
  
  public enum SearchContext {
    DESIGNER, COMPONENT_ENGINEER, EXTERNAL_CONTENT_ASSIGNMENT;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\client\DesktopClient.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */