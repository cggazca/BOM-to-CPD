package com.mentor.dms.contentprovider.plugin.searchui;

import com.mentor.datafusion.oi.model.OIField;
import com.mentor.dms.contentprovider.config.ContentProviderConfigProperty;

public class ContentProviderCharacteristic {
  ContentProviderSearchRestriction parent;
  
  private OIField oiField;
  
  private ContentProviderConfigProperty property;
  
  public ContentProviderCharacteristic(ContentProviderSearchRestriction paramContentProviderSearchRestriction, OIField paramOIField, ContentProviderConfigProperty paramContentProviderConfigProperty) {
    this.parent = paramContentProviderSearchRestriction;
    this.oiField = paramOIField;
    this.property = paramContentProviderConfigProperty;
  }
  
  public ContentProviderSearchRestriction getParent() {
    return this.parent;
  }
  
  public void setParent(ContentProviderSearchRestriction paramContentProviderSearchRestriction) {
    this.parent = paramContentProviderSearchRestriction;
  }
  
  public OIField<?> getOIField() {
    return this.oiField;
  }
  
  public ContentProviderConfigProperty getProperty() {
    return this.property;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\plugin\searchui\ContentProviderCharacteristic.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */