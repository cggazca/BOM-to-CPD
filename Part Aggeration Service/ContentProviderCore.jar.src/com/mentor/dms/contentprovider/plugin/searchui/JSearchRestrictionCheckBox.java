package com.mentor.dms.contentprovider.plugin.searchui;

import com.mentor.datafusion.oi.model.OIField;
import javax.swing.JCheckBox;

public class JSearchRestrictionCheckBox extends JCheckBox {
  private OIField<?> oiField;
  
  public JSearchRestrictionCheckBox(OIField<?> paramOIField) {
    this.oiField = paramOIField;
  }
  
  public OIField<?> getOIField() {
    return this.oiField;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\plugin\searchui\JSearchRestrictionCheckBox.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */