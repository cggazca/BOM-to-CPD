package com.mentor.dms.contentprovider.sf.datamodel.csv2xml.csvdata;

public enum Command {
  NONE(""),
  ADD("C"),
  UPD("U"),
  DEL("D"),
  DELALL("DA");
  
  private final String value;
  
  Command(String paramString1) {
    this.value = paramString1;
  }
  
  public String toValue() {
    return this.value;
  }
  
  public static Command fromValue(String paramString) throws CSVParseException {
    String str = paramString.toUpperCase();
    for (Command command : values()) {
      if (command.value.equals(str))
        return command; 
    } 
    throw new CSVParseException("Invalid command. [" + paramString + "]");
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\datamodel\csv2xml\csvdata\PropertyFromCsv$Command.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */