package com.mentor.dms.contentprovider.utils.setrepmpn.config;

import com.mentor.dms.contentprovider.utils.setrepmpn.SetRepMPNException;
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

public class SetRepMPNConfiguration {
  public static SetRepMPNCfgSetRepMPNCfg read(String paramString) throws SetRepMPNException {
    SetRepMPNCfgSetRepMPNCfg setRepMPNCfgSetRepMPNCfg;
    try {
      FileInputStream fileInputStream = new FileInputStream(new File(paramString));
      try {
        setRepMPNCfgSetRepMPNCfg = read(fileInputStream);
      } finally {
        fileInputStream.close();
      } 
    } catch (IOException|SetRepMPNException iOException) {
      throw new SetRepMPNException("Unable to read SetRepMPN Configuration XML from file '" + paramString + "': " + iOException.getMessage());
    } 
    return setRepMPNCfgSetRepMPNCfg;
  }
  
  public static SetRepMPNCfgSetRepMPNCfg read(InputStream paramInputStream) throws SetRepMPNException {
    SetRepMPNCfgSetRepMPNCfg setRepMPNCfgSetRepMPNCfg;
    try {
      SchemaFactory schemaFactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
      StreamSource streamSource = new StreamSource(SetRepMPNConfiguration.class.getResourceAsStream("schemas/SetRepMPNCfg.xsd"));
      Schema schema = schemaFactory.newSchema(streamSource);
      JAXBContext jAXBContext = JAXBContext.newInstance(new Class[] { SetRepMPNCfgSetRepMPNCfg.class });
      Unmarshaller unmarshaller = jAXBContext.createUnmarshaller();
      unmarshaller.setSchema(schema);
      XMLInputFactory xMLInputFactory = XMLInputFactory.newInstance();
      XMLStreamReader xMLStreamReader = xMLInputFactory.createXMLStreamReader(paramInputStream);
      JAXBElement jAXBElement = unmarshaller.unmarshal(xMLStreamReader, SetRepMPNCfgSetRepMPNCfg.class);
      setRepMPNCfgSetRepMPNCfg = (SetRepMPNCfgSetRepMPNCfg)jAXBElement.getValue();
    } catch (SAXException|JAXBException|javax.xml.stream.XMLStreamException sAXException) {
      if (sAXException.getCause() != null)
        throw new SetRepMPNException("Unable to read SetRepMPN Configuration XML : " + String.valueOf(sAXException.getCause())); 
      throw new SetRepMPNException("Unable to read SetRepMPN Configuration XML : " + sAXException.getMessage());
    } 
    return setRepMPNCfgSetRepMPNCfg;
  }
  
  public static void write(SetRepMPNCfgSetRepMPNCfg paramSetRepMPNCfgSetRepMPNCfg, String paramString) throws SetRepMPNException {
    FileOutputStream fileOutputStream = null;
    try {
      fileOutputStream = new FileOutputStream(paramString);
      JAXBContext jAXBContext = JAXBContext.newInstance(new Class[] { SetRepMPNCfgSetRepMPNCfg.class });
      Marshaller marshaller = jAXBContext.createMarshaller();
      marshaller.setProperty("jaxb.encoding", "UTF-8");
      JAXBElement jAXBElement = new JAXBElement(new QName("SetRepMPNCfg"), SetRepMPNCfgSetRepMPNCfg.class, paramSetRepMPNCfgSetRepMPNCfg);
      marshaller.marshal(jAXBElement, fileOutputStream);
    } catch (JAXBException|java.io.FileNotFoundException jAXBException) {
      throw new SetRepMPNException("Unable to write SetRepMPN XML to file '" + paramString + "': " + jAXBException.getMessage());
    } finally {
      try {
        fileOutputStream.close();
      } catch (IOException iOException) {}
    } 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovide\\utils\setrepmpn\config\SetRepMPNConfiguration.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */