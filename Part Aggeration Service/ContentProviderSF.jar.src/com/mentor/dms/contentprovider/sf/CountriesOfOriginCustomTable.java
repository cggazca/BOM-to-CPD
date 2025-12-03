package com.mentor.dms.contentprovider.sf;

import com.mentor.dms.contentprovider.core.IContentProviderCustomTable;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.Vector;
import javax.swing.Icon;
import javax.swing.ImageIcon;

public class CountriesOfOriginCustomTable implements IContentProviderCustomTable {
  private Vector<Vector<?>> vData = new Vector<>();
  
  private final String[] columns = new String[] { "Country", "Source" };
  
  public void addCountryOfOrigin(String paramString1, String paramString2) {
    Vector<?> vector = new Vector();
    this.vData.add(vector);
    vector.addElement(paramString1);
    vector.addElement(createViewDocumentCell(paramString2));
  }
  
  public String getCategoryLabel() {
    return "Countries Of Origin";
  }
  
  public String getViewLabel() {
    return "Country(s) Of Origin";
  }
  
  public Icon getIcon() {
    Image image = Toolkit.getDefaultToolkit().getImage(CountriesOfOriginCustomTable.class.getResource("images/country.png"));
    return new ImageIcon(image);
  }
  
  public Class<?> getColumnClass(int paramInt) {
    return (getRowCount() == 0) ? String.class : getValueAt(0, paramInt).getClass();
  }
  
  public int getColumnCount() {
    return this.columns.length;
  }
  
  public String getColumnName(int paramInt) {
    return this.columns[paramInt];
  }
  
  public int getRowCount() {
    return this.vData.size();
  }
  
  public Object getValueAt(int paramInt1, int paramInt2) {
    return ((Vector)this.vData.get(paramInt1)).get(paramInt2);
  }
  
  public int getWidth() {
    return 1000;
  }
  
  public int getHeight() {
    return 300;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\CountriesOfOriginCustomTable.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */