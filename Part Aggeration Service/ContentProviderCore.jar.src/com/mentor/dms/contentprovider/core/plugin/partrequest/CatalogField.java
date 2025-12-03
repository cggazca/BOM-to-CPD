package com.mentor.dms.contentprovider.core.plugin.partrequest;

import com.mentor.datafusion.oi.model.OIField;
import javax.swing.JTextField;

public class CatalogField {
  JTextField textfield;
  
  String label;
  
  String domainName;
  
  String refClassDomainName = null;
  
  int maxLength = 0;
  
  int precision = 0;
  
  OIField.Type fieldType = OIField.Type.STRING;
  
  public JTextField getTextfield() {
    return this.textfield;
  }
  
  public String getInputValue() {
    return this.textfield.getText();
  }
  
  public String getLabel() {
    return this.label;
  }
  
  public String getDomainName() {
    return this.domainName;
  }
  
  public String getRefClassDomainName() {
    return this.refClassDomainName;
  }
  
  public OIField.Type getFieldType() {
    return this.fieldType;
  }
  
  public int getMaxLength() {
    return this.maxLength;
  }
  
  public int getPrecision() {
    return this.precision;
  }
  
  public boolean isObjectRefefence() {
    return (this.refClassDomainName != null);
  }
  
  public void setFieldType(int paramInt) {
    if (paramInt == 1) {
      this.fieldType = OIField.Type.INTEGER;
    } else if (paramInt == 2) {
      this.fieldType = OIField.Type.DOUBLE;
    } else if (paramInt == 3) {
      this.fieldType = OIField.Type.STRING;
    } else if (paramInt == 4) {
      this.fieldType = OIField.Type.INTEGER;
    } else if (paramInt == 5) {
      this.fieldType = OIField.Type.DATE;
    } else if (paramInt == 6) {
      this.fieldType = OIField.Type.BLOB;
    } 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\plugin\partrequest\CatalogField.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */