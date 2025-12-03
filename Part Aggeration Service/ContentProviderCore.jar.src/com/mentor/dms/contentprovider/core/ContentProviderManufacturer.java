package com.mentor.dms.contentprovider.core;

import java.util.Collection;
import java.util.HashMap;

public class ContentProviderManufacturer {
  private HashMap<String, ContentProviderManufacturerProperty> propMap = new HashMap<>();
  
  public void addProperty(String paramString1, String paramString2, ContentProviderManufacturerProperty.MfgPropertyType paramMfgPropertyType) {
    ContentProviderManufacturerProperty contentProviderManufacturerProperty = new ContentProviderManufacturerProperty(paramString1, paramString2, paramMfgPropertyType);
    this.propMap.put(paramString1, contentProviderManufacturerProperty);
  }
  
  public void addProperty(ContentProviderManufacturerProperty paramContentProviderManufacturerProperty) {
    this.propMap.put(paramContentProviderManufacturerProperty.getName(), paramContentProviderManufacturerProperty);
  }
  
  public ContentProviderManufacturerProperty getPropertyByName(String paramString) {
    return this.propMap.get(paramString);
  }
  
  public Collection<ContentProviderManufacturerProperty> getProperties() {
    return this.propMap.values();
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\ContentProviderManufacturer.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */