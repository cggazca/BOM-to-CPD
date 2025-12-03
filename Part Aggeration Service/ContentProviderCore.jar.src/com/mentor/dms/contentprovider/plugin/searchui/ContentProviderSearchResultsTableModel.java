package com.mentor.dms.contentprovider.plugin.searchui;

import com.mentor.dms.contentprovider.AbstractContentProvider;
import com.mentor.dms.contentprovider.ComponentProperty;
import com.mentor.dms.contentprovider.ContentProviderException;
import com.mentor.dms.contentprovider.EmptyContentProviderResults;
import com.mentor.dms.contentprovider.IContentProviderResultRecord;
import com.mentor.dms.contentprovider.IContentProviderResults;
import com.mentor.dms.contentprovider.plugin.ViewDocumentCell;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.table.AbstractTableModel;

public class ContentProviderSearchResultsTableModel extends AbstractTableModel {
  private AbstractContentProvider ccp = null;
  
  private ArrayList<ContentProviderSearchResultsColumn> columnList = new ArrayList<>();
  
  private HashMap<Integer, String> columnIdToIndexHash = new HashMap<>();
  
  private ArrayList<IContentProviderResultRecord> partList = new ArrayList<>();
  
  private final ImageIcon checkIcon = new ImageIcon(getClass().getResource("images/check.png"));
  
  private final ImageIcon symbolIcon = new ImageIcon(getClass().getResource("images/symbol.png"));
  
  private final ImageIcon cellIcon = new ImageIcon(getClass().getResource("images/cell.png"));
  
  private final ImageIcon Model3DIcon = new ImageIcon(getClass().getResource("images/3DModel.png"));
  
  private IContentProviderResults results = (IContentProviderResults)new EmptyContentProviderResults();
  
  private int fixedColumnsCount = 2;
  
  public ContentProviderSearchResultsTableModel(AbstractContentProvider paramAbstractContentProvider) {
    this.ccp = paramAbstractContentProvider;
    this.results = this.results;
  }
  
  public void reset() {
    this.columnList.clear();
    this.columnIdToIndexHash.clear();
    this.partList.clear();
  }
  
  public void resetColumns() {
    this.columnList.clear();
    this.columnIdToIndexHash.clear();
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
    return this.columnList.size() + this.fixedColumnsCount;
  }
  
  public String getColumnName(int paramInt) {
    return (paramInt == 0) ? "" : ((paramInt == 1 && this.results.containsECADModelResults()) ? "ECAD" : ((paramInt == 1 || (paramInt == 2 && this.results.containsECADModelResults())) ? "Classification" : ((ContentProviderSearchResultsColumn)this.columnList.get(paramInt - this.fixedColumnsCount)).getColumnName()));
  }
  
  public Class<? extends Object> getColumnClass(int paramInt) {
    Object object = getValueAt(0, paramInt);
    return (Class)((object == null) ? Object.class : ((paramInt == 1 && this.results.containsECADModelResults()) ? Icon.class : object.getClass()));
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
        return (str2 != null) ? new ViewDocumentCell(str2, 16, 16) : new ViewDatasheetCell(iContentProviderResultRecord, 16, 16);
      } 
      if (paramInt2 == 1 && this.results.containsECADModelResults()) {
        ArrayList<ImageIcon> arrayList = new ArrayList();
        if (iContentProviderResultRecord.hasECADModels() && !iContentProviderResultRecord.isECADSymbolAvailable() && !iContentProviderResultRecord.isECADFootprintAvailable() && !iContentProviderResultRecord.isECAD3DModelAvailable()) {
          arrayList.add(this.checkIcon);
        } else {
          if (iContentProviderResultRecord.isECADSymbolAvailable())
            arrayList.add(this.symbolIcon); 
          if (iContentProviderResultRecord.isECADFootprintAvailable())
            arrayList.add(this.cellIcon); 
          if (iContentProviderResultRecord.isECAD3DModelAvailable())
            arrayList.add(this.Model3DIcon); 
        } 
        return new CompoundIcon((Collection)arrayList);
      } 
      if (paramInt2 == 1 || (paramInt2 == 2 && this.results.containsECADModelResults()))
        return iContentProviderResultRecord.getPartClassName(); 
      String str1 = this.columnIdToIndexHash.get(Integer.valueOf(paramInt2 - this.fixedColumnsCount));
      ComponentProperty componentProperty = iContentProviderResultRecord.getPartProperty(str1);
      if (componentProperty != null)
        str = componentProperty.getValueWithOUM(); 
    } 
    return str;
  }
  
  public boolean isCellEditable(int paramInt1, int paramInt2) {
    return (paramInt2 == 0);
  }
  
  public void setContentProviderResults(IContentProviderResults paramIContentProviderResults) throws ContentProviderException {
    this.results = paramIContentProviderResults;
    for (IContentProviderResultRecord iContentProviderResultRecord : paramIContentProviderResults.getResultRecords())
      addPart(iContentProviderResultRecord); 
    if (!paramIContentProviderResults.containsECADModelResults()) {
      this.fixedColumnsCount = 2;
    } else {
      this.fixedColumnsCount = 3;
    } 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\plugin\searchui\ContentProviderSearchResultsTableModel.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */