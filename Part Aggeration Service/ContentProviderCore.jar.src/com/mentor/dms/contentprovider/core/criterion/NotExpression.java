package com.mentor.dms.contentprovider.core.criterion;

import com.mentor.dms.contentprovider.core.AbstractCriteria;

public class NotExpression implements ICriterion {
  private ICriterion criterion;
  
  protected NotExpression(ICriterion paramICriterion) {
    this.criterion = paramICriterion;
  }
  
  public ICriterion getCriterion() {
    return this.criterion;
  }
  
  public String toSearchString(AbstractCriteria paramAbstractCriteria) {
    return null;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\criterion\NotExpression.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */