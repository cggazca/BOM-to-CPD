package com.mentor.dms.contentprovider.sf.datamodel.csv2xml;

import com.mentor.datafusion.utils.logger.MGLogger;
import com.mentor.dms.contentprovider.core.AbstractContentProvider;
import com.mentor.dms.contentprovider.core.config.ContentProviderConfig;
import com.mentor.dms.contentprovider.core.utils.validate.CPPartClass;
import com.mentor.dms.contentprovider.core.utils.validate.CPProperty;
import com.mentor.dms.contentprovider.sf.datamodel.csv2xml.csvdata.CSVParseException;
import com.mentor.dms.contentprovider.sf.datamodel.csv2xml.csvdata.CatalogFromCsv;
import com.mentor.dms.contentprovider.sf.datamodel.csv2xml.csvdata.PropertyFromCsv;
import com.mentor.dms.contentprovider.sf.datamodel.csv2xml.xml.DataModelXML;
import com.mentor.dms.contentprovider.sf.datamodel.csv2xml.xml.DirectiveXML;
import com.mentor.dms.contentprovider.sf.datamodel.definition.DataModelDefinition;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

public class CSVParseMPN extends CSVParseBase {
  private MGLogger logger = MGLogger.getLogger(CSVParseMPN.class);
  
  public CSVParseMPN(String paramString, AbstractContentProvider paramAbstractContentProvider, ContentProviderConfig paramContentProviderConfig) {
    super(paramString, paramAbstractContentProvider, paramContentProviderConfig);
  }
  
  public int exportXML(String paramString, List<CPPartClass> paramList, DataModelDefinition paramDataModelDefinition) {
    this.partListCP = paramList;
    this.existingDMD = paramDataModelDefinition;
    try {
      this.logger.info("Reading csv... [" + this.csvPath + "]");
      Map<String, CatalogFromCsv> map = readCSV(this.csvPath);
      if (map.size() <= 0) {
        this.logger.error("ERROR: No valid command exists in the csv file.");
        this.errorNum++;
        return this.errorNum;
      } 
      this.logger.info("Export XML...");
      DataModelXML dataModelXML = new DataModelXML(this.cpp.getId(), DataModelXML.Target.MPN);
      DirectiveXML directiveXML = new DirectiveXML();
      HashSet<String> hashSet = new HashSet();
      for (CatalogFromCsv catalogFromCsv : map.values()) {
        this.logger.info(String.valueOf(catalogFromCsv.getCommand()) + " " + String.valueOf(catalogFromCsv.getCommand()) + ":" + catalogFromCsv.getDomainName());
        if (catalogFromCsv.getProperties() != null)
          for (PropertyFromCsv propertyFromCsv : catalogFromCsv.getProperties()) {
            if (propertyFromCsv.getCommand() != PropertyFromCsv.Command.DEL && hashSet.contains(propertyFromCsv.getDomainName()))
              continue; 
            try {
              boolean bool = dataModelXML.addCharacteristicElement(propertyFromCsv, catalogFromCsv);
              directiveXML.addCharacteristicElement(propertyFromCsv, catalogFromCsv);
              if (bool) {
                hashSet.add(propertyFromCsv.getDomainName());
                this.logger.info("  " + String.valueOf(propertyFromCsv.getCommand()) + ": " + propertyFromCsv.getDomainName() + ":" + propertyFromCsv.getName());
              } 
            } catch (CSVParseException cSVParseException) {
              this.logger.warn("  WARN: " + cSVParseException.getMessage());
              this.warnNum++;
            } 
          }  
        dataModelXML.addCatalogElement(catalogFromCsv);
        directiveXML.addCatalogElement(catalogFromCsv);
      } 
      dataModelXML.writeXML(new File(paramString + "/DataModelMPNUpgrade.xml"));
      directiveXML.writeXML(new File(paramString + "/DataModelUpgrade.xml"));
    } catch (Throwable throwable) {
      this.logger.error("ERROR: " + throwable.getMessage(), throwable);
      this.errorNum++;
    } 
    return this.errorNum;
  }
  
  private Map<String, CatalogFromCsv> readCSV(String paramString) {
    HashMap<Object, Object> hashMap = new HashMap<>();
    try {
      BufferedReader bufferedReader = new BufferedReader(new FileReader(paramString));
      try {
        String str1 = "";
        CatalogFromCsv catalogFromCsv = null;
        bufferedReader.readLine();
        byte b = 1;
        String str2;
        while ((str2 = bufferedReader.readLine()) != null) {
          b++;
          try {
            ArrayList<String> arrayList = CSVUtil.csv2List(str2);
            if (!((String)arrayList.get(1)).equals(str1)) {
              str1 = arrayList.get(1);
              catalogFromCsv = new CatalogFromCsv(arrayList.get(1), arrayList.get(2));
            } 
            if (arrayList.size() > 10 && StringUtils.isNoneEmpty(new CharSequence[] { arrayList.get(10) })) {
              if (((String)arrayList.get(8)).matches("^(1|3|5):.*")) {
                HashMap<String, CPProperty> hashMap1 = getPropertiesCP(arrayList.get(1));
                if (hashMap.containsKey(str1)) {
                  this.logger.debug("Part class [" + (String)arrayList.get(2) + "] already exists in XML file. line:" + b);
                  continue;
                } 
                catalogFromCsv.setCommand(arrayList.get(10));
                catalogFromCsv.setParentKey(getParentCatalogID(catalogFromCsv.getDomainName()));
                catalogFromCsv.setName(arrayList.get(2));
                hashMap.put(str1, catalogFromCsv);
                this.logger.info("part class '" + (String)arrayList.get(2) + "'  line:" + b);
                if (catalogFromCsv.getCommand() == CatalogFromCsv.Command.ADD || catalogFromCsv.getCommand() == CatalogFromCsv.Command.UPD)
                  catalogFromCsv.setRename(isCatalogNameChanged(catalogFromCsv)); 
                continue;
              } 
              if (((String)arrayList.get(8)).matches("^16:.*")) {
                String str = ((String)arrayList.get(10)).toUpperCase();
                if (!str.equals("D")) {
                  this.logger.warn("Only delete command can be set on lines with Check Contents of '16'. line:" + b);
                  continue;
                } 
                str1 = getMappedMPN(arrayList.get(1));
                if (str1.equals("")) {
                  this.logger.warn("Part class not mapped. '" + (String)arrayList.get(2) + "'  line:" + b);
                  continue;
                } 
                if (hashMap.containsKey(str1)) {
                  catalogFromCsv = (CatalogFromCsv)hashMap.get(str1);
                } else {
                  catalogFromCsv = new CatalogFromCsv(str1, arrayList.get(2));
                } 
                catalogFromCsv.setCommand(arrayList.get(10));
                catalogFromCsv.setParentKey(getParentCatalogID(catalogFromCsv.getDomainName()));
                catalogFromCsv.setName(arrayList.get(2));
                hashMap.put(str1, catalogFromCsv);
                this.logger.info("part class '" + (String)arrayList.get(2) + "'  line:" + b);
                continue;
              } 
              if (((String)arrayList.get(8)).matches("^(7|9|12):.*")) {
                HashMap<String, CPProperty> hashMap1 = getPropertiesCP(arrayList.get(1));
                CPProperty cPProperty = hashMap1.get(arrayList.get(3));
                PropertyFromCsv propertyFromCsv = new PropertyFromCsv(arrayList.get(3), arrayList.get(4));
                String str = "";
                if (11 < arrayList.size())
                  str = arrayList.get(11); 
                if (str == "")
                  str = getTabSheetNameEDM(propertyFromCsv.getDomainName()); 
                propertyFromCsv.setCommand(arrayList.get(10));
                propertyFromCsv.setTabName(str);
                propertyFromCsv.setType(cPProperty.getType());
                propertyFromCsv.setUnit(cPProperty.getUnit());
                propertyFromCsv.setRename(isPropertyNameChanged(propertyFromCsv));
                catalogFromCsv.addProperties(propertyFromCsv);
                if (!hashMap.containsKey(str1)) {
                  catalogFromCsv.setCommand("");
                  hashMap.put(str1, catalogFromCsv);
                  this.logger.info("part class. [" + (String)arrayList.get(2) + "] line:" + b);
                } 
                this.logger.info("property. [" + (String)arrayList.get(4) + "] to [" + (String)arrayList.get(2) + "] line:" + b);
                continue;
              } 
              if (((String)arrayList.get(8)).matches("^17:.*")) {
                String str = ((String)arrayList.get(10)).toUpperCase();
                if (!str.equals("D") && !str.equals("DA")) {
                  this.logger.warn("Only delete command can be set on lines with Check Contents of '17'. line:" + b);
                  continue;
                } 
                PropertyFromCsv propertyFromCsv = new PropertyFromCsv(arrayList.get(3), arrayList.get(4));
                propertyFromCsv.setCommand(arrayList.get(10));
                str1 = getMappedMPN(arrayList.get(1));
                if (str1.equals("")) {
                  this.logger.warn("Part class not mapped. '" + (String)arrayList.get(2) + "'  line:" + b);
                  continue;
                } 
                if (hashMap.containsKey(str1)) {
                  catalogFromCsv = (CatalogFromCsv)hashMap.get(str1);
                } else {
                  catalogFromCsv = new CatalogFromCsv(str1, arrayList.get(2));
                } 
                catalogFromCsv.addProperties(propertyFromCsv);
                if (!hashMap.containsKey(str1)) {
                  catalogFromCsv.setCommand("");
                  hashMap.put(str1, catalogFromCsv);
                  this.logger.info("part class. [" + (String)arrayList.get(2) + "] line:" + b);
                } 
                this.logger.info("part class '" + (String)arrayList.get(2) + "'  line:" + b);
              } 
            } 
          } catch (Exception exception) {
            this.logger.error("ERROR: " + exception.getMessage() + " line:" + b);
            this.errorNum++;
          } 
        } 
        bufferedReader.close();
        bufferedReader.close();
      } catch (Throwable throwable) {
        try {
          bufferedReader.close();
        } catch (Throwable throwable1) {
          throwable.addSuppressed(throwable1);
        } 
        throw throwable;
      } 
    } catch (IOException iOException) {
      this.logger.error("ERROR: " + iOException.getMessage(), iOException);
    } 
    return (Map)hashMap;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\datamodel\csv2xml\CSVParseMPN.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */