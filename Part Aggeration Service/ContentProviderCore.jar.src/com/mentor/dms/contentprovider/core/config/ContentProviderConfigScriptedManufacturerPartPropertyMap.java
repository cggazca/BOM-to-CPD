package com.mentor.dms.contentprovider.core.config;

import com.mentor.dms.contentprovider.core.config.xml.ConfigXMLScriptedManufacturerPartPropertyMap;
import java.util.HashSet;
import java.util.Set;

public class ContentProviderConfigScriptedManufacturerPartPropertyMap extends AbstractContentProviderConfigManufacturerPartPropertyMap {
  private String mappingFunction;
  
  private HashSet<String> componentPropDMNSet = new HashSet<>();
  
  public ContentProviderConfigScriptedManufacturerPartPropertyMap(ContentProviderConfigManufacturerPartMap paramContentProviderConfigManufacturerPartMap) {
    super(paramContentProviderConfigManufacturerPartMap);
  }
  
  public void setMappingFunction(String paramString) {
    this.mappingFunction = paramString;
  }
  
  public String getMappingFunction() {
    return this.mappingFunction;
  }
  
  public void read(ConfigXMLScriptedManufacturerPartPropertyMap paramConfigXMLScriptedManufacturerPartPropertyMap) throws ContentProviderConfigException {
    super.read(paramConfigXMLScriptedManufacturerPartPropertyMap);
    this.mappingFunction = paramConfigXMLScriptedManufacturerPartPropertyMap.getMappingFunction();
  }
  
  public void addComponentProperty(String paramString) {
    this.componentPropDMNSet.add(paramString);
  }
  
  public Set<String> getComponentPropertySet() {
    return this.componentPropDMNSet;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\config\ContentProviderConfigScriptedManufacturerPartPropertyMap.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */