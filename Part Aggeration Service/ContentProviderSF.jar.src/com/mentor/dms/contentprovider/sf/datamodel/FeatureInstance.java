package com.mentor.dms.contentprovider.sf.datamodel;

public class FeatureInstance {
  public boolean bStaticPackage = false;
  
  public boolean bDynamicPackage = false;
  
  public boolean bSearchable = false;
  
  public boolean bScriptedMapping = false;
  
  public boolean bDoubleMapping = false;
  
  public String type = "";
  
  public String units = "";
  
  private Feature feature = null;
  
  public FeatureInstance(Feature paramFeature) {
    this.feature = paramFeature;
  }
  
  public Feature getFeature() {
    return this.feature;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\datamodel\FeatureInstance.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */