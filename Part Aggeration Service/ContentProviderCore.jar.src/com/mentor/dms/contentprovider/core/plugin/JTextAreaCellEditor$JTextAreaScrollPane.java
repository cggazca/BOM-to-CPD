package com.mentor.dms.contentprovider.core.plugin;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class JTextAreaScrollPane extends JScrollPane {
  private JTextArea textarea = new JTextArea();
  
  public JTextAreaScrollPane() {
    this.textarea.setEditable(false);
    this.textarea.setFont((new JLabel()).getFont());
    this.textarea.setLineWrap(true);
    this.textarea.setWrapStyleWord(true);
    getViewport().setView(this.textarea);
  }
  
  public void setText(String paramString) {
    this.textarea.setText(paramString);
  }
  
  public String getText() {
    return this.textarea.getText();
  }
  
  public void setTextForeground(Color paramColor) {
    if (this.textarea != null)
      this.textarea.setForeground(paramColor); 
  }
  
  public void setTextBackground(Color paramColor) {
    if (this.textarea != null)
      this.textarea.setBackground(paramColor); 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\plugin\JTextAreaCellEditor$JTextAreaScrollPane.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */