package com.mentor.dms.contentprovider.core.plugin;

public enum PricePropertyEnum {
  Quantity(true, "Quantity"),
  Value(true, "Value"),
  Currency(true, "Currency"),
  Complex(false, "__complex__");
  
  private boolean display;
  
  private String propid;
  
  PricePropertyEnum(boolean paramBoolean, String paramString1) {
    this.display = paramBoolean;
    this.propid = paramString1;
  }
  
  public boolean isDisplay() {
    return this.display;
  }
  
  public String getPropid() {
    return this.propid;
  }
  
  public int getDisplayCount() {
    byte b = 0;
    for (PricePropertyEnum pricePropertyEnum : values()) {
      if (pricePropertyEnum.isDisplay())
        b++; 
    } 
    return b;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\plugin\PricePropertyEnum.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */