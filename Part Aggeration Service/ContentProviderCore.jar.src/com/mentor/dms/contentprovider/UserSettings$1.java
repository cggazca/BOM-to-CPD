package com.mentor.dms.contentprovider;

import java.beans.ExceptionListener;

class null implements ExceptionListener {
  public void exceptionThrown(Exception paramException) {
    UserSettings.log.warn("Unable to load window state:" + paramException.getMessage());
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\UserSettings$1.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */