package com.mentor.dms.contentprovider.core.client;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import javax.swing.AbstractListModel;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class FilteredJList extends JList<Object> {
  private FilterField filterField;
  
  private int DEFAULT_FIELD_WIDTH = 20;
  
  private Map<String, String> valueMap = new LinkedHashMap<>();
  
  public FilteredJList() {
    setModel(new FilterModel());
    this.filterField = new FilterField(this.DEFAULT_FIELD_WIDTH);
  }
  
  public FilteredJList(Map<String, String> paramMap) {
    this.valueMap = paramMap;
    FilterModel filterModel = new FilterModel();
    setModel(filterModel);
    this.filterField = new FilterField(this.DEFAULT_FIELD_WIDTH);
    for (String str : paramMap.keySet())
      filterModel.addElement(str); 
  }
  
  public void setModel(ListModel paramListModel) {
    if (!(paramListModel instanceof FilterModel))
      throw new IllegalArgumentException(); 
    super.setModel(paramListModel);
  }
  
  public void addItem(Object paramObject) {
    FilterModel filterModel = (FilterModel)getModel();
    filterModel.addElement(paramObject);
  }
  
  public JTextField getFilterField() {
    return this.filterField;
  }
  
  class FilterModel extends AbstractListModel {
    ArrayList items = new ArrayList();
    
    ArrayList filterItems = new ArrayList();
    
    public Object getElementAt(int param1Int) {
      return (param1Int < this.filterItems.size()) ? this.filterItems.get(param1Int) : null;
    }
    
    public int getSize() {
      return this.filterItems.size();
    }
    
    public void addElement(Object param1Object) {
      this.items.add(param1Object);
      refilter();
    }
    
    private void refilter() {
      this.filterItems.clear();
      String str = FilteredJList.this.getFilterField().getText().toUpperCase();
      for (String str1 : FilteredJList.this.valueMap.values()) {
        if (str1.toUpperCase().indexOf(str, 0) != -1) {
          String str2 = searchFromValue(FilteredJList.this.valueMap, str1);
          this.filterItems.add(str2);
        } 
      } 
      fireContentsChanged(this, 0, getSize());
    }
    
    public String searchFromValue(Map<String, String> param1Map, String param1String) {
      for (Map.Entry<String, String> entry : param1Map.entrySet()) {
        if (Objects.equals(param1String, entry.getValue()))
          return (String)entry.getKey(); 
      } 
      return null;
    }
  }
  
  class FilterField extends JTextField implements DocumentListener {
    public FilterField(int param1Int) {
      super(param1Int);
      getDocument().addDocumentListener(this);
    }
    
    public void changedUpdate(DocumentEvent param1DocumentEvent) {
      ((FilteredJList.FilterModel)FilteredJList.this.getModel()).refilter();
    }
    
    public void insertUpdate(DocumentEvent param1DocumentEvent) {
      ((FilteredJList.FilterModel)FilteredJList.this.getModel()).refilter();
    }
    
    public void removeUpdate(DocumentEvent param1DocumentEvent) {
      ((FilteredJList.FilterModel)FilteredJList.this.getModel()).refilter();
    }
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\client\FilteredJList.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */