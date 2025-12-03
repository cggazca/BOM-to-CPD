package com.mentor.dms.contentprovider.sf.datamodel;

import java.util.Collection;
import java.util.TreeMap;

public class Taxonomy extends AbstractTaxonomyNode {
  public Taxonomy parent = null;
  
  public TreeMap<String, MainCategory> mainCategoryMap = new TreeMap<>();
  
  public Taxonomy() {}
  
  public Taxonomy(String paramString) {
    super(paramString);
  }
  
  public Collection<? extends AbstractTaxonomyNode> getChildren() {
    return this.mainCategoryMap.values();
  }
  
  public AbstractTaxonomyNode getParent() {
    return this.parent;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\datamodel\Taxonomy.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */