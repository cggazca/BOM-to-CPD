package com.mentor.dms.contentprovider.core.plugin;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

public class SfBigDecimal extends BigDecimal {
  public SfBigDecimal(BigInteger paramBigInteger, int paramInt, MathContext paramMathContext) {
    super(paramBigInteger, paramInt, paramMathContext);
  }
  
  public SfBigDecimal(BigInteger paramBigInteger, int paramInt) {
    super(paramBigInteger, paramInt);
  }
  
  public SfBigDecimal(BigInteger paramBigInteger, MathContext paramMathContext) {
    super(paramBigInteger, paramMathContext);
  }
  
  public SfBigDecimal(BigInteger paramBigInteger) {
    super(paramBigInteger);
  }
  
  public SfBigDecimal(char[] paramArrayOfchar, int paramInt1, int paramInt2, MathContext paramMathContext) {
    super(paramArrayOfchar, paramInt1, paramInt2, paramMathContext);
  }
  
  public SfBigDecimal(char[] paramArrayOfchar, int paramInt1, int paramInt2) {
    super(paramArrayOfchar, paramInt1, paramInt2);
  }
  
  public SfBigDecimal(char[] paramArrayOfchar, MathContext paramMathContext) {
    super(paramArrayOfchar, paramMathContext);
  }
  
  public SfBigDecimal(char[] paramArrayOfchar) {
    super(paramArrayOfchar);
  }
  
  public SfBigDecimal(double paramDouble, MathContext paramMathContext) {
    super(paramDouble, paramMathContext);
  }
  
  public SfBigDecimal(double paramDouble) {
    super(paramDouble);
  }
  
  public SfBigDecimal(int paramInt, MathContext paramMathContext) {
    super(paramInt, paramMathContext);
  }
  
  public SfBigDecimal(int paramInt) {
    super(paramInt);
  }
  
  public SfBigDecimal(long paramLong, MathContext paramMathContext) {
    super(paramLong, paramMathContext);
  }
  
  public SfBigDecimal(long paramLong) {
    super(paramLong);
  }
  
  public SfBigDecimal(String paramString, MathContext paramMathContext) {
    super(paramString, paramMathContext);
  }
  
  public SfBigDecimal(String paramString) {
    super(paramString);
  }
  
  public String toString() {
    return toPlainString();
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\plugin\SfBigDecimal.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */