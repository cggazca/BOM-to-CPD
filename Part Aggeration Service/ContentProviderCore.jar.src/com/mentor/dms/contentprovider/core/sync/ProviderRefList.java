package com.mentor.dms.contentprovider.core.sync;

import com.mentor.datafusion.oi.OIException;
import com.mentor.datafusion.oi.type.OIObject;
import com.mentor.datafusion.oi.type.OIObjectSet;
import java.util.ArrayList;
import java.util.Collection;

public class ProviderRefList {
  private OIObject obj;
  
  private ArrayList<ProviderRefListLine> providerRefList;
  
  public ProviderRefList(OIObject paramOIObject) throws OIException {
    this.obj = paramOIObject;
    this.providerRefList = new ArrayList<>();
    OIObjectSet oIObjectSet = paramOIObject.getSet("ECProviderReferences");
    for (OIObject oIObject : oIObjectSet) {
      ProviderRefListLine providerRefListLine = new ProviderRefListLine(oIObject);
      this.providerRefList.add(providerRefListLine);
    } 
  }
  
  public void clearDMSList() throws OIException {
    OIObjectSet oIObjectSet = this.obj.getSet("ECProviderReferences");
    oIObjectSet.clear();
  }
  
  public void addIncludedToDMSList() throws OIException {
    for (ProviderRefListLine providerRefListLine : getProviderRefListLines()) {
      if (providerRefListLine.isIncluded())
        providerRefListLine.addToDMSList(this.obj); 
    } 
  }
  
  public Collection<ProviderRefListLine> getProviderRefListLines() {
    return this.providerRefList;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\sync\ProviderRefList.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */