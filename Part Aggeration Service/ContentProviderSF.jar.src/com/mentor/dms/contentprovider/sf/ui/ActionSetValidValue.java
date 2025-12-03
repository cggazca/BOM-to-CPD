package com.mentor.dms.contentprovider.sf.ui;

import com.mentor.datafusion.oi.model.OIClass;
import com.mentor.datafusion.oi.model.OIField;
import com.mentor.datafusion.utils.logger.MGLogger;
import com.mentor.dms.contentprovider.core.AbstractContentProvider;
import com.mentor.dms.contentprovider.core.AbstractCriteria;
import com.mentor.dms.contentprovider.core.ContentProviderException;
import com.mentor.dms.contentprovider.core.ContentProviderFactory;
import com.mentor.dms.contentprovider.core.client.PropertyValueSelectionDlg;
import com.mentor.dms.contentprovider.core.config.ContentProviderConfig;
import com.mentor.dms.contentprovider.core.config.ContentProviderConfigComponentCatalog;
import com.mentor.dms.contentprovider.core.config.ContentProviderConfigContentProviderMap;
import com.mentor.dms.contentprovider.core.config.ContentProviderConfigMPNCatalog;
import com.mentor.dms.contentprovider.core.config.ContentProviderConfigPartClass;
import com.mentor.dms.contentprovider.core.config.ContentProviderConfigProperty;
import com.mentor.dms.contentprovider.core.config.ContentProviderConfigPropertyMap;
import com.mentor.dms.contentprovider.core.plugin.searchui.ContentProviderSearchPanel;
import com.mentor.dms.contentprovider.sf.Activator;
import com.mentor.dms.contentprovider.sf.ContentProviderImpl;
import com.mentor.dms.contentprovider.sf.SFValidValues;
import com.mentor.dms.ui.DMSInstance;
import com.mentor.dms.ui.DefaultActionDelegate;
import com.mentor.dms.ui.popupcontext.ContextEvent;
import com.mentor.dms.ui.searchmask.SearchMask;
import java.awt.Cursor;
import java.util.List;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ActionSetValidValue extends DefaultActionDelegate {
  private static MGLogger log = MGLogger.getLogger(ActionSetValidValue.class);
  
  public static ContentProviderConfig cfg = null;
  
  public void actionPerformed(ContextEvent paramContextEvent) {
    DMSInstance dMSInstance = Activator.getDefault().getDMSInstance();
    List<Map.Entry<String, String>> list = actionPerformed(dMSInstance.getJFrame(), paramContextEvent.getOIClass(), paramContextEvent.getOIField());
    if (list == null)
      return; 
    try {
      AbstractContentProvider abstractContentProvider = ContentProviderFactory.getInstance().createContentProvider("SE");
      PropertyValueSelectionDlg propertyValueSelectionDlg = new PropertyValueSelectionDlg(dMSInstance.getJFrame(), abstractContentProvider, paramContextEvent.getOIField().getLabel(), list);
      if (!propertyValueSelectionDlg.isCancelled())
        ((SearchMask)dMSInstance.getSearchMaskManager().getActive()).setRestriction(paramContextEvent.getOIField(), propertyValueSelectionDlg.getRestriction()); 
    } catch (Exception exception) {
      log.error(exception);
    } 
  }
  
  public List<Map.Entry<String, String>> actionPerformed(JFrame paramJFrame, OIClass paramOIClass, OIField<?> paramOIField) {
    List<Map.Entry<String, String>> list = null;
    try {
      ContentProviderImpl contentProviderImpl = (ContentProviderImpl)ContentProviderFactory.getInstance().createContentProvider("SE");
      if (cfg == null)
        cfg = contentProviderImpl.getConfig(); 
      ContentProviderConfigContentProviderMap contentProviderConfigContentProviderMap = null;
      if (paramOIClass.getRootClass().getName().equals("Component")) {
        ContentProviderConfigComponentCatalog contentProviderConfigComponentCatalog = cfg.getComponentCatalogConfigByDMN(paramOIClass.getName());
        if (contentProviderConfigComponentCatalog == null) {
          JOptionPane.showMessageDialog(paramJFrame, paramOIClass.getLabel() + " is not mapped to a SiliconExpert product line.", "Select Silicon Expert Valid Value", 1);
          return null;
        } 
        if (contentProviderConfigComponentCatalog.getContentProviderMaps().size() == 1) {
          contentProviderConfigContentProviderMap = contentProviderConfigComponentCatalog.getContentProviderMaps().iterator().next();
        } else if (contentProviderConfigComponentCatalog.getContentProviderMaps().size() > 1) {
          String[] arrayOfString = new String[contentProviderConfigComponentCatalog.getContentProviderMaps().size()];
          byte b = 0;
          for (ContentProviderConfigContentProviderMap contentProviderConfigContentProviderMap1 : contentProviderConfigComponentCatalog.getContentProviderMaps())
            arrayOfString[b++] = contentProviderConfigContentProviderMap1.getContentProviderLabel(); 
          String str = (String)JOptionPane.showInputDialog(paramJFrame, "Select Part Class:", "Select Part Classs", -1, contentProviderImpl.getIcon(), (Object[])arrayOfString, "ham");
          if (str == null)
            return null; 
          for (ContentProviderConfigContentProviderMap contentProviderConfigContentProviderMap1 : contentProviderConfigComponentCatalog.getContentProviderMaps()) {
            if (contentProviderConfigContentProviderMap1.getContentProviderLabel().equals(str)) {
              contentProviderConfigContentProviderMap = contentProviderConfigContentProviderMap1;
              break;
            } 
          } 
        } 
      } else {
        ContentProviderConfigMPNCatalog contentProviderConfigMPNCatalog = cfg.getMPNCatalogConfigByDMN(paramOIClass.getName());
        if (contentProviderConfigMPNCatalog == null)
          return null; 
        contentProviderConfigContentProviderMap = contentProviderConfigMPNCatalog.getContentProviderMap();
      } 
      if (contentProviderConfigContentProviderMap == null)
        return null; 
      String str1 = contentProviderConfigContentProviderMap.getContentProviderId();
      ContentProviderConfigPropertyMap contentProviderConfigPropertyMap = null;
      contentProviderConfigPropertyMap = contentProviderConfigContentProviderMap.getPropertyMapBySearchDMN(paramOIField.getName());
      if (contentProviderConfigPropertyMap == null)
        return null; 
      String str2 = contentProviderConfigPropertyMap.getContentProviderId();
      ContentProviderConfigPartClass contentProviderConfigPartClass = cfg.getPartClassByContentProviderId(contentProviderConfigContentProviderMap.getContentProviderId());
      ContentProviderConfigProperty contentProviderConfigProperty = contentProviderConfigPartClass.getClassPropertyByContentProviderId(str2);
      if (contentProviderConfigProperty == null || !contentProviderConfigProperty.isSearchable())
        return null; 
      if (paramJFrame != null)
        paramJFrame.setCursor(new Cursor(3)); 
      try {
        ContentProviderSearchPanel contentProviderSearchPanel = new ContentProviderSearchPanel((AbstractContentProvider)contentProviderImpl);
        AbstractCriteria abstractCriteria = contentProviderSearchPanel.getFacetsSearchFilteringCriteria();
        list = SFValidValues.getPropValues(contentProviderImpl, str1, str2, abstractCriteria);
      } finally {
        if (paramJFrame != null)
          paramJFrame.setCursor(null); 
      } 
    } catch (ContentProviderException contentProviderException) {
      JOptionPane.showMessageDialog(paramJFrame, "Error encountered when getting Supplyframe valid values for '" + paramOIField.getLabel() + " was not found in Supplyframe parameter search.", "Select Supplyframe Valid Value", 1);
      return null;
    } 
    return list;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\s\\ui\ActionSetValidValue.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */