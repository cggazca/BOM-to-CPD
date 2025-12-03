package com.mentor.dms.contentprovider.core.client;

import com.mentor.datafusion.oi.OIException;
import com.mentor.datafusion.oi.OIObjectManager;
import com.mentor.datafusion.oi.OIObjectManagerFactory;
import com.mentor.datafusion.oi.login.OIAuthenticate;
import com.mentor.datafusion.oi.login.OIAuthenticateFactory;
import com.mentor.datafusion.oi.model.OIField;
import com.mentor.datafusion.oi.type.OIObject;
import com.mentor.datafusion.oi.type.OIObjectSet;
import com.mentor.datafusion.utils.logger.MGLogger;
import com.mentor.dms.contentprovider.core.AbstractContentProvider;
import com.mentor.dms.contentprovider.core.ContentProviderDocument;
import com.mentor.dms.contentprovider.core.ContentProviderDocumentList;
import com.mentor.dms.contentprovider.core.ContentProviderException;
import com.mentor.dms.contentprovider.core.ContentProviderFactory;
import com.mentor.dms.contentprovider.core.ContentProviderGlobal;
import com.mentor.dms.contentprovider.core.ContentProviderPartNotFoundException;
import com.mentor.dms.contentprovider.core.ContentProviderRegistryEntry;
import com.mentor.dms.contentprovider.core.IContentProviderApplication;
import com.mentor.dms.contentprovider.core.IContentProviderResultRecord;
import com.mentor.dms.contentprovider.core.config.ContentProviderConfig;
import com.mentor.dms.contentprovider.core.plugin.ContentProviderDocumentViewer;
import com.mentor.dms.contentprovider.core.plugin.ContentProviderViewCompareWindow;
import com.mentor.dms.contentprovider.core.plugin.searchui.ContentProviderSelectDlg;
import com.mentor.dms.contentprovider.core.sync.ContentProviderSync;
import com.mentor.dms.contentprovider.core.sync.ContentProviderSyncException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.ProgressMonitor;
import javax.swing.SwingWorker;
import javax.swing.UIManager;

public class ViewEC implements PropertyChangeListener {
  private static MGLogger logger = MGLogger.getLogger(ViewEC.class);
  
  private ProgressMonitor progressMonitor;
  
  private Task task;
  
  private boolean bTaskInProgress = false;
  
  private HashMap<String, String> idPropMap = new HashMap<>();
  
  private List<IContentProviderResultRecord> partRecList;
  
  private String ccpId = null;
  
  private static OIObject testObj;
  
  public ViewEC() {}
  
  public ViewEC(String paramString) {
    this.ccpId = paramString;
  }
  
  public static void direct(OIObject paramOIObject, String paramString1, String paramString2, String paramString3) {
    ViewEC viewEC = new ViewEC(paramString1);
    viewEC.execImpl(paramOIObject, new String[] { paramString2, paramString3 });
  }
  
  public static void direct(OIObject paramOIObject, String paramString1, String paramString2, String paramString3, String paramString4, String paramString5) {
    ViewEC viewEC = new ViewEC(paramString1);
    viewEC.execImpl(paramOIObject, new String[] { paramString2, paramString3, paramString4, paramString5 });
  }
  
  public static void direct(OIObject paramOIObject, String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7) {
    ViewEC viewEC = new ViewEC(paramString1);
    viewEC.execImpl(paramOIObject, new String[] { paramString2, paramString3, paramString4, paramString5, paramString6, paramString7 });
  }
  
  public static void direct(OIObject paramOIObject, String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7, String paramString8, String paramString9) {
    ViewEC viewEC = new ViewEC(paramString1);
    viewEC.execImpl(paramOIObject, new String[] { paramString2, paramString3, paramString4, paramString5, paramString6, paramString7, paramString8, paramString9 });
  }
  
  public static void direct(OIObject paramOIObject, String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7, String paramString8, String paramString9, String paramString10, String paramString11) {
    ViewEC viewEC = new ViewEC(paramString1);
    viewEC.execImpl(paramOIObject, new String[] { paramString2, paramString3, paramString4, paramString5, paramString6, paramString7, paramString8, paramString9, paramString10, paramString11 });
  }
  
  public static void direct(OIObject paramOIObject, String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7, String paramString8, String paramString9, String paramString10, String paramString11, String paramString12, String paramString13) {
    ViewEC viewEC = new ViewEC(paramString1);
    viewEC.execImpl(paramOIObject, new String[] { 
          paramString2, paramString3, paramString4, paramString5, paramString6, paramString7, paramString8, paramString9, paramString10, paramString11, 
          paramString12, paramString13 });
  }
  
  public static void direct(OIObject paramOIObject, String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7, String paramString8, String paramString9, String paramString10, String paramString11, String paramString12, String paramString13, String paramString14, String paramString15) {
    ViewEC viewEC = new ViewEC(paramString1);
    viewEC.execImpl(paramOIObject, new String[] { 
          paramString2, paramString3, paramString4, paramString5, paramString6, paramString7, paramString8, paramString9, paramString10, paramString11, 
          paramString12, paramString13, paramString14, paramString15 });
  }
  
  public static void direct(OIObject paramOIObject, String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7, String paramString8, String paramString9, String paramString10, String paramString11, String paramString12, String paramString13, String paramString14, String paramString15, String paramString16, String paramString17) {
    ViewEC viewEC = new ViewEC(paramString1);
    viewEC.execImpl(paramOIObject, new String[] { 
          paramString2, paramString3, paramString4, paramString5, paramString6, paramString7, paramString8, paramString9, paramString10, paramString11, 
          paramString12, paramString13, paramString14, paramString15, paramString16, paramString17 });
  }
  
  public static void direct(OIObject paramOIObject, String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7, String paramString8, String paramString9, String paramString10, String paramString11, String paramString12, String paramString13, String paramString14, String paramString15, String paramString16, String paramString17, String paramString18, String paramString19) {
    ViewEC viewEC = new ViewEC(paramString1);
    viewEC.execImpl(paramOIObject, new String[] { 
          paramString2, paramString3, paramString4, paramString5, paramString6, paramString7, paramString8, paramString9, paramString10, paramString11, 
          paramString12, paramString13, paramString14, paramString15, paramString16, paramString17, paramString18, paramString19 });
  }
  
  public static void direct(OIObject paramOIObject, String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7, String paramString8, String paramString9, String paramString10, String paramString11, String paramString12, String paramString13, String paramString14, String paramString15, String paramString16, String paramString17, String paramString18, String paramString19, String paramString20, String paramString21) {
    ViewEC viewEC = new ViewEC(paramString1);
    viewEC.execImpl(paramOIObject, new String[] { 
          paramString2, paramString3, paramString4, paramString5, paramString6, paramString7, paramString8, paramString9, paramString10, paramString11, 
          paramString12, paramString13, paramString14, paramString15, paramString16, paramString17, paramString18, paramString19, paramString20, paramString21 });
  }
  
  public static void fromMPN(OIObject paramOIObject) throws Exception {
    try {
      logger.info("View Content Provider. [" + paramOIObject.getObjectID() + "]");
      OIObject oIObject = paramOIObject.getObject("ExternalContentId");
      if (oIObject == null)
        throw new ContentProviderException("Manufacturer Part is not associated to External Content."); 
      AbstractContentProvider abstractContentProvider = ContentProviderSelectDlg.selectContentProvider(paramOIObject.getObjectManager(), ContentProviderRegistryEntry.ContentProviderRole.VIEWABLE_CONTENT, paramOIObject.getStringified("ExternalContentId"));
      HashMap<String, String> hashMap = ContentProviderSync.getProviderIdMapForMPN(abstractContentProvider.getId(), paramOIObject);
      ViewEC viewEC = new ViewEC(abstractContentProvider.getId());
      viewEC.execImpl(paramOIObject, hashMap);
    } catch (ContentProviderException contentProviderException) {
      if (contentProviderException.isSSLCertException()) {
        logger.error("The SSL certificate is invalid.\nSee log for details.");
        logger.error(contentProviderException.getMessage(), (Throwable)contentProviderException);
      } else {
        JOptionPane.showMessageDialog(ContentProviderGlobal.getRootFrame(), contentProviderException.getMessage(), "View External Content", 0);
        logger.info(contentProviderException.getMessage(), (Throwable)contentProviderException);
      } 
    } 
  }
  
  protected void execImpl(OIObject paramOIObject, HashMap<String, String> paramHashMap) {
    if (this.bTaskInProgress)
      return; 
    logger.info("Retrieving External Content..");
    this.idPropMap = paramHashMap;
    Object object = null;
    this.progressMonitor = new ProgressMonitor(ContentProviderGlobal.getRootFrame(), "Retrieving External Content...", "", 0, 1);
    this.progressMonitor.setProgress(0);
    this.progressMonitor.setMillisToDecideToPopup(0);
    this.progressMonitor.setMillisToPopup(0);
    this.bTaskInProgress = true;
    this.task = new Task();
    this.task.addPropertyChangeListener(this);
    this.task.execute();
  }
  
  protected void execImpl(OIObject paramOIObject, String... paramVarArgs) {
    if (paramVarArgs.length == 0 || paramVarArgs.length % 2 != 0) {
      logger.error("Expecting 'dfobject' and a list of Content Provider Id Property/Value pairs.");
      return;
    } 
    try {
      this.idPropMap = new HashMap<>();
      for (int i = 0; i < paramVarArgs.length - 1; i += 2)
        this.idPropMap.put(paramVarArgs[i], paramOIObject.getStringified(paramVarArgs[i + 1])); 
      execImpl(paramOIObject, this.idPropMap);
    } catch (OIException oIException) {
      logger.error(oIException.getMessage());
      return;
    } 
  }
  
  public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent) {
    if ("progress" == paramPropertyChangeEvent.getPropertyName() && (this.progressMonitor.isCanceled() || this.task.isDone()) && this.progressMonitor.isCanceled())
      this.task.cancel(false); 
  }
  
  public static void viewDatasheet(OIObject paramOIObject) {
    try {
      logger.info("View Datasheet. [" + paramOIObject.getObjectID() + "]");
      AbstractContentProvider abstractContentProvider = ContentProviderSelectDlg.selectContentProvider(ContentProviderRegistryEntry.ContentProviderRole.VIEWABLE_CONTENT);
      ContentProviderConfig contentProviderConfig = abstractContentProvider.getConfig();
      String str = null;
      if (paramOIObject.getOIClass().getRootClass().getName().equals("Component")) {
        str = chooseDataSheetFromManufacturers(paramOIObject, abstractContentProvider);
        if (str != null && str.equals("CANCEL"))
          return; 
      } else if (paramOIObject.getOIClass().getRootClass().getName().equals("ManufacturerPart")) {
        HashMap hashMap = ContentProviderSync.getProviderIdMapForMPN(abstractContentProvider.getId(), paramOIObject);
        str = abstractContentProvider.getDatasheetURL(hashMap);
      } else if (paramOIObject.getOIClass().getRootClass().getName().equals("ExternalContent")) {
        HashMap hashMap = ContentProviderSync.getProviderIdMapForEC(abstractContentProvider.getId(), paramOIObject);
        str = abstractContentProvider.getDatasheetURL(hashMap);
      } 
      if (str != null && !str.trim().isEmpty()) {
        ContentProviderDocumentViewer.viewDocument(ContentProviderGlobal.getRootFrame(), str);
        logger.info("View datasheet URL:" + str);
      } else {
        JOptionPane.showMessageDialog(ContentProviderGlobal.getRootFrame(), "No datasheet available.", "View Datasheet", 1);
        logger.info("No datasheet available.");
      } 
    } catch (ContentProviderException contentProviderException) {
      if (contentProviderException.isSSLCertException()) {
        logger.error("The SSL certificate is invalid.\nSee log for details.");
        logger.error(contentProviderException.getMessage(), (Throwable)contentProviderException);
      } else {
        JOptionPane.showMessageDialog(ContentProviderGlobal.getRootFrame(), contentProviderException.getMessage(), "View Datasheet", 0);
        logger.info(contentProviderException.getMessage(), (Throwable)contentProviderException);
      } 
    } 
  }
  
  private static String chooseDataSheetFromManufacturers(OIObject paramOIObject, AbstractContentProvider paramAbstractContentProvider) throws ContentProviderException {
    IContentProviderApplication iContentProviderApplication = ContentProviderFactory.getInstance().getAppConfig().getApplicationImpl();
    MPNSelection mPNSelection = null;
    String str = null;
    int i = 0;
    MPNSelection[] arrayOfMPNSelection = null;
    try {
      OIObjectSet oIObjectSet = paramOIObject.getSet("ApprovedManufacturerList");
      i = oIObjectSet.size();
      arrayOfMPNSelection = new MPNSelection[i];
      byte b = 0;
      for (OIObject oIObject1 : oIObjectSet) {
        HashMap<Object, Object> hashMap = new HashMap<>();
        for (OIField oIField : oIObject1.getOIClass().getFields())
          hashMap.put(oIField.getName(), oIObject1.getStringified(oIField.getName())); 
        boolean bool = iContentProviderApplication.isRepresentativeMPN(hashMap);
        MPNSelection mPNSelection1 = new MPNSelection(oIObject1, bool);
        if (bool)
          mPNSelection = mPNSelection1; 
        arrayOfMPNSelection[b++] = new MPNSelection(oIObject1, bool);
      } 
    } catch (OIException oIException) {
      throw new ContentProviderSyncException(oIException);
    } 
    OIObject oIObject = null;
    if (i == 1) {
      oIObject = arrayOfMPNSelection[0].getMPNObject();
    } else if (i > 1) {
      if (mPNSelection == null)
        mPNSelection = arrayOfMPNSelection[0]; 
      MPNSelection mPNSelection1 = (MPNSelection)JOptionPane.showInputDialog(ContentProviderGlobal.getRootFrame(), "Select Manufacturer Part (*** = Representative)", "View Datasheet", -1, null, (Object[])arrayOfMPNSelection, mPNSelection);
      if (mPNSelection1 != null) {
        oIObject = mPNSelection1.getMPNObject();
      } else {
        str = "CANCEL";
      } 
    } 
    if (oIObject != null) {
      HashMap hashMap = ContentProviderSync.getProviderIdMapForMPN(paramAbstractContentProvider.getId(), oIObject);
      str = paramAbstractContentProvider.getDatasheetURL(hashMap);
    } 
    return str;
  }
  
  public static void viewDocumentURL(OIObject paramOIObject, String paramString) {
    try {
      String str = paramOIObject.getString(paramString);
      if (str != null && !str.trim().isEmpty()) {
        ContentProviderDocumentViewer.viewDocument(ContentProviderGlobal.getRootFrame(), str);
      } else {
        JOptionPane.showMessageDialog(ContentProviderGlobal.getRootFrame(), "No document available.", "View Document URL", 1);
      } 
    } catch (Exception exception) {
      logger.error("Unable to view URL:  " + exception.getMessage());
    } 
  }
  
  public static void viewAlertAttachedDocument(OIObject paramOIObject) {
    OIObject oIObject = paramOIObject;
    try {
      ContentProviderDocumentList contentProviderDocumentList = new ContentProviderDocumentList();
      if (paramOIObject.getOIClass().hasField("AlertECID")) {
        boolean bool = false;
        String str = paramOIObject.getString("Alert_ihsid");
        OIObject oIObject1 = paramOIObject.getObjectManager().getObjectByID(paramOIObject.getStringified("AlertECID"), "ExternalContent", true);
        if (!bool && oIObject1.getOIClass().hasField("PCNAlertList")) {
          OIObjectSet oIObjectSet1 = oIObject1.getSet("PCNAlertList");
          for (OIObject oIObject2 : oIObjectSet1) {
            if (str.equals(oIObject2.getString("Alert_ihsid"))) {
              oIObject = oIObject2;
              bool = true;
              break;
            } 
          } 
        } 
        if (!bool && oIObject1.getOIClass().hasField("PFNAlertList")) {
          OIObjectSet oIObjectSet1 = oIObject1.getSet("PFNAlertList");
          for (OIObject oIObject2 : oIObjectSet1) {
            if (str.equals(oIObject2.getString("Alert_ihsid"))) {
              oIObject = oIObject2;
              bool = true;
              break;
            } 
          } 
        } 
        if (!bool && oIObject1.getOIClass().hasField("EOLAlertList")) {
          OIObjectSet oIObjectSet1 = oIObject1.getSet("EOLAlertList");
          for (OIObject oIObject2 : oIObjectSet1) {
            if (str.equals(oIObject2.getString("Alert_ihsid"))) {
              oIObject = oIObject2;
              bool = true;
              break;
            } 
          } 
        } 
        if (!bool) {
          logger.error("Referenced alert was not found in the External Content object.");
          return;
        } 
      } 
      OIObjectSet oIObjectSet = oIObject.getSet("AlertDocList");
      for (OIObject oIObject1 : oIObjectSet) {
        ContentProviderDocument contentProviderDocument = new ContentProviderDocument();
        contentProviderDocument.setObjectId(oIObject1.getString("AlertDocLinekey"));
        contentProviderDocument.setTitle(oIObject1.getString("AlertDocTitle"));
        contentProviderDocument.setType(oIObject1.getString("AlertDocType"));
        contentProviderDocument.setPublicationDate(oIObject1.getDate("AlertDocPubDate"));
        contentProviderDocument.setURL(oIObject1.getString("AlertDocURL"));
        contentProviderDocumentList.add(contentProviderDocument);
      } 
      ContentProviderDocumentViewer.viewDocument(ContentProviderGlobal.getRootFrame(), contentProviderDocumentList);
    } catch (Exception exception) {
      logger.error(exception);
    } 
  }
  
  public static void main(String[] paramArrayOfString) {
    OIObjectManagerFactory oIObjectManagerFactory = null;
    OIObjectManager oIObjectManager = null;
    try {
      OIAuthenticate oIAuthenticate = OIAuthenticateFactory.createBatchAuthenticate("AggregationSvcTest");
      oIObjectManagerFactory = oIAuthenticate.login("Test App");
      System.out.println("Connected");
      oIObjectManager = oIObjectManagerFactory.createObjectManager();
      ContentProviderFactory.getInstance().registerContentProviders(oIObjectManager);
      AbstractContentProvider abstractContentProvider = ContentProviderFactory.getInstance().createContentProvider("SF");
      HashMap<Object, Object> hashMap = new HashMap<>();
      hashMap.put("DataProviderID", "24643725");
      IContentProviderResultRecord iContentProviderResultRecord = abstractContentProvider.getPart(hashMap);
      ArrayList<IContentProviderResultRecord> arrayList = new ArrayList();
      arrayList.add(iContentProviderResultRecord);
      ContentProviderViewCompareWindow.show(ContentProviderGlobal.getRootFrame(), arrayList);
    } catch (Exception exception) {
      exception.printStackTrace();
    } finally {
      if (oIObjectManager != null)
        oIObjectManager.close(); 
      if (oIObjectManagerFactory != null)
        oIObjectManagerFactory.close(); 
    } 
  }
  
  public static void createAndShowGUI() {
    try {
      if (System.getProperty("os.name").toLowerCase().startsWith("windows"))
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); 
    } catch (Exception exception) {}
    try {
      OIAuthenticate oIAuthenticate = OIAuthenticateFactory.createBatchAuthenticate("ONSemi");
      OIObjectManagerFactory oIObjectManagerFactory = oIAuthenticate.login("Test App");
      System.out.println("Connected");
      OIObjectManager oIObjectManager = oIObjectManagerFactory.createObjectManager();
      ContentProviderFactory.getInstance().registerContentProviders(oIObjectManager);
      testObj = oIObjectManager.getObjectByID("ON0000000608", "Component", true);
      viewDatasheet(testObj);
    } catch (Exception exception) {
      exception.printStackTrace();
    } 
  }
  
  class Task extends SwingWorker<Void, Void> {
    public Void doInBackground() {
      setProgress(0);
      ViewEC.this.progressMonitor.setProgress(0);
      try {
        ContentProviderFactory contentProviderFactory = ContentProviderFactory.getInstance();
        AbstractContentProvider abstractContentProvider = contentProviderFactory.createContentProvider(ViewEC.this.ccpId);
        ContentProviderConfig contentProviderConfig = abstractContentProvider.getConfig();
        IContentProviderResultRecord iContentProviderResultRecord = abstractContentProvider.getPart(ViewEC.this.idPropMap);
        if (iContentProviderResultRecord != null) {
          ViewEC.this.partRecList = new ArrayList<>();
          ViewEC.this.partRecList.add(iContentProviderResultRecord);
        } 
        setProgress(50);
        try {
          Thread.sleep(500L);
        } catch (InterruptedException interruptedException) {}
        if (!isCancelled())
          setProgress(100); 
      } catch (ContentProviderPartNotFoundException contentProviderPartNotFoundException) {
        JOptionPane.showMessageDialog(ContentProviderGlobal.getRootFrame(), "Referenced Content Provider part not found.");
      } catch (ContentProviderException contentProviderException) {
        if (contentProviderException.isSSLCertException()) {
          ViewEC.logger.error("The SSL certificate is invalid.\nSee log for details.");
          ViewEC.logger.error(contentProviderException.getMessage(), (Throwable)contentProviderException);
        } else {
          ViewEC.logger.error(contentProviderException.getMessage(), (Throwable)contentProviderException);
        } 
      } 
      return null;
    }
    
    public void done() {
      ViewEC.this.bTaskInProgress = false;
      ViewEC.this.progressMonitor.close();
      if (!isCancelled() && ViewEC.this.partRecList != null && !ViewEC.this.partRecList.isEmpty())
        ContentProviderViewCompareWindow.show(ContentProviderGlobal.getRootFrame(), ViewEC.this.partRecList); 
    }
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\client\ViewEC.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */