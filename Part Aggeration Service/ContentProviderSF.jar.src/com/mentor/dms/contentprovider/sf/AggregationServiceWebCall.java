package com.mentor.dms.contentprovider.sf;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.mentor.datafusion.utils.logger.MGLogger;
import com.mentor.dms.contentprovider.core.AbstractContentProvider;
import com.mentor.dms.contentprovider.core.AbstractCriteria;
import com.mentor.dms.contentprovider.core.ComponentListProperty;
import com.mentor.dms.contentprovider.core.ComponentProperty;
import com.mentor.dms.contentprovider.core.ContentProviderAlternate;
import com.mentor.dms.contentprovider.core.ContentProviderAppConfig;
import com.mentor.dms.contentprovider.core.ContentProviderException;
import com.mentor.dms.contentprovider.core.ContentProviderFactory;
import com.mentor.dms.contentprovider.core.ContentProviderManufacturer;
import com.mentor.dms.contentprovider.core.ContentProviderManufacturerProperty;
import com.mentor.dms.contentprovider.core.ContentProviderSubscribedAML;
import com.mentor.dms.contentprovider.core.ContentProviderSubscribedComponent;
import com.mentor.dms.contentprovider.core.ContentProviderSupplyChain;
import com.mentor.dms.contentprovider.core.EmptyContentProviderResults;
import com.mentor.dms.contentprovider.core.IContentProviderResultRecord;
import com.mentor.dms.contentprovider.core.IContentProviderResults;
import com.mentor.dms.contentprovider.core.ListPropertyRow;
import com.mentor.dms.contentprovider.core.SSLCertException;
import com.mentor.dms.contentprovider.core.client.CheckLicense;
import com.mentor.dms.contentprovider.core.config.ContentProviderConfig;
import com.mentor.dms.contentprovider.core.config.ContentProviderConfigPartClass;
import com.mentor.dms.contentprovider.core.config.ContentProviderConfigProperty;
import com.mentor.dms.contentprovider.core.config.SearchCapability;
import com.mentor.dms.contentprovider.core.criterion.Conjunction;
import com.mentor.dms.contentprovider.core.criterion.Disjunction;
import com.mentor.dms.contentprovider.core.criterion.ICriterion;
import com.mentor.dms.contentprovider.core.criterion.IPropertyExpression;
import com.mentor.dms.contentprovider.core.criterion.Junction;
import com.mentor.dms.contentprovider.core.criterion.KeywordExpression;
import com.mentor.dms.contentprovider.core.criterion.LikeExpression;
import com.mentor.dms.contentprovider.core.criterion.SimpleExpression;
import com.mentor.dms.contentprovider.core.plugin.SupplyPropertyEnum;
import com.mentor.dms.contentprovider.core.request.Filter;
import com.mentor.dms.contentprovider.core.utils.validate.CPPartClass;
import com.mentor.dms.contentprovider.core.utils.validate.CPProperty;
import com.mentor.dms.contentprovider.sf.request.FreeTextSearchRequest;
import com.mentor.dms.contentprovider.sf.request.GetNextPageRequest;
import com.mentor.dms.contentprovider.sf.request.GetPartByIDRequest;
import com.mentor.dms.contentprovider.sf.request.ParametricSearchRequest;
import com.mentor.dms.contentprovider.sf.request.data.EnrichingDetailsParameters;
import com.mentor.dms.contentprovider.sf.request.data.EnrichingParameter;
import com.mentor.dms.contentprovider.sf.request.data.FtsParameter;
import com.mentor.dms.contentprovider.sf.request.data.Paging;
import com.mentor.dms.contentprovider.sf.request.data.SearchParameter;
import com.mentor.dms.contentprovider.sf.request.data.filter.FilterFactory;
import com.mentor.dms.contentprovider.sf.response.DefinitionProperty;
import com.mentor.dms.contentprovider.sf.response.EnrichRecord;
import com.mentor.dms.contentprovider.sf.response.EnrichRecordList;
import com.mentor.dms.contentprovider.sf.response.EnrichingResult;
import com.mentor.dms.contentprovider.sf.response.ErrorObject;
import com.mentor.dms.contentprovider.sf.response.PartProperties;
import com.mentor.dms.contentprovider.sf.response.ResponseDataParamSearch;
import com.mentor.dms.contentprovider.sf.response.ResponseDefinitionSearch;
import com.mentor.dms.contentprovider.sf.response.SearchProviderPart;
import com.mentor.dms.contentprovider.sf.response.SearchResult;
import com.mentor.dms.contentprovider.sf.response.facets.ResponseDataGetFacets;
import com.mentor.dms.contentprovider.sf.response.hierarchy.ClassHierarchyData;
import com.mentor.dms.contentprovider.sf.response.hierarchy.PartClass;
import com.mentor.dms.contentprovider.sf.response.partclassdef.Detail;
import com.mentor.dms.contentprovider.sf.response.partclassdef.PartData;
import com.mentor.dms.contentprovider.sf.response.partclassdef.Property;
import com.mentor.dms.contentprovider.sf.response.propertydef.PropData;
import com.mentor.dms.contentprovider.sf.response.propertydef.PropertyDefinitions;
import com.mentor.dms.contentprovider.sf.response.searchcapabilities.PropertySearchCapability;
import com.mentor.dms.contentprovider.sf.response.searchcapabilities.ResponseDataSearchCapabilities;
import com.mentor.dms.contentprovider.sf.responsePartByID.Part;
import com.mentor.dms.contentprovider.sf.responsePartByID.ResponseDataPartByID;
import com.mentor.dms.contentprovider.sf.utils.PropertyUtil;
import com.mentor.dms.contentprovider.sf.utils.SfGson;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.net.ssl.SSLContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.routing.HttpRoutePlanner;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class AggregationServiceWebCall {
  private static MGLogger log = MGLogger.getLogger(AggregationServiceWebCall.class);
  
  private static final String WebServiceBaseURL = "https://app.siliconexpert.com/ProductAPI/";
  
  private HttpClient client = null;
  
  private HttpClientContext httpContext = null;
  
  private final XPath xpath = XPathFactory.newInstance().newXPath();
  
  private static int currPageNumber = -1;
  
  private ContentProviderImpl ccp = null;
  
  private SearchInfo si = null;
  
  protected ContentProviderConfigProperty searchProperty = null;
  
  private final Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
  
  private static String accessToken = null;
  
  private static String baseURL = null;
  
  private static Gson gson = (new GsonBuilder()).setPrettyPrinting().create();
  
  private HashMap<String, ContentProviderSubscribedComponent> subscribedComponentMap;
  
  public boolean isNumeric(String paramString) {
    return (paramString == null) ? false : this.pattern.matcher(paramString).matches();
  }
  
  public AggregationServiceWebCall() {}
  
  public AggregationServiceWebCall(ContentProviderImpl paramContentProviderImpl) {
    this.ccp = paramContentProviderImpl;
  }
  
  public String getWebServiceBaseURL() throws ContentProviderException {
    if (this.ccp == null)
      return "https://app.siliconexpert.com/ProductAPI/"; 
    if (baseURL != null)
      return baseURL; 
    String str1 = getConfigurationParameter("ICARUS_SERVER");
    String str2 = getConfigurationParameter("SEARCH_PROVIDER_ID");
    String str3 = getConfigurationParameter("SEARCH_PROVIDER_VERSION");
    String str4 = getConfigurationParameter("SEARCH_API_VERSION");
    if (str4.equals("v1")) {
      baseURL = "https://api." + str1 + "/api/" + str4 + "/search-providers/" + str2;
      AbstractContentProvider.setV1API();
    } else {
      baseURL = "https://api." + str1 + "/api/" + str4 + "/search-providers/" + str2 + "/" + str3;
    } 
    baseURL = baseURL.trim();
    if (baseURL.isEmpty())
      throw new ContentProviderException("'ICARUS_SERVER' must have a value in the Content Provider toolbox MetaDataMap."); 
    if (!baseURL.endsWith("/"))
      baseURL += "/"; 
    log.info("WebServiceBaseURL()=" + baseURL);
    return baseURL;
  }
  
  public String getConfigurationParameter(String paramString) throws ContentProviderException {
    if (this.ccp == null)
      return null; 
    String str = this.ccp.getConfigurationParameter(paramString);
    if (str == null)
      throw new ContentProviderException("'" + paramString + "' must have a value in the Content Provider toolbox MetaDataMap."); 
    return str;
  }
  
  public Map<String, String> searchDefinitionsEnricher() throws ContentProviderException {
    HashMap<Object, Object> hashMap = new HashMap<>();
    try {
      String str2;
      this.si = new SearchInfo();
      this.si.searchURL = getWebServiceBaseURL();
      this.si.bDefinitionSearch = true;
      String str1 = getConfigurationParameter("SEARCH_API_VERSION");
      if (str1.equals("v1")) {
        str2 = this.si.searchURL + "enrichers/" + this.si.searchURL + "/get-definition";
      } else {
        str2 = this.si.searchURL + "enrichers/" + this.si.searchURL + "/" + getConfigurationParameter("ENRICHER_PROVIDER_ID") + "/get-definition";
      } 
      log.debug("URL:" + str2);
      String str3 = sendGetReq(str2);
      log.debug("---- response json ----");
      log.debug("\n" + str3);
      log.debug("----------------------");
      ResponseDefinitionSearch responseDefinitionSearch = new ResponseDefinitionSearch();
      responseDefinitionSearch = (ResponseDefinitionSearch)SfGson.createGson().fromJson(str3, ResponseDefinitionSearch.class);
      checkErrors(responseDefinitionSearch, true);
      List list = responseDefinitionSearch.getResult().getPropertyDefinitions();
      for (DefinitionProperty definitionProperty : list)
        hashMap.put(definitionProperty.getId(), definitionProperty.getName()); 
    } catch (Exception exception) {
      currPageNumber = 0;
      throw new ContentProviderException(exception.getMessage());
    } 
    return (Map)hashMap;
  }
  
  public IContentProviderResults searchParts(ContentProviderConfigPartClass paramContentProviderConfigPartClass, AbstractCriteria paramAbstractCriteria, boolean paramBoolean) throws ContentProviderException {
    boolean bool = false;
    ContentProviderResultsImpl contentProviderResultsImpl = new ContentProviderResultsImpl();
    log.info("Executing External Content search at " + this.ccp.getName());
    if (paramAbstractCriteria.isKeywordSearch()) {
      log.debug("Quick search");
      return searchPartsFreeText(paramContentProviderConfigPartClass, paramAbstractCriteria, paramBoolean);
    } 
    ParametricSearchRequest parametricSearchRequest = buildParametricSearchRequest(paramContentProviderConfigPartClass, paramAbstractCriteria);
    try {
      log.debug("\n---- request json ----");
      log.debug("\n" + gson.toJson(parametricSearchRequest) + "\n----------------------");
    } catch (Error error) {
      error.printStackTrace();
    } 
    Integer integer = Integer.valueOf(currPageNumber + 1);
    this.si.paramMap.put("pageNumber", integer.toString());
    try {
      if (this.si.bKeywordSearch) {
        String str = this.si.searchURL + "free-text/search";
        log.debug("URL:" + str);
      } else if (this.si.bParametricSearch) {
        String str1 = this.si.searchURL + "parametric/search";
        log.debug("URL:" + str1);
        String str2 = sendPostReq(str1, gson.toJson(parametricSearchRequest));
        log.debug("---- response json ----");
        log.debug("\n" + str2);
        log.debug("----------------------");
        ResponseDataParamSearch responseDataParamSearch = new ResponseDataParamSearch();
        responseDataParamSearch = (ResponseDataParamSearch)SfGson.createGson().fromJson(str2, ResponseDataParamSearch.class);
        checkErrors(responseDataParamSearch, !paramBoolean);
        contentProviderResultsImpl.setReturnCount(responseDataParamSearch.getResult().getTotalCount().intValue());
        List list = responseDataParamSearch.getResult().getResults();
        log.info("Total count:" + responseDataParamSearch.getResult().getTotalCount());
        for (SearchResult searchResult : list) {
          ContentProviderResultRecordImpl contentProviderResultRecordImpl = new ContentProviderResultRecordImpl(this.ccp);
          contentProviderResultsImpl.addResults(contentProviderResultRecordImpl);
          PartProperties partProperties = searchResult.getSearchProviderPart().getProperties();
          Map map = partProperties.getSucceeded();
          Set set = map.keySet();
          String str = searchResult.getSearchProviderPart().getPartClassId();
          ContentProviderConfigPartClass contentProviderConfigPartClass = ContentProviderConfigPartClass.getPartClass(str);
          contentProviderResultRecordImpl.setPartClassID(str);
          contentProviderResultRecordImpl.setPartClassName(contentProviderConfigPartClass.getContentProviderLabel());
          for (String str3 : set) {
            ContentProviderConfigProperty contentProviderConfigProperty = contentProviderConfigPartClass.getCPProperty(str3);
            map.get(str3);
            String str4 = str3;
            String str5 = partProperties.getValue(str3);
            String str6 = contentProviderConfigProperty.getBaseUnits();
            if (str3.equals("750a45c8"))
              contentProviderResultRecordImpl.setDatasheetURL(str5); 
            if (str3.equals("2a2b1476"))
              contentProviderResultRecordImpl.setFindchipsURL(str5); 
            contentProviderResultRecordImpl.addPartProperty("Part Details", new ComponentProperty(str4, str4, str5, str6));
          } 
          EnrichRecordList enrichRecordList = searchResult.getEnrichingResult().getEnrichRecordList();
          if (enrichRecordList != null) {
            List list1 = enrichRecordList.getMatchedRecords();
            for (EnrichRecord enrichRecord : list1) {
              ContentProviderSupplyChain contentProviderSupplyChain = new ContentProviderSupplyChain();
              Map map1 = enrichRecord.getProperties().getSucceeded();
              Set set1 = map1.keySet();
              for (String str3 : set1) {
                Object object = map1.get(str3);
                contentProviderSupplyChain.add(str3, object);
              } 
              contentProviderResultRecordImpl.addSupplyChain(contentProviderSupplyChain);
            } 
          } else {
            Map map1 = searchResult.getEnrichingResult().getFailed();
            for (String str3 : map1.keySet()) {
              ErrorObject errorObject = (ErrorObject)map1.get(str3);
              contentProviderResultsImpl.setWarningMessage(errorObject.getMessage());
              log.warn(errorObject.getCode() + ":" + errorObject.getCode());
            } 
          } 
          contentProviderResultRecordImpl.addPartProperty("Part Details", new ComponentProperty("PartNumber", "PartNumber", searchResult.getSearchProviderPart().getManufacturerPartNumber(), ""));
          contentProviderResultRecordImpl.addPartProperty("Part Details", new ComponentProperty("Manufacturer", "Manufacturer", searchResult.getSearchProviderPart().getManufacturerName(), ""));
          contentProviderResultRecordImpl.addPartProperty("Part Details", new ComponentProperty("DataProviderID", "DataProviderID", searchResult.getSearchProviderPart().getPartId(), ""));
        } 
        if (responseDataParamSearch.getResult().getNextPageToken() != null) {
          currPageNumber++;
          contentProviderResultsImpl.setNextPageToken(responseDataParamSearch.getResult().getNextPageToken());
        } else {
          if (paramBoolean)
            contentProviderResultsImpl.setResultCount(-3); 
          currPageNumber = 0;
        } 
      } 
    } catch (Exception exception) {
      currPageNumber = 0;
      throw new ContentProviderException(exception.getMessage());
    } 
    return contentProviderResultsImpl;
  }
  
  public IContentProviderResults searchPartsFreeText(ContentProviderConfigPartClass paramContentProviderConfigPartClass, AbstractCriteria paramAbstractCriteria, boolean paramBoolean) throws ContentProviderException {
    boolean bool = false;
    ContentProviderResultsImpl contentProviderResultsImpl = new ContentProviderResultsImpl();
    log.info("Executing External Content search(Quick Search) at " + this.ccp.getName());
    FreeTextSearchRequest freeTextSearchRequest = buildFreeTextSearchRequest(paramContentProviderConfigPartClass, paramAbstractCriteria);
    log.debug("\n---- request json ----");
    log.debug("\n" + gson.toJson(freeTextSearchRequest) + "\n----------------------");
    Integer integer = Integer.valueOf(currPageNumber + 1);
    this.si.paramMap.put("pageNumber", integer.toString());
    try {
      String str1 = this.si.searchURL + "free-text/search";
      log.debug("URL:" + str1);
      String str2 = sendPostReq(str1, gson.toJson(freeTextSearchRequest));
      log.debug("---- response json ----");
      log.debug("\n" + str2);
      log.debug("----------------------");
      ResponseDataParamSearch responseDataParamSearch = new ResponseDataParamSearch();
      responseDataParamSearch = (ResponseDataParamSearch)SfGson.createGson().fromJson(str2, ResponseDataParamSearch.class);
      checkErrors(responseDataParamSearch, !paramBoolean);
      contentProviderResultsImpl.setReturnCount(responseDataParamSearch.getResult().getTotalCount().intValue());
      List list = responseDataParamSearch.getResult().getResults();
      log.info("Total count:" + responseDataParamSearch.getResult().getTotalCount());
      for (SearchResult searchResult : list) {
        ContentProviderResultRecordImpl contentProviderResultRecordImpl = new ContentProviderResultRecordImpl(this.ccp);
        contentProviderResultsImpl.addResults(contentProviderResultRecordImpl);
        PartProperties partProperties = searchResult.getSearchProviderPart().getProperties();
        Map map = partProperties.getSucceeded();
        Set set = map.keySet();
        String str = searchResult.getSearchProviderPart().getPartClassId();
        ContentProviderConfigPartClass contentProviderConfigPartClass = ContentProviderConfigPartClass.getPartClass(str);
        contentProviderResultRecordImpl.setPartClassID(str);
        contentProviderResultRecordImpl.setPartClassName(contentProviderConfigPartClass.getContentProviderLabel());
        for (String str3 : set) {
          ContentProviderConfigProperty contentProviderConfigProperty = contentProviderConfigPartClass.getCPProperty(str3);
          map.get(str3);
          String str4 = str3;
          String str5 = partProperties.getValue(str3);
          String str6 = contentProviderConfigProperty.getBaseUnits();
          if (str3.equals("750a45c8"))
            contentProviderResultRecordImpl.setDatasheetURL(str5); 
          if (str3.equals("2a2b1476"))
            contentProviderResultRecordImpl.setFindchipsURL(str5); 
          contentProviderResultRecordImpl.addPartProperty("Part Details", new ComponentProperty(str4, str4, str5, str6));
        } 
        List list1 = searchResult.getEnrichingResult().getEnrichRecordList().getMatchedRecords();
        for (EnrichRecord enrichRecord : list1) {
          ContentProviderSupplyChain contentProviderSupplyChain = new ContentProviderSupplyChain();
          Map map1 = enrichRecord.getProperties().getSucceeded();
          Set set1 = map1.keySet();
          for (String str3 : set1) {
            Object object = map1.get(str3);
            contentProviderSupplyChain.add(str3, object);
          } 
          contentProviderResultRecordImpl.addSupplyChain(contentProviderSupplyChain);
        } 
        log.debug("PartNumber:" + searchResult.getSearchProviderPart().getManufacturerPartNumber() + "  Manufacturer:" + searchResult.getSearchProviderPart().getManufacturerName());
        contentProviderResultRecordImpl.addPartProperty("Part Details", new ComponentProperty("PartNumber", "PartNumber", searchResult.getSearchProviderPart().getManufacturerPartNumber(), ""));
        contentProviderResultRecordImpl.addPartProperty("Part Details", new ComponentProperty("Manufacturer", "Manufacturer", searchResult.getSearchProviderPart().getManufacturerName(), ""));
        contentProviderResultRecordImpl.addPartProperty("Part Details", new ComponentProperty("DataProviderID", "DataProviderID", searchResult.getSearchProviderPart().getPartId(), ""));
      } 
      if (responseDataParamSearch.getResult().getNextPageToken() != null) {
        currPageNumber++;
        contentProviderResultsImpl.setNextPageToken(responseDataParamSearch.getResult().getNextPageToken());
      } else {
        if (paramBoolean)
          contentProviderResultsImpl.setResultCount(-3); 
        currPageNumber = 0;
      } 
    } catch (Exception exception) {
      currPageNumber = 0;
      throw new ContentProviderException(exception.getMessage());
    } 
    return contentProviderResultsImpl;
  }
  
  public IContentProviderResults searchNextParts(ContentProviderConfigPartClass paramContentProviderConfigPartClass, String paramString, AbstractCriteria paramAbstractCriteria) throws ContentProviderException {
    ContentProviderResultsImpl contentProviderResultsImpl = new ContentProviderResultsImpl();
    log.info("Executing External Content search(NextPage) at " + this.ccp.getName());
    GetNextPageRequest getNextPageRequest = buildNextPageRequest(paramContentProviderConfigPartClass, paramString);
    log.debug("\n---- request json ----");
    log.debug("\n" + gson.toJson(getNextPageRequest) + "\n----------------------");
    Integer integer = Integer.valueOf(currPageNumber + 1);
    this.si.paramMap.put("pageNumber", integer.toString());
    try {
      if (!this.si.bPartNumberSearch && !this.si.bDescriptionSearch && !this.si.bKeywordSearch && this.si.bParametricSearch) {
        String str1 = this.si.searchURL + "parametric/get-next-page";
        if (paramAbstractCriteria.isKeywordSearch())
          str1 = this.si.searchURL + "free-text/get-next-page"; 
        log.debug("URL:" + str1);
        String str2 = sendPostReq(str1, gson.toJson(getNextPageRequest));
        log.debug("---- response json ----");
        log.debug("\n" + str2);
        log.debug("----------------------");
        ResponseDataParamSearch responseDataParamSearch = new ResponseDataParamSearch();
        responseDataParamSearch = (ResponseDataParamSearch)SfGson.createGson().fromJson(str2, ResponseDataParamSearch.class);
        checkErrors(responseDataParamSearch, false);
        contentProviderResultsImpl.setReturnCount(responseDataParamSearch.getResult().getTotalCount().intValue());
        List list = responseDataParamSearch.getResult().getResults();
        for (SearchResult searchResult : list) {
          ContentProviderResultRecordImpl contentProviderResultRecordImpl = new ContentProviderResultRecordImpl(this.ccp);
          contentProviderResultsImpl.addResults(contentProviderResultRecordImpl);
          PartProperties partProperties = searchResult.getSearchProviderPart().getProperties();
          Map map = partProperties.getSucceeded();
          Set set = map.keySet();
          String str = searchResult.getSearchProviderPart().getPartClassId();
          ContentProviderConfigPartClass contentProviderConfigPartClass = ContentProviderConfigPartClass.getPartClass(str);
          contentProviderResultRecordImpl.setPartClassID(str);
          contentProviderResultRecordImpl.setPartClassName(contentProviderConfigPartClass.getContentProviderLabel());
          for (String str3 : set) {
            ContentProviderConfigProperty contentProviderConfigProperty = contentProviderConfigPartClass.getCPProperty(str3);
            String str4 = str3;
            String str5 = partProperties.getValue(str3);
            String str6 = contentProviderConfigProperty.getBaseUnits();
            if (str3.equals("750a45c8"))
              contentProviderResultRecordImpl.setDatasheetURL(str5); 
            if (str3.equals("2a2b1476"))
              contentProviderResultRecordImpl.setFindchipsURL(str5); 
            contentProviderResultRecordImpl.addPartProperty("Part Details", new ComponentProperty(str4, str4, str5, str6));
          } 
          List list1 = searchResult.getEnrichingResult().getEnrichRecordList().getMatchedRecords();
          for (EnrichRecord enrichRecord : list1) {
            ContentProviderSupplyChain contentProviderSupplyChain = new ContentProviderSupplyChain();
            Map map1 = enrichRecord.getProperties().getSucceeded();
            Set set1 = map1.keySet();
            for (String str3 : set1) {
              Object object = map1.get(str3);
              contentProviderSupplyChain.add(str3, object);
            } 
            contentProviderResultRecordImpl.addSupplyChain(contentProviderSupplyChain);
          } 
          log.debug("PartNumber:" + searchResult.getSearchProviderPart().getManufacturerPartNumber() + "  Manufacturer:" + searchResult.getSearchProviderPart().getManufacturerName());
          contentProviderResultRecordImpl.addPartProperty("Part Details", new ComponentProperty("PartNumber", "PartNumber", searchResult.getSearchProviderPart().getManufacturerPartNumber(), ""));
          contentProviderResultRecordImpl.addPartProperty("Part Details", new ComponentProperty("Manufacturer", "Manufacturer", searchResult.getSearchProviderPart().getManufacturerName(), ""));
          contentProviderResultRecordImpl.addPartProperty("Part Details", new ComponentProperty("DataProviderID", "DataProviderID", searchResult.getSearchProviderPart().getPartId(), ""));
        } 
        if (responseDataParamSearch.getResult().getNextPageToken() != null) {
          currPageNumber++;
          contentProviderResultsImpl.setNextPageToken(responseDataParamSearch.getResult().getNextPageToken());
        } else {
          contentProviderResultsImpl.setResultCount(-3);
          currPageNumber = 0;
        } 
      } 
    } catch (Exception exception) {
      currPageNumber = 0;
      throw new ContentProviderException(exception.getMessage());
    } 
    return contentProviderResultsImpl;
  }
  
  public IContentProviderResults searchParts(ContentProviderConfigPartClass paramContentProviderConfigPartClass, AbstractCriteria paramAbstractCriteria) throws ContentProviderException {
    currPageNumber = 0;
    return searchParts(paramContentProviderConfigPartClass, paramAbstractCriteria, false);
  }
  
  public static boolean hasNextParts() {
    return (currPageNumber > 0);
  }
  
  public IContentProviderResults getNextParts(ContentProviderConfigPartClass paramContentProviderConfigPartClass, String paramString, AbstractCriteria paramAbstractCriteria) throws ContentProviderException {
    return (IContentProviderResults)((currPageNumber == 0) ? new EmptyContentProviderResults() : searchNextParts(paramContentProviderConfigPartClass, paramString, paramAbstractCriteria));
  }
  
  protected IContentProviderResultRecord getPart(IContentProviderResultRecord paramIContentProviderResultRecord) throws ContentProviderException {
    return getPartByID(paramIContentProviderResultRecord.getObjectID());
  }
  
  public IContentProviderResults searchExactMatchSF(String paramString1, String paramString2) throws ContentProviderException {
    this.si = new SearchInfo();
    this.si.searchURL = getWebServiceBaseURL();
    ContentProviderResultsImpl contentProviderResultsImpl = new ContentProviderResultsImpl();
    String str1 = "{\"searchParameters\":{\"partClassId\":\"76f2225d\",\"customParameters\":{},\"outputs\":[\"6230417e\",\"d8ac8dcc\",\"750a45c8\",\"2a2b1476\",\"e1aa6f26\"],\"sort\":[],\"paging\":{\"requestedPageSize\":10},\"filter\":{\"__logicalOperator__\":\"And\",\"__expression__\":\"LogicalExpression\",\"left\":{\"__valueOperator__\":\"SmartMatch\",\"__expression__\":\"ValueExpression\",\"propertyId\":\"6230417e\",\"term\":\"" + escape(paramString2) + "\"},\"right\":{\"__valueOperator__\":\"SmartMatch\",\"__expression__\":\"ValueExpression\",\"propertyId\":\"d8ac8dcc\",\"term\":\"" + escape(paramString1) + "\"}}}}";
    log.debug("---- request json ----");
    log.debug("\n" + str1);
    log.debug("----------------------");
    String str2 = this.si.searchURL + "parametric/search";
    log.debug("URL:" + str2);
    String str3 = sendPostReq(str2, str1);
    log.debug("---- response json ----");
    log.debug("\n" + str3);
    log.debug("----------------------");
    ResponseDataParamSearch responseDataParamSearch = new ResponseDataParamSearch();
    responseDataParamSearch = (ResponseDataParamSearch)SfGson.createGson().fromJson(str3, ResponseDataParamSearch.class);
    checkErrors(responseDataParamSearch, false);
    contentProviderResultsImpl.setReturnCount(responseDataParamSearch.getResult().getTotalCount().intValue());
    List list = responseDataParamSearch.getResult().getResults();
    for (SearchResult searchResult : list)
      storingResults(searchResult, contentProviderResultsImpl); 
    while (responseDataParamSearch.getResult().getNextPageToken() != null) {
      str2 = this.si.searchURL + "parametric/get-next-page";
      str1 = "{\"pageToken\":\"" + responseDataParamSearch.getResult().getNextPageToken() + "\"}";
      log.debug("URL:" + str2);
      str3 = sendPostReq(str2, str1);
      log.debug("---- response json ----");
      log.debug("\n" + str3);
      log.debug("----------------------");
      responseDataParamSearch = new ResponseDataParamSearch();
      responseDataParamSearch = (ResponseDataParamSearch)SfGson.createGson().fromJson(str3, ResponseDataParamSearch.class);
      checkErrors(responseDataParamSearch, false);
      list = responseDataParamSearch.getResult().getResults();
      for (SearchResult searchResult : list)
        storingResults(searchResult, contentProviderResultsImpl); 
    } 
    return contentProviderResultsImpl;
  }
  
  public IContentProviderResults searchExactMatchSF(String paramString) throws ContentProviderException {
    log.info("Starts the initial search.");
    this.si = new SearchInfo();
    this.si.searchURL = getWebServiceBaseURL();
    ContentProviderResultsImpl contentProviderResultsImpl = new ContentProviderResultsImpl();
    String str1 = "{\"searchParameters\":{\"partClassId\":\"76f2225d\",\"customParameters\":{},\"outputs\":[\"6230417e\",\"d8ac8dcc\",\"750a45c8\",\"2a2b1476\",\"e1aa6f26\"],\"sort\":[],\"paging\":{\"requestedPageSize\":50},\"filter\":{\"__valueOperator__\":\"SmartMatch\",\"__expression__\":\"ValueExpression\",\"propertyId\":\"d8ac8dcc\",\"term\":\"" + escape(paramString) + "\"}}}";
    String str2 = this.si.searchURL + "parametric/search";
    log.debug("URL:" + str2);
    String str3 = sendPostReq(str2, str1);
    log.debug("---- response json ----");
    log.debug("\n" + str3);
    log.debug("----------------------");
    ResponseDataParamSearch responseDataParamSearch = new ResponseDataParamSearch();
    responseDataParamSearch = (ResponseDataParamSearch)SfGson.createGson().fromJson(str3, ResponseDataParamSearch.class);
    checkErrors(responseDataParamSearch, false);
    contentProviderResultsImpl.setReturnCount(responseDataParamSearch.getResult().getTotalCount().intValue());
    List list = responseDataParamSearch.getResult().getResults();
    for (SearchResult searchResult : list)
      storingResults(searchResult, contentProviderResultsImpl); 
    while (responseDataParamSearch.getResult().getNextPageToken() != null) {
      log.info("Start the second and subsequent searches.");
      str2 = this.si.searchURL + "parametric/get-next-page";
      str1 = "{\"pageToken\":\"" + responseDataParamSearch.getResult().getNextPageToken() + "\"}";
      log.debug("URL:" + str2);
      str3 = sendPostReq(str2, str1);
      log.debug("---- response json ----");
      log.debug("\n" + str3);
      log.debug("----------------------");
      responseDataParamSearch = new ResponseDataParamSearch();
      responseDataParamSearch = (ResponseDataParamSearch)SfGson.createGson().fromJson(str3, ResponseDataParamSearch.class);
      checkErrors(responseDataParamSearch, false);
      list = responseDataParamSearch.getResult().getResults();
      for (SearchResult searchResult : list)
        storingResults(searchResult, contentProviderResultsImpl); 
    } 
    return contentProviderResultsImpl;
  }
  
  private void storingResults(SearchResult paramSearchResult, ContentProviderResultsImpl paramContentProviderResultsImpl) {
    ContentProviderResultRecordImpl contentProviderResultRecordImpl = new ContentProviderResultRecordImpl(this.ccp);
    paramContentProviderResultsImpl.addResults(contentProviderResultRecordImpl);
    PartProperties partProperties = paramSearchResult.getSearchProviderPart().getProperties();
    Map map = partProperties.getSucceeded();
    Set set = map.keySet();
    String str = paramSearchResult.getSearchProviderPart().getPartClassId();
    ContentProviderConfigPartClass contentProviderConfigPartClass = ContentProviderConfigPartClass.getPartClass(str);
    contentProviderResultRecordImpl.setPartClassID(str);
    contentProviderResultRecordImpl.setPartClassName(contentProviderConfigPartClass.getContentProviderLabel());
    for (String str1 : set) {
      ContentProviderConfigProperty contentProviderConfigProperty = contentProviderConfigPartClass.getCPProperty(str1);
      map.get(str1);
      String str2 = str1;
      String str3 = partProperties.getValue(str1);
      String str4 = contentProviderConfigProperty.getBaseUnits();
      if (str1.equals("750a45c8"))
        contentProviderResultRecordImpl.setDatasheetURL(str3); 
      if (str1.equals("2a2b1476"))
        contentProviderResultRecordImpl.setFindchipsURL(str3); 
      contentProviderResultRecordImpl.addPartProperty("Part Details", new ComponentProperty(str2, str2, str3, str4));
    } 
    EnrichingResult enrichingResult = paramSearchResult.getEnrichingResult();
    if (enrichingResult == null)
      return; 
    EnrichRecordList enrichRecordList = enrichingResult.getEnrichRecordList();
    if (enrichRecordList != null) {
      List list = enrichRecordList.getMatchedRecords();
      for (EnrichRecord enrichRecord : list) {
        ContentProviderSupplyChain contentProviderSupplyChain = new ContentProviderSupplyChain();
        Map map1 = enrichRecord.getProperties().getSucceeded();
        Set set1 = map1.keySet();
        for (String str1 : set1) {
          Object object = map1.get(str1);
          contentProviderSupplyChain.add(str1, object);
        } 
        contentProviderResultRecordImpl.addSupplyChain(contentProviderSupplyChain);
      } 
    } else {
      Map map1 = enrichingResult.getFailed();
      for (String str1 : map1.keySet()) {
        ErrorObject errorObject = (ErrorObject)map1.get(str1);
        paramContentProviderResultsImpl.setWarningMessage(errorObject.getMessage());
        log.warn(errorObject.getCode() + ":" + errorObject.getCode());
      } 
    } 
  }
  
  private String escape(String paramString) {
    StringBuffer stringBuffer = new StringBuffer(paramString.length() + 4);
    for (byte b = 0; b < paramString.length(); b++) {
      char c = paramString.charAt(b);
      if (c == '\\' || c == '”')
        stringBuffer.append('\\'); 
      stringBuffer.append(c);
    } 
    return stringBuffer.toString();
  }
  
  public IContentProviderResultRecord searchExactMatch(String paramString1, String paramString2) throws ContentProviderException {
    ContentProviderResultRecordImpl contentProviderResultRecordImpl = null;
    this.si = new SearchInfo();
    this.si.searchURL = getWebServiceBaseURL() + "search/listPartSearch";
    String str = "[{\"partNumber\":\"" + paramString1 + "\",\"manufacturer\":\"" + paramString2 + "\"}]";
    this.si.paramMap.put("partNumber", str);
    try {
      InputStream inputStream = callSE(this.si.searchURL, this.si.paramMap);
      Document document = parseXML(inputStream);
      checkErrors(document, false);
      String str1 = "/ServiceResult/Result/PartData/PartList/PartDto";
      NodeList nodeList = (NodeList)this.xpath.evaluate(str1, document, XPathConstants.NODESET);
      if (nodeList.getLength() == 1) {
        Node node = nodeList.item(0);
        String str2 = this.xpath.evaluate("MatchRating/text()", node);
        if (str2.equals("Exact") || str2.equals("Alias")) {
          contentProviderResultRecordImpl = new ContentProviderResultRecordImpl(this.ccp);
          NodeList nodeList1 = nodeList.item(0).getChildNodes();
          for (byte b = 0; b < nodeList1.getLength(); b++) {
            Element element = (Element)nodeList1.item(b);
            String str3 = element.getNodeName();
            if (str3.equals("PlName")) {
              contentProviderResultRecordImpl.setPartClassName(element.getTextContent());
            } else if (str3.equals("TaxonomyPathID")) {
              contentProviderResultRecordImpl.setPartClassID(element.getTextContent().replaceAll(" > ", "."));
            } else if (str3.equals("Datasheet")) {
              contentProviderResultRecordImpl.setDatasheetURL(element.getTextContent());
            } else {
              if (str3.equals("ComID")) {
                str3 = "DataProviderID";
              } else if (str3.equals("Description")) {
                str3 = "PartDescription";
              } else if (str3.equals("Lifecycle")) {
                str3 = "PartStatus";
              } else if (str3.equals("RoHS")) {
                str3 = "RoHSStatus";
              } 
              contentProviderResultRecordImpl.addPartProperty("Part Details", new ComponentProperty(str3, str3, element.getTextContent(), ""));
            } 
          } 
        } 
      } 
    } catch (Exception exception) {
      throw new ContentProviderException(exception.getMessage());
    } 
    return (IContentProviderResultRecord)contentProviderResultRecordImpl;
  }
  
  public InputStream getPartDetailsXML(String paramString) throws ContentProviderException {
    HashMap<Object, Object> hashMap = new HashMap<>();
    hashMap.put("comIds", paramString);
    return callSE(getWebServiceBaseURL() + "search/partDetail", (Map)hashMap);
  }
  
  public Document parseXML(InputStream paramInputStream) throws ContentProviderException {
    Document document = null;
    try {
      DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      document = documentBuilder.parse(paramInputStream);
    } catch (Exception exception) {
      String str = document.getTextContent().replaceFirst("^<!.*>[\\n\\r]*", "");
      throw new ContentProviderException(str);
    } 
    if (document.getDocumentElement().getNodeName().equalsIgnoreCase("HTML"))
      throw new ContentProviderException(document.getTextContent()); 
    return document;
  }
  
  public IContentProviderResultRecord getPartByID(String paramString) throws ContentProviderException {
    log.info("getPartByID [" + paramString + "]");
    this.si = new SearchInfo();
    this.si.searchURL = getWebServiceBaseURL();
    this.si.bPartNumberSearch = true;
    ContentProviderResultRecordImpl contentProviderResultRecordImpl = new ContentProviderResultRecordImpl(this.ccp);
    contentProviderResultRecordImpl.addCategories();
    GetPartByIDRequest getPartByIDRequest = new GetPartByIDRequest();
    getPartByIDRequest.addPartID(paramString);
    EnrichingDetailsParameters enrichingDetailsParameters = createEnrichingDetailedParameter();
    getPartByIDRequest.addEnrichingDetailsParameters(enrichingDetailsParameters);
    log.debug("\n---- request json ----");
    log.debug("\n" + gson.toJson(getPartByIDRequest) + "\n----------------------");
    try {
      String str1 = this.si.searchURL + "part-details/get-by-ids";
      log.debug("URL:" + str1);
      String str2 = sendPostReq(str1, gson.toJson(getPartByIDRequest));
      log.debug("---- response json ----");
      log.debug("\n" + str2);
      log.debug("----------------------");
      ResponseDataPartByID responseDataPartByID = new ResponseDataPartByID();
      responseDataPartByID = (ResponseDataPartByID)SfGson.createGson().fromJson(str2, ResponseDataPartByID.class);
      checkErrors(responseDataPartByID, true);
      Part part = responseDataPartByID.getResult().getParts();
      if (part.getSucceeded().size() > 0) {
        SearchProviderPart searchProviderPart = part.getPartRecord().getSearchProviderPart();
        PartProperties partProperties = part.getPartRecord().getSearchProviderPart().getProperties();
        String str = part.getPartRecord().getSearchProviderPart().getPartClassId();
        Map map = partProperties.getSucceeded();
        Set set = map.keySet();
        ContentProviderConfigPartClass contentProviderConfigPartClass = ContentProviderConfigPartClass.getPartClass(str);
        contentProviderResultRecordImpl.setPartClassID(str);
        contentProviderResultRecordImpl.setPartClassName(contentProviderConfigPartClass.getContentProviderLabel());
        for (String str3 : set) {
          boolean bool = true;
          ContentProviderConfigProperty contentProviderConfigProperty = contentProviderConfigPartClass.getCPProperty(str3);
          String str4 = str3;
          String str5 = partProperties.getValue(str3);
          String str6 = "";
          String str7 = "";
          if (contentProviderConfigProperty != null) {
            str6 = contentProviderConfigProperty.getBaseUnits();
            str7 = contentProviderConfigProperty.getContentProviderLabel();
          } 
          if (str3.equals("750a45c8"))
            contentProviderResultRecordImpl.setDatasheetURL(str5); 
          if (str3.equals("2a2b1476"))
            contentProviderResultRecordImpl.setFindchipsURL(str5); 
          if (str3.equals("4f456504"))
            contentProviderResultRecordImpl.setProductImageURL(str5); 
          if (str3.equals("f5724997")) {
            bool = false;
            List list = (List)partProperties.get(str3);
            ComponentListProperty componentListProperty = new ComponentListProperty(str4, str7, list, bool);
            for (ListPropertyRow listPropertyRow : componentListProperty.getList()) {
              ContentProviderAlternate contentProviderAlternate = new ContentProviderAlternate();
              contentProviderResultRecordImpl.addAlternate(contentProviderAlternate);
              contentProviderAlternate.setManufacturerName(listPropertyRow.getValue("Manufacturer"));
              contentProviderAlternate.setPartNumber(listPropertyRow.getValue("Manufacturer Part Number"));
              contentProviderAlternate.setBaseNumber(listPropertyRow.getValue("Base Number"));
              contentProviderAlternate.setDatasheetUrl(listPropertyRow.getValue("Current Datasheet Url"));
              contentProviderAlternate.setDescription(listPropertyRow.getValue("Description"));
              contentProviderAlternate.setLifeCycleCode(listPropertyRow.getValue("Part Life Cycle Code"));
              contentProviderAlternate.setMfrid(listPropertyRow.getValue("mfrid"));
              contentProviderAlternate.setUid(listPropertyRow.getValue("uid"));
            } 
          } 
          if (partProperties.get(str3) instanceof List) {
            contentProviderResultRecordImpl.addPartProperty("List", (ComponentProperty)new ComponentListProperty(str4, str7, (List)partProperties.get(str3), bool));
            continue;
          } 
          contentProviderResultRecordImpl.addPartProperty("Part Details", new ComponentProperty(str4, str7, str5, str6, bool));
        } 
        contentProviderResultRecordImpl.addPartProperty("Part Details", new ComponentProperty("DataProviderID", "DataProviderID", searchProviderPart.getPartId(), ""));
        contentProviderResultRecordImpl.addPartProperty(null, new ComponentProperty("PartNumber", "PartNumber", searchProviderPart.getManufacturerPartNumber(), ""));
        contentProviderResultRecordImpl.addPartProperty(null, new ComponentProperty("Manufacturer", "Manufacturer", searchProviderPart.getManufacturerName(), "", false));
      } 
      if (part.getFailed().size() > 0) {
        log.error(part.getFailedPartRecord().getCode());
        log.error(part.getFailedPartRecord().getMessage());
      } 
      contentProviderResultRecordImpl.setResponseRaw(str2);
    } catch (Exception exception) {
      log.error(exception.getMessage(), exception);
      throw new ContentProviderException(exception.getMessage());
    } 
    return (IContentProviderResultRecord)contentProviderResultRecordImpl;
  }
  
  public IContentProviderResultRecord JsonToResult(String paramString) {
    ContentProviderResultRecordImpl contentProviderResultRecordImpl = new ContentProviderResultRecordImpl(this.ccp);
    contentProviderResultRecordImpl.addCategories();
    ResponseDataPartByID responseDataPartByID = (ResponseDataPartByID)SfGson.createGson().fromJson(paramString, ResponseDataPartByID.class);
    Part part = responseDataPartByID.getResult().getParts();
    if (part.getSucceeded().size() > 0) {
      SearchProviderPart searchProviderPart = part.getPartRecord().getSearchProviderPart();
      PartProperties partProperties = part.getPartRecord().getSearchProviderPart().getProperties();
      Map map = partProperties.getSucceeded();
      Set set = map.keySet();
      ContentProviderConfigPartClass contentProviderConfigPartClass = ContentProviderConfigPartClass.getPartClass(searchProviderPart.getPartClassId());
      for (String str1 : set) {
        ContentProviderConfigProperty contentProviderConfigProperty = contentProviderConfigPartClass.getCPProperty(str1);
        String str2 = str1;
        String str3 = partProperties.getValue(str1);
        String str4 = contentProviderConfigProperty.getBaseUnits();
        if (partProperties.get(str1) instanceof List) {
          contentProviderResultRecordImpl.addPartProperty("List", (ComponentProperty)new ComponentListProperty(str2, str2, (List)partProperties.get(str1)));
          continue;
        } 
        contentProviderResultRecordImpl.addPartProperty("Part Details", new ComponentProperty(str2, str2, str3, str4));
      } 
      contentProviderResultRecordImpl.addPartProperty("Part Details", new ComponentProperty("DataProviderID", "DataProviderID", searchProviderPart.getPartId(), ""));
      log.debug("PartNumber:" + searchProviderPart.getManufacturerPartNumber() + "  Manufacturer:" + searchProviderPart.getManufacturerName());
      contentProviderResultRecordImpl.addPartProperty("Part Details", new ComponentProperty("PartNumber", "PartNumber", searchProviderPart.getManufacturerPartNumber(), ""));
      contentProviderResultRecordImpl.addPartProperty("Part Details", new ComponentProperty("Manufacturer", "Manufacturer", searchProviderPart.getManufacturerName(), ""));
      contentProviderResultRecordImpl.setPartClassID(searchProviderPart.getPartClassId());
    } 
    contentProviderResultRecordImpl.setResponseRaw(paramString);
    return (IContentProviderResultRecord)contentProviderResultRecordImpl;
  }
  
  public List<Map.Entry<String, String>> getFacets(String paramString1, String paramString2, AbstractCriteria paramAbstractCriteria) throws ContentProviderException {
    ParametricSearchRequest parametricSearchRequest = buildFacetsRequest(paramString1, paramAbstractCriteria);
    Filter filter = parametricSearchRequest.getSearchParameters().getFilter();
    String str = "";
    if (filter == null) {
      str = "{\"partClassId\": \"" + paramString1 + "\",\"customParameters\": {},\"facets\": [\"" + paramString2 + "\"]}";
    } else {
      str = "{\"partClassId\": \"" + paramString1 + "\",\"customParameters\": {},\"filter\":" + gson.toJson(filter) + ",\"facets\": [\"" + paramString2 + "\"]}";
    } 
    try {
      String str1 = getWebServiceBaseURL() + "parametric/get-facets";
      log.debug("URL:" + str1);
      String str2 = sendPostReq(str1, str);
      log.debug("---- response json ----");
      log.debug("\n" + str2);
      log.debug("----------------------");
      ResponseDataGetFacets responseDataGetFacets = new ResponseDataGetFacets();
      responseDataGetFacets = (ResponseDataGetFacets)SfGson.createGson().fromJson(str2, ResponseDataGetFacets.class);
      if (responseDataGetFacets.getResult() == null)
        throw new ContentProviderException(responseDataGetFacets.getError().getMessage()); 
      if (responseDataGetFacets.getResult().getFacets().getFailed().values().size() > 0) {
        StringBuffer stringBuffer = new StringBuffer();
        for (ErrorObject errorObject : responseDataGetFacets.getResult().getFacets().getFailed().values()) {
          if (!stringBuffer.toString().equals(""))
            stringBuffer.append("\r\n"); 
          stringBuffer.append(((ErrorObject)errorObject).getMessage());
        } 
        throw new ContentProviderException(stringBuffer.toString());
      } 
      ContentProviderConfig contentProviderConfig = this.ccp.getConfig();
      SearchCapability searchCapability = (SearchCapability)contentProviderConfig.getSearchCapabilityMap().get(paramString2);
      boolean bool = searchCapability.isCaseSensitive();
      return responseDataGetFacets.getResult().getFacets().getValues(paramString2, bool);
    } catch (ContentProviderException contentProviderException) {
      throw contentProviderException;
    } catch (Exception exception) {
      currPageNumber = 0;
      throw new ContentProviderException(exception.getMessage());
    } 
  }
  
  private ParametricSearchRequest buildFacetsRequest(String paramString, AbstractCriteria paramAbstractCriteria) throws ContentProviderException {
    SearchParameter searchParameter = new SearchParameter(paramString);
    ParametricSearchRequest parametricSearchRequest = new ParametricSearchRequest(searchParameter);
    if (paramAbstractCriteria.getOutputFields().size() > 0) {
      for (String str : paramAbstractCriteria.getOutputFields())
        searchParameter.addOutputs(str); 
      log.debug("Output Fields:" + (String)paramAbstractCriteria.getOutputFields().stream().collect(Collectors.joining(",")));
    } 
    if (paramAbstractCriteria.getCriterion().size() > 0)
      checkSearchCapabilities(paramAbstractCriteria); 
    if (paramAbstractCriteria.getCriterion().size() > 0) {
      ICriterion iCriterion;
      Conjunction conjunction = null;
      if (paramAbstractCriteria.getCriterion().size() > 1) {
        conjunction = paramAbstractCriteria.getCriterionAsConjunction();
      } else {
        Iterator<ICriterion> iterator = paramAbstractCriteria.getCriterion().iterator();
        if (iterator.hasNext()) {
          ICriterion iCriterion1 = iterator.next();
          iCriterion = iCriterion1;
        } 
      } 
      try {
        log.debug("criterion:" + iCriterion.getClass().getName());
        searchParameter.setFilter(FilterFactory.create(iCriterion));
      } catch (Error error) {
        log.error(error.getMessage(), error);
      } 
    } 
    return parametricSearchRequest;
  }
  
  public ResponseDataPartByID gePartByIDJson(String paramString) throws ContentProviderException {
    ResponseDataPartByID responseDataPartByID = new ResponseDataPartByID();
    this.si = new SearchInfo();
    this.si.searchURL = getWebServiceBaseURL();
    this.si.bPartNumberSearch = true;
    ContentProviderResultRecordImpl contentProviderResultRecordImpl = new ContentProviderResultRecordImpl(this.ccp);
    contentProviderResultRecordImpl.addCategories();
    GetPartByIDRequest getPartByIDRequest = new GetPartByIDRequest();
    getPartByIDRequest.addPartID(paramString);
    log.debug("\n---- request json ----");
    log.debug("\n" + gson.toJson(getPartByIDRequest) + "\n----------------------");
    try {
      String str1 = this.si.searchURL + "part-details/get-by-ids";
      log.debug("URL:" + str1);
      String str2 = sendPostReq(str1, gson.toJson(getPartByIDRequest));
      log.debug("---- response json ----");
      log.debug("\n" + str2);
      log.debug("----------------------");
      responseDataPartByID = (ResponseDataPartByID)SfGson.createGson().fromJson(str2, ResponseDataPartByID.class);
      checkErrors(responseDataPartByID, true);
    } catch (Exception exception) {
      log.error(exception.getMessage(), exception);
      throw new ContentProviderException(exception.getMessage());
    } 
    return responseDataPartByID;
  }
  
  public IContentProviderResultRecord getPart(Map<String, String> paramMap) throws ContentProviderException {
    return getPartByID(paramMap.get("e1aa6f26"));
  }
  
  private GetNextPageRequest buildNextPageRequest(ContentProviderConfigPartClass paramContentProviderConfigPartClass, String paramString) throws ContentProviderException {
    this.si = new SearchInfo();
    this.si.searchURL = getWebServiceBaseURL();
    this.si.bParametricSearch = true;
    return new GetNextPageRequest(paramString);
  }
  
  private ParametricSearchRequest buildParametricSearchRequest(ContentProviderConfigPartClass paramContentProviderConfigPartClass, AbstractCriteria paramAbstractCriteria) throws ContentProviderException {
    this.si = new SearchInfo();
    this.si.searchURL = getWebServiceBaseURL();
    this.si.bParametricSearch = true;
    SearchParameter searchParameter = new SearchParameter(paramContentProviderConfigPartClass.getContentProviderId());
    ParametricSearchRequest parametricSearchRequest = new ParametricSearchRequest(searchParameter);
    if (paramAbstractCriteria.getOutputFields().size() > 0) {
      for (String str : paramAbstractCriteria.getOutputFields())
        searchParameter.addOutputs(str); 
      log.debug("Output Fields:" + (String)paramAbstractCriteria.getOutputFields().stream().collect(Collectors.joining(",")));
    } 
    searchParameter.setPaging(new Paging(getPageSize()));
    if (paramContentProviderConfigPartClass.getContentProviderId().equals(this.ccp.getRootClassDomain()) && paramAbstractCriteria.getCriterion().size() == 0)
      throw new ContentProviderException("Enter search criteria in the search for \"Supplyframe Part Classes Root\"."); 
    if (paramAbstractCriteria.getCriterion().size() > 0)
      checkSearchCapabilities(paramAbstractCriteria); 
    if (paramAbstractCriteria.getCriterion().size() > 0) {
      ICriterion iCriterion;
      Conjunction conjunction = null;
      if (paramAbstractCriteria.getCriterion().size() > 1) {
        conjunction = paramAbstractCriteria.getCriterionAsConjunction();
      } else {
        Iterator<ICriterion> iterator = paramAbstractCriteria.getCriterion().iterator();
        if (iterator.hasNext()) {
          ICriterion iCriterion1 = iterator.next();
          iCriterion = iCriterion1;
        } 
      } 
      try {
        searchParameter.setFilter(FilterFactory.create(iCriterion));
      } catch (Error error) {
        log.error(error.getMessage(), error);
      } 
    } 
    EnrichingParameter enrichingParameter = createEnrichingParameter();
    parametricSearchRequest.addEnrichingParameter(enrichingParameter);
    return parametricSearchRequest;
  }
  
  private void checkSearchCapabilities(AbstractCriteria paramAbstractCriteria) throws ContentProviderException {
    StringBuilder stringBuilder = new StringBuilder();
    HashMap hashMap = this.ccp.getConfig().getSearchCapabilityMap();
    for (ICriterion iCriterion : paramAbstractCriteria.getCriterion()) {
      if (iCriterion instanceof SimpleExpression) {
        SimpleExpression simpleExpression = (SimpleExpression)iCriterion;
        log.debug("SimpleExpression:" + simpleExpression.getProperty().getContentProviderLabel() + "=" + String.valueOf(simpleExpression.getValue()));
        SearchCapability searchCapability = (SearchCapability)hashMap.get(simpleExpression.getProperty().getContentProviderId());
        if (!searchOperatorAvailable(simpleExpression, searchCapability))
          stringBuilder.append(simpleExpression.getOp().toString() + " [" + simpleExpression.getOp().toString() + "].\n"); 
        continue;
      } 
      if (iCriterion instanceof Disjunction) {
        Disjunction disjunction = (Disjunction)iCriterion;
        log.debug("Disjunction");
        for (ICriterion iCriterion1 : disjunction.getCriterion()) {
          if (iCriterion1 instanceof SimpleExpression) {
            SimpleExpression simpleExpression = (SimpleExpression)iCriterion1;
            log.debug("  SimpleExpression:" + simpleExpression.getProperty().getContentProviderLabel() + "=" + String.valueOf(simpleExpression.getValue()));
            SearchCapability searchCapability = (SearchCapability)hashMap.get(simpleExpression.getProperty().getContentProviderId());
            if (!searchCapability.isAnyOf() && !searchCapability.isAnyOfSmartMatch()) {
              stringBuilder.append("OR [" + simpleExpression.getProperty().getContentProviderLabel() + "].\n");
              break;
            } 
            if (!searchOperatorAvailable(simpleExpression, searchCapability))
              stringBuilder.append(simpleExpression.getOp().toString() + " [" + simpleExpression.getOp().toString() + "].\n"); 
          } 
        } 
        continue;
      } 
      if (iCriterion instanceof Conjunction) {
        Conjunction conjunction = (Conjunction)iCriterion;
        log.debug("Conjunction");
        for (ICriterion iCriterion1 : conjunction.getCriterion()) {
          if (iCriterion1 instanceof SimpleExpression) {
            SimpleExpression simpleExpression = (SimpleExpression)iCriterion1;
            log.debug("  SimpleExpression:" + simpleExpression.getProperty().getContentProviderLabel() + "=" + String.valueOf(simpleExpression.getValue()));
            SearchCapability searchCapability = (SearchCapability)hashMap.get(simpleExpression.getProperty().getContentProviderId());
            if (!searchOperatorAvailable(simpleExpression, searchCapability))
              stringBuilder.append(simpleExpression.getOp().toString() + " [" + simpleExpression.getOp().toString() + "].\n"); 
          } 
        } 
        continue;
      } 
      throw new ContentProviderException("Invalid criterion.");
    } 
    if (0 < stringBuilder.length())
      throw new ContentProviderException("Invalid criterion.\n" + stringBuilder.toString()); 
  }
  
  private boolean searchOperatorAvailable(SimpleExpression paramSimpleExpression, SearchCapability paramSearchCapability) {
    SimpleExpression.OperatorType operatorType = paramSimpleExpression.getOp();
    paramSimpleExpression.setSmartMatch(paramSearchCapability.isSmartMatch());
    if (paramSimpleExpression.getProperty().getContentProviderId().equals(PropertyUtil.getProperty("BOOL_FIELDS")))
      paramSimpleExpression.setTypeBoolean(true); 
    return (SimpleExpression.OperatorType.EQUAL == operatorType) ? ((paramSearchCapability.isEquality() || paramSearchCapability.isSmartMatch())) : ((SimpleExpression.OperatorType.GREATER_THAN == operatorType) ? paramSearchCapability.isRangeExclusive() : ((SimpleExpression.OperatorType.LESS_THAN == operatorType) ? paramSearchCapability.isRangeExclusive() : ((SimpleExpression.OperatorType.GREATER_THAN_OR_EQUAL == operatorType) ? paramSearchCapability.isRangeInclusive() : ((SimpleExpression.OperatorType.LESS_THAN_OR_EQUAL == operatorType) ? paramSearchCapability.isRangeInclusive() : false))));
  }
  
  private FreeTextSearchRequest buildFreeTextSearchRequest(ContentProviderConfigPartClass paramContentProviderConfigPartClass, AbstractCriteria paramAbstractCriteria) throws ContentProviderException {
    this.si = new SearchInfo();
    this.si.searchURL = getWebServiceBaseURL();
    this.si.bKeywordSearch = true;
    FtsParameter ftsParameter = new FtsParameter();
    FreeTextSearchRequest freeTextSearchRequest = new FreeTextSearchRequest(ftsParameter);
    ftsParameter.setPaging(new Paging(getPageSize()));
    ftsParameter.setPartClassID(paramContentProviderConfigPartClass.getContentProviderId());
    if (paramAbstractCriteria.getCriterion().size() > 0)
      for (ICriterion iCriterion : paramAbstractCriteria.getCriterion()) {
        try {
          log.debug("criterion:" + iCriterion.getClass().getName());
          if (iCriterion instanceof KeywordExpression) {
            log.debug("criterion is KeywordExpression");
            KeywordExpression keywordExpression = (KeywordExpression)iCriterion;
            ftsParameter.setTerm(keywordExpression.getValue());
            continue;
          } 
          log.warn("NOT SUPPORT. " + iCriterion.getClass().getName());
        } catch (Error error) {
          log.error(error.getMessage(), error);
        } 
      }  
    if (paramAbstractCriteria.getOutputFields().size() > 0) {
      for (String str : paramAbstractCriteria.getOutputFields())
        ftsParameter.addOutputs(str); 
      log.debug("Output Fields:" + (String)paramAbstractCriteria.getOutputFields().stream().collect(Collectors.joining(",")));
    } 
    EnrichingParameter enrichingParameter = createEnrichingParameter();
    freeTextSearchRequest.addEnrichingParameter(enrichingParameter);
    return freeTextSearchRequest;
  }
  
  private EnrichingParameter createEnrichingParameter() throws ContentProviderException {
    EnrichingParameter enrichingParameter = null;
    String str1 = getConfigurationParameter("ENRICHER_PROVIDER_ID");
    String str2 = getConfigurationParameter("ENRICHER_PROVIDER_VERSION");
    if (str1 != null) {
      enrichingParameter = new EnrichingParameter(str1, str2);
      for (SupplyPropertyEnum supplyPropertyEnum : SupplyPropertyEnum.values())
        enrichingParameter.addOutputs(supplyPropertyEnum.getId()); 
    } 
    return enrichingParameter;
  }
  
  private EnrichingDetailsParameters createEnrichingDetailedParameter() throws ContentProviderException {
    EnrichingDetailsParameters enrichingDetailsParameters = null;
    String str1 = getConfigurationParameter("ENRICHER_PROVIDER_ID");
    String str2 = getConfigurationParameter("ENRICHER_PROVIDER_VERSION");
    if (str1 != null) {
      enrichingDetailsParameters = new EnrichingDetailsParameters(str1, str2);
      for (SupplyPropertyEnum supplyPropertyEnum : SupplyPropertyEnum.values())
        enrichingDetailsParameters.addOutputs(supplyPropertyEnum.getId()); 
    } 
    return enrichingDetailsParameters;
  }
  
  protected void buildPartSearchURL(AbstractCriteria paramAbstractCriteria) throws ContentProviderException {
    this.si.searchURL = getWebServiceBaseURL() + "search/listPartSearch";
    this.si.paramMap.put("wildcardSingle", "?");
    this.si.paramMap.put("wildcardMulti", "*");
    try {
      if (paramAbstractCriteria.getCriterion().size() > 0) {
        String str1 = "[{";
        String str2 = "";
        for (ICriterion iCriterion : paramAbstractCriteria.getCriterion()) {
          String str = getPartSearchString(iCriterion);
          if (!str.isEmpty()) {
            str1 = str1 + str1 + str2;
            str2 = ",";
          } 
        } 
        str1 = str1 + "}]";
        this.si.paramMap.put("partNumber", str1);
      } 
    } catch (Exception exception) {
      throw new ContentProviderException(exception.getMessage());
    } 
  }
  
  protected void buildDescriptionSearchURL(AbstractCriteria paramAbstractCriteria) throws ContentProviderException {
    this.si.searchURL = getWebServiceBaseURL() + "search/partsearch";
    try {
      if (paramAbstractCriteria.getCriterion().size() > 0)
        for (ICriterion iCriterion : paramAbstractCriteria.getCriterion())
          getDescriptonSearchParams(iCriterion);  
    } catch (Exception exception) {
      throw new ContentProviderException(exception.getMessage());
    } 
  }
  
  protected void buildKeywordSearchURL(AbstractCriteria paramAbstractCriteria) throws ContentProviderException {
    this.si.searchURL = getWebServiceBaseURL() + "search/partsearch";
    for (ICriterion iCriterion : paramAbstractCriteria.getCriterion()) {
      if (iCriterion instanceof KeywordExpression) {
        KeywordExpression keywordExpression = (KeywordExpression)iCriterion;
        this.si.paramMap.put("description", keywordExpression.getValue());
        return;
      } 
    } 
  }
  
  protected String getPartSearchString(ICriterion paramICriterion) throws ContentProviderException {
    String str = "";
    if (paramICriterion instanceof SimpleExpression) {
      SimpleExpression simpleExpression = (SimpleExpression)paramICriterion;
      if (!simpleExpression.getOp().equals(SimpleExpression.OperatorType.EQUAL))
        throw new ContentProviderException(String.valueOf(simpleExpression.getOp()) + " operations not supported in " + String.valueOf(simpleExpression.getOp()) + "."); 
      String str1 = simpleExpression.getPropertyValue().getValue().toString();
      str1 = addSiliconExpertEscapes(str1);
      if (simpleExpression.getProperty().getContentProviderId().equals("PartNumber")) {
        str = "\"partNumber\":\"" + str1 + "\"";
      } else if (simpleExpression.getProperty().getContentProviderId().equals("Manufacturer")) {
        str = "\"manufacturer\":\"" + str1 + "\"";
      } else if (simpleExpression.getProperty().getContentProviderId().equals("PartDescription")) {
        str = "\"description\":\"" + str1 + "\"";
      } 
    } else if (paramICriterion instanceof LikeExpression) {
      LikeExpression likeExpression = (LikeExpression)paramICriterion;
      String str1 = likeExpression.getProperty().getContentProviderId();
      if (!str1.equals("PartNumber") && !str1.equals("Manufacturer") && !str1.equals("PartDescription"))
        return str; 
      if (!str1.equals("PartNumber"))
        throw new ContentProviderException("Silicon Expert Part Search only supports wildcard on Part Number."); 
      str = "\"partNumber\":\"";
      for (String str2 : likeExpression.getValue()) {
        if (str2.equals("*")) {
          str = str + "*";
          continue;
        } 
        if (str2.equals("?")) {
          str = str + "?";
          continue;
        } 
        str = str + str;
      } 
      str = str + "\"";
    } else {
      throw new ContentProviderException(paramICriterion.getClass().getSimpleName() + " restrictions not supported in SiliconExpert Part Search.");
    } 
    return str;
  }
  
  protected void getDescriptonSearchParams(ICriterion paramICriterion) throws ContentProviderException {
    if (paramICriterion instanceof SimpleExpression) {
      SimpleExpression simpleExpression = (SimpleExpression)paramICriterion;
      if (!simpleExpression.getOp().equals(SimpleExpression.OperatorType.EQUAL))
        throw new ContentProviderException(String.valueOf(simpleExpression.getOp()) + " operations not supported in " + String.valueOf(simpleExpression.getOp()) + " keyword (description) search."); 
      String str = simpleExpression.getPropertyValue().getValue().toString();
      str = addSiliconExpertEscapes(str);
      if (simpleExpression.getProperty().getContentProviderId().equals("PartDescription")) {
        this.si.paramMap.put("description", str);
      } else if (simpleExpression.getProperty().getContentProviderId().equals("Manufacturer")) {
        this.si.paramMap.put("mfr", str);
      } 
    } else {
      throw new ContentProviderException(paramICriterion.getClass().getSimpleName() + " restrictions not supported in " + paramICriterion.getClass().getSimpleName() + " keyword (description) search.");
    } 
  }
  
  protected String validValue(String paramString1, String paramString2) throws ContentProviderException {
    return paramString2;
  }
  
  protected String getParametricSearchString(String paramString, ICriterion paramICriterion) throws ContentProviderException {
    String str = "";
    if (paramICriterion instanceof Disjunction) {
      String str1 = "";
      for (ICriterion iCriterion : ((Junction)paramICriterion).getCriterion()) {
        str = str + str;
        str1 = ",";
        str = str + str;
      } 
    } else if (paramICriterion instanceof SimpleExpression) {
      SimpleExpression simpleExpression = (SimpleExpression)paramICriterion;
      if (!simpleExpression.getOp().equals(SimpleExpression.OperatorType.EQUAL))
        throw new ContentProviderException(String.valueOf(simpleExpression.getOp()) + " operations not supported in " + String.valueOf(simpleExpression.getOp()) + " Parametric Search."); 
      this.searchProperty = simpleExpression.getProperty();
      String str1 = simpleExpression.getPropertyValue().getValue().toString();
      if (simpleExpression.getPropertyValue().getUnits() != null)
        str1 = str1 + str1; 
      str1 = validValue(paramString, str1);
      str1 = addSiliconExpertEscapes(str1);
      str = str + "\"" + str + "\"";
    } else {
      throw new ContentProviderException(paramICriterion.getClass().getSimpleName() + " restrictions not supported in SiliconExpert Parametric Search.");
    } 
    return str;
  }
  
  private static String addSiliconExpertEscapes(String paramString) {
    null = paramString.replaceAll("\\\\", "\\\\\\\\");
    return null.replaceAll("\"", "\\\\\"");
  }
  
  public void authenticate(String paramString1, String paramString2, boolean paramBoolean1, String paramString3, String paramString4, boolean paramBoolean2, String paramString5) throws ContentProviderException {
    try {
      Document document;
      this.httpContext = HttpClientContext.create();
      if (!paramBoolean1 && !paramBoolean2) {
        this.client = (HttpClient)HttpClients.custom().setDefaultRequestConfig(RequestConfig.custom().setCookieSpec("standard").build()).build();
      } else if (paramBoolean1 && !paramBoolean2) {
        HttpHost httpHost = new HttpHost(paramString3, Integer.parseInt(paramString4));
        DefaultProxyRoutePlanner defaultProxyRoutePlanner = new DefaultProxyRoutePlanner(httpHost);
        this.client = (HttpClient)HttpClients.custom().setDefaultRequestConfig(RequestConfig.custom().setCookieSpec("standard").build()).setRoutePlanner((HttpRoutePlanner)defaultProxyRoutePlanner).build();
      } else if (!paramBoolean1 && paramBoolean2) {
        SSLContext sSLContext = getSSLContext(paramString5);
        SSLConnectionSocketFactory sSLConnectionSocketFactory = new SSLConnectionSocketFactory(sSLContext);
        this.client = (HttpClient)HttpClients.custom().setDefaultRequestConfig(RequestConfig.custom().setCookieSpec("standard").build()).setSSLSocketFactory((LayeredConnectionSocketFactory)sSLConnectionSocketFactory).build();
      } else if (paramBoolean1 && paramBoolean2) {
        HttpHost httpHost = new HttpHost(paramString3, Integer.parseInt(paramString4));
        DefaultProxyRoutePlanner defaultProxyRoutePlanner = new DefaultProxyRoutePlanner(httpHost);
        SSLContext sSLContext = getSSLContext(paramString5);
        SSLConnectionSocketFactory sSLConnectionSocketFactory = new SSLConnectionSocketFactory(sSLContext);
        this.client = (HttpClient)HttpClients.custom().setDefaultRequestConfig(RequestConfig.custom().setCookieSpec("standard").build()).setRoutePlanner((HttpRoutePlanner)defaultProxyRoutePlanner).setSSLSocketFactory((LayeredConnectionSocketFactory)sSLConnectionSocketFactory).build();
      } 
      HttpPost httpPost = new HttpPost(getWebServiceBaseURL() + "search/authenticateUser");
      ArrayList<BasicNameValuePair> arrayList = new ArrayList();
      arrayList.add(new BasicNameValuePair("login", paramString1));
      arrayList.add(new BasicNameValuePair("apiKey", paramString2));
      arrayList.add(new BasicNameValuePair("fmt", "xml"));
      httpPost.setEntity((HttpEntity)new UrlEncodedFormEntity(arrayList));
      HttpResponse httpResponse = this.client.execute((HttpUriRequest)httpPost, (HttpContext)this.httpContext);
      DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      String str = convertStreamToString(httpResponse.getEntity().getContent());
      try {
        document = documentBuilder.parse(new InputSource(new StringReader(str)));
      } catch (SAXException sAXException) {
        String str1 = str.replaceFirst("^<!.*>[\\n\\r]*", "");
        throw new ContentProviderException(str1);
      } 
      checkErrors(document, true);
      EntityUtils.consume(httpResponse.getEntity());
    } catch (IOException|javax.xml.parsers.ParserConfigurationException|SSLCertException iOException) {
      throw new ContentProviderException(iOException.getMessage());
    } 
  }
  
  private static SSLContext getSSLContext(String paramString) throws SSLCertException {
    try {
      KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
      FileInputStream fileInputStream = new FileInputStream(new File(System.getProperty("user.home"), "CCPCert.jks"));
      try {
        keyStore.load(fileInputStream, paramString.toCharArray());
      } finally {
        fileInputStream.close();
      } 
      return SSLContexts.custom().loadTrustMaterial(keyStore, null).build();
    } catch (Exception exception) {
      throw new SSLCertException(exception);
    } 
  }
  
  private String urlEncode(String paramString) throws ContentProviderException {
    try {
      return URLEncoder.encode(paramString, "UTF-8");
    } catch (Exception exception) {
      throw new ContentProviderException(exception.getMessage());
    } 
  }
  
  public void checkErrors(ResponseDefinitionSearch paramResponseDefinitionSearch, boolean paramBoolean) throws ContentProviderException {
    try {
      Boolean bool = paramResponseDefinitionSearch.getSuccess();
      if (bool == null)
        throw new ContentProviderException("Invalid response returned from web service API."); 
      if (!bool.booleanValue()) {
        String str1 = paramResponseDefinitionSearch.getError().getCode();
        log.debug("Return Code = " + str1);
        String str2 = paramResponseDefinitionSearch.getError().getMessage();
        throw new ContentProviderException(str2);
      } 
      List list = paramResponseDefinitionSearch.getResult().getPropertyDefinitions();
      if (list == null || list.size() == 0)
        throw new ContentProviderException("get-definition data not found"); 
    } catch (Exception exception) {
      throw new ContentProviderException(exception.getMessage());
    } 
  }
  
  public void checkErrors(ResponseDataParamSearch paramResponseDataParamSearch, boolean paramBoolean) throws ContentProviderException {
    try {
      Boolean bool = paramResponseDataParamSearch.getSuccess();
      if (bool == null)
        throw new ContentProviderException("Invalid response returned from web service API."); 
      if (!bool.booleanValue()) {
        String str1 = paramResponseDataParamSearch.getError().getCode();
        log.debug("Return Code = " + str1);
        String str2 = paramResponseDataParamSearch.getError().getMessage();
        log.error(str2);
        List list = paramResponseDataParamSearch.getError().getCauses();
        for (ErrorObject errorObject : list)
          log.error(errorObject.getMessage()); 
        throw new ContentProviderException(str2);
      } 
    } catch (Exception exception) {
      throw new ContentProviderException(exception.getMessage());
    } 
  }
  
  public void checkErrors(ResponseDataPartByID paramResponseDataPartByID, boolean paramBoolean) throws ContentProviderException {
    try {
      Boolean bool = paramResponseDataPartByID.getSuccess();
      if (bool == null)
        throw new ContentProviderException("Invalid response returned from web service API."); 
      if (!bool.booleanValue()) {
        String str1 = paramResponseDataPartByID.getError().getCode();
        log.debug("Return Code = " + str1);
        String str2 = paramResponseDataPartByID.getError().getMessage();
        throw new ContentProviderException(str2);
      } 
    } catch (Exception exception) {
      throw new ContentProviderException(exception.getMessage());
    } 
  }
  
  public void checkErrors(Document paramDocument, boolean paramBoolean) throws ContentProviderException {
    try {
      String str = this.xpath.evaluate("/ServiceResult/Status/Success/text()", paramDocument);
      if (str.isEmpty())
        throw new ContentProviderException("Invalid response returned from web service API."); 
      if (!str.equals("true")) {
        String str1 = this.xpath.evaluate("/ServiceResult/Status/Code/text()", paramDocument);
        log.debug("Return Code = " + str1);
        if (str1.equals("1")) {
          String str2 = this.xpath.evaluate("/ServiceResult/Status/Message/text()", paramDocument);
          throw new ContentProviderSEAuthenticationException(str2);
        } 
        if (paramBoolean || (!str1.equals("3") && !str1.equals("6"))) {
          String str2 = this.xpath.evaluate("/ServiceResult/Status/Message/text()", paramDocument);
          throw new ContentProviderException(str2);
        } 
      } 
    } catch (XPathExpressionException xPathExpressionException) {
      throw new ContentProviderException(xPathExpressionException.getMessage());
    } 
  }
  
  private boolean isExpressionOnProperty(ICriterion paramICriterion, String paramString) {
    if (paramICriterion instanceof IPropertyExpression) {
      IPropertyExpression iPropertyExpression = (IPropertyExpression)paramICriterion;
      return iPropertyExpression.getProperty().getContentProviderId().equals(paramString);
    } 
    return false;
  }
  
  private boolean isSearchContainsProperty(ICriterion paramICriterion, String paramString) {
    if (isExpressionOnProperty(paramICriterion, paramString))
      return true; 
    if (paramICriterion instanceof Junction)
      for (ICriterion iCriterion : ((Junction)paramICriterion).getCriterion()) {
        if (isExpressionOnProperty(iCriterion, paramString))
          return true; 
        if (iCriterion instanceof Junction)
          return isSearchContainsProperty(iCriterion, paramString); 
      }  
    return false;
  }
  
  private boolean isSearchContainsProperty(AbstractCriteria paramAbstractCriteria, String paramString) {
    for (ICriterion iCriterion : paramAbstractCriteria.getCriterion()) {
      if (isSearchContainsProperty(iCriterion, paramString))
        return true; 
    } 
    return false;
  }
  
  public static void printDocument(Document paramDocument) throws IOException, TransformerException {
    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    Transformer transformer = transformerFactory.newTransformer();
    transformer.setOutputProperty("omit-xml-declaration", "no");
    transformer.setOutputProperty("method", "xml");
    transformer.setOutputProperty("indent", "yes");
    transformer.setOutputProperty("encoding", "UTF-8");
    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
    transformer.transform(new DOMSource(paramDocument), new StreamResult(System.out));
  }
  
  private static String convertStreamToString(InputStream paramInputStream) throws IOException {
    if (paramInputStream != null) {
      StringWriter stringWriter = new StringWriter();
      char[] arrayOfChar = new char[1024];
      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(paramInputStream, "UTF-8"));
      int i;
      while ((i = bufferedReader.read(arrayOfChar)) != -1)
        stringWriter.write(arrayOfChar, 0, i); 
      return stringWriter.toString();
    } 
    return "";
  }
  
  private void getPartUpdatesByAlertType(HashSet<String> paramHashSet1, String paramString1, String paramString2, HashSet<String> paramHashSet2, String paramString3) throws ContentProviderException {
    byte b = 0;
    while (getPartUpdatesByAlertTypeAndPageNo(paramHashSet1, paramString1, paramString2, paramHashSet2, paramString3, ++b));
  }
  
  private boolean getPartUpdatesByAlertTypeAndPageNo(HashSet<String> paramHashSet1, String paramString1, String paramString2, HashSet<String> paramHashSet2, String paramString3, int paramInt) throws ContentProviderException {
    try {
      HashMap<Object, Object> hashMap = new HashMap<>();
      hashMap.put("fromDate", paramString1);
      hashMap.put("toDate", paramString2);
      hashMap.put("pageNo", Integer.toString(paramInt));
      hashMap.put("alertTypes", paramString3);
      if (paramInt > 1)
        log.info("  ...Page " + paramInt + "..."); 
      InputStream inputStream = callSE(getWebServiceBaseURL() + "search/alert/getUpdates", (Map)hashMap);
      Document document = parseXML(inputStream);
      String str1 = this.xpath.evaluate("/ServiceResult/Status/Code/text()", document);
      String str2 = this.xpath.evaluate("/ServiceResult/Status/Message/text()", document);
      if (str1.equals("7") || str2.equals("No result found") || str1.equals("30") || str2.equals("Entered fromDate exceed last update date"))
        return false; 
      checkErrors(document, true);
      NodeList nodeList = (NodeList)this.xpath.evaluate("/ServiceResult/updates/partUpdates/*/comId", document, XPathConstants.NODESET);
      byte b;
      for (b = 0; b < nodeList.getLength(); b++) {
        Element element = (Element)nodeList.item(b);
        String str = this.xpath.evaluate("text()", element);
        paramHashSet2.add(str);
      } 
      nodeList = (NodeList)this.xpath.evaluate("/ServiceResult/updates/partUpdates/*/affectedParts/comId", document, XPathConstants.NODESET);
      for (b = 0; b < nodeList.getLength(); b++) {
        Element element = (Element)nodeList.item(b);
        String str = this.xpath.evaluate("text()", element);
        if (paramHashSet1.contains(str))
          paramHashSet2.add(str); 
      } 
    } catch (Exception exception) {
      throw new ContentProviderException(exception.getMessage());
    } 
    return true;
  }
  
  public String addSubscription(Map<String, String> paramMap) throws ContentProviderException {
    String str = paramMap.get("DataProviderID");
    this.si = new SearchInfo();
    this.si.searchURL = getWebServiceBaseURL() + "search/alert/addPart";
    this.si.paramMap.put("comIds", str);
    try {
      InputStream inputStream = callSE(this.si.searchURL, this.si.paramMap);
      Document document = parseXML(inputStream);
      String str1 = this.xpath.evaluate("/ServiceResult/Status/Code/text()", document);
      if (!str1.equals("0")) {
        String str2 = this.xpath.evaluate("/ServiceResult/addedParts/status/text()", document);
        if (!str2.contains("already exist"))
          throw new ContentProviderException(str2); 
        return null;
      } 
      log.info("Added " + this.ccp.getName() + " subscription for DataProviderID = '" + str + "'.");
      return str;
    } catch (Exception exception) {
      throw new ContentProviderException("Unable to add " + this.ccp.getName() + " subscription: " + exception.getMessage());
    } 
  }
  
  public void deleteSubscription(Map<String, String> paramMap) throws ContentProviderException {
    String str = paramMap.get("DataProviderID");
    this.si = new SearchInfo();
    this.si.searchURL = getWebServiceBaseURL() + "search/alert/deletePart";
    this.si.paramMap.put("comIDs", str);
    try {
      InputStream inputStream = callSE(this.si.searchURL, this.si.paramMap);
      Document document = parseXML(inputStream);
      String str1 = this.xpath.evaluate("/ServiceResult/Status/Code/text()", document);
      if (!str1.equals("0")) {
        String str2 = this.xpath.evaluate("/ServiceResult/Status/Message/text()", document);
        throw new ContentProviderException(str2);
      } 
      log.info("Deleted " + this.ccp.getName() + " subscription for DataProviderID = '" + str + "'.");
    } catch (Exception exception) {
      throw new ContentProviderException("Unable to delete " + this.ccp.getName() + " subscription: " + exception.getMessage());
    } 
  }
  
  public Collection<Map<String, String>> getSubscriptions() throws ContentProviderException {
    ArrayList<Map<String, String>> arrayList = new ArrayList();
    byte b = 0;
    while (getSubscriptions(arrayList, ++b));
    return arrayList;
  }
  
  private boolean getSubscriptions(ArrayList<Map<String, String>> paramArrayList, int paramInt) throws ContentProviderException {
    try {
      HashMap<Object, Object> hashMap = new HashMap<>();
      hashMap.put("pageNo", Integer.toString(paramInt));
      hashMap.put("perPage", "500");
      InputStream inputStream = callSE(getWebServiceBaseURL() + "search/alert/listParts", (Map)hashMap);
      Document document = parseXML(inputStream);
      String str1 = this.xpath.evaluate("/ServiceResult/Status/Code/text()", document);
      String str2 = this.xpath.evaluate("/ServiceResult/Status/Message/text()", document);
      if (str1.equals("3") || str2.equals("No Results Found"))
        return false; 
      checkErrors(document, true);
      NodeList nodeList = (NodeList)this.xpath.evaluate("/ServiceResult/addedParts", document, XPathConstants.NODESET);
      for (byte b = 0; b < nodeList.getLength(); b++) {
        Element element = (Element)nodeList.item(b);
        String str = this.xpath.evaluate("comId/text()", element);
        HashMap<Object, Object> hashMap1 = new HashMap<>();
        hashMap1.put("DataProviderID", str);
        paramArrayList.add(hashMap1);
      } 
    } catch (Exception exception) {
      String str = "Error while getting list of subscribed parts from " + this.ccp.getName() + ":  " + exception.getMessage();
      throw new ContentProviderException(str);
    } 
    return true;
  }
  
  public Collection<ContentProviderSubscribedComponent> getSubscribedComponents() throws ContentProviderException {
    ArrayList<ContentProviderSubscribedComponent> arrayList = new ArrayList();
    this.subscribedComponentMap = new HashMap<>();
    byte b = 0;
    while (getSubscribedComponents(arrayList, ++b));
    return arrayList;
  }
  
  private boolean getSubscribedComponents(Collection<ContentProviderSubscribedComponent> paramCollection, int paramInt) throws ContentProviderException {
    try {
      HashMap<Object, Object> hashMap = new HashMap<>();
      hashMap.put("pageNo", Integer.toString(paramInt));
      hashMap.put("perPage", "500");
      InputStream inputStream = callSE(getWebServiceBaseURL() + "search/alert/listParts", (Map)hashMap);
      Document document = parseXML(inputStream);
      String str1 = this.xpath.evaluate("/ServiceResult/Status/Code/text()", document);
      String str2 = this.xpath.evaluate("/ServiceResult/Status/Message/text()", document);
      if (str1.equals("3") || str2.equals("No Results Found"))
        return false; 
      checkErrors(document, true);
      NodeList nodeList = (NodeList)this.xpath.evaluate("/ServiceResult/addedParts", document, XPathConstants.NODESET);
      for (byte b = 0; b < nodeList.getLength(); b++) {
        Element element = (Element)nodeList.item(b);
        ContentProviderSubscribedAML contentProviderSubscribedAML = new ContentProviderSubscribedAML();
        String str3 = this.xpath.evaluate("comId/text()", element);
        contentProviderSubscribedAML.addIDProperty("DataProviderID", str3);
        String str4 = this.xpath.evaluate("sePartNumber/text()", element);
        contentProviderSubscribedAML.setPartNumber(str4);
        String str5 = this.xpath.evaluate("seManufacturer/text()", element);
        contentProviderSubscribedAML.setManufacturer(str5);
        str4 = this.xpath.evaluate("partNumber/text()", element);
        contentProviderSubscribedAML.setCustomerPartNumber(str4);
        str5 = this.xpath.evaluate("manufacturer/text()", element);
        contentProviderSubscribedAML.setCustomerManufacturer(str5);
        String str6 = this.xpath.evaluate("CPN/text()", element);
        ContentProviderSubscribedComponent contentProviderSubscribedComponent = this.subscribedComponentMap.get(str6);
        if (contentProviderSubscribedComponent == null) {
          contentProviderSubscribedComponent = new ContentProviderSubscribedComponent();
          contentProviderSubscribedComponent.setCPN(str6);
          this.subscribedComponentMap.put(str6, contentProviderSubscribedComponent);
          paramCollection.add(contentProviderSubscribedComponent);
        } 
        contentProviderSubscribedComponent.addAML(contentProviderSubscribedAML);
      } 
    } catch (Exception exception) {
      String str = "Error while getting list of subscribed components from " + this.ccp.getName() + ":  " + exception.getMessage();
      throw new ContentProviderException(str);
    } 
    return true;
  }
  
  public InputStream callSE(String paramString1, Map<String, String> paramMap, String paramString2, String paramString3) throws ContentProviderException {
    return callSE(paramString1, paramMap, paramString2, paramString3, false, null, null, false, null);
  }
  
  public InputStream callSE(String paramString1, Map<String, String> paramMap, String paramString2, String paramString3, boolean paramBoolean1, String paramString4, String paramString5, boolean paramBoolean2, String paramString6) throws ContentProviderException {
    byte b = 0;
    while (true) {
      b++;
      try {
        authenticate(paramString2, paramString3, paramBoolean1, paramString4, paramString5, paramBoolean2, paramString6);
        HttpPost httpPost = new HttpPost(paramString1);
        ArrayList<BasicNameValuePair> arrayList = new ArrayList();
        for (Map.Entry<String, String> entry : paramMap.entrySet())
          arrayList.add(new BasicNameValuePair((String)entry.getKey(), (String)entry.getValue())); 
        arrayList.add(new BasicNameValuePair("fmt", "xml"));
        UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(arrayList, "utf-8");
        log.debug(paramString1 + "?" + paramString1);
        httpPost.setEntity((HttpEntity)urlEncodedFormEntity);
        HttpResponse httpResponse = this.client.execute((HttpUriRequest)httpPost);
        if (!httpResponse.getEntity().getContentType().getValue().contains("application/xml"))
          throw new ContentProviderException(convertStreamToString(httpResponse.getEntity().getContent())); 
        return httpResponse.getEntity().getContent();
      } catch (Exception exception) {
        if (exception instanceof ContentProviderSEAuthenticationException)
          throw (ContentProviderSEAuthenticationException)exception; 
        if (b <= 3) {
          log.warn("Error calling Aggregation service : " + exception.getMessage() + ".  Retrying (" + b + ")...");
          try {
            Thread.sleep(3000L);
          } catch (InterruptedException interruptedException) {}
          continue;
        } 
        log.error(exception, exception);
        throw new ContentProviderException(exception.getMessage());
      } 
    } 
  }
  
  public InputStream callSE(String paramString, Map<String, String> paramMap) throws ContentProviderException {
    boolean bool1 = ((AggregationServiceUserPreferences)this.ccp.getUserPreferences()).isProxyEnabled();
    boolean bool2 = ((AggregationServiceUserPreferences)this.ccp.getUserPreferences()).isSSLKeystoreEnabled();
    ContentProviderAppConfig contentProviderAppConfig = ContentProviderFactory.getInstance().getAppConfig();
    String str1 = this.ccp.getAuthorizeConnection().getSearchServiceUserName();
    String str2 = this.ccp.getAuthorizeConnection().getSearchServicePassword();
    return callSE(paramString, paramMap, str1, str2, bool1, contentProviderAppConfig.getProxyHost(), contentProviderAppConfig.getProxyPort(), bool2, contentProviderAppConfig.getSSLKeystorePassword());
  }
  
  public String getPartNumberFromPartDetailsMessage(Document paramDocument) throws ContentProviderException {
    try {
      String str = "/ServiceResult/Results/ResultDto/SummaryData/PartNumber/text()";
      return (String)this.xpath.evaluate(str, paramDocument, XPathConstants.STRING);
    } catch (Exception exception) {
      throw new ContentProviderException("Unable to get part number from " + this.ccp.getName() + " return message.");
    } 
  }
  
  private int getPageSize() {
    int i = 10;
    try {
      i = Integer.parseInt(((AggregationServiceUserPreferences)this.ccp.getUserPreferences()).getNumSearchResults());
    } catch (NumberFormatException numberFormatException) {}
    return i;
  }
  
  public Collection<String> searchManufacturers(String paramString) throws ContentProviderException {
    ArrayList<String> arrayList = new ArrayList();
    HashMap<Object, Object> hashMap = new HashMap<>();
    hashMap.put("mfr", paramString);
    hashMap.put("pageNo", "1");
    hashMap.put("pageSize", "500");
    InputStream inputStream = callSE(getWebServiceBaseURL() + "search/manufacturers", (Map)hashMap);
    Document document = parseXML(inputStream);
    try {
      String str1 = this.xpath.evaluate("/ServiceResult/Status/Code/text()", document);
      String str2 = this.xpath.evaluate("/ServiceResult/Status/Message/text()", document);
      if (str1.equals("3") || str2.equals("No Results Found"))
        return arrayList; 
    } catch (Exception exception) {
      throw new ContentProviderException("Error while searching manufacturers from " + this.ccp.getName() + ":  " + exception.getMessage());
    } 
    checkErrors(document, true);
    try {
      NodeList nodeList = (NodeList)this.xpath.evaluate("/ServiceResult/Result/MfrDto", document, XPathConstants.NODESET);
      for (byte b = 0; b < nodeList.getLength(); b++) {
        Element element = (Element)nodeList.item(b);
        String str = this.xpath.evaluate("ManufacturerName/text()", element);
        arrayList.add(str);
      } 
    } catch (Exception exception) {
      throw new ContentProviderException("Error while searching manufacturers from " + this.ccp.getName() + ":  " + exception.getMessage());
    } 
    return arrayList;
  }
  
  public Collection<ContentProviderSubscribedAML> searchCPN(String paramString1, String paramString2) throws ContentProviderException {
    ArrayList<ContentProviderSubscribedAML> arrayList = new ArrayList();
    HashMap<Object, Object> hashMap = new HashMap<>();
    String str = "[{\"cpn\":\"" + paramString1 + "\"";
    if (paramString2 != null && !paramString2.isEmpty())
      str = str + ",\"mpn\":\"" + str + "\""; 
    str = str + "}]";
    hashMap.put("cpns", str);
    InputStream inputStream = callSE(getWebServiceBaseURL() + "search/CPNSearch", (Map)hashMap);
    Document document = parseXML(inputStream);
    try {
      String str1 = this.xpath.evaluate("/ServiceResult/Status/Code/text()", document);
      String str2 = this.xpath.evaluate("/ServiceResult/Status/Message/text()", document);
      if (str1.equals("3") || str2.equals("No Results Found"))
        return arrayList; 
    } catch (Exception exception) {
      throw new ContentProviderException("Error while searching CPN from " + this.ccp.getName() + ":  " + exception.getMessage());
    } 
    checkErrors(document, true);
    try {
      NodeList nodeList = (NodeList)this.xpath.evaluate("/ServiceResult/Results/ResultDto", document, XPathConstants.NODESET);
      if (nodeList != null)
        for (byte b = 0; b < nodeList.getLength(); b++) {
          Element element = (Element)nodeList.item(b);
          ContentProviderSubscribedAML contentProviderSubscribedAML = new ContentProviderSubscribedAML();
          String str1 = this.xpath.evaluate("COM_ID/text()", element);
          contentProviderSubscribedAML.addIDProperty("DataProviderID", str1);
          String str2 = this.xpath.evaluate("MPN/text()", element);
          contentProviderSubscribedAML.setPartNumber(str2);
          String str3 = this.xpath.evaluate("Supplier/text()", element);
          contentProviderSubscribedAML.setManufacturer(str3);
          arrayList.add(contentProviderSubscribedAML);
        }  
    } catch (Exception exception) {
      throw new ContentProviderException("Error while searching CPN from " + this.ccp.getName() + ":  " + exception.getMessage());
    } 
    return arrayList;
  }
  
  private void loadECADModels(ContentProviderResultRecordImpl paramContentProviderResultRecordImpl, Element paramElement) throws XPathExpressionException {
    String str = this.xpath.evaluate("SchematicSymbol/text()", paramElement);
    boolean bool = str.equals("Yes");
    paramContentProviderResultRecordImpl.setSymbolAvailable(bool);
    paramContentProviderResultRecordImpl.addPartProperty("ECAD Models", new ComponentProperty("ECAD Symbol", "Symbol", bool ? "Yes" : "No", ""));
    str = this.xpath.evaluate("PCBFootprint/text()", paramElement);
    bool = str.equals("Yes");
    paramContentProviderResultRecordImpl.setFootprintAvailable(bool);
    paramContentProviderResultRecordImpl.addPartProperty("ECAD Models", new ComponentProperty("ECAD Footprint", "Footprint", bool ? "Yes" : "No", ""));
    str = this.xpath.evaluate("Model3D/text()", paramElement);
    bool = str.equals("Yes");
    paramContentProviderResultRecordImpl.set3DModelAvailable(bool);
    paramContentProviderResultRecordImpl.addPartProperty("ECAD Models", new ComponentProperty("ECAD 3D Model", "3D Model", bool ? "Yes" : "No", ""));
    paramContentProviderResultRecordImpl.setHasECADModels((paramContentProviderResultRecordImpl.isECADSymbolAvailable() || paramContentProviderResultRecordImpl.isECADFootprintAvailable() || paramContentProviderResultRecordImpl.isECAD3DModelAvailable()));
  }
  
  public Collection<ContentProviderManufacturer> getAvailableManufacturers() throws ContentProviderException {
    ArrayList<ContentProviderManufacturer> arrayList = new ArrayList();
    TreeSet<String> treeSet = new TreeSet();
    HashSet<String> hashSet = new HashSet();
    hashSet.add("Semiconductor");
    hashSet.add("Passive");
    hashSet.add("Electrical and Electronic Components");
    hashSet.add("Optoelectronics, Lighting and Displays");
    hashSet.add("Interconnect");
    hashSet.add("Test and Measurement");
    try {
      HashMap<Object, Object> hashMap = new HashMap<>();
      InputStream inputStream = callSE(getWebServiceBaseURL() + "search/parametric/getAllTaxonomy", (Map)hashMap);
      Document document = parseXML(inputStream);
      inputStream.close();
      checkErrors(document, true);
      XPath xPath = XPathFactory.newInstance().newXPath();
      NodeList nodeList = (NodeList)xPath.evaluate("/ServiceResult/Result/TaxonomyList/Taxonomy", document, XPathConstants.NODESET);
      for (byte b = 0; b < nodeList.getLength(); b++) {
        Element element = (Element)nodeList.item(b);
        String str = xPath.evaluate("PlType/text()", element);
        if (hashSet.contains(str)) {
          NodeList nodeList1 = (NodeList)xPath.evaluate("MainCategoryList/MainCategory/SubCategoryList/SubCategory/ProductLines/*", element, XPathConstants.NODESET);
          for (byte b1 = 0; b1 < nodeList1.getLength(); b1++) {
            Element element1 = (Element)nodeList1.item(b1);
            processProductLineManufacturers(element1, treeSet);
          } 
        } 
      } 
      log.info("Retrieving supplier profile for all manufacturers...");
      for (String str : treeSet) {
        Element element = null;
        try {
          element = getSupplierInfo(str);
        } catch (Exception exception) {
          log.error("Error: Unable to get supplier info for Manufacturer '" + str + "': " + exception.getMessage());
          continue;
        } 
        try {
          ContentProviderManufacturer contentProviderManufacturer = getManufacturer(str);
          arrayList.add(contentProviderManufacturer);
        } catch (ContentProviderException contentProviderException) {
          log.error("Error: " + contentProviderException.getMessage());
        } 
      } 
    } catch (Exception exception) {
      throw new ContentProviderException("Error: Unable to retreive available manufacturers", exception);
    } 
    return arrayList;
  }
  
  @Deprecated
  private void processProductLineManufacturers(Element paramElement, TreeSet<String> paramTreeSet) throws Exception {
    XPath xPath = XPathFactory.newInstance().newXPath();
    HashMap<Object, Object> hashMap = new HashMap<>();
    String str = xPath.evaluate("plName/text()", paramElement);
    hashMap.put("plName", str);
    log.info("Retrieving Manufacturers for Product Line '" + str + "'...");
    InputStream inputStream = callSE(getWebServiceBaseURL() + "search/parametric/getPlFeatures", (Map)hashMap);
    Document document = parseXML(inputStream);
    inputStream.close();
    try {
      checkErrors(document, true);
    } catch (ContentProviderException contentProviderException) {
      log.warn(contentProviderException.getMessage());
      return;
    } 
    NodeList nodeList = (NodeList)xPath.evaluate("/ServiceResult/Result/PlFeatures/Features/Feature[FeatureName = \"Manufacturer\"]/FeatureValues/FeatureValue", document, XPathConstants.NODESET);
    for (byte b = 0; b < nodeList.getLength(); b++) {
      Element element = (Element)nodeList.item(b);
      String str1 = element.getTextContent();
      paramTreeSet.add(str1);
    } 
  }
  
  @Deprecated
  private Element getSupplierInfo(String paramString) throws Exception {
    null = null;
    HashMap<Object, Object> hashMap = new HashMap<>();
    hashMap.put("manufacturerName", paramString);
    XPath xPath = XPathFactory.newInstance().newXPath();
    Document document = null;
    InputStream inputStream = callSE(getWebServiceBaseURL() + "search/supplierProfile", (Map)hashMap);
    document = parseXML(inputStream);
    checkErrors(document, true);
    String str1 = xPath.evaluate("/ServiceResult/Status/Code/text()", document);
    String str2 = xPath.evaluate("/ServiceResult/Status/Message/text()", document);
    return (str1.equals("3") || str2.equals("No Results Found")) ? null : (Element)xPath.evaluate("/ServiceResult/Result/SuppllierProfileData", document, XPathConstants.NODE);
  }
  
  @Deprecated
  private String getSupplierProperty(Element paramElement, String paramString) throws XPathExpressionException {
    XPath xPath = XPathFactory.newInstance().newXPath();
    null = xPath.evaluate(paramString + "/text()", paramElement);
    null = null.replace("\t", " ");
    return null.replace("\n", " ");
  }
  
  @Deprecated
  public ContentProviderManufacturer getManufacturer(String paramString) throws ContentProviderException {
    Element element = null;
    ContentProviderManufacturer contentProviderManufacturer = null;
    try {
      element = getSupplierInfo(paramString);
      contentProviderManufacturer = new ContentProviderManufacturer();
      contentProviderManufacturer.addProperty("ManufacturerName", paramString, ContentProviderManufacturerProperty.MfgPropertyType.LONG_NAME);
      contentProviderManufacturer.addProperty("ManufacturerID", getSupplierProperty(element, "ManufacturerID"), ContentProviderManufacturerProperty.MfgPropertyType.OBJECT_ID);
      contentProviderManufacturer.addProperty("Address", getSupplierProperty(element, "Address"), ContentProviderManufacturerProperty.MfgPropertyType.ADDRESS);
      contentProviderManufacturer.addProperty("PhoneNumber", getSupplierProperty(element, "PhoneNumber"), ContentProviderManufacturerProperty.MfgPropertyType.ADDRESS);
      contentProviderManufacturer.addProperty("ManufacturerURL", getSupplierProperty(element, "ManufacturerURL"), ContentProviderManufacturerProperty.MfgPropertyType.ADDRESS);
      contentProviderManufacturer.addProperty("Email", getSupplierProperty(element, "Email"), ContentProviderManufacturerProperty.MfgPropertyType.ADDRESS);
      contentProviderManufacturer.addProperty("DUNSNumber", getSupplierProperty(element, "DUNSNumber"), ContentProviderManufacturerProperty.MfgPropertyType.ADDRESS);
    } catch (Exception exception) {
      throw new ContentProviderException("Unable to get supplier info for Manufacturer '" + paramString + "': " + exception.getMessage());
    } 
    return contentProviderManufacturer;
  }
  
  public HashMap<String, SearchCapability> getSeachCapabilities() throws Exception {
    HashMap<Object, Object> hashMap = new HashMap<>();
    String str1 = getWebServiceBaseURL();
    String str2 = sendGetReq(str1 + "/operations/get-property-search-capabilities");
    ResponseDataSearchCapabilities responseDataSearchCapabilities = (ResponseDataSearchCapabilities)SfGson.createGson().fromJson(str2, ResponseDataSearchCapabilities.class);
    List list1 = responseDataSearchCapabilities.getResult().getPropertySearchCapabilities();
    for (PropertySearchCapability propertySearchCapability : list1) {
      SearchCapability searchCapability = new SearchCapability();
      String str = propertySearchCapability.getSearchPropertyId();
      searchCapability.setSearchable(propertySearchCapability.isSearchable());
      searchCapability.setFacet(propertySearchCapability.canUseFacet());
      searchCapability.setEquality(propertySearchCapability.isSupportOperator("Equality"));
      searchCapability.setAnyOf(propertySearchCapability.isSupportOperator("AnyOf"));
      searchCapability.setRangeExclusive(propertySearchCapability.isSupportOperator("Range_exclusive"));
      searchCapability.setRangeInclusive(propertySearchCapability.isSupportOperator("Range_inclusive"));
      searchCapability.setPatternMatch(propertySearchCapability.isSupportOperator("PatternMatch"));
      searchCapability.setSmartMatch(propertySearchCapability.isSupportOperator("SmartMatch"));
      searchCapability.setAnyOfSmartMatch(propertySearchCapability.isSupportOperator("AnyOfSmartMatch"));
      hashMap.put(str, searchCapability);
    } 
    str2 = sendGetReq(str1 + "/scheme/get-property-definitions");
    PropData propData = (PropData)SfGson.createGson().fromJson(str2, PropData.class);
    List list2 = propData.getResult().getProperties();
    for (PropertyDefinitions propertyDefinitions : list2) {
      String str = propertyDefinitions.getId();
      SearchCapability searchCapability = (SearchCapability)hashMap.get(str);
      Boolean bool = propertyDefinitions.getType().getCaseSensitive();
      if (bool == null) {
        searchCapability.setCaseSensitive(false);
      } else if (bool.booleanValue()) {
        searchCapability.setCaseSensitive(true);
      } else {
        searchCapability.setCaseSensitive(false);
      } 
      if (propertyDefinitions.getType().getUnit() != null)
        searchCapability.setUnit(propertyDefinitions.getType().getUnit()); 
      hashMap.put(str, searchCapability);
    } 
    return (HashMap)hashMap;
  }
  
  public ArrayList<CPPartClass> getPartClassInfo() throws Exception {
    ArrayList<CPPartClass> arrayList = new ArrayList();
    String str1 = getWebServiceBaseURL();
    String str2 = sendGetReq(str1 + "/scheme/get-part-class-hierarchy");
    ClassHierarchyData classHierarchyData = (ClassHierarchyData)SfGson.createGson().fromJson(str2, ClassHierarchyData.class);
    if (classHierarchyData == null)
      throw new ContentProviderException("Failed to get Class Hierarchy."); 
    addCPClassList(classHierarchyData.getResult().getRoot(), "", arrayList);
    return arrayList;
  }
  
  private void addCPClassList(PartClass paramPartClass, String paramString, ArrayList<CPPartClass> paramArrayList) {
    CPPartClass cPPartClass = new CPPartClass(paramPartClass.getId());
    cPPartClass.setLabel(paramPartClass.getName());
    cPPartClass.setParentID(paramString);
    cPPartClass.setChildNum(paramPartClass.getDescendants().size());
    paramArrayList.add(cPPartClass);
    for (PartClass partClass : paramPartClass.getDescendants())
      addCPClassList(partClass, paramPartClass.getId(), paramArrayList); 
  }
  
  public List<CPProperty> getPartProperties(String paramString) throws Exception {
    ArrayList<CPProperty> arrayList = new ArrayList();
    String str1 = getWebServiceBaseURL();
    String str2 = sendGetReq(str1 + "/scheme/get-part-class-definitions?PartClassIds=" + str1);
    PartData partData = (PartData)(new Gson()).fromJson(str2, PartData.class);
    List list = ((Detail)partData.getResult().getDetails().get(0)).getProperties();
    for (Property property : list) {
      CPProperty cPProperty = new CPProperty(property.getId(), property.getName(), property.getTypeString());
      cPProperty.setUnit(property.getType().getUnit());
      arrayList.add(cPProperty);
    } 
    return arrayList;
  }
  
  public void getNewAccessToken() throws ContentProviderException {
    accessToken = this.ccp.getAuthorizeConnection().getAccessToken();
  }
  
  public String sendPostReq(String paramString1, String paramString2) throws ContentProviderException {
    String str = "";
    if (!CheckLicense.hasSupplyChainLicense())
      throw new ContentProviderException("You have no valid license for Supplychain Integration."); 
    byte b = 0;
    while (b < 2) {
      if (b != 0)
        log.info("Try request again."); 
      getNewAccessToken();
      try {
        CloseableHttpClient closeableHttpClient = createCloseableHttpClient(paramString1);
        try {
          HttpPost httpPost = new HttpPost(paramString1);
          httpPost.addHeader("Authorization", "Bearer " + accessToken);
          httpPost.addHeader("Content-Type", "application/JSON; charset=utf-8");
          StringEntity stringEntity = new StringEntity(paramString2, "utf-8");
          httpPost.setEntity((HttpEntity)stringEntity);
          CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute((HttpUriRequest)httpPost);
          int i = closeableHttpResponse.getStatusLine().getStatusCode();
          if (i == 401) {
            accessToken = null;
          } else {
            if (i == 502 || i == 504 || i == 524) {
              log.error("Error on request to the following URI:" + String.valueOf(httpPost.getURI()));
              log.error("Response Code : " + i);
              b++;
              Thread.sleep(3000L);
              if (closeableHttpClient != null)
                closeableHttpClient.close(); 
              continue;
            } 
            if (i != 200) {
              log.error("Search failed. The input value is invalid or there is a problem on the server side.\nCheck the log for details.");
              log.error("Error on request to the following URI:" + String.valueOf(httpPost.getURI()));
            } 
          } 
          str = EntityUtils.toString(closeableHttpResponse.getEntity());
          str = gson.toJson(JsonParser.parseReader(new StringReader(str)));
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
        log.error(exception.getMessage(), exception);
        throw new ContentProviderException(exception);
      } 
    } 
    return str;
  }
  
  public String sendGetReq(String paramString) throws ContentProviderException {
    String str = "";
    if (!CheckLicense.hasSupplyChainLicense())
      throw new ContentProviderException("You have no valid license for Supplychain Integration."); 
    byte b = 0;
    while (b < 2) {
      if (b != 0)
        log.info("Try request again."); 
      getNewAccessToken();
      try {
        CloseableHttpClient closeableHttpClient = createCloseableHttpClient(paramString);
        try {
          HttpGet httpGet = new HttpGet(paramString);
          httpGet.addHeader("Authorization", "Bearer " + accessToken);
          httpGet.addHeader("Content-Type", "application/JSON; charset=utf-8");
          CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute((HttpUriRequest)httpGet);
          int i = closeableHttpResponse.getStatusLine().getStatusCode();
          if (i == 401) {
            accessToken = null;
          } else {
            if (i == 502 || i == 504 || i == 524) {
              log.error("Error on request to the following URI:" + String.valueOf(httpGet.getURI()));
              log.error("Response Code : " + i);
              b++;
              Thread.sleep(3000L);
              if (closeableHttpClient != null)
                closeableHttpClient.close(); 
              continue;
            } 
            if (i != 200) {
              log.error("Error on request to the following URI:" + String.valueOf(httpGet.getURI()));
              log.error("Response Code : " + i);
            } 
          } 
          str = EntityUtils.toString(closeableHttpResponse.getEntity());
          str = gson.toJson(JsonParser.parseReader(new StringReader(str)));
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
    } 
    return str;
  }
  
  public CloseableHttpClient createCloseableHttpClient(String paramString) throws SSLCertException {
    AggregationServiceUserPreferences aggregationServiceUserPreferences = (AggregationServiceUserPreferences)this.ccp.getUserPreferences();
    HttpClientBuilder httpClientBuilder = HttpClients.custom();
    if (aggregationServiceUserPreferences.isProxyEnabled()) {
      ContentProviderAppConfig contentProviderAppConfig = ContentProviderFactory.getInstance().getAppConfig();
      HttpHost httpHost = new HttpHost(contentProviderAppConfig.getProxyHost(), Integer.parseInt(contentProviderAppConfig.getProxyPort()));
      DefaultProxyRoutePlanner defaultProxyRoutePlanner = new DefaultProxyRoutePlanner(httpHost);
      httpClientBuilder.setRoutePlanner((HttpRoutePlanner)defaultProxyRoutePlanner);
    } 
    if (aggregationServiceUserPreferences.isSSLKeystoreEnabled()) {
      ContentProviderAppConfig contentProviderAppConfig = ContentProviderFactory.getInstance().getAppConfig();
      String str = contentProviderAppConfig.getSSLKeystorePassword();
      SSLContext sSLContext = getSSLContext(str);
      SSLConnectionSocketFactory sSLConnectionSocketFactory = new SSLConnectionSocketFactory(sSLContext);
      httpClientBuilder.setSSLSocketFactory((LayeredConnectionSocketFactory)sSLConnectionSocketFactory).build();
    } 
    return httpClientBuilder.build();
  }
  
  class SearchInfo {
    boolean bPartNumberSearch = false;
    
    boolean bDescriptionSearch = false;
    
    boolean bKeywordSearch = false;
    
    boolean bParametricSearch = false;
    
    boolean bDefinitionSearch = false;
    
    public String searchURL;
    
    public HashMap<String, String> paramMap = new HashMap<>();
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\sf\AggregationServiceWebCall.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */