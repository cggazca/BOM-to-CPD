package com.mentor.dms.contentprovider.utils.setrepmpn;

import com.mentor.dms.contentprovider.utils.setrepmpn.config.SetRepMPNCfgBaseFieldDataType;
import com.mentor.dms.contentprovider.utils.setrepmpn.config.SetRepMPNCfgDataType;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AbstractEDMObject {
  private String id;
  
  private HashMap<String, Object> propMap = new HashMap<>();
  
  AbstractEDMObject(String paramString) {
    this.id = paramString;
  }
  
  public String getId() {
    return this.id;
  }
  
  public PropertyValue getProperty(String paramString) throws SetRepMPNException {
    return new PropertyValue(this.propMap.get(paramString), null);
  }
  
  public PropertyValue getProperty(SetRepMPNCfgBaseFieldDataType paramSetRepMPNCfgBaseFieldDataType) throws SetRepMPNException {
    Object object = this.propMap.get(paramSetRepMPNCfgBaseFieldDataType.getDmn());
    if (object == null)
      return new PropertyValue(null, null); 
    if (object instanceof String) {
      String str = (String)object;
      PropertyValue propertyValue = new PropertyValue(str);
      if (str.trim().isEmpty())
        return propertyValue; 
      if (paramSetRepMPNCfgBaseFieldDataType.getRegex() != null) {
        Pattern pattern = Pattern.compile(paramSetRepMPNCfgBaseFieldDataType.getRegex());
        Matcher matcher = pattern.matcher(str);
        if (matcher.find())
          str = matcher.group(1); 
      } 
      if (str.trim().isEmpty())
        return propertyValue; 
      if (paramSetRepMPNCfgBaseFieldDataType.getDataType().equals(SetRepMPNCfgDataType.DEFAULT)) {
        propertyValue.setValue(str);
      } else if (paramSetRepMPNCfgBaseFieldDataType.getDataType().equals(SetRepMPNCfgDataType.INTEGER)) {
        try {
          propertyValue.setValue(Integer.valueOf(Integer.parseInt(str)));
        } catch (NumberFormatException numberFormatException) {
          throw new SetRepMPNException(numberFormatException);
        } 
      } else if (paramSetRepMPNCfgBaseFieldDataType.getDataType().equals(SetRepMPNCfgDataType.DOUBLE)) {
        try {
          propertyValue.setValue(Double.valueOf(Double.parseDouble(str)));
        } catch (NumberFormatException numberFormatException) {
          throw new SetRepMPNException(numberFormatException);
        } 
      } else if (paramSetRepMPNCfgBaseFieldDataType.getDataType().equals(SetRepMPNCfgDataType.DATE)) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(paramSetRepMPNCfgBaseFieldDataType.getDateFormat());
        try {
          propertyValue.setValue(simpleDateFormat.parse(str));
        } catch (ParseException parseException) {
          throw new SetRepMPNException(parseException);
        } 
      } 
      return propertyValue;
    } 
    return new PropertyValue(object, null);
  }
  
  public void addProperty(String paramString, Object paramObject) {
    this.propMap.put(paramString, paramObject);
  }
  
  public HashMap<String, Object> getProperties() {
    return this.propMap;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovide\\utils\setrepmpn\AbstractEDMObject.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */