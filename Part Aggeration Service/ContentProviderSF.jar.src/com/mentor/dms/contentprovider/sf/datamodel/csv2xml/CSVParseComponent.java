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
import org.apache.commons.lang3.StringUtils;

public class CSVParseComponent extends CSVParseBase {
  private MGLogger logger = MGLogger.getLogger(CSVParseComponent.class);
  
  public CSVParseComponent(String paramString, AbstractContentProvider paramAbstractContentProvider, ContentProviderConfig paramContentProviderConfig) {
    super(paramString, paramAbstractContentProvider, paramContentProviderConfig);
  }
  
  public int exportXML(String paramString, List<CPPartClass> paramList, DataModelDefinition paramDataModelDefinition) {
    this.partListCP = paramList;
    this.existingDMD = paramDataModelDefinition;
    try {
      this.logger.info("Reading csv... [" + this.csvPath + "]");
      List<CatalogFromCsv> list = readCSV(this.csvPath);
      if (list.size() <= 0) {
        this.logger.error("ERROR: No valid command exists in the csv file.");
        this.errorNum++;
        return this.errorNum;
      } 
      this.logger.info("Export XML...");
      DataModelXML dataModelXML = new DataModelXML(this.cpp.getId(), DataModelXML.Target.COMP);
      DirectiveXML directiveXML = new DirectiveXML();
      HashSet<String> hashSet = new HashSet();
      for (CatalogFromCsv catalogFromCsv : list) {
        this.logger.info(String.valueOf(catalogFromCsv.getCommand()) + " " + String.valueOf(catalogFromCsv.getCommand()) + ":" + catalogFromCsv.getDomainName());
        if (catalogFromCsv.getProperties() != null)
          for (PropertyFromCsv propertyFromCsv : catalogFromCsv.getProperties()) {
            if (hashSet.contains(propertyFromCsv.getDomainName()))
              continue; 
            try {
              dataModelXML.addCharacteristicElement(propertyFromCsv, catalogFromCsv);
              directiveXML.addCharacteristicElement(propertyFromCsv, catalogFromCsv);
              hashSet.add(propertyFromCsv.getDomainName());
              this.logger.info("  " + String.valueOf(propertyFromCsv.getCommand()) + ": " + propertyFromCsv.getDomainName() + ":" + propertyFromCsv.getName());
            } catch (CSVParseException cSVParseException) {
              this.logger.warn("  WARN: " + cSVParseException.getMessage());
              this.warnNum++;
            } 
          }  
        dataModelXML.addCatalogElement(catalogFromCsv);
        directiveXML.addCatalogElement(catalogFromCsv);
      } 
      dataModelXML.writeXML(new File(paramString + "/DataModelCompUpgrade.xml"));
      directiveXML.writeXML(new File(paramString + "/DataModelUpgrade.xml"));
    } catch (Throwable throwable) {
      this.logger.error("ERROR: " + throwable.getMessage(), throwable);
      this.errorNum++;
    } 
    return this.errorNum;
  }
  
  private List<CatalogFromCsv> readCSV(String paramString) {
    ArrayList<CatalogFromCsv> arrayList = new ArrayList();
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
            ArrayList<String> arrayList1 = CSVUtil.csv2List(str2);
            if (((String)arrayList1.get(8)).startsWith("4:") && !((String)arrayList1.get(7)).equals(str1) && !((String)arrayList1.get(7)).isEmpty()) {
              str1 = arrayList1.get(7);
              catalogFromCsv = new CatalogFromCsv(arrayList1.get(7), arrayList1.get(2));
            } 
            if (arrayList1.size() > 10 && StringUtils.isNoneEmpty(new CharSequence[] { arrayList1.get(10) }) && ((String)arrayList1.get(8)).matches("^(10|14):.*")) {
              HashMap<String, CPProperty> hashMap = getPropertiesCP(arrayList1.get(1));
              CPProperty cPProperty = hashMap.get(arrayList1.get(3));
              PropertyFromCsv propertyFromCsv = new PropertyFromCsv("001_" + (String)arrayList1.get(3), arrayList1.get(4));
              String str = "";
              if (11 < arrayList1.size())
                str = arrayList1.get(11); 
              if (str == "")
                str = getTabSheetNameEDM(propertyFromCsv.getDomainName()); 
              propertyFromCsv.setCommand(arrayList1.get(10));
              propertyFromCsv.setTabName(str);
              propertyFromCsv.setType(cPProperty.getType());
              propertyFromCsv.setUnit(cPProperty.getUnit());
              propertyFromCsv.setRename(isPropertyNameChanged(propertyFromCsv));
              catalogFromCsv.addProperties(propertyFromCsv);
              if (!arrayList.contains(catalogFromCsv)) {
                catalogFromCsv.setCommand("");
                arrayList.add(catalogFromCsv);
                this.logger.info("part class. [" + (String)arrayList1.get(2) + "] line:" + b);
              } 
              this.logger.info("property. [" + (String)arrayList1.get(4) + "] to [" + catalogFromCsv.getName() + "] line:" + b);
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
    return arrayList;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\datamodel\csv2xml\CSVParseComponent.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */