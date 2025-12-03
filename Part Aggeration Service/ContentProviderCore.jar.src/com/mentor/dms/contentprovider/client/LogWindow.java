package com.mentor.dms.contentprovider.client;

import com.mentor.dms.contentprovider.ContentProviderGlobal;
import java.awt.Dialog;
import java.awt.Window;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class LogWindow {
  public static void displayText(String paramString1, String paramString2) {
    final JTextArea textArea = new JTextArea(20, 120);
    jTextArea.setText(paramString2);
    jTextArea.setEditable(false);
    JScrollPane jScrollPane = new JScrollPane(jTextArea);
    jTextArea.addHierarchyListener(new HierarchyListener() {
          public void hierarchyChanged(HierarchyEvent param1HierarchyEvent) {
            Window window = SwingUtilities.getWindowAncestor(textArea);
            if (window instanceof Dialog) {
              Dialog dialog = (Dialog)window;
              if (!dialog.isResizable())
                dialog.setResizable(true); 
            } 
          }
        });
    JOptionPane.showMessageDialog(ContentProviderGlobal.getRootFrame(), jScrollPane, paramString1, 2);
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\client\LogWindow.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */