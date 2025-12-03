package com.mentor.dms.contentprovider.core;

public class ContentProviderException extends Exception {
  public ContentProviderException() {}
  
  public ContentProviderException(String paramString, Throwable paramThrowable) {
    super(paramString, paramThrowable);
  }
  
  public ContentProviderException(String paramString) {
    super(paramString);
  }
  
  public ContentProviderException(Throwable paramThrowable) {
    super(paramThrowable);
  }
  
  public boolean isSSLCertException() {
    if (getCause() != null) {
      if (getCause() instanceof SSLCertException)
        return true; 
      if (getCause() instanceof javax.net.ssl.SSLHandshakeException)
        return true; 
      if (getCause() instanceof ContentProviderException)
        return ((ContentProviderException)getCause()).isSSLCertException(); 
    } 
    return false;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\ContentProviderException.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */