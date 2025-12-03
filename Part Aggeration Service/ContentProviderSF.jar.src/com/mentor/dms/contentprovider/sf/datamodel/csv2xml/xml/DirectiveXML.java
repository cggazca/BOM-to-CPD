package com.mentor.dms.contentprovider.sf.datamodel.csv2xml.xml;

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

public class DirectiveXML {
  private Document docDMU = initDMUDocument();
  
  private Element elmParent;
  
  public void addCharacteristicElement(PropertyFromCsv paramPropertyFromCsv, CatalogFromCsv paramCatalogFromCsv) {
    if (paramPropertyFromCsv.getCommand() == PropertyFromCsv.Command.ADD || paramPropertyFromCsv.getCommand() == PropertyFromCsv.Command.UPD) {
      if (paramPropertyFromCsv.isRename()) {
        Element element = this.docDMU.createElement("RenameProperty");
        element.setAttribute("domainModelName", paramPropertyFromCsv.getDomainName());
        element.setAttribute("toName", paramPropertyFromCsv.getName());
        this.elmParent.appendChild(element);
      } 
      return;
    } 
    if (paramPropertyFromCsv.getCommand() == PropertyFromCsv.Command.DEL) {
      Element element = this.docDMU.createElement("DeleteProperty");
      element.setAttribute("domainModelName", paramPropertyFromCsv.getDomainName());
      element.setAttribute("name", paramPropertyFromCsv.getName());
      element.setAttribute("catalogDMN", paramCatalogFromCsv.getDomainName());
      this.elmParent.appendChild(element);
    } else if (paramPropertyFromCsv.getCommand() == PropertyFromCsv.Command.DELALL) {
      Element element = this.docDMU.createElement("DeleteAllProperty");
      element.setAttribute("domainModelName", paramPropertyFromCsv.getDomainName());
      element.setAttribute("name", paramPropertyFromCsv.getName());
      this.elmParent.appendChild(element);
    } 
  }
  
  public void addCatalogElement(CatalogFromCsv paramCatalogFromCsv) {
    if (paramCatalogFromCsv.getCommand() == CatalogFromCsv.Command.ADD || paramCatalogFromCsv.getCommand() == CatalogFromCsv.Command.UPD) {
      if (paramCatalogFromCsv.isRename()) {
        Element element = this.docDMU.createElement("RenameCatalog");
        element.setAttribute("domainModelName", paramCatalogFromCsv.getDomainName());
        element.setAttribute("toName", paramCatalogFromCsv.getName());
        this.elmParent.appendChild(element);
      } else {
        Element element = this.docDMU.createElement("AddCatalog");
        element.setAttribute("domainModelName", paramCatalogFromCsv.getDomainName());
        this.elmParent.appendChild(element);
      } 
    } else if (paramCatalogFromCsv.getCommand() == CatalogFromCsv.Command.DEL) {
      Element element = this.docDMU.createElement("DeleteCatalog");
      element.setAttribute("domainModelName", paramCatalogFromCsv.getDomainName());
      element.setAttribute("name", paramCatalogFromCsv.getName());
      this.elmParent.appendChild(element);
    } 
  }
  
  private Element createFieldElement(String paramString1, String paramString2) {
    Element element = this.docDMU.createElement("field");
    element.setAttribute("id", paramString1);
    element.setTextContent(paramString2);
    return element;
  }
  
  private Document initDMUDocument() {
    DocumentBuilder documentBuilder = null;
    try {
      documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    } catch (ParserConfigurationException parserConfigurationException) {
      parserConfigurationException.printStackTrace();
    } 
    Document document = documentBuilder.newDocument();
    Element element = document.createElement("DataModelUpgrade");
    this.elmParent = document.createElement("UpgradeDirectives");
    element.appendChild(this.elmParent);
    document.appendChild(element);
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
      transformer.transform(new DOMSource(this.docDMU), new StreamResult(paramFile));
    } catch (TransformerException transformerException) {
      transformerException.printStackTrace();
      return false;
    } 
    return true;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\datamodel\csv2xml\xml\DirectiveXML.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */