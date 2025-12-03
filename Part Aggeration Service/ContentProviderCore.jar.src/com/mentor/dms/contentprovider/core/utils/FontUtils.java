package com.mentor.dms.contentprovider.core.utils;

import java.awt.Component;
import java.awt.FontMetrics;

public class FontUtils {
  public static int getWidth(Component paramComponent, String paramString) {
    FontMetrics fontMetrics = paramComponent.getGraphics().getFontMetrics();
    return fontMetrics.stringWidth(paramString);
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\cor\\utils\FontUtils.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */