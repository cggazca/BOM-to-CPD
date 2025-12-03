package com.mentor.dms.contentprovider.config.xml;

import jakarta.xml.bind.Unmarshaller;

public abstract class BaseXMLClass extends Unmarshaller.Listener {
  protected Object parent;
  
  public void afterUnmarshal(Unmarshaller paramUnmarshaller, Object paramObject) {
    this.parent = paramObject;
  }
  
  public Object getParent() {
    return this.parent;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\config\xml\BaseXMLClass.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */