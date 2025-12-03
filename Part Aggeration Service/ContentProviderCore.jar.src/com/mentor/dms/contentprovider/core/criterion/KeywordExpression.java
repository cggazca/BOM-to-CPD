package com.mentor.dms.contentprovider.core.criterion;

import com.mentor.dms.contentprovider.core.AbstractCriteria;

public class KeywordExpression implements ICriterion {
  private String value;
  
  private MatchType matchType;
  
  private boolean ignoreCase = true;
  
  protected KeywordExpression(String paramString, MatchType paramMatchType) {
    this.value = paramString;
    this.matchType = paramMatchType;
  }
  
  public String getValue() {
    return this.value;
  }
  
  public MatchType getMatchType() {
    return this.matchType;
  }
  
  public String toSearchString(AbstractCriteria paramAbstractCriteria) {
    return null;
  }
  
  public enum MatchType {
    ALL, ANY, EXACT;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\criterion\KeywordExpression.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */