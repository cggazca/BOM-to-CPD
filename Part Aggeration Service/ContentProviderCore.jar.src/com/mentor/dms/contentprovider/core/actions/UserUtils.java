package com.mentor.dms.contentprovider.core.actions;

import com.mentor.datafusion.oi.OICursor;
import com.mentor.datafusion.oi.OIObjectManager;
import com.mentor.datafusion.oi.OIObjectManagerFactory;
import com.mentor.datafusion.oi.OIQuery;
import com.mentor.datafusion.oi.login.OIAuthenticate;
import com.mentor.datafusion.oi.login.OIAuthenticateFactory;
import java.util.HashMap;
import java.util.HashSet;
import javax.swing.UIManager;

public class UserUtils {
  private static HashMap<String, Boolean> currUserMap = new HashMap<>();
  
  public static boolean isCurrentUserInGroup(OIObjectManager paramOIObjectManager, String paramString) throws Exception {
    String str = paramOIObjectManager.getObjectManagerFactory().getUserName();
    Boolean bool = currUserMap.get(paramString);
    if (bool != null)
      return bool.booleanValue(); 
    bool = Boolean.valueOf(isUserInGroup(paramOIObjectManager, str, paramString));
    currUserMap.put(paramString, bool);
    return bool.booleanValue();
  }
  
  public static boolean isUserInGroup(OIObjectManager paramOIObjectManager, String paramString1, String paramString2) throws Exception {
    HashSet<String> hashSet = new HashSet();
    return isUserInGroup(paramOIObjectManager, paramString1, paramString2, hashSet);
  }
  
  private static boolean isUserInGroup(OIObjectManager paramOIObjectManager, String paramString1, String paramString2, HashSet<String> paramHashSet) throws Exception {
    if (paramString2 == null || paramString2.trim().isEmpty())
      return true; 
    if (paramString1.equals(paramString2))
      return true; 
    OIQuery oIQuery = paramOIObjectManager.createQuery("User", true);
    oIQuery.addRestriction("Login", paramString1);
    oIQuery.addRestriction("AssignedToThisUser.Groups", "~NULL");
    oIQuery.addColumn("AssignedToThisUser.Groups");
    OICursor oICursor = oIQuery.execute();
    boolean bool = false;
    while (oICursor.next() && !bool) {
      String str = oICursor.getStringified("Groups");
      if (paramString2.equals(str)) {
        bool = true;
        continue;
      } 
      if (!paramHashSet.contains(str)) {
        bool = isUserInGroup(paramOIObjectManager, str, paramString2, paramHashSet);
        paramHashSet.add(str);
      } 
    } 
    oICursor.close();
    return bool;
  }
  
  public static void main(String[] paramArrayOfString) {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception exception) {
      exception.printStackTrace();
    } 
    OIObjectManagerFactory oIObjectManagerFactory = null;
    OIObjectManager oIObjectManager = null;
    try {
      OIAuthenticate oIAuthenticate = OIAuthenticateFactory.createBatchAuthenticate("amazon");
      oIObjectManagerFactory = oIAuthenticate.login("Test App");
      System.out.println("Connected");
      oIObjectManager = oIObjectManagerFactory.createObjectManager();
      System.out.println(isCurrentUserInGroup(oIObjectManager, "FartStick"));
      System.out.println(isCurrentUserInGroup(oIObjectManager, "FartStick"));
    } catch (Exception exception) {
      exception.printStackTrace();
    } finally {
      if (oIObjectManager != null)
        oIObjectManager.close(); 
      if (oIObjectManagerFactory != null)
        oIObjectManagerFactory.close(); 
    } 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\actions\UserUtils.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */