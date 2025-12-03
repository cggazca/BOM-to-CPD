package com.mentor.dms.contentprovider.sf.datamodel.upgrade;

import com.mentor.dms.contentprovider.core.config.ContentProviderConfigException;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.xml.sax.SAXException;

public class DataModelUpgradeXML {
  public static DataModelUpgrade read(String paramString) throws ContentProviderConfigException {
    DataModelUpgrade dataModelUpgrade;
    try {
      FileInputStream fileInputStream = new FileInputStream(new File(paramString));
      try {
        dataModelUpgrade = read(fileInputStream);
      } finally {
        fileInputStream.close();
      } 
    } catch (IOException|ContentProviderConfigException iOException) {
      throw new ContentProviderConfigException("Unable to read Data Model Update XML from file '" + paramString + "': " + iOException.getMessage());
    } 
    return dataModelUpgrade;
  }
  
  public static DataModelUpgrade read(InputStream paramInputStream) throws ContentProviderConfigException {
    DataModelUpgrade dataModelUpgrade;
    try {
      SchemaFactory schemaFactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
      StreamSource streamSource = new StreamSource(DataModelUpgrade.class.getResourceAsStream("schemas/DataModelUpgrade.xsd"));
      Schema schema = schemaFactory.newSchema(streamSource);
      JAXBContext jAXBContext = JAXBContext.newInstance(new Class[] { DataModelUpgrade.class });
      Unmarshaller unmarshaller = jAXBContext.createUnmarshaller();
      unmarshaller.setSchema(schema);
      XMLInputFactory xMLInputFactory = XMLInputFactory.newInstance();
      XMLStreamReader xMLStreamReader = xMLInputFactory.createXMLStreamReader(paramInputStream);
      JAXBElement jAXBElement = unmarshaller.unmarshal(xMLStreamReader, DataModelUpgrade.class);
      dataModelUpgrade = (DataModelUpgrade)jAXBElement.getValue();
    } catch (SAXException|JAXBException|javax.xml.stream.XMLStreamException sAXException) {
      if (sAXException.getCause() != null)
        throw new ContentProviderConfigException("Unable to read Data Model Configuration XML : " + String.valueOf(sAXException.getCause())); 
      throw new ContentProviderConfigException("Unable to read Data Model Configuration XML : " + sAXException.getMessage());
    } 
    return dataModelUpgrade;
  }
  
  public static void write(DataModelUpgrade paramDataModelUpgrade, String paramString) throws ContentProviderConfigException {
    FileOutputStream fileOutputStream = null;
    try {
      fileOutputStream = new FileOutputStream(paramString);
      JAXBContext jAXBContext = JAXBContext.newInstance(new Class[] { DataModelUpgrade.class });
      Marshaller marshaller = jAXBContext.createMarshaller();
      marshaller.setProperty("jaxb.encoding", "UTF-8");
      JAXBElement jAXBElement = new JAXBElement(new QName("DataModelUpgrade"), DataModelUpgrade.class, paramDataModelUpgrade);
      marshaller.marshal(jAXBElement, fileOutputStream);
    } catch (JAXBException|java.io.FileNotFoundException jAXBException) {
      throw new ContentProviderConfigException("Unable to write Data Model Update XML to file '" + paramString + "': " + jAXBException.getMessage());
    } finally {
      try {
        fileOutputStream.close();
      } catch (IOException iOException) {}
    } 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\datamode\\upgrade\DataModelUpgradeXML.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */