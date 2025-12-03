package com.mentor.dms.contentprovider.sf.datamodel;

import com.mentor.dms.contentprovider.core.config.datamodel.DataModelCfgDataModelConfiguration;
import java.util.HashMap;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;

public abstract class AbstractSEDataModelApp {
  protected static String dataModelCfgFile = null;
  
  protected static String abbrevFile = null;
  
  protected static String seuser = null;
  
  protected static String sepass = null;
  
  protected static String cacheDir = null;
  
  protected static boolean bUseCache = false;
  
  protected DataModelCfgDataModelConfiguration dataModelCfg = null;
  
  protected HashMap<String, String> abbrevMap = new HashMap<>();
  
  protected String getCacheDir(String paramString) {
    String str = paramString;
    if (str == null)
      str = System.getProperty("user.home") + System.getProperty("user.home") + ".dmsccp"; 
    return str;
  }
  
  protected void checkErrors(Document paramDocument) throws Exception {
    XPath xPath = XPathFactory.newInstance().newXPath();
    try {
      String str = xPath.evaluate("/ServiceResult/Status/Success/text()", paramDocument);
      if (!str.equals("true")) {
        String str1 = xPath.evaluate("/ServiceResult/Status/Code/text()", paramDocument);
        if (!str1.equals("3")) {
          String str2 = xPath.evaluate("/ServiceResult/Status/Message/text()", paramDocument);
          throw new Exception(str2);
        } 
      } 
    } catch (XPathExpressionException xPathExpressionException) {
      throw new Exception(xPathExpressionException.getMessage());
    } 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\datamodel\AbstractSEDataModelApp.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */