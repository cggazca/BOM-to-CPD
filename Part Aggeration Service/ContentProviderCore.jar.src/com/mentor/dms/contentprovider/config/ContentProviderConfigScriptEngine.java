package com.mentor.dms.contentprovider.config;

import com.mentor.datafusion.oi.model.OIClass;
import com.mentor.datafusion.oi.model.OIField;
import com.mentor.datafusion.oi.type.OIObject;
import com.mentor.dms.contentprovider.mapping.MappingUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class ContentProviderConfigScriptEngine {
  private ScriptEngine jsEngine;
  
  private String jsText = "";
  
  private ObjectPropertyMap mpnPropMap = null;
  
  private ObjectPropertyMap compPropMap = null;
  
  private MappingUtils mappingUtils = new MappingUtils();
  
  public ContentProviderConfigScriptEngine(MappingUtils paramMappingUtils) {
    this.mappingUtils = paramMappingUtils;
    ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
    this.jsEngine = scriptEngineManager.getEngineByExtension("js");
    this.jsEngine.put("javax.script.filename", "Content Provider mapping configuration JavaScript");
  }
  
  public void read(String paramString) throws Exception {
    this.jsText = paramString;
    this.jsEngine.eval(this.jsText);
  }
  
  public void read(File paramFile) throws Exception {
    FileInputStream fileInputStream = new FileInputStream(paramFile);
    read(fileInputStream);
  }
  
  public void read(InputStream paramInputStream) throws Exception {
    this.jsText = convertStreamToString(paramInputStream);
    this.jsEngine.eval(this.jsText);
  }
  
  public void setMappingUtils(MappingUtils paramMappingUtils) {
    this.mappingUtils = paramMappingUtils;
  }
  
  public ObjectPropertyMap getManufacturerPartPropertyMap() {
    return this.mpnPropMap;
  }
  
  public ObjectPropertyMap getComponentPropertyMap() {
    return this.compPropMap;
  }
  
  public Object getComponentPropertyValue(String paramString) throws Exception {
    return this.compPropMap.get(paramString);
  }
  
  public void callMappingFunction(OIObject paramOIObject1, OIObject paramOIObject2, String paramString) throws ContentProviderConfigException {
    Bindings bindings = this.jsEngine.createBindings();
    this.jsEngine.setBindings(bindings, 100);
    try {
      this.jsEngine.put("MappingUtils", this.mappingUtils);
      this.mpnPropMap = new ObjectPropertyMap("Manufacturer Part");
      if (paramOIObject1 != null)
        for (OIField oIField : paramOIObject1.getOIClass().getFields()) {
          if (oIField.hasFlag(OIField.Flag.INPUT) && (oIField.getType() == OIField.Type.STRING || oIField.getType() == OIField.Type.DOUBLE || oIField.getType() == OIField.Type.INTEGER || oIField.getType() == OIField.Type.DATE))
            this.mpnPropMap.putCurrent(oIField.getName(), paramOIObject1.get(oIField.getName())); 
        }  
      this.jsEngine.put("mpnProps", this.mpnPropMap);
      this.compPropMap = new ObjectPropertyMap("Component");
      if (paramOIObject2 != null)
        for (OIField oIField : paramOIObject2.getOIClass().getFields()) {
          if (oIField.hasFlag(OIField.Flag.INPUT) && (oIField.getType() == OIField.Type.STRING || oIField.getType() == OIField.Type.DOUBLE || oIField.getType() == OIField.Type.INTEGER || oIField.getType() == OIField.Type.DATE))
            this.compPropMap.putCurrent(oIField.getName(), paramOIObject2.get(oIField.getName())); 
        }  
      this.jsEngine.put("componentProps", this.compPropMap);
      this.jsEngine.eval(this.jsText);
      this.jsEngine.eval(paramString);
    } catch (Exception exception) {
      throw new ContentProviderConfigException(exception.getMessage());
    } 
  }
  
  public void callMappingFunction(OIClass paramOIClass1, OIClass paramOIClass2, String paramString) throws ContentProviderConfigException {
    Bindings bindings = this.jsEngine.createBindings();
    this.jsEngine.setBindings(bindings, 100);
    try {
      this.jsEngine.put("MappingUtils", this.mappingUtils);
      this.mpnPropMap = new ObjectPropertyMap("Manufacturer Part");
      for (OIField oIField : paramOIClass1.getFields()) {
        if (oIField.hasFlag(OIField.Flag.INPUT) && (oIField.getType() == OIField.Type.STRING || oIField.getType() == OIField.Type.DOUBLE || oIField.getType() == OIField.Type.INTEGER || oIField.getType() == OIField.Type.DATE))
          this.mpnPropMap.putCurrent(oIField.getName(), ""); 
      } 
      this.jsEngine.put("mpnProps", this.mpnPropMap);
      this.compPropMap = new ObjectPropertyMap("Component");
      for (OIField oIField : paramOIClass2.getFields()) {
        if (oIField.hasFlag(OIField.Flag.INPUT) && (oIField.getType() == OIField.Type.STRING || oIField.getType() == OIField.Type.DOUBLE || oIField.getType() == OIField.Type.INTEGER || oIField.getType() == OIField.Type.DATE))
          this.compPropMap.putCurrent(oIField.getName(), ""); 
      } 
      this.jsEngine.put("componentProps", this.compPropMap);
      this.jsEngine.eval(this.jsText);
      this.jsEngine.eval(paramString);
    } catch (ScriptException scriptException) {
      throw new ContentProviderConfigException(scriptException.getMessage());
    } catch (RuntimeException runtimeException) {
      if (runtimeException.getCause() instanceof ContentProviderConfigFieldNotFoundException)
        throw new ContentProviderConfigFieldNotFoundException(runtimeException.getMessage()); 
      throw new ContentProviderConfigException(runtimeException.getMessage());
    } 
  }
  
  private String convertStreamToString(InputStream paramInputStream) throws IOException {
    if (paramInputStream != null) {
      StringWriter stringWriter = new StringWriter();
      char[] arrayOfChar = new char[1024];
      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(paramInputStream, "UTF-8"));
      int i;
      while ((i = bufferedReader.read(arrayOfChar)) != -1)
        stringWriter.write(arrayOfChar, 0, i); 
      return stringWriter.toString();
    } 
    return "";
  }
  
  public static void main(String[] paramArrayOfString) {
    ContentProviderConfigScriptEngine contentProviderConfigScriptEngine = new ContentProviderConfigScriptEngine(new MappingUtils());
    try {
      contentProviderConfigScriptEngine.read("");
      contentProviderConfigScriptEngine.callMappingFunction((OIObject)null, (OIObject)null, "resistanceValue = MappingUtils.getSingleValueDouble('1Ohm', 'Ohm'); componentProps.put(\"testResistance\", resistanceValue);");
      Object object = contentProviderConfigScriptEngine.getComponentPropertyValue("resistanceValue");
      System.out.println(String.valueOf(object.getClass()) + " : " + String.valueOf(object.getClass()));
    } catch (Exception exception) {
      exception.printStackTrace();
    } 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\config\ContentProviderConfigScriptEngine.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */