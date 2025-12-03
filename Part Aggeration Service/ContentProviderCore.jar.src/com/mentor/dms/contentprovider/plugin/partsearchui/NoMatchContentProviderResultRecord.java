package com.mentor.dms.contentprovider.plugin.partsearchui;

import com.mentor.datafusion.oi.OIObjectManager;
import com.mentor.dms.contentprovider.AbstractContentProvider;
import com.mentor.dms.contentprovider.AbstractContentProviderResultRecord;
import com.mentor.dms.contentprovider.ContentProviderException;

public class NoMatchContentProviderResultRecord extends AbstractContentProviderResultRecord {
  private String partNumber;
  
  private String mfgName;
  
  public NoMatchContentProviderResultRecord(AbstractContentProvider paramAbstractContentProvider, String paramString1, String paramString2) {
    super(paramAbstractContentProvider);
    this.partNumber = paramString1;
    this.mfgName = paramString2;
  }
  
  public boolean isExistsInDMS(OIObjectManager paramOIObjectManager) throws ContentProviderException {
    return false;
  }
  
  public String getObjectID() {
    return "";
  }
  
  public String getPartNumber() {
    return this.partNumber;
  }
  
  public String getManufacturerID() {
    return "";
  }
  
  public String getManufacturerName() {
    return this.mfgName;
  }
  
  public String getPartClassID() {
    return "Part";
  }
  
  public String getPartClassName() {
    return "Part";
  }
  
  public boolean isECADSymbolAvailable() {
    return false;
  }
  
  public boolean isECADFootprintAvailable() {
    return false;
  }
  
  public boolean isECAD3DModelAvailable() {
    return false;
  }
  
  public boolean hasECADModels() {
    return false;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\plugin\partsearchui\NoMatchContentProviderResultRecord.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */