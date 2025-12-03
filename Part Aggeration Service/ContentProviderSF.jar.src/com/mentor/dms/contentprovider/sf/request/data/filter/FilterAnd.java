package com.mentor.dms.contentprovider.sf.request.data.filter;

import com.mentor.dms.contentprovider.core.criterion.ICriterion;
import com.mentor.dms.contentprovider.core.criterion.Junction;
import com.mentor.dms.contentprovider.core.request.Filter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FilterAnd implements Filter {
  private String __logicalOperator__ = "And";
  
  private String __expression__ = "LogicalExpression";
  
  private Filter left;
  
  private Filter right;
  
  public FilterAnd(Junction paramJunction) {
    Collection collection = paramJunction.getCriterion();
    ArrayList<Filter> arrayList = new ArrayList();
    for (ICriterion iCriterion : collection)
      arrayList.add(FilterFactory.create(iCriterion)); 
    List<Filter> list = nestFilters(arrayList);
    this.left = list.get(0);
    this.right = list.get(1);
  }
  
  private FilterAnd(Filter paramFilter1, Filter paramFilter2) {
    this.left = paramFilter1;
    this.right = paramFilter2;
  }
  
  private static List<Filter> nestFilters(List<Filter> paramList) {
    if (paramList.size() == 2)
      return paramList; 
    ArrayList<Filter> arrayList = new ArrayList();
    int i = paramList.size();
    for (byte b = 0; b < i; b++) {
      Filter filter = paramList.get(b);
      if (++b < i)
        filter = new FilterAnd(filter, paramList.get(b)); 
      arrayList.add(filter);
    } 
    return nestFilters(arrayList);
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\request\data\filter\FilterAnd.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */