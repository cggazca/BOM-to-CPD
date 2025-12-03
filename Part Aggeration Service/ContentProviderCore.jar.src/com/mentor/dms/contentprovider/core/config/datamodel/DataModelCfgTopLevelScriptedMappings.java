package com.mentor.dms.contentprovider.core.config.datamodel;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"topLevelScriptedMapping"})
@XmlRootElement(name = "TopLevelScriptedMappings")
public class DataModelCfgTopLevelScriptedMappings {
  @XmlElement(name = "TopLevelScriptedMapping")
  protected List<DataModelCfgTopLevelScriptedMapping> topLevelScriptedMapping;
  
  public List<DataModelCfgTopLevelScriptedMapping> getTopLevelScriptedMapping() {
    if (this.topLevelScriptedMapping == null)
      this.topLevelScriptedMapping = new ArrayList<>(); 
    return this.topLevelScriptedMapping;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\config\datamodel\DataModelCfgTopLevelScriptedMappings.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */