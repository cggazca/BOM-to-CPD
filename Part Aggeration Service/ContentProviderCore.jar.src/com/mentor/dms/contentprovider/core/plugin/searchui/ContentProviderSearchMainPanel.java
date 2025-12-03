package com.mentor.dms.contentprovider.core.plugin.searchui;

import com.mentor.datafusion.utils.logger.MGLogger;
import com.mentor.dms.contentprovider.core.AbstractContentProvider;
import com.mentor.dms.contentprovider.core.ContentProviderException;
import com.mentor.dms.contentprovider.core.ContentProviderFactory;
import com.mentor.dms.contentprovider.core.ContentProviderRegistryEntry;
import com.mentor.dms.contentprovider.core.IContentProviderResultRecord;
import com.mentor.dms.contentprovider.core.client.CheckLicense;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXStatusBar;
import org.jdesktop.swingx.plaf.LookAndFeelAddons;
import org.jdesktop.swingx.plaf.windows.WindowsClassicLookAndFeelAddons;

public class ContentProviderSearchMainPanel extends JXPanel implements ActionListener {
  private static MGLogger log = MGLogger.getLogger(ContentProviderSearchMainPanel.class);
  
  private ContentProviderSearchTabbedPane tabs;
  
  private boolean bSearchEnabled = false;
  
  private JButton searchButton;
  
  private JButton nextButton;
  
  private JLabel statusLabel;
  
  private String searchRestrictionChecksum = "";
  
  private ContentProviderRegistryEntry.ContentProviderRole currentRole = ContentProviderRegistryEntry.ContentProviderRole.NONE;
  
  public ContentProviderSearchMainPanel() {
    setName("Extended Search");
    initComponents();
  }
  
  public void initComponents() {
    try {
      setLayout(new BorderLayout());
      setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
      ImageIcon imageIcon = new ImageIcon(ContentProviderSearchMainPanel.class.getResource("images/search_catalog.png"));
      this.searchButton = new JButton("Search", imageIcon);
      this.searchButton.setActionCommand("Search");
      this.searchButton.addActionListener(this);
      this.searchButton.setPreferredSize(new Dimension(100, 24));
      this.searchButton.setToolTipText("Search Content Provider");
      if (!CheckLicense.hasSupplyChainLicense())
        this.searchButton.setEnabled(false); 
      imageIcon = new ImageIcon(ContentProviderSearchMainPanel.class.getResource("images/search_next.png"));
      this.nextButton = new JButton("More", imageIcon);
      this.nextButton.setActionCommand("Next");
      this.nextButton.addActionListener(this);
      this.nextButton.setPreferredSize(new Dimension(100, 24));
      this.nextButton.setToolTipText("Get next Content Provider results");
      this.nextButton.setEnabled(false);
      JPanel jPanel = new JPanel();
      jPanel.setLayout(new BoxLayout(jPanel, 2));
      jPanel.setBorder(BorderFactory.createEmptyBorder(3, 0, 3, 0));
      jPanel.add(this.searchButton);
      jPanel.add(this.nextButton);
      jPanel.add(Box.createHorizontalGlue());
      add("North", jPanel);
      this.tabs = new ContentProviderSearchTabbedPane(this);
      this.tabs.setBorder((Border)null);
      LookAndFeelAddons.setAddon(WindowsClassicLookAndFeelAddons.class);
      add("Center", this.tabs);
      JXStatusBar jXStatusBar = new JXStatusBar();
      jXStatusBar.setPreferredSize(new Dimension(10000, 24));
      jXStatusBar.setMaximumSize(new Dimension(10000, 24));
      this.statusLabel = new JLabel("");
      jXStatusBar.add(this.statusLabel);
      add("South", (Component)jXStatusBar);
      this.bSearchEnabled = true;
    } catch (Exception exception) {
      log.error(exception);
      JOptionPane.showMessageDialog(null, exception.getMessage());
    } 
  }
  
  public void actionPerformed(ActionEvent paramActionEvent) {
    try {
      if (paramActionEvent.getActionCommand().equals("Search")) {
        doSearch(false);
      } else if (paramActionEvent.getActionCommand().equals("Next")) {
        doSearch(true);
      } 
    } catch (ContentProviderException contentProviderException) {
      log.error(contentProviderException);
    } 
  }
  
  public void doSearch(boolean paramBoolean) throws ContentProviderException {
    ContentProviderSearchPanel contentProviderSearchPanel = (ContentProviderSearchPanel)this.tabs.getSelectedComponent();
    contentProviderSearchPanel.doSearch(paramBoolean);
  }
  
  public void setStatus(String paramString) {
    this.statusLabel.setForeground(Color.BLACK);
    this.statusLabel.setText(paramString);
  }
  
  public void setStatus(boolean paramBoolean, String paramString) {
    if (paramBoolean) {
      this.statusLabel.setForeground(Color.RED);
    } else {
      this.statusLabel.setForeground(Color.BLACK);
    } 
    this.statusLabel.setText(paramString);
  }
  
  public void setSearchEnabled(boolean paramBoolean) {
    this.bSearchEnabled = paramBoolean;
    this.searchButton.setEnabled(paramBoolean);
  }
  
  public boolean isSearchEnabled() {
    return this.bSearchEnabled;
  }
  
  public void setNextEnabled(boolean paramBoolean) {
    this.nextButton.setEnabled(paramBoolean);
  }
  
  public void setWaitCursor(boolean paramBoolean) {
    setCursor(paramBoolean ? new Cursor(3) : null);
  }
  
  public void updateRestrictions() throws ContentProviderException {
    log.debug("Update Restrictions");
    if (!isSearchEnabled() || !CheckLicense.hasSupplyChainLicense())
      return; 
    for (byte b = 0; b < this.tabs.getTabCount(); b++) {
      ContentProviderSearchPanel contentProviderSearchPanel = (ContentProviderSearchPanel)this.tabs.getComponentAt(b);
      if (b == this.tabs.getSelectedIndex())
        contentProviderSearchPanel.updateRestrictions(); 
    } 
  }
  
  public Collection<IContentProviderResultRecord> getSelectedResults() {
    ContentProviderSearchPanel contentProviderSearchPanel = (ContentProviderSearchPanel)this.tabs.getSelectedComponent();
    return contentProviderSearchPanel.getSelectedResults();
  }
  
  public int getSelectionCount() {
    ContentProviderSearchPanel contentProviderSearchPanel = (ContentProviderSearchPanel)this.tabs.getSelectedComponent();
    return contentProviderSearchPanel.getSelectionCount();
  }
  
  public AbstractContentProvider getSelectedContentProvider() {
    ContentProviderSearchPanel contentProviderSearchPanel = (ContentProviderSearchPanel)this.tabs.getSelectedComponent();
    return contentProviderSearchPanel.getContentProvider();
  }
  
  public void initSearchProviders(ContentProviderRegistryEntry.ContentProviderRole paramContentProviderRole) {
    if (paramContentProviderRole != this.currentRole) {
      log.info("initSearchProviders Role=" + String.valueOf(paramContentProviderRole));
      try {
        this.tabs.removeAll();
        ImageIcon imageIcon = new ImageIcon(ContentProviderSearchMainPanel.class.getResource("images/internet.png"));
        byte b = 0;
        for (AbstractContentProvider abstractContentProvider : ContentProviderFactory.getInstance().getRegisteredContentProviders()) {
          if (!abstractContentProvider.hasRole(paramContentProviderRole))
            continue; 
          Icon icon = abstractContentProvider.getIcon();
          if (icon == null)
            icon = imageIcon; 
          ContentProviderSearchTabComponent contentProviderSearchTabComponent = new ContentProviderSearchTabComponent(this.tabs, icon);
          ContentProviderSearchPanel contentProviderSearchPanel = new ContentProviderSearchPanel(this, contentProviderSearchTabComponent, abstractContentProvider);
          this.tabs.addTab(abstractContentProvider.getName(), contentProviderSearchPanel);
          this.tabs.setTabComponentAt(b++, contentProviderSearchTabComponent);
        } 
        this.currentRole = paramContentProviderRole;
      } catch (Exception exception) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        exception.printStackTrace(printWriter);
        log.error(stringWriter.toString());
      } 
    } 
    if (this.tabs.getComponentCount() > 0) {
      this.tabs.setSelectedIndex(0);
    } else {
      JOptionPane.showMessageDialog((Component)this, "No Content Providers are configured with role '" + String.valueOf(paramContentProviderRole) + "'.  Search not allowed.");
      log.error("No Content Providers are configured with role '" + String.valueOf(paramContentProviderRole) + "'.  Search not allowed.");
    } 
  }
  
  public AbstractContentProvider getPreferredContentProvider(ContentProviderRegistryEntry.ContentProviderRole paramContentProviderRole) throws ContentProviderException {
    try {
      for (AbstractContentProvider abstractContentProvider : ContentProviderFactory.getInstance().getRegisteredContentProviders()) {
        if (abstractContentProvider.hasRole(paramContentProviderRole))
          return abstractContentProvider; 
      } 
    } catch (Exception exception) {
      StringWriter stringWriter = new StringWriter();
      PrintWriter printWriter = new PrintWriter(stringWriter);
      exception.printStackTrace(printWriter);
      log.error(stringWriter.toString());
    } 
    throw new ContentProviderException("No Content Providers are configured with role '" + String.valueOf(paramContentProviderRole) + "'.  Search not allowed.");
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\plugin\searchui\ContentProviderSearchMainPanel.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */