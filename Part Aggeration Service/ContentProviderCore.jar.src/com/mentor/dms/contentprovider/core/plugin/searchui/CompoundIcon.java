package com.mentor.dms.contentprovider.core.plugin.searchui;

import java.awt.Component;
import java.awt.Graphics;
import java.util.Collection;
import javax.swing.Icon;

public class CompoundIcon implements Icon {
  private final int spacing = 2;
  
  private Collection<Icon> icons;
  
  public CompoundIcon(Collection<Icon> paramCollection) {
    this.icons = paramCollection;
  }
  
  public void paintIcon(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2) {
    for (Icon icon : this.icons) {
      icon.paintIcon(paramComponent, paramGraphics, paramInt1 + 2, paramInt2);
      paramInt1 += icon.getIconWidth();
    } 
  }
  
  public int getIconWidth() {
    int i = 0;
    for (Icon icon : this.icons)
      i += icon.getIconWidth() + 2; 
    return i;
  }
  
  public int getIconHeight() {
    int i = 0;
    for (Icon icon : this.icons) {
      if (icon.getIconHeight() > i)
        i = icon.getIconHeight(); 
    } 
    return i;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\plugin\searchui\CompoundIcon.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */