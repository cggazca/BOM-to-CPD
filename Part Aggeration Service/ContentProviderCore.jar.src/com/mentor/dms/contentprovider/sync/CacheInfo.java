package com.mentor.dms.contentprovider.sync;

import com.mentor.datafusion.utils.logger.MGLogger;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;

public class CacheInfo {
  static MGLogger logger = MGLogger.getLogger(CacheInfo.class);
  
  private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
  
  private String directory = null;
  
  private String id;
  
  private Date fromDate = new Date();
  
  private Date toDate = new Date();
  
  public static CacheInfo readCacheInfo(String paramString) throws ContentProviderSyncException {
    CacheInfo cacheInfo = new CacheInfo(paramString);
    try {
      cacheInfo.readCache();
    } catch (IOException|ParseException iOException) {
      throw new ContentProviderSyncException(iOException);
    } 
    return cacheInfo;
  }
  
  public static CacheInfo createCacheInfo(Date paramDate1, Date paramDate2, String paramString) throws ContentProviderSyncException {
    CacheInfo cacheInfo = new CacheInfo(paramString);
    try {
      cacheInfo.setId("" + (new Date()).getTime());
      cacheInfo.setFromDate(paramDate1);
      cacheInfo.setToDate(paramDate2);
      cacheInfo.writeCache();
    } catch (IOException iOException) {
      throw new ContentProviderSyncException(iOException);
    } 
    return cacheInfo;
  }
  
  public static String getDefaultCacheDirectory() {
    return System.getProperty("user.home") + System.getProperty("user.home") + ".dmsccp";
  }
  
  public String getSyncDirectory() {
    return this.directory + this.directory + "synccache";
  }
  
  protected CacheInfo(String paramString) {
    this.directory = paramString;
  }
  
  public String getDirectory() {
    return this.directory;
  }
  
  protected void setDirectory(String paramString) {
    this.directory = paramString;
  }
  
  public String getId() {
    return this.id;
  }
  
  protected void setId(String paramString) {
    this.id = paramString;
  }
  
  public Date getFromDate() {
    return this.fromDate;
  }
  
  protected void setFromDate(Date paramDate) {
    this.fromDate = paramDate;
  }
  
  public Date getToDate() {
    return this.toDate;
  }
  
  protected void setToDate(Date paramDate) {
    this.toDate = paramDate;
  }
  
  public String getFromDateString() {
    return this.dateFormat.format(this.fromDate);
  }
  
  public String getToDateString() {
    return this.dateFormat.format(this.toDate);
  }
  
  protected void readCache() throws IOException, ParseException {
    File file = new File(getSyncDirectory(), "cache.properties");
    Properties properties = new Properties();
    properties.load(new FileInputStream(file));
    this.id = properties.getProperty("ID");
    String str1 = properties.getProperty("FROM_DATE");
    if (str1 != null)
      this.fromDate = this.dateFormat.parse(str1); 
    String str2 = properties.getProperty("TO_DATE");
    if (str2 != null)
      this.toDate = this.dateFormat.parse(str2); 
  }
  
  protected void writeCache() throws IOException, ContentProviderSyncException {
    File file1 = new File(getSyncDirectory());
    if (!file1.exists()) {
      logger.info("Creating synchronization cache directory at '" + String.valueOf(file1) + "'...");
      boolean bool = file1.mkdirs();
      if (!bool)
        throw new ContentProviderSyncException("Unable to create synchronization cache directory at '" + String.valueOf(file1) + "'."); 
    } else {
      logger.info("Using synchronization cache directory at '" + String.valueOf(file1) + "'...");
    } 
    File file2 = new File(getSyncDirectory(), "cache.properties");
    Properties properties = new Properties();
    properties.setProperty("ID", this.id);
    String str1 = this.dateFormat.format(this.fromDate);
    properties.setProperty("FROM_DATE", str1);
    String str2 = this.dateFormat.format(this.toDate);
    properties.setProperty("TO_DATE", str2);
    properties.store(new FileWriter(file2), (String)null);
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\sync\CacheInfo.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */