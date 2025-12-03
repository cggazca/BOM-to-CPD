package com.mentor.dms.contentprovider.client.catalogselector;

import com.mentor.datafusion.oi.OIException;
import com.mentor.datafusion.oi.type.OIBitSet;
import com.mentor.datafusion.oi.type.OIObject;

public class CatalogNodeInfo extends AbstractNodeInfo {
  private static final int NOOBJECTSPERM = 0;
  
  private static final int NOSUBCATALOGSPERM = 1;
  
  private String id;
  
  private String parentKey;
  
  private String dmn;
  
  private boolean bNoObjectsPerm;
  
  public CatalogNodeInfo(OIObject paramOIObject) throws OIException {
    super(paramOIObject.getString("CatalogTitle"));
    this.id = paramOIObject.getString("CatalogGroup");
    this.parentKey = paramOIObject.getString("ParentKey");
    this.dmn = paramOIObject.getString("DomainModelName");
    OIBitSet oIBitSet = paramOIObject.getBitSet("CatalogStatus");
    this.bNoObjectsPerm = oIBitSet.get(0);
  }
  
  public String getID() {
    return this.id;
  }
  
  public String getParentKey() {
    return this.parentKey;
  }
  
  public String getDMN() {
    return this.dmn;
  }
  
  public boolean isNoObjectsPerm() {
    return this.bNoObjectsPerm;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\client\catalogselector\CatalogNodeInfo.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */