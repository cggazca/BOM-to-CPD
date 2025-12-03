package com.mentor.dms.contentprovider.core.utils.setrepmpn;

import com.mentor.dms.contentprovider.core.utils.setrepmpn.config.SetRepMPNCfgBaseFieldDataType;
import com.mentor.dms.contentprovider.core.utils.setrepmpn.config.SetRepMPNCfgBaseFieldType;

public class ComponentAML extends AbstractEDMObject {
  private ManufacturerPart mpn = null;
  
  private Component comp = null;
  
  private String repMPN = null;
  
  ComponentAML(Component paramComponent, String paramString1, String paramString2) {
    super(paramString1);
    this.comp = paramComponent;
    this.mpn = new ManufacturerPart(this, paramString1);
    this.repMPN = paramString2;
  }
  
  public ManufacturerPart getManufacturerPart() {
    return this.mpn;
  }
  
  public Component getComponent() {
    return this.comp;
  }
  
  public String getRepMPN() {
    return this.repMPN;
  }
  
  public Object getAMLProperty(String paramString, boolean paramBoolean) throws SetRepMPNException {
    return paramBoolean ? getProperty(paramString) : this.mpn.getProperty(paramString);
  }
  
  public PropertyValue getAMLProperty(SetRepMPNCfgBaseFieldDataType paramSetRepMPNCfgBaseFieldDataType) throws SetRepMPNException {
    return paramSetRepMPNCfgBaseFieldDataType.isAmlProperty() ? getProperty(paramSetRepMPNCfgBaseFieldDataType) : this.mpn.getProperty(paramSetRepMPNCfgBaseFieldDataType);
  }
  
  public PropertyValue getAMLProperty(SetRepMPNCfgBaseFieldType paramSetRepMPNCfgBaseFieldType) throws SetRepMPNException {
    return (paramSetRepMPNCfgBaseFieldType instanceof SetRepMPNCfgBaseFieldDataType) ? getAMLProperty((SetRepMPNCfgBaseFieldDataType)paramSetRepMPNCfgBaseFieldType) : (paramSetRepMPNCfgBaseFieldType.isAmlProperty() ? getProperty(paramSetRepMPNCfgBaseFieldType.getDmn()) : this.mpn.getProperty(paramSetRepMPNCfgBaseFieldType.getDmn()));
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\cor\\utils\setrepmpn\ComponentAML.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */