package com.mentor.dms.contentprovider.client.userpref;

import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class UserPreferencesTabComponent extends JPanel {
  private final JTabbedPane pane;
  
  public UserPreferencesTabComponent(final JTabbedPane pane, Icon paramIcon) {
    super(new FlowLayout(0, 0, 0));
    if (pane == null)
      throw new NullPointerException("TabbedPane is null"); 
    this.pane = pane;
    setOpaque(false);
    JLabel jLabel1 = new JLabel(paramIcon);
    jLabel1.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
    add(jLabel1);
    JLabel jLabel2 = new JLabel() {
        public String getText() {
          int i = pane.indexOfTabComponent(UserPreferencesTabComponent.this);
          return (i != -1) ? pane.getTitleAt(i) : null;
        }
      };
    add(jLabel2);
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\clien\\userpref\UserPreferencesTabComponent.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */