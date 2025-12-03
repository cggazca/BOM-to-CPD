package com.mentor.dms.contentprovider.plugin;

import com.mentor.datafusion.oi.OICursor;
import com.mentor.datafusion.oi.OIException;
import com.mentor.datafusion.oi.OIQuery;
import com.mentor.datafusion.oi.interceptor.CommitInterceptor;
import com.mentor.datafusion.oi.interceptor.InterceptionException;
import com.mentor.datafusion.oi.interceptor.InterceptorFactory;
import com.mentor.datafusion.oi.interceptor.OpenInterceptor;
import com.mentor.datafusion.oi.type.OIObject;
import com.mentor.datafusion.oi.type.OIObjectSet;
import com.mentor.datafusion.utils.logger.MGLogger;
import com.mentor.dms.contentprovider.AbstractContentProvider;
import com.mentor.dms.contentprovider.ContentProviderException;
import com.mentor.dms.contentprovider.ContentProviderFactory;
import com.mentor.dms.contentprovider.ContentProviderGlobal;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class ExternalContentInterceptorFactory implements InterceptorFactory {
  private static MGLogger log = MGLogger.getLogger(ExternalContentInterceptorFactory.class);
  
  public CommitInterceptor getCommitInterceptor() {
    return new CommitInterceptor() {
        public void exception(Collection<OIObject> param1Collection, OIException param1OIException) {}
        
        public void postCommit(Collection<OIObject> param1Collection) {}
        
        public Collection<OIObject> preCommit(OIObject param1OIObject) throws InterceptionException {
          if (param1OIObject.getMode() != 5)
            return Collections.emptyList(); 
          try {
            HashSet<String> hashSet = new HashSet();
            OIObjectSet oIObjectSet = param1OIObject.getSet("ECProviderReferences");
            for (OIObject oIObject : oIObjectSet)
              hashSet.add(oIObject.getString("ECProviderReferenceID")); 
            HashMap<Object, Object> hashMap = new HashMap<>();
            for (String str : hashSet) {
              AbstractContentProvider abstractContentProvider = null;
              try {
                abstractContentProvider = ContentProviderFactory.getInstance().createContentProvider(str);
              } catch (ContentProviderException contentProviderException) {
                ExternalContentInterceptorFactory.log.warn("Unable to create Content Provider instance for id = '" + str + "' while deleting External Content Object :" + contentProviderException.getMessage());
                continue;
              } 
              for (OIObject oIObject : oIObjectSet) {
                if (oIObject.getString("ECProviderReferenceID").equals(str))
                  hashMap.put(oIObject.getString("ECProviderReferenceKey"), oIObject.getString("ECProviderReferenceValue")); 
              } 
              if (abstractContentProvider.isClientSubscriptionManagementRequired()) {
                OIObjectSet oIObjectSet1 = param1OIObject.getSet("ECProviderSubscribeList");
                for (OIObject oIObject : oIObjectSet1) {
                  if (oIObject.getString("ECProviderSubscribeID").equals(str))
                    hashMap.put("SubscriptionID", oIObject.getString("ECProviderSubscribeValue")); 
                } 
              } else {
                HashMap<Object, Object> hashMap1 = new HashMap<>();
                for (Map.Entry<Object, Object> entry : hashMap.entrySet()) {
                  OIQuery oIQuery = ContentProviderGlobal.getOIObjectManager().createQuery("ExternalContent", true);
                  oIQuery.addRestriction("ECProviderReferences.ECProviderReferenceID", str);
                  oIQuery.addRestriction("ECProviderReferences.ECProviderReferenceKey", (String)entry.getKey());
                  oIQuery.addRestriction("ECProviderReferences.ECProviderReferenceValue", (String)entry.getValue());
                  oIQuery.addColumn("ExternalContentId");
                  OICursor oICursor = oIQuery.execute();
                  while (oICursor.next()) {
                    String str1 = oICursor.getString("ExternalContentId");
                    Integer integer1 = (Integer)hashMap1.get(str1);
                    Integer integer2 = integer1;
                    integer1 = Integer.valueOf(integer1.intValue() + 1);
                    integer1 = Integer.valueOf((integer1 == null) ? 0 : integer2.intValue());
                    hashMap1.put(str1, integer1);
                  } 
                  oICursor.close();
                } 
                byte b = 0;
                for (Integer integer : hashMap1.values()) {
                  if (integer.intValue() == hashMap.size())
                    b++; 
                  if (b > 1)
                    return Collections.emptyList(); 
                } 
              } 
              try {
                String str1 = abstractContentProvider.getConfigurationParameter("DISABLE_SUBSCRIPTION");
                if (str1 == null || !str1.equalsIgnoreCase("TRUE"))
                  abstractContentProvider.deleteSubscription(hashMap); 
              } catch (ContentProviderException contentProviderException) {
                ExternalContentInterceptorFactory.log.warn("Unable to delete subscripton from Content Provider " + abstractContentProvider.getName() + " while deleting External Content Object :" + contentProviderException.getMessage());
              } 
            } 
          } catch (Exception exception) {
            ExternalContentInterceptorFactory.log.warn("Unable to process Content Provider subscription deletions while deleting External Content Object :" + exception.getMessage());
          } 
          return Collections.emptyList();
        }
      };
  }
  
  public OpenInterceptor getOpenInterceptor() {
    return null;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\plugin\ExternalContentInterceptorFactory.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */