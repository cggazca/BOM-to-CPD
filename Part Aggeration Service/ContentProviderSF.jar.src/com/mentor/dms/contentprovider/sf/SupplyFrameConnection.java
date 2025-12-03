package com.mentor.dms.contentprovider.sf;

import com.google.gson.Gson;
import com.mentor.datafusion.utils.logger.MGLogger;
import com.mentor.dms.contentprovider.core.ContentProviderException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class SupplyFrameConnection {
  private static MGLogger log = MGLogger.getLogger(SupplyFrameConnection.class);
  
  protected ContentProviderImpl ccp;
  
  private String accessToken;
  
  private long expiresIn = 0L;
  
  private static final long RENEWAL_TIME = 120L;
  
  public SupplyFrameConnection(ContentProviderImpl paramContentProviderImpl) {
    this.ccp = paramContentProviderImpl;
  }
  
  public String getSearchServiceUserName() throws ContentProviderException {
    String str = this.ccp.getCredential("SEARCH_SERVICE_USERNAME");
    if (str == null || str.trim().isEmpty())
      str = this.ccp.getConfigurationParameter("SEARCH_SERVICE_USERNAME"); 
    if (str == null || str.trim().isEmpty())
      throw new ContentProviderException(this.ccp.getName() + " username not found in Content Provider Configuration."); 
    return str;
  }
  
  public String getSearchServicePassword() throws ContentProviderException {
    String str = this.ccp.getCredential("SEARCH_SERVICE_PASSWORD");
    if (str == null || str.trim().isEmpty())
      str = this.ccp.getConfigurationParameter("SEARCH_SERVICE_PASSWORD"); 
    if (str == null || str.trim().isEmpty())
      throw new ContentProviderException(this.ccp.getName() + " password not found in Content Provider Configuration."); 
    return str;
  }
  
  public String getAccessToken() throws ContentProviderException {
    long l = System.currentTimeMillis();
    return (l < this.expiresIn) ? this.accessToken : getAccessTokenForce();
  }
  
  public String getAccessTokenForce() throws ContentProviderException {
    log.info("get new AccessToken");
    String str1 = getSearchServiceUserName();
    String str2 = getSearchServicePassword();
    String str3 = str1 + ":" + str1;
    String str4 = this.ccp.getConfigurationParameter("AUTH_TOKEN_URL");
    String str5 = this.ccp.getConfigurationParameter("AUTH_SCOPE");
    try {
      CloseableHttpClient closeableHttpClient = (new AggregationServiceWebCall(this.ccp)).createCloseableHttpClient(str4);
      try {
        if (log.isDebugEnabled()) {
          log.debug("url:" + str4);
          log.debug("authScope:" + str5);
        } 
        HttpPost httpPost = new HttpPost(str4);
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
        httpPost.addHeader("Authorization", "Basic " + Base64.getEncoder().encodeToString(str3.getBytes()));
        ArrayList<BasicNameValuePair> arrayList = new ArrayList();
        arrayList.add(new BasicNameValuePair("grant_type", "client_credentials"));
        arrayList.add(new BasicNameValuePair("scope", str5));
        httpPost.setEntity((HttpEntity)new UrlEncodedFormEntity(arrayList));
        CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute((HttpUriRequest)httpPost);
        int i = closeableHttpResponse.getStatusLine().getStatusCode();
        if (log.isDebugEnabled()) {
          log.debug("Response Code : " + i);
          log.debug("Response Body : " + String.valueOf(closeableHttpResponse.getEntity()));
        } 
        if (i != 200) {
          log.error("Failed to get access token.\n" + String.valueOf(httpPost.getURI()));
          log.error("Response Code : " + i);
          throw new ContentProviderException(closeableHttpResponse.getStatusLine().getReasonPhrase());
        } 
        String str = EntityUtils.toString(closeableHttpResponse.getEntity());
        HashMap hashMap = (HashMap)(new Gson()).fromJson(str, HashMap.class);
        this.accessToken = hashMap.get("access_token").toString();
        long l = System.currentTimeMillis();
        double d = ((Double)hashMap.get("expires_in")).doubleValue();
        this.expiresIn = (long)(d * 1000.0D);
        this.expiresIn = l + this.expiresIn - 120000L;
        if (closeableHttpClient != null)
          closeableHttpClient.close(); 
      } catch (Throwable throwable) {
        if (closeableHttpClient != null)
          try {
            closeableHttpClient.close();
          } catch (Throwable throwable1) {
            throwable.addSuppressed(throwable1);
          }  
        throw throwable;
      } 
    } catch (Exception exception) {
      throw new ContentProviderException(exception);
    } 
    return this.accessToken;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\SupplyFrameConnection.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */