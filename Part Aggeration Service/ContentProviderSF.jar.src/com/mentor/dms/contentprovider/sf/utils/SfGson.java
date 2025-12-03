package com.mentor.dms.contentprovider.sf.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.ToNumberStrategy;
import com.google.gson.stream.JsonReader;
import com.mentor.dms.contentprovider.core.plugin.SfBigDecimal;
import java.io.IOException;

public class SfGson {
  public static Gson createGson() {
    return (new GsonBuilder()).setObjectToNumberStrategy(createObjectToNumberStrategy()).create();
  }
  
  private static ToNumberStrategy createObjectToNumberStrategy() {
    return new ToNumberStrategy() {
        public Number readNumber(JsonReader param1JsonReader) throws IOException {
          String str = param1JsonReader.nextString();
          try {
            return Integer.valueOf(Integer.parseInt(str));
          } catch (NumberFormatException numberFormatException) {
            try {
              return Long.valueOf(Long.parseLong(str));
            } catch (NumberFormatException numberFormatException1) {
              return (Number)new SfBigDecimal(str);
            } 
          } 
        }
      };
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderSF.jar!\com\mentor\dms\contentprovider\s\\utils\SfGson.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */