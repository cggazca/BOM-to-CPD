package com.mentor.dms.contentprovider.criterion;

import com.mentor.dms.contentprovider.AbstractCriteria;

public class IdentifierEqExpression implements ICriterion {
  PropertyValue value;
  
  protected IdentifierEqExpression(PropertyValue paramPropertyValue) {
    this.value = paramPropertyValue;
  }
  
  public String toSearchString(AbstractCriteria paramAbstractCriteria) {
    return null;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\criterion\IdentifierEqExpression.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */