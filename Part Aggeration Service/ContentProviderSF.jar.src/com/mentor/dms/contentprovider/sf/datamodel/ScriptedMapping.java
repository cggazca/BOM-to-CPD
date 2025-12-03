package com.mentor.dms.contentprovider.sf.datamodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ScriptedMapping {
  private static HashMap<String, HashMap<String, ScriptedMapping>> scriptedMappingMap = new HashMap<>();
  
  private String property;
  
  private String function;
  
  private ArrayList<ScriptedMappingArgument> argumentList = new ArrayList<>();
  
  public ScriptedMapping(String paramString1, String paramString2) {
    this.property = paramString1;
    this.function = paramString2;
  }
  
  public void addStringArgument(String paramString) {
    this.argumentList.add(new ScriptedMappingArgumentString(paramString));
  }
  
  public void addMPNArgument(String paramString) {
    this.argumentList.add(new ScriptedMappingArgumentMPNProperty(paramString));
  }
  
  public String getFunctionString(Map<String, String> paramMap) {
    null = "tempValue = MappingUtils." + this.function + "(";
    String str = "";
    for (ScriptedMappingArgument scriptedMappingArgument : this.argumentList) {
      null = null + null + str;
      str = ", ";
    } 
    return null + "); componentProps.put('" + null + "_R', tempValue);";
  }
  
  public static void add(String paramString, ScriptedMapping paramScriptedMapping) {
    HashMap<Object, Object> hashMap = (HashMap)scriptedMappingMap.get(paramString);
    if (hashMap == null) {
      hashMap = new HashMap<>();
      scriptedMappingMap.put(paramString, hashMap);
    } 
    hashMap.put(paramScriptedMapping.property, paramScriptedMapping);
  }
  
  public static ScriptedMapping get(String paramString1, String paramString2, boolean paramBoolean) {
    ScriptedMapping scriptedMapping = null;
    HashMap hashMap = scriptedMappingMap.get(paramString1);
    if (hashMap != null)
      scriptedMapping = (ScriptedMapping)hashMap.get(paramString2); 
    if (!paramBoolean && scriptedMapping == null) {
      hashMap = scriptedMappingMap.get("*");
      if (hashMap != null)
        scriptedMapping = (ScriptedMapping)hashMap.get(paramString2); 
    } 
    return scriptedMapping;
  }
  
  public static boolean hasExplicitPropMaps(String paramString) {
    return scriptedMappingMap.containsKey(paramString);
  }
  
  public static boolean isPropMapped(String paramString1, String paramString2, boolean paramBoolean) {
    ScriptedMapping scriptedMapping = get(paramString1, paramString2, paramBoolean);
    return (scriptedMapping != null);
  }
  
  public static String getGenericFunctionString(String paramString1, String paramString2) {
    null = "tempValue = MappingUtils.getSingleValueDouble(";
    null = null + "mpnProps.get('" + null + "', '" + paramString1 + "')";
    return null + "); componentProps.put('" + null + "_R', tempValue);";
  }
  
  private class ScriptedMappingArgumentString extends ScriptedMappingArgument {
    public ScriptedMappingArgumentString(String param1String) {
      super(param1String);
    }
    
    public String getMappingString(Map<String, String> param1Map) {
      return "'" + this.argument + "'";
    }
  }
  
  private class ScriptedMappingArgumentMPNProperty extends ScriptedMappingArgument {
    public ScriptedMappingArgumentMPNProperty(String param1String) {
      super(param1String);
    }
    
    public String getMappingString(Map<String, String> param1Map) {
      return "mpnProps.get('" + (String)param1Map.get(this.argument) + "')";
    }
  }
  
  private abstract class ScriptedMappingArgument {
    protected String argument;
    
    public ScriptedMappingArgument(String param1String) {
      this.argument = param1String;
    }
    
    public abstract String getMappingString(Map<String, String> param1Map);
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\datamodel\ScriptedMapping.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */