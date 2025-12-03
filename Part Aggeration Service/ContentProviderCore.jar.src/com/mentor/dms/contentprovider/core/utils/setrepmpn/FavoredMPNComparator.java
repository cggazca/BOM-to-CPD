package com.mentor.dms.contentprovider.core.utils.setrepmpn;

import com.mentor.dms.contentprovider.core.utils.setrepmpn.config.SetRepMPNCfgAggregationSelectionCriteriaType;
import com.mentor.dms.contentprovider.core.utils.setrepmpn.config.SetRepMPNCfgBaseFieldType;
import com.mentor.dms.contentprovider.core.utils.setrepmpn.config.SetRepMPNCfgCustomSelectionCriteriaType;
import com.mentor.dms.contentprovider.core.utils.setrepmpn.config.SetRepMPNCfgFavoredManufacturerPart;
import com.mentor.dms.contentprovider.core.utils.setrepmpn.config.SetRepMPNCfgPrioritySelectionCriteriaType;
import com.mentor.dms.contentprovider.core.utils.setrepmpn.config.SetRepMPNCfgSelectionCriteriaAggregationType;
import java.util.Comparator;
import org.apache.log4j.Logger;

class FavoredMPNComparator implements Comparator<ComponentAML> {
  static Logger logger = Logger.getLogger(SetRepresentativeMPNEngine.class);
  
  private SetRepMPNCfgFavoredManufacturerPart favoredCfg;
  
  public FavoredMPNComparator(SetRepMPNCfgFavoredManufacturerPart paramSetRepMPNCfgFavoredManufacturerPart) {
    this.favoredCfg = paramSetRepMPNCfgFavoredManufacturerPart;
  }
  
  public int compare(ComponentAML paramComponentAML1, ComponentAML paramComponentAML2) {
    if (this.favoredCfg.getSelectionCriteria() == null)
      return 0; 
    if (paramComponentAML1 == paramComponentAML2)
      return 0; 
    logger.debug(paramComponentAML1.getId() + " : " + paramComponentAML1.getId());
    int i = 0;
    try {
      for (SetRepMPNCfgBaseFieldType setRepMPNCfgBaseFieldType : this.favoredCfg.getSelectionCriteria().getPrioritySelectionCriteriaOrAggregationSelectionCriteriaOrCustomSelectionCriteria()) {
        logger.debug("  " + setRepMPNCfgBaseFieldType.getDmn());
        PropertyValue propertyValue1 = getFilteredPropValue(paramComponentAML1, setRepMPNCfgBaseFieldType);
        PropertyValue propertyValue2 = getFilteredPropValue(paramComponentAML2, setRepMPNCfgBaseFieldType);
        if (propertyValue1 == null && propertyValue2 == null) {
          logger.debug("    " + paramComponentAML1.getId() + " : '" + String.valueOf(paramComponentAML1.getAMLProperty(setRepMPNCfgBaseFieldType).getValue()) + "' is filtered.");
          logger.debug("    " + paramComponentAML2.getId() + " : '" + String.valueOf(paramComponentAML2.getAMLProperty(setRepMPNCfgBaseFieldType).getValue()) + "' is filtered.");
          logger.debug("    Compare :" + i);
          continue;
        } 
        if (propertyValue1 != null && propertyValue2 == null) {
          logger.debug("    " + paramComponentAML2.getId() + " : '" + String.valueOf(paramComponentAML2.getAMLProperty(setRepMPNCfgBaseFieldType).getValue()) + "' is filtered.");
          logger.debug("    Compare :-1");
          return -1;
        } 
        if (propertyValue1 == null && propertyValue2 != null) {
          logger.debug("    " + paramComponentAML1.getId() + " : '" + String.valueOf(paramComponentAML1.getAMLProperty(setRepMPNCfgBaseFieldType).getValue()) + "' is filtered.");
          logger.debug("    Compare :1");
          return 1;
        } 
        logger.debug("    " + paramComponentAML1.getId() + " : " + String.valueOf(propertyValue1.getValue()));
        logger.debug("    " + paramComponentAML2.getId() + " : " + String.valueOf(propertyValue2.getValue()));
        if (setRepMPNCfgBaseFieldType instanceof SetRepMPNCfgAggregationSelectionCriteriaType) {
          SetRepMPNCfgAggregationSelectionCriteriaType setRepMPNCfgAggregationSelectionCriteriaType = (SetRepMPNCfgAggregationSelectionCriteriaType)setRepMPNCfgBaseFieldType;
          if (setRepMPNCfgAggregationSelectionCriteriaType.getAggregationType().equals(SetRepMPNCfgSelectionCriteriaAggregationType.MAX)) {
            i = SetRepresentativeMPNEngine.compareLikeObjects(propertyValue2.getValue(), propertyValue1.getValue());
          } else {
            i = SetRepresentativeMPNEngine.compareLikeObjects(propertyValue1.getValue(), propertyValue2.getValue());
          } 
        } else if (setRepMPNCfgBaseFieldType instanceof SetRepMPNCfgPrioritySelectionCriteriaType) {
          SetRepMPNCfgPrioritySelectionCriteriaType setRepMPNCfgPrioritySelectionCriteriaType = (SetRepMPNCfgPrioritySelectionCriteriaType)setRepMPNCfgBaseFieldType;
          Integer integer1 = SetRepresentativeMPNEngine.priorityLookup(propertyValue1.getValue(), setRepMPNCfgPrioritySelectionCriteriaType.getPriorityValues());
          Integer integer2 = SetRepresentativeMPNEngine.priorityLookup(propertyValue2.getValue(), setRepMPNCfgPrioritySelectionCriteriaType.getPriorityValues());
          i = integer1.compareTo(integer2);
        } else if (setRepMPNCfgBaseFieldType instanceof SetRepMPNCfgCustomSelectionCriteriaType) {
          SetRepMPNCfgCustomSelectionCriteriaType setRepMPNCfgCustomSelectionCriteriaType = (SetRepMPNCfgCustomSelectionCriteriaType)setRepMPNCfgBaseFieldType;
          i = SetRepMPNScriptEngine.getScriptEngine().callCustomSelectionCriteria(propertyValue1, propertyValue2, setRepMPNCfgCustomSelectionCriteriaType.getCompare());
        } 
        logger.debug("    Compare : " + i);
        if (i != 0)
          break; 
      } 
    } catch (SetRepMPNException setRepMPNException) {
      System.err.println(setRepMPNException.getMessage());
    } 
    return i;
  }
  
  private PropertyValue getFilteredPropValue(ComponentAML paramComponentAML, SetRepMPNCfgBaseFieldType paramSetRepMPNCfgBaseFieldType) throws SetRepMPNException {
    PropertyValue propertyValue = paramComponentAML.getAMLProperty(paramSetRepMPNCfgBaseFieldType);
    if (propertyValue.getValue() == null || (propertyValue.getValue() instanceof String && ((String)propertyValue.getValue()).trim().isEmpty()))
      return null; 
    if (paramSetRepMPNCfgBaseFieldType instanceof SetRepMPNCfgAggregationSelectionCriteriaType) {
      SetRepMPNCfgAggregationSelectionCriteriaType setRepMPNCfgAggregationSelectionCriteriaType = (SetRepMPNCfgAggregationSelectionCriteriaType)paramSetRepMPNCfgBaseFieldType;
      if (setRepMPNCfgAggregationSelectionCriteriaType.getFilter() != null && !setRepMPNCfgAggregationSelectionCriteriaType.getFilter().isEmpty() && !SetRepMPNScriptEngine.getScriptEngine().callFilter(propertyValue, setRepMPNCfgAggregationSelectionCriteriaType.getFilter()))
        return null; 
    } 
    return propertyValue;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\cor\\utils\setrepmpn\FavoredMPNComparator.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */