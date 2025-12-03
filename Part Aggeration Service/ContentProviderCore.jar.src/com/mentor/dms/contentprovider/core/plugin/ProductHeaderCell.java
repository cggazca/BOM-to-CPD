package com.mentor.dms.contentprovider.core.plugin;

import com.mentor.dms.contentprovider.core.ComponentProperty;
import com.mentor.dms.contentprovider.core.DefaultDisplayColumn;
import com.mentor.dms.contentprovider.core.IContentProviderPartRequest;
import com.mentor.dms.contentprovider.core.IContentProviderResultRecord;
import com.mentor.dms.contentprovider.core.client.DesktopClient;
import com.mentor.dms.contentprovider.core.plugin.searchui.ContentProviderSearchWindow;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class ProductHeaderCell extends AbstractPushButtonCell {
  private boolean dispNewPartRequest = false;
  
  private boolean dispViewDatasheet = false;
  
  private GridBagLayout gridBagLayout = new GridBagLayout();
  
  private int nextCountX = 0;
  
  private int nextCountY = 0;
  
  public ProductHeaderCell(IContentProviderResultRecord paramIContentProviderResultRecord) {
    this.resultRecord = paramIContentProviderResultRecord;
    setOpaque(true);
    setLayout(this.gridBagLayout);
    JLabel jLabel1 = null;
    GridBagConstraints gridBagConstraints = null;
    JLabel jLabel2 = null;
    for (DefaultDisplayColumn defaultDisplayColumn : paramIContentProviderResultRecord.getContentProvider().getDefaultDisplayColumns()) {
      if (!defaultDisplayColumn.isDisplayOnHeader())
        continue; 
      ComponentProperty componentProperty = paramIContentProviderResultRecord.getPartProperty(defaultDisplayColumn.getContentProviderPropertyId());
      if (componentProperty != null) {
        jLabel1 = new JLabel(componentProperty.getValue());
      } else {
        jLabel1 = new JLabel("N/A");
      } 
      jLabel1.setHorizontalAlignment(0);
      Font font = new Font(jLabel1.getFont().getName(), 1, jLabel1.getFont().getSize());
      jLabel1.setFont(font);
      GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
      gridBagConstraints1.gridx = this.nextCountX;
      gridBagConstraints1.gridy = this.nextCountY;
      this.nextCountY++;
      gridBagConstraints1.weightx = 1.0D;
      gridBagConstraints1.weighty = 0.0D;
      gridBagConstraints1.fill = 2;
      gridBagConstraints1.anchor = 11;
      this.gridBagLayout.setConstraints(jLabel1, gridBagConstraints1);
      gridBagConstraints = gridBagConstraints1;
      jLabel2 = jLabel1;
      add(jLabel1);
    } 
    if (paramIContentProviderResultRecord.getContentProvider().isPartDetailsProductImageSupported() && paramIContentProviderResultRecord.getProductImageURL() != null) {
      ProductImageLabel productImageLabel = new ProductImageLabel(paramIContentProviderResultRecord.getProductImageURL(), 64);
      productImageLabel.setHorizontalAlignment(0);
      GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
      gridBagConstraints1.gridx = this.nextCountX;
      gridBagConstraints1.gridy = this.nextCountY;
      this.nextCountY++;
      gridBagConstraints1.weightx = 0.0D;
      gridBagConstraints1.weighty = 0.0D;
      gridBagConstraints1.fill = 2;
      gridBagConstraints1.anchor = 11;
      this.gridBagLayout.setConstraints(productImageLabel, gridBagConstraints1);
      gridBagConstraints = gridBagConstraints1;
      jLabel2 = productImageLabel;
      add(productImageLabel);
    } 
    this.dispViewDatasheet = (paramIContentProviderResultRecord.getDatasheetURL() != null);
    if (this.dispViewDatasheet) {
      ViewDocumentButton viewDocumentButton = new ViewDocumentButton(paramIContentProviderResultRecord.getDatasheetURL());
      viewDocumentButton.setToolTipText("View datasheet for " + paramIContentProviderResultRecord.getPartNumber());
      viewDocumentButton.setHorizontalAlignment(0);
      GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
      gridBagConstraints1.gridx = this.nextCountX;
      gridBagConstraints1.gridy = this.nextCountY;
      this.nextCountY++;
      gridBagConstraints1.weightx = 0.0D;
      gridBagConstraints1.weighty = 1.0D;
      gridBagConstraints1.fill = 2;
      gridBagConstraints1.anchor = 15;
      this.gridBagLayout.setConstraints(viewDocumentButton, gridBagConstraints1);
      viewDocumentButton.setCursor(Cursor.getPredefinedCursor(12));
      add(viewDocumentButton);
    } else {
      gridBagConstraints.weighty = 1.0D;
      this.gridBagLayout.setConstraints(jLabel2, gridBagConstraints);
    } 
    this.dispNewPartRequest = (paramIContentProviderResultRecord.getContentProvider().getPartRequest() != null && ContentProviderSearchWindow.getCurrentSearchContext() != DesktopClient.SearchContext.EXTERNAL_CONTENT_ASSIGNMENT);
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
  
  public boolean isDispViewDatasheet() {
    return this.dispViewDatasheet;
  }
  
  public void dispDummyViewDatasheet() {
    if (this.dispViewDatasheet)
      return; 
    JLabel jLabel = new JLabel(" ");
    jLabel.setHorizontalAlignment(0);
    Font font = new Font(jLabel.getFont().getName(), 1, 22);
    jLabel.setFont(font);
    GridBagConstraints gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = this.nextCountX;
    gridBagConstraints.gridy = this.nextCountY;
    this.nextCountY++;
    gridBagConstraints.weightx = 0.0D;
    gridBagConstraints.weighty = 0.0D;
    gridBagConstraints.fill = 2;
    gridBagConstraints.anchor = 15;
    this.gridBagLayout.setConstraints(jLabel, gridBagConstraints);
    add(jLabel);
  }
  
  public boolean isDispNewPartRequest() {
    return this.dispNewPartRequest;
  }
  
  public void dispNewPartRequest() {
    if (this.dispNewPartRequest) {
      Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("images/add_request.png"));
      ImageIcon imageIcon = new ImageIcon(image);
      JButton jButton = new JButton("New Part Request", imageIcon);
      jButton.setHorizontalAlignment(0);
      jButton.setActionCommand("PartRequest");
      jButton.addActionListener(this);
      jButton.setToolTipText("Create request for this part");
      add(Box.createRigidArea(new Dimension(0, 5)));
      GridBagConstraints gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = this.nextCountX;
      gridBagConstraints.gridy = this.nextCountY;
      this.nextCountY++;
      gridBagConstraints.weightx = 0.0D;
      gridBagConstraints.weighty = 0.0D;
      gridBagConstraints.anchor = 15;
      this.gridBagLayout.setConstraints(jButton, gridBagConstraints);
      add(jButton);
    } else {
      JLabel jLabel = new JLabel(" ");
      jLabel.setHorizontalAlignment(0);
      Font font = new Font(jLabel.getFont().getName(), 1, 18);
      jLabel.setFont(font);
      GridBagConstraints gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = this.nextCountX;
      gridBagConstraints.gridy = this.nextCountY;
      this.nextCountY++;
      gridBagConstraints.weightx = 0.0D;
      gridBagConstraints.weighty = 0.0D;
      gridBagConstraints.fill = 2;
      gridBagConstraints.anchor = 15;
      this.gridBagLayout.setConstraints(jLabel, gridBagConstraints);
      add(jLabel);
    } 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\plugin\ProductHeaderCell.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */