package com.mentor.dms.contentprovider.config.xml;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"property"})
@XmlRootElement(name = "Properties")
public class ConfigXMLProperties extends BaseXMLClass {
  @XmlElement(name = "Property")
  protected List<ConfigXMLProperty> property;
  
  public List<ConfigXMLProperty> getProperty() {
    if (this.property == null)
      this.property = new ArrayList<>(); 
    return this.property;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\config\xml\ConfigXMLProperties.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */