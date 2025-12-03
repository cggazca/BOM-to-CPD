package com.mentor.dms.contentprovider.criterion;

import com.mentor.dms.contentprovider.AbstractCriteria;
import com.mentor.dms.contentprovider.config.ContentProviderConfigProperty;

public class NotEmptyExpression implements ICriterion {
  ContentProviderConfigProperty property;
  
  protected NotEmptyExpression(ContentProviderConfigProperty paramContentProviderConfigProperty) {
    this.property = paramContentProviderConfigProperty;
  }
  
  public String toSearchString(AbstractCriteria paramAbstractCriteria) {
    return null;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\criterion\NotEmptyExpression.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */