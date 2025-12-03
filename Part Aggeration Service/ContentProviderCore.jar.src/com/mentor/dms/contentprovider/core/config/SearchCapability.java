package com.mentor.dms.contentprovider.core.config;

public class SearchCapability {
  private boolean searchable = false;
  
  private boolean facet = false;
  
  private boolean equality = false;
  
  private boolean anyOf = false;
  
  private boolean rangeInclusive = false;
  
  private boolean rangeExclusive = false;
  
  private boolean patternMatch = false;
  
  private boolean smartMatch = false;
  
  private boolean anyOfSmartMatch = false;
  
  private boolean caseSensitive = false;
  
  private String unit = null;
  
  public boolean isSearchable() {
    return this.searchable;
  }
  
  public boolean isFacet() {
    return this.facet;
  }
  
  public boolean isEquality() {
    return this.equality;
  }
  
  public boolean isAnyOf() {
    return this.anyOf;
  }
  
  public boolean isRangeInclusive() {
    return this.rangeInclusive;
  }
  
  public boolean isRangeExclusive() {
    return this.rangeExclusive;
  }
  
  public boolean isPatternMatch() {
    return this.patternMatch;
  }
  
  public boolean isSmartMatch() {
    return this.smartMatch;
  }
  
  public boolean isAnyOfSmartMatch() {
    return this.anyOfSmartMatch;
  }
  
  public boolean isCaseSensitive() {
    return this.caseSensitive;
  }
  
  public String getUnit() {
    return this.unit;
  }
  
  public void setSearchable(boolean paramBoolean) {
    this.searchable = paramBoolean;
  }
  
  public void setFacet(boolean paramBoolean) {
    this.facet = paramBoolean;
  }
  
  public void setEquality(boolean paramBoolean) {
    this.equality = paramBoolean;
  }
  
  public void setAnyOf(boolean paramBoolean) {
    this.anyOf = paramBoolean;
  }
  
  public void setRangeInclusive(boolean paramBoolean) {
    this.rangeInclusive = paramBoolean;
  }
  
  public void setRangeExclusive(boolean paramBoolean) {
    this.rangeExclusive = paramBoolean;
  }
  
  public void setPatternMatch(boolean paramBoolean) {
    this.patternMatch = paramBoolean;
  }
  
  public void setSmartMatch(boolean paramBoolean) {
    this.smartMatch = paramBoolean;
  }
  
  public void setAnyOfSmartMatch(boolean paramBoolean) {
    this.anyOfSmartMatch = paramBoolean;
  }
  
  public void setCaseSensitive(boolean paramBoolean) {
    this.caseSensitive = paramBoolean;
  }
  
  public void setUnit(String paramString) {
    this.unit = paramString;
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (!(paramObject instanceof SearchCapability))
      return false; 
    SearchCapability searchCapability = (SearchCapability)paramObject;
    if (!searchCapability.canEqual(this))
      return false; 
    if (isSearchable() != searchCapability.isSearchable())
      return false; 
    if (isFacet() != searchCapability.isFacet())
      return false; 
    if (isEquality() != searchCapability.isEquality())
      return false; 
    if (isAnyOf() != searchCapability.isAnyOf())
      return false; 
    if (isRangeInclusive() != searchCapability.isRangeInclusive())
      return false; 
    if (isRangeExclusive() != searchCapability.isRangeExclusive())
      return false; 
    if (isPatternMatch() != searchCapability.isPatternMatch())
      return false; 
    if (isSmartMatch() != searchCapability.isSmartMatch())
      return false; 
    if (isAnyOfSmartMatch() != searchCapability.isAnyOfSmartMatch())
      return false; 
    if (isCaseSensitive() != searchCapability.isCaseSensitive())
      return false; 
    String str1 = getUnit();
    String str2 = searchCapability.getUnit();
    return !((str1 == null) ? (str2 != null) : !str1.equals(str2));
  }
  
  protected boolean canEqual(Object paramObject) {
    return paramObject instanceof SearchCapability;
  }
  
  public int hashCode() {
    null = 1;
    null = null * 59 + (isSearchable() ? 79 : 97);
    null = null * 59 + (isFacet() ? 79 : 97);
    null = null * 59 + (isEquality() ? 79 : 97);
    null = null * 59 + (isAnyOf() ? 79 : 97);
    null = null * 59 + (isRangeInclusive() ? 79 : 97);
    null = null * 59 + (isRangeExclusive() ? 79 : 97);
    null = null * 59 + (isPatternMatch() ? 79 : 97);
    null = null * 59 + (isSmartMatch() ? 79 : 97);
    null = null * 59 + (isAnyOfSmartMatch() ? 79 : 97);
    null = null * 59 + (isCaseSensitive() ? 79 : 97);
    String str = getUnit();
    return null * 59 + ((str == null) ? 43 : str.hashCode());
  }
  
  public String toString() {
    return "SearchCapability(searchable=" + isSearchable() + ", facet=" + isFacet() + ", equality=" + isEquality() + ", anyOf=" + isAnyOf() + ", rangeInclusive=" + isRangeInclusive() + ", rangeExclusive=" + isRangeExclusive() + ", patternMatch=" + isPatternMatch() + ", smartMatch=" + isSmartMatch() + ", anyOfSmartMatch=" + isAnyOfSmartMatch() + ", caseSensitive=" + isCaseSensitive() + ", unit=" + getUnit() + ")";
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\config\SearchCapability.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */