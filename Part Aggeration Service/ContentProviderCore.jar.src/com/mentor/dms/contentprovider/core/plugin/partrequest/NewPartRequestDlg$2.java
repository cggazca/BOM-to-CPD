package com.mentor.dms.contentprovider.core.plugin.partrequest;

import java.awt.event.ActionEvent;
import javax.swing.SwingWorker;

class null extends SwingWorker<Void, Void> {
  protected Void doInBackground() throws Exception {
    if (e.getActionCommand().equals("OK_BUTTON")) {
      NewPartRequestDlg.this.prodLib = NewPartRequestDlg.this.cmbProdLib.getSelectedItem().toString();
      NewPartRequestDlg.this.siteDmnName = ((NewPartRequestDlg.Site)NewPartRequestDlg.this.cmbSite.getSelectedItem()).domainName;
      NewPartRequestDlg.this.buttonPressed = 0;
      NewPartRequestDlg.this.action.dialogClosing(NewPartRequestDlg.this);
    } else if (e.getActionCommand().equals("CANCEL_BUTTON")) {
      NewPartRequestDlg.this.buttonPressed = 2;
      NewPartRequestDlg.this.action.dialogClosing(NewPartRequestDlg.this);
    } 
    return null;
  }
  
  protected void done() {
    NewPartRequestDlg.this.getGlassPane().setVisible(false);
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\plugin\partrequest\NewPartRequestDlg$2.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */