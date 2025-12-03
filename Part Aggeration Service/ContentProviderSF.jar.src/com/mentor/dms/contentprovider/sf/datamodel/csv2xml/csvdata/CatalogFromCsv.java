package com.mentor.dms.contentprovider.sf.datamodel.csv2xml.csvdata;

import java.util.ArrayList;
import java.util.List;

public class CatalogFromCsv {
  private Command command = Command.NONE;
  
  private String domainName;
  
  private String name;
  
  private String parentKey;
  
  private boolean rename = false;
  
  private ArrayList<PropertyFromCsv> properties = null;
  
  public CatalogFromCsv(String paramString1, String paramString2) {
    this.domainName = paramString1;
    this.name = paramString2;
  }
  
  public Command getCommand() {
    return this.command;
  }
  
  public void setCommand(String paramString) throws CSVParseException {
    this.command = Command.fromValue(paramString);
  }
  
  public String getDomainName() {
    return this.domainName;
  }
  
  public String getName() {
    return this.name;
  }
  
  public void setName(String paramString) {
    this.name = paramString;
  }
  
  public String getParentKey() {
    return this.parentKey;
  }
  
  public void setParentKey(String paramString) {
    this.parentKey = paramString;
  }
  
  public List<PropertyFromCsv> getProperties() {
    return this.properties;
  }
  
  public void addProperties(PropertyFromCsv paramPropertyFromCsv) {
    if (this.properties == null)
      this.properties = new ArrayList<>(); 
    if (!this.properties.contains(paramPropertyFromCsv))
      this.properties.add(paramPropertyFromCsv); 
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
    DEL("D");
    
    private final String value;
    
    Command(String param1String1) {
      this.value = param1String1;
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


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\datamodel\csv2xml\csvdata\CatalogFromCsv.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */