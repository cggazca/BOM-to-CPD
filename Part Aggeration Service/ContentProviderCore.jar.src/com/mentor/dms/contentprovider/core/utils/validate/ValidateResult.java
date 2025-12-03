package com.mentor.dms.contentprovider.core.utils.validate;

public class ValidateResult {
  public static final String RESULT_HEADER = "Master Source,PartClass,PartClassName,Property,Propertyname,Destination Source,Check Item1,Check Item2,Check Contents,Result";
  
  public static final String[][] OUTPUT_VALUE_TBL = new String[][] { 
      { "Content Provider", "Mapping", "", "" }, { "Content Provider", "Mapping", "PartClass", "1:Existence check" }, { "Content Provider", "Mapping", "PartClass", "2:Duplicate check" }, { "Content Provider", "Mapping", "Manufacturer Part", "3:Check the corresponding Catalog Group is set" }, { "Content Provider", "Mapping", "Component", "4:Check the corresponding Catalog Group is set" }, { "Content Provider", "EDM Library", "Manufacturer Part", "5:Check the corresponding Catalog Group exists" }, { "Content Provider", "EDM Library", "Component", "6:Check the corresponding Catalog Group exists" }, { "Content Provider", "Mapping", "Property", "7:Existence check" }, { "Content Provider", "Mapping", "Property", "8:Check the Searchable settings of a Property match." }, { "Content Provider", "Mapping", "Manufacturer Part", "9:Check the corresponding Characteristics are set" }, 
      { "Content Provider", "Mapping", "Component", "10:Check the corresponding Characteristics are set" }, { "Content Provider", "Mapping", "Manufacturer Part to Component", "11:Check the corresponding Manufacturer Part to Component is set" }, { "Content Provider", "EDM Library", "Manufacturer Part", "12:Existence check" }, { "Content Provider", "EDM Library", "Manufacturer Part", "13:Unit Check" }, { "Content Provider", "EDM Library", "Component", "14:Existence check" }, { "Content Provider", "EDM Library", "Component", "15:Unit Check" }, { "Mapping", "Content Provider", "PartClass", "16:Existence check" }, { "Mapping", "Content Provider", "Properties", "17:Existence check" } };
  
  private String partID;
  
  private String partName;
  
  private String propertyID = "";
  
  private String propertyName = "";
  
  private String checkItem2 = "";
  
  private Result result = Result.OK;
  
  private int contentNo = 0;
  
  private String message;
  
  public ValidateResult(String paramString1, String paramString2) {
    this.partID = paramString1;
    this.partName = paramString2;
  }
  
  public String getOutputLine() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("\"" + OUTPUT_VALUE_TBL[this.contentNo][0] + "\",");
    stringBuilder.append("\"" + this.partID + "\",");
    stringBuilder.append("\"" + this.partName + "\",");
    stringBuilder.append("\"" + this.propertyID + "\",");
    stringBuilder.append("\"" + this.propertyName + "\",");
    stringBuilder.append("\"" + OUTPUT_VALUE_TBL[this.contentNo][1] + "\",");
    stringBuilder.append("\"" + OUTPUT_VALUE_TBL[this.contentNo][2] + "\",");
    stringBuilder.append("\"" + this.checkItem2 + "\",");
    stringBuilder.append("\"" + OUTPUT_VALUE_TBL[this.contentNo][3] + "\",");
    stringBuilder.append("\"" + this.result.toString() + ":" + this.message + "\"");
    return stringBuilder.toString();
  }
  
  public Result getResult() {
    return this.result;
  }
  
  public void setResult(int paramInt, Result paramResult) {
    setResult(paramInt, paramResult, "", "");
  }
  
  public void setResult(int paramInt, Result paramResult, String paramString) {
    setResult(paramInt, paramResult, paramString, "");
  }
  
  public void setResult(int paramInt, Result paramResult, String paramString1, String paramString2) {
    this.contentNo = paramInt;
    this.result = paramResult;
    this.message = paramString1;
    this.checkItem2 = paramString2;
  }
  
  public void setProperty(String paramString1, String paramString2) {
    this.propertyID = paramString1;
    this.propertyName = paramString2;
  }
  
  public enum Result {
    OK, NG, WARN;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\cor\\utils\validate\ValidateResult.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */