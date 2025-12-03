package com.mentor.dms.contentprovider.sf.datamodel;

import com.mentor.dms.contentprovider.core.ContentProviderException;
import com.mentor.dms.contentprovider.core.config.ContentProviderConfigException;
import com.mentor.dms.contentprovider.core.config.datamodel.DataModelCfgDoubleMapping;
import com.mentor.dms.contentprovider.core.config.datamodel.DataModelCfgMPNArgument;
import com.mentor.dms.contentprovider.core.config.datamodel.DataModelCfgPartClass;
import com.mentor.dms.contentprovider.core.config.datamodel.DataModelCfgPartClassUnit;
import com.mentor.dms.contentprovider.core.config.datamodel.DataModelCfgProperty;
import com.mentor.dms.contentprovider.core.config.datamodel.DataModelCfgPropertyLengthOverride;
import com.mentor.dms.contentprovider.core.config.datamodel.DataModelCfgRedirectPartClass;
import com.mentor.dms.contentprovider.core.config.datamodel.DataModelCfgSamplePart;
import com.mentor.dms.contentprovider.core.config.datamodel.DataModelCfgScriptedMapping;
import com.mentor.dms.contentprovider.core.config.datamodel.DataModelCfgStringArgument;
import com.mentor.dms.contentprovider.core.config.datamodel.DataModelCfgTopLevelScriptedMapping;
import com.mentor.dms.contentprovider.core.config.datamodel.DataModelCfgUnitsMapping;
import com.mentor.dms.contentprovider.core.config.datamodel.DataModelConfigurationXML;
import com.mentor.dms.contentprovider.sf.AggregationServiceWebCall;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class GenerateDataModel extends AbstractSEDataModelApp {
  private static boolean bPartClass = false;
  
  private static boolean bMapping = false;
  
  private static boolean bMPN = false;
  
  private static boolean bComponent = false;
  
  private static boolean bSpreadsheet = false;
  
  private static boolean bAutoMapping = false;
  
  private static boolean bUpgrade = true;
  
  private static String outputDir = "";
  
  private static Taxonomy topTxNode = null;
  
  private static ArrayList<Taxonomy> topTaxonomyList = new ArrayList<>();
  
  private static TreeMap<String, AbstractTaxonomyNode> taxonomyNamePathMap = new TreeMap<>();
  
  private static HashMap<String, String> plToSamplePartMap = new HashMap<>();
  
  private static HashSet<String> uniqueFeatureSet = new HashSet<>();
  
  private static XPath xpath = null;
  
  private static DocumentBuilder parser = null;
  
  private static String dmCacheDir;
  
  private static AggregationServiceWebCall seWSCall = new AggregationServiceWebCall();
  
  public static void main(String[] paramArrayOfString) {
    Options options = new Options();
    options.addOption("help", false, "help");
    options.addOption("datamodelconfigfile", true, "Data Model Configuration filename");
    options.addOption("abbrevfile", true, "Label abbreviation file (CSV)");
    options.addOption("seuser", true, "SiliconExpert web service username");
    options.addOption("sepass", true, "SiliconExpert web service password");
    options.addOption("cachedir", true, "Directory used to save cached file (defaults to home directory");
    options.addOption("usecache", false, "Reads datamodel from cache instead of calling SiliconExport web service");
    options.addOption("partclass", false, "Generate mapping PartClass only");
    options.addOption("mapping", false, "Generate mapping");
    options.addOption("mpn", false, "Generate Manufacturer Part EDM Library data model");
    options.addOption("component", false, "Generate Component EDM Library data model");
    options.addOption("spreadsheet", false, "Generate data model spreadsheet");
    options.addOption("automapping", false, "Use auto-mapping in generated map file");
    options.addOption("noinherit", false, "Use noinherit in generated map file");
    options.addOption("upgrade", false, "Produce upgrade output");
    options.addOption("outputdir", true, "Directory to place output files (defaults to current directory");
    BasicParser basicParser = new BasicParser();
    CommandLine commandLine = null;
    try {
      commandLine = basicParser.parse(options, paramArrayOfString);
    } catch (ParseException parseException) {
      System.err.println("Error: " + parseException.getMessage());
      HelpFormatter helpFormatter = new HelpFormatter();
      helpFormatter.printHelp("GenerateDataModel", options);
      System.exit(1);
    } 
    if (commandLine.hasOption("help")) {
      HelpFormatter helpFormatter = new HelpFormatter();
      helpFormatter.printHelp("GenerateDataModel", options);
      System.exit(0);
    } 
    if (commandLine.hasOption("datamodelconfigfile"))
      dataModelCfgFile = commandLine.getOptionValue("datamodelconfigfile"); 
    if (commandLine.hasOption("abbrevfile"))
      abbrevFile = commandLine.getOptionValue("abbrevfile"); 
    bUseCache = commandLine.hasOption("usecache");
    if (commandLine.hasOption("seuser")) {
      seuser = commandLine.getOptionValue("seuser");
    } else if (!bUseCache) {
      System.err.println("Error: SiliconExpert username is required when not using cache.");
      System.exit(1);
    } 
    if (commandLine.hasOption("sepass")) {
      sepass = commandLine.getOptionValue("sepass");
    } else if (!bUseCache) {
      System.err.println("Error: SiliconExpert password is required when not using cache.");
      System.exit(1);
    } 
    cacheDir = commandLine.getOptionValue("cachedir");
    bPartClass = commandLine.hasOption("partclass");
    bMapping = commandLine.hasOption("mapping");
    if (bPartClass && bMapping) {
      System.err.println("Error: Only one of -mapping or -partclass can be specified.");
      System.exit(1);
    } 
    bMPN = commandLine.hasOption("mpn");
    bComponent = commandLine.hasOption("component");
    bSpreadsheet = commandLine.hasOption("spreadsheet");
    bAutoMapping = commandLine.hasOption("automapping");
    bUpgrade = commandLine.hasOption("upgrade");
    if (bComponent && bMPN) {
      System.err.println("Error: Only one of -mpn or -component can be specified.");
      System.exit(1);
    } 
    if (!bMPN && !bComponent && !bPartClass && !bMapping && !bSpreadsheet) {
      System.err.println("Error:  [-mapping or -partclass] or [-mpn or -component] or [-spreadsheet] must be specified.");
      System.exit(1);
    } 
    if (commandLine.hasOption("component"))
      EDMClassMode.setComponentMode(); 
    if (commandLine.hasOption("outputdir"))
      outputDir = commandLine.getOptionValue("outputdir"); 
    GenerateDataModel generateDataModel = new GenerateDataModel();
    generateDataModel.doGenerate();
  }
  
  private void doGenerate() {
    try {
      if (dataModelCfgFile != null) {
        System.out.println("Reading data model configuration file...");
        try {
          this.dataModelCfg = DataModelConfigurationXML.read(dataModelCfgFile);
        } catch (ContentProviderConfigException contentProviderConfigException) {
          System.err.println(contentProviderConfigException.getMessage());
          return;
        } 
        for (DataModelCfgSamplePart dataModelCfgSamplePart : this.dataModelCfg.getSampleParts().getSamplePart())
          plToSamplePartMap.put(dataModelCfgSamplePart.getPath(), dataModelCfgSamplePart.getId()); 
        if (EDMClassMode.isComponentMode() || bMapping) {
          for (DataModelCfgDoubleMapping dataModelCfgDoubleMapping : this.dataModelCfg.getDoubleMappings().getDoubleMapping()) {
            DoubleMapping doubleMapping = new DoubleMapping(dataModelCfgDoubleMapping.getProperty());
            for (DataModelCfgPartClass dataModelCfgPartClass : dataModelCfgDoubleMapping.getPartClasses().getPartClass())
              DoubleMapping.add(dataModelCfgPartClass.getPath(), doubleMapping); 
          } 
          for (DataModelCfgScriptedMapping dataModelCfgScriptedMapping : this.dataModelCfg.getScriptedMappings().getScriptedMapping()) {
            ScriptedMapping scriptedMapping = new ScriptedMapping(dataModelCfgScriptedMapping.getProperty(), dataModelCfgScriptedMapping.getFunctionName());
            for (DataModelCfgMPNArgument dataModelCfgMPNArgument : dataModelCfgScriptedMapping.getArguments().getMPNArgument())
              scriptedMapping.addMPNArgument(dataModelCfgMPNArgument.getValue()); 
            for (DataModelCfgStringArgument dataModelCfgStringArgument : dataModelCfgScriptedMapping.getArguments().getStringArgument())
              scriptedMapping.addStringArgument(dataModelCfgStringArgument.getValue()); 
            for (DataModelCfgPartClass dataModelCfgPartClass : dataModelCfgScriptedMapping.getPartClasses().getPartClass())
              ScriptedMapping.add(dataModelCfgPartClass.getPath(), scriptedMapping); 
          } 
        } 
      } 
      if (abbrevFile != null);
      dmCacheDir = getCacheDir(cacheDir) + getCacheDir(cacheDir) + "datamodelcache";
      File file1 = new File(dmCacheDir);
      if (!file1.exists()) {
        System.out.println("Creating data model cache directory at '" + String.valueOf(file1) + "'...");
        boolean bool = file1.mkdirs();
        if (!bool)
          throw new ContentProviderException("Unable to create data model cache directory at '" + String.valueOf(file1) + "'."); 
      } else {
        System.out.println("Data model cache directory is located at '" + String.valueOf(file1) + "'...");
      } 
      if (bUseCache) {
        System.out.println("Using the data model cache...");
      } else {
        System.out.println("Clearing data model synchronization cache...");
        for (File file : file1.listFiles()) {
          if (!file.delete())
            throw new ContentProviderException("Unable to remove file '" + file.getName() + "' from the synchronization cache."); 
        } 
      } 
      Feature.setAbbrevMap(this.abbrevMap);
      System.out.println("Reading SiliconExpert data model...");
      topTxNode = new Taxonomy(EDMClassMode.getRootCatKey());
      topTxNode.setName("Part");
      HashMap<Object, Object> hashMap = new HashMap<>();
      String str1 = dmCacheDir + dmCacheDir + "taxonomy.xml";
      InputStream inputStream = null;
      parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      Document document = null;
      xpath = XPathFactory.newInstance().newXPath();
      File file2 = new File(str1);
      if (!bUseCache || !file2.exists()) {
        inputStream = seWSCall.callSE(seWSCall.getWebServiceBaseURL() + "search/parametric/getAllTaxonomy", hashMap, seuser, sepass);
        saveXMLMessage(inputStream, str1);
        inputStream.close();
      } 
      try {
        document = parser.parse(new FileInputStream(new File(str1)));
      } catch (Exception exception) {
        System.err.println("Missing cached file for taxonomy: " + str1);
        return;
      } 
      checkErrors(document);
      String str2 = "/ServiceResult/Result/TaxonomyList/Taxonomy";
      NodeList nodeList = (NodeList)xpath.evaluate(str2, document, XPathConstants.NODESET);
      byte b;
      for (b = 0; b < nodeList.getLength(); b++) {
        Element element = (Element)nodeList.item(b);
        String str = xpath.evaluate("PlType/text()", element);
        Taxonomy taxonomy = new Taxonomy();
        taxonomy.setTaxomomyID(str);
        taxonomy.setName(str);
        taxonomy.parent = topTxNode;
        topTaxonomyList.add(taxonomy);
        taxonomyNamePathMap.put(taxonomy.getNamePath(), taxonomy);
        str2 = "MainCategoryList/MainCategory";
        NodeList nodeList1 = (NodeList)xpath.evaluate(str2, element, XPathConstants.NODESET);
        for (byte b1 = 0; b1 < nodeList1.getLength(); b1++) {
          Element element1 = (Element)nodeList1.item(b1);
          MainCategory mainCategory = new MainCategory();
          mainCategory.setName(xpath.evaluate("CategoryName/text()", element1));
          mainCategory.setTaxomomyID(xpath.evaluate("CategoryID/text()", element1));
          mainCategory.bSearchableAsBase = xpath.evaluate("SearchableAsBase/text()", element1).equals("true");
          taxonomy.mainCategoryMap.put(mainCategory.getName(), mainCategory);
          mainCategory.taxonomy = taxonomy;
          taxonomyNamePathMap.put(mainCategory.getNamePath(), mainCategory);
          str1 = taxonomy.getName() + "_" + taxonomy.getName();
          str1 = dmCacheDir + dmCacheDir + File.separator + ".xml";
          file2 = new File(str1);
          if (!bUseCache || !file2.exists()) {
            hashMap.clear();
            hashMap.put("plName", mainCategory.getName());
            hashMap.put("level", "1");
            inputStream = seWSCall.callSE(seWSCall.getWebServiceBaseURL() + "search/parametric/getPlFeatures", hashMap, seuser, sepass);
            saveXMLMessage(inputStream, str1);
          } 
          loadFeatures(str1, (String)null, "Undefined", mainCategory);
          str2 = "SubCategoryList/SubCategory";
          NodeList nodeList2 = (NodeList)xpath.evaluate(str2, element1, XPathConstants.NODESET);
          for (byte b2 = 0; b2 < nodeList2.getLength(); b2++) {
            Element element2 = (Element)nodeList2.item(b2);
            SubCategory subCategory = new SubCategory();
            subCategory.setName(xpath.evaluate("CategoryName/text()", element2));
            subCategory.setTaxomomyID(xpath.evaluate("CategoryID/text()", element2));
            subCategory.bSearchableAsBase = xpath.evaluate("SearchableAsBase/text()", element2).equals("true");
            mainCategory.subCategoryMap.put(subCategory.getName(), subCategory);
            subCategory.mainCategory = mainCategory;
            taxonomyNamePathMap.put(subCategory.getNamePath(), subCategory);
            str1 = taxonomy.getName() + "_" + taxonomy.getName() + "_" + mainCategory.getName();
            str1 = dmCacheDir + dmCacheDir + File.separator + ".xml";
            file2 = new File(str1);
            if (!bUseCache || !file2.exists()) {
              hashMap.clear();
              hashMap.put("plName", mainCategory.getName() + "@" + mainCategory.getName());
              hashMap.put("level", "2");
              inputStream = seWSCall.callSE(seWSCall.getWebServiceBaseURL() + "search/parametric/getPlFeatures", hashMap, seuser, sepass);
              saveXMLMessage(inputStream, str1);
            } 
            loadFeatures(str1, (String)null, "Undefined", subCategory);
            str2 = "ProductLines/*";
            NodeList nodeList3 = (NodeList)xpath.evaluate(str2, element2, XPathConstants.NODESET);
            for (byte b3 = 0; b3 < nodeList3.getLength(); b3++) {
              Element element3 = (Element)nodeList3.item(b3);
              ProductLine productLine = new ProductLine();
              productLine.setName(xpath.evaluate("plName/text()", element3));
              productLine.setTaxomomyID(xpath.evaluate("plID/text()", element3));
              subCategory.productLineMap.put(productLine.getName(), productLine);
              productLine.subCategory = subCategory;
              taxonomyNamePathMap.put(productLine.getNamePath(), productLine);
              String str3 = taxonomy.getTaxonomyID() + "_" + taxonomy.getTaxonomyID() + "_" + mainCategory.getName() + "_" + subCategory.getName();
              str1 = dmCacheDir + dmCacheDir + File.separator + ".xml";
              file2 = new File(str1);
              if (!bUseCache || !file2.exists()) {
                hashMap.clear();
                hashMap.put("plName", productLine.getName());
                inputStream = seWSCall.callSE(seWSCall.getWebServiceBaseURL() + "search/parametric/getPlFeatures", hashMap, seuser, sepass);
                saveXMLMessage(inputStream, str1);
              } 
              String str4 = null;
              String str5 = "Undefined";
              if (!checkExcludePartClass("Part Class", productLine) && !isPartClassRedirected(productLine)) {
                str4 = dmCacheDir + dmCacheDir + File.separator + "_Part.xml";
                file2 = new File(str4);
                str5 = plToSamplePartMap.get(productLine.getNamePath());
                if (!bUseCache || !file2.exists())
                  if (str5 != null) {
                    hashMap.clear();
                    hashMap.put("comIds", str5);
                    inputStream = seWSCall.callSE(seWSCall.getWebServiceBaseURL() + "search/partDetail", hashMap, seuser, sepass);
                    saveXMLMessage(inputStream, str4);
                  } else {
                    System.err.println("No sample part found in data model configuration for taxonomy path '" + productLine.getNamePath() + "'.");
                  }  
              } 
              loadFeatures(str1, str4, str5, productLine);
            } 
          } 
        } 
      } 
      for (Taxonomy taxonomy : topTaxonomyList) {
        if (checkExcludePartClass("Part Classes", taxonomy, false))
          continue; 
        for (MainCategory mainCategory : taxonomy.mainCategoryMap.values()) {
          if (checkExcludePartClass("Part Classes", mainCategory, false))
            continue; 
          for (SubCategory subCategory : mainCategory.subCategoryMap.values()) {
            if (checkExcludePartClass("Part Classes", subCategory, false))
              continue; 
            for (ProductLine productLine : subCategory.productLineMap.values()) {
              if (checkExcludePartClass("Part Classes", productLine, false))
                continue; 
              for (FeatureInstance featureInstance : productLine.featureMap.values()) {
                if (featureInstance.bDynamicPackage || featureInstance.bStaticPackage)
                  for (AbstractTaxonomyNode abstractTaxonomyNode = productLine.getParent(); abstractTaxonomyNode != null; abstractTaxonomyNode = abstractTaxonomyNode.getParent()) {
                    FeatureInstance featureInstance1 = abstractTaxonomyNode.featureMap.get(featureInstance.getFeature().getName());
                    if (featureInstance1 != null) {
                      featureInstance1.bDynamicPackage = featureInstance.bDynamicPackage;
                      featureInstance1.bStaticPackage = featureInstance.bStaticPackage;
                    } 
                  }  
              } 
            } 
          } 
        } 
      } 
      b = 1;
      for (Taxonomy taxonomy : topTaxonomyList) {
        if (checkExcludePartClass("Part Classes", taxonomy, false))
          continue; 
        for (MainCategory mainCategory : taxonomy.mainCategoryMap.values()) {
          if (checkExcludePartClass("Part Classes", mainCategory, false))
            continue; 
          for (SubCategory subCategory : mainCategory.subCategoryMap.values()) {
            if (checkExcludePartClass("Part Classes", subCategory, false))
              continue; 
            for (FeatureInstance featureInstance : subCategory.featureMap.values()) {
              if (!bUpgrade && (featureInstance.bScriptedMapping || featureInstance.bDoubleMapping))
                for (ProductLine productLine : subCategory.productLineMap.values()) {
                  if (checkExcludePartClass("Part Classes", productLine, false))
                    continue; 
                  FeatureInstance featureInstance1 = productLine.featureMap.get(featureInstance.getFeature().getName());
                  if (featureInstance1 != null && !bUpgrade && !featureInstance1.bScriptedMapping && !featureInstance1.bDoubleMapping) {
                    System.err.println("Data type descrepancy:");
                    System.err.println("  Sub Category: " + subCategory.getNamePath());
                    System.err.println("  Product Line: " + productLine.getNamePath());
                    System.err.println("  Feature Name: " + featureInstance.getFeature().getName());
                    b = 0;
                  } 
                }  
            } 
          } 
        } 
      } 
      if (b == 0) {
        System.err.println("Error in data model configuration.  Invalid data model would be produced.");
        return;
      } 
      if (bPartClass || bMapping)
        writeMapping(); 
      if (bMPN || bComponent)
        writeDataModel(); 
      if (bSpreadsheet)
        writeSpreadsheet(); 
    } catch (Exception exception) {
      exception.printStackTrace();
    } 
  }
  
  private void loadFeatures(String paramString1, String paramString2, String paramString3, AbstractTaxonomyNode paramAbstractTaxonomyNode) throws Exception {
    System.out.println("Processing Part Class '" + paramAbstractTaxonomyNode.getNamePath() + "...");
    boolean bool = false;
    if (paramString2 != null) {
      bool = (new File(paramString2)).canRead();
      if (!bool)
        System.err.println("Unable to locate sample part file : " + paramString2 + "."); 
    } 
    Document document = null;
    try {
      document = parser.parse(new FileInputStream(new File(paramString1)));
    } catch (Exception exception) {
      throw new Exception("Unable to read category, subcategory, or product line file : " + paramString1 + ": " + exception.getMessage());
    } 
    HashMap<Object, Object> hashMap = new HashMap<>();
    boolean bool1 = true;
    try {
      seWSCall.checkErrors(document, true);
    } catch (Exception exception) {
      System.err.println("Part Class '" + paramAbstractTaxonomyNode.getNamePath() + "': SiliconExpert getPlFeatures returned '" + exception.getMessage() + "'.");
      bool1 = false;
    } 
    String str = null;
    NodeList nodeList = null;
    if (bool1) {
      str = "/ServiceResult/Result/PlFeatures/Features/Feature";
      nodeList = (NodeList)xpath.evaluate(str, document, XPathConstants.NODESET);
      for (byte b = 0; b < nodeList.getLength(); b++) {
        Element element = (Element)nodeList.item(b);
        String str1 = xpath.evaluate("FeatureName/text()", element);
        if (this.dataModelCfg != null) {
          boolean bool2 = false;
          for (DataModelCfgProperty dataModelCfgProperty : this.dataModelCfg.getExcludeProperties().getProperty()) {
            if (dataModelCfgProperty.getId().equals(str1)) {
              bool2 = true;
              break;
            } 
          } 
          if (bool2)
            continue; 
        } 
        String str2 = "";
        NodeList nodeList1 = (NodeList)xpath.evaluate("FeatureValues/FeatureValue", element, XPathConstants.NODESET);
        for (byte b1 = 0; b1 < nodeList1.getLength(); b1++) {
          Element element1 = (Element)nodeList1.item(b1);
          String str5 = element1.getTextContent();
          if (str5.length() > str2.length())
            str2 = str5; 
        } 
        String str3 = xpath.evaluate("FeatureType/text()", element);
        String str4 = xpath.evaluate("FeatureUnit/text()", element);
        if (bool) {
          hashMap.put(str1, new SearchableFeatureData(str3, str2));
        } else {
          Feature feature = Feature.addFeature(str1, str3, str4);
          FeatureInstance featureInstance = new FeatureInstance(feature);
          feature.addValue(str2);
          featureInstance.bSearchable = true;
          featureInstance.type = str3;
          featureInstance.units = str4;
          featureInstance.bScriptedMapping = (EDMClassMode.isComponentMode() && ScriptedMapping.isPropMapped(paramAbstractTaxonomyNode.getNamePath(), str1, false));
          featureInstance.bDoubleMapping = (EDMClassMode.isComponentMode() && DoubleMapping.isPropMapped(paramAbstractTaxonomyNode.getNamePath(), str1, false));
          paramAbstractTaxonomyNode.featureMap.put(str1, featureInstance);
          if (!bUpgrade && (featureInstance.bScriptedMapping || featureInstance.bDoubleMapping))
            for (AbstractTaxonomyNode abstractTaxonomyNode = paramAbstractTaxonomyNode.getParent(); abstractTaxonomyNode != null; abstractTaxonomyNode = abstractTaxonomyNode.getParent()) {
              FeatureInstance featureInstance1 = abstractTaxonomyNode.featureMap.get(str1);
              if (featureInstance1 != null)
                featureInstance1.bDoubleMapping = true; 
            }  
        } 
        continue;
      } 
    } 
    if (bool) {
      try {
        document = parser.parse(new FileInputStream(new File(paramString2)));
      } catch (Exception exception) {
        System.out.println("Unable to read sample part file : " + paramString2 + ": " + exception.getMessage());
      } 
      String str1 = (String)xpath.evaluate("/ServiceResult/Results/ResultDto/SummaryData/TaxonomyPath", document, XPathConstants.STRING);
      String[] arrayOfString = str1.split(" > ");
      String str2 = arrayOfString[arrayOfString.length - 1];
      if (!str2.equals(paramAbstractTaxonomyNode.getName()))
        System.err.println("Part Class is '" + paramAbstractTaxonomyNode.getNamePath() + "' but sample part '" + paramString3 + "' is '" + str1.replaceAll(" > ", ".") + "'."); 
      str = "/ServiceResult/Results/ResultDto/ParametricData/Features";
      nodeList = (NodeList)xpath.evaluate(str, document, XPathConstants.NODESET);
      if (nodeList.getLength() == 0)
        System.err.println("Part Class '" + paramAbstractTaxonomyNode.getNamePath() + "' sample part '" + paramString3 + "' does not contain features."); 
      byte b;
      for (b = 0; b < nodeList.getLength(); b++) {
        Element element = (Element)nodeList.item(b);
        String str3 = xpath.evaluate("FeatureName/text()", element);
        SearchableFeatureData searchableFeatureData = (SearchableFeatureData)hashMap.get(str3);
        boolean bool2 = (searchableFeatureData != null) ? true : false;
        String str4 = xpath.evaluate("FeatureUnit/text()", element);
        Feature feature = Feature.addFeature(str3, bool2 ? searchableFeatureData.type : "", str4);
        feature.patternMatch = getMappedUnits(str3, str4);
        String str5 = xpath.evaluate("fetID/text()", element);
        feature.featureID = str5;
        FeatureInstance featureInstance = new FeatureInstance(feature);
        featureInstance.bSearchable = bool2;
        if (bool2)
          feature.addValue(((SearchableFeatureData)hashMap.get(str3)).longestFeatureValue); 
        featureInstance.bSearchable = hashMap.containsKey(str3);
        featureInstance.type = bool2 ? searchableFeatureData.type : ((str4 != null) ? "N" : "");
        featureInstance.units = str4;
        featureInstance.bScriptedMapping = (EDMClassMode.isComponentMode() && ScriptedMapping.isPropMapped(paramAbstractTaxonomyNode.getNamePath(), str3, false));
        featureInstance.bDoubleMapping = (EDMClassMode.isComponentMode() && DoubleMapping.isPropMapped(paramAbstractTaxonomyNode.getNamePath(), str3, false));
        paramAbstractTaxonomyNode.featureMap.put(str3, featureInstance);
        if (!bUpgrade && (featureInstance.bScriptedMapping || featureInstance.bDoubleMapping))
          for (AbstractTaxonomyNode abstractTaxonomyNode = paramAbstractTaxonomyNode.getParent(); abstractTaxonomyNode != null; abstractTaxonomyNode = abstractTaxonomyNode.getParent()) {
            FeatureInstance featureInstance1 = abstractTaxonomyNode.featureMap.get(str3);
            if (featureInstance1 != null)
              featureInstance1.bDoubleMapping = true; 
          }  
      } 
      str = "/ServiceResult/Results/ResultDto/PackageData/Feature";
      nodeList = (NodeList)xpath.evaluate(str, document, XPathConstants.NODESET);
      if (nodeList.getLength() > 0) {
        for (b = 0; b < nodeList.getLength(); b++) {
          Element element = (Element)nodeList.item(b);
          String str3 = xpath.evaluate("FeatureName/text()", element);
          String str4 = xpath.evaluate("FeatureUnit/text()", element);
          SearchableFeatureData searchableFeatureData = (SearchableFeatureData)hashMap.get(str3);
          boolean bool2 = (searchableFeatureData != null) ? true : false;
          String str5 = bool2 ? searchableFeatureData.type : (str4.isEmpty() ? "" : "N");
          Feature feature = Feature.addFeature(str3, str5, str4);
          FeatureInstance featureInstance = new FeatureInstance(feature);
          featureInstance.bSearchable = bool2;
          if (bool2)
            feature.addValue(((SearchableFeatureData)hashMap.get(str3)).longestFeatureValue); 
          featureInstance.type = str5;
          featureInstance.units = str4;
          featureInstance.bDynamicPackage = true;
          paramAbstractTaxonomyNode.featureMap.put(str3, featureInstance);
        } 
      } else {
        str = "/ServiceResult/Results/ResultDto/PackageData/*";
        nodeList = (NodeList)xpath.evaluate(str, document, XPathConstants.NODESET);
        if (nodeList.getLength() == 0)
          System.err.println("Part Class '" + paramAbstractTaxonomyNode.getNamePath() + "' sample part '" + paramString3 + "' does not contain package data."); 
        if (nodeList.getLength() > 0)
          for (b = 0; b < nodeList.getLength(); b++) {
            Element element = (Element)nodeList.item(b);
            String str3 = element.getNodeName();
            Feature feature = Feature.addFeature(str3, "", "");
            FeatureInstance featureInstance = new FeatureInstance(feature);
            featureInstance.bStaticPackage = true;
            paramAbstractTaxonomyNode.featureMap.put(str3, featureInstance);
          }  
      } 
    } 
  }
  
  private void writeDataModel() throws Exception {
    String str = "SiliconExpertDataModel";
    if (bMPN) {
      str = str + "MPN";
    } else if (bComponent) {
      str = str + "Comp";
    } 
    if (bUpgrade)
      str = str + "Upgrade"; 
    str = str + ".xml";
    StreamResult streamResult = new StreamResult(new FileOutputStream(new File(outputDir, str)));
    SAXTransformerFactory sAXTransformerFactory = (SAXTransformerFactory)TransformerFactory.newInstance();
    TransformerHandler transformerHandler = sAXTransformerFactory.newTransformerHandler();
    Transformer transformer = transformerHandler.getTransformer();
    transformer.setOutputProperty("encoding", "UTF-8");
    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
    transformer.setOutputProperty("indent", "yes");
    transformerHandler.setResult(streamResult);
    transformerHandler.startDocument();
    AttributesImpl attributesImpl = new AttributesImpl();
    transformerHandler.startElement("", "", "data", attributesImpl);
    for (Taxonomy taxonomy : topTaxonomyList) {
      if (checkExcludePartClass("Datamodel Characteristics", taxonomy))
        continue; 
      writeCharacteristics(transformerHandler, taxonomy);
      for (MainCategory mainCategory : taxonomy.mainCategoryMap.values()) {
        if (checkExcludePartClass("Datamodel Characteristics", mainCategory))
          continue; 
        writeCharacteristics(transformerHandler, mainCategory);
        for (SubCategory subCategory : mainCategory.subCategoryMap.values()) {
          if (checkExcludePartClass("Datamodel Characteristics", subCategory))
            continue; 
          writeCharacteristics(transformerHandler, subCategory);
          for (ProductLine productLine : subCategory.productLineMap.values()) {
            if (checkExcludePartClass("Datamodel Characteristics", productLine))
              continue; 
            writeCharacteristics(transformerHandler, productLine);
          } 
        } 
      } 
    } 
    writeDynIdCharacteristics(transformerHandler);
    for (Taxonomy taxonomy : topTaxonomyList) {
      if (checkExcludePartClass("Datamodel Catalog", taxonomy))
        continue; 
      writeCatalog(transformerHandler, taxonomy);
      for (MainCategory mainCategory : taxonomy.mainCategoryMap.values()) {
        if (checkExcludePartClass("Datamodel Catalog", mainCategory))
          continue; 
        writeCatalog(transformerHandler, mainCategory);
        for (SubCategory subCategory : mainCategory.subCategoryMap.values()) {
          if (checkExcludePartClass("Datamodel Catalog", subCategory))
            continue; 
          writeCatalog(transformerHandler, subCategory);
          for (ProductLine productLine : subCategory.productLineMap.values()) {
            if (checkExcludePartClass("Datamodel Catalog", productLine))
              continue; 
            writeCatalog(transformerHandler, productLine);
          } 
        } 
      } 
    } 
    transformerHandler.endElement("", "", "data");
    transformerHandler.endDocument();
    streamResult.getOutputStream().flush();
    streamResult.getOutputStream().close();
  }
  
  private boolean checkExcludePartClass(String paramString, AbstractTaxonomyNode paramAbstractTaxonomyNode, boolean paramBoolean) {
    boolean bool = false;
    for (DataModelCfgPartClass dataModelCfgPartClass : this.dataModelCfg.getExcludePartClasses().getPartClass()) {
      if (paramAbstractTaxonomyNode.getNamePath().startsWith(dataModelCfgPartClass.getPath())) {
        if (paramBoolean)
          System.out.println("Excluding " + paramString + " for '" + paramAbstractTaxonomyNode.getNamePath() + " (" + paramAbstractTaxonomyNode.getIDPath() + ")'"); 
        bool = true;
        break;
      } 
    } 
    return bool;
  }
  
  private boolean checkExcludePartClass(String paramString, AbstractTaxonomyNode paramAbstractTaxonomyNode) {
    return checkExcludePartClass(paramString, paramAbstractTaxonomyNode, true);
  }
  
  private boolean isPartClassRedirected(AbstractTaxonomyNode paramAbstractTaxonomyNode) {
    for (DataModelCfgRedirectPartClass dataModelCfgRedirectPartClass : this.dataModelCfg.getRedirectPartClasses().getRedirectPartClass()) {
      if (paramAbstractTaxonomyNode.getNamePath().equals(dataModelCfgRedirectPartClass.getFromPath()))
        return true; 
    } 
    return false;
  }
  
  private String getRedirectPartClassId(AbstractTaxonomyNode paramAbstractTaxonomyNode) {
    for (DataModelCfgRedirectPartClass dataModelCfgRedirectPartClass : this.dataModelCfg.getRedirectPartClasses().getRedirectPartClass()) {
      if (paramAbstractTaxonomyNode.getNamePath().equals(dataModelCfgRedirectPartClass.getFromPath())) {
        AbstractTaxonomyNode abstractTaxonomyNode = taxonomyNamePathMap.get(dataModelCfgRedirectPartClass.getToPath());
        if (abstractTaxonomyNode == null) {
          System.err.println("Redirect toPath '" + dataModelCfgRedirectPartClass.getToPath() + "' does not exist.");
          return null;
        } 
        System.out.println("Redirecting '" + paramAbstractTaxonomyNode.getNamePath() + " (" + paramAbstractTaxonomyNode.getIDPath() + ")' to " + abstractTaxonomyNode.getNamePath() + " (" + abstractTaxonomyNode.getIDPath() + ")'");
        return abstractTaxonomyNode.getIDPath();
      } 
    } 
    return null;
  }
  
  private String getMappedUnits(String paramString1, String paramString2) {
    String str = paramString2;
    for (DataModelCfgUnitsMapping dataModelCfgUnitsMapping : this.dataModelCfg.getUnitsMappings().getUnitsMapping()) {
      if (dataModelCfgUnitsMapping.getFromUnit() != null && !dataModelCfgUnitsMapping.getFromUnit().isEmpty() && paramString2.equals(dataModelCfgUnitsMapping.getFromUnit()))
        str = dataModelCfgUnitsMapping.getToUnit(); 
      if (dataModelCfgUnitsMapping.getProperty() != null && !dataModelCfgUnitsMapping.getProperty().isEmpty() && paramString1.equals(dataModelCfgUnitsMapping.getProperty())) {
        str = dataModelCfgUnitsMapping.getToUnit();
        break;
      } 
    } 
    return str;
  }
  
  private void writeMapping() throws Exception {
    String str = "SiliconExpertParametricMapping.xml";
    if (bUpgrade)
      str = "SiliconExpertPartClassUpgrade.xml"; 
    StreamResult streamResult = new StreamResult(new FileOutputStream(new File(outputDir, str)));
    SAXTransformerFactory sAXTransformerFactory = (SAXTransformerFactory)TransformerFactory.newInstance();
    TransformerHandler transformerHandler = sAXTransformerFactory.newTransformerHandler();
    Transformer transformer = transformerHandler.getTransformer();
    transformer.setOutputProperty("encoding", "UTF-8");
    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
    transformer.setOutputProperty("indent", "yes");
    transformerHandler.setResult(streamResult);
    transformerHandler.startDocument();
    AttributesImpl attributesImpl = new AttributesImpl();
    if (bAutoMapping)
      attributesImpl.addAttribute("", "", "defaultInherit", "CDATA", "false"); 
    transformerHandler.startElement("", "", "ContentProviderCfg", attributesImpl);
    attributesImpl.clear();
    transformerHandler.startElement("", "", "PartClasses", attributesImpl);
    writePartClass(transformerHandler, topTxNode);
    for (Taxonomy taxonomy : topTaxonomyList) {
      if (checkExcludePartClass("Part Classes", taxonomy))
        continue; 
      writePartClass(transformerHandler, taxonomy);
      for (MainCategory mainCategory : taxonomy.mainCategoryMap.values()) {
        if (checkExcludePartClass("Part Classes", mainCategory))
          continue; 
        writePartClass(transformerHandler, mainCategory);
        for (SubCategory subCategory : mainCategory.subCategoryMap.values()) {
          if (checkExcludePartClass("Part Classes", subCategory))
            continue; 
          writePartClass(transformerHandler, subCategory);
          for (ProductLine productLine : subCategory.productLineMap.values()) {
            if (checkExcludePartClass("Part Classes", productLine))
              continue; 
            writePartClass(transformerHandler, productLine);
          } 
        } 
      } 
    } 
    transformerHandler.endElement("", "", "PartClasses");
    if (bMapping) {
      if (!bAutoMapping) {
        transformerHandler.startElement("", "", "ManufacturerPartCatalogs", attributesImpl);
        writeManufacturerPartCatalog(transformerHandler, topTxNode);
        for (Taxonomy taxonomy : topTaxonomyList) {
          if (checkExcludePartClass("Manufacturer Part Mapping", taxonomy))
            continue; 
          writeManufacturerPartCatalog(transformerHandler, taxonomy);
          for (MainCategory mainCategory : taxonomy.mainCategoryMap.values()) {
            if (checkExcludePartClass("Manufacturer Part Mapping", mainCategory))
              continue; 
            writeManufacturerPartCatalog(transformerHandler, mainCategory);
            for (SubCategory subCategory : mainCategory.subCategoryMap.values()) {
              if (checkExcludePartClass("Manufacturer Part Mapping", subCategory))
                continue; 
              writeManufacturerPartCatalog(transformerHandler, subCategory);
              for (ProductLine productLine : subCategory.productLineMap.values()) {
                if (checkExcludePartClass("Manufacturer Part Mapping", productLine))
                  continue; 
                writeManufacturerPartCatalog(transformerHandler, productLine);
              } 
            } 
          } 
        } 
        transformerHandler.endElement("", "", "ManufacturerPartCatalogs");
      } else {
        attributesImpl.addAttribute("", "", "autoMapping", "CDATA", "true");
        attributesImpl.addAttribute("", "", "defaultSyncType", "CDATA", "DIRECT");
        transformerHandler.startElement("", "", "ManufacturerPartCatalogs", attributesImpl);
        writeManufacturerPartCatalog(transformerHandler, topTxNode);
        transformerHandler.endElement("", "", "ManufacturerPartCatalogs");
      } 
      attributesImpl.clear();
      if (bAutoMapping)
        attributesImpl.addAttribute("", "", "autoMapping", "CDATA", "true"); 
      transformerHandler.startElement("", "", "ComponentCatalogs", attributesImpl);
      writeComponentCatalog(transformerHandler, topTxNode);
      for (Taxonomy taxonomy : topTaxonomyList) {
        if (checkExcludePartClass("Component Mapping", taxonomy))
          continue; 
        writeComponentCatalog(transformerHandler, taxonomy);
        for (MainCategory mainCategory : taxonomy.mainCategoryMap.values()) {
          if (checkExcludePartClass("Component Mapping", mainCategory))
            continue; 
          writeComponentCatalog(transformerHandler, mainCategory);
          for (SubCategory subCategory : mainCategory.subCategoryMap.values()) {
            if (checkExcludePartClass("Component Mapping", subCategory))
              continue; 
            writeComponentCatalog(transformerHandler, subCategory);
            for (ProductLine productLine : subCategory.productLineMap.values()) {
              if (checkExcludePartClass("Component Mapping", productLine))
                continue; 
              writeComponentCatalog(transformerHandler, productLine);
            } 
          } 
        } 
      } 
      transformerHandler.endElement("", "", "ComponentCatalogs");
    } 
    attributesImpl.clear();
    attributesImpl.addAttribute("", "", "ccpMfgId", "CDATA", "ManufacturerName");
    attributesImpl.addAttribute("", "", "ccpId", "CDATA", "ManufacturerID");
    attributesImpl.addAttribute("", "", "ccpMfgName", "CDATA", "ManufacturerName");
    transformerHandler.startElement("", "", "Manufacturers", attributesImpl);
    attributesImpl.clear();
    transformerHandler.startElement("", "", "ManufacturerPropertyMaps", attributesImpl);
    attributesImpl.clear();
    attributesImpl.addAttribute("", "", "dmn", "CDATA", "InternetAddress");
    attributesImpl.addAttribute("", "", "ccpId", "CDATA", "ManufacturerURL");
    attributesImpl.addAttribute("", "", "fieldType", "CDATA", "INTERNET_ADDRESS");
    transformerHandler.startElement("", "", "ManufacturerPropertyMap", attributesImpl);
    transformerHandler.endElement("", "", "ManufacturerPropertyMap");
    attributesImpl.clear();
    attributesImpl.addAttribute("", "", "dmn", "CDATA", "TelephoneNo");
    attributesImpl.addAttribute("", "", "ccpId", "CDATA", "PhoneNumber");
    attributesImpl.addAttribute("", "", "fieldType", "CDATA", "PHONE_NUMBER");
    transformerHandler.startElement("", "", "ManufacturerPropertyMap", attributesImpl);
    transformerHandler.endElement("", "", "ManufacturerPropertyMap");
    attributesImpl.clear();
    attributesImpl.addAttribute("", "", "dmn", "CDATA", "ECMfgAddress");
    attributesImpl.addAttribute("", "", "ccpId", "CDATA", "Address");
    attributesImpl.addAttribute("", "", "fieldType", "CDATA", "ADDRESS");
    transformerHandler.startElement("", "", "ManufacturerPropertyMap", attributesImpl);
    transformerHandler.endElement("", "", "ManufacturerPropertyMap");
    attributesImpl.clear();
    attributesImpl.addAttribute("", "", "dmn", "CDATA", "EmailAddress");
    attributesImpl.addAttribute("", "", "ccpId", "CDATA", "Email");
    attributesImpl.addAttribute("", "", "fieldType", "CDATA", "EMAIL_ADDRESS");
    transformerHandler.startElement("", "", "ManufacturerPropertyMap", attributesImpl);
    transformerHandler.endElement("", "", "ManufacturerPropertyMap");
    attributesImpl.clear();
    attributesImpl.addAttribute("", "", "dmn", "CDATA", "ECMfgDUNSNumber");
    attributesImpl.addAttribute("", "", "ccpId", "CDATA", "DUNSNumber");
    attributesImpl.addAttribute("", "", "fieldType", "CDATA", "DUNS_NUMBER");
    transformerHandler.startElement("", "", "ManufacturerPropertyMap", attributesImpl);
    transformerHandler.endElement("", "", "ManufacturerPropertyMap");
    transformerHandler.endElement("", "", "ManufacturerPropertyMaps");
    transformerHandler.endElement("", "", "Manufacturers");
    transformerHandler.endElement("", "", "ContentProviderCfg");
    transformerHandler.endDocument();
    streamResult.getOutputStream().close();
  }
  
  private void writePartClass(TransformerHandler paramTransformerHandler, AbstractTaxonomyNode paramAbstractTaxonomyNode) throws Exception {
    AttributesImpl attributesImpl = new AttributesImpl();
    String str1 = paramAbstractTaxonomyNode.getIDPath();
    String str2 = getRedirectPartClassId(paramAbstractTaxonomyNode);
    if (str2 != null) {
      String str = "Redirection from " + paramAbstractTaxonomyNode.getNamePath() + "(" + str1 + ")";
      paramTransformerHandler.comment(str.toCharArray(), 0, str.length());
      str1 = str2;
    } 
    boolean bool = false;
    for (AbstractTaxonomyNode abstractTaxonomyNode = paramAbstractTaxonomyNode; abstractTaxonomyNode != null && !bool; abstractTaxonomyNode = abstractTaxonomyNode.getParent()) {
      if (checkExcludePartClass("Mapping Part Class", abstractTaxonomyNode))
        bool = true; 
    } 
    if (bool) {
      System.out.println("Excluding PartClass for '" + paramAbstractTaxonomyNode.getNamePath() + "'");
      return;
    } 
    attributesImpl.addAttribute("", "", "id", "CDATA", str1);
    if (paramAbstractTaxonomyNode.getParent() != null && paramAbstractTaxonomyNode.getName() != null)
      attributesImpl.addAttribute("", "", "label", "CDATA", paramAbstractTaxonomyNode.getName()); 
    if (paramAbstractTaxonomyNode.getParent() != null)
      attributesImpl.addAttribute("", "", "parentId", "CDATA", paramAbstractTaxonomyNode.getParent().getIDPath()); 
    paramTransformerHandler.startElement("", "", "PartClass", attributesImpl);
    attributesImpl.clear();
    paramTransformerHandler.startElement("", "", "Properties", attributesImpl);
    for (FeatureInstance featureInstance : paramAbstractTaxonomyNode.featureMap.values()) {
      if (featureInstance.bSearchable && !featureInstance.bDynamicPackage && !featureInstance.bStaticPackage)
        writePartClassProperty(paramTransformerHandler, featureInstance); 
    } 
    for (FeatureInstance featureInstance : paramAbstractTaxonomyNode.featureMap.values()) {
      if (featureInstance.bDynamicPackage || featureInstance.bStaticPackage)
        writePartClassProperty(paramTransformerHandler, featureInstance); 
    } 
    for (FeatureInstance featureInstance : paramAbstractTaxonomyNode.featureMap.values()) {
      if (!featureInstance.bSearchable && !featureInstance.bDynamicPackage && !featureInstance.bStaticPackage)
        writePartClassProperty(paramTransformerHandler, featureInstance); 
    } 
    paramTransformerHandler.endElement("", "", "Properties");
    paramTransformerHandler.endElement("", "", "PartClass");
  }
  
  private void writePartClassProperty(TransformerHandler paramTransformerHandler, FeatureInstance paramFeatureInstance) throws Exception {
    AttributesImpl attributesImpl = new AttributesImpl();
    Feature feature = paramFeatureInstance.getFeature();
    attributesImpl.addAttribute("", "", "id", "CDATA", getFullFeatureName(paramFeatureInstance));
    if (paramFeatureInstance.type.equals("N") && paramFeatureInstance.units != null && !paramFeatureInstance.units.isEmpty()) {
      String str = paramFeatureInstance.units;
      if (this.dataModelCfg.getPartClassUnits() != null)
        for (DataModelCfgPartClassUnit dataModelCfgPartClassUnit : this.dataModelCfg.getPartClassUnits().getPartClassUnit()) {
          if ((dataModelCfgPartClassUnit.getProperty() != null || dataModelCfgPartClassUnit.getFromUnit() != null) && (dataModelCfgPartClassUnit.getProperty() == null || dataModelCfgPartClassUnit.getProperty().equals(feature.getName())) && (dataModelCfgPartClassUnit.getFromUnit() == null || paramFeatureInstance.units.equals(dataModelCfgPartClassUnit.getFromUnit()))) {
            str = dataModelCfgPartClassUnit.getToUnit();
            break;
          } 
        }  
      if (!str.trim().isEmpty())
        attributesImpl.addAttribute("", "", "baseUnits", "CDATA", str); 
    } 
    if (paramFeatureInstance.bSearchable)
      attributesImpl.addAttribute("", "", "searchable", "CDATA", "true"); 
    paramTransformerHandler.startElement("", "", "Property", attributesImpl);
    paramTransformerHandler.endElement("", "", "Property");
  }
  
  private String getFullFeatureName(FeatureInstance paramFeatureInstance) {
    Feature feature = paramFeatureInstance.getFeature();
    return feature.getName();
  }
  
  private void writeManufacturerPartCatalog(TransformerHandler paramTransformerHandler, AbstractTaxonomyNode paramAbstractTaxonomyNode) throws Exception {
    String str1 = paramAbstractTaxonomyNode.getIDPath();
    String str2 = getRedirectPartClassId(paramAbstractTaxonomyNode);
    if (str2 != null)
      str1 = str2; 
    AttributesImpl attributesImpl = new AttributesImpl();
    if (paramAbstractTaxonomyNode.getParent() != null) {
      attributesImpl.addAttribute("", "", "classDMN", "CDATA", paramAbstractTaxonomyNode.getDMN());
    } else {
      attributesImpl.addAttribute("", "", "classDMN", "CDATA", "RootManufacturerPart");
    } 
    paramTransformerHandler.startElement("", "", "ManufacturerPartCatalog", attributesImpl);
    attributesImpl.clear();
    paramTransformerHandler.startElement("", "", "ContentProviderMaps", attributesImpl);
    paramTransformerHandler.comment(paramAbstractTaxonomyNode.getNameOrID().toCharArray(), 0, paramAbstractTaxonomyNode.getNameOrID().length());
    attributesImpl.clear();
    attributesImpl.addAttribute("", "", "ccpId", "CDATA", str1);
    paramTransformerHandler.startElement("", "", "ContentProviderMap", attributesImpl);
    attributesImpl.clear();
    paramTransformerHandler.startElement("", "", "ContentProviderPropertyMaps", attributesImpl);
    for (FeatureInstance featureInstance : paramAbstractTaxonomyNode.featureMap.values()) {
      if (featureInstance.bSearchable && !featureInstance.bDynamicPackage && !featureInstance.bStaticPackage)
        writeContentProviderPropertyMap(paramTransformerHandler, featureInstance); 
    } 
    for (FeatureInstance featureInstance : paramAbstractTaxonomyNode.featureMap.values()) {
      if (!featureInstance.bSearchable && !featureInstance.bDynamicPackage && !featureInstance.bStaticPackage)
        writeContentProviderPropertyMap(paramTransformerHandler, featureInstance); 
    } 
    for (FeatureInstance featureInstance : paramAbstractTaxonomyNode.featureMap.values()) {
      if (featureInstance.bDynamicPackage || featureInstance.bStaticPackage)
        writeContentProviderPropertyMap(paramTransformerHandler, featureInstance); 
    } 
    paramTransformerHandler.endElement("", "", "ContentProviderPropertyMaps");
    paramTransformerHandler.endElement("", "", "ContentProviderMap");
    paramTransformerHandler.endElement("", "", "ContentProviderMaps");
    paramTransformerHandler.endElement("", "", "ManufacturerPartCatalog");
  }
  
  private void writeContentProviderPropertyMap(TransformerHandler paramTransformerHandler, FeatureInstance paramFeatureInstance) throws Exception {
    Feature feature = paramFeatureInstance.getFeature();
    AttributesImpl attributesImpl = new AttributesImpl();
    attributesImpl.clear();
    attributesImpl.addAttribute("", "", "ccpId", "CDATA", getFullFeatureName(paramFeatureInstance));
    attributesImpl.addAttribute("", "", "dmn", "CDATA", feature.getDMN());
    paramTransformerHandler.startElement("", "", "ContentProviderPropertyMap", attributesImpl);
    paramTransformerHandler.endElement("", "", "ContentProviderPropertyMap");
  }
  
  private void writeComponentCatalog(TransformerHandler paramTransformerHandler, AbstractTaxonomyNode paramAbstractTaxonomyNode) throws Exception {
    boolean bool = (paramAbstractTaxonomyNode.getParent() == null) ? true : false;
    if (bAutoMapping && !bool && (!(paramAbstractTaxonomyNode instanceof ProductLine) || !ScriptedMapping.hasExplicitPropMaps(paramAbstractTaxonomyNode.getNamePath())))
      return; 
    AttributesImpl attributesImpl = new AttributesImpl();
    if (paramAbstractTaxonomyNode.getParent() != null) {
      String str = paramAbstractTaxonomyNode.getDMN().replaceAll("M-", "C-");
      attributesImpl.addAttribute("", "", "classDMN", "CDATA", str);
    } else {
      attributesImpl.addAttribute("", "", "classDMN", "CDATA", "RootComponent");
    } 
    paramTransformerHandler.startElement("", "", "ComponentCatalog", attributesImpl);
    attributesImpl.clear();
    paramTransformerHandler.startElement("", "", "ManufacturerPartMaps", attributesImpl);
    attributesImpl.clear();
    if (!bool) {
      attributesImpl.addAttribute("", "", "classDMN", "CDATA", paramAbstractTaxonomyNode.getDMN());
    } else {
      attributesImpl.addAttribute("", "", "classDMN", "CDATA", "RootManufacturerPart");
    } 
    paramTransformerHandler.startElement("", "", "ManufacturerPartMap", attributesImpl);
    attributesImpl.clear();
    if (paramAbstractTaxonomyNode instanceof ProductLine) {
      HashMap<Object, Object> hashMap = new HashMap<>();
      for (FeatureInstance featureInstance : paramAbstractTaxonomyNode.featureMap.values()) {
        Feature feature = featureInstance.getFeature();
        hashMap.put(feature.getName(), feature.getDMN());
      } 
      paramTransformerHandler.startElement("", "", "ManufacturerPartPropertyMaps", attributesImpl);
      for (FeatureInstance featureInstance : paramAbstractTaxonomyNode.featureMap.values()) {
        Feature feature = featureInstance.getFeature();
        if (featureInstance.bDynamicPackage || featureInstance.bStaticPackage)
          continue; 
        attributesImpl.clear();
        ScriptedMapping scriptedMapping = ScriptedMapping.get(paramAbstractTaxonomyNode.getNamePath(), feature.getName(), true);
        if (scriptedMapping != null) {
          attributesImpl.addAttribute("", "", "mappingFunction", "CDATA", scriptedMapping.getFunctionString((Map)hashMap));
          paramTransformerHandler.startElement("", "", "ScriptedManufacturerPartPropertyMap", attributesImpl);
          paramTransformerHandler.endElement("", "", "ScriptedManufacturerPartPropertyMap");
          continue;
        } 
        if (!bAutoMapping) {
          attributesImpl.addAttribute("", "", "compDMN", "CDATA", feature.getDMN());
          attributesImpl.addAttribute("", "", "mpnDMN", "CDATA", feature.getDMN());
          paramTransformerHandler.startElement("", "", "ManufacturerPartPropertyMap", attributesImpl);
          paramTransformerHandler.endElement("", "", "ManufacturerPartPropertyMap");
        } 
      } 
      paramTransformerHandler.endElement("", "", "ManufacturerPartPropertyMaps");
    } else if (bool) {
      paramTransformerHandler.startElement("", "", "ManufacturerPartPropertyMaps", attributesImpl);
      if (this.dataModelCfg.getTopLevelScriptedMappings() != null && !this.dataModelCfg.getTopLevelScriptedMappings().getTopLevelScriptedMapping().isEmpty())
        for (DataModelCfgTopLevelScriptedMapping dataModelCfgTopLevelScriptedMapping : this.dataModelCfg.getTopLevelScriptedMappings().getTopLevelScriptedMapping()) {
          attributesImpl.clear();
          attributesImpl.addAttribute("", "", "mappingFunction", "CDATA", dataModelCfgTopLevelScriptedMapping.getMappingFunction());
          attributesImpl.addAttribute("", "", "inherit", "", dataModelCfgTopLevelScriptedMapping.isInherit() ? "true" : "false");
          attributesImpl.addAttribute("", "", "ignoreMissing", "", dataModelCfgTopLevelScriptedMapping.isIgnoreMissing() ? "true" : "false");
          paramTransformerHandler.startElement("", "", "ScriptedManufacturerPartPropertyMap", attributesImpl);
          paramTransformerHandler.endElement("", "", "ScriptedManufacturerPartPropertyMap");
        }  
      paramTransformerHandler.endElement("", "", "ManufacturerPartPropertyMaps");
    } 
    paramTransformerHandler.endElement("", "", "ManufacturerPartMap");
    paramTransformerHandler.endElement("", "", "ManufacturerPartMaps");
    if (!bAutoMapping) {
      attributesImpl.clear();
      paramTransformerHandler.startElement("", "", "ContentProviderMaps", attributesImpl);
      paramTransformerHandler.comment(paramAbstractTaxonomyNode.getNameOrID().toCharArray(), 0, paramAbstractTaxonomyNode.getNameOrID().length());
      String str1 = paramAbstractTaxonomyNode.getIDPath();
      String str2 = getRedirectPartClassId(paramAbstractTaxonomyNode);
      if (str2 != null)
        str1 = str2; 
      attributesImpl.clear();
      attributesImpl.addAttribute("", "", "ccpId", "CDATA", str1);
      paramTransformerHandler.startElement("", "", "ContentProviderMap", attributesImpl);
      attributesImpl.clear();
      paramTransformerHandler.startElement("", "", "ContentProviderPropertyMaps", attributesImpl);
      for (FeatureInstance featureInstance : paramAbstractTaxonomyNode.featureMap.values()) {
        if (featureInstance.bDynamicPackage || featureInstance.bStaticPackage)
          continue; 
        writeContentProviderPropertyMap(paramTransformerHandler, featureInstance);
      } 
      paramTransformerHandler.endElement("", "", "ContentProviderPropertyMaps");
      paramTransformerHandler.endElement("", "", "ContentProviderMap");
      paramTransformerHandler.endElement("", "", "ContentProviderMaps");
    } 
    paramTransformerHandler.endElement("", "", "ComponentCatalog");
  }
  
  private void writeCatalog(TransformerHandler paramTransformerHandler, AbstractTaxonomyNode paramAbstractTaxonomyNode) throws SAXException {
    writeObjectStart(paramTransformerHandler, paramAbstractTaxonomyNode.getDMSCatalogID(), "CatalogGroup");
    writeField(paramTransformerHandler, "CatalogGroup", paramAbstractTaxonomyNode.getDMSCatalogID());
    writeField(paramTransformerHandler, "ParentKey", paramAbstractTaxonomyNode.getParent().getDMSCatalogID());
    writeField(paramTransformerHandler, "ObjectClass", EDMClassMode.getClassNum());
    if (bMPN) {
      writeField(paramTransformerHandler, "InitFileName", "SiliconExpertTaxonomyMPN.init");
    } else if (bComponent) {
      writeField(paramTransformerHandler, "InitFileName", "SiliconExpertTaxonomyComp.init");
    } else {
      writeField(paramTransformerHandler, "InitFileName", "SiliconExpertTaxonomy.init");
    } 
    if (paramAbstractTaxonomyNode.getChildren().isEmpty()) {
      writeField(paramTransformerHandler, "CatalogStatus", "01");
    } else {
      writeField(paramTransformerHandler, "CatalogStatus", "10");
    } 
    writeField(paramTransformerHandler, "DomainModelName", paramAbstractTaxonomyNode.getDMN());
    writeListStart(paramTransformerHandler, "Text", true);
    writeField(paramTransformerHandler, "Language", "e");
    String str = paramAbstractTaxonomyNode.getDMN();
    if (str.length() > 30)
      str = str.substring(0, 30); 
    writeField(paramTransformerHandler, "Abbreviation", str);
    writeField(paramTransformerHandler, "CatalogTitle", paramAbstractTaxonomyNode.getNameOrID());
    writeField(paramTransformerHandler, "Description", paramAbstractTaxonomyNode.getNameOrID());
    writeListEnd(paramTransformerHandler);
    boolean bool = true;
    byte b = 10;
    for (FeatureInstance featureInstance : paramAbstractTaxonomyNode.featureMap.values()) {
      Feature feature = featureInstance.getFeature();
      writeListStart(paramTransformerHandler, "CatalogCharacteristics", bool);
      if (bool)
        bool = false; 
      if (featureInstance.bSearchable && !featureInstance.bDynamicPackage && !featureInstance.bStaticPackage) {
        writeField(paramTransformerHandler, "TabSheet", "General");
      } else if (!featureInstance.bSearchable && !featureInstance.bDynamicPackage && !featureInstance.bStaticPackage) {
        writeField(paramTransformerHandler, "TabSheet", "SE Other");
      } else if (featureInstance.bDynamicPackage || featureInstance.bStaticPackage) {
        writeField(paramTransformerHandler, "TabSheet", "Package");
      } 
      String str1 = feature.getId();
      if (!bUpgrade && EDMClassMode.isComponentMode() && (featureInstance.bScriptedMapping || featureInstance.bDoubleMapping))
        str1 = str1 + "_R"; 
      writeField(paramTransformerHandler, "Characteristic", str1);
      writeField(paramTransformerHandler, "OrderNo", Integer.toString(b));
      writeListEnd(paramTransformerHandler);
      b += 10;
    } 
    writeObjectEnd(paramTransformerHandler);
  }
  
  private void writeDynIdCharacteristics(TransformerHandler paramTransformerHandler) throws SAXException {
    for (String str1 : Feature.getTableNames()) {
      String str2 = str1.replaceAll("[A-Za-z_]", "");
      String str3 = EDMClassMode.getClassNumPrefix() + "dyn_id" + EDMClassMode.getClassNumPrefix();
      writeObjectStart(paramTransformerHandler, str3, "Characteristic");
      writeField(paramTransformerHandler, "Characteristic", str3);
      writeField(paramTransformerHandler, "ValueType", "3");
      writeField(paramTransformerHandler, "ValueLength", "111");
      writeField(paramTransformerHandler, "Accesspath", str3);
      writeField(paramTransformerHandler, "TableColumn", "obj_id");
      writeField(paramTransformerHandler, "DomainModelName", "Dyn_id" + str2);
      writeField(paramTransformerHandler, "ObjectClass", EDMClassMode.getClassNum());
      writeField(paramTransformerHandler, "RefClass", EDMClassMode.getClassNum());
      if (bMPN) {
        writeField(paramTransformerHandler, "InitFileName", "SiliconExpertParamsMPN.init");
      } else if (bComponent) {
        writeField(paramTransformerHandler, "InitFileName", "SiliconExpertParamsComp.init");
      } else {
        writeField(paramTransformerHandler, "InitFileName", "SiliconExpertParams.init");
      } 
      writeListStart(paramTransformerHandler, "Text", true);
      writeField(paramTransformerHandler, "TabSheet", "General");
      writeField(paramTransformerHandler, "InformationText", str3);
      writeField(paramTransformerHandler, "SearchText", str3);
      writeField(paramTransformerHandler, "Language", "e");
      writeListEnd(paramTransformerHandler);
      writeField(paramTransformerHandler, "TableName", str1);
      writeField(paramTransformerHandler, "DisposeOrder", "75");
      writeField(paramTransformerHandler, "SearchAlignment", "3");
      writeField(paramTransformerHandler, "InformationAlignment", "3");
      writeField(paramTransformerHandler, "CharactStatus", "{5,8,11,13,15}");
      writeObjectEnd(paramTransformerHandler);
    } 
  }
  
  private void writeCharacteristics(TransformerHandler paramTransformerHandler, AbstractTaxonomyNode paramAbstractTaxonomyNode) throws SAXException {
    for (FeatureInstance featureInstance : paramAbstractTaxonomyNode.featureMap.values())
      writeCharacteristic(paramTransformerHandler, paramAbstractTaxonomyNode, featureInstance); 
  }
  
  private void writeCharacteristic(TransformerHandler paramTransformerHandler, AbstractTaxonomyNode paramAbstractTaxonomyNode, FeatureInstance paramFeatureInstance) throws SAXException {
    Feature feature = paramFeatureInstance.getFeature();
    String str = feature.getName();
    if (!bUpgrade && (paramFeatureInstance.bScriptedMapping || paramFeatureInstance.bDoubleMapping))
      str = str + "_R"; 
    if (uniqueFeatureSet.contains(str))
      return; 
    uniqueFeatureSet.add(str);
    if (!bUpgrade && (paramFeatureInstance.bScriptedMapping || paramFeatureInstance.bDoubleMapping)) {
      writeObjectStart(paramTransformerHandler, feature.getId() + "_R", "Characteristic");
      writeField(paramTransformerHandler, "Characteristic", feature.getId() + "_R");
      writeField(paramTransformerHandler, "ValueType", "2");
      writeField(paramTransformerHandler, "ValueLength", "32");
      writeField(paramTransformerHandler, "Precision", "16");
      writeField(paramTransformerHandler, "InformationWidth", "20");
      writeField(paramTransformerHandler, "TableColumn", feature.getColumn() + "_R");
      writeField(paramTransformerHandler, "Patternmatch", feature.patternMatch);
      writeField(paramTransformerHandler, "DomainModelName", feature.getDMN() + "_R");
    } else {
      writeObjectStart(paramTransformerHandler, feature.getId(), "Characteristic");
      writeField(paramTransformerHandler, "Characteristic", feature.getId());
      writeField(paramTransformerHandler, "ValueType", "3");
      int i = feature.length;
      if (this.dataModelCfg.getPropertyLengthOverrides() != null)
        for (DataModelCfgPropertyLengthOverride dataModelCfgPropertyLengthOverride : this.dataModelCfg.getPropertyLengthOverrides().getPropertyLengthOverride()) {
          if (feature.getInfoText().equals(dataModelCfgPropertyLengthOverride.getId())) {
            i = dataModelCfgPropertyLengthOverride.getLength();
            break;
          } 
        }  
      writeField(paramTransformerHandler, "ValueLength", Integer.toString(i));
      writeField(paramTransformerHandler, "InformationWidth", Integer.toString(feature.displayLength));
      writeField(paramTransformerHandler, "TableColumn", feature.getColumn());
      writeField(paramTransformerHandler, "DomainModelName", feature.getDMN());
    } 
    writeField(paramTransformerHandler, "ObjectClass", "0");
    writeField(paramTransformerHandler, "RefClass", EDMClassMode.getClassNum());
    if (bMPN) {
      writeField(paramTransformerHandler, "InitFileName", "SiliconExpertParamsMPN.init");
    } else if (bComponent) {
      writeField(paramTransformerHandler, "InitFileName", "SiliconExpertParamsComp.init");
    } else {
      writeField(paramTransformerHandler, "InitFileName", "SiliconExpertParams.init");
    } 
    writeListStart(paramTransformerHandler, "Text", true);
    writeField(paramTransformerHandler, "TabSheet", "General");
    writeField(paramTransformerHandler, "InformationText", feature.getInfoText());
    writeField(paramTransformerHandler, "SearchText", feature.getSearchText());
    writeField(paramTransformerHandler, "Language", "e");
    writeListEnd(paramTransformerHandler);
    writeField(paramTransformerHandler, "TableName", feature.getTableName());
    writeField(paramTransformerHandler, "DisposeOrder", "75");
    writeField(paramTransformerHandler, "SearchAlignment", "3");
    writeField(paramTransformerHandler, "InformationAlignment", "3");
    writeObjectEnd(paramTransformerHandler);
  }
  
  private void writeObjectStart(TransformerHandler paramTransformerHandler, String paramString1, String paramString2) throws SAXException {
    AttributesImpl attributesImpl = new AttributesImpl();
    attributesImpl.addAttribute("", "", "objectid", "CDATA", paramString1);
    attributesImpl.addAttribute("", "", "class", "CDATA", paramString2);
    paramTransformerHandler.startElement("", "", "object", attributesImpl);
  }
  
  private void writeObjectEnd(TransformerHandler paramTransformerHandler) throws SAXException {
    paramTransformerHandler.endElement("", "", "object");
  }
  
  private void writeListStart(TransformerHandler paramTransformerHandler, String paramString, boolean paramBoolean) throws SAXException {
    AttributesImpl attributesImpl = new AttributesImpl();
    attributesImpl.addAttribute("", "", "id", "CDATA", paramString);
    if (paramBoolean)
      attributesImpl.addAttribute("", "", "clear", "CDATA", "true"); 
    paramTransformerHandler.startElement("", "", "list", attributesImpl);
  }
  
  private void writeListEnd(TransformerHandler paramTransformerHandler) throws SAXException {
    paramTransformerHandler.endElement("", "", "list");
  }
  
  private void writeField(TransformerHandler paramTransformerHandler, String paramString1, String paramString2) throws SAXException {
    AttributesImpl attributesImpl = new AttributesImpl();
    attributesImpl.addAttribute("", "", "id", "CDATA", paramString1);
    paramTransformerHandler.startElement("", "", "field", attributesImpl);
    paramTransformerHandler.characters(paramString2.toCharArray(), 0, paramString2.length());
    paramTransformerHandler.endElement("", "", "field");
  }
  
  private void saveXMLMessage(InputStream paramInputStream, String paramString) throws IOException {
    FileOutputStream fileOutputStream = new FileOutputStream(new File(paramString));
    if (paramInputStream != null) {
      byte[] arrayOfByte = new byte[1024];
      int i;
      for (i = paramInputStream.read(arrayOfByte); i != -1; i = paramInputStream.read(arrayOfByte))
        fileOutputStream.write(arrayOfByte, 0, i); 
    } 
    fileOutputStream.flush();
    fileOutputStream.close();
  }
  
  private void writeSpreadsheet() throws Exception {}
  
  private boolean doesPropertyExistInTaxonomyNode(AbstractTaxonomyNode paramAbstractTaxonomyNode, String paramString) {
    for (FeatureInstance featureInstance : paramAbstractTaxonomyNode.featureMap.values()) {
      Feature feature = featureInstance.getFeature();
      if (feature.getName().equals(paramString))
        return true; 
    } 
    return false;
  }
  
  class SearchableFeatureData {
    String type = "";
    
    String longestFeatureValue = "";
    
    public SearchableFeatureData(String param1String1, String param1String2) {
      this.type = param1String1;
      this.longestFeatureValue = param1String2;
    }
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\datamodel\GenerateDataModel.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */