package com.mentor.dms.contentprovider.core.utils.setrepmpn;

import com.mentor.datafusion.oi.OIException;
import com.mentor.datafusion.oi.type.OIObject;
import com.mentor.datafusion.oi.type.OIProxyObject;
import com.mentor.dms.contentprovider.core.utils.setrepmpn.config.SetRepMPNCfgBaseFieldType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.TreeSet;

public class Component extends AbstractEDMObject {
  private OIProxyObject oiProxyObject;
  
  private OIObject oiObject = null;
  
  private TreeSet<ComponentAML> amlSet = null;
  
  Component(String paramString, OIProxyObject paramOIProxyObject) {
    super(paramString);
    this.oiProxyObject = paramOIProxyObject;
    this.amlSet = new TreeSet<>(new Comparator<ComponentAML>() {
          public int compare(ComponentAML param1ComponentAML1, ComponentAML param1ComponentAML2) {
            return param1ComponentAML1.getId().compareTo(param1ComponentAML2.getId());
          }
        });
  }
  
  public OIProxyObject getOIProxyObject() {
    return this.oiProxyObject;
  }
  
  public OIObject getOIObject() throws OIException {
    if (this.oiObject == null)
      this.oiObject = this.oiProxyObject.getObject(); 
    return this.oiObject;
  }
  
  public Collection<ComponentAML> getAmlList() {
    return this.amlSet;
  }
  
  public ComponentAML addAML(String paramString1, String paramString2) {
    ComponentAML componentAML = new ComponentAML(this, paramString1, paramString2);
    this.amlSet.add(componentAML);
    return componentAML;
  }
  
  public Collection<PropertyValue> getMPNValues(SetRepMPNCfgBaseFieldType paramSetRepMPNCfgBaseFieldType) throws SetRepMPNException {
    ArrayList<PropertyValue> arrayList = new ArrayList();
    for (ComponentAML componentAML : this.amlSet) {
      PropertyValue propertyValue = componentAML.getAMLProperty(paramSetRepMPNCfgBaseFieldType);
      if (propertyValue.getValue() != null)
        arrayList.add(propertyValue); 
    } 
    return arrayList;
  }
  
  public TreeSet<PropertyValue> getSortedMPNValues(SetRepMPNCfgBaseFieldType paramSetRepMPNCfgBaseFieldType) throws SetRepMPNException {
    TreeSet<PropertyValue> treeSet = new TreeSet();
    for (ComponentAML componentAML : this.amlSet) {
      PropertyValue propertyValue = componentAML.getAMLProperty(paramSetRepMPNCfgBaseFieldType);
      if (propertyValue.getValue() != null)
        treeSet.add(propertyValue); 
    } 
    return treeSet;
  }
  
  public TreeSet<PropertyValue> getSortedMPNValues(SetRepMPNCfgBaseFieldType paramSetRepMPNCfgBaseFieldType, Comparator<PropertyValue> paramComparator) throws SetRepMPNException {
    TreeSet<PropertyValue> treeSet = new TreeSet<>(paramComparator);
    for (ComponentAML componentAML : this.amlSet) {
      PropertyValue propertyValue = componentAML.getAMLProperty(paramSetRepMPNCfgBaseFieldType);
      if (propertyValue.getValue() != null)
        treeSet.add(propertyValue); 
    } 
    return treeSet;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\cor\\utils\setrepmpn\Component.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */