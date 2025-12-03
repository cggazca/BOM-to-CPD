package com.mentor.dms.contentprovider.sf.datamodel.csv2xml.xml;

import com.mentor.dms.contentprovider.sf.datamodel.DMUpgradeProperties;
import com.mentor.dms.contentprovider.sf.datamodel.DomainNameConverter;
import com.mentor.dms.contentprovider.sf.datamodel.UnitConverter;
import com.mentor.dms.contentprovider.sf.datamodel.csv2xml.csvdata.CSVParseException;
import com.mentor.dms.contentprovider.sf.datamodel.csv2xml.csvdata.CatalogFromCsv;
import com.mentor.dms.contentprovider.sf.datamodel.csv2xml.csvdata.PropertyFromCsv;
import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class DataModelXML {
  private Document docDMD = initDMDDocument();
  
  private Element elmParent;
  
  private String cpID;
  
  private Target target;
  
  private String objectIDNo;
  
  private String objectClass;
  
  private String tableNameKey;
  
  private static final String REGEXP_SYMBOL = "[ -/:-@\\[-`{-~]";
  
  public DataModelXML(String paramString, Target paramTarget) {
    this.cpID = paramString;
    this.target = paramTarget;
    if (paramTarget == Target.MPN) {
      this.objectIDNo = "060";
      this.objectClass = "60";
      this.tableNameKey = "Charact_TableNameMPN";
    } else if (paramTarget == Target.COMP) {
      this.objectIDNo = "001";
      this.objectClass = "1";
      this.tableNameKey = "Charact_TableNameComp";
    } 
  }
  
  public boolean addCharacteristicElement(PropertyFromCsv paramPropertyFromCsv, CatalogFromCsv paramCatalogFromCsv) throws CSVParseException {
    if (paramPropertyFromCsv.getCommand() != PropertyFromCsv.Command.ADD && paramPropertyFromCsv.getCommand() != PropertyFromCsv.Command.UPD)
      return false; 
    Element element1 = this.docDMD.createElement("object");
    element1.setAttribute("class", "Characteristic");
    String str = DomainNameConverter.convert(paramPropertyFromCsv.getDomainName());
    if (str == null)
      str = this.objectIDNo + this.objectIDNo + this.cpID; 
    element1.setAttribute("objectid", str);
    element1.appendChild(createFieldElement("CharactType", DMUpgradeProperties.getProperty("Charact_Type")));
    if (paramPropertyFromCsv.getType().equals("Text")) {
      element1.appendChild(createFieldElement("ValueType", "3"));
      element1.appendChild(createFieldElement("ValueLength", DMUpgradeProperties.getProperty("Charact_ValueLength_char")));
    } else if (paramPropertyFromCsv.getType().equals("Bool")) {
      element1.appendChild(createFieldElement("ValueType", "3"));
      element1.appendChild(createFieldElement("ValueLength", DMUpgradeProperties.getProperty("Charact_ValueLength_bool")));
    } else if (paramPropertyFromCsv.getType().equals("Numeric")) {
      element1.appendChild(createFieldElement("ValueType", "2"));
      element1.appendChild(createFieldElement("ValueLength", DMUpgradeProperties.getProperty("Charact_ValueLength_dbl")));
      element1.appendChild(createFieldElement("Precision", DMUpgradeProperties.getProperty("Charact_Precision_dbl")));
      if (paramPropertyFromCsv.getUnit() != null) {
        String str1 = UnitConverter.convert(paramPropertyFromCsv.getUnit());
        if (str1 != null) {
          element1.appendChild(createFieldElement("Unit", str1));
        } else {
          throw new CSVParseException("Unit[" + paramPropertyFromCsv.getUnit() + "] cannot be converted to SEDA Unit. [" + paramPropertyFromCsv.getName() + "]");
        } 
      } 
    } else if (paramPropertyFromCsv.getType().equals("Timestamp")) {
      element1.appendChild(createFieldElement("ValueType", "5"));
      element1.appendChild(createFieldElement("ValueLength", DMUpgradeProperties.getProperty("Charact_ValueLength_date")));
    } else if (paramPropertyFromCsv.getType().equals("Url")) {
      element1.appendChild(createFieldElement("ValueType", "3"));
      element1.appendChild(createFieldElement("ValueLength", DMUpgradeProperties.getProperty("Charact_ValueLength_char")));
    } else {
      paramPropertyFromCsv.clearCommand();
      throw new CSVParseException("Property type '" + paramPropertyFromCsv.getType() + "' is not supported. '" + paramPropertyFromCsv.getName() + "'");
    } 
    element1.appendChild(createFieldElement("InitFileName", DMUpgradeProperties.getProperty("Charact_InitFileName")));
    element1.appendChild(createFieldElement("InformationWidth", "20"));
    element1.appendChild(createFieldElement("TableName", DMUpgradeProperties.getPropertyReplace(this.tableNameKey, "${CPID}", this.cpID)));
    element1.appendChild(createFieldElement("TableColumn", this.cpID + "_" + this.cpID));
    element1.appendChild(createFieldElement("DomainModelName", paramPropertyFromCsv.getDomainName()));
    element1.appendChild(createFieldElement("DisposeOrder", DMUpgradeProperties.getProperty("Charact_DisposeOrder")));
    element1.appendChild(createFieldElement("SearchAlignment", "0"));
    element1.appendChild(createFieldElement("InformationAlignment", "0"));
    Element element2 = this.docDMD.createElement("list");
    element2.setAttribute("id", "Text");
    element2.setAttribute("clear", "true");
    element2.appendChild(createFieldElement("TabSheet", paramPropertyFromCsv.getTabName()));
    element2.appendChild(createFieldElement("SearchText", paramPropertyFromCsv.getName()));
    element2.appendChild(createFieldElement("InformationText", paramPropertyFromCsv.getName()));
    element1.appendChild(element2);
    this.elmParent.appendChild(element1);
    return true;
  }
  
  public void addCatalogElement(CatalogFromCsv paramCatalogFromCsv) throws CSVParseException {
    boolean bool = false;
    if (paramCatalogFromCsv.getCommand() == CatalogFromCsv.Command.ADD)
      bool = true; 
    Element element1 = this.docDMD.createElement("object");
    element1.setAttribute("class", "CatalogGroup");
    element1.setAttribute("objectid", "");
    element1.appendChild(createFieldElement("ParentKey", paramCatalogFromCsv.getParentKey()));
    element1.appendChild(createFieldElement("ObjectClass", this.objectClass));
    element1.appendChild(createFieldElement("DomainModelName", paramCatalogFromCsv.getDomainName()));
    element1.appendChild(createFieldElement("InitFileName", DMUpgradeProperties.getProperty("Catalog_InitFileName")));
    element1.appendChild(createFieldElement("CatalogStatus", DMUpgradeProperties.getProperty("Catalog_catalog_status")));
    Element element2 = this.docDMD.createElement("list");
    element2.setAttribute("id", "Text");
    element2.setAttribute("clear", "true");
    element2.appendChild(createFieldElement("Abbreviation", paramCatalogFromCsv.getName()));
    element2.appendChild(createFieldElement("CatalogTitle", paramCatalogFromCsv.getName()));
    element2.appendChild(createFieldElement("Description", paramCatalogFromCsv.getName()));
    element1.appendChild(element2);
    if (paramCatalogFromCsv.getProperties() != null)
      for (PropertyFromCsv propertyFromCsv : paramCatalogFromCsv.getProperties()) {
        if (propertyFromCsv.getCommand() != PropertyFromCsv.Command.ADD && propertyFromCsv.getCommand() != PropertyFromCsv.Command.UPD)
          continue; 
        bool = true;
        Element element = this.docDMD.createElement("list");
        element.setAttribute("id", "CatalogCharacteristics");
        element.setAttribute("clear", "true");
        element.appendChild(createFieldElement("TabSheet", propertyFromCsv.getTabName()));
        element.appendChild(createFieldElement("OrderNo", DMUpgradeProperties.getProperty("Catalog_Charact_OrderNo")));
        String str = DomainNameConverter.convert(propertyFromCsv.getDomainName());
        if (str == null)
          str = this.objectIDNo + this.objectIDNo + this.cpID; 
        element.appendChild(createFieldElement("Characteristic", str));
        element1.appendChild(element);
      }  
    if (bool)
      this.elmParent.appendChild(element1); 
  }
  
  private Element createFieldElement(String paramString1, String paramString2) {
    Element element = this.docDMD.createElement("field");
    element.setAttribute("id", paramString1);
    element.setTextContent(paramString2);
    return element;
  }
  
  private String removeSymbols(String paramString) {
    return paramString.replaceAll("[ -/:-@\\[-`{-~]", "");
  }
  
  private Document initDMDDocument() {
    DocumentBuilder documentBuilder = null;
    try {
      documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    } catch (ParserConfigurationException parserConfigurationException) {
      parserConfigurationException.printStackTrace();
    } 
    Document document = documentBuilder.newDocument();
    this.elmParent = document.createElement("data");
    document.appendChild(this.elmParent);
    return document;
  }
  
  public boolean writeXML(File paramFile) {
    Transformer transformer = null;
    try {
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      transformer = transformerFactory.newTransformer();
    } catch (TransformerConfigurationException transformerConfigurationException) {
      transformerConfigurationException.printStackTrace();
      return false;
    } 
    transformer.setOutputProperty("indent", "yes");
    transformer.setOutputProperty("encoding", "UTF-8");
    try {
      transformer.transform(new DOMSource(this.docDMD), new StreamResult(paramFile));
    } catch (TransformerException transformerException) {
      transformerException.printStackTrace();
      return false;
    } 
    return true;
  }
  
  public enum Target {
    MPN, COMP;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\datamodel\csv2xml\xml\DataModelXML.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */