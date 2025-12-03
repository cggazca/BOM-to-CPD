package com.mentor.dms.contentprovider.sf.ui;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import com.mentor.dms.contentprovider.sf.ContentProviderImpl;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import org.jdesktop.swingx.JXTitledSeparator;

public class AccountStatusDlg extends JDialog {
  private static final long serialVersionUID = 1L;
  
  private final JPanel contentPanel = new JPanel();
  
  public AccountStatusDlg(JFrame paramJFrame, String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6) {
    setTitle("SiliconExpert Part Search� Account Status");
    Image image = Toolkit.getDefaultToolkit().getImage(ContentProviderImpl.class.getResource("images/SiliconExpert_Small.png"));
    setIconImage(image);
    setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
    setLocationRelativeTo(paramJFrame);
    this.contentPanel.setBorder(new EmptyBorder(10, 10, 20, 10));
    getContentPane().add(this.contentPanel, "Center");
    this.contentPanel.setLayout((LayoutManager)new FormLayout(new ColumnSpec[] { FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow") }, new RowSpec[] { 
            FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, 
            FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC }));
    byte b = 2;
    JXTitledSeparator jXTitledSeparator = new JXTitledSeparator("Account");
    this.contentPanel.add((Component)jXTitledSeparator, "1, " + b + ", 4, 1, fill, default");
    b += 2;
    JLabel jLabel = new JLabel("Name:");
    this.contentPanel.add(jLabel, "2, " + b + ", right, default");
    JTextField jTextField = new JTextField(20);
    jTextField.setEditable(false);
    jTextField.setText(paramString1);
    this.contentPanel.add(jTextField, "4, " + b + ", left, default");
    b += 2;
    jLabel = new JLabel("Creation Date:");
    this.contentPanel.add(jLabel, "2, " + b + ", right, default");
    jTextField = new JTextField(10);
    jTextField.setEditable(false);
    jTextField.setText(paramString2);
    this.contentPanel.add(jTextField, "4, " + b + ", left, default");
    b += 2;
    jLabel = new JLabel("Expiration Date:");
    this.contentPanel.add(jLabel, "2, " + b + ", right, default");
    jTextField = new JTextField(10);
    jTextField.setEditable(false);
    jTextField.setText(paramString3);
    this.contentPanel.add(jTextField, "4, " + b + ", left, default");
    b += 4;
    jXTitledSeparator = new JXTitledSeparator("Quota");
    this.contentPanel.add((Component)jXTitledSeparator, "1, " + b + ", 4, 1, fill, default");
    b += 2;
    jLabel = new JLabel("Limit:");
    this.contentPanel.add(jLabel, "2, " + b + ", right, default");
    jTextField = new JTextField(10);
    jTextField.setEditable(false);
    jTextField.setText(paramString4);
    this.contentPanel.add(jTextField, "4, " + b + ", left, default");
    b += 2;
    jLabel = new JLabel("Count:");
    this.contentPanel.add(jLabel, "2, " + b + ", right, default");
    jTextField = new JTextField(10);
    jTextField.setEditable(false);
    jTextField.setText(paramString5);
    this.contentPanel.add(jTextField, "4, " + b + ", left, default");
    b += 2;
    jLabel = new JLabel("Remaining:");
    this.contentPanel.add(jLabel, "2, " + b + ", right, default");
    jTextField = new JTextField(10);
    jTextField.setEditable(false);
    jTextField.setText(paramString6);
    this.contentPanel.add(jTextField, "4, " + b + ", left, default");
    b += 2;
    pack();
    setVisible(true);
  }
  
  public static void main(String[] paramArrayOfString) {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception exception) {
      exception.printStackTrace();
    } 
    AccountStatusDlg accountStatusDlg = new AccountStatusDlg(null, "", "", "", "", "", "");
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\s\\ui\AccountStatusDlg.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */