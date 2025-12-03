package com.mentor.dms.contentprovider.sf.datamodel;

import java.util.Collection;
import java.util.Collections;

public class ProductLine extends AbstractTaxonomyNode {
  public SubCategory subCategory = null;
  
  public Collection<AbstractTaxonomyNode> getChildren() {
    return Collections.emptyList();
  }
  
  public AbstractTaxonomyNode getParent() {
    return this.subCategory;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\datamodel\ProductLine.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */