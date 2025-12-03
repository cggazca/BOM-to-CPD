package com.mentor.dms.contentprovider.core.sync;

import com.mentor.datafusion.oi.OIException;
import com.mentor.datafusion.oi.type.OIObject;
import com.mentor.datafusion.oi.type.OIObjectSet;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class PropertySyncListLine {
  private static final int MAX_LENGTH = 220;
  
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
    if (this.propValue.length() > 220) {
      List<String> list1 = splitByLength(this.propValue, 220);
      List<String> list2 = splitByLength(this.propPrevValue, 220);
      if (list2.size() < list1.size()) {
        int i = list1.size() - list2.size();
        for (byte b1 = 0; b1 < i; b1++)
          list2.add(null); 
      } 
      for (byte b = 0; b < list1.size(); b++) {
        OIObject oIObject = oIObjectSet.createLine();
        oIObject.set("ECProviderID", this.ccpId);
        if (b == 0) {
          oIObject.set("ECPropID", this.propId);
        } else {
          oIObject.set("ECPropID", this.propId + "_" + this.propId);
        } 
        oIObject.set("ECPropValue", list1.get(b));
        oIObject.set("ECPrevPropValue", list2.get(b));
        if (this.reconcileAction != null)
          oIObject.set("ECPropReconcileAction", this.reconcileAction); 
      } 
    } else {
      OIObject oIObject = oIObjectSet.createLine();
      oIObject.set("ECProviderID", this.ccpId);
      oIObject.set("ECPropID", this.propId);
      oIObject.set("ECPropValue", this.propValue);
      oIObject.set("ECPrevPropValue", this.propPrevValue);
      if (this.reconcileAction != null)
        oIObject.set("ECPropReconcileAction", this.reconcileAction); 
    } 
  }
  
  boolean needMerging() {
    return (this.propValue.length() >= 220);
  }
  
  private List<String> splitByLength(String paramString, int paramInt) {
    ArrayList<String> arrayList = new ArrayList();
    int i;
    for (i = 0; i < StringUtils.length(paramString); i += paramInt)
      arrayList.add(StringUtils.substring(paramString, i, i + paramInt)); 
    return arrayList;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\sync\PropertySyncListLine.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */