package com.mentor.dms.contentprovider.core.sync.gui;

import com.mentor.datafusion.oi.OIException;
import com.mentor.datafusion.oi.OIObjectManager;
import com.mentor.datafusion.oi.type.OIObject;
import com.mentor.datafusion.utils.logger.MGLogger;
import com.mentor.dms.contentprovider.core.ContentProviderGlobal;
import com.mentor.dms.contentprovider.core.config.ContentProviderConfigException;
import com.mentor.dms.contentprovider.core.sync.ContentProviderSync;
import com.mentor.dms.contentprovider.core.sync.ContentProviderSyncException;
import com.mentor.dms.contentprovider.core.sync.ReconcileResultCounter;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.HashSet;
import javax.swing.BoxLayout;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ProgressMonitor;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import org.apache.log4j.Logger;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.table.TableColumnExt;
import org.jdesktop.swingx.treetable.TreeTableModel;

public class ContentProviderReconcileApp extends JPanel implements ActionListener, PropertyChangeListener {
  static Logger logger = (Logger)MGLogger.getLogger(ContentProviderReconcileApp.class);
  
  private static final long serialVersionUID = 1L;
  
  public static final String APP_TITLE = "EDM Library/External Content Property Reconcile";
  
  private OIObjectManager om;
  
  private HashSet<String> mpnIDSet = null;
  
  private JDialog frame;
  
  private ContentProviderReconcileTableModel tm;
  
  private JXTreeTable dmsReconcileJTable;
  
  private JScrollPane dmsReconcileScrollPane;
  
  private JMenuItem reconcileMenuItem;
  
  private JMenuItem exitMenuItem;
  
  private JMenuItem viewDiffsMenuItem;
  
  private JMenuItem expandAllMenuItem;
  
  private JMenuItem collapseAllMenuItem;
  
  private ProgressMonitor progressMonitor;
  
  private Task task;
  
  private HashMap<String, HashMap<String, String>> errorHash = new HashMap<>();
  
  public ContentProviderReconcileApp(JDialog paramJDialog, OIObjectManager paramOIObjectManager, HashSet<String> paramHashSet) {
    logger.info("Reconcile App Start.");
    this.frame = paramJDialog;
    this.om = paramOIObjectManager;
    this.mpnIDSet = paramHashSet;
    this.tm = new ContentProviderReconcileTableModel(paramJDialog);
    this.dmsReconcileJTable = new ContentProviderReconcileJXTreeTable((TreeTableModel)this.tm);
    this.dmsReconcileScrollPane = new JScrollPane((Component)this.dmsReconcileJTable);
    setLayout(new BoxLayout(this, 1));
    add(this.dmsReconcileScrollPane);
    setWindowListener(paramJDialog);
    paramJDialog.setJMenuBar(createMenuBar());
    paramJDialog.getContentPane().add(this);
    paramJDialog.setPreferredSize(new Dimension(1000, 600));
    paramJDialog.setIconImage(ContentProviderGlobal.getAppIconImage());
    paramJDialog.pack();
    Toolkit toolkit = Toolkit.getDefaultToolkit();
    Dimension dimension = toolkit.getScreenSize();
    int i = (dimension.width - paramJDialog.getWidth()) / 2;
    int j = (dimension.height - paramJDialog.getHeight()) / 2;
    paramJDialog.setLocation(i, j);
    if (doLoad())
      paramJDialog.setVisible(true); 
  }
  
  public static void createAndShowGUI(OIObjectManager paramOIObjectManager, HashSet<String> paramHashSet) {
    try {
      if (System.getProperty("os.name").toLowerCase().startsWith("windows"))
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); 
    } catch (Exception exception) {}
    JDialog jDialog = new JDialog(null, "EDM Library/External Content Property Reconcile", Dialog.ModalityType.APPLICATION_MODAL);
    jDialog.setDefaultCloseOperation(0);
    new ContentProviderReconcileApp(jDialog, paramOIObjectManager, paramHashSet);
  }
  
  public void setWindowListener(Window paramWindow) {
    paramWindow.addWindowListener(new WindowAdapter() {
          public void windowClosing(WindowEvent param1WindowEvent) {
            ContentProviderReconcileApp.this.confirmExit(param1WindowEvent.getWindow());
          }
          
          public void windowOpened(WindowEvent param1WindowEvent) {
            TableColumnExt tableColumnExt = ContentProviderReconcileApp.this.dmsReconcileJTable.getColumnExt(3);
            tableColumnExt.setMaxWidth(120);
            tableColumnExt.setMinWidth(30);
            tableColumnExt.setPreferredWidth(100);
            tableColumnExt.setWidth(100);
            tableColumnExt = ContentProviderReconcileApp.this.dmsReconcileJTable.getColumnExt(1);
            tableColumnExt.setMaxWidth(120);
            tableColumnExt.setMinWidth(30);
            tableColumnExt.setPreferredWidth(100);
            tableColumnExt.setWidth(100);
          }
        });
  }
  
  public JMenuBar createMenuBar() {
    JMenuBar jMenuBar = new JMenuBar();
    JMenu jMenu = new JMenu("File");
    jMenu.setMnemonic(70);
    jMenuBar.add(jMenu);
    this.reconcileMenuItem = new JMenuItem("Reconcile", 82);
    this.reconcileMenuItem.addActionListener(this);
    jMenu.add(this.reconcileMenuItem);
    jMenu.addSeparator();
    this.exitMenuItem = new JMenuItem("Exit", 88);
    this.exitMenuItem.addActionListener(this);
    jMenu.add(this.exitMenuItem);
    jMenu = new JMenu("View");
    jMenu.setMnemonic(86);
    jMenuBar.add(jMenu);
    this.viewDiffsMenuItem = new JCheckBoxMenuItem("Show only property updates/errors");
    this.viewDiffsMenuItem.addActionListener(this);
    jMenu.add(this.viewDiffsMenuItem);
    jMenu.addSeparator();
    this.collapseAllMenuItem = new JMenuItem("Collapse All", 67);
    this.collapseAllMenuItem.addActionListener(this);
    jMenu.add(this.collapseAllMenuItem);
    this.expandAllMenuItem = new JMenuItem("Expand All", 69);
    this.expandAllMenuItem.addActionListener(this);
    jMenu.add(this.expandAllMenuItem);
    return jMenuBar;
  }
  
  public void actionPerformed(ActionEvent paramActionEvent) {
    JMenuItem jMenuItem = (JMenuItem)paramActionEvent.getSource();
    if (jMenuItem.equals(this.reconcileMenuItem)) {
      doReconcile();
    } else if (jMenuItem.equals(this.exitMenuItem)) {
      confirmExit(this);
    } else if (jMenuItem.equals(this.viewDiffsMenuItem)) {
      this.tm.setUpdateErrorFilter(this.viewDiffsMenuItem.isSelected());
      this.dmsReconcileJTable.expandAll();
      this.dmsReconcileJTable.packAll();
    } else if (jMenuItem.equals(this.collapseAllMenuItem)) {
      this.dmsReconcileJTable.collapseAll();
      this.dmsReconcileJTable.packAll();
    } else if (jMenuItem.equals(this.expandAllMenuItem)) {
      this.dmsReconcileJTable.expandAll();
      this.dmsReconcileJTable.packAll();
    } 
  }
  
  private boolean doLoad() {
    Cursor cursor1 = this.frame.getCursor();
    Cursor cursor2 = new Cursor(3);
    this.frame.setCursor(cursor2);
    try {
      this.tm.loadUnReconciledProperties(this.om, this.mpnIDSet, this.errorHash, this.frame);
    } catch (ContentProviderConfigException contentProviderConfigException) {
      JOptionPane.showMessageDialog(this.frame, "Configuration problem encountered while loading unreconciled Manufacturer Parts:\n\n" + contentProviderConfigException.getMessage());
    } catch (Exception exception) {
      JOptionPane.showMessageDialog(this.frame, "Problem encountered while querying for unreconciled Manufacturer Parts:\n\n" + exception.getMessage());
      exception.printStackTrace();
    } 
    this.frame.setCursor(cursor1);
    this.dmsReconcileJTable.expandAll();
    this.dmsReconcileJTable.packAll();
    this.dmsReconcileJTable.setAutoResizeMode(3);
    boolean bool = true;
    if (!this.tm.isbReconcileDataExist() && ContentProviderGlobal.isReviewSyncMode()) {
      JOptionPane.showMessageDialog(this.frame, "Reconcile target data does not exist in Manufacture Part.", "EDM Library/External Content Property Reconcile", 1);
      this.frame.dispose();
      return false;
    } 
    if (this.tm.getMPNCount() == 0) {
      if (ReconcileResultCounter.getiReconcileTotalCount() == ReconcileResultCounter.getiReconcileCount()) {
        JOptionPane.showMessageDialog(this.frame, "Reconcile finished successfully.", "EDM Library/External Content Property Reconcile", 1);
      } else {
        JOptionPane.showMessageDialog(this.frame, "Reconcile canceled during the process. " + ReconcileResultCounter.getiReconcileCount() + " / " + ReconcileResultCounter.getiReconcileTotalCount() + " reconciled.", "EDM Library/External Content Property Reconcile", 1);
      } 
      this.frame.dispose();
      bool = false;
    } else {
      this.frame.setCursor((Cursor)null);
    } 
    return bool;
  }
  
  private void doReconcile() {
    logger.info("Start Reconcile..");
    boolean bool1 = false;
    boolean bool2 = false;
    for (ReconcileTreeNode reconcileTreeNode : ((ReconcileTreeNode)this.tm.getRoot()).getChildren()) {
      if (reconcileTreeNode.getSyncAction() != ContentProviderSync.SyncActionEnum.RECONCILE)
        continue; 
      for (ReconcileTreeNode reconcileTreeNode1 : reconcileTreeNode.getChildren()) {
        if (reconcileTreeNode1.getSyncAction() == ContentProviderSync.SyncActionEnum.IGNORE_ONCE)
          bool1 = true; 
        if (reconcileTreeNode1.getSyncAction() == ContentProviderSync.SyncActionEnum.IGNORE_ALWAYS)
          bool2 = true; 
      } 
    } 
    if (bool1) {
      int j = JOptionPane.showConfirmDialog(this, "You indicated an Action of 'Ignore Once' on one or more properties.\nThese property(s) will be ignored when the Manufacturer Part is reconciled.\n\nContinue?", "EDM Library/External Content Property Reconcile", 2);
      if (j != 0)
        return; 
    } 
    if (bool2) {
      int j = JOptionPane.showConfirmDialog(this, "You have indicated an Action of 'Ignore Always' on one or more properties.\nThese property(s) will be ignored when the Manufacturer Part is reconciled.\n\nContinue?", "EDM Library/External Content Property Reconcile", 2);
      if (j != 0)
        return; 
    } 
    int i = this.tm.getMPNSyncCount();
    if (i == 1) {
      this.frame.setCursor(new Cursor(3));
      this.task = new Task();
      this.task.addPropertyChangeListener(this);
      this.task.execute();
      this.reconcileMenuItem.setEnabled(false);
      setCursor(null);
    } else if (i != 0) {
      this.progressMonitor = new ProgressMonitor(this, "Reconciling with External Content..", "", 0, this.tm.getMPNSyncCount());
      this.progressMonitor.setProgress(0);
      this.progressMonitor.setMillisToDecideToPopup(0);
      this.progressMonitor.setMillisToPopup(0);
      this.task = new Task();
      this.task.addPropertyChangeListener(this);
      this.task.execute();
      this.reconcileMenuItem.setEnabled(false);
    } else if (this.tm.getMPNCount() == 0) {
      JOptionPane.showMessageDialog(this, "No unreconciled Manufacturer Parts exist.  No action performed.");
    } else {
      JOptionPane.showMessageDialog(this, "No Manufacturer Parts have Action set to 'Reconcile'.  No action performed.");
    } 
  }
  
  private void confirmExit(Component paramComponent) {
    int i = 0;
    if (this.tm.getMPNCount() != 0)
      i = JOptionPane.showConfirmDialog(paramComponent, "Unreconciled Manufacturer Parts exist.  Are you sure you want to exit?", "EDM Library/External Content Property Reconcile", 0); 
    if (i == 0)
      this.frame.dispose(); 
  }
  
  public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent) {
    if ("progress" == paramPropertyChangeEvent.getPropertyName() && (this.progressMonitor != null || this.progressMonitor.isCanceled() || this.task.isDone())) {
      if (this.progressMonitor.isCanceled())
        this.task.cancel(false); 
      this.reconcileMenuItem.setEnabled(true);
    } 
  }
  
  class Task extends SwingWorker<Void, Void> {
    public Void doInBackground() {
      byte b = 0;
      setProgress(0);
      ContentProviderReconcileApp.this.errorHash.clear();
      new ReconcileResultCounter(ContentProviderReconcileApp.this.tm.getMPNSyncCount());
      for (ReconcileTreeNode reconcileTreeNode : ((ReconcileTreeNode)ContentProviderReconcileApp.this.tm.getRoot()).getChildren()) {
        if (reconcileTreeNode.getSyncAction() != ContentProviderSync.SyncActionEnum.RECONCILE)
          continue; 
        HashMap<Object, Object> hashMap1 = new HashMap<>();
        ContentProviderReconcileApp.this.errorHash.put(reconcileTreeNode.getPropName(), hashMap1);
        if (ContentProviderReconcileApp.this.progressMonitor != null) {
          ContentProviderReconcileApp.this.progressMonitor.setNote(reconcileTreeNode.getPropName());
          ContentProviderReconcileApp.this.progressMonitor.setProgress(b);
        } 
        int i = b * 100 / ContentProviderReconcileApp.this.tm.getMPNSyncCount();
        setProgress(i);
        b++;
        HashMap<Object, Object> hashMap2 = new HashMap<>();
        HashSet<String> hashSet = new HashSet();
        for (ReconcileTreeNode reconcileTreeNode1 : reconcileTreeNode.getChildren()) {
          if (reconcileTreeNode1.getSyncAction() == ContentProviderSync.SyncActionEnum.IGNORE_ONCE) {
            hashMap2.put(((ReconcilePropertyTreeNode)reconcileTreeNode1).getPropId(), ((ReconcilePropertyTreeNode)reconcileTreeNode1).getDMSValue());
            continue;
          } 
          if (reconcileTreeNode1.getSyncAction() == ContentProviderSync.SyncActionEnum.IGNORE_ALWAYS) {
            hashSet.add(((ReconcilePropertyTreeNode)reconcileTreeNode1).getPropId());
            hashMap2.put(((ReconcilePropertyTreeNode)reconcileTreeNode1).getPropId(), ((ReconcilePropertyTreeNode)reconcileTreeNode1).getDMSValue());
          } 
        } 
        if (isCancelled())
          break; 
        try {
          OIObject oIObject = ContentProviderReconcileApp.this.om.getObjectByID(reconcileTreeNode.getPropName(), "ExternalContent", true);
          oIObject.getObjectManager().refreshAndLockObject(oIObject);
          if (hashSet.size() > 0)
            ContentProviderSync.setReconcileActions(oIObject, hashSet); 
          ContentProviderSync.compareAndReconcile(null, oIObject, true, hashMap2, hashMap1);
        } catch (OIException oIException) {
          ContentProviderReconcileApp.logger.error(oIException.getMessage());
        } catch (ContentProviderSyncException contentProviderSyncException) {
          ContentProviderReconcileApp.logger.error(contentProviderSyncException.getMessage());
        } catch (ContentProviderConfigException contentProviderConfigException) {
          ContentProviderReconcileApp.logger.error(contentProviderConfigException.getMessage());
        } 
        ReconcileResultCounter.addiReconcileCount();
        if (isCancelled())
          break; 
      } 
      return null;
    }
    
    public void done() {
      if (ContentProviderReconcileApp.this.progressMonitor != null)
        ContentProviderReconcileApp.this.progressMonitor.close(); 
      ContentProviderReconcileApp.this.reconcileMenuItem.setEnabled(true);
      ContentProviderReconcileApp.this.doLoad();
    }
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\sync\gui\ContentProviderReconcileApp.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */