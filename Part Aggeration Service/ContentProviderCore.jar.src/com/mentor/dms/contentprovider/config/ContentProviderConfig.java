package com.mentor.dms.contentprovider.config;

import com.mentor.datafusion.oi.OIClassManager;
import com.mentor.datafusion.oi.OIException;
import com.mentor.datafusion.oi.OIObjectManager;
import com.mentor.datafusion.oi.OIObjectManagerFactory;
import com.mentor.datafusion.oi.login.OIAuthenticate;
import com.mentor.datafusion.oi.login.OIAuthenticateFactory;
import com.mentor.datafusion.oi.model.OIClass;
import com.mentor.datafusion.oi.model.OIField;
import com.mentor.datafusion.oi.type.OIBlob;
import com.mentor.datafusion.oi.type.OIObject;
import com.mentor.datafusion.utils.logger.MGLogger;
import com.mentor.dms.contentprovider.AbstractContentProvider;
import com.mentor.dms.contentprovider.ContentProviderFactory;
import com.mentor.dms.contentprovider.config.xml.ConfigXMLComponentCatalog;
import com.mentor.dms.contentprovider.config.xml.ConfigXMLContentProviderCfg;
import com.mentor.dms.contentprovider.config.xml.ConfigXMLManufacturerPartCatalog;
import com.mentor.dms.contentprovider.config.xml.ConfigXMLManufacturerPartMap;
import com.mentor.dms.contentprovider.config.xml.ConfigXMLManufacturerPropertyMap;
import com.mentor.dms.contentprovider.config.xml.ConfigXMLPartClass;
import com.mentor.dms.contentprovider.config.xml.ContentProviderConfigurationXML;
import com.mentor.dms.contentprovider.utils.DataModelUtils;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

public class ContentProviderConfig {
  static MGLogger logger = MGLogger.getLogger(ContentProviderConfig.class);
  
  private AbstractContentProvider ccp;
  
  private ConfigXMLContentProviderCfg cfgXML;
  
  private OIObjectManager om = null;
  
  private Date configDocDate = null;
  
  private ContentProviderConfigScriptEngine se = null;
  
  private boolean bDefaultInherit = true;
  
  private boolean configMode = false;
  
  private boolean bValidateScriptedMappings = true;
  
  private boolean bMpnAutoMapping = false;
  
  private boolean bCompAutoMapping = false;
  
  private boolean bCompAllowMoveParts = true;
  
  protected PropertySyncType defaultMPNSyncType = PropertySyncType.DIRECT;
  
  protected PropertySyncType defaultCompSyncType = PropertySyncType.DIRECT;
  
  private ContentProviderConfigDMSCatalogRepository<ContentProviderConfigComponentCatalog> compCatalogRepository = new ContentProviderConfigDMSCatalogRepository<>();
  
  private ContentProviderConfigDMSCatalogRepository<ContentProviderConfigMPNCatalog> mpnCatalogRepository = new ContentProviderConfigDMSCatalogRepository<>();
  
  private ArrayList<ConfigXMLManufacturerPropertyMap> mfgPropMapList = null;
  
  protected Map<String, List<ContentProviderConfigComponentCatalog>> mpnDMNToComponentCatalogListMap = new HashMap<>();
  
  private LinkedHashMap<String, ContentProviderConfigPartClass> ccpId2PartClassMap = new LinkedHashMap<>();
  
  ArrayList<ContentProviderConfigProperty> idPropertyList = new ArrayList<>();
  
  public ContentProviderConfig(AbstractContentProvider paramAbstractContentProvider) {
    this.ccp = paramAbstractContentProvider;
    this.se = new ContentProviderConfigScriptEngine(paramAbstractContentProvider.getMappingUtils());
  }
  
  public void setObjectManager(OIObjectManager paramOIObjectManager) {
    this.om = paramOIObjectManager;
  }
  
  public boolean isDefaultInherit() {
    return this.bDefaultInherit;
  }
  
  public boolean isMPNAutoMapping() {
    return this.bMpnAutoMapping;
  }
  
  public boolean isCompAutoMapping() {
    return this.bCompAutoMapping;
  }
  
  public boolean isCompAllowMoveParts() {
    return this.bCompAllowMoveParts;
  }
  
  public Collection<ContentProviderConfigPartClass> getPartClasses() {
    return this.ccpId2PartClassMap.values();
  }
  
  public ContentProviderConfigPartClass getPartClassByContentProviderId(String paramString) {
    return this.ccpId2PartClassMap.get(paramString);
  }
  
  public Collection<ContentProviderConfigProperty> getIdProperties() {
    return this.idPropertyList;
  }
  
  public Collection<ContentProviderConfigMPNCatalog> getMPNCatalogs() {
    return this.mpnCatalogRepository.getCatalogs();
  }
  
  public ContentProviderConfigMPNCatalog getMPNCatalogConfigByDMN(String paramString) {
    return this.mpnCatalogRepository.getCatalog(paramString);
  }
  
  public Collection<ContentProviderConfigMPNCatalog> getMPNCatalogsByContentProviderId(String paramString) {
    return this.mpnCatalogRepository.getCatalogsByContentProviderId(paramString);
  }
  
  public Collection<ContentProviderConfigComponentCatalog> getComponentCatalogs() {
    return this.compCatalogRepository.getCatalogs();
  }
  
  public ContentProviderConfigComponentCatalog getComponentCatalogConfigByDMN(String paramString) {
    return getComponentCatalogConfigByDMN(paramString, false);
  }
  
  public ContentProviderConfigComponentCatalog getComponentCatalogConfigByDMN(String paramString, boolean paramBoolean) {
    ContentProviderConfigComponentCatalog contentProviderConfigComponentCatalog = this.compCatalogRepository.getCatalog(paramString);
    if (contentProviderConfigComponentCatalog == null && paramBoolean)
      contentProviderConfigComponentCatalog = getRootComponentCatalogConfig(); 
    return contentProviderConfigComponentCatalog;
  }
  
  public ContentProviderConfigComponentCatalog getRootComponentCatalogConfig() {
    return this.compCatalogRepository.getCatalog("RootComponent");
  }
  
  public Collection<ContentProviderConfigComponentCatalog> getComponentCatalogsByContentProviderId(String paramString) {
    return this.compCatalogRepository.getCatalogsByContentProviderId(paramString);
  }
  
  public Collection<ContentProviderConfigComponentCatalog> getComponentCatalogsByMPNDMN(String paramString) {
    Collection<ContentProviderConfigComponentCatalog> collection = this.mpnDMNToComponentCatalogListMap.get(paramString);
    return (collection != null) ? collection : Collections.<ContentProviderConfigComponentCatalog>emptyList();
  }
  
  public ContentProviderConfigScriptEngine getScriptEngine() {
    return this.se;
  }
  
  public String getManufacturersCCPID() {
    return (this.cfgXML.getManufacturers() != null) ? this.cfgXML.getManufacturers().getCcpId() : null;
  }
  
  public String getManufacturersCCPManufacturerID() {
    return (this.cfgXML.getManufacturers() != null) ? this.cfgXML.getManufacturers().getCcpMfgId() : null;
  }
  
  public String getManufacturersCCPManufacturerName() {
    return (this.cfgXML.getManufacturers() != null) ? this.cfgXML.getManufacturers().getCcpMfgName() : null;
  }
  
  public Collection<ConfigXMLManufacturerPropertyMap> getManufacturersPropertyMaps() {
    if (this.cfgXML.getManufacturers() != null && this.cfgXML.getManufacturers().getManufacturerPropertyMaps() != null) {
      if (this.mfgPropMapList == null) {
        this.mfgPropMapList = new ArrayList<>();
        Iterator<ConfigXMLManufacturerPropertyMap> iterator = this.cfgXML.getManufacturers().getManufacturerPropertyMaps().getManufacturerPropertyMap().iterator();
        while (iterator.hasNext())
          this.mfgPropMapList.add(iterator.next()); 
      } 
      return this.mfgPropMapList;
    } 
    return Collections.emptyList();
  }
  
  protected PropertySyncType getDefaultMPNSyncType() {
    return this.defaultMPNSyncType;
  }
  
  protected PropertySyncType getDefaultCompSyncType() {
    return this.defaultCompSyncType;
  }
  
  private OIObject getDocumentObject(OIObjectManager paramOIObjectManager) throws ContentProviderConfigException {
    try {
      String str = this.ccp.getToolboxId();
      return paramOIObjectManager.getObjectByID(str, "ToolsContentProviderCfgs", true);
    } catch (OIException oIException) {
      throw new ContentProviderConfigException("Unable to locate Content Provider application configuration toolbox:  " + oIException.getMessage());
    } 
  }
  
  private InputStream getConfigFileInputStream(OIObject paramOIObject) throws ContentProviderConfigException {
    try {
      OIBlob oIBlob = paramOIObject.getBlob("CCPCfgBlob");
      return new BufferedInputStream(oIBlob.getInputStream(), 32768);
    } catch (OIException oIException) {
      throw new ContentProviderConfigException("While reading contents of Content Provider mapping configuration file:  " + oIException.getMessage());
    } 
  }
  
  private InputStream getMappingJavaScriptFileInputStream(OIObject paramOIObject) throws ContentProviderConfigException {
    if (!paramOIObject.getOIClass().hasField("CCPCfgJSBlob"))
      return null; 
    try {
      OIBlob oIBlob = paramOIObject.getBlob("CCPCfgJSBlob");
      return oIBlob.getInputStream();
    } catch (OIException oIException) {
      throw new ContentProviderConfigException("While reading contents of Content Provider mapping JavaScript file:  " + oIException.getMessage());
    } 
  }
  
  public void read(InputStream paramInputStream) throws ContentProviderConfigException {
    if (this.om == null)
      throw new ContentProviderConfigException("If reading configuration mapping from file then first call setObjectManager()."); 
    this.cfgXML = ContentProviderConfigurationXML.read(paramInputStream);
    read(this.cfgXML);
  }
  
  private void read(ConfigXMLContentProviderCfg paramConfigXMLContentProviderCfg) throws ContentProviderConfigException {
    this.bDefaultInherit = paramConfigXMLContentProviderCfg.isDefaultInherit();
    for (ConfigXMLPartClass configXMLPartClass : paramConfigXMLContentProviderCfg.getPartClasses().getPartClass()) {
      ContentProviderConfigPartClass contentProviderConfigPartClass = new ContentProviderConfigPartClass(this);
      contentProviderConfigPartClass.read(configXMLPartClass);
      this.ccpId2PartClassMap.put(contentProviderConfigPartClass.getContentProviderId(), contentProviderConfigPartClass);
    } 
    this.bMpnAutoMapping = paramConfigXMLContentProviderCfg.getManufacturerPartCatalogs().isAutoMapping();
    this.defaultMPNSyncType = Enum.<PropertySyncType>valueOf(PropertySyncType.class, paramConfigXMLContentProviderCfg.getManufacturerPartCatalogs().getDefaultSyncType().value());
    if (this.bMpnAutoMapping)
      processManufacturerPartAutoMappings(); 
    if (paramConfigXMLContentProviderCfg.getManufacturerPartCatalogs() != null)
      for (ConfigXMLManufacturerPartCatalog configXMLManufacturerPartCatalog : paramConfigXMLContentProviderCfg.getManufacturerPartCatalogs().getManufacturerPartCatalog()) {
        ContentProviderConfigMPNCatalog contentProviderConfigMPNCatalog = getMPNCatalogConfigByDMN(configXMLManufacturerPartCatalog.getClassDMN());
        if (contentProviderConfigMPNCatalog == null)
          contentProviderConfigMPNCatalog = new ContentProviderConfigMPNCatalog(this, this.mpnCatalogRepository); 
        contentProviderConfigMPNCatalog.read(configXMLManufacturerPartCatalog);
      }  
    setDMSCatalogHierarchy((Collection)getMPNCatalogs());
    this.bCompAutoMapping = paramConfigXMLContentProviderCfg.getComponentCatalogs().isAutoMapping();
    this.bCompAllowMoveParts = paramConfigXMLContentProviderCfg.getComponentCatalogs().isAllowMoveParts();
    this.defaultCompSyncType = Enum.<PropertySyncType>valueOf(PropertySyncType.class, paramConfigXMLContentProviderCfg.getComponentCatalogs().getDefaultSyncType().value());
    if (this.bCompAutoMapping)
      processComponentAutoMappings(); 
    if (paramConfigXMLContentProviderCfg.getComponentCatalogs() != null)
      for (ConfigXMLComponentCatalog configXMLComponentCatalog : paramConfigXMLContentProviderCfg.getComponentCatalogs().getComponentCatalog()) {
        ContentProviderConfigComponentCatalog contentProviderConfigComponentCatalog = getComponentCatalogConfigByDMN(configXMLComponentCatalog.getClassDMN());
        if (contentProviderConfigComponentCatalog == null)
          contentProviderConfigComponentCatalog = new ContentProviderConfigComponentCatalog(this, this.compCatalogRepository); 
        contentProviderConfigComponentCatalog.read(configXMLComponentCatalog);
        if (configXMLComponentCatalog.getManufacturerPartMaps() != null)
          for (ConfigXMLManufacturerPartMap configXMLManufacturerPartMap : configXMLComponentCatalog.getManufacturerPartMaps().getManufacturerPartMap()) {
            List<ContentProviderConfigComponentCatalog> list = this.mpnDMNToComponentCatalogListMap.get(configXMLManufacturerPartMap.getClassDMN());
            if (list == null) {
              list = new ArrayList();
              this.mpnDMNToComponentCatalogListMap.put(configXMLManufacturerPartMap.getClassDMN(), list);
            } 
            boolean bool = false;
            for (ContentProviderConfigComponentCatalog contentProviderConfigComponentCatalog1 : list) {
              if (contentProviderConfigComponentCatalog1 == contentProviderConfigComponentCatalog) {
                bool = true;
                break;
              } 
            } 
            if (!bool)
              list.add(contentProviderConfigComponentCatalog); 
          }  
      }  
    setDMSCatalogHierarchy((Collection)getComponentCatalogs());
    validate();
  }
  
  public void read(OIObjectManager paramOIObjectManager) throws ContentProviderConfigException {
    this.om = paramOIObjectManager;
    OIObject oIObject = getDocumentObject(paramOIObjectManager);
    read(oIObject);
  }
  
  public void read(OIObject paramOIObject) throws ContentProviderConfigException {
    try {
      Date date = paramOIObject.getDate("CCPCfgBlobDate");
      if (this.configDocDate == null || date.compareTo(this.configDocDate) != 0) {
        String str = paramOIObject.getString("CCPCfgJSBlobPath");
        if (str != null && !str.trim().isEmpty()) {
          InputStream inputStream = getMappingJavaScriptFileInputStream(paramOIObject);
          if (inputStream != null)
            this.se.read(inputStream); 
        } 
        ZipInputStream zipInputStream = new ZipInputStream(getConfigFileInputStream(paramOIObject));
        zipInputStream.getNextEntry();
        read(zipInputStream);
        this.configDocDate = date;
      } 
    } catch (Exception exception) {
      throw new ContentProviderConfigException("While reading Configuration object:  " + exception.getMessage());
    } 
  }
  
  private void validate() throws ContentProviderConfigException {
    OIClassManager oIClassManager = this.om.getObjectManagerFactory().getClassManager();
    boolean bool1 = true;
    if (this.idPropertyList.size() == 0) {
      bool1 = false;
      logger.warn("Root PartClass must define one or more ID properties.");
    } 
    boolean bool2 = true;
    byte b = 0;
    for (ContentProviderConfigPartClass contentProviderConfigPartClass1 : getPartClasses()) {
      if (contentProviderConfigPartClass1.getParentContentProviderId() != null && !contentProviderConfigPartClass1.getParentContentProviderId().isEmpty() && contentProviderConfigPartClass1.getParentPartClass() == null) {
        bool1 = false;
        logger.warn("PartClass '" + contentProviderConfigPartClass1.getContentProviderId() + "' has a parent PartClass '" + contentProviderConfigPartClass1.getParentContentProviderId() + "'. which does not exist in the configuration.");
      } 
      if (contentProviderConfigPartClass1.getParentContentProviderId() == null)
        b++; 
      ContentProviderConfigPartClass contentProviderConfigPartClass2 = contentProviderConfigPartClass1.getParentPartClass();
      if (contentProviderConfigPartClass2 != null)
        contentProviderConfigPartClass2.addChildPartClass(contentProviderConfigPartClass1); 
    } 
    if (b != 1) {
      bool1 = false;
      logger.warn("One and only one root PartClass (no parent) must be defined.");
    } 
    for (ContentProviderConfigMPNCatalog contentProviderConfigMPNCatalog : getMPNCatalogs()) {
      OIClass oIClass = oIClassManager.getOIClass(contentProviderConfigMPNCatalog.getClassDMN());
      bool1 = (validateDMSCatalog(oIClass, contentProviderConfigMPNCatalog) && bool1) ? true : false;
    } 
    for (ContentProviderConfigComponentCatalog contentProviderConfigComponentCatalog : getComponentCatalogs()) {
      for (ContentProviderConfigManufacturerPartMap contentProviderConfigManufacturerPartMap : contentProviderConfigComponentCatalog.getManufacturerPartMaps())
        for (AbstractContentProviderConfigManufacturerPartPropertyMap abstractContentProviderConfigManufacturerPartPropertyMap : contentProviderConfigManufacturerPartMap.getCatalogComponentPropertyMaps()); 
    } 
    for (ContentProviderConfigComponentCatalog contentProviderConfigComponentCatalog : getComponentCatalogs()) {
      OIClass oIClass = oIClassManager.getOIClass(contentProviderConfigComponentCatalog.getClassDMN());
      bool1 = (validateDMSCatalog(oIClass, contentProviderConfigComponentCatalog) && bool1) ? true : false;
      for (ContentProviderConfigManufacturerPartMap contentProviderConfigManufacturerPartMap : contentProviderConfigComponentCatalog.getManufacturerPartMaps()) {
        OIClass oIClass1 = oIClassManager.getOIClass(contentProviderConfigManufacturerPartMap.getClassDMN());
        if (oIClass1 == null) {
          bool1 = false;
          logger.warn("ManufacturerPartMap references a classDMN  = '" + contentProviderConfigManufacturerPartMap.getClassDMN() + "' which does not exist in EDM Library.");
          continue;
        } 
        ArrayList<String> arrayList = new ArrayList();
        ArrayList<ContentProviderConfigManufacturerPartPropertyMap> arrayList1 = new ArrayList();
        for (AbstractContentProviderConfigManufacturerPartPropertyMap abstractContentProviderConfigManufacturerPartPropertyMap : contentProviderConfigManufacturerPartMap.getCatalogComponentPropertyMaps()) {
          if (abstractContentProviderConfigManufacturerPartPropertyMap instanceof ContentProviderConfigManufacturerPartPropertyMap) {
            ContentProviderConfigManufacturerPartPropertyMap contentProviderConfigManufacturerPartPropertyMap = (ContentProviderConfigManufacturerPartPropertyMap)abstractContentProviderConfigManufacturerPartPropertyMap;
            if (oIClass1 != null && !oIClass1.hasField(contentProviderConfigManufacturerPartPropertyMap.getManufacturerPartPropertyDMN()))
              if (contentProviderConfigManufacturerPartPropertyMap.isIgnoreMissing()) {
                arrayList1.add(contentProviderConfigManufacturerPartPropertyMap);
              } else {
                bool1 = false;
                String str = "ManufacturerPartPropertyMap with mpnDMN = '" + contentProviderConfigManufacturerPartPropertyMap.getManufacturerPartPropertyDMN() + "' which does not exist in EDM Library.";
                logger.warn("ComponentCatalog for catalog dmn = '" + contentProviderConfigComponentCatalog.getClassDMN() + "' on ManufacturerPartMap catalog dmn = '" + contentProviderConfigManufacturerPartMap.getClassDMN() + ":" + System.getProperty("line.separator") + "    " + str);
                contentProviderConfigManufacturerPartPropertyMap.setValid(false);
                contentProviderConfigManufacturerPartPropertyMap.setErrorText(str);
              }  
            if (oIClass != null) {
              String str = contentProviderConfigManufacturerPartPropertyMap.getComponentPropertyDMN();
              if (!oIClass.hasField(str)) {
                if (contentProviderConfigManufacturerPartPropertyMap.isIgnoreMissing()) {
                  arrayList1.add(contentProviderConfigManufacturerPartPropertyMap);
                  continue;
                } 
                bool1 = false;
                String str1 = "ManufacturerPartPropertyMap with compDMN = '" + str + "' which does not exist in EDM Library.";
                logger.warn("ComponentCatalog for catalog dmn = '" + contentProviderConfigComponentCatalog.getClassDMN() + "' on ManufacturerPartMap catalog dmn = '" + contentProviderConfigManufacturerPartMap.getClassDMN() + ":" + System.getProperty("line.separator") + "    " + str1);
                contentProviderConfigManufacturerPartPropertyMap.setValid(false);
                contentProviderConfigManufacturerPartPropertyMap.setErrorText(str1);
              } 
            } 
            continue;
          } 
          if (this.bValidateScriptedMappings && abstractContentProviderConfigManufacturerPartPropertyMap instanceof ContentProviderConfigScriptedManufacturerPartPropertyMap) {
            ContentProviderConfigScriptedManufacturerPartPropertyMap contentProviderConfigScriptedManufacturerPartPropertyMap = (ContentProviderConfigScriptedManufacturerPartPropertyMap)abstractContentProviderConfigManufacturerPartPropertyMap;
            ContentProviderConfigScriptEngine contentProviderConfigScriptEngine = getScriptEngine();
            try {
              contentProviderConfigScriptEngine.callMappingFunction(oIClass1, oIClass, contentProviderConfigScriptedManufacturerPartPropertyMap.getMappingFunction());
            } catch (ContentProviderConfigException contentProviderConfigException) {
              if (!(contentProviderConfigException instanceof ContentProviderConfigFieldNotFoundException) || contentProviderConfigException instanceof ContentProviderConfigFieldNotFoundException)
                if (contentProviderConfigScriptedManufacturerPartPropertyMap.isIgnoreMissing()) {
                  arrayList1.add(contentProviderConfigScriptedManufacturerPartPropertyMap);
                } else {
                  bool1 = false;
                  String str = contentProviderConfigException.getMessage();
                  logger.warn("ComponentCatalog for catalog dmn = '" + contentProviderConfigComponentCatalog.getClassDMN() + "' on ManufacturerPartMap catalog dmn = '" + contentProviderConfigManufacturerPartMap.getClassDMN() + " :" + System.getProperty("line.separator") + "  Error encountered when validating ScriptedComponentPropertyMap mapping function : " + System.getProperty("line.separator") + " " + str + System.getProperty("line.separator") + "  Script text = '" + contentProviderConfigScriptedManufacturerPartPropertyMap.getMappingFunction() + "'.");
                  contentProviderConfigScriptedManufacturerPartPropertyMap.setValid(false);
                  contentProviderConfigScriptedManufacturerPartPropertyMap.setErrorText(str);
                }  
            } 
            for (String str : contentProviderConfigScriptEngine.getComponentPropertyMap().getMap().keySet()) {
              if (oIClass.hasField(str)) {
                contentProviderConfigScriptedManufacturerPartPropertyMap.addComponentProperty(str);
                arrayList.add(str);
              } 
            } 
          } 
        } 
        if (!this.configMode)
          for (AbstractContentProviderConfigManufacturerPartPropertyMap abstractContentProviderConfigManufacturerPartPropertyMap : arrayList1)
            contentProviderConfigManufacturerPartMap.removeManufacturerPartPropertyMap(abstractContentProviderConfigManufacturerPartPropertyMap);  
        for (String str : arrayList)
          contentProviderConfigManufacturerPartMap.removeManufacturerPartPropertyMap(str); 
      } 
    } 
    if (!bool1) {
      if (this.ccp != null)
        throw new ContentProviderConfigException("Invalid Mapping Configuration for Content Provider '" + this.ccp.getName() + "'."); 
      throw new ContentProviderConfigException("Invalid Mapping Configuration.");
    } 
  }
  
  public boolean validateDMSCatalog(OIClass paramOIClass, ContentProviderConfigDMSCatalog<?> paramContentProviderConfigDMSCatalog) {
    boolean bool = true;
    String str = (paramContentProviderConfigDMSCatalog instanceof ContentProviderConfigMPNCatalog) ? "ManufacturerPartCatalog" : "ComponentCatalog";
    if (paramOIClass == null) {
      bool = false;
      logger.warn(str + " references a classDMN  = '" + str + "' which does not exist in EDM Library.");
    } else {
      OIClass oIClass = paramOIClass.getSuperclass();
      if (oIClass != null && oIClass.getSuperclass() != null && paramContentProviderConfigDMSCatalog.parentDMSCatalog == null) {
        bool = false;
        logger.warn(str + " mapping exists for classDMN = '" + str + "' but parent mapping is missing for classDMN  = '" + paramOIClass.getName() + "'.");
      } 
    } 
    for (ContentProviderConfigContentProviderMap contentProviderConfigContentProviderMap : paramContentProviderConfigDMSCatalog.getContentProviderMaps()) {
      ContentProviderConfigPartClass contentProviderConfigPartClass = contentProviderConfigContentProviderMap.getPartClass();
      if (contentProviderConfigPartClass == null) {
        bool = false;
        logger.warn(str + " references a non-existent PartClass id = '" + str + "'.");
      } 
      for (ContentProviderConfigPropertyMap contentProviderConfigPropertyMap : contentProviderConfigContentProviderMap.getCatalogPropertyMaps()) {
        if (contentProviderConfigPartClass != null) {
          ContentProviderConfigProperty contentProviderConfigProperty = contentProviderConfigPartClass.getClassPropertyByContentProviderId(contentProviderConfigPropertyMap.getContentProviderId());
          if (contentProviderConfigProperty == null) {
            bool = false;
            logger.warn("ContentProviderPropertyMap for " + str + " with dmn = '" + paramContentProviderConfigDMSCatalog.getClassDMN() + "' references a non-existent PartClass '" + contentProviderConfigPartClass.getContentProviderId() + "' property id = '" + contentProviderConfigPropertyMap.getContentProviderId() + "'.");
          } 
        } 
        if (contentProviderConfigPropertyMap.getSyncType() != PropertySyncType.IGNORE && contentProviderConfigPropertyMap.getDMN() == null) {
          bool = false;
          logger.warn("ContentProviderPropertyMap for " + str + " with dmn = '" + paramContentProviderConfigDMSCatalog.getClassDMN() + "' has sync type of '" + String.valueOf(contentProviderConfigPropertyMap.getSyncType()) + "' :  'dmn' attribute must be specified.");
        } 
      } 
    } 
    return bool;
  }
  
  private void processManufacturerPartAutoMappings() throws ContentProviderConfigException {
    logger.info("Auto-mapping Manufacturer Part configuration...");
    try {
      OIClass oIClass = this.om.getObjectManagerFactory().getClassManager().getOIClass("RootManufacturerPart");
      DataModelUtils dataModelUtils = new DataModelUtils(this.om);
      for (ContentProviderConfigPartClass contentProviderConfigPartClass : getPartClasses()) {
        OIClass oIClass1 = dataModelUtils.findDescendentOIClass(oIClass, contentProviderConfigPartClass.getLabelPath(contentProviderConfigPartClass, "."));
        if (oIClass1 == null)
          continue; 
        ContentProviderConfigMPNCatalog contentProviderConfigMPNCatalog = getMPNCatalogConfigByDMN(oIClass1.getName());
        if (contentProviderConfigMPNCatalog == null) {
          contentProviderConfigMPNCatalog = new ContentProviderConfigMPNCatalog(this, this.mpnCatalogRepository);
          contentProviderConfigMPNCatalog.setClassDMN(oIClass1.getName());
          contentProviderConfigMPNCatalog.parentDMSCatalog = contentProviderConfigMPNCatalog;
          this.mpnCatalogRepository.addCatalog(contentProviderConfigMPNCatalog);
        } 
        ContentProviderConfigContentProviderMap contentProviderConfigContentProviderMap = contentProviderConfigMPNCatalog.getContentProviderMapInstance(contentProviderConfigPartClass);
        this.mpnCatalogRepository.addContentProviderReference(contentProviderConfigPartClass.getContentProviderId(), contentProviderConfigMPNCatalog);
        for (ContentProviderConfigProperty contentProviderConfigProperty : contentProviderConfigPartClass.getLeafProperties()) {
          OIField oIField = dataModelUtils.findOIFieldParametric(oIClass1, contentProviderConfigProperty.getContentProviderLabel());
          if (oIField == null) {
            String[] arrayOfString = contentProviderConfigProperty.getContentProviderId().split("\\.");
            if (arrayOfString.length > 1)
              oIField = dataModelUtils.findOIFieldParametric(oIClass1, arrayOfString[1]); 
          } 
          if (oIField == null)
            continue; 
          ContentProviderConfigPropertyMap contentProviderConfigPropertyMap = new ContentProviderConfigPropertyMap(contentProviderConfigContentProviderMap);
          contentProviderConfigPropertyMap.setContentProviderId(contentProviderConfigProperty.getContentProviderId());
          contentProviderConfigPropertyMap.setDMN(oIField.getName());
          contentProviderConfigPropertyMap.setSyncType(this.defaultMPNSyncType);
          contentProviderConfigPropertyMap.setInherit(this.bDefaultInherit);
          contentProviderConfigContentProviderMap.addContentProviderPropertyMap(contentProviderConfigPropertyMap);
        } 
      } 
    } catch (Exception exception) {
      throw new ContentProviderConfigException("Unable to load Manufacturer Part auto mappings: " + exception.getMessage());
    } 
  }
  
  private void processComponentAutoMappings() throws ContentProviderConfigException {
    logger.info("Auto-mapping Component configuration...");
    try {
      OIClass oIClass1 = this.om.getObjectManagerFactory().getClassManager().getOIClass("RootComponent");
      OIClass oIClass2 = this.om.getObjectManagerFactory().getClassManager().getOIClass("RootManufacturerPart");
      DataModelUtils dataModelUtils = new DataModelUtils(this.om);
      for (ContentProviderConfigPartClass contentProviderConfigPartClass : getPartClasses()) {
        OIClass oIClass3 = dataModelUtils.findDescendentOIClass(oIClass1, contentProviderConfigPartClass.getLabelPath("."));
        if (oIClass3 == null)
          continue; 
        ContentProviderConfigComponentCatalog contentProviderConfigComponentCatalog = getComponentCatalogConfigByDMN(oIClass3.getName());
        if (contentProviderConfigComponentCatalog == null) {
          contentProviderConfigComponentCatalog = new ContentProviderConfigComponentCatalog(this, this.compCatalogRepository);
          contentProviderConfigComponentCatalog.setClassDMN(oIClass3.getName());
          this.compCatalogRepository.addCatalog(contentProviderConfigComponentCatalog);
        } 
        ContentProviderConfigContentProviderMap contentProviderConfigContentProviderMap = contentProviderConfigComponentCatalog.getContentProviderMapInstance(contentProviderConfigPartClass);
        this.compCatalogRepository.addContentProviderReference(contentProviderConfigPartClass.getContentProviderId(), contentProviderConfigComponentCatalog);
        ContentProviderConfigMPNCatalog contentProviderConfigMPNCatalog = null;
        ContentProviderConfigManufacturerPartMap contentProviderConfigManufacturerPartMap = null;
        OIClass oIClass4 = dataModelUtils.findDescendentOIClass(oIClass2, contentProviderConfigPartClass.getLabelPath("."));
        if (oIClass4 != null) {
          contentProviderConfigMPNCatalog = getMPNCatalogConfigByDMN(oIClass4.getName());
          if (contentProviderConfigMPNCatalog != null) {
            contentProviderConfigManufacturerPartMap = new ContentProviderConfigManufacturerPartMap(contentProviderConfigComponentCatalog);
            contentProviderConfigManufacturerPartMap.setClassDMN(contentProviderConfigMPNCatalog.getClassDMN());
            contentProviderConfigComponentCatalog.addManufacturerPartMap(contentProviderConfigManufacturerPartMap);
            List<ContentProviderConfigComponentCatalog> list = this.mpnDMNToComponentCatalogListMap.get(oIClass4.getName());
            if (list == null) {
              list = new ArrayList();
              this.mpnDMNToComponentCatalogListMap.put(oIClass4.getName(), list);
            } 
            list.add(contentProviderConfigComponentCatalog);
          } 
        } 
        for (ContentProviderConfigProperty contentProviderConfigProperty : contentProviderConfigPartClass.getLeafProperties()) {
          OIField oIField = dataModelUtils.findOIFieldParametric(oIClass3, contentProviderConfigProperty.getContentProviderLabel());
          if (oIField == null) {
            String[] arrayOfString = contentProviderConfigProperty.getContentProviderId().split("\\.");
            if (arrayOfString.length > 1)
              oIField = dataModelUtils.findOIFieldParametric(oIClass3, arrayOfString[1]); 
          } 
          if (oIField == null)
            continue; 
          if (contentProviderConfigProperty.isSearchable()) {
            ContentProviderConfigPropertyMap contentProviderConfigPropertyMap = new ContentProviderConfigPropertyMap(contentProviderConfigContentProviderMap);
            contentProviderConfigPropertyMap.setContentProviderId(contentProviderConfigProperty.getContentProviderId());
            contentProviderConfigPropertyMap.setDMN(oIField.getName());
            contentProviderConfigPropertyMap.setInherit(this.bDefaultInherit);
            contentProviderConfigContentProviderMap.addContentProviderPropertyMap(contentProviderConfigPropertyMap);
          } 
          if (oIClass3.getSubclasses().isEmpty() && contentProviderConfigManufacturerPartMap != null) {
            ContentProviderConfigContentProviderMap contentProviderConfigContentProviderMap1 = contentProviderConfigMPNCatalog.getContentProviderMap();
            if (contentProviderConfigContentProviderMap1 != null) {
              ContentProviderConfigPropertyMap contentProviderConfigPropertyMap = contentProviderConfigContentProviderMap1.getLeafPropertyMapByContentProviderId(contentProviderConfigProperty.getContentProviderId());
              if (contentProviderConfigPropertyMap != null) {
                ContentProviderConfigManufacturerPartPropertyMap contentProviderConfigManufacturerPartPropertyMap = new ContentProviderConfigManufacturerPartPropertyMap(contentProviderConfigManufacturerPartMap);
                contentProviderConfigManufacturerPartPropertyMap.setAutoMapped(true);
                contentProviderConfigManufacturerPartPropertyMap.setComponentPropertyDMN(oIField.getName());
                contentProviderConfigManufacturerPartPropertyMap.setManufacturerPartPropertyDMN(contentProviderConfigPropertyMap.getDMN());
                contentProviderConfigManufacturerPartPropertyMap.setSyncType(this.defaultCompSyncType);
                contentProviderConfigManufacturerPartMap.addManufacturerPartPropertyMap(contentProviderConfigManufacturerPartPropertyMap);
              } 
            } 
          } 
        } 
      } 
    } catch (Exception exception) {
      throw new ContentProviderConfigException("Unable to load Manufacturer Part auto mappings: " + exception.getMessage());
    } 
  }
  
  private void setDMSCatalogHierarchy(Collection<? extends ContentProviderConfigDMSCatalog<?>> paramCollection) {
    OIClassManager oIClassManager = this.om.getObjectManagerFactory().getClassManager();
    for (ContentProviderConfigDMSCatalog<?> contentProviderConfigDMSCatalog : paramCollection) {
      OIClass oIClass = oIClassManager.getOIClass(contentProviderConfigDMSCatalog.getClassDMN());
      setDMSCatalogHierarchy(oIClass, contentProviderConfigDMSCatalog);
    } 
  }
  
  private void setDMSCatalogHierarchy(OIClass paramOIClass, ContentProviderConfigDMSCatalog<?> paramContentProviderConfigDMSCatalog) {
    if (paramOIClass == null)
      return; 
    OIClass oIClass = paramOIClass.getSuperclass();
    if (oIClass != null) {
      ContentProviderConfigComponentCatalog contentProviderConfigComponentCatalog;
      if (paramContentProviderConfigDMSCatalog instanceof ContentProviderConfigMPNCatalog) {
        ContentProviderConfigMPNCatalog contentProviderConfigMPNCatalog = getMPNCatalogConfigByDMN(oIClass.getName());
      } else {
        contentProviderConfigComponentCatalog = getComponentCatalogConfigByDMN(oIClass.getName());
      } 
      paramContentProviderConfigDMSCatalog.parentDMSCatalog = contentProviderConfigComponentCatalog;
    } 
  }
  
  public void setConfigMode(boolean paramBoolean) {
    this.configMode = paramBoolean;
  }
  
  public boolean getConfigMode() {
    return this.configMode;
  }
  
  public boolean isValidateScriptedMappings() {
    return this.bValidateScriptedMappings;
  }
  
  public void setValidateScriptedMappings(boolean paramBoolean) {
    this.bValidateScriptedMappings = paramBoolean;
  }
  
  public static void main(String[] paramArrayOfString) {
    OIObjectManagerFactory oIObjectManagerFactory = null;
    OIObjectManager oIObjectManager = null;
    try {
      OIAuthenticate oIAuthenticate = OIAuthenticateFactory.createBatchAuthenticate("amazon");
      oIObjectManagerFactory = oIAuthenticate.login("Test App");
      System.out.println("Connected");
      oIObjectManager = oIObjectManagerFactory.createObjectManager();
      ContentProviderConfig contentProviderConfig = new ContentProviderConfig(null);
      contentProviderConfig.setObjectManager(oIObjectManager);
      try {
        ContentProviderFactory.getInstance().registerContentProviders(oIObjectManager);
        AbstractContentProvider abstractContentProvider = ContentProviderFactory.getInstance().createContentProvider("SE");
        System.out.println("Reading mapping functions script...");
        contentProviderConfig.se.read(new File("D:/Consulting/IP/DemoContentProviderImpl/SiliconExpert/MappingFunctions.js"));
        System.out.println("Reading mapping configuration...");
        contentProviderConfig.read(new FileInputStream(new File("D:\\Consulting\\Customers\\Amazon\\ContentProvider\\mapping\\SiliconExpertMapping.xml")));
        System.out.println("Configuration read successfully!");
      } catch (ContentProviderConfigException contentProviderConfigException) {
        logger.warn(contentProviderConfigException);
      } 
      for (ContentProviderConfigMPNCatalog contentProviderConfigMPNCatalog : contentProviderConfig.getMPNCatalogs()) {
        System.out.println("Catalog: " + contentProviderConfigMPNCatalog.getClassDMN());
        for (ContentProviderConfigContentProviderMap contentProviderConfigContentProviderMap : contentProviderConfigMPNCatalog.getContentProviderMaps()) {
          System.out.println("  Map: " + contentProviderConfigContentProviderMap.getDMSCatalog().getClassDMN() + " : " + String.valueOf(contentProviderConfigContentProviderMap.getDefaultSyncType()));
          for (ContentProviderConfigPropertyMap contentProviderConfigPropertyMap : contentProviderConfigContentProviderMap.getLeafPropertyMaps())
            System.out.println("    PropMap: " + contentProviderConfigPropertyMap.getDMN() + " : " + String.valueOf(contentProviderConfigPropertyMap.getSyncType())); 
        } 
      } 
      for (ContentProviderConfigComponentCatalog contentProviderConfigComponentCatalog : contentProviderConfig.getComponentCatalogs()) {
        System.out.println("Catalog: " + contentProviderConfigComponentCatalog.getClassDMN());
        for (ContentProviderConfigManufacturerPartMap contentProviderConfigManufacturerPartMap : contentProviderConfigComponentCatalog.getManufacturerPartMaps()) {
          System.out.println("  Map: " + contentProviderConfigManufacturerPartMap.getClassDMN() + " : " + String.valueOf(contentProviderConfigManufacturerPartMap.getDefaultSyncType()));
          for (AbstractContentProviderConfigManufacturerPartPropertyMap abstractContentProviderConfigManufacturerPartPropertyMap : contentProviderConfigManufacturerPartMap.getLeafComponentPropertyMaps()) {
            if (abstractContentProviderConfigManufacturerPartPropertyMap instanceof ContentProviderConfigManufacturerPartPropertyMap) {
              ContentProviderConfigManufacturerPartPropertyMap contentProviderConfigManufacturerPartPropertyMap = (ContentProviderConfigManufacturerPartPropertyMap)abstractContentProviderConfigManufacturerPartPropertyMap;
              System.out.println("    PropMap: " + contentProviderConfigManufacturerPartPropertyMap.getComponentPropertyDMN() + " : " + String.valueOf(contentProviderConfigManufacturerPartPropertyMap.getSyncType()));
              continue;
            } 
            if (abstractContentProviderConfigManufacturerPartPropertyMap instanceof ContentProviderConfigScriptedManufacturerPartPropertyMap) {
              ContentProviderConfigScriptedManufacturerPartPropertyMap contentProviderConfigScriptedManufacturerPartPropertyMap = (ContentProviderConfigScriptedManufacturerPartPropertyMap)abstractContentProviderConfigManufacturerPartPropertyMap;
              System.out.println("    Scripted PropMap: " + contentProviderConfigScriptedManufacturerPartPropertyMap.getMappingFunction() + " : " + String.valueOf(contentProviderConfigScriptedManufacturerPartPropertyMap.getSyncType()));
            } 
          } 
        } 
      } 
      return;
    } catch (Exception exception) {
      exception.printStackTrace();
      return;
    } finally {
      if (oIObjectManager != null) {
        System.out.println("Disconnecting...");
        oIObjectManager.close();
      } 
      if (oIObjectManagerFactory != null)
        oIObjectManagerFactory.close(); 
    } 
  }
  
  public enum PropertySyncType {
    DIRECT, SYNC, IGNORE, CREATEONLY;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\config\ContentProviderConfig.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */