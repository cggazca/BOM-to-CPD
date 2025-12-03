package com.mentor.dms.contentprovider.sf.datamodel.upgrade;

import java.util.ArrayList;

public class MappingUpgradeResult {
  private int lineNo;
  
  private String procName;
  
  private String partID;
  
  private String partName;
  
  private String partPropID;
  
  private String partPropName;
  
  private String message = "";
  
  private ArrayList<String> errorMsgs = null;
  
  private ArrayList<String> warnMsgs = null;
  
  public MappingUpgradeResult(int paramInt, ArrayList<String> paramArrayList) {
    this.lineNo = paramInt;
    this.partID = paramArrayList.get(1);
    this.partName = paramArrayList.get(2);
    this.partPropID = paramArrayList.get(3);
    this.partPropName = paramArrayList.get(4);
  }
  
  public void addError(String paramString) {
    if (this.errorMsgs == null)
      this.errorMsgs = new ArrayList<>(); 
    this.errorMsgs.add(paramString);
  }
  
  public void addWarn(String paramString) {
    if (this.warnMsgs == null)
      this.warnMsgs = new ArrayList<>(); 
    this.warnMsgs.add(paramString);
  }
  
  public boolean hasError() {
    return (this.errorMsgs != null);
  }
  
  public boolean hasWarn() {
    return (this.warnMsgs != null);
  }
  
  public int getLineNo() {
    return this.lineNo;
  }
  
  public String getProcName() {
    return this.procName;
  }
  
  public String getPartID() {
    return this.partID;
  }
  
  public String getPartName() {
    return this.partName;
  }
  
  public String getPartPropID() {
    return this.partPropID;
  }
  
  public String getPartPropName() {
    return this.partPropName;
  }
  
  public String getMessage() {
    return this.message;
  }
  
  public ArrayList<String> getErrorMsgs() {
    return this.errorMsgs;
  }
  
  public ArrayList<String> getWarnMsgs() {
    return this.warnMsgs;
  }
  
  public void setLineNo(int paramInt) {
    this.lineNo = paramInt;
  }
  
  public void setProcName(String paramString) {
    this.procName = paramString;
  }
  
  public void setPartID(String paramString) {
    this.partID = paramString;
  }
  
  public void setPartName(String paramString) {
    this.partName = paramString;
  }
  
  public void setPartPropID(String paramString) {
    this.partPropID = paramString;
  }
  
  public void setPartPropName(String paramString) {
    this.partPropName = paramString;
  }
  
  public void setMessage(String paramString) {
    this.message = paramString;
  }
  
  public void setErrorMsgs(ArrayList<String> paramArrayList) {
    this.errorMsgs = paramArrayList;
  }
  
  public void setWarnMsgs(ArrayList<String> paramArrayList) {
    this.warnMsgs = paramArrayList;
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (!(paramObject instanceof MappingUpgradeResult))
      return false; 
    MappingUpgradeResult mappingUpgradeResult = (MappingUpgradeResult)paramObject;
    if (!mappingUpgradeResult.canEqual(this))
      return false; 
    if (getLineNo() != mappingUpgradeResult.getLineNo())
      return false; 
    String str1 = getProcName();
    String str2 = mappingUpgradeResult.getProcName();
    if ((str1 == null) ? (str2 != null) : !str1.equals(str2))
      return false; 
    String str3 = getPartID();
    String str4 = mappingUpgradeResult.getPartID();
    if ((str3 == null) ? (str4 != null) : !str3.equals(str4))
      return false; 
    String str5 = getPartName();
    String str6 = mappingUpgradeResult.getPartName();
    if ((str5 == null) ? (str6 != null) : !str5.equals(str6))
      return false; 
    String str7 = getPartPropID();
    String str8 = mappingUpgradeResult.getPartPropID();
    if ((str7 == null) ? (str8 != null) : !str7.equals(str8))
      return false; 
    String str9 = getPartPropName();
    String str10 = mappingUpgradeResult.getPartPropName();
    if ((str9 == null) ? (str10 != null) : !str9.equals(str10))
      return false; 
    String str11 = getMessage();
    String str12 = mappingUpgradeResult.getMessage();
    if ((str11 == null) ? (str12 != null) : !str11.equals(str12))
      return false; 
    ArrayList<String> arrayList1 = getErrorMsgs();
    ArrayList<String> arrayList2 = mappingUpgradeResult.getErrorMsgs();
    if ((arrayList1 == null) ? (arrayList2 != null) : !arrayList1.equals(arrayList2))
      return false; 
    ArrayList<String> arrayList3 = getWarnMsgs();
    ArrayList<String> arrayList4 = mappingUpgradeResult.getWarnMsgs();
    return !((arrayList3 == null) ? (arrayList4 != null) : !arrayList3.equals(arrayList4));
  }
  
  protected boolean canEqual(Object paramObject) {
    return paramObject instanceof MappingUpgradeResult;
  }
  
  public int hashCode() {
    null = 1;
    null = null * 59 + getLineNo();
    String str1 = getProcName();
    null = null * 59 + ((str1 == null) ? 43 : str1.hashCode());
    String str2 = getPartID();
    null = null * 59 + ((str2 == null) ? 43 : str2.hashCode());
    String str3 = getPartName();
    null = null * 59 + ((str3 == null) ? 43 : str3.hashCode());
    String str4 = getPartPropID();
    null = null * 59 + ((str4 == null) ? 43 : str4.hashCode());
    String str5 = getPartPropName();
    null = null * 59 + ((str5 == null) ? 43 : str5.hashCode());
    String str6 = getMessage();
    null = null * 59 + ((str6 == null) ? 43 : str6.hashCode());
    ArrayList<String> arrayList1 = getErrorMsgs();
    null = null * 59 + ((arrayList1 == null) ? 43 : arrayList1.hashCode());
    ArrayList<String> arrayList2 = getWarnMsgs();
    return null * 59 + ((arrayList2 == null) ? 43 : arrayList2.hashCode());
  }
  
  public String toString() {
    return "MappingUpgradeResult(lineNo=" + getLineNo() + ", procName=" + getProcName() + ", partID=" + getPartID() + ", partName=" + getPartName() + ", partPropID=" + getPartPropID() + ", partPropName=" + getPartPropName() + ", message=" + getMessage() + ", errorMsgs=" + String.valueOf(getErrorMsgs()) + ", warnMsgs=" + String.valueOf(getWarnMsgs()) + ")";
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\datamode\\upgrade\MappingUpgradeResult.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */