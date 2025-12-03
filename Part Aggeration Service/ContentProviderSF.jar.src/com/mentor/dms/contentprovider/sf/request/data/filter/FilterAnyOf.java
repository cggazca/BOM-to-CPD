package com.mentor.dms.contentprovider.sf.request.data.filter;

import com.mentor.dms.contentprovider.core.criterion.ICriterion;
import com.mentor.dms.contentprovider.core.criterion.Junction;
import com.mentor.dms.contentprovider.core.criterion.SimpleExpression;
import com.mentor.dms.contentprovider.core.request.Filter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FilterAnyOf implements Filter {
  private String __valueOperator__ = "AnyOf";
  
  private String __expression__ = "ValueExpression";
  
  private String propertyId;
  
  private List<Object> values;
  
  private List<Object> terms;
  
  public FilterAnyOf(Junction paramJunction) {
    Collection collection = paramJunction.getCriterion();
    this.values = new ArrayList();
    this.terms = new ArrayList();
    for (ICriterion iCriterion : collection) {
      if (iCriterion instanceof SimpleExpression) {
        SimpleExpression simpleExpression = (SimpleExpression)iCriterion;
        if (simpleExpression.isSmartMatch()) {
          this.__valueOperator__ = "AnyOfSmartMatch";
          this.propertyId = simpleExpression.getProperty().getContentProviderId();
          this.terms.add(simpleExpression.getValue());
          continue;
        } 
        this.propertyId = simpleExpression.getProperty().getContentProviderId();
        this.values.add(simpleExpression.getValue());
      } 
    } 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\request\data\filter\FilterAnyOf.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */