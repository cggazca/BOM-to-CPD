package com.mentor.dms.contentprovider.core.config.datamodel;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"mpnArgument", "stringArgument"})
@XmlRootElement(name = "Arguments")
public class DataModelCfgArguments {
  @XmlElement(name = "MPNArgument", required = true)
  protected List<DataModelCfgMPNArgument> mpnArgument;
  
  @XmlElement(name = "StringArgument")
  protected List<DataModelCfgStringArgument> stringArgument;
  
  public List<DataModelCfgMPNArgument> getMPNArgument() {
    if (this.mpnArgument == null)
      this.mpnArgument = new ArrayList<>(); 
    return this.mpnArgument;
  }
  
  public List<DataModelCfgStringArgument> getStringArgument() {
    if (this.stringArgument == null)
      this.stringArgument = new ArrayList<>(); 
    return this.stringArgument;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\config\datamodel\DataModelCfgArguments.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */