package com.mentor.dms.contentprovider.core.plugin.searchui;

import com.mentor.datafusion.oi.OICursor;
import com.mentor.datafusion.oi.OIQuery;
import com.mentor.datafusion.oi.model.OIClass;
import com.mentor.datafusion.oi.model.OIField;
import com.mentor.datafusion.utils.logger.MGLogger;
import com.mentor.dms.contentprovider.core.AbstractContentProvider;
import com.mentor.dms.contentprovider.core.AbstractCriteria;
import com.mentor.dms.contentprovider.core.ContentProviderException;
import com.mentor.dms.contentprovider.core.ContentProviderGlobal;
import com.mentor.dms.contentprovider.core.DefaultDisplayColumn;
import com.mentor.dms.contentprovider.core.IContentProviderResultRecord;
import com.mentor.dms.contentprovider.core.client.CheckLicense;
import com.mentor.dms.contentprovider.core.config.ContentProviderConfigComponentCatalog;
import com.mentor.dms.contentprovider.core.config.ContentProviderConfigContentProviderMap;
import com.mentor.dms.contentprovider.core.config.ContentProviderConfigDMSCatalog;
import com.mentor.dms.contentprovider.core.config.ContentProviderConfigMPNCatalog;
import com.mentor.dms.contentprovider.core.config.ContentProviderConfigProperty;
import com.mentor.dms.contentprovider.core.config.ContentProviderConfigPropertyMap;
import com.mentor.dms.contentprovider.core.config.SearchCapability;
import com.mentor.dms.contentprovider.core.criterion.ICriterion;
import com.mentor.dms.contentprovider.core.criterion.KeywordExpression;
import com.mentor.dms.contentprovider.core.criterion.Restrictions;
import com.mentor.dms.contentprovider.core.plugin.DMSDoubleExprLexer;
import com.mentor.dms.contentprovider.core.plugin.DMSDoubleExprParser;
import com.mentor.dms.contentprovider.core.plugin.DMSStringExprLexer;
import com.mentor.dms.contentprovider.core.plugin.DMSStringExprParser;
import com.mentor.dms.contentprovider.core.utils.DateUtils;
import com.mentor.dms.contentprovider.core.utils.PropertiesUtils;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
  
  private JLabel countText;
  
  private AbstractContentProvider ccp = null;
  
  private ContentProviderSearchRestriction rest = new ContentProviderSearchRestriction();
  
  private AbstractCriteria crit;
  
  private String lastSearchClass = "";
  
  private String lastSearchRestrictionChecksum = "";
  
  private boolean bNextSearch = false;
  
  private SearchMask targetSearchMask = null;
  
  private static final String REGEX_DATE = "[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01]) ([01][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]";
  
  SearchMask getTargetSearchMask() {
    return this.targetSearchMask;
  }
  
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
    JXTitledPanel jXTitledPanel = new JXTitledPanel("<html><b>Search Results</b><br/>* <i>Blue and Bold items are an indication that part exists in EDM Library</i></html>");
    JXPanel jXPanel = new JXPanel();
    jXPanel.setPreferredSize(new Dimension(18, 24));
    jXPanel.setOpaque(false);
    ImageIcon imageIcon = new ImageIcon(ContentProviderSearchMainPanel.class.getResource("images/documents.png"));
    jXPanel.add(new JLabel(imageIcon));
    jXTitledPanel.setLeftDecoration((JComponent)jXPanel);
    this.countText = new JLabel();
    this.countText.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
    this.countText.setOpaque(false);
    jXTitledPanel.setRightDecoration(this.countText);
    jXTitledPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
    jXTitledPanel.add(jScrollPane);
    setBottomComponent((Component)jXTitledPanel);
  }
  
  public ContentProviderSearchPanel(AbstractContentProvider paramAbstractContentProvider) {
    this.ccp = paramAbstractContentProvider;
  }
  
  public void doSearch(boolean paramBoolean) throws ContentProviderException {
    this.bNextSearch = paramBoolean;
    if (!this.mainPanel.isSearchEnabled())
      return; 
    try {
      if (!CheckLicense.hasSupplyChainLicense())
        throw new Exception("EDM Server doesn't provide such functionality, please contact with the system administrator."); 
      DMSInstance dMSInstance = ContentProviderGlobal.getDMSInstance();
      if (dMSInstance != null) {
        SearchMaskManager searchMaskManager = dMSInstance.getSearchMaskManager();
        SearchMask searchMask = (SearchMask)searchMaskManager.getActive();
        if (searchMask == null) {
          setStatus("There is no active search mask.");
          return;
        } 
      } 
      if (DMSQuickSearch.isVisible() && DMSQuickSearch.getSearchString().length() < 3) {
        this.table.resetSortOrder();
        this.tm.reset();
        this.tm.fireTableDataChanged();
        throw new Exception("Search Condition Error : Please enter at least 3 characters for search condition.");
      } 
      if (!paramBoolean) {
        this.table.resetSortOrder();
        this.tm.reset();
        this.tm.fireTableDataChanged();
      } else {
        this.tm.resetColumns();
      } 
      setSearchEnabled(false);
      setNextEnabled(false);
      setSearchIcon();
      setWaitCursor(true);
    } catch (Exception exception) {
      this.countText.setText("");
      setStatus(true, exception.getMessage());
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
      logger.info("Start search settings.");
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
        if (contentProviderConfigProperty != null) {
          if (defaultDisplayColumn.isDisplayOnResult())
            this.tm.addColumn(new ContentProviderSearchResultsColumn(defaultDisplayColumn.getColumnLabel(), contentProviderConfigProperty)); 
          this.crit.addOutputFields(defaultDisplayColumn.getContentProviderPropertyId());
          logger.debug("DefaultDisplayColumn :" + defaultDisplayColumn.getColumnLabel() + "[" + defaultDisplayColumn.getContentProviderPropertyId() + "]");
        } 
      } 
      boolean bool = DMSQuickSearch.isVisible();
      if (this.rest.isQuickSearchEnabled()) {
        logger.debug("QUICK SEARCH:" + this.rest.getQuickSearchString());
        this.crit.add((ICriterion)Restrictions.keyword(this.rest.getQuickSearchString(), KeywordExpression.MatchType.ALL));
      } 
      SearchMask searchMask = this.targetSearchMask;
      HashMap hashMap = this.ccp.getConfig().getSearchCapabilityMap();
      for (ContentProviderCharacteristic contentProviderCharacteristic : this.rest.getCharacteristics()) {
        SearchCondition searchCondition = searchMask.getSearchCondition(contentProviderCharacteristic.getOIField());
        if (searchCondition.isSelected() || !searchCondition.getRestriction().isEmpty()) {
          ContentProviderConfigProperty contentProviderConfigProperty = contentProviderCharacteristic.getProperty();
          if (contentProviderConfigProperty != null && searchCondition.isSelected()) {
            boolean bool1 = false;
            boolean bool2 = false;
            for (DefaultDisplayColumn defaultDisplayColumn : collection) {
              if (contentProviderConfigProperty.getContentProviderId().equals(defaultDisplayColumn.getContentProviderPropertyId())) {
                bool1 = true;
                if (defaultDisplayColumn.isDisplayOnResult())
                  bool2 = true; 
                break;
              } 
            } 
            if (!bool2)
              this.tm.addColumn(new ContentProviderSearchResultsColumn(PropertiesUtils.replace(searchCondition.getField().getLabel()), contentProviderConfigProperty)); 
            if (!bool1) {
              this.crit.addOutputFields(contentProviderCharacteristic.getProperty().getContentProviderId());
              logger.info("Display column:" + searchCondition.getField().getLabel() + "[" + contentProviderCharacteristic.getProperty().getContentProviderId() + "]");
            } 
          } 
          if (!bool && !searchCondition.getRestriction().isEmpty()) {
            logger.info("Restriction:" + searchCondition.getField().getLabel() + "[" + contentProviderCharacteristic.getProperty().getContentProviderId() + "] val:[" + searchCondition.getRestriction() + "]");
            if (contentProviderConfigProperty == null) {
              logger.info("Characteristic '" + searchCondition.getField().getLabel() + "' is not found in Content Provider configuration.  Skipping...");
              continue;
            } 
            if (!contentProviderConfigProperty.isSearchable()) {
              logger.info("Characteristic '" + searchCondition.getField().getLabel() + "' not set to searchable in the Content Provider configuration.  Unable to add search restriction.");
              continue;
            } 
            if (hashMap.get(contentProviderConfigProperty.getContentProviderId()) != null && !((SearchCapability)hashMap.get(contentProviderConfigProperty.getContentProviderId())).isSearchable())
              continue; 
            String str = searchCondition.getRestriction();
            if (searchCondition.getField().getType() == OIField.Type.DATE)
              str = replaceDateStr(str); 
            ANTLRInputStream aNTLRInputStream = new ANTLRInputStream(new ByteArrayInputStream(str.getBytes("utf-8")), "utf-8");
            if (searchCondition.getField().getType() == OIField.Type.STRING || searchCondition.getField().getType() == OIField.Type.REFERENCE) {
              if ("d8739de6".equals(searchCondition.getField().getName()) && !"true".equalsIgnoreCase(str) && !"false".equalsIgnoreCase(str)) {
                String str1 = "Only \"true\" or \"false\" can be specified for the '" + searchCondition.getField().getLabel() + "' field.";
                JOptionPane.showMessageDialog(this, str1, "Select " + this.ccp.getName() + " Valid Value", 1);
                setStatus(str1);
                setWarningIcon(str1);
                setSearchEnabled(true);
                return;
              } 
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
              this.crit.add(AbstractCriteria.doubleGetCriteria(contentProviderConfigProperty, commonTree, searchCondition));
              continue;
            } 
            if (searchCondition.getField().getType() == OIField.Type.DATE) {
              dMSDoubleExprLexer.setCharStream((CharStream)aNTLRInputStream);
              commonTokenStream2.setTokenSource((TokenSource)dMSDoubleExprLexer);
              dMSDoubleExprParser.setTokenStream((TokenStream)commonTokenStream2);
              DMSDoubleExprParser.prog_return prog_return = dMSDoubleExprParser.prog();
              if (dMSStringExprParser.getNumberOfSyntaxErrors() > 0)
                throw new ContentProviderException("Error in query syntax for '" + searchCondition.getField().getLabel() + "'."); 
              CommonTree commonTree = prog_return.getTree();
              this.crit.add(AbstractCriteria.dateGetCriteria(contentProviderConfigProperty, commonTree, searchCondition));
            } 
          } 
        } 
      } 
    } catch (Exception exception) {
      logger.error(exception.getMessage(), exception);
      setStatus(exception.getMessage());
      setWarningIcon(exception.getMessage());
      setSearchEnabled(true);
      return;
    } finally {
      setWaitCursor(false);
    } 
    logger.debug("Start Request!");
    setStatus("Querying Content Provider...");
    ContentProviderSearchTask contentProviderSearchTask = new ContentProviderSearchTask(this);
    contentProviderSearchTask.execute();
  }
  
  private String replaceDateStr(String paramString) {
    Pattern pattern = Pattern.compile("[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01]) ([01][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]");
    Matcher matcher = pattern.matcher(paramString);
    while (matcher.find()) {
      String str1 = matcher.group();
      String str2 = DateUtils.toUNIXTime(str1);
      paramString = paramString.replace(str1, str2);
    } 
    return paramString;
  }
  
  public void setCountText(String paramString) {
    if (paramString == null || paramString.equals("")) {
      this.countText.setText("");
    } else {
      this.countText.setText("<html><b>Showing " + paramString + "</b></html>");
    } 
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
  
  protected void setStatus(boolean paramBoolean, String paramString) {
    this.mainPanel.setStatus(paramBoolean, paramString);
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
  
  void setReturnCountWarn(int paramInt, String paramString) {
    this.tabComponent.setReturnCountWarn(paramInt, paramString);
  }
  
  public void doUpdateRestrictions() {
    doUpdateRestrictions(false);
  }
  
  public void doUpdateRestrictions(boolean paramBoolean) {
    StringBuilder stringBuilder = new StringBuilder();
    boolean bool1 = false;
    boolean bool2 = true;
    String str = null;
    try {
      ContentProviderConfigMPNCatalog contentProviderConfigMPNCatalog;
      logger.debug("doUpdateRestrictions(" + paramBoolean + ")");
      DMSInstance dMSInstance = ContentProviderGlobal.getDMSInstance();
      SearchMaskManager searchMaskManager = dMSInstance.getSearchMaskManager();
      boolean bool = DMSQuickSearch.isVisible();
      if (paramBoolean) {
        bool = this.rest.isQuickSearchEnabled();
        str = this.rest.getQuickSearchString();
      } 
      this.rest = new ContentProviderSearchRestriction();
      if (bool) {
        logger.debug("is Quick Search.");
        if (str == null)
          str = DMSQuickSearch.getSearchString(); 
        this.rest.setQuickSearchEnabled(true);
        this.rest.setQuickSearchString(str);
        stringBuilder.append("<QuickSearch>" + str);
      } else {
        logger.debug("not Quick Search.");
      } 
      SearchMask searchMask = (SearchMask)searchMaskManager.getActive();
      if (searchMask == null)
        return; 
      this.targetSearchMask = searchMask;
      OIClass oIClass = searchMask.getOIClass();
      if (!this.ccp.isClassificationSearchSupported()) {
        oIClass = oIClass.getRootClass();
        if (oIClass.getSubclasses().size() > 0)
          oIClass = oIClass.getSubclasses().iterator().next(); 
      } 
      this.rest.setOIClass(oIClass);
      stringBuilder.append("<EDMSearchClass>" + oIClass.getName());
      bool1 = !this.lastSearchClass.equals(oIClass.getName()) ? true : false;
      String str1 = oIClass.getRootClass().getName();
      HashMap<Object, Object> hashMap1 = (HashMap)ContentProviderSearchRestriction.getTabMapCache().get(str1);
      if (hashMap1 == null) {
        hashMap1 = new HashMap<>();
        OIQuery oIQuery = dMSInstance.getObjectManager().createQuery("Characteristic", true);
        oIQuery.addRestriction("ObjectClass", str1.equals("Component") ? "1" : "60");
        oIQuery.addRestriction("Text.Language", "e");
        oIQuery.addColumn("DomainModelName");
        oIQuery.addColumn("Text.TabSheet");
        OICursor oICursor = oIQuery.execute();
        while (oICursor.next())
          hashMap1.put(oICursor.getString("DomainModelName"), oICursor.getString("TabSheet")); 
        oICursor.close();
        ContentProviderSearchRestriction.getTabMapCache().put(str1, hashMap1);
      } 
      ContentProviderConfigComponentCatalog contentProviderConfigComponentCatalog = null;
      if (CheckLicense.hasSupplyChainLicense())
        if (str1.equals("Component")) {
          contentProviderConfigComponentCatalog = this.ccp.getConfig().getComponentCatalogConfigByDMN(oIClass.getName());
        } else if (str1.equals("ManufacturerPart")) {
          contentProviderConfigMPNCatalog = this.ccp.getConfig().getMPNCatalogConfigByDMN(oIClass.getName());
        }  
      this.rest.setDMSCatalogConfig((ContentProviderConfigDMSCatalog<?>)contentProviderConfigMPNCatalog);
      if (contentProviderConfigMPNCatalog == null || contentProviderConfigMPNCatalog.getContentProviderMaps().isEmpty()) {
        if (paramBoolean)
          return; 
        OIClass oIClass1 = dMSInstance.getObjectManager().getObjectManagerFactory().getClassManager().getOIClass(this.ccp.getRootClassDomain());
        searchMaskManager.open(oIClass1);
        if (!bool && bool1)
          ((SearchMask)searchMaskManager.getActive()).reset(); 
        doUpdateRestrictions(true);
        searchMaskManager.open(oIClass);
        this.rest.setOIClass(oIClass);
        this.lastSearchClass = this.rest.getOIClass().getName();
        bool2 = false;
        return;
      } 
      logger.debug("dmsCatalogConfig=" + contentProviderConfigMPNCatalog.getClassDMN());
      ContentProviderConfigContentProviderMap contentProviderConfigContentProviderMap = null;
      if (bool1) {
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
          String str2 = oICursor.getString("TabSheet");
          if (!str2.isEmpty())
            hashMap2.put(oICursor.getString("DomainModelName"), str2); 
        } 
        ContentProviderSearchRestriction.getTabMapCache().put(oIClass.getName(), hashMap2);
      } 
      this.rest.setTabMap((HashMap)hashMap2);
      for (SearchCondition searchCondition : searchMask.getSearchConditionList()) {
        if (searchCondition.getField().getType() == OIField.Type.DOUBLE || searchCondition.getField().getType() == OIField.Type.INTEGER || searchCondition.getField().getType() == OIField.Type.STRING || searchCondition.getField().getType() == OIField.Type.DATE || searchCondition.getField().getType() == OIField.Type.REFERENCE) {
          ContentProviderConfigPropertyMap contentProviderConfigPropertyMap = null;
          contentProviderConfigPropertyMap = contentProviderConfigContentProviderMap.getPropertyMapBySearchDMN(searchCondition.getField().getName());
          ContentProviderConfigProperty contentProviderConfigProperty = null;
          String str2 = "Null";
          if (contentProviderConfigPropertyMap != null) {
            contentProviderConfigProperty = contentProviderConfigContentProviderMap.getPartClass().getClassPropertyByContentProviderId(contentProviderConfigPropertyMap.getContentProviderId());
            str2 = contentProviderConfigPropertyMap.getContentProviderId();
          } 
          if (contentProviderConfigProperty != null) {
            this.rest.addCharacteristic(searchCondition, contentProviderConfigProperty);
            if (contentProviderConfigProperty.isSearchable())
              stringBuilder.append(":" + searchCondition.getField().getName() + ":" + searchCondition.getRestriction()); 
          } 
        } 
      } 
    } catch (ContentProviderException contentProviderException) {
      if (contentProviderException.isSSLCertException()) {
        logger.error("The SSL certificate is invalid.\nSee log for details.");
        logger.error(contentProviderException.getMessage(), (Throwable)contentProviderException);
      } else {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        contentProviderException.printStackTrace(printWriter);
        logger.error(stringWriter.toString());
      } 
    } catch (Exception exception) {
      StringWriter stringWriter = new StringWriter();
      PrintWriter printWriter = new PrintWriter(stringWriter);
      exception.printStackTrace(printWriter);
      logger.error(stringWriter.toString());
    } finally {
      if (bool2) {
        this.searchRestrictionsPane.updateRestrictions(this.rest, bool1);
        if (this.rest != null && this.rest.getOIClass() != null)
          this.lastSearchClass = this.rest.getOIClass().getName(); 
        String str1 = DigestUtils.md5Hex(stringBuilder.toString());
        if (!str1.equals(this.lastSearchRestrictionChecksum)) {
          this.mainPanel.setNextEnabled(false);
          this.lastSearchRestrictionChecksum = str1;
        } 
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
    tableColumnExt.setMinWidth(32);
    tableColumnExt = this.table.getColumnExt(1);
    tableColumnExt.setMaxWidth(40);
    tableColumnExt.setMinWidth(32);
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
  
  public boolean rootFacet() {
    return (((SearchMask)ContentProviderGlobal.getDMSInstance().getSearchMaskManager().getActive()).getOIClass().getName().equals(this.ccp.getRootClassDomain()) && getSearchCriteria().getCriteria().size() == 0);
  }
  
  public AbstractCriteria getFacetsSearchFilteringCriteria() throws ContentProviderException {
    try {
      doUpdateRestrictions();
      this.crit = this.ccp.createCriteria();
      this.crit.setMaxResults(50);
      DMSStringExprLexer dMSStringExprLexer = new DMSStringExprLexer();
      CommonTokenStream commonTokenStream1 = new CommonTokenStream((TokenSource)dMSStringExprLexer);
      DMSStringExprParser dMSStringExprParser = new DMSStringExprParser((TokenStream)commonTokenStream1);
      DMSDoubleExprLexer dMSDoubleExprLexer = new DMSDoubleExprLexer();
      CommonTokenStream commonTokenStream2 = new CommonTokenStream((TokenSource)dMSDoubleExprLexer);
      DMSDoubleExprParser dMSDoubleExprParser = new DMSDoubleExprParser((TokenStream)commonTokenStream2);
      boolean bool = DMSQuickSearch.isVisible();
      if (this.rest.isQuickSearchEnabled()) {
        logger.debug("QUICK SEARCH:" + this.rest.getQuickSearchString());
        this.crit.add((ICriterion)Restrictions.keyword(this.rest.getQuickSearchString(), KeywordExpression.MatchType.ALL));
      } 
      SearchMask searchMask = this.targetSearchMask;
      HashMap hashMap = this.ccp.getConfig().getSearchCapabilityMap();
      for (ContentProviderCharacteristic contentProviderCharacteristic : this.rest.getCharacteristics()) {
        logger.debug(">>charact:" + contentProviderCharacteristic.getOIField().getLabel());
        SearchCondition searchCondition = searchMask.getSearchCondition(contentProviderCharacteristic.getOIField());
        if (searchCondition.isSelected() || !searchCondition.getRestriction().isEmpty()) {
          ContentProviderConfigProperty contentProviderConfigProperty = contentProviderCharacteristic.getProperty();
          if (bool || searchCondition.getRestriction().isEmpty() || searchCondition.getField().getName().equals("3982bc42"))
            continue; 
          logger.debug("  >>getRestriction():" + searchCondition.getRestriction());
          if (contentProviderConfigProperty == null) {
            logger.info("Characteristic '" + searchCondition.getField().getLabel() + "' is not found in Content Provider configuration.  Skipping...");
            continue;
          } 
          if (!contentProviderConfigProperty.isSearchable()) {
            logger.info("Characteristic '" + searchCondition.getField().getLabel() + "' not set to searchable in the Content Provider configuration.  Unable to add search restriction.");
            continue;
          } 
          if (hashMap.get(searchCondition.getField().getName()) != null && !((SearchCapability)hashMap.get(searchCondition.getField().getName())).isSearchable())
            continue; 
          String str = searchCondition.getRestriction();
          if (searchCondition.getField().getType() == OIField.Type.DATE)
            str = replaceDateStr(str); 
          ANTLRInputStream aNTLRInputStream = new ANTLRInputStream(new ByteArrayInputStream(str.getBytes()));
          logger.debug("    sc.getField().getType()=" + String.valueOf(searchCondition.getField().getType()) + "\tgetName()=" + searchCondition.getField().getName());
          if (searchCondition.getField().getType() == OIField.Type.STRING || searchCondition.getField().getType() == OIField.Type.REFERENCE) {
            dMSStringExprLexer.setCharStream((CharStream)aNTLRInputStream);
            commonTokenStream1.setTokenSource((TokenSource)dMSStringExprLexer);
            dMSStringExprParser.setTokenStream((TokenStream)commonTokenStream1);
            DMSStringExprParser.prog_return prog_return = dMSStringExprParser.prog();
            if (dMSStringExprParser.getNumberOfSyntaxErrors() > 0)
              throw new ContentProviderException("Error in query syntax for '" + searchCondition.getField().getLabel()); 
            CommonTree commonTree = prog_return.getTree();
            this.crit.add(AbstractCriteria.stringGetCriteria(contentProviderConfigProperty, commonTree));
            if ("d8739de6".equals(searchCondition.getField().getName()) && !"true".equalsIgnoreCase(str) && !"false".equalsIgnoreCase(str)) {
              String str1 = "Only \"true\" or \"false\" can be specified for the '" + searchCondition.getField().getLabel() + "' field.";
              JOptionPane.showMessageDialog(this, str1, "Select " + this.ccp.getName() + " Valid Value", 1);
              setStatus(str1);
              return null;
            } 
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
            this.crit.add(AbstractCriteria.doubleGetCriteria(contentProviderConfigProperty, commonTree, searchCondition));
            continue;
          } 
          if (searchCondition.getField().getType() == OIField.Type.DATE) {
            dMSDoubleExprLexer.setCharStream((CharStream)aNTLRInputStream);
            commonTokenStream2.setTokenSource((TokenSource)dMSDoubleExprLexer);
            dMSDoubleExprParser.setTokenStream((TokenStream)commonTokenStream2);
            DMSDoubleExprParser.prog_return prog_return = dMSDoubleExprParser.prog();
            if (dMSStringExprParser.getNumberOfSyntaxErrors() > 0)
              throw new ContentProviderException("Error in query syntax for '" + searchCondition.getField().getLabel() + "'."); 
            CommonTree commonTree = prog_return.getTree();
            this.crit.add(AbstractCriteria.dateGetCriteria(contentProviderConfigProperty, commonTree, searchCondition));
          } 
        } 
      } 
    } catch (Exception exception) {
      logger.error(exception.getMessage(), exception);
      return null;
    } 
    logger.debug("Start Request!");
    return this.crit;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\plugin\searchui\ContentProviderSearchPanel.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */