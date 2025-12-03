package com.mentor.dms.contentprovider.plugin;

import org.antlr.runtime.CharStream;
import org.antlr.runtime.EarlyExitException;
import org.antlr.runtime.IntStream;
import org.antlr.runtime.Lexer;
import org.antlr.runtime.MismatchedSetException;
import org.antlr.runtime.NoViableAltException;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.RecognizerSharedState;

public class DMSStringExprLexer extends Lexer {
  public static final int EOF = -1;
  
  public static final int T__13 = 13;
  
  public static final int T__14 = 14;
  
  public static final int T__15 = 15;
  
  public static final int T__16 = 16;
  
  public static final int T__17 = 17;
  
  public static final int ESC = 4;
  
  public static final int LOGICAL_AND = 5;
  
  public static final int LOGICAL_NOT = 6;
  
  public static final int LOGICAL_NOT_NULL = 7;
  
  public static final int LOGICAL_NULL = 8;
  
  public static final int LOGICAL_OR = 9;
  
  public static final int WILDCARD = 10;
  
  public static final int WILDSTRING = 11;
  
  public static final int WSTRING = 12;
  
  public Lexer[] getDelegates() {
    return new Lexer[0];
  }
  
  public DMSStringExprLexer() {}
  
  public DMSStringExprLexer(CharStream paramCharStream) {
    this(paramCharStream, new RecognizerSharedState());
  }
  
  public DMSStringExprLexer(CharStream paramCharStream, RecognizerSharedState paramRecognizerSharedState) {
    super(paramCharStream, paramRecognizerSharedState);
  }
  
  public String getGrammarFileName() {
    return "C:\\Consulting\\Customers\\Raytheon\\ANTLR\\DMSStringExpr.g";
  }
  
  public final void mT__13() throws RecognitionException {
    byte b = 13;
    boolean bool = false;
    match(38);
    this.state.type = b;
    this.state.channel = bool;
  }
  
  public final void mT__14() throws RecognitionException {
    byte b = 14;
    boolean bool = false;
    match("NULL");
    this.state.type = b;
    this.state.channel = bool;
  }
  
  public final void mT__15() throws RecognitionException {
    byte b = 15;
    boolean bool = false;
    match(124);
    this.state.type = b;
    this.state.channel = bool;
  }
  
  public final void mT__16() throws RecognitionException {
    byte b = 16;
    boolean bool = false;
    match(126);
    this.state.type = b;
    this.state.channel = bool;
  }
  
  public final void mT__17() throws RecognitionException {
    byte b = 17;
    boolean bool = false;
    match("~NULL");
    this.state.type = b;
    this.state.channel = bool;
  }
  
  public final void mWILDCARD() throws RecognitionException {
    byte b = 10;
    boolean bool = false;
    if (this.input.LA(1) == 42 || this.input.LA(1) == 63) {
      this.input.consume();
    } else {
      MismatchedSetException mismatchedSetException = new MismatchedSetException(null, (IntStream)this.input);
      recover((RecognitionException)mismatchedSetException);
      throw mismatchedSetException;
    } 
    this.state.type = b;
    this.state.channel = bool;
  }
  
  public final void mESC() throws RecognitionException {
    byte b = 4;
    boolean bool = false;
    match(92);
    this.state.type = b;
    this.state.channel = bool;
  }
  
  public final void mWSTRING() throws RecognitionException {
    byte b1 = 12;
    boolean bool = false;
    for (byte b2 = 0;; b2++) {
      MismatchedSetException mismatchedSetException;
      EarlyExitException earlyExitException;
      byte b = 8;
      int i = this.input.LA(1);
      if ((i >= 0 && i <= 37) || (i >= 39 && i <= 41) || (i >= 43 && i <= 62) || (i >= 64 && i <= 91) || (i >= 93 && i <= 123) || i == 125 || (i >= 127 && i <= 65535)) {
        b = 1;
      } else if (i == 92) {
        switch (this.input.LA(2)) {
          case 38:
            b = 2;
            break;
          case 124:
            b = 3;
            break;
          case 42:
            b = 4;
            break;
          case 63:
            b = 5;
            break;
          case 126:
            b = 6;
            break;
          case 92:
            b = 7;
            break;
        } 
      } 
      switch (b) {
        case 1:
          if ((this.input.LA(1) >= 0 && this.input.LA(1) <= 37) || (this.input.LA(1) >= 39 && this.input.LA(1) <= 41) || (this.input.LA(1) >= 43 && this.input.LA(1) <= 62) || (this.input.LA(1) >= 64 && this.input.LA(1) <= 91) || (this.input.LA(1) >= 93 && this.input.LA(1) <= 123) || this.input.LA(1) == 125 || (this.input.LA(1) >= 127 && this.input.LA(1) <= 65535)) {
            this.input.consume();
            break;
          } 
          mismatchedSetException = new MismatchedSetException(null, (IntStream)this.input);
          recover((RecognitionException)mismatchedSetException);
          throw mismatchedSetException;
        case 2:
          mESC();
          match(38);
          break;
        case 3:
          mESC();
          match(124);
          break;
        case 4:
          mESC();
          match(42);
          break;
        case 5:
          mESC();
          match(63);
          break;
        case 6:
          mESC();
          match(126);
          break;
        case 7:
          mESC();
          mESC();
          break;
        default:
          if (b2 >= 1) {
            this.state.type = b1;
            this.state.channel = bool;
            return;
          } 
          earlyExitException = new EarlyExitException(1, (IntStream)this.input);
          throw earlyExitException;
      } 
    } 
  }
  
  public void mTokens() throws RecognitionException {
    byte b = 8;
    int i = this.input.LA(1);
    if (i == 38) {
      b = 1;
    } else if (i == 78) {
      int j = this.input.LA(2);
      if (j == 85) {
        int k = this.input.LA(3);
        if (k == 76) {
          int m = this.input.LA(4);
          if (m == 76) {
            int n = this.input.LA(5);
            if ((n >= 0 && n <= 37) || (n >= 39 && n <= 41) || (n >= 43 && n <= 62) || (n >= 64 && n <= 123) || n == 125 || (n >= 127 && n <= 65535)) {
              b = 8;
            } else {
              b = 2;
            } 
          } else {
            b = 8;
          } 
        } else {
          b = 8;
        } 
      } else {
        b = 8;
      } 
    } else if (i == 124) {
      b = 3;
    } else if (i == 126) {
      int j = this.input.LA(2);
      if (j == 78) {
        b = 5;
      } else {
        b = 4;
      } 
    } else if (i == 42 || i == 63) {
      b = 6;
    } else if (i == 92) {
      int j = this.input.LA(2);
      if (j == 38 || j == 42 || j == 63 || j == 92 || j == 124 || j == 126) {
        b = 8;
      } else {
        b = 7;
      } 
    } else if ((i >= 0 && i <= 37) || (i >= 39 && i <= 41) || (i >= 43 && i <= 62) || (i >= 64 && i <= 77) || (i >= 79 && i <= 91) || (i >= 93 && i <= 123) || i == 125 || (i >= 127 && i <= 65535)) {
      b = 8;
    } else {
      NoViableAltException noViableAltException = new NoViableAltException("", 2, 0, (IntStream)this.input);
      throw noViableAltException;
    } 
    switch (b) {
      case 1:
        mT__13();
        break;
      case 2:
        mT__14();
        break;
      case 3:
        mT__15();
        break;
      case 4:
        mT__16();
        break;
      case 5:
        mT__17();
        break;
      case 6:
        mWILDCARD();
        break;
      case 7:
        mESC();
        break;
      case 8:
        mWSTRING();
        break;
    } 
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\plugin\DMSStringExprLexer.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */