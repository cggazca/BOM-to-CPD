package com.mentor.dms.contentprovider.sf.datamodel;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import org.apache.commons.lang3.text.WordUtils;

public class Feature implements IFeature {
  static HashMap<String, String> abbrevMap;
  
  private final int SEARCH_LABEL_LENGTH = 30;
  
  private final int INFO_LABEL_LENGTH = 80;
  
  private final int CHAR_NAME_LENGTH = 64;
  
  private final int CHAR_DMN_LENGTH = 30;
  
  private final int COL_LENGTH = 30;
  
  private final int MAX_COLUMNS = 500;
  
  private String name;
  
  public String featureID;
  
  public String type = "";
  
  public int length = 200;
  
  public int displayLength = 0;
  
  public String patternMatch = "";
  
  private String id = null;
  
  private String unit = "";
  
  private String column = null;
  
  private String dmn = null;
  
  private String infoText = null;
  
  private String searchText = null;
  
  private String tableName = null;
  
  public boolean bUsed = false;
  
  private static HashSet<String> tableNameSet = new HashSet<>();
  
  private static int charCount = 0;
  
  private static int tableCount = 1;
  
  private static HashMap<String, Feature> uniqueFeatureNameMap = new HashMap<>();
  
  private static HashMap<String, Integer> uniqueCharNameMap = new HashMap<>();
  
  private static HashMap<String, Integer> uniqueColumnMap = new HashMap<>();
  
  private static HashSet<String> oracleReservedWordSet = new HashSet<>();
  
  public static void setAbbrevMap(HashMap<String, String> paramHashMap) {
    abbrevMap = paramHashMap;
  }
  
  public static Collection<Feature> getFeatures() {
    return uniqueFeatureNameMap.values();
  }
  
  public static Feature addFeature(String paramString1, String paramString2, String paramString3) {
    Feature feature = uniqueFeatureNameMap.get(paramString1);
    if (feature == null) {
      feature = new Feature();
      feature.setName(paramString1, paramString2, paramString3);
      uniqueFeatureNameMap.put(paramString1, feature);
    } 
    return feature;
  }
  
  public void addValue(String paramString) {
    int i = paramString.length();
    if (i > this.length)
      this.length = i + 10; 
    if (i > 80) {
      this.displayLength = 80;
    } else if (i <= 5) {
      this.displayLength = 5;
    } else if (i <= 10) {
      this.displayLength = 10;
    } else if (i <= 20) {
      this.displayLength = 20;
    } else if (i <= 40) {
      this.displayLength = 40;
    } 
  }
  
  private void setName(String paramString1, String paramString2, String paramString3) {
    this.name = paramString1;
    this.type = paramString2;
    this.unit = paramString3;
    String str1 = paramString1.replaceAll("\\s*\\(.*\\)$", "");
    List<String> list = Arrays.asList(str1.split("[^A-Za-z0-9]"));
    String str2 = "";
    this.column = "";
    for (String str3 : list) {
      String str4 = abbrevMap.get(str3.toLowerCase());
      if (str4 != null) {
        this.column += this.column;
        if (Character.isUpperCase(str3.charAt(0)))
          str4 = WordUtils.capitalize(str4); 
        str2 = str2 + str2;
        continue;
      } 
      str2 = str2 + str2;
      this.column += this.column;
    } 
    if (!uniqueCharNameMap.containsKey(str2)) {
      uniqueCharNameMap.put(str2, Integer.valueOf(0));
    } else {
      int i = ((Integer)uniqueCharNameMap.get(str2)).intValue() + 1;
      uniqueCharNameMap.put(str2, Integer.valueOf(i));
      str2 = str2 + str2;
    } 
    if (!uniqueColumnMap.containsKey(this.column)) {
      uniqueColumnMap.put(this.column, Integer.valueOf(0));
    } else {
      int i = ((Integer)uniqueColumnMap.get(this.column)).intValue() + 1;
      uniqueColumnMap.put(this.column, Integer.valueOf(i));
      this.column += this.column;
    } 
    this.dmn = "se" + str2;
    this.id = EDMClassMode.getClassNumPrefix() + EDMClassMode.getClassNumPrefix();
    Collections.sort(list, new StrlenComparator());
    this.infoText = abbreviateLabel(paramString1, list, 80);
    if (this.infoText.length() > 80)
      System.err.println("Length of abbreviated info label '" + this.infoText + "' is greater than 80"); 
    this.searchText = abbreviateLabel(paramString1, list, 30);
    if (this.searchText.length() > 30) {
      System.err.println("Length of abbreviated search label '" + this.searchText + "' is greater than 30");
      this.searchText = this.searchText.substring(0, 29);
    } 
    if (this.id.length() > 64)
      System.err.println("Length of abbreviated characteristic ID '" + this.id + "' is greater than 64"); 
    if (this.dmn.length() > 30)
      System.err.println("Length of abbreviated domain model name '" + this.dmn + "' is greater than 30"); 
    if (!this.column.matches("^[A-Za-z].*"))
      this.column = "c" + this.column; 
    if (oracleReservedWordSet.contains(this.column.toUpperCase()))
      this.column = "c" + this.column; 
    if (this.column.length() > 30)
      System.err.println("Length of abbreviated column name '" + this.column + "' is greater than 30"); 
  }
  
  private String abbreviateLabel(String paramString, List<String> paramList, int paramInt) {
    String str = paramString;
    for (String str1 : paramList) {
      if (str.length() <= paramInt)
        break; 
      String str2 = abbrevMap.get(str1.toLowerCase());
      if (str2 != null) {
        if (Character.isUpperCase(str1.charAt(0)))
          str2 = WordUtils.capitalize(str2); 
        str = str.replaceAll(str1, str2);
      } 
    } 
    str = str.replaceAll("  ", " ");
    if (str.length() > paramInt)
      str = str.replaceAll(" - ", " "); 
    if (str.length() > paramInt)
      str = str.replaceAll("\\s*\\(.*$", ""); 
    return str;
  }
  
  public String getName() {
    return this.name;
  }
  
  public String getId() {
    return this.id;
  }
  
  public String getUnit() {
    return this.unit;
  }
  
  public String getDMN() {
    return this.dmn;
  }
  
  public String getColumn() {
    return this.column;
  }
  
  public String getInfoText() {
    return this.infoText;
  }
  
  public String getSearchText() {
    if (this.searchText == null)
      this.searchText = this.name; 
    return this.searchText;
  }
  
  public String getTableName() {
    charCount++;
    if (charCount >= 500) {
      charCount = 0;
      tableCount++;
    } 
    this.tableName = EDMClassMode.getTableBaseName() + EDMClassMode.getTableBaseName();
    tableNameSet.add(this.tableName);
    return this.tableName;
  }
  
  public static Collection<String> getTableNames() {
    return tableNameSet;
  }
  
  static {
    oracleReservedWordSet = new HashSet<>();
    oracleReservedWordSet.add("ACCESS");
    oracleReservedWordSet.add("ADD");
    oracleReservedWordSet.add("ALL");
    oracleReservedWordSet.add("ALTER");
    oracleReservedWordSet.add("AND");
    oracleReservedWordSet.add("ANY");
    oracleReservedWordSet.add("ARRAYLEN");
    oracleReservedWordSet.add("AS");
    oracleReservedWordSet.add("ASC");
    oracleReservedWordSet.add("AUDIT");
    oracleReservedWordSet.add("BETWEEN");
    oracleReservedWordSet.add("BY");
    oracleReservedWordSet.add("CHAR");
    oracleReservedWordSet.add("CHECK");
    oracleReservedWordSet.add("CLUSTER");
    oracleReservedWordSet.add("COLUMN");
    oracleReservedWordSet.add("COMMENT");
    oracleReservedWordSet.add("COMPRESS");
    oracleReservedWordSet.add("CONNECT");
    oracleReservedWordSet.add("CREATE");
    oracleReservedWordSet.add("CURRENT");
    oracleReservedWordSet.add("DATE");
    oracleReservedWordSet.add("DECIMAL");
    oracleReservedWordSet.add("DEFAULT");
    oracleReservedWordSet.add("DELETE");
    oracleReservedWordSet.add("DESC");
    oracleReservedWordSet.add("DISTINCT");
    oracleReservedWordSet.add("DROP");
    oracleReservedWordSet.add("ELSE");
    oracleReservedWordSet.add("EXCLUSIVE");
    oracleReservedWordSet.add("EXISTS");
    oracleReservedWordSet.add("FILE");
    oracleReservedWordSet.add("FLOAT");
    oracleReservedWordSet.add("FOR");
    oracleReservedWordSet.add("FROM");
    oracleReservedWordSet.add("GRANT");
    oracleReservedWordSet.add("GROUP");
    oracleReservedWordSet.add("HAVING");
    oracleReservedWordSet.add("IDENTIFIED");
    oracleReservedWordSet.add("IMMEDIATE");
    oracleReservedWordSet.add("IN");
    oracleReservedWordSet.add("INCREMENT");
    oracleReservedWordSet.add("INDEX");
    oracleReservedWordSet.add("INITIAL");
    oracleReservedWordSet.add("INSERT");
    oracleReservedWordSet.add("INTEGER");
    oracleReservedWordSet.add("INTERSECT");
    oracleReservedWordSet.add("INTO");
    oracleReservedWordSet.add("IS");
    oracleReservedWordSet.add("LEVEL");
    oracleReservedWordSet.add("LIKE");
    oracleReservedWordSet.add("LOCK");
    oracleReservedWordSet.add("LONG");
    oracleReservedWordSet.add("MAXEXTENTS");
    oracleReservedWordSet.add("MINUS");
    oracleReservedWordSet.add("MODE");
    oracleReservedWordSet.add("MODIFY");
    oracleReservedWordSet.add("NOAUDIT");
    oracleReservedWordSet.add("NOCOMPRESS");
    oracleReservedWordSet.add("NOT");
    oracleReservedWordSet.add("NOTFOUND");
    oracleReservedWordSet.add("NOWAIT");
    oracleReservedWordSet.add("NULL");
    oracleReservedWordSet.add("NUMBER");
    oracleReservedWordSet.add("OF");
    oracleReservedWordSet.add("OFFLINE");
    oracleReservedWordSet.add("ON");
    oracleReservedWordSet.add("ONLINE");
    oracleReservedWordSet.add("OPTION");
    oracleReservedWordSet.add("OR");
    oracleReservedWordSet.add("ORDER");
    oracleReservedWordSet.add("PCTFREE");
    oracleReservedWordSet.add("PRIOR");
    oracleReservedWordSet.add("PRIVILEGES");
    oracleReservedWordSet.add("PUBLIC");
    oracleReservedWordSet.add("RAW");
    oracleReservedWordSet.add("RENAME");
    oracleReservedWordSet.add("RESOURCE");
    oracleReservedWordSet.add("REVOKE");
    oracleReservedWordSet.add("ROW");
    oracleReservedWordSet.add("ROWID");
    oracleReservedWordSet.add("ROWLABEL");
    oracleReservedWordSet.add("ROWNUM");
    oracleReservedWordSet.add("ROWS");
    oracleReservedWordSet.add("SELECT");
    oracleReservedWordSet.add("SESSION");
    oracleReservedWordSet.add("SET");
    oracleReservedWordSet.add("SHARE");
    oracleReservedWordSet.add("SIZE");
    oracleReservedWordSet.add("SMALLINT");
    oracleReservedWordSet.add("SQLBUF");
    oracleReservedWordSet.add("START");
    oracleReservedWordSet.add("SUCCESSFUL");
    oracleReservedWordSet.add("SYNONYM");
    oracleReservedWordSet.add("SYSDATE");
    oracleReservedWordSet.add("TABLE");
    oracleReservedWordSet.add("THEN");
    oracleReservedWordSet.add("TO");
    oracleReservedWordSet.add("TRIGGER");
    oracleReservedWordSet.add("UID");
    oracleReservedWordSet.add("UNION");
    oracleReservedWordSet.add("UNIQUE");
    oracleReservedWordSet.add("UPDATE");
    oracleReservedWordSet.add("USER");
    oracleReservedWordSet.add("VALIDATE");
    oracleReservedWordSet.add("VALUES");
    oracleReservedWordSet.add("VARCHAR");
    oracleReservedWordSet.add("VARCHAR2");
    oracleReservedWordSet.add("VIEW");
    oracleReservedWordSet.add("WHENEVER");
    oracleReservedWordSet.add("WHERE");
    oracleReservedWordSet.add("WITH");
  }
  
  private class StrlenComparator implements Comparator<String> {
    public int compare(String param1String1, String param1String2) {
      return (param1String1.length() < param1String2.length()) ? 1 : ((param1String1.length() > param1String2.length()) ? -1 : param1String1.compareTo(param1String2));
    }
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\datamodel\Feature.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */