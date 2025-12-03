package com.mentor.dms.contentprovider.core.plugin.mfgsearchui;

import com.mentor.datafusion.oi.OIObjectManager;
import com.mentor.datafusion.oi.OIObjectManagerFactory;
import com.mentor.datafusion.oi.login.OIAuthenticate;
import com.mentor.datafusion.oi.login.OIAuthenticateFactory;
import com.mentor.datafusion.utils.logger.MGLogger;
import com.mentor.dms.contentprovider.core.AbstractContentProvider;
import com.mentor.dms.contentprovider.core.ContentProviderException;
import com.mentor.dms.contentprovider.core.ContentProviderFactory;
import com.mentor.dms.contentprovider.core.ContentProviderGlobal;
import com.mentor.dms.contentprovider.core.plugin.Activator;
import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.prefs.Preferences;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.UIManager;

public class ContentProviderManufacturerSearchDlg extends JDialog {
  private static MGLogger log = MGLogger.getLogger(ContentProviderManufacturerSearchDlg.class);
  
  private Preferences windowPrefs;
  
  private static ContentProviderManufacturerSearchMainPanel mainPanel = null;
  
  public ContentProviderManufacturerSearchDlg(JFrame paramJFrame, AbstractContentProvider paramAbstractContentProvider) {
    super(paramJFrame);
    init(paramAbstractContentProvider);
  }
  
  public ContentProviderManufacturerSearchDlg(JDialog paramJDialog, AbstractContentProvider paramAbstractContentProvider) {
    super(paramJDialog);
    init(paramAbstractContentProvider);
  }
  
  private void init(AbstractContentProvider paramAbstractContentProvider) {
    setTitle("Content Provider Manufacturer Search");
    setIconImage(ContentProviderGlobal.getAppIconImage());
    mainPanel = new ContentProviderManufacturerSearchMainPanel(this, paramAbstractContentProvider);
    setContentPane((Container)mainPanel);
    pack();
    Preferences preferences = Preferences.userRoot();
    this.windowPrefs = preferences.node("/com/mentor/mcd/dms/ContentProvider/plugin/partsearchui/ContentProviderManufacturerSearchDlg");
    int i = this.windowPrefs.getInt("left", -1);
    int j = this.windowPrefs.getInt("top", -1);
    int k = this.windowPrefs.getInt("width", 900);
    int m = this.windowPrefs.getInt("height", 500);
    if (i > 0) {
      setBounds(i, j, k, m);
    } else {
      setSize(k, m);
      setLocationRelativeTo(getOwner());
    } 
    setDefaultCloseOperation(2);
    addWindowListener(new WindowAdapter() {
          public void windowClosing(WindowEvent param1WindowEvent) {
            ContentProviderManufacturerSearchDlg.this.saveWindowPrefs();
          }
        });
    setVisible(true);
  }
  
  public void saveWindowPrefs() {
    this.windowPrefs.putInt("left", getX());
    this.windowPrefs.putInt("top", getY());
    this.windowPrefs.putInt("width", getWidth());
    this.windowPrefs.putInt("height", getHeight());
  }
  
  private static void setAppLookAndFeel() {
    try {
      if (System.getProperty("os.name").toLowerCase().startsWith("windows"))
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); 
    } catch (Exception exception) {
      exception.printStackTrace();
    } 
  }
  
  public static void show(JFrame paramJFrame, AbstractContentProvider paramAbstractContentProvider) throws ContentProviderException {
    setAppLookAndFeel();
    ContentProviderManufacturerSearchDlg contentProviderManufacturerSearchDlg = new ContentProviderManufacturerSearchDlg(paramJFrame, paramAbstractContentProvider);
  }
  
  public static void display(JDialog paramJDialog, AbstractContentProvider paramAbstractContentProvider) throws ContentProviderException {
    setAppLookAndFeel();
    ContentProviderManufacturerSearchDlg contentProviderManufacturerSearchDlg = new ContentProviderManufacturerSearchDlg(paramJDialog, paramAbstractContentProvider);
  }
  
  public static void main(String[] paramArrayOfString) {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception exception) {}
    OIObjectManagerFactory oIObjectManagerFactory = null;
    OIObjectManager oIObjectManager = null;
    try {
      ArrayList arrayList = new ArrayList();
      AbstractContentProvider abstractContentProvider = null;
      OIAuthenticate oIAuthenticate = OIAuthenticateFactory.createBatchAuthenticate("ONSemi");
      oIObjectManagerFactory = oIAuthenticate.login("Test App");
      System.out.println("Connected");
      oIObjectManager = oIObjectManagerFactory.createObjectManager();
      Activator.setObjectManager(oIObjectManager);
      ContentProviderFactory.getInstance().registerContentProviders(oIObjectManager);
      abstractContentProvider = ContentProviderFactory.getInstance().createContentProvider("SE");
      show((JFrame)null, abstractContentProvider);
    } catch (Exception exception) {
      exception.printStackTrace();
    } finally {
      if (oIObjectManager != null)
        oIObjectManager.close(); 
      if (oIObjectManagerFactory != null)
        oIObjectManagerFactory.close(); 
    } 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\plugin\mfgsearchui\ContentProviderManufacturerSearchDlg.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */