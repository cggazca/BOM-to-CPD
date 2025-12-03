package com.mentor.dms.contentprovider.core;

public enum ContentProviderRole {
  NONE(0),
  DESIGNER_SEARCH(1),
  COMPONENT_ENGINEER_SEARCH(2),
  MANUFACTURER_PART_CREATION(4),
  MANUFACTURER_PART_SYNCHRONIZATION(8),
  COMPONENT_SYNCHRONIZATION(16),
  VIEWABLE_CONTENT(32);
  
  private int _val;
  
  ContentProviderRole(int paramInt1) {
    this._val = paramInt1;
  }
  
  public int getValue() {
    return this._val;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\ContentProviderRegistryEntry$ContentProviderRole.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */