package com.mentor.dms.contentprovider.core.utils;

import com.mentor.datafusion.utils.logger.MGLogger;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;

public class SearchAndAssignCreateCSV {
  MGLogger logger = MGLogger.getLogger(SearchAndAssignCreateCSV.class);
  
  String outputdir;
  
  String outputpath;
  
  public void createFile(String paramString1, String paramString2) throws IOException {
    this.outputdir = paramString1;
    this.outputpath = paramString2;
    File file1 = new File(paramString1);
    File file2 = new File(paramString1 + "/" + paramString1);
    if (!file1.exists())
      file1.mkdirs(); 
    if (!file2.exists()) {
      this.logger.info("Create output file with results.");
      this.logger.info("The result is output to " + file2.getCanonicalPath());
      file2.createNewFile();
      FileWriter fileWriter = new FileWriter(paramString1 + "/" + paramString1, Charset.forName("UTF-8"), true);
      PrintWriter printWriter = new PrintWriter(new BufferedWriter(fileWriter));
      printWriter.print("\"PartNumber\",\"ManufacturerName\",\"MatchStatus\",\"MatchValue(PartNumber@ManufacturerName)\"");
      printWriter.write(System.lineSeparator());
      printWriter.close();
    } 
  }
  
  public void postscriptCSV(SearchAndAssignContent paramSearchAndAssignContent) {
    try {
      FileWriter fileWriter = new FileWriter(this.outputdir + "/" + this.outputdir, Charset.forName("UTF-8"), true);
      PrintWriter printWriter = new PrintWriter(new BufferedWriter(fileWriter));
      printWriter.write("\"" + paramSearchAndAssignContent.getPartNumber() + "\",\"" + paramSearchAndAssignContent.getManufuctureName() + "\",\"" + paramSearchAndAssignContent.getMatchStatus() + "\",");
      if (paramSearchAndAssignContent.getMatchValues() != null)
        for (byte b = 0; b < paramSearchAndAssignContent.getMatchValues().size(); b++) {
          String str = paramSearchAndAssignContent.getMatchValues().get(b);
          printWriter.write("\"" + str + "\"");
          if (paramSearchAndAssignContent.getMatchValues().size() != b + 1)
            printWriter.write(","); 
        }  
      printWriter.write(System.lineSeparator());
      printWriter.close();
    } catch (IOException iOException) {
      this.logger.error(iOException.getMessage(), iOException);
    } 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\cor\\utils\SearchAndAssignCreateCSV.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */