package com.mentor.dms.contentprovider.plugin;

import com.mentor.dms.contentprovider.ContentProviderDocumentList;
import com.mentor.dms.contentprovider.IContentProviderResultRecord;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

public class ViewDocumentsCell extends AbstractPushButtonCell {
  private ContentProviderDocumentList docList;
  
  public ViewDocumentsCell(IContentProviderResultRecord paramIContentProviderResultRecord, ContentProviderDocumentList paramContentProviderDocumentList) {
    super(paramIContentProviderResultRecord);
    JLabel jLabel;
    this.docList = paramContentProviderDocumentList;
    if (paramContentProviderDocumentList == null || paramContentProviderDocumentList.size() == 0) {
      jLabel = new JLabel("No Documents available.");
    } else {
      jLabel = new JLabel("" + paramContentProviderDocumentList.size() + " Document(s)");
    } 
    jLabel.setAlignmentX(0.5F);
    jLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
    add(jLabel);
    if (paramContentProviderDocumentList != null && paramContentProviderDocumentList.size() > 0) {
      Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("images/documents.png"));
      ImageIcon imageIcon = new ImageIcon(image);
      JButton jButton = new JButton("View...", imageIcon);
      jButton.setAlignmentX(0.5F);
      jButton.setActionCommand("ViewDocuments");
      jButton.setEnabled((paramContentProviderDocumentList.size() > 0));
      jButton.addActionListener(this);
      jButton.setToolTipText("View documents details");
      add(jButton);
    } 
  }
  
  public void actionPerformed(ActionEvent paramActionEvent) {
    if (paramActionEvent.getActionCommand().equals("ViewDocuments"))
      new ContentProviderViewDocumentsDlg(ContentProviderViewCompareWindow.getJFrame(), this.docList); 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\plugin\ViewDocumentsCell.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */