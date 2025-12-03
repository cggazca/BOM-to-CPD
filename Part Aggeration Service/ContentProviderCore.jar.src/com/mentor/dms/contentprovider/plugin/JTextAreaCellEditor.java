package com.mentor.dms.contentprovider.plugin;

import java.awt.Color;
import java.awt.Component;
import java.util.EventObject;
import javax.swing.AbstractCellEditor;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.table.TableCellEditor;

class JTextAreaCellEditor extends AbstractCellEditor implements TableCellEditor {
  JTextAreaScrollPane scrollPane = new JTextAreaScrollPane();
  
  public Component getTableCellEditorComponent(JTable paramJTable, Object paramObject, boolean paramBoolean, int paramInt1, int paramInt2) {
    this.scrollPane.setText((String)paramObject);
    UIDefaults uIDefaults = UIManager.getDefaults();
    this.scrollPane.setTextForeground(uIDefaults.getColor("TextField.foreground"));
    this.scrollPane.setTextBackground(uIDefaults.getColor("TextField.inactiveBackground"));
    return this.scrollPane;
  }
  
  public Object getCellEditorValue() {
    return this.scrollPane.getText();
  }
  
  public boolean isCellEditable(EventObject paramEventObject) {
    return true;
  }
  
  public boolean shouldSelectCell(EventObject paramEventObject) {
    return false;
  }
  
  public boolean stopCellEditing() {
    fireEditingStopped();
    return true;
  }
  
  public class JTextAreaScrollPane extends JScrollPane {
    private JTextArea textarea = new JTextArea();
    
    public JTextAreaScrollPane() {
      this.textarea.setEditable(false);
      this.textarea.setFont((new JLabel()).getFont());
      this.textarea.setLineWrap(true);
      this.textarea.setWrapStyleWord(true);
      getViewport().setView(this.textarea);
    }
    
    public void setText(String param1String) {
      this.textarea.setText(param1String);
    }
    
    public String getText() {
      return this.textarea.getText();
    }
    
    public void setTextForeground(Color param1Color) {
      if (this.textarea != null)
        this.textarea.setForeground(param1Color); 
    }
    
    public void setTextBackground(Color param1Color) {
      if (this.textarea != null)
        this.textarea.setBackground(param1Color); 
    }
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\plugin\JTextAreaCellEditor.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */