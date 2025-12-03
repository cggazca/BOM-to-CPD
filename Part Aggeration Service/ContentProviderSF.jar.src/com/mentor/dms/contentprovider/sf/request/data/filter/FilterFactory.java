package com.mentor.dms.contentprovider.sf.request.data.filter;

import com.mentor.dms.contentprovider.core.criterion.Conjunction;
import com.mentor.dms.contentprovider.core.criterion.Disjunction;
import com.mentor.dms.contentprovider.core.criterion.ICriterion;
import com.mentor.dms.contentprovider.core.criterion.Junction;
import com.mentor.dms.contentprovider.core.criterion.SimpleExpression;
import com.mentor.dms.contentprovider.core.request.Filter;
import java.util.Collection;

public class FilterFactory {
  public static final Filter create(ICriterion paramICriterion) {
    return (paramICriterion instanceof SimpleExpression) ? createFromSimpleExpression((SimpleExpression)paramICriterion) : ((paramICriterion instanceof Conjunction) ? createFromConjunction((Conjunction)paramICriterion) : ((paramICriterion instanceof Disjunction) ? createFromDisjunction((Disjunction)paramICriterion) : new FilterDummy(paramICriterion)));
  }
  
  private static Filter createFromSimpleExpression(SimpleExpression paramSimpleExpression) {
    FilterRange filterRange;
    FilterEqual filterEqual = null;
    if (paramSimpleExpression.getOp() == SimpleExpression.OperatorType.EQUAL) {
      filterEqual = new FilterEqual(paramSimpleExpression);
    } else if (paramSimpleExpression.getOp() == SimpleExpression.OperatorType.NOT_EQUAL) {
      FilterDummy filterDummy = new FilterDummy((ICriterion)paramSimpleExpression);
    } else {
      filterRange = new FilterRange(paramSimpleExpression);
    } 
    return filterRange;
  }
  
  private static Filter createFromConjunction(Conjunction paramConjunction) {
    boolean bool = isRangeBetween((Junction)paramConjunction);
    return (Filter)(bool ? new FilterRangeBoth((Junction)paramConjunction) : new FilterAnd((Junction)paramConjunction));
  }
  
  private static boolean isRangeBetween(Junction paramJunction) {
    boolean bool1 = false;
    boolean bool2 = false;
    Collection collection = paramJunction.getCriterion();
    for (ICriterion iCriterion : collection) {
      if (iCriterion instanceof SimpleExpression) {
        SimpleExpression simpleExpression = (SimpleExpression)iCriterion;
        if (SimpleExpression.OperatorType.GREATER_THAN == simpleExpression.getOp() || SimpleExpression.OperatorType.GREATER_THAN_OR_EQUAL == simpleExpression.getOp())
          bool1 = true; 
        if (SimpleExpression.OperatorType.LESS_THAN == simpleExpression.getOp() || SimpleExpression.OperatorType.LESS_THAN_OR_EQUAL == simpleExpression.getOp())
          bool2 = true; 
      } 
    } 
    return (bool1 && bool2);
  }
  
  private static Filter createFromDisjunction(Disjunction paramDisjunction) {
    return new FilterAnyOf((Junction)paramDisjunction);
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\request\data\filter\FilterFactory.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */