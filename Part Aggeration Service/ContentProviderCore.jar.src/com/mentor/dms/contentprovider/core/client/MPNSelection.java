package com.mentor.dms.contentprovider.core.client;

import com.mentor.datafusion.oi.OIException;
import com.mentor.datafusion.oi.type.OIObject;
import com.mentor.dms.contentprovider.core.ContentProviderException;

class MPNSelection {
  private OIObject mpnObj = null;
  
  private String displayString = "";
  
  public MPNSelection(OIObject paramOIObject, boolean paramBoolean) throws OIException, ContentProviderException {
    this.mpnObj = paramOIObject.getObject("MfgPartNumber");
    if (paramBoolean)
      this.displayString = "*** "; 
    this.displayString = this.displayString + this.displayString + " (" + this.mpnObj.getString("PartNumber") + ") - " + this.mpnObj.getString("ManufacturerName");
  }
  
  public OIObject getMPNObject() {
    return this.mpnObj;
  }
  
  public String toString() {
    return this.displayString;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\client\MPNSelection.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */