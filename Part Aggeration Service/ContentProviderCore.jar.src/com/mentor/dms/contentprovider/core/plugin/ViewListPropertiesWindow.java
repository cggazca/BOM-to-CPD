package com.mentor.dms.contentprovider.core.plugin;

import com.mentor.dms.contentprovider.core.ComponentListProperty;
import com.mentor.dms.contentprovider.core.ContentProviderGlobal;
import com.mentor.dms.contentprovider.core.IContentProviderResultRecord;
import com.mentor.dms.contentprovider.core.ListPropertyRow;
import com.mentor.dms.contentprovider.core.utils.DateUtils;
import com.mentor.dms.contentprovider.core.utils.ListViewProperties;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
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

public class ViewListPropertiesWindow extends JFrame {
  private ContentProviderViewAlternatesJXTable altsTable;
  
  private static ViewListPropertiesWindow theWindow = null;
  
  private static Map<String, ViewListPropertiesWindow> mActiveWindow = new LinkedHashMap<>();
  
  public ViewListPropertiesWindow(Frame paramFrame, ComponentListProperty paramComponentListProperty, IContentProviderResultRecord paramIContentProviderResultRecord) {
    List<String> list1 = ListViewProperties.getPropertyCSV(paramComponentListProperty.getId());
    List<String> list2 = ListViewProperties.getPropertyCSV(paramComponentListProperty.getId() + "_type");
    int i = ListViewProperties.getIntProperty(paramComponentListProperty.getId() + "_width", 1024);
    int j = ListViewProperties.getIntProperty(paramComponentListProperty.getId() + "_height", 300);
    StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append(" (");
    stringBuffer.append(paramIContentProviderResultRecord.getManufacturerName());
    stringBuffer.append(" : ");
    stringBuffer.append(paramIContentProviderResultRecord.getPartNumber());
    stringBuffer.append(")");
    Dimension dimension = new Dimension(i, j);
    setTitle("View " + paramComponentListProperty.getLabel() + String.valueOf(stringBuffer));
    setIconImage(ContentProviderGlobal.getAppIconImage());
    addWindowFocusListener(new WindowAdapter() {
          public void windowGainedFocus(WindowEvent param1WindowEvent) {
            if (ViewListPropertiesWindow.this.altsTable != null && ViewListPropertiesWindow.this.altsTable.getCellEditor() != null)
              ViewListPropertiesWindow.this.altsTable.getCellEditor().cancelCellEditing(); 
          }
        });
    Container container = getContentPane();
    Vector<String> vector = new Vector();
    for (byte b1 = 0; b1 < list1.size(); b1++) {
      if (((String)list2.get(b1)).equals("URL")) {
        vector.add("");
      } else {
        vector.add(list1.get(b1));
      } 
    } 
    Vector<Vector> vector1 = new Vector();
    List list = paramComponentListProperty.getList();
    for (ListPropertyRow listPropertyRow : list) {
      Vector<String> vector2 = new Vector();
      vector1.add(vector2);
      for (byte b = 0; b < list1.size(); b++) {
        String str = listPropertyRow.getValue(list1.get(b));
        vector2.addElement(str);
      } 
    } 
    this.altsTable = new ContentProviderViewAlternatesJXTable(new ContentProviderViewAlternatesTableModel(vector1, vector, list2));
    JScrollPane jScrollPane = new JScrollPane((Component)this.altsTable);
    container.add(jScrollPane);
    JPanel jPanel = new JPanel();
    jPanel.setBorder((Border)null);
    JButton jButton = new JButton();
    jButton.setAlignmentX(0.5F);
    jButton.setText("OK");
    jButton.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent param1ActionEvent) {
            ViewListPropertiesWindow.this.dispose();
          }
        });
    jPanel.add(jButton);
    container.add(jPanel, "South");
    this.altsTable.packAll();
    for (byte b2 = 0; b2 < list2.size(); b2++) {
      if (((String)list2.get(b2)).equals("URL")) {
        TableColumnExt tableColumnExt = this.altsTable.getColumnExt(b2);
        tableColumnExt.setMaxWidth(32);
      } 
    } 
    setDefaultCloseOperation(2);
    pack();
    setSize(dimension);
    setMinimumSize(dimension);
    setLocationRelativeTo(paramFrame);
    setVisible(true);
  }
  
  public static void show(Frame paramFrame, ComponentListProperty paramComponentListProperty, IContentProviderResultRecord paramIContentProviderResultRecord) {
    StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append(paramComponentListProperty.getLabel());
    stringBuffer.append(paramIContentProviderResultRecord.getManufacturerName());
    stringBuffer.append(paramIContentProviderResultRecord.getPartNumber());
    theWindow = new ViewListPropertiesWindow(paramFrame, paramComponentListProperty, paramIContentProviderResultRecord);
    if (mActiveWindow.isEmpty() || !mActiveWindow.containsKey(stringBuffer.toString())) {
      byte b = 5;
      int i = mActiveWindow.size() % b;
      int j = mActiveWindow.size() / b;
      theWindow.setLocation(100 + 50 * i + 75 * j % b, 100 + 50 * i);
    } else {
      Point point = new Point(((ViewListPropertiesWindow)mActiveWindow.get(stringBuffer.toString())).getLocation());
      if (((ViewListPropertiesWindow)mActiveWindow.get(stringBuffer.toString())).isShowing())
        ((ViewListPropertiesWindow)mActiveWindow.get(stringBuffer.toString())).dispose(); 
      theWindow.setLocation(point);
    } 
    theWindow.setVisible(true);
    mActiveWindow.put(stringBuffer.toString(), theWindow);
  }
  
  public static Collection<ViewListPropertiesWindow> getlAllWindow() {
    return mActiveWindow.values();
  }
  
  private String convertValue(Object paramObject, String paramString) {
    return paramString.equals("TIME") ? DateUtils.toStringTime((Map)paramObject) : String.valueOf(paramObject);
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\plugin\ViewListPropertiesWindow.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */