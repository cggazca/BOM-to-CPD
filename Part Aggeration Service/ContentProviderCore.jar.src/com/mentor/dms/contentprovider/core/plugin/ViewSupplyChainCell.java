package com.mentor.dms.contentprovider.core.plugin;

import com.mentor.dms.contentprovider.core.ContentProviderSupplyChain;
import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.Map;

public class ViewSupplyChainCell extends AbstractPushButtonCell implements Comparable<ViewSupplyChainCell> {
  private Collection<ContentProviderSupplyChain> supplychain;
  
  private Map<String, String> manufactureInfo;
  
  public ViewSupplyChainCell(Map<String, String> paramMap1, Map<String, String> paramMap2, Collection<ContentProviderSupplyChain> paramCollection, int paramInt1, int paramInt2) {
    this.supplychain = paramCollection;
    this.manufactureInfo = paramMap1;
    ViewSupplyChainButton viewSupplyChainButton = new ViewSupplyChainButton(paramMap1, paramMap2, paramCollection, paramInt1, paramInt2);
    if (paramCollection != null)
      viewSupplyChainButton.setToolTipText("Show Supply Chain List…"); 
    add(viewSupplyChainButton);
  }
  
  public void actionPerformed(ActionEvent paramActionEvent) {}
  
  public int compareTo(ViewSupplyChainCell paramViewSupplyChainCell) {
    return (this.supplychain != null && paramViewSupplyChainCell.getSupplychain() != null) ? ((this.supplychain.size() > 0 && paramViewSupplyChainCell.getSupplychain().size() > 0) ? 0 : ((paramViewSupplyChainCell.getSupplychain().size() > 0) ? 1 : -1)) : -1;
  }
  
  public Collection<ContentProviderSupplyChain> getSupplychain() {
    return this.supplychain;
  }
  
  public Map<String, String> getManufactureInfo() {
    return this.manufactureInfo;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\plugin\ViewSupplyChainCell.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */