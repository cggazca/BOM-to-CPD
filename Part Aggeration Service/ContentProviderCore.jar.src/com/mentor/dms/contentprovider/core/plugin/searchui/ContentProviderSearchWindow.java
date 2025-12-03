package com.mentor.dms.contentprovider.core.plugin.searchui;

import com.mentor.datafusion.oi.OIException;
import com.mentor.datafusion.oi.OIHelper;
import com.mentor.datafusion.oi.OIObjectManager;
import com.mentor.datafusion.oi.OIObjectManagerFactory;
import com.mentor.datafusion.oi.login.OIAuthenticate;
import com.mentor.datafusion.oi.login.OIAuthenticateFactory;
import com.mentor.datafusion.oi.model.OIClass;
import com.mentor.datafusion.oi.model.OIField;
import com.mentor.datafusion.oi.type.OIObject;
import com.mentor.datafusion.utils.logger.MGLogger;
import com.mentor.dms.contentprovider.core.AbstractContentProvider;
import com.mentor.dms.contentprovider.core.ContentProviderException;
import com.mentor.dms.contentprovider.core.ContentProviderFactory;
import com.mentor.dms.contentprovider.core.ContentProviderGlobal;
import com.mentor.dms.contentprovider.core.ContentProviderRegistryEntry;
import com.mentor.dms.contentprovider.core.IContentProviderResultRecord;
import com.mentor.dms.contentprovider.core.UserSettings;
import com.mentor.dms.contentprovider.core.client.DesktopClient;
import com.mentor.dms.contentprovider.core.config.ContentProviderConfig;
import com.mentor.dms.contentprovider.core.config.ContentProviderConfigMPNCatalog;
import com.mentor.dms.contentprovider.core.config.ContentProviderConfigPropertyMap;
import com.mentor.dms.contentprovider.core.plugin.Activator;
import com.mentor.dms.contentprovider.core.plugin.ViewSupplyChainWindow;
import com.mentor.dms.dmsbrowser.DMSSearchRestrictionClassPanel;
import com.mentor.dms.dmsbrowser.DMSSearchRestrictionPanel;
import com.mentor.dms.ui.searchmask.NoSearchMaskFoundException;
import com.mentor.dms.ui.searchmask.SearchMask;
import com.mentor.dms.ui.searchmask.SearchMaskException;
import com.mentor.dms.ui.searchmask.SearchMaskManager;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Collection;
import javax.swing.JFrame;
import javax.swing.UIManager;
import org.apache.commons.lang3.StringUtils;
import org.jdesktop.swingx.util.WindowUtils;

public class ContentProviderSearchWindow extends JFrame {
  private static MGLogger log = MGLogger.getLogger(ContentProviderSearchWindow.class);
  
  private static ContentProviderSearchMainPanel mainPanel = null;
  
  private static ContentProviderSearchWindow mainWindow = null;
  
  private static DesktopClient.SearchContext currentSearchContext = DesktopClient.SearchContext.DESIGNER;
  
  private static OIObject assignmentMPNObj = null;
  
  public ContentProviderSearchWindow() {
    initComponents();
    setContentPane((Container)mainPanel);
    UserSettings.get().read();
    setSize(new Dimension(UserSettings.get().getSearchWindowWidth(), UserSettings.get().getSearchWindowHeight()));
    if (UserSettings.get().getSearchWindowLocX() >= 0) {
      setLocation(UserSettings.get().getSearchWindowLocX(), UserSettings.get().getSearchWindowLocY());
    } else {
      setLocation(WindowUtils.getPointForCentering(this));
    } 
    addWindowFocusListener(new WindowAdapter() {
          public void windowGainedFocus(WindowEvent param1WindowEvent) {
            try {
              ContentProviderSearchWindow.mainPanel.updateRestrictions();
            } catch (ContentProviderException contentProviderException) {}
          }
        });
    addWindowListener(new WindowAdapter() {
          public void windowClosing(WindowEvent param1WindowEvent) {
            Dimension dimension = ContentProviderSearchWindow.this.getSize();
            UserSettings.get().setSearchWindowWidth(dimension.width);
            UserSettings.get().setSearchWindowHeight(dimension.height);
            Point point = ContentProviderSearchWindow.this.getLocation();
            UserSettings.get().setSearchWindowLocX(point.x);
            UserSettings.get().setSearchWindowLocY(point.y);
            UserSettings.get().save();
            for (ViewSupplyChainWindow viewSupplyChainWindow : ViewSupplyChainWindow.getAllWindow()) {
              if (viewSupplyChainWindow != null)
                viewSupplyChainWindow.dispose(); 
            } 
            ViewSupplyChainWindow.clearActiveWindow();
          }
        });
  }
  
  private void initComponents() {
    setIconImage(ContentProviderGlobal.getAppIconImage());
    setDefaultCloseOperation(1);
    pack();
  }
  
  private static void setAppLookAndFeel() {
    try {
      if (System.getProperty("os.name").toLowerCase().startsWith("windows"))
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); 
    } catch (Exception exception) {
      exception.printStackTrace();
    } 
  }
  
  public static ContentProviderSearchWindow getInstance() {
    return mainWindow;
  }
  
  public static void main() {
    setAppLookAndFeel();
    mainPanel = new ContentProviderSearchMainPanel();
    mainWindow = new ContentProviderSearchWindow();
    mainWindow.setTitle("Extended Search");
  }
  
  public static void doSearch(DesktopClient.SearchContext paramSearchContext) {
    log.info("Start search in Content Provider.");
    currentSearchContext = paramSearchContext;
    if (mainWindow == null)
      main(); 
    ContentProviderRegistryEntry.ContentProviderRole contentProviderRole = ContentProviderRegistryEntry.ContentProviderRole.DESIGNER_SEARCH;
    if (paramSearchContext.equals(DesktopClient.SearchContext.COMPONENT_ENGINEER))
      contentProviderRole = ContentProviderRegistryEntry.ContentProviderRole.COMPONENT_ENGINEER_SEARCH; 
    mainPanel.initSearchProviders(contentProviderRole);
    mainWindow.setTitle("Extended Search");
    mainWindow.setVisible(true);
  }
  
  public static void doExternalContentAssignment(OIObject paramOIObject) throws ContentProviderException {
    currentSearchContext = DesktopClient.SearchContext.EXTERNAL_CONTENT_ASSIGNMENT;
    if (mainWindow == null)
      main(); 
    assignmentMPNObj = paramOIObject;
    try {
      AbstractContentProvider abstractContentProvider = mainPanel.getPreferredContentProvider(ContentProviderRegistryEntry.ContentProviderRole.MANUFACTURER_PART_SYNCHRONIZATION);
      ContentProviderConfig contentProviderConfig = abstractContentProvider.getConfig();
      ContentProviderConfigMPNCatalog contentProviderConfigMPNCatalog = contentProviderConfig.getMPNCatalogConfigByDMN(paramOIObject.getOIClass().getName());
      ContentProviderConfigPropertyMap contentProviderConfigPropertyMap1 = contentProviderConfigMPNCatalog.getContentProviderMap().getPropertyMapByContentProviderId(abstractContentProvider.getPartNumberPropID());
      String str1 = contentProviderConfigPropertyMap1.getDMN();
      String str2 = contentProviderConfigPropertyMap1.getSearchDMN();
      ContentProviderConfigPropertyMap contentProviderConfigPropertyMap2 = contentProviderConfigMPNCatalog.getContentProviderMap().getPropertyMapByContentProviderId(abstractContentProvider.getManufacturerPropID());
      String str3 = contentProviderConfigPropertyMap2.getDMN();
      String str4 = contentProviderConfigPropertyMap2.getSearchDMN();
      String str5 = paramOIObject.getString(str1);
      String str6 = paramOIObject.getStringified(str3);
      if (StringUtils.isEmpty(str6)) {
        str6 = paramOIObject.getStringified(str4);
        if (StringUtils.isEmpty(str6))
          throw new ContentProviderException("ManufacturerName is not set. Please enter and then execute."); 
      } 
      SearchMaskManager searchMaskManager = ContentProviderGlobal.getDMSInstance().getSearchMaskManager();
      SearchMask searchMask = searchMaskManager.open(paramOIObject.getObjectManager().getObjectManagerFactory().getClassManager().getOIClass("RootManufacturerPart"));
      searchMask = searchMaskManager.open(paramOIObject.getOIClass());
      DMSSearchRestrictionPanel dMSSearchRestrictionPanel = DMSSearchRestrictionPanel.getInstance();
      if (dMSSearchRestrictionPanel != null) {
        DMSSearchRestrictionClassPanel dMSSearchRestrictionClassPanel = dMSSearchRestrictionPanel.getCurSearchRestrictionClassPanel();
        if (dMSSearchRestrictionClassPanel != null && !dMSSearchRestrictionClassPanel.isClassicSearchVisible())
          dMSSearchRestrictionClassPanel.enableClassicSearch(); 
      } 
      searchMask.reset();
      searchMask.getRestrictionList().clear();
      try {
        OIField oIField = searchMask.getOIClass().getField(str2);
        String str = searchMask.getRestriction(oIField);
        if (str == null || str.trim().isEmpty())
          searchMask.setRestriction(oIField, OIHelper.escapeQueryRestriction(str5)); 
        oIField = searchMask.getOIClass().getField(str4);
        str = searchMask.getRestriction(oIField);
        if (str == null || str.trim().isEmpty())
          searchMask.setRestriction(oIField, OIHelper.escapeQueryRestriction(str6)); 
      } catch (OIException oIException) {
        OIField oIField = searchMask.getOIClass().getField("PartNumber");
        String str = searchMask.getRestriction(oIField);
        if (str == null || str.trim().isEmpty())
          searchMask.setRestriction(oIField, str5); 
        oIField = searchMask.getOIClass().getField("ManufacturerName");
        str = searchMask.getRestriction(oIField);
        if (str == null || str.trim().isEmpty())
          searchMask.setRestriction(oIField, str6); 
      } 
      mainWindow.setTitle("Assign External Content for Manufacturer Part '" + str5 + "' by '" + str6 + "'");
    } catch (OIException oIException) {
      throw new ContentProviderException(oIException.getMessage());
    } catch (SearchMaskException searchMaskException) {
      throw new ContentProviderException(searchMaskException.getMessage());
    } catch (NoSearchMaskFoundException noSearchMaskFoundException) {
      throw new ContentProviderException(noSearchMaskFoundException.getMessage());
    } 
    mainPanel.initSearchProviders(ContentProviderRegistryEntry.ContentProviderRole.MANUFACTURER_PART_SYNCHRONIZATION);
    mainWindow.setVisible(true);
  }
  
  public static void doSearchByMPN(OIClass paramOIClass, String paramString1, String paramString2) throws ContentProviderException {
    if (mainWindow == null)
      main(); 
    try {
      SearchMaskManager searchMaskManager = ContentProviderGlobal.getDMSInstance().getSearchMaskManager();
      SearchMask searchMask = searchMaskManager.open(paramOIClass);
      searchMask.getRestrictionList().clear();
      OIField oIField = searchMask.getOIClass().getField("PartNumber");
      String str = searchMask.getRestriction(oIField);
      if (str == null || str.trim().isEmpty())
        searchMask.setRestriction(oIField, paramString1); 
      oIField = searchMask.getOIClass().getField("ManufacturerName");
      str = searchMask.getRestriction(oIField);
      if (str == null || str.trim().isEmpty())
        searchMask.setRestriction(oIField, paramString2); 
    } catch (OIException oIException) {
      throw new ContentProviderException(oIException.getMessage());
    } catch (SearchMaskException searchMaskException) {
      throw new ContentProviderException(searchMaskException.getMessage());
    } catch (NoSearchMaskFoundException noSearchMaskFoundException) {
      throw new ContentProviderException(noSearchMaskFoundException.getMessage());
    } 
    mainPanel.initSearchProviders(ContentProviderRegistryEntry.ContentProviderRole.MANUFACTURER_PART_SYNCHRONIZATION);
    mainWindow.setVisible(true);
  }
  
  public static void main(String[] paramArrayOfString) {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception exception) {}
    OIObjectManagerFactory oIObjectManagerFactory = null;
    OIObjectManager oIObjectManager = null;
    try {
      OIAuthenticate oIAuthenticate = OIAuthenticateFactory.createBatchAuthenticate("testedm");
      oIObjectManagerFactory = oIAuthenticate.login("testedm");
      System.out.println("Connected");
      oIObjectManager = oIObjectManagerFactory.createObjectManager();
      Activator.setObjectManager(oIObjectManager);
      OIClass oIClass1 = oIObjectManagerFactory.getClassManager().getOIClass("M-Resistors");
      String str = "";
      OIClass oIClass2 = oIClass1.getSuperclass();
      if (oIClass2 != null && oIClass2.equals(oIClass1.getRootClass())) {
        str = oIClass2.getLabel();
      } else {
        str = oIClass1.getLabel();
      } 
      System.out.println(oIClass1.getLabel() + " = " + oIClass1.getLabel());
      ContentProviderFactory.getInstance().registerContentProviders(oIObjectManager);
      AbstractContentProvider abstractContentProvider = ContentProviderFactory.getInstance().createContentProvider("AS");
      main();
      doSearch(DesktopClient.SearchContext.COMPONENT_ENGINEER);
      return;
    } catch (Exception exception) {
      exception.printStackTrace();
    } finally {}
  }
  
  public static Collection<IContentProviderResultRecord> getSelectedResults() {
    return mainPanel.getSelectedResults();
  }
  
  public static int getSelectionCount() {
    return mainPanel.getSelectionCount();
  }
  
  public static AbstractContentProvider getSelectedContentProvider() {
    return mainPanel.getSelectedContentProvider();
  }
  
  public static DesktopClient.SearchContext getCurrentSearchContext() {
    return currentSearchContext;
  }
  
  public static OIObject getAssignmentMPN() {
    return assignmentMPNObj;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\plugin\searchui\ContentProviderSearchWindow.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */