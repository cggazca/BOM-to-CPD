package com.mentor.dms.contentprovider.core.utils.setrepmpn;

public class ManufacturerPart extends AbstractEDMObject {
  private ComponentAML aml = null;
  
  ManufacturerPart(ComponentAML paramComponentAML, String paramString) {
    super(paramString);
    this.aml = paramComponentAML;
  }
  
  public ComponentAML getAML() {
    return this.aml;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\cor\\utils\setrepmpn\ManufacturerPart.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */