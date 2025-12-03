package com.mentor.dms.contentprovider.criterion;

import com.mentor.dms.contentprovider.AbstractCriteria;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Junction implements ICriterion {
  OperatorType operator;
  
  List<ICriterion> criteria = new ArrayList<>();
  
  protected Junction(OperatorType paramOperatorType) {
    this.operator = paramOperatorType;
  }
  
  public Junction add(ICriterion paramICriterion) {
    this.criteria.add(paramICriterion);
    return this;
  }
  
  public Collection<ICriterion> getCriterion() {
    return Collections.unmodifiableCollection(this.criteria);
  }
  
  public String toSearchString(AbstractCriteria paramAbstractCriteria) {
    return null;
  }
  
  public enum OperatorType {
    AND, OR;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\criterion\Junction.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */