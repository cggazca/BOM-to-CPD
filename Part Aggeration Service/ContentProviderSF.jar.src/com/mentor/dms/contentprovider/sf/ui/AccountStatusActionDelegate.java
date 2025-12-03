package com.mentor.dms.contentprovider.sf.ui;

import com.mentor.datafusion.utils.logger.MGLogger;
import com.mentor.dms.contentprovider.core.ContentProviderFactory;
import com.mentor.dms.contentprovider.sf.Activator;
import com.mentor.dms.contentprovider.sf.AggregationServiceWebCall;
import com.mentor.dms.contentprovider.sf.ContentProviderImpl;
import com.mentor.dms.contentprovider.sf.SupplyFrameConnection;
import com.mentor.dms.ui.DMSInstance;
import com.mentor.dms.ui.DefaultActionDelegate;
import com.mentor.dms.ui.popupcontext.ContextEvent;
import java.awt.Cursor;
import java.io.InputStream;
import java.util.HashMap;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;

public class AccountStatusActionDelegate extends DefaultActionDelegate {
  private static MGLogger log = MGLogger.getLogger(AccountStatusActionDelegate.class);
  
  public void actionPerformed(ContextEvent paramContextEvent) {
    DMSInstance dMSInstance = Activator.getDefault().getDMSInstance();
    try {
      ContentProviderImpl contentProviderImpl = (ContentProviderImpl)ContentProviderFactory.getInstance().createContentProvider("SE");
      try {
        dMSInstance.getJFrame().setCursor(new Cursor(3));
        HashMap<Object, Object> hashMap = new HashMap<>();
        AggregationServiceWebCall aggregationServiceWebCall = new AggregationServiceWebCall(contentProviderImpl);
        InputStream inputStream = aggregationServiceWebCall.callSE(aggregationServiceWebCall.getWebServiceBaseURL() + "search/userStatus", hashMap);
        Document document = aggregationServiceWebCall.parseXML(inputStream);
        aggregationServiceWebCall.checkErrors(document, false);
        dMSInstance.getJFrame().setCursor(null);
        XPath xPath = XPathFactory.newInstance().newXPath();
        String str1 = (new SupplyFrameConnection(contentProviderImpl)).getSearchServiceUserName();
        String str2 = (String)xPath.evaluate("/ServiceResult/UserStatus/CreationDate/text()", document, XPathConstants.STRING);
        String str3 = (String)xPath.evaluate("/ServiceResult/UserStatus/ExpirationDate/text()", document, XPathConstants.STRING);
        String str4 = (String)xPath.evaluate("/ServiceResult/UserStatus/PartDetailLimit/text()", document, XPathConstants.STRING);
        String str5 = (String)xPath.evaluate("/ServiceResult/UserStatus/PartDetailCount/text()", document, XPathConstants.STRING);
        String str6 = (String)xPath.evaluate("/ServiceResult/UserStatus/PartDetailRemaining/text()", document, XPathConstants.STRING);
        AccountStatusDlg accountStatusDlg = new AccountStatusDlg(dMSInstance.getJFrame(), str1, str2, str3, str4, str5, str6);
      } finally {
        dMSInstance.getJFrame().setCursor(null);
      } 
    } catch (Exception exception) {
      log.error(exception);
    } 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\s\\ui\AccountStatusActionDelegate.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */