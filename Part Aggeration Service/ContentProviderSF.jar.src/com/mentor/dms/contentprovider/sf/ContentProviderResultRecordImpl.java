package com.mentor.dms.contentprovider.sf;

import com.mentor.datafusion.oi.OIException;
import com.mentor.datafusion.oi.OIHelper;
import com.mentor.datafusion.oi.OIObjectManager;
import com.mentor.datafusion.oi.OIQuery;
import com.mentor.datafusion.utils.logger.MGLogger;
import com.mentor.dms.contentprovider.core.AbstractContentProvider;
import com.mentor.dms.contentprovider.core.AbstractContentProviderResultRecord;
import com.mentor.dms.contentprovider.core.ComponentProperty;
import com.mentor.dms.contentprovider.core.ContentProviderAlternate;
import com.mentor.dms.contentprovider.core.ContentProviderChangeAlert;
import com.mentor.dms.contentprovider.core.ContentProviderDocument;
import com.mentor.dms.contentprovider.core.ContentProviderDocumentList;
import com.mentor.dms.contentprovider.core.ContentProviderException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.commons.codec.digest.DigestUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ContentProviderResultRecordImpl extends AbstractContentProviderResultRecord {
  private boolean bExistsInDMSCheckPerformed = false;
  
  private boolean bExistsInDMS = false;
  
  private String partClassID = "All Parts";
  
  private String partClassName = "All Parts";
  
  private boolean bHasECADModels = false;
  
  private boolean bSymbolAvailable = false;
  
  private boolean bFootprintAvailable = false;
  
  private boolean b3DModelAvailable = false;
  
  private static MGLogger log = MGLogger.getLogger(ContentProviderResultRecordImpl.class);
  
  public ContentProviderResultRecordImpl(AbstractContentProvider paramAbstractContentProvider) {
    super(paramAbstractContentProvider);
  }
  
  public String getPartNumber() {
    String str = "Unspecified";
    ComponentProperty componentProperty = getPartProperty("PartNumber");
    if (componentProperty != null)
      str = componentProperty.getValue(); 
    return str;
  }
  
  public String getManufacturerID() {
    String str = "Unspecified";
    ComponentProperty componentProperty = getPartProperty("Summary.ManufacturerID");
    if (componentProperty != null)
      str = componentProperty.getValue(); 
    return str;
  }
  
  public String getManufacturerName() {
    String str = "Unspecified";
    ComponentProperty componentProperty = getPartProperty("Manufacturer");
    if (componentProperty != null)
      str = componentProperty.getValue(); 
    return str;
  }
  
  public String getPartClassID() {
    return (this.partClassID != null && !this.partClassID.isEmpty()) ? this.partClassID : "Part";
  }
  
  public String getPartClassName() {
    return (this.partClassName != null && !this.partClassName.isEmpty()) ? this.partClassName : "Part";
  }
  
  protected void addCategories() {}
  
  public boolean isExistsInDMS(OIObjectManager paramOIObjectManager) throws ContentProviderException {
    if (!this.bExistsInDMSCheckPerformed) {
      this.bExistsInDMSCheckPerformed = true;
      try {
        OIQuery oIQuery = paramOIObjectManager.createQuery("ManufacturerPart", true);
        oIQuery.addRestriction("PartNumber", OIHelper.escapeQueryRestriction(getPartNumber()));
        oIQuery.addRestriction("ManufacturerName", OIHelper.escapeQueryRestriction(getManufacturerName()));
        this.bExistsInDMS = (oIQuery.count() > 0L);
        log.debug("PartNumber:" + getPartNumber() + " ManufacturerName:" + getManufacturerName() + " In EDM?:" + this.bExistsInDMS);
      } catch (OIException oIException) {
        throw new ContentProviderException(oIException.getMessage());
      } 
    } 
    return this.bExistsInDMS;
  }
  
  public String getObjectID() {
    return getPartProperty("DataProviderID").getValue();
  }
  
  public boolean isECADSymbolAvailable() {
    return this.bSymbolAvailable;
  }
  
  public boolean isECADFootprintAvailable() {
    return this.bFootprintAvailable;
  }
  
  public boolean isECAD3DModelAvailable() {
    return this.b3DModelAvailable;
  }
  
  public boolean hasECADModels() {
    return this.bHasECADModels;
  }
  
  protected void setPartClassID(String paramString) {
    this.partClassID = paramString;
  }
  
  protected void setPartClassName(String paramString) {
    this.partClassName = paramString;
  }
  
  protected void setHasECADModels(boolean paramBoolean) {
    this.bHasECADModels = paramBoolean;
  }
  
  protected void setSymbolAvailable(boolean paramBoolean) {
    this.bSymbolAvailable = paramBoolean;
  }
  
  protected void setFootprintAvailable(boolean paramBoolean) {
    this.bFootprintAvailable = paramBoolean;
  }
  
  protected void set3DModelAvailable(boolean paramBoolean) {
    this.b3DModelAvailable = paramBoolean;
  }
  
  public void loadPartDetails(Document paramDocument, boolean paramBoolean) throws ContentProviderException {
    try {
      XPath xPath = XPathFactory.newInstance().newXPath();
      String str1 = "/ServiceResult/Results/ResultDto";
      Element element = (Element)xPath.evaluate(str1, paramDocument, XPathConstants.NODE);
      NodeList nodeList1 = (NodeList)xPath.evaluate("NewPartData/*", element, XPathConstants.NODESET);
      if (nodeList1 != null && nodeList1.getLength() != 0) {
        String str4 = "";
        String str5 = "";
        String str6 = "";
        String str7 = "";
        String str8 = "";
        String str9 = "";
        for (byte b1 = 0; b1 < nodeList1.getLength(); b1++) {
          Element element1 = (Element)nodeList1.item(b1);
          String str = element1.getNodeName();
          if (str.equals("ComId")) {
            str4 = element1.getTextContent();
          } else if (str.equals("PartNumber")) {
            str5 = element1.getTextContent();
          } else if (str.equals("ManName")) {
            str6 = element1.getTextContent();
          } else if (str.equals("NewComID")) {
            str7 = element1.getTextContent();
          } else if (str.equals("NewPartNumber")) {
            str8 = element1.getTextContent();
          } else if (str.equals("NewManName")) {
            str9 = element1.getTextContent();
          } 
        } 
        throw new ContentProviderException("Part '" + str5 + "' by '" + str6 + " (" + str4 + ") has been acquired by '" + str9 + "'.  New part number is '" + str8 + "' (" + str7 + ").");
      } 
      try {
        String str = xPath.evaluate("/ServiceResult/Results/ResultDto/RequestedComID/text()", paramDocument);
        ComponentProperty componentProperty = new ComponentProperty("DataProviderID", "DataProviderID", str, "");
        addPartProperty("Summary", componentProperty);
      } catch (XPathExpressionException xPathExpressionException) {}
      String str2 = (String)xPath.evaluate("ECADModelsData/ECADModelsDto/ECADURL/text()", element, XPathConstants.STRING);
      if (str2 != null && !str2.trim().isEmpty()) {
        ComponentProperty componentProperty = new ComponentProperty("ECADURL", "ECAD Models URL", str2, "");
        componentProperty.setDocumentURL(true);
        addPartProperty("ECAD Models", componentProperty);
        setHasECADModels(true);
      } 
      NodeList nodeList2 = (NodeList)xPath.evaluate("SummaryData/*", element, XPathConstants.NODESET);
      if (nodeList2 != null) {
        ContentProviderDocumentList contentProviderDocumentList = new ContentProviderDocumentList();
        addCategoryDocumentList("Summary", contentProviderDocumentList);
        String str4 = null;
        String str5 = null;
        for (byte b1 = 0; b1 < nodeList2.getLength(); b1++) {
          String str7;
          Element element1 = (Element)nodeList2.item(b1);
          String str6 = element1.getNodeName();
          if (str6.equals("TaxonomyPathIDs")) {
            str7 = element1.getTextContent().replaceAll(" > ", ".");
            setPartClassID(str7);
            addPartProperty("Summary", new ComponentProperty("PartClassID", "PartClassID", str7, ""));
          } else if (str6.equals("TaxonomyPath")) {
            String[] arrayOfString = element1.getTextContent().split(" > ");
            setPartClassName(arrayOfString[arrayOfString.length - 1]);
          } else if (str6.equals("Datasheet")) {
            str4 = element1.getTextContent();
          } else if (str6.equals("OnlineSupplierDatasheetURL")) {
            str5 = element1.getTextContent();
          } else if (str6.equals("CountriesOfOrigin")) {
            continue;
          } 
          if (str6.equals("DataProviderID") || str6.equals("PartNumber") || str6.equals("Manufacturer") || str6.equals("PartDescription")) {
            str7 = str6;
          } else {
            str7 = "Summary." + str6;
          } 
          ComponentProperty componentProperty = new ComponentProperty(str7, element1.getNodeName(), element1.getTextContent(), "");
          componentProperty.setDocumentURL((str6.equals("Datasheet") || str6.equals("ESDSourceofInformation") || str6.equals("OnlineSupplierDatasheetURL")));
          addPartProperty("Summary", componentProperty);
          continue;
        } 
        if (str5 != null && !str5.isBlank()) {
          setDatasheetURL(str5);
        } else if (str4 != null && !str4.isBlank()) {
          setDatasheetURL(str4);
        } 
      } 
      if (!paramBoolean) {
        NodeList nodeList = (NodeList)xPath.evaluate("SummaryData/CountriesOfOrigin/CountryOfOrigin", element, XPathConstants.NODESET);
        if (nodeList != null) {
          CountriesOfOriginCustomTable countriesOfOriginCustomTable = new CountriesOfOriginCustomTable();
          addCustomTable(countriesOfOriginCustomTable);
          for (byte b1 = 0; b1 < nodeList.getLength(); b1++) {
            Element element1 = (Element)nodeList.item(b1);
            String str4 = (String)xPath.evaluate("Country/text()", element1, XPathConstants.STRING);
            String str5 = (String)xPath.evaluate("Source/text()", element1, XPathConstants.STRING);
            countriesOfOriginCustomTable.addCountryOfOrigin(str4, str5);
          } 
        } 
      } 
      nodeList2 = (NodeList)xPath.evaluate("LifeCycleData/*", element, XPathConstants.NODESET);
      if (nodeList2 != null)
        for (byte b1 = 0; b1 < nodeList2.getLength(); b1++) {
          Element element1 = (Element)nodeList2.item(b1);
          String str = element1.getNodeName();
          if (str.equals("PartCounterfeitReports")) {
            if (!paramBoolean) {
              PartCounterfeitCustomTable partCounterfeitCustomTable = new PartCounterfeitCustomTable();
              addCustomTable(partCounterfeitCustomTable);
              NodeList nodeList = (NodeList)xPath.evaluate("PartCounterFeit", element1, XPathConstants.NODESET);
              for (byte b2 = 0; b2 < nodeList.getLength(); b2++) {
                Element element2 = (Element)nodeList.item(b2);
                String str4 = (String)xPath.evaluate("MPN/text()", element2, XPathConstants.STRING);
                String str5 = (String)xPath.evaluate("Supplier/text()", element2, XPathConstants.STRING);
                String str6 = (String)xPath.evaluate("CounterfitMethods/text()", element2, XPathConstants.STRING);
                String str7 = (String)xPath.evaluate("NotificationDate/text()", element2, XPathConstants.STRING);
                String str8 = (String)xPath.evaluate("Source/text()", element2, XPathConstants.STRING);
                String str9 = (String)xPath.evaluate("Description/text()", element2, XPathConstants.STRING);
                partCounterfeitCustomTable.addPartCounterfeit(str4, str5, str6, str7, str8, str9);
              } 
            } 
          } else {
            String str4 = str;
            if (!str.equals("PartStatus"))
              str4 = "LC." + str; 
            ComponentProperty componentProperty = new ComponentProperty(str4, str, element1.getTextContent(), "");
            componentProperty.setDocumentURL(str.equals("Source"));
            addPartProperty("Life Cycle", componentProperty);
          } 
        }  
      NodeList nodeList3 = (NodeList)xPath.evaluate("ParametricData/Features", element, XPathConstants.NODESET);
      byte b;
      for (b = 0; b < nodeList3.getLength(); b++) {
        Element element1 = (Element)nodeList3.item(b);
        String str4 = xPath.evaluate("FeatureName/text()", element1);
        String str5 = xPath.evaluate("FeatureValue/text()", element1);
        String str6 = xPath.evaluate("FeatureUnit/text()", element1);
        addPartProperty("Parametrics", new ComponentProperty(str4, str4, str5, str6));
      } 
      nodeList2 = (NodeList)xPath.evaluate("PackagingData/*", element, XPathConstants.NODESET);
      if (nodeList2 != null)
        for (b = 0; b < nodeList2.getLength(); b++) {
          Element element1 = (Element)nodeList2.item(b);
          String str = "Pack." + element1.getNodeName();
          ComponentProperty componentProperty = new ComponentProperty(str, element1.getNodeName(), element1.getTextContent(), "");
          componentProperty.setDocumentURL(element1.getNodeName().equals("PackagingDocument"));
          addPartProperty("Packaging", componentProperty);
        }  
      nodeList3 = (NodeList)xPath.evaluate("PackageData/Feature", element, XPathConstants.NODESET);
      for (b = 0; b < nodeList3.getLength(); b++) {
        Element element1 = (Element)nodeList3.item(b);
        String str4 = xPath.evaluate("FeatureName/text()", element1);
        String str5 = xPath.evaluate("FeatureValue/text()", element1);
        String str6 = xPath.evaluate("FeatureUnit/text()", element1);
        addPartProperty("Package", new ComponentProperty(str4, str4, str5, str6));
      } 
      nodeList2 = (NodeList)xPath.evaluate("PackageData/*", element, XPathConstants.NODESET);
      if (nodeList2 != null)
        for (b = 0; b < nodeList2.getLength(); b++) {
          Element element1 = (Element)nodeList2.item(b);
          String str = element1.getNodeName();
          ComponentProperty componentProperty = new ComponentProperty(str, element1.getNodeName(), element1.getTextContent(), "");
          componentProperty.setDocumentURL(element1.getNodeName().equals("PackageOutline"));
          addPartProperty("Package", componentProperty);
        }  
      nodeList2 = (NodeList)xPath.evaluate("ManufacturingData/*", element, XPathConstants.NODESET);
      if (nodeList2 != null)
        for (b = 0; b < nodeList2.getLength(); b++) {
          Element element1 = (Element)nodeList2.item(b);
          String str = "Mfg." + element1.getNodeName();
          ComponentProperty componentProperty = new ComponentProperty(str, element1.getNodeName(), element1.getTextContent(), "");
          componentProperty.setDocumentURL((element1.getNodeName().equals("ReflowTempSource") || element1.getNodeName().equals("WaveTempSource")));
          addPartProperty("Manufacturing", componentProperty);
        }  
      nodeList2 = (NodeList)xPath.evaluate("ReachData/ReachDto/*", element, XPathConstants.NODESET);
      if (nodeList2 != null)
        for (b = 0; b < nodeList2.getLength(); b++) {
          Element element1 = (Element)nodeList2.item(b);
          String str4 = element1.getNodeName();
          String str5 = "Reach." + str4;
          ComponentProperty componentProperty = new ComponentProperty(str5, str4, element1.getTextContent(), "");
          componentProperty.setDocumentURL(str4.equals("CachedSource"));
          addPartProperty("Reach", componentProperty);
        }  
      nodeList2 = (NodeList)xPath.evaluate("EnvironmentalDto/*", element, XPathConstants.NODESET);
      if (nodeList2 != null)
        for (b = 0; b < nodeList2.getLength(); b++) {
          Element element1 = (Element)nodeList2.item(b);
          String str = element1.getNodeName();
          if (str.equals("ChinaRoHS")) {
            NodeList nodeList = (NodeList)xPath.evaluate("*", element1, XPathConstants.NODESET);
            for (byte b1 = 0; b1 < nodeList.getLength(); b1++) {
              Element element2 = (Element)nodeList.item(b1);
              String str4 = element2.getNodeName();
              ComponentProperty componentProperty = new ComponentProperty("ChnRoHS." + str4, str4, element2.getTextContent(), "");
              componentProperty.setDocumentURL(((str4.equals("CachedSource") || str4.equals("LiveSupplierSource")) && element2.getTextContent().startsWith("http://")));
              addPartProperty("China RoHS", componentProperty);
            } 
          } else {
            String str4 = str;
            if (!str.equals("RoHSStatus") && !str.equals("RoHSVersion"))
              str4 = "Env." + str; 
            ComponentProperty componentProperty = new ComponentProperty(str4, str, element1.getTextContent(), "");
            componentProperty.setDocumentURL((str.equals("ConflictMineralStatement") || str.equals("Source") || str.equals("EICCTemplate")));
            addPartProperty("Environmental", componentProperty);
          } 
        }  
      String str3 = (String)xPath.evaluate("averageInventory/text()", element, XPathConstants.STRING);
      addPartProperty("Availability/Pricing", new ComponentProperty("averageInventory", "Average Inventory", str3, ""));
      nodeList2 = (NodeList)xPath.evaluate("PricingData/*", element, XPathConstants.NODESET);
      if (nodeList2 != null)
        for (byte b1 = 0; b1 < nodeList2.getLength(); b1++) {
          Element element1 = (Element)nodeList2.item(b1);
          addPartProperty("Availability/Pricing", new ComponentProperty("Price." + element1.getNodeName(), element1.getNodeName(), element1.getTextContent(), ""));
        }  
      nodeList2 = (NodeList)xPath.evaluate("RiskData/*", element, XPathConstants.NODESET);
      if (nodeList2 != null)
        for (byte b1 = 0; b1 < nodeList2.getLength(); b1++) {
          Element element1 = (Element)nodeList2.item(b1);
          String str = "Risk." + element1.getNodeName();
          addPartProperty("Risk", new ComponentProperty(str, element1.getNodeName(), element1.getTextContent(), ""));
        }  
      if (!paramBoolean) {
        NodeList nodeList = (NodeList)xPath.evaluate("FranchisedInventoryData/FranchisedInventoryDto", element, XPathConstants.NODESET);
        if (nodeList != null) {
          FranchisedInventoryTable franchisedInventoryTable = new FranchisedInventoryTable();
          addCustomTable(franchisedInventoryTable);
          for (byte b1 = 0; b1 < nodeList.getLength(); b1++) {
            Element element1 = (Element)nodeList.item(b1);
            String str4 = (String)xPath.evaluate("Distributor/text()", element1, XPathConstants.STRING);
            String str5 = (String)xPath.evaluate("Quantity/text()", element1, XPathConstants.STRING);
            String str6 = (String)xPath.evaluate("BuyNowLink/text()", element1, XPathConstants.STRING);
            franchisedInventoryTable.addFranchise(str4, str5, str6);
          } 
        } 
      } 
      if (!paramBoolean) {
        NodeList nodeList = (NodeList)xPath.evaluate("ChemicalData/ChemicalDto", element, XPathConstants.NODESET);
        if (nodeList != null) {
          ChemicalCustomTable chemicalCustomTable = new ChemicalCustomTable();
          addCustomTable(chemicalCustomTable);
          for (byte b1 = 0; b1 < nodeList.getLength(); b1++) {
            Element element1 = (Element)nodeList.item(b1);
            String str4 = (String)xPath.evaluate("TotalMassInGram/text()", element1, XPathConstants.STRING);
            String str5 = (String)xPath.evaluate("TotalMassSummationInGram/text()", element1, XPathConstants.STRING);
            String str6 = (String)xPath.evaluate("LocationName/text()", element1, XPathConstants.STRING);
            String str7 = (String)xPath.evaluate("HomogenousMaterial/text()", element1, XPathConstants.STRING);
            String str8 = (String)xPath.evaluate("SubstanceIdentification/text()", element1, XPathConstants.STRING);
            String str9 = (String)xPath.evaluate("SubstanceMass/text()", element1, XPathConstants.STRING);
            String str10 = (String)xPath.evaluate("PPM/text()", element1, XPathConstants.STRING);
            String str11 = (String)xPath.evaluate("CASNumber/text()", element1, XPathConstants.STRING);
            String str12 = (String)xPath.evaluate("MDSURL/text()", element1, XPathConstants.STRING);
            chemicalCustomTable.addChemical(toDouble(str4), toDouble(str5), str6, str7, str8, toDouble(str9), toDouble(str10), str11, str12);
          } 
        } 
      } 
      if (!paramBoolean) {
        NodeList nodeList = (NodeList)xPath.evaluate("CrossData/CrossDto", element, XPathConstants.NODESET);
        if (nodeList != null)
          for (byte b1 = 0; b1 < nodeList.getLength(); b1++) {
            Element element1 = (Element)nodeList.item(b1);
            ContentProviderAlternate contentProviderAlternate = new ContentProviderAlternate();
            addAlternate(contentProviderAlternate);
            String str4 = (String)xPath.evaluate("CrossType/text()", element1, XPathConstants.STRING);
            String str5 = (String)xPath.evaluate("CrossPartNumber/text()", element1, XPathConstants.STRING);
            String str6 = (String)xPath.evaluate("CrossManufacturer/text()", element1, XPathConstants.STRING);
            String str7 = (String)xPath.evaluate("Comment/text()", element1, XPathConstants.STRING);
            contentProviderAlternate.setManufacturerCrossRefType(str4);
            contentProviderAlternate.setPartNumber(str5);
            contentProviderAlternate.setManufacturerName(str6);
            contentProviderAlternate.setComments(str7);
          }  
      } 
      NodeList nodeList4 = (NodeList)xPath.evaluate("FullCounterfeitData/*", element, XPathConstants.NODESET);
      if (nodeList4 != null)
        for (byte b1 = 0; b1 < nodeList4.getLength(); b1++) {
          Element element1 = (Element)nodeList4.item(b1);
          String str = "";
          if (element1.getFirstChild() == null || element1.getFirstChild().getNodeType() != 1) {
            String str4 = element1.getNodeName();
            String str5 = "Counterfeit." + str4;
            if (element1.getFirstChild() != null && element1.getFirstChild().getNodeType() != 1)
              str = element1.getTextContent(); 
            addPartProperty("Counterfeit", new ComponentProperty(str5, element1.getNodeName(), str, ""));
          } 
        }  
      NodeList nodeList5 = (NodeList)xPath.evaluate("PCNDetails/PCNDto", element, XPathConstants.NODESET);
      if (nodeList5 != null)
        for (byte b1 = 0; b1 < nodeList5.getLength(); b1++) {
          String str10;
          Element element1 = (Element)nodeList5.item(b1);
          ContentProviderChangeAlert contentProviderChangeAlert = new ContentProviderChangeAlert();
          addChangeAlert(contentProviderChangeAlert);
          String str4 = (String)xPath.evaluate("PCNNumber/text()", element1, XPathConstants.STRING);
          contentProviderChangeAlert.setAlertNumber(str4);
          contentProviderChangeAlert.setNotificationNumber(str4);
          String str5 = (String)xPath.evaluate("DescriptionOfChange/text()", element1, XPathConstants.STRING);
          contentProviderChangeAlert.setNotificationDescription(str5);
          String str6 = (String)xPath.evaluate("TypeOfChange/text()", element1, XPathConstants.STRING);
          contentProviderChangeAlert.setNotificationType(str6);
          String str7 = (String)xPath.evaluate("NotificationDate/text()", element1, XPathConstants.STRING);
          contentProviderChangeAlert.setIssueDate(getSEDate(str7));
          String str8 = (String)xPath.evaluate("EffectiveDate/text()", element1, XPathConstants.STRING);
          contentProviderChangeAlert.setImplementationDate(getSEDate(str8));
          String str9 = (String)xPath.evaluate("PcnSource/text()", element1, XPathConstants.STRING);
          if (!str4.isEmpty()) {
            str10 = str4;
          } else {
            String str = str5 + str5 + str6 + str7 + str8;
            str10 = DigestUtils.md5Hex(str);
          } 
          contentProviderChangeAlert.setObjectId(str10);
          if (str9 != null && !str9.isEmpty()) {
            ContentProviderDocument contentProviderDocument = new ContentProviderDocument();
            contentProviderDocument.setTitle("Information Source");
            contentProviderDocument.setPublicationDate(new Date(0L));
            contentProviderDocument.setURL(str9);
            contentProviderChangeAlert.getAttachedDocuments().add(contentProviderDocument);
            contentProviderChangeAlert.setInformationSource("See attached document.");
          } 
        }  
      if (!paramBoolean) {
        String str = (String)xPath.evaluate("ProductImage/ProductImageLarge/text()", element, XPathConstants.STRING);
        setProductImageURL(str);
      } 
    } catch (Exception exception) {
      throw new ContentProviderException(exception.getMessage());
    } 
  }
  
  private Date getSEDate(String paramString) throws ParseException {
    Date date = null;
    if (paramString != null && !paramString.isEmpty()) {
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
      date = simpleDateFormat.parse(paramString);
    } 
    return date;
  }
  
  private double toDouble(String paramString) {
    return (paramString == null || paramString.trim().isEmpty()) ? 0.0D : Double.parseDouble(paramString);
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\ContentProviderResultRecordImpl.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */