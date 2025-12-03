package com.mentor.dms.contentprovider.plugin;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;

class JTextAreaCellRenderer extends JScrollPane implements TableCellRenderer {
  private JTextArea textarea;
  
  public JTextAreaCellRenderer() {
    setBorder((Border)null);
    this.textarea = new JTextArea();
    this.textarea.setFont((new JLabel()).getFont());
    this.textarea.setLineWrap(true);
    this.textarea.setWrapStyleWord(true);
    this.textarea.setEditable(false);
    getViewport().setView(this.textarea);
  }
  
  public Component getTableCellRendererComponent(JTable paramJTable, Object paramObject, boolean paramBoolean1, boolean paramBoolean2, int paramInt1, int paramInt2) {
    String str = (String)paramObject;
    this.textarea.setText(str);
    AffineTransform affineTransform = new AffineTransform();
    FontRenderContext fontRenderContext = new FontRenderContext(affineTransform, true, true);
    int i = (int)getFont().getStringBounds("X", fontRenderContext).getWidth();
    int j = (int)getFont().getStringBounds("X", fontRenderContext).getHeight();
    int k = (int)Math.ceil(str.length() / 120.0D);
    if (k < 1)
      k = 1; 
    int m = str.length();
    if (m < 1)
      m = 1; 
    if (m > 120)
      m = 120; 
    int n = k * j + 10;
    int i1 = m * i;
    setPreferredSize(new Dimension(i1, n));
    return this;
  }
  
  public void setForeground(Color paramColor) {
    if (this.textarea != null)
      this.textarea.setForeground(paramColor); 
  }
  
  public void setBackground(Color paramColor) {
    if (this.textarea != null)
      this.textarea.setBackground(paramColor); 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\plugin\JTextAreaCellRenderer.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */