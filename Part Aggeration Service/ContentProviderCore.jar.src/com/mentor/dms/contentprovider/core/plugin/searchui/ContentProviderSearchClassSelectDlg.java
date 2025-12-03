package com.mentor.dms.contentprovider.core.plugin.searchui;

import com.mentor.dms.contentprovider.core.ContentProviderException;
import com.mentor.dms.contentprovider.core.ContentProviderGlobal;
import com.mentor.dms.contentprovider.core.config.ContentProviderConfigContentProviderMap;
import com.mentor.dms.contentprovider.core.config.ContentProviderConfigDMSCatalog;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.KeyStroke;

public class ContentProviderSearchClassSelectDlg extends JDialog implements ActionListener {
  private ContentProviderConfigContentProviderMap selectedContentProviderMap = null;
  
  private ButtonGroup group = new ButtonGroup();
  
  public ContentProviderSearchClassSelectDlg(JFrame paramJFrame, ContentProviderConfigDMSCatalog<?> paramContentProviderConfigDMSCatalog) {
    super(paramJFrame);
    setTitle("Content Provider Part Search Class");
    setIconImage(ContentProviderGlobal.getAppIconImage());
    JPanel jPanel1 = new JPanel(new BorderLayout());
    setContentPane(jPanel1);
    jPanel1.add(new JLabel("Please select a Content Provider part search class:"), "North");
    GridLayout gridLayout = new GridLayout(0, 1);
    JPanel jPanel2 = new JPanel(gridLayout);
    boolean bool = true;
    for (ContentProviderConfigContentProviderMap contentProviderConfigContentProviderMap : paramContentProviderConfigDMSCatalog.getContentProviderMaps()) {
      FlowLayout flowLayout = new FlowLayout(3);
      flowLayout.setVgap(0);
      JPanel jPanel = new JPanel(flowLayout);
      JContentProviderMapRadioButton jContentProviderMapRadioButton = new JContentProviderMapRadioButton();
      jContentProviderMapRadioButton.setContentProviderMap(contentProviderConfigContentProviderMap);
      if (bool) {
        jContentProviderMapRadioButton.setSelected(true);
        bool = false;
      } 
      jPanel.add(jContentProviderMapRadioButton);
      jPanel.add(new JLabel(contentProviderConfigContentProviderMap.getContentProviderLabel()));
      this.group.add(jContentProviderMapRadioButton);
      jPanel2.add(jPanel);
    } 
    jPanel2.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
    jPanel1.add(jPanel2, "Before");
    JPanel jPanel3 = new JPanel();
    JButton jButton1 = new JButton("Select");
    jButton1.addActionListener(this);
    jButton1.setActionCommand("SELECT_BUTTON");
    getRootPane().setDefaultButton(jButton1);
    jPanel3.add(jButton1);
    JButton jButton2 = new JButton("Cancel");
    jButton2.addActionListener(this);
    jButton2.setActionCommand("CANCEL_BUTTON");
    jPanel3.add(jButton2);
    jPanel1.add(jPanel3, "South");
    jPanel1.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    pack();
    ActionListener actionListener = new ActionListener() {
        public void actionPerformed(ActionEvent param1ActionEvent) {
          ContentProviderSearchClassSelectDlg.this.setVisible(false);
        }
      };
    KeyStroke keyStroke = KeyStroke.getKeyStroke(27, 0);
    getRootPane().registerKeyboardAction(actionListener, keyStroke, 2);
    setDefaultCloseOperation(2);
    setResizable(false);
    setModal(true);
    setLocationRelativeTo(paramJFrame);
  }
  
  public void actionPerformed(ActionEvent paramActionEvent) {
    if (paramActionEvent.getActionCommand().equals("SELECT_BUTTON")) {
      Enumeration<AbstractButton> enumeration = this.group.getElements();
      while (enumeration.hasMoreElements()) {
        JContentProviderMapRadioButton jContentProviderMapRadioButton = (JContentProviderMapRadioButton)enumeration.nextElement();
        if (jContentProviderMapRadioButton.isSelected()) {
          this.selectedContentProviderMap = jContentProviderMapRadioButton.getContentProviderMap();
          break;
        } 
      } 
      setVisible(false);
    } else if (paramActionEvent.getActionCommand().equals("CANCEL_BUTTON")) {
      this.selectedContentProviderMap = null;
      setVisible(false);
    } 
  }
  
  public static ContentProviderConfigContentProviderMap selectContentProviderMap(ContentProviderConfigDMSCatalog<?> paramContentProviderConfigDMSCatalog) throws ContentProviderException {
    ContentProviderConfigContentProviderMap contentProviderConfigContentProviderMap = null;
    if (paramContentProviderConfigDMSCatalog.getContentProviderMaps().size() != 0)
      if (paramContentProviderConfigDMSCatalog.getContentProviderMaps().size() == 1) {
        contentProviderConfigContentProviderMap = paramContentProviderConfigDMSCatalog.getContentProviderMaps().iterator().next();
      } else {
        ContentProviderSearchClassSelectDlg contentProviderSearchClassSelectDlg = new ContentProviderSearchClassSelectDlg(null, paramContentProviderConfigDMSCatalog);
        contentProviderSearchClassSelectDlg.setVisible(true);
        contentProviderConfigContentProviderMap = contentProviderSearchClassSelectDlg.getSelectedContentProviderMap();
      }  
    return contentProviderConfigContentProviderMap;
  }
  
  private ContentProviderConfigContentProviderMap getSelectedContentProviderMap() throws ContentProviderException {
    return this.selectedContentProviderMap;
  }
  
  public class JContentProviderMapRadioButton extends JRadioButton {
    private ContentProviderConfigContentProviderMap ccpMap = null;
    
    public ContentProviderConfigContentProviderMap getContentProviderMap() {
      return this.ccpMap;
    }
    
    public void setContentProviderMap(ContentProviderConfigContentProviderMap param1ContentProviderConfigContentProviderMap) {
      this.ccpMap = param1ContentProviderConfigContentProviderMap;
    }
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\plugin\searchui\ContentProviderSearchClassSelectDlg.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */