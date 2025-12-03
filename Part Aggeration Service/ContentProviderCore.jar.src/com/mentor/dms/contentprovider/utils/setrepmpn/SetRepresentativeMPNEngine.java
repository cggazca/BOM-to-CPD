package com.mentor.dms.contentprovider.utils.setrepmpn;

import com.mentor.datafusion.oi.OICursor;
import com.mentor.datafusion.oi.OIException;
import com.mentor.datafusion.oi.OIObjectManager;
import com.mentor.datafusion.oi.OIQuery;
import com.mentor.datafusion.oi.type.OIObject;
import com.mentor.datafusion.oi.type.OIObjectSet;
import com.mentor.datafusion.oi.type.OIProxyObject;
import com.mentor.dms.contentprovider.utils.setrepmpn.config.SetRepMPNCfgBaseFieldType;
import com.mentor.dms.contentprovider.utils.setrepmpn.config.SetRepMPNCfgFavoredManufacturerPart;
import com.mentor.dms.contentprovider.utils.setrepmpn.config.SetRepMPNCfgPriorityValueType;
import com.mentor.dms.contentprovider.utils.setrepmpn.config.SetRepMPNCfgPriorityValues;
import com.mentor.dms.contentprovider.utils.setrepmpn.config.SetRepMPNCfgRestriction;
import com.mentor.dms.contentprovider.utils.setrepmpn.config.SetRepMPNCfgRollupValueType;
import com.mentor.dms.contentprovider.utils.setrepmpn.config.SetRepMPNCfgSetRepMPNCfg;
import com.mentor.dms.contentprovider.utils.setrepmpn.config.SetRepMPNCfgValues;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.TreeMap;
import java.util.TreeSet;
import org.apache.log4j.Logger;

public class SetRepresentativeMPNEngine {
  static Logger logger = Logger.getLogger(SetRepresentativeMPNEngine.class);
  
  private static boolean bDebugMode = false;
  
  private static boolean bInterceptorMode = false;
  
  private static String mpnPathPrefix = "ApprovedManufacturerList.MfgPartNumber.";
  
  private static String amlPathPrefix = "ApprovedManufacturerList.";
  
  private String partNumberSearch = "";
  
  protected void setDebugMode(boolean paramBoolean) {
    bDebugMode = paramBoolean;
  }
  
  protected void setInterceptorPartNumbers(Collection<String> paramCollection) {
    bInterceptorMode = true;
    this.partNumberSearch = "";
    for (String str : paramCollection) {
      if (!this.partNumberSearch.isEmpty())
        this.partNumberSearch += "|"; 
      this.partNumberSearch += this.partNumberSearch;
    } 
  }
  
  protected Collection<OIObject> run(OIObjectManager paramOIObjectManager, SetRepMPNCfgSetRepMPNCfg paramSetRepMPNCfgSetRepMPNCfg) {
    byte b1 = 0;
    byte b2 = 0;
    ArrayList<OIObject> arrayList = new ArrayList();
    try {
      logger.info("Querying Components...");
      OIQuery oIQuery = paramOIObjectManager.createQuery("Component", true);
      if (bInterceptorMode)
        oIQuery.addRestriction("PartNumber", this.partNumberSearch); 
      if (paramSetRepMPNCfgSetRepMPNCfg.getComponentRestrictions() != null && paramSetRepMPNCfgSetRepMPNCfg.getComponentRestrictions().getRestriction() != null)
        for (SetRepMPNCfgRestriction setRepMPNCfgRestriction : paramSetRepMPNCfgSetRepMPNCfg.getComponentRestrictions().getRestriction()) {
          if (bInterceptorMode && setRepMPNCfgRestriction.getDmn().equals("PartNumber"))
            continue; 
          logger.debug("Adding Component restriction : " + setRepMPNCfgRestriction.getDmn() + " = '" + setRepMPNCfgRestriction.getValue() + "'.");
          oIQuery.addRestriction(setRepMPNCfgRestriction.getDmn(), setRepMPNCfgRestriction.getValue());
        }  
      if (paramSetRepMPNCfgSetRepMPNCfg.getComponentAMLRestrictions() != null && paramSetRepMPNCfgSetRepMPNCfg.getComponentAMLRestrictions().getRestriction() != null)
        for (SetRepMPNCfgRestriction setRepMPNCfgRestriction : paramSetRepMPNCfgSetRepMPNCfg.getComponentAMLRestrictions().getRestriction()) {
          logger.debug("Adding Component AML restriction : " + amlPathPrefix + setRepMPNCfgRestriction.getDmn() + " = '" + setRepMPNCfgRestriction.getValue() + "'.");
          oIQuery.addRestriction(amlPathPrefix + amlPathPrefix, setRepMPNCfgRestriction.getValue());
        }  
      if (paramSetRepMPNCfgSetRepMPNCfg.getManufacturerPartRestrictions() != null && paramSetRepMPNCfgSetRepMPNCfg.getManufacturerPartRestrictions().getRestriction() != null)
        for (SetRepMPNCfgRestriction setRepMPNCfgRestriction : paramSetRepMPNCfgSetRepMPNCfg.getManufacturerPartRestrictions().getRestriction()) {
          logger.debug("Adding Manufacturer Part restriction : " + mpnPathPrefix + setRepMPNCfgRestriction.getDmn() + " = '" + setRepMPNCfgRestriction.getValue() + "'.");
          oIQuery.addRestriction(mpnPathPrefix + mpnPathPrefix, setRepMPNCfgRestriction.getValue());
        }  
      oIQuery.addColumn("PartNumber");
      oIQuery.addAlias("PartNumber", getCompAlias("PartNumber"));
      oIQuery.addColumn("ApprovedManufacturerList.MfgPartNumber");
      oIQuery.addColumn("ApprovedManufacturerList.ECRepresentativeMPN");
      oIQuery.addSortBy("PartNumber", true);
      if (paramSetRepMPNCfgSetRepMPNCfg.getFavoredManufacturerPart() != null && paramSetRepMPNCfgSetRepMPNCfg.getFavoredManufacturerPart().getSelectionCriteria() != null)
        for (SetRepMPNCfgBaseFieldType setRepMPNCfgBaseFieldType : paramSetRepMPNCfgSetRepMPNCfg.getFavoredManufacturerPart().getSelectionCriteria().getPrioritySelectionCriteriaOrAggregationSelectionCriteriaOrCustomSelectionCriteria()) {
          String str = setRepMPNCfgBaseFieldType.getDmn();
          addMPNColumn(oIQuery, setRepMPNCfgBaseFieldType.isAmlProperty(), setRepMPNCfgBaseFieldType.getDmn());
        }  
      TreeMap<Object, Object> treeMap = new TreeMap<>();
      OICursor oICursor = oIQuery.execute();
      while (oICursor.next()) {
        OIProxyObject oIProxyObject = oICursor.getProxyObject();
        String str = oICursor.getString(getCompAlias("PartNumber"));
        Component component = (Component)treeMap.get(str);
        if (component == null) {
          component = new Component(str, oIProxyObject);
          treeMap.put(str, component);
        } 
        if (oICursor.get("MfgPartNumber") == null)
          continue; 
        ComponentAML componentAML = component.addAML(oICursor.getStringified("MfgPartNumber"), oICursor.getString("ECRepresentativeMPN"));
        if (paramSetRepMPNCfgSetRepMPNCfg.getFavoredManufacturerPart() != null && paramSetRepMPNCfgSetRepMPNCfg.getFavoredManufacturerPart().getSelectionCriteria() != null)
          for (SetRepMPNCfgBaseFieldType setRepMPNCfgBaseFieldType : paramSetRepMPNCfgSetRepMPNCfg.getFavoredManufacturerPart().getSelectionCriteria().getPrioritySelectionCriteriaOrAggregationSelectionCriteriaOrCustomSelectionCriteria())
            addMPNProp(componentAML, oICursor, setRepMPNCfgBaseFieldType.isAmlProperty(), setRepMPNCfgBaseFieldType.getDmn());  
      } 
      oICursor.close();
      logger.info("Processing Representative MPNs...");
      byte b = 1;
      for (Component component : treeMap.values()) {
        if (paramSetRepMPNCfgSetRepMPNCfg.getComponentRestrictions().isIgnoreChildComps() && component.getId().contains("|"))
          continue; 
        if (component.getAmlList().isEmpty()) {
          logger.info("   Component '" + component.getId() + "' AML is empty.  Skipping...");
          continue;
        } 
        logger.info("  " + b++ + ": Component: " + component.getId());
        boolean bool = false;
        try {
          ComponentAML componentAML = getFavoredMPN(component, paramSetRepMPNCfgSetRepMPNCfg.getFavoredManufacturerPart());
          boolean bool1 = false;
          byte b3 = 0;
          for (ComponentAML componentAML1 : component.getAmlList()) {
            if (componentAML1.getRepMPN().equals("Y"))
              b3++; 
            if (b3 > 1) {
              bool1 = true;
              break;
            } 
          } 
          if (b3 == 0)
            bool1 = true; 
          if (!bool1)
            for (ComponentAML componentAML1 : component.getAmlList()) {
              if (componentAML1.getId().equals(componentAML.getId()) && !componentAML1.getRepMPN().equals("Y")) {
                bool1 = true;
                break;
              } 
            }  
          if (bool1) {
            logger.info("     Updating Component '" + component.getId() + "' with representative Manufacturer Part '" + componentAML.getId() + "'...");
            paramOIObjectManager.refreshAndLockObject(component.getOIObject());
            OIObjectSet oIObjectSet = component.getOIObject().getSet("ApprovedManufacturerList");
            for (OIObject oIObject : oIObjectSet) {
              if (oIObject.getStringified("MfgPartNumber").equals(componentAML.getId())) {
                oIObject.set("ECRepresentativeMPN", "Y");
                continue;
              } 
              oIObject.set("ECRepresentativeMPN", "N");
            } 
            bool = true;
          } 
          if (bool) {
            if (!bDebugMode)
              if (!bInterceptorMode) {
                logger.info("     Committing updates...");
                paramOIObjectManager.makePermanent(component.getOIObject());
              } else {
                arrayList.add(component.getOIObject());
              }  
            b1++;
          } 
          if (bool) {
            if (!bInterceptorMode)
              paramOIObjectManager.evict(component.getOIObject()); 
            continue;
          } 
        } catch (Exception exception) {
          logger.error(exception);
          b2++;
          if (bool) {
            if (!bInterceptorMode)
              paramOIObjectManager.evict(component.getOIObject()); 
            continue;
          } 
        } finally {
          if (bool) {
            if (!bInterceptorMode)
              paramOIObjectManager.evict(component.getOIObject()); 
          } else {
            logger.info("     No updates required.");
          } 
        } 
      } 
    } catch (Exception exception) {
      exception.printStackTrace();
    } 
    logger.info("#");
    if (!bDebugMode) {
      logger.info("# Number of Components updated: " + b1);
    } else {
      logger.info("# Number of Components that require updating: " + b1);
    } 
    logger.info("# Number of errors: " + b2);
    if (!bInterceptorMode)
      logger.info("# Set Representative MPN completed at: " + String.valueOf(new Date())); 
    return arrayList;
  }
  
  private void addMPNProp(ComponentAML paramComponentAML, OICursor paramOICursor, boolean paramBoolean, String paramString) throws OIException {
    Object object = paramOICursor.get(getMPNAlias(paramBoolean, paramString));
    if (paramBoolean) {
      paramComponentAML.addProperty(paramString, object);
    } else {
      paramComponentAML.getManufacturerPart().addProperty(paramString, object);
    } 
  }
  
  private void addMPNColumn(OIQuery paramOIQuery, boolean paramBoolean, String paramString) throws OIException {
    String str1 = getAccessPath(paramBoolean, paramString);
    String str2 = getMPNAlias(str1);
    logger.debug("Adding MPN column : " + str1 + " (" + str2 + ")");
    paramOIQuery.addColumn(str1);
    paramOIQuery.addAlias(str1, str2);
  }
  
  private String getAccessPath(boolean paramBoolean, String paramString) {
    return (paramBoolean ? amlPathPrefix : mpnPathPrefix) + (paramBoolean ? amlPathPrefix : mpnPathPrefix);
  }
  
  private String getMPNAlias(boolean paramBoolean, String paramString) {
    String str = getAccessPath(paramBoolean, paramString);
    return getMPNAlias(str);
  }
  
  private String getMPNAlias(String paramString) {
    return paramString.replace(".", "_");
  }
  
  private String getCompAlias(String paramString) {
    return "COMP_" + paramString;
  }
  
  public ComponentAML getFavoredMPN(Component paramComponent, SetRepMPNCfgFavoredManufacturerPart paramSetRepMPNCfgFavoredManufacturerPart) {
    if (paramComponent.getAmlList().size() == 1)
      return paramComponent.getAmlList().iterator().next(); 
    TreeSet<ComponentAML> treeSet = new TreeSet(new FavoredMPNComparator(paramSetRepMPNCfgFavoredManufacturerPart));
    for (ComponentAML componentAML : paramComponent.getAmlList())
      treeSet.add(componentAML); 
    return treeSet.first();
  }
  
  static int compareLikeObjects(Object paramObject1, Object paramObject2) {
    return (paramObject1 == null && paramObject2 == null) ? 0 : ((paramObject1 != null && paramObject2 == null) ? 1 : ((paramObject1 == null && paramObject2 != null) ? -1 : ((paramObject1 instanceof Double) ? ((Double)paramObject1).compareTo((Double)paramObject2) : ((paramObject1 instanceof Integer) ? ((Integer)paramObject1).compareTo((Integer)paramObject2) : ((paramObject1 instanceof String) ? ((String)paramObject1).compareTo((String)paramObject2) : ((paramObject1 instanceof Date) ? ((Date)paramObject1).compareTo((Date)paramObject2) : 0))))));
  }
  
  static Integer priorityLookup(Object paramObject, SetRepMPNCfgPriorityValues paramSetRepMPNCfgPriorityValues) {
    int i = Integer.MAX_VALUE;
    if (paramObject == null)
      return Integer.valueOf(i); 
    byte b = 0;
    for (SetRepMPNCfgPriorityValueType setRepMPNCfgPriorityValueType : paramSetRepMPNCfgPriorityValues.getPriorityValue()) {
      if (setRepMPNCfgPriorityValueType.isRegex()) {
        if (paramObject.toString().matches(setRepMPNCfgPriorityValueType.getValue())) {
          i = b;
          break;
        } 
      } else if (paramObject.toString().equals(setRepMPNCfgPriorityValueType.getValue())) {
        i = b;
        break;
      } 
      b++;
    } 
    return Integer.valueOf(i);
  }
  
  static Integer priorityLookup(Object paramObject, SetRepMPNCfgValues paramSetRepMPNCfgValues) {
    int i = Integer.MAX_VALUE;
    if (paramObject == null)
      return Integer.valueOf(i); 
    byte b = 0;
    for (SetRepMPNCfgRollupValueType setRepMPNCfgRollupValueType : paramSetRepMPNCfgValues.getValue()) {
      if (setRepMPNCfgRollupValueType.isRegex()) {
        if (paramObject.toString().matches(setRepMPNCfgRollupValueType.getValue())) {
          i = b;
          break;
        } 
      } else if (paramObject.toString().equals(setRepMPNCfgRollupValueType.getValue())) {
        i = b;
        break;
      } 
      b++;
    } 
    return Integer.valueOf(i);
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovide\\utils\setrepmpn\SetRepresentativeMPNEngine.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */