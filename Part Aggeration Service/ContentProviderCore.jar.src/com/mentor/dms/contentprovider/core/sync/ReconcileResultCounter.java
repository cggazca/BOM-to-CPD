package com.mentor.dms.contentprovider.core.sync;

public class ReconcileResultCounter {
  private static int iReconcileTotalCount;
  
  private static int iReconcileCount;
  
  public ReconcileResultCounter(int paramInt) {
    iReconcileTotalCount = paramInt;
    iReconcileCount = 0;
  }
  
  public static int getiReconcileTotalCount() {
    return iReconcileTotalCount;
  }
  
  public static int getiReconcileCount() {
    return iReconcileCount;
  }
  
  public static void addiReconcileCount() {
    iReconcileCount++;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\sync\ReconcileResultCounter.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */