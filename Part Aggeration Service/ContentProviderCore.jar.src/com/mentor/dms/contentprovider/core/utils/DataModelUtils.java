package com.mentor.dms.contentprovider.core.utils;

import com.mentor.datafusion.oi.OICursor;
import com.mentor.datafusion.oi.OIException;
import com.mentor.datafusion.oi.OIObjectManager;
import com.mentor.datafusion.oi.OIQuery;
import com.mentor.datafusion.oi.model.OIClass;
import com.mentor.datafusion.oi.model.OIField;
import com.mentor.datafusion.oi.type.OIObject;
import java.util.ArrayList;
import java.util.HashMap;

public class DataModelUtils {
  private static HashMap<String, HashMap<String, String>> classNumToCharKeyMap = new HashMap<>();
  
  private static HashMap<String, String> classNumMap = null;
  
  private static HashMap<String, HashMap<String, OIClass>> rootOIClass2CatalogLabelMap = new HashMap<>();
  
  private OIObjectManager om;
  
  public DataModelUtils(OIObjectManager paramOIObjectManager) {
    this.om = paramOIObjectManager;
  }
  
  public String getInfoLabel(OIField<?> paramOIField) throws OIException {
    loadClassNumMap();
    String str1 = paramOIField.getName();
    OIClass oIClass1 = paramOIField.getDeclaringClass();
    OIClass oIClass2 = oIClass1.getRootClass();
    String str2 = classNumMap.get(oIClass2.getName());
    String str3 = oIClass1.getName();
    String str4 = str3 + "." + str3;
    HashMap<Object, Object> hashMap = (HashMap)classNumToCharKeyMap.get(str2);
    if (hashMap == null) {
      hashMap = new HashMap<>();
      classNumToCharKeyMap.put(str2, hashMap);
      HashMap<Object, Object> hashMap1 = new HashMap<>();
      OIQuery oIQuery = this.om.createQuery("Characteristic", true);
      oIQuery.addRestriction("ObjectClass", "0");
      oIQuery.addRestriction("RefClass", str2);
      oIQuery.addRestriction("Text.Language", "e");
      oIQuery.addColumn("Characteristic");
      oIQuery.addColumn("DomainModelName");
      oIQuery.addColumn("Text.InformationText");
      OICursor oICursor = oIQuery.execute();
      while (oICursor.next()) {
        String str = oICursor.getString("Characteristic");
        hashMap1.put(str, oICursor.getProxyObject());
      } 
      oICursor.close();
      oIQuery = this.om.createQuery("CatalogGroup", true);
      oIQuery.addRestriction("CatalogCharacteristics.Characteristic", "~NULL");
      oIQuery.addRestriction("ObjectClass", str2);
      oIQuery.addColumn("DomainModelName");
      oIQuery.addColumn("CatalogCharacteristics.Characteristic");
      oICursor = oIQuery.execute();
      while (oICursor.next()) {
        String str5 = oICursor.getStringified("Characteristic");
        OIObject oIObject = (OIObject)hashMap1.get(str5);
        if (oIObject == null)
          continue; 
        String str6 = oICursor.getString("DomainModelName") + "." + oICursor.getString("DomainModelName");
        hashMap.put(str6, oIObject.getString("InformationText"));
      } 
      oICursor.close();
    } 
    return (String)hashMap.get(str4);
  }
  
  private void loadClassNumMap() throws OIException {
    if (classNumMap == null) {
      classNumMap = new HashMap<>();
      OIQuery oIQuery = this.om.createQuery("ObjectClass", true);
      oIQuery.addColumn("ClassNumber");
      oIQuery.addColumn("DomainModelName");
      OICursor oICursor = oIQuery.execute();
      while (oICursor.next())
        classNumMap.put(oICursor.getStringified("DomainModelName"), oICursor.getStringified("ClassNumber")); 
      oICursor.close();
    } 
  }
  
  public OIField<?> findOIFieldStatic(OIClass paramOIClass, String paramString) throws OIException {
    if (paramOIClass == null)
      return null; 
    for (OIField<?> oIField : (Iterable<OIField<?>>)paramOIClass.getFields()) {
      if (!oIField.getName().startsWith("seSt"))
        continue; 
      String str1 = oIField.getName().substring(4, oIField.getName().length());
      String str2 = getInfoLabel(oIField);
      String str3 = str2.replaceAll("\\s+", "");
      if (paramString.equals(str1) || paramString.equals(str2) || paramString.equals(str3))
        return oIField; 
    } 
    return null;
  }
  
  public OIField<?> findOIFieldParametric(OIClass paramOIClass, String paramString) throws OIException {
    if (paramOIClass == null)
      return null; 
    for (OIField<?> oIField : (Iterable<OIField<?>>)paramOIClass.getDeclaredFields()) {
      if (!oIField.hasFlag(OIField.Flag.INPUT))
        continue; 
      String str = getInfoLabel(oIField);
      if (paramString.equals(str))
        return oIField; 
    } 
    return null;
  }
  
  public OIField<?> findOIFieldParametricByDMN(OIClass paramOIClass, String paramString) throws OIException {
    if (paramOIClass == null)
      return null; 
    for (OIField<?> oIField : (Iterable<OIField<?>>)paramOIClass.getDeclaredFields()) {
      if (oIField.hasFlag(OIField.Flag.INPUT) && oIField.getName().equals(paramString))
        return oIField; 
    } 
    return null;
  }
  
  public OIClass findSubOIClass(OIClass paramOIClass, String paramString) {
    if (paramOIClass == null)
      return null; 
    for (OIClass oIClass : paramOIClass.getSubclasses()) {
      if (oIClass.getLabel().equals(paramString))
        return oIClass; 
    } 
    return null;
  }
  
  public OIClass findDescendentOIClass(OIClass paramOIClass, String paramString) {
    if (paramOIClass == null)
      return null; 
    HashMap<Object, Object> hashMap = (HashMap)rootOIClass2CatalogLabelMap.get(paramOIClass.getName());
    if (hashMap == null) {
      hashMap = new HashMap<>();
      loadCatalogLabelMap((HashMap)hashMap, paramOIClass);
    } 
    return (OIClass)hashMap.get(paramString);
  }
  
  private void loadCatalogLabelMap(HashMap<String, OIClass> paramHashMap, OIClass paramOIClass) {
    for (OIClass oIClass : paramOIClass.getSubclasses()) {
      paramHashMap.put(getCatalogPath(oIClass, "."), oIClass);
      loadCatalogLabelMap(paramHashMap, oIClass);
    } 
  }
  
  public String getCatalogPath(OIClass paramOIClass) {
    return getCatalogPath(paramOIClass, " -> ");
  }
  
  public String getCatalogPath(OIClass paramOIClass, String paramString) {
    ArrayList<String> arrayList = new ArrayList();
    OIClass oIClass = paramOIClass.getRootClass();
    while (!paramOIClass.getSuperclass().equals(oIClass)) {
      arrayList.add(paramOIClass.getLabel());
      paramOIClass = paramOIClass.getSuperclass();
    } 
    String str = arrayList.get(arrayList.size() - 1);
    for (int i = arrayList.size() - 2; i >= 0; i--)
      str = str + str + paramString; 
    return str;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\cor\\utils\DataModelUtils.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */