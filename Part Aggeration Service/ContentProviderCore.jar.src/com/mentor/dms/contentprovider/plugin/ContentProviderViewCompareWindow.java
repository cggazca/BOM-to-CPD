package com.mentor.dms.contentprovider.plugin;

import com.mentor.dms.contentprovider.AbstractContentProvider;
import com.mentor.dms.contentprovider.ComponentProperty;
import com.mentor.dms.contentprovider.ContentProviderGlobal;
import com.mentor.dms.contentprovider.IContentProviderCustomTable;
import com.mentor.dms.contentprovider.IContentProviderResultRecord;
import java.awt.Component;
import java.awt.Container;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

public class ContentProviderViewCompareWindow extends JFrame {
  private JFrame parent;
  
  private ContentProviderViewCompareJXTable compTable;
  
  private static ContentProviderViewCompareWindow theWindow = null;
  
  protected ContentProviderViewCompareWindow(JFrame paramJFrame, Collection<IContentProviderResultRecord> paramCollection) {
    this.parent = paramJFrame;
    setIconImage(ContentProviderGlobal.getAppIconImage());
    AbstractContentProvider abstractContentProvider = ((IContentProviderResultRecord)paramCollection.iterator().next()).getContentProvider();
    setTitle("View/Compare...");
    Container container = getContentPane();
    Vector<Vector> vector = new Vector();
    Vector<ImageIcon> vector1 = new Vector();
    vector.add(vector1);
    ImageIcon imageIcon = abstractContentProvider.getLogoImageIcon();
    if (imageIcon == null) {
      Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("images/DMS_Logo.png"));
      imageIcon = new ImageIcon(image);
    } 
    vector1.addElement(imageIcon);
    boolean bool1 = false;
    boolean bool = false;
    boolean bool2 = false;
    boolean bool3 = false;
    boolean bool4 = false;
    boolean bool5 = false;
    boolean bool6 = false;
    for (IContentProviderResultRecord iContentProviderResultRecord : paramCollection) {
      vector1.addElement(new ProductHeaderCell(iContentProviderResultRecord));
      bool1 = iContentProviderResultRecord.getContentProvider().isDocumentsSupported();
      bool2 = iContentProviderResultRecord.getContentProvider().isChangeAlertsSupported();
      bool3 = iContentProviderResultRecord.getContentProvider().isFailureAlertsSupported();
      bool4 = iContentProviderResultRecord.getContentProvider().isPartStatusChangeAlertSupported();
      bool5 = iContentProviderResultRecord.getContentProvider().isEndOfLifeAlertSupported();
      bool = (bool2 || bool3 || bool4 || bool5) ? true : false;
      bool6 = iContentProviderResultRecord.getContentProvider().isSuggestedAlternatesSupported();
    } 
    vector1.addElement("Compare");
    LinkedHashMap<Object, Object> linkedHashMap = new LinkedHashMap<>();
    for (IContentProviderResultRecord iContentProviderResultRecord : paramCollection) {
      for (String str : iContentProviderResultRecord.getPartPropertyCategories()) {
        Map<Object, Object> map = (Map)linkedHashMap.get(str);
        if (map == null) {
          map = new LinkedHashMap<>();
          linkedHashMap.put(str, map);
        } 
        for (ComponentProperty componentProperty : iContentProviderResultRecord.getPartPropertiesByCategory(str)) {
          if (componentProperty.isVisibleInViewCompare())
            map.put(componentProperty.getId(), componentProperty); 
        } 
      } 
    } 
    SectionHeaderCell sectionHeaderCell = new SectionHeaderCell("Classification");
    vector1 = new Vector<>();
    vector.add(vector1);
    vector1.addElement(sectionHeaderCell);
    for (IContentProviderResultRecord iContentProviderResultRecord : paramCollection)
      vector1.addElement(iContentProviderResultRecord.getPartClassName()); 
    Comparator<ComponentProperty> comparator = new Comparator<ComponentProperty>() {
        public int compare(ComponentProperty param1ComponentProperty1, ComponentProperty param1ComponentProperty2) {
          return param1ComponentProperty1.getLabel().compareTo(param1ComponentProperty2.getLabel());
        }
      };
    for (String str : linkedHashMap.keySet()) {
      sectionHeaderCell = new SectionHeaderCell(str);
      vector1 = new Vector<>();
      vector.add(vector1);
      vector1.addElement(sectionHeaderCell);
      for (byte b = 0; b < paramCollection.size(); b++)
        vector1.addElement(""); 
      ArrayList<ComponentProperty> arrayList = new ArrayList(((Map)linkedHashMap.get(str)).values());
      Collections.sort(arrayList, comparator);
      for (ComponentProperty componentProperty : arrayList) {
        vector1 = new Vector<>();
        vector.add(vector1);
        vector1.addElement(new PropertyIdSectionHeaderCell("    " + componentProperty.getLabel()));
        for (IContentProviderResultRecord iContentProviderResultRecord : paramCollection) {
          ComponentProperty componentProperty1 = iContentProviderResultRecord.getPartProperty(componentProperty.getId());
          if (componentProperty1 != null) {
            if (componentProperty1.isDocumentURL()) {
              vector1.addElement(new ViewDocumentCell(componentProperty1.getValue()));
              continue;
            } 
            vector1.addElement(componentProperty1.getValueWithOUM());
            continue;
          } 
          vector1.addElement("** N/A **");
        } 
      } 
      boolean bool7 = false;
      for (IContentProviderResultRecord iContentProviderResultRecord : paramCollection) {
        if (iContentProviderResultRecord.getCategoryDocuments(str) != null) {
          vector1 = new Vector<>();
          vector.add(vector1);
          vector1.addElement(new SectionHeaderCell(str + " Documents"));
          bool7 = true;
          break;
        } 
      } 
      if (bool7)
        for (IContentProviderResultRecord iContentProviderResultRecord : paramCollection)
          vector1.addElement(new ViewDocumentsCell(iContentProviderResultRecord, iContentProviderResultRecord.getCategoryDocuments(str)));  
    } 
    for (byte b1 = 1; b1 < vector.size(); b1++) {
      Vector<Object> vector2 = vector.get(b1);
      HashSet<String> hashSet = new HashSet();
      boolean bool7 = false;
      byte b;
      for (b = 1; b < vector2.size(); b++) {
        String str = (String)vector2.get(b);
        if (str instanceof String) {
          hashSet.add(str);
        } else if (str instanceof ContentProviderViewCompareJTextArea) {
          ContentProviderViewCompareJTextArea contentProviderViewCompareJTextArea = (ContentProviderViewCompareJTextArea)vector2.get(b);
          hashSet.add(contentProviderViewCompareJTextArea.getText());
        } else if (str instanceof ViewDocumentCell) {
          bool7 = true;
        } 
      } 
      vector2.addElement(new Boolean((hashSet.size() > 1)));
      if (bool7)
        for (b = 1; b < vector2.size(); b++) {
          Object object = vector2.get(b);
          if (!(object instanceof ViewDocumentCell))
            vector2.set(b, new ViewDocumentCell("")); 
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
      vector1 = new Vector<>();
      vector.add(vector1);
      vector1.addElement(sectionHeaderCell);
      for (IContentProviderResultRecord iContentProviderResultRecord : paramCollection) {
        boolean bool7 = false;
        for (IContentProviderCustomTable iContentProviderCustomTable : iContentProviderResultRecord.getCustomTables()) {
          if (iContentProviderCustomTable.getCategoryLabel().equals(str)) {
            vector1.addElement(new ViewCustomTableCell(iContentProviderResultRecord, iContentProviderCustomTable));
            bool7 = true;
            break;
          } 
        } 
        if (!bool7)
          vector1.addElement(new ViewCustomTableCell(iContentProviderResultRecord, str)); 
      } 
    } 
    if (bool1) {
      sectionHeaderCell = new SectionHeaderCell("Documents");
      vector1 = new Vector<>();
      vector.add(vector1);
      vector1.addElement(sectionHeaderCell);
      for (IContentProviderResultRecord iContentProviderResultRecord : paramCollection)
        vector1.addElement(new ViewDocumentsCell(iContentProviderResultRecord, iContentProviderResultRecord.getDocuments())); 
    } 
    if (bool) {
      sectionHeaderCell = new SectionHeaderCell("Alerts");
      vector1 = new Vector<>();
      vector.add(vector1);
      vector1.addElement(sectionHeaderCell);
      for (IContentProviderResultRecord iContentProviderResultRecord : paramCollection)
        vector1.addElement(new ViewAlertsCell(iContentProviderResultRecord)); 
    } 
    if (bool6) {
      sectionHeaderCell = new SectionHeaderCell("Suggested Alternates");
      vector1 = new Vector<>();
      vector.add(vector1);
      vector1.addElement(sectionHeaderCell);
      for (IContentProviderResultRecord iContentProviderResultRecord : paramCollection)
        vector1.addElement(new ViewAlternatesCell(iContentProviderResultRecord)); 
    } 
    this.compTable = new ContentProviderViewCompareJXTable(new ContentProviderViewCompareTableModel(vector, vector.get(0)));
    JScrollPane jScrollPane = new JScrollPane((Component)this.compTable);
    container.add(jScrollPane);
    this.compTable.getColumnExt(0).setMinWidth(250);
    this.compTable.getColumnExt(0).setMaxWidth(250);
    this.compTable.getColumnExt(this.compTable.getColumnCount() - 1).setVisible(false);
    for (byte b2 = 0; b2 < this.compTable.getRowCount(); b2++) {
      int i = this.compTable.getRowHeight();
      for (byte b = 0; b < this.compTable.getColumnCount(); b++) {
        Component component = this.compTable.prepareRenderer(this.compTable.getCellRenderer(b2, b), b2, b);
        i = Math.max(i, (component.getPreferredSize()).height);
      } 
      this.compTable.setRowHeight(b2, i);
    } 
    setDefaultCloseOperation(2);
    pack();
    setSize(800, 800);
    setLocationRelativeTo(paramJFrame);
    setVisible(true);
  }
  
  public static void show(JFrame paramJFrame, Collection<IContentProviderResultRecord> paramCollection) {
    if (theWindow != null)
      theWindow.dispose(); 
    theWindow = new ContentProviderViewCompareWindow(paramJFrame, paramCollection);
  }
  
  public static JFrame getJFrame() {
    return (theWindow == null) ? null : theWindow;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\plugin\ContentProviderViewCompareWindow.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */