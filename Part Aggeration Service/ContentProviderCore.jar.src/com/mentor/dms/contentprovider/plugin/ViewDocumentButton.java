package com.mentor.dms.contentprovider.plugin;

import com.mentor.datafusion.utils.logger.MGLogger;
import com.mentor.dms.contentprovider.ContentProviderException;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;

public class ViewDocumentButton extends JButton implements ActionListener {
  private static MGLogger log = MGLogger.getLogger(ViewDocumentButton.class);
  
  private static HashMap<String, ImageIcon> docIconMap = new HashMap<>();
  
  private String url = null;
  
  public ViewDocumentButton(String paramString) {
    this(paramString, 24, 24);
  }
  
  public ViewDocumentButton(String paramString, int paramInt1, int paramInt2) {
    this.url = paramString;
    setOpaque(false);
    setContentAreaFilled(false);
    setBorderPainted(false);
    setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
    setFocusable(false);
    setAlignmentX(0.5F);
    if (paramString != null && !paramString.isEmpty()) {
      URL uRL;
      try {
        uRL = new URL(paramString);
      } catch (MalformedURLException malformedURLException) {
        log.error("Invalid URL : '" + paramString + "'.");
        return;
      } 
      String str = uRL.getPath().toLowerCase();
      int i = str.indexOf("?");
      if (i > 0)
        str = str.substring(0, i - 1); 
      setIcon(getImage(str, paramInt1, paramInt2));
      setToolTipText("View document");
      addActionListener(this);
    } else {
      setEnabled(false);
    } 
  }
  
  private ImageIcon getImage(String paramString, int paramInt1, int paramInt2) {
    ImageIcon imageIcon = null;
    String str1 = null;
    String str2 = null;
    if (paramString.endsWith(".pdf")) {
      str1 = "PDF";
      str2 = "images/Adobe_PDF_file_icon_24x24.png";
    } else if (paramString.endsWith(".doc") || paramString.endsWith(".docx")) {
      str1 = "DOC";
      str2 = "images/msword.png";
    } else if (paramString.endsWith(".xls") || paramString.endsWith(".xlsx")) {
      str1 = "XLS";
      str2 = "images/msexcel.png";
    } else if (paramString.endsWith(".msg")) {
      str1 = "MSG";
      str2 = "images/msg.png";
    } else {
      str1 = "WWW";
      str2 = "images/www_link.jpg";
    } 
    String str3 = str1 + "_" + str1 + "_" + Integer.toString(paramInt1);
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
      ContentProviderDocumentViewer.viewDocument(this, this.url);
    } catch (ContentProviderException contentProviderException) {
      JOptionPane.showMessageDialog(getParent(), "Unable to view document: " + contentProviderException.getMessage());
    } 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\plugin\ViewDocumentButton.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */