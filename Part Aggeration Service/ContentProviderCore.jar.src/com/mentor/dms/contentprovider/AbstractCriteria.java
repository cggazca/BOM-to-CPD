package com.mentor.dms.contentprovider;

import com.mentor.datafusion.utils.logger.MGLogger;
import com.mentor.dms.contentprovider.config.ContentProviderConfigProperty;
import com.mentor.dms.contentprovider.criterion.Conjunction;
import com.mentor.dms.contentprovider.criterion.Disjunction;
import com.mentor.dms.contentprovider.criterion.ICriterion;
import com.mentor.dms.contentprovider.criterion.Junction;
import com.mentor.dms.contentprovider.criterion.LikeExpression;
import com.mentor.dms.contentprovider.criterion.PropertyValue;
import com.mentor.dms.contentprovider.criterion.Restrictions;
import com.mentor.dms.contentprovider.criterion.SimpleExpression;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
  
  public Collection<ICriterion> getCriterion() {
    return Collections.unmodifiableCollection(this.criteria);
  }
  
  private boolean isKeywordSearch(ICriterion paramICriterion) {
    if (paramICriterion instanceof com.mentor.dms.contentprovider.criterion.KeywordExpression)
      return true; 
    if (paramICriterion instanceof Junction)
      for (ICriterion iCriterion : ((Junction)paramICriterion).getCriterion()) {
        if (iCriterion instanceof com.mentor.dms.contentprovider.criterion.KeywordExpression)
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
  
  public static ICriterion doubleGetCriteria(ContentProviderConfigProperty paramContentProviderConfigProperty, CommonTree paramCommonTree) {
    SimpleExpression simpleExpression;
    Disjunction disjunction = null;
    if (paramCommonTree.getText().equals("LOGICAL_OR")) {
      if (paramCommonTree.getChildCount() == 1)
        return doubleGetCriteria(paramContentProviderConfigProperty, (CommonTree)paramCommonTree.getChild(0)); 
      Disjunction disjunction1 = Restrictions.disjunction();
      Iterator<CommonTree> iterator = paramCommonTree.getChildren().iterator();
      while (iterator.hasNext())
        disjunction1.add(doubleGetCriteria(paramContentProviderConfigProperty, iterator.next())); 
      disjunction = disjunction1;
    } else if (paramCommonTree.getText().equals("LOGICAL_AND")) {
      if (paramCommonTree.getChildCount() == 1)
        return doubleGetCriteria(paramContentProviderConfigProperty, (CommonTree)paramCommonTree.getChild(0)); 
      Conjunction conjunction2 = Restrictions.conjunction();
      Iterator<CommonTree> iterator = paramCommonTree.getChildren().iterator();
      while (iterator.hasNext())
        conjunction2.add(doubleGetCriteria(paramContentProviderConfigProperty, iterator.next())); 
      Conjunction conjunction1 = conjunction2;
    } else if (paramCommonTree.getText().equals("LOGICAL_NOT")) {
      ICriterion iCriterion = Restrictions.not(doubleGetCriteria(paramContentProviderConfigProperty, (CommonTree)paramCommonTree.getChild(0)));
    } else if (paramCommonTree.getText().equals("RANGE_EXPRESSION")) {
      ICriterion iCriterion = Restrictions.between(paramContentProviderConfigProperty, getDoublePropertyValue((CommonTree)paramCommonTree.getChild(0)), getDoublePropertyValue((CommonTree)paramCommonTree.getChild(1)));
    } else if (paramCommonTree.getText().equals("RELATIONAL_EXPRESSION")) {
      String str = paramCommonTree.getChild(0).getText();
      if (str.equals("<")) {
        simpleExpression = Restrictions.lt(paramContentProviderConfigProperty, getDoublePropertyValue((CommonTree)paramCommonTree.getChild(1)));
      } else if (str.equals(">")) {
        simpleExpression = Restrictions.gt(paramContentProviderConfigProperty, getDoublePropertyValue((CommonTree)paramCommonTree.getChild(1)));
      } else if (str.equals("<=")) {
        simpleExpression = Restrictions.le(paramContentProviderConfigProperty, getDoublePropertyValue((CommonTree)paramCommonTree.getChild(1)));
      } else if (str.equals(">=")) {
        simpleExpression = Restrictions.ge(paramContentProviderConfigProperty, getDoublePropertyValue((CommonTree)paramCommonTree.getChild(1)));
      } 
    } else if (paramCommonTree.getText().equals("LOGICAL_NULL")) {
      ICriterion iCriterion = Restrictions.isNull(paramContentProviderConfigProperty);
    } else if (paramCommonTree.getText().equals("LOGICAL_NOT_NULL")) {
      ICriterion iCriterion = Restrictions.isNotNull(paramContentProviderConfigProperty);
    } else if (paramCommonTree.getText().equals("DOUBLE_VALUE")) {
      simpleExpression = Restrictions.eq(paramContentProviderConfigProperty, getDoublePropertyValue(paramCommonTree));
    } else {
      System.out.println("What is this : " + paramCommonTree.getText());
    } 
    return (ICriterion)simpleExpression;
  }
  
  public static PropertyValue getDoublePropertyValue(CommonTree paramCommonTree) {
    byte b = 0;
    String str1 = "";
    if (paramCommonTree.getChild(b).getText().equals("NEGATIVE")) {
      str1 = "-";
      b++;
    } 
    str1 = str1 + str1;
    String str2 = "";
    if (paramCommonTree.getChildCount() > b)
      str2 = paramCommonTree.getChild(b).getText(); 
    return new PropertyValue(str1, str2);
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\AbstractCriteria.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */