package com.mentor.dms.contentprovider.plugin;

import com.mentor.dms.contentprovider.ComponentProperty;
import com.mentor.dms.contentprovider.DefaultDisplayColumn;
import com.mentor.dms.contentprovider.IContentProviderPartRequest;
import com.mentor.dms.contentprovider.IContentProviderResultRecord;
import com.mentor.dms.contentprovider.client.DesktopClient;
import com.mentor.dms.contentprovider.plugin.searchui.ContentProviderSearchWindow;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class ProductHeaderCell extends AbstractPushButtonCell {
  public ProductHeaderCell(IContentProviderResultRecord paramIContentProviderResultRecord) {
    super(paramIContentProviderResultRecord);
    JLabel jLabel = null;
    for (DefaultDisplayColumn defaultDisplayColumn : paramIContentProviderResultRecord.getContentProvider().getDefaultDisplayColumns()) {
      ComponentProperty componentProperty = paramIContentProviderResultRecord.getPartProperty(defaultDisplayColumn.getContentProviderPropertyId());
      if (componentProperty != null) {
        jLabel = new JLabel(componentProperty.getValue());
      } else {
        jLabel = new JLabel("N/A");
      } 
      jLabel.setAlignmentX(0.5F);
      Font font = new Font(jLabel.getFont().getName(), 1, jLabel.getFont().getSize());
      jLabel.setFont(font);
      add(jLabel);
    } 
    if (paramIContentProviderResultRecord.getContentProvider().isPartDetailsProductImageSupported() && paramIContentProviderResultRecord.getProductImageURL() != null) {
      ProductImageLabel productImageLabel = new ProductImageLabel(paramIContentProviderResultRecord.getProductImageURL(), 64);
      add(productImageLabel);
    } 
    if (paramIContentProviderResultRecord.getDatasheetURL() != null) {
      ViewDocumentButton viewDocumentButton = new ViewDocumentButton(paramIContentProviderResultRecord.getDatasheetURL());
      viewDocumentButton.setToolTipText("View datasheet for " + paramIContentProviderResultRecord.getPartNumber());
      add(viewDocumentButton);
    } 
    if (paramIContentProviderResultRecord.getContentProvider().getPartRequest() != null && ContentProviderSearchWindow.getCurrentSearchContext() != DesktopClient.SearchContext.EXTERNAL_CONTENT_ASSIGNMENT) {
      Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("images/add_request.png"));
      ImageIcon imageIcon = new ImageIcon(image);
      JButton jButton = new JButton("New Part Request", imageIcon);
      jButton.setAlignmentX(0.5F);
      jButton.setActionCommand("PartRequest");
      jButton.addActionListener(this);
      jButton.setToolTipText("Create request for this part");
      add(Box.createRigidArea(new Dimension(0, 5)));
      add(jButton);
    } 
    add(Box.createRigidArea(new Dimension(0, 5)));
  }
  
  public void actionPerformed(ActionEvent paramActionEvent) {
    if (paramActionEvent.getActionCommand().equals("PartRequest")) {
      IContentProviderPartRequest iContentProviderPartRequest = this.resultRecord.getContentProvider().getPartRequest();
      if (iContentProviderPartRequest != null)
        iContentProviderPartRequest.createPartRequest(this.resultRecord); 
    } else if (paramActionEvent.getActionCommand().equals("RemoveItem")) {
      JOptionPane.showMessageDialog(this, "Remove Item not yet implemented.");
    } 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\plugin\ProductHeaderCell.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */