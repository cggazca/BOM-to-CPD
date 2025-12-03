package com.mentor.dms.contentprovider.plugin;

import com.mentor.dms.contentprovider.IContentProviderResultRecord;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

public abstract class AbstractPushButtonCell extends JPanel implements ICustomCell, ActionListener {
  protected IContentProviderResultRecord resultRecord;
  
  public AbstractPushButtonCell() {
    setOpaque(true);
    setLayout(new BoxLayout(this, 1));
    setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
  }
  
  public AbstractPushButtonCell(IContentProviderResultRecord paramIContentProviderResultRecord) {
    this();
    this.resultRecord = paramIContentProviderResultRecord;
  }
  
  public IContentProviderResultRecord getResultRecord() {
    return this.resultRecord;
  }
  
  public abstract void actionPerformed(ActionEvent paramActionEvent);
  
  public String toString() {
    return "";
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\plugin\AbstractPushButtonCell.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */