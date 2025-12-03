package com.mentor.dms.contentprovider.plugin;

import com.mentor.dms.contentprovider.IContentProviderResultRecord;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

public class ViewAlternatesCell extends AbstractPushButtonCell {
  public ViewAlternatesCell(IContentProviderResultRecord paramIContentProviderResultRecord) {
    super(paramIContentProviderResultRecord);
    JLabel jLabel = new JLabel("" + paramIContentProviderResultRecord.getAlternates().size() + " Suggested Alternate(s)");
    jLabel.setAlignmentX(0.5F);
    jLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
    Font font = new Font(jLabel.getFont().getName(), 1, jLabel.getFont().getSize());
    add(jLabel);
    if (paramIContentProviderResultRecord.getAlternates().size() > 0) {
      Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("images/alternates.png"));
      ImageIcon imageIcon = new ImageIcon(image);
      JButton jButton = new JButton("View...", imageIcon);
      jButton.setAlignmentX(0.5F);
      jButton.setActionCommand("ViewAlternates");
      jButton.setEnabled((paramIContentProviderResultRecord.getAlternates().size() > 0));
      jButton.addActionListener(this);
      jButton.setToolTipText("View alternates details");
      add(jButton);
    } 
  }
  
  public void actionPerformed(ActionEvent paramActionEvent) {
    if (paramActionEvent.getActionCommand().equals("ViewAlternates"))
      new ContentProviderViewAlternatesDlg(ContentProviderViewCompareWindow.getJFrame(), this.resultRecord); 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\plugin\ViewAlternatesCell.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */