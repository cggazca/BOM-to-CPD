package com.mentor.dms.contentprovider.sf;

import com.mentor.datafusion.utils.logger.MGLogger;
import com.mentor.dms.contentprovider.core.ContentProviderException;
import com.mentor.dms.contentprovider.core.IContentProviderResultRecord;
import com.mentor.dms.contentprovider.core.IContentProviderUpdateSearchResults;
import com.mentor.dms.contentprovider.core.sync.CacheInfo;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class ContentProviderUpdateSearchResultsImpl implements IContentProviderUpdateSearchResults {
  private static MGLogger log = MGLogger.getLogger(ContentProviderUpdateSearchResultsImpl.class);
  
  private final SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
  
  private final int MAX_WS_CALL_TRIES = 3;
  
  private ContentProviderImpl ccp;
  
  private Collection<Map<String, String>> dmsECIdList;
  
  private ArrayList<String> partList = new ArrayList<>();
  
  private Iterator<String> partIterator;
  
  private CacheInfo cacheInfo = null;
  
  public ContentProviderUpdateSearchResultsImpl(ContentProviderImpl paramContentProviderImpl, CacheInfo paramCacheInfo, Collection<Map<String, String>> paramCollection) {
    this.ccp = paramContentProviderImpl;
    this.cacheInfo = paramCacheInfo;
    this.dmsECIdList = paramCollection;
  }
  
  public void executeIncrementalMode(boolean paramBoolean) throws ContentProviderException {
    clearSyncCache(paramBoolean);
    HashSet<String> hashSet = new HashSet();
    for (Map<String, String> map : this.dmsECIdList) {
      String str = (String)map.get("e1aa6f26");
      hashSet.add(str);
    } 
    log.info("Retrieving updated parts from '" + this.ccp.getName() + "'...");
    AggregationServiceWebCall aggregationServiceWebCall = new AggregationServiceWebCall(this.ccp);
    for (String str : hashSet) {
      try {
        getPartDetails(aggregationServiceWebCall, str);
      } catch (Exception exception) {
        throw new ContentProviderException("Error encountered retrieving part with DataProviderID '" + str + "' from " + this.ccp.getName() + ": " + exception.getMessage());
      } 
    } 
    this.partIterator = this.partList.iterator();
  }
  
  private void getPartDetails(AggregationServiceWebCall paramAggregationServiceWebCall, String paramString) throws ContentProviderException {
    try {
      byte b = 0;
      while (b++ < 3) {
        String str = this.cacheInfo.getSyncDirectory() + this.cacheInfo.getSyncDirectory() + "part" + File.separator + ".json.gz";
        IContentProviderResultRecord iContentProviderResultRecord = null;
        if (paramString == null) {
          log.error("dataProviderId is null on External Contents.");
          break;
        } 
        if (!(new File(str)).exists()) {
          log.info("Retrieving part with DataProviderID '" + paramString + "' from " + this.ccp.getName() + "...");
          try {
            iContentProviderResultRecord = paramAggregationServiceWebCall.getPartByID(paramString);
          } catch (ContentProviderException contentProviderException) {
            continue;
          } 
          if (getPartDateCondition(iContentProviderResultRecord, this.formatter.format(this.cacheInfo.getFromDate()))) {
            String str1 = iContentProviderResultRecord.getResponseRaw();
            saveJson(str1, str);
          } else {
            return;
          } 
        } else {
          log.info("Retrieving part with DataProviderID '" + paramString + "' from the synchronization cache...");
          try {
            String str1 = readJson(str);
            log.info(str1);
            iContentProviderResultRecord = paramAggregationServiceWebCall.JsonToResult(str1);
            if (!getPartDateCondition(iContentProviderResultRecord, this.formatter.format(this.cacheInfo.getFromDate())))
              break; 
          } catch (ContentProviderException contentProviderException) {
            File file = new File(str);
            file.delete();
            throw new ContentProviderException("Unable to read part details json: " + contentProviderException.getMessage());
          } 
        } 
        if (iContentProviderResultRecord != null) {
          this.partList.add(paramString);
          break;
        } 
        log.error("Failed to retrieving part with DataProviderID '" + paramString + "'");
      } 
    } catch (Exception exception) {
      throw new ContentProviderException("Error encountered retrieving part with DataProviderID '" + paramString + "' from " + this.ccp.getName() + " : " + exception.getMessage());
    } 
  }
  
  private boolean getPartDateCondition(IContentProviderResultRecord paramIContentProviderResultRecord, String paramString) {
    try {
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
      Date date1 = simpleDateFormat.parse(paramIContentProviderResultRecord.getPartProperty("c905b5e3").getValue().toString().split(" ")[0]);
      Date date2 = this.formatter.parse(paramString);
      if (date1.compareTo(date2) == -1)
        return false; 
    } catch (Exception exception) {
      log.error(exception, new Object[] { exception.getMessage() });
    } 
    return true;
  }
  
  public void executeFullMode(boolean paramBoolean) throws ContentProviderException {
    clearSyncCache(paramBoolean);
    AggregationServiceWebCall aggregationServiceWebCall = new AggregationServiceWebCall(this.ccp);
    for (Map<String, String> map : this.dmsECIdList) {
      String str = (String)map.get("e1aa6f26");
      try {
        byte b = 0;
        while (b++ < 3) {
          String str1 = this.cacheInfo.getSyncDirectory() + this.cacheInfo.getSyncDirectory() + "part" + File.separator + ".json.gz";
          IContentProviderResultRecord iContentProviderResultRecord = null;
          if (str == null) {
            log.error("dataProviderId is null on External Contents.");
            break;
          } 
          if (!(new File(str1)).exists()) {
            log.info("Retrieving part with DataProviderID '" + str + "' from " + this.ccp.getName() + "...");
            try {
              iContentProviderResultRecord = aggregationServiceWebCall.getPartByID(str);
            } catch (ContentProviderException contentProviderException) {
              continue;
            } 
            String str2 = iContentProviderResultRecord.getResponseRaw();
            saveJson(str2, str1);
            continue;
          } 
          log.info("Retrieving part with DataProviderID '" + str + "' from the synchronization cache...");
          try {
            String str2 = readJson(str1);
            log.info(str2);
            iContentProviderResultRecord = aggregationServiceWebCall.JsonToResult(str2);
          } catch (ContentProviderException contentProviderException) {
            File file = new File(str1);
            file.delete();
            throw new ContentProviderException("Unable to read part details json: " + contentProviderException.getMessage());
          } 
          if (iContentProviderResultRecord != null) {
            this.partList.add(str);
            continue;
          } 
          log.error("Failed to retrieving part with DataProviderID '" + str + "'");
        } 
      } catch (Exception exception) {
        throw new ContentProviderException("Error encountered retrieving part with DataProviderID '" + str + "' from " + this.ccp.getName() + " : " + exception.getMessage());
      } 
    } 
    this.partIterator = this.partList.iterator();
  }
  
  private void clearSyncCache(boolean paramBoolean) throws ContentProviderException {
    if (paramBoolean) {
      log.info("Restarting updates from the synchronization cache...");
      String str1 = this.formatter.format(this.cacheInfo.getFromDate());
      String str2 = this.formatter.format(this.cacheInfo.getToDate());
      log.debug("Using existing cache FROM date of '" + str1 + "' and TO date of '" + str2 + "'...");
    } else {
      log.info("Clearing the synchronization cache...");
      File file = new File(this.cacheInfo.getSyncDirectory());
      for (File file1 : file.listFiles()) {
        if (!file1.getName().equals("cache.properties") && !file1.delete())
          throw new ContentProviderException("Unable to remove file '" + file1.getName() + "' from the synchronization cache."); 
      } 
    } 
  }
  
  public boolean next() throws ContentProviderException {
    return this.partIterator.hasNext();
  }
  
  public int getCount() {
    return this.partList.size();
  }
  
  public IContentProviderResultRecord getPartRecord() throws ContentProviderException {
    ContentProviderResultRecordImpl contentProviderResultRecordImpl = new ContentProviderResultRecordImpl(this.ccp);
    String str1 = this.partIterator.next();
    String str2 = this.cacheInfo.getSyncDirectory() + this.cacheInfo.getSyncDirectory() + "part" + File.separator + ".json.gz";
    String str3 = readJson(str2);
    AggregationServiceWebCall aggregationServiceWebCall = new AggregationServiceWebCall(this.ccp);
    return aggregationServiceWebCall.JsonToResult(str3);
  }
  
  private void printDocument(Document paramDocument, String paramString) throws IOException, TransformerException {
    FileOutputStream fileOutputStream = new FileOutputStream(new File(paramString));
    GZIPOutputStream gZIPOutputStream = new GZIPOutputStream(fileOutputStream);
    StreamResult streamResult = new StreamResult(new OutputStreamWriter(gZIPOutputStream, "UTF-8"));
    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    Transformer transformer = transformerFactory.newTransformer();
    transformer.setOutputProperty("omit-xml-declaration", "no");
    transformer.setOutputProperty("method", "xml");
    transformer.setOutputProperty("indent", "yes");
    transformer.setOutputProperty("encoding", "UTF-8");
    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
    transformer.transform(new DOMSource(paramDocument), streamResult);
    gZIPOutputStream.close();
    fileOutputStream.close();
  }
  
  private void saveXMLMessage(InputStream paramInputStream, String paramString) throws IOException {
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
  
  private Document readXMLMessage(String paramString) throws ContentProviderException {
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
  
  private void saveJson(String paramString1, String paramString2) throws IOException {
    FileOutputStream fileOutputStream = new FileOutputStream(new File(paramString2));
    GZIPOutputStream gZIPOutputStream = new GZIPOutputStream(fileOutputStream);
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(paramString1.getBytes(StandardCharsets.UTF_8));
    if (byteArrayInputStream != null) {
      byte[] arrayOfByte = new byte[1024];
      int i;
      for (i = byteArrayInputStream.read(arrayOfByte); i != -1; i = byteArrayInputStream.read(arrayOfByte))
        gZIPOutputStream.write(arrayOfByte, 0, i); 
    } 
    gZIPOutputStream.close();
    fileOutputStream.close();
  }
  
  private String readJson(String paramString) throws ContentProviderException {
    try {
      return IOUtils.toString(new GZIPInputStream(new FileInputStream(new File(paramString))), StandardCharsets.UTF_8);
    } catch (IOException iOException) {
      throw new ContentProviderException(iOException.getMessage());
    } 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\ContentProviderUpdateSearchResultsImpl.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */