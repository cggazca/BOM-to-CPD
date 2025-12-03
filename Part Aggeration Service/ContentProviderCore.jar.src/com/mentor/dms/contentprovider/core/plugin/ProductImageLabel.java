package com.mentor.dms.contentprovider.core.plugin;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.ToolTipManager;

public class ProductImageLabel extends JLabel {
  private final int defaultDismissDelay = ToolTipManager.sharedInstance().getDismissDelay();
  
  private final int defaultInitialDelay = ToolTipManager.sharedInstance().getInitialDelay();
  
  private int imageSize = 24;
  
  public ProductImageLabel(String paramString, int paramInt) {
    this.imageSize = paramInt;
    ImageIcon imageIcon = null;
    if (paramString != null && !paramString.isEmpty())
      try {
        BufferedImage bufferedImage1 = ImageIO.read(new URL(paramString));
        boolean bool = (bufferedImage1.getType() == 0) ? true : bufferedImage1.getType();
        BufferedImage bufferedImage2 = resizeImage(bufferedImage1, bool);
        imageIcon = new ImageIcon(bufferedImage2);
        setToolTipText("<html><body><img src='" + paramString + "'>");
      } catch (Exception exception) {} 
    if (imageIcon == null) {
      Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("images/No_image_available.jpg"));
      imageIcon = new ImageIcon(image);
    } 
    setIcon(imageIcon);
    setDisabledIcon(imageIcon);
    setEnabled(false);
    setPreferredSize(new Dimension(paramInt, paramInt));
    setMaximumSize(new Dimension(paramInt, paramInt));
    setOpaque(false);
    setFocusable(false);
    setVerticalTextPosition(0);
    setAlignmentX(0.5F);
    addMouseListener(new MouseAdapter() {
          public void mouseEntered(MouseEvent param1MouseEvent) {
            ToolTipManager.sharedInstance().setInitialDelay(0);
            ToolTipManager.sharedInstance().setDismissDelay(60000);
          }
          
          public void mouseExited(MouseEvent param1MouseEvent) {
            ToolTipManager.sharedInstance().setInitialDelay(ProductImageLabel.this.defaultInitialDelay);
            ToolTipManager.sharedInstance().setDismissDelay(ProductImageLabel.this.defaultDismissDelay);
          }
        });
  }
  
  private BufferedImage resizeImage(BufferedImage paramBufferedImage, int paramInt) {
    BufferedImage bufferedImage = new BufferedImage(this.imageSize, this.imageSize, paramInt);
    Graphics2D graphics2D = bufferedImage.createGraphics();
    graphics2D.drawImage(paramBufferedImage, 0, 0, this.imageSize, this.imageSize, null);
    graphics2D.dispose();
    return bufferedImage;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\plugin\ProductImageLabel.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */