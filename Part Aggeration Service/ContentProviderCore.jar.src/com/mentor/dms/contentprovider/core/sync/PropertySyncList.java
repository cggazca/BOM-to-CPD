package com.mentor.dms.contentprovider.core.sync;

import com.mentor.datafusion.oi.OIException;
import com.mentor.datafusion.oi.type.OIObject;
import com.mentor.datafusion.oi.type.OIObjectSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PropertySyncList {
  private OIObject obj;
  
  private ArrayList<PropertySyncListLine> propertySyncList;
  
  public PropertySyncList(OIObject paramOIObject) throws OIException {
    this.obj = paramOIObject;
    this.propertySyncList = new ArrayList<>();
    OIObjectSet oIObjectSet = paramOIObject.getSet("ECPropSyncList");
    for (OIObject oIObject : oIObjectSet) {
      PropertySyncListLine propertySyncListLine = new PropertySyncListLine(oIObject);
      if (propertySyncListLine.getPropId().matches(".+_[0-9]+$"))
        continue; 
      if (propertySyncListLine.needMerging())
        propertySyncListLine = merge(propertySyncListLine); 
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
  
  private PropertySyncListLine merge(PropertySyncListLine paramPropertySyncListLine) throws OIException {
    String str = paramPropertySyncListLine.getPropId();
    HashMap<Object, Object> hashMap = new HashMap<>();
    OIObjectSet oIObjectSet = this.obj.getSet("ECPropSyncList");
    for (OIObject oIObject : oIObjectSet) {
      String str1 = oIObject.getStringified("ECPropID");
      if (str1.startsWith(str)) {
        propertyValue propertyValue = new propertyValue(oIObject.getStringified("ECPropValue"), oIObject.getStringified("ECPrevPropValue"));
        hashMap.put(str1, propertyValue);
      } 
    } 
    Pattern pattern = Pattern.compile("_(\\d+)$");
    ArrayList<?> arrayList = new ArrayList(hashMap.keySet());
    Collections.sort(arrayList, (paramString1, paramString2) -> {
          Matcher matcher1 = paramPattern.matcher(paramString1);
          Matcher matcher2 = paramPattern.matcher(paramString2);
          if (!matcher1.find())
            return -1; 
          if (!matcher2.find())
            return 1; 
          int i = Integer.parseInt(matcher1.group(1));
          int j = Integer.parseInt(matcher2.group(1));
          return Integer.compare(i, j);
        });
    StringBuilder stringBuilder1 = new StringBuilder();
    StringBuilder stringBuilder2 = new StringBuilder();
    for (Object object : arrayList) {
      stringBuilder1.append(((propertyValue)hashMap.get(object)).propertyValue);
      stringBuilder2.append(((propertyValue)hashMap.get(object)).previousValue);
    } 
    paramPropertySyncListLine.setPropValue(stringBuilder1.toString());
    paramPropertySyncListLine.setPropPrevValue(stringBuilder2.toString());
    return paramPropertySyncListLine;
  }
  
  private class propertyValue {
    String propertyValue;
    
    String previousValue;
    
    public propertyValue(String param1String1, String param1String2) {
      this.propertyValue = param1String1;
      this.previousValue = param1String2;
    }
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\sync\PropertySyncList.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */