package com.mentor.dms.contentprovider.sf.response.searchcapabilities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.Map;

public class PropertySearchCapability {
  public static final String OP_EQUALITY = "Equality";
  
  public static final String OP_ANY_OF = "AnyOf";
  
  private static final String OP_RANGE = "Range";
  
  public static final String OP_RANGE_INC = "Range_inclusive";
  
  public static final String OP_RANGE_EXC = "Range_exclusive";
  
  public static final String OP_PATTERN = "PatternMatch";
  
  public static final String OP_SMART_MATCH = "SmartMatch";
  
  public static final String OP_ANY_OF_SMART_MATCH = "AnyOfSmartMatch";
  
  @SerializedName("searchPropertyId")
  @Expose
  public String searchPropertyId;
  
  @SerializedName("supportedPredicateOperators")
  @Expose
  public List<Object> supportedPredicateOperators;
  
  @SerializedName("queryCapabilities")
  @Expose
  public List<String> queryCapabilities;
  
  public boolean isSearchable() {
    return this.queryCapabilities.contains("Filter");
  }
  
  public boolean canUseFacet() {
    return this.queryCapabilities.contains("Facet");
  }
  
  public boolean isSupportOperator(String paramString) {
    boolean bool = false;
    if (paramString.equals("Range_inclusive")) {
      paramString = "Range";
      bool = true;
    } 
    if (paramString.equals("Range_exclusive")) {
      paramString = "Range";
      bool = false;
    } 
    for (Map map1 : this.supportedPredicateOperators) {
      Map map2 = map1;
      Object object = map2.get("__operator__");
      if (object.equals(paramString)) {
        if (paramString.equals("Range")) {
          List list = (List)map2.get("leftBoundaryFlags");
          return bool ? list.contains("Inclusive") : list.contains("exclusive");
        } 
        return true;
      } 
    } 
    return false;
  }
  
  public String getSearchPropertyId() {
    return this.searchPropertyId;
  }
  
  public List<Object> getSupportedPredicateOperators() {
    return this.supportedPredicateOperators;
  }
  
  public List<String> getQueryCapabilities() {
    return this.queryCapabilities;
  }
  
  public void setSearchPropertyId(String paramString) {
    this.searchPropertyId = paramString;
  }
  
  public void setSupportedPredicateOperators(List<Object> paramList) {
    this.supportedPredicateOperators = paramList;
  }
  
  public void setQueryCapabilities(List<String> paramList) {
    this.queryCapabilities = paramList;
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (!(paramObject instanceof PropertySearchCapability))
      return false; 
    PropertySearchCapability propertySearchCapability = (PropertySearchCapability)paramObject;
    if (!propertySearchCapability.canEqual(this))
      return false; 
    String str1 = getSearchPropertyId();
    String str2 = propertySearchCapability.getSearchPropertyId();
    if ((str1 == null) ? (str2 != null) : !str1.equals(str2))
      return false; 
    List<Object> list1 = getSupportedPredicateOperators();
    List<Object> list2 = propertySearchCapability.getSupportedPredicateOperators();
    if ((list1 == null) ? (list2 != null) : !list1.equals(list2))
      return false; 
    List<String> list3 = getQueryCapabilities();
    List<String> list4 = propertySearchCapability.getQueryCapabilities();
    return !((list3 == null) ? (list4 != null) : !list3.equals(list4));
  }
  
  protected boolean canEqual(Object paramObject) {
    return paramObject instanceof PropertySearchCapability;
  }
  
  public int hashCode() {
    null = 1;
    String str = getSearchPropertyId();
    null = null * 59 + ((str == null) ? 43 : str.hashCode());
    List<Object> list = getSupportedPredicateOperators();
    null = null * 59 + ((list == null) ? 43 : list.hashCode());
    List<String> list1 = getQueryCapabilities();
    return null * 59 + ((list1 == null) ? 43 : list1.hashCode());
  }
  
  public String toString() {
    return "PropertySearchCapability(searchPropertyId=" + getSearchPropertyId() + ", supportedPredicateOperators=" + String.valueOf(getSupportedPredicateOperators()) + ", queryCapabilities=" + String.valueOf(getQueryCapabilities()) + ")";
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\response\searchcapabilities\PropertySearchCapability.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */