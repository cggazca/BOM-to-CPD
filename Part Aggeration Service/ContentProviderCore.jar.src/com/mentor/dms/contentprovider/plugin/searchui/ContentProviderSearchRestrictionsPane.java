package com.mentor.dms.contentprovider.plugin.searchui;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import com.mentor.datafusion.oi.model.OIClass;
import com.mentor.datafusion.oi.model.OIField;
import com.mentor.datafusion.utils.logger.MGLogger;
import com.mentor.dms.contentprovider.AbstractContentProvider;
import com.mentor.dms.contentprovider.ContentProviderException;
import com.mentor.dms.contentprovider.ContentProviderGlobal;
import com.mentor.dms.contentprovider.client.PropertyValueSelectionDlg;
import com.mentor.dms.contentprovider.config.ContentProviderConfigContentProviderMap;
import com.mentor.dms.contentprovider.config.ContentProviderConfigDMSCatalog;
import com.mentor.dms.contentprovider.config.ContentProviderConfigProperty;
import com.mentor.dms.ui.searchmask.SearchMask;
import com.mentor.dms.ui.searchmask.SearchMaskManager;
import com.mentor.dms.ui.searchmask.restrictions.SearchCondition;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.text.Document;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTitledPanel;

public class ContentProviderSearchRestrictionsPane extends JXTitledPanel {
  private static MGLogger logger = MGLogger.getLogger(ContentProviderSearchRestrictionsPane.class);
  
  private SearchMaskManager searchMaskManager;
  
  private ContentProviderSearchPanel searchPanel;
  
  private AbstractContentProvider ccp;
  
  private JLabel xdmCatalogLabel;
  
  private JComboBox<ContentProviderConfigContentProviderMap> contentProviderClassCmb;
  
  private JPanel restrictionsPanel;
  
  private JScrollPane restScrollPane;
  
  private MouseAdapter popupMouseAdapter;
  
  private CheckBoxTree checkBoxTree;
  
  private DocumentListener textFieldDocumentListener;
  
  private ContentProviderSearchRestriction rest = new ContentProviderSearchRestriction();
  
  private final String searchCharsLayoutSpec = "right:max(20dlu;p), 0dlu, pref, 1dlu, [20dlu,pref,20dlu]:grow, 2dlu, pref, 10dlu,  right:max(20dlu;p), 0dlu, pref, 1dlu, [20dlu,pref,20dlu]:grow, 2dlu, pref, 10dlu,  right:max(20dlu;p), 0dlu, pref, 1dlu, [20dlu,pref,20dlu]:grow, 2dlu, pref, 10dlu,  right:max(20dlu;p), 0dlu, pref, 1dlu, [20dlu,pref,20dlu]:grow, 2dlu, pref ";
  
  private final String quickSearchLayoutSpec = "right:max(20dlu;p), 2dlu, 200dlu";
  
  public ContentProviderSearchRestrictionsPane(final ContentProviderSearchPanel searchPanel, AbstractContentProvider paramAbstractContentProvider) {
    this.searchPanel = searchPanel;
    this.ccp = paramAbstractContentProvider;
    this.searchMaskManager = ContentProviderGlobal.getDMSInstance().getSearchMaskManager();
    setBackground(UIManager.getColor("TextField.background"));
    setTitle("<html><b>Search Restrictions</b></html>");
    setToolTipText("Search Restriction(s) from the search mask that are applicable to this Content Provider.");
    JXPanel jXPanel = new JXPanel();
    jXPanel.setPreferredSize(new Dimension(18, 24));
    jXPanel.setOpaque(false);
    ImageIcon imageIcon = new ImageIcon(ContentProviderSearchMainPanel.class.getResource("images/search_catalog.png"));
    jXPanel.add(new JLabel(imageIcon));
    setLeftDecoration((JComponent)jXPanel);
    JPanel jPanel1 = new JPanel();
    jPanel1.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
    JLabel jLabel1 = new JLabel("<html><b>EDM Library Catalog</b>:</html>", 4);
    this.xdmCatalogLabel = new JLabel();
    JLabel jLabel2 = new JLabel("<html><b>Content Provider Class</b>:</html>", 4);
    this.contentProviderClassCmb = new JComboBox<>();
    this.contentProviderClassCmb.addItemListener(new ItemListener() {
          public void itemStateChanged(ItemEvent param1ItemEvent) {
            ContentProviderConfigContentProviderMap contentProviderConfigContentProviderMap = (ContentProviderConfigContentProviderMap)param1ItemEvent.getItem();
            searchPanel.doUpdateRestrictions();
          }
        });
    jPanel1.setLayout((LayoutManager)new FormLayout(new ColumnSpec[] { FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow") }, new RowSpec[] { FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC }));
    jPanel1.add(jLabel1, "2, 2 , left, default");
    jPanel1.add(this.xdmCatalogLabel, "4, 2 , left, default");
    jPanel1.add(jLabel2, "2, 4 , left, default");
    jPanel1.add(this.contentProviderClassCmb, "4, 4 , left, default");
    getContentContainer().setLayout(new BorderLayout());
    getContentContainer().add(jPanel1, "North");
    this.restrictionsPanel = new JPanel();
    this.restrictionsPanel.setLayout(new BorderLayout());
    FormLayout formLayout = new FormLayout("right:max(20dlu;p), 0dlu, pref, 1dlu, [20dlu,pref,20dlu]:grow, 2dlu, pref, 10dlu,  right:max(20dlu;p), 0dlu, pref, 1dlu, [20dlu,pref,20dlu]:grow, 2dlu, pref, 10dlu,  right:max(20dlu;p), 0dlu, pref, 1dlu, [20dlu,pref,20dlu]:grow, 2dlu, pref, 10dlu,  right:max(20dlu;p), 0dlu, pref, 1dlu, [20dlu,pref,20dlu]:grow, 2dlu, pref ", "");
    DefaultFormBuilder defaultFormBuilder = new DefaultFormBuilder(formLayout);
    defaultFormBuilder.border((Border)Borders.DIALOG);
    defaultFormBuilder.append("<html><body><b><i>Search has not yet been executed.</i></b><body></html>");
    JPanel jPanel2 = defaultFormBuilder.getPanel();
    this.checkBoxTree = new CheckBoxTree();
    this.checkBoxTree.addListener(new CheckBoxTreeListener() {
          public void stateChanged(CheckBoxNode param1CheckBoxNode) {
            try {
              SearchMask searchMask = ContentProviderSearchRestrictionsPane.this.searchMaskManager.open(ContentProviderSearchRestrictionsPane.this.rest.getOIClass());
              OIField oIField = (OIField)param1CheckBoxNode.getClientObject();
              searchMask.setSelected(oIField, param1CheckBoxNode.isSelected());
            } catch (Exception exception) {
              JOptionPane.showMessageDialog(ContentProviderGlobal.getDMSInstance().getJFrame(), exception.getMessage());
            } 
          }
        });
    JScrollPane jScrollPane = new JScrollPane((Component)this.checkBoxTree);
    this.restScrollPane = new JScrollPane(jPanel2);
    JPanel jPanel3 = new JPanel();
    jPanel3.setLayout(new BorderLayout());
    JPanel jPanel4 = new JPanel();
    final JCheckBox btnAll = new JCheckBox("Select All", true);
    jCheckBox1.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent param1ActionEvent) {
            ContentProviderSearchRestrictionsPane.this.checkBoxTree.selectAll(true);
            btnAll.setSelected(true);
          }
        });
    jPanel4.add(jCheckBox1);
    final JCheckBox btnNone = new JCheckBox("Unselect All", false);
    jCheckBox2.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent param1ActionEvent) {
            ContentProviderSearchRestrictionsPane.this.checkBoxTree.selectAll(false);
            btnNone.setSelected(false);
          }
        });
    jPanel4.add(jCheckBox2);
    jPanel3.add(jPanel4, "North");
    jPanel3.add(jScrollPane, "Center");
    this.restrictionsPanel.add(this.restScrollPane, "Center");
    this.restrictionsPanel.add(jPanel3, "East");
    getContentContainer().add(this.restrictionsPanel, "Center");
    if (paramAbstractContentProvider.isValidValuesSupported())
      this.popupMouseAdapter = new MouseAdapter() {
          public void mousePressed(MouseEvent param1MouseEvent) {
            if (param1MouseEvent.isPopupTrigger())
              doPop(param1MouseEvent); 
          }
          
          public void mouseReleased(MouseEvent param1MouseEvent) {
            if (param1MouseEvent.isPopupTrigger())
              doPop(param1MouseEvent); 
          }
          
          private void doPop(MouseEvent param1MouseEvent) {
            JLabel jLabel = (JLabel)param1MouseEvent.getComponent();
            ContentProviderSearchRestrictionsPane.ValidValuesPopUpMenu validValuesPopUpMenu = new ContentProviderSearchRestrictionsPane.ValidValuesPopUpMenu(jLabel);
            validValuesPopUpMenu.show(param1MouseEvent.getComponent(), param1MouseEvent.getX(), param1MouseEvent.getY());
          }
        }; 
    this.textFieldDocumentListener = new DocumentListener() {
        public void insertUpdate(DocumentEvent param1DocumentEvent) {
          doUpdate(param1DocumentEvent);
        }
        
        public void removeUpdate(DocumentEvent param1DocumentEvent) {
          doUpdate(param1DocumentEvent);
        }
        
        public void changedUpdate(DocumentEvent param1DocumentEvent) {
          doUpdate(param1DocumentEvent);
        }
        
        private void doUpdate(DocumentEvent param1DocumentEvent) {
          try {
            SearchMask searchMask = ContentProviderSearchRestrictionsPane.this.searchMaskManager.open(ContentProviderSearchRestrictionsPane.this.rest.getOIClass());
            Document document = param1DocumentEvent.getDocument();
            OIField oIField = (OIField)document.getProperty("OIFIELD");
            String str = document.getText(0, document.getLength());
            searchMask.setRestriction(oIField, str);
            searchPanel.restrictionChangeEvent();
          } catch (Exception exception) {
            JOptionPane.showMessageDialog(ContentProviderGlobal.getDMSInstance().getJFrame(), exception.getMessage());
          } 
        }
      };
  }
  
  public ContentProviderConfigContentProviderMap getSelectedContentProviderMap() {
    return (ContentProviderConfigContentProviderMap)this.contentProviderClassCmb.getSelectedItem();
  }
  
  public void updateRestrictions(ContentProviderSearchRestriction paramContentProviderSearchRestriction, boolean paramBoolean) {
    try {
      ContentProviderSearchRestriction contentProviderSearchRestriction = this.rest;
      this.rest = paramContentProviderSearchRestriction;
      if (this.rest == null || this.rest.getOIClass() == null)
        return; 
      OIClass oIClass = this.rest.getOIClass().getSuperclass();
      if (oIClass != null && oIClass.equals(this.rest.getOIClass().getRootClass())) {
        this.xdmCatalogLabel.setText(oIClass.getLabel());
      } else {
        this.xdmCatalogLabel.setText(this.rest.getOIClass().getLabel());
      } 
      ContentProviderConfigDMSCatalog<?> contentProviderConfigDMSCatalog = this.rest.getDmsCatalogConfig();
      if (paramBoolean)
        if (this.rest.getDmsCatalogConfig() == null) {
          this.contentProviderClassCmb.removeAllItems();
        } else if (contentProviderSearchRestriction.getDmsCatalogConfig() == null || this.rest.getDmsCatalogConfig() != contentProviderSearchRestriction.getDmsCatalogConfig()) {
          this.contentProviderClassCmb.removeAllItems();
          for (ContentProviderConfigContentProviderMap contentProviderConfigContentProviderMap : contentProviderConfigDMSCatalog.getContentProviderMaps())
            this.contentProviderClassCmb.addItem(contentProviderConfigContentProviderMap); 
          if (this.contentProviderClassCmb.getItemCount() > 0)
            this.contentProviderClassCmb.setSelectedIndex(0); 
        }  
      if (this.rest.getOIClass() == null) {
        updateWithNoMapping();
        return;
      } 
      if (this.rest.getDmsCatalogConfig() == null) {
        updateWithNoMapping();
        return;
      } 
      if (this.rest.getDmsCatalogConfig().getContentProviderMaps().isEmpty()) {
        updateWithNoMapping();
        return;
      } 
      HashMap<Object, Object> hashMap = new HashMap<>();
      TreeMap<Object, Object> treeMap1 = new TreeMap<>();
      TreeMap<Object, Object> treeMap2 = new TreeMap<>();
      SearchConditionComparator searchConditionComparator = new SearchConditionComparator();
      for (ContentProviderCharacteristic contentProviderCharacteristic : this.rest.getCharacteristics()) {
        OIField<?> oIField = contentProviderCharacteristic.getOIField();
        hashMap.put(oIField.getLabel(), contentProviderCharacteristic.getProperty());
        treeMap1.put(oIField.getLabel(), ((SearchMask)this.searchMaskManager.getActive()).getSearchCondition(oIField));
      } 
      SearchMask searchMask = (SearchMask)this.searchMaskManager.getActive();
      for (SearchCondition searchCondition : searchMask.getSearchConditionList()) {
        String str = this.rest.getTabMap().get(searchCondition.getField().getName());
        if (str != null) {
          TreeSet<SearchCondition> treeSet = (TreeSet)treeMap2.get(str);
          if (treeSet == null) {
            treeSet = new TreeSet(searchConditionComparator);
            treeMap2.put(str, treeSet);
          } 
          treeSet.add(searchCondition);
        } 
      } 
      ArrayList<TabOrder> arrayList = new ArrayList();
      for (Map.Entry<Object, Object> entry : treeMap2.entrySet()) {
        String str = (String)entry.getKey();
        int i = Integer.MAX_VALUE;
        for (SearchCondition searchCondition : entry.getValue()) {
          if (searchCondition.getDisposeOrder() < i)
            i = searchCondition.getDisposeOrder(); 
        } 
        arrayList.add(new TabOrder(str, i));
      } 
      Collections.sort(arrayList, new Comparator<TabOrder>() {
            public int compare(TabOrder param1TabOrder1, TabOrder param1TabOrder2) {
              return param1TabOrder1.getMinDisposeOrder().compareTo(param1TabOrder2.getMinDisposeOrder());
            }
          });
      FormLayout formLayout = null;
      if (this.rest.isQuickSearchEnabled()) {
        formLayout = new FormLayout("right:max(20dlu;p), 2dlu, 200dlu", "");
      } else {
        formLayout = new FormLayout("right:max(20dlu;p), 0dlu, pref, 1dlu, [20dlu,pref,20dlu]:grow, 2dlu, pref, 10dlu,  right:max(20dlu;p), 0dlu, pref, 1dlu, [20dlu,pref,20dlu]:grow, 2dlu, pref, 10dlu,  right:max(20dlu;p), 0dlu, pref, 1dlu, [20dlu,pref,20dlu]:grow, 2dlu, pref, 10dlu,  right:max(20dlu;p), 0dlu, pref, 1dlu, [20dlu,pref,20dlu]:grow, 2dlu, pref ", "");
      } 
      DefaultFormBuilder defaultFormBuilder = new DefaultFormBuilder(formLayout);
      defaultFormBuilder.border((Border)Borders.DIALOG);
      if (this.rest.isQuickSearchEnabled()) {
        JTextField jTextField = new JTextField();
        jTextField.setEditable(false);
        jTextField.setText(this.rest.getQuickSearchString());
        defaultFormBuilder.append("Quick Search (keyword)", jTextField);
      } else {
        for (TabOrder tabOrder : arrayList) {
          boolean bool = false;
          for (SearchCondition searchCondition : treeMap2.get(tabOrder.getTabName())) {
            ContentProviderConfigProperty contentProviderConfigProperty = (ContentProviderConfigProperty)hashMap.get(searchCondition.getField().getLabel());
            if (contentProviderConfigProperty != null && contentProviderConfigProperty.isSearchable()) {
              if (!bool) {
                defaultFormBuilder.appendSeparator(tabOrder.getTabName());
                bool = true;
              } 
              JTextField jTextField = new JTextField();
              jTextField.setText(searchCondition.getRestriction());
              jTextField.getDocument().addDocumentListener(this.textFieldDocumentListener);
              jTextField.getDocument().putProperty("OIFIELD", searchCondition.getField());
              JLabel jLabel = defaultFormBuilder.append(searchCondition.getField().getLabel());
              jLabel.setLabelFor(jTextField);
              JSearchRestrictionCheckBox jSearchRestrictionCheckBox = new JSearchRestrictionCheckBox(searchCondition.getField());
              jSearchRestrictionCheckBox.setSelected(searchCondition.isSelected());
              jSearchRestrictionCheckBox.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent param1ActionEvent) {
                      JSearchRestrictionCheckBox jSearchRestrictionCheckBox = (JSearchRestrictionCheckBox)param1ActionEvent.getSource();
                      try {
                        SearchMask searchMask = ContentProviderSearchRestrictionsPane.this.searchMaskManager.open(ContentProviderSearchRestrictionsPane.this.rest.getOIClass());
                        searchMask.setSelected(jSearchRestrictionCheckBox.getOIField(), jSearchRestrictionCheckBox.isSelected());
                      } catch (Exception exception) {}
                    }
                  });
              defaultFormBuilder.append(jSearchRestrictionCheckBox);
              defaultFormBuilder.append(jTextField);
              jLabel.putClientProperty("CCPID", contentProviderConfigProperty.getContentProviderId());
              jLabel.addMouseListener(this.popupMouseAdapter);
              defaultFormBuilder.append(contentProviderConfigProperty.getBaseUnits());
            } 
          } 
          if (bool)
            defaultFormBuilder.nextLine(); 
        } 
      } 
      this.restScrollPane.getViewport().removeAll();
      JPanel jPanel = defaultFormBuilder.getPanel();
      this.restScrollPane.getViewport().add(jPanel);
      this.restScrollPane.revalidate();
      this.restScrollPane.repaint();
      this.checkBoxTree.clear();
      for (TabOrder tabOrder : arrayList) {
        boolean bool = false;
        for (SearchCondition searchCondition : treeMap2.get(tabOrder.getTabName())) {
          ContentProviderConfigProperty contentProviderConfigProperty = (ContentProviderConfigProperty)hashMap.get(searchCondition.getField().getLabel());
          if (contentProviderConfigProperty != null && !contentProviderConfigProperty.isSearchable()) {
            bool = true;
            break;
          } 
        } 
        if (bool) {
          TabNode tabNode = this.checkBoxTree.addTab(tabOrder.getTabName());
          for (SearchCondition searchCondition : treeMap2.get(tabOrder.getTabName())) {
            ContentProviderConfigProperty contentProviderConfigProperty = (ContentProviderConfigProperty)hashMap.get(searchCondition.getField().getLabel());
            if (contentProviderConfigProperty != null && (this.rest.isQuickSearchEnabled() || !contentProviderConfigProperty.isSearchable()))
              this.checkBoxTree.addCheckBox(tabNode, searchCondition.getField().getLabel(), searchCondition.isSelected(), searchCondition.getField()); 
          } 
        } 
      } 
      this.checkBoxTree.expandAll();
      this.checkBoxTree.refresh();
      this.restScrollPane.requestFocusInWindow();
    } catch (Exception exception) {
      StringWriter stringWriter = new StringWriter();
      PrintWriter printWriter = new PrintWriter(stringWriter);
      exception.printStackTrace(printWriter);
      logger.error(stringWriter.toString());
    } 
  }
  
  private void updateWithNoMapping() {
    FormLayout formLayout = new FormLayout("right:max(20dlu;p), 0dlu, pref, 1dlu, [20dlu,pref,20dlu]:grow, 2dlu, pref, 10dlu,  right:max(20dlu;p), 0dlu, pref, 1dlu, [20dlu,pref,20dlu]:grow, 2dlu, pref, 10dlu,  right:max(20dlu;p), 0dlu, pref, 1dlu, [20dlu,pref,20dlu]:grow, 2dlu, pref, 10dlu,  right:max(20dlu;p), 0dlu, pref, 1dlu, [20dlu,pref,20dlu]:grow, 2dlu, pref ", "");
    DefaultFormBuilder defaultFormBuilder = new DefaultFormBuilder(formLayout);
    defaultFormBuilder.border((Border)Borders.DIALOG);
    defaultFormBuilder.append("Selected Class/Catalog has no mappings to " + this.ccp.getName() + ".");
    this.restScrollPane.getViewport().removeAll();
    JPanel jPanel = defaultFormBuilder.getPanel();
    this.restScrollPane.getViewport().add(jPanel);
    this.restScrollPane.revalidate();
    this.restScrollPane.repaint();
    this.checkBoxTree.clear();
  }
  
  class ValidValuesPopUpMenu extends JPopupMenu implements ActionListener {
    private JMenuItem item;
    
    private JLabel lbl;
    
    public ValidValuesPopUpMenu(JLabel param1JLabel) {
      this.lbl = param1JLabel;
      this.item = new JMenuItem("Set " + ContentProviderSearchRestrictionsPane.this.ccp.getName() + " valid values...", ContentProviderSearchRestrictionsPane.this.ccp.getIcon());
      add(this.item);
      this.item.addActionListener(this);
    }
    
    public void actionPerformed(ActionEvent param1ActionEvent) {
      Map map;
      String str = (String)this.lbl.getClientProperty("CCPID");
      JTextField jTextField = (JTextField)this.lbl.getLabelFor();
      try {
        map = ContentProviderSearchRestrictionsPane.this.ccp.getValidValues(ContentProviderSearchRestrictionsPane.this.rest.getPartClass().getContentProviderId(), str);
      } catch (ContentProviderException contentProviderException) {
        JOptionPane.showMessageDialog(this, contentProviderException.getMessage(), "Select " + ContentProviderSearchRestrictionsPane.this.ccp.getName() + " Valid Value", 1);
        return;
      } 
      if (map == null) {
        JOptionPane.showMessageDialog(this, ContentProviderSearchRestrictionsPane.this.ccp.getName() + " does not provide valid values for '" + ContentProviderSearchRestrictionsPane.this.ccp.getName() + "' at this level.", "Select " + ContentProviderSearchRestrictionsPane.this.ccp.getName() + " Valid Value", 1);
        return;
      } 
      PropertyValueSelectionDlg propertyValueSelectionDlg = new PropertyValueSelectionDlg(ContentProviderSearchWindow.getInstance(), ContentProviderSearchRestrictionsPane.this.ccp, this.lbl.getText(), map.keySet());
      if (!propertyValueSelectionDlg.isCancelled())
        jTextField.setText(propertyValueSelectionDlg.getRestriction()); 
    }
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\plugin\searchui\ContentProviderSearchRestrictionsPane.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */