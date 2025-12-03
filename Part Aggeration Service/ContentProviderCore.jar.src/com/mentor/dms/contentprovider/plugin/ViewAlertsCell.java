package com.mentor.dms.contentprovider.plugin;

import com.mentor.dms.contentprovider.IContentProviderResultRecord;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

public class ViewAlertsCell extends AbstractPushButtonCell {
  public ViewAlertsCell(IContentProviderResultRecord paramIContentProviderResultRecord) {
    super(paramIContentProviderResultRecord);
    if (paramIContentProviderResultRecord.getContentProvider().isChangeAlertsSupported()) {
      JLabel jLabel = new JLabel("" + paramIContentProviderResultRecord.getChangeAlerts().size() + " Change Alert(s)");
      jLabel.setAlignmentX(0.5F);
      jLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
      add(jLabel);
    } 
    if (paramIContentProviderResultRecord.getContentProvider().isFailureAlertsSupported()) {
      JLabel jLabel = new JLabel("" + paramIContentProviderResultRecord.getFailureAlerts().size() + " Failure Alert(s)");
      jLabel.setAlignmentX(0.5F);
      jLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
      add(jLabel);
    } 
    if (paramIContentProviderResultRecord.getContentProvider().isPartStatusChangeAlertSupported()) {
      JLabel jLabel = new JLabel("" + paramIContentProviderResultRecord.getPartStatusChanges().size() + " Part Status Change(s)");
      jLabel.setAlignmentX(0.5F);
      jLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
      add(jLabel);
    } 
    if (paramIContentProviderResultRecord.getContentProvider().isEndOfLifeAlertSupported()) {
      JLabel jLabel = new JLabel("" + paramIContentProviderResultRecord.getEndOfLifeAlerts().size() + " End Of Life Alert(s)");
      jLabel.setAlignmentX(0.5F);
      jLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
      add(jLabel);
    } 
    if (paramIContentProviderResultRecord.getChangeAlerts().size() > 0 || paramIContentProviderResultRecord.getFailureAlerts().size() > 0 || paramIContentProviderResultRecord.getPartStatusChanges().size() > 0 || paramIContentProviderResultRecord.getEndOfLifeAlerts().size() > 0) {
      Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("images/alerts.png"));
      ImageIcon imageIcon = new ImageIcon(image);
      JButton jButton = new JButton("View...", imageIcon);
      jButton.setAlignmentX(0.5F);
      jButton.setActionCommand("ViewAlerts");
      jButton.addActionListener(this);
      jButton.setToolTipText("View alerts details");
      add(jButton);
    } 
  }
  
  public void actionPerformed(ActionEvent paramActionEvent) {
    if (paramActionEvent.getActionCommand().equals("ViewAlerts"))
      new ContentProviderViewAlertsDlg(ContentProviderViewCompareWindow.getJFrame(), this.resultRecord); 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\plugin\ViewAlertsCell.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */