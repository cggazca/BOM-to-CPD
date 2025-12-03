package com.mentor.dms.contentprovider.core.plugin;

import com.mentor.dms.contentprovider.core.ContentProviderAlternate;
import com.mentor.dms.contentprovider.core.ContentProviderGlobal;
import com.mentor.dms.contentprovider.core.IContentProviderResultRecord;
import com.mentor.dms.contentprovider.core.utils.ListViewProperties;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import org.jdesktop.swingx.table.TableColumnExt;

public class ContentProviderViewAlternatesWindow extends JFrame {
  private ContentProviderViewAlternatesJXTable altsTable;
  
  private static ContentProviderViewAlternatesWindow theWindow = null;
  
  private static Map<String, ContentProviderViewAlternatesWindow> mActiveWindow = new LinkedHashMap<>();
  
  public ContentProviderViewAlternatesWindow(JFrame paramJFrame, IContentProviderResultRecord paramIContentProviderResultRecord) {
    List list = ListViewProperties.getPropertyCSV("Alternates");
    List<String> list1 = ListViewProperties.getPropertyCSV("Alternates_type");
    int i = ListViewProperties.getIntProperty("Alternates_width", 850);
    int j = ListViewProperties.getIntProperty("Alternates_height", 300);
    StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append(" (");
    stringBuffer.append(paramIContentProviderResultRecord.getManufacturerName());
    stringBuffer.append(" : ");
    stringBuffer.append(paramIContentProviderResultRecord.getPartNumber());
    stringBuffer.append(")");
    Dimension dimension = new Dimension(i, j);
    setTitle("View Suggested Alternates" + String.valueOf(stringBuffer));
    setIconImage(ContentProviderGlobal.getAppIconImage());
    addWindowFocusListener(new WindowAdapter() {
          public void windowGainedFocus(WindowEvent param1WindowEvent) {
            if (ContentProviderViewAlternatesWindow.this.altsTable != null && ContentProviderViewAlternatesWindow.this.altsTable.getCellEditor() != null)
              ContentProviderViewAlternatesWindow.this.altsTable.getCellEditor().cancelCellEditing(); 
          }
        });
    Container container = getContentPane();
    Vector<String> vector = new Vector();
    vector.add("");
    vector.add("Base Number");
    vector.add("Manufacturer Name");
    vector.add("Manufacturer Part Number");
    vector.add("Part Life Cycle Code");
    vector.add("Description");
    Vector<Vector> vector1 = new Vector();
    for (ContentProviderAlternate contentProviderAlternate : paramIContentProviderResultRecord.getAlternates()) {
      Vector<String> vector2 = new Vector();
      vector1.add(vector2);
      vector2.addElement(contentProviderAlternate.getDatasheetUrl());
      vector2.addElement(contentProviderAlternate.getBaseNumber());
      vector2.addElement(contentProviderAlternate.getManufacturerName());
      vector2.addElement(contentProviderAlternate.getPartNumber());
      vector2.addElement(contentProviderAlternate.getLifeCycleCode());
      vector2.addElement(contentProviderAlternate.getDescription());
      vector2.addElement(contentProviderAlternate.getMfrid());
      vector2.addElement(contentProviderAlternate.getUid());
    } 
    this.altsTable = new ContentProviderViewAlternatesJXTable(new ContentProviderViewAlternatesTableModel(vector1, vector, list1));
    JScrollPane jScrollPane = new JScrollPane((Component)this.altsTable);
    container.add(jScrollPane);
    JPanel jPanel = new JPanel();
    jPanel.setBorder((Border)null);
    JButton jButton = new JButton();
    jButton.setAlignmentX(0.5F);
    jButton.setText("OK");
    jButton.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent param1ActionEvent) {
            ContentProviderViewAlternatesWindow.this.dispose();
          }
        });
    jPanel.add(jButton);
    container.add(jPanel, "South");
    this.altsTable.packAll();
    for (byte b = 0; b < list1.size(); b++) {
      if (((String)list1.get(b)).equals("URL")) {
        TableColumnExt tableColumnExt = this.altsTable.getColumnExt(b);
        tableColumnExt.setMaxWidth(32);
      } 
    } 
    setDefaultCloseOperation(2);
    pack();
    setSize(dimension);
    setMinimumSize(dimension);
    setLocationRelativeTo(paramJFrame);
    setVisible(true);
  }
  
  public static void show(JFrame paramJFrame, IContentProviderResultRecord paramIContentProviderResultRecord) {
    StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append(paramIContentProviderResultRecord.getPartProperty("6230417e").getValue());
    stringBuffer.append(paramIContentProviderResultRecord.getPartProperty("d8ac8dcc").getValue());
    theWindow = new ContentProviderViewAlternatesWindow(paramJFrame, paramIContentProviderResultRecord);
    if (mActiveWindow.isEmpty() || !mActiveWindow.containsKey(stringBuffer.toString())) {
      byte b = 5;
      int i = mActiveWindow.size() % b;
      int j = mActiveWindow.size() / b;
      theWindow.setLocation(100 + 50 * i + 75 * j % b, 100 + 50 * i);
    } else {
      Point point = new Point(((ContentProviderViewAlternatesWindow)mActiveWindow.get(stringBuffer.toString())).getLocation());
      if (((ContentProviderViewAlternatesWindow)mActiveWindow.get(stringBuffer.toString())).isShowing())
        ((ContentProviderViewAlternatesWindow)mActiveWindow.get(stringBuffer.toString())).dispose(); 
      theWindow.setLocation(point);
    } 
    theWindow.setVisible(true);
    mActiveWindow.put(stringBuffer.toString(), theWindow);
  }
  
  public static Collection<ContentProviderViewAlternatesWindow> getlAllWindow() {
    return mActiveWindow.values();
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\plugin\ContentProviderViewAlternatesWindow.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */