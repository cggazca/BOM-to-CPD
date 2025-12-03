package com.mentor.dms.contentprovider.core.plugin;

import com.mentor.dms.contentprovider.core.ListPropertyRow;
import java.util.Comparator;

class PriceBreakdownComparator implements Comparator<ListPropertyRow> {
  public int compare(ListPropertyRow paramListPropertyRow1, ListPropertyRow paramListPropertyRow2) {
    try {
      Object object1 = paramListPropertyRow1.getValueObj(PricePropertyEnum.Quantity.getPropid());
      Object object2 = paramListPropertyRow2.getValueObj(PricePropertyEnum.Quantity.getPropid());
      return (object1 instanceof Integer && object2 instanceof Integer) ? ((Integer)object1).compareTo((Integer)object2) : ((object1 instanceof Long && object2 instanceof Long) ? ((Long)object1).compareTo((Long)object2) : ((object1 instanceof SfBigDecimal && object2 instanceof SfBigDecimal) ? ((SfBigDecimal)object1).compareTo((SfBigDecimal)object2) : ((object1 instanceof Double && object2 instanceof Double) ? ((Double)object1).compareTo((Double)object2) : 0)));
    } catch (Exception exception) {
      return 0;
    } 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\plugin\ViewSupplyChainWindow$PriceBreakdownComparator.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */