package com.mentor.dms.contentprovider.sf.datamodel.upgrade;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;

public class MappingUpgradeReport {
  private static final String FILE_NAME = "MappingUpgradeReport.txt";
  
  private static PrintWriter pw;
  
  public static void createFile(String paramString) throws IOException {
    File file1 = new File(paramString);
    File file2 = new File(file1.getParent());
    File file3 = new File(String.valueOf(file2) + "/MappingUpgradeReport.txt");
    if (!file2.exists())
      file2.mkdirs(); 
    if (!file3.exists())
      file3.createNewFile(); 
    FileWriter fileWriter = new FileWriter(String.valueOf(file2) + "/MappingUpgradeReport.txt", Charset.forName("UTF-8"), false);
    pw = new PrintWriter(new BufferedWriter(fileWriter));
    pw.print("");
  }
  
  public static void print(MappingUpgradeResult paramMappingUpgradeResult) {
    if (paramMappingUpgradeResult == null)
      return; 
    if (paramMappingUpgradeResult.hasWarn())
      for (String str : paramMappingUpgradeResult.getWarnMsgs())
        printMessage(paramMappingUpgradeResult, "WARN", str);  
    if (paramMappingUpgradeResult.hasError())
      for (String str : paramMappingUpgradeResult.getErrorMsgs())
        printMessage(paramMappingUpgradeResult, "ERROR", str);  
    if (!paramMappingUpgradeResult.hasError() && !paramMappingUpgradeResult.hasWarn())
      printMessage(paramMappingUpgradeResult, "OK", ""); 
  }
  
  private static void printMessage(MappingUpgradeResult paramMappingUpgradeResult, String paramString1, String paramString2) {
    pw.write("" + paramMappingUpgradeResult.getLineNo() + "\t" + paramMappingUpgradeResult.getLineNo() + "\t" + paramMappingUpgradeResult.getProcName() + "\t" + paramMappingUpgradeResult.getPartID() + "\t" + paramMappingUpgradeResult.getPartName() + "\t" + paramMappingUpgradeResult.getPartPropID() + "\t" + paramMappingUpgradeResult.getPartPropName() + "\t" + paramString1 + "\t");
    pw.write(System.lineSeparator());
  }
  
  public static void close() {
    if (pw != null)
      pw.close(); 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\datamode\\upgrade\MappingUpgradeReport.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */