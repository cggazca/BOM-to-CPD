package com.mentor.dms.contentprovider.sf.request.data.filter;

import com.mentor.dms.contentprovider.core.criterion.ICriterion;
import com.mentor.dms.contentprovider.core.request.Filter;
import com.mentor.dms.contentprovider.sf.request.data.LeftBoundary;
import com.mentor.dms.contentprovider.sf.request.data.RightBoundary;

public class FilterDummy implements Filter {
  private String __valueOperator__ = "Equality";
  
  private Object value;
  
  private String propertyId;
  
  private String __expression__ = "ValueExpression";
  
  private String pattern = null;
  
  private LeftBoundary leftBoundary = null;
  
  private RightBoundary rightBoundary = null;
  
  public FilterDummy(ICriterion paramICriterion) {}
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\request\data\filter\FilterDummy.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */