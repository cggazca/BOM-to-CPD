package com.mentor.dms.contentprovider.sync;

import com.mentor.datafusion.oi.OIException;
import com.mentor.datafusion.oi.type.OIObject;
import com.mentor.datafusion.oi.type.OIObjectSet;

public class PropertySyncListLine {
  private boolean bInclude = true;
  
  private String ccpId;
  
  private String propId;
  
  private String propValue;
  
  private String propPrevValue;
  
  private String reconcileAction;
  
  public PropertySyncListLine() {}
  
  public PropertySyncListLine(OIObject paramOIObject) throws OIException {
    this.ccpId = paramOIObject.getStringified("ECProviderID");
    this.propId = paramOIObject.getStringified("ECPropID");
    this.propValue = paramOIObject.getStringified("ECPropValue");
    this.propPrevValue = paramOIObject.getStringified("ECPrevPropValue");
    this.reconcileAction = paramOIObject.getStringified("ECPropReconcileAction");
  }
  
  public void setInclude(boolean paramBoolean) {
    this.bInclude = paramBoolean;
  }
  
  public boolean isIncluded() {
    return this.bInclude;
  }
  
  public void setContentProviderId(String paramString) {
    this.ccpId = paramString;
  }
  
  public String getContentProviderId() {
    return this.ccpId;
  }
  
  public void setPropId(String paramString) {
    this.propId = paramString;
  }
  
  public String getPropId() {
    return this.propId;
  }
  
  public void setPropValue(String paramString) {
    this.propValue = paramString;
  }
  
  public String getPropValue() {
    return this.propValue;
  }
  
  public void setPropPrevValue(String paramString) {
    this.propPrevValue = paramString;
  }
  
  public String getPropPrevValue() {
    return this.propPrevValue;
  }
  
  public void setReconcileAction(String paramString) {
    this.reconcileAction = paramString;
  }
  
  public String getReconcileAction() {
    return this.reconcileAction;
  }
  
  public void addToDMSList(OIObject paramOIObject) throws OIException {
    OIObjectSet oIObjectSet = paramOIObject.getSet("ECPropSyncList");
    OIObject oIObject = oIObjectSet.createLine();
    oIObject.set("ECProviderID", this.ccpId);
    oIObject.set("ECPropID", this.propId);
    oIObject.set("ECPropValue", this.propValue);
    oIObject.set("ECPrevPropValue", this.propPrevValue);
    if (this.reconcileAction != null)
      oIObject.set("ECPropReconcileAction", this.reconcileAction); 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\sync\PropertySyncListLine.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */