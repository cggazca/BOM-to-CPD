package com.mentor.dms.contentprovider.core.utils.setrepmpn;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class SetRepMPNScriptEngine {
  private ScriptEngine jsEngine;
  
  private String jsText = "";
  
  private static SetRepMPNScriptEngine scriptEngine = null;
  
  private SetRepMPNScriptEngine() {
    ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
    this.jsEngine = scriptEngineManager.getEngineByExtension("js");
    this.jsEngine.put("javax.script.filename", "Set Representative MPN configuration JavaScript");
  }
  
  public static SetRepMPNScriptEngine getScriptEngine() {
    if (scriptEngine == null)
      scriptEngine = new SetRepMPNScriptEngine(); 
    return scriptEngine;
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
  
  public boolean callFilter(PropertyValue paramPropertyValue, String paramString) throws SetRepMPNException {
    Bindings bindings = this.jsEngine.createBindings();
    this.jsEngine.setBindings(bindings, 100);
    try {
      this.jsEngine.put("value", paramPropertyValue.getValue());
      this.jsEngine.eval(this.jsText);
      return ((Boolean)this.jsEngine.eval(paramString)).booleanValue();
    } catch (Exception exception) {
      throw new SetRepMPNException(exception.getMessage());
    } 
  }
  
  public PropertyValue callCustomRollup(Collection<PropertyValue> paramCollection, String paramString) throws SetRepMPNException {
    Bindings bindings = this.jsEngine.createBindings();
    this.jsEngine.setBindings(bindings, 100);
    ArrayList<Object> arrayList = new ArrayList();
    for (PropertyValue propertyValue : paramCollection)
      arrayList.add(propertyValue.getValue()); 
    try {
      this.jsEngine.put("values", arrayList);
      this.jsEngine.eval(this.jsText);
      return new PropertyValue(this.jsEngine.eval(paramString), null);
    } catch (Exception exception) {
      throw new SetRepMPNException(exception.getMessage());
    } 
  }
  
  public int callCustomSelectionCriteria(PropertyValue paramPropertyValue1, PropertyValue paramPropertyValue2, String paramString) throws SetRepMPNException {
    Bindings bindings = this.jsEngine.createBindings();
    this.jsEngine.setBindings(bindings, 100);
    try {
      this.jsEngine.put("value1", paramPropertyValue1.getValue());
      this.jsEngine.put("value2", paramPropertyValue2.getValue());
      this.jsEngine.eval(this.jsText);
      return ((Integer)this.jsEngine.eval(paramString)).intValue();
    } catch (Exception exception) {
      throw new SetRepMPNException(exception.getMessage());
    } 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\cor\\utils\setrepmpn\SetRepMPNScriptEngine.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */