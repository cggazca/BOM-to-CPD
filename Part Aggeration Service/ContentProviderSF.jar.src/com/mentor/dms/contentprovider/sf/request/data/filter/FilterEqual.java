package com.mentor.dms.contentprovider.sf.request.data.filter;

import com.mentor.dms.contentprovider.core.criterion.SimpleExpression;
import com.mentor.dms.contentprovider.core.request.Filter;

public class FilterEqual implements Filter {
  private String __valueOperator__ = "Equality";
  
  private String __expression__ = "ValueExpression";
  
  private Object value;
  
  private String propertyId;
  
  private Object term;
  
  public FilterEqual(SimpleExpression paramSimpleExpression) {
    this.propertyId = paramSimpleExpression.getProperty().getContentProviderId();
    if (paramSimpleExpression.isSmartMatch()) {
      this.__valueOperator__ = "SmartMatch";
      this.term = paramSimpleExpression.getValue();
    } else {
      this.value = paramSimpleExpression.getValue();
    } 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\request\data\filter\FilterEqual.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */