package com.mentor.dms.contentprovider.core.plugin.searchui;

import com.mentor.datafusion.utils.logger.MGLogger;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class ContentProviderSearchTabComponent extends JPanel {
  private static MGLogger log = MGLogger.getLogger(ContentProviderSearchTabComponent.class);
  
  private final JTabbedPane pane;
  
  private static Icon waitIcon = new ImageIcon(ContentProviderSearchMainPanel.class.getResource("images/animated-wait.gif"));
  
  private JLabel waitIconLabel;
  
  private static Icon warnIcon = new ImageIcon(ContentProviderSearchMainPanel.class.getResource("images/warning.png"));
  
  private JLabel warnIconLabel;
  
  private JLabel searchResultCountLabel;
  
  public ContentProviderSearchTabComponent(final JTabbedPane pane, Icon paramIcon) {
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
          int i = pane.indexOfTabComponent(ContentProviderSearchTabComponent.this);
          return (i != -1) ? pane.getTitleAt(i) : null;
        }
      };
    add(jLabel2);
    jLabel2.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
    this.waitIconLabel = new JLabel(waitIcon);
    this.waitIconLabel.setVisible(false);
    add(this.waitIconLabel);
    this.warnIconLabel = new JLabel(warnIcon);
    this.warnIconLabel.setVisible(false);
    add(this.warnIconLabel);
    this.searchResultCountLabel = new JLabel();
    this.searchResultCountLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
    Font font = new Font(this.searchResultCountLabel.getFont().getName(), 3, this.searchResultCountLabel.getFont().getSize());
    this.searchResultCountLabel.setFont(font);
    this.searchResultCountLabel.setVisible(false);
    add(this.searchResultCountLabel);
    setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
  }
  
  void setSearchIcon() {
    this.waitIconLabel.setVisible(true);
    this.warnIconLabel.setVisible(false);
    this.searchResultCountLabel.setVisible(false);
  }
  
  void setWarningIcon(String paramString) {
    this.warnIconLabel.setVisible(true);
    this.warnIconLabel.setToolTipText(paramString);
    this.waitIconLabel.setVisible(false);
    this.searchResultCountLabel.setVisible(false);
  }
  
  void setReturnCount(int paramInt) {
    this.waitIconLabel.setVisible(false);
    this.warnIconLabel.setVisible(false);
    this.searchResultCountLabel.setVisible(true);
    if (paramInt >= 0) {
      this.searchResultCountLabel.setText(Integer.toString(paramInt));
    } else {
      this.searchResultCountLabel.setText("");
    } 
  }
  
  void setReturnCountWarn(int paramInt, String paramString) {
    this.waitIconLabel.setVisible(false);
    this.warnIconLabel.setVisible(true);
    this.warnIconLabel.setToolTipText(paramString);
    this.searchResultCountLabel.setVisible(true);
    if (paramInt >= 0) {
      this.searchResultCountLabel.setText(Integer.toString(paramInt));
    } else {
      this.searchResultCountLabel.setText("");
    } 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\plugin\searchui\ContentProviderSearchTabComponent.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */