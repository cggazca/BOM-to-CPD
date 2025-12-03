package com.mentor.dms.contentprovider.core;

import com.mentor.datafusion.oi.OIObjectManager;
import com.mentor.datafusion.oi.OIObjectManagerFactory;
import com.mentor.datafusion.oi.login.OIAuthenticate;
import com.mentor.datafusion.oi.login.OIAuthenticateFactory;
import com.mentor.dms.contentprovider.core.config.ContentProviderConfig;
import com.mentor.dms.contentprovider.core.config.ContentProviderConfigPartClass;
import com.mentor.dms.contentprovider.core.config.ContentProviderConfigProperty;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import javax.swing.UIManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class CCPMain {
  public static void main(String[] paramArrayOfString) {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception exception) {
      exception.printStackTrace();
    } 
    OIObjectManagerFactory oIObjectManagerFactory = null;
    OIObjectManager oIObjectManager = null;
    try {
      OIAuthenticate oIAuthenticate = OIAuthenticateFactory.createBatchAuthenticate("testedm");
      oIObjectManagerFactory = oIAuthenticate.login("Test App");
      System.out.println("Connected");
      oIObjectManager = oIObjectManagerFactory.createObjectManager();
      ContentProviderGlobal.setOIObjectManager(oIObjectManager);
      Object object1 = null;
      HashMap<Object, Object> hashMap = new HashMap<>();
      Object object2 = null;
      ContentProviderFactory.getInstance().registerContentProviders(oIObjectManager);
      AbstractContentProvider abstractContentProvider = ContentProviderFactory.getInstance().createContentProvider("SE");
      ContentProviderConfig contentProviderConfig = abstractContentProvider.getConfig(true);
      ContentProviderConfigPartClass contentProviderConfigPartClass = contentProviderConfig.getPartClassByContentProviderId("Part");
      for (ContentProviderConfigProperty contentProviderConfigProperty : contentProviderConfigPartClass.getClassProperties())
        System.out.println(contentProviderConfigProperty.getContentProviderId()); 
      abstractContentProvider = ContentProviderFactory.getInstance().createContentProvider("EDM");
      contentProviderConfig = abstractContentProvider.getConfig(true);
      contentProviderConfigPartClass = contentProviderConfig.getPartClassByContentProviderId("Part");
      for (ContentProviderConfigProperty contentProviderConfigProperty : contentProviderConfigPartClass.getClassProperties())
        System.out.println(contentProviderConfigProperty.getContentProviderId()); 
      return;
    } catch (Exception exception) {
      exception.printStackTrace();
    } finally {
      if (oIObjectManager != null)
        oIObjectManager.close(); 
      if (oIObjectManagerFactory != null)
        oIObjectManagerFactory.close(); 
    } 
  }
  
  public static String escapeHTML(String paramString) {
    StringBuilder stringBuilder = new StringBuilder(Math.max(16, paramString.length()));
    for (byte b = 0; b < paramString.length(); b++) {
      char c = paramString.charAt(b);
      if (c > '' || c == '"' || c == '<' || c == '>' || c == '&' || c == '\'') {
        stringBuilder.append("&#");
        stringBuilder.append(c);
        stringBuilder.append(';');
      } else {
        stringBuilder.append(c);
      } 
    } 
    return stringBuilder.toString();
  }
  
  private static void saveXMLMessage(InputStream paramInputStream, String paramString) throws IOException {
    FileOutputStream fileOutputStream = new FileOutputStream(new File(paramString));
    GZIPOutputStream gZIPOutputStream = new GZIPOutputStream(fileOutputStream);
    if (paramInputStream != null) {
      byte[] arrayOfByte = new byte[1024];
      int i;
      for (i = paramInputStream.read(arrayOfByte); i != -1; i = paramInputStream.read(arrayOfByte))
        gZIPOutputStream.write(arrayOfByte, 0, i); 
    } 
    gZIPOutputStream.close();
    fileOutputStream.close();
  }
  
  private static Document readXMLMessage(String paramString) throws ContentProviderException {
    Document document = null;
    try {
      DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      document = documentBuilder.parse(new GZIPInputStream(new FileInputStream(new File(paramString))));
    } catch (ParserConfigurationException parserConfigurationException) {
      throw new ContentProviderException(parserConfigurationException.getMessage());
    } catch (IOException iOException) {
      throw new ContentProviderException(iOException.getMessage());
    } catch (SAXException sAXException) {
      throw new ContentProviderException(sAXException.getMessage());
    } 
    return document;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\CCPMain.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */