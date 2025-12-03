package com.mentor.dms.contentprovider.core.plugin;

import com.mentor.dms.contentprovider.core.AbstractContentProvider;
import com.mentor.dms.contentprovider.core.ComponentListProperty;
import com.mentor.dms.contentprovider.core.ComponentProperty;
import com.mentor.dms.contentprovider.core.ContentProviderGlobal;
import com.mentor.dms.contentprovider.core.IContentProviderCustomTable;
import com.mentor.dms.contentprovider.core.IContentProviderResultRecord;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.border.Border;
import javax.swing.table.TableModel;

public class ContentProviderViewCompareWindow extends JFrame {
  private JFrame parent;
  
  private ContentProviderViewCompareJXTable compTableHeader;
  
  private ContentProviderViewCompareJXTable compTable;
  
  private static ContentProviderViewCompareWindow theWindow = null;
  
  private static Map<List<String>, ContentProviderViewCompareWindow> mActiveWindow = new LinkedHashMap<>();
  
  public static int WINDOWS_HEIGHT = 800;
  
  public static int WINDOWS_WIDTH = 840;
  
  public static int PROPARTY_NAME_WIDTH = 265;
  
  protected ContentProviderViewCompareWindow(JFrame paramJFrame, Collection<IContentProviderResultRecord> paramCollection) {
    this.parent = paramJFrame;
    setIconImage(ContentProviderGlobal.getAppIconImage());
    AbstractContentProvider abstractContentProvider = ((IContentProviderResultRecord)paramCollection.iterator().next()).getContentProvider();
    setTitle("View/Compare...");
    Container container = getContentPane();
    Vector<Vector<ImageIcon>> vector = new Vector();
    Vector<ImageIcon> vector2 = new Vector();
    Vector<ProductHeaderCell> vector3 = new Vector();
    Vector<Vector<ProductHeaderCell>> vector4 = new Vector();
    JPanel jPanel1 = new JPanel();
    BoxLayout boxLayout = new BoxLayout(jPanel1, 1);
    jPanel1.setLayout(boxLayout);
    jPanel1.setBackground(Color.WHITE);
    Vector<ProductHeaderCell> vector5 = new Vector();
    ImageIcon imageIcon = abstractContentProvider.getLogoImageIcon();
    if (imageIcon == null) {
      Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("images/DMS_Logo.png"));
      imageIcon = new ImageIcon(image);
    } 
    vector2.addElement(imageIcon);
    vector.addElement(vector2);
    boolean bool1 = false;
    boolean bool2 = false;
    boolean bool3 = false;
    boolean bool4 = false;
    boolean bool5 = false;
    boolean bool6 = false;
    boolean bool7 = false;
    double d = 0.0D;
    boolean bool8 = false;
    boolean bool9 = false;
    ArrayList<ProductHeaderCell> arrayList = new ArrayList();
    for (IContentProviderResultRecord iContentProviderResultRecord : paramCollection) {
      ProductHeaderCell productHeaderCell = new ProductHeaderCell(iContentProviderResultRecord);
      arrayList.add(productHeaderCell);
      if (productHeaderCell.isDispViewDatasheet())
        bool8 = true; 
      if (productHeaderCell.isDispNewPartRequest())
        bool9 = true; 
      vector3.addElement(productHeaderCell);
      bool1 = iContentProviderResultRecord.getContentProvider().isDocumentsSupported();
      bool3 = iContentProviderResultRecord.getContentProvider().isChangeAlertsSupported();
      bool4 = iContentProviderResultRecord.getContentProvider().isFailureAlertsSupported();
      bool5 = iContentProviderResultRecord.getContentProvider().isPartStatusChangeAlertSupported();
      bool6 = iContentProviderResultRecord.getContentProvider().isEndOfLifeAlertSupported();
      bool2 = (bool3 || bool4 || bool5 || bool6) ? true : false;
      bool7 = iContentProviderResultRecord.getContentProvider().isSuggestedAlternatesSupported();
    } 
    for (ProductHeaderCell productHeaderCell : arrayList) {
      if (bool8)
        productHeaderCell.dispDummyViewDatasheet(); 
      if (bool9)
        productHeaderCell.dispNewPartRequest(); 
      if (productHeaderCell.getPreferredSize().getHeight() > d)
        d = productHeaderCell.getPreferredSize().getHeight(); 
    } 
    for (Object object : vector5.toArray()) {
      if (object instanceof ProductHeaderCell) {
        ProductHeaderCell productHeaderCell = (ProductHeaderCell)object;
        Dimension dimension1 = productHeaderCell.getPreferredSize();
        dimension1.setSize(dimension1.getWidth(), d);
      } 
    } 
    vector3.addElement("Compare");
    vector4.addElement(vector3);
    LinkedHashMap<Object, Object> linkedHashMap1 = new LinkedHashMap<>();
    LinkedHashMap<Object, Object> linkedHashMap2 = new LinkedHashMap<>();
    for (IContentProviderResultRecord iContentProviderResultRecord : paramCollection) {
      for (String str : iContentProviderResultRecord.getPartPropertyCategories()) {
        Map<Object, Object> map = (Map)linkedHashMap2.get(str);
        if (map == null) {
          map = new LinkedHashMap<>();
          linkedHashMap2.put(str, map);
        } 
        for (ComponentProperty componentProperty : iContentProviderResultRecord.getPartPropertiesByCategory(str)) {
          if (componentProperty.isVisibleInViewCompare())
            map.put(componentProperty.getId(), componentProperty); 
        } 
      } 
    } 
    LinkedList<?> linkedList = new LinkedList(linkedHashMap2.entrySet());
    Collections.sort(linkedList, (paramEntry1, paramEntry2) -> ((String)paramEntry1.getKey()).equals("List") ? -1 : 1);
    for (Map.Entry entry : linkedList)
      linkedHashMap1.put(entry.getKey(), entry.getValue()); 
    SectionHeaderCell sectionHeaderCell = new SectionHeaderCell("Classification");
    vector5 = new Vector();
    vector4.add(vector5);
    Vector<SectionHeaderCell> vector1 = new Vector();
    vector1.add(sectionHeaderCell);
    vector.addElement(vector1);
    for (IContentProviderResultRecord iContentProviderResultRecord : paramCollection)
      vector5.addElement(iContentProviderResultRecord.getPartClassName()); 
    Comparator<ComponentProperty> comparator = new Comparator<ComponentProperty>() {
        public int compare(ComponentProperty param1ComponentProperty1, ComponentProperty param1ComponentProperty2) {
          return param1ComponentProperty1.getLabel().compareTo(param1ComponentProperty2.getLabel());
        }
      };
    if (bool7) {
      sectionHeaderCell = new SectionHeaderCell("Suggested Alternates");
      vector5 = new Vector<>();
      vector4.add(vector5);
      vector1 = new Vector<>();
      vector1.add(sectionHeaderCell);
      vector.addElement(vector1);
      for (IContentProviderResultRecord iContentProviderResultRecord : paramCollection)
        vector5.addElement(new ViewAlternatesCell(iContentProviderResultRecord)); 
    } 
    for (String str : linkedHashMap1.keySet()) {
      sectionHeaderCell = new SectionHeaderCell(str);
      vector5 = new Vector<>();
      vector4.add(vector5);
      vector1 = new Vector<>();
      vector1.add(sectionHeaderCell);
      vector.addElement(vector1);
      for (byte b = 0; b < paramCollection.size(); b++)
        vector5.addElement(""); 
      ArrayList<ComponentProperty> arrayList1 = new ArrayList(((Map)linkedHashMap1.get(str)).values());
      Collections.sort(arrayList1, comparator);
      for (ComponentProperty componentProperty1 : arrayList1) {
        vector5 = new Vector<>();
        vector4.add(vector5);
        vector1 = new Vector<>();
        vector1.add(new PropertyIdSectionHeaderCell("    " + componentProperty1.getLabel()));
        vector.addElement(vector1);
        ComponentProperty componentProperty2 = null;
        for (IContentProviderResultRecord iContentProviderResultRecord : paramCollection) {
          componentProperty2 = iContentProviderResultRecord.getPartProperty(componentProperty1.getId());
          boolean bool10 = true;
          if (componentProperty2 != null && !componentProperty2.isListProperty() && !componentProperty2.isDocumentURL())
            bool10 = false; 
          if (componentProperty2 != null) {
            if (componentProperty2.isListProperty()) {
              ComponentListProperty componentListProperty = (ComponentListProperty)componentProperty2;
              vector5.addElement(new ViewListPropertyCell(iContentProviderResultRecord, componentListProperty, componentListProperty.getLabel()));
              continue;
            } 
            if (bool10) {
              vector5.addElement(new ViewDocumentCell(componentProperty2.getValue()));
              continue;
            } 
            vector5.addElement(componentProperty2.getValueWithOUM());
            continue;
          } 
          vector5.addElement("** N/A **");
        } 
      } 
      boolean bool = false;
      for (IContentProviderResultRecord iContentProviderResultRecord : paramCollection) {
        if (iContentProviderResultRecord.getCategoryDocuments(str) != null) {
          vector5 = new Vector<>();
          vector4.add(vector5);
          vector1 = new Vector<>();
          vector1.add(new SectionHeaderCell(str + " Documents"));
          vector.addElement(vector1);
          bool = true;
          break;
        } 
      } 
      if (bool)
        for (IContentProviderResultRecord iContentProviderResultRecord : paramCollection)
          vector5.addElement(new ViewDocumentsCell(iContentProviderResultRecord, iContentProviderResultRecord.getCategoryDocuments(str)));  
    } 
    for (byte b1 = 1; b1 < vector4.size(); b1++) {
      Vector<Object> vector6 = (Vector)vector4.get(b1);
      HashSet<String> hashSet = new HashSet();
      boolean bool = false;
      byte b;
      for (b = 0; b < vector6.size(); b++) {
        String str = (String)vector6.get(b);
        if (str instanceof String) {
          hashSet.add(str);
        } else if (str instanceof ContentProviderViewCompareJTextArea) {
          ContentProviderViewCompareJTextArea contentProviderViewCompareJTextArea = (ContentProviderViewCompareJTextArea)vector6.get(b);
          hashSet.add(contentProviderViewCompareJTextArea.getText());
        } else if (str instanceof ViewDocumentCell) {
          bool = true;
        } 
      } 
      vector6.addElement(new Boolean((hashSet.size() > 1)));
      if (bool)
        for (b = 0; b < vector6.size(); b++) {
          Object object = vector6.get(b);
          if (!(object instanceof ViewDocumentCell))
            vector6.set(b, new ViewDocumentCell("")); 
        }  
    } 
    LinkedHashSet<String> linkedHashSet = new LinkedHashSet();
    for (IContentProviderResultRecord iContentProviderResultRecord : paramCollection) {
      for (IContentProviderCustomTable iContentProviderCustomTable : iContentProviderResultRecord.getCustomTables()) {
        if (!linkedHashSet.contains(iContentProviderCustomTable.getCategoryLabel()))
          linkedHashSet.add(iContentProviderCustomTable.getCategoryLabel()); 
      } 
    } 
    for (String str : linkedHashSet) {
      sectionHeaderCell = new SectionHeaderCell(str);
      vector5 = new Vector<>();
      vector4.add(vector5);
      vector1 = new Vector<>();
      vector1.add(sectionHeaderCell);
      vector.addElement(vector1);
      for (IContentProviderResultRecord iContentProviderResultRecord : paramCollection) {
        boolean bool = false;
        for (IContentProviderCustomTable iContentProviderCustomTable : iContentProviderResultRecord.getCustomTables()) {
          if (iContentProviderCustomTable.getCategoryLabel().equals(str)) {
            vector5.addElement(new ViewCustomTableCell(iContentProviderResultRecord, iContentProviderCustomTable));
            bool = true;
            break;
          } 
        } 
        if (!bool)
          vector5.addElement(new ViewCustomTableCell(iContentProviderResultRecord, str)); 
      } 
    } 
    if (bool1) {
      sectionHeaderCell = new SectionHeaderCell("Documents");
      vector5 = new Vector<>();
      vector4.add(vector5);
      vector1 = new Vector<>();
      vector1.add(sectionHeaderCell);
      vector.addElement(vector1);
      for (IContentProviderResultRecord iContentProviderResultRecord : paramCollection)
        vector5.addElement(new ViewDocumentsCell(iContentProviderResultRecord, iContentProviderResultRecord.getDocuments())); 
    } 
    if (bool2) {
      sectionHeaderCell = new SectionHeaderCell("Alerts");
      vector5 = new Vector<>();
      vector4.add(vector5);
      vector1 = new Vector<>();
      vector1.add(sectionHeaderCell);
      vector.addElement(vector1);
      for (IContentProviderResultRecord iContentProviderResultRecord : paramCollection)
        vector5.addElement(new ViewAlertsCell(iContentProviderResultRecord)); 
    } 
    ContentProviderViewCompareTableModel contentProviderViewCompareTableModel1 = new ContentProviderViewCompareTableModel(vector, vector2);
    this.compTableHeader = new ContentProviderViewCompareJXTable(contentProviderViewCompareTableModel1, abstractContentProvider);
    ContentProviderViewCompareTableModel contentProviderViewCompareTableModel2 = new ContentProviderViewCompareTableModel(vector4, vector3);
    this.compTable = new ContentProviderViewCompareJXTable(contentProviderViewCompareTableModel2, abstractContentProvider);
    this.compTableHeader.getColumnExt(0).setMinWidth(PROPARTY_NAME_WIDTH);
    this.compTableHeader.getColumnExt(0).setMaxWidth(PROPARTY_NAME_WIDTH);
    byte b2;
    for (b2 = 0; b2 < this.compTable.getColumnCount(); b2++)
      this.compTable.getColumnExt(b2).setMinWidth(200); 
    this.compTable.getColumnExt(this.compTable.getColumnCount() - 1).setVisible(false);
    for (b2 = 0; b2 < this.compTable.getRowCount(); b2++) {
      int j = this.compTable.getRowHeight();
      for (byte b = 0; b < this.compTable.getColumnCount(); b++) {
        Component component = this.compTable.prepareRenderer(this.compTable.getCellRenderer(b2, b), b2, b);
        j = Math.max(j, (component.getPreferredSize()).height);
      } 
      this.compTable.setRowHeight(b2, j);
      this.compTableHeader.setRowHeight(b2, j);
    } 
    jPanel1.add((Component)this.compTable);
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
            for (Window window : ViewListPropertiesWindow.getlAllWindow()) {
              if (window != null)
                window.dispose(); 
            } 
            for (Window window : ContentProviderViewAlternatesWindow.getlAllWindow()) {
              if (window != null)
                window.dispose(); 
            } 
            ContentProviderViewCompareWindow.this.dispose();
          }
        });
    jPanel2.add(jButton);
    container.add(jPanel2, "South");
    setDefaultCloseOperation(2);
    addWindowListener(new WindowAdapter() {
          public void windowClosing(WindowEvent param1WindowEvent) {
            for (Window window : ViewListPropertiesWindow.getlAllWindow()) {
              if (window != null)
                window.dispose(); 
            } 
            for (Window window : ContentProviderViewAlternatesWindow.getlAllWindow()) {
              if (window != null)
                window.dispose(); 
            } 
          }
        });
    pack();
    Dimension dimension = new Dimension(WINDOWS_WIDTH, WINDOWS_HEIGHT);
    int i = this.compTableHeader.getWidth() + this.compTable.getWidth() + this.compTable.getColumnCount() * 12;
    if (i > 1500) {
      setSize(new Dimension(1500, WINDOWS_HEIGHT));
    } else {
      setSize(new Dimension(i, WINDOWS_HEIGHT));
    } 
    setMinimumSize(dimension);
    setLocationRelativeTo(paramJFrame);
    setVisible(true);
  }
  
  public static void show(JFrame paramJFrame, Collection<IContentProviderResultRecord> paramCollection) {
    if (theWindow != null)
      theWindow.dispose(); 
    theWindow = new ContentProviderViewCompareWindow(paramJFrame, paramCollection);
  }
  
  private static boolean exsistCheck(Set<List<String>> paramSet, List<String> paramList) {
    ArrayList<List<String>> arrayList = new ArrayList<>(paramSet);
    for (List<String> list : arrayList) {
      if (list.containsAll(paramList))
        return true; 
    } 
    return false;
  }
  
  public static JFrame getJFrame() {
    return (theWindow == null) ? null : theWindow;
  }
  
  public Collection<ContentProviderViewCompareWindow> getAllWindow() {
    return (mActiveWindow == null) ? null : mActiveWindow.values();
  }
  
  public void clearActiveWindow() {
    mActiveWindow.clear();
  }
  
  public static ViewCompareDataForExport getCurrentData() {
    byte b1 = 1;
    TableModel tableModel1 = theWindow.compTableHeader.getModel();
    TableModel tableModel2 = theWindow.compTable.getModel();
    int i = tableModel2.getColumnCount() - 1;
    int j = tableModel2.getRowCount();
    ViewCompareDataForExport viewCompareDataForExport = new ViewCompareDataForExport(j + 1, i);
    String[] arrayOfString = new String[j];
    byte b2;
    for (b2 = 1; b2 < j; b2++) {
      Object object = tableModel1.getValueAt(b2, 0);
      arrayOfString[b2 - 1] = object.toString();
    } 
    viewCompareDataForExport.set(0, arrayOfString);
    for (b2 = 0; b2 < i; b2++) {
      String[] arrayOfString1 = new String[j];
      for (byte b = 1; b < j; b++) {
        Object object = tableModel2.getValueAt(b, b2);
        if (object instanceof ViewListPropertyCell) {
          String[][] arrayOfString2 = ((ViewListPropertyCell)object).getTable();
          if (arrayOfString2 != null) {
            viewCompareDataForExport.addListProperty("List" + b1, arrayOfString2);
            arrayOfString1[b - 1] = "List" + b1++;
          } else {
            arrayOfString1[b - 1] = "";
          } 
        } else {
          arrayOfString1[b - 1] = object.toString();
        } 
      } 
      viewCompareDataForExport.set(b2 + 1, arrayOfString1);
    } 
    return viewCompareDataForExport;
  }
  
  public static List<List<String>> getCurrentDataBK2() {
    HashMap<Object, Object> hashMap = new HashMap<>();
    byte b1 = 1;
    TableModel tableModel1 = theWindow.compTableHeader.getModel();
    TableModel tableModel2 = theWindow.compTable.getModel();
    int i = tableModel2.getColumnCount() - 1;
    int j = tableModel2.getRowCount();
    ArrayList<ArrayList<String>> arrayList = new ArrayList();
    ArrayList<String> arrayList1 = new ArrayList();
    byte b2;
    for (b2 = 1; b2 < j; b2++) {
      Object object = tableModel1.getValueAt(b2, 0);
      arrayList1.add(object.toString());
    } 
    arrayList.add(arrayList1);
    for (b2 = 0; b2 < i; b2++) {
      ArrayList<String> arrayList2 = new ArrayList();
      for (byte b = 1; b < j; b++) {
        Object object = tableModel2.getValueAt(b, b2);
        if (object instanceof ViewListPropertyCell) {
          hashMap.put("List" + b1, ((ViewListPropertyCell)object).getTable());
          arrayList2.add("List" + b1++);
        } else {
          arrayList2.add(object.toString());
        } 
      } 
      arrayList.add(arrayList2);
    } 
    for (b2 = 1; b2 < b1 + 1; b2++) {
      String[][] arrayOfString = (String[][])hashMap.get("List" + b2);
      if (arrayOfString == null) {
        System.out.println("Not found \"List" + b2 + "\"");
      } else {
        ArrayList<String> arrayList2 = new ArrayList();
        arrayList2.add("List" + b2);
        arrayList.add(arrayList2);
        for (String[] arrayOfString1 : arrayOfString) {
          arrayList2 = new ArrayList<>();
          for (String str : arrayOfString1)
            arrayList2.add(str); 
          arrayList.add(arrayList2);
        } 
      } 
    } 
    return (List)arrayList;
  }
  
  public static String[][] getCurrentDataBK() {
    TableModel tableModel1 = theWindow.compTableHeader.getModel();
    TableModel tableModel2 = theWindow.compTable.getModel();
    int i = tableModel2.getColumnCount() - 1;
    int j = tableModel2.getRowCount();
    String[][] arrayOfString = new String[j][i + 1];
    byte b;
    for (b = 0; b < j; b++) {
      Object object = tableModel1.getValueAt(b, 0);
      arrayOfString[b][0] = object.toString();
    } 
    for (b = 0; b < j; b++) {
      for (byte b1 = 0; b1 < i; b1++) {
        Object object = tableModel2.getValueAt(b, b1);
        if (object instanceof ViewListPropertyCell) {
          ((ViewListPropertyCell)object).getTable();
          arrayOfString[b][b1 + 1] = object.toString();
        } else {
          arrayOfString[b][b1 + 1] = object.toString();
        } 
      } 
    } 
    return arrayOfString;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\plugin\ContentProviderViewCompareWindow.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */