package com.mentor.dms.contentprovider.sf.datamodel;

import java.util.Collection;
import java.util.HashMap;
import java.util.TreeMap;

public abstract class AbstractTaxonomyNode {
  protected final int DMN_LENGTH = 80;
  
  protected final int TITLE_LENGTH = 80;
  
  private static HashMap<String, Integer> dmnMap = new HashMap<>();
  
  private String taxonomyID = null;
  
  private String dmsCatalogID = null;
  
  private String name = null;
  
  private String dmn = null;
  
  private String currChildKey = null;
  
  public TreeMap<String, FeatureInstance> featureMap = new TreeMap<>();
  
  public abstract Collection<? extends AbstractTaxonomyNode> getChildren();
  
  public abstract AbstractTaxonomyNode getParent();
  
  public AbstractTaxonomyNode() {}
  
  public AbstractTaxonomyNode(String paramString) {
    this.dmsCatalogID = paramString;
  }
  
  public void setTaxomomyID(String paramString) {
    this.taxonomyID = paramString;
  }
  
  public String getTaxonomyID() {
    return this.taxonomyID;
  }
  
  public void setName(String paramString) {
    this.name = paramString;
    if (paramString.length() > 80)
      System.err.println("Length of catalog title name '" + paramString + "' is greater than 80"); 
  }
  
  public String getName() {
    return this.name;
  }
  
  public String getNameOrID() {
    return (this.name != null) ? this.name : this.taxonomyID;
  }
  
  public String getIDOrName() {
    return (this.taxonomyID != null) ? this.taxonomyID : this.name;
  }
  
  public String getIDPath() {
    if (getParent() == null || getParent().getParent() == null)
      return getIDOrName(); 
    String str = getIDOrName();
    for (AbstractTaxonomyNode abstractTaxonomyNode = getParent(); abstractTaxonomyNode.getParent().getParent() != null; abstractTaxonomyNode = abstractTaxonomyNode.getParent())
      str = abstractTaxonomyNode.getIDOrName() + "." + abstractTaxonomyNode.getIDOrName(); 
    return str;
  }
  
  public String getNamePath() {
    if (getParent() == null || getParent().getParent() == null)
      return getName(); 
    String str = getName();
    for (AbstractTaxonomyNode abstractTaxonomyNode = getParent(); abstractTaxonomyNode.getParent() != null; abstractTaxonomyNode = abstractTaxonomyNode.getParent())
      str = abstractTaxonomyNode.getName() + "." + abstractTaxonomyNode.getName(); 
    return str;
  }
  
  public String getDMSCatalogID() {
    if (this.dmsCatalogID == null)
      this.dmsCatalogID = getParent().getDMSCatalogID() + getParent().getDMSCatalogID(); 
    return this.dmsCatalogID;
  }
  
  public String getDMN() {
    if (this.dmn == null)
      this.dmn = getUniqueDMN(EDMClassMode.getCatDMNPrefix() + EDMClassMode.getCatDMNPrefix()); 
    if (this.dmn.length() > 80)
      System.err.println("Length of catalog domain name '" + this.dmn + "' is greater than 80"); 
    return this.dmn;
  }
  
  private String getUniqueDMN(String paramString) {
    String str = paramString;
    int i = 0;
    if (dmnMap.containsKey(paramString)) {
      i = ((Integer)dmnMap.get(paramString)).intValue();
      str = paramString + paramString;
    } 
    dmnMap.put(paramString, Integer.valueOf(i));
    return str;
  }
  
  private String incrementCatalogKey() {
    if ((getParent()).currChildKey == null) {
      (getParent()).currChildKey = "AA";
      return (getParent()).currChildKey;
    } 
    StringBuilder stringBuilder = new StringBuilder((getParent()).currChildKey);
    char c = (getParent()).currChildKey.charAt((getParent()).currChildKey.length() - 1);
    if (c == 'z') {
      c = 'A';
      char c1 = (getParent()).currChildKey.charAt((getParent()).currChildKey.length() - 2);
      if (c1 == 'Z') {
        c1 = 'a';
      } else {
        c1 = (char)(c1 + 1);
      } 
      stringBuilder.setCharAt((getParent()).currChildKey.length() - 2, c1);
    } else if (c == 'Z') {
      c = 'a';
    } else {
      c = (char)(c + 1);
    } 
    stringBuilder.setCharAt((getParent()).currChildKey.length() - 1, c);
    (getParent()).currChildKey = stringBuilder.toString();
    return (getParent()).currChildKey;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\datamodel\AbstractTaxonomyNode.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */