package com.mentor.dms.contentprovider.core.plugin;

public enum SupplyPropertyEnum {
  DistributorName("339c3014"),
  DistributorPartNumber("8f6be867"),
  DistributorAuthorizedStatus("08c82fa6"),
  ManufacturerPartNumber("d8ac8dcc"),
  ManufacturerName("9df8ff36"),
  Description("bf4dd752"),
  LifeCycleStatus("d3ce1ea0"),
  RohsCompliance("41e14b24"),
  MinimumOrderQuantity("a376c2e6"),
  Stock("8f6f3508"),
  ContainerType("1a54a21c"),
  PriceBreakdown("5702a948"),
  LastUpdated("fdd91810"),
  BuyNowUrl("db80a0d0");
  
  private String id;
  
  SupplyPropertyEnum(String paramString1) {
    this.id = paramString1;
  }
  
  public String getId() {
    return this.id;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\plugin\SupplyPropertyEnum.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */