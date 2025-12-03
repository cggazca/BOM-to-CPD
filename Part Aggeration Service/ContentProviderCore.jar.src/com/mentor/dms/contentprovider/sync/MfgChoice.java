package com.mentor.dms.contentprovider.sync;

import com.mentor.datafusion.oi.OIException;
import com.mentor.datafusion.oi.type.OIProxyObject;
import com.mentor.datafusion.utils.logger.MGLogger;

public class MfgChoice {
  static MGLogger logger = MGLogger.getLogger(MfgChoice.class);
  
  private OIProxyObject obj;
  
  public MfgChoice(OIProxyObject paramOIProxyObject) {
    this.obj = paramOIProxyObject;
  }
  
  public OIProxyObject getOIProxyObject() {
    return this.obj;
  }
  
  public String toString() {
    try {
      String str = this.obj.getString("ManufacturerId") + " : " + this.obj.getString("ManufacturerId");
      if (this.obj.getOIClass().hasField("ECMfgNameAlias"))
        str = str + " (" + str + ")"; 
      return str;
    } catch (OIException oIException) {
      logger.error(oIException.getMessage());
      return "<Error>";
    } 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\sync\MfgChoice.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */