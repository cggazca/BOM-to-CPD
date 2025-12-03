package com.mentor.dms.contentprovider.sf.request.data.filter;

import com.mentor.dms.contentprovider.core.criterion.ICriterion;
import com.mentor.dms.contentprovider.core.criterion.Junction;
import com.mentor.dms.contentprovider.core.criterion.SimpleExpression;
import com.mentor.dms.contentprovider.core.request.Filter;
import com.mentor.dms.contentprovider.sf.request.data.LeftBoundary;
import com.mentor.dms.contentprovider.sf.request.data.RightBoundary;
import java.util.Collection;

public class FilterRangeBoth implements Filter {
  private String __valueOperator__ = "Range";
  
  private String __expression__ = "ValueExpression";
  
  private String propertyId;
  
  private LeftBoundary leftBoundary = null;
  
  private RightBoundary rightBoundary = null;
  
  public FilterRangeBoth(Junction paramJunction) {
    Collection collection = paramJunction.getCriterion();
    for (ICriterion iCriterion : collection) {
      if (iCriterion instanceof SimpleExpression) {
        SimpleExpression simpleExpression = (SimpleExpression)iCriterion;
        if (simpleExpression.getOp() == SimpleExpression.OperatorType.GREATER_THAN) {
          this.propertyId = simpleExpression.getProperty().getContentProviderId();
          this.leftBoundary = new LeftBoundary(simpleExpression.getValue(), true);
          continue;
        } 
        if (simpleExpression.getOp() == SimpleExpression.OperatorType.GREATER_THAN_OR_EQUAL) {
          this.propertyId = simpleExpression.getProperty().getContentProviderId();
          this.leftBoundary = new LeftBoundary(simpleExpression.getValue(), true);
          continue;
        } 
        if (simpleExpression.getOp() == SimpleExpression.OperatorType.LESS_THAN) {
          this.propertyId = simpleExpression.getProperty().getContentProviderId();
          this.rightBoundary = new RightBoundary(simpleExpression.getValue(), false);
          continue;
        } 
        if (simpleExpression.getOp() == SimpleExpression.OperatorType.LESS_THAN_OR_EQUAL) {
          this.propertyId = simpleExpression.getProperty().getContentProviderId();
          this.rightBoundary = new RightBoundary(simpleExpression.getValue(), true);
        } 
      } 
    } 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\request\data\filter\FilterRangeBoth.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */