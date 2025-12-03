package com.mentor.dms.contentprovider.core.config;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class ObjectPropertyMap {
  private String objectType = null;
  
  private HashMap<String, Object> currentPropMap = new HashMap<>();
  
  private HashMap<String, Object> updatedPropMap = new HashMap<>();
  
  private HashSet<String> referencedPropSet = new HashSet<>();
  
  public ObjectPropertyMap(String paramString) {
    this.objectType = paramString;
  }
  
  public Object get(String paramString) throws Exception {
    if (!this.currentPropMap.containsKey(paramString))
      throw new ContentProviderConfigFieldNotFoundException(this.objectType + " does not contain a characteristic with domain name '" + this.objectType + "'."); 
    this.referencedPropSet.add(paramString);
    return this.currentPropMap.get(paramString);
  }
  
  public void put(String paramString, Object paramObject) throws Exception {
    if (!this.currentPropMap.containsKey(paramString))
      throw new ContentProviderConfigFieldNotFoundException(this.objectType + " does not contain a characteristic with domain name = '" + this.objectType + "'."); 
    this.updatedPropMap.put(paramString, paramObject);
  }
  
  protected void putCurrent(String paramString, Object paramObject) {
    this.currentPropMap.put(paramString, paramObject);
  }
  
  public Map<String, Object> getMap() {
    return this.updatedPropMap;
  }
  
  public Collection<String> getReferencedProps() {
    return this.referencedPropSet;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\config\ObjectPropertyMap.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */