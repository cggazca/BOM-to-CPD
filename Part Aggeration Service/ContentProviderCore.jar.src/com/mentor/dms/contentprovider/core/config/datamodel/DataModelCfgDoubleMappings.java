package com.mentor.dms.contentprovider.core.config.datamodel;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"doubleMapping"})
@XmlRootElement(name = "DoubleMappings")
public class DataModelCfgDoubleMappings {
  @XmlElement(name = "DoubleMapping")
  protected List<DataModelCfgDoubleMapping> doubleMapping;
  
  public List<DataModelCfgDoubleMapping> getDoubleMapping() {
    if (this.doubleMapping == null)
      this.doubleMapping = new ArrayList<>(); 
    return this.doubleMapping;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\config\datamodel\DataModelCfgDoubleMappings.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */