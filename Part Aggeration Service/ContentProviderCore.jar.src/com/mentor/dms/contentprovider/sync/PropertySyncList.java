package com.mentor.dms.contentprovider.sync;

import com.mentor.datafusion.oi.OIException;
import com.mentor.datafusion.oi.type.OIObject;
import com.mentor.datafusion.oi.type.OIObjectSet;
import java.util.ArrayList;
import java.util.Collection;

public class PropertySyncList {
  private OIObject obj;
  
  private ArrayList<PropertySyncListLine> propertySyncList;
  
  public PropertySyncList(OIObject paramOIObject) throws OIException {
    this.obj = paramOIObject;
    this.propertySyncList = new ArrayList<>();
    OIObjectSet oIObjectSet = paramOIObject.getSet("ECPropSyncList");
    for (OIObject oIObject : oIObjectSet) {
      PropertySyncListLine propertySyncListLine = new PropertySyncListLine(oIObject);
      this.propertySyncList.add(propertySyncListLine);
    } 
  }
  
  public void addPropertySyncListLine(PropertySyncListLine paramPropertySyncListLine) {
    this.propertySyncList.add(paramPropertySyncListLine);
  }
  
  public void clearDMSList() throws OIException {
    OIObjectSet oIObjectSet = this.obj.getSet("ECPropSyncList");
    oIObjectSet.clear();
  }
  
  public void addIncludedToDMSList() throws OIException {
    for (PropertySyncListLine propertySyncListLine : getPropertySyncListLines()) {
      if (propertySyncListLine.isIncluded())
        propertySyncListLine.addToDMSList(this.obj); 
    } 
  }
  
  public Collection<PropertySyncListLine> getPropertySyncListLines() {
    return this.propertySyncList;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\sync\PropertySyncList.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */