package com.mentor.dms.contentprovider.client.catalogselector;

import com.mentor.datafusion.oi.OIException;
import com.mentor.datafusion.oi.OIObjectManager;
import com.mentor.datafusion.oi.OIObjectManagerFactory;
import com.mentor.datafusion.oi.login.OIAuthenticate;
import com.mentor.datafusion.oi.login.OIAuthenticateFactory;
import com.mentor.datafusion.oi.model.OIClass;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

public class CatalogSelectionDialog extends JDialog implements ActionListener, TreeSelectionListener {
  private DMSCatalogJTree _catalogJTree;
  
  private OIClass _selOICatalog = null;
  
  private final JButton _selectButton;
  
  public CatalogSelectionDialog(OIObjectManager paramOIObjectManager, JFrame paramJFrame, String paramString) {
    super(paramJFrame, paramString, true);
    this._catalogJTree = new DMSCatalogJTree<>(paramOIObjectManager, new CatalogNodeInfoFactory());
    this._catalogJTree.addTreeSelectionListener(this);
    this._catalogJTree.setShowsRootHandles(true);
    this._catalogJTree.getSelectionModel().setSelectionMode(1);
    JScrollPane jScrollPane = new JScrollPane(this._catalogJTree);
    jScrollPane.setPreferredSize(new Dimension(400, 500));
    jScrollPane.setAlignmentX(0.0F);
    JPanel jPanel1 = new JPanel();
    jPanel1.setLayout(new BoxLayout(jPanel1, 3));
    jPanel1.add(jScrollPane);
    jPanel1.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    JButton jButton = new JButton("Cancel", new ImageIcon(CatalogSelectionDialog.class.getResource("images/close_icon.png")));
    jButton.addActionListener(this);
    this._selectButton = new JButton("Select", new ImageIcon(CatalogSelectionDialog.class.getResource("images/check.png")));
    this._selectButton.setActionCommand("Select");
    this._selectButton.addActionListener(this);
    this._selectButton.setEnabled(false);
    getRootPane().setDefaultButton(this._selectButton);
    JPanel jPanel2 = new JPanel();
    jPanel2.setLayout(new BoxLayout(jPanel2, 2));
    jPanel2.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
    jPanel2.add(Box.createHorizontalGlue());
    jPanel2.add(jButton);
    jPanel2.add(Box.createRigidArea(new Dimension(10, 0)));
    jPanel2.add(this._selectButton);
    Container container = getContentPane();
    container.add(jPanel1, "Center");
    container.add(jPanel2, "Last");
    setDefaultCloseOperation(2);
    pack();
    setLocationRelativeTo(paramJFrame);
    setModal(true);
  }
  
  public OIClass showDialog(ImageIcon paramImageIcon, String paramString, Collection<String> paramCollection) throws OIException {
    this._catalogJTree.loadCatalogTree(paramImageIcon, paramString, paramCollection);
    setVisible(true);
    return this._selOICatalog;
  }
  
  public OIClass showDialog(ImageIcon paramImageIcon, String paramString) throws OIException {
    this._catalogJTree.loadCatalogTree(paramImageIcon, paramString);
    setVisible(true);
    return this._selOICatalog;
  }
  
  public void actionPerformed(ActionEvent paramActionEvent) {
    boolean bool = false;
    if ("Select".equals(paramActionEvent.getActionCommand())) {
      this._selOICatalog = this._catalogJTree.getSelectedOICatalog();
      if (this._selOICatalog == null) {
        JOptionPane.showMessageDialog(this, "Please select a catalog.");
        bool = true;
      } 
    } 
    setVisible(bool);
  }
  
  public void valueChanged(TreeSelectionEvent paramTreeSelectionEvent) {
    boolean bool = false;
    CatalogNodeInfo catalogNodeInfo = (CatalogNodeInfo)this._catalogJTree.getSelectedCatalogNode();
    if (catalogNodeInfo != null)
      bool = !catalogNodeInfo.isNoObjectsPerm() ? true : false; 
    this._selectButton.setEnabled(bool);
  }
  
  public static ImageIcon getComponentImageIcon() {
    return new ImageIcon(CatalogSelectionDialog.class.getResource("images/component.png"));
  }
  
  public static ImageIcon getManufacturerPartImageIcon() {
    return new ImageIcon(CatalogSelectionDialog.class.getResource("images/mpn.png"));
  }
  
  public static void main(String[] paramArrayOfString) {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception exception) {}
    JFrame jFrame = new JFrame();
    OIClass oIClass = null;
    OIObjectManagerFactory oIObjectManagerFactory = null;
    try {
      OIAuthenticate oIAuthenticate = OIAuthenticateFactory.createBatchAuthenticate("localhost");
      oIObjectManagerFactory = oIAuthenticate.login("Test App");
      System.out.println("Client ID = " + oIObjectManagerFactory.getClientID());
      System.out.println("Connected");
      ArrayList<String> arrayList = new ArrayList();
      CatalogSelectionDialog catalogSelectionDialog = new CatalogSelectionDialog(oIObjectManagerFactory.createObjectManager(), jFrame, "Select target Manufacturer Part catalog");
      oIClass = catalogSelectionDialog.showDialog(getManufacturerPartImageIcon(), "RootManufacturerPart", arrayList);
    } catch (OIException oIException) {
      JOptionPane.showMessageDialog(jFrame, oIException.getMessage());
      return;
    } catch (Exception exception) {
      JOptionPane.showMessageDialog(jFrame, exception.getMessage());
      return;
    } 
    if (oIObjectManagerFactory != null)
      oIObjectManagerFactory.close(); 
    if (oIClass != null) {
      JOptionPane.showMessageDialog(jFrame, oIClass.getLabel());
    } else {
      JOptionPane.showMessageDialog(jFrame, "User canceled the dialog.");
    } 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\client\catalogselector\CatalogSelectionDialog.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */