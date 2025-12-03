package com.mentor.dms.contentprovider.core.actions;

import com.mentor.datafusion.oi.model.OIClass;
import com.mentor.dms.contentprovider.core.client.DesktopClient;
import com.mentor.dms.contentprovider.core.plugin.Activator;
import com.mentor.dms.ui.extsearch.ExternalSearchButtonDefinition;
import java.net.URL;
import java.util.Objects;
import javax.swing.Icon;
import javax.swing.ImageIcon;

public class SupplyframeExternalSearchButtonDefinition implements ExternalSearchButtonDefinition {
  private final OIClass oiClass;
  
  public SupplyframeExternalSearchButtonDefinition(OIClass paramOIClass) {
    this.oiClass = Objects.<OIClass>requireNonNull(paramOIClass);
  }
  
  public String getLabel() {
    return "Search in Content Provider";
  }
  
  public void performAction() throws Exception {
    DesktopClient.doSearch(DesktopClient.SearchContext.COMPONENT_ENGINEER);
  }
  
  public Icon getIcon() {
    URL uRL = Activator.class.getResource("images/search_catalog.png");
    ImageIcon imageIcon = null;
    if (uRL != null)
      imageIcon = new ImageIcon(uRL); 
    return imageIcon;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\actions\ContentProviderSearchButtonDefinitionProvider$SupplyframeExternalSearchButtonDefinition.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */