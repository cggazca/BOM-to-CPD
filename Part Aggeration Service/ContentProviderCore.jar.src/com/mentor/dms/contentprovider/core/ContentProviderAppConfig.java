package com.mentor.dms.contentprovider.core;

import com.mentor.datafusion.oi.OIException;
import com.mentor.datafusion.oi.OIObjectManager;
import com.mentor.datafusion.oi.type.OIBlob;
import com.mentor.datafusion.oi.type.OIObject;
import com.mentor.datafusion.oi.type.OIObjectSet;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public class ContentProviderAppConfig {
  private static IContentProviderApplication ccpAppImpl = null;
  
  private HashMap<String, String> appConfigParams = new HashMap<>();
  
  private boolean bProxyEnabled = false;
  
  private String proxyHost;
  
  private String proxyPort;
  
  private boolean bSSLKeystoreEnabled = false;
  
  private String SSLKeystorePassword;
  
  public boolean isProxyEnabled() {
    return this.bProxyEnabled;
  }
  
  public void setProxyEnabled(boolean paramBoolean) {
    this.bProxyEnabled = paramBoolean;
  }
  
  public String getProxyHost() {
    return this.proxyHost;
  }
  
  public void setProxyHost(String paramString) {
    this.proxyHost = paramString;
  }
  
  public String getProxyPort() {
    return this.proxyPort;
  }
  
  public void setProxyPort(String paramString) {
    this.proxyPort = paramString;
  }
  
  public boolean isSSLKeystoreEnabled() {
    return this.bSSLKeystoreEnabled;
  }
  
  public void setSSLKeystoreEnabled(boolean paramBoolean) {
    this.bSSLKeystoreEnabled = paramBoolean;
  }
  
  public String getSSLKeystorePassword() {
    return this.SSLKeystorePassword;
  }
  
  public void setSSLKeystorePassword(String paramString) {
    this.SSLKeystorePassword = paramString;
  }
  
  public IContentProviderApplication getApplicationImpl() throws ContentProviderException {
    if (ccpAppImpl == null)
      throw new ContentProviderException("Unable to retrieve Content Provider Application inplementation."); 
    return ccpAppImpl;
  }
  
  public Map<String, String> getAppConfigParams() {
    return this.appConfigParams;
  }
  
  public String getAppConfigurationParameter(String paramString) {
    return this.appConfigParams.get(paramString);
  }
  
  public void load(OIObjectManager paramOIObjectManager) throws Exception {
    OIObject oIObject = null;
    try {
      oIObject = paramOIObjectManager.getObjectByID("ContentProviderAppConfig:", "ToolsContentProviderAppCfg", true);
    } catch (OIException oIException) {}
    Class<DefaultContentProviderApplicationImpl> clazz = DefaultContentProviderApplicationImpl.class;
    if (oIObject != null && oIObject.getString("Status").equals("A")) {
      OIObjectSet oIObjectSet = oIObject.getSet("MetaDataMap");
      for (OIObject oIObject1 : oIObjectSet) {
        String str1 = oIObject1.getString("Key");
        String str2 = oIObject1.getString("Value");
        if (str1.equals("APPLICATION_IMPLEMENTATION")) {
          clazz = (Class)Class.forName(str2);
          continue;
        } 
        this.appConfigParams.put(str1, str2);
      } 
      if (oIObject.getOIClass().hasField("CCPAppCfgAppImpl")) {
        String str = oIObject.getString("CCPAppCfgAppImpl").trim();
        if (!str.isEmpty())
          clazz = (Class)Class.forName(str); 
        this.bProxyEnabled = oIObject.get("CCPAppCfgEnableProxy").equals("Yes");
        this.proxyHost = oIObject.getString("CCPAppCfgProxyHost");
        this.proxyPort = oIObject.getString("CCPAppCfgProxyPort");
        this.bSSLKeystoreEnabled = oIObject.get("CCPAppCfgEnableSSLCert").equals("Yes");
        this.SSLKeystorePassword = oIObject.getString("CCPAppCfgSSLKeyPass");
        if (this.bSSLKeystoreEnabled) {
          File file = new File(System.getProperty("user.home"), "CCPCert.jks");
          OIBlob oIBlob = oIObject.getBlob("CCPAppCfgKeystoreBlob");
          InputStream inputStream = oIBlob.getInputStream();
          FileOutputStream fileOutputStream = new FileOutputStream(file);
          try {
            byte[] arrayOfByte = new byte[1024];
            int i;
            for (i = inputStream.read(arrayOfByte); i > -1; i = inputStream.read(arrayOfByte))
              fileOutputStream.write(arrayOfByte, 0, i); 
            fileOutputStream.flush();
          } finally {
            fileOutputStream.close();
          } 
        } 
      } else {
        for (OIObject oIObject1 : oIObjectSet) {
          String str1 = oIObject1.getString("Key");
          String str2 = oIObject1.getString("Value");
          if (str1.equals("APPLICATION_IMPLEMENTATION")) {
            clazz = (Class)Class.forName(str2);
            break;
          } 
        } 
      } 
    } 
    Constructor<DefaultContentProviderApplicationImpl> constructor = clazz.getDeclaredConstructor(new Class[0]);
    ccpAppImpl = constructor.newInstance(new Object[0]);
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\ContentProviderAppConfig.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */