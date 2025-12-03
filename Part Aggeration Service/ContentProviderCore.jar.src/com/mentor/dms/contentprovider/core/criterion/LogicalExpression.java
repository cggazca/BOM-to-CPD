package com.mentor.dms.contentprovider.core.criterion;

import com.mentor.dms.contentprovider.core.AbstractCriteria;

public class LogicalExpression implements ICriterion {
  private ICriterion lhs;
  
  private ICriterion rhs;
  
  private OperatorType operator;
  
  protected LogicalExpression(ICriterion paramICriterion1, ICriterion paramICriterion2, OperatorType paramOperatorType) {
    this.lhs = paramICriterion1;
    this.rhs = paramICriterion2;
    this.operator = paramOperatorType;
  }
  
  public String toSearchString(AbstractCriteria paramAbstractCriteria) {
    return null;
  }
  
  public enum OperatorType {
    AND, OR;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\criterion\LogicalExpression.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */