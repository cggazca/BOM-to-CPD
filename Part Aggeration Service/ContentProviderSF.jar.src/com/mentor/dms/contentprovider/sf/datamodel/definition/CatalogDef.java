package com.mentor.dms.contentprovider.sf.datamodel.definition;

import com.mentor.datafusion.utils.logger.MGLogger;
import java.util.HashMap;
import org.apache.commons.lang3.StringUtils;

public class CatalogDef {
  static MGLogger logger = MGLogger.getLogger(CatalogDef.class);
  
  protected DataModelDefinition dataModelDefinition = null;
  
  public String objectid;
  
  public String objectClass;
  
  public String parentKey;
  
  public String catalogStatus;
  
  public String domainModelName;
  
  public String abbreviation;
  
  public String initFilename;
  
  public String catalogTitle;
  
  public String description;
  
  public String path = "";
  
  public HashMap<String, CatalogCharactersticDef> charLabelMap = new HashMap<>(101);
  
  public HashMap<String, CatalogCharactersticDef> charDMNMap = new HashMap<>(101);
  
  public CatalogDef(DataModelDefinition paramDataModelDefinition) {
    this.dataModelDefinition = paramDataModelDefinition;
  }
  
  public CatalogDef(CatalogDef paramCatalogDef, String paramString1, String paramString2) {
    this.dataModelDefinition = paramCatalogDef.dataModelDefinition;
    this.objectid = paramString1;
    this.objectClass = paramCatalogDef.objectClass;
    this.parentKey = paramString2;
    this.catalogStatus = paramCatalogDef.catalogStatus;
    this.domainModelName = paramCatalogDef.domainModelName;
    this.abbreviation = paramCatalogDef.abbreviation;
    this.initFilename = paramCatalogDef.initFilename;
    this.catalogTitle = paramCatalogDef.catalogTitle;
    this.description = paramCatalogDef.description;
    this.path = paramCatalogDef.path;
  }
  
  public void read(ObjectType paramObjectType) throws Exception {
    this.objectid = paramObjectType.getObjectid();
    for (FieldType fieldType : paramObjectType.getListOrField()) {
      if (fieldType instanceof FieldType) {
        FieldType fieldType1 = fieldType;
        String str1 = fieldType1.getId();
        String str2 = fieldType1.getValue();
        if (str1.equals("ParentKey")) {
          this.parentKey = str2;
        } else if (str1.equals("ObjectClass")) {
          this.objectClass = str2;
        } else if (str1.equals("DomainModelName")) {
          this.domainModelName = str2;
          this.dataModelDefinition.catalogDMNMap.put(this.domainModelName, this);
        } else if (str1.equals("CatalogStatus")) {
          this.catalogStatus = str2;
        } else if (str1.equals("InitFileName")) {
          this.initFilename = str2;
        } 
      } else if (fieldType instanceof ListType) {
        ListType listType = (ListType)fieldType;
        if (listType.getId().equals("CatalogCharacteristics")) {
          CatalogCharactersticDef catalogCharactersticDef = new CatalogCharactersticDef();
          for (FieldType fieldType1 : listType.getFieldOrList()) {
            if (fieldType1 instanceof FieldType) {
              FieldType fieldType2 = fieldType1;
              String str1 = fieldType2.getId();
              String str2 = fieldType2.getValue();
              if (str1.equals("TabSheet")) {
                catalogCharactersticDef.tabSheet = str2;
                continue;
              } 
              if (str1.equals("OrderNo")) {
                catalogCharactersticDef.orderNo = Integer.valueOf(Integer.parseInt(str2));
                continue;
              } 
              if (str1.equals("Characteristic")) {
                catalogCharactersticDef.charDef = this.dataModelDefinition.charIdMap.get(str2);
                this.charLabelMap.put(catalogCharactersticDef.charDef.informationText, catalogCharactersticDef);
                this.charDMNMap.put(catalogCharactersticDef.charDef.domainModelName, catalogCharactersticDef);
              } 
            } 
          } 
        } else if (listType.getId().equals("Text")) {
          for (FieldType fieldType1 : listType.getFieldOrList()) {
            if (fieldType1 instanceof FieldType) {
              FieldType fieldType2 = fieldType1;
              String str1 = fieldType2.getId();
              String str2 = fieldType2.getValue();
              if (str1.equals("Abbreviation")) {
                this.abbreviation = str2;
                continue;
              } 
              if (str1.equals("CatalogTitle")) {
                this.catalogTitle = str2;
                continue;
              } 
              if (str1.equals("Description"))
                this.description = str2; 
            } 
          } 
        } 
      } 
      if (!StringUtils.isEmpty(this.objectid))
        this.dataModelDefinition.catalogIdMap.put(this.objectid, this); 
    } 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\datamodel\definition\CatalogDef.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */