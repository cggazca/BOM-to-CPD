package com.mentor.dms.contentprovider.sf.request.data.filter;

import com.mentor.dms.contentprovider.core.criterion.SimpleExpression;
import com.mentor.dms.contentprovider.core.request.Filter;
import com.mentor.dms.contentprovider.sf.request.data.LeftBoundary;
import com.mentor.dms.contentprovider.sf.request.data.RightBoundary;

public class FilterRange implements Filter {
  private String __valueOperator__ = "Range";
  
  private String __expression__ = "ValueExpression";
  
  private String propertyId;
  
  private LeftBoundary leftBoundary = null;
  
  private RightBoundary rightBoundary = null;
  
  public FilterRange(SimpleExpression paramSimpleExpression) {
    if (paramSimpleExpression.getOp() == SimpleExpression.OperatorType.GREATER_THAN) {
      this.propertyId = paramSimpleExpression.getProperty().getContentProviderId();
      this.leftBoundary = new LeftBoundary(paramSimpleExpression.getValue(), true);
    } else if (paramSimpleExpression.getOp() == SimpleExpression.OperatorType.GREATER_THAN_OR_EQUAL) {
      this.propertyId = paramSimpleExpression.getProperty().getContentProviderId();
      this.leftBoundary = new LeftBoundary(paramSimpleExpression.getValue(), true);
    } else if (paramSimpleExpression.getOp() == SimpleExpression.OperatorType.LESS_THAN) {
      this.propertyId = paramSimpleExpression.getProperty().getContentProviderId();
      this.rightBoundary = new RightBoundary(paramSimpleExpression.getValue(), false);
    } else if (paramSimpleExpression.getOp() == SimpleExpression.OperatorType.LESS_THAN_OR_EQUAL) {
      this.propertyId = paramSimpleExpression.getProperty().getContentProviderId();
      this.rightBoundary = new RightBoundary(paramSimpleExpression.getValue(), true);
    } 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\request\data\filter\FilterRange.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */