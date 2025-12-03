package com.mentor.dms.contentprovider.core;

import com.mentor.datafusion.oi.OIObjectManager;
import com.mentor.dms.contentprovider.core.plugin.Activator;
import com.mentor.dms.ui.DMSInstance;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.JFrame;

public class ContentProviderGlobal {
  private static ExecModeEnum ExecMode = ExecModeEnum.INTERACTIVE;
  
  private static boolean reviewSyncMode = false;
  
  private static JFrame rootFrame = null;
  
  private static DMSInstance dmsInstance = null;
  
  private static OIObjectManager om = null;
  
  public static DMSInstance getDMSInstance() {
    return dmsInstance;
  }
  
  public static void setOIObjectManager(OIObjectManager paramOIObjectManager) {
    om = paramOIObjectManager;
  }
  
  public static OIObjectManager getOIObjectManager() {
    return (om != null) ? om : Activator.getObjectManager();
  }
  
  public static JFrame getRootFrame() {
    return rootFrame;
  }
  
  public static void setInteractiveExecMode() {
    ExecMode = ExecModeEnum.INTERACTIVE;
  }
  
  public static void setBatchExecMode() {
    ExecMode = ExecModeEnum.BATCH;
  }
  
  public static boolean isInteractiveExecMode() {
    return (ExecMode == ExecModeEnum.INTERACTIVE);
  }
  
  public static boolean isBatchExecMode() {
    return (ExecMode == ExecModeEnum.BATCH);
  }
  
  public static void setReviewSyncMode() {
    reviewSyncMode = true;
  }
  
  public static boolean isReviewSyncMode() {
    return reviewSyncMode;
  }
  
  public static Image getAppIconImage() {
    return Toolkit.getDefaultToolkit().getImage(ContentProviderGlobal.class.getResource("images/external_content.png"));
  }
  
  static {
    try {
      Class.forName("org.eclipse.core.runtime.Plugin");
      Activator activator = Activator.getDefault();
      if (activator != null) {
        dmsInstance = activator.getDMSInstance();
        rootFrame = dmsInstance.getJFrame();
      } 
    } catch (ClassNotFoundException classNotFoundException) {}
  }
  
  private enum ExecModeEnum {
    BATCH, INTERACTIVE;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\ContentProviderGlobal.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */