package com.mentor.dms.contentprovider.criterion;

import com.mentor.dms.contentprovider.AbstractCriteria;
import com.mentor.dms.contentprovider.config.ContentProviderConfigProperty;

public class SimpleExpression implements ICriterion, IPropertyExpression {
  private ContentProviderConfigProperty property;
  
  private PropertyValue value;
  
  private OperatorType op;
  
  private boolean ignoreCase = true;
  
  protected SimpleExpression(ContentProviderConfigProperty paramContentProviderConfigProperty, PropertyValue paramPropertyValue, OperatorType paramOperatorType) {
    this.property = paramContentProviderConfigProperty;
    this.value = paramPropertyValue;
    this.op = paramOperatorType;
  }
  
  protected SimpleExpression(ContentProviderConfigProperty paramContentProviderConfigProperty, PropertyValue paramPropertyValue, OperatorType paramOperatorType, boolean paramBoolean) {
    this(paramContentProviderConfigProperty, paramPropertyValue, paramOperatorType);
    this.ignoreCase = paramBoolean;
  }
  
  public ContentProviderConfigProperty getProperty() {
    return this.property;
  }
  
  public PropertyValue getPropertyValue() {
    return this.value;
  }
  
  public OperatorType getOp() {
    return this.op;
  }
  
  public boolean isIgnoreCase() {
    return this.ignoreCase;
  }
  
  public String toSearchString(AbstractCriteria paramAbstractCriteria) {
    return null;
  }
  
  public enum OperatorType {
    EQUAL, NOT_EQUAL, GREATER_THAN, GREATER_THAN_OR_EQUAL, LESS_THAN, LESS_THAN_OR_EQUAL;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\criterion\SimpleExpression.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */