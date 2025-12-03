package com.mentor.dms.contentprovider.sf.datamodel.definition;

import com.mentor.datafusion.oi.OIObjectManager;
import com.mentor.datafusion.utils.logger.MGLogger;

public class CharacteristicDef {
  static MGLogger logger = MGLogger.getLogger(CharacteristicDef.class);
  
  protected DataModelDefinition dataModelDefinition = null;
  
  public String objectid;
  
  public int charactType = 0;
  
  public int valueType = 3;
  
  public int valueLength = 20;
  
  public String initFilename;
  
  public int informationWidth = 20;
  
  public String tableName;
  
  public String tableColumn;
  
  public String domainModelName;
  
  public String tabSheet;
  
  public String searchText;
  
  public String informationText;
  
  public String status;
  
  public String unit;
  
  public int disposeOrder = 75;
  
  public int searchAlignment = 3;
  
  public int informationAlignment = 3;
  
  public int precision = 0;
  
  public int classNum = 0;
  
  public CharacteristicDef(DataModelDefinition paramDataModelDefinition) {
    this.dataModelDefinition = paramDataModelDefinition;
  }
  
  public void read(ObjectType paramObjectType) throws Exception {
    this.objectid = paramObjectType.getObjectid();
    for (FieldType fieldType : paramObjectType.getListOrField()) {
      if (fieldType instanceof FieldType) {
        FieldType fieldType1 = fieldType;
        String str1 = fieldType1.getId();
        String str2 = fieldType1.getValue();
        if (str1.equals("CharactType")) {
          this.charactType = Integer.parseInt(str2);
        } else if (str1.equals("ValueType")) {
          this.valueType = Integer.parseInt(str2);
        } else if (str1.equals("ValueLength")) {
          this.valueLength = Integer.parseInt(str2);
        } else if (str1.equals("InitFileName")) {
          this.initFilename = str2;
        } else if (str1.equals("InformationWidth")) {
          this.informationWidth = Integer.parseInt(str2);
        } else if (str1.equals("TableName")) {
          this.tableName = str2;
        } else if (str1.equals("TableColumn")) {
          this.tableColumn = str2;
        } else if (str1.equals("DomainModelName")) {
          this.domainModelName = str2;
          this.dataModelDefinition.charDMNMap.put(this.domainModelName, this);
        } else if (str1.equals("DisposeOrder")) {
          this.disposeOrder = Integer.parseInt(str2);
        } else if (str1.equals("SearchAlignment")) {
          this.searchAlignment = Integer.parseInt(str2);
        } else if (str1.equals("InformationAlignment")) {
          this.informationAlignment = Integer.parseInt(str2);
        } else if (str1.equals("Precision")) {
          this.precision = Integer.parseInt(str2);
        } else if (str1.equals("Unit")) {
          this.unit = str2;
        } 
      } else if (fieldType instanceof ListType) {
        ListType listType = (ListType)fieldType;
        if (!listType.getId().equals("Text"))
          continue; 
        for (FieldType fieldType1 : listType.getFieldOrList()) {
          if (fieldType1 instanceof FieldType) {
            FieldType fieldType2 = fieldType1;
            String str1 = fieldType2.getId();
            String str2 = fieldType2.getValue();
            if (str1.equals("TabSheet")) {
              this.tabSheet = str2;
              continue;
            } 
            if (str1.equals("SearchText")) {
              this.searchText = str2;
              continue;
            } 
            if (str1.equals("InformationText"))
              this.informationText = str2; 
          } 
        } 
      } 
      this.dataModelDefinition.charLabelMap.put(this.informationText, this);
      this.dataModelDefinition.charIdMap.put(this.objectid, this);
    } 
  }
  
  public void read(OIObjectManager paramOIObjectManager) throws Exception {}
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\datamodel\definition\CharacteristicDef.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */