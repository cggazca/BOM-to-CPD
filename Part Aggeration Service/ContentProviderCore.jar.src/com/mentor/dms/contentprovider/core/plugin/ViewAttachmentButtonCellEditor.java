package com.mentor.dms.contentprovider.core.plugin;

import com.mentor.datafusion.utils.logger.MGLogger;
import com.mentor.dms.contentprovider.core.ContentProviderDocument;
import com.mentor.dms.contentprovider.core.ContentProviderDocumentList;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

public class ViewAttachmentButtonCellEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
  private static MGLogger log = MGLogger.getLogger(ViewAttachmentButtonCellEditor.class);
  
  private Object docObj;
  
  private ViewAttachmentButton button = new ViewAttachmentButton();
  
  public ViewAttachmentButtonCellEditor() {
    this.button.addActionListener(this);
  }
  
  public Component getTableCellEditorComponent(JTable paramJTable, Object paramObject, boolean paramBoolean, int paramInt1, int paramInt2) {
    this.docObj = paramObject;
    return this.button;
  }
  
  public Object getCellEditorValue() {
    return null;
  }
  
  public void actionPerformed(ActionEvent paramActionEvent) {
    if (paramActionEvent.getActionCommand().equals("ViewAttachment"))
      try {
        if (this.docObj instanceof ContentProviderDocumentList) {
          ContentProviderDocumentViewer.viewDocument(this.button, (ContentProviderDocumentList)this.docObj);
        } else if (this.docObj instanceof ContentProviderDocument) {
          ContentProviderDocumentViewer.viewDocument(this.button, (ContentProviderDocument)this.docObj);
        } 
      } catch (Exception exception) {
        log.error("Error viewing document(s): " + exception.getMessage());
      }  
    cancelCellEditing();
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\plugin\ViewAttachmentButtonCellEditor.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */