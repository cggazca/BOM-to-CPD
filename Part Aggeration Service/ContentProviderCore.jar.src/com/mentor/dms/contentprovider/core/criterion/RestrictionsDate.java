package com.mentor.dms.contentprovider.core.criterion;

import com.mentor.dms.contentprovider.core.config.ContentProviderConfigProperty;

public class RestrictionsDate {
  public static LogicalExpression and(ICriterion paramICriterion1, ICriterion paramICriterion2) {
    return new LogicalExpression(paramICriterion1, paramICriterion2, LogicalExpression.OperatorType.AND);
  }
  
  public static ICriterion between(ContentProviderConfigProperty paramContentProviderConfigProperty, PropertyValue paramPropertyValue1, PropertyValue paramPropertyValue2) {
    return new RangeExpression(paramContentProviderConfigProperty, paramPropertyValue1, paramPropertyValue2);
  }
  
  public static Conjunction conjunction() {
    return new Conjunction();
  }
  
  public static Disjunction disjunction() {
    return new Disjunction();
  }
  
  public static SimpleDateExpression eq(ContentProviderConfigProperty paramContentProviderConfigProperty, PropertyValue paramPropertyValue) {
    return new SimpleDateExpression(paramContentProviderConfigProperty, paramPropertyValue, SimpleExpression.OperatorType.EQUAL);
  }
  
  public static SimpleDateExpression ge(ContentProviderConfigProperty paramContentProviderConfigProperty, PropertyValue paramPropertyValue) {
    return new SimpleDateExpression(paramContentProviderConfigProperty, paramPropertyValue, SimpleExpression.OperatorType.GREATER_THAN_OR_EQUAL);
  }
  
  public static SimpleDateExpression gt(ContentProviderConfigProperty paramContentProviderConfigProperty, PropertyValue paramPropertyValue) {
    return new SimpleDateExpression(paramContentProviderConfigProperty, paramPropertyValue, SimpleExpression.OperatorType.GREATER_THAN);
  }
  
  public static ICriterion idEq(PropertyValue paramPropertyValue) {
    return new IdentifierEqExpression(paramPropertyValue);
  }
  
  public static ICriterion ilike(ContentProviderConfigProperty paramContentProviderConfigProperty) {
    return new LikeExpression(paramContentProviderConfigProperty, true);
  }
  
  public static ICriterion isEmpty(ContentProviderConfigProperty paramContentProviderConfigProperty) {
    return new EmptyExpression(paramContentProviderConfigProperty);
  }
  
  public static ICriterion isNotEmpty(ContentProviderConfigProperty paramContentProviderConfigProperty) {
    return new NotEmptyExpression(paramContentProviderConfigProperty);
  }
  
  public static ICriterion isNotNull(ContentProviderConfigProperty paramContentProviderConfigProperty) {
    return new NotNullExpression(paramContentProviderConfigProperty);
  }
  
  public static ICriterion isNull(ContentProviderConfigProperty paramContentProviderConfigProperty) {
    return new NullExpression(paramContentProviderConfigProperty);
  }
  
  public static SimpleDateExpression le(ContentProviderConfigProperty paramContentProviderConfigProperty, PropertyValue paramPropertyValue) {
    return new SimpleDateExpression(paramContentProviderConfigProperty, paramPropertyValue, SimpleExpression.OperatorType.LESS_THAN_OR_EQUAL);
  }
  
  public static LikeExpression like(ContentProviderConfigProperty paramContentProviderConfigProperty) {
    return new LikeExpression(paramContentProviderConfigProperty);
  }
  
  public static SimpleDateExpression lt(ContentProviderConfigProperty paramContentProviderConfigProperty, PropertyValue paramPropertyValue) {
    return new SimpleDateExpression(paramContentProviderConfigProperty, paramPropertyValue, SimpleExpression.OperatorType.LESS_THAN);
  }
  
  public static SimpleDateExpression ne(ContentProviderConfigProperty paramContentProviderConfigProperty, PropertyValue paramPropertyValue) {
    return new SimpleDateExpression(paramContentProviderConfigProperty, paramPropertyValue, SimpleExpression.OperatorType.NOT_EQUAL);
  }
  
  public static ICriterion not(ICriterion paramICriterion) {
    return new NotExpression(paramICriterion);
  }
  
  public static LogicalExpression or(ICriterion paramICriterion1, ICriterion paramICriterion2) {
    return new LogicalExpression(paramICriterion1, paramICriterion2, LogicalExpression.OperatorType.OR);
  }
  
  public static KeywordExpression keyword(String paramString, KeywordExpression.MatchType paramMatchType) {
    return new KeywordExpression(paramString, paramMatchType);
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\criterion\RestrictionsDate.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */