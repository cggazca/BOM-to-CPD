package com.mentor.dms.contentprovider.criterion;

import com.mentor.dms.contentprovider.AbstractCriteria;
import com.mentor.dms.contentprovider.config.ContentProviderConfigProperty;

public class RangeExpression implements ICriterion, IPropertyExpression {
  private ContentProviderConfigProperty property;
  
  private PropertyValue lo;
  
  private PropertyValue hi;
  
  protected RangeExpression(ContentProviderConfigProperty paramContentProviderConfigProperty, PropertyValue paramPropertyValue1, PropertyValue paramPropertyValue2) {
    this.property = paramContentProviderConfigProperty;
    this.lo = paramPropertyValue1;
    this.hi = paramPropertyValue2;
  }
  
  public PropertyValue getHi() {
    return this.hi;
  }
  
  public PropertyValue getLo() {
    return this.lo;
  }
  
  public ContentProviderConfigProperty getProperty() {
    return this.property;
  }
  
  public String toSearchString(AbstractCriteria paramAbstractCriteria) {
    return null;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\criterion\RangeExpression.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */