package com.mentor.dms.contentprovider.sf.response.propertydef;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PropType {
  @SerializedName("caseSensitive")
  @Expose
  public Boolean caseSensitive;
  
  @SerializedName("unit")
  @Expose
  public String unit;
  
  public Boolean getCaseSensitive() {
    return this.caseSensitive;
  }
  
  public String getUnit() {
    return this.unit;
  }
  
  public void setCaseSensitive(Boolean paramBoolean) {
    this.caseSensitive = paramBoolean;
  }
  
  public void setUnit(String paramString) {
    this.unit = paramString;
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (!(paramObject instanceof PropType))
      return false; 
    PropType propType = (PropType)paramObject;
    if (!propType.canEqual(this))
      return false; 
    Boolean bool1 = getCaseSensitive();
    Boolean bool2 = propType.getCaseSensitive();
    if ((bool1 == null) ? (bool2 != null) : !bool1.equals(bool2))
      return false; 
    String str1 = getUnit();
    String str2 = propType.getUnit();
    return !((str1 == null) ? (str2 != null) : !str1.equals(str2));
  }
  
  protected boolean canEqual(Object paramObject) {
    return paramObject instanceof PropType;
  }
  
  public int hashCode() {
    null = 1;
    Boolean bool = getCaseSensitive();
    null = null * 59 + ((bool == null) ? 43 : bool.hashCode());
    String str = getUnit();
    return null * 59 + ((str == null) ? 43 : str.hashCode());
  }
  
  public String toString() {
    return "PropType(caseSensitive=" + getCaseSensitive() + ", unit=" + getUnit() + ")";
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\response\propertydef\PropType.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */