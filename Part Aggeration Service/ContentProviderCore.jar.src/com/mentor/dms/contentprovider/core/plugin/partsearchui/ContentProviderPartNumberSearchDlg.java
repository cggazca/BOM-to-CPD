package com.mentor.dms.contentprovider.core.plugin.partsearchui;

import com.mentor.datafusion.oi.OICursor;
import com.mentor.datafusion.oi.OIObjectManager;
import com.mentor.datafusion.oi.OIObjectManagerFactory;
import com.mentor.datafusion.oi.OIQuery;
import com.mentor.datafusion.oi.login.OIAuthenticate;
import com.mentor.datafusion.oi.login.OIAuthenticateFactory;
import com.mentor.datafusion.utils.logger.MGLogger;
import com.mentor.dms.contentprovider.core.AbstractContentProvider;
import com.mentor.dms.contentprovider.core.ContentProviderException;
import com.mentor.dms.contentprovider.core.ContentProviderFactory;
import com.mentor.dms.contentprovider.core.ContentProviderGlobal;
import com.mentor.dms.contentprovider.core.IContentProviderResultRecord;
import com.mentor.dms.contentprovider.core.plugin.Activator;
import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class ContentProviderPartNumberSearchDlg extends JDialog {
  private static MGLogger log = MGLogger.getLogger(ContentProviderPartNumberSearchDlg.class);
  
  private Preferences windowPrefs;
  
  private static ContentProviderPartNumberSearchMainPanel mainPanel = null;
  
  public ContentProviderPartNumberSearchDlg(JFrame paramJFrame, AbstractContentProvider paramAbstractContentProvider, String paramString1, String paramString2, List<String> paramList) {
    super(paramJFrame, true);
    init(paramAbstractContentProvider, paramString1, paramString2, paramList);
  }
  
  public ContentProviderPartNumberSearchDlg(JDialog paramJDialog, AbstractContentProvider paramAbstractContentProvider, String paramString1, String paramString2, List<String> paramList) {
    super(paramJDialog, true);
    init(paramAbstractContentProvider, paramString1, paramString2, paramList);
  }
  
  private void init(AbstractContentProvider paramAbstractContentProvider, String paramString1, String paramString2, List<String> paramList) {
    setTitle("Content Provider Part Number Search");
    setIconImage(ContentProviderGlobal.getAppIconImage());
    mainPanel = new ContentProviderPartNumberSearchMainPanel(this, paramAbstractContentProvider, paramString1, paramString2, paramList);
    setContentPane((Container)mainPanel);
    pack();
    Preferences preferences = Preferences.userRoot();
    this.windowPrefs = preferences.node("/com/mentor/mcd/dms/ContentProvider/plugin/partsearchui/ContentProviderPartNumberSearchDlg");
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
            ContentProviderPartNumberSearchDlg.this.saveWindowPrefs();
          }
        });
    try {
      if (!paramString1.trim().isEmpty())
        mainPanel.doSearch(); 
    } catch (ContentProviderException contentProviderException) {
      JOptionPane.showMessageDialog(ContentProviderGlobal.getRootFrame(), contentProviderException.getMessage());
    } 
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
  
  public static IContentProviderResultRecord doSearch(JFrame paramJFrame, AbstractContentProvider paramAbstractContentProvider, String paramString1, String paramString2, List<String> paramList) throws ContentProviderException {
    setAppLookAndFeel();
    ContentProviderPartNumberSearchDlg contentProviderPartNumberSearchDlg = new ContentProviderPartNumberSearchDlg(paramJFrame, paramAbstractContentProvider, paramString1, paramString2, paramList);
    return contentProviderPartNumberSearchDlg.getSelectedPart();
  }
  
  public static IContentProviderResultRecord doSearch(JDialog paramJDialog, AbstractContentProvider paramAbstractContentProvider, String paramString1, String paramString2, List<String> paramList) throws ContentProviderException {
    setAppLookAndFeel();
    ContentProviderPartNumberSearchDlg contentProviderPartNumberSearchDlg = new ContentProviderPartNumberSearchDlg(paramJDialog, paramAbstractContentProvider, paramString1, paramString2, paramList);
    return contentProviderPartNumberSearchDlg.getSelectedPart();
  }
  
  private IContentProviderResultRecord getSelectedPart() {
    return mainPanel.getSelectedPart();
  }
  
  public static void main(String[] paramArrayOfString) {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception exception) {}
    OIObjectManagerFactory oIObjectManagerFactory = null;
    OIObjectManager oIObjectManager = null;
    try {
      ArrayList<String> arrayList = new ArrayList();
      AbstractContentProvider abstractContentProvider = null;
      OIAuthenticate oIAuthenticate = OIAuthenticateFactory.createBatchAuthenticate("testedm");
      oIObjectManagerFactory = oIAuthenticate.login("Test App");
      System.out.println("Connected");
      oIObjectManager = oIObjectManagerFactory.createObjectManager();
      Activator.setObjectManager(oIObjectManager);
      ContentProviderFactory.getInstance().registerContentProviders(oIObjectManager);
      abstractContentProvider = ContentProviderFactory.getInstance().createContentProvider("SE");
      OIQuery oIQuery = oIObjectManager.createQuery("Manufacturer", true);
      oIQuery.addRestriction("ManufacturerName", "~NULL");
      oIQuery.addColumn("ManufacturerName");
      OICursor oICursor = oIQuery.execute();
      while (oICursor.next())
        arrayList.add(oICursor.getString("ManufacturerName")); 
      IContentProviderResultRecord iContentProviderResultRecord = doSearch((JFrame)null, abstractContentProvider, "08055C*", "AVX", arrayList);
      if (iContentProviderResultRecord == null) {
        JOptionPane.showMessageDialog(null, "Cancelled");
      } else {
        JOptionPane.showMessageDialog(null, iContentProviderResultRecord.getPartNumber() + " by " + iContentProviderResultRecord.getPartNumber() + " (" + iContentProviderResultRecord.getManufacturerName() + ")");
      } 
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


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\plugin\partsearchui\ContentProviderPartNumberSearchDlg.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */