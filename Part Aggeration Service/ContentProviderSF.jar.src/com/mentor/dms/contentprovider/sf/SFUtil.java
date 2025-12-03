package com.mentor.dms.contentprovider.sf;

public class SFUtil {
  public static String getPartClassID(String paramString) {
    PartClassSearchInfo partClassSearchInfo = getPartClassSearchInfo(paramString);
    return partClassSearchInfo.getID();
  }
  
  public static PartClassSearchInfo getPartClassSearchInfo(String paramString) {
    PartClassSearchInfo partClassSearchInfo = new PartClassSearchInfo();
    partClassSearchInfo.path = paramString;
    partClassSearchInfo.pathArray = paramString.split("\\.");
    return partClassSearchInfo;
  }
  
  public static void main(String[] paramArrayOfString) {
    PartClassSearchInfo partClassSearchInfo = getPartClassSearchInfo("Electrical and Electronic Components");
    System.out.println("ID = " + partClassSearchInfo.getID());
    System.out.println("Level = " + partClassSearchInfo.getLevel());
    partClassSearchInfo = getPartClassSearchInfo("103754");
    System.out.println("ID = " + partClassSearchInfo.getID());
    System.out.println("Level = " + partClassSearchInfo.getLevel());
    partClassSearchInfo = getPartClassSearchInfo("103754.103853");
    System.out.println("ID = " + partClassSearchInfo.getID());
    System.out.println("Level = " + partClassSearchInfo.getLevel());
    partClassSearchInfo = getPartClassSearchInfo("103754.103853.4358");
    System.out.println("ID = " + partClassSearchInfo.getID());
    System.out.println("Level = " + partClassSearchInfo.getLevel());
  }
  
  public static class PartClassSearchInfo {
    private String path;
    
    private String[] pathArray;
    
    public String getID() {
      return this.pathArray[this.pathArray.length - 1];
    }
    
    public String getIDByLevel(int param1Int) {
      return this.pathArray[param1Int - 1];
    }
    
    public int getLevel() {
      return this.pathArray.length;
    }
    
    public String getPath() {
      return this.path;
    }
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\SFUtil.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */