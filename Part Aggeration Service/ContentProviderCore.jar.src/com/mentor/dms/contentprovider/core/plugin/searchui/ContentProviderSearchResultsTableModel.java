package com.mentor.dms.contentprovider.core.plugin.searchui;

import com.mentor.dms.contentprovider.core.AbstractContentProvider;
import com.mentor.dms.contentprovider.core.ComponentProperty;
import com.mentor.dms.contentprovider.core.ContentProviderException;
import com.mentor.dms.contentprovider.core.EmptyContentProviderResults;
import com.mentor.dms.contentprovider.core.IContentProviderResultRecord;
import com.mentor.dms.contentprovider.core.IContentProviderResults;
import com.mentor.dms.contentprovider.core.plugin.ViewDocumentCell;
import com.mentor.dms.contentprovider.core.plugin.ViewSupplyChainCell;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
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
  
  private int fixedColumnsCount = 3;
  
  Map<String, String> definitionsEnricher = new HashMap<>();
  
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
    return (paramInt == 0) ? "" : ((paramInt == 1) ? "" : ((paramInt == 2 || (paramInt == 3 && this.results.containsECADModelResults())) ? "Classification" : ((ContentProviderSearchResultsColumn)this.columnList.get(paramInt - this.fixedColumnsCount)).getColumnName()));
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
      if (paramInt2 == 1) {
        Collection collection = iContentProviderResultRecord.getSupplyChain();
        HashMap<Object, Object> hashMap = new HashMap<>();
        hashMap.put("manufacturerName", iContentProviderResultRecord.getManufacturerName());
        hashMap.put("manufacturerPartNumber", iContentProviderResultRecord.getPartNumber());
        return (collection != null) ? new ViewSupplyChainCell(hashMap, this.definitionsEnricher, collection, 16, 16) : new ViewSupplyChainCell(hashMap, this.definitionsEnricher, null, 16, 16);
      } 
      if (paramInt2 == 2 || (paramInt2 == 3 && this.results.containsECADModelResults()))
        return iContentProviderResultRecord.getPartClassName(); 
      String str1 = this.columnIdToIndexHash.get(Integer.valueOf(paramInt2 - this.fixedColumnsCount));
      ComponentProperty componentProperty = iContentProviderResultRecord.getPartProperty(str1);
      if (componentProperty != null)
        str = componentProperty.getValueWithOUM(); 
    } 
    return str;
  }
  
  public boolean isCellEditable(int paramInt1, int paramInt2) {
    return (paramInt2 == 0 || paramInt2 == 1);
  }
  
  public void setDefinitionsEnricher(Map<String, String> paramMap) {
    this.definitionsEnricher = paramMap;
  }
  
  public void setContentProviderResults(IContentProviderResults paramIContentProviderResults) throws ContentProviderException {
    this.results = paramIContentProviderResults;
    for (IContentProviderResultRecord iContentProviderResultRecord : paramIContentProviderResults.getResultRecords())
      addPart(iContentProviderResultRecord); 
  }
  
  public String getNextPageToken() {
    return this.results.getNextPageToken();
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\plugin\searchui\ContentProviderSearchResultsTableModel.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */