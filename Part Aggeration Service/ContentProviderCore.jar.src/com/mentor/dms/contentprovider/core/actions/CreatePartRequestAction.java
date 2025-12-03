package com.mentor.dms.contentprovider.core.actions;

import com.mentor.datafusion.oi.OICursor;
import com.mentor.datafusion.oi.OIException;
import com.mentor.datafusion.oi.OIHelper;
import com.mentor.datafusion.oi.OIObjectManager;
import com.mentor.datafusion.oi.OIQuery;
import com.mentor.datafusion.oi.model.OIField;
import com.mentor.datafusion.oi.type.OIObject;
import com.mentor.datafusion.oi.type.OIObjectSet;
import com.mentor.datafusion.utils.logger.MGLogger;
import com.mentor.dms.contentprovider.core.AbstractContentProvider;
import com.mentor.dms.contentprovider.core.ContentProviderException;
import com.mentor.dms.contentprovider.core.ContentProviderGlobal;
import com.mentor.dms.contentprovider.core.IContentProviderPartRequest;
import com.mentor.dms.contentprovider.core.IContentProviderResultRecord;
import com.mentor.dms.contentprovider.core.plugin.ContentProviderViewCompareWindow;
import com.mentor.dms.contentprovider.core.plugin.partrequest.CatalogField;
import com.mentor.dms.contentprovider.core.plugin.partrequest.NewPartRequestDlg;
import com.mentor.dms.contentprovider.core.plugin.searchui.ContentProviderSearchWindow;
import com.mentor.dms.contentprovider.core.utils.DateUtils;
import com.mentor.dms.contentprovider.core.utils.validate.CPPartClass;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Window;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import org.apache.commons.lang3.StringUtils;

public class CreatePartRequestAction extends AbstractSearchResultsAction implements IContentProviderPartRequest {
  private static MGLogger log = MGLogger.getLogger(CreatePartRequestAction.class);
  
  private static HashMap<String, String> partClassPathMap = null;
  
  private AbstractContentProvider ccp;
  
  private LinkedHashMap<String, OIObject> prodLibMap;
  
  private static NewPartRequestDlg dialog = null;
  
  private boolean bMultiplePartsPerRequest = false;
  
  public CreatePartRequestAction() {
    super("New Part Request", getImageIcon());
    setEnabled(true);
  }
  
  public CreatePartRequestAction(boolean paramBoolean) {
    this();
    this.bMultiplePartsPerRequest = paramBoolean;
  }
  
  private static ImageIcon getImageIcon() {
    Image image = Toolkit.getDefaultToolkit().getImage(CreatePartRequestAction.class.getResource("images/add_request.png"));
    return new ImageIcon(image);
  }
  
  public void selectionHandler() {
    if (this.bMultiplePartsPerRequest) {
      setEnabled((ContentProviderSearchWindow.getSelectionCount() >= 1 && ContentProviderSearchWindow.getSelectionCount() <= 5));
    } else {
      setEnabled((ContentProviderSearchWindow.getSelectionCount() == 1));
    } 
  }
  
  public void doAction() {
    LinkedHashMap<String, String> linkedHashMap;
    log.info("CreatePartRequestAction");
    if (dialog != null && dialog.isShowing()) {
      JOptionPane.showMessageDialog(ContentProviderGlobal.getRootFrame(), "The New Part Request process is already running.", "New Part Request", 2);
      dialog.toFront();
      return;
    } 
    Collection<IContentProviderResultRecord> collection = ContentProviderSearchWindow.getSelectedResults();
    IContentProviderResultRecord iContentProviderResultRecord = collection.iterator().next();
    this.ccp = iContentProviderResultRecord.getContentProvider();
    String str1 = "New Part Request (" + iContentProviderResultRecord.getManufacturerName() + " : " + iContentProviderResultRecord.getPartNumber() + ")";
    String str2 = iContentProviderResultRecord.getManufacturerName();
    String str3 = iContentProviderResultRecord.getPartNumber();
    try {
      this.prodLibMap = getProdLibList();
      linkedHashMap = getRequestCatalogs();
    } catch (OIException oIException) {
      log.error(oIException.getMessage(), (Throwable)oIException);
      return;
    } 
    if (this.prodLibMap.size() == 0) {
      log.error("No available Production libraries in EDM.");
      return;
    } 
    if (linkedHashMap.size() == 0) {
      log.error("No available request catalogs in EDM.");
      return;
    } 
    try {
      int i = 0;
      OIObject oIObject = getMPN(str2, str3);
      if (oIObject != null) {
        List<String> list = getComponentsFromMPN(oIObject);
        if (0 < list.size()) {
          String str = String.join("\n", (Iterable)list);
          log.info("Components already exists. Do you want to create a new request?");
          i = showMessage(str1, "Components already exists. Do you want to create a new request?", str);
        } else {
          log.info("A Manufacturer part already exists. Do you want to create a new request?");
          i = JOptionPane.showConfirmDialog(ContentProviderGlobal.getRootFrame(), "A Manufacturer part already exists. Do you want to create a new request?", str1, 0, 2);
        } 
        if (i != 0)
          return; 
      } 
      dialog = new NewPartRequestDlg(this, ContentProviderGlobal.getRootFrame(), str1, iContentProviderResultRecord, this.prodLibMap.keySet(), linkedHashMap);
      dialog.setParentFrame((Window)ContentProviderSearchWindow.getInstance());
      dialog.setVisible(true);
    } catch (OIException oIException) {
      log.error(oIException.getMessage(), (Throwable)oIException);
    } 
  }
  
  public void createPartRequest(IContentProviderResultRecord paramIContentProviderResultRecord) {
    LinkedHashMap<String, String> linkedHashMap;
    if (dialog != null && dialog.isShowing()) {
      JOptionPane.showMessageDialog(ContentProviderGlobal.getRootFrame(), "The New Part Request process is already running.", "New Part Request", 2);
      dialog.toFront();
      return;
    } 
    this.ccp = paramIContentProviderResultRecord.getContentProvider();
    String str1 = "New Part Request (" + paramIContentProviderResultRecord.getManufacturerName() + " : " + paramIContentProviderResultRecord.getPartNumber() + ")";
    String str2 = paramIContentProviderResultRecord.getManufacturerName();
    String str3 = paramIContentProviderResultRecord.getPartNumber();
    try {
      this.prodLibMap = getProdLibList();
      linkedHashMap = getRequestCatalogs();
    } catch (OIException oIException) {
      log.error(oIException.getMessage(), (Throwable)oIException);
      return;
    } 
    if (this.prodLibMap.size() == 0) {
      log.error("No available Production libraries in EDM.");
      return;
    } 
    if (linkedHashMap.size() == 0) {
      log.error("No available request catalogs in EDM.");
      return;
    } 
    try {
      int i = 0;
      OIObject oIObject = getMPN(str2, str3);
      if (oIObject != null) {
        List<String> list = getComponentsFromMPN(oIObject);
        if (0 < list.size()) {
          String str = String.join("\n", (Iterable)list);
          i = showMessage(str1, "Components already exists. Do you want to create a new request?", str);
        } else {
          i = JOptionPane.showConfirmDialog(ContentProviderGlobal.getRootFrame(), "A Manufacturer part already exists. Do you want to create a new request?", str1, 0, 2);
        } 
        if (i != 0)
          return; 
      } 
      dialog = new NewPartRequestDlg(this, ContentProviderGlobal.getRootFrame(), str1, paramIContentProviderResultRecord, this.prodLibMap.keySet(), linkedHashMap);
      dialog.setParentFrame(ContentProviderViewCompareWindow.getJFrame());
      dialog.setVisible(true);
    } catch (OIException oIException) {
      log.error(oIException.getMessage(), (Throwable)oIException);
    } 
  }
  
  public void createPartRequest(Collection<IContentProviderResultRecord> paramCollection) {
    if (!isMultiplePartsSupported())
      JOptionPane.showMessageDialog(ContentProviderGlobal.getRootFrame(), "Part Requests on multiple parts is not supported"); 
  }
  
  public boolean isMultiplePartsSupported() {
    return false;
  }
  
  public void dialogClosing(NewPartRequestDlg paramNewPartRequestDlg) {
    try {
      IContentProviderResultRecord iContentProviderResultRecord = paramNewPartRequestDlg.getResultRec();
      String str1 = iContentProviderResultRecord.getObjectID();
      String str2 = iContentProviderResultRecord.getContentProvider().getId();
      String str3 = paramNewPartRequestDlg.getProductionLibrary();
      String str4 = paramNewPartRequestDlg.getSiteDomainName();
      List<CatalogField> list = paramNewPartRequestDlg.getMandatoryFields();
      if (paramNewPartRequestDlg.getButton() == 0) {
        log.info("");
        int i = 0;
        checkInputValues(list);
        OIObject oIObject = getRequest(str3, str4, str2, str1);
        if (oIObject != null) {
          log.info("A request already exists. Do you want to create a new request?");
          i = JOptionPane.showConfirmDialog(ContentProviderGlobal.getRootFrame(), "A request already exists. Do you want to create a new request?", paramNewPartRequestDlg.getTitle(), 0, 2);
        } 
        if (i == 0) {
          OIObject oIObject1 = this.prodLibMap.get(str3);
          IContentProviderResultRecord iContentProviderResultRecord1 = this.ccp.getPart(iContentProviderResultRecord);
          createRequest(str4, oIObject1, list, iContentProviderResultRecord1);
        } else if (i == 1) {
          if (oIObject != null)
            showObjectOnEDM(oIObject); 
        } else if (i == 2) {
          log.info("CANCEL");
        } 
      } 
    } catch (OIException oIException) {
      log.error(oIException.getMessage(), (Throwable)oIException);
      return;
    } catch (ContentProviderException contentProviderException) {
      log.error(contentProviderException.getMessage(), (Throwable)contentProviderException);
      return;
    } catch (Exception exception) {
      log.error(exception.getMessage(), exception);
      return;
    } 
    paramNewPartRequestDlg.dispose();
  }
  
  private int showMessage(String paramString1, String paramString2, String paramString3) {
    JPanel jPanel = new JPanel();
    jPanel.setLayout(new BoxLayout(jPanel, 1));
    JLabel jLabel = new JLabel(paramString2);
    jLabel.setAlignmentY(0.0F);
    jLabel.setAlignmentX(0.5F);
    jPanel.add(jLabel);
    jPanel.add(Box.createRigidArea(new Dimension(1, 8)));
    JTextArea jTextArea = new JTextArea(paramString3, 5, 10);
    jTextArea.setAlignmentY(1.0F);
    jTextArea.setAlignmentX(0.5F);
    jTextArea.setEditable(false);
    jTextArea.setLineWrap(true);
    JScrollPane jScrollPane = new JScrollPane();
    jScrollPane.setViewportView(jTextArea);
    jPanel.add(jScrollPane);
    return JOptionPane.showConfirmDialog(ContentProviderGlobal.getRootFrame(), jPanel, paramString1, 0, 2);
  }
  
  private LinkedHashMap<String, OIObject> getProdLibList() throws OIException {
    LinkedHashMap<Object, Object> linkedHashMap = new LinkedHashMap<>();
    String str = ContentProviderGlobal.getDMSInstance().getObjectManager().getCurrentLibraryConfiguration();
    OIObjectManager oIObjectManager = ContentProviderGlobal.getDMSInstance().getObjectManager();
    OIQuery oIQuery = oIObjectManager.createQuery("ProductionLibrary", true);
    oIQuery.addRestriction("Status", "~U&~X&~R");
    if (str != null)
      oIQuery.addRestriction("ProductionLibrary", str); 
    oIQuery.addColumn("ProductionLibrary");
    oIQuery.addSortBy("ProductionLibrary", true);
    OICursor oICursor = oIQuery.execute();
    while (oICursor.next())
      linkedHashMap.put(oICursor.getStringified("ProductionLibrary"), oICursor.getObject()); 
    return (LinkedHashMap)linkedHashMap;
  }
  
  private LinkedHashMap<String, String> getRequestCatalogs() throws OIException {
    LinkedHashMap<Object, Object> linkedHashMap = new LinkedHashMap<>();
    OIObjectManager oIObjectManager = ContentProviderGlobal.getDMSInstance().getObjectManager();
    OIQuery oIQuery = oIObjectManager.createQuery("CatalogGroup", true);
    oIQuery.addRestriction("ObjectClass", "111");
    oIQuery.addRestriction("Text.Language", "e");
    oIQuery.addRestriction("CatalogGroup", "IIXB?*");
    oIQuery.addColumn("DomainModelName");
    oIQuery.addColumn("Text.Abbreviation");
    oIQuery.addColumn("CatalogGroup");
    oIQuery.addSortBy("CatalogGroup", true);
    OICursor oICursor = oIQuery.execute();
    while (oICursor.next())
      linkedHashMap.put(oICursor.getString("DomainModelName"), oICursor.getString("Abbreviation")); 
    return (LinkedHashMap)linkedHashMap;
  }
  
  private OIObject getRequest(String paramString1, String paramString2, String paramString3, String paramString4) throws OIException {
    OIObjectManager oIObjectManager = ContentProviderGlobal.getDMSInstance().getObjectManager();
    OIQuery oIQuery = oIObjectManager.createQuery(paramString2, true);
    oIQuery.addRestriction("ProductionLibrary", paramString1);
    oIQuery.addRestriction("SciReqParts_PartsList.SciReqParts_CpProviderId", OIHelper.escapeQueryRestriction(paramString3));
    oIQuery.addRestriction("SciReqParts_PartsList.SciReqParts_CpPartUid", OIHelper.escapeQueryRestriction(paramString4));
    oIQuery.addColumn("RequestNumber");
    oIQuery.addColumn("ProductionLibrary");
    oIQuery.addColumn("SciReqParts_PartsList.SciReqParts_CpProviderId");
    oIQuery.addColumn("SciReqParts_PartsList.SciReqParts_CpPartUid");
    OICursor oICursor = oIQuery.execute();
    return oICursor.next() ? oICursor.getObject() : null;
  }
  
  private OIObject getMPN(String paramString1, String paramString2) throws OIException {
    OIObjectManager oIObjectManager = ContentProviderGlobal.getDMSInstance().getObjectManager();
    OIQuery oIQuery = oIObjectManager.createQuery("ManufacturerPart", true);
    oIQuery.addRestriction("PartNumber", OIHelper.escapeQueryRestriction(paramString2));
    oIQuery.addRestriction("ManufacturerName", OIHelper.escapeQueryRestriction(paramString1));
    oIQuery.addColumn("ManufacturerpartId");
    oIQuery.addColumn("PartNumber");
    oIQuery.addColumn("ManufacturerName");
    OICursor oICursor = oIQuery.execute();
    return oICursor.next() ? oICursor.getObject() : null;
  }
  
  private List<String> getComponentsFromMPN(OIObject paramOIObject) throws OIException {
    ArrayList<String> arrayList = new ArrayList();
    OIObjectManager oIObjectManager = ContentProviderGlobal.getDMSInstance().getObjectManager();
    OIQuery oIQuery = oIObjectManager.createQuery("Component", true);
    oIQuery.addRestriction("ApprovedManufacturerList.MfgPartNumber", OIHelper.escapeQueryRestriction(paramOIObject.getString("ManufacturerpartId")));
    oIQuery.addColumn("PartNumber");
    oIQuery.addColumn("ProductionLibraryList.ProductionLibrary");
    OICursor oICursor = oIQuery.execute();
    while (oICursor.next()) {
      String str1 = oICursor.getStringified("PartNumber");
      String str2 = oICursor.getStringified("ProductionLibrary");
      if (str2 == null)
        str2 = "-"; 
      arrayList.add(str1 + " (" + str1 + ")");
    } 
    return arrayList;
  }
  
  private void createRequest(String paramString, OIObject paramOIObject, List<CatalogField> paramList, IContentProviderResultRecord paramIContentProviderResultRecord) throws OIException {
    OIObjectManager oIObjectManager = ContentProviderGlobal.getDMSInstance().getObjectManager();
    OIObject oIObject = null;
    try {
      log.info("Create Request siteDMN:[" + paramString + "] Lib:[" + paramOIObject.getObjectID() + "]");
      ContentProviderSearchWindow.getInstance().setCursor(new Cursor(3));
      oIObject = oIObjectManager.createObject(paramString);
      oIObject.set("ProductionLibrary", paramOIObject);
      oIObject.set("LongDescription", paramIContentProviderResultRecord.getFindchipsURL());
      for (CatalogField catalogField : paramList) {
        log.debug("set Mandatory Fields:[" + catalogField.getLabel() + "]");
        setValue(oIObject, catalogField);
      } 
      OIObjectSet oIObjectSet = oIObject.getSet("SciReqParts_PartsList");
      OIObject oIObject1 = oIObjectSet.createLine();
      oIObject1.set("SciReqParts_CpProviderId", paramIContentProviderResultRecord.getContentProvider().getId());
      oIObject1.set("SciReqParts_CpPartUid", paramIContentProviderResultRecord.getObjectID());
      oIObject1.set("SciReqParts_CpMpn", paramIContentProviderResultRecord.getPartNumber());
      oIObject1.set("SciReqParts_CpMfr", paramIContentProviderResultRecord.getManufacturerName());
      oIObject1.set("SciReqParts_CpPartDescription", paramIContentProviderResultRecord.getPartProperty(this.ccp.getDescriptionPropID()).getValue());
      String str = getTaxonoyPath(this.ccp, paramIContentProviderResultRecord.getPartClassID());
      if (str == null) {
        log.info("Failed to get taxonoyPath.");
        str = paramIContentProviderResultRecord.getPartClassID();
      } 
      oIObject1.set("SciReqParts_CpClsPath", str);
      oIObjectManager.makePermanent(oIObject);
      try {
        Thread.sleep(500L);
      } catch (InterruptedException interruptedException) {}
      try {
        showObjectOnEDM(oIObject);
      } catch (Exception exception) {
        JOptionPane.showMessageDialog((Component)dialog, "Request was created successfully but could not be displayed.", "Warning", 2);
        log.warn("Request was created successfully but could not be displayed.", exception);
      } 
    } catch (OIException oIException) {
      if (oIObject != null)
        oIObjectManager.evict(oIObject); 
      throw oIException;
    } catch (Exception exception) {
      if (oIObject != null)
        oIObjectManager.evict(oIObject); 
      throw new OIException(exception);
    } finally {
      ContentProviderSearchWindow.getInstance().setCursor(null);
    } 
  }
  
  private boolean checkInputValues(List<CatalogField> paramList) throws OIException {
    for (CatalogField catalogField : paramList) {
      if (catalogField.getInputValue().isEmpty())
        throw new OIException("Characteristic '" + catalogField.getLabel() + "' not set"); 
      if (catalogField.isObjectRefefence()) {
        OIObject oIObject = getReferenceObject(catalogField.getInputValue(), catalogField.getRefClassDomainName(), catalogField.getLabel());
        continue;
      } 
      String str = catalogField.getInputValue();
      if (catalogField.getFieldType() == OIField.Type.DATE) {
        try {
          DateUtils.parse(str);
        } catch (ParseException parseException) {
          throw new OIException("Invalid date '" + str + "' in characteristic '" + catalogField.getLabel() + "'. Defined format: YYYY-MM-DD HH24:MI:SS");
        } 
        continue;
      } 
      try {
        int i = catalogField.getMaxLength();
        int j = catalogField.getPrecision();
        if (catalogField.getFieldType() == OIField.Type.DOUBLE) {
          String[] arrayOfString = str.split("\\.");
          if (arrayOfString[0].startsWith("-"))
            arrayOfString[0] = arrayOfString[0].substring(1); 
          if (i < arrayOfString[0].length())
            throw new OIException(catalogField.getLabel() + " value is too long. It must be between 1 and " + catalogField.getLabel() + " characters long."); 
          if (1 < arrayOfString.length && j < arrayOfString[1].length())
            throw new OIException("Value of the \"" + catalogField.getLabel() + "\" characteristic does not match the pattern \"Floating point value, wrong precision, required " + j + " fraction digits\"."); 
          continue;
        } 
        if (catalogField.getFieldType() == OIField.Type.INTEGER) {
          String str1 = str;
          if (str.startsWith("-"))
            str1 = str.substring(1); 
          if (i < str1.length())
            throw new OIException(catalogField.getLabel() + " value is too long. It must be between 1 and " + catalogField.getLabel() + " characters long."); 
          continue;
        } 
        if (i < str.length())
          throw new OIException(catalogField.getLabel() + " value is too long. It must be between 1 and " + catalogField.getLabel() + " characters long."); 
      } catch (NumberFormatException numberFormatException) {
        throw new OIException("Value of the '" + catalogField.getLabel() + "' characteristic does not match the pattern.");
      } 
    } 
    return true;
  }
  
  private void setValue(OIObject paramOIObject, CatalogField paramCatalogField) throws OIException {
    if (paramCatalogField.getInputValue().isEmpty())
      throw new OIException("Characteristic '" + paramCatalogField.getLabel() + "' not set"); 
    if (paramCatalogField.isObjectRefefence()) {
      OIObject oIObject = getReferenceObject(paramCatalogField.getInputValue(), paramCatalogField.getRefClassDomainName(), paramCatalogField.getLabel());
      paramOIObject.set(paramCatalogField.getDomainName(), oIObject);
    } else {
      String str = paramCatalogField.getInputValue();
      if (paramCatalogField.getFieldType() == OIField.Type.DATE) {
        try {
          paramOIObject.set(paramCatalogField.getDomainName(), DateUtils.parse(str));
        } catch (ParseException parseException) {
          throw new OIException("Invalid date '" + str + "' in characteristic '" + paramCatalogField.getLabel() + "'. Defined format: YYYY-MM-DD HH24:MI:SS");
        } 
      } else {
        try {
          int i = paramCatalogField.getMaxLength();
          int j = paramCatalogField.getPrecision();
          if (paramCatalogField.getFieldType() == OIField.Type.DOUBLE) {
            String[] arrayOfString = str.split("\\.");
            if (arrayOfString[0].startsWith("-"))
              arrayOfString[0] = arrayOfString[0].substring(1); 
            if (i < arrayOfString[0].length())
              throw new OIException(paramCatalogField.getLabel() + " value is too long. It must be between 1 and " + paramCatalogField.getLabel() + " characters long."); 
            if (1 < arrayOfString.length && j < arrayOfString[1].length())
              throw new OIException("Value of the \"" + paramCatalogField.getLabel() + "\" characteristic does not match the pattern \"Floating point value, wrong precision, required " + j + " fraction digits\"."); 
          } else if (paramCatalogField.getFieldType() == OIField.Type.INTEGER) {
            String str1 = str;
            if (str.startsWith("-"))
              str1 = str.substring(1); 
            if (i < str1.length())
              throw new OIException(paramCatalogField.getLabel() + " value is too long. It must be between 1 and " + paramCatalogField.getLabel() + " characters long."); 
          } else if (i < str.length()) {
            throw new OIException(paramCatalogField.getLabel() + " value is too long. It must be between 1 and " + paramCatalogField.getLabel() + " characters long.");
          } 
          if (paramCatalogField.getFieldType() == OIField.Type.INTEGER) {
            paramOIObject.set(paramCatalogField.getDomainName(), Integer.valueOf(Integer.parseInt(str)));
          } else if (paramCatalogField.getFieldType() == OIField.Type.DOUBLE) {
            paramOIObject.set(paramCatalogField.getDomainName(), Double.valueOf(Double.parseDouble(str)));
          } else {
            paramOIObject.set(paramCatalogField.getDomainName(), str);
          } 
        } catch (NumberFormatException numberFormatException) {
          throw new OIException("Value of the '" + paramCatalogField.getLabel() + "' characteristic does not match the pattern.");
        } 
      } 
    } 
  }
  
  private OIObject getReferenceObject(String paramString1, String paramString2, String paramString3) throws OIException {
    if (paramString1.isEmpty())
      throw new OIException("Characteristic " + paramString3 + " not set"); 
    OIObjectManager oIObjectManager = ContentProviderGlobal.getDMSInstance().getObjectManager();
    try {
      return oIObjectManager.getObjectByID(paramString1, paramString2, true);
    } catch (OIException oIException) {
      if (oIException.getCause() instanceof com.mentor.datafusion.dfo.DFObjectNotFoundException)
        throw new OIException("Referenced object " + paramString3 + " " + paramString1 + " not found"); 
      throw oIException;
    } 
  }
  
  private void showObjectOnEDM(OIObject paramOIObject) {
    if (paramOIObject != null) {
      ContentProviderGlobal.getDMSInstance().getObjectPanelManager().update(paramOIObject);
      ContentProviderGlobal.getDMSInstance().getObjectPanelManager().showObject(paramOIObject);
    } 
  }
  
  private String getTaxonoyPath(AbstractContentProvider paramAbstractContentProvider, String paramString) {
    try {
      partClassPathMap = getTaxonomyPathMap(paramAbstractContentProvider);
    } catch (Exception exception) {
      log.info(exception.getMessage());
      return null;
    } 
    return partClassPathMap.get(paramString);
  }
  
  private HashMap<String, String> getTaxonomyPathMap(AbstractContentProvider paramAbstractContentProvider) throws Exception {
    if (partClassPathMap != null)
      return partClassPathMap; 
    partClassPathMap = new HashMap<>();
    List list = paramAbstractContentProvider.getPartClassInfo();
    for (CPPartClass cPPartClass : list) {
      if (StringUtils.isEmpty(cPPartClass.getParentID())) {
        partClassPathMap.put(cPPartClass.getId(), cPPartClass.getLabel());
        continue;
      } 
      String str = partClassPathMap.get(cPPartClass.getParentID());
      partClassPathMap.put(cPPartClass.getId(), str + " - " + str);
    } 
    return partClassPathMap;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\actions\CreatePartRequestAction.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */