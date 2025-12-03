package com.mentor.dms.contentprovider.core.sync;

import com.mentor.datafusion.oi.OIException;
import com.mentor.datafusion.oi.type.OIObject;
import com.mentor.datafusion.oi.type.OIObjectSet;

public class ProviderRefListLine {
  private boolean bInclude = true;
  
  private String ccpId;
  
  private String propId;
  
  private String propValue;
  
  public ProviderRefListLine() {}
  
  public ProviderRefListLine(OIObject paramOIObject) throws OIException {
    this.ccpId = paramOIObject.getStringified("ECProviderReferenceID");
    this.propId = paramOIObject.getStringified("ECProviderReferenceKey");
    this.propValue = paramOIObject.getStringified("ECProviderReferenceValue");
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
  
  public void addToDMSList(OIObject paramOIObject) throws OIException {
    OIObjectSet oIObjectSet = paramOIObject.getSet("ECProviderReferences");
    OIObject oIObject = oIObjectSet.createLine();
    oIObject.set("ECProviderReferenceID", this.ccpId);
    oIObject.set("ECProviderReferenceKey", this.propId);
    oIObject.set("ECProviderReferenceValue", this.propValue);
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\sync\ProviderRefListLine.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */