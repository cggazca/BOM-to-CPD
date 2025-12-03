package com.mentor.dms.contentprovider.sf;

import com.mentor.datafusion.oi.OIObjectManager;
import com.mentor.datafusion.oi.OIObjectManagerFactory;
import com.mentor.datafusion.oi.login.OIAuthenticate;
import com.mentor.datafusion.oi.login.OIAuthenticateFactory;
import com.mentor.dms.contentprovider.core.AbstractContentProvider;
import com.mentor.dms.contentprovider.core.ContentProviderFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;

public class TestWebService {
  public static void main(String[] paramArrayOfString) {
    OIObjectManagerFactory oIObjectManagerFactory = null;
    OIObjectManager oIObjectManager = null;
    try {
      OIAuthenticate oIAuthenticate = OIAuthenticateFactory.createBatchAuthenticate("dms_desktop");
      oIObjectManagerFactory = oIAuthenticate.login("Test App");
      System.out.println("Connected");
      oIObjectManager = oIObjectManagerFactory.createObjectManager();
      ContentProviderFactory.getInstance().registerContentProviders(oIObjectManager);
      AbstractContentProvider abstractContentProvider = ContentProviderFactory.getInstance().createContentProvider("SE");
      HashMap<Object, Object> hashMap = new HashMap<>();
      hashMap.put("partNumber", "[{\"partNumber\":\"04023C121KAT4A\"}]");
      hashMap.put("mode", "exact");
      AggregationServiceWebCall aggregationServiceWebCall = new AggregationServiceWebCall((ContentProviderImpl)abstractContentProvider);
      Collection collection = abstractContentProvider.getSubscriptions();
      HashSet<String> hashSet = new HashSet();
      for (Map map : collection)
        hashSet.add((String)map.get("DataProviderID")); 
    } catch (Exception exception) {
      exception.printStackTrace();
    } finally {
      if (oIObjectManager != null)
        oIObjectManager.close(); 
      if (oIObjectManagerFactory != null)
        oIObjectManagerFactory.close(); 
    } 
  }
  
  public static void printDocument(Document paramDocument) throws IOException, TransformerException {
    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    Transformer transformer = transformerFactory.newTransformer();
    transformer.setOutputProperty("omit-xml-declaration", "no");
    transformer.setOutputProperty("method", "xml");
    transformer.setOutputProperty("indent", "yes");
    transformer.setOutputProperty("encoding", "UTF-8");
    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
    transformer.transform(new DOMSource(paramDocument), new StreamResult(System.out));
  }
  
  private static String convertStreamToString(InputStream paramInputStream) throws IOException {
    if (paramInputStream != null) {
      StringWriter stringWriter = new StringWriter();
      char[] arrayOfChar = new char[1024];
      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(paramInputStream, "UTF-8"));
      int i;
      while ((i = bufferedReader.read(arrayOfChar)) != -1) {
        stringWriter.write(arrayOfChar, 0, i);
        stringWriter.flush();
      } 
      return stringWriter.toString();
    } 
    return "";
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\TestWebService.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */