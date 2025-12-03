package com.mentor.dms.contentprovider.core.criterion;

import com.mentor.dms.contentprovider.core.AbstractCriteria;
import com.mentor.dms.contentprovider.core.config.ContentProviderConfigProperty;

public class EmptyExpression implements ICriterion, IPropertyExpression {
  ContentProviderConfigProperty property;
  
  protected EmptyExpression(ContentProviderConfigProperty paramContentProviderConfigProperty) {
    this.property = paramContentProviderConfigProperty;
  }
  
  public String toSearchString(AbstractCriteria paramAbstractCriteria) {
    return null;
  }
  
  public ContentProviderConfigProperty getProperty() {
    return this.property;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\criterion\EmptyExpression.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */