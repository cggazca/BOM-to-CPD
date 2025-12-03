package com.mentor.dms.contentprovider.sync.gui;

import com.mentor.dms.contentprovider.sync.ContentProviderSync;
import java.util.List;

public abstract class ReconcileTreeNode {
  private String name;
  
  private ContentProviderSync.SyncActionEnum syncAction;
  
  public ReconcileTreeNode(String paramString) {
    this.name = paramString;
    this.syncAction = ContentProviderSync.SyncActionEnum.RECONCILE;
  }
  
  public String getPropName() {
    return this.name;
  }
  
  public void setName(String paramString) {
    this.name = paramString;
  }
  
  public abstract List<? extends ReconcileTreeNode> getChildren();
  
  public String toString() {
    return this.name;
  }
  
  public ContentProviderSync.SyncActionEnum getSyncAction() {
    return this.syncAction;
  }
  
  public void setSyncAction(ContentProviderSync.SyncActionEnum paramSyncActionEnum) {
    this.syncAction = paramSyncActionEnum;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\sync\gui\ReconcileTreeNode.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */