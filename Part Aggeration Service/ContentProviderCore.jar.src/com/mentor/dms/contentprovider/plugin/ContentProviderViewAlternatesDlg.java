package com.mentor.dms.contentprovider.plugin;

import com.mentor.dms.contentprovider.ContentProviderAlternate;
import com.mentor.dms.contentprovider.ContentProviderGlobal;
import com.mentor.dms.contentprovider.IContentProviderResultRecord;
import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.util.Vector;
import javax.swing.JDialog;
import javax.swing.JScrollPane;

public class ContentProviderViewAlternatesDlg extends JDialog {
  private ContentProviderViewAlternatesJXTable altsTable;
  
  public ContentProviderViewAlternatesDlg(Frame paramFrame, IContentProviderResultRecord paramIContentProviderResultRecord) {
    super(paramFrame, true);
    char c1 = '͒';
    char c2 = 'Ĭ';
    setTitle("View Suggested Alternates");
    setIconImage(ContentProviderGlobal.getAppIconImage());
    Container container = getContentPane();
    Vector<String> vector = new Vector();
    vector.add("Part Number");
    vector.add("Manufacturer Name");
    vector.add("Alternate Type");
    vector.add("Alternate Part Information Source");
    vector.add("Manufacturer Cross Ref Type");
    vector.add("Comments");
    Vector<Vector> vector1 = new Vector();
    for (ContentProviderAlternate contentProviderAlternate : paramIContentProviderResultRecord.getAlternates()) {
      Vector<String> vector2 = new Vector();
      vector1.add(vector2);
      vector2.addElement(contentProviderAlternate.getPartNumber());
      vector2.addElement(contentProviderAlternate.getManufacturerName());
      vector2.addElement(contentProviderAlternate.getAlternateType());
      vector2.addElement(contentProviderAlternate.getPartInformationSource());
      vector2.addElement(contentProviderAlternate.getManufacturerCrossRefType());
      vector2.addElement(contentProviderAlternate.getComments());
    } 
    this.altsTable = new ContentProviderViewAlternatesJXTable(new ContentProviderViewAlternatesTableModel(vector1, vector));
    JScrollPane jScrollPane = new JScrollPane((Component)this.altsTable);
    container.add(jScrollPane);
    this.altsTable.packAll();
    setDefaultCloseOperation(2);
    pack();
    setSize(c1, c2);
    setLocationRelativeTo(paramFrame);
    setVisible(true);
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\plugin\ContentProviderViewAlternatesDlg.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */