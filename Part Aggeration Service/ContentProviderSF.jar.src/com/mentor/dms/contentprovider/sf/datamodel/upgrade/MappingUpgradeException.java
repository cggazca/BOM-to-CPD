package com.mentor.dms.contentprovider.sf.datamodel.upgrade;

public class MappingUpgradeException extends Exception {
  private ELevel level;
  
  public ELevel getLevel() {
    return this.level;
  }
  
  public MappingUpgradeException(String paramString, ELevel paramELevel) {
    super(paramString);
    this.level = paramELevel;
  }
  
  public enum ELevel {
    ERROR, WARN, INFO, DEBUG;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\datamode\\upgrade\MappingUpgradeException.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */