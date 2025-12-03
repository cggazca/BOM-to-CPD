package com.mentor.dms.contentprovider.client;

import java.util.ArrayList;
import javax.swing.AbstractListModel;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class FilteredJList extends JList {
  private FilterField filterField;
  
  private int DEFAULT_FIELD_WIDTH = 20;
  
  public FilteredJList() {
    setModel(new FilterModel());
    this.filterField = new FilterField(this.DEFAULT_FIELD_WIDTH);
  }
  
  public FilteredJList(Object[] paramArrayOfObject) {
    FilterModel filterModel = new FilterModel();
    setModel(filterModel);
    this.filterField = new FilterField(this.DEFAULT_FIELD_WIDTH);
    for (byte b = 0; b < paramArrayOfObject.length; b++)
      filterModel.addElement(paramArrayOfObject[b]); 
  }
  
  public void setModel(ListModel<?> paramListModel) {
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
      for (byte b = 0; b < this.items.size(); b++) {
        if (this.items.get(b).toString().toUpperCase().indexOf(str, 0) != -1)
          this.filterItems.add(this.items.get(b)); 
      } 
      fireContentsChanged(this, 0, getSize());
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


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\client\FilteredJList.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */