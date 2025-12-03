package com.mentor.dms.contentprovider.core.actions;

import com.mentor.dms.contentprovider.core.ContentProviderGlobal;
import java.awt.Component;
import java.awt.HeadlessException;
import javax.swing.JDialog;
import javax.swing.JFileChooser;

class null extends JFileChooser {
  protected JDialog createDialog(Component paramComponent) throws HeadlessException {
    JDialog jDialog = super.createDialog(paramComponent);
    jDialog.setIconImage(ContentProviderGlobal.getAppIconImage());
    return jDialog;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\actions\ExportCSVAction$1.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */