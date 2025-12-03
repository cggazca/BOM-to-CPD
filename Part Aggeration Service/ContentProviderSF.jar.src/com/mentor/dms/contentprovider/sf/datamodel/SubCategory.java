package com.mentor.dms.contentprovider.sf.datamodel;

import java.util.Collection;
import java.util.TreeMap;

public class SubCategory extends AbstractTaxonomyNode {
  public TreeMap<String, ProductLine> productLineMap = new TreeMap<>();
  
  public MainCategory mainCategory = null;
  
  boolean bSearchableAsBase = false;
  
  public Collection<? extends AbstractTaxonomyNode> getChildren() {
    return this.productLineMap.values();
  }
  
  public AbstractTaxonomyNode getParent() {
    return this.mainCategory;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\datamodel\SubCategory.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */