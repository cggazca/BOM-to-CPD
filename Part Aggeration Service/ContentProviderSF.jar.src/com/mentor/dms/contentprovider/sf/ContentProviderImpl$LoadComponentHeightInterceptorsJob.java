package com.mentor.dms.contentprovider.sf;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

class LoadComponentHeightInterceptorsJob extends Job {
  public LoadComponentHeightInterceptorsJob() {
    super("Loading SiliconExpert Component Height Interceptors...");
    setSystem(true);
    setPriority(40);
  }
  
  public IStatus run(IProgressMonitor paramIProgressMonitor) {
    ContentProviderImpl.this.loadComponentHeightInterceptors();
    return Status.OK_STATUS;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\ContentProviderImpl$LoadComponentHeightInterceptorsJob.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */