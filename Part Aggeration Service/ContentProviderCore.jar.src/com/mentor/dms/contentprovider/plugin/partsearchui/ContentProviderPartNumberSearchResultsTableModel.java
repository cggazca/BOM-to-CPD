package com.mentor.dms.contentprovider.plugin.partsearchui;

import com.mentor.dms.contentprovider.ComponentProperty;
import com.mentor.dms.contentprovider.IContentProviderResultRecord;
import com.mentor.dms.contentprovider.plugin.ViewDocumentCell;
import com.mentor.dms.contentprovider.plugin.searchui.ContentProviderSearchResultsColumn;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.table.AbstractTableModel;

public class ContentProviderPartNumberSearchResultsTableModel extends AbstractTableModel {
  private ArrayList<ContentProviderSearchResultsColumn> columnList = new ArrayList<>();
  
  private HashMap<Integer, String> columnIdToIndexHash = new HashMap<>();
  
  private ArrayList<IContentProviderResultRecord> partList = new ArrayList<>();
  
  public void reset() {
    this.columnList.clear();
    this.columnIdToIndexHash.clear();
    this.partList.clear();
  }
  
  public void clearPartList() {
    this.partList.clear();
  }
  
  public IContentProviderResultRecord getResultRecordAt(int paramInt) {
    return this.partList.get(paramInt);
  }
  
  public void addColumn(ContentProviderSearchResultsColumn paramContentProviderSearchResultsColumn) {
    this.columnList.add(paramContentProviderSearchResultsColumn);
    this.columnIdToIndexHash.put(Integer.valueOf(this.columnList.size() - 1), paramContentProviderSearchResultsColumn.getProperty().getContentProviderId());
  }
  
  public void addPart(IContentProviderResultRecord paramIContentProviderResultRecord) {
    this.partList.add(paramIContentProviderResultRecord);
  }
  
  public int getColumnCount() {
    return this.columnList.size() + 2;
  }
  
  public String getColumnName(int paramInt) {
    return (paramInt == 0) ? "" : ((paramInt == 1) ? "Classification" : ((ContentProviderSearchResultsColumn)this.columnList.get(paramInt - 2)).getColumnName());
  }
  
  public Class<? extends Object> getColumnClass(int paramInt) {
    Object object = getValueAt(0, paramInt);
    return (Class)((object == null) ? Object.class : object.getClass());
  }
  
  public int getRowCount() {
    return this.partList.size();
  }
  
  public Object getValueAt(int paramInt1, int paramInt2) {
    String str = "** N/A **";
    if (this.partList.size() > 0) {
      IContentProviderResultRecord iContentProviderResultRecord = this.partList.get(paramInt1);
      if (paramInt2 == 0) {
        String str2 = iContentProviderResultRecord.getDatasheetURL();
        return new ViewDocumentCell(str2, 16, 16);
      } 
      if (paramInt2 == 1)
        return iContentProviderResultRecord.getPartClassName(); 
      String str1 = this.columnIdToIndexHash.get(Integer.valueOf(paramInt2 - 2));
      ComponentProperty componentProperty = iContentProviderResultRecord.getPartProperty(str1);
      if (componentProperty != null)
        str = componentProperty.getValueWithOUM(); 
    } 
    return str;
  }
  
  public boolean isCellEditable(int paramInt1, int paramInt2) {
    return (paramInt2 == 0);
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\plugin\partsearchui\ContentProviderPartNumberSearchResultsTableModel.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */