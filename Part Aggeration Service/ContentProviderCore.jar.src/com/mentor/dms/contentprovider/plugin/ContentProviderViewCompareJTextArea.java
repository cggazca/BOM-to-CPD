package com.mentor.dms.contentprovider.plugin;

import java.awt.Dimension;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import javax.swing.JLabel;
import javax.swing.JTextArea;

public class ContentProviderViewCompareJTextArea extends JTextArea {
  public ContentProviderViewCompareJTextArea() {
    setFont((new JLabel()).getFont());
    setWrapStyleWord(true);
    setLineWrap(true);
  }
  
  public ContentProviderViewCompareJTextArea(String paramString) {
    super(paramString);
    setFont((new JLabel()).getFont());
    setWrapStyleWord(true);
    setLineWrap(true);
    setEditable(false);
    AffineTransform affineTransform = new AffineTransform();
    FontRenderContext fontRenderContext = new FontRenderContext(affineTransform, true, true);
    int i = (int)getFont().getStringBounds("X", fontRenderContext).getWidth();
    int j = (int)getFont().getStringBounds("X", fontRenderContext).getHeight();
    int k = (int)Math.ceil(paramString.length() / 120.0D);
    if (k < 1)
      k = 1; 
    int m = paramString.length();
    if (m < 1)
      m = 1; 
    if (m > 120)
      m = 120; 
    int n = k * j + 5;
    int i1 = m * i;
    setPreferredSize(new Dimension(i1, n));
  }
  
  public String toString() {
    return getText();
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\plugin\ContentProviderViewCompareJTextArea.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */