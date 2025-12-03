package com.mentor.dms.contentprovider.sf;

import com.mentor.dms.contentprovider.core.IContentProviderCustomTable;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.Vector;
import javax.swing.Icon;
import javax.swing.ImageIcon;

public class ChemicalCustomTable implements IContentProviderCustomTable {
  private Vector<Vector<?>> vData = new Vector<>();
  
  private final String[] columns = new String[] { "Total Mass In Gram", "Total Mass Summation In Gram", "Location Name", "Homogenous Material", "Substance Identification", "Substance Mass", "Parts Per Million", "CAS Number", "Material Composition Declaration" };
  
  public void addChemical(double paramDouble1, double paramDouble2, String paramString1, String paramString2, String paramString3, double paramDouble3, double paramDouble4, String paramString4, String paramString5) {
    Vector<?> vector = new Vector();
    this.vData.add(vector);
    vector.addElement(Double.valueOf(paramDouble1));
    vector.addElement(Double.valueOf(paramDouble2));
    vector.addElement(paramString1);
    vector.addElement(paramString2);
    vector.addElement(paramString3);
    vector.addElement(Double.valueOf(paramDouble3));
    vector.addElement(Double.valueOf(paramDouble4));
    vector.addElement(paramString4);
    vector.addElement(createViewDocumentCell(paramString5));
  }
  
  public String getCategoryLabel() {
    return "Chemical Information";
  }
  
  public String getViewLabel() {
    return "Chemical(s)";
  }
  
  public Icon getIcon() {
    Image image = Toolkit.getDefaultToolkit().getImage(ChemicalCustomTable.class.getResource("images/material.png"));
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


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\ChemicalCustomTable.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */