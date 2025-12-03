package com.mentor.dms.contentprovider.core.actions;

import com.mentor.datafusion.oi.OIException;
import com.mentor.datafusion.oi.OIObjectManagerFactory;
import com.mentor.datafusion.oi.internal.OIInternalHelper;
import com.mentor.datafusion.oi.model.OIClass;
import com.mentor.dms.contentprovider.core.client.DesktopClient;
import com.mentor.dms.contentprovider.core.plugin.Activator;
import com.mentor.dms.ui.extsearch.ExternalSearchButtonDefinition;
import com.mentor.dms.ui.extsearch.ExternalSearchButtonDefinitionProvider;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import javax.swing.Icon;
import javax.swing.ImageIcon;

public class ContentProviderSearchButtonDefinitionProvider implements ExternalSearchButtonDefinitionProvider {
  private static Boolean isSupplyChainEnabled = null;
  
  private static boolean isSupplyChainEnabled() {
    if (isSupplyChainEnabled == null)
      try {
        OIObjectManagerFactory oIObjectManagerFactory = Activator.getDefault().getDMSInstance().getOIObjectManagerFactory();
        isSupplyChainEnabled = Boolean.valueOf(OIInternalHelper.checkNamedLicenses(oIObjectManagerFactory, List.of("edmsf")));
      } catch (OIException oIException) {
        return false;
      }  
    return isSupplyChainEnabled.booleanValue();
  }
  
  private static boolean isButtonVisibleForClass(OIClass paramOIClass) {
    String str = paramOIClass.getRootClass().getName();
    return (str.equals("Component") || str.equals("ManufacturerPart"));
  }
  
  public ExternalSearchButtonDefinition getButtonDefinition(OIClass paramOIClass) {
    return (!isSupplyChainEnabled() || !isButtonVisibleForClass(paramOIClass)) ? null : new SupplyframeExternalSearchButtonDefinition(paramOIClass);
  }
  
  public class SupplyframeExternalSearchButtonDefinition implements ExternalSearchButtonDefinition {
    private final OIClass oiClass;
    
    public SupplyframeExternalSearchButtonDefinition(OIClass param1OIClass) {
      this.oiClass = Objects.<OIClass>requireNonNull(param1OIClass);
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
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\actions\ContentProviderSearchButtonDefinitionProvider.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */