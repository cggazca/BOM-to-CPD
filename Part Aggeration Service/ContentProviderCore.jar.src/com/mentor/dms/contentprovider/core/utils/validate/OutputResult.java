package com.mentor.dms.contentprovider.core.utils.validate;

import com.mentor.datafusion.utils.logger.MGLogger;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;

public class OutputResult {
  public static final String BR = System.getProperty("line.separator");
  
  public static final String LINE = "---------------------------------------";
  
  private static final String SP1 = "  ";
  
  private static final String SP2 = "    ";
  
  private MGLogger logger = MGLogger.getLogger(OutputResult.class);
  
  private String outputdir;
  
  private String fileName;
  
  public void createFile(String paramString1, String paramString2) throws IOException {
    this.logger.info("Create output file with results.");
    this.outputdir = paramString1;
    this.fileName = paramString2;
    File file1 = new File(paramString1);
    File file2 = new File(paramString1 + "/" + paramString1);
    this.logger.info("The result is output to " + file2.getCanonicalPath());
    if (!file1.exists())
      file1.mkdirs(); 
    if (!file2.exists())
      file2.createNewFile(); 
    FileWriter fileWriter = new FileWriter(file2, false);
  }
  
  public void writeHeader(String paramString) {
    try {
      FileWriter fileWriter = new FileWriter(this.outputdir + "/" + this.outputdir, Charset.forName("UTF-8"), true);
      PrintWriter printWriter = new PrintWriter(new BufferedWriter(fileWriter));
      printWriter.write("---------------------------------------" + BR);
      printWriter.write(paramString + paramString);
      printWriter.write("---------------------------------------" + BR);
      printWriter.close();
    } catch (IOException iOException) {
      this.logger.error(iOException.getMessage(), iOException);
    } 
  }
  
  public void write(String paramString) {
    try {
      FileWriter fileWriter = new FileWriter(this.outputdir + "/" + this.outputdir, Charset.forName("UTF-8"), true);
      PrintWriter printWriter = new PrintWriter(new BufferedWriter(fileWriter));
      printWriter.write(paramString + paramString);
      printWriter.close();
    } catch (IOException iOException) {
      this.logger.error(iOException.getMessage(), iOException);
    } 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\cor\\utils\validate\OutputResult.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */