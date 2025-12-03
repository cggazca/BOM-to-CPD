package com.mentor.dms.contentprovider.core.plugin.searchui;

import com.mentor.dms.contentprovider.core.config.ContentProviderConfigProperty;

public class ContentProviderSearchResultsColumn {
  private String columnName;
  
  private ContentProviderConfigProperty property;
  
  public ContentProviderSearchResultsColumn(String paramString, ContentProviderConfigProperty paramContentProviderConfigProperty) {
    this.columnName = paramString;
    this.property = paramContentProviderConfigProperty;
  }
  
  public String getColumnName() {
    return this.columnName;
  }
  
  public ContentProviderConfigProperty getProperty() {
    return this.property;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\plugin\searchui\ContentProviderSearchResultsColumn.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */