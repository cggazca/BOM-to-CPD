package com.mentor.dms.contentprovider.core.actions;

import com.mentor.datafusion.utils.logger.MGLogger;
import com.mentor.dms.contentprovider.core.ContentProviderGlobal;
import com.mentor.dms.contentprovider.core.plugin.ContentProviderViewCompareWindow;
import com.mentor.dms.contentprovider.core.plugin.ViewCompareDataForExport;
import com.mentor.dms.contentprovider.core.plugin.searchui.ContentProviderSearchWindow;
import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ExportCSVAction extends AbstractDetailViewAction {
  private static MGLogger log = MGLogger.getLogger(ExportCSVAction.class);
  
  public ExportCSVAction() {
    super("CSV Export", (ImageIcon)null);
    setEnabled(true);
  }
  
  private static ImageIcon getImageIcon() {
    Image image = Toolkit.getDefaultToolkit().getImage(ExportCSVAction.class.getResource("images/add_mpn.png"));
    return new ImageIcon(image);
  }
  
  public void selectionHandler() {}
  
  public void doAction() {
    try {
      JFileChooser jFileChooser = new JFileChooser() {
          protected JDialog createDialog(Component param1Component) throws HeadlessException {
            JDialog jDialog = super.createDialog(param1Component);
            jDialog.setIconImage(ContentProviderGlobal.getAppIconImage());
            return jDialog;
          }
        };
      FileNameExtensionFilter fileNameExtensionFilter = new FileNameExtensionFilter("CSV (Comma delimited) (*.csv)", new String[] { "csv" });
      jFileChooser.setFileFilter(fileNameExtensionFilter);
      int i = jFileChooser.showSaveDialog(null);
      if (i == 0) {
        File file = jFileChooser.getSelectedFile();
        String str = file.getAbsolutePath();
        if (!str.toLowerCase().endsWith(".csv")) {
          str = str + ".csv";
          file = new File(str);
        } 
        log.info("CSV EXport to " + file.getCanonicalPath());
        exportCSV(file);
      } else if (i == 1) {
        log.info("FileChooser Canceled.");
      } else if (i == -1) {
        log.info("FileChooser Error.");
      } 
      JOptionPane.showMessageDialog(ContentProviderGlobal.getRootFrame(), "CSV export complete.");
    } catch (IOException iOException) {
      log.error("Failed to export CSV.");
      log.error(iOException.getMessage(), iOException);
    } finally {
      ContentProviderSearchWindow.getInstance().setCursor(null);
    } 
  }
  
  private void exportCSV(File paramFile) throws IOException {
    ViewCompareDataForExport viewCompareDataForExport = ContentProviderViewCompareWindow.getCurrentData();
    try {
      FileWriter fileWriter = new FileWriter(paramFile);
      try {
        log.info("File:" + paramFile.getName());
        String[][] arrayOfString = viewCompareDataForExport.get();
        for (String[] arrayOfString1 : arrayOfString) {
          StringBuilder stringBuilder = new StringBuilder();
          for (byte b = 0; b < arrayOfString1.length; b++) {
            String str = arrayOfString1[b];
            if (str == null) {
              stringBuilder.append("");
            } else if (str.contains(",")) {
              stringBuilder.append("\"").append(str).append("\"");
            } else {
              stringBuilder.append(str);
            } 
            if (b < arrayOfString1.length - 1)
              stringBuilder.append(","); 
          } 
          stringBuilder.append("\n");
          fileWriter.write(stringBuilder.toString());
        } 
        fileWriter.close();
        log.info("CSV export complete.");
        fileWriter.close();
      } catch (Throwable throwable) {
        try {
          fileWriter.close();
        } catch (Throwable throwable1) {
          throwable.addSuppressed(throwable1);
        } 
        throw throwable;
      } 
    } catch (IOException iOException) {
      log.error("Failed to export CSV file. " + String.valueOf(paramFile));
      throw iOException;
    } 
    HashMap hashMap = viewCompareDataForExport.getListPropTable();
    for (String str : hashMap.keySet()) {
      File file = getNewFile(paramFile, str);
      try {
        FileWriter fileWriter = new FileWriter(file);
        try {
          log.info("File:" + file.getName());
          String[][] arrayOfString = (String[][])hashMap.get(str);
          for (String[] arrayOfString1 : arrayOfString) {
            StringBuilder stringBuilder = new StringBuilder();
            for (byte b = 0; b < arrayOfString1.length; b++) {
              String str1 = arrayOfString1[b];
              if (str1 == null) {
                stringBuilder.append("");
              } else if (str1.contains(",")) {
                stringBuilder.append("\"").append(str1).append("\"");
              } else {
                stringBuilder.append(str1);
              } 
              if (b < arrayOfString1.length - 1)
                stringBuilder.append(","); 
            } 
            stringBuilder.append("\n");
            fileWriter.write(stringBuilder.toString());
          } 
          fileWriter.close();
          log.info("CSV export complete.");
          fileWriter.close();
        } catch (Throwable throwable) {
          try {
            fileWriter.close();
          } catch (Throwable throwable1) {
            throwable.addSuppressed(throwable1);
          } 
          throw throwable;
        } 
      } catch (IOException iOException) {
        log.error("Failed to export CSV file (List Property). " + String.valueOf(paramFile));
        throw iOException;
      } 
    } 
  }
  
  private File getNewFile(File paramFile, String paramString) {
    String str3;
    String str1 = paramFile.getParent();
    String str2 = paramFile.getName();
    String str4 = "";
    int i = str2.lastIndexOf('.');
    if (i > 0) {
      str3 = str2.substring(0, i);
      str4 = str2.substring(i);
    } else {
      str3 = str2;
    } 
    return new File(str1, str3 + "_" + str3 + paramString);
  }
  
  private void exportCSVBK2(File paramFile) {
    List list = ContentProviderViewCompareWindow.getCurrentDataBK2();
    try {
      FileWriter fileWriter = new FileWriter(paramFile);
      try {
        for (List<String> list1 : (Iterable<List<String>>)list) {
          StringBuilder stringBuilder = new StringBuilder();
          for (byte b = 0; b < list1.size(); b++) {
            String str = list1.get(b);
            if (str == null) {
              stringBuilder.append("");
            } else if (str.contains(",")) {
              stringBuilder.append("\"").append(str).append("\"");
            } else {
              stringBuilder.append(str);
            } 
            if (b < list1.size() - 1)
              stringBuilder.append(","); 
          } 
          stringBuilder.append("\n");
          fileWriter.write(stringBuilder.toString());
        } 
        fileWriter.close();
        System.out.println("CSV export complete.");
        fileWriter.close();
      } catch (Throwable throwable) {
        try {
          fileWriter.close();
        } catch (Throwable throwable1) {
          throwable.addSuppressed(throwable1);
        } 
        throw throwable;
      } 
    } catch (IOException iOException) {
      System.out.println("Error:" + iOException.getMessage());
    } 
  }
  
  private void exportCSVBK(File paramFile) {
    String[][] arrayOfString = ContentProviderViewCompareWindow.getCurrentDataBK();
    try {
      FileWriter fileWriter = new FileWriter(paramFile);
      for (String[] arrayOfString1 : arrayOfString) {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b = 0; b < arrayOfString1.length; b++) {
          if (arrayOfString1[b].contains(",")) {
            stringBuilder.append("\"").append(arrayOfString1[b]).append("\"");
          } else {
            stringBuilder.append(arrayOfString1[b]);
          } 
          if (b < arrayOfString1.length - 1)
            stringBuilder.append(","); 
        } 
        stringBuilder.append("\n");
        fileWriter.write(stringBuilder.toString());
      } 
      fileWriter.close();
      System.out.println("CSVファイルの出力が完了しました。");
    } catch (IOException iOException) {
      System.out.println("エラーが発生しました: " + iOException.getMessage());
    } 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\actions\ExportCSVAction.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */