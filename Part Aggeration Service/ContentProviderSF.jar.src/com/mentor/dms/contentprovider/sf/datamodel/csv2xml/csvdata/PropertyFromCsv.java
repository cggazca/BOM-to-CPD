package com.mentor.dms.contentprovider.sf.datamodel.csv2xml.csvdata;

public class PropertyFromCsv {
  private Command command = Command.NONE;
  
  private String domainName;
  
  private String name;
  
  private String tabName;
  
  private String type;
  
  private String unit;
  
  private boolean rename = false;
  
  public PropertyFromCsv(String paramString1, String paramString2) {
    this.domainName = paramString1;
    this.name = paramString2;
  }
  
  public Command getCommand() {
    return this.command;
  }
  
  public void clearCommand() {
    this.command = Command.NONE;
  }
  
  public void setCommand(String paramString) throws CSVParseException {
    this.command = Command.fromValue(paramString);
  }
  
  public String getTabName() {
    return this.tabName;
  }
  
  public void setTabName(String paramString) {
    this.tabName = paramString;
  }
  
  public String getDomainName() {
    return this.domainName;
  }
  
  public String getName() {
    return this.name;
  }
  
  public String getType() {
    return this.type;
  }
  
  public void setType(String paramString) {
    this.type = paramString;
  }
  
  public String getUnit() {
    return this.unit;
  }
  
  public void setUnit(String paramString) {
    this.unit = paramString;
  }
  
  public boolean isRename() {
    return this.rename;
  }
  
  public void setRename(boolean paramBoolean) {
    this.rename = paramBoolean;
  }
  
  public enum Command {
    NONE(""),
    ADD("C"),
    UPD("U"),
    DEL("D"),
    DELALL("DA");
    
    private final String value;
    
    Command(String param1String1) {
      this.value = param1String1;
    }
    
    public String toValue() {
      return this.value;
    }
    
    public static Command fromValue(String param1String) throws CSVParseException {
      String str = param1String.toUpperCase();
      for (Command command : values()) {
        if (command.value.equals(str))
          return command; 
      } 
      throw new CSVParseException("Invalid command. [" + param1String + "]");
    }
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\datamodel\csv2xml\csvdata\PropertyFromCsv.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */