package com.mentor.dms.contentprovider.core;

import com.mentor.datafusion.oi.OIException;
import com.mentor.datafusion.utils.logger.MGLogger;
import com.mentor.dms.contentprovider.core.config.ContentProviderConfigProperty;
import com.mentor.dms.contentprovider.core.criterion.Conjunction;
import com.mentor.dms.contentprovider.core.criterion.Disjunction;
import com.mentor.dms.contentprovider.core.criterion.ICriterion;
import com.mentor.dms.contentprovider.core.criterion.Junction;
import com.mentor.dms.contentprovider.core.criterion.LikeExpression;
import com.mentor.dms.contentprovider.core.criterion.PropertyValue;
import com.mentor.dms.contentprovider.core.criterion.Restrictions;
import com.mentor.dms.contentprovider.core.criterion.RestrictionsDate;
import com.mentor.dms.contentprovider.core.criterion.SimpleDateExpression;
import com.mentor.dms.contentprovider.core.criterion.SimpleExpression;
import com.mentor.dms.contentprovider.core.mapping.MappingUtils;
import com.mentor.dms.contentprovider.core.utils.UnitUtils;
import com.mentor.dms.ui.searchmask.restrictions.SearchCondition;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.antlr.runtime.tree.CommonTree;

public abstract class AbstractCriteria {
  private static MGLogger log = MGLogger.getLogger(AbstractCriteria.class);
  
  protected List<ICriterion> criteria = new ArrayList<>();
  
  protected int firstResult = -1;
  
  protected int timeout = -1;
  
  protected int maxResults = -1;
  
  protected Map<String, String> configParams;
  
  protected List<String> outputFields = new ArrayList<>();
  
  public AbstractCriteria(Map<String, String> paramMap) {
    this.configParams = paramMap;
  }
  
  public AbstractCriteria add(ICriterion paramICriterion) {
    this.criteria.add(paramICriterion);
    return this;
  }
  
  public AbstractCriteria setMaxResults(int paramInt) {
    this.maxResults = paramInt;
    return this;
  }
  
  AbstractCriteria setFirstResult(int paramInt) {
    this.firstResult = paramInt;
    return this;
  }
  
  AbstractCriteria setTimeout(int paramInt) {
    this.timeout = paramInt;
    return this;
  }
  
  public List<ICriterion> getCriteria() {
    return this.criteria;
  }
  
  public int getFirstResult() {
    return this.firstResult;
  }
  
  public int getTimeout() {
    return this.timeout;
  }
  
  public int getMaxResults() {
    return this.maxResults;
  }
  
  public void addOutputFields(String paramString) {
    this.outputFields.add(paramString);
  }
  
  public List<String> getOutputFields() {
    return this.outputFields;
  }
  
  public Collection<ICriterion> getCriterion() {
    return Collections.unmodifiableCollection(this.criteria);
  }
  
  public Conjunction getCriterionAsConjunction() {
    Conjunction conjunction = Restrictions.conjunction();
    for (ICriterion iCriterion : this.criteria)
      conjunction.add(iCriterion); 
    return conjunction;
  }
  
  private boolean isKeywordSearch(ICriterion paramICriterion) {
    if (paramICriterion instanceof com.mentor.dms.contentprovider.core.criterion.KeywordExpression)
      return true; 
    if (paramICriterion instanceof Junction)
      for (ICriterion iCriterion : ((Junction)paramICriterion).getCriterion()) {
        if (iCriterion instanceof com.mentor.dms.contentprovider.core.criterion.KeywordExpression)
          return true; 
        if (iCriterion instanceof Junction)
          return isKeywordSearch(iCriterion); 
      }  
    return false;
  }
  
  public boolean isKeywordSearch() {
    for (ICriterion iCriterion : getCriterion()) {
      if (isKeywordSearch(iCriterion))
        return true; 
    } 
    return false;
  }
  
  public static ICriterion stringGetCriteria(ContentProviderConfigProperty paramContentProviderConfigProperty, CommonTree paramCommonTree) {
    LikeExpression likeExpression;
    Disjunction disjunction = null;
    if (paramCommonTree.getText().equals("LOGICAL_OR")) {
      if (paramCommonTree.getChildCount() == 1)
        return stringGetCriteria(paramContentProviderConfigProperty, (CommonTree)paramCommonTree.getChild(0)); 
      Disjunction disjunction1 = Restrictions.disjunction();
      Iterator<CommonTree> iterator = paramCommonTree.getChildren().iterator();
      while (iterator.hasNext())
        disjunction1.add(stringGetCriteria(paramContentProviderConfigProperty, iterator.next())); 
      disjunction = disjunction1;
    } else if (paramCommonTree.getText().equals("LOGICAL_AND")) {
      if (paramCommonTree.getChildCount() == 1)
        return stringGetCriteria(paramContentProviderConfigProperty, (CommonTree)paramCommonTree.getChild(0)); 
      Conjunction conjunction2 = Restrictions.conjunction();
      Iterator<CommonTree> iterator = paramCommonTree.getChildren().iterator();
      while (iterator.hasNext())
        conjunction2.add(stringGetCriteria(paramContentProviderConfigProperty, iterator.next())); 
      Conjunction conjunction1 = conjunction2;
    } else if (paramCommonTree.getText().equals("LOGICAL_NOT")) {
      ICriterion iCriterion = Restrictions.not(stringGetCriteria(paramContentProviderConfigProperty, (CommonTree)paramCommonTree.getChild(0)));
    } else if (paramCommonTree.getText().equals("LOGICAL_NULL")) {
      ICriterion iCriterion = Restrictions.isNull(paramContentProviderConfigProperty);
    } else if (paramCommonTree.getText().equals("LOGICAL_NOT_NULL")) {
      ICriterion iCriterion = Restrictions.isNotNull(paramContentProviderConfigProperty);
    } else if (paramCommonTree.getText().equals("WILDSTRING")) {
      if (paramCommonTree.getChildCount() == 1 && !paramCommonTree.getChild(0).getText().equals("*") && !paramCommonTree.getChild(0).getText().equals("?")) {
        SimpleExpression simpleExpression = Restrictions.eq(paramContentProviderConfigProperty, new PropertyValue(removeEscapes(paramCommonTree.getChild(0).getText()), ""));
      } else {
        LikeExpression likeExpression1 = Restrictions.like(paramContentProviderConfigProperty);
        Iterator<CommonTree> iterator = paramCommonTree.getChildren().iterator();
        while (iterator.hasNext())
          likeExpression1.addSubString(removeEscapes(((CommonTree)iterator.next()).getText())); 
        likeExpression = likeExpression1;
      } 
    } else {
      log.error("Unrecognized criteria: " + paramCommonTree.getText());
    } 
    return (ICriterion)likeExpression;
  }
  
  private static String removeEscapes(String paramString) {
    StringBuilder stringBuilder = new StringBuilder();
    for (byte b = 0; b < paramString.length(); b++) {
      if (paramString.charAt(b) == '\\')
        b++; 
      if (b < paramString.length())
        stringBuilder.append(paramString.charAt(b)); 
    } 
    return stringBuilder.toString();
  }
  
  public static ICriterion doubleGetCriteria(ContentProviderConfigProperty paramContentProviderConfigProperty, CommonTree paramCommonTree, SearchCondition paramSearchCondition) {
    SimpleExpression simpleExpression;
    Disjunction disjunction = null;
    if (paramCommonTree.getText().equals("LOGICAL_OR")) {
      if (paramCommonTree.getChildCount() == 1)
        return doubleGetCriteria(paramContentProviderConfigProperty, (CommonTree)paramCommonTree.getChild(0), paramSearchCondition); 
      Disjunction disjunction1 = Restrictions.disjunction();
      Iterator<CommonTree> iterator = paramCommonTree.getChildren().iterator();
      while (iterator.hasNext())
        disjunction1.add(doubleGetCriteria(paramContentProviderConfigProperty, iterator.next(), paramSearchCondition)); 
      disjunction = disjunction1;
    } else if (paramCommonTree.getText().equals("LOGICAL_AND")) {
      if (paramCommonTree.getChildCount() == 1)
        return doubleGetCriteria(paramContentProviderConfigProperty, (CommonTree)paramCommonTree.getChild(0), paramSearchCondition); 
      Conjunction conjunction2 = Restrictions.conjunction();
      Iterator<CommonTree> iterator = paramCommonTree.getChildren().iterator();
      while (iterator.hasNext())
        conjunction2.add(doubleGetCriteria(paramContentProviderConfigProperty, iterator.next(), paramSearchCondition)); 
      Conjunction conjunction1 = conjunction2;
    } else if (paramCommonTree.getText().equals("LOGICAL_NOT")) {
      ICriterion iCriterion = Restrictions.not(doubleGetCriteria(paramContentProviderConfigProperty, (CommonTree)paramCommonTree.getChild(0), paramSearchCondition));
    } else if (paramCommonTree.getText().equals("RANGE_EXPRESSION")) {
      ICriterion iCriterion = Restrictions.between(paramContentProviderConfigProperty, getNumericPropertyValue((CommonTree)paramCommonTree.getChild(0), paramSearchCondition, paramContentProviderConfigProperty), getNumericPropertyValue((CommonTree)paramCommonTree.getChild(1), paramSearchCondition, paramContentProviderConfigProperty));
    } else if (paramCommonTree.getText().equals("RELATIONAL_EXPRESSION")) {
      String str = paramCommonTree.getChild(0).getText();
      if (str.equals("<")) {
        simpleExpression = Restrictions.lt(paramContentProviderConfigProperty, getNumericPropertyValue((CommonTree)paramCommonTree.getChild(1), paramSearchCondition, paramContentProviderConfigProperty));
      } else if (str.equals(">")) {
        simpleExpression = Restrictions.gt(paramContentProviderConfigProperty, getNumericPropertyValue((CommonTree)paramCommonTree.getChild(1), paramSearchCondition, paramContentProviderConfigProperty));
      } else if (str.equals("<=")) {
        simpleExpression = Restrictions.le(paramContentProviderConfigProperty, getNumericPropertyValue((CommonTree)paramCommonTree.getChild(1), paramSearchCondition, paramContentProviderConfigProperty));
      } else if (str.equals(">=")) {
        simpleExpression = Restrictions.ge(paramContentProviderConfigProperty, getNumericPropertyValue((CommonTree)paramCommonTree.getChild(1), paramSearchCondition, paramContentProviderConfigProperty));
      } 
    } else if (paramCommonTree.getText().equals("LOGICAL_NULL")) {
      ICriterion iCriterion = Restrictions.isNull(paramContentProviderConfigProperty);
    } else if (paramCommonTree.getText().equals("LOGICAL_NOT_NULL")) {
      ICriterion iCriterion = Restrictions.isNotNull(paramContentProviderConfigProperty);
    } else if (paramCommonTree.getText().equals("DOUBLE_VALUE")) {
      simpleExpression = Restrictions.eq(paramContentProviderConfigProperty, getNumericPropertyValue(paramCommonTree, paramSearchCondition, paramContentProviderConfigProperty));
    } else {
      System.out.println("What is this : " + paramCommonTree.getText());
    } 
    return (ICriterion)simpleExpression;
  }
  
  public static PropertyValue getNumericPropertyValue(CommonTree paramCommonTree, SearchCondition paramSearchCondition, ContentProviderConfigProperty paramContentProviderConfigProperty) {
    byte b = 0;
    BigDecimal bigDecimal = null;
    String str1 = "";
    if (paramCommonTree.getChild(b).getText().equals("NEGATIVE")) {
      str1 = "-";
      b++;
    } 
    str1 = str1 + str1;
    bigDecimal = new BigDecimal(str1);
    String str2 = "";
    if (paramCommonTree.getChildCount() > b)
      str2 = paramCommonTree.getChild(b).getText(); 
    log.debug("NumericProperty UNIT:[" + str2 + "]");
    String str3 = UnitUtils.convertUnit(paramContentProviderConfigProperty.getBaseUnits());
    if (str2.equals(str3))
      return new PropertyValue(bigDecimal, str2); 
    try {
      String str = "";
      HashMap hashMap = UnitUtils.getUnitRange(paramSearchCondition.getField().getUnitName());
      for (Map.Entry entry : hashMap.entrySet()) {
        if (((Double)entry.getValue()).equals(Double.valueOf(1.0D))) {
          str = (String)entry.getKey();
          break;
        } 
      } 
      BigDecimal bigDecimal1 = new BigDecimal(1);
      BigDecimal bigDecimal2 = null;
      for (Map.Entry entry : hashMap.entrySet()) {
        if (((String)entry.getKey()).equals(str3))
          bigDecimal1 = BigDecimal.valueOf(((Double)entry.getValue()).doubleValue()); 
        if (!((String)entry.getKey()).equals(str) && str2.equals(entry.getKey())) {
          BigDecimal bigDecimal3 = new BigDecimal(str1);
          bigDecimal2 = bigDecimal3.multiply(BigDecimal.valueOf(((Double)entry.getValue()).doubleValue()));
        } 
      } 
      try {
        if (bigDecimal2 == null)
          bigDecimal2 = new BigDecimal(str1); 
        str1 = bigDecimal2.divide(bigDecimal1).toPlainString();
      } catch (NumberFormatException numberFormatException) {}
      bigDecimal = new BigDecimal(str1);
    } catch (OIException oIException) {
      oIException.printStackTrace();
    } 
    return new PropertyValue(bigDecimal, str3);
  }
  
  public static ICriterion dateGetCriteria(ContentProviderConfigProperty paramContentProviderConfigProperty, CommonTree paramCommonTree, SearchCondition paramSearchCondition) {
    SimpleDateExpression simpleDateExpression;
    Disjunction disjunction = null;
    if (paramCommonTree.getText().equals("LOGICAL_OR")) {
      if (paramCommonTree.getChildCount() == 1)
        return dateGetCriteria(paramContentProviderConfigProperty, (CommonTree)paramCommonTree.getChild(0), paramSearchCondition); 
      Disjunction disjunction1 = RestrictionsDate.disjunction();
      Iterator<CommonTree> iterator = paramCommonTree.getChildren().iterator();
      while (iterator.hasNext())
        disjunction1.add(dateGetCriteria(paramContentProviderConfigProperty, iterator.next(), paramSearchCondition)); 
      disjunction = disjunction1;
    } else if (paramCommonTree.getText().equals("LOGICAL_AND")) {
      if (paramCommonTree.getChildCount() == 1)
        return dateGetCriteria(paramContentProviderConfigProperty, (CommonTree)paramCommonTree.getChild(0), paramSearchCondition); 
      Conjunction conjunction2 = RestrictionsDate.conjunction();
      Iterator<CommonTree> iterator = paramCommonTree.getChildren().iterator();
      while (iterator.hasNext())
        conjunction2.add(dateGetCriteria(paramContentProviderConfigProperty, iterator.next(), paramSearchCondition)); 
      Conjunction conjunction1 = conjunction2;
    } else if (paramCommonTree.getText().equals("LOGICAL_NOT")) {
      ICriterion iCriterion = RestrictionsDate.not(dateGetCriteria(paramContentProviderConfigProperty, (CommonTree)paramCommonTree.getChild(0), paramSearchCondition));
    } else if (paramCommonTree.getText().equals("RANGE_EXPRESSION")) {
      ICriterion iCriterion = RestrictionsDate.between(paramContentProviderConfigProperty, getNumericPropertyValue((CommonTree)paramCommonTree.getChild(0), paramSearchCondition, paramContentProviderConfigProperty), getNumericPropertyValue((CommonTree)paramCommonTree.getChild(1), paramSearchCondition, paramContentProviderConfigProperty));
    } else if (paramCommonTree.getText().equals("RELATIONAL_EXPRESSION")) {
      String str = paramCommonTree.getChild(0).getText();
      if (str.equals("<")) {
        simpleDateExpression = RestrictionsDate.lt(paramContentProviderConfigProperty, getNumericPropertyValue((CommonTree)paramCommonTree.getChild(1), paramSearchCondition, paramContentProviderConfigProperty));
      } else if (str.equals(">")) {
        simpleDateExpression = RestrictionsDate.gt(paramContentProviderConfigProperty, getNumericPropertyValue((CommonTree)paramCommonTree.getChild(1), paramSearchCondition, paramContentProviderConfigProperty));
      } else if (str.equals("<=")) {
        simpleDateExpression = RestrictionsDate.le(paramContentProviderConfigProperty, getNumericPropertyValue((CommonTree)paramCommonTree.getChild(1), paramSearchCondition, paramContentProviderConfigProperty));
      } else if (str.equals(">=")) {
        simpleDateExpression = RestrictionsDate.ge(paramContentProviderConfigProperty, getNumericPropertyValue((CommonTree)paramCommonTree.getChild(1), paramSearchCondition, paramContentProviderConfigProperty));
      } 
    } else if (paramCommonTree.getText().equals("LOGICAL_NULL")) {
      ICriterion iCriterion = RestrictionsDate.isNull(paramContentProviderConfigProperty);
    } else if (paramCommonTree.getText().equals("LOGICAL_NOT_NULL")) {
      ICriterion iCriterion = RestrictionsDate.isNotNull(paramContentProviderConfigProperty);
    } else if (paramCommonTree.getText().equals("DOUBLE_VALUE")) {
      simpleDateExpression = RestrictionsDate.eq(paramContentProviderConfigProperty, getNumericPropertyValue(paramCommonTree, paramSearchCondition, paramContentProviderConfigProperty));
    } else {
      System.out.println("What is this : " + paramCommonTree.getText());
    } 
    return (ICriterion)simpleDateExpression;
  }
  
  public static PropertyValue getDoublePropertyValue_bk(CommonTree paramCommonTree) {
    byte b = 0;
    BigDecimal bigDecimal = null;
    String str1 = "";
    if (paramCommonTree.getChild(b).getText().equals("NEGATIVE")) {
      str1 = "-";
      b++;
    } 
    str1 = str1 + str1;
    bigDecimal = new BigDecimal(str1);
    String str2 = "";
    if (paramCommonTree.getChildCount() > b)
      str2 = paramCommonTree.getChild(b).getText(); 
    log.debug("DoubleProperty UNIT:[" + str2 + "]");
    bigDecimal = bigDecimal.multiply(MappingUtils.getUnitsMultiplier(str2));
    return new PropertyValue(bigDecimal, str2);
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\AbstractCriteria.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */