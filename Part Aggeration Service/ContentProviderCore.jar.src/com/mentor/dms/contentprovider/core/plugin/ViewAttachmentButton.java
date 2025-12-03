package com.mentor.dms.contentprovider.core.plugin;

import com.mentor.datafusion.utils.logger.MGLogger;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class ViewAttachmentButton extends JButton {
  private static MGLogger log = MGLogger.getLogger(ViewAttachmentButton.class);
  
  public ViewAttachmentButton() {
    Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("images/attachment.png"));
    ImageIcon imageIcon = new ImageIcon(image);
    setIcon(imageIcon);
    setOpaque(false);
    setContentAreaFilled(false);
    setBorderPainted(false);
    setFocusable(false);
    setAlignmentX(0.5F);
    setActionCommand("ViewAttachment");
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\plugin\ViewAttachmentButton.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */