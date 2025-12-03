package com.mentor.dms.contentprovider.sf;

import com.mentor.datafusion.oi.OIClassManager;
import com.mentor.datafusion.oi.OIException;
import com.mentor.datafusion.oi.OIObjectManager;
import com.mentor.datafusion.oi.interceptor.InterceptorFactory;
import com.mentor.datafusion.oi.model.OIClass;
import com.mentor.datafusion.oi.type.OIObject;
import com.mentor.datafusion.oi.type.OIObjectSet;
import com.mentor.datafusion.utils.logger.MGLogger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class ComponentHeightInterceptorFactory implements InterceptorFactory {
  private static MGLogger log = MGLogger.getLogger(ComponentHeightInterceptorFactory.class);
  
  protected static boolean bCompHeightExists = false;
  
  protected static boolean bCompHeightUpdateExists = false;
  
  protected static boolean bCompHeightChecked = false;
  
  protected boolean updateComponentHeight(OIObject paramOIObject) throws OIException {
    if (!isComponentHeightDataModelLoaded(paramOIObject.getObjectManager()) || (bCompHeightUpdateExists && paramOIObject.getString("seComponentHeightUpdate").equals("Disable")))
      return false; 
    Double double_1 = paramOIObject.getDouble("seComponentHeight");
    String str1 = paramOIObject.getStringifiedWithUnit("seComponentHeight");
    Double double_2 = Double.valueOf(0.0D);
    String str2 = null;
    OIObjectSet oIObjectSet = paramOIObject.getSet("ApprovedManufacturerList");
    for (OIObject oIObject1 : oIObjectSet) {
      OIObject oIObject2 = oIObject1.getObject("MfgPartNumber");
      String str = null;
      if (oIObject2.getOIClass().hasField("seProductDepth"))
        str = oIObject2.getString("seProductDepth"); 
      if (oIObject2.getOIClass().hasField("sePackageHeight"))
        str = oIObject2.getString("sePackageHeight"); 
      if (str == null)
        continue; 
      str = str.trim();
      if (str.isEmpty() || str.startsWith("N/R") || str.startsWith("N/A"))
        continue; 
      Pattern pattern1 = Pattern.compile("^([0-9\\.]+)(?:\\(Max\\))*(?:.*?)([A-Za-z]*)$");
      Pattern pattern2 = Pattern.compile("^([0-9\\.]+)(?:\\(Min\\))*(?:.*?)([A-Za-z]*)$");
      Matcher matcher = pattern1.matcher(str);
      if (!matcher.find()) {
        matcher = pattern2.matcher(str);
        if (!matcher.find()) {
          log.warn("Warning:  Unrecognized format for Component height '" + str + "' for Component '" + paramOIObject.getObjectID() + "'.");
          continue;
        } 
      } 
      str = matcher.group(1) + matcher.group(1);
      try {
        paramOIObject.setWithUnit("seComponentHeight", str);
      } catch (OIException oIException) {
        log.warn("Warning:  Unrecognized format for Component height '" + str + "' for Component '" + paramOIObject.getObjectID() + ": " + oIException.getMessage());
        continue;
      } 
      double d = paramOIObject.getDouble("seComponentHeight").doubleValue();
      if (d > double_2.doubleValue()) {
        double_2 = Double.valueOf(d);
        str2 = str;
      } 
    } 
    if (double_1 == null || !double_2.equals(double_1)) {
      if (double_2.doubleValue() > 0.0D) {
        paramOIObject.set("seComponentHeight", double_2);
        log.info("Updating Component height for Component '" + paramOIObject.getObjectID() + "' from '" + str1 + "' to '" + str2 + "'.");
        return true;
      } 
    } else {
      paramOIObject.set("seComponentHeight", double_1);
      return false;
    } 
    return false;
  }
  
  protected boolean isComponentHeightDataModelLoaded(OIObjectManager paramOIObjectManager) {
    if (!bCompHeightChecked) {
      OIClassManager oIClassManager = paramOIObjectManager.getObjectManagerFactory().getClassManager();
      OIClass oIClass = oIClassManager.getOIClass("Component");
      bCompHeightExists = oIClass.hasField("seComponentHeight");
      bCompHeightUpdateExists = oIClass.hasField("seComponentHeightUpdate");
      bCompHeightChecked = true;
    } 
    return bCompHeightExists;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\ComponentHeightInterceptorFactory.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */