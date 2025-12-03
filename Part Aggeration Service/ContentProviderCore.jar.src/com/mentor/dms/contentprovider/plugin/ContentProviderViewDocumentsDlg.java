package com.mentor.dms.contentprovider.plugin;

import com.mentor.dms.contentprovider.ContentProviderDocument;
import com.mentor.dms.contentprovider.ContentProviderDocumentList;
import com.mentor.dms.contentprovider.ContentProviderGlobal;
import com.mentor.dms.contentprovider.IContentProviderResultRecord;
import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.util.Vector;
import javax.swing.JDialog;
import javax.swing.JScrollPane;

public class ContentProviderViewDocumentsDlg extends JDialog {
  private ContentProviderViewDocumentsJXTable docTable;
  
  public ContentProviderViewDocumentsDlg(Frame paramFrame, ContentProviderDocumentList paramContentProviderDocumentList) {
    super(paramFrame, true);
    char c1 = '͒';
    char c2 = 'Ĭ';
    setTitle("View Documents");
    setIconImage(ContentProviderGlobal.getAppIconImage());
    Container container = getContentPane();
    Vector<String> vector = new Vector();
    vector.add("");
    vector.add("Title");
    vector.add("Type");
    vector.add("Publication Date");
    Vector<Vector> vector1 = new Vector();
    for (ContentProviderDocument contentProviderDocument : paramContentProviderDocumentList) {
      Vector<ContentProviderDocument> vector2 = new Vector();
      vector1.add(vector2);
      vector2.addElement(contentProviderDocument);
      vector2.addElement(contentProviderDocument.getTitle());
      vector2.addElement(contentProviderDocument.getType());
      vector2.addElement(contentProviderDocument.getPublicationDate());
    } 
    this.docTable = new ContentProviderViewDocumentsJXTable(new ContentProviderViewDocumentsTableModel(vector1, vector));
    JScrollPane jScrollPane = new JScrollPane((Component)this.docTable);
    container.add(jScrollPane);
    this.docTable.getColumnExt(0).setMaxWidth(20);
    this.docTable.packAll();
    setDefaultCloseOperation(2);
    pack();
    setSize(c1, c2);
    setLocationRelativeTo(paramFrame);
    setVisible(true);
  }
  
  public ContentProviderViewDocumentsDlg(Frame paramFrame, IContentProviderResultRecord paramIContentProviderResultRecord) {
    this(paramFrame, paramIContentProviderResultRecord.getDocuments());
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\plugin\ContentProviderViewDocumentsDlg.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */