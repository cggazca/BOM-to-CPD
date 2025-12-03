package com.mentor.dms.contentprovider.criterion;

import com.mentor.dms.contentprovider.AbstractCriteria;
import com.mentor.dms.contentprovider.config.ContentProviderConfigProperty;
import java.util.ArrayList;
import java.util.Collection;

public class LikeExpression implements ICriterion, IPropertyExpression {
  public static final String WILDCARD_MATCH_SINGLE = "?";
  
  public static final String WILDCARD_MATCH_MULTIPLE = "*";
  
  private ContentProviderConfigProperty property;
  
  private ArrayList<String> value = new ArrayList<>();
  
  boolean ignoreCase = false;
  
  protected LikeExpression(ContentProviderConfigProperty paramContentProviderConfigProperty) {
    this.property = paramContentProviderConfigProperty;
  }
  
  protected LikeExpression(ContentProviderConfigProperty paramContentProviderConfigProperty, boolean paramBoolean) {
    this.property = paramContentProviderConfigProperty;
    this.ignoreCase = paramBoolean;
  }
  
  public void addSubString(String paramString) {
    this.value.add(paramString);
  }
  
  public ContentProviderConfigProperty getProperty() {
    return this.property;
  }
  
  public Collection<String> getValue() {
    return this.value;
  }
  
  public boolean isIgnoreCase() {
    return this.ignoreCase;
  }
  
  public String toSearchString(AbstractCriteria paramAbstractCriteria) {
    return null;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\criterion\LikeExpression.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */