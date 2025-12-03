package com.mentor.dms.contentprovider.sf;

import com.mentor.datafusion.oi.OIObjectManager;
import com.mentor.datafusion.utils.logger.MGLogger;
import com.mentor.dms.ui.DMSInstance;
import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class Activator extends Plugin {
  private static MGLogger log = MGLogger.getLogger(Activator.class);
  
  private boolean mDmsInstanceObtained;
  
  private DMSInstance dmsInstance;
  
  private BundleContext mContext;
  
  public static final String PLUGIN_ID = "com.mentor.mcd.dms.ContentProvider.se";
  
  private static Activator plugin = null;
  
  private static OIObjectManager oiObjectManager;
  
  public Activator() {
    plugin = this;
  }
  
  public void start(BundleContext paramBundleContext) throws Exception {
    super.start(paramBundleContext);
    this.mContext = paramBundleContext;
    oiObjectManager = getDMSInstance().getObjectManager();
  }
  
  public void stop(BundleContext paramBundleContext) throws Exception {
    plugin = null;
    this.mContext = null;
    super.stop(paramBundleContext);
  }
  
  public static Activator getDefault() {
    return plugin;
  }
  
  public DMSInstance getDMSInstance() {
    if (!this.mDmsInstanceObtained)
      obtainDMSInstance(); 
    return this.dmsInstance;
  }
  
  public static OIObjectManager getObjectManager() {
    return oiObjectManager;
  }
  
  public static void setObjectManager(OIObjectManager paramOIObjectManager) {
    oiObjectManager = paramOIObjectManager;
  }
  
  private void obtainDMSInstance() {
    this.mDmsInstanceObtained = true;
    ServiceReference serviceReference = null;
    if (this.mContext != null)
      serviceReference = this.mContext.getServiceReference(DMSInstance.class.getName()); 
    if (serviceReference != null) {
      this.dmsInstance = (DMSInstance)this.mContext.getService(serviceReference);
    } else {
      log.error("Could not get DMSInstance reference object.");
    } 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\Activator.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */