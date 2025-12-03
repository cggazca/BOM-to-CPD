package com.mentor.dms.contentprovider.sf.datamodel.definition;

import com.mentor.datafusion.oi.OICursor;
import com.mentor.datafusion.oi.OIObjectManager;
import com.mentor.datafusion.oi.OIQuery;
import com.mentor.datafusion.utils.logger.MGLogger;
import com.mentor.dms.contentprovider.core.config.ContentProviderConfigException;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.xml.sax.SAXException;

public class DataModelDefinition {
  static MGLogger logger = MGLogger.getLogger(DataModelDefinition.class);
  
  public HashMap<String, CharacteristicDef> charLabelMap = new HashMap<>(101);
  
  public HashMap<String, CharacteristicDef> charIdMap = new HashMap<>(101);
  
  public HashMap<String, CharacteristicDef> charDMNMap = new HashMap<>(101);
  
  public HashMap<String, CatalogDef> catalogPathMap = new HashMap<>(101);
  
  public HashMap<String, CatalogDef> catalogIdMap = new HashMap<>(101);
  
  public HashMap<String, CatalogDef> catalogDMNMap = new HashMap<>(101);
  
  public void read(String paramString) throws Exception {
    DmsElements dmsElements;
    FileInputStream fileInputStream = null;
    try {
      fileInputStream = new FileInputStream(new File(paramString));
      SchemaFactory schemaFactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
      StreamSource streamSource = new StreamSource(DmsElements.class.getResourceAsStream("schemas/XMLImport.xsd"));
      Schema schema = schemaFactory.newSchema(streamSource);
      JAXBContext jAXBContext = JAXBContext.newInstance(new Class[] { DmsElements.class });
      Unmarshaller unmarshaller = jAXBContext.createUnmarshaller();
      unmarshaller.setSchema(schema);
      XMLInputFactory xMLInputFactory = XMLInputFactory.newInstance();
      XMLStreamReader xMLStreamReader = xMLInputFactory.createXMLStreamReader(fileInputStream);
      JAXBElement jAXBElement = unmarshaller.unmarshal(xMLStreamReader, DmsElements.class);
      dmsElements = (DmsElements)jAXBElement.getValue();
    } catch (SAXException|jakarta.xml.bind.JAXBException|javax.xml.stream.XMLStreamException sAXException) {
      sAXException.printStackTrace();
      if (sAXException.getCause() != null)
        throw new ContentProviderConfigException("Unable to read Data Model Definition XML : " + String.valueOf(sAXException.getCause())); 
      throw new ContentProviderConfigException("Unable to read Data Model Definition XML : " + sAXException.getMessage());
    } finally {
      if (fileInputStream != null)
        fileInputStream.close(); 
    } 
    for (ObjectType objectType : dmsElements.getObjectOrDelete()) {
      if (objectType instanceof ObjectType) {
        ObjectType objectType1 = objectType;
        if (objectType1.getClazz().equals("Characteristic")) {
          CharacteristicDef characteristicDef = new CharacteristicDef(this);
          characteristicDef.read(objectType1);
          continue;
        } 
        if (objectType1.getClazz().equals("CatalogGroup")) {
          CatalogDef catalogDef = new CatalogDef(this);
          catalogDef.read(objectType1);
        } 
      } 
    } 
    buildCatalogPaths();
  }
  
  public void read(OIObjectManager paramOIObjectManager, String paramString1, String paramString2, String paramString3) throws Exception {
    OIQuery oIQuery = paramOIObjectManager.createQuery("Characteristic", true);
    oIQuery.addRestriction("Status", "A|U");
    oIQuery.addRestriction("ObjectClass", paramString1);
    oIQuery.addRestriction("RefClass", paramString2);
    oIQuery.addRestriction("TableColumn", paramString3 + "_*");
    oIQuery.addRestriction("Text.Language", "e");
    oIQuery.addColumn("Characteristic");
    oIQuery.addColumn("ObjectClass");
    oIQuery.addColumn("CharactType");
    oIQuery.addColumn("ValueType");
    oIQuery.addColumn("ValueLength");
    oIQuery.addColumn("InformationWidth");
    oIQuery.addColumn("TableColumn");
    oIQuery.addColumn("DomainModelName");
    oIQuery.addColumn("TableName");
    oIQuery.addColumn("DisposeOrder");
    oIQuery.addColumn("SearchAlignment");
    oIQuery.addColumn("InformationAlignment");
    oIQuery.addColumn("Status");
    oIQuery.addColumn("Patternmatch");
    oIQuery.addColumn("Text.TabSheet");
    oIQuery.addColumn("Text.InformationText");
    oIQuery.addColumn("Text.SearchText");
    OICursor oICursor = oIQuery.execute();
    while (oICursor.next()) {
      CharacteristicDef characteristicDef = new CharacteristicDef(this);
      characteristicDef.objectid = oICursor.getString("Characteristic");
      characteristicDef.charactType = oICursor.getInteger("CharactType").intValue();
      characteristicDef.valueType = oICursor.getInteger("ValueType").intValue();
      characteristicDef.valueLength = oICursor.getInteger("ValueLength").intValue();
      characteristicDef.informationWidth = oICursor.getInteger("InformationWidth").intValue();
      characteristicDef.tableName = oICursor.getString("TableName");
      characteristicDef.tableColumn = oICursor.getString("TableColumn");
      characteristicDef.domainModelName = oICursor.getString("DomainModelName");
      characteristicDef.disposeOrder = oICursor.getInteger("DisposeOrder").intValue();
      characteristicDef.searchAlignment = oICursor.getInteger("SearchAlignment").intValue();
      characteristicDef.informationAlignment = oICursor.getInteger("InformationAlignment").intValue();
      characteristicDef.status = oICursor.getString("Status");
      characteristicDef.unit = oICursor.getString("Patternmatch");
      characteristicDef.classNum = Integer.parseInt(oICursor.getStringified("ObjectClass"));
      characteristicDef.tabSheet = oICursor.getString("TabSheet");
      characteristicDef.searchText = oICursor.getString("SearchText");
      characteristicDef.informationText = oICursor.getString("InformationText");
      this.charLabelMap.put(characteristicDef.informationText, characteristicDef);
      this.charIdMap.put(characteristicDef.objectid, characteristicDef);
      this.charDMNMap.put(characteristicDef.domainModelName, characteristicDef);
    } 
    oICursor.close();
    oIQuery = paramOIObjectManager.createQuery("CatalogGroup", true);
    oIQuery.addRestriction("CatalogGroup", "????*");
    oIQuery.addRestriction("ObjectClass", paramString2);
    oIQuery.addRestriction("Text.Language", "e");
    oIQuery.addColumn("CatalogGroup");
    oIQuery.addColumn("ParentKey");
    oIQuery.addColumn("CatalogStatus");
    oIQuery.addColumn("DomainModelName");
    oIQuery.addColumn("Text.Abbreviation");
    oIQuery.addColumn("Text.CatalogTitle");
    oIQuery.addColumn("Text.Description");
    oIQuery.addColumn("ObjectClass");
    oICursor = oIQuery.execute();
    while (oICursor.next()) {
      CatalogDef catalogDef = new CatalogDef(this);
      catalogDef.objectid = oICursor.getString("CatalogGroup");
      catalogDef.objectClass = oICursor.getStringified("ObjectClass");
      catalogDef.parentKey = oICursor.getString("ParentKey");
      catalogDef.catalogStatus = oICursor.getStringified("CatalogStatus");
      catalogDef.domainModelName = oICursor.getString("DomainModelName");
      catalogDef.abbreviation = oICursor.getString("Abbreviation");
      catalogDef.catalogTitle = oICursor.getString("CatalogTitle");
      catalogDef.description = oICursor.getString("Description");
      this.catalogIdMap.put(catalogDef.objectid, catalogDef);
      this.catalogDMNMap.put(catalogDef.domainModelName, catalogDef);
    } 
    oICursor.close();
    oIQuery = paramOIObjectManager.createQuery("CatalogGroup", true);
    oIQuery.addRestriction("CatalogGroup", "????*");
    oIQuery.addRestriction("ObjectClass", paramString2);
    oIQuery.addRestriction("CatalogCharacteristics.Characteristic", "~NULL");
    oIQuery.addColumn("CatalogGroup");
    oIQuery.addColumn("CatalogCharacteristics.TabSheet");
    oIQuery.addColumn("CatalogCharacteristics.Characteristic");
    oIQuery.addColumn("CatalogCharacteristics.OrderNo");
    oICursor = oIQuery.execute();
    while (oICursor.next()) {
      if (!oICursor.getStringified("Characteristic").substring(3, 5).equals("SF"))
        continue; 
      CatalogDef catalogDef = this.catalogIdMap.get(oICursor.getString("CatalogGroup"));
      CatalogCharactersticDef catalogCharactersticDef = new CatalogCharactersticDef();
      catalogCharactersticDef.tabSheet = oICursor.getString("TabSheet");
      catalogCharactersticDef.orderNo = oICursor.getInteger("OrderNo");
      catalogCharactersticDef.charDef = this.charIdMap.get(oICursor.getStringified("Characteristic"));
      if (catalogCharactersticDef.charDef != null) {
        catalogDef.charLabelMap.put(catalogCharactersticDef.charDef.informationText, catalogCharactersticDef);
        catalogDef.charDMNMap.put(catalogCharactersticDef.charDef.domainModelName, catalogCharactersticDef);
        continue;
      } 
      logger.error("'" + catalogDef.objectid + "' references non-existent characteristic '" + oICursor.getStringified("Characteristic") + "'.");
    } 
    buildCatalogPaths();
  }
  
  public void removeCatalog(CatalogDef paramCatalogDef) {
    this.catalogIdMap.remove(paramCatalogDef.objectid);
    this.catalogPathMap.remove(paramCatalogDef.path);
    this.catalogDMNMap.remove(paramCatalogDef.domainModelName);
  }
  
  public void addCatalog(CatalogDef paramCatalogDef) {
    this.catalogIdMap.put(paramCatalogDef.objectid, paramCatalogDef);
    this.catalogPathMap.put(paramCatalogDef.path, paramCatalogDef);
    this.catalogDMNMap.put(paramCatalogDef.domainModelName, paramCatalogDef);
  }
  
  private void buildCatalogPaths() {
    for (CatalogDef catalogDef : this.catalogIdMap.values()) {
      for (int i = 0; i < catalogDef.objectid.length(); i += 2) {
        String str = catalogDef.objectid.substring(0, i + 2);
        CatalogDef catalogDef1 = this.catalogIdMap.get(str);
        if (catalogDef1 != null) {
          if (!catalogDef.path.isEmpty())
            catalogDef.path += "."; 
          catalogDef.path += catalogDef.path;
        } 
      } 
      this.catalogPathMap.put(catalogDef.path, catalogDef);
    } 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\datamodel\definition\DataModelDefinition.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */