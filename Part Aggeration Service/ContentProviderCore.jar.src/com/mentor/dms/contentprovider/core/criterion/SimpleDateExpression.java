package com.mentor.dms.contentprovider.core.criterion;

import com.mentor.dms.contentprovider.core.AbstractCriteria;
import com.mentor.dms.contentprovider.core.config.ContentProviderConfigProperty;
import com.mentor.dms.contentprovider.core.utils.DateUtils;
import java.math.BigDecimal;

public class SimpleDateExpression extends SimpleExpression {
  protected SimpleDateExpression(ContentProviderConfigProperty paramContentProviderConfigProperty, PropertyValue paramPropertyValue, SimpleExpression.OperatorType paramOperatorType) {
    super(paramContentProviderConfigProperty, paramPropertyValue, paramOperatorType);
  }
  
  protected SimpleDateExpression(ContentProviderConfigProperty paramContentProviderConfigProperty, PropertyValue paramPropertyValue, SimpleExpression.OperatorType paramOperatorType, boolean paramBoolean) {
    super(paramContentProviderConfigProperty, paramPropertyValue, paramOperatorType, paramBoolean);
  }
  
  public String toSearchString(AbstractCriteria paramAbstractCriteria) {
    return null;
  }
  
  public Object getValue() {
    Object object = getPropertyValue().getValue();
    long l = ((BigDecimal)object).longValue();
    return DateUtils.toJsonTime(l);
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\criterion\SimpleDateExpression.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */