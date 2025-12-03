package com.mentor.dms.contentprovider.core;

import java.util.Collection;
import javax.swing.JOptionPane;

public interface IContentProviderPartRequest {
  default void createPartRequest(IContentProviderResultRecord paramIContentProviderResultRecord) {
    if (isMultiplePartsSupported())
      JOptionPane.showMessageDialog(ContentProviderGlobal.getRootFrame(), "Part Requests on single parts only is not supported"); 
  }
  
  default void createPartRequest(Collection<IContentProviderResultRecord> paramCollection) {
    if (!isMultiplePartsSupported())
      JOptionPane.showMessageDialog(ContentProviderGlobal.getRootFrame(), "Part Requests on multiple parts is not supported"); 
  }
  
  default boolean isMultiplePartsSupported() {
    return false;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\IContentProviderPartRequest.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */