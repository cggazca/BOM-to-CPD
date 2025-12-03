package com.mentor.dms.contentprovider.core.plugin;

import java.awt.Font;
import javax.swing.JLabel;

public class SectionHeaderCell extends JLabel implements ICustomCell {
  public SectionHeaderCell(String paramString) {
    super(paramString);
    Font font = new Font(getFont().getName(), 3, getFont().getSize());
    setFont(font);
    setOpaque(true);
  }
  
  public String toString() {
    return getText().trim();
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\plugin\SectionHeaderCell.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */