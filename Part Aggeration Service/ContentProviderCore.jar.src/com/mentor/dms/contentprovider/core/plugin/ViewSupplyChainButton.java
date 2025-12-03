package com.mentor.dms.contentprovider.core.plugin;

import com.mentor.datafusion.utils.logger.MGLogger;
import com.mentor.dms.contentprovider.core.ContentProviderSupplyChain;
import com.mentor.dms.contentprovider.core.plugin.searchui.ContentProviderSearchWindow;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ViewSupplyChainButton extends JButton implements ActionListener {
  private static MGLogger log = MGLogger.getLogger(ViewSupplyChainButton.class);
  
  private static HashMap<String, ImageIcon> docIconMap = new HashMap<>();
  
  private String url = null;
  
  private Collection<ContentProviderSupplyChain> supplychain = null;
  
  Map<String, String> definitions = new HashMap<>();
  
  Map<String, String> manufacturerInfo;
  
  public ViewSupplyChainButton(Map<String, String> paramMap1, Map<String, String> paramMap2, Collection<ContentProviderSupplyChain> paramCollection, int paramInt1, int paramInt2) {
    this.definitions = paramMap2;
    this.supplychain = paramCollection;
    this.manufacturerInfo = paramMap1;
    setOpaque(false);
    setContentAreaFilled(false);
    setBorderPainted(false);
    setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
    setFocusable(false);
    setAlignmentX(0.5F);
    if (paramCollection != null && paramCollection.size() > 0) {
      setIcon(getImage(paramInt1, paramInt2));
      setToolTipText(this.url);
      addActionListener(this);
    } else {
      setEnabled(false);
    } 
  }
  
  private ImageIcon getImage(int paramInt1, int paramInt2) {
    ImageIcon imageIcon = null;
    String str1 = null;
    String str2 = null;
    str2 = "images/documents.png";
    String str3 = "" + str1 + "_" + str1 + "_" + Integer.toString(paramInt1);
    imageIcon = docIconMap.get(str3);
    if (imageIcon == null) {
      try {
        BufferedImage bufferedImage = ImageIO.read(getClass().getResourceAsStream(str2));
        if (paramInt2 == 24 && paramInt1 == 24) {
          imageIcon = new ImageIcon(bufferedImage);
        } else {
          imageIcon = new ImageIcon(resize(bufferedImage, paramInt1, paramInt2));
        } 
      } catch (IOException iOException) {
        log.error(iOException);
      } 
      docIconMap.put(str3, imageIcon);
    } 
    return imageIcon;
  }
  
  private BufferedImage resize(BufferedImage paramBufferedImage, int paramInt1, int paramInt2) {
    Image image = paramBufferedImage.getScaledInstance(paramInt2, paramInt1, 4);
    BufferedImage bufferedImage = new BufferedImage(paramInt2, paramInt1, 2);
    Graphics2D graphics2D = bufferedImage.createGraphics();
    graphics2D.drawImage(image, 0, 0, (ImageObserver)null);
    graphics2D.dispose();
    return bufferedImage;
  }
  
  public void actionPerformed(ActionEvent paramActionEvent) {
    try {
      ViewSupplyChainWindow.show((JFrame)ContentProviderSearchWindow.getInstance(), this.definitions, this.supplychain, this.manufacturerInfo);
    } catch (Exception exception) {
      log.error(exception.getMessage(), exception);
      JOptionPane.showMessageDialog(getParent(), "Unable to view Supply Chain List: " + exception.getMessage());
    } 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\plugin\ViewSupplyChainButton.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */