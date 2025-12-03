package com.mentor.dms.contentprovider.core.actions;

import com.mentor.dms.contentprovider.core.AbstractContentProvider;
import com.mentor.dms.contentprovider.core.ContentProviderGlobal;
import com.mentor.dms.contentprovider.core.plugin.searchui.ContentProviderSearchWindow;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

public abstract class AbstractSearchResultsAction extends AbstractAction {
  private String enabledForUsersParam = null;
  
  public AbstractSearchResultsAction(String paramString, ImageIcon paramImageIcon) {
    super(paramString, paramImageIcon);
  }
  
  public AbstractSearchResultsAction(String paramString1, ImageIcon paramImageIcon, String paramString2) {
    super(paramString1, paramImageIcon);
    this.enabledForUsersParam = paramString2;
  }
  
  public void getSelectedResults() {
    ContentProviderSearchWindow.getSelectedResults();
  }
  
  public void setEnabledForUsersParam(String paramString) {
    this.enabledForUsersParam = paramString;
  }
  
  public String getEnabledForUsersParam() {
    return this.enabledForUsersParam;
  }
  
  public void actionPerformed(ActionEvent paramActionEvent) {
    doAction();
  }
  
  public boolean isUserEnabled() {
    if (this.enabledForUsersParam == null)
      return true; 
    AbstractContentProvider abstractContentProvider = ContentProviderSearchWindow.getSelectedContentProvider();
    String str = abstractContentProvider.getConfigurationParameter(this.enabledForUsersParam);
    if (str == null)
      return true; 
    str = str.trim();
    if (str.isEmpty())
      return false; 
    boolean bool = false;
    ContentProviderSearchWindow.getInstance().setCursor(new Cursor(3));
    String[] arrayOfString = str.split("\\s*,\\s*");
    for (String str1 : arrayOfString) {
      try {
        bool = UserUtils.isCurrentUserInGroup(ContentProviderGlobal.getOIObjectManager(), str1);
        if (bool)
          break; 
      } catch (Exception exception) {}
    } 
    ContentProviderSearchWindow.getInstance().setCursor(null);
    return bool;
  }
  
  public abstract void selectionHandler();
  
  public abstract void doAction();
  
  public void setUserEnabled() {
    if (!isEnabled())
      return; 
    boolean bool = isUserEnabled();
    if (!bool)
      setEnabled(false); 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\actions\AbstractSearchResultsAction.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */