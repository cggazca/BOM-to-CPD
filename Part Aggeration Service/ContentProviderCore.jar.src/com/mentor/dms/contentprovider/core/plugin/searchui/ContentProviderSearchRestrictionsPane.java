package com.mentor.dms.contentprovider.core.plugin.searchui;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import com.mentor.datafusion.oi.model.OIClass;
import com.mentor.datafusion.oi.model.OIField;
import com.mentor.datafusion.utils.logger.MGLogger;
import com.mentor.dms.contentprovider.core.AbstractContentProvider;
import com.mentor.dms.contentprovider.core.AbstractCriteria;
import com.mentor.dms.contentprovider.core.ContentProviderException;
import com.mentor.dms.contentprovider.core.ContentProviderGlobal;
import com.mentor.dms.contentprovider.core.client.PropertyValueSelectionDlg;
import com.mentor.dms.contentprovider.core.config.ContentProviderConfigContentProviderMap;
import com.mentor.dms.contentprovider.core.config.ContentProviderConfigDMSCatalog;
import com.mentor.dms.contentprovider.core.config.ContentProviderConfigProperty;
import com.mentor.dms.contentprovider.core.config.SearchCapability;
import com.mentor.dms.contentprovider.core.utils.FontUtils;
import com.mentor.dms.contentprovider.core.utils.PropertiesUtils;
import com.mentor.dms.contentprovider.core.utils.UnitUtils;
import com.mentor.dms.ui.searchmask.SearchMask;
import com.mentor.dms.ui.searchmask.SearchMaskManager;
import com.mentor.dms.ui.searchmask.restrictions.SearchCondition;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.text.Document;
import org.apache.commons.lang3.StringUtils;
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
  
  private boolean displayed = true;
  
  private final String searchCharsLayoutSpec = "right:max(20dlu;p), 0dlu, pref, 1dlu, [20dlu,pref,20dlu]:grow, 2dlu, pref, 10dlu,  right:max(20dlu;p), 0dlu, pref, 1dlu, [20dlu,pref,20dlu]:grow, 2dlu, pref, 10dlu,  right:max(20dlu;p), 0dlu, pref, 1dlu, [20dlu,pref,20dlu]:grow, 2dlu, pref, 10dlu,  right:max(20dlu;p), 0dlu, pref, 1dlu, [20dlu,pref,20dlu]:grow, 2dlu, pref ";
  
  private final String quickSearchLayoutSpec = "right:max(20dlu;p), 2dlu, 200dlu";
  
  private HashMap<String, ContentProviderConfigProperty> propMap = new HashMap<>();
  
  Map<String, Double> rangeMap = new HashMap<>();
  
  private static final String REGEXP = "\\||&|>=|>|<=|<";
  
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
            if (ContentProviderSearchRestrictionsPane.this.rest != null && ContentProviderSearchRestrictionsPane.this.rest.getCCPConfigMap() != null && ContentProviderSearchRestrictionsPane.this.rest.getCCPConfigMap() != contentProviderConfigContentProviderMap && ContentProviderSearchRestrictionsPane.this.displayed)
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
    this.restScrollPane = new JScrollPane(jPanel2);
    this.restScrollPane.getVerticalScrollBar().setUnitIncrement(20);
    this.restrictionsPanel.add(this.restScrollPane, "Center");
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
            SearchMask searchMask = searchPanel.getTargetSearchMask();
            Document document = param1DocumentEvent.getDocument();
            OIField oIField = (OIField)document.getProperty("OIFIELD");
            String str = document.getText(0, document.getLength());
            if (oIField.getUnitName() != null && !str.isEmpty()) {
              String[] arrayOfString = str.split("((?<=\\||&|>=|>|<=|<)|(?=\\||&|>=|>|<=|<))");
              for (byte b = 0; b < arrayOfString.length; b++) {
                String str1 = arrayOfString[b];
                if (UnitUtils.isNumberStrPrefix(str1)) {
                  ContentProviderConfigProperty contentProviderConfigProperty = ContentProviderSearchRestrictionsPane.this.propMap.get(oIField.getName());
                  String str2 = UnitUtils.convertUnit(contentProviderConfigProperty.getBaseUnits());
                  if (!str2.contains("-") && !str2.contains(".") && !str2.matches(".*[0-9].*"))
                    str1 = str1.concat(str2); 
                  arrayOfString[b] = str1;
                } 
              } 
              str = String.join("", (CharSequence[])arrayOfString);
            } 
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
      this.displayed = false;
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
          int i = 30;
          for (ContentProviderConfigContentProviderMap contentProviderConfigContentProviderMap : contentProviderConfigDMSCatalog.getContentProviderMaps()) {
            this.contentProviderClassCmb.addItem(contentProviderConfigContentProviderMap);
            int j = FontUtils.getWidth(this.contentProviderClassCmb, contentProviderConfigContentProviderMap.toString());
            if (i < j)
              i = j; 
          } 
          if (this.contentProviderClassCmb.getItemCount() > 0)
            this.contentProviderClassCmb.setSelectedIndex(0); 
          this.contentProviderClassCmb.setPreferredSize(new Dimension(i + 40, this.contentProviderClassCmb.getHeight()));
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
      this.propMap = (HashMap)hashMap;
      TreeMap<Object, Object> treeMap = new TreeMap<>();
      SearchConditionComparator searchConditionComparator = new SearchConditionComparator();
      for (ContentProviderCharacteristic contentProviderCharacteristic : this.rest.getCharacteristics()) {
        OIField<?> oIField = contentProviderCharacteristic.getOIField();
        hashMap.put(oIField.getName(), contentProviderCharacteristic.getProperty());
      } 
      SearchMask searchMask = (SearchMask)this.searchMaskManager.getActive();
      for (SearchCondition searchCondition : searchMask.getSearchConditionList()) {
        String str = this.rest.getTabMap().get(searchCondition.getField().getName());
        if (str != null) {
          TreeSet<SearchCondition> treeSet = (TreeSet)treeMap.get(str);
          if (treeSet == null) {
            treeSet = new TreeSet(searchConditionComparator);
            treeMap.put(str, treeSet);
          } 
          treeSet.add(searchCondition);
        } 
      } 
      ArrayList<TabOrder> arrayList = new ArrayList();
      for (Map.Entry<Object, Object> entry : treeMap.entrySet()) {
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
        formLayout = new FormLayout("right:max(20dlu;p), 2dlu, 200dlu", "pref, 4dlu, 48dlu");
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
        CellConstraints cellConstraints = new CellConstraints();
        JTextArea jTextArea = new JTextArea();
        jTextArea.setEditable(false);
        jTextArea.setBackground(new Color(11584734));
        jTextArea.append("Enter at least 3 characters for keywords.\nWildcards (*?) is not supported.\nRange search is not supported.");
        defaultFormBuilder.nextLine(2);
        defaultFormBuilder.add(new JScrollPane(jTextArea), cellConstraints.xyw(1, defaultFormBuilder.getRow(), 3, "fill, fill"));
      } else {
        logger.debug("Copy restrictions.");
        HashMap hashMap1 = this.ccp.getConfig().getSearchCapabilityMap();
        for (TabOrder tabOrder : arrayList) {
          boolean bool = false;
          for (SearchCondition searchCondition : treeMap.get(tabOrder.getTabName())) {
            ContentProviderConfigProperty contentProviderConfigProperty = (ContentProviderConfigProperty)hashMap.get(searchCondition.getField().getName());
            boolean bool1 = true;
            if (contentProviderConfigProperty != null && contentProviderConfigProperty.isSearchable()) {
              if (!bool) {
                defaultFormBuilder.appendSeparator(tabOrder.getTabName());
                bool = true;
              } 
              JTextField jTextField = new JTextField();
              try {
                if (hashMap1.get(contentProviderConfigProperty.getContentProviderId()) != null && !((SearchCapability)hashMap1.get(contentProviderConfigProperty.getContentProviderId())).isSearchable()) {
                  jTextField.setEditable(false);
                  jTextField.setText("");
                } else if (!StringUtils.isEmpty(searchCondition.getField().getUnitName())) {
                  if (searchCondition.getRestriction() != null && !searchCondition.getRestriction().isEmpty()) {
                    logger.debug("Field:[" + contentProviderConfigProperty.getContentProviderLabel() + "] val:[" + searchCondition.getRestriction() + "]");
                    String str1 = searchCondition.getRestriction();
                    String str2 = UnitUtils.convertUnit(contentProviderConfigProperty.getBaseUnits());
                    this.rangeMap = UnitUtils.getUnitRange(searchCondition.getField().getUnitName());
                    logger.debug("  CP Unit :" + contentProviderConfigProperty.getBaseUnits());
                    String str3 = "";
                    for (Map.Entry<String, Double> entry : this.rangeMap.entrySet()) {
                      if (((Double)entry.getValue()).equals(Double.valueOf(1.0D))) {
                        logger.debug("  EDM Unit:" + (String)entry.getKey());
                        str3 = (String)entry.getKey();
                        break;
                      } 
                    } 
                    String[] arrayOfString = str1.split("((?<=\\||&|>=|>|<=|<)|(?=\\||&|>=|>|<=|<))");
                    for (byte b = 0; b < arrayOfString.length; b++) {
                      String str4 = arrayOfString[b];
                      String str5 = arrayOfString[b];
                      if (UnitUtils.isNumberStr(str5))
                        str4 = str5.concat(str3); 
                      BigDecimal bigDecimal1 = new BigDecimal(1);
                      BigDecimal bigDecimal2 = null;
                      DecimalFormat decimalFormat = new DecimalFormat("#.#");
                      for (Map.Entry<String, Double> entry : this.rangeMap.entrySet()) {
                        if (((String)entry.getKey()).equals(str2))
                          bigDecimal1 = BigDecimal.valueOf(((Double)entry.getValue()).doubleValue()); 
                        if (((Double)entry.getValue()).doubleValue() != 1.0D && !((String)entry.getKey()).equals(str3) && str4.endsWith((String)entry.getKey())) {
                          String str6 = StringUtils.removeEnd(str4, (String)entry.getKey());
                          if (!UnitUtils.isNumberStr(str6))
                            continue; 
                          BigDecimal bigDecimal = new BigDecimal(str6);
                          decimalFormat.setMaximumFractionDigits(bigDecimal.scale());
                          decimalFormat.setMinimumFractionDigits(bigDecimal.scale());
                          bigDecimal2 = bigDecimal.multiply(BigDecimal.valueOf(((Double)entry.getValue()).doubleValue()));
                        } 
                      } 
                      try {
                        if (bigDecimal2 == null) {
                          bigDecimal2 = new BigDecimal(StringUtils.removeEnd(str4, str3));
                          BigDecimal bigDecimal = bigDecimal2.divide(bigDecimal1);
                          String str6 = bigDecimal.stripTrailingZeros().toPlainString();
                          boolean bool2 = str6.contains(".") ? str6.split("\\.")[1].length() : false;
                          decimalFormat.setMaximumFractionDigits(bool2);
                          decimalFormat.setMinimumFractionDigits(0);
                        } 
                        str4 = decimalFormat.format(bigDecimal2.divide(bigDecimal1));
                      } catch (NumberFormatException numberFormatException) {}
                      arrayOfString[b] = str4;
                    } 
                    jTextField.setText(String.join("", (CharSequence[])arrayOfString));
                    logger.debug("  Restriction value:[" + jTextField.getText() + "]");
                  } 
                } else {
                  jTextField.setText(searchCondition.getRestriction());
                } 
              } catch (Throwable throwable) {
                jTextField.setText(searchCondition.getRestriction());
              } 
              jTextField.getDocument().addDocumentListener(this.textFieldDocumentListener);
              jTextField.getDocument().putProperty("OIFIELD", searchCondition.getField());
              if (this.ccp.isWideField(contentProviderConfigProperty.getContentProviderId())) {
                defaultFormBuilder.nextLine();
                bool1 += true;
              } 
              String str = PropertiesUtils.replace(searchCondition.getField().getLabel());
              JLabel jLabel = defaultFormBuilder.append(str);
              jLabel.setLabelFor(jTextField);
              JSearchRestrictionCheckBox jSearchRestrictionCheckBox = new JSearchRestrictionCheckBox(searchCondition.getField());
              jSearchRestrictionCheckBox.setSelected(searchCondition.isSelected());
              jSearchRestrictionCheckBox.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent param1ActionEvent) {
                      JSearchRestrictionCheckBox jSearchRestrictionCheckBox = (JSearchRestrictionCheckBox)param1ActionEvent.getSource();
                      try {
                        SearchMask searchMask = ContentProviderSearchRestrictionsPane.this.searchPanel.getTargetSearchMask();
                        searchMask.setSelected(jSearchRestrictionCheckBox.getOIField(), jSearchRestrictionCheckBox.isSelected());
                      } catch (Exception exception) {}
                    }
                  });
              defaultFormBuilder.append(jSearchRestrictionCheckBox);
              defaultFormBuilder.append(jTextField, bool1);
              jLabel.putClientProperty("CCPID", contentProviderConfigProperty.getContentProviderId());
              jLabel.addMouseListener(this.popupMouseAdapter);
              defaultFormBuilder.append(contentProviderConfigProperty.getBaseUnits());
            } 
          } 
          if (bool)
            defaultFormBuilder.nextLine(); 
        } 
      } 
      Point point = this.restScrollPane.getViewport().getViewPosition();
      this.restScrollPane.getViewport().removeAll();
      JPanel jPanel = defaultFormBuilder.getPanel();
      this.restScrollPane.getViewport().add(jPanel);
      this.restScrollPane.revalidate();
      this.restScrollPane.repaint();
      if (!paramBoolean)
        this.restScrollPane.getViewport().setViewPosition(point); 
      this.restScrollPane.requestFocusInWindow();
    } catch (Exception exception) {
      StringWriter stringWriter = new StringWriter();
      PrintWriter printWriter = new PrintWriter(stringWriter);
      exception.printStackTrace(printWriter);
      logger.error(stringWriter.toString());
    } 
    this.displayed = true;
  }
  
  private void updateWithNoMapping() {
    FormLayout formLayout = new FormLayout("right:max(20dlu;p), 0dlu, pref, 1dlu, [20dlu,pref,20dlu]:grow, 2dlu, pref, 10dlu,  right:max(20dlu;p), 0dlu, pref, 1dlu, [20dlu,pref,20dlu]:grow, 2dlu, pref, 10dlu,  right:max(20dlu;p), 0dlu, pref, 1dlu, [20dlu,pref,20dlu]:grow, 2dlu, pref, 10dlu,  right:max(20dlu;p), 0dlu, pref, 1dlu, [20dlu,pref,20dlu]:grow, 2dlu, pref ", "");
    DefaultFormBuilder defaultFormBuilder = new DefaultFormBuilder(formLayout);
    defaultFormBuilder.border((Border)Borders.DIALOG);
    this.displayed = false;
    defaultFormBuilder.append("Selected Class/Catalog has no mappings to " + this.ccp.getName() + ".");
    this.restScrollPane.getViewport().removeAll();
    JPanel jPanel = defaultFormBuilder.getPanel();
    this.restScrollPane.getViewport().add(jPanel);
    this.restScrollPane.revalidate();
    this.restScrollPane.repaint();
    this.displayed = true;
  }
  
  public void setWaitCursor(boolean paramBoolean) {
    setCursor(paramBoolean ? new Cursor(3) : null);
  }
  
  class ValidValuesPopUpMenu extends JPopupMenu implements ActionListener {
    private JMenuItem item;
    
    private JLabel lbl;
    
    public ValidValuesPopUpMenu(JLabel param1JLabel) {
      this.lbl = param1JLabel;
      this.item = new JMenuItem("Set " + ContentProviderSearchRestrictionsPane.this.ccp.getName() + " Qualified Values...", ContentProviderSearchRestrictionsPane.this.ccp.getIcon());
      add(this.item);
      this.item.addActionListener(this);
    }
    
    public void actionPerformed(ActionEvent param1ActionEvent) {
      List list;
      String str = (String)this.lbl.getClientProperty("CCPID");
      ContentProviderSearchRestrictionsPane.logger.info("Set Qualified Values. Property ID:[" + str + "]");
      JTextField jTextField = (JTextField)this.lbl.getLabelFor();
      ContentProviderSearchRestrictionsPane.this.setWaitCursor(true);
      try {
        HashMap hashMap = ContentProviderSearchRestrictionsPane.this.ccp.getConfig().getSearchCapabilityMap();
        boolean bool = ((SearchCapability)hashMap.get(str)).isFacet();
        if (!bool) {
          JOptionPane.showMessageDialog(this, "Valid values for \"" + this.lbl.getText() + "\" is not available.", "Select " + ContentProviderSearchRestrictionsPane.this.ccp.getName() + " Valid Value", 1);
          return;
        } 
        jTextField.setText("");
        AbstractCriteria abstractCriteria = ContentProviderSearchRestrictionsPane.this.searchPanel.getFacetsSearchFilteringCriteria();
        if (abstractCriteria == null)
          return; 
        if (ContentProviderSearchRestrictionsPane.this.searchPanel.rootFacet()) {
          JOptionPane.showMessageDialog(this, "\"Supplyframe Part Classes Root\" requires one or more search criteria.", "Select " + ContentProviderSearchRestrictionsPane.this.ccp.getName() + " Valid Value", 1);
          return;
        } 
        list = ContentProviderSearchRestrictionsPane.this.ccp.getValidValues(ContentProviderSearchRestrictionsPane.this.rest.getPartClass().getContentProviderId(), str, abstractCriteria);
      } catch (ContentProviderException contentProviderException) {
        ContentProviderSearchRestrictionsPane.this.searchPanel.setStatus(true, "'Valid values' acquisition failed : " + contentProviderException.getMessage());
        return;
      } finally {
        ContentProviderSearchRestrictionsPane.this.setWaitCursor(false);
      } 
      if (list == null || list.isEmpty()) {
        JOptionPane.showMessageDialog(this, "No valid values found for \"" + this.lbl.getText() + "\".", "Select " + ContentProviderSearchRestrictionsPane.this.ccp.getName() + " Valid Value", 1);
        return;
      } 
      PropertyValueSelectionDlg propertyValueSelectionDlg = new PropertyValueSelectionDlg(ContentProviderSearchWindow.getInstance(), ContentProviderSearchRestrictionsPane.this.ccp, this.lbl.getText(), list);
      if (!propertyValueSelectionDlg.isCancelled())
        jTextField.setText(propertyValueSelectionDlg.getRestriction()); 
    }
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\plugin\searchui\ContentProviderSearchRestrictionsPane.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */