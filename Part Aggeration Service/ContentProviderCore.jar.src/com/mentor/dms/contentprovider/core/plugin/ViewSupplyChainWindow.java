package com.mentor.dms.contentprovider.core.plugin;

import com.google.gson.internal.LinkedTreeMap;
import com.mentor.dms.contentprovider.core.ComponentListProperty;
import com.mentor.dms.contentprovider.core.ContentProviderGlobal;
import com.mentor.dms.contentprovider.core.ContentProviderSupplyChain;
import com.mentor.dms.contentprovider.core.ListPropertyRow;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

public class ViewSupplyChainWindow extends JFrame {
  private ViewSupplyChainJXTable compTableHeader;
  
  private ViewSupplyChainJXTable compTable;
  
  private static ViewSupplyChainWindow theWindow = null;
  
  private static Map<Map<String, String>, ViewSupplyChainWindow> mActiveWindow = new LinkedHashMap<>();
  
  protected ViewSupplyChainWindow(JFrame paramJFrame, Map<String, String> paramMap1, Collection<ContentProviderSupplyChain> paramCollection, Map<String, String> paramMap2) {
    setIconImage(ContentProviderGlobal.getAppIconImage());
    StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append(" (");
    stringBuffer.append(paramMap2.get("manufacturerName"));
    stringBuffer.append(" : ");
    stringBuffer.append(paramMap2.get("manufacturerPartNumber"));
    stringBuffer.append(")");
    setTitle("Supply Chain List" + stringBuffer.toString());
    Container container = getContentPane();
    Vector<String> vector1 = new Vector();
    vector1.addElement("");
    Vector<String> vector2 = new Vector();
    for (byte b1 = 0; b1 < paramCollection.size(); b1++)
      vector2.addElement(""); 
    JPanel jPanel1 = new JPanel();
    BoxLayout boxLayout = new BoxLayout(jPanel1, 1);
    jPanel1.setLayout(boxLayout);
    jPanel1.setBackground(Color.WHITE);
    Vector<Vector> vector = new Vector();
    Vector<Vector<SectionHeaderCell>> vector3 = new Vector();
    for (SupplyPropertyEnum supplyPropertyEnum : SupplyPropertyEnum.values()) {
      String str = supplyPropertyEnum.getId();
      Vector<SectionHeaderCell> vector4 = new Vector();
      SectionHeaderCell sectionHeaderCell = new SectionHeaderCell("  " + (String)paramMap1.get(str));
      vector4.addElement(sectionHeaderCell);
      vector3.add(vector4);
      Vector<JTable> vector5 = new Vector();
      vector.add(vector5);
      for (ContentProviderSupplyChain contentProviderSupplyChain : paramCollection) {
        LinkedHashMap linkedHashMap = contentProviderSupplyChain.getProperties();
        Object object = linkedHashMap.get(str);
        if (str.equals(SupplyPropertyEnum.PriceBreakdown.getId())) {
          List<?> list = new ArrayList();
          List list1 = (List)object;
          if (list1 != null) {
            ComponentListProperty componentListProperty = new ComponentListProperty(str, list1);
            list = componentListProperty.getList();
          } 
          Collections.sort(list, new PriceBreakdownComparator());
          int i = list.size() + 1;
          if (i == 1)
            i++; 
          byte b3 = 0;
          byte b4 = 0;
          byte b5 = 0;
          ArrayList<String> arrayList = new ArrayList();
          for (PricePropertyEnum pricePropertyEnum : PricePropertyEnum.values()) {
            if (pricePropertyEnum.isDisplay()) {
              arrayList.add(pricePropertyEnum.toString());
              b3++;
            } 
          } 
          DefaultTableModel defaultTableModel = new DefaultTableModel(i, b3 + 1);
          for (String str1 : arrayList) {
            defaultTableModel.setValueAt(str1, b4, b5);
            b5++;
          } 
          for (ListPropertyRow listPropertyRow1 : list) {
            b4++;
            b5 = 0;
            ListPropertyRow listPropertyRow2 = listPropertyRow1;
            for (PricePropertyEnum pricePropertyEnum : PricePropertyEnum.values()) {
              if (pricePropertyEnum.isDisplay()) {
                defaultTableModel.setValueAt(listPropertyRow2.getValue(pricePropertyEnum.getPropid()), b4, b5);
                b5++;
              } 
            } 
          } 
          JTable jTable = new JTable(defaultTableModel) {
              public Component prepareRenderer(TableCellRenderer param1TableCellRenderer, int param1Int1, int param1Int2) {
                Component component = super.prepareRenderer(param1TableCellRenderer, param1Int1, param1Int2);
                if (param1Int1 == 0) {
                  component.setFont(new Font("boldFont", 1, 11));
                } else {
                  component.setFont(new Font("MS Gothic", 0, 12));
                } 
                return component;
              }
            };
          for (byte b6 = 0; b6 < jTable.getColumnCount(); b6++) {
            if (b6 == 3) {
              jTable.getColumnModel().getColumn(b6).setMinWidth(15);
              jTable.getColumnModel().getColumn(b6).setMaxWidth(15);
            } 
          } 
          jTable.setShowGrid(false);
          DefaultTableCellRenderer defaultTableCellRenderer = new DefaultTableCellRenderer();
          defaultTableCellRenderer.setHorizontalAlignment(4);
          for (byte b7 = 0; b7 < b3; b7++)
            jTable.getColumnModel().getColumn(b7).setCellRenderer(defaultTableCellRenderer); 
          vector5.addElement(jTable);
          continue;
        } 
        if (object == null) {
          vector5.addElement("");
          continue;
        } 
        if (object.toString().startsWith("http")) {
          vector5.addElement(new ViewDocumentCell(object.toString()));
          continue;
        } 
        if (object instanceof LinkedTreeMap) {
          LinkedTreeMap linkedTreeMap = (LinkedTreeMap)object;
          String str1 = (String)linkedTreeMap.get("__complex__");
          if (str1 != null && str1.equals("Timestamp")) {
            str1 = (String)linkedTreeMap.get("iso8601Timestamp");
            if (str1 != null && !str1.isBlank()) {
              LocalDateTime localDateTime = ZonedDateTime.parse(str1).toLocalDateTime();
              DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
              vector5.addElement(localDateTime.format(dateTimeFormatter));
              continue;
            } 
            vector5.addElement(object.toString());
            continue;
          } 
          if (str1 != null && str1.equals("Url")) {
            vector5.addElement(new ViewDocumentCell((String)linkedTreeMap.get("value")));
            continue;
          } 
          vector5.addElement(object.toString());
          continue;
        } 
        vector5.addElement(object.toString());
      } 
    } 
    ViewSupplyChainTableModel viewSupplyChainTableModel1 = new ViewSupplyChainTableModel(vector3, vector1);
    this.compTableHeader = new ViewSupplyChainJXTable(viewSupplyChainTableModel1);
    ViewSupplyChainTableModel viewSupplyChainTableModel2 = new ViewSupplyChainTableModel(vector, vector2);
    this.compTable = new ViewSupplyChainJXTable(viewSupplyChainTableModel2);
    this.compTableHeader.getColumnExt(0).setMinWidth(180);
    this.compTableHeader.getColumnExt(0).setMaxWidth(180);
    byte b2;
    for (b2 = 0; b2 < this.compTable.getColumnCount(); b2++) {
      this.compTable.getColumnExt(b2).setMinWidth(220);
      this.compTable.getColumnExt(b2).setMaxWidth(220);
    } 
    for (b2 = 0; b2 < this.compTable.getRowCount(); b2++) {
      int i = this.compTable.getRowHeight();
      for (byte b = 0; b < this.compTable.getColumnCount(); b++) {
        Component component = this.compTable.prepareRenderer(this.compTable.getCellRenderer(b2, b), b2, b);
        i = Math.max(i, (component.getPreferredSize()).height);
      } 
      if (b2 != SupplyPropertyEnum.PriceBreakdown.ordinal() && i > 75)
        i = 75; 
      this.compTable.setRowHeight(b2, i);
      this.compTableHeader.setRowHeight(b2, i);
    } 
    jPanel1.add((Component)this.compTable);
    this.compTableHeader.setBorder(new LineBorder(Color.BLACK));
    this.compTableHeader.setAutoResizeMode(0);
    this.compTable.setBorder(new LineBorder(Color.BLACK));
    this.compTable.setAutoResizeMode(0);
    JScrollPane jScrollPane = new JScrollPane(jPanel1);
    jScrollPane.getHorizontalScrollBar().setUnitIncrement(40);
    jScrollPane.getVerticalScrollBar().setUnitIncrement(40);
    JViewport jViewport = new JViewport();
    jViewport.setView((Component)this.compTableHeader);
    jViewport.setPreferredSize(this.compTableHeader.getPreferredSize());
    jScrollPane.setRowHeader(jViewport);
    container.add(jScrollPane);
    JPanel jPanel2 = new JPanel();
    jPanel2.setBorder((Border)null);
    JButton jButton = new JButton();
    jButton.setAlignmentX(0.5F);
    jButton.setText("OK");
    jButton.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent param1ActionEvent) {
            ViewSupplyChainWindow.this.dispose();
          }
        });
    jPanel2.add(jButton);
    container.add(jPanel2, "South");
    setDefaultCloseOperation(2);
    pack();
    Dimension dimension = getPreferredSize();
    if (dimension.getWidth() > 1200.0D)
      setSize(1200, (int)dimension.getHeight() + 17); 
    setResizable(false);
  }
  
  public static void show(JFrame paramJFrame, Map<String, String> paramMap1, Collection<ContentProviderSupplyChain> paramCollection, Map<String, String> paramMap2) {
    theWindow = new ViewSupplyChainWindow(paramJFrame, paramMap1, paramCollection, paramMap2);
    if (mActiveWindow.isEmpty() || !mActiveWindow.containsKey(paramMap2)) {
      byte b = 5;
      int i = mActiveWindow.size() % b;
      int j = mActiveWindow.size() / b;
      theWindow.setLocation(100 + 50 * i + 75 * j % b, 100 + 50 * i);
    } else {
      Point point = new Point(((ViewSupplyChainWindow)mActiveWindow.get(paramMap2)).getLocation());
      if (((ViewSupplyChainWindow)mActiveWindow.get(paramMap2)).isShowing())
        ((ViewSupplyChainWindow)mActiveWindow.get(paramMap2)).dispose(); 
      theWindow.setLocation(point);
    } 
    theWindow.setVisible(true);
    mActiveWindow.put(paramMap2, theWindow);
  }
  
  public static JFrame getJFrame() {
    return (theWindow == null) ? null : theWindow;
  }
  
  public static Collection<ViewSupplyChainWindow> getAllWindow() {
    return (mActiveWindow == null) ? null : mActiveWindow.values();
  }
  
  public static void clearActiveWindow() {
    mActiveWindow.clear();
  }
  
  private class PriceBreakdownComparator implements Comparator<ListPropertyRow> {
    public int compare(ListPropertyRow param1ListPropertyRow1, ListPropertyRow param1ListPropertyRow2) {
      try {
        Object object1 = param1ListPropertyRow1.getValueObj(PricePropertyEnum.Quantity.getPropid());
        Object object2 = param1ListPropertyRow2.getValueObj(PricePropertyEnum.Quantity.getPropid());
        return (object1 instanceof Integer && object2 instanceof Integer) ? ((Integer)object1).compareTo((Integer)object2) : ((object1 instanceof Long && object2 instanceof Long) ? ((Long)object1).compareTo((Long)object2) : ((object1 instanceof SfBigDecimal && object2 instanceof SfBigDecimal) ? ((SfBigDecimal)object1).compareTo((SfBigDecimal)object2) : ((object1 instanceof Double && object2 instanceof Double) ? ((Double)object1).compareTo((Double)object2) : 0)));
      } catch (Exception exception) {
        return 0;
      } 
    }
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\plugin\ViewSupplyChainWindow.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */