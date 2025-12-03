package com.mentor.dms.contentprovider.sync.gui;

import java.util.Collections;
import java.util.List;

public class ReconcilePropertyTreeNode extends ReconcileTreeNode {
  private String ccpId = "";
  
  private String propId = "";
  
  private String dmn = "";
  
  private String dmsValue = "";
  
  private String ccpValue = "";
  
  private String syncType = "";
  
  private String errorString = null;
  
  private boolean bHighlight = false;
  
  public ReconcilePropertyTreeNode(String paramString) {
    super(paramString);
  }
  
  public String getContentProviderId() {
    return this.ccpId;
  }
  
  public void setContentProviderId(String paramString) {
    this.ccpId = paramString;
  }
  
  public String getPropId() {
    return this.propId;
  }
  
  public void setPropId(String paramString) {
    this.propId = paramString;
  }
  
  public String getDMN() {
    return this.dmn;
  }
  
  public void setDMN(String paramString) {
    this.dmn = paramString;
  }
  
  public String getDMSValue() {
    return this.dmsValue;
  }
  
  public void setDMSValue(String paramString) {
    this.dmsValue = paramString;
  }
  
  public String getContentProviderValue() {
    return this.ccpValue;
  }
  
  public void setContentProviderValue(String paramString) {
    this.ccpValue = paramString;
  }
  
  public String getSyncType() {
    return this.syncType;
  }
  
  public void setSyncType(String paramString) {
    this.syncType = paramString;
  }
  
  public String getErrorString() {
    return this.errorString;
  }
  
  public void setErrorString(String paramString) {
    this.errorString = paramString;
  }
  
  public void setHighlight(boolean paramBoolean) {
    this.bHighlight = paramBoolean;
  }
  
  public boolean isHighlight() {
    return this.bHighlight;
  }
  
  public List<? extends ReconcileTreeNode> getChildren() {
    return Collections.emptyList();
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\sync\gui\ReconcilePropertyTreeNode.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */