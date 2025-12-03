package com.mentor.dms.contentprovider.sf.datamodel.csv2xml;

import com.mentor.dms.contentprovider.core.AbstractContentProvider;
import com.mentor.dms.contentprovider.core.config.ContentProviderConfig;
import com.mentor.dms.contentprovider.core.config.ContentProviderConfigComponentCatalog;
import com.mentor.dms.contentprovider.core.config.ContentProviderConfigMPNCatalog;
import com.mentor.dms.contentprovider.core.utils.validate.CPPartClass;
import com.mentor.dms.contentprovider.core.utils.validate.CPProperty;
import com.mentor.dms.contentprovider.sf.datamodel.DMUpgradeProperties;
import com.mentor.dms.contentprovider.sf.datamodel.csv2xml.csvdata.CSVParseException;
import com.mentor.dms.contentprovider.sf.datamodel.csv2xml.csvdata.CatalogFromCsv;
import com.mentor.dms.contentprovider.sf.datamodel.csv2xml.csvdata.PropertyFromCsv;
import com.mentor.dms.contentprovider.sf.datamodel.definition.CatalogDef;
import com.mentor.dms.contentprovider.sf.datamodel.definition.CharacteristicDef;
import com.mentor.dms.contentprovider.sf.datamodel.definition.DataModelDefinition;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public abstract class CSVParseBase {
  public static final int COL_PART_ID = 1;
  
  public static final int COL_PART_NAME = 2;
  
  public static final int COL_PROP_ID = 3;
  
  public static final int COL_PROP_NAME = 4;
  
  public static final int COL_CHK_ITEM1 = 6;
  
  public static final int COL_CHK_ITEM2 = 7;
  
  public static final int COL_CHK_CONTENT = 8;
  
  public static final int COL_RESULT = 9;
  
  public static final int COL_COMMAND = 10;
  
  public static final int COL_TAB_SHEET = 11;
  
  protected String csvPath = null;
  
  protected AbstractContentProvider cpp;
  
  protected List<CPPartClass> partListCP = null;
  
  protected int errorNum = 0;
  
  protected int warnNum = 0;
  
  protected Map<String, HashMap<String, CPProperty>> partPropetiesCP = new HashMap<>();
  
  protected DataModelDefinition existingDMD = null;
  
  private ContentProviderConfig config = null;
  
  public CSVParseBase(String paramString, AbstractContentProvider paramAbstractContentProvider, ContentProviderConfig paramContentProviderConfig) {
    this.csvPath = paramString;
    this.cpp = paramAbstractContentProvider;
    this.config = paramContentProviderConfig;
  }
  
  public int getErrorNum() {
    return this.errorNum;
  }
  
  public int getWarnNum() {
    return this.warnNum;
  }
  
  protected void addAllProperties(CatalogFromCsv paramCatalogFromCsv, Map<String, CPProperty> paramMap) throws CSVParseException {
    for (CPProperty cPProperty : paramMap.values()) {
      if (cPProperty.getType().equals("Collection"))
        continue; 
      PropertyFromCsv propertyFromCsv = new PropertyFromCsv(cPProperty.getId(), cPProperty.getName());
      propertyFromCsv.setCommand(PropertyFromCsv.Command.ADD.toValue());
      propertyFromCsv.setTabName(getTabSheetNameEDM(cPProperty.getId()));
      propertyFromCsv.setType(cPProperty.getType());
      propertyFromCsv.setUnit(cPProperty.getUnit());
      paramCatalogFromCsv.addProperties(propertyFromCsv);
    } 
  }
  
  protected String getTabSheetNameEDM(String paramString) throws CSVParseException {
    CharacteristicDef characteristicDef = (CharacteristicDef)this.existingDMD.charDMNMap.get(paramString);
    return (characteristicDef != null) ? characteristicDef.tabSheet : DMUpgradeProperties.getProperty("Charact_TabSheetName");
  }
  
  protected String getParentCatalogID(String paramString) {
    String str = null;
    for (CPPartClass cPPartClass : this.partListCP) {
      if (paramString.equals(cPPartClass.getId())) {
        str = cPPartClass.getParentID();
        break;
      } 
    } 
    if (str != null) {
      HashMap hashMap = this.existingDMD.catalogIdMap;
      for (String str1 : hashMap.keySet()) {
        CatalogDef catalogDef = (CatalogDef)hashMap.get(str1);
        if (catalogDef.domainModelName.equals(str))
          return str1; 
      } 
    } 
    return "";
  }
  
  protected HashMap<String, CPProperty> getPropertiesCP(String paramString) throws Exception {
    if (!this.partPropetiesCP.containsKey(paramString)) {
      Thread.sleep(500L);
      HashMap<Object, Object> hashMap = new HashMap<>();
      List list = this.cpp.getPartProperties(paramString);
      for (CPProperty cPProperty : list)
        hashMap.put(cPProperty.getId(), cPProperty); 
      this.partPropetiesCP.put(paramString, hashMap);
    } 
    return this.partPropetiesCP.get(paramString);
  }
  
  protected boolean isCatalogNameChanged(CatalogFromCsv paramCatalogFromCsv) {
    String str = paramCatalogFromCsv.getDomainName();
    CatalogDef catalogDef = (CatalogDef)this.existingDMD.catalogDMNMap.get(str);
    return (catalogDef == null) ? false : (!catalogDef.abbreviation.equals(paramCatalogFromCsv.getName()));
  }
  
  protected boolean isPropertyNameChanged(PropertyFromCsv paramPropertyFromCsv) {
    String str = paramPropertyFromCsv.getDomainName();
    CharacteristicDef characteristicDef = (CharacteristicDef)this.existingDMD.charDMNMap.get(str);
    return (characteristicDef == null) ? false : (!characteristicDef.informationText.equals(paramPropertyFromCsv.getName()));
  }
  
  protected String getMappedMPN(String paramString) {
    Collection collection = this.config.getMPNCatalogsByContentProviderId(paramString);
    Iterator<ContentProviderConfigMPNCatalog> iterator = collection.iterator();
    if (iterator.hasNext()) {
      ContentProviderConfigMPNCatalog contentProviderConfigMPNCatalog = iterator.next();
      return contentProviderConfigMPNCatalog.getClassDMN();
    } 
    return "";
  }
  
  protected String getMappedComponent(String paramString) {
    Collection collection = this.config.getComponentCatalogsByContentProviderId(paramString);
    Iterator<ContentProviderConfigComponentCatalog> iterator = collection.iterator();
    if (iterator.hasNext()) {
      ContentProviderConfigComponentCatalog contentProviderConfigComponentCatalog = iterator.next();
      return contentProviderConfigComponentCatalog.getClassDMN();
    } 
    return "";
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\datamodel\csv2xml\CSVParseBase.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */