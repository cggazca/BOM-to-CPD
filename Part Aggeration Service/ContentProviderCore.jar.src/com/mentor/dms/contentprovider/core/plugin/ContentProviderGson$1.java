package com.mentor.dms.contentprovider.core.plugin;

import com.google.gson.ToNumberStrategy;
import com.google.gson.stream.JsonReader;
import java.io.IOException;

class null implements ToNumberStrategy {
  public Number readNumber(JsonReader paramJsonReader) throws IOException {
    String str = paramJsonReader.nextString();
    try {
      return Integer.valueOf(Integer.parseInt(str));
    } catch (NumberFormatException numberFormatException) {
      try {
        return Long.valueOf(Long.parseLong(str));
      } catch (NumberFormatException numberFormatException1) {
        return new SfBigDecimal(str);
      } 
    } 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\plugin\ContentProviderGson$1.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */