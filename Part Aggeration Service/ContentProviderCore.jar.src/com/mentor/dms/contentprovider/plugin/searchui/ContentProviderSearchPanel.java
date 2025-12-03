package com.mentor.dms.contentprovider.plugin.searchui;

import com.mentor.datafusion.oi.OICursor;
import com.mentor.datafusion.oi.OIQuery;
import com.mentor.datafusion.oi.model.OIClass;
import com.mentor.datafusion.oi.model.OIField;
import com.mentor.datafusion.utils.logger.MGLogger;
import com.mentor.dms.contentprovider.AbstractContentProvider;
import com.mentor.dms.contentprovider.AbstractCriteria;
import com.mentor.dms.contentprovider.ContentProviderException;
import com.mentor.dms.contentprovider.ContentProviderGlobal;
import com.mentor.dms.contentprovider.DefaultDisplayColumn;
import com.mentor.dms.contentprovider.IContentProviderResultRecord;
import com.mentor.dms.contentprovider.config.ContentProviderConfigComponentCatalog;
import com.mentor.dms.contentprovider.config.ContentProviderConfigContentProviderMap;
import com.mentor.dms.contentprovider.config.ContentProviderConfigDMSCatalog;
import com.mentor.dms.contentprovider.config.ContentProviderConfigMPNCatalog;
import com.mentor.dms.contentprovider.config.ContentProviderConfigProperty;
import com.mentor.dms.contentprovider.config.ContentProviderConfigPropertyMap;
import com.mentor.dms.contentprovider.criterion.ICriterion;
import com.mentor.dms.contentprovider.criterion.KeywordExpression;
import com.mentor.dms.contentprovider.criterion.Restrictions;
import com.mentor.dms.contentprovider.plugin.DMSDoubleExprLexer;
import com.mentor.dms.contentprovider.plugin.DMSDoubleExprParser;
import com.mentor.dms.contentprovider.plugin.DMSStringExprLexer;
import com.mentor.dms.contentprovider.plugin.DMSStringExprParser;
import com.mentor.dms.ui.DMSInstance;
import com.mentor.dms.ui.searchmask.SearchMask;
import com.mentor.dms.ui.searchmask.SearchMaskManager;
import com.mentor.dms.ui.searchmask.restrictions.SearchCondition;
import java.awt.Component;
import java.awt.Dimension;
import java.io.ByteArrayInputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.border.EmptyBorder;
import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.TokenSource;
import org.antlr.runtime.TokenStream;
import org.antlr.runtime.tree.CommonTree;
import org.apache.commons.codec.digest.DigestUtils;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTitledPanel;
import org.jdesktop.swingx.table.TableColumnExt;

public class ContentProviderSearchPanel extends JSplitPane {
  private static MGLogger logger = MGLogger.getLogger(ContentProviderSearchPanel.class);
  
  private ContentProviderSearchMainPanel mainPanel = null;
  
  private ContentProviderSearchTabComponent tabComponent = null;
  
  private ContentProviderSearchRestrictionsPane searchRestrictionsPane = null;
  
  private ContentProviderSearchResultsTableModel tm = null;
  
  private ContentProviderSearchResultsJXTable table = null;
  
  private AbstractContentProvider ccp = null;
  
  private ContentProviderSearchRestriction rest = new ContentProviderSearchRestriction();
  
  private AbstractCriteria crit;
  
  private String lastSearchClass = "";
  
  private String lastSearchRestrictionChecksum = "";
  
  private boolean bNextSearch = false;
  
  public ContentProviderSearchPanel(ContentProviderSearchMainPanel paramContentProviderSearchMainPanel, ContentProviderSearchTabComponent paramContentProviderSearchTabComponent, AbstractContentProvider paramAbstractContentProvider) {
    super(0);
    setOneTouchExpandable(true);
    setDividerLocation(400);
    this.mainPanel = paramContentProviderSearchMainPanel;
    this.tabComponent = paramContentProviderSearchTabComponent;
    this.ccp = paramAbstractContentProvider;
    this.searchRestrictionsPane = new ContentProviderSearchRestrictionsPane(this, paramAbstractContentProvider);
    setTopComponent((Component)this.searchRestrictionsPane);
    this.tm = new ContentProviderSearchResultsTableModel(paramAbstractContentProvider);
    this.table = new ContentProviderSearchResultsJXTable(this.tm, paramAbstractContentProvider);
    JScrollPane jScrollPane = new JScrollPane((Component)this.table);
    jScrollPane.setAlignmentX(0.0F);
    JXTitledPanel jXTitledPanel = new JXTitledPanel("<html><b>Search Results</b><br/>* <i>Highlighted items are an indication that part exists in EDM Library</i></html>");
    JXPanel jXPanel = new JXPanel();
    jXPanel.setPreferredSize(new Dimension(18, 24));
    jXPanel.setOpaque(false);
    ImageIcon imageIcon = new ImageIcon(ContentProviderSearchMainPanel.class.getResource("images/documents.png"));
    jXPanel.add(new JLabel(imageIcon));
    jXTitledPanel.setLeftDecoration((JComponent)jXPanel);
    jXTitledPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
    jXTitledPanel.add(jScrollPane);
    setBottomComponent((Component)jXTitledPanel);
  }
  
  public void doSearch(boolean paramBoolean) throws ContentProviderException {
    this.bNextSearch = paramBoolean;
    if (!this.mainPanel.isSearchEnabled())
      return; 
    try {
      DMSInstance dMSInstance = ContentProviderGlobal.getDMSInstance();
      if (dMSInstance != null) {
        SearchMaskManager searchMaskManager = dMSInstance.getSearchMaskManager();
        SearchMask searchMask = (SearchMask)searchMaskManager.getActive();
        if (searchMask == null) {
          setStatus("There is no active search mask.");
          return;
        } 
      } 
      if (!paramBoolean) {
        this.tm.reset();
      } else {
        this.tm.resetColumns();
      } 
      setSearchEnabled(false);
      setNextEnabled(false);
      setSearchIcon();
      setWaitCursor(true);
    } catch (Exception exception) {
      setStatus(exception.getMessage());
      setSearchEnabled(true);
      setWarningIcon(exception.getMessage());
      setWaitCursor(false);
      return;
    } 
    setStatus("Loading Content Provider Configuration for '" + this.ccp.getName() + "'...");
    LoadConfigAndSearchTask loadConfigAndSearchTask = new LoadConfigAndSearchTask(this);
    loadConfigAndSearchTask.execute();
  }
  
  protected void doSearchTask() throws ContentProviderException {
    try {
      doUpdateRestrictions();
      if (this.rest.getDmsCatalogConfig() == null) {
        this.tm.fireTableStructureChanged();
        setStatus("There is no searchable class defined for '" + this.rest.getOIClass().getLabel() + "' for Content Provider '" + this.ccp.getName() + "'.");
        setReturnCount(-1);
        setSearchEnabled(true);
        setWaitCursor(false);
        return;
      } 
      ContentProviderConfigContentProviderMap contentProviderConfigContentProviderMap = this.searchRestrictionsPane.getSelectedContentProviderMap();
      if (contentProviderConfigContentProviderMap == null) {
        this.tm.fireTableStructureChanged();
        setStatus("Catalog '" + this.rest.getOIClass().getLabel() + "' is not mapped to a search class in Content Provider '" + this.ccp.getName() + "'.");
        setReturnCount(-1);
        setSearchEnabled(true);
        setWaitCursor(false);
        return;
      } 
      this.crit = this.ccp.createCriteria();
      this.crit.setMaxResults(50);
      DMSStringExprLexer dMSStringExprLexer = new DMSStringExprLexer();
      CommonTokenStream commonTokenStream1 = new CommonTokenStream((TokenSource)dMSStringExprLexer);
      DMSStringExprParser dMSStringExprParser = new DMSStringExprParser((TokenStream)commonTokenStream1);
      DMSDoubleExprLexer dMSDoubleExprLexer = new DMSDoubleExprLexer();
      CommonTokenStream commonTokenStream2 = new CommonTokenStream((TokenSource)dMSDoubleExprLexer);
      DMSDoubleExprParser dMSDoubleExprParser = new DMSDoubleExprParser((TokenStream)commonTokenStream2);
      Collection collection = this.ccp.getDefaultDisplayColumns();
      for (DefaultDisplayColumn defaultDisplayColumn : collection) {
        ContentProviderConfigProperty contentProviderConfigProperty = this.rest.getPartClass().getClassPropertyByContentProviderId(defaultDisplayColumn.getContentProviderPropertyId());
        if (contentProviderConfigProperty != null)
          this.tm.addColumn(new ContentProviderSearchResultsColumn(defaultDisplayColumn.getColumnLabel(), contentProviderConfigProperty)); 
      } 
      boolean bool = DMSQuickSearch.isVisible();
      if (this.rest.isQuickSearchEnabled())
        this.crit.add((ICriterion)Restrictions.keyword(this.rest.getQuickSearchString(), KeywordExpression.MatchType.ALL)); 
      SearchMaskManager searchMaskManager = ContentProviderGlobal.getDMSInstance().getSearchMaskManager();
      SearchMask searchMask = (SearchMask)searchMaskManager.getActive();
      for (ContentProviderCharacteristic contentProviderCharacteristic : this.rest.getCharacteristics()) {
        SearchCondition searchCondition = searchMask.getSearchCondition(contentProviderCharacteristic.getOIField());
        if (searchCondition.isSelected() || !searchCondition.getRestriction().isEmpty()) {
          ContentProviderConfigProperty contentProviderConfigProperty = contentProviderCharacteristic.getProperty();
          if (contentProviderConfigProperty != null && searchCondition.isSelected()) {
            boolean bool1 = false;
            for (DefaultDisplayColumn defaultDisplayColumn : collection) {
              if (contentProviderConfigProperty.getContentProviderId().equals(defaultDisplayColumn.getContentProviderPropertyId())) {
                bool1 = true;
                break;
              } 
            } 
            if (!bool1)
              this.tm.addColumn(new ContentProviderSearchResultsColumn(searchCondition.getField().getLabel(), contentProviderConfigProperty)); 
          } 
          if (!bool && !searchCondition.getRestriction().isEmpty()) {
            if (contentProviderConfigProperty == null) {
              logger.info("Characteristic '" + searchCondition.getField().getLabel() + "' is not found in Content Provider configuration.  Skipping...");
              continue;
            } 
            if (!contentProviderConfigProperty.isSearchable()) {
              logger.info("Characteristic '" + searchCondition.getField().getLabel() + "' not set to searchable in the Content Provider configuration.  Unable to add search restriction.");
              continue;
            } 
            String str = searchCondition.getRestriction();
            ANTLRInputStream aNTLRInputStream = new ANTLRInputStream(new ByteArrayInputStream(str.getBytes()));
            if (searchCondition.getField().getType() == OIField.Type.STRING || searchCondition.getField().getType() == OIField.Type.REFERENCE) {
              dMSStringExprLexer.setCharStream((CharStream)aNTLRInputStream);
              commonTokenStream1.setTokenSource((TokenSource)dMSStringExprLexer);
              dMSStringExprParser.setTokenStream((TokenStream)commonTokenStream1);
              DMSStringExprParser.prog_return prog_return = dMSStringExprParser.prog();
              if (dMSStringExprParser.getNumberOfSyntaxErrors() > 0)
                throw new ContentProviderException("Error in query syntax for '" + searchCondition.getField().getLabel()); 
              CommonTree commonTree = prog_return.getTree();
              this.crit.add(AbstractCriteria.stringGetCriteria(contentProviderConfigProperty, commonTree));
              continue;
            } 
            if (searchCondition.getField().getType() == OIField.Type.DOUBLE || searchCondition.getField().getType() == OIField.Type.INTEGER) {
              dMSDoubleExprLexer.setCharStream((CharStream)aNTLRInputStream);
              commonTokenStream2.setTokenSource((TokenSource)dMSDoubleExprLexer);
              dMSDoubleExprParser.setTokenStream((TokenStream)commonTokenStream2);
              DMSDoubleExprParser.prog_return prog_return = dMSDoubleExprParser.prog();
              if (dMSStringExprParser.getNumberOfSyntaxErrors() > 0)
                throw new ContentProviderException("Error in query syntax for '" + searchCondition.getField().getLabel() + "'."); 
              CommonTree commonTree = prog_return.getTree();
              this.crit.add(AbstractCriteria.doubleGetCriteria(contentProviderConfigProperty, commonTree));
            } 
          } 
        } 
      } 
    } catch (Exception exception) {
      setStatus(exception.getMessage());
      setSearchEnabled(true);
      setWaitCursor(false);
      return;
    } 
    setStatus("Querying Content Provider...");
    ContentProviderSearchTask contentProviderSearchTask = new ContentProviderSearchTask(this);
    contentProviderSearchTask.execute();
  }
  
  protected AbstractContentProvider getContentProvider() {
    return this.ccp;
  }
  
  protected ContentProviderSearchResultsTableModel getTableModel() {
    return this.tm;
  }
  
  protected ContentProviderSearchResultsJXTable getTable() {
    return this.table;
  }
  
  protected AbstractCriteria getSearchCriteria() {
    return this.crit;
  }
  
  protected void setSearchEnabled(boolean paramBoolean) {
    this.mainPanel.setSearchEnabled(paramBoolean);
  }
  
  protected void setNextEnabled(boolean paramBoolean) {
    this.mainPanel.setNextEnabled(paramBoolean);
  }
  
  protected boolean isSearchEnabled() {
    return this.mainPanel.isSearchEnabled();
  }
  
  protected void setStatus(String paramString) {
    this.mainPanel.setStatus(paramString);
  }
  
  public void setWaitCursor(boolean paramBoolean) {
    this.mainPanel.setWaitCursor(paramBoolean);
  }
  
  public void setSearchIcon() {
    this.tabComponent.setSearchIcon();
  }
  
  void setReturnCount(int paramInt) {
    this.tabComponent.setReturnCount(paramInt);
  }
  
  public void setWarningIcon(String paramString) {
    this.tabComponent.setWarningIcon(paramString);
  }
  
  public void doUpdateRestrictions() {
    StringBuilder stringBuilder = new StringBuilder();
    boolean bool = false;
    try {
      ContentProviderConfigMPNCatalog contentProviderConfigMPNCatalog;
      DMSInstance dMSInstance = ContentProviderGlobal.getDMSInstance();
      SearchMaskManager searchMaskManager = dMSInstance.getSearchMaskManager();
      SearchMask searchMask = (SearchMask)searchMaskManager.getActive();
      this.rest = new ContentProviderSearchRestriction();
      if (searchMask == null)
        return; 
      boolean bool1 = DMSQuickSearch.isVisible();
      if (bool1) {
        String str = DMSQuickSearch.getSearchString();
        this.rest.setQuickSearchEnabled(true);
        this.rest.setQuickSearchString(str);
        stringBuilder.append("<QuickSearch>" + str);
      } 
      OIClass oIClass = searchMask.getOIClass();
      if (!this.ccp.isClassificationSearchSupported()) {
        oIClass = oIClass.getRootClass();
        if (oIClass.getSubclasses().size() > 0)
          oIClass = oIClass.getSubclasses().iterator().next(); 
      } 
      this.rest.setOIClass(oIClass);
      stringBuilder.append("<EDMSearchClass>" + oIClass.getName());
      bool = !this.lastSearchClass.equals(oIClass.getName()) ? true : false;
      String str2 = oIClass.getRootClass().getName();
      HashMap<Object, Object> hashMap1 = (HashMap)ContentProviderSearchRestriction.getTabMapCache().get(str2);
      if (hashMap1 == null) {
        hashMap1 = new HashMap<>();
        OIQuery oIQuery = dMSInstance.getObjectManager().createQuery("Characteristic", true);
        oIQuery.addRestriction("ObjectClass", str2.equals("Component") ? "1" : "60");
        oIQuery.addRestriction("Text.Language", "e");
        oIQuery.addColumn("DomainModelName");
        oIQuery.addColumn("Text.TabSheet");
        OICursor oICursor = oIQuery.execute();
        while (oICursor.next())
          hashMap1.put(oICursor.getString("DomainModelName"), oICursor.getString("TabSheet")); 
        oICursor.close();
        ContentProviderSearchRestriction.getTabMapCache().put(str2, hashMap1);
      } 
      ContentProviderConfigComponentCatalog contentProviderConfigComponentCatalog = null;
      if (str2.equals("Component")) {
        contentProviderConfigComponentCatalog = this.ccp.getConfig().getComponentCatalogConfigByDMN(oIClass.getName());
      } else if (str2.equals("ManufacturerPart")) {
        contentProviderConfigMPNCatalog = this.ccp.getConfig().getMPNCatalogConfigByDMN(oIClass.getName());
      } 
      this.rest.setDMSCatalogConfig((ContentProviderConfigDMSCatalog<?>)contentProviderConfigMPNCatalog);
      if (contentProviderConfigMPNCatalog == null || contentProviderConfigMPNCatalog.getContentProviderMaps().isEmpty())
        return; 
      ContentProviderConfigContentProviderMap contentProviderConfigContentProviderMap = null;
      if (bool) {
        if (!contentProviderConfigMPNCatalog.getContentProviderMaps().isEmpty())
          contentProviderConfigContentProviderMap = contentProviderConfigMPNCatalog.getContentProviderMaps().iterator().next(); 
      } else {
        contentProviderConfigContentProviderMap = this.searchRestrictionsPane.getSelectedContentProviderMap();
      } 
      if (contentProviderConfigContentProviderMap == null)
        return; 
      this.rest.setCCPConfigMap(contentProviderConfigContentProviderMap);
      this.rest.setPartClass(contentProviderConfigContentProviderMap.getPartClass());
      stringBuilder.append("<ContentProviderClass>" + contentProviderConfigContentProviderMap.getContentProviderId());
      HashMap<Object, Object> hashMap2 = (HashMap)ContentProviderSearchRestriction.getTabMapCache().get(oIClass.getName());
      if (hashMap2 == null) {
        hashMap2 = new HashMap<>();
        hashMap2.putAll(hashMap1);
        OIQuery oIQuery = dMSInstance.getObjectManager().createQuery("CatalogGroup", true);
        oIQuery.addRestriction("DomainModelName", oIClass.getName());
        oIQuery.addAlias("DomainModelName", "CatalogDomainModelName");
        oIQuery.addColumn("CatalogCharacteristics.Characteristic.DomainModelName");
        oIQuery.addColumn("CatalogCharacteristics.TabSheet");
        OICursor oICursor = oIQuery.execute();
        while (oICursor.next()) {
          String str = oICursor.getString("TabSheet");
          if (!str.isEmpty())
            hashMap2.put(oICursor.getString("DomainModelName"), str); 
        } 
        ContentProviderSearchRestriction.getTabMapCache().put(oIClass.getName(), hashMap2);
      } 
      this.rest.setTabMap((HashMap)hashMap2);
      for (SearchCondition searchCondition : searchMask.getSearchConditionList()) {
        if (searchCondition.getField().getType() == OIField.Type.DOUBLE || searchCondition.getField().getType() == OIField.Type.INTEGER || searchCondition.getField().getType() == OIField.Type.STRING || searchCondition.getField().getType() == OIField.Type.DATE || searchCondition.getField().getType() == OIField.Type.REFERENCE) {
          ContentProviderConfigPropertyMap contentProviderConfigPropertyMap = null;
          contentProviderConfigPropertyMap = contentProviderConfigContentProviderMap.getPropertyMapBySearchDMN(searchCondition.getField().getName());
          ContentProviderConfigProperty contentProviderConfigProperty = null;
          if (contentProviderConfigPropertyMap != null)
            contentProviderConfigProperty = contentProviderConfigContentProviderMap.getPartClass().getClassPropertyByContentProviderId(contentProviderConfigPropertyMap.getContentProviderId()); 
          if (contentProviderConfigProperty != null) {
            this.rest.addCharacteristic(searchCondition, contentProviderConfigProperty);
            if (contentProviderConfigProperty.isSearchable())
              stringBuilder.append(":" + searchCondition.getField().getName() + ":" + searchCondition.getRestriction()); 
          } 
        } 
      } 
    } catch (Exception exception) {
      StringWriter stringWriter = new StringWriter();
      PrintWriter printWriter = new PrintWriter(stringWriter);
      exception.printStackTrace(printWriter);
      logger.error(stringWriter.toString());
    } finally {
      this.searchRestrictionsPane.updateRestrictions(this.rest, bool);
      this.lastSearchClass = this.rest.getOIClass().getName();
      String str = DigestUtils.md5Hex(stringBuilder.toString());
      if (!str.equals(this.lastSearchRestrictionChecksum)) {
        this.mainPanel.setNextEnabled(false);
        this.lastSearchRestrictionChecksum = str;
      } 
    } 
  }
  
  public void updateRestrictions() throws ContentProviderException {
    LoadConfigAndUpdateRestrictionTextTask loadConfigAndUpdateRestrictionTextTask = new LoadConfigAndUpdateRestrictionTextTask(this);
    loadConfigAndUpdateRestrictionTextTask.execute();
  }
  
  public Collection<IContentProviderResultRecord> getSelectedResults() {
    ArrayList<IContentProviderResultRecord> arrayList = new ArrayList();
    for (int i : this.table.getSelectedRows()) {
      int j = this.table.convertRowIndexToModel(i);
      IContentProviderResultRecord iContentProviderResultRecord = this.tm.getResultRecordAt(j);
      arrayList.add(iContentProviderResultRecord);
    } 
    return arrayList;
  }
  
  public int getSelectionCount() {
    return this.table.getSelectedRowCount();
  }
  
  public void setComplete(boolean paramBoolean) {
    this.tm.fireTableStructureChanged();
    this.tm.fireTableDataChanged();
    if (this.tm.getRowCount() == 0) {
      setSearchEnabled(true);
      setWaitCursor(false);
      return;
    } 
    for (byte b = 0; b < this.table.getRowCount(); b++) {
      int i = this.table.getRowHeight();
      for (byte b1 = 0; b1 < this.table.getColumnCount(); b1++) {
        Component component = this.table.prepareRenderer(this.table.getCellRenderer(b, b1), b, b1);
        i = Math.max(i, (component.getPreferredSize()).height);
      } 
      this.table.setRowHeight(b, i);
    } 
    this.table.packAll();
    TableColumnExt tableColumnExt = this.table.getColumnExt(0);
    tableColumnExt.setMaxWidth(40);
    tableColumnExt = this.table.getColumnExt(1);
    if (paramBoolean)
      tableColumnExt.setMaxWidth(80); 
    setSearchEnabled(true);
    setNextEnabled(getContentProvider().hasNextParts());
    setWaitCursor(false);
  }
  
  public boolean isNextSearch() {
    return this.bNextSearch;
  }
  
  public ContentProviderSearchRestriction getSearchRestriction() {
    return this.rest;
  }
  
  public void restrictionChangeEvent() {
    this.mainPanel.setNextEnabled(false);
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\plugin\searchui\ContentProviderSearchPanel.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */