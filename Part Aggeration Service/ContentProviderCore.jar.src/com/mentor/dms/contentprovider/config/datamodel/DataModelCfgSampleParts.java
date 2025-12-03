package com.mentor.dms.contentprovider.config.datamodel;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"samplePart"})
@XmlRootElement(name = "SampleParts")
public class DataModelCfgSampleParts {
  @XmlElement(name = "SamplePart")
  protected List<DataModelCfgSamplePart> samplePart;
  
  public List<DataModelCfgSamplePart> getSamplePart() {
    if (this.samplePart == null)
      this.samplePart = new ArrayList<>(); 
    return this.samplePart;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\config\datamodel\DataModelCfgSampleParts.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */