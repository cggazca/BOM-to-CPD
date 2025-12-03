package com.mentor.dms.contentprovider.sf.datamodel;

import java.util.Map;

class ScriptedMappingArgumentMPNProperty extends ScriptedMapping.ScriptedMappingArgument {
  public ScriptedMappingArgumentMPNProperty(String paramString) {
    super(paramString);
  }
  
  public String getMappingString(Map<String, String> paramMap) {
    return "mpnProps.get('" + (String)paramMap.get(this.argument) + "')";
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\datamodel\ScriptedMapping$ScriptedMappingArgumentMPNProperty.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */