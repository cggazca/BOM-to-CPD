package com.mentor.dms.contentprovider;

import com.mentor.datafusion.utils.logger.MGLogger;
import java.beans.ExceptionListener;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class UserSettings {
  private static MGLogger log = MGLogger.getLogger(UserSettings.class);
  
  private static UserSettings obj;
  
  private int searchWindowLocX;
  
  private int searchWindowLocY;
  
  private int searchWindowWidth;
  
  private int searchWindowHeight;
  
  public static UserSettings get() {
    if (obj == null) {
      obj = new UserSettings();
      obj.searchWindowLocX = -1;
      obj.searchWindowLocY = -1;
      obj.searchWindowHeight = 1000;
      obj.searchWindowWidth = 1800;
    } 
    return obj;
  }
  
  public void save() {
    try {
      XMLEncoder xMLEncoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(getUserSettingsFilename())));
      xMLEncoder.writeObject(obj);
      xMLEncoder.close();
    } catch (Exception exception) {
      log.info("Unable to save window state:" + exception.getMessage());
    } 
  }
  
  public void read() {
    try {
      XMLDecoder xMLDecoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(getUserSettingsFilename())), null, null, UserSettings.class.getClassLoader());
      xMLDecoder.setExceptionListener(new ExceptionListener() {
            public void exceptionThrown(Exception param1Exception) {
              UserSettings.log.warn("Unable to load window state:" + param1Exception.getMessage());
            }
          });
      obj = (UserSettings)xMLDecoder.readObject();
      xMLDecoder.close();
    } catch (Exception exception) {
      log.warn("Unable to load window state:" + exception.getMessage());
    } 
  }
  
  public int getSearchWindowLocX() {
    return this.searchWindowLocX;
  }
  
  public void setSearchWindowLocX(int paramInt) {
    this.searchWindowLocX = paramInt;
  }
  
  public int getSearchWindowLocY() {
    return this.searchWindowLocY;
  }
  
  public void setSearchWindowLocY(int paramInt) {
    this.searchWindowLocY = paramInt;
  }
  
  public int getSearchWindowWidth() {
    return this.searchWindowWidth;
  }
  
  public void setSearchWindowWidth(int paramInt) {
    this.searchWindowWidth = paramInt;
  }
  
  public int getSearchWindowHeight() {
    return this.searchWindowHeight;
  }
  
  public void setSearchWindowHeight(int paramInt) {
    this.searchWindowHeight = paramInt;
  }
  
  private static String getUserSettingsFilename() {
    String str = System.getProperty("user.home") + System.getProperty("user.home") + ".dmsccp";
    boolean bool = (new File(str)).mkdirs();
    return str + str + "userSettings";
  }
  
  public String toString() {
    return "" + obj.searchWindowLocX + ", " + obj.searchWindowLocX + ", " + obj.searchWindowLocY + ", " + obj.searchWindowHeight;
  }
  
  public static void main(String[] paramArrayOfString) {
    try {
      get().read();
      System.out.println(obj);
    } catch (Exception exception) {
      exception.printStackTrace();
    } 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\UserSettings.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */