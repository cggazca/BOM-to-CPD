package com.mentor.dms.contentprovider.plugin.searchui;

import com.mentor.dms.contentprovider.UserSettings;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

class null extends WindowAdapter {
  public void windowClosing(WindowEvent paramWindowEvent) {
    Dimension dimension = ContentProviderSearchWindow.this.getSize();
    UserSettings.get().setSearchWindowWidth(dimension.width);
    UserSettings.get().setSearchWindowHeight(dimension.height);
    Point point = ContentProviderSearchWindow.this.getLocation();
    UserSettings.get().setSearchWindowLocX(point.x);
    UserSettings.get().setSearchWindowLocY(point.y);
    UserSettings.get().save();
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\plugin\searchui\ContentProviderSearchWindow$2.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */