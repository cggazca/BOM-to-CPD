package com.mentor.dms.contentprovider.core;

import com.mentor.datafusion.oi.OIException;
import com.mentor.datafusion.oi.type.OIBitSet;
import com.mentor.datafusion.oi.type.OIObject;
import com.mentor.datafusion.oi.type.OIObjectSet;
import java.util.HashMap;
import java.util.Map;

public class ContentProviderRegistryEntry {
  private String id;
  
  private String name;
  
  private int tabOrder = -1;
  
  private String className;
  
  private String toolboxId;
  
  private int ccpRoles = 0;
  
  private HashMap<String, String> configParamsMap = new HashMap<>();
  
  private HashMap<String, String> propNamesMap = new HashMap<>();
  
  private HashMap<String, String> credentialsMap = new HashMap<>();
  
  public void load(OIObject paramOIObject) throws ContentProviderException {
    try {
      this.toolboxId = paramOIObject.getString("ToolBoxId");
      OIObjectSet oIObjectSet = paramOIObject.getSet("MetaDataMap");
      for (OIObject oIObject : oIObjectSet) {
        String str1 = oIObject.getString("Key");
        String str2 = oIObject.getString("Value");
        if (str1.equals("PROVIDER_ID")) {
          this.id = str2;
          continue;
        } 
        if (str1.equals("PROVIDER_NAME")) {
          this.name = str2;
          continue;
        } 
        if (str1.equals("PROVIDER_SEARCH_TAB_ORDER")) {
          try {
            this.tabOrder = Integer.parseInt(str2);
          } catch (Exception exception) {
            throw new ContentProviderException("Content Provider Configuration '" + this.toolboxId + "':  PROVIDER_SEARCH_TAB_ORDER must be a positive integer.");
          } 
          continue;
        } 
        if (str1.equals("PROVIDER_IMPLEMENTATION")) {
          this.className = str2;
          continue;
        } 
        this.configParamsMap.put(str1, str2);
      } 
      if (this.id == null)
        throw new ContentProviderException("Content Provider Configuration '" + this.toolboxId + "' must provide a PROVIDER_ID."); 
      if (this.name == null)
        throw new ContentProviderException("Content Provider Configuration '" + this.toolboxId + "' must provide a PROVIDER_NAME."); 
      if (this.tabOrder == -1)
        throw new ContentProviderException("Content Provider Configuration '" + this.toolboxId + "' must provide a PROVIDER_SEARCH_TAB_ORDER."); 
      if (this.className == null)
        throw new ContentProviderException("Content Provider Configuration '" + this.toolboxId + "' must provide a PROVIDER_IMPLEMENTATION."); 
      OIBitSet oIBitSet = paramOIObject.getBitSet("CCPCfgRoles");
      for (byte b = 1; b < (ContentProviderRole.values()).length; b++) {
        ContentProviderRole contentProviderRole = ContentProviderRole.values()[b];
        if (oIBitSet.get(b - 1))
          this.ccpRoles |= contentProviderRole.getValue(); 
      } 
      if (paramOIObject.getOIClass().hasField("ECPropList")) {
        OIObjectSet oIObjectSet1 = paramOIObject.getSet("ECPropList");
        for (OIObject oIObject : oIObjectSet1) {
          String str1 = oIObject.getString("ECPropId");
          String str2 = oIObject.getString("ECPropName");
          this.propNamesMap.put(str1, str2);
        } 
      } 
      if (paramOIObject.getOIClass().hasField("ECCredList")) {
        OIObjectSet oIObjectSet1 = paramOIObject.getSet("ECCredList");
        for (OIObject oIObject : oIObjectSet1) {
          String str1 = oIObject.getString("ECCredId");
          String str2 = oIObject.getString("ECCredValue");
          this.credentialsMap.put(str1, str2);
        } 
      } 
    } catch (OIException oIException) {
      throw new ContentProviderException(oIException.getMessage());
    } 
  }
  
  protected String getId() {
    return this.id;
  }
  
  protected String getName() {
    return this.name;
  }
  
  protected int getSearchTabOrder() {
    return this.tabOrder;
  }
  
  protected String getClassName() {
    return this.className;
  }
  
  protected String getToolboxId() {
    return this.toolboxId;
  }
  
  protected Map<String, String> getConfigParams() {
    return this.configParamsMap;
  }
  
  protected Map<String, String> getPropertyNames() {
    return this.propNamesMap;
  }
  
  protected boolean hasRole(ContentProviderRole paramContentProviderRole) {
    return ((this.ccpRoles & paramContentProviderRole.getValue()) != 0);
  }
  
  public String getCredential(String paramString) {
    return this.credentialsMap.get(paramString);
  }
  
  public Map<String, String> getCredentials() {
    return this.credentialsMap;
  }
  
  public void setCredentials(Map<String, String> paramMap) {
    this.credentialsMap.clear();
    for (Map.Entry<String, String> entry : paramMap.entrySet())
      this.credentialsMap.put((String)entry.getKey(), (String)entry.getValue()); 
  }
  
  public enum ContentProviderRole {
    NONE(0),
    DESIGNER_SEARCH(1),
    COMPONENT_ENGINEER_SEARCH(2),
    MANUFACTURER_PART_CREATION(4),
    MANUFACTURER_PART_SYNCHRONIZATION(8),
    COMPONENT_SYNCHRONIZATION(16),
    VIEWABLE_CONTENT(32);
    
    private int _val;
    
    ContentProviderRole(int param1Int1) {
      this._val = param1Int1;
    }
    
    public int getValue() {
      return this._val;
    }
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\ContentProviderRegistryEntry.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */