package com.mentor.dms.contentprovider;

import com.mentor.datafusion.oi.OICursor;
import com.mentor.datafusion.oi.OIQuery;
import com.mentor.datafusion.oi.type.OIObject;
import java.util.HashMap;
import java.util.List;
import javax.swing.table.AbstractTableModel;

class null extends AbstractTableModel {
  public int getColumnCount() {
    return 1;
  }
  
  public String getColumnName(int paramInt) {
    return "Property Label";
  }
  
  public int getRowCount() {
    return objectList.size();
  }
  
  public Object getValueAt(int paramInt1, int paramInt2) {
    try {
      OIObject oIObject = objectList.get(paramInt1);
      if (oIObject != null) {
        String str1 = oIObject.getString("ECProviderID");
        if (!PropertyLabelCustomListFrameContrib.propLabelMap.containsKey(str1)) {
          HashMap<Object, Object> hashMap1 = new HashMap<>(111);
          PropertyLabelCustomListFrameContrib.propLabelMap.put(str1, hashMap1);
          OIQuery oIQuery = object.getObjectManager().createQuery("ToolsContentProviderCfgs", true);
          oIQuery.addRestriction("MetaDataMap.Key", "PROVIDER_ID");
          oIQuery.addRestriction("MetaDataMap.Value", str1);
          oIQuery.addRestriction("Status", "A");
          oIQuery.addColumn("ECPropList.ECPropId");
          oIQuery.addColumn("ECPropList.ECPropName");
          OICursor oICursor = oIQuery.execute();
          while (oICursor.next())
            hashMap1.put(oICursor.getString("ECPropId"), oICursor.getString("ECPropName")); 
          oICursor.close();
        } 
        HashMap hashMap = PropertyLabelCustomListFrameContrib.propLabelMap.get(str1);
        String str2 = oIObject.getString("ECPropID");
        String str3 = (String)hashMap.get(str2);
        if (str3 == null)
          str3 = "*** Undefined ***"; 
        return str3;
      } 
    } catch (Exception exception) {
      PropertyLabelCustomListFrameContrib.this.log.error("Error at position " + paramInt1 + "," + paramInt2 + " of list " + PropertyLabelCustomListFrameContrib.this.mListName, exception);
    } 
    return "Error";
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\PropertyLabelCustomListFrameContrib$1.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */