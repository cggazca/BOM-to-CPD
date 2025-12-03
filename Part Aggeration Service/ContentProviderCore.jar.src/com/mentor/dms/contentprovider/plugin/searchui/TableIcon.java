package com.mentor.dms.contentprovider.plugin.searchui;

import java.awt.Component;
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.table.TableColumnExt;

public class TableIcon extends JPanel {
  private final ImageIcon checkIcon = new ImageIcon(getClass().getResource("images/check.png"));
  
  private final ImageIcon symbolIcon = new ImageIcon(getClass().getResource("images/symbol.png"));
  
  private final ImageIcon cellIcon = new ImageIcon(getClass().getResource("images/cell.png"));
  
  private final ImageIcon Model3DIcon = new ImageIcon(getClass().getResource("images/3DModel.png"));
  
  public TableIcon() {
    String[] arrayOfString = { "Picture", "Description" };
    ArrayList<ImageIcon> arrayList = new ArrayList();
    arrayList.add(this.checkIcon);
    arrayList.add(this.checkIcon);
    CompoundIcon compoundIcon = new CompoundIcon((Collection)arrayList);
    Object[][] arrayOfObject = { { compoundIcon, "BR549" } };
    DefaultTableModel defaultTableModel = new DefaultTableModel(arrayOfObject, (Object[])arrayOfString) {
        public Class getColumnClass(int param1Int) {
          return (param1Int == 0) ? Icon.class : getValueAt(0, param1Int).getClass();
        }
      };
    JXTable jXTable = new JXTable(defaultTableModel);
    jXTable.setPreferredScrollableViewportSize(jXTable.getPreferredSize());
    JScrollPane jScrollPane = new JScrollPane((Component)jXTable);
    add(jScrollPane);
    TableColumnExt tableColumnExt = jXTable.getColumnExt(0);
    tableColumnExt.setMaxWidth(60);
  }
  
  private static void createAndShowGUI() {
    JFrame jFrame = new JFrame("Table Icon");
    jFrame.setDefaultCloseOperation(3);
    jFrame.add(new TableIcon());
    jFrame.setLocationByPlatform(true);
    jFrame.pack();
    jFrame.setVisible(true);
  }
  
  public static void main(String[] paramArrayOfString) {
    EventQueue.invokeLater(new Runnable() {
          public void run() {
            TableIcon.createAndShowGUI();
          }
        });
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\plugin\searchui\TableIcon.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */