package com.mentor.dms.contentprovider.core.plugin;

import com.mentor.datafusion.oi.OIObjectManager;
import com.mentor.datafusion.oi.OIObjectManagerFactory;
import com.mentor.datafusion.oi.login.OIAuthenticate;
import com.mentor.datafusion.oi.login.OIAuthenticateFactory;
import com.mentor.dms.contentprovider.core.AbstractContentProvider;
import com.mentor.dms.contentprovider.core.ContentProviderChangeAlert;
import com.mentor.dms.contentprovider.core.ContentProviderDocumentList;
import com.mentor.dms.contentprovider.core.ContentProviderEndOfLifeAlert;
import com.mentor.dms.contentprovider.core.ContentProviderFactory;
import com.mentor.dms.contentprovider.core.ContentProviderFailureAlert;
import com.mentor.dms.contentprovider.core.ContentProviderGlobal;
import com.mentor.dms.contentprovider.core.ContentProviderPartStatusChange;
import com.mentor.dms.contentprovider.core.IContentProviderResultRecord;
import com.mentor.dms.contentprovider.core.config.ContentProviderConfig;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;
import org.jdesktop.swingx.plaf.LookAndFeelAddons;
import org.jdesktop.swingx.plaf.windows.WindowsClassicLookAndFeelAddons;

public class ContentProviderViewAlertsDlg extends JDialog {
  public ContentProviderViewAlertsDlg(Frame paramFrame, IContentProviderResultRecord paramIContentProviderResultRecord) {
    super(paramFrame, true);
    char c1 = 'ζ';
    char c2 = '̠';
    setTitle("View Alerts for " + paramIContentProviderResultRecord.getPartNumber() + " from " + paramIContentProviderResultRecord.getManufacturerName());
    setIconImage(ContentProviderGlobal.getAppIconImage());
    JXPanel jXPanel = new JXPanel();
    jXPanel.setLayout(new BorderLayout());
    getContentPane().add((Component)jXPanel);
    JXTaskPaneContainer jXTaskPaneContainer = new JXTaskPaneContainer();
    jXPanel.add(new JScrollPane((Component)jXTaskPaneContainer), "Center");
    if (paramIContentProviderResultRecord.getChangeAlerts().size() > 0) {
      Vector<String> vector = new Vector();
      vector.add("");
      vector.add("Change Notification Number");
      vector.add("Change Notification Type");
      vector.add("Description");
      vector.add("Issue Date");
      vector.add("Implementation Date");
      vector.add("Alert Information Source");
      vector.add("Change Notification Code");
      Vector<Vector> vector1 = new Vector();
      for (ContentProviderChangeAlert contentProviderChangeAlert : paramIContentProviderResultRecord.getChangeAlerts()) {
        Vector<ContentProviderDocumentList> vector2 = new Vector();
        vector1.add(vector2);
        vector2.addElement(contentProviderChangeAlert.getAttachedDocuments());
        vector2.addElement(contentProviderChangeAlert.getNotificationNumber());
        vector2.addElement(contentProviderChangeAlert.getNotificationType());
        vector2.addElement(contentProviderChangeAlert.getNotificationDescription());
        vector2.addElement(contentProviderChangeAlert.getIssueDate());
        vector2.addElement(contentProviderChangeAlert.getImplementationDate());
        vector2.addElement(contentProviderChangeAlert.getInformationSource());
        vector2.addElement(contentProviderChangeAlert.getCode());
      } 
      ContentProviderViewAlertsJXTable contentProviderViewAlertsJXTable = new ContentProviderViewAlertsJXTable(new ContentProviderViewAlertsTableModel(vector1, vector));
      JScrollPane jScrollPane = new JScrollPane((Component)contentProviderViewAlertsJXTable);
      JXTaskPane jXTaskPane = new JXTaskPane();
      jXTaskPane.add(jScrollPane);
      jXTaskPane.setTitle("Product Change Notices (" + paramIContentProviderResultRecord.getChangeAlerts().size() + ")");
      jXTaskPane.setIcon(new ImageIcon(ContentProviderViewAlertsDlg.class.getResource("images/alerts.png")));
      jXTaskPaneContainer.add((Component)jXTaskPane);
    } 
    if (paramIContentProviderResultRecord.getFailureAlerts().size() > 0) {
      Vector<String> vector = new Vector();
      vector.add("");
      vector.add("Failure Notification Number");
      vector.add("Manufacturer Issue Date");
      vector.add("Affected Batch/Serial Number");
      vector.add("Problem Description");
      vector.add("Action Taken/Planned");
      vector.add("Alert Information Source");
      Vector<Vector> vector1 = new Vector();
      for (ContentProviderFailureAlert contentProviderFailureAlert : paramIContentProviderResultRecord.getFailureAlerts()) {
        Vector<ContentProviderDocumentList> vector2 = new Vector();
        vector1.add(vector2);
        vector2.addElement(contentProviderFailureAlert.getAttachedDocuments());
        vector2.addElement(contentProviderFailureAlert.getNotificationNumber());
        vector2.addElement(contentProviderFailureAlert.getIssueDate());
        vector2.addElement(contentProviderFailureAlert.getSerialNumber());
        vector2.addElement(contentProviderFailureAlert.getProblemDescription());
        vector2.addElement(contentProviderFailureAlert.getPlannedAction());
        vector2.addElement(contentProviderFailureAlert.getInformationSource());
      } 
      ContentProviderViewAlertsJXTable contentProviderViewAlertsJXTable = new ContentProviderViewAlertsJXTable(new ContentProviderViewAlertsTableModel(vector1, vector));
      JScrollPane jScrollPane = new JScrollPane((Component)contentProviderViewAlertsJXTable);
      JXTaskPane jXTaskPane = new JXTaskPane();
      jXTaskPane.add(jScrollPane);
      jXTaskPane.setTitle("Product Failure Notices (" + paramIContentProviderResultRecord.getFailureAlerts().size() + ")");
      jXTaskPane.setIcon(new ImageIcon(ContentProviderViewAlertsDlg.class.getResource("images/alerts.png")));
      jXTaskPaneContainer.add((Component)jXTaskPane);
    } 
    if (paramIContentProviderResultRecord.getPartStatusChanges().size() > 0) {
      Vector<String> vector = new Vector();
      vector.add("Alert Number");
      vector.add("Change Notification Type");
      vector.add("Description");
      vector.add("Issue Date");
      vector.add("Information Source");
      vector.add("Modified Date");
      Vector<Vector> vector1 = new Vector();
      for (ContentProviderPartStatusChange contentProviderPartStatusChange : paramIContentProviderResultRecord.getPartStatusChanges()) {
        Vector<String> vector2 = new Vector();
        vector1.add(vector2);
        vector2.addElement(contentProviderPartStatusChange.getAlertNumber());
        vector2.addElement(contentProviderPartStatusChange.getNotificationType());
        vector2.addElement(contentProviderPartStatusChange.getNotificationDescription());
        vector2.addElement(contentProviderPartStatusChange.getIssueDate());
        vector2.addElement(contentProviderPartStatusChange.getInformationSource());
        vector2.addElement(contentProviderPartStatusChange.getModifiedDate());
      } 
      ContentProviderViewAlertsJXTable contentProviderViewAlertsJXTable = new ContentProviderViewAlertsJXTable(new ContentProviderViewAlertsTableModel(vector1, vector));
      JScrollPane jScrollPane = new JScrollPane((Component)contentProviderViewAlertsJXTable);
      JXTaskPane jXTaskPane = new JXTaskPane();
      jXTaskPane.add(jScrollPane);
      jXTaskPane.setTitle("Part Status Changes (" + paramIContentProviderResultRecord.getPartStatusChanges().size() + ")");
      jXTaskPane.setIcon(new ImageIcon(ContentProviderViewAlertsDlg.class.getResource("images/alerts.png")));
      jXTaskPaneContainer.add((Component)jXTaskPane);
    } 
    if (paramIContentProviderResultRecord.getEndOfLifeAlerts().size() > 0) {
      Vector<String> vector = new Vector();
      vector.add("");
      vector.add("Alert Number");
      vector.add("Last Time Buy Date");
      vector.add("Last Time Delivery Date");
      vector.add("Life Cyle Information Source");
      Vector<Vector> vector1 = new Vector();
      for (ContentProviderEndOfLifeAlert contentProviderEndOfLifeAlert : paramIContentProviderResultRecord.getEndOfLifeAlerts()) {
        Vector<ContentProviderDocumentList> vector2 = new Vector();
        vector1.add(vector2);
        vector2.addElement(contentProviderEndOfLifeAlert.getAttachedDocuments());
        vector2.addElement(contentProviderEndOfLifeAlert.getAlertNumber());
        vector2.addElement(contentProviderEndOfLifeAlert.getLastTimeBuyDate());
        vector2.addElement(contentProviderEndOfLifeAlert.getLastTimeDeliveryDate());
        vector2.addElement(contentProviderEndOfLifeAlert.getLifeCycleInformationSource());
      } 
      ContentProviderViewAlertsJXTable contentProviderViewAlertsJXTable = new ContentProviderViewAlertsJXTable(new ContentProviderViewAlertsTableModel(vector1, vector));
      JScrollPane jScrollPane = new JScrollPane((Component)contentProviderViewAlertsJXTable);
      JXTaskPane jXTaskPane = new JXTaskPane();
      jXTaskPane.add(jScrollPane);
      jXTaskPane.setTitle("End Of Life Notices (" + paramIContentProviderResultRecord.getEndOfLifeAlerts().size() + ")");
      jXTaskPane.setIcon(new ImageIcon(ContentProviderViewAlertsDlg.class.getResource("images/alerts.png")));
      jXTaskPaneContainer.add((Component)jXTaskPane);
    } 
    setDefaultCloseOperation(2);
    pack();
    setLocationRelativeTo(paramFrame);
    setVisible(true);
  }
  
  public static void main(String[] paramArrayOfString) {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      LookAndFeelAddons.setAddon(WindowsClassicLookAndFeelAddons.class);
    } catch (Exception exception) {}
    OIObjectManagerFactory oIObjectManagerFactory = null;
    OIObjectManager oIObjectManager = null;
    try {
      OIAuthenticate oIAuthenticate = OIAuthenticateFactory.createBatchAuthenticate("dms_desktop");
      oIObjectManagerFactory = oIAuthenticate.login("Test App");
      System.out.println("Connected");
      oIObjectManager = oIObjectManagerFactory.createObjectManager();
      ContentProviderFactory.getInstance().registerContentProviders(oIObjectManager);
      AbstractContentProvider abstractContentProvider = ContentProviderFactory.getInstance().createContentProvider("SE");
      ContentProviderConfig contentProviderConfig = abstractContentProvider.getConfig();
      HashMap<Object, Object> hashMap = new HashMap<>();
      hashMap.put("DataProviderID", "24643725");
      IContentProviderResultRecord iContentProviderResultRecord = abstractContentProvider.getPart(hashMap);
      ContentProviderViewAlertsDlg contentProviderViewAlertsDlg = new ContentProviderViewAlertsDlg(null, iContentProviderResultRecord);
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


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\plugin\ContentProviderViewAlertsDlg.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */